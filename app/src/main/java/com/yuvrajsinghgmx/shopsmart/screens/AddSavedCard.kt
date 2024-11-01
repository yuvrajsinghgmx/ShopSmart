package com.yuvrajsinghgmx.shopsmart.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.profilefeatures.SavedCardsManager
import java.util.Calendar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class CardDetail(
    val cardNumber: TextFieldValue = TextFieldValue(""),
    val cardholderName: String = "",
    val expiryMonth: String = "",
    val expiryYear: String = "",
    val cvv: String = "",
    var cardType: CardType = CardType.UNKNOWN
)

enum class CardType(val icon: Int, val pattern: Regex, val length: List<Int>) {
    VISA(R.drawable.visa_large, Regex("^4[0-9]*$"), listOf(16)),
    MASTERCARD(R.drawable.mastercard_large, Regex("^5[1-5][0-9]*$"), listOf(16)),
    AMEX(R.drawable.amex_large, Regex("^3[47][0-9]*$"), listOf(15)),
    UNKNOWN(R.drawable.credit_card_24px, Regex(".*"), listOf(16))
}

data class CardValidation(
    val isValid: Boolean = true,
    val errorMessage: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSavedCardScreen(navController: NavController) {
    var cardDetail by remember { mutableStateOf(CardDetail()) }
    var validations by remember { mutableStateOf(mapOf<String, CardValidation>()) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    val isFormValid = validations.all { it.value.isValid } &&
            cardDetail.cardNumber.text.isNotBlank() &&  // Added .text
            cardDetail.cardholderName.isNotBlank() &&
            cardDetail.expiryMonth.isNotBlank() &&
            cardDetail.expiryYear.isNotBlank() &&
            cardDetail.cvv.isNotBlank()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Add Card",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF332D25)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back_24px),
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF6F5F3)
                )
            )
        },
        containerColor = Color(0xFFF6F5F3)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Security Notice
            item {
                SecurityNoticeCard()
            }

            // Card Input Form
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Card Number
                        CardNumberTextField(
                            value = cardDetail.cardNumber.text,
                            onValueChange = { newValue ->
                                if (newValue.length <= 19) {
                                    val digitsOnly = newValue.filter { it.isDigit() }
                                    cardDetail = cardDetail.copy(
                                        cardNumber = TextFieldValue(newValue),
                                        cardType = detectCardType(digitsOnly)
                                    )
                                    validations = validations + ("cardNumber" to validateCardNumber(digitsOnly))
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            cardType = cardDetail.cardType,
                            isError = validations["cardNumber"]?.isValid == false,
                            errorMessage = validations["cardNumber"]?.takeIf { !it.isValid }?.errorMessage
                        )

                        // Cardholder Name
                        OutlinedTextField(
                            value = cardDetail.cardholderName,
                            onValueChange = { value ->
                                cardDetail = cardDetail.copy(cardholderName = value.uppercase())
                                validations = validations + ("cardholderName" to validateCardholderName(value))
                            },
                            label = { Text("Cardholder Name") },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.person_24px),
                                    contentDescription = null
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Characters
                            ),
                            singleLine = true,
                            isError = validations["cardholderName"]?.isValid == false,
                            supportingText = validations["cardholderName"]?.let { validation ->
                                if (!validation.isValid) {
                                    { Text(validation.errorMessage) }
                                } else null
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Expiry Date and CVV Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Expiry Month
                            OutlinedTextField(
                                value = cardDetail.expiryMonth,
                                onValueChange = { value ->
                                    if (value.length <= 2) {
                                        cardDetail = cardDetail.copy(expiryMonth = value)
                                        validations = validations + ("expiryMonth" to validateExpiryMonth(value))
                                    }
                                },
                                label = { Text("MM") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true,
                                isError = validations["expiryMonth"]?.isValid == false,
                                supportingText = validations["expiryMonth"]?.let { validation ->
                                    if (!validation.isValid) {
                                        { Text(validation.errorMessage) }
                                    } else null
                                },
                                modifier = Modifier.weight(1f)
                            )

                            // Expiry Year
                            OutlinedTextField(
                                value = cardDetail.expiryYear,
                                onValueChange = { value ->
                                    if (value.length <= 2) {
                                        cardDetail = cardDetail.copy(expiryYear = value)
                                        validations = validations + ("expiryYear" to validateExpiryYear(value))
                                    }
                                },
                                label = { Text("YY") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true,
                                isError = validations["expiryYear"]?.isValid == false,
                                supportingText = validations["expiryYear"]?.let { validation ->
                                    if (!validation.isValid) {
                                        { Text(validation.errorMessage) }
                                    } else null
                                },
                                modifier = Modifier.weight(1f)
                            )

                            // CVV
                            OutlinedTextField(
                                value = cardDetail.cvv,
                                onValueChange = { value ->
                                    if (value.length <= 4) {
                                        cardDetail = cardDetail.copy(cvv = value)
                                        validations = validations + ("cvv" to validateCVV(value, cardDetail.cardType))
                                    }
                                },
                                label = { Text("CVV") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                isError = validations["cvv"]?.isValid == false,
                                supportingText = validations["cvv"]?.let { validation ->
                                    if (!validation.isValid) {
                                        { Text(validation.errorMessage) }
                                    } else null
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Save Button
                        Button(
                            onClick = {
                                scope.launch {
                                    isLoading = true
                                    // Simulate API call
                                    delay(1500)

                                    // Add the new card to SavedCardsManager
                                    val lastFourDigits = cardDetail.cardNumber.text.filter { it.isDigit() }.takeLast(4)
                                    val expiryDate = "${cardDetail.expiryMonth}/${cardDetail.expiryYear}"

                                    SavedCardsManager.addCard(
                                        PaymentMethodInfo(
                                            "•••• •••• •••• $lastFourDigits",
                                            "Expires $expiryDate",
                                            R.drawable.credit_card_24px,
                                            "saved_cards"
                                        )
                                    )

                                    isLoading = false
                                    showSuccessDialog = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = isFormValid && !isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF006D40)
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Save Card")
                            }
                        }
                    }
                }
            }

            // Supported Cards Section
            item {
                SupportedCardsSection()
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                navController.navigateUp()
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.check_circle_24px),
                    contentDescription = null,
                    tint = Color(0xFF006D40)
                )
            },
            title = {
                Text("Card Added Successfully")
            },
            text = {
                Text("Your card has been securely saved for future use.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigateUp()
                    }
                ) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
private fun SecurityNoticeCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFFE7F5EC)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.security_24px),
                contentDescription = null,
                tint = Color(0xFF006D40)
            )
            Column {
                Text(
                    "Secure Card Storage",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF006D40)
                )
                Text(
                    "Your card information is encrypted and securely stored according to PCI DSS standards",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF006D40)
                )
                Text(
                    "For testing, use:\nVisa: 4532015112830366\nMastercard: 5425233430109903\nAMEX: 374245455400126",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF006D40),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun SupportedCardsSection() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Supported Cards",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.visa_large),
                    contentDescription = "Visa",
                    modifier = Modifier.height(24.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.mastercard_large),
                    contentDescription = "Mastercard",
                    modifier = Modifier.height(24.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.amex_large),
                    contentDescription = "American Express",
                    modifier = Modifier.height(24.dp)
                )
            }
        }
    }
}

