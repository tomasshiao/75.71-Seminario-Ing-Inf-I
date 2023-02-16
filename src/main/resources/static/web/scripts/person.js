const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

var personRecord = new Vue({
    el: "#personRecord",
    data: {
        person: {},
        organization: {},
        recordExists: false,
        hasPermission: false,
        sameOrg: false
    },
    methods: {
        createDonation(){
            window.location.href = '/web/createDonation.html?orgId=' + urlParams.get('orgId') + '&donorId=' + personRecord.person.id;
        },
        back() {
            window.location.href = '/web/mainPage.html?id=' + urlParams.get('orgId');
        }
    },
    created: function () {
        getPerson()
    }
});

function getPerson(){
    $.get('/api/person/'+urlParams.get('type')+'/'+urlParams.get('id'))
        .done(function(data){
            personRecord.person = data.person;
            personRecord.organization = data.organization;
            personRecord.recordExists = data.recordExists;
            personRecord.hasPermission = data.hasPermission;
            personRecord.sameOrg = (data.orgId.toString() === urlParams.get('orgId').toString());
        })
}