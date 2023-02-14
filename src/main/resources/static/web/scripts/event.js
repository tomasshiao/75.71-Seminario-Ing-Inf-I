const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

const eventRecord = new Vue({
    el: "#eventRecord",
    data: {
        event: {},
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
        })
}