var mainPage = new Vue({
    el: "#mainPage",
    data: {
        organization: {},
        hasPermission: false,
        organizationExists: false,
        takenUsernames: []
    },
    methods: {
        logout(){
            $.post("/api/logout")
                .done(function(){
                    swal.fire({
                        icon:'success',
                        title:"Sesión Cerrada",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    window.location.href="/web/login.html";
                })
                .fail(function(){
                    swal.fire({
                        icon: 'error',
                        title:"Error",
                        text: "No se pudo cerrar sesión.",
                        showConfirmButton: true,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'OK'
                    });
                })
        },
        async addCollaborator(){
            const { value: email } = await Swal.fire({
                title: 'Agregar Colaborador',
                input: 'email',
                inputLabel: 'Ingrese el usuario',
                inputPlaceholder: 'Enter your email address',
                showCancelButton: true,
                validationMessage: "Nombre de usuario inválido, debe ser de formato email.",
                preConfirm: (email) => {
                    if (mainPage.takenUsernames.includes(email)) {
                        Swal.showValidationMessage("El nombre de usuario ya existe.")
                    }
                }
            })
            const request = {
                "collaboratorName": email,
                "password": null
            }
            $.post("/api/collaborators/"+urlParams.get('id')+"/create", request)
                .done(function (data){
                    swal.fire({
                        icon:'success',
                        title: `Usuario ${data.collaborator.fullName} Creado`,
                        showConfirmButton: true,
                        html:
                            'El nuevo usuario puede ingresar en ' +
                            '<a href="/web/signUp.html">esta página</a> ' +
                            'para setear su contraseña',
                    });
            })
        }
    },
    created: function(){
        getOrgData()
    }
});

function getOrgData(){
    $.get('/api/organization/' + urlParams.get('id'))
        .done(function(data){
            mainPage.organization = data.organization
            mainPage.hasPermission = data.hasPermission
            mainPage.organizationExists = data.organizationExists
        })

    $.get("/collaborators/names").done(function(data){
        mainPage.takenUsernames = data.takenUsernames
    })
}