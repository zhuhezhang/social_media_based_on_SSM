let originHeadPortrait//后台原始的数据
let originUsername
let originEmail
let originPhoneNumber
let originSex
let originBirthday
let originHometown
let originIntroduce

//页面加载完成后
$(function () {
    //获取原先的用户资料，避免重复上传
    originHeadPortrait = $('#headPortrait').attr('src')
    originUsername = $('#username').val()
    originEmail = $('#email').val()
    originPhoneNumber = $('#phoneNumber').val()
    let tmp = $('input[name=sex]:checked').val()
    if (tmp === undefined) {
        originSex = undefined
    } else originSex = tmp === 'true'
    tmp = $('#birthday')
    originBirthday = tmp.val()
    if (originBirthday === '') {
        tmp.attr('value', '9999-12-31')
    }
    originHometown = $('#hometown').val()
    originIntroduce = $('#introduce').val()
})

//修改按钮点击：使指定按钮可用/不可用、修改img标题
function modifyBtnClick() {
    $('input').removeAttr('disabled')
    $('#cancelBtn').removeAttr('disabled')
    $('#submitBtn').removeAttr('disabled')
    $('#modifyBtn').attr('disabled', true)
    $('form div img').attr('title', '点击选择新的头像')
}

//检查并展示头像
function showHp() {
    let headPortraitInput = $('#selectHp')
    let file = headPortraitInput[0].files[0]
    if (headPortraitInput[0].files && file) {
        if (file.size > 5000000) {//size单位：字节
            showNoticeModal('文件需要小于5MB')
            headPortraitInput.val('')
            return
        }
        if (file.type !== 'image/jpeg' && file.type !== 'image/png') {
            showNoticeModal('只能选择.png或.jpg类型的图片')
            headPortraitInput.val('')
            return
        }
        let fileReader = new FileReader()
        fileReader.onload = function (e) {
            $("#headPortrait").attr("src", e.target.result)
        }
        fileReader.readAsDataURL(file)
    }
}

//取消按钮点击
function cancelBtnClick() {
    //使指定按钮可用/不可用、修改img标题
    $('input').attr('disabled', true)
    $('#cancelBtn').attr('disabled', true)
    $('#submitBtn').attr('disabled', true)
    $('#modifyBtn').attr('disabled', false)
    $('form div img').attr('title', '请点击修改按钮后再来点击选择新的头像');

    //清除所选的图片
    $('#selectHp').val(null)

    //相关参考链接 https://blog.csdn.net/qq_43794633/article/details/123294565
    //设置各个input的页面值为后台初始值
    $('#headPortrait').prop('src', originHeadPortrait)
    $('#username').val(originUsername)
    $('#email').val(originEmail);
    $('#phoneNumber').val(originPhoneNumber)
    if (originSex === true) {
        $('#man').prop('checked', true)
    } else if (originSex === false) {
        $('#women').prop('checked', true)
    } else {
        $('input[name=sex]:checked').prop('checked', false)
    }
    let b = $('#birthday')
    if (originBirthday === '') b.val('9999-12-31')
    else b.val(originBirthday)
    $('#hometown').val(originHometown)
    $('#introduce').val(originIntroduce)
}

