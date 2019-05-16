package com.tesla.parkingapp.web;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;

import com.tesla.parkingapp.constraint.FieldMatch;

import javax.validation.constraints.AssertTrue;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
        @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")
})
public class UserRegistrationDto {

	public UserRegistrationDto() {
		
	}
	
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    @Email
    @NotBlank
    private String email;

    @Email
    @NotBlank
    private String confirmEmail;

    @AssertTrue
    private Boolean terms;

    private Boolean company;
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmEmail() {
        return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
        this.confirmEmail = confirmEmail;
    }
    
    public Boolean getCompany() {
		return company;
	}

	public void setCompany(Boolean company) {
		this.company = company;
	}

	public Boolean getTerms() {
        return terms;
    }

    public void setTerms(Boolean terms) {
        this.terms = terms;
    }

}