const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

var mainPage = new Vue({
    el: "#mainPage",
    data: {
        organization: {},
        collaborator: {},
        hasPermission: false,
        organizationExists: false,
        takenUsernames: [],
        friends: [],
        events: [],
        transactions: [],
        donations: []
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
                inputLabel: 'Ingrese el usuario en formato de email',
                inputPlaceholder: 'Ingrese el usuario en formato de email',
                validationMessage: "Nombre de usuario inválido, debe ser de formato email.",
                showConfirmButton: true,
                confirmButtonText: "Crear",
                showCancelButton: true,
                cancelButtonText: "Cancelar",
                preConfirm: (email) => {
                    if (mainPage.takenUsernames.includes(email)) {
                        Swal.showValidationMessage("El nombre de usuario ya existe.")
                    }
                }
            })
            if(!(email == null || email === "" || email === undefined)){
                const request = {
                    collaboratorName: email,
                    password: null,
                    passwordConfirmed: false
                }
                $.post("/api/collaborators/" + urlParams.get('id') + "/create", request)
                    .done(function (data) {
                        mainPage.takenUsernames.push(request.collaboratorName);
                        swal.fire({
                            icon: 'success',
                            title: `Usuario ${data.collaborator.fullName} creado`,
                            showConfirmButton: true,
                            html:
                                'El nuevo usuario puede ingresar en ' +
                                '<a href="/web/signUp.html">esta página</a> ' +
                                'para setear su contraseña',
                        });
                    })
            }
        },
        createEvent(){
            window.location.href = '/web/createEvent.html?orgId='+urlParams.get("id");
        },
        async addFriend(){
            const { value: email } = await Swal.fire({
                title: 'Agregar Amigo',
                input: 'email',
                inputLabel: 'Ingrese el usuario',
                inputPlaceholder: 'Ingrese el nombre del amigo en formato Email',
                showCancelButton: true,
                validationMessage: "Nombre inválido, debe ser de formato email."
            })
            if(!(email == null || email === "" || email === undefined)) {
                const request = {
                    friendName: email
                }
                $.post("/api/friends/" + urlParams.get('id') + "/create", request)
                    .done(function (data) {
                        swal.fire({
                            icon: 'success',
                            title: `Amigo ${data.friend.fullName} creado`,
                            showConfirmButton: true
                        });
                        window.location.href = '/web/person.html?type=friend&orgId=' + urlParams.get('id') + "&id=" + data.friend.id;
                    })
                    .fail(function (data) {
                        console.log(data);
                        swal.fire({
                            icon: 'error',
                            title: "No se pudo realizar",
                            text: data.responseJSON.errorMsg,
                            showConfirmButton: true,
                            confirmButtonColor: '#e30c0c',
                            confirmButtonText: 'OK'
                        });
                    })
            }
        },
        createOrgTxn(){
            window.location.href = '/web/createTxn.html?orgId='+urlParams.get("id");
        },
        redirectToRecord(record, id){
            if(record.toLowerCase() === 'friend' || record.toLowerCase() === 'collaborator'){
                window.location.href = '/web/person.html?id=' + id + '&type=' +record+ '&orgId=' + +urlParams.get("id");
            } else {
                window.location.href = '/web/'+record+'.html?id=' + id + '&orgId=' + +urlParams.get("id");
            }
        }
    },
    created: function(){
        getOrgData();
        getCollaboratorNames();
    }
});

function getOrgData(){
    $.get('/api/organization/' + urlParams.get('id'))
        .done(function(data){
            mainPage.organization = data.organization;
            mainPage.hasPermission = data.hasPermission;
            mainPage.organizationExists = data.organizationExists;
            mainPage.collaborator = data.collaborator;
            mainPage.friends = data.friends;
            mainPage.events = data.events;
            mainPage.donations = data.donations;
            mainPage.transactions = data.transactions;
            document.title = data.organization.name;
            console.info("Organization data", data)
        })
}

function getCollaboratorNames(){
    $.get('/api/collaborators/names')
        .done(function(data){
        mainPage.takenUsernames = data.takenUsernames
    })
}