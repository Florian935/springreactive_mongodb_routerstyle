package com.reactive.mongodb.router.function.reactive.service;

import com.reactive.mongodb.router.function.reactive.model.Student;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {

    Flux<Student> findAll();

    Mono<Student> save(Student student);

    Mono<Student> findById(String id);

    Mono<Void> deleteById(String id);
}
