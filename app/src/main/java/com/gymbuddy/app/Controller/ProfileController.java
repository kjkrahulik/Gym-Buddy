package com.gymbuddy.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String profile() {
        // Forward to the static profile.html file
        return "forward:/profile.html";
    }
}