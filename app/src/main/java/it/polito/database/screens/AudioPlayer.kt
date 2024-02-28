package it.polito.database.screens

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import it.polito.database.AppViewModel
import it.polito.database.R

class AudioPlayer(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var startHandler: Handler? = null
    private var stopHandler: Handler? = null

    fun playAudioWithDelay(delayMilliseconds: Long, stopDelayMilliseconds: Long,viewModel: AppViewModel, audioPlayerShouldRun: Boolean) {
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm)
        startHandler = Handler(Looper.getMainLooper())
        stopHandler = Handler(Looper.getMainLooper())

        startHandler?.postDelayed({
            mediaPlayer?.start()
            viewModel.hoChiusoLoSportello.value=false
            if(!audioPlayerShouldRun){
                stopAudio()
            }

        }, delayMilliseconds)

        stopHandler?.postDelayed({
            stopAudio()
            viewModel.hoChiusoLoSportello.value=true
        }, delayMilliseconds + stopDelayMilliseconds)
    }

    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}