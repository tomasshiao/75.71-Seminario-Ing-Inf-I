const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

var createEventForm = new Vue({
    el: "#createEventForm",
    data: {
        event: {},
        eventOptions: []
    },
    methods: {
        create() {
            let hora = $("#hora").val();
            console.log(hora);
            let request = {
                eventName: $("#eventName").val(),
                address: $("#address").val(),
                eventType: $("#eventType").val(),
                eventTimeHour: hora.substring(0, 2),
                eventTimeMin: hora.substring(3,5),
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
                .fail(function () {
                    swal.fire({
                        icon: 'error',
                        title: "No se pudo realizar",
                        text: "Ocurrió un error creando la transacción.",
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