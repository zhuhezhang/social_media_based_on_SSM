//随说通用js

//取消点赞或者点赞
function unlikeOrLike(thisLabel) {
    let isLike = false//点赞标志，false取消点赞，true点赞
    if ($(thisLabel).prop('title') === '点赞') {
        isLike = true
    }
    let casualWordId = Number($(thisLabel).parent().children(':first-child').val())
    $.ajax({
        type: 'post',
        url: '/casualWord/like',
        data: JSON.stringify({isLike, casualWordId}),
        contentType: "application/json;charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data.state === 2) {
                if (isLike) {
                    $(thisLabel).prop('src', '../../static/img/like.svg')
                    $(thisLabel).prop('title', '取消点赞')
                } else {
                    $(thisLabel).prop('src', '../../static/img/unlike.svg')
                    $(thisLabel).prop('title', '点赞')
                }
            } else {
                showNoticeModal('点赞或取消点赞失败')
            }
        },
        error: function () {
            showNoticeModal('服务出错，请重试')
        }
    })
}

//显示评论模态框，传入参数为调用为此函数的标签，用来获取随说id、评论id、标志位
function showCommentModal(thisLabel) {
    $('#commentOrAnswerContent').attr('placeholder', '评论内容')
    $('[for="commentOrAnswerContent"]').text('评论内容')
    $('#confirmPublishCommentOrAnswer').attr('onclick', 'publishComment(this)')
    let id = $(thisLabel).parent().children(':first-child').val()//随说/评论id
    let flag = $(thisLabel).parent().children(':nth-child(2)').val()//0-随说页面评论；1-随说详细页面评论；2-随说详细页面回复评论
    $('.modal-footer #confirmPublishCommentOrAnswer').val(id)
    $('.modal-footer #flag').val(flag)
    $('#publishCommentOrAnswerModal').modal('show')
}

//发布评论，传入参数为确认按钮
function publishComment(btn) {
    let id = $(btn).val()//评论的随说id/回复的评论id
    let commentContent = $('#commentOrAnswerContent').val()//评论内容
    if (commentContent.length === 0) {
        showErrorInfoOfComment('评论内容不能为空')
        return
    }
    if (commentContent.length > 100) {
        showErrorInfoOfComment('评论字符个数应在100以内')
        return
    }
    let flag = Number($('.modal-footer #flag').val())
    $.ajax({
        type: 'post',
        url: '/casualWord/comment',
        data: JSON.stringify({commentContent, id, flag}),
        contentType: "application/json;charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data['status'] === 200) {
                $('#publishCommentOrAnswerModal').modal('hide')
                if (flag === 0) {
                    showNoticeModal('评论成功')
                } else if (flag === 1) {//随说详细界面评论随说
                    $('.casualWordComment').prepend('' +
                        '                <div class="aComment">\n' +
                        '                    <div class="d-flex align-items-center">\n' +
                        '                        <input type="hidden" value="' + data['commentId'] + '">\n' +
                        '                        <input type="hidden" value="2">\n' +
                        '                        <img src="' + $('header .dropdown a img').prop('src') + '" class="rounded-circle" height="35"\n' +
                        '                             width="35px" alt="">\n' +
                        '                        <a href="/myProfile">' + data['myName'] + '</a>\n' +
                        '                        <span>：</span>\n' +
                        '                        <span class="time" style="margin-left: auto">' + formatTimeStamp(data['time']) + '</span>\n' +
                        '                        <span onclick="showCommentModal(this)"\n' +
                        '                              class="responseComment">回复</span>\n' +
                        '                    </div>\n' +
                        '                    <span class="d-block">' + commentContent + '</span>\n' +
                        '                </div>')
                } else {//随说详细界面回复评论
                    $('.casualWordComment').prepend('' +
                        '                <div class="aComment">\n' +
                        '                    <div class="d-flex align-items-center">\n' +
                        '                        <input type="hidden" value="' + data['commentId'] + '">\n' +
                        '                        <input type="hidden" value="2">\n' +
                        '                        <img src="' + $('header .dropdown a img').prop('src') + '" class="rounded-circle" height="35"\n' +
                        '                             width="35px" alt="">\n' +
                        '                        <a href="/myProfile">' + data['myName'] + '</a>\n' +
                        '                        <span>回复</span>\n' +
                        '                        <a href="/userFriendProfile/' + data['respondentId'] + '">' + data['respondentName'] + '</a>\n' +
                        '                        <span>：</span>\n' +
                        '                        <span class="time" style="margin-left: auto">' + formatTimeStamp(data['time']) + '</span>\n' +
                        '                        <span onclick="showCommentModal(this)" class="responseComment">回复</span>\n' +
                        '                    </div>\n' +
                        '                    <span class="d-block">' + commentContent + '</span>\n' +
                        '                </div>')
                }
            } else {
                showNoticeModal('评论失败')
            }
        },
        error: function () {
            showNoticeModal('评论服务出错，请重试')
        }
    })
}

//进入随说详细页面，传入参数为更多信息图片的标签，用来获取随说id
function moreInfoOfThisCasualWord(thisLabel) {
    let casualWordId = $(thisLabel).parent().children(':first-child').val()
    window.location.href = '/casualWord/detail/' + casualWordId
}