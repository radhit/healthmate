package com.healthmate.menu.reusable.data

import com.google.gson.annotations.SerializedName

data class User(
        var type: String = "",
        var token: String = "",
        var id: String = "",
        var validated: Boolean = true,
        var phone_number: String = "",
        var password: String? = "",
        var name: String = "",
        var covid_checked: Boolean = false,
        var covid_status: String = "",
        var hospital: Hospital? = Hospital(),
        var profile_picture: String? = "",
        var age: Int? = 0,
        var kia: Kia? = Kia(),
        var city: Location? = Location(),
        var district: Location? = Location(),
        var diagnostic_color: String? = "",
        var str_number: String? = "",
        var anc_history: AncHistory = AncHistory(),
        var return_date: String? = ""
)

data class AncHistory(
        var id: String = "",
        var hpht: String? = "",
        var hpl: String? = "",

        var lila: Double? = 0.0,
        var lila_status: String? = "",
        var height: Double? = 0.0,
        var kontrasepsi: String? = "",
        var riwayat_penyakit: ArrayList<String> = arrayListOf(),
        var alergi_obat: String? = "",
        var alergi_lain: String? = "",

//        var preg_num: String? = "",
//        var labor_num: String? = "",
//        var miscarriage_num: String? = "",
        var g: Int? = 0,
        var p: Int? = 0,
        var a: Int? = 0,
        var live_child_num: Int? = 0,

        var dead_child_num: Int? = 0,
        var less_month_child_num: Int? = 0,

        var prev_child_month: Int? = 0,
        var prev_child_year: Int? = 0,

        var status_imunisasi: String? = "",
        var date_imunisasi: String? = "",
        var helper: String? = "",
        var born_method: String? = ""
)
data class Hospital(
        var id: String = "",
        var name: String = "",
        var level: String = "",
        var subdistrict: String = "",
        var district: String = "",
        var city: String = ""
)

data class Location(
        var id : String = "",
        var name: String = "",
        var level: String = "",
        var address: String = "",
        var created_time: String = "",
        var updated_time: String = ""
)

data class Kia(
        var domisili: String = "",
        var nik: String = "",
        var birth_place: String = "",
        var birth_date: String = "",
        var number_of_pregnancy: String = "",
        var last_child_age: String = "",
        var last_education: String = "",
        var job: String = "",
        var religion: String = "",
        var jkn_number: String = "",
        var blood_type: String = "",
        var husband: Husband? = Husband(),
        var persalinan: Persalinan? = Persalinan()
)

data class Persalinan(
        var helper: String = "",
        var funds: String = "",
        var vehicle: String = "",
        var metode_kb: String = "",
        var pendonor: String = "",
        var kontak_pendonor: String = ""
)

data class Husband(
        var name: String = "",
        var birth_place: String = "",
        var birth_date: String = "",
        var phone_number: String = "",
        var religion: String = "",
        var blood_type: String = "",
        var job: String = "",
        var last_education: String = "",
        var address: String = "",
        var city: Location? = Location(),
        var district: Location? = Location()
)

data class Rujukan(
        var id: String = "",
        var status: String = "",
        var date: String = "",
        var time: String = "",
        var nama_perujuk: String = "",
        var asal_perujuk: Hospital? = Hospital(),
        var sebab: String = "",
        var diagnosis: String = "",
        var tindakan: String = "",
        var tujuan: String = "",
        var umpanbalik_rujukan: UmpanbalikRujukan? = UmpanbalikRujukan()
)

data class UmpanbalikRujukan(
        var id: String = "",
        var date: String = "",
        var time: String = "",
        var penerima_rujukan: String = "",
        var asal_penerima: Hospital? = Hospital(),
        var diagnosis: String = "",
        var tindakan: String = ""
)