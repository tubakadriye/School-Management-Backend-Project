package com.project.schoolmanagment.controller.user;


import com.project.schoolmanagment.payload.request.user.DeanRequest;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.DeanResponse;
import com.project.schoolmanagment.service.user.DeanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dean")
@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
public class DeanController {

    private final DeanService deanService;

    @PostMapping("/save")
    public ResponseMessage<DeanResponse>saveDean(@RequestBody @Valid DeanRequest deanRequest){
        return deanService.saveDean(deanRequest);
    }

    @PutMapping("/update/{userId}")
    public ResponseMessage<DeanResponse>updateDeanById(@PathVariable Long userId, @RequestBody @Valid DeanRequest deanRequest){

        return deanService.updateDeanById(userId, deanRequest);


    }
}
