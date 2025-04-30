package com.github.lambdv.ParametricTransformer.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
public class HttpServer {
    public static void main(String[] args) {
        SpringApplication.run(HttpServer.class, args);
    }

    @Bean
    public Function<String, String> myFunction() {
        return input -> "Processed: " + input;
    }
}

@RestController
@RequestMapping("/api")
 class FunctionController {

    private final Function<String, String> myFunction;

    public FunctionController(Function<String, String> myFunction) {
        this.myFunction = myFunction;
    }

    @PostMapping("/execute")
    public String executeFunction(@RequestBody String input) {
        return myFunction.apply(input);
    }
}