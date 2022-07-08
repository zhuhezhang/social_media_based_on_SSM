//显示确认删除随说的modal
function showDeleteModal() {
    $('#confirmDeleteModal div div .modal-body').html('确认删除该随说吗？')
    $('#confirmDelete').attr('onclick', 'deleteCasualWord()')
    $('#confirmDeleteModal').modal('show')
}

//删除随说
function deleteCasualWord() {
    let casualWordId = $('#deleteCasualWordBtn').val()//随说id
    $.ajax({
        type: 'delete',
        url: '/casualWord',
        data: JSON.stringify({casualWordId: casualWordId}),
        contentType: "application/json;charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data.status === 200) {
                window.location.href = document.referrer//返回上一页并刷新
            } else {
                showNoticeModal('随说删除失败，请重试')
            }
        },
        error: function () {
            showNoticeModal('删除随说服务出错，请重试')
        }
    })
}