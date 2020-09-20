package com.healthmate.menu.mom.home.data

import com.healthmate.menu.reusable.data.Hospital
import com.healthmate.menu.reusable.data.User

data class CheckUpModel(
        var id: String = "",
        var finished: Boolean = false,
        var object_type: String = "",
        var object_id: String = "",
        var rating: Int = 0,
        var mother: User = User(),
        var midwive: User = User(),
        var next_hospital: Hospital? = Hospital()
)