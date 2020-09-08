package com.healthmate.menu.reusable.data

import com.google.gson.annotations.SerializedName

data class User(
        var type: String = "",
        var token: String = "",
        var id: String = "",
        var validated: Boolean = true,
        var phone_number: String = "",
        var password: String = "",
        var name: String = "",
        var covid_checked: Boolean = false,
        var hospital: Hospital? = Hospital(),
        var location: Location? = Location(),
        var profil_picture: String? = "",
        var age: Int?,
        var kia: Kia? = Kia(),
        var diagnostics_color: String?
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
        var subdistrict: String = "",
        var district: String = "",
        var city: String = ""
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
        var districts: String = "",
        var sub_districts: String = ""
)