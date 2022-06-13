package com.example.mandatoryassignment.models

import java.time.LocalDate

data class Item (val id: Int, val title: String, val price: Int, val date: Int, val description: String, val seller: String/*, val pictureUrl: String*/) {
    constructor(title: String, price: Int, date: Int, description: String, seller: String/*, pictureUrl: String*/) : this(-1, title, price, date, description, seller/*, pictureUrl*/)

    override fun toString(): String {
        return "$id  $title,  $price, $date, $description, $seller"
    }
}