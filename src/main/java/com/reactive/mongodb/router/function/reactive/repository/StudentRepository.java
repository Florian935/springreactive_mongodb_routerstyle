package com.reactive.mongodb.router.function.reactive.repository;

import com.reactive.mongodb.router.function.reactive.model.Student;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends ReactiveMongoRepository<Student, String> {
}
