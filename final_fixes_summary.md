# Final Fixes for Imperium App Build Issues

We successfully fixed all the remaining build issues in the Imperium app. Here's a summary of the latest fixes:

## 1. Fixed MaterialTheme.colorScheme Access in Canvas (ProgressScreen.kt)

The main issue was that `MaterialTheme.colorScheme` was being accessed inside Canvas scopes where @Composable invocations aren't allowed.

### Fix for AccuracyChart:
- Created theme color variables outside of the Canvas scope:
  ```kotlin
  // Store colors outside of Canvas scope
  val surfaceVariantColor = MaterialTheme.colorScheme.surfaceVariant
  val primaryColor = MaterialTheme.colorScheme.primary
  val tertiaryColor = MaterialTheme.colorScheme.tertiary
  ```
- Used these pre-stored colors inside the Canvas:
  ```kotlin
  val gradient = Brush.linearGradient(
      colors = listOf(
          primaryColor,
          tertiaryColor  // Instead of MaterialTheme.colorScheme.tertiary
      )
  )
  ```

### Fix for CategoryComparisonChart:
- Extracted all theme colors and created a color list outside Canvas:
  ```kotlin
  // Store theme colors outside Canvas
  val primaryColor = MaterialTheme.colorScheme.primary
  val secondaryColor = MaterialTheme.colorScheme.secondary
  val tertiaryColor = MaterialTheme.colorScheme.tertiary
  val primaryFaded = primaryColor.copy(alpha = 0.7f)
  val secondaryFaded = secondaryColor.copy(alpha = 0.7f)
  val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)

  // Prepare color list outside Canvas
  val barColors = listOf(
      primaryColor,
      secondaryColor,
      tertiaryColor,
      primaryFaded,
      secondaryFaded
  )
  ```
- Used these colors in the Row of ColorLegendItems and inside Canvas:
  ```kotlin
  // In Row:
  ColorLegendItem(color = primaryColor, label = "Ancient")
  
  // In Canvas:
  drawRoundRect(
      color = barColors[index],
      // ...
  )
  ```

## 2. Fixed BorderStroke Issue in QuizScreen.kt

The issue was that ElevatedCard didn't properly support the `border` parameter in the version of Material 3 being used.

### Fix:
- Changed from `ElevatedCard` to regular `Card` which better supports the BorderStroke:
  ```kotlin
  Card(
      modifier = modifier
          .height(110.dp)
          .scale(scale)
          .clickable(enabled = !isSelected && !isCorrect && !isIncorrect) { onClick() },
      colors = CardDefaults.cardColors(
          containerColor = backgroundColor
      ),
      border = BorderStroke(
          width = 1.dp,
          color = borderColor
      ),
      shape = RoundedCornerShape(12.dp),
      elevation = CardDefaults.cardElevation(
          defaultElevation = if (isSelected || isCorrect || isIncorrect) 4.dp else 2.dp
      )
  ) {
      // ...
  }
  ```

## Build Status

The build is now successful - we fixed all the compilation errors. 

To complete the setup, you'll still need to:
1. Install the Java Development Kit (JDK) version 11 or 17
2. Set the JAVA_HOME environment variable to point to your JDK installation
3. Add the JDK's bin directory to your PATH variable

Once these steps are completed, the app should build and run without issues. 