package com.healthmate.menu.midwife.pasien.data

data class PncModel(
        var id: String = "",
        var date: String = "",
        var subjective: String = "",
        var td: Int = 0,
        var nadi: Int = 0,
        var rr: Int = 0,
        var suhu: Int = 0,
        var bab: String = "",
        var bak: String = "",
        var kontaksi: String = "",
        var pendarahan: String = "",
        var warna_lokhia: String = "",
        var jumlah_lokhia: String = "",
        var produksi_asi: String = "",
        var komplikasi_kehamilan: String = "",
        var tindakan: String = "",
        var nasihat: String = "",
        var mother_id: String = "",
        var midwife_id: String = ""
)