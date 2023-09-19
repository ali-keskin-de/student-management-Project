package com.project.payload.request.business;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UpdatePasswordRequest {

    @NotBlank(message = "Please provider old password")
    private String oldPassword;

    @NotBlank(message = "Please provider new password")
    private String newPassword;

}
