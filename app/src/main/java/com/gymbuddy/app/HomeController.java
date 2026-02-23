package com.gymbuddy.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Value;

//Denotes that this class is a Spring MVC controller that handles HTTP requests and returns views.
@Controller
public class HomeController {

    @Value("${spring.application.name}")
    private String appName;

    //Denotes that this method will handle HTTP requests to the root URL ("/") and return the name of the view to be rendered, which in this case is "index".
    @RequestMapping("/")
    public String index() {
        System.out.println("Application Name: " + appName); // Log the application name to the console
        String viewName = getViewName();
        return viewName + ".html"; 
    }

    private String getViewName() {
        return "index";
    }
}


