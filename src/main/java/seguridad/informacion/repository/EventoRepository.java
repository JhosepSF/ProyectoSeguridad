package seguridad.informacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import seguridad.informacion.entitys.Evento;

public interface EventoRepository extends JpaRepository<Evento, Integer>{

}
