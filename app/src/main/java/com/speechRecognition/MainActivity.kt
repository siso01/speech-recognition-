package com.speechRecognition

import android.app.Activity
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.speechRecognition.databinding.ActivityMainBinding
import java.lang.Exception
import java.util.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private val TAG = MainActivity::class.java.simpleName
    val RECOGNIZER_RESULT = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        binding.imgBtnSpeech.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val speechIntent : Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"speech to Text")
        startActivityForResult(speechIntent,RECOGNIZER_RESULT,)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK && data != null){
            val matches : ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            binding.tv.text = matches.get(0).toString()
        }else if (resultCode == RESULT_CANCELED){
            binding.tv.text = getString(R.string.voice_cancel)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.share -> {
                shareText()
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareText() {
        try {
                if (binding.tv.text.isNotEmpty()) {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_sub))
                        putExtra(Intent.EXTRA_TEXT, binding.tv.text)

                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }else{
                    Toast.makeText(this, R.string.empty_text, Toast.LENGTH_SHORT).show()
                }

        } catch (e: Exception) {
            Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show()
        }
    }
}