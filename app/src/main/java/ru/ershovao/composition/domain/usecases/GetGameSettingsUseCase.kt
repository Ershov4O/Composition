package ru.ershovao.composition.domain.usecases

import ru.ershovao.composition.domain.entity.GameSettings
import ru.ershovao.composition.domain.entity.Level
import ru.ershovao.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(val repository: GameRepository) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}