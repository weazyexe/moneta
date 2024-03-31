package dev.weazyexe.moneta.screens.convert

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.weazyexe.moneta.R
import dev.weazyexe.moneta.domain.model.Currency
import dev.weazyexe.moneta.screens.convert.viewstate.AmountViewState
import dev.weazyexe.moneta.screens.convert.viewstate.ConvertViewState
import dev.weazyexe.moneta.screens.convert.viewstate.CurrencyViewState
import dev.weazyexe.moneta.screens.currencies.CurrenciesScreen
import dev.weazyexe.moneta.ui.extensions.ReceiveEffect
import dev.weazyexe.moneta.ui.extensions.getResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvertBody(
    state: ConvertViewState,
    eventSink: (ConvertEvent) -> Unit,
    effects: Flow<ConvertEffect>
) {
    val navigator = LocalNavigator.currentOrThrow
    var isFrom by rememberSaveable { mutableStateOf(true) }
    val newlySelectedCurrency by navigator.getResult<Currency>(screenKey = CurrenciesScreen().key)

    ReceiveEffect(effects) {
        when (this) {
            is ConvertEffect.OpenCurrencyPicker -> {
                isFrom = this.isFrom
                navigator.push(CurrenciesScreen(selectedCurrency))
            }
        }
    }

    LaunchedEffect(newlySelectedCurrency) {
        val currency = newlySelectedCurrency ?: return@LaunchedEffect
        if (isFrom) {
            eventSink(ConvertEvent.SelectFromCurrency(currency))
        } else {
            eventSink(ConvertEvent.SelectToCurrency(currency))
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(title = { Text(text = stringResource(id = R.string.convert_title)) })
        }
    ) { innerPaddings ->
        when (state) {
            is ConvertViewState.Error -> {
                // Todo
            }

            is ConvertViewState.Loading -> {
                // Todo
            }

            is ConvertViewState.Content -> {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            top = innerPaddings.calculateTopPadding(),
                            end = 16.dp
                        )
                ) {
                    CurrencyConvertField(
                        amount = state.from,
                        actionTitle = stringResource(id = R.string.convert_pick_from_currency),
                        onClick = { eventSink(ConvertEvent.OnFromCurrencyClick) },
                        onValueChange = { eventSink(ConvertEvent.FromAmountChange(it)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    CurrencyConvertField(
                        amount = state.to,
                        actionTitle = stringResource(id = R.string.convert_pick_to_currency),
                        onClick = { eventSink(ConvertEvent.OnToCurrencyClick) },
                        onValueChange = { eventSink(ConvertEvent.ToAmountChange(it)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrencyConvertField(
    amount: AmountViewState,
    actionTitle: String,
    onClick: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isActive by remember { mutableStateOf(false) }

    val borderColor = if (isActive) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outline
    }
    val borderWidth = if (isActive) {
        2.dp
    } else {
        1.dp
    }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 56.dp)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = MaterialTheme.shapes.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(16.dp))

        CurrencyTextField(
            amount = amount,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            onFocusChange = { isActive = it }
        )

        CurrencyBlock(
            currency = amount.currency,
            actionTitle = actionTitle,
            onClick = onClick,
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 8.dp,
                bottom = 16.dp
            )
        )

    }
}

@Composable
private fun CurrencyBlock(
    currency: CurrencyViewState,
    actionTitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    Row(
        modifier = modifier
            .clearAndSetSemantics {
                text = AnnotatedString(currency.title)
                customActions = listOf(
                    CustomAccessibilityAction(
                        label = actionTitle,
                        action = {
                            onClick()
                            true
                        }
                    )
                )
            }
            .clip(MaterialTheme.shapes.small)
            .clickable { onClick() }
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currency.emoji,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.size(4.dp))

        Text(
            text = currency.code,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.size(4.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_expand_24),
            contentDescription = null,
        )
    }
}

@Composable
private fun CurrencyTextField(
    amount: AmountViewState,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFocusChange: (Boolean) -> Unit = {}
) {
    BasicTextField(
        value = amount.value,
        onValueChange = { textFieldValue ->
            /*if (value.isEmpty()) {
                onValueChange("")
            } else {
                onValueChange(
                    when (value.toDoubleOrNull()) {
                        null -> amount.value
                        else -> value
                    }
                )
            }*/
            textFieldValue.forEach {
                if (!it.isDigit() && it != '.' && it != ',') {
                    return@BasicTextField
                }
            }

            onValueChange(textFieldValue)
            /*if (textFieldValue.isEmpty()) {
                onValueChange(null)
                return@BasicTextField
            }

            val numberValue = textFieldValue.toDoubleOrNull() ?: return@BasicTextField

            if (numberValue % 1.0 == 0.0) {
                onValueChange(numberValue.toInt())
            } else {

            }*/
        },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { onFocusChange(it.hasFocus) },
        singleLine = true,
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = { innerTextField ->
            Row {
                Text(
                    text = amount.currency.symbol,
                    modifier = Modifier.alignByBaseline(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.size(2.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alignByBaseline()
                ) {
                    innerTextField()
                }
            }
        }
    )
}

@Preview
@Composable
private fun ConvertScreenContentPreview() {
    ConvertBody(
        state = ConvertScreenPreviews.Default,
        eventSink = {},
        effects = emptyFlow()
    )
}

private data object ConvertScreenPreviews {

    val Default = ConvertViewState.Content(
        from = AmountViewState(
            currency = CurrencyViewState(
                code = "EUR",
                emoji = "🇪🇺",
                title = "Euro",
                symbol = "€"
            ),
            value = "100"
        ),
        to = AmountViewState(
            currency = CurrencyViewState(
                code = "RUB",
                emoji = "🇷🇺",
                title = "Russian Ruble",
                symbol = "₽"
            ),
            value = "10000"
        )
    )
}