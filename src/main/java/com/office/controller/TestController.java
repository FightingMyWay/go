package com.office.controller;

import com.office.common.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestController {
    
    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello from Spring Boot!");
    }
    
    @PostMapping("/echo")
    public Result<String> echo(@RequestBody String message) {
        return Result.success("Echo: " + message);
    }
}
