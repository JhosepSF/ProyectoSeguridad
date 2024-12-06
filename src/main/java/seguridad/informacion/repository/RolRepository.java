package seguridad.informacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import seguridad.informacion.entitys.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {

}
