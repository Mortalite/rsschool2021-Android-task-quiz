package com.rsschool.quiz

import kotlinx.serialization.Serializable

@Serializable
class Question(
    id: Int,
    question: String,
    optiona: String,
    optionb: String,
    optionc: String,
    optiond: String,
    optione: String,
    answer: Int
    ) {
    var id = id
    var question = question
    var optiona = optiona
    var optionb = optionb
    var optionc = optionc
    var optiond = optiond
    var optione = optione
    var answer = answer
}