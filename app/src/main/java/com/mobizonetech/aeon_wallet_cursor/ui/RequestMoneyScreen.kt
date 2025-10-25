package com.mobizonetech.aeon_wallet_cursor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestMoneyScreen(
    onBackClick: () -> Unit,
    onRequestClick: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var requesterName by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    // Validation states
    var requesterNameError by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf("") }
    var noteError by remember { mutableStateOf("") }
    
    // Validation functions
    fun validateRequesterName(name: String): String {
        return when {
            name.isBlank() -> "Person name is required"
            name.length < 2 -> "Name must be at least 2 characters"
            name.length > 50 -> "Name must be less than 50 characters"
            !name.matches(Regex("^[a-zA-Z\\s]+$")) -> "Name can only contain letters and spaces"
            else -> ""
        }
    }
    
    fun validateAmount(amountStr: String): String {
        return when {
            amountStr.isBlank() -> "Amount is required"
            amountStr.toDoubleOrNull() == null -> "Please enter a valid amount"
            amountStr.toDoubleOrNull()!! <= 0 -> "Amount must be greater than 0"
            amountStr.toDoubleOrNull()!! > 10000 -> "Amount cannot exceed $10,000"
            amountStr.contains(Regex("\\d+\\.\\d{3,}")) -> "Amount can have maximum 2 decimal places"
            else -> ""
        }
    }
    
    fun validateNote(noteStr: String): String {
        return when {
            noteStr.length > 200 -> "Note must be less than 200 characters"
            else -> ""
        }
    }
    
    fun isFormValid(): Boolean {
        return requesterNameError.isEmpty() && amountError.isEmpty() && noteError.isEmpty() &&
               requesterName.isNotBlank() && amount.isNotBlank()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Money") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Requester Information
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Request From",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = requesterName,
                        onValueChange = { 
                            requesterName = it
                            requesterNameError = validateRequesterName(it)
                        },
                        label = { Text("Person Name") },
                        placeholder = { Text("Enter person name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        isError = requesterNameError.isNotEmpty(),
                        supportingText = if (requesterNameError.isNotEmpty()) {
                            { Text(requesterNameError, color = MaterialTheme.colorScheme.error) }
                        } else null,
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person") }
                    )
                }
            }

            // Amount Information
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Amount",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { 
                            amount = it
                            amountError = validateAmount(it)
                        },
                        label = { Text("Amount") },
                        placeholder = { Text("0.00") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = amountError.isNotEmpty(),
                        supportingText = if (amountError.isNotEmpty()) {
                            { Text(amountError, color = MaterialTheme.colorScheme.error) }
                        } else null,
                        leadingIcon = { Icon(Icons.Default.Star, contentDescription = "Money") }
                    )
                }
            }

            // Note
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Note (Optional)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = note,
                        onValueChange = { 
                            note = it
                            noteError = validateNote(it)
                        },
                        label = { Text("Add a note") },
                        placeholder = { Text("What's this for?") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = noteError.isNotEmpty(),
                        supportingText = if (noteError.isNotEmpty()) {
                            { Text(noteError, color = MaterialTheme.colorScheme.error) }
                        } else {
                            { Text("${note.length}/200 characters") }
                        },
                        leadingIcon = { Icon(Icons.Default.Info, contentDescription = "Note") }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Request Button
            Button(
                onClick = {
                    isLoading = true
                    onRequestClick(requesterName, amount, note)
                },
                enabled = isFormValid() && !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Send Request",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
