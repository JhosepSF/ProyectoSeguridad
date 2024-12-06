package seguridad.informacion.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import seguridad.informacion.DTO.UsuarioDTO;
import seguridad.informacion.entitys.Rol;
import seguridad.informacion.entitys.Usuario;
import seguridad.informacion.repository.RolRepository;
import seguridad.informacion.repository.UsuarioRepository;
import seguridad.informacion.request.UsuarioRequest;

@Service
public class UsuarioService 
{
	@Autowired
    UsuarioRepository userrepo;
	
	@Autowired
    RolRepository rolrepo;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
    private EncryptionService encryption;

    public void saveUsuario(UsuarioRequest request) throws Exception
    {
        Usuario userr = new Usuario();
        userr.setUsername(request.getUsername());
        String encoded = passwordEncoder.encode(request.getPassword());
        userr.setPassword(encoded);
        userr.setAddress(request.getAddress());
        userr.setLastname(request.getLastname());
        userr.setName(request.getName());
        userr.setLink(encryption.encrypt(request.getLink()));
        userr.setEstado("Activo");
        userr.setLogueado(0);
        
        Set <Rol> roles = new HashSet<>();
        Rol rol = rolrepo.findById(request.getRol())
                .orElseThrow(() -> new RuntimeException ("No se encontro el rol"));
        roles.add(rol);

        userr.setRol(roles);

        userrepo.save(userr);
    }

    public void updateUsuario(Integer id, UsuarioRequest request) throws Exception 
    {
        Usuario userr = userrepo.findById(id)
                    .orElseThrow(() -> new RuntimeException ("No se encontro el usuario"));

        userr.setAddress(request.getAddress());
        userr.setLastname(request.getLastname());
        userr.setName(request.getName());
        userr.setLink(encryption.encrypt(request.getLink()));
        Set <Rol> roles = userr.getRol();
        roles.clear();
        Rol rol = rolrepo.findById(request.getRol())
                .orElseThrow(() -> new RuntimeException ("No se encontro el rol"));
        roles.add(rol);
        
        userrepo.save(userr);
    }

    public List<UsuarioDTO> getUsers () throws Exception
    {
    	List<UsuarioDTO> dto = new ArrayList<>();
    	List<Usuario> usuarios = (List<Usuario>) userrepo.findAll();
    	
    	for(Usuario user : usuarios) 
    	{
    		if(user.getEstado().equals("Activo")) 
    		{
    			String roleName = "";
    		    if (user.getRol() != null && !user.getRol().isEmpty()) {
    		        Rol Cargo = user.getRol().iterator().next();
    		        roleName = Cargo.getName();
    		    }
    		    String link = encryption.decrypt(user.getLink());
        		UsuarioDTO userdto = new UsuarioDTO
        				(
        					user.getId(),
        					user.getName(),
        					user.getLastname(),
        					user.getAddress(),
        					roleName,
        					link
        				);
        		dto.add(userdto);
    		}
    	}
    	
        return dto;
    }

    public UsuarioDTO getUsuario(Integer id) throws Exception
    {
        Usuario user = userrepo.findById(id)
                    .orElseThrow(() -> new RuntimeException ("No se encontro el usuario"));
        
        UsuarioDTO userdto = new UsuarioDTO();
        
        if(user.getEstado().equals("Activo")) 
		{
			String roleName = "";
		    if (user.getRol() != null && !user.getRol().isEmpty()) {
		        Rol Cargo = user.getRol().iterator().next();
		        roleName = Cargo.getName();
		    }
		    String link = encryption.decrypt(user.getLink());
    		
		    userdto.setId(user.getId());
		    userdto.setName(user.getName());
		    userdto.setAddress(link);
		    userdto.setLastname(user.getLastname());
		    userdto.setRole(roleName);
		    userdto.setLink(link);
		}
        return userdto;
    }

    public void deleteUsuario(Integer id)
    {
        Usuario userr = userrepo.findById(id)
                    .orElseThrow(() -> new RuntimeException ("No se encontro el usuario"));
        userr.setEstado("Inactivo");
        userrepo.save(userr);
    }

	public List<UsuarioDTO> getUsersInactivos() throws Exception 
	{
		List<UsuarioDTO> dto = new ArrayList<>();
    	List<Usuario> usuarios = (List<Usuario>) userrepo.findAll();
    	
    	for(Usuario user : usuarios) 
    	{
    		if(user.getEstado().equals("Inactivo")) 
    		{
    			String roleName = "";
    		    if (user.getRol() != null && !user.getRol().isEmpty()) {
    		        Rol Cargo = user.getRol().iterator().next();
    		        roleName = Cargo.getName();
    		    }
    		    String link = encryption.decrypt(user.getLink());
        		UsuarioDTO userdto = new UsuarioDTO
        				(
        					user.getId(),
        					user.getName(),
        					user.getLastname(),
        					user.getAddress(),
        					roleName,
        					link
        				);
        		dto.add(userdto);
    		}
    	}
    	
        return dto;
	}

	public void activateUsuario(Integer id) {
		Usuario userr = userrepo.findById(id)
                .orElseThrow(() -> new RuntimeException ("No se encontro el usuario"));
	    userr.setEstado("Activo");
	    userrepo.save(userr);
	}
}
