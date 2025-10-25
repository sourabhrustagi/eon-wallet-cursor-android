package com.mobizonetech.aeon_wallet_cursor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobizonetech.aeon_wallet_cursor.ui.theme.AeonwalletcursorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterIdScreen(
    onBackClick: () -> Unit = {},
    onContinueClick: (id: String, cardNumber: String, cardType: String) -> Unit = { _, _, _ -> }
) {
    var selectedIdType by remember { mutableStateOf("") }
    var idNumber by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    
    val idTypes = listOf(
        "Passport",
        "National ID",
        "Military ID",
        "Aadhaar Card",
        "Social Security Number"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Enter Your ID",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Progress Indicator
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Step 1 of 5",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = 0.2f,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Form
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // ID Type Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedIdType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select ID Type") },
                    placeholder = { Text("Choose your ID type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    idTypes.forEach { idType ->
                        DropdownMenuItem(
                            text = { Text(idType) },
                            onClick = {
                                selectedIdType = idType
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            // ID Number Input
            OutlinedTextField(
                value = idNumber,
                onValueChange = { idNumber = it },
                label = { Text("${if (selectedIdType.isNotEmpty()) selectedIdType else "ID"} Number") },
                placeholder = { 
                    Text(
                        when (selectedIdType) {
                            "Passport" -> "e.g., A1234567"
                            "National ID" -> "e.g., NID123456789"
                            "Military ID" -> "e.g., MIL123456"
                            "Aadhaar Card" -> "e.g., 1234 5678 9012"
                            "Social Security Number" -> "e.g., 123-45-6789"
                            else -> "Enter your ID number"
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                supportingText = {
                    if (selectedIdType.isNotEmpty()) {
                        Text(
                            text = "Enter your ${selectedIdType.lowercase()} number",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Continue Button
        Button(
            onClick = { 
                if (selectedIdType.isNotBlank() && idNumber.isNotBlank()) {
                    onContinueClick(selectedIdType, idNumber, selectedIdType)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = selectedIdType.isNotBlank() && idNumber.isNotBlank()
        ) {
            Text(
                text = "Continue",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Security Notice
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = "🔒 Your information is encrypted and secure. We use bank-level security to protect your data.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                lineHeight = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EnterIdScreenPreview() {
    AeonwalletcursorTheme {
        EnterIdScreen()
    }
}
