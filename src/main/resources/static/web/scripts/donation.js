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
        isRecurrent: false
    },
    methods: {
        cancelar(){
            $.post("/api/donations/"+urlParams.get('id')+"/"+urlParams.get('orgId')+"/cancel")
                .done(function(){
                    window.location.reload();
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
        })
}