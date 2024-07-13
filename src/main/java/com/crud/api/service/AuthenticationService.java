package com.crud.api.service;

import com.crud.api.dto.AuthenticationRequest;
import com.crud.api.dto.AuthenticationResponse;
import com.crud.api.dto.RegisterRequest;
import com.crud.api.entity.UserInfo;
import com.crud.api.enums.Role;
import com.crud.api.error.UserAlreadyExistsException;
import com.crud.api.error.UserNotFoundException;
import com.crud.api.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.crud.api.util.ConstantsUtils.USER_ALREADY_EXISTS;
import static com.crud.api.util.ConstantsUtils.USER_EMAIL_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userInfoRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(String.format(USER_ALREADY_EXISTS, request.getEmail()));
        }
        UserInfo user = UserInfo.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userInfoRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        UserInfo userInfo = userInfoRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_EMAIL_NOT_FOUND, request.getEmail())));
        String jwtToken = jwtService.generateToken(userInfo);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
