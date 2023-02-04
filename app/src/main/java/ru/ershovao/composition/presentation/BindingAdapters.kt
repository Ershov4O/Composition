package ru.ershovao.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import ru.ershovao.composition.R
import ru.ershovao.composition.domain.entity.GameResult

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.msg_finish_count_of_right_answers, count)
    )
}

@BindingAdapter("finishScore")
fun bindFinishScore(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.txt_finish_score, count)
    )
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, percentage: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.txt_finish_required_percentage, percentage)
    )
}

private fun getFinishPercentage(gameResult: GameResult): Int {
    return if (gameResult.countOfQuestions == 0) 0 else {
        ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
    }
}

@BindingAdapter("finishPercentage")
fun bindFinishPercentage(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.txt_finish_score_percentage, getFinishPercentage(gameResult))
    )
}

@BindingAdapter("resultEmoji")
fun bindResultEmoji(imageView: ImageView, winner: Boolean) {
    imageView.setImageResource(getEmojiResource(winner))
}

fun getEmojiResource(winner: Boolean): Int {
    return if (winner) {
        R.drawable.ic_smile
    } else {
        R.drawable.ic_sad
    }
}

