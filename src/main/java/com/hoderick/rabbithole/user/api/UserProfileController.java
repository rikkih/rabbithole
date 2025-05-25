package com.hoderick.rabbithole.user.api;

import com.hoderick.rabbithole.user.dto.UserProfileDto;
import com.hoderick.rabbithole.user.dto.UserProfileRequest;
import com.hoderick.rabbithole.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PutMapping
    public ResponseEntity<UserProfileDto> getOrCreateUserProfile(@RequestBody UserProfileRequest request) {
        return ResponseEntity.ok(userProfileService.getOrCreateProfile(request.name(), request.email()));
    }

    @PutMapping("/avatar")
    public ResponseEntity<Void> uploadAvatar(@RequestParam("file") MultipartFile file) {
        userProfileService.updateAvatar(file);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/avatar-url")
    public ResponseEntity<URL> getAvatarUrl() {
        URL url = userProfileService.getAvatarUrl();
        return ResponseEntity.ok(url);
    }
}
