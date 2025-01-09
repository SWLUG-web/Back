package com.boot.swlugweb.v1.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//    @PostMapping("/users/delete")
//    public ResponseEntity<String> deleteUser(@RequestParam Integer usersInfoNum) {
//        adminService.deleteUserInfo(usersInfoNum);
//        System.out.println(usersInfoNum);
//        return ResponseEntity.ok("User and related data deleted successfully");
//    }

}