@Composable
private fun CardNumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    cardType: CardType = CardType.UNKNOWN,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    var previousValue by remember { mutableStateOf("") }

    OutlinedTextField(
        value = textFieldValueState,
        onValueChange = { newValue ->
            // Get cursor position
            val cursorPosition = newValue.selection.start
            // Get raw text without spaces
            val newText = newValue.text.filter { it.isDigit() }

            if (newText.length <= 16) {
                // Format the text with spaces
                val formattedText = newText.chunked(4).joinToString(" ")
                // Calculate new cursor position
                val spacesBeforeCursor = formattedText.take(cursorPosition).count { it == ' ' }
                val originalSpacesBeforeCursor = textFieldValueState.text.take(cursorPosition).count { it == ' ' }
                val cursorOffset = spacesBeforeCursor - originalSpacesBeforeCursor

                val newCursorPosition = when {
                    newText.length > previousValue.length -> // Adding digit
                        minOf(cursorPosition + cursorOffset + 1, formattedText.length)
                    else -> // Removing digit
                        maxOf(cursorPosition + cursorOffset, 0)
                }

                textFieldValueState = TextFieldValue(
                    text = formattedText,
                    selection = TextRange(newCursorPosition)
                )
                onValueChange(formattedText)
                previousValue = newText
            }
        },
        label = { Text("Card Number") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = cardType.icon),
                contentDescription = null,
                tint = Color.Unspecified
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        singleLine = true,
        isError = isError,
        supportingText = errorMessage?.let { { Text(it) } },
        modifier = modifier
    )
}

