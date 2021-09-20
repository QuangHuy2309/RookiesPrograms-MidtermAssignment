package com.nashtech.MyBikeShop.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChangePasswordRequest {
	@NotBlank
    @Size(min = 6)
    private String oldPassword;

    @NotBlank
    @Size(min = 6)
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
    
    
}
