package com.gymbuddy.app.Service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymbuddy.app.AccountDomain.Profile;
import com.gymbuddy.app.Repositories.ProfileRepository;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepo;

    public Profile getProfile(String username) {
        return profileRepo.findByAccount_Username(username)
            .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public Profile updateBio(String username, String bio) {
        Profile profile = getProfile(username);
        profile.setBio(bio);
        return profileRepo.save(profile);
    }

    public void uploadProfilePicture(String username, InputStream inputStream) {
        Profile profile = getProfile(username);
        try {
            profile.setProfilePicture(inputStream.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Error reading image: " + e.getMessage(), e);
        }
        profileRepo.save(profile);
    }

    public void deleteProfilePicture(String username) {
        Profile profile = getProfile(username);
        profile.deleteProfilePicture();
        profileRepo.save(profile);
    }

    public String getProfilePictureBase64(String username) {
        return profileRepo.findByAccount_Username(username)
            .map(Profile::getProfilePictureBase64)
            .orElse("");
    }
}
