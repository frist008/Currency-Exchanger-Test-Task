package ua.testtask.currencyexchanger.data.repository.impl

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ua.testtask.currencyexchanger.TestApplication
import ua.testtask.currencyexchanger.data.database.dao.WalletDAO
import ua.testtask.currencyexchanger.data.database.entity.WalletDBO
import ua.testtask.currencyexchanger.data.network.api.AppApi
import ua.testtask.currencyexchanger.data.network.entity.CurrencyDTO
import ua.testtask.currencyexchanger.data.repository.CurrencyRepository
import ua.testtask.currencyexchanger.domain.entity.WalletEntity
import ua.testtask.currencyexchanger.util.TestConfig

@RunWith(RobolectricTestRunner::class) @Config(
    manifest = TestConfig.MANIFEST,
    application = TestApplication::class,
    sdk = [TestConfig.SDK_VERSION],
)
class CurrencyRepositoryImplTest {

    private lateinit var testClass: CurrencyRepository

    private val appApi: AppApi = mockk()
    private val walletDAO: WalletDAO = mockk()

    private val mockCurrencyName1 = "USD"
    private val mockCurrencyValue1 = 2f
    private val mockCurrencyName2 = "UAH"
    private val mockCurrencyValue2 = 3f
    private val mockDto = CurrencyDTO(
        base = WalletEntity.DEFAULT_BASE_CURRENCY,
        rateMap = mapOf(
            mockCurrencyName1 to mockCurrencyValue1,
            mockCurrencyName2 to mockCurrencyValue2,
        ),
    )
    private val mockWalletDBO0 =
        WalletDBO(id = 1, name = WalletEntity.DEFAULT_BASE_CURRENCY, balance = 100f)
    private val mockWalletDBO1 =
        WalletDBO(id = 2, name = mockCurrencyName1, balance = 200f)

    @Before fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        testClass = CurrencyRepositoryImpl(
            api = appApi,
            walletDAO = walletDAO,
        )
    }

    @Test fun `test getWallets() with values`() {
        `getWallets() with values`(listOf(mockWalletDBO0, mockWalletDBO1))
    }

    @Test fun `test getWallets() when database is empty`() {
        `getWallets() with values`(emptyList())
    }

    private fun `getWallets() with values`(mockWallets: List<WalletDBO>) {
        coEvery { walletDAO.getAll() } returns flowOf(mockWallets)

        runTest {
            val result = testClass.getWallets().firstOrNull()
            val expected = mockWallets.map(WalletDBO::toDomainEntity)

            assertEquals(expected, result)
        }
    }

    @Test fun `test getPriceOfCurrencies() with values`() {
        val dboList = mockDto.toDBOList()

        runTest {
            coEvery { appApi.getCurrencies() } returns mockDto
            coEvery { walletDAO.insertAll(dboList) } just runs
            val result = testClass.getPriceOfCurrencies().first()

            coVerify { walletDAO.insertAll(dboList) }
            assertEquals(mockDto.rateMap.size, result.size)
            assertTrue(result.containsKey(mockCurrencyName1))
            assertTrue(result.containsKey(mockCurrencyName2))
            assertTrue(result.all { it.value.baseCurrency == mockDto.base })
            assertEquals(mockCurrencyName1, result[mockCurrencyName1]?.targetCurrency)
            assertEquals(mockCurrencyValue1, result[mockCurrencyName1]?.sellPrice)
            assertEquals(mockCurrencyValue1, result[mockCurrencyName1]?.buyPrice)
            assertEquals(mockCurrencyName2, result[mockCurrencyName2]?.targetCurrency)
            assertEquals(mockCurrencyValue2, result[mockCurrencyName2]?.sellPrice)
            assertEquals(mockCurrencyValue2, result[mockCurrencyName2]?.buyPrice)

            confirmVerified(walletDAO)
        }
    }

    @Test
    fun `test getPriceOfCurrencies with invalid baseCurrency`() {
        assertThrows(UnsupportedOperationException::class.java) {
            runBlocking {
                testClass.getPriceOfCurrencies(mockCurrencyName1)
            }
        }
    }
}
