//设置通知模态框modal的内容并显示、关闭时使指定input获得焦点
function showNoticeModal(html, input) {
    let modal = $("#noticeModal")
    if (input !== undefined) {
        modal.off('hidden.bs.modal', function () {//防止多次绑定
            input.focus()
        })
        modal.on('hidden.bs.modal', function () {
            input.focus()
        })
    }
    $("#noticeModal div div .modal-body").html(html)
    modal.modal('show')
}