const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

const createOrgForm = new Vue({
    el: "#createOrgForm",
    data: {
        organization: {},
        orgNamesList: [],
        nameTaken: false
    },
    methods: {
        create() {
            const inputName = $("#organizationName").val();
            if(createOrgForm.orgNamesList.includes(inputName.toLowerCase())){
                createOrgForm.nameTaken = true;
            } else {
                const request = {
                    "orgName": inputName,
                    "adminUser": {
                        "username": $("#adminUser").val(),
                        "password": $("#adminPassword").val()
                    }
                }
                $.post("/api/organization", request)
                    .done(function (data) {
                        swal.fire({
                            icon: 'success',
                            title: "Realizada Exitosa",
                            text: "Organización creada correctamente.",
                            showConfirmButton: false,
                            timer: 1500
                        });
                        let donationId = data.orgId
                        window.location.href = '/web/mainPage.html?orgId=' + donationId;
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