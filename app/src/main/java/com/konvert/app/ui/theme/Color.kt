package com.konvert.app.ui.theme

import androidx.compose.ui.graphics.Color

/** Екран блокування */
val Background = Color(0xFF131313)
/** Круги клавіатури */
val KeypadButton = Color(0xFF232323)
/** Основний текст (клавіатура, посилання тощо) */
val TextPrimary = Color(0xFFDFDFDF)
/** «З поверненням, …» */
val GreetingText = Color(0xFFDCDCDC)
/** «Уведіть ПІН-код» */
val PinPromptText = Color(0xFF878787)
/** Другорядний текст */
val TextMuted = Color(0xFFB8B8B8)
/** Плейсхолдер під аватар */
val AvatarPlaceholder = Color(0xFF2A2A2A)
val PinDotEmpty = Color(0xFF3A3A3C)
/** Заповнена крапка під час вводу PIN */
val PinDotFilled = Color(0xFFF8484B)
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
val HomeBalanceBarTint = Color(0xFFA9A9A9)

/** Круги швидких дій («Переказати на картку» …) */
val QuickActionCircleFill = Color(0xFF02070E)

/** Картка «Операції» */
val OperationsBlockColor = Color(0xFF262626)

/** Нижня навігація (пігулка + Маркет): заливка та ледь світліший бордер */
val HomeBottomBarFill = Color(0xFF2B2B2B)
val HomeBottomBarBorder = Color(0xFF3A3A3A)

/** Іконки нижньої навігації (неактивні) */
val HomeNavIconInactive = Color(0xFFA9A9A9)

/** Картка «Корисне» */
val HomeUsefulCardColor = Color(0xFF272727)

/** Плитки всередині «Корисне» (курси, 2×2) */
val HomeUsefulTileFill = Color(0xFF343434)
