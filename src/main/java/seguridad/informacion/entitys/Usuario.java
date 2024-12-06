package seguridad.informacion.entitys;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "usuario", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class Usuario implements UserDetails
{
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue (strategy = GenerationType.AUTO)
	Integer id;
	
	@Column
	String name;
    String lastname;
    String address;
    String username;
    String password;
	String estado;
	String link;
	Boolean accountnonlocked = true;
    Integer intentosfallidos = 0;
    Integer logueado;
    
	public Integer getLogueado() {
		return logueado;
	}
	public void setLogueado(Integer logueado) {
		this.logueado = logueado;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Boolean getAccountnonlocked() {
		return accountnonlocked;
	}
	public void setAccountnonlocked(Boolean accountnonlocked) {
		this.accountnonlocked = accountnonlocked;
	}
	public Integer getIntentosfallidos() {
		return intentosfallidos;
	}
	public void setIntentosfallidos(Integer intentosfallidos) {
		this.intentosfallidos = intentosfallidos;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Rol.class, cascade = CascadeType.MERGE)
	@JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name="id_rol"))
	private Set<Rol> Rol;

	public Set<Rol> getRol() {
		return Rol;
	}
	public void setRol(Set<Rol> rol) {
		Rol = rol;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Rol.stream().map(cargo -> new SimpleGrantedAuthority(cargo.getName())).collect(Collectors.toList());
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return	true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
