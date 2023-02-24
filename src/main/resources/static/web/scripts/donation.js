const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

var donationRecord = new Vue({
    el: "#donationRecord",
    data: {
        status: "",
        donation: {},
        organization: {},
        donor: {},
        recordExists: false,
        hasPermission: false,
        sameOrg: false,
        type: "",
        isRecurrent: false,
        receiptGenerated: false
    },
    methods: {
        cancelar(){
            $.post("/api/donations/"+urlParams.get('id')+"/"+urlParams.get('orgId')+"/cancel")
                .done(function(){
                    window.location.reload();
                })
        },
        confirmarRecepcion(){
            $.post("/api/donations/"+urlParams.get('id')+"/"+urlParams.get('orgId')+"/confirm")
                .done(function(){
                    window.location.reload();
                })
        },
        generarRecibo(){
            $.post("/api/transactions/debt/generate/"+urlParams.get('id'))
                .done(function (data) {
                swal.fire({
                    icon: 'success',
                    title: "Realizada Exitosa",
                    text: "Transacción creada correctamente.",
                    showConfirmButton: false,
                    timer: 1500
                });
                let txnId = data.receiptTransactionId;
                window.location.href = '/web/transaction.html?id=' + txnId + '&orgId=' + urlParams.get('orgId');
            })
        },
        back() {
            window.location.href = '/web/mainPage.html?id=' + urlParams.get('orgId');
        }
    },
    created: function () {
        getDonation()
    }
});

function getDonation(){
    $.get('/api/donation/'+urlParams.get('id'))
        .done(function(data){
            donationRecord.status = data.status;
            donationRecord.donation = data.donation;
            donationRecord.organization = data.organization;
            donationRecord.donor = data.donor;
            donationRecord.recordExists = data.recordExists;
            donationRecord.hasPermission = data.hasPermission;
            donationRecord.isRecurrent = data.isRecurrent;
            donationRecord.type = data.type;
            donationRecord.sameOrg = (data.orgId.toString() === urlParams.get('orgId').toString());
            donationRecord.receiptGenerated = data.receiptGenerated;
            document.title = "Donación " + data.donationNumber;
        })
}