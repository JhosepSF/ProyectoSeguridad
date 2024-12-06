package seguridad.informacion.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import seguridad.informacion.entitys.Rol;
import seguridad.informacion.repository.RolRepository;
import seguridad.informacion.request.RolRequest;

@Service
public class RolService 
{
    @Autowired
    RolRepository rolrepo;
    
    public void saveRol(RolRequest request)
    {
        Rol rol = new Rol();
        rol.setName(request.getName());
        rolrepo.save(rol);
    }

    public List<Rol> getRolList()
    {
        return rolrepo.findAll();
    }

    public Rol getRolById(Integer id)
    {
        return rolrepo.findById(id).orElse(null);
    }
}
