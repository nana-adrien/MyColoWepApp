package empire.digiprem.mycoloapp.features.pre_auth.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import empire.digiprem.mycoloapp.core.design_system.WebPageScaffold
import empire.digiprem.mycoloapp.core.design_system.animation.FadeSlideInOnScroll
import empire.digiprem.mycoloapp.core.design_system.animation.FloatingBox
import empire.digiprem.mycoloapp.core.design_system.components.MyColoFooter
import empire.digiprem.mycoloapp.core.design_system.components.MyColoNavBar
import empire.digiprem.mycoloapp.core.design_system.components.NavItem
import empire.digiprem.mycoloapp.core.design_system.currentDeviceConfigure
import empire.digiprem.mycoloapp.core.design_system.layout.AdaptativeContainerLayout
import empire.digiprem.mycoloapp.core.design_system.layout.SectionLayout
import empire.digiprem.mycoloapp.core.design_system.theme.Primary
import empire.digiprem.mycoloapp.core.ui.CountdownTimer
import empire.digiprem.mycoloapp.core.ui.MyColoFilledButton
import empire.digiprem.mycoloapp.core.ui.currentTimeMillis
import mycolowepapp.shared.generated.resources.Res
import mycolowepapp.shared.generated.resources.colonie_de_vaccance_au__tappii
import mycolowepapp.shared.generated.resources.landing_cta_register
import mycolowepapp.shared.generated.resources.landing_feature_1_desc
import mycolowepapp.shared.generated.resources.landing_feature_1_title
import mycolowepapp.shared.generated.resources.landing_feature_2_desc
import mycolowepapp.shared.generated.resources.landing_feature_2_title
import mycolowepapp.shared.generated.resources.landing_feature_3_desc
import mycolowepapp.shared.generated.resources.landing_feature_3_title
import mycolowepapp.shared.generated.resources.landing_feature_4_desc
import mycolowepapp.shared.generated.resources.landing_feature_4_title
import mycolowepapp.shared.generated.resources.landing_hero_subtitle
import mycolowepapp.shared.generated.resources.landing_hero_title
import mycolowepapp.shared.generated.resources.landing_nav_contact
import mycolowepapp.shared.generated.resources.landing_nav_program
import mycolowepapp.shared.generated.resources.powered_by
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class LandingPageSection {
    HOME_SECTION;

    companion object {
        fun fromString(section: String): LandingPageSection {
            return entries.first { it.name == section.uppercase() } ?: HOME_SECTION
        }
    }
}

@Composable
fun LandingScreen(
    navigateToSection: LandingPageSection = LandingPageSection.HOME_SECTION,
    onNavigateToRegistration: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToLive: () -> Unit = {},
) {
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val targetDate = remember { currentTimeMillis() + 44L * 24 * 3600 * 1000 }
    val navItems = listOf(
        NavItem("program", stringResource(Res.string.landing_nav_program)),
        NavItem("contact", stringResource(Res.string.landing_nav_contact)),
    )
    val height = LocalWindowInfo.current.containerDpSize.height
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        /*  WebPageScaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize(),
        scrollState = scrollState,
        header = {
            MyColoNavBar(
                scrollState = scrollState,
                navItems = navItems,
                onNavItemClick = { item ->
                    coroutineScope.launch {
                        when (item.id) {
                            "program" -> scrollState.animateScrollToItem(1)
                            "contact" -> scrollState.animateScrollToItem(scrollState.layoutInfo.totalItemsCount - 1)
                        }
                    }
                },
                onAdminClick = onNavigateToAdmin
            )
        },
        footer = { MyColoFooter(stringResource(Res.string.powered_by)) },
        floatingButton = {
            AnimatedVisibility(
                modifier = Modifier.padding(end = 16.dp, bottom = 24.dp),
                visible = scrollState.firstVisibleItemIndex > 0,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally(),
            ) {
                Box(
                    modifier = Modifier.size(48.dp).background(Primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("↑", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
            }
        }
    ) {*/
        //  item {
        FadeSlideInOnScroll {
            HeroSection(targetDate = targetDate, onRegisterClick = onNavigateToRegistration)
        }
        //  }
        // item {
        FadeSlideInOnScroll(
            modifier = Modifier.padding( horizontal = 24.dp)
        ) {
            FeaturesSection()
        }
        // }
        /* }*/
    }
}

