$(function () {
    $(document).keydown(function (event) {
        if (event.keyCode === 13) {//回车确认
            login()
        }
    })
})

//登录
function login() {
    //验证各个输入的格式是否符合规范
    let usernameInput = $("#username")
    let username = usernameInput.val()
    let passwordInput = $("#password")
    let password = passwordInput.val()
    if (username.length < 1 || username.length > 11) {
        showNoticeModal("用户名长度请保持在1~11内", usernameInput)
        return
    }
    if (password.length < 10 || password.length > 20) {
        showNoticeModal("密码长度请保持在10~20内", passwordInput)
        return
    }

    $.ajax({
        type: "PATCH",
        url: "/login",
        data: JSON.stringify({username, password}),
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.state === 0) {
                window.location.href = data.url;
            } else if (data.state === 1) {
                showNoticeModal("账户未激活，请查看邮件进行激活")
            } else if (data.state === 2) {
                showNoticeModal("用户名或密码出错", usernameInput)
            } else {
                showNoticeModal("登录出错，请重试")
            }
        },
        error: function () {
            showNoticeModal("登录失败，请重试")
        }
    })
}