package com.healthmate.menu.midwife.pasien.data

data class IncModel(
        var id: String = "",
        var baby_note: BabyNote? = BabyNote(),
        var summary: Summary? = Summary(),
        var kala_1: ArrayList<IncKalaModel> = arrayListOf(),
        var kala_2: ArrayList<IncKalaModel> = arrayListOf(),
        var kala_3: ArrayList<IncKalaModel> = arrayListOf(),
        var kala_4: ArrayList<IncKalaModel> = arrayListOf(),
        var created_time: String = "",
        var updated_time: String = ""

)

data class Summary(
        var inc_id: String? = "",
        var date_birth: String? = "",
        var time_birth: String? = "",
        var age_pregnancy: String? = "",
        var helper: String? = "",
        var born_method: String? = "",
        var mother_condition: String? = "",
        var information: String? = ""
)

data class BabyNote(
        var inc_id: String? = "",
        var child_number: Int? = 0,
        var weight: Int? = 0,
        var height: Int? = 0,
        var lila: Int? = 0,
        var gender: String? = "",
        var baby_condition: String? = "",
        var apgar: Int? = 0,
        var asuhan_bayi: ArrayList<String>? = arrayListOf(),
        var information: String? = ""
)