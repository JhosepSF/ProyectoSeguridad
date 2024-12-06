package seguridad.informacion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController 
{
	@GetMapping("/")
	public String dashboard() 
	{
		return "dashboard.html";
	}
		
	@GetMapping("/usuarios/activos")
	public String usuariosactivos() 
	{
		return "dashboard-usuarios-activos.html";
	}
	
	@GetMapping("/usuarios/inactivos")
	public String usuariosinactivos() 
	{
		return "dashboard-usuarios-inactivos.html";
	}
}
