package com.boot.swlugweb.v1.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String getHome() {
        System.out.println("admin page connect");
        return "admin";
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponseDto>> getUsers() {
        List<AdminUserResponseDto> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
