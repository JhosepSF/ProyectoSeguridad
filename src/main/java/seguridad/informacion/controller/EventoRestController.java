package seguridad.informacion.controller;

import java.util.List;

import org.springframework.ai.chat.model.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.io.IOException;
import seguridad.informacion.entitys.Evento;
import seguridad.informacion.request.EventoRequest;
import seguridad.informacion.request.FirmaRequest;
import seguridad.informacion.service.EventoService;

@RestController
@RequestMapping("/evento")
public class EventoRestController {
    
	@Autowired
    EventoService service;

    @PostMapping("/new")
    public List<Generation> saveEvento(@RequestBody EventoRequest request) throws IOException 
    {
        return service.saveEvento(request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEvento(@PathVariable Integer id) 
    {
        service.deleteEvento(id);
    }

    @GetMapping("/all/{id}")
    public List<Evento> getAllEventos(@PathVariable Integer id) throws IOException
    {
        return service.getAll(id);
    }
    
    @GetMapping("/{id}")
    public Evento getById(@PathVariable Integer id) throws IOException
    {
        return service.getById(id);
    }
    
    @PostMapping("/firmarinforme")
    public String firmarInforme(@RequestBody FirmaRequest request)
    {
        return service.firmarInforme(request);
    }
    
    @PostMapping("/upload")
    public String pdf(@RequestParam("idevento") Integer idevento,
            @RequestParam("informe") MultipartFile informe) throws java.io.IOException
    {
        return service.uploadPDF(idevento, informe);
    }
    
    @GetMapping("/download/{idEvento}")
    public ResponseEntity<byte[]> descargarInforme(@PathVariable Integer idEvento) {
        return service.descargarInforme(idEvento);
    }
}