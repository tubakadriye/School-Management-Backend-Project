package com.project.schoolmanagment.contoller.user;

import com.project.schoolmanagment.entity.concretes.user.Admin;
import com.project.schoolmanagment.payload.request.AdminRequest;
import com.project.schoolmanagment.payload.response.AdminResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.service.user.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@PostMapping("/save")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<ResponseMessage<AdminResponse>>saveAdmin(@RequestBody @Valid AdminRequest adminRequest){
		return ResponseEntity.ok(adminService.saveAdmin(adminRequest));
	}


	@GetMapping("/getAllAdminsByPage")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<Page<Admin>>getAllAdminsByPage(
			@RequestParam(value = "page",defaultValue = "0") int page,
			@RequestParam(value = "size",defaultValue = "10") int size,
			@RequestParam(value = "sort",defaultValue = "name") String sort,
			@RequestParam(value = "type",defaultValue = "desc") String type
	){
		Page<Admin>admins = adminService.getAllAdminsByPage(page,size,sort,type);
		return new ResponseEntity<>(admins, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<String> deleteAdminById(@PathVariable Long id){
		return ResponseEntity.ok(adminService.deleteAdminById(id));
	}






}
