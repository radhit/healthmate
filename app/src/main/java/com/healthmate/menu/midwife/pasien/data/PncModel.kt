package com.healthmate.menu.midwife.pasien.data

data class PncModel(
        var id: String = "",
        var date: String = "",
        var keluhan: String = "",
        var td: Int = 0,
        var nadi: Int = 0,
        var rr: Int = 0,
        var suhu: Int = 0,
        var kontaksi: String = "",
        var tfu: String = "",
        var pendarahan: Int = 0,
        var warna_lokhia: String = "",
        var jumlah_lokhia: Int = 0,
        var produksi_asi: String = "",
        var bab: String = "",
        var bak: String = "",
        var tindakan: String = "",
        var nasihat: String = "",
        var komplikasi_kehamilan: String = "",
        var keadaan_ibu: String = "",
        var keadaan_bayi: String = "",
        var mother_id: String = "",
        var midwife_id: String = ""
)