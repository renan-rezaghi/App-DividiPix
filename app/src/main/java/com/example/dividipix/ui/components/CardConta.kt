package com.example.dividipix.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dividipix.data.model.Conta

@Composable
fun CardConta(
    conta: Conta,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,

        modifier = Modifier
            .fillMaxWidth(),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp),

            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = conta.nome,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            if (conta.descricao.isNotBlank()) {

                Text(
                    text = conta.descricao,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {

                    Text(
                        text = "Valor total",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "R$ %.2f".format(conta.valorTotal),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Column {

                    Text(
                        text = "Data",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = conta.data.ifBlank { "Não informada" },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}