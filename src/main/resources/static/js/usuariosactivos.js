document.addEventListener("DOMContentLoaded", () => {
    const userTableBody = document.getElementById("userTableBody");
    const addUserBtn = document.getElementById("addUserBtn");
    const addUserModal = document.getElementById("addUserModal");
    const editUserModal = document.getElementById("editUserModal");
    const addUserForm = document.getElementById("addUserForm");
    const editUserForm = document.getElementById("editUserForm");
    const closeAddModal = document.querySelector("#addUserModal .close");
    const closeEditModal = document.querySelector("#editUserModal .close");

    let editingUserId = null;

    // Función para obtener usuarios desde el servidor
    function fetchUsers() {
        fetch('http://localhost:5555/usuarios/all')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error al obtener los usuarios');
                }
                return response.json();
            })
            .then(data => {
                renderUserTable(data);
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    // Renderizar la tabla de usuarios
    function renderUserTable(users) {
	    const userCardsContainer = document.getElementById("userCardsContainer");
	    userCardsContainer.innerHTML = "";
	
	    users.forEach(user => {
	        const card = document.createElement("div");
	        card.classList.add("card");
	
	        card.innerHTML = `
	        	 <div class="card-image">
			        <img src="${user.link}" alt="Foto de ${user.name}" class="user-photo" />
			    </div>
	            <div class="card-header">${user.name} ${user.lastname}</div>
	            <div class="card-content">
	                <p><strong>ID:</strong> ${user.id}</p>
	                <p><strong>Dirección:</strong> ${user.address}</p>
	                <p><strong>Rol:</strong> ${user.role}</p>
	            </div>
	            <div class="card-actions">
	                <button class="btn-primary" onclick="editUser(${user.id})">Editar</button>
	                <button class="btn-danger" onclick="deleteUser(${user.id})">Eliminar</button>
	            </div>
	        `;
	
	        userCardsContainer.appendChild(card);
	    });
	}

    // Abrir modal para agregar usuario
    addUserBtn.addEventListener("click", () => {
        addUserModal.style.display = "block";
        addUserForm.reset();
        editingUserId = null;
    });

    // Cerrar modal para agregar usuario
    closeAddModal.addEventListener("click", () => {
        addUserModal.style.display = "none";
    });

    // Cerrar modal para editar usuario
    closeEditModal.addEventListener("click", () => {
        editUserModal.style.display = "none";
    });

    // Función para obtener datos del usuario para editar
    window.editUser = function(id) {
        fetch(`http://localhost:5555/usuarios/get/${id}`)
            .then(response => response.json())
            .then(user => {
                // Rellenar el formulario con los datos del usuario
                document.getElementById("editName").value = user.name;
                document.getElementById("editLastname").value = user.lastname;
                document.getElementById("editAddress").value = user.address;
                document.getElementById("editLink").value = user.link;
                if(user.role === "ADMINISTRADOR")
                {
					document.getElementById("editRole").value = 1;
				}
				else if (user.role === "USUARIO")
				{
					document.getElementById("editRole").value = 2;
				}

                editingUserId = user.id;
                editUserModal.style.display = "block";
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    // Agregar nuevo usuario
    addUserForm.addEventListener("submit", (event) => {
        event.preventDefault();
        const requestData = {
            "name": document.getElementById("addName").value,
            "lastname": document.getElementById("addLastname").value,
            "address": document.getElementById("addAddress").value,
            "username": document.getElementById("addUsername").value,
            "password": document.getElementById("addPassword").value,
            "rol": document.getElementById("addRole").value,
            "link": document.getElementById("addLink").value
        };

        fetch('http://localhost:5555/usuarios/new', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    title: '¡Éxito!',
                    text: 'Usuario agregado correctamente',
                    icon: 'success',
                    confirmButtonText: 'Aceptar'
                }).then(() => {
                    fetchUsers();
                    addUserModal.style.display = "none";
                });
            } else {
                throw new Error('Error al agregar usuario');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    });

    // Actualizar usuario
    editUserForm.addEventListener("submit", (event) => {
        event.preventDefault();
        const requestData = {
            "name": document.getElementById("editName").value,
            "lastname": document.getElementById("editLastname").value,
            "address": document.getElementById("editAddress").value,
            "link": document.getElementById("editLink").value,
            "rol": document.getElementById("editRole").value
        };

        fetch(`http://localhost:5555/usuarios/update/${editingUserId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    title: '¡Éxito!',
                    text: 'Usuario actualizado correctamente',
                    icon: 'success',
                    confirmButtonText: 'Aceptar'
                }).then(() => {
                    fetchUsers();
                    editUserModal.style.display = "none";
                    editingUserId = null; 
                });
            } else {
                throw new Error('Error al actualizar usuario');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    });

    // Función para eliminar usuario
    window.deleteUser = function(id) {
        fetch(`http://localhost:5555/usuarios/delete/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                Swal.fire({
                    title: '¡Éxito!',
                    text: 'Usuario eliminado correctamente',
                    icon: 'success',
                    confirmButtonText: 'Aceptar'
                }).then(() => {
                    fetchUsers();
                });
            } else {
                throw new Error('Error al eliminar usuario');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }

    fetchUsers();
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
