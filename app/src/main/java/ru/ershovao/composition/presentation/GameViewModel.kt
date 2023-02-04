package ru.ershovao.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import ru.ershovao.composition.R
import ru.ershovao.composition.domain.entity.GameResult
import ru.ershovao.composition.domain.entity.GameSettings
import ru.ershovao.composition.domain.entity.Level
import ru.ershovao.composition.domain.entity.Question
import ru.ershovao.composition.domain.repository.GameRepositoryImpl
import ru.ershovao.composition.domain.usecases.GenerateQuestionUseCase
import ru.ershovao.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val MILLIS_IN_SECOND = 1000L
        const val SECONDS_IN_MINUTE = 60
    }

    private val context = application
    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private val _question: MutableLiveData<Question> = MutableLiveData()
    val question: LiveData<Question> = _question

    private val _enoughCountOfRightAnswers: MutableLiveData<Boolean> = MutableLiveData()
    val enoughCountOfRightAnswers: LiveData<Boolean> = _enoughCountOfRightAnswers

    private val _enoughPercentOfRightAnswers: MutableLiveData<Boolean> = MutableLiveData()
    val enoughPercentOfRightAnswers: LiveData<Boolean> = _enoughPercentOfRightAnswers

    private val _percentRightAnswers: MutableLiveData<Int> = MutableLiveData()
    val percentRightAnswers: LiveData<Int> = _percentRightAnswers

    private val _progressAnswers: MutableLiveData<String> = MutableLiveData()
    val progressAnswers: LiveData<String> = _progressAnswers

    private val _percentRightAnswersMin: MutableLiveData<Int> = MutableLiveData()
    val percentRightAnswersMin: LiveData<Int> = _percentRightAnswersMin

    private val _gameResult: MutableLiveData<GameResult> = MutableLiveData()
    val gameResult: LiveData<GameResult> = _gameResult

    private val _formattedTime: MutableLiveData<String> = MutableLiveData()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private var timer: CountDownTimer? = null
    private lateinit var gameSettings: GameSettings
    private var countOfRightAnswers = 0
    private var countOfQuestions = 0

    fun startGame(level: Level) {
        getGameSettings(level)
        generateQuestion()
        startTimer()
        updateProgress()
    }

    private fun getGameSettings(level: Level) {
        gameSettings = getGameSettingsUseCase(level)
        _percentRightAnswersMin.value = gameSettings.minPercentOfRightAnswers
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    fun chooseAnswer(answer: Int) {
        checkAnswer(answer)
        generateQuestion()
        updateProgress()
    }

    private fun checkAnswer(answer: Int) {
        val rightAnswer = _question.value?.rightAnswer
        if (rightAnswer == answer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentRightAnswers.value = percent
        _progressAnswers.value = String.format(
            context.resources.getString(R.string.txt_finish_score_percentage, countOfRightAnswers, gameSettings.minCountOfRightAnswers)
        )
        _enoughCountOfRightAnswers.value = countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercentOfRightAnswers.value = (_percentRightAnswers.value ?: 0) >= gameSettings.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        return if (countOfQuestions == 0)
            0
        else
            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    private fun formatTime(millis: Long): String {
        val seconds = millis / MILLIS_IN_SECOND
        val minutes = seconds / SECONDS_IN_MINUTE
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTE)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(gameSettings.gameTimeInSeconds * MILLIS_IN_SECOND, MILLIS_IN_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }

        }
        timer?.start()
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            winner = _enoughCountOfRightAnswers.value == true && _enoughPercentOfRightAnswers.value == true,
            countOfRightAnswers = countOfRightAnswers,
            countOfQuestions = countOfQuestions,
            gameSettings = gameSettings
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
}