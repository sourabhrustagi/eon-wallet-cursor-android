package com.mobizonetech.aeon_wallet_cursor.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanCardApplicationScreen(
    onBackClick: () -> Unit,
    onApplicationSubmitted: () -> Unit
) {
    var currentStep by remember { mutableStateOf(1) }
    var personalInfo by remember { mutableStateOf(PersonalInfo()) }
    var otp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var documentsUploaded by remember { mutableStateOf(false) }
    var salaryInfo by remember { mutableStateOf(SalaryInfo()) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val totalSteps = 5

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Apply for Loan/Card") },
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
                .padding(16.dp)
        ) {
            // Progress indicator
            LinearProgressIndicator(
                progress = currentStep.toFloat() / totalSteps,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Step indicator
            Text(
                text = "Step $currentStep of $totalSteps",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (currentStep) {
                1 -> PersonalInfoStep(
                    personalInfo = personalInfo,
                    onPersonalInfoChanged = { personalInfo = it },
                    onBack = { onBackClick() },
                    onNext = { currentStep = 2 }
                )
                2 -> OtpEmailStep(
                    email = email,
                    onEmailChanged = { email = it },
                    otp = otp,
                    onOtpChanged = { otp = it },
                    onBack = { currentStep = 1 },
                    onNext = { currentStep = 3 },
                    isLoading = isLoading,
                    onSendOtp = {
                        isLoading = true
                        coroutineScope.launch {
                            kotlinx.coroutines.delay(2000)
                            isLoading = false
                        }
                    }
                )
                3 -> DocumentUploadStep(
                    documentsUploaded = documentsUploaded,
                    onDocumentsUploaded = { documentsUploaded = it },
                    onBack = { currentStep = 2 },
                    onNext = { currentStep = 4 }
                )
                4 -> SalaryInfoStep(
                    salaryInfo = salaryInfo,
                    onSalaryInfoChanged = { salaryInfo = it },
                    onBack = { currentStep = 3 },
                    onNext = { currentStep = 5 }
                )
                5 -> ReviewStep(
                    selectedType = "Loan/Card Application",
                    personalInfo = personalInfo,
                    email = email,
                    salaryInfo = salaryInfo,
                    onBack = { currentStep = 4 },
                    onSubmit = {
                        isLoading = true
                        coroutineScope.launch {
                            kotlinx.coroutines.delay(3000)
                            isLoading = false
                            onApplicationSubmitted()
                        }
                    },
                    isLoading = isLoading
                )
            }
        }
    }
}

@Composable
fun PersonalInfoStep(
    personalInfo: PersonalInfo,
    onPersonalInfoChanged: (PersonalInfo) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column {
        Text(
            text = "Personal Information",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = personalInfo.firstName,
            onValueChange = { onPersonalInfoChanged(personalInfo.copy(firstName = it)) },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = personalInfo.firstName.isNotEmpty() && personalInfo.firstName.length < 2,
            supportingText = {
                if (personalInfo.firstName.isNotEmpty() && personalInfo.firstName.length < 2) {
                    Text("First name must be at least 2 characters", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = personalInfo.lastName,
            onValueChange = { onPersonalInfoChanged(personalInfo.copy(lastName = it)) },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = personalInfo.lastName.isNotEmpty() && personalInfo.lastName.length < 2,
            supportingText = {
                if (personalInfo.lastName.isNotEmpty() && personalInfo.lastName.length < 2) {
                    Text("Last name must be at least 2 characters", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = personalInfo.phone,
            onValueChange = { onPersonalInfoChanged(personalInfo.copy(phone = it)) },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = personalInfo.phone.isNotEmpty() && !personalInfo.isValidPhone(),
            supportingText = {
                if (personalInfo.phone.isNotEmpty() && !personalInfo.isValidPhone()) {
                    Text("Please enter a valid phone number", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = personalInfo.address,
            onValueChange = { onPersonalInfoChanged(personalInfo.copy(address = it)) },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            minLines = 2,
            isError = personalInfo.address.isNotEmpty() && personalInfo.address.length < 10,
            supportingText = {
                if (personalInfo.address.isNotEmpty() && personalInfo.address.length < 10) {
                    Text("Address must be at least 10 characters", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                enabled = personalInfo.isValid()
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun OtpEmailStep(
    email: String,
    onEmailChanged: (String) -> Unit,
    otp: String,
    onOtpChanged: (String) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
    isLoading: Boolean,
    onSendOtp: () -> Unit
) {
    Column {
        Text(
            text = "Email & OTP Verification",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChanged,
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = email.isNotEmpty() && !isValidEmail(email),
            supportingText = {
                if (email.isNotEmpty() && !isValidEmail(email)) {
                    Text("Please enter a valid email address", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSendOtp,
            modifier = Modifier.fillMaxWidth(),
            enabled = isValidEmail(email) && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Send OTP")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { if (it.length <= 6 && it.all { char -> char.isDigit() }) onOtpChanged(it) },
            label = { Text("Enter OTP") },
            placeholder = { Text("123456") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = { Text("Enter the 6-digit OTP sent to your email") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                enabled = otp.length == 6
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun DocumentUploadStep(
    documentsUploaded: Boolean,
    onDocumentsUploaded: (Boolean) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column {
        Text(
            text = "Upload Documents",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (documentsUploaded) 
                    MaterialTheme.colorScheme.primaryContainer 
                else MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = if (documentsUploaded) 
                        MaterialTheme.colorScheme.primary 
                        else MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = if (documentsUploaded) "Documents Uploaded" else "Upload Government ID",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = if (documentsUploaded) 
                        "Your documents have been successfully uploaded" 
                    else "Upload your government-issued ID document",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { onDocumentsUploaded(true) },
                    enabled = !documentsUploaded
                ) {
                    Text("Upload Document")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                enabled = documentsUploaded
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun SalaryInfoStep(
    salaryInfo: SalaryInfo,
    onSalaryInfoChanged: (SalaryInfo) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column {
        Text(
            text = "Salary Information",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = salaryInfo.monthlyIncome,
            onValueChange = { onSalaryInfoChanged(salaryInfo.copy(monthlyIncome = it)) },
            label = { Text("Monthly Income") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = { Text("Enter your monthly income in USD") },
            isError = salaryInfo.monthlyIncome.isNotEmpty() && !salaryInfo.isValidIncome(),
            trailingIcon = {
                if (salaryInfo.monthlyIncome.isNotEmpty() && !salaryInfo.isValidIncome()) {
                    Icon(Icons.Default.Info, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = salaryInfo.employer,
            onValueChange = { onSalaryInfoChanged(salaryInfo.copy(employer = it)) },
            label = { Text("Employer") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = salaryInfo.employer.isNotEmpty() && salaryInfo.employer.length < 2,
            supportingText = {
                if (salaryInfo.employer.isNotEmpty() && salaryInfo.employer.length < 2) {
                    Text("Employer name must be at least 2 characters", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = salaryInfo.jobTitle,
            onValueChange = { onSalaryInfoChanged(salaryInfo.copy(jobTitle = it)) },
            label = { Text("Job Title") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = salaryInfo.jobTitle.isNotEmpty() && salaryInfo.jobTitle.length < 2,
            supportingText = {
                if (salaryInfo.jobTitle.isNotEmpty() && salaryInfo.jobTitle.length < 2) {
                    Text("Job title must be at least 2 characters", color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f),
                enabled = salaryInfo.isValid()
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun ReviewStep(
    selectedType: String,
    personalInfo: PersonalInfo,
    email: String,
    salaryInfo: SalaryInfo,
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean
) {
    Column {
        Text(
            text = "Review Details",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Application Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                ReviewItem("Type", selectedType)
                ReviewItem("Name", "${personalInfo.firstName} ${personalInfo.lastName}")
                ReviewItem("Phone", personalInfo.phone)
                ReviewItem("Email", email)
                ReviewItem("Address", personalInfo.address)
                ReviewItem("Monthly Income", salaryInfo.monthlyIncome)
                ReviewItem("Employer", salaryInfo.employer)
                ReviewItem("Job Title", salaryInfo.jobTitle)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            Button(
                onClick = onSubmit,
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Submit Application")
                }
            }
        }
    }
}

@Composable
fun ReviewItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

data class PersonalInfo(
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val address: String = ""
) {
    fun isValid(): Boolean {
        return firstName.length >= 2 && lastName.length >= 2 && isValidPhone() && address.length >= 10
    }
    
    fun isValidPhone(): Boolean {
        return phone.matches(Regex("^[+]?[0-9]{10,15}$"))
    }
}

data class SalaryInfo(
    val monthlyIncome: String = "",
    val employer: String = "",
    val jobTitle: String = ""
) {
    fun isValid(): Boolean {
        return isValidIncome() && employer.length >= 2 && jobTitle.length >= 2
    }
    
    fun isValidIncome(): Boolean {
        return try {
            val income = monthlyIncome.toDoubleOrNull()
            income != null && income >= 1000.0
        } catch (e: Exception) {
            false
        }
    }
}

fun isValidEmail(email: String): Boolean {
    return email.matches(Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"))
}
