package com.reactive.mongodb.router.function.reactive;

import com.reactive.mongodb.router.function.reactive.model.Student;
import com.reactive.mongodb.router.function.reactive.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Component
@Slf4j
public class DataInitializer {

    private final StudentRepository studentRepository;

    public DataInitializer(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @EventListener(value = ContextRefreshedEvent.class)
    public void init() {
        log.info("Start data initialization ...");

        this.studentRepository
                .deleteAll()
                .thenMany(
                        Flux
                                .just(
                                        new Student(UUID.randomUUID().toString(), "toto1"),
                                        new Student(UUID.randomUUID().toString(), "toto2"),
                                        new Student(UUID.randomUUID().toString(), "toto3"),
                                        new Student(UUID.randomUUID().toString(), "toto4"),
                                        new Student(UUID.randomUUID().toString(), "toto5")
                                )
                                .map(
                                        student -> {
                                            student.setName(String.format("%s !", student.getName()));
                                            return student;
                                        }
                                )
                                .flatMap(studentRepository::save)
                )
                .log()
                .subscribe(
                        data -> log.info("data: {}", data),
                        error -> log.error("error: ", error),
                        () -> log.info("done initialization ...")
                );
    }
}
