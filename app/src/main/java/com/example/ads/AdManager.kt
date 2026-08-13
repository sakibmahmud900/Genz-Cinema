package com.example.ads

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AdManager {

    private const val TAG = "AdManager"

    // Ad Unit IDs provided by user
    const val APP_ID = "ca-app-pub-9158495383522549~8550840170"
    const val APP_OPEN_AD_ID = "ca-app-pub-9158495383522549/5028146103"
    const val BANNER_AD_ID = "ca-app-pub-9158495383522549/5792960681"
    const val INTERSTITIAL_AD_ID = "ca-app-pub-9158495383522549/7098157707"
    const val NATIVE_AD_ID = "ca-app-pub-9158495383522549/8547116532"
    const val REWARDED_AD_ID = "ca-app-pub-9158495383522549/9867937069"

    private var appOpenAd: AppOpenAd? = null
    private var interstitialAd: InterstitialAd? = null
    private var isAppOpenAdLoading = false

    fun initialize(context: Context) {
        MobileAds.initialize(context) { initializationStatus ->
            Log.d(TAG, "AdMob initialized: $initializationStatus")
            loadAppOpenAd(context)
            loadInterstitialAd(context)
        }
    }

    // --- APP OPEN AD ---
    fun loadAppOpenAd(context: Context) {
        if (isAppOpenAdLoading || appOpenAd != null) return
        isAppOpenAdLoading = true

        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context,
            APP_OPEN_AD_ID,
            request,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isAppOpenAdLoading = false
                    Log.d(TAG, "App Open Ad loaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    isAppOpenAdLoading = false
                    Log.e(TAG, "App Open Ad failed to load: ${loadAdError.message}")
                }
            }
        )
    }

    fun showAppOpenAdIfAvailable(activity: Activity) {
        appOpenAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    loadAppOpenAd(activity)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    loadAppOpenAd(activity)
                }
            }
            ad.show(activity)
        } ?: run {
            loadAppOpenAd(activity)
        }
    }

    // --- INTERSTITIAL AD ---
    fun loadInterstitialAd(context: Context) {
        val request = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            INTERSTITIAL_AD_ID,
            request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    Log.d(TAG, "Interstitial Ad loaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    Log.e(TAG, "Interstitial Ad failed to load: ${loadAdError.message}")
                }
            }
        )
    }

    fun showInterstitialAd(activity: Activity, onAdDismissed: () -> Unit) {
        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitialAd(activity)
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    loadInterstitialAd(activity)
                    onAdDismissed()
                }
            }
            ad.show(activity)
        } ?: run {
            onAdDismissed()
            loadInterstitialAd(activity)
        }
    }

    // --- REWARDED AD (DAILY FIRST-TIME VIDEO REQUIREMENT) ---
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("genz_cinema_ads_prefs", Context.MODE_PRIVATE)
    }

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun isDailyRewardedAdWatchedToday(context: Context): Boolean {
        val prefs = getPrefs(context)
        val lastWatchedDate = prefs.getString("last_rewarded_date", "")
        return lastWatchedDate == getTodayDateString()
    }

    fun markDailyRewardedAdWatched(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit().putString("last_rewarded_date", getTodayDateString()).apply()
        Log.d(TAG, "Daily rewarded ad marked watched for today: ${getTodayDateString()}")
    }

    fun showRewardedAdForVideoUnlock(
        activity: Activity,
        onSuccess: () -> Unit,
        onFailedToLoad: (String) -> Unit
    ) {
        val request = AdRequest.Builder().build()
        RewardedAd.load(
            activity,
            REWARDED_AD_ID,
            request,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            // User finished or dismissed ad
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Log.e(TAG, "Rewarded Ad failed to show: ${adError.message}")
                            onFailedToLoad("এড দেখাতে সমস্যা হয়েছে: ${adError.message}")
                        }
                    }

                    rewardedAd.show(activity) { rewardItem ->
                        Log.d(TAG, "User earned reward: ${rewardItem.amount} ${rewardItem.type}")
                        markDailyRewardedAdWatched(activity)
                        onSuccess()
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e(TAG, "Rewarded Ad failed to load: ${loadAdError.message}")
                    onFailedToLoad("এড লোড হয়নি: ${loadAdError.message}")
                }
            }
        )
    }
}
