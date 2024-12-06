package seguridad.informacion.request;

public class AuthResponse 
{
	String token;
	String role;

	public AuthResponse(String token) {
		super();
		this.token = token;
	}
	public AuthResponse() {
		
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
}
