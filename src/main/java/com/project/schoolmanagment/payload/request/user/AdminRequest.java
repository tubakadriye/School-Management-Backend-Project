package com.project.schoolmanagment.payload.request.user;

import com.project.schoolmanagment.payload.request.abstracts.BaseUserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AdminRequest extends BaseUserRequest {

    private boolean builtIn;  // if builtin, we cannot delete it. admin cannot be deleted
}
