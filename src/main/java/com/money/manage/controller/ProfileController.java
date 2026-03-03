package com.money.manage.controller;

import com.money.manage.dto.AuthDTO;
import com.money.manage.dto.ProfileDTO;
import com.money.manage.entity.ProfileEntity;
import com.money.manage.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO){

        ProfileDTO registeredProfile = profileService.registerProfile(profileDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token) {

        boolean isActivated = profileService.activateProfile(token);

        if (isActivated) {
            return ResponseEntity.ok("Profile activated successfully");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or already used");
        }

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDTO){

        try {

            if (!profileService.isProfileActive(authDTO.getEmail())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "message", "Account is not active. Please activate your account first"
                ));
            }

            Map<String,Object> response = profileService.authenticateAndGenerateToken(authDTO);

            return ResponseEntity.ok(response);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "message","Internal Server Error: " + e.getMessage()
                    )
            );
        }

    }

    @GetMapping("/info")
    public ResponseEntity<ProfileDTO> getPublicProfile() {
        ProfileDTO publicProfile = profileService.getPublicProfile(null);
        return ResponseEntity.ok(publicProfile);
    }

    @GetMapping("/test")
    public String test() {
        return "Test Successful";
    }




}
