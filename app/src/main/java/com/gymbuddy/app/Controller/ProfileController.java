package com.gymbuddy.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.gymbuddy.app.AccountDomain.Profile;
import com.gymbuddy.app.Service.ProfileService;

@Controller
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/profile/{username}")
    @ResponseBody
    public ResponseEntity<Profile> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getProfile(username));
    }

    @PutMapping("/profile/{username}/bio")
    @ResponseBody
    public ResponseEntity<Profile> updateBio(
            @PathVariable String username,
            @RequestParam String bio
    ) {
        return ResponseEntity.ok(profileService.updateBio(username, bio));
    }

    @PostMapping("/profile/{username}/picture")
    @ResponseBody
    public ResponseEntity<String> uploadPicture(
            @PathVariable String username,
            @RequestParam("file") MultipartFile imageFile
    ) {
        try {
            profileService.uploadProfilePicture(username, imageFile.getInputStream());
            return ResponseEntity.ok("Profile picture uploaded.");
        } catch (Exception e) {
            throw new RuntimeException("Error uploading profile picture: " + e.getMessage(), e);
        }
    }

    @GetMapping("/profile/{username}/picture")
    @ResponseBody
    public ResponseEntity<String> getPicture(@PathVariable String username) {
        String base64 = profileService.getProfilePictureBase64(username);
        if (base64.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(base64);
    }

    @DeleteMapping("/profile/{username}/picture")
    @ResponseBody
    public ResponseEntity<String> deletePicture(@PathVariable String username) {
        profileService.deleteProfilePicture(username);
        return ResponseEntity.ok("Profile picture deleted.");
    }
}