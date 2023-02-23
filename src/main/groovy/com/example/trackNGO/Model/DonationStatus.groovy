package com.example.trackNGO.Model

enum DonationStatus {
    PENDING,
    NOT_APPLICABLE,
    ACTIVE,
    INACTIVE,
    SUSPENDED
}

/**
 * NOT_APPLICABLE: La donación es de una sola vez.
 * ACTIVE: La donación recurrente se encuentra activa.
 * INACTIVE: El/La donante solicita la baja de la donación.
 * SUSPENDED: El/La donante solicita pausar la recurrencia.
 * */
