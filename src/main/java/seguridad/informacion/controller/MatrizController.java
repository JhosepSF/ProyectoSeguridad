package seguridad.informacion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MatrizController 
{
	@GetMapping("/matriz")
	public String matriz() 
	{    
		return "matriz.html";
	}
	
	@GetMapping("/empresas")
	public String empresas() 
	{    
		return "empresas.html";
	}
	
	@GetMapping("/pdf")
	public String pdf() 
	{
		return "pdf.html";
	}
}
