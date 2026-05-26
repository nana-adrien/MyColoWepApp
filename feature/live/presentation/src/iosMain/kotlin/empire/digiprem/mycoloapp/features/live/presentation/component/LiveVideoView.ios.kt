package empire.digiprem.mycoloapp.features.live.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.unit.dp
import empire.digiprem.mycoloapp.features.live.domain.model.LiveKitWebData
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun LiveVideoView(
    videoTrack: Any?,
    modifier: Modifier,
) {
    val data = videoTrack as? LiveKitWebData

    if (data == null) {
        Box(
            modifier = modifier.background(MaterialTheme.colorScheme.inverseSurface),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Outlined.VideocamOff,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
            )
        }
        return
    }

    val html = remember(data) { buildLiveKitHtml(data) }

    key(data.roomName, data.token) {
        UIKitView(
            modifier = modifier,
            factory = {
                val config = WKWebViewConfiguration()
                config.allowsInlineMediaPlayback = true
                config.setMediaTypesRequiringUserActionForPlayback(0u)
                WKWebView(frame = CGRectZero.readValue(), configuration = config).also { wv ->
                    wv.loadHTMLString(html, baseURL = null)
                }
            },
            update = {},
        )
    }
}

private fun buildLiveKitHtml(data: LiveKitWebData): String {
    val safeUrl = data.livekitUrl.replace("\\", "\\\\").replace("\"", "\\\"")
    val safeToken = data.token.replace("\\", "\\\\").replace("\"", "\\\"")
    return """<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1">
<style>
*{margin:0;padding:0;box-sizing:border-box}
body{background:#000;color:#fff;font-family:sans-serif;height:100vh;display:flex;flex-direction:column;overflow:hidden}
#videos{flex:1;display:flex;flex-wrap:wrap;align-items:center;justify-content:center}
video{flex:1;min-width:120px;background:#111;max-height:100%}
#bar{padding:6px;text-align:center;font-size:11px;background:rgba(0,0,0,0.9);display:flex;gap:6px;justify-content:center}
button{background:rgba(255,255,255,0.1);color:#fff;border:1px solid rgba(255,255,255,0.2);padding:6px 12px;border-radius:14px;font-size:11px}
button.off{background:#c0392b;border-color:#c0392b}
#loading{flex:1;display:flex;align-items:center;justify-content:center;font-size:14px}
</style>
</head>
<body>
<div id="loading">Connexion...</div>
<div id="videos" style="display:none"></div>
<div id="bar" style="display:none"></div>
<script src="https://cdn.jsdelivr.net/npm/livekit-client@2.5.3/dist/livekit-client.umd.min.js"></script>
<script>
const LK_URL="${safeUrl}",TOKEN="${safeToken}",PUB=${data.isPublishing};
const loading=document.getElementById('loading'),videos=document.getElementById('videos'),bar=document.getElementById('bar');
const room=new LivekitClient.Room({adaptiveStream:true,dynacast:true});
room.on(LivekitClient.RoomEvent.TrackSubscribed,(t)=>{
  if(t.kind===LivekitClient.Track.Kind.Video){const e=t.attach();e.style.flex='1';e.style.minWidth='120px';videos.appendChild(e);}
  else if(t.kind===LivekitClient.Track.Kind.Audio){document.body.appendChild(t.attach());}
});
room.on(LivekitClient.RoomEvent.TrackUnsubscribed,t=>t.detach().forEach(e=>e.remove()));
room.connect(LK_URL,TOKEN).then(async()=>{
  loading.style.display='none';videos.style.display='flex';bar.style.display='flex';
  if(PUB){
    await room.localParticipant.enableCameraAndMicrophone();
    const cp=room.localParticipant.getTrackPublication(LivekitClient.Track.Source.Camera);
    if(cp&&cp.videoTrack){const e=cp.videoTrack.attach();e.muted=true;e.style.flex='1';e.style.minWidth='120px';videos.appendChild(e);}
    const sl=document.createElement('span');sl.textContent='🔴 EN DIRECT';sl.style='color:#e74c3c;font-size:11px;align-self:center;';
    let micOn=true,camOn=true;
    const mb=document.createElement('button');mb.textContent='🎤';mb.onclick=async()=>{micOn=!micOn;await room.localParticipant.setMicrophoneEnabled(micOn);mb.className=micOn?'':'off';};
    const cb=document.createElement('button');cb.textContent='📹';cb.onclick=async()=>{camOn=!camOn;await room.localParticipant.setCameraEnabled(camOn);cb.className=camOn?'':'off';};
    bar.append(sl,mb,cb);
  } else {
    const sl=document.createElement('span');sl.textContent='👁 Live';sl.style='align-self:center;font-size:11px;';bar.appendChild(sl);
    if(!videos.children.length){const p=document.createElement('div');p.style='color:#555;font-size:14px;';p.textContent='En attente du diffuseur...';videos.appendChild(p);}
  }
}).catch(e=>{loading.textContent='Erreur: '+e.message;});
</script>
</body>
</html>"""
}
