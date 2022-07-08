$(function () {
    //格式化时间
    $('.time').each(function () {
        $(this).text(formatTimeStamp(Number($(this).text())))
    })
})

//根据传入时间戳判断是否同一天返回时间or日期
function formatTimeStamp(time) {
    time = new Date(time)
    let currentTime = new Date()
    let year = time.getFullYear()//年
    let month = time.getMonth()//月.月份0开始
    let day = time.getDate()//日
    if (year === currentTime.getFullYear() && month === currentTime.getMonth() && day === currentTime.getDate()) {//今天的消息则显示时间
        if (time.getMinutes() < 10) {
            return time.getHours() + ':0' + time.getMinutes()
        } else {
            return time.getHours() + ':' + time.getMinutes()
        }
    } else {//否则显示日期
        month += 1
        return year + '-' + month + '-' + day
    }
}