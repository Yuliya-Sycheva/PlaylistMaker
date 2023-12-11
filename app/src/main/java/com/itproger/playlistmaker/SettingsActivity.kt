package com.itproger.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val imageBack = findViewById<ImageView>(R.id.back)
        imageBack.setOnClickListener {
            finish()
        }

        val imageTermsOfUse = findViewById<ImageView>(R.id.termsOfViews)
        imageTermsOfUse.setOnClickListener {
            val browseIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.terms_of_use_url)))
            startActivity(browseIntent)
        }

        val imageSupport = findViewById<ImageView>(R.id.support)
        imageSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_text))
                putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf(getString(R.string.support_email))
                )
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
                startActivity(this)
            }
        }

        val imageShare = findViewById<ImageView>(R.id.share)
        imageShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.share_email)
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

    }
}