package com.healthmate.menu.mom.covid.data

import java.io.StringReader

data class ScreeningCovidAnswer(
        var finished_quarantine: Boolean = false,
        var heavy_breath: Boolean = false,
        var have_pcr: Boolean = false,
        var sore_throat: Boolean = false,
        var going_to_covid_area: Boolean = false,
        var contact_with_covid: Boolean = false,
        var medical_without_apd: Boolean = false
)