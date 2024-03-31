package dev.weazyexe.moneta.screens.currencies

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.weazyexe.moneta.R
import dev.weazyexe.moneta.core.AsyncResult
import dev.weazyexe.moneta.screens.currencies.viewstate.CurrenciesViewState
import dev.weazyexe.moneta.screens.currencies.viewstate.CurrencyViewState
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrenciesBody(
    state: CurrenciesViewState,
    modifier: Modifier = Modifier
) {
    val nestedScroll = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.currencies_title)) },
                scrollBehavior = nestedScroll
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            when (state.currencies) {
                is AsyncResult.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is AsyncResult.Error -> {
                    Text(
                        text = stringResource(id = R.string.currencies_error),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is AsyncResult.Success -> {
                    LazyColumn(
                        modifier = Modifier.nestedScroll(nestedScroll.nestedScrollConnection),
                        contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        itemsIndexed(state.currencies.data) { index, currency ->
                            CurrencyItem(
                                currency = currency,
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (index < state.currencies.data.size - 1) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencyItem(
    currency: CurrencyViewState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .defaultMinSize(minHeight = 56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(8.dp)
        ) {
            Text(text = currency.emoji)
        }

        Column {
            Text(
                text = currency.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = currency.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (currency.checked) {
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.ic_check_24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Preview
@Composable
fun CurrenciesBodySuccessPreview() {
    CurrenciesBody(
        state = CurrenciesViewState(
            currencies = AsyncResult.Success(
                data = persistentListOf(
                    CurrencyViewState(
                        code = CurrencyViewState.Code("USD"),
                        emoji = "🇺🇸",
                        title = "United States Dollar",
                        subtitle = "USD, $",
                        checked = true
                    ),
                    CurrencyViewState(
                        code = CurrencyViewState.Code("EUR"),
                        emoji = "🇪🇺",
                        title = "Euro",
                        subtitle = "EUR, €",
                        checked = false
                    ),
                    CurrencyViewState(
                        code = CurrencyViewState.Code("RUB"),
                        emoji = "🇷🇺",
                        title = "Russian Rouble",
                        subtitle = "RUB, ₽",
                        checked = false
                    ),
                    CurrencyViewState(
                        code = CurrencyViewState.Code("JPY"),
                        emoji = "🇯🇵",
                        title = "Japanese Yen",
                        subtitle = "JPY, ¥",
                        checked = false
                    )
                )
            )
        )
    )
}

@Preview
@Composable
fun CurrenciesBodyLoadingPreview() {
    CurrenciesBody(
        state = CurrenciesViewState(
            currencies = AsyncResult.Loading()
        )
    )
}

@Preview
@Composable
fun CurrenciesBodyErrorPreview() {
    CurrenciesBody(
        state = CurrenciesViewState(
            currencies = AsyncResult.Error(Throwable("Error"))
        )
    )
}