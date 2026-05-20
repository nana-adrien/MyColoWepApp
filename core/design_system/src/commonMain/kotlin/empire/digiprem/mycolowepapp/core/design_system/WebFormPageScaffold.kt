package empire.digiprem.mycolowepapp.core.design_system

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import empire.digiprem.mycolowepapp.core.theme.Primary
import empire.digiprem.mycolowepapp.core.theme.PrimaryDark
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.powered_by
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.unit.min
import empire.digiprem.mycolowepapp.core.design_system.components.wrapper.CardWrapper
import empire.digiprem.mycolowepapp.core.design_system.extension.asString
import empire.digiprem.mycolowepapp.core.domain.util.UiText
import empire.digiprem.mycolowepapp.core.theme.MyColoTheme


@Composable
fun BrandLogo(
    color: Color = Color.White.copy(alpha = 0.2f),
) {
    Box(
        modifier = Modifier.size(72.dp).background(color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text("MC", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
    }
}

@Composable
fun BrandLogoDetail() {
    listOf(
        "Remplissez le formulaire en quelques minutes",
        "Un code de référence vous sera attribué",
        "Votre inscription sera examinée par l'administration"
    ).forEach { info ->
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.size(20.dp).background(Color.White.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Check, null, tint = Color.White, modifier = Modifier.size(12.dp))
            }
            Text(
                text = info,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(start = 10.dp)
            )
        }
    }
}

@Composable
fun WebFormPageScaffold(
    modifier: Modifier = Modifier,
    title: String = "Inscription à la colonie",
    description: String = "Inscription à la colonie",
    logo: @Composable (Color) -> Unit = { BrandLogo(color = it) },
    formDetailSection: @Composable () -> Unit = { BrandLogoDetail() },
    errorMessage: UiText? = null,
    onCleanErrorClick: (() -> Unit)? = null,
    formContent: @Composable () -> Unit
) {
    val isMobileDevice = currentDeviceConfigure().isMobileDevice()

    val pageDescription: @Composable (ColumnScope) -> Unit = {
        logo(if (isMobileDevice) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            title,
            style = if (isMobileDevice) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = if (!isMobileDevice) Color.White else MaterialTheme.colorScheme.onBackground,
        )
        Text(
            description,
            style = MaterialTheme.typography.bodyMedium,
            color = if (!isMobileDevice) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.85f
            ),
            textAlign = if (isMobileDevice) TextAlign.Center else TextAlign.Start,
        )
        Spacer(Modifier.height(16.dp))
    }

    if (isMobileDevice) {
        WebFormPageScaffoldMobile(
            modifier = modifier,
            pageDescription = pageDescription,
            formContent = formContent,
            errorMessage = errorMessage,
            onCleanErrorClick = { onCleanErrorClick?.invoke() },
        )
    } else {
        WebFormPageScaffoldDesktop(
            // modifier = modifier,
            pageDescription = pageDescription,
            formDetailSection = formDetailSection,
            formContent = formContent,
            errorMessage = errorMessage,
            onCleanErrorClick = { onCleanErrorClick?.invoke() },

        )
    }
}

@Composable
private fun WebFormPageScaffoldMobile(
    modifier: Modifier = Modifier,
    pageDescription: @Composable (ColumnScope) -> Unit,
    errorMessage: UiText? = null,
    onCleanErrorClick: () -> Unit,
    formContent: @Composable () -> Unit
) {
    val animeBoxError by animateDpAsState(
        targetValue = if (errorMessage == null) 0.dp else 46.dp
    )

    Box(
        modifier = modifier
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background).padding(top = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 30.dp, horizontal = 20.dp)
                    .height(200.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterVertically),
                content = pageDescription
            )


            CardWrapper(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
            ) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    errorMessage?.let {
                        Spacer(Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().heightIn(min = animeBoxError)
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.errorContainer).padding(vertical = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                modifier = Modifier.padding(horizontal = 10.dp).size(24.dp),
                                onClick = {
                                    onCleanErrorClick()
                                }) {
                                Icon(Icons.Filled.Close, null, tint = Color.White)
                            }
                            Text(
                                text = it.asString(),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                    formContent()
                }
            }

        }
    }
}

