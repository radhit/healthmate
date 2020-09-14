package com.healthmate.menu.midwife.pasien.data

import com.google.gson.annotations.SerializedName

data class HistoryAncsModel(
        var hpht: String = "",
        var hml: String = "",
        var preg_num: String = "",
        var labor_num: String = "",
        var miscarriage_num: String = "",
        var live_child_num: String = "",
        var prev_child_difference: String = ""
)