package com.gymbuddy.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//Denotes that this class is a Spring MVC controller that handles HTTP requests and returns views.
@Controller
public class HomeController {

    //Denotes that this method will handle HTTP requests to the root URL ("/") and return the name of the view to be rendered, which in this case is "index".
    @RequestMapping("/")
    public String index() {
        String viewName = getViewName();
        return viewName + ".html"; 
    }

    private String getViewName() {
        return "index";
    }
}


