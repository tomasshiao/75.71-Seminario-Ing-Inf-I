package com.example.trackNGO.Controller

import com.example.trackNGO.Model.Organization
import com.example.trackNGO.Repositories.OrganizationRepository
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

@WebMvcTest(TrackNGOController.class)
class TrackNGOControllerTest {
    // Tests de las US de la sección 5
    @Test
    void whenPurchaseAmountIsLessThanBalance_thenPurchaseIsSuccessful() throws Exception{

    }

    @Test
    void whenPurchaseAmountIsMoreThanBalance_thenPurchaseIsDenied(){

    }

    @Test
    void whenPaymentAmountIsLessThanBalance_thenPaymentIsSuccessful(){

    }

    @Test
    void whenPaymentAmountIsMoreThanBalance_thenItIsAcceptedAndDebtCreated(){

    }

    // Test de las US de la sección 6
    @Test
    void whenDonationReceived_thenRecurrencyIsExplicit(){

    }

    @Test
    void whenDonationIsRecurrent_thenRecurrencyPeriodIsAmongAccepted(){

    }

    @Test
    void whenDonationIsMonetary_thenRecurrencyPeriodIsShown(){

    }

    @Test
    void whenDonationIsMonetary_thenIsActiveOnceItIsConfirmed(){

    }

    @Test
    void whenDonationIsMaterial_thenItMustBeDistinguishableAmongAcceptedCategories(){

    }

    @Test
    void whenDonationIsFood_thenItMustBeDistinguishableBetweenPerishableAndNonPerishable(){

    }

    @Test
    void whenDonationIsRecurrent_thenAnyoneCanOptOutAndCancelDonation(){

    }
}
