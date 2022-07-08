//发送添加好友请求
function sendAddFriendRequest() {
    let friendId = $('#friendId').val()
    $.ajax({
        type: 'post',
        url: '/addFriend',
        data: JSON.stringify({friendId}),
        contentType: "application/json;charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data.state === 1) {
                showNoticeModal("已成功发送添加好友请求")
            } else if (data.state === 2) {
                showNoticeModal("您已经添加过该好友，请耐心等待回复")
            } else if (data.state === 3) {
                showNoticeModal('请前往系统消息查看对方添加您的信息')
            } else if (data.state === 4) {
                showNoticeModal('你们已经是好友，无须重复添加')
            } else {
                showNoticeModal("好友添加失败，请重试")
            }
        },
        error: function () {
            showNoticeModal("请求出错，请重试")
        }
    })
}