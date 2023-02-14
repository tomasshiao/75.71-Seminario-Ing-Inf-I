const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

const createDonationForm = new Vue({
    el: "#createDonationForm",
    data: {
        donation: {}
    },
    methods: {
        create() {
            const request = {
                "donationType": $("#donationType").val(),
                "donorId": urlParams.get('donorId'),
                "donationRecurrency": $("#recurrency").val(),
                "amount": $("#amount").val()
            }
            $.post("/api/organization", request)
                .done(function (data) {
                    swal.fire({
                        icon: 'success',
                        title: "Realizada Exitosa",
                        text: "Donación creada correctamente.",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    let donationId = data.donationId
                    window.location.href = '/web/donation.html?id=' + donationId;
                })
                .fail(function (data) {
                    swal.fire({
                        icon: 'error',
                        title: "No se pudo realizar",
                        text: "Ocurrió un error creando la donación.\n" + data.errorMsg,
                        showConfirmButton: true,
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'OK'
                    });
                })
        },
        back() {
            window.location.href = '/web/mainPage.html?orgId=' + urlParams.get('orgId');
        }
    },
    created: function () {
    }
});