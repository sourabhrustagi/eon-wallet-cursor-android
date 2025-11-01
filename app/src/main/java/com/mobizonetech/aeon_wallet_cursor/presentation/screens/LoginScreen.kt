package com.mobizonetech.aeon_wallet_cursor.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mobizonetech.aeon_wallet_cursor.R
import com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel.LoginViewModel
import com.mobizonetech.aeon_wallet_cursor.presentation.viewmodel.LoginUiState
import com.mobizonetech.aeon_wallet_cursor.ui.theme.AeonwalletcursorTheme
import com.mobizonetech.aeon_wallet_cursor.util.Constants
import kotlinx.coroutines.launch

/**
 * Login Screen
 * Allows users to sign in with email/phone and password
 * 
 * Features:
 * - Email/Phone and password input fields
 * - Real-time validation
 * - Loading and error states
 * - Password visibility toggle
 * - Gradient background matching app theme
 * 
 * @param viewModel ViewModel for managing login state
 * @param onLoginSuccess Callback when login is successful
 * @param onSignUpClick Callback when user wants to sign up
 * @param onForgotPasswordClick Callback when user clicks forgot password
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }
    
    // Navigate on successful login
    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
            onLoginSuccess()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(Constants.GRADIENT_START),
                        Color(Constants.GRADIENT_END)
                    )
                )
            )
    ) {
        // Show loading overlay
        if (uiState.isLoading) {
            LoadingOverlay()
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Constants.SPACING_LARGE.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            LoginTitle()
            
            Spacer(modifier = Modifier.height(Constants.SPACING_EXTRA_LARGE.dp))
            
            // Login Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Constants.CARD_CORNER_RADIUS.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = Constants.ELEVATION_DEFAULT.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(Constants.SPACING_LARGE.dp),
                    verticalArrangement = Arrangement.spacedBy(Constants.SPACING_MEDIUM.dp)
                ) {
                    // Email/Phone Field
                    EmailOrPhoneField(
                        value = uiState.emailOrPhone,
                        error = uiState.emailOrPhoneError,
                        onValueChange = viewModel::updateEmailOrPhone,
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                    
                    // Password Field
                    PasswordField(
                        value = uiState.password,
                        error = uiState.passwordError,
                        passwordVisible = passwordVisible,
                        onValueChange = viewModel::updatePassword,
                        onToggleVisibility = { passwordVisible = !passwordVisible },
                        onDone = {
                            focusManager.clearFocus()
                            if (uiState.isLoginButtonEnabled) {
                                viewModel.login()
                            }
                        }
                    )
                    
                    // Error Message
                    uiState.error?.let { error ->
                        ErrorMessage(
                            message = error,
                            onDismiss = viewModel::clearError
                        )
                    }
                    
                    // Forgot Password
                    TextButton(
                        onClick = onForgotPasswordClick,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = stringResource(R.string.login_forgot_password),
                            color = Color(Constants.PRIMARY_COLOR),
                            fontSize = Constants.TEXT_SIZE_SMALL.sp
                        )
                    }
                    
                    // Login Button
                    Button(
                        onClick = viewModel::login,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Constants.BUTTON_HEIGHT.dp),
                        enabled = uiState.isLoginButtonEnabled,
                        shape = RoundedCornerShape(Constants.BUTTON_CORNER_RADIUS.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(Constants.PRIMARY_COLOR)
                        )
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.login_button),
                                fontSize = Constants.TEXT_SIZE_MEDIUM.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(Constants.SPACING_LARGE.dp))
            
            // Sign Up Link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.login_no_account),
                    color = Color.White.copy(alpha = Constants.ALPHA_SEMI_TRANSPARENT),
                    fontSize = Constants.TEXT_SIZE_NORMAL.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(onClick = onSignUpClick) {
                    Text(
                        text = stringResource(R.string.login_sign_up),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = Constants.TEXT_SIZE_NORMAL.sp
                    )
                }
            }
        }
    }
}

/**
 * Login title composable
 */
@Composable
private fun LoginTitle() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login_title),
            fontSize = Constants.TEXT_SIZE_LARGE.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Constants.SPACING_SMALL.dp))
        Text(
            text = stringResource(R.string.login_subtitle),
            fontSize = Constants.TEXT_SIZE_NORMAL.sp,
            color = Color.White.copy(alpha = Constants.ALPHA_SEMI_TRANSPARENT),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Email or Phone input field
 */
@Composable
private fun EmailOrPhoneField(
    value: String,
    error: String?,
    onValueChange: (String) -> Unit,
    onNext: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.login_email_or_phone)) },
        placeholder = { Text(stringResource(R.string.login_email_or_phone_hint)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = Color(Constants.PRIMARY_COLOR)
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNext() }
        ),
        isError = error != null,
        supportingText = {
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(Constants.PRIMARY_COLOR),
            unfocusedBorderColor = Color.Gray,
            errorBorderColor = MaterialTheme.colorScheme.error
        )
    )
}

/**
 * Password input field
 */
@Composable
private fun PasswordField(
    value: String,
    error: String?,
    passwordVisible: Boolean,
    onValueChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    onDone: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.login_password)) },
        placeholder = { Text(stringResource(R.string.login_password_hint)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color(Constants.PRIMARY_COLOR)
            )
        },
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = Color(Constants.PRIMARY_COLOR)
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() }
        ),
        isError = error != null,
        supportingText = {
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(Constants.PRIMARY_COLOR),
            unfocusedBorderColor = Color.Gray,
            errorBorderColor = MaterialTheme.colorScheme.error
        )
    )
}

/**
 * Error message composable
 */
@Composable
private fun ErrorMessage(
    message: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Constants.SPACING_MEDIUM.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontSize = Constants.TEXT_SIZE_SMALL.sp,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Loading overlay composable
 */
@Composable
private fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 4.dp
        )
    }
}

// =============================================================================
// Preview Functions
// =============================================================================

@Preview(
    name = "Login Screen - Light",
    showBackground = true
)
@Composable
private fun LoginScreenPreview() {
    AeonwalletcursorTheme {
        LoginScreen()
    }
}
