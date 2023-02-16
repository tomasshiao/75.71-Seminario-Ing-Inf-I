const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

var createEventForm = new Vue({
    el: "#createEventForm",
    data: {
        event: {},
        hours: [],
        mins: [],
        eventOptions: []
    },
    methods: {
        create() {
            let request = {
                eventName: $("#eventName").val(),
                address: $("#address").val(),
                eventType: $("#eventType").val(),
                eventTimeHour: $("#eventTimeHour").val(),
                eventTimeMin: $("#eventTimeMin").val(),
                eventDate: $("#eventDate").val()
            };
            $.post("/api/events/"+urlParams.get('orgId')+"/create", request)
                .done(function (data) {
                    swal.fire({
                        icon: 'success',
                        title: "Realizada Exitosa",
                        text: "Evento creado correctamente.",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    let eventId = data.eventId;
                    window.location.href = '/web/event.html?id=' + eventId + '&orgId=' + urlParams.get('orgId');
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

getEventData();

function getEventData(){
    let hours = [];
    let mins = [];
    for(let i = 0; i < 60; i++){
        mins.push({label: i, value: i});
        if(i < 24){
            hours.push({label: i, value: i})
        }
    }
    createEventForm.hours = hours;
    createEventForm.mins = mins;
    createEventForm.eventOptions = [
        {
            label: "CHARITY",
            value: "Charity"
        },
        {
            label: "FESTIVE",
            value: "Festive"
        },
        {
            label: "INTERNAL",
            value: "Internal"
        }
    ];
}