let chatData//聊天的全部信息
let currentChatUserId//当前聊天用户账号id

$(function () {
    let chatDataInput = $('#chatData')
    chatData = JSON.parse(chatDataInput.val())
    chatDataInput.remove()
    if (chatData['status'] === 200) {
        delete chatData['status']
        let lastMsgs = []//存放信息用户id、用户最新的消息时间time
        for (let id of Object.keys(chatData)) {
            let obj = {}
            obj['id'] = id
            obj['time'] = chatData[id]['lastTime']
            lastMsgs.push(obj)
        }
        lastMsgs.sort(function (x, y) {//根据时间降序排序
            return y['time'] - x['time']
        })
        for (let item of lastMsgs) {//用户界面显示聊天用户列表
            comAddUserToChatUserList(chatData, item['id'], false)
        }
        setInterval(getNotReceiveChatContent, 3000)//每3秒获取一次未接收的消息
        $('#contentsList').scroll(loadMoreChatContents)//设置监听事件，消息列表滑动到顶部加载与当前聊天用户的更多消息
        $(document).keydown(function (event) {
            if (event.keyCode === 13) {//回车发送消息
                sendChatMessage()
            }
        })
    } else {
        showNoticeModal('聊天消息获取出错，请退出重试')
    }
})

//获取未接收的聊天消息
function getNotReceiveChatContent() {
    $.ajax({
            type: 'get',
            url: '/notReceiveChatMessage',
            dataType: "json",
            success: function (data) {//data里各个用户的消息列表messages按照时间升序排列
                if (data['status'] === 200) {
                    delete data['status']
                    let lastMsgs = []//存放信息用户id、用户最新的消息时间time，用来排序
                    for (let id of Object.keys(data)) {
                        let obj = {}
                        obj['id'] = id
                        obj['time'] = data[id]['lastTime']
                        lastMsgs.push(obj)
                    }
                    lastMsgs.sort(function (x, y) {//根据时间升序排序
                        return x['time'] - y['time']
                    })

                    for (let el of lastMsgs) {//保存消息到chatData、输出显示聊天信息
                        let id = el['id']
                        //获取的信息添加到聊天全部信息chatData
                        if (chatData.hasOwnProperty(id)) {//当前聊天用户列表存在该用户
                            let user = $('input[value=' + id + ']').parent()
                            user.children('.time').text(formatTimeStamp(el['time']))
                            user.children('.preview').text(data[id]['messages'][data[id]['messages'].length - 1]['content'])
                            user.remove()
                            $('.chatUserList').prepend(user)//使当前用户放到聊天用户列表最上方
                            chatData[id]['lastTime'] = data[id]['lastTime']
                            for (let message of data[id]['messages']) {
                                chatData[id]['messages'].push(message)
                            }
                        } else {//不存在
                            chatData[id] = data[id]
                            comAddUserToChatUserList(data, id, true)
                        }
                        if (id === currentChatUserId) {//判断当前聊天用户，是则往聊天界面输出聊天信息
                            for (let msg of data[id]['messages']) {
                                $('#contentsList').append('<div class="bubble friendBubble"><span>' + msg['content'] + '</span></div>')
                            }
                        }
                    }
                } else {
                    showNoticeModal('获取聊天消息出错，请退出重试')
                }
            },
            error: function () {
                showNoticeModal('获取聊天内容出错，请刷新重试')
            }
        }
    )
}

//点击加载用户聊天信息
function loadChatContents(clickedItem) {
    $('.chatUserList div.active').removeClass('active')
    $(clickedItem).addClass('active')
    let userId = $(clickedItem).children(':first-child').val()
    $('#currentChatUser span').html(chatData[userId]['name'])
    $('#currentChatUser button').removeClass('d-none')
    currentChatUserId = userId
    let contentsList = $('#contentsList')
    contentsList.empty()//页面清除之前的聊天信息

    for (let msg of chatData[userId]['messages']) {
        if (msg['flag'] === false) {
            contentsList.append('<div class="bubble friendBubble"><span>' + msg['content'] + '</span></div>')
        } else {
            contentsList.append('<div class="bubble myBubble"><span>' + msg['content'] + '</span></div>')
        }
    }

    contentsList.scrollTop(contentsList.prop('scrollHeight'))//使滑动条滑到底部
}

