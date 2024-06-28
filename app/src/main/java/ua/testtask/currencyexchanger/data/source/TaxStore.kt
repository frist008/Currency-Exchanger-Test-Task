package ua.testtask.currencyexchanger.data.source

interface TaxStore {

    fun getTaxCoefficient(): Float

    fun consume(): Float
}
