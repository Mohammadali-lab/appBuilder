package ir.samin.appbuilder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.samin.appbuilder.dao.UserRepository;
import ir.samin.appbuilder.dto.*;
import ir.samin.appbuilder.entity.Ticket;
import ir.samin.appbuilder.entity.User;
import ir.samin.appbuilder.exception.GeneralRuntimeException;
import ir.samin.appbuilder.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Value("${application.kavenegar.apiKey}")
    private String apiKey;

    @Value("${application.kavehnegar.sendURL}")
    private String sendURL;

    private JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public UserService(UserDetailsService userDetailsService
            , UserRepository userRepository, JwtUtil jwtUtil){
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public UserDetails loadUserByUsername(String username) {
        return userDetailsService.loadUserByUsername(username);
    }


    public void generateOtp(UserAuthRequestDTO requestDTO) throws UnsupportedEncodingException, JsonProcessingException {
        User user = userRepository.findByUsername(requestDTO.getPhoneNumber()).get();
        user.setEnabled(false);
        String code = Integer.toString(generateConfirmCode());
        user.setConfirmCode(code);

        user.setConfirmCodeRegisterTime(LocalDateTime.now());
        userRepository.save(user);

        try{
            sendSms(requestDTO.getPhoneNumber(), code);
        } catch (RuntimeException | JsonProcessingException | UnsupportedEncodingException e) {
            throw e;
        }
    }

    public UserResponseDTO createUser(UserAuthRequestDTO requestDTO, Long userId) throws UnsupportedEncodingException, JsonProcessingException {

        User user = new User();

        user.setId(userId);
        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        user.setUsername(requestDTO.getPhoneNumber());
        user.setEnabled(false);
        user.setConfirmed(false);
        String code = Integer.toString(generateConfirmCode());
        user.setConfirmCode(code);

        user.setConfirmCodeRegisterTime(LocalDateTime.now());

        user = userRepository.save(user);
        UserResponseDTO res = new UserResponseDTO();
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setPhoneNumber(user.getUsername());

        try{
            sendSms(requestDTO.getPhoneNumber(), code);
        } catch (RuntimeException | JsonProcessingException | UnsupportedEncodingException e) {
            throw e;
        }
        return res;
    }

    public void disableUser(String username) {
        User user = userRepository.findByUsername(username).get();
        user.setEnabled(false);
        userRepository.save(user);
    }

    public void enableUser(String username) {
        User user = userRepository.findByUsername(username).get();
        user.setEnabled(true);
        userRepository.save(user);
    }

//    public void sendOTP(String username) throws JsonProcessingException {
//        Customer byEmail = customerRepository.findByEmailOrMobileNumber(username).get();
//        String template = "LOGIN";
//        String token = sendTwoStepCode(byEmail);
//        String token2 = "120";
//        if (byEmail.getMobileNumber() == null) {
//            throw new GeneralRuntimeException("MOBILE_NUMBER_NOT_FOUND", HttpStatus.NOT_FOUND, "mobile number not found");
//        }
//        sendSms(template, token, token2, byEmail.getMobileNumber());
//    }

    public UserAuthResponseDTO confirmUser(ConfirmationCode confirmationCode) {
        Optional<User> returnedUser = userRepository.findByUsername(confirmationCode.getPhoneNumber());
        if (returnedUser.isPresent()){
            User user = returnedUser.get();
            LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
            if (user.getConfirmCodeRegisterTime().isAfter(fiveMinutesAgo) &&
                    confirmationCode.getOtpCode().equals(user.getConfirmCode())) {
                user.setConfirmed(true);
                user.setEnabled(true);
                userRepository.save(user);
                String token = jwtUtil.generateToken(user.getUsername(), false);
                UserAuthResponseDTO userAuthResponseDTO = new UserAuthResponseDTO();
                userAuthResponseDTO.setFirstName(user.getFirstName());
                userAuthResponseDTO.setLastName(user.getLastName());
                userAuthResponseDTO.setPhoneNumber(user.getUsername());
                userAuthResponseDTO.setToken(token);
                userAuthResponseDTO.setMessage("user registered successfully");

                return userAuthResponseDTO;
            }
            throw new RuntimeException("otpCode is not valid");
        }
        throw new RuntimeException("user not found");
    }

    public User findUser(String phoneNumber) {
        Optional<User> user = userRepository.findByUsername(phoneNumber);
        return user.orElse(null);
    }

    public User findUserByPhoneNumber(String phoneNumber) {

        Optional<User> user = userRepository.findByUsername(phoneNumber);
        return user.orElse(null);
    }

    public void setTicket(User user, TicketDTO ticketDTO) {

        Ticket ticket = new Ticket();
        ticket.setMessage(ticketDTO.getMessage());
        user.add(ticket);
        userRepository.save(user);
    }

    public ResponseEntity sendSms(String phoneNumber, String otpCode) throws JsonProcessingException, UnsupportedEncodingException {

        String message = "کد اعتبار سنجی: " + otpCode + "\n" + "لغو11";
        String encodedMessage = URLEncoder.encode(message, "UTF-8");

        RestTemplate restTemplate = new RestTemplate();
        String checkUrl = String.format(sendURL, apiKey, phoneNumber, encodedMessage);
        RequestEntity requestEntity = new RequestEntity(HttpMethod.GET, URI.create(checkUrl));
        try {
            restTemplate.exchange(requestEntity, Map.class);
        } catch (HttpClientErrorException exception) {
            HttpStatus statusCode = exception.getStatusCode();
            String responseString = exception.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            KaveNegarError result = mapper.readValue(responseString,
                    KaveNegarError.class);
            handleError(statusCode, result);
        }
        return ResponseEntity.ok().body("LOGIN_SMS_OTP");
    }

    private void handleError(HttpStatus statusCode, KaveNegarError result) {
        if (statusCode.equals(HttpStatus.I_AM_A_TEAPOT) || statusCode.equals(HttpStatus.FAILED_DEPENDENCY) ||
                statusCode.equals(HttpStatus.UPGRADE_REQUIRED) || statusCode.equals(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE)) {
            String error = result.toString();
            throw new GeneralRuntimeException("SMS_OTP_UNAVAILABLE", HttpStatus.NOT_FOUND, (String) result.getReturny().get("message"));
        } else if (statusCode.equals(HttpStatus.UNPROCESSABLE_ENTITY)) {
            String error = result.toString();
            throw new GeneralRuntimeException("SMS_SERVICE_HAS_PROBLEM", HttpStatus.NOT_FOUND, (String) result.getReturny().get("message"));
        } else {
            String error = result.toString();
            throw new GeneralRuntimeException("SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, (String) result.getReturny().get("message"));
        }
    }

    public int generateConfirmCode() {
        Random r = new Random( System.currentTimeMillis() );
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }
}


