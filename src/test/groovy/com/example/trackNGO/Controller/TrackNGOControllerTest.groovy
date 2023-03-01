package com.example.trackNGO.Controller

import com.example.trackNGO.Model.Transaction
import com.example.trackNGO.Model.TransactionStatus
import com.example.trackNGO.Model.TransactionType
import com.example.trackNGO.Repositories.OrganizationRepository
import com.example.trackNGO.Repositories.TransactionRepository
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
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
