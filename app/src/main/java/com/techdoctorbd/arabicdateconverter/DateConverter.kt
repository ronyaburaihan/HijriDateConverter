package com.techdoctorbd.arabicdateconverter

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

object DateConverter {

    private val monthRanges = arrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    /*private val monthNames = arrayOf("মহররম", "সফর", "রবিউল আউয়াল", "রবিউস সানি", "জমাদিউল আউয়াল", "জমাদিউস সানি", "রজব", "শা‘বান", "রমজান", "শাওয়াল", "জ্বিলকদ", "জ্বিলহজ্জ")*/

    private val dayNames =
            arrayOf("শনিবার", "রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার", "শুক্রবার")

    private val banglaDigits = arrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
    private val englishDigits = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')

    fun getDateInArabic(
            date: Int,
            month: Int,
            year: Int,
            dayName: String,
            context: Context
    ): String? {
        var arabicDate: String? = null
        val arabicDateInt: Int?

        val dateInString = "$year-${month.toTwoDigits()}-${date.toTwoDigits()}"
        val hijriMonthList = getArabicMonthList(context)

        Log.d(
                "DATE_STRING",
                dateInString
        )

        for (position in hijriMonthList!!.indices) {
            val hijriMonthItem = hijriMonthList[position]
            if (hijriMonthItem.startDate <= dateInString && dateInString <= hijriMonthItem.endDate) {
                val startDate = hijriMonthItem.startDate.substring(8).toInt()
                val startMonth = hijriMonthItem.startDate.substring(5, 7).toInt()

                val endDate = hijriMonthItem.endDate.substring(8).toInt()
                val endMonth = hijriMonthItem.endDate.substring(5, 7).toInt()

                Log.d(
                        "DATE_SUBSTRING",
                        "StartDate: $startDate StartMonth: $startMonth EndDate: $endDate EndMonth: $endMonth"
                )
                when {
                    startMonth == endMonth -> {
                        arabicDateInt = date - (startDate - 1)
                        arabicDate =
                                "$arabicDateInt ${hijriMonthItem.monthName} ${hijriMonthItem.hijriYear}"
                    }
                    month == startMonth -> {
                        arabicDateInt = date - (startDate - 1)
                        arabicDate =
                                "$arabicDateInt ${hijriMonthItem.monthName} ${hijriMonthItem.hijriYear}"
                    }
                    else -> {
                        arabicDateInt = (monthRanges[startMonth - 1] - (startDate - 1)) + date
                        arabicDate =
                                "$arabicDateInt ${hijriMonthItem.monthName} ${hijriMonthItem.hijriYear}"
                    }
                }

                break
            }
        }

        return "${arabicDate!!.convertStringToBangla()} , ${getDayNamesInBangla(dayName)}"
    }

    private fun String.convertStringToBangla(): String? {
        var resultString = ""
        var replaceableChar: Char

        for (position in this.indices) {
            val char = this[position]
            replaceableChar = if (char in englishDigits) {
                val index = englishDigits.indexOf(char)
                banglaDigits[index]
            } else {
                char
            }

            resultString += replaceableChar
        }

        return resultString
    }

    private fun getDayNamesInBangla(dayName: String): String? {
        return when (dayName) {
            "saturday" -> dayNames[0]
            "sunday" -> dayNames[1]
            "monday" -> dayNames[2]
            "tuesday" -> dayNames[3]
            "wednesday" -> dayNames[4]
            "thursday" -> dayNames[5]
            "friday" -> dayNames[6]
            else -> ""
        }
    }

    private fun getArabicMonthList(context: Context): MutableList<HijriMonth>? {
        try {
            val stream = context.assets.open("hijri-month.json")

            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()

            val tContents = String(buffer)

            val groupListType = object : TypeToken<ArrayList<HijriMonth>>() {}.type
            val gson = GsonBuilder().create()
            return gson.fromJson(tContents, groupListType)

        } catch (exception: Exception) {
            exception.printStackTrace()
            return null
        }
    }

    private fun Int.toTwoDigits(): String {
        return ((if (this < 10) "0" else "") + this)
    }
}
