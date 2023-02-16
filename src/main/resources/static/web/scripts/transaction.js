const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

var transactionRecord = new Vue({
    el: "#transactionRecord",
    data: {
        transaction: {},
        organization: {},
        transactionPerson: {},
        recordExists: false,
        hasPermission: false
    },
    methods: {
        back() {
            window.location.href = '/web/mainPage.html?id=' + urlParams.get('orgId');
        }
    },
    created: function () {
        getTransaction()
    }
});

function getTransaction(){
    $.get('/api/transaction/'+urlParams.get('id'))
        .done(function(data){
            transactionRecord.transaction = data.transaction;
            transactionRecord.organization = data.organization;
            transactionRecord.transactionPerson = data.transactionPerson;
            transactionRecord.recordExists = data.recordExists;
            transactionRecord.hasPermission = data.hasPermission;
        })
}