/*package seguridad.informacion.config;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import seguridad.informacion.entitys.Rol;
import seguridad.informacion.entitys.Usuario;
import seguridad.informacion.repository.RolRepository;
import seguridad.informacion.repository.UsuarioRepository;
import seguridad.informacion.service.EncryptionService;

@Configuration
public class DataInitializer 
{
	@Autowired
    private EncryptionService encryption;
    
    @Bean
    CommandLineRunner initializeData(UsuarioRepository usuario, RolRepository rol, PasswordEncoder passwordEncoder) 
    {
        return args ->
        {
            Rol perfilAdministrador = new Rol(1, "ADMINISTRADOR");
            Rol perfilUsuario = new Rol(2, "USUARIO");
            rol.saveAll(List.of(perfilAdministrador, perfilUsuario));

            Usuario usuario1 = new Usuario();
            usuario1.setName("Jhosep");
            usuario1.setLastname("Sánchez");
            usuario1.setEstado("Activo");
            usuario1.setAddress("Jr. Leoncio Prado 1475");
            usuario1.setUsername("jhosep@unsm.com");
            usuario1.setLink(encryption.encrypt("https://drive.google.com/uc?id=1val2sfpWYQbqQT5njE622nhSIT4zL6XG"));
            usuario1.setPassword(passwordEncoder.encode("123456"));
            usuario1.setLogueado(0);
            Set<Rol> authorities1 = new HashSet<>();
            authorities1.add(perfilAdministrador);
            usuario1.setRol(authorities1);
            usuario.save(usuario1);
            
            Usuario usuario2 = new Usuario();
            usuario2.setName("George");
            usuario2.setLastname("Gonzales");
            usuario2.setEstado("Activo");
            usuario2.setAddress("Jr. San Martin 915");
            usuario2.setUsername("george@unsm.com");
            usuario2.setLink(encryption.encrypt("https://drive.google.com/uc?id=1val2sfpWYQbqQT5njE622nhSIT4zL6XG"));
            usuario2.setPassword(passwordEncoder.encode("123456"));
            usuario2.setLogueado(0);
            Set<Rol> authorities2 = new HashSet<>();
            authorities2.add(perfilUsuario);
            usuario2.setRol(authorities2);
            usuario.save(usuario2);
        };
    }
}*/