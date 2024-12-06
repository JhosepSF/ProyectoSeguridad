package seguridad.informacion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import seguridad.informacion.entitys.Empresa;
import seguridad.informacion.entitys.Usuario;
import seguridad.informacion.repository.EmpresaRepository;
import seguridad.informacion.repository.UsuarioRepository;
import seguridad.informacion.request.EmpresaRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class EmpresaService {
    
	@Autowired
	EmpresaRepository repository;
	
	@Autowired
	UsuarioRepository repouser;
	
	public void saveEmpresa(EmpresaRequest request, String token)
	{
		Empresa emp = new Empresa();
		String username = getUsernameFromToken(token);
		Optional<Usuario> user = repouser.findByUsername(username);
		
		if (user.isPresent()) {
	        emp.setActivo("Activo");
	        emp.setCiudad(request.getCiudad());
	        emp.setDescripcion(request.getDescripcion());
	        emp.setDireccion(request.getDireccion());
	        emp.setNombre(request.getNombre());
	        emp.setNumeroempleados(request.getNumeroempleados());
	        emp.setPais(request.getPais());
	        emp.setRuc(request.getRuc());
	        emp.setSector(request.getSector());
	        emp.setTelefono(request.getTelefono());

	        repository.save(emp);
	    }
		else 
	    {
	        throw new RuntimeException("Usuario no encontrado: " + username);
	    }
	}
	
	public void deleteEvento(Integer id) 
	{
		repository.deleteById(id);
	}
	
	public List<Empresa> getAll() 
	{
	    return repository.findAll();
    }
	
	private String getUsernameFromToken(String token) {
	    String secretKey = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";
	
	    @SuppressWarnings("deprecation")
		Claims claims = Jwts.parser()
	            .setSigningKey(secretKey)
	            .parseClaimsJws(token)
	            .getBody();
	    
	    return claims.getSubject();
	}
}