@Composable
private fun HeroSection(targetDate: Long, onRegisterClick: () -> Unit) {
    val isMobile = currentDeviceConfigure().isCompact()

    Box(
        modifier = Modifier.fillMaxWidth()
            .heightIn(min = if (isMobile) 600.dp else 700.dp)
            .paint(
                painterResource(Res.drawable.colonie_de_vaccance_au__tappii),
                contentScale = ContentScale.Crop,
            )
            .background(Color.Black.copy(alpha = 0.3f))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Black,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Black
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        FloatingBox(seed = 42, modifier = Modifier.align(Alignment.TopStart).padding(top = 80.dp, start = 40.dp)) {
            Box(modifier = Modifier.size(60.dp).background(Color.White.copy(alpha = 0.08f), CircleShape))
        }
        FloatingBox(seed = 99, modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 60.dp, end = 60.dp)) {
            Box(modifier = Modifier.size(40.dp).background(Color.White.copy(alpha = 0.06f), RoundedCornerShape(12.dp)))
        }

        AdaptativeContainerLayout {
            Column(
                modifier = Modifier
                    //.widthIn(max = if (isMobile) 360.dp else 800.dp)
                    .padding(horizontal = if (isMobile) 20.dp else 32.dp, vertical = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(50.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        "COLONIE MY COLO · INSCRIPTIONS OUVERTES",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(Res.string.landing_hero_title),
                    fontSize = if (isMobile) 26.sp else 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = if (isMobile) 34.sp else 44.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.landing_hero_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(40.dp))
                CountdownTimer(targetDate = targetDate)
                Spacer(modifier = Modifier.height(40.dp))
                Box(modifier = Modifier.widthIn(max = 320.dp)) {
                    MyColoFilledButton(
                        text = stringResource(Res.string.landing_cta_register),
                        onClick = onRegisterClick
                    )
                }
            }
        }

    }
}

@Composable
private fun FeaturesSection() {
    val isMobile = currentDeviceConfigure().isCompact()
    SectionLayout(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)) {
        if (isMobile) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                FeatureCard(
                    icon = Icons.Filled.Add,
                    title = stringResource(Res.string.landing_feature_1_title),
                    description = stringResource(Res.string.landing_feature_1_desc),
                    modifier = Modifier.fillMaxWidth()
                )
                FeatureCard(
                    icon = Icons.Filled.Lock,
                    title = stringResource(Res.string.landing_feature_2_title),
                    description = stringResource(Res.string.landing_feature_2_desc),
                    modifier = Modifier.fillMaxWidth()
                )
                FeatureCard(
                    icon = Icons.Filled.Person,
                    title = stringResource(Res.string.landing_feature_3_title),
                    description = stringResource(Res.string.landing_feature_3_desc),
                    modifier = Modifier.fillMaxWidth()
                )
                FeatureCard(
                    icon = Icons.Filled.Settings,
                    title = stringResource(Res.string.landing_feature_4_title),
                    description = stringResource(Res.string.landing_feature_4_desc),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    FeatureCard(
                        icon = Icons.Filled.Add,
                        title = stringResource(Res.string.landing_feature_1_title),
                        description = stringResource(Res.string.landing_feature_1_desc),
                        modifier = Modifier.weight(1f)
                    )
                    FeatureCard(
                        icon = Icons.Filled.Lock,
                        title = stringResource(Res.string.landing_feature_2_title),
                        description = stringResource(Res.string.landing_feature_2_desc),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    FeatureCard(
                        icon = Icons.Filled.Person,
                        title = stringResource(Res.string.landing_feature_3_title),
                        description = stringResource(Res.string.landing_feature_3_desc),
                        modifier = Modifier.weight(1f)
                    )
                    FeatureCard(
                        icon = Icons.Filled.Settings,
                        title = stringResource(Res.string.landing_feature_4_title),
                        description = stringResource(Res.string.landing_feature_4_desc),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(icon: ImageVector, title: String, description: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier.size(48.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Primary, modifier = Modifier.size(28.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
