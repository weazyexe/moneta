package dev.weazyexe.moneta.screens.currencies

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.weazyexe.moneta.R
import dev.weazyexe.moneta.core.AsyncResult
import dev.weazyexe.moneta.screens.currencies.viewstate.CurrenciesViewState
import dev.weazyexe.moneta.screens.currencies.viewstate.CurrencyViewState
import dev.weazyexe.moneta.ui.core.IconButton
import dev.weazyexe.moneta.ui.core.SearchTextField
import dev.weazyexe.moneta.ui.extensions.ReceiveEffect
import dev.weazyexe.moneta.ui.extensions.popWithResult
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrenciesBody(
    state: CurrenciesViewState,
    eventSink: (CurrenciesEvent) -> Unit,
    effects: Flow<CurrenciesEffect>,
    modifier: Modifier = Modifier
) {
    val nestedScroll = TopAppBarDefaults.pinnedScrollBehavior()
    val searchFieldFocus = remember { FocusRequester() }
    val navigator = LocalNavigator.currentOrThrow

    ReceiveEffect(effects) {
        when (this) {
            CurrenciesEffect.FocusSearchTextField -> {
                delay(200L)
                searchFieldFocus.requestFocus()
            }

            CurrenciesEffect.GoBack -> {
                navigator.pop()
            }

            is CurrenciesEffect.SelectCurrency -> {
                navigator.popWithResult(CurrenciesScreen().key, currency)
            }
        }
    }

    BackHandler {
        eventSink(CurrenciesEvent.OnBackClick)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    if (state.isSearchActive) {
                        SearchTextField(
                            text = state.searchQuery,
                            onTextChanged = {
                                eventSink(CurrenciesEvent.OnSearchFieldTextChange(it))
                            },
                            placeholder = stringResource(id = R.string.currencies_title_search_hint),
                            textStyle = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.focusRequester(searchFieldFocus)
                        )
                    } else {
                        Text(text = stringResource(id = R.string.currencies_title))
                    }
                },
                scrollBehavior = nestedScroll,
                navigationIcon = {
                    IconButton(
                        painter = painterResource(id = R.drawable.ic_arrow_back_24),
                        contentDescription = stringResource(id = R.string.content_description_back),
                        onClick = { eventSink(CurrenciesEvent.OnBackClick) }
                    )
                },
                actions = {
                    if (state.isSearchActive) {
                        IconButton(
                            painter = painterResource(id = R.drawable.ic_close_24),
                            contentDescription = stringResource(id = R.string.content_description_clear_search_field),
                            onClick = { eventSink(CurrenciesEvent.OnSearchFieldTextChange(query = "")) }
                        )
                    } else {
                        IconButton(
                            painter = painterResource(id = R.drawable.ic_search_24),
                            contentDescription = stringResource(id = R.string.content_description_search),
                            onClick = { eventSink(CurrenciesEvent.OnSearchActiveChange(isActive = true)) }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .imePadding()
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
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        eventSink(CurrenciesEvent.OnCurrencyClick(currency.code))
                                    }
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
        state = CurrenciesScreenPreviews.Content,
        eventSink = {},
        effects = emptyFlow()
    )
}

@Preview
@Composable
fun CurrenciesBodyLoadingPreview() {
    CurrenciesBody(
        state = CurrenciesScreenPreviews.Loading,
        eventSink = {},
        effects = emptyFlow()
    )
}

@Preview
@Composable
fun CurrenciesBodyErrorPreview() {
    CurrenciesBody(
        state = CurrenciesScreenPreviews.Error,
        eventSink = {},
        effects = emptyFlow()
    )
}

@Preview
@Composable
fun CurrenciesBodySearchPreview() {
    CurrenciesBody(
        state = CurrenciesScreenPreviews.SearchActive,
        eventSink = {},
        effects = emptyFlow()
    )
}

data object CurrenciesScreenPreviews {

    private val currencies = persistentListOf(
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

    val Loading = CurrenciesViewState(
        currencies = AsyncResult.Loading(),
        searchQuery = "",
        isSearchActive = false
    )

    val Error = CurrenciesViewState(
        currencies = AsyncResult.Error(Throwable("Error")),
        searchQuery = "",
        isSearchActive = false
    )

    val SearchActive = CurrenciesViewState(
        currencies = AsyncResult.Success(currencies),
        searchQuery = "Dollar",
        isSearchActive = true
    )

    val Content = CurrenciesViewState(
        currencies = AsyncResult.Success(currencies),
        searchQuery = "",
        isSearchActive = false
    )
}