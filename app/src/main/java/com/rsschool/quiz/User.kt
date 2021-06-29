package com.rsschool.quiz

import kotlinx.serialization.Serializable

@Serializable
class User(id: Int, score: Int) {
    var id: Int = id
    var score: Int = score
}