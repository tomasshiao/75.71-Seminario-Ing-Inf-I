package com.example.trackNGO.Model

enum TransactionStatus {
    PROCESSING,
    DENIED,
    ACCEPTED
}

/**
 * PROCESSING: La transacción está pendiente de realizarse. En una Transacción de tipo DEBT, quiere decir que la deuda sigue impaga.
 * DENIED: Tipo de transacción inválida o transacción denegada.
 * ACCEPTED: Transacción aceptada. En una Transacción de tipo DEBT, quiere decir que la deuda está saldada.
 */