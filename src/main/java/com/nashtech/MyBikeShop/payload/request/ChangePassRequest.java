package com.nashtech.MyBikeShop.payload.request;

import javax.validation.constraints.NotBlank;

public class ChangePassRequest {
		@NotBlank
	    private String email;

	    @NotBlank
	    private String oldPassword;
	    
	    @NotBlank
	    private String newPassword;
	    
	    public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getOldPassword() {
			return oldPassword;
		}

		public void setOldPassword(String oldPassword) {
			this.oldPassword = oldPassword;
		}

		public String getNewpassword() {
			return newPassword;
		}

		public void setNewpassword(String newpassword) {
			newPassword = newpassword;
		}

		
	
}
