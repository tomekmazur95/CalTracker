package com.crud.api.service

import com.crud.api.dto.AuthenticationRequest
import com.crud.api.dto.RegisterRequest
import com.crud.api.entity.UserInfo
import com.crud.api.enums.Role
import com.crud.api.error.UserAlreadyExistsException
import com.crud.api.error.UserNotFoundException
import com.crud.api.repository.UserInfoRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class AuthenticationServiceTest extends Specification {

    UserInfoRepository userInfoRepository = Mock()
    PasswordEncoder passwordEncoder = Mock()
    JwtService jwtService = Mock()
    AuthenticationManager authenticationManager = Mock()

    AuthenticationService authenticationService = new AuthenticationService(
            userInfoRepository,
            passwordEncoder,
            jwtService,
            authenticationManager)

    def "should throw UserAlreadyExistsException for register method"() {
        given:
        RegisterRequest registerRequest = new RegisterRequest("john@gmail.com", "password")

        when:
        authenticationService.register(registerRequest)

        then:
        1 * userInfoRepository.existsByEmail(_ as String) >> true
        def exception = thrown(UserAlreadyExistsException)
        exception.message == "User with email: " + registerRequest.getEmail() + " already exists"

        verifyAll {
            0 * passwordEncoder.encode(_ as String)
            0 * userInfoRepository.save(_ as UserInfo)
            0 * jwtService.generateToken(_ as UserDetails)
        }
    }

    def "positive path for register method"() {
        given:
        RegisterRequest registerRequest = new RegisterRequest("john@gmail.com", "password")
        UserInfo user = new UserInfo(1L, registerRequest.getEmail(), registerRequest.getPassword(), Role.USER)

        when:
        def result = authenticationService.register(registerRequest)

        then:
        1 * userInfoRepository.existsByEmail(_ as String) >> false
        1 * userInfoRepository.save(_ as UserInfo) >> user
        1 * passwordEncoder.encode(_ as String) >> "encodedPassword"
        1 * jwtService.generateToken(_ as UserDetails) >> "jwtToken"
        result.getToken() == "jwtToken"
    }

    def "positive path for authenticate method"() {
        given:
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("john@gmail.com", "password")
        UserInfo userInfo = new UserInfo(1L, authenticationRequest.email, authenticationRequest.password, Role.USER)
        when:
        def result = authenticationService.authenticate(authenticationRequest)

        then:
        1 * authenticationManager.authenticate(_ as Authentication)
        1 * userInfoRepository.findByEmail(_ as String) >> Optional.of(userInfo)
        1 * jwtService.generateToken(_ as UserDetails) >> "jwtToken"
        result.token == "jwtToken"
    }

    def "should throw UserNotFoundException for authenticate method"() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("john@gmail.com", "password")

        when:
        authenticationService.authenticate(authenticationRequest)

        then:
        1 * userInfoRepository.findByEmail(_ as String) >> Optional.empty()
        def exception = thrown(UserNotFoundException)
        exception.message == "User with email: "  + authenticationRequest.getEmail() + " not found"

        verifyAll {
            0 * jwtService.generateToken(_ as UserDetails)
            1 * authenticationManager.authenticate(_ as Authentication)
        }
    }
}
