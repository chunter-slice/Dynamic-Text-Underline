# Dynamic Text Underline

An investigation for dynamic sizing and placement of an underline drawable or lottie animation.

We want to be able to place an underline drawable or lottie animation under a particular word(s) in a text string. This can be dynamic and the word(s) can appear anywhere in the text string.

We created a custom view that will measure the text to underline to determine the size and position to place the underline, this way it will be placed regardless of the text orientation or whatever line the text apears on.

There is also an example of this using Jetpack Compose.

Available attributes:

`android:text` - The text to display

`android:gravity` - The gravity of the text

`android:textAllCaps` - Flag to determine if the text should be uppercase, we must pass attribute in separately as opposed to part of the `textStyle` otherwise the underline width will not be calculated correctly

`textStyle` - Style to use with the text

`textToUnderline` - The string to place the underline under, if it cannot be found no underline will be rendered

`drawableUnderline` - The drawable resource to use for the underline (this or `lottieUnderline` must be supplied)

`lottieUnderline` - The lottie animation resource to use for the underline (this or `drawableUnderline` must be supplied)

## Demo
<img src="https://github.com/chunter-slice/Dynamic-Text-Underline/blob/master/media/dynamic_underline_demo.gif" width="320" height="640" />

## Custom View Example
```
<com.example.dynamictextunderline.ui.view.TextViewWithUnderlineImage
    android:id="@+id/drawableTitle"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/find_a_pizzeria_today"
    android:textAllCaps="true"
    android:gravity="center"
    app:textStyle="@style/Slice.TextAppearance.Standard.Type2"
    app:textToUnderline="@string/underline_string"
    app:drawableUnderline="@drawable/ic_underline"/>

<com.example.dynamictextunderline.ui.view.TextViewWithUnderlineImage
    android:id="@+id/animatedTitle"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/find_a_pizzeria_today"
    android:gravity="end"
    app:textStyle="@style/Slice.TextAppearance.Standard"
    app:textToUnderline="@string/underline_string"
    app:lottieUnderline="animations/underline.json"/>
```


## Jetpack Compose Example
```
TextWithUnderlineImage(
    modifier = Modifier.fillMaxWidth(),
    text = stringResource(id = R.string.find_a_pizzeria_today),
    underlineText = stringResource(id = R.string.underline_string),
    textStyle = MaterialTheme.typography.h2,
    drawableRes = R.drawable.ic_underline,
)

TextWithUnderlineImage(
    modifier = Modifier.fillMaxWidth(),
    text = stringResource(id = R.string.find_a_pizzeria_today),
    underlineText = stringResource(id = R.string.underline_string),
    textStyle = MaterialTheme.typography.h4,
    lottieFile = "animations/underline.json",
)
```
