#创建数据库
drop database if exists lescha;
create database `lescha` character set utf8 collate utf8_general_ci;
use lescha;

#用户表
drop table if exists user;
create table user
(
    id            bigint auto_increment primary key comment '用户账号/表id',
    username      nvarchar(11) unique  not null comment '用户名',
    is_active     bit default 0        not null comment '是否在线，0下线，1在线',
    state         bit default 0        not null comment '是否已激活，0未激活，1已激活',
    email         varchar(30) unique   not null comment '邮箱',
    password      varchar(32)          not null comment '密码',
    phone_number  varchar(11) unique   null comment '手机号码',
    sex           bit                  null comment '性别，0女，1男',
    head_portrait nvarchar(255) unique null comment '头像名称，存放在目录/webapp/userData/headPortrait下，默认系统头像（/static/img/login.svg）',
    register_time datetime             not null comment '注册时间',
    birthday      date                 null comment '生日',
    hometown      nvarchar(20)         null comment '家乡',
    introduce     nvarchar(150)        null comment '个人简介'
) engine = InnoDB
  default charset = utf8;

#用户确认注册激活码和修改密码表
drop table if exists confirm_register_and_forget_pwd;
create table confirm_register_and_forget_pwd
(
    id           bigint auto_increment primary key comment '表id',
    user_id      bigint          not null comment '用户账号',
    state        bit             not null comment '是否有效，0无效，1有效',
    flag         bit             not null comment '注册还是修改密码，0注册，1修改密码',
    code         char(32) unique not null comment '注册激活码/修改密码确认码',
    new_password char(32)        null comment '新密码'
) engine = InnoDB
  default charset = utf8;

#好友分组表
# drop table if exists friend_group;
# create table friend_group
# (
#     id     bigint auto_increment primary key comment '表id',
#     name   nvarchar(10) not null comment '分组名字',
#     userId bigint       not null comment '该分组所属的用户id'
# ) engine = InnoDB
#   default charset = utf8;

#用户好友表
drop table if exists user_friend;
create table user_friend
(
    id              bigint auto_increment primary key comment '表id',
    friend_id       bigint       not null comment '好友id',
    user_id         bigint       not null comment '该表所属的用户id',
    friend_nickname nvarchar(11) not null comment '备注好友昵称，默认为好友的账户名',
    unique key friend_user_id (friend_id, user_id)
) engine = InnoDB
  default charset = utf8;

#系统消息表
drop table if exists system_message;
create table system_message
(
    id             bigint auto_increment primary key comment '表id',
    send_time      datetime      not null comment '消息发送时间',
    sender_id      bigint        not null comment '发送者id',
    receiver_id    bigint        not null comment '接收者id',
    content        nvarchar(100) null comment '消息内容',
    message_type   tinyint       not null comment '消息类型，0请求添加好友;1同意添加好友;2拒绝添加好友;3删除好友;4点赞随说;5取消点赞随说;6评论本人的随说;7回复本人的评论;8回复本人的问题;9回复本人的回答',
    casual_word_id bigint        null comment '消息相关的随说id',
    question_id    bigint        null comment '消息相关的问题id',
    is_effective   bit default 1 not null comment '是否生效，1生效，0失效，默认生效'
) engine = InnoDB
  default charset = utf8;

#随说表
drop table if exists casual_word;
create table casual_word
(
    id               bigint auto_increment primary key comment '表id',
    user_id          bigint             not null comment '发布者id',
    publish_time     datetime           not null comment '随说发布时间',
    content          nvarchar(5000)     null comment '文字内容，该字段和图/视频字段至少有一个不为空',
    picture_or_video varchar(40) unique null comment '图/视频文件名'
) engine = InnoDB
  default charset = utf8;

#随说点赞表
drop table if exists casual_word_like;
create table casual_word_like
(
    id             bigint auto_increment primary key comment '表id',
    casual_word_id bigint   not null comment '随说id',
    like_user_id   bigint   not null comment '点赞者id',
    like_time      datetime not null comment '点赞随说时间'
) engine = InnoDB
  default charset = utf8;

#随说评论表
drop table if exists casual_word_comment;
create table casual_word_comment
(
    id               bigint auto_increment primary key comment '表id',
    casual_word_id   bigint        not null comment '随说id',
    commenter_id     bigint        not null comment '评论者id',
    comment_time     datetime      not null comment '评论时间',
    content          nvarchar(100) not null comment '评论内容',
    reply_comment_id bigint        null comment '回复的评论id'
) engine = InnoDB
  default charset = utf8;

