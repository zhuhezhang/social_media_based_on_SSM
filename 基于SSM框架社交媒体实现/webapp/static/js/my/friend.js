//根据用户名查找用户
function searchUserByUsername() {
    let usernameInput = $('#username');
    let username = usernameInput.val()

    if (username.length < 1 || username.length > 11) {
        showNoticeModal('用户名长度请保持在1~11内', usernameInput)
        return
    }

    $.ajax({
        type: "get",
        url: "/user/name/" + username,
        dataType: "json",
        success: function (data) {
            if (data.state === 3) {//用户非好友界面
                window.location.href = "/notUserFriendProfile/" + data.id
            } else if (data.state === 2) {//用户好友界面
                window.location.href = "/userFriendProfile/" + data.id
            } else if (data.state === 0) {//用户不存在
                showNoticeModal("用户不存在", usernameInput)
            } else {//用户本人
                window.location.href = "/myProfile"
            }
        },
        error: function () {
            showNoticeModal("用户查找出错，请重试")
        }
    })
}

//根据用户账号跳转到显示用户该好友的信息界面
function showUserFriendProfile(thisBtn) {
    window.location.href = "/userFriendProfile/" + $(thisBtn).val()
}