//将当前聊天用户移除出聊天列表
function removeUserFromChatList() {
    $.ajax({
        type: 'delete',
        url: '/chatUserList',
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({currentChatUserId}),
        dataType: 'json',
        success: function (data) {
            if (data['status'] === 200) {
                $('.chatUserList div.active').remove()
                delete chatData[currentChatUserId]
                currentChatUserId = null
                $('#currentChatUser span').html('')
                $('#currentChatUser button').addClass('d-none')
                $('#contentsList').empty()
            } else {
                showNoticeModal('移出聊天列表失败，请重试')
            }
        },
        error: function () {
            showNoticeModal('移出聊天列表出错，请刷新重试')
        }
    })
}

//聊天消息滚动条到顶部加载更多聊天信息
function loadMoreChatContents() {
    let contentsList = $('#contentsList')
    if (contentsList.scrollTop() === 0) {
        $.ajax({
            type: 'get',
            url: '/moreChatContent/' + currentChatUserId + '/' + chatData[currentChatUserId]['firstTime'],
            dataType: 'json',
            success: function (data) {
                if (data['hasMsg']) {//有消息
                    for (let message of data['messages']) {//消息是按照时间降序排列的
                        chatData[currentChatUserId]['messages'].unshift(message)
                    }
                    chatData[currentChatUserId]['firstTime'] = data['firstTime']
                    for (let msg of data['messages']) {
                        if (msg['flag'] === false) {
                            contentsList.prepend('<div class="bubble friendBubble"><span>' + msg['content'] + '</span></div>')
                        } else {
                            contentsList.prepend('<div class="bubble myBubble"><span>' + msg['content'] + '</span></div>')
                        }
                    }
                }
            },
            error: function () {
                showNoticeModal('消息加载失败，请重试')
            }
        })
    }
}

//处理发送聊天信息
function sendChatMessage() {
    if (currentChatUserId === null || currentChatUserId === undefined) {
        showNoticeModal('请选择要聊天的好友')
        return
    }
    let msg = $('#content').val()
    if (msg.length === 0) {
        showNoticeModal('不能发送空消息')
        return
    } else if (msg.length > 1000) {
        showNoticeModal('消息字数不能超过1000')
        return
    }
    $.ajax({
        type: 'post',
        url: '/chat',
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({currentChatUserId, msg}),
        dataType: 'json',
        success: function (data) {
            if (data['status'] === 200) {
                $('#content').val('')
                let currentUser = $('.chatUserList div.active')
                currentUser.children('.time').text(formatTimeStamp(data['lastTime']))
                currentUser.children('.preview').text(msg)
                currentUser.remove()
                $('.chatUserList').prepend(currentUser)//使当前用户放到聊天用户列表最上方
                chatData[currentChatUserId]['lastTime'] = data['lastTime']
                chatData[currentChatUserId]['messages'].push({"content": msg, "flag": true})
                let contentsList = $('#contentsList')
                contentsList.append('<div class="bubble myBubble"><span>' + msg + '</span></div>')
                contentsList.scrollTop(contentsList.prop('scrollHeight'))//使滑动条滑到底部
            } else if (data['state'] === false) {
                showNoticeModal('您已被TA移出好友列表，发送失败')
            } else {
                showNoticeModal('消息发送失败，请重试')
            }
        },
        error: function () {
            showNoticeModal('消息发送出错，请重试')
        }
    })
}

//公共添加用户到左侧当前聊天用户列表，data-用户聊天信息；id-用户账号；flag-false(添加到下面)，true(添加到上面)
function comAddUserToChatUserList(data, id, flag) {
    let content
    if (data[id]['messages'].length === 0) {
        content = ''
    } else {
        content = data[id]['messages'][data[id]['messages'].length - 1]['content']
    }
    let appendData = '<div onclick="loadChatContents(this)" class="chatUser position-relative align-items-center">\n' +
        '                    <input value="' + id + '" type="hidden">\n' +
        '                    <img src="' + data[id]['hpName'] + '" class="rounded-circle" alt="">\n' +
        '                    <span class="name">' + data[id]['name'] + '</span>\n' +
        '                    <span class="time">' + formatTimeStamp(data[id]['lastTime']) + '</span>\n' +
        '                    <span class="preview">' + content + '</span>\n' +
        '                </div>'
    if (flag === true) {
        $('.message .chatUserList').prepend(appendData)
    } else {
        $('.message .chatUserList').append(appendData)
    }
}