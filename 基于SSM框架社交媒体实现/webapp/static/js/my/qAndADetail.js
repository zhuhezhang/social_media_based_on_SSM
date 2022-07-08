//显示确认删除问题的modal
function showDeleteModal() {
    $('#confirmDeleteModal div div .modal-body').html('确认删除该问题吗？')
    $('#confirmDelete').attr('onclick', 'deleteQuestion()')
    $('#confirmDeleteModal').modal('show')
}

//删除问题
function deleteQuestion() {
    let questionId = $('#deleteQuestionBtn').val()//问题id
    $.ajax({
        type: 'delete',
        url: '/qAndA/question',
        data: JSON.stringify({questionId: questionId}),
        contentType: "application/json;charset=utf-8",
        success: function () {
            window.location.href = document.referrer//返回上一页并刷新
        },
        error: function () {
            showNoticeModal('问题删除服务出错，请重试')
        }
    })
}