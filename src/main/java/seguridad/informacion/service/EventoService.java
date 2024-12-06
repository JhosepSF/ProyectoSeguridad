package seguridad.informacion.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import seguridad.informacion.entitys.Empresa;
import seguridad.informacion.entitys.Evento;
import seguridad.informacion.entitys.Usuario;
import seguridad.informacion.repository.EmpresaRepository;
import seguridad.informacion.repository.EventoRepository;
import seguridad.informacion.repository.UsuarioRepository;
import seguridad.informacion.request.EventoRequest;
import seguridad.informacion.request.FirmaRequest;

@Service
public class EventoService 
{
	@Autowired
	EventoRepository repository;

	@Autowired
	EmpresaRepository repoempre;
	
	@Autowired
	UsuarioRepository usuarior;
	
	public EventoService(ChatClient.Builder chatClientBuilder) 
    {
        this.chatClient = chatClientBuilder.build();
    }
	
	private ChatClient chatClient;
	
	public List<Generation> saveEvento(EventoRequest request)
	{
		Evento evento = new Evento();
		
		evento.setName(request.getName());
		List<Integer> impacto = new ArrayList<>(request.getImpacto_v());
		List<Integer> probabilidad = new ArrayList<>(request.getProbabilidad_v());
		Integer valorriesgo = impacto.get(request.getImpacto()-1)*probabilidad.get(request.getProbabilidad()-1);
		evento.setValorRiesgo(valorriesgo);
		
		List<Integer> aceptable = new ArrayList<>(request.getRiesgo_aceptable());
		List<Integer> tolerable = new ArrayList<>(request.getRiesgo_tolerable());		
		List<Integer> alto = new ArrayList<>(request.getRiesgo_alto());		
		List<Integer> extremo = new ArrayList<>(request.getRiesgo_extremo());

		if(valorriesgo>=aceptable.get(0) && valorriesgo<=aceptable.get(1)) 
		{
			evento.setColor("#198754");
			evento.setNivel("Riesgo Aceptable");
		}
		else if(valorriesgo>=tolerable.get(0) && valorriesgo<=tolerable.get(1)) 
		{
			evento.setColor("#faf206");
			evento.setNivel("Riesgo Tolerable");
		}
		else if(valorriesgo>=alto.get(1) && valorriesgo<=alto.get(0)) 
		{
			evento.setColor("#ffc107");
			evento.setNivel("Riesgo Alto");
		}
		else if(valorriesgo>=extremo.get(0) && valorriesgo<=extremo.get(1)) 
		{
			evento.setColor("#dc3545");
			evento.setNivel("Riesgo Extremo");
		}
		else 
		{
			evento.setColor("#silver");
			evento.setNivel("Riesgo No Definido");
		}
		evento.setImpacto(request.getImpacto());
		evento.setImpacto_v(request.getImpacto_v());
		evento.setProbabilidad(request.getProbabilidad());
		evento.setProbabilidad_v(request.getProbabilidad_v());		
		evento.setRiesgo_alto(request.getRiesgo_alto());
		evento.setRiesgo_extremo(request.getRiesgo_extremo());
		evento.setRiesgo_tolerable(request.getRiesgo_tolerable());
		evento.setRiesgo_aceptable(request.getRiesgo_aceptable());

		Empresa emp = repoempre.findById(request.getEmpresa())
						.orElseThrow(()->new RuntimeException("No se encontro la empresa"));
		
		evento.setIdempresa(emp);
		evento.setEstadoFirmado(request.getEstadoFirmado());
		evento.setNombreInforme(request.getNombreInforme());
		
		String promptMessage = generarPrompt(request);

        PromptTemplate promptTemplate = new PromptTemplate(promptMessage);
        Prompt prompt = promptTemplate.create();

        CallResponseSpec responseSpec = chatClient.prompt(prompt).call();
        
        List<Generation> recomendacion = responseSpec.chatResponse().getResults();
        
        System.out.println(recomendacion.toString());
       
        evento.setRecomendacion(recomendacion.toString());
        
        repository.save(evento);
        
        return recomendacion;
	}

	public void deleteEvento(Integer id) 
	{
		repository.deleteById(id);
	}
	
	public List<Evento> getAll(Integer id) {
		List<Evento> eve = new ArrayList<>();
		List<Evento> eventos = repository.findAll();
		
		for(Evento evento : eventos) 
		{
			if(evento.getIdempresa().getId().equals(id)) 
			{
				eve.add(evento);
			}
		}
		return eve;
    }
	