#用户聊天列表表
drop table if exists user_chat_list;
create table user_chat_list
(
    id          bigint auto_increment primary key comment '表id',
    create_time datetime(3) not null comment '创建时间，3表示保留3位毫秒数，6则是微秒',
    user_id     bigint      not null comment '所属用户账号',
    friend_id   bigint      not null comment '用户好友的账号'
) engine = InnoDB
  default charset = utf8;

#用户聊天消息表
drop table if exists user_chat_message;
create table user_chat_message
(
    id            bigint auto_increment primary key comment '表id',
    content       nvarchar(1000) not null comment '消息内容',
    receive_state bit default 0  not null comment '接收状态，0未接收，1接收',
    send_time     datetime(3)    not null comment '消息发送时间，3表示保留3位毫秒数，6则是微秒',
    sender_id     bigint         not null comment '发送者账号',
    receiver_id   bigint         not null comment '接收者账号'
) engine = InnoDB
  default charset = utf8;

#问答-问题表
drop table if exists q_and_a_question;
create table q_and_a_question
(
    id            bigint auto_increment primary key comment '表id',
    questioner_id bigint         not null comment '提问者id',
    question_time datetime       not null comment '提问时间',
    content       nvarchar(5000) not null comment '提问内容'
) engine = InnoDB
  default charset = utf8;

#问答-回答表
drop table if exists q_and_a_answer;
create table q_and_a_answer
(
    id              bigint auto_increment primary key comment '表id',
    question_id     bigint        not null comment '问答-问题id',
    responder_id    bigint        not null comment '回复者id',
    response_time   datetime      not null comment '回答时间',
    content         nvarchar(100) not null comment '回复内容',
    reply_answer_id bigint        null comment '回复的问题-回答id'
) engine = InnoDB
  default charset = utf8;

#群聊表
# drop table if exists group_chat;
# create table group_chat
# (
#     id            bigint auto_increment primary key comment '表id',
#     name          nvarchar(10)  not null comment '群聊名称',
#     root_id       bigint        not null comment '群主id，指向用户表',
#     head_portrait varchar(255)  not null comment '头像，默认系统头像',
#     introduce     nvarchar(150) null comment '群聊简介',
#     create_time   datetime      not null comment '群聊创建时间'
# ) engine = InnoDB
#   default charset = utf8;

#群聊用户表
# drop table if exists user_belongs_group_chat;
# create table user_belongs_group_chat
# (
#     id        bigint auto_increment primary key comment '表id',
#     user_id   bigint       not null comment '用户id',
#     group_id  bigint       not null comment '用户所属的群聊id',
#     user_name nvarchar(10) not null comment '用户群内昵称，默认为用户昵称',
#     join_time datetime     not null comment '加入群聊时间'
# ) engine = InnoDB
#   default charset = utf8;

#群聊消息表
# drop table if exists group_chat_message;
# create table group_chat_message
# (
#     id           bigint auto_increment primary key comment '表id',
#     sender_id    bigint         not null comment '发送者id',
#     group_id     bigint         not null comment '消息所属的群聊id',
#     send_time    datetime       not null comment '发送时间',
#     content      nvarchar(1000) not null comment '消息内容',
#     content_type binary         not null comment '消息类型，0文本；1图片；2分享好友链接'
# ) engine = InnoDB
#   default charset = utf8;

#用户群聊消息关联表
# drop table if exists user_group_chat_message;
# create table user_group_chat_message
# (
#     id             bigint auto_increment primary key comment '表id',
#     sender_id      bigint        not null comment '发送者id',
#     group_id       bigint        not null comment '消息所属的群聊id',
#     receive_status bit default 0 not null comment '接收状态，0未接收，1接收',
#     receiver_id    bigint        not null comment '接收者id，关联用户表',
#     send_time      datetime      not null comment '消息发送时间'
# ) engine = InnoDB
#   default charset = utf8;

# alter table user
#     auto_increment = 1;#修改表自增字段值
# show table status;#查询当前数据库表信息
# show full processlist;#查看数据库已有的连接（这里用来验证druid连接池是否生效）