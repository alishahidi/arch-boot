package com.alishahidi.api.core.security.user;

import com.alishahidi.api.core.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(
            @RequestBody UserRegisterDto request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(userService.register(request))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(
            @RequestBody UserLoginDto request
    ) {
        try {
            return ResponseEntity.ok(
                    ApiResponse.success(userService.login(request))
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    ApiResponse.error("Credential error", HttpStatus.NOT_FOUND)
            );
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserDto>> get() {
        return ResponseEntity.ok(
                ApiResponse.success(userService.get())
        );
    }
}