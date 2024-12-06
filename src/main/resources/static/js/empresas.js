//Función para cargar empresas
async function loadCompanies() {
    const token = localStorage.getItem('token');

    if (!token) {
        console.error('No se encontró el token en el localStorage');
        return;
    }

    try {
        const response = await fetch('http://localhost:5555/empresa/all', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Error al obtener las empresas');
        }

        const companies = await response.json();
        displayCompanies(companies);

    } catch (error) {
        console.error('Hubo un problema con el fetch:', error);
    }
}

// Función para mostrar las empresas en el DOM
function displayCompanies(companies) {
    const companyCardsContainer = document.getElementById('companyCards');
    companyCardsContainer.innerHTML = '';

    companies.forEach(company => {
        const companyCard = document.createElement('div');
        companyCard.className = 'company-card';

        const h2 = document.createElement('h2');
        h2.textContent = company.nombre;

        const pLocation = document.createElement('p');
        pLocation.textContent = company.direccion || 'Ubicación desconocida';

        const pPrice = document.createElement('p');
        pPrice.className = 'price';
        pPrice.textContent = company.sector || ``;

        // Botón para crear matrices
        const createMatrixBtn = document.createElement('button');
        createMatrixBtn.textContent = 'Crear Matrices';
        createMatrixBtn.className = 'btn btn-primary';
        createMatrixBtn.addEventListener('click', () => {
            Swal.fire({
                title: 'Crear Matrices',
                text: `¿Deseas crear una matriz para ${company.nombre}?`,
                icon: 'question',
                showCancelButton: true,
                confirmButtonText: 'Sí, crear',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) 
                {
					localStorage.setItem('idempresa', company.id);
                    window.location.href = `/matriz`;
                }
            });
        });

        // Botón para eliminar empresa
        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = 'Eliminar';
        deleteBtn.className = 'btn btn-danger';
        deleteBtn.addEventListener('click', async () => {
            Swal.fire({
                title: '¿Estás seguro?',
                text: `Eliminarás la empresa ${company.nombre}. Esta acción no se puede deshacer.`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then(async (result) => {
                if (result.isConfirmed) {
                    try {
                        const token = localStorage.getItem('token');
                        if (!token) {
                            Swal.fire({
                                title: 'Error',
                                text: 'No se encontró el token en el localStorage',
                                icon: 'error',
                                confirmButtonText: 'OK'
                            });
                            return;
                        }

                        const response = await fetch(`http://localhost:5555/empresa/delete/${company.id}`, {
                            method: 'DELETE',
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': `Bearer ${token}`
                            }
                        });

                        if (response.ok) {
                            Swal.fire({
                                title: 'Empresa eliminada',
                                text: `La empresa ${company.nombre} ha sido eliminada exitosamente.`,
                                icon: 'success',
                                confirmButtonText: 'OK'
                            }).then(() => {
                                loadCompanies();
                            });
                        } else {
                            throw new Error('Error al eliminar la empresa');
                        }
                    } catch (error) {
                        Swal.fire({
                            title: 'Error',
                            text: 'Hubo un error al eliminar la empresa.',
                            icon: 'error',
                            confirmButtonText: 'OK'
                        });
                        console.error('Error al eliminar la empresa:', error);
                    }
                }
            });
        });

        // Añadir los elementos al card de la empresa
        companyCard.appendChild(h2);
        companyCard.appendChild(pLocation);
        companyCard.appendChild(pPrice);
        companyCard.appendChild(createMatrixBtn);
        companyCard.appendChild(deleteBtn);

        companyCardsContainer.appendChild(companyCard);
    });
}

// Función para manejar el modal y la creación de nuevas empresas
document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById("modalForm");
    const openModalBtn = document.getElementById("openModalBtn");
    const closeModal = document.getElementById("closeModal");
    const empresaForm = document.getElementById("empresaForm");

    openModalBtn.addEventListener('click', () => {
        modal.style.display = "flex";
    });

    closeModal.addEventListener('click', () => {
        modal.style.display = "none";
    });

    window.addEventListener('click', (event) => {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    });

    empresaForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const formData = {
            nombre: document.getElementById("nombre").value,
            ruc: document.getElementById("ruc").value,
            direccion: document.getElementById("direccion").value,
            telefono: document.getElementById("telefono").value,
            sector: document.getElementById("sector").value,
            numeroempleados: document.getElementById("numeroempleados").value,
            pais: document.getElementById("pais").value,
            ciudad: document.getElementById("ciudad").value,
            descripcion: document.getElementById("descripcion").value
        };
        
        const token = localStorage.getItem('token');

	    if (!token) {
	        console.error('No se encontró el token en el localStorage');
	        return;
	    }

        try {
            const response = await fetch('http://localhost:5555/empresa/new', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                Swal.fire({
	                title: 'Empresa creada exitosamente',
	                text: 'Nueva empresa.',
	                icon: 'success',
	                confirmButtonText: 'OK'
	            })
                modal.style.display = "none"; 
                empresaForm.reset();
                loadCompanies(); 
            } else {
                throw new Error("Error al crear la empresa");
            }
        } catch (error) {
            console.error('Error:', error);
            alert("Hubo un error al crear la empresa");
        }
    });

    loadCompanies();
});

async function logout()
{
	const url = 'http://localhost:5555/auth/logout';
	    
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            localStorage.removeItem('token');
            
            Swal.fire({
                title: 'Cierre de sesión exitoso',
                text: 'Has cerrado sesión correctamente.',
                icon: 'success',
                confirmButtonText: 'OK'
            }).then(() => {
                window.location.href = '/login';
            });
        } else {
            Swal.fire({
                title: 'Error',
                text: 'No se pudo cerrar sesión correctamente.',
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    } catch (error) {
        Swal.fire({
            title: 'Error',
            text: 'Ocurrió un error inesperado.',
            icon: 'error',
            confirmButtonText: 'OK'
        });
        console.error('Error:', error);
    }
}