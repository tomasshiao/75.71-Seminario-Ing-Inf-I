package com.example.trackNGO.Controller

import com.example.trackNGO.Model.Donation
import com.example.trackNGO.Model.DonationRecurrency
import com.example.trackNGO.Model.DonationStatus
import com.example.trackNGO.Model.DonationType
import com.example.trackNGO.Model.Transaction
import com.example.trackNGO.Model.TransactionStatus
import com.example.trackNGO.Model.TransactionType
import com.example.trackNGO.Repositories.DonationRepository
import com.example.trackNGO.Repositories.OrganizationRepository
import com.example.trackNGO.Repositories.TransactionRepository
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = [
                "spring.security.user.name=admin@admin.com",
                "spring.security.user.password=admin123",
                "spring.security.user.roles=ADMIN"
        ]
)
@AutoConfigureMockMvc
class TrackNGOControllerTest {
    @LocalServerPort
    private Integer port

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private OrganizationRepository organizationRepository

    @Autowired
    private TransactionRepository transactionRepository

    @Autowired
    private DonationRepository donationRepository

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc)
    }

    // Tests de las US de la sección 5
    @Test
    void whenPurchaseAmountIsLessThanBalance_thenPurchaseIsSuccessful() throws Exception{
        Long id = RestAssuredMockMvc
            .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("transactionType","PURCHASE", "amount", "100", "transactionEntity", "2")
            .when()
                .post("/api/transactions/{orgId}/create", 2L)
                .then()
                .statusCode(201)
                .body(Matchers.containsString("transactionId"))
                .extract().body().jsonPath().getLong("transactionId")
        Transaction txn = transactionRepository.findById(id).orElse(null)
        assert(txn.getStatus() == TransactionStatus.ACCEPTED)
    }

    @Test
    void whenPurchaseAmountIsMoreThanBalance_thenPurchaseIsDenied(){
       Long id = RestAssuredMockMvc
            .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("transactionType","PURCHASE", "amount", "10000000000", "transactionEntity", "2")
            .when()
                .post("/api/transactions/{orgId}/create", 2L)
            .then()
                .statusCode(201)
                .body(Matchers.containsString("transactionId"))
            .extract().body().jsonPath().getLong("transactionId")
        Transaction txn = transactionRepository.findById(id).orElse(null)
        assert(txn.getStatus() == TransactionStatus.DENIED)
    }

    @Test
    void whenPaymentAmountIsLessThanBalance_thenPaymentIsSuccessful(){
        Long id = RestAssuredMockMvc
            .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("transactionType","PAYMENT", "amount", "23", "transactionEntity", "2")
            .when()
                .post("/api/transactions/{orgId}/create", 2L)
                .then()
                .statusCode(201)
                .body(Matchers.containsString("transactionId"))
                .extract().body().jsonPath().getLong("transactionId")
        Transaction txn = transactionRepository.findById(id).orElse(null)
        assert(txn.getStatus() == TransactionStatus.ACCEPTED)
    }

    @Test
    void whenPaymentAmountIsMoreThanBalance_thenItIsAcceptedAndDebtCreated(){
        Long id = RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("transactionType","PAYMENT", "amount", "8000000000", "transactionEntity", "2")
                .when()
                .post("/api/transactions/{orgId}/create", 2L)
                .then()
                .statusCode(201)
                .body(Matchers.containsString("transactionId"))
                .extract().body().jsonPath().getLong("debtTransactionId")
        Transaction txn = transactionRepository.findById(id).orElse(null)
        assert(txn.getStatus() == TransactionStatus.PROCESSING)
        assert(txn.getTxnType() == TransactionType.DEBT)
    }

    // Test de las US de la sección 6
    @Test
    void whenDonationReceived_thenRecurrencyIsExplicit(){
        Long id = RestAssuredMockMvc
            .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("donationType","MONETARY", "donorId", "2", "donationRecurrency", "MONTHLY", "amount", "100")
            .when()
                .post("/api/donations/{orgId}/create", 2L)
            .then()
                .statusCode(201)
                .body(Matchers.containsString("donationId"))
                .extract().body().jsonPath().getLong("donationId")
        Donation donation = donationRepository.findById(id).orElse(null)
        assert(donation.getDonationType() == DonationType.MONETARY)
        assert(donation.getRecurrencyType() == DonationRecurrency.MONTHLY)
    }

    @Test
    void whenDonationIsRecurrent_thenRecurrencyPeriodIsAmongAccepted(){
        List<DonationRecurrency> accepted = DonationRecurrency.values()
        Long id = RestAssuredMockMvc
            .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("donationType","MONETARY", "donorId", "2", "donationRecurrency", "BIENNIALLY", "amount", "100")
            .when()
                .post("/api/donations/{orgId}/create", 2L)
            .then()
                .statusCode(201)
                .body(Matchers.containsString("donationId"))
                .extract().body().jsonPath().getLong("donationId")
        Donation donation = donationRepository.findById(id).orElse(null)
        assert(donation.getRecurrencyType() == DonationRecurrency.BIENNIALLY)
        assert(accepted.contains(donation.getRecurrencyType()))
    }

    @Test
    void whenDonationIsMonetary_thenRecurrencyPeriodIsShown(){
        Long id = RestAssuredMockMvc
            .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("donationType","MONETARY", "donorId", "2", "donationRecurrency", "SEASONAL", "amount", "100")
            .when()
                .post("/api/donations/{orgId}/create", 2L)
            .then()
                .statusCode(201)
                .body(Matchers.containsString("donationId"))
                .extract().body().jsonPath().getLong("donationId")
        Donation donation = donationRepository.findById(id).orElse(null)
        assert(donation.getDonationType() == DonationType.MONETARY)
        assert(donation.getRecurrencyType() == DonationRecurrency.SEASONAL)
    }

    @Test
    void whenDonationIsMonetary_thenIsActiveOnceItIsConfirmed(){
        Long id = RestAssuredMockMvc
            .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("donationType","MONETARY", "donorId", "2", "donationRecurrency", "MONTHLY", "amount", "100")
            .when()
                .post("/api/donations/{orgId}/create", 2L)
            .then()
                .statusCode(201)
                .body(Matchers.containsString("donationId"))
                .extract().body().jsonPath().getLong("donationId")
        Donation donation = donationRepository.findById(id).orElse(null)
        assert(donation.getDonationType() == DonationType.MONETARY)
        assert(donation.getStatus() == DonationStatus.PENDING)

        RestAssuredMockMvc
            .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("donationType","MONETARY", "donorId", "2", "donationRecurrency", "MONTHLY", "amount", "100")
            .when()
                .post("/api/donations/{id}/{orgId}/confirm", id, 2L)
            .then()
                .statusCode(202)
        donation = donationRepository.findById(id).orElse(null)
        assert(donation.getDonationType() == DonationType.MONETARY)
        assert(donation.getStatus() == DonationStatus.ACTIVE)
    }

    @Test
    void whenDonationIsMaterial_thenItMustBeDistinguishableAmongAcceptedCategories(){
        List<DonationType> acceptedCategories = DonationType.values()
        Long id = RestAssuredMockMvc
            .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("donationType","MATERIAL_GOODS", "donorId", "2", "donationRecurrency", "NOT_APPLICABLE", "amount", "0")
            .when()
                .post("/api/donations/{orgId}/create", 2L)
            .then()
                .statusCode(201)
                .body(Matchers.containsString("donationId"))
                .extract().body().jsonPath().getLong("donationId")
        Donation donation = donationRepository.findById(id).orElse(null)
        assert(donation.getDonationType() == DonationType.MATERIAL_GOODS)
        assert(donation.getRecurrencyType() == DonationRecurrency.ONE_TIME)
        assert(acceptedCategories.contains(donation.getDonationType()))
    }

    @Test
    void whenDonationIsFood_thenItMustBeDistinguishableBetweenPerishableAndNonPerishable_testPerishable(){
        List<DonationType> acceptedCategories = DonationType.values()
        Long id = RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("donationType","PERISHABLE_FOOD", "donorId", "2", "donationRecurrency", "NOT_APPLICABLE", "amount", "0")
                .when()
                .post("/api/donations/{orgId}/create", 2L)
                .then()
                .statusCode(201)
                .body(Matchers.containsString("donationId"))
                .extract().body().jsonPath().getLong("donationId")
        Donation donation = donationRepository.findById(id).orElse(null)
        assert(donation.getDonationType() == DonationType.PERISHABLE_FOOD)
        assert(donation.getRecurrencyType() == DonationRecurrency.ONE_TIME)
        assert(acceptedCategories.contains(donation.getDonationType()))
    }

    @Test
    void whenDonationIsFood_thenItMustBeDistinguishableBetweenPerishableAndNonPerishable_testNonPerishable(){
        List<DonationType> acceptedCategories = DonationType.values()
        Long id = RestAssuredMockMvc
                .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("donationType","NON_PERISHABLE_FOOD", "donorId", "2", "donationRecurrency", "NOT_APPLICABLE", "amount", "0")
                .when()
                .post("/api/donations/{orgId}/create", 2L)
                .then()
                .statusCode(201)
                .body(Matchers.containsString("donationId"))
                .extract().body().jsonPath().getLong("donationId")
        Donation donation = donationRepository.findById(id).orElse(null)
        assert(donation.getDonationType() == DonationType.NON_PERISHABLE_FOOD)
        assert(donation.getRecurrencyType() == DonationRecurrency.ONE_TIME)
        assert(acceptedCategories.contains(donation.getDonationType()))
    }

    @Test
    void whenDonationIsRecurrent_thenAnyoneCanOptOutAndCancelDonation(){
        Long id = RestAssuredMockMvc
            .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("donationType","MONETARY", "donorId", "2", "donationRecurrency", "MONTHLY", "amount", "100")
            .when()
                .post("/api/donations/{orgId}/create", 2L)
            .then()
                .statusCode(201)
                .body(Matchers.containsString("donationId"))
                .extract().body().jsonPath().getLong("donationId")
        Donation donation = donationRepository.findById(id).orElse(null)
        assert(donation.getDonationType() == DonationType.MONETARY)
        assert(donation.getStatus() == DonationStatus.PENDING)

        RestAssuredMockMvc
            .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("donationType","MONETARY", "donorId", "2", "donationRecurrency", "MONTHLY", "amount", "100")
            .when()
                .post("/api/donations/{id}/{orgId}/confirm", id, 2L)
            .then()
                .statusCode(202)
        donation = donationRepository.findById(id).orElse(null)
        assert(donation.getDonationType() == DonationType.MONETARY)
        assert(donation.getStatus() == DonationStatus.ACTIVE)

        RestAssuredMockMvc
            .given()
                .auth().with(SecurityMockMvcRequestPostProcessors.user("admin@admin.com").authorities(AuthorityUtils.createAuthorityList("ADMIN")))
                .params("donationType","MONETARY", "donorId", "2", "donationRecurrency", "MONTHLY", "amount", "100")
            .when()
                .post("/api/donations/{id}/{orgId}/cancel", id, 2L)
            .then()
                .statusCode(202)
        donation = donationRepository.findById(id).orElse(null)
        assert(donation.getDonationType() == DonationType.MONETARY)
        assert(donation.getStatus() == DonationStatus.INACTIVE)
    }
}
