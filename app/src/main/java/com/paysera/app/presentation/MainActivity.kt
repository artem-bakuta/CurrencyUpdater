package com.paysera.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paysera.app.R
import com.paysera.app.presentation.viewmodel.ExchangeRateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ExchangeRateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
        setContent {
            MainScreen(viewModel)
        }
    }
}

@Composable
fun MainScreen(viewModel: ExchangeRateViewModel) {
    val model = viewModel.latestRate.observeAsState().value

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(
                R.string.latest_date, model?.date ?: stringResource(R.string.n_a)
            ),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = model?.base ?: stringResource(R.string.n_a),
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )

        ExchangeRateList(model?.rates?.map { Pair(it.key, it.value) } ?: emptyList())
    }
}


@Composable
fun ExchangeRateItem(pair: Pair<String, Double>) {
    // Compose item layout here
    // Example: Display base, date, and rates
    Text(text = "Currency: ${pair.first}", fontSize = 14.sp, color = Color.Red)
    Text(text = "Date: ${pair.second}", fontSize = 14.sp, color = Color.Green)
}

@Composable
fun ExchangeRateList(exchangeRates: List<Pair<String, Double>>) =
    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(exchangeRates) { exchangeRate ->
            ExchangeRateItem(pair = exchangeRate)
        }
    }

