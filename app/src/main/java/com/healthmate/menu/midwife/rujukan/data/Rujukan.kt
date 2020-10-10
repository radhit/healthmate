package com.healthmate.menu.midwife.rujukan.data

import com.healthmate.menu.reusable.data.Hospital
import com.healthmate.menu.reusable.data.User

data class Rujukan(
        var id: String = "",
        var status: String = "",
        var datetime: String = "",
        var perujuk: Midwife = Midwife(),
        var asal_perujuk: Hospital = Hospital(),
        var sebab: String = "",
        var diagnosis: String = "",
        var tindakan: String = "",
        var tujuan: Hospital = Hospital(),
        var mother: User = User(),
        var umpanbalik_rujukan: UmpanbalikRujukan? = UmpanbalikRujukan()
)

data class UmpanbalikRujukan(
        var id: String = "",
        var datetime: String = "",
        var penerima: Midwife = Midwife(),
        var asal_penerima: Hospital = Hospital(),
        var diagnosis: String = "",
        var anjuran: String = ""
)

data class Midwife(
        var id: String = "",
        var name: String = ""
)