package com.konvert.app.ui.admin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.konvert.app.R
import com.konvert.app.admin.AppAdminController
import com.konvert.app.admin.CardAdminConfig
import com.konvert.app.admin.CardOperationAdminConfig
import com.konvert.app.admin.JarAdminConfig
import com.konvert.app.admin.JarTopUpTransactionAdminConfig
import com.konvert.app.ui.home.SystemBarsColorEffect
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max

private val AdminBg = Color(0xFF1C1C1C)
private val AdminBorder = Color(0xFF444444)
private val AdminFieldBg = Color(0xFF2A2A2A)
private val AdminBlue = Color(0xFF007BFF)
private val AdminGreen = Color(0xFF0A8433)
private val AdminLabel = Color(0xFFAEAEB2)
private val AdminCardBg = Color(0xFF242424)
private val AdminAvatarCircleBg = Color(0xFF333333)

@Composable
private fun AdminSystemBars() {
    SystemBarsColorEffect(
        statusBarColor = AdminBg,
        navigationBarColor = AdminBg,
        decorBackgroundColor = AdminBg
    )
}

enum class JarAdminTransactionsKind {
    CardNumber,
    Link
}

@Composable
fun AdminMainPanel(
    controller: AppAdminController,
    onBack: () -> Unit,
    onOpenJarSettings: (Int) -> Unit,
    onOpenCardSettings: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    AdminSystemBars()
    val s = controller.state
    var mainFirst by remember { mutableStateOf(s.mainFirstName) }
    var fullName by remember { mutableStateOf(s.accountFullName) }
    var cashbackAmount by remember { mutableStateOf(s.cashbackAmount) }
    LaunchedEffect(
        s.mainFirstName,
        s.accountFullName,
        s.cashbackAmount
    ) {
        mainFirst = s.mainFirstName
        fullName = s.accountFullName
        cashbackAmount = s.cashbackAmount
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AdminBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.jar_bank_back_cd),
                    tint = Color.White
                )
            }
            Text(
                text = stringResource(R.string.admin_title),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            AdminAvatarPicker(
                avatarPath = controller.state.profileAvatarPath,
                onAvatarSaved = controller::updateProfileAvatar
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_field_name_main),
                value = mainFirst,
                onValueChange = { mainFirst = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_field_name_account),
                value = fullName,
                onValueChange = { fullName = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_field_cashback_amount),
                value = cashbackAmount,
                onValueChange = { cashbackAmount = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
            controller.state.cards.forEachIndexed { index, card ->
                val cardSettingsLabel = when (index) {
                    0 -> stringResource(R.string.admin_configure_card_1)
                    1 -> stringResource(R.string.admin_configure_card_2)
                    2 -> stringResource(R.string.admin_configure_card_3)
                    else -> stringResource(R.string.admin_configure_card_4)
                }
                Button(
                    onClick = { onOpenCardSettings(index) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AdminBlue)
                ) {
                    Text(
                        text = cardSettingsLabel + " — ${card.name}",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            controller.state.jars.forEachIndexed { index, jar ->
                Button(
                    onClick = { onOpenJarSettings(index) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AdminBlue)
                ) {
                    Text(
                        text = stringResource(R.string.admin_configure_jar, index + 1) + " — ${jar.name}",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        Button(
            onClick = {
                controller.updateMain(
                    mainFirstName = mainFirst,
                    accountFullName = fullName,
                    balanceMain = controller.state.balanceMain,
                    balanceWallet = controller.state.balanceWallet,
                    balanceCredit = controller.state.balanceCredit,
                    cashbackAmount = cashbackAmount
                )
                onBack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AdminGreen)
        ) {
            Text(stringResource(R.string.admin_save), color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun CardAdminPanel(
    controller: AppAdminController,
    cardIndex: Int,
    onBack: () -> Unit,
    onOpenOperationsSettings: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    AdminSystemBars()
    val card = controller.state.cardOrDefault(cardIndex)
    var name by remember(cardIndex) { mutableStateOf(card.name) }
    var balance by remember(cardIndex) { mutableStateOf(card.balanceDisplay) }
    var number by remember(cardIndex) { mutableStateOf(card.cardNumber) }
    var balMain by remember(cardIndex) { mutableStateOf(controller.state.balanceMain) }
    var balWallet by remember(cardIndex) { mutableStateOf(controller.state.balanceWallet) }
    var balCredit by remember(cardIndex) { mutableStateOf(controller.state.balanceCredit) }

    LaunchedEffect(cardIndex, controller.state.cards) {
        val c = controller.state.cardOrDefault(cardIndex)
        name = c.name
        balance = c.balanceDisplay
        number = c.cardNumber
        balMain = controller.state.balanceMain
        balWallet = controller.state.balanceWallet
        balCredit = controller.state.balanceCredit
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AdminBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.jar_bank_back_cd),
                    tint = Color.White
                )
            }
            Text(
                text = stringResource(R.string.admin_card_title, cardIndex + 1),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AdminLabeledField(
                label = stringResource(R.string.admin_card_field_name),
                value = name,
                onValueChange = { name = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_card_field_balance),
                value = balance,
                onValueChange = { balance = it }
            )
            if (cardIndex == 0) {
                AdminLabeledField(
                    label = stringResource(R.string.admin_field_balance_main),
                    value = balMain,
                    onValueChange = { balMain = it }
                )
                AdminLabeledField(
                    label = stringResource(R.string.admin_field_balance_wallet),
                    value = balWallet,
                    onValueChange = { balWallet = it }
                )
                AdminLabeledField(
                    label = stringResource(R.string.admin_field_balance_credit),
                    value = balCredit,
                    onValueChange = { balCredit = it }
                )
            }
            AdminLabeledField(
                label = stringResource(R.string.admin_card_field_number),
                value = number,
                onValueChange = { number = it }
            )
            Button(
                onClick = { onOpenOperationsSettings(cardIndex) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdminBlue)
            ) {
                Text(stringResource(R.string.admin_card_operations_open), color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (cardIndex == 0) {
                        controller.updateMain(
                            mainFirstName = controller.state.mainFirstName,
                            accountFullName = controller.state.accountFullName,
                            balanceMain = balMain,
                            balanceWallet = balWallet,
                            balanceCredit = balCredit,
                            cashbackAmount = controller.state.cashbackAmount
                        )
                    }
                    controller.updateCard(
                        cardIndex,
                        CardAdminConfig(
                            name = name,
                            balanceDisplay = balance,
                            cardNumber = number,
                            operations = controller.state.cardOrDefault(cardIndex).operations
                        )
                    )
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdminGreen)
            ) {
                Text(stringResource(R.string.admin_jar_confirm), color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun CardOperationsAdminPanel(
    controller: AppAdminController,
    cardIndex: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    AdminSystemBars()
    val context = LocalContext.current
    val defaultOperationTitle = stringResource(R.string.admin_card_operation_default_title)
    val defaultOperationAmount = stringResource(R.string.admin_card_operation_default_amount)
    val defaultOperationDate = stringResource(R.string.admin_card_operation_default_date)
    var operations by remember(cardIndex) {
        mutableStateOf(controller.state.cardOrDefault(cardIndex).operations)
    }
    LaunchedEffect(cardIndex, controller.state.cards) {
        operations = controller.state.cardOrDefault(cardIndex).operations
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AdminBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.jar_bank_back_cd),
                    tint = Color.White
                )
            }
            Text(
                text = stringResource(R.string.admin_card_operations_title),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            operations.forEachIndexed { opIndex, operation ->
                Text(
                    text = stringResource(R.string.admin_card_operation_block, opIndex + 1),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                AdminLabeledField(
                    label = stringResource(R.string.admin_card_operation_name, opIndex + 1),
                    value = operation.title,
                    onValueChange = { value ->
                        operations = operations.mapIndexed { i, op ->
                            if (i == opIndex) op.copy(title = value) else op
                        }
                    }
                )
                AdminLabeledField(
                    label = stringResource(R.string.admin_card_operation_amount, opIndex + 1),
                    value = operation.amount,
                    onValueChange = { value ->
                        operations = operations.mapIndexed { i, op ->
                            if (i == opIndex) op.copy(amount = value) else op
                        }
                    }
                )
                AdminLabeledField(
                    label = stringResource(R.string.admin_card_operation_date, opIndex + 1),
                    value = operation.dateLabel,
                    onValueChange = { value ->
                        operations = operations.mapIndexed { i, op ->
                            if (i == opIndex) op.copy(dateLabel = value) else op
                        }
                    }
                )
                AdminLabeledField(
                    label = stringResource(R.string.admin_card_operation_balance_after, opIndex + 1),
                    value = operation.balanceAfter,
                    onValueChange = { value ->
                        operations = operations.mapIndexed { i, op ->
                            if (i == opIndex) op.copy(balanceAfter = value) else op
                        }
                    }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            operations = operations.mapIndexed { i, op ->
                                if (i == opIndex) op.copy(hasCommission = !op.hasCommission) else op
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = operation.hasCommission,
                        onCheckedChange = { checked ->
                            operations = operations.mapIndexed { i, op ->
                                if (i == opIndex) op.copy(hasCommission = checked) else op
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.admin_card_operation_has_commission),
                        color = Color.White
                    )
                }
                if (operation.hasCommission) {
                    AdminLabeledField(
                        label = stringResource(R.string.admin_card_operation_commission, opIndex + 1),
                        value = operation.commissionAmount,
                        onValueChange = { value ->
                            operations = operations.mapIndexed { i, op ->
                                if (i == opIndex) op.copy(commissionAmount = value) else op
                            }
                        }
                    )
                }
                AdminLabeledField(
                    label = "\u2116 \u043A\u0432\u0438\u0442\u0430\u043D\u0446\u0456\u0457",
                    value = operation.receiptNumber,
                    onValueChange = { value ->
                        operations = operations.mapIndexed { i, op ->
                            if (i == opIndex) op.copy(receiptNumber = value) else op
                        }
                    }
                )
                AdminPdfPickerRow(
                    pdfUri = operation.receiptPdfUri,
                    onPdfPicked = { uri ->
                        operations = operations.mapIndexed { i, op ->
                            if (i == opIndex) op.copy(receiptPdfUri = uri) else op
                        }
                    },
                    onPersistPermission = { uri ->
                        runCatching {
                            context.contentResolver.takePersistableUriPermission(
                                uri,
                                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                        }
                    }
                )
                Button(
                    onClick = { operations = operations.filterIndexed { i, _ -> i != opIndex } },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF992222))
                ) {
                    Text(stringResource(R.string.admin_card_operation_delete), color = Color.White)
                }
            }

            Button(
                onClick = {
                    operations = operations + CardOperationAdminConfig(
                        title = defaultOperationTitle,
                        amount = defaultOperationAmount,
                        dateLabel = defaultOperationDate
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdminBlue)
            ) {
                Text(stringResource(R.string.admin_card_operation_add), color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val card = controller.state.cardOrDefault(cardIndex)
                    controller.updateCard(
                        cardIndex,
                        card.copy(
                            operations = operations.filter {
                                it.title.isNotBlank() || it.amount.isNotBlank()
                            }
                        )
                    )
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdminGreen)
            ) {
                Text(stringResource(R.string.admin_jar_confirm), color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun JarAdminPanel(
    controller: AppAdminController,
    jarIndex: Int,
    onBack: () -> Unit,
    onOpenTransactionsSettings: (Int, JarAdminTransactionsKind) -> Unit,
    modifier: Modifier = Modifier
) {
    AdminSystemBars()
    val jar = controller.state.jarOrDefault(jarIndex)
    var name by remember(jarIndex) { mutableStateOf(jar.name) }
    var balance by remember(jarIndex) { mutableStateOf(jar.balanceDisplay) }
    var withdrawn by remember(jarIndex) { mutableStateOf(jar.withdrawnDisplay) }
    var card by remember(jarIndex) { mutableStateOf(jar.cardNumber) }
    var link by remember(jarIndex) { mutableStateOf(jar.jarLink) }
    var statPersonal by remember(jarIndex) { mutableStateOf(jar.statPersonalCard) }
    var statUa by remember(jarIndex) { mutableStateOf(jar.statOtherUa) }
    var statAbroad by remember(jarIndex) { mutableStateOf(jar.statAbroad) }
    var statByNumber by remember(jarIndex) { mutableStateOf(jar.statByNumber) }
    var statByLink by remember(jarIndex) { mutableStateOf(jar.statByLink) }
    var statMono by remember(jarIndex) { mutableStateOf(jar.statMonoDisplay) }
    var statFlag by remember(jarIndex) { mutableStateOf(jar.statFlagDisplay) }
    var statGlobe by remember(jarIndex) { mutableStateOf(jar.statGlobeDisplay) }
    var target by remember(jarIndex) { mutableStateOf(jar.targetDisplay) }
    var accumulated by remember(jarIndex) { mutableStateOf(jar.accumulatedDisplay) }

    LaunchedEffect(jarIndex, controller.state.jars) {
        val j = controller.state.jarOrDefault(jarIndex)
        name = j.name
        balance = j.balanceDisplay
        withdrawn = j.withdrawnDisplay
        card = j.cardNumber
        link = j.jarLink
        statPersonal = j.statPersonalCard
        statUa = j.statOtherUa
        statAbroad = j.statAbroad
        statByNumber = j.statByNumber
        statByLink = j.statByLink
        statMono = j.statMonoDisplay
        statFlag = j.statFlagDisplay
        statGlobe = j.statGlobeDisplay
        target = j.targetDisplay
        accumulated = j.accumulatedDisplay
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AdminBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.jar_bank_back_cd),
                    tint = Color.White
                )
            }
            Text(
                text = stringResource(R.string.admin_jar_title, jarIndex + 1),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_field_name),
                value = name,
                onValueChange = { name = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_field_balance),
                value = balance,
                onValueChange = { balance = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_field_withdrawn),
                value = withdrawn,
                onValueChange = { withdrawn = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_field_card),
                value = card,
                onValueChange = { card = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_field_link),
                value = link,
                onValueChange = { link = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_field_target),
                value = target,
                onValueChange = { target = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_field_accumulated),
                value = accumulated,
                onValueChange = { accumulated = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_stat_personal),
                value = statPersonal,
                onValueChange = { statPersonal = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_stat_ua),
                value = statUa,
                onValueChange = { statUa = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_stat_abroad),
                value = statAbroad,
                onValueChange = { statAbroad = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_stat_mono_row),
                value = statMono,
                onValueChange = { statMono = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_stat_flag_row),
                value = statFlag,
                onValueChange = { statFlag = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_stat_globe_row),
                value = statGlobe,
                onValueChange = { statGlobe = it }
            )
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_stat_by_number),
                value = statByNumber,
                onValueChange = { statByNumber = it }
            )
            Button(
                onClick = { onOpenTransactionsSettings(jarIndex, JarAdminTransactionsKind.CardNumber) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdminBlue)
            ) {
                Text(stringResource(R.string.admin_jar_btn_txn_card), color = Color.White)
            }
            AdminLabeledField(
                label = stringResource(R.string.admin_jar_stat_by_link),
                value = statByLink,
                onValueChange = { statByLink = it }
            )
            Button(
                onClick = { onOpenTransactionsSettings(jarIndex, JarAdminTransactionsKind.Link) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdminBlue)
            ) {
                Text(stringResource(R.string.admin_jar_btn_txn_link), color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    controller.updateJar(
                        jarIndex,
                        JarAdminConfig(
                            name = name,
                            balanceDisplay = balance,
                            withdrawnDisplay = withdrawn,
                            targetDisplay = target,
                            accumulatedDisplay = accumulated,
                            cardNumber = card,
                            jarLink = link,
                            statPersonalCard = statPersonal,
                            statOtherUa = statUa,
                            statAbroad = statAbroad,
                            statByNumber = statByNumber,
                            statByLink = statByLink,
                            statMonoDisplay = statMono,
                            statFlagDisplay = statFlag,
                            statGlobeDisplay = statGlobe,
                            cardNumberTransactions = jar.cardNumberTransactions,
                            linkTransactions = jar.linkTransactions
                        )
                    )
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdminGreen)
            ) {
                Text(stringResource(R.string.admin_jar_confirm), color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun JarTransactionsAdminPanel(
    controller: AppAdminController,
    jarIndex: Int,
    kind: JarAdminTransactionsKind,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    AdminSystemBars()
    val jar = controller.state.jarOrDefault(jarIndex)
    val defaultSignature = when (kind) {
        JarAdminTransactionsKind.CardNumber -> "Номер картки банки"
        JarAdminTransactionsKind.Link -> "cat_transfer.png"
    }
    var transactions by remember(jarIndex, kind) {
        mutableStateOf(
            when (kind) {
                JarAdminTransactionsKind.CardNumber -> jar.cardNumberTransactions
                JarAdminTransactionsKind.Link -> jar.linkTransactions
            }
        )
    }
    LaunchedEffect(jarIndex, kind, controller.state.jars) {
        val current = controller.state.jarOrDefault(jarIndex)
        transactions = when (kind) {
            JarAdminTransactionsKind.CardNumber -> current.cardNumberTransactions
            JarAdminTransactionsKind.Link -> current.linkTransactions
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AdminBg)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.jar_bank_back_cd),
                    tint = Color.White
                )
            }
            Text(
                text = when (kind) {
                    JarAdminTransactionsKind.CardNumber -> "Транзакції за номером картки"
                    JarAdminTransactionsKind.Link -> "Транзакції за посиланням"
                },
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            transactions.forEachIndexed { index, transaction ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(AdminCardBg)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Транзакція ${index + 1}",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    AdminLabeledField(
                        label = "Ім'я/джерело",
                        value = transaction.senderTitle,
                        onValueChange = { value ->
                            transactions = transactions.mapIndexed { i, item ->
                                if (i == index) item.copy(senderTitle = value) else item
                            }
                        }
                    )
                    AdminLabeledField(
                        label = "Підпис під транзакцією",
                        value = transaction.signature,
                        onValueChange = { value ->
                            transactions = transactions.mapIndexed { i, item ->
                                if (i == index) item.copy(signature = value) else item
                            }
                        }
                    )
                    AdminLabeledField(
                        label = "Сума",
                        value = transaction.amount,
                        onValueChange = { value ->
                            transactions = transactions.mapIndexed { i, item ->
                                if (i == index) item.copy(amount = value) else item
                            }
                        }
                    )
                    AdminLabeledField(
                        label = "Дата у списку",
                        value = transaction.dateLabel,
                        onValueChange = { value ->
                            transactions = transactions.mapIndexed { i, item ->
                                if (i == index) item.copy(dateLabel = value) else item
                            }
                        }
                    )
                    AdminLabeledField(
                        label = "Дата і час у деталях",
                        value = transaction.dateTimeDisplay,
                        onValueChange = { value ->
                            transactions = transactions.mapIndexed { i, item ->
                                if (i == index) item.copy(dateTimeDisplay = value) else item
                            }
                        }
                    )
                    AdminLabeledField(
                        label = "Залишок після транзакції",
                        value = transaction.balanceAfter,
                        onValueChange = { value ->
                            transactions = transactions.mapIndexed { i, item ->
                                if (i == index) item.copy(balanceAfter = value) else item
                            }
                        }
                    )
                    Button(
                        onClick = { transactions = transactions.filterIndexed { i, _ -> i != index } },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF992222))
                    ) {
                        Text("Видалити транзакцію", color = Color.White)
                    }
                }
            }
            Button(
                onClick = {
                    transactions = transactions + JarTopUpTransactionAdminConfig(
                        senderTitle = if (kind == JarAdminTransactionsKind.Link) "Від:" else "З Білої картки",
                        signature = defaultSignature,
                        amount = "150 ₴",
                        dateLabel = "Сьогодні",
                        dateTimeDisplay = "12 травня 2026, 13:13",
                        balanceAfter = "150 ₴"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdminBlue)
            ) {
                Text("Додати транзакцію", color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val clean = transactions.filter {
                        it.senderTitle.isNotBlank() || it.amount.isNotBlank() || it.signature.isNotBlank()
                    }
                    controller.updateJar(
                        jarIndex,
                        when (kind) {
                            JarAdminTransactionsKind.CardNumber ->
                                controller.state.jarOrDefault(jarIndex).copy(cardNumberTransactions = clean)
                            JarAdminTransactionsKind.Link ->
                                controller.state.jarOrDefault(jarIndex).copy(linkTransactions = clean)
                        }
                    )
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AdminGreen)
            ) {
                Text(stringResource(R.string.admin_jar_confirm), color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun AdminPdfPickerRow(
    pdfUri: String?,
    onPdfPicked: (String?) -> Unit,
    onPersistPermission: (Uri) -> Unit
) {
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            onPersistPermission(uri)
            onPdfPicked(uri.toString())
        }
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = AdminCardBg
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "PDF-\u043A\u0432\u0438\u0442\u0430\u043D\u0446\u0456\u044F",
                color = AdminLabel,
                fontSize = 13.sp
            )
            Text(
                text = pdfUri?.takeIf { it.isNotBlank() } ?: "\u0424\u0430\u0439\u043B \u043D\u0435 \u0432\u0438\u0431\u0440\u0430\u043D\u043E",
                color = Color.White.copy(alpha = 0.82f),
                fontSize = 12.sp,
                maxLines = 1
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { picker.launch(arrayOf("application/pdf")) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AdminBlue)
                ) {
                    Text(
                        text = "\u0412\u0438\u0431\u0440\u0430\u0442\u0438 PDF",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
                if (!pdfUri.isNullOrBlank()) {
                    Button(
                        onClick = { onPdfPicked(null) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B2A2A))
                    ) {
                        Text(
                            text = "\u041E\u0447\u0438\u0441\u0442\u0438\u0442\u0438",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminAvatarPicker(
    avatarPath: String?,
    onAvatarSaved: (String) -> Unit
) {
    var cropUri by remember { mutableStateOf<Uri?>(null) }
    val avatarBitmap = rememberAdminAvatarBitmap(avatarPath)
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        cropUri = uri
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = AdminCardBg
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(AdminAvatarCircleBg),
                contentAlignment = Alignment.Center
            ) {
                if (avatarBitmap != null) {
                    Image(
                        bitmap = avatarBitmap,
                        contentDescription = stringResource(R.string.admin_avatar_cd),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = stringResource(R.string.admin_avatar_cd),
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.admin_avatar_title),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { picker.launch(arrayOf("image/*")) },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AdminBlue)
                ) {
                    Text(
                        text = stringResource(
                            if (avatarBitmap == null) R.string.admin_avatar_pick else R.string.admin_avatar_change
                        ),
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    cropUri?.let { uri ->
        AdminAvatarCropDialog(
            sourceUri = uri,
            onDismiss = { cropUri = null },
            onAvatarSaved = { path ->
                onAvatarSaved(path)
                cropUri = null
            }
        )
    }
}

@Composable
private fun AdminAvatarCropDialog(
    sourceUri: Uri,
    onDismiss: () -> Unit,
    onAvatarSaved: (String) -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val sourceBitmap = remember(sourceUri) { loadBitmapFromUri(context, sourceUri) }
    var scale by remember(sourceUri) { mutableStateOf(1f) }
    var offset by remember(sourceUri) { mutableStateOf(Offset.Zero) }
    var cropSizePx by remember(sourceUri) { mutableStateOf(0f) }
    val fallbackCropSizePx = with(density) { 280.dp.toPx() }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = AdminBg,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.admin_avatar_crop_title),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.admin_avatar_crop_hint),
                    color = AdminLabel,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (sourceBitmap != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(Color.Black)
                            .onSizeChanged { cropSizePx = it.width.toFloat() }
                            .pointerInput(sourceBitmap, scale) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    offset += dragAmount
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = sourceBitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    translationX = offset.x
                                    translationY = offset.y
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = stringResource(R.string.admin_avatar_crop_zoom),
                        color = AdminLabel,
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Slider(
                        value = scale,
                        onValueChange = { scale = it },
                        valueRange = 1f..3f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AdminFieldBg)
                        ) {
                            Text(stringResource(R.string.admin_avatar_crop_cancel), color = Color.White)
                        }
                        Button(
                            onClick = {
                                saveCroppedAvatar(
                                    context = context,
                                    source = sourceBitmap,
                                    cropSizePx = if (cropSizePx > 0f) cropSizePx else fallbackCropSizePx,
                                    scale = scale,
                                    offset = offset
                                )?.let(onAvatarSaved)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AdminGreen)
                        ) {
                            Text(
                                stringResource(R.string.admin_avatar_crop_save),
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    Text(
                        text = stringResource(R.string.admin_avatar_crop_cancel),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onDismiss)
                            .padding(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberAdminAvatarBitmap(path: String?): ImageBitmap? {
    return remember(path) {
        path
            ?.takeIf { it.isNotBlank() && File(it).exists() }
            ?.let { BitmapFactory.decodeFile(it)?.asImageBitmap() }
    }
}

private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return runCatching {
        context.contentResolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input)
        }
    }.getOrNull()
}

private fun saveCroppedAvatar(
    context: Context,
    source: Bitmap,
    cropSizePx: Float,
    scale: Float,
    offset: Offset
): String? {
    val outputSize = 512
    val baseScale = max(cropSizePx / source.width.toFloat(), cropSizePx / source.height.toFloat()) * scale
    val sourceCropSize = cropSizePx / baseScale
    val imageLeft = (cropSizePx - source.width * baseScale) / 2f + offset.x
    val imageTop = (cropSizePx - source.height * baseScale) / 2f + offset.y
    val left = ((-imageLeft) / baseScale)
        .coerceIn(0f, (source.width - sourceCropSize).coerceAtLeast(0f))
    val top = ((-imageTop) / baseScale)
        .coerceIn(0f, (source.height - sourceCropSize).coerceAtLeast(0f))
    val right = (left + sourceCropSize).coerceAtMost(source.width.toFloat())
    val bottom = (top + sourceCropSize).coerceAtMost(source.height.toFloat())
    val src = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
    val output = Bitmap.createBitmap(outputSize, outputSize, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    canvas.drawBitmap(source, src, Rect(0, 0, outputSize, outputSize), paint)
    return runCatching {
        val file = File(context.filesDir, "profile_avatar.png")
        FileOutputStream(file).use { output.compress(Bitmap.CompressFormat.PNG, 100, it) }
        output.recycle()
        file.absolutePath
    }.getOrNull()
}

@Composable
private fun AdminLabeledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = AdminLabel,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = AdminFieldBg,
                unfocusedContainerColor = AdminFieldBg,
                focusedBorderColor = AdminBorder,
                unfocusedBorderColor = AdminBorder,
                cursorColor = Color.White,
                focusedLabelColor = AdminLabel,
                unfocusedLabelColor = AdminLabel
            )
        )
    }
}
