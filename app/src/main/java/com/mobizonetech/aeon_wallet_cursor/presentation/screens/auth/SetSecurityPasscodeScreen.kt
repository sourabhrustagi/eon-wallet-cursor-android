package com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth

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
fun SetSecurityPasscodeScreen(
    onBackClick: () -> Unit = {},
    onSecurityPasscodeSet: (securityPasscode: String) -> Unit = {}
) {
    var securityPasscode by remember { mutableStateOf("") }
    var confirmSecurityPasscode by remember { mutableStateOf("") }
    
    val isSecurityPasscodeValid = securityPasscode.length >= 8
    val isConfirmValid = confirmSecurityPasscode.length >= 8 && securityPasscode == confirmSecurityPasscode
    
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
                text = "Security Passcode",
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
                    text = "Step 4 of 5",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = 0.8f,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Security Icon
        Card(
            modifier = Modifier.size(80.dp),
            shape = androidx.compose.foundation.shape.CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🛡️",
                    fontSize = 32.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Instructions
        Text(
            text = "Set Security Passcode",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Create a strong security passcode for sensitive operations",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Security Passcode Input Fields
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // New Security Passcode
            OutlinedTextField(
                value = securityPasscode,
                onValueChange = { 
                    if (it.length <= 8 && it.all { char -> char.isDigit() }) {
                        securityPasscode = it
                    }
                },
                label = { Text("Enter Security Passcode") },
                placeholder = { Text("Enter 8 digits") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    Text(
                        text = "${securityPasscode.length}/8 digits",
                        fontSize = 12.sp,
                        color = if (isSecurityPasscodeValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                },
                isError = securityPasscode.isNotEmpty() && !isSecurityPasscodeValid
            )
            
            // Confirm Security Passcode
            OutlinedTextField(
                value = confirmSecurityPasscode,
                onValueChange = { 
                    if (it.length <= 8 && it.all { char -> char.isDigit() }) {
                        confirmSecurityPasscode = it
                    }
                },
                label = { Text("Confirm Security Passcode") },
                placeholder = { Text("Confirm 8 digits") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    if (confirmSecurityPasscode.isNotEmpty()) {
                        Text(
                            text = if (securityPasscode == confirmSecurityPasscode) "✓ Passcodes match" else "✗ Passcodes don't match",
                            fontSize = 12.sp,
                            color = if (securityPasscode == confirmSecurityPasscode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = confirmSecurityPasscode.isNotEmpty() && securityPasscode != confirmSecurityPasscode
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Security Requirements
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Security Passcode Requirements:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Exactly 8 digits\n• Use different numbers\n• Different from your regular passcode\n• Used for sensitive operations like transfers",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    lineHeight = 18.sp
                )
            }
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
                text = "🔒 This passcode will be required for:\n• Sending money\n• Changing security settings\n• Accessing sensitive features",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp),
                lineHeight = 16.sp
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Continue Button
        Button(
            onClick = { 
                if (isSecurityPasscodeValid && isConfirmValid) {
                    onSecurityPasscodeSet(securityPasscode)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = isSecurityPasscodeValid && isConfirmValid
        ) {
            Text(
                text = "Complete Setup",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Final Security Notice
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
            )
        ) {
            Text(
                text = "⚠️ Keep both passcodes secure! Write them down in a safe place.",
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
fun SetSecurityPasscodeScreenPreview() {
    AeonwalletcursorTheme {
        SetSecurityPasscodeScreen()
    }
}
