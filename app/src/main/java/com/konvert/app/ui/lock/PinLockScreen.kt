package com.konvert.app.ui.lock

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konvert.app.R
import com.konvert.app.ui.theme.AvatarPlaceholder
import com.konvert.app.ui.theme.ErrorTint
import com.konvert.app.ui.theme.GreetingText
import com.konvert.app.ui.theme.PinDotEmpty
import com.konvert.app.ui.theme.PinDotFilled
import com.konvert.app.ui.theme.PinPromptText
import com.konvert.app.ui.theme.TextMuted
import com.konvert.app.ui.theme.TextPrimary
import kotlinx.coroutines.delay

private const val CorrectPin = "9999"

@Composable
fun PinLockScreen(onUnlocked: () -> Unit) {
    var pin by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    LaunchedEffect(pin, showError) {
        if (pin.length == 4) {
            if (pin == CorrectPin) {
                onUnlocked()
            } else {
                showError = true
                delay(450)
                pin = ""
                showError = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(AvatarPlaceholder),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = stringResource(R.string.pin_avatar_content_description),
                tint = TextMuted,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(
                R.string.pin_greeting,
                stringResource(R.string.pin_user_display_name)
            ),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.2.sp
            ),
            color = GreetingText,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.pin_enter_prompt),
            style = MaterialTheme.typography.bodyLarge,
            color = PinPromptText,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(28.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(4) { index ->
                val filled = index < pin.length
                val color = when {
                    showError -> ErrorTint
                    filled -> PinDotFilled
                    else -> PinDotEmpty
                }
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Keypad(
            onDigit = { d ->
                if (pin.length < 4 && !showError) pin += d
            },
            onBackspace = {
                if (pin.isNotEmpty() && !showError) pin = pin.dropLast(1)
            },
            onBiometricStub = { /* прототип */ }
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.pin_forgot),
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { /* прототип */ }
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun Keypad(
    onDigit: (Char) -> Unit,
    onBackspace: () -> Unit,
    onBiometricStub: () -> Unit
) {
    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("biometric", "0", "backspace")
    )
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { cell ->
                    when (cell) {
                        "biometric" -> KeypadIconSlot(onClick = onBiometricStub) {
                            Icon(
                                imageVector = Icons.Outlined.Face,
                                contentDescription = stringResource(R.string.pin_biometric_content_description),
                                tint = TextPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        "backspace" -> KeypadIconSlot(onClick = onBackspace) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Backspace,
                                contentDescription = stringResource(R.string.pin_backspace_content_description),
                                tint = TextPrimary
                            )
                        }
                        else -> KeypadCircle(onClick = { onDigit(cell[0]) }) {
                            Text(
                                text = cell,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun KeypadCircle(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val background =
        if (pressed) PinDotFilled else MaterialTheme.colorScheme.surface
    Box(
        modifier = Modifier
            .size(width = 88.dp, height = 88.dp)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(background)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/** Та сама зона натискання, що й у цифр, але без круглої заливки (Face / Backspace). */
@Composable
private fun KeypadIconSlot(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .size(88.dp)
            .clickable(
                interactionSource = interaction,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
