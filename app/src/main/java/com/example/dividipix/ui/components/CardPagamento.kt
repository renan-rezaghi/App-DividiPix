package com.example.dividipix.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dividipix.data.model.Pagamento
import com.example.dividipix.ui.theme.Pago
import com.example.dividipix.ui.theme.Pendente

@Composable
fun CardPagamento(
    pagamento: Pagamento,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp),

            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = pagamento.participanteNome,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "R$ %.2f".format(pagamento.valor),
                style = MaterialTheme.typography.headlineSmall
            )

            if (pagamento.descricao.isNotBlank()) {

                Text(
                    text = pagamento.descricao,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = if (pagamento.pago) {
                    "● Pago"
                } else {
                    "● Pendente"
                },

                style = MaterialTheme.typography.labelLarge,

                color = if (pagamento.pago) {
                    Pago
                } else {
                    Pendente
                }
            )

            if (pagamento.comprovante.isNotBlank()) {

                Text(
                    text = "Comprovante: ${pagamento.comprovante}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.End
            ) {

                TextButton(
                    onClick = onEditar
                ) {
                    Text("Editar")
                }

                Button(
                    onClick = onExcluir
                ) {
                    Text("Excluir")
                }
            }
        }
    }
}