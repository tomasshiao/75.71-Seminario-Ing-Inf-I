const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

var createDonationForm = new Vue({
    el:"#createDonationForm",
    data: {
        "donation": {}
    },
    methods: {
        create(){
            var postUri = "/api/donations/" + $("#donationType").val() + "/" + urlParams.get('donor') + "/" + $("#recurrency").val() + "/" + $("#amount").val();
            $.post(postUri)
                .done(function(data){
                    swal.fire({
                        icon: 'success',
                        title: "Realizada Exitosa",
                        text: "Donación creada correctamente.",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    window.location.href = '/api/donation/' + data.donationId;
                })
                .fail(function() {
                    swal.fire({
                        icon: 'error',
                        title: "No se pudo realizar",
                        text: "Ocurrió un error creando la donación.",
                        showConfirmButton: true,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'OK'
                    });
                })
        },
        back(){
            window.location.href = '/web/mainPage.html';
        }
    },
    created: function(){}
})