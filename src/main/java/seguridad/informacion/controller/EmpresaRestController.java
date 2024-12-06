package seguridad.informacion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import seguridad.informacion.entitys.Empresa;
import seguridad.informacion.request.EmpresaRequest;
import seguridad.informacion.service.EmpresaService;

@RestController
@RequestMapping("/empresa")
public class EmpresaRestController
{
	@Autowired
	EmpresaService service;
	
	@PostMapping("/new")
	public void saveEmpresa(@RequestHeader("Authorization") String authHeader, @RequestBody EmpresaRequest request) 
	{
		String token = authHeader.replace("Bearer ", "");
		service.saveEmpresa(request, token);
	}
	
	@GetMapping("/all")
	public List<Empresa> getAll()
	{
		return service.getAll();
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable Integer id) 
	{
		service.deleteEvento(id);
	}
}