//清空问题内容
function clearQuestionContent() {
    $('#questionTextContent').val('')
}

//发布问题
function publishQuestion() {
    let textArea = $('#questionTextContent')
    let content = textArea.val()//问题内容
    if (content === '') {
        showNoticeModal('问题为空', textArea)
        return
    }
    if (content.length > 5000) {
        showNoticeModal('问题长度应在5000字符以内', textArea)
        return
    }

    $.ajax({
        type: 'post',
        url: '/qAndA/question',
        data: JSON.stringify({content}),
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        success: function (data) {
            $('.qAndA .contents').prepend('' +
                '                <div class="aQAndA">\n' +
                '                    <div class="head d-flex align-items-center">\n' +
                '                        <img src="' + $('header .dropdown a img').prop('src') + '" ' +
                '                            onclick="window.location.href = \'/myProfile\'" title="点击查看Ta的资料" ' +
                '                            class="rounded-circle" alt="" width="35" height="35">\n' +
                '                        <span title="点击查看Ta的资料" ' + 'onclick="window.location.href = \'/myProfile\'">' + data['name'] + '</span>\n' +
                '                        <span class="time" style="margin-left: auto">' + formatTimeStamp(data['time']) + '</span>\n' +
                '                    </div>\n' +
                '                    <div class="mainContent">\n' +
                '                        <span class="d-flex">' + content + '</span>\n' +
                '                    </div>\n' +
                '                    <div class="footer d-flex justify-content-end">\n' +
                '                        <input type="hidden" value="' + data['questionId'] + '">\n' +
                '                        <input type="hidden" value="0">\n' +
                '                        <img onclick="showAnswerModal(this)" src="../../static/img/answer.svg" title="回答" alt=""\n' +
                '                             height="20" width="20">\n' +
                '                        <img onclick="moreInfoOfThisQuestion(this)" src="../../static/img/loadMore.svg" title="查看详情"\n' +
                '                             alt="" height="20" width="20">\n' +
                '                    </div>\n' +
                '                </div>')
            clearQuestionContent()//清除输入的文字
        },
        error: function () {
            showNoticeModal('问题发布服务出错，请重试')
        }
    })
}

//搜索问题
function searchQuestion() {
    let textArea = $('#questionTextContent')
    let textContent = textArea.val()//问题内容
    if (textContent.length === 0) {
        showNoticeModal('内容不能为空', textArea)
        return
    }
    if (textContent.length > 5000) {
        showNoticeModal('内容长度应在5000字符以内', textArea)
        return
    }

    $.ajax({
        type: 'get',
        url: '/qAndA/search/' + textContent,
        dataType: 'json',
        success: function (data) {
            $('.aQAndA').remove()
            $('#pagination').children().remove()
            for (let question of data['questions']) {//遍历输出问题
                $('.qAndA .contents').append('' +
                    '                <div class="aQAndA">\n' +
                    '                    <div class="head d-flex align-items-center">\n' +
                    '                        <img src="' + question['questionerHeadPortrait'] + '" ' +
                    '                            onclick="window.location.href = \'/userFriendProfile/' + question['questionerId'] + '\'' + '" title="点击查看Ta的资料" ' +
                    '                            class="rounded-circle" alt="" width="35" height="35">\n' +
                    '                        <span title="点击查看Ta的资料" ' + 'onclick="window.location.href = \'/userFriendProfile/' + question['questionerId'] + '\'' + '">' + question['name'] + '</span>\n' +
                    '                        <span class="time" style="margin-left: auto">' + formatTimeStamp(question['time']) + '</span>\n' +
                    '                    </div>\n' +
                    '                    <div class="mainContent">\n' +
                    '                        <span class="d-flex">' + question['content'] + '</span>\n' +
                    '                    </div>\n' +
                    '                    <div class="footer d-flex justify-content-end">\n' +
                    '                        <input type="hidden" value="' + question['questionId'] + '">\n' +
                    '                        <input type="hidden" value="0">\n' +
                    '                        <img onclick="showAnswerModal(this)" src="../../static/img/answer.svg" title="回答" alt=""\n' +
                    '                             height="20" width="20">\n' +
                    '                        <img onclick="moreInfoOfThisQuestion(this)" src="../../static/img/loadMore.svg" title="查看详情"\n' +
                    '                             alt="" height="20" width="20">\n' +
                    '                    </div>\n' +
                    '                </div>')
            }
        },
        error: function () {
            showNoticeModal('搜索问题服务出错，请重试')
        }
    })
}