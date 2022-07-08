//问答通用js

//显示回答模态框，传入参数为调用为此函数的标签，用来获取问题id、回答id、标志位
function showAnswerModal(thisLabel) {
    $('#commentOrAnswerContent').attr('placeholder', '回答内容')
    $('[for="commentOrAnswerContent"]').text('回答内容')
    $('#confirmPublishCommentOrAnswer').attr('onclick', 'answerQuestion(this)')
    let id = $(thisLabel).parent().children(':first-child').val()//问题/回答id
    let flag = $(thisLabel).parent().children(':nth-child(2)').val()//0-问题页面回答；1-问答详细页面回答；2-问答详细页面回复别人的答案
    $('.modal-footer #flag').val(flag)
    $('.modal-footer #confirmPublishCommentOrAnswer').val(id)
    $('#publishCommentOrAnswerModal').modal('show')
}

//回答问题
function answerQuestion(btn) {
    let id = $(btn).val()//回复的问题id/回复的答案id
    let content = $('#commentOrAnswerContent').val()//内容
    if (content.length === 0) {
        showErrorInfoOfComment('回答不能为空')
        return
    }
    if (content.length > 100) {
        showErrorInfoOfComment('回答字符个数应在100以内')
        return
    }
    let flag = Number($('.modal-footer #flag').val())
    $.ajax({
        type: 'post',
        url: '/qAndA/answer',
        data: JSON.stringify({content, id, flag}),
        contentType: "application/json;charset=utf-8",
        dataType: 'json',
        success: function (data) {
            $('#publishCommentOrAnswerModal').modal('hide')
            if (flag === 0) {
                showNoticeModal('回答成功')
            } else if (flag === 1) {//问答详细界面回答问题
                $('.qAndAAnswer').prepend('' +
                    '                <div class="aQAndAAnswer">\n' +
                    '                    <div class="d-flex align-items-center">\n' +
                    '                        <input type="hidden" value="' + data['answerId'] + '">\n' +
                    '                        <input type="hidden" value="2">\n' +
                    '                        <img src="' + $('header .dropdown a img').prop('src') + '" class="rounded-circle" height="35"\n' +
                    '                             width="35px" alt="">\n' +
                    '                        <a href="/myProfile">' + data['myName'] + '</a>\n' +
                    '                        <span>：</span>\n' +
                    '                        <span class="time" style="margin-left: auto">' + formatTimeStamp(data['time']) + '</span>\n' +
                    '                        <span onclick="showAnswerModal(this)"\n' +
                    '                              class="responseAnswer">回复</span>\n' +
                    '                    </div>\n' +
                    '                    <span class="d-block">' + content + '</span>\n' +
                    '                </div>')
            } else {//问答详细界面回复回答
                $('.qAndAAnswer').prepend('' +
                    '                <div class="aQAndAAnswer">\n' +
                    '                    <div class="d-flex align-items-center">\n' +
                    '                        <input type="hidden" value="' + data['answerId'] + '">\n' +
                    '                        <input type="hidden" value="2">\n' +
                    '                        <img src="' + $('header .dropdown a img').prop('src') + '" class="rounded-circle" height="35"\n' +
                    '                             width="35px" alt="">\n' +
                    '                        <a href="/myProfile">' + data['myName'] + '</a>\n' +
                    '                        <span>回复</span>\n' +
                    '                        <a href="/userFriendProfile/' + data['respondentId'] + '">' + data['respondentName'] + '</a>\n' +
                    '                        <span>：</span>\n' +
                    '                        <span class="time" style="margin-left: auto">' + formatTimeStamp(data['time']) + '</span>\n' +
                    '                        <span onclick="showAnswerModal(this)" class="responseAnswer">回复</span>\n' +
                    '                    </div>\n' +
                    '                    <span class="d-block">' + content + '</span>\n' +
                    '                </div>')
            }
        },
        error: function () {
            showNoticeModal('解答服务出错，请重试')
        }
    })
}

//进入问答详细页面，传入参数为更多信息图片的标签，用来获取问答id
function moreInfoOfThisQuestion(thisLabel) {
    let qAndAId = $(thisLabel).parent().children(':first-child').val()
    window.location.href = '/qAndA/detail/' + qAndAId
}