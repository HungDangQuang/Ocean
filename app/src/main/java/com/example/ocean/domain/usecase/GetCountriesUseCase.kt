package com.example.domain.usecase

import com.example.domain.model.Country
import com.example.domain.repository.CountryRepository
import com.example.ocean.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GetCountriesUseCase(
    private val countryRepository: CountryRepository,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO)
    :  UseCase<Unit, List<Country>>(coroutineDispatcher) {

    override suspend fun execute(parameters: Unit): List<Country> {
        return countryRepository.getCountries()
    }
}