	public Evento getById(Integer id) 
	{
		return repository.findById(id)
				.orElseThrow(()-> new RuntimeException("No se encontro el evento"));
	}
	
	public String uploadPDF(Integer idevento, MultipartFile informe) throws IOException 
	{
        if (informe.isEmpty()) 
        {
            return "No se seleccionó ningún archivo.";
        }

        try 
        {
        	Evento evento = repository.findById(idevento).get();       	
            evento.setInforme(informe.getBytes());
            evento.setNombreInforme(informe.getOriginalFilename());
            evento.setEstadoFirmado(0);

            repository.save(evento);
            return "Archivo subido";
        } catch (IOException e) {
            return "Error al subir el archivo.";
        }		
	}
	
    public ResponseEntity<byte[]> descargarInforme(Integer idEvento)
    {
        Evento evento = repository.findById(idEvento).get();
        return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + evento.getNombreInforme() + "\"")
        .contentType(MediaType.APPLICATION_PDF)
        .body(evento.getInforme());
    }
	
	public String firmarInforme(FirmaRequest request)
	{
        try 
        {
        	Usuario usu = usuarior.findByUsername(request.getUsername()).get();

            if (!new BCryptPasswordEncoder().matches(request.getPassword(), usu.getPassword())) {
                return "Password diferente";
            }

            Evento evento = repository.findById(request.getIdEvento()).get();
            File temFile = File.createTempFile("temp-", ".pdf");

            byte[] archivo = evento.getInforme();
            Files.write(temFile.toPath(), archivo);

            PDDocument document = PDDocument.load(temFile);
            PDPage page = document.getPage(0);

            PDRectangle pageSize = page.getMediaBox();
            float height = (float) pageSize.getHeight();

            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
            contentStream.beginText();

            PDType0Font font = PDType0Font.load(document, new File("C:/Windows/Fonts/arial.ttf"));
            contentStream.setFont(font, 8);

            contentStream.newLineAtOffset(66, (height - 30));
            contentStream.showText("Firmado por: " + usu.getName() + " " + usu.getLastname());
            contentStream.newLineAtOffset(0, -10);
            contentStream.showText("Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            contentStream.newLineAtOffset(0, -10);
            contentStream.showText("Hora: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            contentStream.endText();

            String imagenPath = "src/main/resources/static/img/Logo-FISI.jpg";
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagenPath, document);
            contentStream.drawImage(pdImage, 10, (height - 65), 60, 60);
            
            contentStream.close();

            document.save(temFile);
            document.close();

            byte[] archivoFirmado = Files.readAllBytes(temFile.toPath());
            evento.setInforme(archivoFirmado);
            evento.setEstadoFirmado(1);
            repository.save(evento);
            return "Firmado correctamente";
        } 
        catch (Exception e)
        {
            return "Error al firmar el archivo";
        }
    }
	
	private String generarPrompt(EventoRequest request)
    {
        List<Integer> impacto = new ArrayList<>(request.getImpacto_v());
        List<Integer> probabilidad = new ArrayList<>(request.getProbabilidad_v());
        Integer valorriesgo = impacto.get(request.getImpacto() - 1) * probabilidad.get(request.getProbabilidad() - 1);
        Empresa emp = repoempre.findById(request.getEmpresa())
                .orElseThrow(() -> new RuntimeException("No se encontró la empresa"));

        return String.format
		(
			"La empresa %s, que se dedica al rubro %s, ha identificado un evento llamado %s. " +
	        "Este evento tiene una probabilidad de %s y un impacto de %s. " +
	        "Según estos valores, el nivel de riesgo calculado es %s. " +
	        "Considerando las leyes de seguridad de la información, como la ISO/IEC 27001, " +
	        "por favor, proporcione las siguientes recomendaciones importantes para mitigar los riesgos " +
	        "asociados a este evento. La respuesta debe ser en formato HTML, pero dentro de un único <div>, " +
	        "sin incluir <head>, <body> ni otras etiquetas fuera del contenido dentro del div. " +
	        "El HTML debe contener las siguientes secciones: " +
	        "<section> para la introducción, <section> para las recomendaciones de mitigación de riesgo, " +
	        "y <section> para los procedimientos de emergencia, cada uno con su respectivo título y contenido " +
	        "en formato adecuado para ser insertado directamente en una página web.",
            emp.getNombre(),
            emp.getSector(),
            request.getName(),
            request.getProbabilidad(),
            request.getImpacto(),
            valorriesgo
        );
    }
}
