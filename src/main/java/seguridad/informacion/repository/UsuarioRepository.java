package seguridad.informacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import seguridad.informacion.entitys.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	Optional <Usuario> findByUsername (String username);
}
