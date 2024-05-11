package com.itproger.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.sharing.domain.ExternalNavigator
import com.itproger.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    private val emailData = EmailData()

    override fun shareLink() {
        context.startActivity(
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    context.getString(R.string.share_email)
                )
                type = "text/plain"
                flags = FLAG_ACTIVITY_NEW_TASK  //т.к. запускаем не из Activity
            }, null
        )
    }

    override fun openLink() {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(context.getString(R.string.terms_of_use_url))
            ).setFlags(FLAG_ACTIVITY_NEW_TASK) //т.к. запускаем не из Activity
        )
    }

    override fun openEmail() {
        context.startActivity(
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(emailData.mailTo)
                putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_text))
                putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf(context.getString(R.string.support_email))
                )
                putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_subject))
                flags = FLAG_ACTIVITY_NEW_TASK
            }
        )
    }
}