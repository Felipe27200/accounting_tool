package com.accounting.accounting_tool.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordAdminDTO
{
    @NotBlank(message = "The new password is required")
    @Size(min = 8, message = "The new password must have minimum 8 characters")
    private String newPassword;
    @NotBlank(message = "The Confirm Password is required")
    @Size(min = 8, message = "The Confirm Password must have minimum 8 characters")
    private String passwordConfirmation;

    public ChangePasswordAdminDTO() {
    }

    public ChangePasswordAdminDTO(String newPassword, String passwordConfirmation) {
        this.newPassword = newPassword;
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
