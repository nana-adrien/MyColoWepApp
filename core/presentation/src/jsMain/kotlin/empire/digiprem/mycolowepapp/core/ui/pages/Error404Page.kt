package empire.digiprem.mycolowepapp.core.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import empire.digiprem.portfolio.app.components.footer.CommonFooter
import empire.digiprem.portfolio.app.components.header.CommonHeader
import empire.digiprem.portfolio.app.components.social_media.CommonSocialMedia
import empire.digiprem.portfolio.core.design_system.PortfolioButton
import empire.digiprem.portfolio.core.design_system.WebPageScaffold
import empire.digiprem.portfolio.core.design_system.layout.SectionLayout
import empire.digiprem.portfolio.core.domain.services.TranslationService
import empire.digiprem.portfolio.core.domain.util.WindowsPlatform

@Composable
fun Error404Page(
    enabledDarkTheme: Boolean,
    onDarkThemeChanged: () -> Unit,
) {
    val scrollState = rememberLazyListState()

    WebPageScaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            .safeContentPadding()
            .fillMaxSize(),
        scrollState = scrollState,
        header = {
            CommonHeader(
                isDarkTheme = enabledDarkTheme,
                scrollState = scrollState,
                onDarkThemeChanged = onDarkThemeChanged,
            )
        },
        footer = {
            CommonFooter()
        },
        socialMedia = {
            CommonSocialMedia(
                visible = scrollState.firstVisibleItemIndex > 0,
            )
        }
    ) {
        item {
            Box(
                modifier = Modifier
                    .padding(32.dp)
                    .fillParentMaxSize(0.85f),
                contentAlignment = Alignment.Center
            ) {
                SectionLayout(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "404",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = TranslationService.getString("page_not_found"),
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        PortfolioButton(
                            onClick = { WindowsPlatform.openLink("/") },
                            text = TranslationService.getString("back_home"),
                        )
                    }
                }

            }
        }
    }
}