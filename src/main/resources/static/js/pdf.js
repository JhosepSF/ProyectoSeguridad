//Botones
function generarPDF(event) 
{
	event.preventDefault();
	const usuarioDiv = document.getElementById('recomendaciones');

	html2canvas(usuarioDiv).then(canvas => {
		const imgData = canvas.toDataURL('image/png');
		const pdf = new jsPDF('portrait', 'cm', 'a5');
		const imgProps = pdf.getImageProperties(imgData);
		const pdfWidth = pdf.internal.pageSize.getWidth();
		const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;
		
		pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
		pdf.save('documento.pdf');
	});
}

let regresar = document.getElementById("btn-regresar");
	regresar.addEventListener("click", function ()
	{
		const token = localStorage.getItem('token');
		
		const fetchOptions = {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        };
        
        fetch('http://localhost:5555/dashboard/', fetchOptions)
        .then(() =>
        {             
          window.location.href = "/matriz";
        });
    });
    
//Relleno de datos
document.addEventListener('DOMContentLoaded', function() 
{
    const eventoId = localStorage.getItem('idevento');

    const token = localStorage.getItem('token');

    if (eventoId && token) {
        fetch(`http://localhost:5555/evento/${eventoId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => 
        {
            if (!response.ok) 
            {
                throw new Error('No se pudo obtener el evento');
            }
            return response.json();
        })
        .then(evento => 
        {
			let recomendacionRaw = evento.recomendacion;

			recomendacionRaw = recomendacionRaw.replace(/^\[Generation\[assistantMessage=AssistantMessage \[messageType=ASSISTANT, toolCalls=\[\], textContent=/, '');
			
			recomendacionRaw = recomendacionRaw.replace(/, metadata=\{messageType=ASSISTANT}], chatGenerationMetadata=.*\]\]$/, '');
			
			let recomendacionLimpia = recomendacionRaw.trim();
			
			const eventoHtml = `
			    <div>
			        <section>
			            <h2>Introducción</h2>
			            <p>El evento <strong>${evento.name}</strong> ha sido identificado como un riesgo crítico para la seguridad de la información de <strong>${evento.idempresa.nombre}</strong>.</p>
			        </section>
			
			        ${recomendacionLimpia}
			    </div>
			`;
			
			document.getElementById('evento-container').innerHTML = eventoHtml;
        })
        .catch(error => {
            console.error('Error al obtener el evento:', error);
            document.getElementById('evento-container').innerHTML = `<p>No se pudo cargar la información del evento.</p>`;
        });
    } else {
        console.error('El id del evento o el token no se encuentran en el localStorage');
        document.getElementById('evento-container').innerHTML = `<p>No se ha encontrado el id del evento o el token.</p>`;
    }
});