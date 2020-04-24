package com.mufeng.kotlin.foundationType

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*



fun main() {
    print(DateUtils.dateToWeek("2020-03-31"))
}



object DateUtils {

    const val PATTERN_1 = "yyyy-MM-dd HH:mm:ss"
    const val PATTERN_2 = "yyyy-MM-dd HH:mm"
    const val PATTERN_3 = "yyyy-MM-dd"
    const val PATTERN_4 = "yyyy/MM/dd HH:mm:ss"
    const val PATTERN_5 = "yyyy/MM/dd HH:mm"
    const val PATTERN_6 = "yyyy/MM/dd"
    const val PATTERN_7 = "yyyy年MM月dd日"
    const val PATTERN_8 = "yyyyMMdd"
    const val PATTERN_9 = "MM"
    const val PATTERN_10 = "dd"
    const val PATTERN_11 = "yyyy/MM"
    const val PATTERN_12 = "yyyy-MM"
    const val PATTERN_13 = "HH:mm"
    const val PATTERN_14 = "MM-dd"
    const val PATTERN_15 = "MM/dd"
    const val PATTERN_16 = "yyyy-MM-dd'T'HH:mm"

    fun formatDateT(dateStr: String, pattern: String = PATTERN_1): String{
        val date = SimpleDateFormat(PATTERN_16).parse(dateStr)
        return SimpleDateFormat(pattern).format(date!!)
    }

    fun dateToWeek(datetime: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val weekDays = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val cal = Calendar.getInstance()
        val date: Date
        try {
            date = sdf.parse(datetime)
            cal.time = date
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val w = cal.get(Calendar.DAY_OF_WEEK) - 1
        return weekDays[w]
    }

    fun formatWeekT(dateStr: String): String{
        val date = SimpleDateFormat(PATTERN_16).parse(dateStr)
        val format: SimpleDateFormat
        var hintDate = ""
        //先获取年份
        val year = Integer.valueOf(SimpleDateFormat("yyyy").format(date))
        //获取一年中的第几天
        val day = Integer.valueOf(SimpleDateFormat("d").format(date))
        //获取当前年份 和 一年中的第几天
        val currentDate = Date(System.currentTimeMillis())
        val currentYear = Integer.valueOf(SimpleDateFormat("yyyy").format(currentDate))
        val currentDay = Integer.valueOf(SimpleDateFormat("d").format(currentDate))
        //计算 如果是去年的
        if (currentYear - year == 1) {
            //如果当前正好是 1月1日 计算去年有多少天，指定时间是否是一年中的最后一天
            if (currentDay == 1) {
                val yearDay: Int = if (year % 400 == 0) {
                    366//世纪闰年
                } else if (year % 4 == 0 && year % 100 != 0) {
                    366//普通闰年
                } else {
                    365//平年
                }
                if (day == yearDay) {
                    hintDate = "昨天"
                }
            }
        } else {
            when {
                currentDay - day == 1 -> hintDate = "昨天"
                currentDay - day == 0 -> hintDate = "今天"
                currentDay - day == -1 -> hintDate = "明天"
                currentDay - day == -2 -> hintDate = "后天"
                else ->{
                    val weekDays = arrayListOf("周日","周一","周二","周三","周四","周五","周六")
                    val calendar = Calendar.getInstance()
                    var weekIndex = calendar.get(Calendar.DAY_OF_WEEK) -1
                    if (weekIndex < 0)weekIndex = 0
                    hintDate =  weekDays[weekIndex]
                }
            }
        }
        return if (hintDate.isEmpty()) {
            format = SimpleDateFormat("yyyy-MM-dd HH:mm")
            format.format(date)
        } else {
            hintDate
        }
    }

}

fun String.format(): Long{
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")

    return format.parse(this.substring(0,16)).time
}

/**
 * 格式化时间（输出类似于 刚刚, 4分钟前, 一小时前, 昨天这样的时间）
 *
 * @param time
 * @return
 */
fun Date.formatDisplayTime(): String {

    if (this == null) {
        return ""
    }

    var display = ""
    val tMin = 60 * 1000
    val tHour = 60 * tMin
    val tDay = 24 * tHour



    try {

        val today = Date()
        val thisYearDf = SimpleDateFormat("yyyy")
        val todayDf = SimpleDateFormat("yyyy-MM-dd")
        val thisYear = Date(thisYearDf.parse(thisYearDf.format(today)).time)
        val yesterday = Date(todayDf.parse(todayDf.format(today)).time)
        val beforeYes = Date(yesterday.time - tDay)
        if (this != null) {
            val halfDf = SimpleDateFormat("MM月dd日")
            val dTime = today.time - this.time
            if (this.before(thisYear)) {
                display = SimpleDateFormat("yyyy年MM月dd日").format(this)
            } else {

                if (dTime < tMin) {
                    display = "刚刚"
                } else if (dTime < tHour) {
                    display = Math.ceil((dTime / tMin).toDouble()).toInt().toString() + "分钟前"
                } else if (dTime < tDay && this.after(yesterday)) {
                    display = Math.ceil((dTime / tHour).toDouble()).toInt().toString() + "小时前"
                } else if (this.after(beforeYes) && this.before(yesterday)) {
                    display = "昨天  " + SimpleDateFormat("HH:mm").format(this)
                } else {
                    display = SimpleDateFormat("MM-dd HH:mm").format(this)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return display
}