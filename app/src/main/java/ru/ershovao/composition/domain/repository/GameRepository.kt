package ru.ershovao.composition.domain.repository

import ru.ershovao.composition.domain.entity.GameSettings
import ru.ershovao.composition.domain.entity.Level
import ru.ershovao.composition.domain.entity.Question

interface GameRepository {
    fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question
    fun getGameSettings(level: Level): GameSettings
}