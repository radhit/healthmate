package com.healthmate.menu.mom.home.data

import com.healthmate.menu.reusable.data.User

data class CheckUpModel(
        var id: String = "",
        var in_progress: Boolean = false,
        var object_type: String = "",
        var object_id: String = "",
        var rating: String = "",
        var mother: User = User(),
        var midwive: User = User()
)