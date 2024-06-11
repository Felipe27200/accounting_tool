package com.accounting.accounting_tool.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordDTO
{
    @NotBlank(message = "The password is required")
    @Size(min = 8, message = "The password have to have minimum 8 characters")
    private String oldPassword;
    @NotBlank(message = "The new password is required")
    @Size(min = 8, message = "The password have to have minimum 8 characters")
    private String newPassword;
    @NotBlank(message = "The check new password is required")
    @Size(min = 8, message = "The password have to have minimum 8 characters")
    private String passwordConfirmation;

    public ChangePasswordDTO() {
    }

    public ChangePasswordDTO(String oldPassword, String newPassword, String passwordConfirmation) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.passwordConfirmation = passwordConfirmation;
    }

    public @NotBlank(message = "The password is required") String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(@NotBlank(message = "The password is required") String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public @NotBlank(message = "The new password is required") @Size(min = 8) String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank(message = "The new password is required") @Size(min = 8) String newPassword) {
        this.newPassword = newPassword;
    }

    public @NotBlank(message = "The check new password is required") @Size(min = 8) String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(@NotBlank(message = "The check new password is required") @Size(min = 8) String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
