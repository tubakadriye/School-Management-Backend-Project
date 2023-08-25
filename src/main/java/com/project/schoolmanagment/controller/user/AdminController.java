package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.entity.concretes.user.Admin;
import com.project.schoolmanagment.payload.request.user.AdminRequest;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.AdminResponse;
import com.project.schoolmanagment.service.user.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    //constructoe injection
    private final AdminService adminService;

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage<AdminResponse>>saveAdmin(@RequestBody @Valid AdminRequest adminRequest){ //valid annotation will initialize validations
        return ResponseEntity.ok(adminService.saveAdmin(adminRequest));
    }

    @GetMapping("/getAllAdminByPage")
    public ResponseEntity<Page<Admin>>getAllAdminsByPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        Page<Admin>admins = adminService.getAllAdminsByPage(page,size,sort,type);
        return new ResponseEntity<>(admins, HttpStatus.OK);


    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String>deleteByAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteById(id));
    }

    //TODO

    @GetMapping("/getAllAdmins")
    public ResponseEntity<List<AdminResponse>>getAllAdmins(){
        return null;
        }

        @GetMapping("/getAdminByUserName/{username}")
        public ResponseEntity<List<AdminResponse>>findAdminByUsername(@PathVariable String username){

        return null;

        }


//        @GetMapping()
//        public ResponseEntity<List<AdminResponse>>getAdminByNameOrLastName(@RequestParam String )
}
