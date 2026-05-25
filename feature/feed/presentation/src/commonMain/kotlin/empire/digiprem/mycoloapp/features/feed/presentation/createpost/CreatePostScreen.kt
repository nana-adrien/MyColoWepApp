package empire.digiprem.mycoloapp.features.feed.presentation.createpost

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import empire.digiprem.mycoloapp.core.design_system.picker.rememberCameraPicker
import empire.digiprem.mycoloapp.core.design_system.picker.rememberGalleryPicker
import empire.digiprem.mycoloapp.core.domain.model.AppFile
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreatePostRoot(
    onPublished: () -> Unit,
    viewModel: CreatePostViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val galleryPicker = rememberGalleryPicker { file ->
        file?.let { viewModel.onAction(CreatePostAction.OnMediaPicked(it.first())) }
    }
    val cameraPicker = rememberCameraPicker { file ->
        file?.let { viewModel.onAction(CreatePostAction.OnMediaPicked(it)) }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                CreatePostEvent.OnPublished -> onPublished()
                CreatePostEvent.OnNavigateBack -> onPublished()
            }
        }
    }

    CreatePostScreen(
        state = state,
        onAction = viewModel::onAction,
        onGalleryClick = { galleryPicker.launch() },
        onCameraClick = { cameraPicker.launch() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    state: CreatePostState,
    onAction: (CreatePostAction) -> Unit,
    onGalleryClick: () -> Unit = {},
    onCameraClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nouvelle publication") },
                navigationIcon = {
                    IconButton(onClick = { onAction(CreatePostAction.OnNavigateBack) }) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { onAction(CreatePostAction.OnPublishClick) },
                        enabled = state.isFormValid && !state.isPublishing,
                    ) {
                        if (state.isPublishing) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Text(
                                "Publier",
                                fontWeight = FontWeight.SemiBold,
                                color = if (state.isFormValid) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
        ) {
            if (maxWidth >= 600.dp) {
                // ── Tablette / Desktop : 2 colonnes ─────────────────────────
                Row(
                    Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    // Colonne gauche : média
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(14.dp),
                    ) {
                        Text(
                            "MÉDIA",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 0.5.sp,
                        )
                        Spacer(Modifier.height(10.dp))
                        MediaPickerArea(
                            selectedFile = state.selectedFile,
                            onGalleryClick = onGalleryClick,
                            onCameraClick = onCameraClick,
                            onRemove = { onAction(CreatePostAction.OnRemoveMedia) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(Modifier.height(10.dp))
                        UploadProgressBar(progress = state.uploadProgress, modifier = Modifier.fillMaxWidth())
                    }

                    HorizontalDivider(modifier = Modifier.width(1.dp).fillMaxHeight())

                    // Colonne droite : légende + publier
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(14.dp),
                    ) {
                        Text(
                            "LÉGENDE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 0.5.sp,
                        )
                        Spacer(Modifier.height(10.dp))
                        CaptionField(
                            caption = state.caption,
                            captionLength = state.captionLength,
                            onValueChange = { onAction(CreatePostAction.OnCaptionChange(it)) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(Modifier.height(14.dp))
                        if (!state.isFormValid && state.selectedMediaType == null) {
                            InfoBanner("Sélectionnez un média ou écrivez une légende pour publier.")
                            Spacer(Modifier.height(12.dp))
                        }
                        state.errorMessage?.let {
                            Text(it, color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                            Spacer(Modifier.height(8.dp))
                        }
                        Button(
                            onClick = { onAction(CreatePostAction.OnPublishClick) },
                            enabled = state.isFormValid && !state.isPublishing,
                            modifier = Modifier.fillMaxWidth().height(36.dp),
                            shape = RoundedCornerShape(100.dp),
                        ) {
                            if (state.isPublishing) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                            } else {
                                Icon(Icons.Outlined.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Publier", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            } else {
                // ── Mobile : colonne unique scrollable ───────────────────────
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                  /*  MediaPickerArea(
                     //   hasMedia = state.selectedMediaType != null,
                        onGalleryClick = { onAction(CreatePostAction.OnMediaSelected("image")) },
                        onCameraClick = { onAction(CreatePostAction.OnMediaSelected("video")) },
                        onRemove = { onAction(CreatePostAction.OnRemoveMedia) },
                        modifier = Modifier.fillMaxWidth(),
                    )*/

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                "LÉGENDE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                letterSpacing = 0.3.sp,
                            )
                            Spacer(Modifier.height(6.dp))
                            CaptionField(
                                caption = state.caption,
                                captionLength = state.captionLength,
                                onValueChange = { onAction(CreatePostAction.OnCaptionChange(it)) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }

                    //UploadProgressBar(hasMedia = state.selectedMediaType != null, modifier = Modifier.fillMaxWidth())

                    state.errorMessage?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                    }

                    Spacer(Modifier.height(60.dp))
                }
            }
        }
    }
}

@Composable
private fun MediaPickerArea(
    selectedFile: AppFile?,
    onGalleryClick: () -> Unit,
    onCameraClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .border(
                width = 2.dp,
                color = if (selectedFile != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp),
            )
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
            .padding(26.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (selectedFile != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(64.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        if (selectedFile.isVideo) Icons.Outlined.PlayCircleOutline else Icons.Outlined.Image,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp),
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text("Média sélectionné ✓", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(2.dp))
                Text(selectedFile.name, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                Spacer(Modifier.height(6.dp))
                TextButton(onClick = onRemove) {
                    Text("Supprimer", fontSize = 10.sp, color = MaterialTheme.colorScheme.error)
                }
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(44.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Outlined.AddPhotoAlternate, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.height(8.dp))
                Text("Ajouter un média", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                Text("Photo ou vidéo · Max 10 MB", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(
                        onClick = onGalleryClick,
                        modifier = Modifier.height(34.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                    ) {
                        Icon(Icons.Outlined.PhotoLibrary, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Galerie", fontSize = 11.sp)
                    }
                    OutlinedButton(
                        onClick = onCameraClick,
                        modifier = Modifier.height(34.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                    ) {
                        Icon(Icons.Outlined.CameraAlt, contentDescription = null, modifier = Modifier.size(13.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Appareil photo", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CaptionField(
    caption: String,
    captionLength: Int,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        OutlinedTextField(
            value = caption,
            onValueChange = { if (it.length <= 500) onValueChange(it) },
            placeholder = { Text("Ajoutez une légende…", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 80.dp),
            minLines = 3,
            maxLines = 8,
            shape = RoundedCornerShape(8.dp),
        )
        Text(
            "$captionLength / 500",
            fontSize = 9.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.End).padding(top = 2.dp),
        )
    }
}

@Composable
private fun UploadProgressBar(progress: Float, modifier: Modifier = Modifier) {
    if (progress > 0f) {
        Column(modifier) {
            Text(
                "UPLOAD EN COURS",
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.3.sp,
            )
            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(6.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}

@Composable
private fun InfoBanner(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Icon(Icons.Outlined.Info, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.primary)
        Text(message, fontSize = 9.sp, color = MaterialTheme.colorScheme.primary)
    }
}
