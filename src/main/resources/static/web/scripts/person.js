const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

const personRecord = new Vue({
    el: "#personRecord",
    data: {
        person: {},
        organization: {},
        recordExists: false,
        hasPermission: false
    },
    methods: {
        back() {
            window.location.href = '/web/mainPage.html?orgId=' + urlParams.get('orgId');
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
        })
}