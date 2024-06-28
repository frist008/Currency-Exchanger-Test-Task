package ua.testtask.currencyexchanger.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.PersistentList
import ua.testtask.currencyexchanger.R
import ua.testtask.currencyexchanger.ui.component.button.ButtonComponent
import ua.testtask.currencyexchanger.ui.component.item.ExchangeItemComponent
import ua.testtask.currencyexchanger.ui.component.item.TextItemComponent
import ua.testtask.currencyexchanger.ui.component.popup.PopupBox
import ua.testtask.currencyexchanger.ui.component.root.DefaultScaffold
import ua.testtask.currencyexchanger.ui.component.text.SubtitleComponent
import ua.testtask.currencyexchanger.ui.entity.main.MainSuccessState
import ua.testtask.currencyexchanger.ui.entity.main.entity.WalletUIEntity
import ua.testtask.currencyexchanger.ui.svg.ArrowDown
import ua.testtask.currencyexchanger.ui.svg.ArrowUp
import ua.testtask.currencyexchanger.ui.theme.RootTheme
import ua.testtask.currencyexchanger.ui.theme.color.Palette
import kotlin.math.min

@Composable
fun MainSuccessScreen(
    innerPadding: PaddingValues,
    mainSuccessState: MainSuccessState,
    onSellValueChanged: (String) -> Unit,
    onReceiveValueChanged: (String) -> Unit,
    onSellNewCurrencySelected: (WalletUIEntity) -> Unit,
    onReceiveNewCurrencySelected: (WalletUIEntity) -> Unit,
    onSubmitClicked: () -> Unit,
) {
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
        MainBalanceComponent(mainSuccessState.walletList)
        SubtitleComponent(
            modifier = Modifier.padding(horizontal = 16.dp),
            textRes = R.string.main_exchange_title,
        )
        Spacer(modifier = Modifier.height(4.dp))
        MainExchangeComponents(
            mainSuccessState = mainSuccessState,
            onSellValueChanged = onSellValueChanged,
            onSellSelectCurrencyClicked = onSellNewCurrencySelected,
            onReceiveValueChanged = onReceiveValueChanged,
            onReceiveSelectCurrencyClicked = onReceiveNewCurrencySelected,
        )

        var isError by remember { mutableStateOf(mainSuccessState.isError) }
        isError = mainSuccessState.isError

        MainFooterComponent(isError, onSubmitClicked)
    }
}

@Composable
private fun MainBalanceComponent(
    walletList: PersistentList<WalletUIEntity>,
) {
    LazyRow(contentPadding = PaddingValues(vertical = 8.dp)) {
        items(
            items = walletList,
            key = WalletUIEntity::id,
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
    mainSuccessState: MainSuccessState,
    onSellValueChanged: (String) -> Unit,
    onSellSelectCurrencyClicked: (WalletUIEntity) -> Unit,
    onReceiveValueChanged: (String) -> Unit,
    onReceiveSelectCurrencyClicked: (WalletUIEntity) -> Unit,
) {
    val horizontalLayoutPadding = 16.dp

    val sellPopupExpanded = remember { mutableStateOf(false) }
    PopupGrid(
        popupExpanded = sellPopupExpanded,
        walletList = mainSuccessState.walletList,
        isAllEnabled = false,
        onSellSelectCurrencyClicked = onSellSelectCurrencyClicked,
    )
    ExchangeItemComponent(
        horizontalPadding = horizontalLayoutPadding,
        icon = Icons.ArrowUp,
        iconBackgroundColor = Palette.RED,
        textRes = R.string.main_exchange_sell,
        exchangeEntity = mainSuccessState.exchangeTransactionEntity.sell,
        onValueChange = onSellValueChanged,
        isError = mainSuccessState.isError,
        onCurrencyClicked = { sellPopupExpanded.value = true },
    )

    HorizontalDivider(
        modifier = Modifier.padding(
            start = 64.dp,
            end = horizontalLayoutPadding,
        ),
        thickness = 0.5.dp,
    )

    val receivePopupExpanded = remember { mutableStateOf(false) }
    PopupGrid(
        popupExpanded = receivePopupExpanded,
        walletList = mainSuccessState.walletList,
        isAllEnabled = true,
        onSellSelectCurrencyClicked = onReceiveSelectCurrencyClicked,
    )
    ExchangeItemComponent(
        horizontalPadding = horizontalLayoutPadding,
        icon = Icons.ArrowDown,
        iconBackgroundColor = Palette.GREEN,
        textRes = R.string.main_exchange_receive,
        exchangeEntity = mainSuccessState.exchangeTransactionEntity.receive,
        onValueChange = onReceiveValueChanged,
        isError = false,
        onCurrencyClicked = { receivePopupExpanded.value = true },
    )
}

@Composable
private fun PopupGrid(
    popupExpanded: MutableState<Boolean>,
    walletList: PersistentList<WalletUIEntity>,
    isAllEnabled: Boolean,
    onSellSelectCurrencyClicked: (WalletUIEntity) -> Unit,
) {
    if (!popupExpanded.value) return

    PopupBox(alignment = Alignment.TopEnd, onDismissRequest = { popupExpanded.value = false }) {
        val cellsCount = min(3, walletList.size)
        LazyVerticalGrid(
            modifier = Modifier
                .shadow(4.dp)
                .background(Palette.WHITE)
                .widthIn(max = (cellsCount * 85).dp)
                .heightIn(max = 256.dp),
            columns = GridCells.Fixed(cellsCount),
        ) {
            items(
                items = walletList,
                key = WalletUIEntity::id,
            ) {
                val enabled = isAllEnabled || it.isEnable
                TextItemComponent(
                    modifier = Modifier
                        .clickable(enabled = enabled) {
                            onSellSelectCurrencyClicked(it)
                            popupExpanded.value = false
                        }
                        .padding(8.dp),
                    isEnabled = enabled,
                    text = it.name,
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.MainFooterComponent(isError: Boolean, onSubmitClicked: () -> Unit) {
    val focusManager = LocalFocusManager.current

    Spacer(
        modifier = Modifier
            .fillMaxHeight()
            .weight(1f),
    )
    ButtonComponent(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .padding(horizontal = 32.dp),
        isEnabled = !isError,
        textRes = R.string.main_action_button,
        onClick = {
            focusManager.clearFocus()
            onSubmitClicked()
        },
    )
}

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@Composable
private fun MainScreenPreview() {
    RootTheme {
        DefaultScaffold(titleRes = R.string.main_title) { innerPadding ->
            MainSuccessScreen(
                innerPadding = innerPadding,
                mainSuccessState = MainSuccessState.preview(),
                onSellValueChanged = {},
                onReceiveValueChanged = {},
                onSellNewCurrencySelected = {},
                onReceiveNewCurrencySelected = {},
                onSubmitClicked = {},
            )
        }
    }
}
