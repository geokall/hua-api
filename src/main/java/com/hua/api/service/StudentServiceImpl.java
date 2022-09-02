package com.hua.api.service;

import com.hua.api.dto.*;
import com.hua.api.entity.HuaEvent;
import com.hua.api.entity.HuaRole;
import com.hua.api.entity.HuaUser;
import com.hua.api.enums.EventTypeEnum;
import com.hua.api.exception.HuaNotFound;
import com.hua.api.repository.HuaEventRepository;
import com.hua.api.repository.RoleRepository;
import com.hua.api.repository.UserRepository;
import com.hua.api.utilities.HuaUtil;
import com.hua.api.utilities.MinioUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    private static final String TEMP = "temp";
    private static final String READER_ROLE = "READER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MinioUtil minioUtil;
    private final HuaEventRepository huaEventRepository;

    @Autowired
    public StudentServiceImpl(UserRepository userRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder,
                              MinioUtil minioUtil,
                              HuaEventRepository huaEventRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.minioUtil = minioUtil;
        this.huaEventRepository = huaEventRepository;
    }

    @SneakyThrows
    @Override
    public Long createStudent(StudentDTO studentDTO) {

        handleMobileAndAfmUniqueness(studentDTO.getMobileNumber(), studentDTO.getVatNumber());

        HuaUser user = new HuaUser();

        setBasicInfo(studentDTO, user);

        setStudentDetails(studentDTO, user);

        setContactInfo(studentDTO, user);

        roleRepository.findByName(READER_ROLE)
                .ifPresent(user::addRole);

        HuaUser savedUser = userRepository.save(user);

        userRepository.findById(savedUser.getId())
                .ifPresent(huaUser -> {
                    HuaUser updatedUser = generateUsernameAndEmail(huaUser);
                    userRepository.save(updatedUser);
                });

        HuaEvent huaEvent = new HuaEvent();
        huaEvent.setHuaUser(savedUser);
        huaEvent.setEventType(EventTypeEnum.REGISTRATION);
        huaEvent.setAdminInformed(false);
        huaEvent.setCreatedDate(savedUser.getDateCreated());

        huaEventRepository.save(huaEvent);

        FileDTO fileDTO = studentDTO.getFile();

        saveFileOnDbAndMinio(savedUser, fileDTO);

        return savedUser.getId();
    }

    @Override
    public StudentDTO findStudent(Long id) {
        HuaUser huaUser = userRepository.findById(id)
                .orElseThrow(() -> new HuaNotFound("Δεν βρέθηκε ο φοιτητής με id: " + id));

        return toStudentDTO(huaUser);
    }

    @Override
    public void updateStudent(Long id, StudentDTO studentDTO) throws IOException {
        HuaUser huaUser = userRepository.findById(id)
                .orElseThrow(() -> new HuaNotFound("Δεν βρέθηκε ο φοιτητής με id: " + id));

        huaUser.setVerified(studentDTO.getIsVerified());
        huaUser.setDateChanged(LocalDateTime.now());
        huaUser.setSurname(studentDTO.getSurname());
        huaUser.setName(studentDTO.getName());
        huaUser.setFatherName(studentDTO.getFatherName());
        huaUser.setMotherName(studentDTO.getMotherName());
        huaUser.setAddress(studentDTO.getAddress());
        huaUser.setCity(studentDTO.getCity());
        huaUser.setPostalCode(studentDTO.getPostalCode());
        huaUser.setMobileNumber(studentDTO.getMobileNumber());
        huaUser.setVatNumber(studentDTO.getVatNumber());

        FileDTO fileDTO = studentDTO.getFile();

        saveFileOnDbAndMinio(huaUser, fileDTO);
    }

    @Override
    public void updatePassword(Long id, PasswordDTO passwordDTO) {
        HuaUser huaUser = userRepository.findById(id)
                .orElseThrow(() -> new HuaNotFound("Δεν βρέθηκε ο φοιτητής με id: " + id));

        huaUser.setPassword(passwordEncoder.encode(passwordDTO.getPassword()));
        userRepository.save(huaUser);
    }

    @Override
    public List<StudentDTO> findAllStudents(Pageable pageable) {
        return userRepository.findAll(pageable).stream()
                .map(this::toStudentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FileDTO fetchMinioFile(String username) {
        HuaUser user = userRepository.findByUsername(username);

        if (user == null) {
            throw new HuaNotFound("Δεν βρέθηκε ο φοιτητής με username: " + username);
        }

        FileDTO fileDTO = new FileDTO();

        String fileEncoded = null;

        //fetching latest name saved on db
        if (user.getFileName() != null) {
            byte[] file = minioUtil.getFile(user.getUsername(), user.getFileName());

            if (!ObjectUtils.isEmpty(file)) {
                fileEncoded = Base64.getEncoder().encodeToString(file);
            }

            fileDTO.setFileName(user.getFileName());
            fileDTO.setActualFile(fileEncoded);
        }

        return fileDTO;
    }

    @Override
    public List<FileDTO> fetchMinioFiles(String username, LocalDate from, LocalDate to) {
        return minioUtil.getFilesByUsername(username, from, to);
    }

    @Override
    public void updateEventPassword(Long id) {
        HuaUser huaUser = userRepository.findById(id)
                .orElseThrow(() -> new HuaNotFound("Δεν βρέθηκε ο φοιτητής με id: " + id));

        HuaEvent huaEvent = new HuaEvent();
        huaEvent.setHuaUser(huaUser);
        huaEvent.setEventType(EventTypeEnum.PASSWORD);
        huaEvent.setAdminInformed(false);
        huaEvent.setCreatedDate(LocalDateTime.now());

        huaEventRepository.save(huaEvent);
    }

    @Override
    public List<NotificationDTO> notifyAdmins() {
        List<NotificationDTO> dto = new ArrayList<>();

        List<HuaUser> admins = userRepository.findByRoles_Id(1L);

        List<String> listOfAdminEmails = admins.stream()
                .map(HuaUser::getEmail)
                .collect(Collectors.toList());

        LOGGER.info("Fetched admin emails: " + listOfAdminEmails.size());

        List<HuaEvent> usersToBeInformed = huaEventRepository.findAllByAdminInformedAndEventType(false, EventTypeEnum.REGISTRATION);

        List<String> usernamesToBeInformed = usersToBeInformed.stream()
                .map(x -> x.getHuaUser().getUsername())
                .collect(Collectors.toList());

        LOGGER.info("Fetched usernames to be informed: " + usernamesToBeInformed.size());

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setAdmins(listOfAdminEmails);
        notificationDTO.setUsernames(usernamesToBeInformed);

        dto.add(notificationDTO);

        usersToBeInformed.forEach(huaEvent -> {
            huaEvent.setAdminInformed(true);
            huaEventRepository.save(huaEvent);
        });

        return dto;
    }


    private void handleMobileAndAfmUniqueness(String mobileNumber, String vatNumber) {
        if (!ObjectUtils.isEmpty(mobileNumber)) {
            userRepository.findByMobileNumber(mobileNumber)
                    .ifPresent(student -> {
                        throw new HuaNotFound("Υπάρχει φοιτητής με το ίδιο τηλέφωνο επικοινωνίας");
                    });
        }

        if (!ObjectUtils.isEmpty(vatNumber)) {
            userRepository.findByVatNumber(vatNumber)
                    .ifPresent(student -> {
                        throw new HuaNotFound("Υπάρχει φοιτητής με τον ίδιο Α.Φ.Μ");
                    });
        }
    }

    private HuaUser generateUsernameAndEmail(HuaUser user) {
        String generatedUsername = HuaUtil.generateUsername(user.getId());
        user.setUsername(generatedUsername);

        String generatedEmail = HuaUtil.generateEmail(generatedUsername);
        user.setEmail(generatedEmail);

        return user;
    }

    private void setBasicInfo(StudentDTO studentDTO, HuaUser user) {
        user.setDateCreated(LocalDateTime.now());
        user.setVerified(false);
        user.setPassword(passwordEncoder.encode(TEMP));
        user.setEmail(TEMP);
        user.setUsername(TEMP);

        user.setSurname(studentDTO.getSurname());
        user.setName(studentDTO.getName());
        user.setFatherName(studentDTO.getFatherName());
        user.setMotherName(studentDTO.getMotherName());

        String birthDate = studentDTO.getBirthDate();

        if (!ObjectUtils.isEmpty(birthDate)) {
            Date birthDateFormatted = HuaUtil.formatStringToDate(birthDate);
            user.setBirthDate(birthDateFormatted);
        }

        user.setGender(studentDTO.getGender());
    }

    private void setContactInfo(StudentDTO studentDTO, HuaUser user) {
        user.setAddress(studentDTO.getAddress());
        user.setCity(studentDTO.getCity());
        user.setPostalCode(studentDTO.getPostalCode());
        user.setMobileNumber(studentDTO.getMobileNumber());
        user.setVatNumber(studentDTO.getVatNumber());
    }

    private void setStudentDetails(StudentDTO studentDTO, HuaUser user) {
        user.setDepartment(studentDTO.getDepartment());

        var directionDTO = studentDTO.getDirection();

        if (directionDTO != null) {
            user.setDirection(directionDTO.getName());
        }
    }

    private StudentDTO toStudentDTO(HuaUser user) {
        StudentDTO dto = new StudentDTO();
        dto.setId(user.getId());
        dto.setDateChanged(user.getDateChanged());
        dto.setDateCreated(user.getDateCreated());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setDepartment(user.getDepartment());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setIsVerified(user.getVerified() != null ? user.getVerified() : false);

        StudentDirectionDTO direction = new StudentDirectionDTO();
        direction.setName(user.getDirection());
        dto.setDirection(direction);

        if (user.getBirthDate() != null) {
            String birthDateFormatted = HuaUtil.formatDateToString(user.getBirthDate());
            dto.setBirthDate(birthDateFormatted);
        }

        dto.setGender(user.getGender());
        dto.setFatherName(user.getFatherName());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setMotherName(user.getMotherName());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setPostalCode(user.getPostalCode());
        dto.setVatNumber(user.getVatNumber());

        return dto;
    }

    private void saveFileOnDbAndMinio(HuaUser user, FileDTO fileDTO) throws IOException {
        String fileName = fileDTO.getFileName();

        if (!ObjectUtils.isEmpty(fileName)) {
            user.setFileName(fileName);
            user.setDateFileCreated(LocalDateTime.now());

            userRepository.save(user);

            String actualFile = fileDTO.getActualFile();
            byte[] decodedFile = Base64.getDecoder().decode(actualFile);
            InputStream targetStream = new ByteArrayInputStream(decodedFile);
            long length = targetStream.available();

            minioUtil.uploadFile(user.getUsername(), fileName, targetStream, length, fileDTO.getMimeType());
        }
    }
}