//处理、提交数据
function submitBtnClick() {
    //获取各个input
    let usernameInput = $('#username')
    let emailInput = $('#email')
    let phoneNumberInput = $('#phoneNumber')
    let hometownInput = $('#hometown')
    let introduceInput = $('#introduce')
    let username = usernameInput.val()
    let email = emailInput.val()
    let phoneNumber = phoneNumberInput.val()
    let sex = $('input[name=sex]:checked').val()//undefined,false女，true男
    if (sex === 'true') sex = true
    else if (sex === 'false') sex = false
    let birthday = $('#birthday').val()
    birthday = birthday === '9999-12-31' ? '' : birthday
    let hometown = hometownInput.val()
    let introduce = introduceInput.val()

    //验证各个输入的格式是否符合规范
    if (username.length < 1 || username.length > 11) {
        showNoticeModal('用户名长度请保持在1~11内', usernameInput)
        return
    }
    let reg = /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/
    if (!reg.test(email)) {
        showNoticeModal('请输入正确的邮箱', emailInput)
        return
    }
    reg = /(^\d{11}$)|(^$)/
    if (!reg.test(phoneNumber)) {
        showNoticeModal('请输入十一位数的手机号或者省略不写', phoneNumberInput)
        return
    }
    if (hometown.length > 20) {
        showNoticeModal('家乡字数请保持在20以内', hometownInput)
        return
    }
    if (introduce.length > 150) {
        showNoticeModal('简介字数请保持在150以内', introduceInput)
        return
    }

    //处理传给后台的数据（除了头像）
    let dataObject = {}
    if (username !== originUsername) {
        dataObject["username"] = username
    }
    if (email !== originEmail) {
        dataObject["email"] = email
    }
    if (phoneNumber !== originPhoneNumber) {
        dataObject["phoneNumber"] = phoneNumber
    }
    if (sex !== originSex && sex !== undefined) {
        dataObject["sex"] = sex
    }
    if (birthday !== originBirthday) {
        dataObject["birthday"] = birthday
    }
    if (hometown !== originHometown) {
        dataObject["hometown"] = hometown
    }
    if (introduce !== originIntroduce) {
        dataObject["introduce"] = introduce
    }

    //没有修改的数据则不上传
    let fileInput = $('#selectHp')
    if (fileInput.val() === '' && Object.keys(dataObject).length === 0) {
        showNoticeModal('没有信息被修改')
        return
    }

    //上传数据
    let formData = new FormData();
    formData.append('headPortrait', fileInput.get(0).files[0])
    formData.append('profile', JSON.stringify(dataObject))
    $.ajax({
            type: 'POST',
            url: '/modifyProfile',
            data: formData,
            contentType: false,
            processData: false,
            dataType: 'json',
            success: function (data) {
                switch (data["state"]) {
                    case 1:
                        showNoticeModal('资料修改成功')
                        if (data["hpPath"] !== undefined) {
                            originHeadPortrait = data["hpPath"]
                            $('header div a img').attr('src', originHeadPortrait)//头像修改也要记得刷新头部的头像
                        }
                        if (dataObject["username"] !== undefined) {//根据提交的数据返回进入修改页面的初始状态
                            originUsername = username
                        }
                        if (dataObject["email"] !== undefined) {
                            originEmail = email
                        }
                        if (dataObject["phoneNumber"] !== undefined) {
                            originPhoneNumber = phoneNumber
                        }
                        if (dataObject["sex"] !== undefined) {
                            originSex = sex
                        }
                        if (dataObject["birthday"] !== undefined) {
                            originBirthday = birthday
                        }
                        if (dataObject["hometown"] !== undefined) {
                            originHometown = hometown
                        }
                        if (dataObject["introduce"] !== undefined) {
                            originIntroduce = introduce
                        }
                        cancelBtnClick()
                        break
                    case 2:
                        showNoticeModal('用户名已被占用', usernameInput)
                        break
                    case 3:
                        showNoticeModal('邮箱已被占用', emailInput)
                        break
                    case 4:
                        showNoticeModal('手机号已被占用', phoneNumberInput)
                        break
                    case 5:
                        showNoticeModal('用户名和邮箱已被占用', usernameInput)
                        break
                    case 6:
                        showNoticeModal('用户名和手机号已被占用', usernameInput)
                        break
                    case 7:
                        showNoticeModal('邮箱和手机号已被占用', emailInput)
                        break
                    case 9:
                        showNoticeModal('用户名、邮箱和手机号均已被占用', usernameInput)
                        break
                    default:
                        showNoticeModal('资料修改出错，请重试')
                }
            },
            error: function () {
                showNoticeModal('资料修改失败，请重试')
            }
        }
    )
}