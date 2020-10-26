package com.novyapp.superplanning

fun String.fromISOtoNice(): String {
    return "${this.substring(0,4)}/${this.substring(5,7)}/${this.substring(8,10)}"
}
//fun CharSequence.fromISOtoNice(): String {
//    return "${this.substring(0,3)}/${this.substring(5,6)}/${this.substring(7,8)}"
//}