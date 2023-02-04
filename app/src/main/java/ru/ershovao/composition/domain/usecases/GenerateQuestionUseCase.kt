package ru.ershovao.composition.domain.usecases

import ru.ershovao.composition.domain.entity.Question
import ru.ershovao.composition.domain.repository.GameRepository

class GenerateQuestionUseCase(private val repository: GameRepository) {
    companion object {
        const val COUNT_OF_OPTIONS = 6
    }

    operator fun invoke(maxSumValue: Int, countOfOptions: Int = COUNT_OF_OPTIONS): Question {
        return repository.generateQuestion(maxSumValue = maxSumValue, countOfOptions = countOfOptions)
    }
}