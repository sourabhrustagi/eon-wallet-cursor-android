package com.mobizonetech.aeon_wallet_cursor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun SetPasscodeScreen(
    onBackClick: () -> Unit = {},
    onPasscodeSet: (passcode: String) -> Unit = {}
) {
    var passcode by remember { mutableStateOf("") }
    var confirmPasscode by remember { mutableStateOf("") }
    
    val isPasscodeValid = passcode.length >= 6
    val isConfirmValid = confirmPasscode.length >= 6 && passcode == confirmPasscode
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
                text = "Set Passcode",
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
                    text = "Step 3 of 5",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = 0.6f,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Passcode Icon
        Card(
            modifier = Modifier.size(80.dp),
            shape = androidx.compose.foundation.shape.CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🔑",
                    fontSize = 32.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Instructions
        Text(
            text = "Create Your Passcode",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Choose a secure 6-digit passcode to protect your wallet",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Passcode Input Fields
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // New Passcode
            OutlinedTextField(
                value = passcode,
                onValueChange = { 
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                        passcode = it
                    }
                },
                label = { Text("Enter Passcode") },
                placeholder = { Text("123456") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    Text(
                        text = "${passcode.length}/6 digits",
                        fontSize = 12.sp,
                        color = if (isPasscodeValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                },
                isError = passcode.isNotEmpty() && !isPasscodeValid
            )
            
            // Confirm Passcode
            OutlinedTextField(
                value = confirmPasscode,
                onValueChange = { 
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                        confirmPasscode = it
                    }
                },
                label = { Text("Confirm Passcode") },
                placeholder = { Text("123456") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    if (confirmPasscode.isNotEmpty()) {
                        Text(
                            text = if (passcode == confirmPasscode) "✓ Passcodes match" else "✗ Passcodes don't match",
                            fontSize = 12.sp,
                            color = if (passcode == confirmPasscode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = confirmPasscode.isNotEmpty() && passcode != confirmPasscode
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Passcode Requirements
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Passcode Requirements:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Must be exactly 6 digits\n• Use different numbers\n• Don't use sequential numbers (123456)\n• Don't use repeated numbers (111111)",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Continue Button
        Button(
            onClick = { 
                if (isPasscodeValid && isConfirmValid) {
                    onPasscodeSet(passcode)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = isPasscodeValid && isConfirmValid
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
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = "⚠️ Remember your passcode! We cannot recover it if you forget.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                lineHeight = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SetPasscodeScreenPreview() {
    AeonwalletcursorTheme {
        SetPasscodeScreen()
    }
}
