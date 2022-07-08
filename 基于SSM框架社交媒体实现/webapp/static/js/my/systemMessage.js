//消息页数相关设置、请求切换
$(function () {
    //分页相关，系统消息每页最多10条
    $('#systemMsgPagination').jqPaginator({
        totalPages: Number($('#totalPages').val()),
        visiblePages: 5,
        currentPage: Number($('#currentPage').val()),
        first: '<li class="prev page-item"><a class="page-link" href="javascript:;">首页</a></li>',
        prev: '<li class="prev page-item"><a class="page-link" href="javascript:;">上一页</a></li>',
        next: '<li class="next page-item"><a class="page-link" href="javascript:;">下一页</a></li>',
        last: '<li class="prev page-item"><a class="page-link" href="javascript:;">末页</a></li>',
        page: '<li class="page page-item"><a class="page-link" href="javascript:;">{{page}}</a></li>',
        onPageChange: function (num, type) {
            if (type === 'change') {//请求分页的数据
                window.location.href = '/systemMessage/' + num
            }
        }
    })
})

//回应是否同意添加好友
function responseAddFriend(btn) {
    let messageId = $(btn).val()
    let flag = false
    if ($(btn).hasClass('agree')) {
        flag = true
    }
    $.ajax({
        type: 'POST',
        url: '/responseAddFriend',
        data: JSON.stringify({messageId, flag}),
        contentType: 'application/json;charset=utf-8',
        dataType: 'json',
        success: function (data) {
            if (data.state === 4) {
                showNoticeModal('已成功添加该好友')
            } else if (data.state === 3) {
                showNoticeModal('该消息已失效')
            } else if (data.state === 2) {
                showNoticeModal('已拒绝添加该好友')
            } else {
                showNoticeModal('出错了，请重试')
            }
        },
        error: function () {
            showNoticeModal('回复失败，请重试')
        }
    })
}

//删除系统消息
function deleteSystemMsg(btn) {
    let messageId = $(btn).val()
    $.ajax({
        type: 'DELETE',
        url: '/systemMessage',
        contentType: 'application/json;charset=utf-8',
        dataType: 'json',
        data: JSON.stringify({messageId}),
        success: function (data) {
            if (data.state === 0) {
                showNoticeModal('消息删除出错了，请重试')
            } else {
                $(btn).parent().remove()//前端页面删除该信息
            }
        },
        error: function () {
            showNoticeModal('服务器连接失败，请重试')
        }
    })
}