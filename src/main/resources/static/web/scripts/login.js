let app = new Vue({
    el: "#app",
    data: {
        collaborator: ""
    },
    methods: {
        login(){
            var request = {
                username: $("#username").val(),
                password: $("#password").val()
            };
            $.post("/api/login", request)
                .then(function(data0){
                    console.info("data GET", data0);
                    $.get("/api/collaborator/organization", {username: $("#username").val()})
                        .done(function(data){
                            console.info("data GET", data);
                            window.location.href="/web/mainPage.html?id=" + data.orgId;
                    }).fail(function(data){
                        console.log(data);
                        swal.fire({
                            icon: 'error',
                            title: "Error.",
                            text: "Ocurrió un error",
                            showConfirmButton: true,
                            confirmButtonColor: '#3085d6',
                            confirmButtonText: 'OK'
                        })
                    })
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
        },
        createOrg(){
            window.location.href = '/web/createOrg.html';
        }
    },
    created: function(){}
});