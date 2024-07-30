package com.schoolprojects.corrreps.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import kotlin.random.Random


//toast function
fun Context.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


/*
@Composable
fun LoadingDialog(modifier: Modifier = Modifier) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))

    Box(
        modifier = Modifier
            .width(120.dp)
            .height(130.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.Transparent)
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever, modifier = Modifier.padding(4.dp)
        )
    }
}
*/


fun openWhatsapp(phoneNumber: String, context: Context) {

    val pm = context.packageManager
    val waIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber")
    )
    val info = pm.queryIntentActivities(waIntent, 0)
    if (info.isNotEmpty()) {
        context.startActivity(waIntent)
    } else {
        context.toast("WhatsApp not Installed")
    }
}

fun openDial(phoneNumber: String, context: Context) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    context.startActivity(intent)
}


// Helper function to generate a payment reference
fun generatePaymentRef(): String {
    var seed: Long = System.currentTimeMillis()
    seed++
    val randomNumber = Random(seed).nextInt(100000, 999999.toInt())
    return "REF$randomNumber"
}


/*
fun copyToClipboard(context: Context, text: String, label: String = "Copied Text") {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)

    // Optionally show a toast message to indicate successful copy
    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
}*/
