package com.project.schoolmanagment.controller.user;


import com.project.schoolmanagment.payload.request.user.DeanRequest;
import com.project.schoolmanagment.payload.response.message.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.DeanResponse;
import com.project.schoolmanagment.service.user.DeanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dean")
public class DeanController {

    private final DeanService deanService;

    @PostMapping("/save")
    public ResponseMessage<DeanResponse>saveDean(@RequestBody @Valid DeanRequest deanRequest){
        return deanService.saveDean(deanRequest);
    }
}
