$(function () {
    $(document).keydown(function (event) {
        if (event.keyCode === 13) {//回车确认
            register()
        }
    })
})

//处理用户提交注册的信息
function register() {
    //验证各个输入的格式是否符合规范
    let usernameInput = $('#username')
    let emailInput = $('#email')
    let passwordInput = $("#password")
    let confirmPasswordInput = $("#confirmPassword")
    let username = usernameInput.val()
    let email = emailInput.val()
    let password = passwordInput.val()
    let confirmPassword = confirmPasswordInput.val()


    if (username.length < 1 || username.length > 11) {
        showNoticeModal("用户名长度请保持在1~11内", usernameInput)
        return
    }
    let reg = /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/
    if (!reg.test(email)) {
        showNoticeModal("请输入正确的邮箱", emailInput)
        return
    }
    if (password.length < 10 || password.length > 20) {
        showNoticeModal("密码长度请保持在10~20内", passwordInput)
        return
    }
    if (password !== confirmPassword) {
        showNoticeModal("两次输入的密码一致，请重新输入", confirmPasswordInput)
        return
    }

    $.ajax({
        type: "POST",
        url: "/register",
        data: JSON.stringify({username, email, password}),
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        async: true,//默认异步
        success: function (data) {
            if (data.state === 4) {
                $(".modal").on('hidden.bs.modal', function () {
                    window.location.href = data.url
                })
                showNoticeModal("请前往邮箱确认以完成注册")
            } else if (data.state === 1) {
                showNoticeModal("用户名已被占用", usernameInput)
            } else if (data.state === 2) {
                showNoticeModal("邮箱已被占用", emailInput)
            } else {
                showNoticeModal("用户名和邮箱均已被占用", usernameInput)
            }
        },
        error: function () {
            showNoticeModal("注册失败，请重试")
        }
    })
}