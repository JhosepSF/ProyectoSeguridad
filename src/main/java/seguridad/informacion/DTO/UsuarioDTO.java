package seguridad.informacion.DTO;

public class UsuarioDTO 
{
	Integer id;
	String name;
    String lastname;
    String address;
    String role;
    String link;
        
	public UsuarioDTO(Integer id, String name, String lastname, String address, String role, String link) {
		super();
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.address = address;
		this.role = role;
		this.link = link;
	}
	public UsuarioDTO() 
	{
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
}
