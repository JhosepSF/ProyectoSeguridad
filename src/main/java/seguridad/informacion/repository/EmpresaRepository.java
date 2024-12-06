package seguridad.informacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import seguridad.informacion.entitys.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer>{

}
