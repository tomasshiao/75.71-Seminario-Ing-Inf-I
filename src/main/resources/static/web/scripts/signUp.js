const signUp = new Vue({
    el: "#signUp",
    data: {
        validated: false,
        userRegisteredInOrg: false,
        enteredName: "",
        orgId: null,
        collaboratorId: null,
        passwordConfirmed: false
    },
    methods: {
        back(){
            $("#collaboratorNameValidation").val = "";
            $("#collaboratorNameSignUp").val = "";
            $("#password").val = "";
            signUp.validated = false;
            signUp.userRegisteredInOrg = false;
            signUp.enteredName = "";
            signUp.orgId = null;
            signUp.collaboratorId = null;
            signUp.passwordConfirmed = false;
            location.reload();
        },
        signUp(){
            const request = {
                collaboratorId: signUp.collaboratorId,
                orgId: signUp.orgId,
                password: $("#password").val()
            };
            $.post("/api/completeSignUp", request)
                .done(function(data){
                    signUp.collaboratorId = data.collaboratorId;
                    signUp.orgId = data.orgId;
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
                        username: signUp.enteredName,
                        password: $("#password").val()
                    })
                        .done(function(){
                        window.location.href="/web/mainPage.html?id=" + signUp.orgId
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
                    if(data.errorMsg != null){
                        signUp.back()
                    } else {
                        signUp.validated = true;
                        signUp.userRegisteredInOrg = data.userRegisteredInOrg;
                        if (data.userRegisteredInOrg) {
                            signUp.enteredName = nameToValidate;
                        }
                        signUp.orgId = data.orgId;
                        signUp.collaboratorId = data.collaboratorId;
                        signUp.passwordConfirmed = data.passwordConfirmed;
                    }
            })
        }
    },
    created: function(){}
})