package ir.samin.appbuilder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import ir.samin.appbuilder.dto.ConfirmationCode;
import ir.samin.appbuilder.dto.UserAuthRequestDTO;
import ir.samin.appbuilder.dto.UserResponseDTO;
import ir.samin.appbuilder.security.JwtUtil;
import ir.samin.appbuilder.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/user")
public class CustomerController {

    private UserService userService;
    private static JwtUtil jwtUtil;

    public CustomerController(UserService userService,
                              JwtUtil jwtUtil){
        this.userService = userService;
        CustomerController.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAuthRequestDTO userAuthRequestDTO) {
        try{
            userService.loadUserByUsername(userAuthRequestDTO.getPhoneNumber());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        } catch (UsernameNotFoundException e){
            UserResponseDTO response;
            try {
                response = userService.createUser(userAuthRequestDTO);
                return ResponseEntity.ok(response.getPhoneNumber());
            } catch (RuntimeException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @PostMapping("/confirm-code")
    public ResponseEntity<?> confirmUser(@RequestBody ConfirmationCode confirmationCode) {

        try{
            return ResponseEntity.ok(userService.confirmUser(confirmationCode));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserAuthRequestDTO userDTO) throws InterruptedException {

        try {
            userService.loadUserByUsername(userDTO.getPhoneNumber());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        try {
            userService.generateOtp(userDTO);
            return ResponseEntity.ok(userDTO.getPhoneNumber());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @PostMapping("/test")
    public ResponseEntity<?> test() throws InterruptedException{
        return ResponseEntity.ok("it's ok");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.disableUser(username);
        // Invalidate the user's session
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout successful");
    }
}
