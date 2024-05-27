package net.sytes.studyshuffle.payload.request;

import jakarta.validation.constraints.NotBlank;
 
public class RegisterRequest {
    @NotBlank
    private String username;

    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }

}
