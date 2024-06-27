package ua.testtask.currencyexchanger.data.repository.impl

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
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
import ua.testtask.currencyexchanger.domain.entity.WalletDomainEntity
import ua.testtask.currencyexchanger.domain.entity.exception.IncorrectBalanceException
import ua.testtask.currencyexchanger.util.TestConfig

@RunWith(RobolectricTestRunner::class) @Config(
    manifest = TestConfig.MANIFEST,
    application = TestApplication::class,
    sdk = [TestConfig.SDK_VERSION],
) class CurrencyRepositoryImplTest {

    private lateinit var testClass: CurrencyRepository

    private val appApi: AppApi = mockk()
    private val walletDAO: WalletDAO = mockk()

    private val mockCurrencyName1 = "USD"
    private val mockCurrencyValue1 = 2f
    private val mockCurrencyName2 = "UAH"
    private val mockCurrencyValue2 = 3f
    private val mockDto = CurrencyDTO(
        base = WalletDomainEntity.DEFAULT_BASE_CURRENCY,
        rateMap = mapOf(
            mockCurrencyName1 to mockCurrencyValue1,
            mockCurrencyName2 to mockCurrencyValue2,
        ),
    )
    private val mockDomainDtoMap = mockDto.toDomainMap()
    private val mockWalletDBO0 =
        WalletDBO(id = 1, name = WalletDomainEntity.DEFAULT_BASE_CURRENCY, balance = 100f)
    private val mockWalletDBO1 = WalletDBO(id = 2, name = mockCurrencyName1, balance = 200f)
    private val mockWalletDBO2 = WalletDBO(id = 3, name = mockCurrencyName2, balance = 300f)
    private val mockWalletDomainEntity0 = mockWalletDBO0.toDomainEntity()
    private val mockWalletDomainEntity1 = mockWalletDBO1.toDomainEntity()
    private val mockWalletDomainEntity2 = mockWalletDBO2.toDomainEntity()

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
            coEvery { walletDAO.insertAll(dboList) } just runs
            coEvery { appApi.getCurrencies() } returns mockDto
            val result = testClass.getPriceOfCurrencies()

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

    @Test
    fun `test updateWallet() when base EUR`() {
        val base = mockWalletDomainEntity0.copy(balance = 100f)
        val target = mockWalletDomainEntity1.copy(balance = 200f)
        val sum = 60f
        val expectedBase = base.copy(balance = 40f)
        val expectedTarget = target.copy(
            balance = target.balance + 60 * mockCurrencyValue1,
        )

        coEvery { testClass.updateWallet(mockDomainDtoMap, base, target, sum) } just runs
        runTest {
            testClass.updateWallet(mockDomainDtoMap, base, target, sum)
        }

        coVerify { walletDAO.update(expectedBase.toDBO(), expectedTarget.toDBO()) }
    }

    @Test
    fun `test updateWallet() when target EUR`() {
        val base = mockWalletDomainEntity1.copy(balance = 200f)
        val target = mockWalletDomainEntity0.copy(balance = 100f)
        val sum = 60f
        val expectedBase = base.copy(balance = 140f)
        val expectedTarget = target.copy(
            balance = target.balance + 60 / mockCurrencyValue1,
        )

        coEvery { testClass.updateWallet(mockDomainDtoMap, base, target, sum) } just runs
        runTest {
            testClass.updateWallet(mockDomainDtoMap, base, target, sum)
        }

        coVerify { walletDAO.update(expectedBase.toDBO(), expectedTarget.toDBO()) }
    }

    @Test
    fun `test updateWallet() when EUR none`() {
        val base = mockWalletDomainEntity1.copy(balance = 200f)
        val target = mockWalletDomainEntity2.copy(balance = 300f)
        val sum = 60f
        val expectedBase = base.copy(balance = 140f)
        val expectedTarget = target.copy(
            balance = target.balance + 60 / mockCurrencyValue1 * mockCurrencyValue2,
        )

        coEvery { testClass.updateWallet(mockDomainDtoMap, base, target, sum) } just runs
        runTest {
            testClass.updateWallet(mockDomainDtoMap, base, target, sum)
        }

        coVerify { walletDAO.update(expectedBase.toDBO(), expectedTarget.toDBO()) }
    }

    @Test
    fun `test updateWallet() when balance is insufficient`() {
        val base = mockWalletDomainEntity0.copy(balance = 100f)
        val target = mockWalletDomainEntity1.copy(balance = 200f)
        val sum = 160f

        assertThrows(IncorrectBalanceException::class.java) {
            runBlocking {
                testClass.updateWallet(mockDomainDtoMap, base, target, sum)
            }
        }
    }

    @Test
    fun `test updateWallet() when IllegalArgumentException is thrown`() {
        val base = mockWalletDomainEntity0
        val target = mockWalletDomainEntity1.copy(name = "NULL")
        val sum = 60f

        assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                testClass.updateWallet(emptyMap(), base, target, sum)
            }
        }
    }
}
