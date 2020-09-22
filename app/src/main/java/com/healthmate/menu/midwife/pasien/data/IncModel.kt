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
        var birth_attendant: String? = "",
        var mode_delivery: String? = "",
        var mother_condition: String? = "",
        var information: String? = ""
)

data class BabyNote(
        var inc_id: String? = "",
        var child_number: String? = "",
        var weight: String? = "",
        var height: String? = "",
        var head: String? = "",
        var gender: String? = "",
        var born_situation: String? = "",
        var apgar: String? = "",
        var asuhan_bayi: ArrayList<String>? = arrayListOf(),
        var information: String? = ""
)