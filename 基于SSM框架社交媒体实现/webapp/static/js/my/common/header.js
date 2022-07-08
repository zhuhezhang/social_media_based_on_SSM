// 根据浏览器高度调整main标签高度
// function adjustMainHeight() {
//     let h2 = window.innerHeight
//     console.log((h2 - 74 - 2) + "px")
//     $("main").css("height", (h2 - 74 - 2) + "px")
// }

// 根据具体页面给相应的列表元素class属性追加active
function adjustHeaderLiClass() {
    let text = $("title:eq(0)").text()
    if (text === "消息") {
        $("#message").addClass("active")
    } else if (text === "好友") {
        $("#friend").addClass("active")
    } else if (text === "随说") {
        $("#casualWord").addClass("active")
    }else if (text === "问答") {
        $("#qAndA").addClass("active")
    }
}

// window.addEventListener("load", adjustMainHeight)
// window.addEventListener("resize", adjustMainHeight)
window.addEventListener("load", adjustHeaderLiClass)