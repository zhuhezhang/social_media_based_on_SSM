$(function () {
    //发布评论/回答模态框关闭时清空提示的错误信息、原来的评论/答复内容
    let modal = $("#publishCommentOrAnswerModal")
    modal.on('hidden.bs.modal', function () {
        $('#publishCommentOrAnswerModal div div .modal-footer span').html('')
        $('#publishCommentOrAnswerModal div div .modal-body div input').val('')
    })
})

//设置显示评论错误信息
function showErrorInfoOfComment(info) {
    $('#publishCommentOrAnswerModal div div .modal-footer span').html(info)
}