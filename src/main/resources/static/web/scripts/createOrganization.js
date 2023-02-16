const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

var createOrgForm = new Vue({
    el: "#createOrgForm",
    data: {
        organization: {},
        orgNamesList: [],
        nameTaken: false,
        redirectOrgId: null
    },
    methods: {
        create() {
            const inputName = $("#organizationName").val();
            const adminUsername = $("#adminUser").val();
            const adminPassword = $("#adminPassword").val();
            if(inputName == null || inputName === "" || adminUsername == null || adminUsername === "" || adminPassword === "" || adminPassword == null){
                swal.fire({
                    icon: 'error',
                    title: 'Campos Incompletos',
                    text: "Todos los campos del formulario son obligatorios.",
                    showConfirmButton: true
                })
            } else if(createOrgForm.orgNamesList.includes(inputName.toLowerCase())){
                createOrgForm.nameTaken = true;
                swal.fire({
                    icon: 'error',
                    title: 'Nombre en uso',
                    text: "Este nombre ya se encuentra en uso.",
                    showConfirmButton: true
                })
            } else {
                const request = {
                    orgName: inputName,
                    adminUserName: adminUsername,
                    adminPassword: adminPassword
                }
                $.post("/api/organizations/create", request)
                    .then(function (data) {
                        swal.fire({
                            icon: 'success',
                            title: "Realizada Exitosa",
                            text: "Organización creada correctamente.",
                            showConfirmButton: false,
                            timer: 1500
                        });
                        createOrgForm.redirectOrgId = data.orgId;
                    })
                    .then(function () {
                        const loginRequest = {
                            username: $("#adminUser").val(),
                            password: $("#adminPassword").val()
                        }
                        $.post("/api/login", loginRequest)
                        .done(function () {
                            window.location.href = '/web/mainPage.html?id=' + createOrgForm.redirectOrgId;
                        })
                    })
                    .fail(function() {
                        swal.fire({
                            icon: 'error',
                            title: "No se pudo realizar",
                            text: "Ocurrió un error creando la organización.",
                            showConfirmButton: true,
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: 'OK'
                        });
                    })
            }
        },
        back() {
            window.location.href = '/web/login.html';
        }
    },
    created: function () {
        getOrgNamesList()
    }
});

function getOrgNamesList(){
    $.get("/api/organizations/names")
        .done(function(data){
            createOrgForm.orgNamesList = data.takenOrgNames;
        })
}