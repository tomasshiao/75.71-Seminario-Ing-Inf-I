var app = new Vue({
    el: "#app",
    data: {
        collaborator: ""
    },
    methods: {
        login(){
            var request = {
                username: $("#collaboratorName").val(),
                password: $("#password").val()
            };
            $.post("/api/login", {
                username: request.username,
                password: request.password
            })
                .then(function(){
                    $.get("/api/collaborator/organization")
                }).done(function(data){
                    window.location.href="/web/mainPage.html?id=" + data.orgId
                })
                .fail(function() {
                    console.log("login failed");
                    swal.fire({
                        icon: 'error',
                        title: "Error.",
                        text: "Login failed.",
                        showConfirmButton: true,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'OK'
                    });
                })
        }
    },
    created: function(){}
});