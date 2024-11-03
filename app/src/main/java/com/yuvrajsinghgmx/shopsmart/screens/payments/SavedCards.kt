package com.yuvrajsinghgmx.shopsmart.screens.payments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R

data class CardDetails(
    val cardNumber: String,
    val cardholderName: String,
    val expiryMonth: String,
    val expiryYear: String,
    val cardType: String,
    val isDefault: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedCardsScreen(navController: NavController) {
    var showAddCardDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<CardDetails?>(null) }
    var editingCard by remember { mutableStateOf<CardDetails?>(null) }

    // Sample saved cards
    var savedCards by remember {
        mutableStateOf(
            listOf(
                CardDetails("**** **** **** 1234", "John Doe", "12", "25", "visa", true),
                CardDetails("**** **** **** 5678", "John Doe", "09", "24", "mastercard", false),
                CardDetails("**** **** **** 9012", "John Doe", "03", "26", "amex", false)
            )
        )
    }

    val lightBackgroundColor = Color(0xFFF6F5F3)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Saved Cards",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF332D25)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showAddCardDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_card_24px),
                            contentDescription = "Add Card"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = lightBackgroundColor
                )
            )
        },
        containerColor = lightBackgroundColor
    ) { innerPadding ->
        if (savedCards.isEmpty()) {
            EmptyCardsMessage(
                modifier = Modifier.padding(innerPadding),
                onAddCard = { showAddCardDialog = true }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(savedCards) { card ->
                    SavedCardItem(
                        card = card,
                        onEditCard = { editingCard = card },
                        onDeleteCard = { showDeleteDialog = card },
                        onSetDefault = { newDefaultCard ->
                            savedCards = savedCards.map { it.copy(isDefault = it == newDefaultCard) }
                        }
                    )
                }

                item {
                    OutlinedButton(
                        onClick = { showAddCardDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_card_24px),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add New Card")
                    }
                }
            }
        }
    }

    if (showAddCardDialog) {
        AddEditCardDialog(
            card = null,
            onDismiss = { showAddCardDialog = false },
            onSave = { newCard ->
                savedCards = savedCards + newCard
                showAddCardDialog = false
            }
        )
    }

    if (editingCard != null) {
        AddEditCardDialog(
            card = editingCard,
            onDismiss = { editingCard = null },
            onSave = { updatedCard ->
                savedCards = savedCards.map {
                    if (it.cardNumber == editingCard?.cardNumber) updatedCard else it
                }
                editingCard = null
            }
        )
    }

    if (showDeleteDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Remove Card") },
            text = {
                Text("Are you sure you want to remove this card ending in ${showDeleteDialog?.cardNumber?.takeLast(4)}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        savedCards = savedCards.filter { it != showDeleteDialog }
                        showDeleteDialog = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SavedCardItem(
    card: CardDetails,
    onEditCard: () -> Unit,
    onDeleteCard: () -> Unit,
    onSetDefault: (CardDetails) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(
                        id = when (card.cardType) {
                            "visa" -> R.drawable.payments_24px
                            "mastercard" -> R.drawable.payments_24px
                            "amex" -> R.drawable.payments_24px
                            else -> R.drawable.credit_card_24px
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.Unspecified
                )

                Row {
                    IconButton(onClick = onEditCard) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit_24px),
                            contentDescription = "Edit Card"
                        )
                    }
                    IconButton(onClick = onDeleteCard) {
                        Icon(
                            painter = painterResource(id = R.drawable.delete_24px),
                            contentDescription = "Delete Card",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = card.cardNumber,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = card.cardholderName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Expires ${card.expiryMonth}/${card.expiryYear}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                if (card.isDefault) {
                    AssistChip(
                        onClick = { },
                        label = { Text("Default") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.check_circle_24px),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                } else {
                    TextButton(onClick = { onSetDefault(card) }) {
                        Text("Set as Default")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditCardDialog(
    card: CardDetails?,
    onDismiss: () -> Unit,
    onSave: (CardDetails) -> Unit
) {
    var cardNumber by remember { mutableStateOf(card?.cardNumber ?: "") }
    var cardholderName by remember { mutableStateOf(card?.cardholderName ?: "") }
    var expiryMonth by remember { mutableStateOf(card?.expiryMonth ?: "") }
    var expiryYear by remember { mutableStateOf(card?.expiryYear ?: "") }
    var cvv by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (card == null) "Add New Card" else "Edit Card") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = {
                        if (it.length <= 19) cardNumber = formatCardNumber(it)
                    },
                    label = { Text("Card Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cardholderName,
                    onValueChange = { cardholderName = it },
                    label = { Text("Cardholder Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = expiryMonth,
                        onValueChange = { if (it.length <= 2) expiryMonth = it },
                        label = { Text("MM") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = expiryYear,
                        onValueChange = { if (it.length <= 2) expiryYear = it },
                        label = { Text("YY") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { if (it.length <= 4) cvv = it },
                        label = { Text("CVV") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cardType = when {
                        cardNumber.startsWith("4") -> "visa"
                        cardNumber.startsWith("5") -> "mastercard"
                        cardNumber.startsWith("3") -> "amex"
                        else -> "other"
                    }
                    onSave(
                        CardDetails(
                            cardNumber = maskCardNumber(cardNumber),
                            cardholderName = cardholderName,
                            expiryMonth = expiryMonth,
                            expiryYear = expiryYear,
                            cardType = cardType,
                            isDefault = card?.isDefault ?: false
                        )
                    )
                },
                enabled = isValidCard(cardNumber, cardholderName, expiryMonth, expiryYear, cvv)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun EmptyCardsMessage(
    modifier: Modifier = Modifier,
    onAddCard: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.credit_card_24px),
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Saved Cards",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Add a card to save it for future use",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onAddCard) {
            Icon(
                painter = painterResource(id = R.drawable.add_card_24px),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Card")
        }
    }
}

private fun formatCardNumber(number: String): String {
    val digitsOnly = number.filter { it.isDigit() }
    return digitsOnly.chunked(4).joinToString(" ")
}

private fun maskCardNumber(number: String): String {
    val last4 = number.filter { it.isDigit() }.takeLast(4)
    return "**** **** **** $last4"
}

private fun isValidCard(
    cardNumber: String,
    cardholderName: String,
    expiryMonth: String,
    expiryYear: String,
    cvv: String
): Boolean {
    val cardNumberValid = cardNumber.filter { it.isDigit() }.length >= 15
    val nameValid = cardholderName.isNotBlank() // Added missing parentheses
    val monthValid = try {
        expiryMonth.toInt() in 1..12
    } catch (e: NumberFormatException) {
        false
    }
    val yearValid = try {
        val year = expiryYear.toInt()
        val currentYear = java.time.LocalDate.now().year % 100
        year >= currentYear
    } catch (e: NumberFormatException) {
        false
    }
    val cvvValid = cvv.length in 3..4

    return cardNumberValid && nameValid && monthValid && yearValid && cvvValid
}