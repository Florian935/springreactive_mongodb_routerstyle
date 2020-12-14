package com.reactive.mongodb.router.function.reactive.router.student;

import com.reactive.mongodb.router.function.reactive.model.Student;
import com.reactive.mongodb.router.function.reactive.service.StudentServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.ServerResponse.*;


@Component
public class StudentHandler {

    private final StudentServiceImpl studentService;

    public StudentHandler(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }

    public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
        return ok()
                .contentType(APPLICATION_JSON)
                .body(studentService.findAll(), Student.class);
    }

    public Mono<ServerResponse> getAllStream(ServerRequest serverRequest) {
        return ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(studentService.findAllStream(), Student.class);
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return noContent()
                .build(studentService.deleteById(serverRequest.pathVariable("id")))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        return created(serverRequest.uri())
                .contentType(APPLICATION_JSON)
                .body(
                        serverRequest
                            .bodyToMono(Student.class)
                            .flatMap(student -> {
                                student.setId(UUID.randomUUID().toString());
                                return studentService.save(student);
                            }),
                        Student.class)
                .switchIfEmpty(badRequest().build());
    }

    public Mono<ServerResponse> getOne(ServerRequest serverRequest) {
        return studentService
                .findById(serverRequest.pathVariable("id"))
                .flatMap(s -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(studentService.findById(serverRequest.pathVariable("id")), Student.class))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return created(serverRequest.uri())
                .contentType(APPLICATION_JSON)
                .body(
                        studentService
                                .findById(serverRequest.pathVariable("id"))
                                .flatMap(s -> {
                                    // block() permet d'être bloquant
                                    Student student = serverRequest.bodyToMono(Student.class).block();
                                    s.setName(student != null ? student.getName() : null);
                                    return studentService.save(s);
                                }),
                        Student.class)
                .switchIfEmpty(notFound().build());
    }
}
