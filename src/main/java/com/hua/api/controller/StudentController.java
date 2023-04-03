package com.hua.api.controller;

import com.hua.api.dto.*;
import com.hua.api.enums.EventTypeEnum;
import com.hua.api.service.EventService;
import com.hua.api.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
public class StudentController extends BaseController {

    private final StudentService studentService;
    private final EventService eventService;

    @Autowired
    public StudentController(StudentService studentService, EventService eventService) {
        this.studentService = studentService;
        this.eventService = eventService;
    }

    @PostMapping("/student/create")
    public ResponseEntity<Long> createStudent(@RequestBody StudentDTO studentDTO) {

        Long studentId = studentService.createStudent(studentDTO);

        return ResponseEntity.ok(studentId);
    }

    @GetMapping("/student/find/{id}")
    public ResponseEntity<StudentDTO> findStudent(@PathVariable Long id) {

        StudentDTO studentId = studentService.findStudent(id);

        return ResponseEntity.ok(studentId);
    }

    @PutMapping("/student/update/{id}")
    public ResponseEntity<Long> updateStudent(@PathVariable Long id,
                                              @RequestBody StudentDTO dto) throws IOException {

        studentService.updateStudent(id, dto);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/student/update-password/{id}")
    public ResponseEntity<Long> updatePassword(@PathVariable Long id,
                                               @RequestBody PasswordDTO dto) {

        studentService.updatePassword(id, dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/student/all")
    public ResponseEntity<List<StudentDTO>> findAllStudents(Pageable pageable) {

        List<StudentDTO> response = studentService.findAllStudents(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/minio-file/{username}")
    public ResponseEntity<FileDTO> fetchMinioFile(@PathVariable String username) {

        FileDTO response = studentService.fetchMinioFile(username);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/minio-files/{username}")
    public ResponseEntity<List<FileDTO>> fetchMinioFiles(@PathVariable String username,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws XmlPullParserException {

        List<FileDTO> response = studentService.fetchMinioFiles(username, from, to);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/student/event-password/{id}")
    public ResponseEntity<FileDTO> updateEventOnPasswordChange(@PathVariable Long id) {

        studentService.updateEventPassword(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/student/notify-admins")
    public ResponseEntity<List<NotificationDTO>> notifyAdmins() {

        List<NotificationDTO> response = studentService.notifyAdmins();

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/students/events")
    public ResponseEntity<List<EventDTO>> getEvents(@RequestParam(required = false) String event,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<EventDTO> events;
        if (StringUtils.hasText(event)) {
            EventTypeEnum eventType = EventTypeEnum.valueOf(event.toUpperCase());
            events = eventService.findAll(from, to, eventType);
        } else {
            events = eventService.findAll(from, to);
        }

        return ResponseEntity.ok(events);
    }
}
