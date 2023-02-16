const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

var eventRecord = new Vue({
    el: "#eventRecord",
    data: {
        event: {},
        organization: {},
        recordExists: false,
        hasPermission: false,
        sameOrg: false
    },
    methods: {
        back() {
            window.location.href = '/web/mainPage.html?id=' + urlParams.get('orgId');
        }
    },
    created: function () {
        getEvent()
    }
});

function getEvent(){
    $.get('/api/event/'+urlParams.get('id'))
        .done(function(data){
            eventRecord.event = data.event;
            eventRecord.organization = data.organization;
            eventRecord.recordExists = data.recordExists;
            eventRecord.hasPermission = data.hasPermission;
            eventRecord.sameOrg = (data.orgId.toString() === urlParams.get('orgId').toString());
            document.title = "Evento " + data.eventNumber;
        })
}