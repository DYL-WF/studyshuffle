package net.sytes.studyshuffle.payload.response;

import java.util.List;

public class UserInfoResponse {
	private Long id;
	private String userId;
	private String email;
	private List<String> roles;

	public UserInfoResponse(Long id, String userId, String email, List<String> roles) {
		this.id = id;
		this.userId = userId;
		this.email = email;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getRoles() {
		return roles;
	}
}
