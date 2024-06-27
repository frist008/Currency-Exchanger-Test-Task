package ua.testtask.currencyexchanger.ui.screen.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ua.testtask.currencyexchanger.R
import ua.testtask.currencyexchanger.presentation.base.MainViewModel
import ua.testtask.currencyexchanger.ui.component.root.DefaultScaffold
import ua.testtask.currencyexchanger.ui.entity.base.UIState
import ua.testtask.currencyexchanger.ui.entity.main.MainSuccessState
import ua.testtask.currencyexchanger.util.exception.unsupportedUI

@Composable fun MainScreen() {
    DefaultScaffold(titleRes = R.string.main_title) { innerPadding ->
        MainScreen(innerPadding)
    }
}

@Composable fun MainScreen(
    innerPadding: PaddingValues,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    when (val currentState = state) {
        is MainSuccessState -> MainSuccessScreen(
            mainSuccessState = currentState,
            innerPadding = innerPadding,
            sellItemClicked = {
                viewModel.onSellItemClicked(currentState)
            },
            receiveItemClicked = {
                viewModel.onReceiveItemClicked(currentState)
            },
            sellSelectCurrencyClicked = {
                viewModel.onSellSelectCurrencyClicked(currentState)
            },
            receiveSelectCurrencyClicked = {
                viewModel.onReceiveSelectCurrencyClicked(currentState)
            },
            submitClicked = {
                viewModel.onSubmitClicked(currentState)
            },
        )

        is UIState.Error,
        is UIState.Progress,
        -> {
            viewModel.listenChanges()
            MainLoadingScreen(innerPadding)
        }

        else -> unsupportedUI()
    }
}
