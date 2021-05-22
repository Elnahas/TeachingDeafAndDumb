package com.teachingthedeafanddumb.other

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


fun createOrderNumber(): String {

    return StringBuilder().append(System.currentTimeMillis())
//            .append(abs(Random().nextInt()))
        .toString()
}


@SuppressLint("SimpleDateFormat")
internal var simpleDateFormat = SimpleDateFormat("dd MMM yyyy HH:mm")

fun convertMillisecondsToDate(milliseconds: Long): String {
    val date = Date(milliseconds)
    return simpleDateFormat.format(date)
}

//For Change Language For number
//public static DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
var decimalFormatDiscount: DecimalFormat =
    DecimalFormat("#.###", DecimalFormatSymbols(Locale.US))
var formatter: NumberFormat = decimalFormatDiscount

fun getDecimalNumber(num: Float): String? {
    return formatter.format(num)
}

fun getDecimalRate(num: Float): String? {
    return DecimalFormat("#.#", DecimalFormatSymbols(Locale.US)).format(num)
}

//This is as generic as it gets, you could use it on any Query, no need to retype it
suspend fun Query.await(): DataSnapshot = suspendCoroutine { cont ->
    addListenerForSingleValueEvent(object : ValueEventListener {

        override fun onCancelled(error: DatabaseError) {
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot != null) {
                cont.resume(snapshot)
            } else {
                cont.resumeWithException(Exception("Null data"))
            }
        }
    })
}




fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

fun getNewOrderTopic(restaurantId:String): String {
    return java.lang.StringBuilder("/topics/")
        .append(restaurantId)
        .append("_")
        .append("new_order")
        .toString()
}

fun getNewMessageTopic(restaurantId:String): String {
    return java.lang.StringBuilder("/topics/")
        .append(restaurantId)
        .append("_")
        .append("new_message")
        .toString()
}

//fun getNewsTopic(restaurantId:String): String? {
//    //Return Something Like : restaurantId_news
//    return java.lang.StringBuilder("/topics/")
//        .append(restaurantId)
//        .append("_")
//        .append("news")
//        .toString()
//
//}

fun getNewsTopic(): String? {
    //Return Something Like : restaurantId_news
    return java.lang.StringBuilder("")
        .append("news")
        .append("_")
        .append("system")
        .toString()

}

fun getNewsOfferTopic(): String? {
    //Return Something Like : restaurantId_news
    return "news_offer"
//    java.lang.StringBuilder("news_offer")
//        .append("news")
//        .append("_")
//        .append("offer")
//        .toString()

}

fun getInstagram(context: Context) {

    val uri: Uri = Uri.parse("https://www.instagram.com/DelivercoApp/")
    val likeIng = Intent(Intent.ACTION_VIEW, uri)

    likeIng.setPackage("com.instagram.android")

    try {
        context.startActivity(likeIng)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.instagram.com/DelivercoApp/")
            )
        )
    }

}

fun getFacebook(context: Context) {

    val uri: Uri = Uri.parse("https://www.facebook.com/deliveroo.apps")
    val likeIng = Intent(Intent.ACTION_VIEW, uri)

    likeIng.setPackage("com.facebook.katana")

    try {
        context.startActivity(likeIng)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.facebook.com/deliveroo.apps")
            )
        )
    }

}


fun getTwitter(context: Context) {

    val uri: Uri = Uri.parse("https://twitter.com/DelivercoApp/")
    val page = Intent(Intent.ACTION_VIEW, uri)

    page.setPackage("com.twitter.android")

    try {
        context.startActivity(page)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/DelivercoApp/")
            )
        )
    }

}


  fun getWhatsApp(context: Context) {
//      try {
//          val sendIntent = Intent().apply {
//              action = Intent.ACTION_SEND
//              putExtra(Intent.EXTRA_TEXT, "Hello Swapz")
//              putExtra("jid", "01066768111@s.whatsapp.net")
//              type = "text/plain"
//              setPackage("com.whatsapp")
//          }
//          context.startActivity(sendIntent)
//      }catch (e: Exception){
//          e.printStackTrace()
//          val appPackageName = "com.whatsapp"
//          try {
//              context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
//          } catch (e :android.content.ActivityNotFoundException) {
//              context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
//          }
//      }

      val url: String ="https://api.whatsapp.com/send?phone=+201201847387"

      try {
          val pm: PackageManager = context.packageManager
          pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
          val i = Intent(Intent.ACTION_VIEW)
          i.data = Uri.parse(url)
          context.startActivity(i)
      } catch (e: PackageManager.NameNotFoundException) {
          context.startActivity(
              Intent(
                  Intent.ACTION_VIEW,
                  Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")
              )
          )
      }





  }