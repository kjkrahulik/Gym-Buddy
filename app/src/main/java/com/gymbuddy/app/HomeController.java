package com.gymbuddy.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Value;

//Denotes that this class is a Spring MVC controller that handles HTTP requests and returns views.
@Controller
public class HomeController {

    @Value("${spring.application.name}")
    private String appName;

    //Denotes that this method will handle HTTP requests to the root URL ("/") and return the name of the view to be rendered, which in this case is "index".
    @GetMapping("/")
    public String index() {
        System.out.println("HomeController: Application Name: " + appName); // Log the application name to the console
        // Forward to the static index.html file
        return "forward:/pages/index.html";
    }
}


