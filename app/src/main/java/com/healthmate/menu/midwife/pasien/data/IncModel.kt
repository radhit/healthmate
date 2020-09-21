package com.healthmate.menu.midwife.pasien.data

data class IncModel(
        var id: String = "",
//        var baby_note: BabyNote? = BabyNote(),
//        var summary: Summary? = Summary(),
        var kala_1: ArrayList<IncKalaModel> = arrayListOf(),
        var kala_2: ArrayList<IncKalaModel> = arrayListOf(),
        var kala_3: ArrayList<IncKalaModel> = arrayListOf(),
        var kala_4: ArrayList<IncKalaModel> = arrayListOf(),
        var created_time: String = "",
        var updated_time: String = ""

)

data class Summary(
        var id: String = ""
)

data class BabyNote(
        var id: String = ""
)