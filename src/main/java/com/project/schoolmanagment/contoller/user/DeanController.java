package com.project.schoolmanagment.contoller.user;

import com.project.schoolmanagment.payload.request.DeanRequest;
import com.project.schoolmanagment.payload.response.DeanResponse;
import com.project.schoolmanagment.payload.response.ResponseMessage;
import com.project.schoolmanagment.service.user.DeanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dean")
/**
 * Class level authorization
 */
@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
public class DeanController {

	private final DeanService deanService;

	@PostMapping("/save")
	public ResponseMessage<DeanResponse> save (@RequestBody @Valid DeanRequest deanRequest){
		return deanService.save(deanRequest);
	}

	@PutMapping("/update/{userId}")
	public ResponseMessage<DeanResponse> update(@RequestBody @Valid DeanRequest deanRequest,
	                                            @PathVariable Long userId){
		return deanService.update(deanRequest,userId);
	}

	@DeleteMapping("/delete/{userId}")
	public ResponseMessage deleteDeanById(@PathVariable Long userId){
		return deanService.deleteDeanById(userId);
	}


	@GetMapping("/getManagerById/{userId}")
	public ResponseMessage<DeanResponse> getDeanById(@PathVariable Long userId){
		return deanService.getDeanById(userId);
	}

	@GetMapping("/getAll")
	public List<DeanResponse> getAllDeans(){
		return deanService.getAllDeans();
	}


	@GetMapping("/search")
	public Page<DeanResponse> getAllDeansByPage(
			@RequestParam(value = "page")int page,
			@RequestParam(value = "size") int size,
			@RequestParam(value = "sort") String sort,
			@RequestParam(defaultValue = "desc",value = "type") String type
	){
		return deanService.getAllDeansByPage(page,size,sort,type);
	}









}