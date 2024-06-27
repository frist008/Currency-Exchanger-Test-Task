package ua.testtask.currencyexchanger.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import ua.testtask.currencyexchanger.R
import ua.testtask.currencyexchanger.ui.component.button.ButtonComponent
import ua.testtask.currencyexchanger.ui.component.item.ExchangeItemComponent
import ua.testtask.currencyexchanger.ui.component.item.TextItemComponent
import ua.testtask.currencyexchanger.ui.component.root.DefaultScaffold
import ua.testtask.currencyexchanger.ui.component.text.SubtitleComponent
import ua.testtask.currencyexchanger.ui.entity.main.ExchangeUIEntity
import ua.testtask.currencyexchanger.ui.entity.main.MainSuccessState
import ua.testtask.currencyexchanger.ui.entity.main.WalletUIEntity
import ua.testtask.currencyexchanger.ui.svg.ArrowDown
import ua.testtask.currencyexchanger.ui.svg.ArrowUp
import ua.testtask.currencyexchanger.ui.theme.RootTheme
import ua.testtask.currencyexchanger.ui.theme.color.Palette

@Composable
fun MainSuccessScreen(
    mainSuccessState: MainSuccessState,
    innerPadding: PaddingValues,
    sellItemClicked: () -> Unit,
    receiveItemClicked: () -> Unit,
    sellSelectCurrencyClicked: () -> Unit,
    receiveSelectCurrencyClicked: () -> Unit,
    submitClicked: () -> Unit,
) {
    val (
        walletList: PersistentList<WalletUIEntity>,
        sellExchangeEntity: ExchangeUIEntity,
        receiveExchangeEntity: ExchangeUIEntity,
        isSubmitEnabled: Boolean,
    ) = mainSuccessState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(top = 16.dp, bottom = 24.dp),
    ) {
        SubtitleComponent(
            modifier = Modifier.padding(horizontal = 16.dp),
            textRes = R.string.main_balance_title,
        )
        MainBalanceComponent(walletList)
        SubtitleComponent(
            modifier = Modifier.padding(horizontal = 16.dp),
            textRes = R.string.main_exchange_title,
        )
        Spacer(modifier = Modifier.height(4.dp))
        MainExchangeComponents(
            sellExchangeEntity,
            sellItemClicked,
            sellSelectCurrencyClicked,
            receiveExchangeEntity,
            receiveItemClicked,
            receiveSelectCurrencyClicked,
        )
        MainFooterComponent(submitClicked)
    }
}

@Composable
private fun MainBalanceComponent(
    walletList: PersistentList<WalletUIEntity>,
) {
    LazyRow(contentPadding = PaddingValues(vertical = 8.dp)) {
        items(
            items = walletList,
            key = { it.id },
        ) {
            TextItemComponent(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = it.balanceWithName,
            )
        }
    }
}

@Composable
private fun MainExchangeComponents(
    sellExchangeEntity: ExchangeUIEntity,
    sellItemClicked: () -> Unit,
    sellSelectCurrencyClicked: () -> Unit,
    receiveExchangeEntity: ExchangeUIEntity,
    receiveItemClicked: () -> Unit,
    receiveSelectCurrencyClicked: () -> Unit,
) {
    val horizontalLayoutPadding = 16
    ExchangeItemComponent(
        horizontalPadding = horizontalLayoutPadding,
        icon = Icons.ArrowUp,
        iconBackgroundColor = Palette.RED,
        textRes = R.string.main_exchange_sell,
        value = sellExchangeEntity.exchangeValueStr,
        valueColor = sellExchangeEntity.color,
        currencyCode = sellExchangeEntity.walletEntity.name,
        itemClicked = sellItemClicked,
        selectCurrencyClicked = sellSelectCurrencyClicked,
    )
    HorizontalDivider(
        modifier = Modifier.padding(
            start = 64.dp,
            end = horizontalLayoutPadding.dp,
        ),
        thickness = 0.5.dp,
    )
    ExchangeItemComponent(
        horizontalPadding = horizontalLayoutPadding,
        icon = Icons.ArrowDown,
        iconBackgroundColor = Palette.GREEN,
        textRes = R.string.main_exchange_receive,
        value = receiveExchangeEntity.exchangeValueStr,
        valueColor = receiveExchangeEntity.color,
        currencyCode = receiveExchangeEntity.walletEntity.name,
        itemClicked = receiveItemClicked,
        selectCurrencyClicked = receiveSelectCurrencyClicked,
    )
}

@Composable
private fun ColumnScope.MainFooterComponent(submitClicked: () -> Unit) {
    Spacer(
        modifier = Modifier
            .fillMaxHeight()
            .weight(1f),
    )
    ButtonComponent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        textRes = R.string.main_action_button,
        onClick = submitClicked,
    )
}

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@Composable
private fun MainScreenPreview() {
    RootTheme {
        val eurWallet = WalletUIEntity(
            id = 0,
            name = "EUR",
            balance = 1000f,
            balanceWithName = "1000.00 EUR",
        )
        val usdWallet = WalletUIEntity(
            id = 1,
            name = "USD",
            balance = 0f,
            balanceWithName = "0.00 USD",
        )

        DefaultScaffold(titleRes = R.string.main_title) { innerPadding ->
            MainSuccessScreen(
                innerPadding = innerPadding,
                mainSuccessState = MainSuccessState(
                    walletList = persistentListOf(eurWallet, usdWallet),
                    sellExchangeEntity = ExchangeUIEntity(
                        walletEntity = eurWallet,
                        exchangeValue = 100.0f,
                        exchangeValueStr = "100.00",
                        color = Palette.BLACK_DARK,
                    ),
                    receiveExchangeEntity = ExchangeUIEntity(
                        walletEntity = usdWallet,
                        exchangeValue = 2340.0f,
                        exchangeValueStr = "+2340.00",
                        color = Palette.GREEN,
                    ),
                    isSubmitEnabled = false,
                ),
                sellItemClicked = {},
                receiveItemClicked = {},
                sellSelectCurrencyClicked = {},
                receiveSelectCurrencyClicked = {},
                submitClicked = {},
            )
        }
    }
}
