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
            if(createOrgForm.orgNamesList.includes(inputName.toLowerCase())){
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
                    adminUser: {
                        username: $("#adminUser").val(),
                        password: $("#adminPassword").val()
                    }
                }
                $.post("/api/organization/create", request)
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
                    .then(function(){
                        $.post("/api/login", {username: $("#adminUser").val(), password:$("#adminPassword").val()})
                    })
                    .done(function(){
                        window.location.href = '/web/mainPage.html?orgId=' + createOrgForm.redirectOrgId;
                    })
                    .fail(function (data) {
                        swal.fire({
                            icon: 'error',
                            title: "No se pudo realizar",
                            text: "Ocurrió un error creando la organización. \n" + data.message,
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
            createOrgForm.orgNamesList = data.orgNamesList
        })
}