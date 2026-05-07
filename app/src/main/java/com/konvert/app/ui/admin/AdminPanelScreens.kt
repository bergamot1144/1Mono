package com.konvert.app.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.konvert.app.R
import com.konvert.app.admin.AppAdminController
import com.konvert.app.admin.CardAdminConfig
import com.konvert.app.admin.CardOperationAdminConfig
import com.konvert.app.admin.JarAdminConfig

private val AdminBg = Color(0xFF1C1C1C)
private val AdminBorder = Color(0xFF444444)
private val AdminFieldBg = Color(0xFF2A2A2A)
private val AdminBlue = Color(0xFF007BFF)
private val AdminGreen = Color(0xFF0A8433)
private val AdminLabel = Color(0xFFAEAEB2)

@Composable
fun AdminMainPanel(
    controller: AppAdminController,
    onBack: () -> Unit,
    onOpenJarSettings: (Int) -> Unit,
    onOpenCardSettings: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val s = controller.state
    var mainFirst by remember { mutableStateOf(s.mainFirstName) }
    var fullName by remember { mutableStateOf(s.accountFullName) }
    LaunchedEffect(
        s.mainFirstName,
        s.accountFullName
    ) {
        mainFirst = s.mainFirstName
        fullName = s.accountFullName
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
                    balanceCredit = controller.state.balanceCredit
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
                            balanceCredit = balCredit
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
    modifier: Modifier = Modifier
) {
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
                onClick = { },
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
                onClick = { },
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
                            statGlobeDisplay = statGlobe
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
