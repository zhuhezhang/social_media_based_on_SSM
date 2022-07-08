$(function (){
    $(document).keydown(function (event) {
        if (event.keyCode === 13) {//回车确认
            forgetPwd()
        }
    })
})

//忘记密码
function forgetPwd() {
    //验证各个输入的格式是否符合规范
    let emailInput = $("#email")
    let passwordInput = $("#password")
    let confirmPwdInput = $("#confirmPassword")
    let email = emailInput.val()
    let password = passwordInput.val()
    let confirmPwd = confirmPwdInput.val()

    let reg = /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/
    if (!reg.test(email)) {
        showNoticeModal("请输入正确的邮箱", emailInput)
        return
    }
    if (password.length < 10 || password.length > 20) {
        showNoticeModal("密码长度请保持在10~20内", passwordInput)
        return;
    }
    if (password !== confirmPwd) {
        showNoticeModal("两次输入的密码一致，请重新输入", confirmPwdInput)
        return
    }

    $.ajax({
        type: "PATCH",
        url: "/forgetPwd",
        data: JSON.stringify({email, password}),
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            if (data.status === 200) {
                $(".modal").on('hidden.bs.modal', function () {
                    window.location.href = "/"//绑定模态框事件
                })
                showNoticeModal("请前往邮箱确认以完成密码修改")
            } else {
                showNoticeModal("账号不存在", emailInput)
            }
        },
        error: function () {
            showNoticeModal("密码修改失败，请重试")
        }
    });
}