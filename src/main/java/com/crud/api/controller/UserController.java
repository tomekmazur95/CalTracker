package com.crud.api.controller;

import com.crud.api.controller.swagger.UserControllerSwagger;
import com.crud.api.dto.RequestUserActivityDTO;
import com.crud.api.dto.RequestUserDTO;
import com.crud.api.dto.ResponseUserActivityDTO;
import com.crud.api.dto.ResponseUserDTO;
import com.crud.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements UserControllerSwagger {

    private final UserService userService;

    @Override
    @PostMapping("/{userInfoId}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ResponseUserDTO> createUser(@RequestBody RequestUserDTO dto, @PathVariable Long userInfoId) {
        return new ResponseEntity<>(userService.createUser(dto, userInfoId), HttpStatus.CREATED);
    }

    @Override
    @GetMapping()
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<ResponseUserDTO>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ResponseUserDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @Override
    @GetMapping("/userInfo")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ResponseUserDTO> findByUserInfoId(@RequestParam(name = "id") Long userInfoId) {
        return new ResponseEntity<>(userService.findByUserInfoId(userInfoId), HttpStatus.OK);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ResponseUserDTO> updateUser(@PathVariable Long id, @RequestBody RequestUserDTO dto) {
        return new ResponseEntity<>(userService.updateUser(id, dto), HttpStatus.OK);
    }

    @Override
    @PatchMapping("/{id}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<ResponseUserActivityDTO> updateUserActivity(@PathVariable Long id, @RequestBody RequestUserActivityDTO activityDTO) {
        return new ResponseEntity<>(userService.updateUserActivity(id, activityDTO), HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