// Utility Functions
private fun formatCardNumber(number: String): String {
    val digitsOnly = number.filter { it.isDigit() }
    return digitsOnly.chunked(4).joinToString(" ")
}

private fun detectCardType(number: String): CardType {
    return CardType.values().find { type ->
        type.pattern.matches(number) && (number.isEmpty() || type.length.contains(number.length))
    } ?: CardType.UNKNOWN
}

private fun validateCardNumber(number: String): CardValidation {
    val digitsOnly = number.filter { it.isDigit() }

    // Test card numbers
    val validTestCards = listOf(
        "4532015112830366",   // Visa test numbers
        "4532858337632948",
        "4532796041295796",

        "5425233430109903",   // Mastercard test numbers
        "2223000048410010",

        "374245455400126",    // Amex test numbers
        "378282246310005",
        "371449635398431"
    )

    return when {
        digitsOnly.isEmpty() -> CardValidation(true)
        digitsOnly.length < 15 -> CardValidation(false, "Card number is too short")
        digitsOnly.length > 16 -> CardValidation(false, "Card number is too long")
        !validTestCards.contains(digitsOnly) -> CardValidation(false, "Please use test card numbers provided above")
        else -> CardValidation(true)
    }
}

private fun validateCardholderName(name: String): CardValidation {
    return when {
        name.isEmpty() -> CardValidation(true)
        name.length < 3 -> CardValidation(false, "Name is too short")
        !name.matches(Regex("^[A-Z ]+$")) -> CardValidation(false, "Use only letters and spaces")
        else -> CardValidation(true)
    }
}

private fun validateExpiryMonth(month: String): CardValidation {
    return when {
        month.isEmpty() -> CardValidation(true)
        month.length != 2 -> CardValidation(false, "Enter 2 digits")
        !month.matches(Regex("^(0[1-9]|1[0-2])$")) -> CardValidation(false, "Invalid month")
        else -> CardValidation(true)
    }
}

private fun validateExpiryYear(year: String): CardValidation {
    if (year.isEmpty()) return CardValidation(true)
    if (year.length != 2) return CardValidation(false, "Enter 2 digits")

    val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100
    val yearInt = year.toIntOrNull() ?: return CardValidation(false, "Invalid year")

    return when {
        yearInt < currentYear -> CardValidation(false, "Card expired")
        yearInt > currentYear + 10 -> CardValidation(false, "Year too far in future")
        else -> CardValidation(true)
    }
}

private fun validateCVV(cvv: String, cardType: CardType): CardValidation {
    return when {
        cvv.isEmpty() -> CardValidation(true)
        !cvv.matches(Regex("^[0-9]+$")) -> CardValidation(false, "Numbers only")
        cardType == CardType.AMEX && cvv.length != 4 -> CardValidation(false, "4 digits for AMEX")
        cardType != CardType.AMEX && cvv.length != 3 -> CardValidation(false, "3 digits required")
        else -> CardValidation(true)
    }
}

// Luhn Algorithm for card number validation
private fun isLuhnValid(number: String): Boolean {
    var sum = 0
    var alternate = false

    // Loop through values starting from the rightmost digit
    for (i in number.length - 1 downTo 0) {
        var n = number[i].toString().toInt()
        if (alternate) {
            n *= 2
            if (n > 9) {
                n = (n % 10) + 1
            }
        }
        sum += n
        alternate = !alternate
    }
    return (sum % 10 == 0)
}

// Credit card brand images
@Composable
private fun CreditCardBrandImage(
    brandResId: Int,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = brandResId),
        contentDescription = contentDescription,
        modifier = modifier.height(24.dp)
    )
}