package com.healthmate.menu.midwife.pasien.data

data class IncKalaModel(
        var id: String = "",
        var date: String = "",
        var time: String = "",
        var complaint: String = "",
        var awareness: String = "",
        var td: Int = 0,
        var pulse: Int = 0,
        var rr: Int = 0,
        var temperature: Int = 0,
        var tfu: Int = 0,
        var djj: ArrayList<Int> = arrayListOf(),
        var his: Int = 0,
        var amniotic_fluid: String = "",
        var vt: Int = 0,
        var effacement: Int = 0,

        //kala2
        var warna_ketuban: String = "",
        var presentasi: String = "",
        var denominator: String = "",
        var tali_pusar: String = "",
        var pernium_menonjol: String = "",
        var vulva_vagina: String = "",

        //kala3 & 4
        var kontrasi_uterus: String = "",
        var kandung_kemih: String = "",
        var teraba_janin_kedua: String = "",
        var pendarahan: Int = 0,
        var laseri: String = "",

        var analisys: String = "",
        var penatalaksaan: String = "",
        var type: String = "",
        var mother_id: String = "",
        var midwife_id: String = ""
)