let originNickname

$(function () {
    originNickname = $('#nickname').val()
})

//修改好友昵称
function modifyNickname() {
    $('#nickname').attr('disabled', false);
    $('#cancelModifyNicknameBtn').removeClass('d-none')
    $('#confirmModifyNicknameBtn').removeClass('d-none')
    $('#modifyNicknameBtn').addClass('d-none')
}

//确认提交修改昵称
function confirmModifyNickname() {
    let nameInput = $('#nickname')
    let nickname = nameInput.val()
    if (nickname.length < 1 || nickname.length > 11) {
        showNoticeModal('昵称长度请保持在1~11字符以内', nameInput)
        return
    }
    if (nickname === originNickname) {
        showNoticeModal('新的好友昵称与原昵称相同', nameInput)
        return
    }

    let friendId = $('#userId').val()
    $.ajax({
        type: 'patch',
        url: '/friendNickname',
        data: JSON.stringify({nickname, friendId}),
        contentType: 'application/json;charset=utf-8',
        dataType: 'json',
        success: function (data) {
            if (data.state === 1) {//修改成功
                originNickname = nickname
                cancelModifyNickname()
            } else {
                showNoticeModal('昵称修改失败')
            }
        },
        error: function () {
            showNoticeModal('连接服务器失败')
        }
    })
}

//取消修改好友昵称
function cancelModifyNickname() {
    let nameInput = $('#nickname')
    nameInput.val(originNickname)
    nameInput.attr('disabled', true)
    $('#modifyNicknameBtn').removeClass('d-none')
    $('#confirmModifyNicknameBtn').addClass('d-none')
    $('#cancelModifyNicknameBtn').addClass('d-none')
}

//发送消息
function sendMessage() {
    let friendId = $('#userId').val()
    $.ajax({
        type: 'post',
        url: '/chatUserList',
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({friendId}),
        success: function () {
            window.location.href = '/chat'
        },
        error: function () {
            showDeleteModal('发送消息请求出错，请重试')
        }
    })
}

//显示确认删除好友的modal
function showDeleteModal() {
    $('#confirmDeleteModal div div .modal-body').html('确认删除该好友吗？')
    $('#confirmDelete').attr('onclick', 'deleteUserFriend()')
    $('#confirmDeleteModal').modal('show')
}

//删除好友
function deleteUserFriend() {
    $('#confirmDeleteModal').modal('hide')
    $('#noticeModal').modal('hide')
    let friendId = $('#userId').val()
    $.ajax({
        type: 'delete',
        url: '/friend',
        data: JSON.stringify({friendId}),
        contentType: "application/json;charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data.state === 3) {//好友删除成功
                window.location.href = '/notUserFriendProfile/' + friendId
            } else {
                showNoticeModal('好友删除失败')
            }
        },
        error: function () {
            showNoticeModal('连接服务器失败')
        }
    })
}