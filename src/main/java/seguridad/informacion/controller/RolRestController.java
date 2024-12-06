package seguridad.informacion.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import seguridad.informacion.entitys.Rol;
import seguridad.informacion.request.RolRequest;
import seguridad.informacion.service.RolService;

@RestController
@RequestMapping("/rol")
public class RolRestController 
{
    @Autowired
    private RolService rolService;

    @PostMapping("/new")
    public void newRole (@RequestBody RolRequest request) throws IOException
    {
        rolService.saveRol(request);
    }

    @GetMapping("/get/{id}")
    public Rol getRole (@PathVariable Integer id) throws IOException
    {
        return rolService.getRolById(id);
    }

    @GetMapping("/all")
    public List<Rol> getAllRoles () throws IOException
    {
        return rolService.getRolList();
    }
}
