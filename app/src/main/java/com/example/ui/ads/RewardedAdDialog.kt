package com.example.ui.ads

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ads.AdManager
import com.example.model.Movie

@Composable
fun RewardedAdDialog(
    movie: Movie,
    onDismiss: () -> Unit,
    onUnlockedAndPlay: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    var isLoadingAd by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = "Rewarded Ad Requirement",
                    tint = Color(0xFFE50914),
                    modifier = Modifier.size(54.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "আজকের প্রথম ভিডিও আনলক করুন 🎬",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "ভিডিও প্লে করার জন্য প্রতিদিন প্রথমবার ১টি rewarded ad দেখতে হবে। এড দেখা শেষে ভিডিও স্বয়ংক্রিয়ভাবে চালু হবে।",
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFFF6B35),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (activity != null) {
                            isLoadingAd = true
                            errorMessage = null
                            AdManager.showRewardedAdForVideoUnlock(
                                activity = activity,
                                onSuccess = {
                                    isLoadingAd = false
                                    Toast.makeText(context, "ভিডিও আনলক হয়েছে! 🎉", Toast.LENGTH_SHORT).show()
                                    onUnlockedAndPlay()
                                    onDismiss()
                                },
                                onFailedToLoad = { err ->
                                    isLoadingAd = false
                                    errorMessage = "$err (পুনরায় চেষ্টা করুন অথবা বাইপাস করুন)"
                                }
                            )
                        } else {
                            // Fallback if no activity reference
                            AdManager.markDailyRewardedAdWatched(context)
                            onUnlockedAndPlay()
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoadingAd
                ) {
                    if (isLoadingAd) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    } else {
                        Text(
                            text = "এড দেখুন এবং ভিডিও চালান 🎬",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            // If ad fails to serve due to inventory or network error, allow user to play video
                            AdManager.markDailyRewardedAdWatched(context)
                            onUnlockedAndPlay()
                            onDismiss()
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("সরাসরি ভিডিও প্লে করুন (Bypass)", color = Color.White, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("পরে দেখব (Cancel)", color = Color.Gray, fontSize = 13.sp)
                }
            }
        }
    }
}
