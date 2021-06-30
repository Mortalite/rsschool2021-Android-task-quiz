package com.rsschool.quiz

class Question(
    var id: Int,
    var themeId: Int,
    var question: String,
    var option0: String,
    var option1: String,
    var option2: String,
    var option3: String,
    var option4: String,
    var checkedId: Int,
    var selectedAnswer: Int,
    var answer: Int
    ) {
}