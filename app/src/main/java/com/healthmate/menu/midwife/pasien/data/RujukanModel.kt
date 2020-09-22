package com.healthmate.menu.midwife.pasien.data

data class RujukanModel(
        var date: String = "",
        var time: String = "",
        var name: String = "",
        var cause: String = "",
        var dx: String = "",
        var action: String = ""
)

data class UmpanBalikRujukan(
        var date: String = "",
        var time: String = "",
        var name: String = "",
        var dx: String = "",
        var action: String = "",
        var advice: String = ""
)