package com.example.trackNGO.Model

enum TransactionType {
    PURCHASE,
    SALE,
    RECEIPT,
    PAYMENT,
    DEBT
}

/**
 * PURCHASE: Compra por parte de la organización. Egresa dinero de la cuenta de la organización.
 * SALE: Venta por parte de la organización. Ingresa dinero a la cuenta de la organización.
 * RECIPT: La organización recibe de una donación. Ingresa dinero a la cuenta de la organización.
 * PAYMENT: La organización dona a otra causa. Egresa dinero de la cuenta de la organización.
 * DEBT: La organización le debe al transactionPerson el amount. Status PROCESSING = impago; ACCEPTED = deuda saldada.
 */