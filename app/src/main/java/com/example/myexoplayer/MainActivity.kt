package com.example.myexoplayer

import android.content.ComponentName
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.myexoplayer.PlaybackService.Companion.player
import com.example.myexoplayer.databinding.ActivityMainBinding
import com.google.common.util.concurrent.MoreExecutors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()

        binding.btnPlayPause.setOnClickListener {
            if (isClicked) {
                isClicked = false
                pausePlayer()
                binding.btnPlayPause.text = "Play"
            }
            else {
                isClicked = true
                startPlayer()
                binding.btnPlayPause.text = "Pause"
            }
        }
    }

    private fun pausePlayer() {
        player.playWhenReady = false
        player.playbackState
    }

    private fun startPlayer() {
        player.playWhenReady = true
        player.playbackState
    }

    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener(
            { binding.playerView.player = controllerFuture.get() },
            MoreExecutors.directExecutor()
        )
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.playerView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}