package com.johnny.external.greeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Setter;

@RestController
public class GreetingController {

    static final String WORLD = "World";
    
    @Setter
    @Autowired
    private GreetingService service;

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue=WORLD) String name) {
        return service.getGreeting(name);
    }

}
