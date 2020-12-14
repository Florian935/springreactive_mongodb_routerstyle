package com.reactive.mongodb.router.function.reactive.router.student;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class StudentRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(StudentHandler studentHandler) {
        return nest(path("/students"),
                route(GET("").and(accept(APPLICATION_JSON)), studentHandler::getAll)
                .andRoute(GET("/stream").and(accept(TEXT_EVENT_STREAM)), studentHandler::getAllStream)
                .andRoute(GET("/{id}"), studentHandler::getOne)
                .andRoute(DELETE("/{id}"), studentHandler::delete)
                .andRoute(POST("").and(accept(APPLICATION_JSON)), studentHandler::create)
                .andRoute(PUT("/{id}").and(accept(APPLICATION_JSON)), studentHandler::update)
        );
    }

}
