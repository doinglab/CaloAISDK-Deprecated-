package com.doinglab.foodlens2.example.listview

import android.graphics.drawable.Drawable

data class ListItem (
    val id : Int? = Int.MIN_VALUE,
    val title : String? = null,
    val icon : Drawable? = null,
    val foodPosition: String? = null,
    val foodNutrition: String? = null
)