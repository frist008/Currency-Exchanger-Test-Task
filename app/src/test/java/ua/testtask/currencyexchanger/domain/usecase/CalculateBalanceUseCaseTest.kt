package ua.testtask.currencyexchanger.domain.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ua.testtask.currencyexchanger.TestApplication
import ua.testtask.currencyexchanger.data.database.entity.WalletDBO
import ua.testtask.currencyexchanger.data.network.entity.CurrencyDTO
import ua.testtask.currencyexchanger.data.repository.CurrencyRepository
import ua.testtask.currencyexchanger.domain.entity.WalletEntity
import ua.testtask.currencyexchanger.util.TestConfig

@RunWith(RobolectricTestRunner::class) @Config(
    manifest = TestConfig.MANIFEST,
    application = TestApplication::class,
    sdk = [TestConfig.SDK_VERSION],
)
class CalculateBalanceUseCaseTest {

    private lateinit var testClass: CalculateBalanceUseCase

    private val repository: CurrencyRepository = mockk()

    private val tax = 0.1f
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
    private val mockDomainDtoMap = mockDto.toDomainMap()
    private val mockWalletDBO0 =
        WalletDBO(id = 1, name = WalletEntity.DEFAULT_BASE_CURRENCY, balance = 100f)
    private val mockWalletDBO1 = WalletDBO(id = 2, name = mockCurrencyName1, balance = 200f)
    private val mockWalletDBO2 = WalletDBO(id = 3, name = mockCurrencyName2, balance = 300f)
    private val mockWalletDomainEntity0 = mockWalletDBO0.toDomainEntity()
    private val mockWalletDomainEntity1 = mockWalletDBO1.toDomainEntity()
    private val mockWalletDomainEntity2 = mockWalletDBO2.toDomainEntity()

    @Before fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        testClass = CalculateBalanceUseCase(repository = repository)

        coEvery { repository.getPriceOfCurrencies() } returns MutableStateFlow(mockDomainDtoMap)
        coEvery { repository.getTaxCoefficient() } returns tax
    }

    @Test
    fun `test updateWallet() when base EUR`() {
        val base = mockWalletDomainEntity0.copy(balance = 100f)
        val target = mockWalletDomainEntity1.copy(balance = 200f)
        val sum = 60f
        val sumAfterTax = sum - sum * tax

        runTest {
            assertEquals(testClass.calc(base, target, sum), sumAfterTax * mockCurrencyValue1)
        }
    }

    @Test
    fun `test updateWallet() when target EUR`() {
        val base = mockWalletDomainEntity1.copy(balance = 200f)
        val target = mockWalletDomainEntity0.copy(balance = 100f)
        val sum = 60f
        val sumAfterTax = sum - sum * tax

        runTest {
            assertEquals(testClass.calc(base, target, sum), sumAfterTax / mockCurrencyValue1)
        }
    }

    @Test
    fun `test updateWallet() when EUR none`() {
        val base = mockWalletDomainEntity1.copy(balance = 200f)
        val target = mockWalletDomainEntity2.copy(balance = 300f)
        val sum = 60f
        val sumAfterTax = sum - sum * tax

        runTest {
            assertEquals(
                testClass.calc(base, target, sum),
                sumAfterTax / mockCurrencyValue1 * mockCurrencyValue2,
            )
        }
    }

    @Test
    fun `test updateWallet() when IllegalArgumentException is thrown`() {
        val base = mockWalletDomainEntity0
        val target = mockWalletDomainEntity1.copy(name = "NULL")
        val sum = 60f

        assertThrows(NoSuchElementException::class.java) {
            runBlocking {
                testClass.calc(base, target, sum)
            }
        }
    }
}
