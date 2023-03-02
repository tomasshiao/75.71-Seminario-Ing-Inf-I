const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

var createDonationForm = new Vue({
    el: "#createDonationForm",
    data: {
        donation: {},
        donationOptions: [
            {
                label: "MATERIAL GOODS",
                value: "Material_Goods"
            },
            {
                label: "PERISHABLE FOOD",
                value: "Perishable_Food"
            },
            {
                label: "NON_PERISHABLE_FOOD",
                value: "Non_Perishable_Food"
            },
            {
                label: "MONETARY",
                value: "Monetary"
            },
            {
                label: "OTHER",
                value: "Other"
            }
        ],
        recurrencyOptions: [
            {
                label: "ONE TIME",
                value: "One_Time"
            },
            {
                label: "WEEKLY",
                value: "Weekly"
            },
            {
                label: "FORTNIGHT",
                value: "Fortnight"
            },
            {
                label: "MONTHLY",
                value: "Monthly"
            },
            {
                label: "BIMONTHLY",
                value: "Bimonthly"
            },
            {
                label: "TRIMONTHLY",
                value: "Trimonthly"
            },
            {
                label: "SEASONAL",
                value: "Seasonal"
            },
            {
                label: "ANNUALLY",
                value: "Annually"
            },
            {
                label: "SEMIANNUALLY",
                value: "Semiannually"
            },
            {
                label: "BIENNIALLY",
                value: "Biennially"
            },
            {
                label: "NOT_APPLICABLE",
                value: "Not_Applicable"
            }
        ],
        type: ""
    },
    methods: {
        updateType(event){
            console.info("Selected value -> ", event.target.value);
            createDonationForm.type = event.target.value;
            if(event.target.value !== "Monetary"){
                $("#recurrency").val = "Not_Applicable";
                $("#amount").val = 0;
            }
        },
        create() {
            let selectedRecurrency = $("#recurrency").val()
            let donationRecurrency = (selectedRecurrency == null) ? "Not_Applicable" : selectedRecurrency;
            let enteredAmount = $("#amount").val()
            let requestAmount = (isNaN(parseInt(enteredAmount)) || enteredAmount === "" || enteredAmount == null) ? 0 : parseInt(enteredAmount)
            let request = {
                donationType: $("#donationType").val(),
                donorId: urlParams.get('donorId'),
                donationRecurrency: donationRecurrency,
                amount: requestAmount
            }
            $.post("/api/donations/"+urlParams.get('orgId')+"/create", request)
                .done(function (data) {
                    swal.fire({
                        icon: 'success',
                        title: "Realizada Exitosa",
                        text: "Donación creada correctamente.",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    let donationId = data.donationId
                    window.location.href = '/web/donation.html?id=' + donationId + '&orgId=' + urlParams.get('orgId');
                })
                .fail(function (data) {
                    swal.fire({
                        icon: 'error',
                        title: "No se pudo realizar",
                        text: "Ocurrió un error creando la donación.\n" + data.errorMsg,
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
