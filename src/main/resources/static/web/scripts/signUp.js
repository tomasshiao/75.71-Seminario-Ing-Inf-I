const signUp = new Vue({
    el: "#signUp",
    data: {
        validated: false,
        userRegisteredInOrg: false,
        enteredName: ""
    },
    methods: {
        back(){
            $("#collaboratorNameValidation").val = "";
            $("#collaboratorNameSignUp").val = "";
            $("#password").val = "";
            signUp.validated = false;
            signUp.userRegisteredInOrg = false;
            signUp.enteredName = ""
            location.reload()
        },
        signUp(){
            const request = {
                username: $("#collaboratorNameSignUp").val(),
                password: $("#password").val(),
            };
            $.post("/api/collaborators", request)
                .done(function(){
                    swal.fire({
                        icon:'success',
                        title: "Success.",
                        text:"¡Cuenta creada!",
                        showConfirmButton: false,
                        timer: 1500
                    });
                })
                .then(function(){
                    $.post("/api/login", {
                        username: request.username,
                        password: request.password
                    })
                        .then(function(){
                            $.get("/api/collaborator/organization")
                        })
                        .done(function(data){
                            window.location.href="/web/mainPage.html?id=" + data.orgId
                        })
                })
                .fail(function(data){
                    swal.fire({
                        icon:'error',
                        title:"Error.",
                        text: "No se pudo crear la cuenta." + data.errorMsg,
                        showConfirmButton: true,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'OK'
                    });
                })
        },
        validateName(){
            const nameToValidate = $("#collaboratorNameValidation").val();
            $("#collaboratorNameSignUp").val = nameToValidate;
            $.get("/api/findNewCollaborator/" + nameToValidate)
                .done(function(data){
                signUp.validated = true;
                signUp.userRegisteredInOrg = data.userRegisteredInOrg;
            })
        }
    },
    created: function(){}
})