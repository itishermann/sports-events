package com.etna.sportevents.payloads.request;

import com.etna.sportevents.models.UserRole;
import com.etna.sportevents.utils.EnumNamePattern;
import javax.validation.constraints.Size;

public class UpdateUserPayload {
    @Size(min = 3, max = 255, message = "Username must be between 3 and 255 characters long")
    private String username = null;

    @Size(min = 8,message = "Old password must be at least 8 characters long")
    private String oldPassword = null;

    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPassword = null;

    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPasswordConfirm = null;

    @Size(min = 3, max = 255, message = "Email must be between 3 and 255 characters long")
    private String email = null;

    @EnumNamePattern(regexp = "ROLE_(USER|ADMIN|MODERATOR)", message = "Role must be either ROLE_USER or ROLE_ADMIN or ROLE_MODERATOR")
    private UserRole role  = null;

    public UpdateUserPayload() {
    }

    public UpdateUserPayload(String username, String oldPassword, String newPassword, String newPasswordConfirm, String email, UserRole role) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