@Composable
private fun WebFormPageScaffoldDesktop(
    modifier: Modifier = Modifier,
    pageDescription: @Composable (ColumnScope) -> Unit,
    errorMessage: UiText? = null,
    onCleanErrorClick: () -> Unit,
    formDetailSection: @Composable () -> Unit,
    formContent: @Composable () -> Unit
) {
    val animeBoxError by animateDpAsState(
        targetValue = if (errorMessage == null) 0.dp else 46.dp
    )
    val height = LocalWindowInfo.current.containerDpSize.height
    Row(
        modifier = modifier.background(brush = Brush.verticalGradient(listOf(PrimaryDark, Primary))),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Panneau gauche
        Box(
            modifier = Modifier
                .weight(0.4f)
                .wrapContentSize()
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.width(400.dp).padding(horizontal = 20.dp),
                // horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
            ) {
                pageDescription.invoke(this)
                formDetailSection()
            }
        }

        // Panneau droit — formulaire
        Box(
            modifier = Modifier
                .weight(0.6f)
                .heightIn(min = height)
                .background(MaterialTheme.colorScheme.background).padding(vertical = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight(), // ✅ scroll ici
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CardWrapper(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 30.dp)
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                ) {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        errorMessage?.let {
                            Spacer(Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth().heightIn(min = animeBoxError)
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(MaterialTheme.colorScheme.errorContainer).padding(vertical = 20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    modifier = Modifier.padding(horizontal = 10.dp).size(24.dp),
                                    onClick = {
                                        onCleanErrorClick()
                                    }) {
                                    Icon(Icons.Filled.Close, null, tint = Color.White)
                                }
                                Text(
                                    text = it.asString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                            Spacer(Modifier.height(10.dp))
                        }
                        formContent()
                    }

                }
            }
        }
    }
}


@Composable
private fun WebFormPageScaffoldPreview() {
    WebFormPageScaffold {

    }
}


@Composable
private fun WebFormPageScaffoldTheme(darkTheme: Boolean = false) {
    MyColoTheme(
        //  darkTheme = darkTheme
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            WebFormPageScaffoldPreview()
        }
    }
}


// Light
@Composable
private fun WebFormPageScaffoldLightThemePreview() {
    WebFormPageScaffoldTheme(
        darkTheme = false
    )
}

// Dark
@Composable
private fun WebFormPageScaffoldDarkThemePreview() {
    WebFormPageScaffoldTheme(
        darkTheme = true
    )
}

// Phone


@Preview(
    name = "Phone",
    group = "WebFormPageScaffold Light Theme",
    device = Devices.PIXEL_3A,
)
@Composable
private fun WebFormPageScaffoldLightThemePhonePreview() {
    WebFormPageScaffoldLightThemePreview()
}


// Phone

@Preview(
    name = "Phone",
    group = "WebFormPageScaffold Dark Theme",
    device = Devices.PIXEL_3A,
)
@Composable
private fun WebFormPageScaffoldDarkThemePhonePreview() {
    WebFormPageScaffoldDarkThemePreview()
}

/*
// Table
@Preview(
    name ="Table" ,
    group = "WebFormPageScaffold Light Theme",
    device = Devices.TABLET,
)

@Composable
private fun WebFormPageScaffoldLightThemeTablePreview() {
    WebFormPageScaffoldLightThemePreview()
}

// Table

@Preview(
    name ="Table" ,
    group = "WebFormPageScaffold Dark Theme",
    device = Devices.TABLET,
)
@Composable
private fun WebFormPageScaffoldDarkThemeTablePreview() {
    WebFormPageScaffoldDarkThemePreview()
}

 // Desktop
@Preview(
    name ="Desktop" ,
    group = "WebFormPageScaffold Light Theme",
    device = Devices.DESKTOP,
)
@Composable
private fun WebFormPageScaffoldLightThemeDesktopPreview() {
    WebFormPageScaffoldLightThemePreview()
}

// Desktop

@Preview(
    name ="Desktop" ,
    group = "WebFormPageScaffold Dark Theme",
    device = Devices.DESKTOP,
)
@Composable
private fun WebFormPageScaffoldDarkThemeDesktopPreview() {
    WebFormPageScaffoldDarkThemePreview()
}


*/


