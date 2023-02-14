var mainPage = new Vue({
    el: "#mainPage",
    data: {
        organization: {},
        hasPermission: false,
        organizationExists: false
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
}