package com.gymbuddy.app.AccountDomain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Lob;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID profileId;

    private String bio;

    @Lob
    @Column(name = "profile_picture", columnDefinition = "BLOB")
    private byte[] profilePicture;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Profile() {}

    public Profile(Account account) {
        this.account = account;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    // ==================== Profile Picture Methods (BLOB Storage) ====================

    /**
     * Gets the profile picture as byte array (for database storage)
     */
    public byte[] getProfilePicture() {
        return profilePicture;
    }

    /**
     * Sets the profile picture from byte array (image file content)
     */
    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * Gets the profile picture as Base64 encoded string for JSON responses
     */
    public String getProfilePictureBase64() {
        if (profilePicture == null || profilePicture.length == 0) {
            return "";
        }
        try {
            // Convert byte array to Base64 string
            java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
            return encoder.encodeToString(profilePicture);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Gets the profile picture as InputStream for direct image display
     */
    public java.io.InputStream getProfilePictureInputStream() {
        if (profilePicture == null || profilePicture.length == 0) {
            return null;
        }
        try {
            // Create a ByteArrayInputStream from the byte array
            return new java.io.ByteArrayInputStream(profilePicture);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets the profile picture from Base64 encoded string
     */
    public void setProfilePictureBase64(String base64String) {
        if (base64String != null && !base64String.isBlank()) {
            try {
                // Convert Base64 string back to byte array
                java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
                this.profilePicture = decoder.decode(base64String);
            } catch (Exception e) {
                throw new RuntimeException("Invalid Base64 string: " + e.getMessage());
            }
        } else {
            this.profilePicture = null;
        }
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    // ==================== Profile Picture Helper Methods (BLOB Storage) ====================

    /**
     * Uploads a new profile picture from byte array (replaces old one)
     */
    public void uploadProfilePicture(byte[] imageBytes) {
        if (imageBytes != null && imageBytes.length > 0) {
            this.profilePicture = imageBytes;
        } else {
            // Optionally clear the image
            this.profilePicture = null;
        }
    }

    /**
     * Deletes/removes the current profile picture
     */
    public void deleteProfilePicture() {
        this.profilePicture = null;
    }
}

