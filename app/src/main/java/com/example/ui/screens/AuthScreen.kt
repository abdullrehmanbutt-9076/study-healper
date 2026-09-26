package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Cyan40
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface1
import com.example.ui.theme.DarkSurface2
import com.example.ui.theme.DarkTextMuted
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.Indigo40
import com.example.ui.theme.Indigo60

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onLoginSuccess: (phone: String, grade: String, language: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSignUp by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("0300 1234567") }
    var otpCode by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }
    var selectedGrade by remember { mutableStateOf("FSc") }
    var gradeMenuExpanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English") } // "English" or "اردو"

    val gradeOptions = listOf("Matric", "O-Level", "FSc", "A-Level", "University")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mode Header selector (Log in / Sign up)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(DarkSurface2)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (!isSignUp) Indigo60 else Color.Transparent)
                        .clickable { isSignUp = false; isOtpSent = false }
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .testTag("tab_login")
                ) {
                    Text(
                        text = "Log in",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (!isSignUp) Color.White else DarkTextSecondary
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSignUp) Indigo60 else Color.Transparent)
                        .clickable { isSignUp = true; isOtpSent = false }
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .testTag("tab_signup")
                ) {
                    Text(
                        text = "Sign up",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSignUp) Color.White else DarkTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Card matching provided HTML mockup
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auth_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    // Brand Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "School Logo",
                            tint = Cyan40,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Student companion",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = if (isSignUp) "Create your account" else "Welcome back",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = if (isSignUp) "Takes less than a minute" else "Log in to keep your streak going",
                        fontSize = 13.sp,
                        color = DarkTextSecondary
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Phone number input
                    Text(
                        text = "Phone number",
                        fontSize = 13.sp,
                        color = DarkTextSecondary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        placeholder = { Text("03xx xxxxxxx", color = DarkTextMuted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("phone_input"),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Cyan40,
                            unfocusedBorderColor = DarkBorder,
                            focusedTextColor = DarkTextPrimary,
                            unfocusedTextColor = DarkTextPrimary
                        ),
                        singleLine = true
                    )

                    // Sign up specific fields
                    if (isSignUp) {
                        Spacer(modifier = Modifier.height(14.dp))

                        // Grade / Board selection
                        Text(
                            text = "Grade / board",
                            fontSize = 13.sp,
                            color = DarkTextSecondary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { gradeMenuExpanded = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("grade_picker_button"),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = DarkTextPrimary
                                )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = selectedGrade, fontSize = 15.sp)
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                                }
                            }

                            DropdownMenu(
                                expanded = gradeMenuExpanded,
                                onDismissRequest = { gradeMenuExpanded = false },
                                modifier = Modifier.background(DarkSurface2)
                            ) {
                                gradeOptions.forEach { grade ->
                                    DropdownMenuItem(
                                        text = { Text(grade, color = DarkTextPrimary) },
                                        onClick = {
                                            selectedGrade = grade
                                            gradeMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Language Preference Selection
                        Text(
                            text = "Explain things in",
                            fontSize = 13.sp,
                            color = DarkTextSecondary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val isEnglish = selectedLanguage == "English"
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isEnglish) Cyan40.copy(alpha = 0.2f) else DarkSurface2)
                                    .border(
                                        width = 1.dp,
                                        color = if (isEnglish) Cyan40 else DarkBorder,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { selectedLanguage = "English" }
                                    .padding(vertical = 12.dp)
                                    .testTag("lang_english"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "English",
                                    fontWeight = FontWeight.Medium,
                                    color = if (isEnglish) Cyan40 else DarkTextSecondary,
                                    fontSize = 14.sp
                                )
                            }

                            val isUrdu = selectedLanguage == "اردو"
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isUrdu) Cyan40.copy(alpha = 0.2f) else DarkSurface2)
                                    .border(
                                        width = 1.dp,
                                        color = if (isUrdu) Cyan40 else DarkBorder,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { selectedLanguage = "اردو" }
                                    .padding(vertical = 12.dp)
                                    .testTag("lang_urdu"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "اردو (Urdu)",
                                    fontWeight = FontWeight.Medium,
                                    color = if (isUrdu) Cyan40 else DarkTextSecondary,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    // OTP field if OTP is sent
                    if (isOtpSent) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Enter 4-digit OTP",
                            fontSize = 13.sp,
                            color = DarkTextSecondary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = otpCode,
                            onValueChange = { if (it.length <= 4) otpCode = it },
                            placeholder = { Text("4 8 2 1", color = DarkTextMuted) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("otp_input"),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Cyan40,
                                unfocusedBorderColor = DarkBorder,
                                focusedTextColor = DarkTextPrimary,
                                unfocusedTextColor = DarkTextPrimary
                            ),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Primary Action Button (Send OTP or Verify & Continue)
                    Button(
                        onClick = {
                            if (!isOtpSent) {
                                isOtpSent = true
                                otpCode = "4821" // Auto fill sample OTP for frictionless UX
                            } else {
                                onLoginSuccess(phoneNumber, selectedGrade, selectedLanguage)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("primary_auth_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Cyan40)
                    ) {
                        Text(
                            text = if (!isOtpSent) {
                                if (isSignUp) "Create account" else "Send OTP"
                            } else {
                                "Verify & Enter App"
                            },
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }

                    if (isSignUp) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "We only use your grade to match explanations to your level.",
                            fontSize = 12.sp,
                            color = DarkTextMuted,
                            lineHeight = 16.sp
                        )
                    }

                    // OR divider
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(DarkBorder)
                        )
                        Text(
                            text = "or",
                            fontSize = 12.sp,
                            color = DarkTextMuted,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(DarkBorder)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Continue with Google Button
                    OutlinedButton(
                        onClick = {
                            onLoginSuccess(phoneNumber, selectedGrade, selectedLanguage)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("google_auth_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkTextPrimary)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "G",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = Cyan40
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Continue with Google",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Switch between Login and Signup
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isSignUp) "Already have an account? " else "New here? ",
                            fontSize = 13.sp,
                            color = DarkTextSecondary
                        )
                        Text(
                            text = if (isSignUp) "Log in" else "Create account",
                            fontSize = 13.sp,
                            color = Cyan40,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clickable {
                                    isSignUp = !isSignUp
                                    isOtpSent = false
                                }
                                .testTag("auth_toggle_mode")
                        )
                    }
                }
            }
        }
    }
}
