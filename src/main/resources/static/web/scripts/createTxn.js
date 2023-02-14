const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

const createTxnForm = new Vue({
    el: "#createTxnForm",
    data: {
        transaction: {}
    },
    methods: {
        create() {
            const request = {
                "transactionType": $("#transactionType").val(),
                "transactionEntity": urlParams.get('orgId'), // Solamente se llama desde la página principal; las demás se crean a partir de las donaciones.
                "donationRecurrency": $("#recurrency").val(),
                "amount": $("#amount").val()
            }
            $.post("/api/transactions", request)
                .done(function (data) {
                    swal.fire({
                        icon: 'success',
                        title: "Realizada Exitosa",
                        text: "Transacción creada correctamente.",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    let txnId = data.txnId
                    window.location.href = '/web/transaction.html?id=' + txnId;
                })
                .fail(function (data) {
                    swal.fire({
                        icon: 'error',
                        title: "No se pudo realizar",
                        text: "Ocurrió un error creando la transacción.\n" + data.errorMsg,
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