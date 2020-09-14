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
        var profil_picture: String? = "",
        var age: Int? = 0,
        var kia: Kia? = Kia(),
        var city: Location? = Location(),
        var district: Location? = Location(),
        var diagnostics_color: String? = "",
        var str_number: String? = "",
        var hpht: String? = "",

        @SerializedName("hml")
        var hpl: String? = "",

        var preg_num: String? = "",
        var labor_num: String? = "",
        var miscarriage_num: String? = "",
        var live_child_num: String? = "",
        var prev_child_difference: String? = ""
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
        var husband: Husband? = Husband()

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