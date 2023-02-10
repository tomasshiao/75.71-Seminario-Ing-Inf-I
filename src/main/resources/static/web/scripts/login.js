var app = new Vue({
    el: "#app",
    data: {
        user: ""
    },
    methods: {
        login(){
            var request = {
                username: $("#username").val(),
                password: $("#password").val()
            };
            $.post("/api/login", request)
                .done(function() {
                    location.reload();
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
        signup(){
            var request = {
                userName: $("#username").val(),
                password: $("#password").val()
            };
            $.post("/api/collaborators", request)
                .done(function(){
                    swal.fire({
                        icon:'success',
                        title: "Success.",
                        text:"Account created!",
                        showConfirmButton: false,
                        timer: 1500
                    });
                })
                .then(function(){
                    $.post("/api/login", {
                        username: request.userName, 
                        password: request.password
                    })
                        .done(function() {
                            location.reload();
                        })
                })
                .fail(function(){
                    swal.fire({
                        icon:'error',
                        title:"Error.",
                        text: "Try something else.",
                        showConfirmButton: true,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'OK'
                    });
                })
        },
        logout(){
            $.post("/api/logout")
                .done(function(){
                    swal.fire({
                        icon:'success',
                        title:"You're now logged out. Come back soon!",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    location.reload();
                    $("#username").val("");
                    $("#password").val("");
                })
                .fail(function(){
                    swal.fire({
                        icon: 'error',
                        title:"Error.",
                        text: "Could not log you out, please try again.",
                        showConfirmButton: true,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'OK'
                    });
                })
        }
    },
    created: function(){}
});