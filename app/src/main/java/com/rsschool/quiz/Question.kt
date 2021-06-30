package com.rsschool.quiz

import kotlinx.serialization.Serializable

@Serializable
class Question(
    id: Int,
    themeId: Int,
    question: String,
    option0: String,
    option1: String,
    option2: String,
    option3: String,
    option4: String,
    checkedId: Int,
    selectedAnswer: Int,
    answer: Int
    ) {
    var id = id
    var themeId = themeId
    var question = question
    var option0 = option0
    var option1 = option1
    var option2 = option2
    var option3 = option3
    var option4 = option4
    var checkedId = checkedId
    var selectedAnswer = selectedAnswer
    var answer = answer
}