const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

var createTxnForm = new Vue({
    el: "#createTxnForm",
    data: {
        transaction: {},
        transactionOptions: [
            {
                label: "PURCHASE",
                value: "Purchase"
            },
            {
                label: "SALE",
                value: "Sale"
            },
            {
                label: "RECEIPT",
                value: "Receipt"
            },
            {
                label: "PAYMENT",
                value: "Payment"
            }
        ]
    },
    methods: {
        create() {
            let request = {
                transactionType: $("#transactionType").val(),
                transactionEntity: urlParams.get('orgId'),
                amount: $("#transactionAmount").val()
            }
            $.post("/api/transactions/"+urlParams.get('orgId')+"/create", request)
                .done(function (data) {
                    swal.fire({
                        icon: 'success',
                        title: "Realizada Exitosa",
                        text: "Transacción creada correctamente.",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    let txnId = data.transactionId
                    window.location.href = '/web/transaction.html?id=' + txnId + '&orgId=' + urlParams.get('orgId');
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
            window.location.href = '/web/mainPage.html?id=' + urlParams.get('orgId');
        }
    },
    created: function () {}
});