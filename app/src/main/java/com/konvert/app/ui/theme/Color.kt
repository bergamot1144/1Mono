package com.konvert.app.ui.theme

import androidx.compose.ui.graphics.Color

/** Екран блокування */
val Background = Color(0xFF101010)
/** Круги клавіатури */
val KeypadButton = Color(0xFF1E1E1E)
/** Основний текст (клавіатура, посилання тощо) */
val TextPrimary = Color(0xFFF3F3F3)
/** «З поверненням, …» */
val GreetingText = Color(0xFFEDEDED)
/** «Уведіть ПІН-код» */
val PinPromptText = Color(0xFF9A9A9A)
/** Другорядний текст */
val TextMuted = Color(0xFFC0C0C0)
/** Плейсхолдер під аватар */
val AvatarPlaceholder = Color(0xFF2B2B2B)
val PinDotEmpty = Color(0xFF3B3B3D)
/** Заповнена крапка під час вводу PIN */
val PinDotFilled = Color(0xFFFF4D57)
/** Круг клавіші Backspace на PIN-екрані */
val PinBackspaceCircleFill = Color(0xFF151515)
val PinBackspaceCirclePressed = Color(0xFF242424)
val ErrorTint = Color(0xFFFF453A)

/** @deprecated для сумісності з Theme — те саме, що кнопки */
val SurfaceKey = KeypadButton

/** Акцент навігації / активні елементи */
val AccentRed = PinDotFilled

/** Градієнт рамки блоку «Ліміти» */
val LimitsGradientStart = Color(0xFF8B5CF6)
val LimitsGradientEnd = Color(0xFF22D3EE)

/** Плаваюча нижня панель (прозорий — без суцільної смуги) */
val BottomBarScrim = Color.Transparent

/** Головний екран: градієнт фону (верх → баланс → зона операцій) */
val HomeBgTop = Color(0xFF0B0D40)
val HomeBgBalance = Color(0xFF14285B)
val HomeBgDeep = Color(0xFF15182A)
val HomeBgBottom = Color(0xFF121212)

/** Баланс + іконки верхньої панелі */
val HomeBalanceBarTint = Color(0xFFB8B8B8)

/** Круги швидких дій («Переказати на картку» …) */
val QuickActionCircleFill = Color(0xFF171A20)

/** Іконки швидких дій (Переказати на картку, IBAN, інші) */
val QuickActionIconTint = Color(0xFFCFE0FE)

/** Підписи під кругами швидких дій («Переказати на картку» тощо) */
val HomeQuickActionCaptionColor = Color(0xFFC8CBD2)

/** Кнопка «Усі ›» у блоці операцій: ледь блакитний напівпрозорий фон, текст #698ed1 */
val HomeOperationsAllChipBackground = Color(0xFF698ED1).copy(alpha = 0.20f)
val HomeOperationsAllChipText = Color(0xFF698ED1)

/** Картка «Операції» */
val OperationsBlockColor = Color(0xFF242424)

/** Фон кола логотипу для операцій з кастомною заливкою (за потреби) */
val OperationLogoProductsBg = Color(0xFFF9642C)

/** Нижня навігація (пігулка + Маркет): заливка та ледь світліший бордер */
val HomeBottomBarFill = Color(0xFF2A2A2A)
val HomeBottomBarBorder = Color(0xFF404040)

/** Текст / стан неактивних пунктів нижньої навігації */
val HomeNavIconInactive = Color(0xFFA9A9A9)

/** Картка «Корисне» */
val HomeUsefulCardColor = Color(0xFF252525)

/** Плитки всередині «Корисне» (курси, 2×2) */
val HomeUsefulTileFill = Color(0xFF323234)
