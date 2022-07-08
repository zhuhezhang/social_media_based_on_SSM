//修改密码验证、提交
function modifyPwd() {
    //验证密码格式是否符合规范
    let passwordInput = $("#password")
    let confirmPwdInput = $("#confirmPwd")
    let password = passwordInput.val()
    let confirmPwd = confirmPwdInput.val()

    if (password.length < 10 || password.length > 20) {
        showNoticeModal("密码长度请保持在10~20内", passwordInput)
        return
    }
    if (password !== confirmPwd) {
        showNoticeModal("两次输入的密码一致，请重新输入", confirmPwdInput)
        return;
    }

    $.ajax({
        type: "PATCH",
        url: "/modifyPwd",
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        data: JSON.stringify({password}),
        success: function (data) {
            if (data.state === 1) {
                showNoticeModal("密码已成功修改")
                passwordInput.val("")
                confirmPwdInput.val("")
            } else {
                showNoticeModal("密码修改失败")
            }
        },
        error: function () {
            showNoticeModal("密码修改出错，请重试")
        }
    })
}