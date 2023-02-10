package com.example.trackNGO.Model

enum DonationRecurrency {
    ONE_TIME,
    WEEKLY,
    FORTNIGHT,
    MONTHLY,
    BIMONTHLY,
    TRIMONTHLY,
    SEASONAL,
    SEMIANNUALLY,
    ANNUALLY,
    BIENNIALLY
}

/**
 * Las recurrencias aplican solo para donaciones monetarias. Para las donaciones no monetarias aplica solo ONE_TIME.
 * ONE_TIME: Donación de una única vez.
 * WEEKLY: Donación semanal.
 * FORTNIGHT: Donación de cada 15 días.
 * MONTHLY: Donación mensual.
 * BIMONTHLY: Donación bimestral.
 * TRIMONTHLY: Donación trimestral.
 * SEASONAL: Se escribió "estacional" en el informe. Sino sería QUARTERLY, cada 4 meses.
 * SEMIANNUALLY: Donación semianual, es decir, cada 6 meses.
 * ANNUALLY: Donación anual.
 * BIENNIALLY: Donación cada dos años.
 * */