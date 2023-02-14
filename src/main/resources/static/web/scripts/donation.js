const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

const donationRecord = new Vue({
    el: "#donationRecord",
    data: {
        donation: {},
        organization: {},
        donor: {},
        recordExists: false,
        hasPermission: false
    },
    methods: {
        back() {
            window.location.href = '/web/mainPage.html?orgId=' + urlParams.get('orgId');
        }
    },
    created: function () {
        getDonation()
    }
});

function getDonation(){
    $.get('/api/donation/'+urlParams.get('id'))
        .done(function(data){
            donationRecord.donation = data.donation;
            donationRecord.organization = data.organization;
            donationRecord.donor = data.donor;
            donationRecord.recordExists = data.recordExists;
            donationRecord.hasPermission = data.hasPermission;
        })
}