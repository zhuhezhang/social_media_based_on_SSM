//随说、问答分页相关

$(function () {
    pagination()
})

//分页相关，随说（问题）每页最多20条，随说评论（回答问题）则为10条
function pagination() {
    let totalPages = Number($('#totalPages').val())//总页数
    let currentPath = $('#currentPath').val()//当前请求路径（不包括页码）
    let currentPage = Number($('#currentPage').val())//当前页码
    if (totalPages > 1) {
        $('#pagination').jqPaginator({
            totalPages: totalPages,
            visiblePages: 10,
            currentPage: currentPage,
            first: '<li class="prev page-item"><a class="page-link" href="javascript:;">首页</a></li>',
            prev: '<li class="prev page-item"><a class="page-link" href="javascript:;">上一页</a></li>',
            next: '<li class="next page-item"><a class="page-link" href="javascript:;">下一页</a></li>',
            last: '<li class="prev page-item"><a class="page-link" href="javascript:;">末页</a></li>',
            page: '<li class="page page-item"><a class="page-link" href="javascript:;">{{page}}</a></li>',
            onPageChange: function (num, type) {
                if (type === 'change') {//请求分页的数据
                    window.location.href = currentPath + num
                }
            }
        })
    }
}