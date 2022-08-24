package com.hua.api.service;

import com.hua.api.dto.*;
import com.hua.api.entity.HuaUser;
import com.hua.api.exception.HuaNotFound;
import com.hua.api.repository.RoleRepository;
import com.hua.api.repository.UserRepository;
import com.hua.api.utilities.HuaUtil;
import com.hua.api.utilities.MinioUtil;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.hua.api.utilities.MinioUtil.HUA_BUCKET;

@Service
public class StudentServiceImpl implements StudentService {

    private static final String TEMP = "temp";
    private static final String READER_ROLE = "READER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MinioUtil minioUtil;

    @Autowired
    public StudentServiceImpl(UserRepository userRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder,
                              MinioUtil minioUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.minioUtil = minioUtil;
    }

    @SneakyThrows
    @Override
    public Long createStudent(StudentDTO studentDTO) {

        handleMobileAndAfmUniqueness(studentDTO.getMobileNumber(), studentDTO.getVatNumber());

        HuaUser user = new HuaUser();

        setBasicInfo(studentDTO, user);

        setStudentDetails(studentDTO, user);

        setContactInfo(studentDTO, user);

        FileDTO fileDTO = studentDTO.getFile();

        saveFileOnDbAndMinio(user, fileDTO);

        roleRepository.findByName(READER_ROLE)
                .ifPresent(user::addRole);

        HuaUser savedUser = userRepository.save(user);

        userRepository.findById(savedUser.getId())
                .ifPresent(huaUser -> {
                    HuaUser updatedUser = generateUsernameAndEmail(huaUser);
                    userRepository.save(updatedUser);
                });

        return savedUser.getId();
    }

    @Override
    public StudentDTO findStudent(Long id) {
        HuaUser huaUser = userRepository.findById(id)
                .orElseThrow(() -> new HuaNotFound("Δεν βρέθηκε ο φοιτητής με id: " + id));

        return toStudentDTO(huaUser);
    }

    @Override
    public void updateStudent(Long id, StudentDTO studentDTO) {
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

        userRepository.save(huaUser);
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

            String actualFile = fileDTO.getActualFile();
            byte[] decodedFile = Base64.getDecoder().decode(actualFile);
            InputStream targetStream = new ByteArrayInputStream(decodedFile);
            long length = targetStream.available();

            minioUtil.uploadFile(HUA_BUCKET, fileName, targetStream, length, fileDTO.getMimeType());
        }
    }
}
