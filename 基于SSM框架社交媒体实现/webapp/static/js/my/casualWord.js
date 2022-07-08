//显示选择的图片或视频
function showImgOrVideo() {
    let imgOrVideoInput = $('#getImgOrVideo')
    let file = imgOrVideoInput[0].files[0]
    if (imgOrVideoInput[0].files && file) {//判断是否选择了文件
        if (file.size > 20000000) {//size单位：字节
            showNoticeModal('文件需要小于20MB')
            imgOrVideoInput.val('')
            return
        }
        let fileType = file.type, fileReader = new FileReader(), selector
        let imgSelector = $('.showImgOrVideo img'), videoSelector = $('.showImgOrVideo video')
        if (fileType === 'image/jpeg' || fileType === 'image/png') {//选择图片
            selector = imgSelector
            selector.removeClass('d-none')//使图片可见
            videoSelector.addClass('d-none')//使视频不可见
            videoSelector.val('')//清除所选的视频
            videoSelector.attr('src', '')
        } else if (fileType === 'video/mp4') {//选择视频
            selector = videoSelector
            selector.removeClass('d-none')
            imgSelector.addClass('d-none')
            imgSelector.val('')
            imgSelector.attr('src', '')
        } else {
            showNoticeModal('只能选择.png/.jpg/.mp4类型的文件')
            imgOrVideoInput.val('')
            return
        }

        fileReader.onload = function (e) {
            selector.attr('src', e.target.result)
        }
        fileReader.readAsDataURL(file)
    }
}

//清空发布随说的文字和图片
function clearPublishContent() {
    let imgAnVideo = $('.showImgOrVideo img,.showImgOrVideo video')
    imgAnVideo.val('')
    imgAnVideo.attr('src', '')
    imgAnVideo.addClass('d-none')
    imgAnVideo.addClass('d-none')
    $('#getImgOrVideo').val(null)
    $('#publishTextContent').val('')
}

//发布随说
function publishCasualWord() {
    let textArea = $('#publishTextContent')
    let textContent = textArea.val()//随说文字内容
    let imgOrVideoInput = $('#getImgOrVideo')//随说图片或视频文件标签
    if (textContent === '' && imgOrVideoInput.val() === '') {
        showNoticeModal('随说内容为空', textArea)
        return
    }
    if (textContent.length > 5000) {
        showNoticeModal('随说内容长度应在5000字符以内', textArea)
        return
    }

    let formData = new FormData();
    formData.append('imgOrVideo', imgOrVideoInput.get(0).files[0])
    formData.append('textContent', textContent)
    $.ajax({
        type: 'post',
        url: '/casualWord',
        data: formData,
        contentType: false,//上传文件要将两者设置成false
        processData: false,
        dataType: 'json',
        success: function (data) {
            let imgOrVideoSrc = data['src']
            if (data['state'] === true) {//发布成功，随说主界显示发布的随说并清空发布随说页面的内容
                $('.casualWord .contents').prepend('' +
                    '                <div class="aCasualWord">\n' +
                    '                    <div class="head d-flex align-items-center">\n' +
                    '                        <img src="' + $('header .dropdown a img').prop('src') + '" ' +
                    '                            onclick="window.location.href = \'/myProfile\'" title="点击查看Ta的资料" ' +
                    '                            class="rounded-circle" alt="" width="35" height="35">\n' +
                    '                        <span title="点击查看Ta的资料" ' + 'onclick="window.location.href = \'/myProfile\'">' + data['name'] + '</span>\n' +
                    '                        <span class="time" style="margin-left: auto">' + formatTimeStamp(data['time']) + '</span>\n' +
                    '                    </div>\n' +
                    '                    <div class="mainContent">\n' +
                    '                        <span class="d-flex">' + $('#publishTextContent').val() + '</span>\n' +
                    '                    </div>\n' +
                    '                    <div class="footer d-flex justify-content-end">\n' +
                    '                        <input type="hidden" value="' + data['casualWordId'] + '">\n' +
                    '                        <img onclick="unlikeOrLike(this)" title="点赞" src="../../static/img/unlike.svg" alt="" height="20" width="20">\n' +
                    '                        <img onclick="showCommentModal(this)" src="../../static/img/comment.svg" title="评论" alt=""\n' +
                    '                             height="20" width="20">\n' +
                    '                        <img onclick="moreInfoOfThisCasualWord(this)" src="../../static/img/loadMore.svg" title="查看详情"\n' +
                    '                             alt="" height="20" width="20">\n' +
                    '                    </div>\n' +
                    '                </div>'
                )

                let insertLoc = $('.casualWord .contents .aCasualWord:first .mainContent span')//图片/视频插入位置
                if (imgOrVideoSrc !== undefined) {//设置显示的图/视频
                    if (imgOrVideoSrc.substring(imgOrVideoSrc.lastIndexOf('.')) === '.mp4') {
                        insertLoc.after('<video controls src="' + imgOrVideoSrc + '" type="video/mp4"></video>')
                    } else {
                        insertLoc.after('<img src="' + imgOrVideoSrc + '" alt="">')
                    }
                }
                clearPublishContent()//清除输入、选择的文字/图片/视频
            } else {
                showNoticeModal('随说发布失败')
            }
        }, error: function () {
            showNoticeModal('随说发布服务出错，请重试')
        }
    })
}