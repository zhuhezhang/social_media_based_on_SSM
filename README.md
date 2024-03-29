﻿==本文由本人的本科毕业论文修改而来，并不是论文的完整篇幅，部分已进行修改或删除。注意，在参考时还请注意查重等其他因素，由此所产生的相关问题均与本人无关，概不负责。==
@[TOC](目录)
# 摘要
社交在人类社会中有着不可或缺的重要意义，不仅仅是线下的社交，线上的社交也尤显得至关重要，特别是目前处于的信息、疫情时代，两者相辅相成。本社交媒体web应用名为lescha，全称为let is chat，意为让我们一起聊，为了方便读便称为此名。系统的后端实现是基于SSM框架（也就是由Spring、SpringMVC以及MyBatis组成），并使用Maven作为项目管理工具，实现了用户访问拦截，同时也利用模板引擎Thymeleaf进行页面渲染，在管理数据库连接方面使用了阿里巴巴Druid数据库连接池管理连接MySQL；而在前端页面的实现利用HTML5、CSS3、JavaScript以及相关的Bootstrap5样式、jQuery库；在前后端数据交互方面利用了jQuery库的ajax方法，并使用JSON作为数据交互的格式，基本遵循了RESTful的API设计风格，充分利用五种HTTP方法。

本系统的定位是简洁，但又不简单的社交媒体，摒弃当前社交软件多余而又繁琐的功能，避免同质化。其功能主要有这几项：1、利用邮箱实现快速注册、忘记密码；2、用户资料的查看、修改；3、管理好友，其中有添加、删除、修改昵称；4、查看系统消息；5、用户发布随说（类似于QQ的说说、微信的朋友圈），文字/图片/视频类型的均可，也可评论随说、回复评论；6、用户发布问题，并且所有注册的用户均可查看、回答；7、对随说、评论、系统消息、问答进行分页请求查看；8、好友聊天。

除以上提到的功能之外，同时在前端和后端均有对用户提交的数据进行验证，保证提交的数据规范性、正确性；遵循MVC模式，使系统可维护性、系统可移植性、代码可重用性提高等非功能性的设计。

__关键词：__ SSM；社交媒体；Ajax；Druid；Thymeleaf

# Abstract
Social has an indispensable importance in human society, not only offline, but also online is especially crucial, especially in the current era of information and epidemic, where the two complement each other. This social media web app is called lescha, and the full name is let is chat, and it is called this for easy reading. The back-end of it is based on SSM framework (Spring + SpringMVC + MyBatis), and use Maven as a project management tool, which implements user access interception, and also utilizes the template engine Thymeleaf for page rendering, and uses Alibaba Druid database connection pool for managing database connections to connect to MySQL; while the front-end page implementation utilizes HTML5, CSS3, JavaScript and related Bootstrap5 style, jQuery library; in front and back-end data interaction using the jQuery library ajax method, and use JSON as the format of data interaction, basically follow the RESTful API design style, make full use of the five HTTP methods.

The positioning of this system is clean, but not simple social media, abandoning the redundant and cumbersome functions of current social software to avoid homogenization. Its functions are mainly these: 1. Use email to achieve rapid registration, forget the password; 2. View and modify user information; 3. Manage friends, including adding friend, deleting friend, and modifying nickname; 4. View the system messages; 5. Publish casual word, which similar to QQ talk, WeChat moments, text / picture / video type are available, can also comment on casual word, reply to comment; 6. Release questions, and all registered users can view and answer the questions; 7. Paginate casual words, comments, system messages, questions and answers; 8. Chat with friends.

In addition to the above-mentioned functions, the data submitted by users is verified at the front-end and the back-end to ensure the standardization and correctness of the data; follow the MVC pattern to improve system maintainability, system portability, and code reusability as well as other non-functional design.

**Key words:** SSM; Social Media; Ajax; Druid; Thymeleaf

# 第一章 绪论
## 1.1 选题背景和意义
随着社会的发展，人类进入信息、大数据时代以来，风口的接连出现使得资本接连涌入，导致于各个公司涉及到的业务也越来越多，具体的反映到APP就是功能越来越多、越来越杂，导致应用占用的存储空间也进一步变大、启动加载的资源过多导致应用启动过慢等问题，甚至在使用过程中卡顿和杀后台的现象常常发生，使得用户使用应用体验较差。虽然导致这一问题的原因也有电子设备的关系（主要体现在设备性能、存储空间、运行内存等），但主要原因还是由于APP无节制的迭代，这一问题在社交媒体类的软件尤为显著。

例如作为社交软件的手机QQ为服务于厘米秀等功能内嵌虚幻4引擎（一款由Epic Games开发的游戏引擎），这一举动导致的问题就是应用大小暴增，在IOS中由上一版本8.8.50的491.4MB到版本8.8.55的879MB，而且作为服务于少数人的功能却要牺牲大部分用户的体验，这一举动受到许多用户的诟病；同样作为问答平台的知乎，加入直播功能以及朝着短视频平台方向的发展，APP内非重要且常用的功能不断出现，应用也变得逐渐臃肿等问题。总之，目前应用的发展基本都朝着“大”的方向进行发展。

社交作为人类社会重要组成，而在当前所处的疫情时代，居家隔离时的线上社交媒介显得尤为重要。因此，本社交媒体的设计以“小而美”作为理念重新出发，基于B/S模式进行开发。简洁而又不简单，简单而又不妥协，具有用户信息管理、好友聊天、随说发布、问答发布、评论等功能，使得社交属性更加纯粹。而作为轻量化的系统，使得用户不再过于受限，人们彼此之间的分享变得更加简单。
## 1.2 相关技术与工具
### 1.2.1 Ajax
Ajax的正式名称为Asynchronous Javascript And XML，翻译过来即是异步的JavaScript和XML，由Jesse James Garrett在2005年提出的，需要注意的是它并非是一种编程语言，而是一种集合了现有技术的方法，如集合了XMLHttpRequest、DOM、Javascript等。使用Ajax可以在页面加载完成后进行异步更新我们的网页信息，避免重新向服务器请求整个网页的信息，节省大量的网络带宽。

Ajax工作原理如图1.1所示：
![图 1.1 Ajax工作原理](https://img-blog.csdnimg.cn/bf75b133ccf94b36a30f9ce51452339e.png)<center>图 1.1 Ajax工作原理</center>
1. 当浏览器中某事件发生时，如页面加载、按钮点击以及其他我们自定义的事件；
2. 由Javascript创建XMLHttpRequest对象后交给Ajax引擎；
3. Ajax处理完成后通过http(s)请求发送到服务端的Web和XML服务器；
4. 之后交给我们的后台处理程序进行处理；
5. 在后台处理完毕后，将其返回到 Web和 XML服务器中进行处理；
6. 之后通过http(s)响应给浏览器的Ajax引擎；
7. 最后交由Javascript处理并更新DOM节点信息完成界面的更新。

在本系统中利用jQuery的ajax方法（一个为简化Ajax的使用从而对其进行封装的函数）请求页面信息、提交信息、删出记相关录，它是用户与用户、用户和服务器的交流平台，是整个系统中的一个关键环节。
### 1.2.2 jQuery
jQuery是于2006年1月由John Resig发布的一个主要为为简化操作DOM及其相关的而开发出的一个Javascript框架，且得到目前主流浏览器的很好的支持。其主要的功能包括：HTML元素的选取和操作、简化ajax的操作、操作CSS、使用Javascript动画、设置使用HTML事件，具有快速获取和操作DOM元素、增强事件处理和Javascript、简化开发动画等其他特点，得到大多数公司的青睐，普及率极高。

而在本系统中利用其简易操作html元素的特性，对DOM节点进行修改、删除、添加事件等操作。
### 1.2.3 RESTful架构
REST也就是Representational State Transfer的缩写，翻译为中文就是代表性状态转移，在2000年由Roy Thomas Fielding在他的博士论文上提出的一组软件开发架构的约束和规范，此时将符合REST相关的约束和规范的称为是RESTful架构。它的主要特点就是充分利用http(s)五种请求的方法（请求GET、提交POST、删除DELETE、全部更新PUT、部分更新PATCH）以及规定客户端请求和服务端返回的数据均已JSON格式进行交互，使请求API规范化、简洁化。

得益于SpringMVC对于RESTful架构的完美支持，使本系统可以更好的根据其相关规范和约束来进行开发。
### 1.2.4 MVC框架
MVC即为model（模型）、view（视图）以及controller（控制器）的缩写，是一种为了将用户界面、数据和业务逻辑解耦而开发设计出来的一种软件构建模式。如下图1.2所示，用户首先会经过控制器进行交互，所以可认为控制器是用户与服务端交互的窗口，之后经由控制器将请求的指令或者提交的数据提交到模型进行处理，根据需要由后者和数据库进行交互，此后模型再将处理好的数据反馈给视图层，此时用户便可查看相关的信息。
![图 1.2 MVC框架示意图](https://img-blog.csdnimg.cn/499557fc9383406287dea56ead0e0a5d.png)<center>图 1.2 MVC框架示意图</center>
通过对 MVC架构的分析，可以看出 MVC架构具有低耦合性、高重用性、高维护性等特点，而 SpringMVC正是在这种架构基础上进行开发的。
### 1.2.5 Thymeleaf模板引擎
模板引擎（Template Engine，在这里特指用于Web开发的Java模板引擎），主要体现在“模板”两字上，主要是为了实现用户界面与数据的分离，提高代码可复用性而开发设计出来的框架，逻辑与业务代码的分离使得开发效率大大提高，常见的模板引擎有Thymeleaf、FreeMarker、Enjoy、Velocity、JSP等。

而这个系统中使用的模板引擎是 Thymeleaf，由 Spring/Spring Boot所推荐，它可以在HTML、JavaScript、CSS、XML中，甚至可以在纯文本中插入类Java语法，使其可随后端业务数据发变化而在后端去渲染页面。而在设计之初，对于Spring/Spring Boot、HTML5有着很好的支持，且同其他模板引擎最大的区别就是可以不用去破坏原有的页面结构、可以在浏览器当中去静态运行，具有语法简单、功能强大的特点。

在本系统中利用了Thymeleaf渲染大部分的页面，例如提取并导入公共头部页面、提取并导入公共模态框、渲染问答界面等等大部分页面。
### 1.2.6 SSM框架
SSM框架，即Spring、SpringMVC以及MyBatis，是当前在Java EE领域中使用较多的一组框架，下面针对以上三者进行逐一介绍。

1）Spring

首先这里说明的Spring是指狭义上的Spring，也就是Spring Core，而广义上的Spring是指它的一整个框架集，包括Spring Data、Spring Cloud、Spring Security等。Spring是由Rod Johnson在2002年提出的一个为解决企业开发复杂度过高、维护成本过大、开发规范混乱的一个Java EE领域的轻量级框架。可以将其认为是一个对象管理器，Spring把对象当成是bean，而它作为bean factory生产并管理bean，具有低侵入式、开发简化等的特点，但它发展至今已经逐渐偏离开发简化这一路线，因此才诞生出了Spring Boot。总体而言，这是一个轻量级的控制反转（Inversion of Control）和面向切面编程（Aspect-Oriented Programming）的软件开发框架。

2）SpringMVC

SpringMVC也是属于Spring旗下的一个框架，它是以MVC为核心思想而设计的一个web框架，它除了拥有MVC的优点以外，同时可以和Spring无缝衔接，其核心主要是通过DispatcherServlet分发处理请求。
![图 1.3 SpringMVC执行流程](https://img-blog.csdnimg.cn/74c822426406429681c68f23433de8d5.png)
<center>图 1.3 SpringMVC执行流程</center>

3）MyBatis

MyBatis是由iBatis演化而来的一个位于Java持久层的框架，隐藏封装起了JDBC繁杂的与数据库交互的代码，同时使用XML或注解的形式来管理SQL代码，并通过Java接口的方法进行使用以及结果集的映射，且可以通过标签的形式支持动态SQL，使之具有开发效率高、结构清晰的特点。
### 1.2.7 MySQL
MySQL是一个由瑞典MySQL AB公司所开发的，目前是Oracle旗下的适合个人以及中小型企业所使用的开源的关系型数据库（Relational Database Management System，RDBMS），具有功能齐全、支持ACID事务、支持多种存储引擎、体积小巧等优点。
### 1.2.8 Druid
Druid是由阿里巴巴所开发的优秀的Java数据库连接池，目前已贡献给Apache开源。它不仅拥有比DBCP和C3P0等诸多数据库连接池的优秀性能、内置监控界面，且具有高扩展性等优点。又由于它针对MySQL进行一系列的优化，因此在本项目中使用它来管理MySQL数据库的连接。

### 1.2.9 IntelliJ IDEA
IntelliJ IDEA是由JetBrains所开发，主要用于Java语言的集成开发环境，相比于eclipse具有更优秀的代码管理、更精美的界面，且支持Java文档的预览、对于SSM项目的良好支持，因此使用该工具来进行开发本项目。
## 1.3 论文组织结构
根据项目本身的特点，将论文分为四个章节，具体如下：

第一章为绪论，阐述了选题背景和意义、系统相关技术与工具。

第二章为系统总体说明，通过对开发运行环境、二次开发或使用说明、系统模块及功能、用例图、项目的前后端代码结构进行介绍，使得对于本系统有一个较为清晰的结构认知。

第三章为系统详细设计，对于数据库、SSM框架设计以及根据八大模块并以用户界面为中心进行逐一的详细介绍。

第四章为性能测试，利用Apache JMeter对三个具有代表性的API（登录、获取聊天信息、发布问题）进行低、中、高负载的性能测试。

第五章为总结与展望，对本文进行总结，同时指出本系统的不足以及未来的改进方向。
## 1.4 本章小结
本章绪论首先介绍了选题背景，说明当下社交媒体过于庞大、功能过于复杂的畸形发展，并且对于用户的正常使用造成了一定的影响，针对此类问题重新开发出了一款轻量级、简洁而不简单的系统，说明了其意义。接着介绍本系统所使用的相关技术与工具，如RESTful、SSM、MVC框架等，最后再对本论文的组织结构进行介绍。


# 第二章 系统总体说明
## 2.1 开发运行相关
### 2.1.1 开发运行环境
1. 操作系统：Windows 11 家庭中文版，版本号为22000.652；

2. 集成开发环境：IntelliJ IDEA 2021.3.2 (Ultimate Edition)；

3. 项目管理工具：Maven，apache-maven-3.8.4；

4. web服务器：Tomcat，apache-tomcat-9.0.50；

5. 开发语言：Java，Java1.8.0\_291；

6. 数据库：MySQL，mysql-5.7.34-winx64。
### 2.1.2 使用说明
1. 将本项目文件夹基于SSM框架社交媒体实现打开为IntelliJ IDEA项目；

2. 安装并配置好Maven、Tomcat、Java、MySQL，建议使用以上开发运行环境中说明的版本，否则容易引发相关异常甚至错误；

3. 执行该项目文件夹下的src/main/resources/database.sql数据库文件生成数据库表；

4. 修改src/main/resources/database.properties文件中的jdbc.url、jdbc.name、jdbc.password的字段值，三者分别对应数据库的连接字符串、用户名、密码，其中数据库连接字符串需要注意的是端口号，如果没有修改过默认端口号就是3306；

5. 本系统注册、忘记密码时需要使用邮箱来进行确认，所以这里需要修改src/main/java/pers/zhz/utils/Utils.java文件的send163Mail方法中的sender、senderName、senderPwd变量值，分别对应您的邮箱、邮件发送名、邮箱授权码。这里还需要注意的是，如果您的邮箱如果不是163邮箱，则还需要修改里面标注的SMTP服务器地址，也有可能需要添加相应的字段，具体得根据您的邮箱服务商的要求来进行相应的修改。

6. 本项目的默认本地登录地址为localhost:8080，如果端口更改或者是非本地登录使用时还需要对src/main/java/pers/zhz/service/impl/NoLoginServiceImpl.java文件下的用户注册userRegister和忘记密码forgetPwd方法中的两处邮件内容mailContent变量进行相应端口号或服务器的地址更改，如图2.1所示的内容。
![图 2.1 邮件内容](https://img-blog.csdnimg.cn/318175a4e4394dd98ea108bab9d52ee8.png)<center>图 2.1 邮件内容</center>
## 2.2 系统模块及功能
![图 2.2 系统模块及功能](https://img-blog.csdnimg.cn/2db7c50df6d2402a9920570c959ea251.png)<center>图 2.2 系统模块及功能</center>
上图2.1展示了整体的一个系统模块及功能，整体分为八大模块。

1）首页模块

其中包含了对用户进行访问拦截，设置未登录禁止访问的403、请求未发现的404自定义错误页面；利用邮箱验证来进行快速注册、忘记密码；登录并退出登录。

2）设置模块

此模块用于修改用户本人的头像、用户名等其他资料信息，当取消修改时会从缓存中读取相关信息填回而非向服务器请求，并且在提交修改的资料时会与缓存中的进行对比，避免重复提交，浪费带宽。

3）系统通知模块

包含各类消息通知、删除消息以及回复添加好友，同时除了对消息进行分页以外，对于回复添加好友的同时也会对消息进行验证是否重复回复。

4）个人中心模块

用户查看本人的资料、发布的随说以及发布的问答。

5）管理好友模块

该模块包含修改好友昵称、将好友添加至聊天列表并跳转至聊天界面、删除好友、查看（非）好友资料、查看（非）好友发布的随说、查看（非）好友发布的问题。

6）问答模块

发布、查看、搜素发布的问题，并可对发布的问题进行回答、查看详情、回复回答，并支持分页查看。

7）随说模块

随说意为随心说，类似于QQ的说说、微信的朋友圈，可以发布文字、图片、视频的内容，可以对其进行点赞、评论，同时依然支持随说、随说评论进行分页查看。

8）聊天模块

该模块提供和好友进行聊天的功能，同时支持按最新消息对好友聊天列表进行实时排序；上滑加载更多好友聊天消息。

除了以上说明的，对于数据、请求参数、提交的数据均在前后端进行了验证，并对于程序进行严格的逻辑测试并修改等，确保能够安全、稳定、正常的使用。
## 2.3 用例图
下图2.3为本系统的用例图，用于直观展示不同角色之间、不同功能之间的逻辑关系。
![图 2.3 系统用例图](https://img-blog.csdnimg.cn/4201a2d2ec214aabaeb003cec4e486c9.png)<center>图 2.3 系统用例图</center>
参与者用户可分为两大类，游客（未登录或者未注册的）、已注册登录用户，而后者又根据相互间是否为好友分为已注册登录用户的好友、已注册登录用户的非好友。

首先用户作为未登录时的游客身份，可进行登录、注册、忘记密码，除此以外的请求会进行拦截并返回403错误界面。

而作为已注册登录的用户时，可以查看其他已注册用户的资料、查看和修改本人资料、查看系统消息、搜素用户、发布和查看随说、发布回答以及搜素问题、查看所有注册用户发布的问题（详细）、回复问题的回答、退出登录。

作为已注册登录用户的好友时，除了继承注册登录用户的以外，还可进行修改好友昵称、同好友聊天、删除该好友、点赞评论本人以及好友的随说、回复随说评论、查看本人及好友随说的详细内容。

最后，作为已注册登录用户的非好友，除了同样作为继承者拥有注册登录用户相关的用例，还可进行添加好友。
## 2.4 项目代码结构
### 2.4.1 后端代码结构
如下图2.4所示的为本项目后端部分的代码结构，对应项目文件路径src/main下的文件。pers.zhz.controller包对应的为控制器相关的代码，用于接收来自前端用户的请求，并对于前端提交的数据进行验证。代码结构按照八大模块进行划分的，自上而下分别对应的是随说相关控制器、聊天相关控制器、管理好友相关控制器、未登录相关控制器、个人中心相关控制器、问答相关控制器、设置相关控制器、系统消息相关控制器。

往下的interceptor为拦截器；listener为监听器相关的，该监听器是为了解决关闭tomcat所引发的相关错误；mapper文件夹下的文件为MyBatis相关操作数据库的代码，而impl文件夹下的xml文件对应相关的SQL映射文件，自上而下的顺序同以上说明controller相关的。
![图 2.4 项目后端代码结构](https://img-blog.csdnimg.cn/66a8dd350bec491bacb0fa009af69f42.png)<center>图 2.4 项目后端代码结构</center>
紧接着的pojo（Plain Ordinary Java Object，简单的Java对象）为对应数据库各个表的实体类；service为服务层相关的，用于调用mapper操作数据库并进行相应的处理，service.impl的为service下的接口相关的对应实现类，各个模块代码文件相关的顺序同上；utils为工具类，包含根据HttpServletRequest对象获取IP地址、生成简单文本邮件并发送的。

resources目录下的主要是配置相关的文件，applicationContext.xml为本SSM项目的总配置文件，在这里负责引入文件spring-mvc.xml和spring-mybatis.xml；database.properties是配置连接MySQL数据的以及Druid数据库连接池的；database.sql为建立数据库及数据表的SQL代码；environment.properties旨在说明本项目的开发运行环境的，并不具备任何功能，可删除；log4j.properties是为输出日志的配置文件；mybatis-config.xml为MyBatis配置输出日志以及驼峰-下划线命名互相转换的配置文件；而后面的两个文件spring-mvc.xml和spring-mybatis.xml分别是SpringMVC和MyBatis整合Spring的配置文件。

最后，在项目文件夹的根目录下pom.xml文件为Maven的配置文件。
### 2.4.2 前端代码结构
![图 2.5 项目前端代码结构](https://img-blog.csdnimg.cn/a76f92224b1f4f70bc4900a05d4b67fb.png)<center>图 2.5 项目前端代码结构</center>
上图2.5展示了本项目的前端代码结构，该部分对应项目文件夹根目录下的webapp文件夹。该文件夹下的static目录为存放静态文件的，其中的css样式文件夹中分为存放通用的bootstrap样式文件的文件夹、403以及404错误界面的error文件夹、存放本项目本人所写的各个css文件；img文件夹下的为.html中使用的svg类型的图片；而js文件夹下保存了各个Javascript相关的文件，有上至下分别对应bootstrap、jQuery、本人所写的关于本项目的以及other文件夹下保存的页面动态背景相关；plugins保存的为第三方分页插件jq-paginator的相关文件。

而在webapp下的userData文件夹，保存了用户发布随说的图片（视频）、用户头像；WEB-INF的templates存放着.html文件，common文件夹里面的为通用的.html模板文件，分别是公共头部、模态框，其它的自上往下分别是403错误、404错误、casualWord随说casualWordDetail随说详细、chat聊天、forgetPwd忘记密码、friend好友管理、index主页（登录）、modifyProfile修改本人资料、modifyPwd修改本人登录密码、myCasualWord我的随说、myProfile我的资料、notUserFriendCasualWord非本人好友随说、notUserFriendProfile非本人好友资料、qAndA问答、qAndADetail问答详细、register注册、systemMessage系统消息、userFriendCasualWord本人好友随说、userFriendProfile本人好友资料、userQAndA用户问答；而在最后的web.xml为web服务器tomcat的配置文件。
## 2.5 本章小结
通过本章，使得用户对于本系统有了一个整体上的结构认知。本章在开头说明了系统开发运行环境，操作系统、集成开发环境、项目管理工具、web服务器、开发语言以及数据库，紧接着针对项目部署或者二次开发说明需要进行修改的参数或配置。而系统模块及功能说明了项目的八大模块，以及针对于该模块所包含的功能，并通过用例图展示了系统不同角色之间关系、不同功能之间的逻辑关系。最后在项目代码结构上，通过对于前后端的代码文件分别进行说明，使得开发者在后期的优化或二次开发上可以迅速找到相对应的优化开发位置。

# 第三章 系统详细设计
## 3.1 数据库设计
### 3.1.1 E-R图
因为有太多的实体和相应的属性，所以在此将E-R图划分为两个相关的区域，见下面的图3.1和3.2。
![图 3.1 E-R图1](https://img-blog.csdnimg.cn/6bcf0a80a6f2428bbc72fa7d5916394e.png)<center>图 3.1 E-R图1</center>
![图 3.2 E-R图2](https://img-blog.csdnimg.cn/2622937a67104a2c80f60f6c5c5704cb.png)<center>图 3.2 E-R图2</center>
### 3.1.2 数据库表详细
本小节针对数据库、数据库表的所有相关信息进行说明。该数据库lescha的基字符集为utf8，而对应的数据库排序规则为utf8\_general\_ci，且数据库表均采用支持事务的InnoDB存储引擎以及utf8编码。
![图 3.3 数据库表组成](https://img-blog.csdnimg.cn/48c2fb77113944289a8a42fd5acf86b6.png)<center>图 3.3 数据库表组成</center>
如上图3.3所示，数据库名为lescha，共有十一个数据表，分别是用户表user、用户确认注册激活码和修改密码表confirm\_register\_and\_forget\_pwd、用户好友表user\_friend、系统消息表system\_message、随说表casual\_word、随说点赞表casual\_word\_like、随说评论表casual\_word\_omment、用户聊天列表表user\_chat\_list、用户聊天消息表user\_chat\_message、问答-问题表q\_and\_a\_question、问答-回答表q\_and\_a\_answer。

数据库表字段的外键约束在数据库表外进行规定，各个数据库表的详细信息如下：

<center>表 3.1 数据库表user</center>

|**字段名**|**数据类型**|**主键**|**唯一**|**空**|**描述**|
| - | - | - | - | - | - |
|id|bigint|是|是|否|自增，用户账号/表id|
|username|nvarchar(11)|否|否|否|用户名|
|is\_active|bit|否|否|否|是否在线，0下线，1在线|
|state|bit|否|否|否|是否已激活，0未激活，1已激活|
|email|varchar(30)|否|是|否|邮箱|
|password|varchar(32)|否|否|否|密码|
|phone\_number|varchar(11)|否|是|是|手机号码|
|sex|bit|否|否|是|性别，0女1男|
|head\_portrait|nvarchar(255)|是|否|是|头像名称|
|register\_time|datetime|否|否|否|注册时间|
|birthday|date|否|否|是|生日|
|hometown|nvarchar(20)|否|否|是|家乡|
|introduce|nvarchar(150)|否|否|是|个人简介|

<center>表 3.2 数据库表confirm_register_and_forget_pwd</center>

|**字段名**|**数据类型**|**主键**|**唯一**|**空**|**描述**|
| - | - | - | - | - | - |
|id|bigint|是|是|否|自增，表id|
|user\_id|bigint|否|否|否|用户账号|
|state|bit|否|否|否|是否有效，0无效，1有效|
|flag|bit|否|否|否|注册还是修改密码，0注册，1修改密码|
|code|char(32)|否|是|否|注册激活码/修改密码确认码|
|new\_password|char(32)|否|否|是|新密码|

<center>表 3.3 数据库表user_friend</center>

|**字段名**|**数据类型**|**主键**|**唯一**|**空**|**描述**|
| - | - | - | - | - | - |
|id|bigint|是|是|否|自增，表id|
|friend\_id|bigint|否|否|否|好友id|
|user\_id|bigint|否|否|否|该表所属的用户id|
|friend\_nickname|nvarchar(11)|否|否|否|备注好友昵称，默认为好友的账户名|

<center>表 3.4 数据库表system_messsge</center>

|**字段名**|**数据类型**|**主键**|**唯一**|**空**|**描述**|
| - | - | - | - | - | - |
|id|bigint|是|是|否|自增，表id|
|send\_time|datetime|否|否|否|消息发送时间|
|sender\_id|bigint|否|否|否|发送者id|
|receiver\_id|bigint|否|否|否|接收者id|
|content|nvarchar(100)|否|否|是|消息内容|
|message\_type|tinyint|否|否|否|消息类型，0请求添加好友;1同意添加好友;2拒绝添加好友;3删除好友;4点赞随说;5取消点赞随说;6评论本人的随说;7回复本人的评论;8回复本人的问题;9回复本人的回答|
|casual\_word\_id|bigint|否|否|是|消息相关随说id|
|question\_id|bigint|否|否|是|消息相关问题id|
|is\_effective|bit|否|否|否|是否生效，1生效，0失效，默认生效|

<center>表 3.5 数据库表casual_word</center>

|**字段名**|**数据类型**|**主键**|**唯一**|**空**|**描述**|
| - | - | - | - | - | - |
|id|bigint|是|是|否|自增，表id|
|user\_id|bigint|否|否|否|发布者id|
|publish\_time|datetime|否|否|否|随说发布时间|
|content|nvarchar(5000)|否|否|是|文字内容，该字段和图/视频字段至少有一个不为空|
|picture\_or\_video|varchar(40)|否|是|是|图/视频文件名|

<center>表 3.6 数据库表casual_word_like</center>

|**字段名**|**数据类型**|**主键**|**唯一**|**空**|**描述**|
| - | - | - | - | - | - |
|id|bigint|是|是|否|自增，表id|
|casual\_word\_id|bigint|否|否|否|点赞的随说id|
|like\_user\_id|bigint|否|否|否|点赞者id|
|like\_time|datetime|否|否|否|点赞随说时间|

<center>表 3.7 数据库表casual_word_comment</center>

|**字段名**|**数据类型**|**主键**|**唯一**|**空**|**描述**|
| - | - | - | - | - | - |
|id|bigint|是|是|否|自增，表id|
|casual\_word\_id|bigint|否|否|否|评论的随说id|
|commenter\_id|bigint|否|否|否|评论者id|
|comment\_time|datetime|否|否|否|评论时间|
|content|nvarchar(100)|否|否|否|评论内容|
|reply\_comment\_id|bigint|否|否|是|回复的评论id|

<center>表 3.8 数据库表user_chat_list</center>

|**字段名**|**数据类型**|**主键**|**唯一**|**空**|**描述**|
| - | - | - | - | - | - |
|id|bigint|是|是|否|自增，表id|
|create\_time|datetime(3)|否|否|否|创建时间|
|user\_id|bigint|否|否|否|所属用户账号|
|friend\_id|bigint|否|否|否|用户好友的账号|

<center>表 3.9 数据库表user_chat_message</center>

|**字段名**|**数据类型**|**主键**|**唯一**|**空**|**描述**|
| - | - | - | - | - | - |
|id|bigint|是|是|否|自增，表id|
|content|nvarchar(1000)|否|否|否|消息内容|
|receive\_state|bit|否|否|否|接收状态，0未接收，1接收|
|send\_time|datetime(3)|否|否|否|消息发送时间|
|sender\_id|bigint|否|否|否|发送者账号|
|receiver\_id|bigint|否|否|否|接收者账号|

<center>表 3.10 数据库表q_and_a_question<center>

|**字段名**|**数据类型**|**主键**|**唯一**|**空**|**描述**|
| - | - | - | - | - | - |
|id|bigint|是|是|否|自增，表id|
|questioner\_id|bigint|否|否|否|提问者id|
|question\_time|datetime|否|否|否|提问时间|
|content|nvarchar(5000)|否|否|否|提问内容|

<center>表 3.11 数据库表q_and_a_answer</center>

|**字段名**|**数据类型**|**主键**|**唯一**|**空**|**描述**|
| - | - | - | - | - | - |
|id|bigint|是|是|否|自增，表id|
|question\_id|bigint|否|否|否|问答-问题id|
|responder\_id|bigint|否|否|否|回复者id|
|response\_time|datetime|否|否|否|回答时间|
|content|nvarchar(100)|否|否|否|回复的内容|
|reply\_answer\_id|bigint|否|否|是|回复的问题-回答id|
## 3.2 SSM框架及Tomcat配置
SSM框架相关的配置文件均在src/main/resources目录下以及处理拦截的配置类src/main/java/pers/zhz/interceptor/UserInterceptor.java，而web服务器Tomcat的配置文件web.xml位于webapp/WEB-INF目录下以及监听器包的文件DriverMangerListener.java。

1）applicationContext.xml

该文件为SSM框架的起点配置文件，在这里该文件通过import标签的resource属性引入Spring整合SpringMVC的配置文件spring-mvc.xml和Spring整合MyBatis的配置文件spring-mybatis.xml。

2）database.properties

该文件为连接MySQL数据库配置了驱动类名、连接字符串、数据库账号、数据库密码，同时为Druid数据库连接池配置了初始连接数、最小连接数、连接等待超时时间、配置监控统计拦截的filters等属性。

3）log4j.properties

该文件为log4j日志提供了相关配置。规定了输出文件最小级别为debug，对于其他日志级别如stdout、info、warn、error均作了相关规定，例如设置了日志文件输出位置、日志文件名、根据日期和类型对日志文件进行分类等。

4）mybatis-config.xml

此文件为MyBatis的文件，该文件通过logImpl、mapUnderscoreToCamelCase分别设置Mybatis操作数据库的相关详细信息在控制台输出、启用数据库字段下划线映射到java对象的驼峰式命名；最后通过typeAliases标签自动为pers.zhz.pojo包下的实体类设置别名，这里使用使用实体类的类名（小写开头）作为别名，避免过多重复代码。

5）spring-mvc.xml和UserInterceptor.java

该文件Spring整合SpringMVC的配置文件。该文件主要就是进行开启SpringMVC注解驱动；静态资源默认servlet配置；配置Thymeleaf的模板引擎、模板解析器以及视图解析器；扫描指定包下的注解将其加入到Spring容器，在这里就是扫描service以及controller包下的注解；配置拦截器，限制游客的访问权限、并配置哪些请求路径不通过拦截器的处理，并在此处通过bean标签指定了处理拦截的类UserInterceptor，该类通过重写preHandle方法，验证了是否存在相应的session来决定是否放行，并输出到日志；最后再配置上传文件（上传的文件有用户的头像、随说的配图/视频）的编码为UTF-8，避免出现乱码。

6）spring-mybatis.xml

spring-mybatis.xml为Spring整合MyBatis的配置文件。首先是关联数据库配置文件database.properties；通过bean的属性注入将数据库配置文件中的所有属性注入；定义sqlSessionFactory的bean，同样通过属性注入的方式将上面的配置的数据库bean、mapper的xml文件位置以及MyBatis配置文件的位置mybatis-config.xml注入该bean；紧接着将上面配置的sqlSessionFactory的bean注入，以及将所有mapper接口的位置以包的形式通过属性注入；最后利用上面配置的数据源为数据库事务管理器进行配置。

7）web.xml和DriverMangerListener.java

web.xml文件配置的基本都是一些web服务器通用的配置。首先会利用spring的web过滤器进行乱码过滤，同样使用的utf-8的编码方式，避免中文乱码；之后通过servlet-mapping配置不拦截的静态资源，这里的配置和Spring整合SpringMVC的配置文件spring-mvc.xml中的静态资源默认servlet配置配合使用；然后为SpringMVC配置它的核心DispatcherServlet，在这里也指出SSM的总配置文件applicationContext.xml的位置；接下来配置监听器DriverMangerListener，该监听器是用来解决Tomcat关闭时出现的可能引起内存泄漏的问题；最后再对session的过期时间、403和404界面进行配置。
## 3.3 模块详细设计
本节主要通过根据八大各个模块，以用户界面为起点逐个分析说明其背后的执行流程的方式来对详细设计进行讲解。以下说明数据验证时均在前端用户界面以及后端机型验证，且数据的请求、提交均采用jQuery的ajax方法，数据格式默认使用JSON，除非提交的数据包含图片文件而无法使用JSON进而去使用FormData的形式。
### 3.3.1 首页模块
该模块主要是包含作为未登录的游客便可访问的资源路径，例如登录、注册、忘记密码、错误界面，对应的源代码文件为以NoLogin开头的controller、service以及mapper。

首先，当用户未登录的情况下，访问了除静态资源、该模块的控制器以外的资源时，通过在拦截器部分对session进行验证，发现没有时会返回403禁止访问的错误界面，如下图3.4所示。
![图 3.4 403错误界面](https://img-blog.csdnimg.cn/c788003d4af5459b9d329fb30f0a1670.png)<center>图 3.4 403错误界面</center>
此时用户可点击此处的链接请求路径返回到如下图3.5所示的登录（首页）界面进行登录，窗口大小调整时会实时调整组件的位置使之居中。
![图 3.5 登录（首页）](https://img-blog.csdnimg.cn/681144c6c30d4dd2916e0739b6dd6cf1.png)<center>图 3.5 登录（首页）</center>
登录、注册、忘记密码界面均可点击进行相互跳转，由于一开始没有账号，所以这里点击注册链接跳转到图3.6所示的注册界面进行注册。
![图 3.6 注册界面](https://img-blog.csdnimg.cn/88605a09c4e348d8851d9ede579e44d2.png)<center>图 3.6 注册界面</center>
输入用户名、邮箱、密码后，点击注册，此时会在前端对于输入的信息进行验证是否符合规范，如验证用户名的长度是否在指定范围内、利用正则表达式验证邮箱格式是否正确、是否缺项、两次输入的密码是否符合规范，如果不符合相应的条件，则会弹出提示通用的通知模态框提示错误的信息，点击界面任意位置可关闭，该模态框通过Thymeleaf的th:replace语法对每个界面进行引入使用。
![图 3.7 注册提示错误信息](https://img-blog.csdnimg.cn/d4e39bfdbe724ffb9824aabaa23f43f4.png)<center>图 3.7 注册提示错误信息</center>
提交的注册信息通过前后端数据格式认证之后，便进入服务层进行处理，首先会验证用户名、邮箱是否出现了重复，若重复则告知返回重复的提示信息，并让用户修改后提交；对密码进行MD5加密后、生成UUID作为唯一标识码用作邮箱验证后同其他数据存储进数据库，同时给用户提交的邮箱发送信息，操作完成之后提示用户，用户关闭提示框后自动跳转至登录界面。此时用户需前往邮箱进行确认注册，点击邮箱里面的的链接使该账号生效、使该链接失效，否则无法登录使用，重复点击会返回链接失效的错误信息。
![图 3.8 注册邮箱信息](https://img-blog.csdnimg.cn/2c51eefd943343378c613494bbf26c2c.png)<center>图 3.8 注册邮箱信息</center>
使用忘记密码时，输入需要修改的账户的邮箱、新密码进行操作，同样会进行数据的验证，生成标识码用户邮箱的验证操作，点击发送到邮箱的链接完成密码的修改，后台会根据标识码判断是属于修改密码还是注册的提取相应的信息进行处理。
![图 3.9 忘记密码界面](https://img-blog.csdnimg.cn/abb9a58b54a540d3a7ff41353e4fdc69.png)<center>图 3.9 忘记密码界面</center>
注册成功后便可输入用户名、密码进行登录，同样进行数据验证，登录、注册、忘记密码均设置了键盘监听时间，按下回车提交信息。用户名、密码验证通过之后将用户状态设为在线并进入聊天界面，否则提示相关错误信息，如账号密码错误，或者说明账号未激活，提示前往邮箱进行激活的操作。在登录进入之后，若访问并未存在的请求，则会转发并返回如图3.10的404错误界面。
![图 3.10 404错误界面](https://img-blog.csdnimg.cn/54fd5a986e844fd2b0ef3a6f4bc90df9.png)<center>图 3.10 404错误界面</center>
### 3.3.2 设置模块
登录后可通过任意界面的公共头部（同样由th:replace引入）跳转至设置页面进行用户资料以及密码的修改，设置模块的修改资料页面如图3.11所示。
![图 3.11 修改资料页面](https://img-blog.csdnimg.cn/86586b7731214e82b76f4b7d1cc7cca5.png)<center>图 3.11 修改资料页面</center>
该页面和数据由Thymeleaf进行渲染、加载，性别为空时男女都不会被选，但选择并提交之后不能置空，而生日为空时回显示为9999/12/31，同时在页面加载完成后对于原始请求的数据使用全局变量进行保存，使得在修改数据时取消修改不经过服务器便可恢复、提交修改的信息时可对比原始数据，只提交变更的数据。

初始进入该页面是输入框均为不可编辑，点击修改之后变成可编辑，同时将不可用的取消、提交按钮变成可用，修改变成不可用，点击取消则反之，并且数据输入框回恢复成原始的数据。修改状态下同样可通过点击头像进行修改头像，点击时进入呼出选择图像的文件，限制选择的文件类型为.png或.jpg类型的图片以及最大为5MB。

提交修改的数据时进行验证，如果什么都不做更改时会提示信息未更改，否则提交到后台进行处理。处理完成后返回信息，其中state为1表示修改成功；2为用户名已被占用；3为邮箱已被占用；4为是手机号已被占用；5为用户名和邮箱已被占用；6为用户名和手机号已被占用；7为邮箱和手机号已被占用；8为用户名、手机号、邮箱均已被占用；其他则为修改出错。

点击修改密码进入修改密码界面，如图3.12所示，这里的修改密码并不会用到邮箱验证，因为此时已经通过现在的密码登录进来。而点击公共头部的头像呼出下拉列表的退出时，将返回首页并设置用户状态为下线。
![图 3.12 修改密码界面](https://img-blog.csdnimg.cn/0dc78c0ad9c64a46b0df8b5f922d4027.png)<center>图 3.12 修改密码界面</center>
### 3.3.3 系统通知模块
如图3.13所示，该界面同样由Thymeleaf进行渲染，请求该页面时返回系统消息相关字段，以及用于分页相关的当前页码、总共页数、当前请求路径。
![图 3.13 系统消息界面](https://img-blog.csdnimg.cn/0fbfb2cbd4244c3797cfac3dafb159b2.png)<center>图 3.13 系统消息界面</center>
消息以List的形式，并根据消息发送时间进行降序排序的方式进行渲染，同时按照每页十条是消息进行分页，当访问请求/systemMessage时会转发到/systemMessage/1，代表访问第一页的。而消息又被分为十种：0请求添加好友；1同意添加好友；2拒绝添加好友；3删除好友；4点赞随说；5取消点赞随说；6评论本人的随说；7回复本人的评论；8回复本人的问题；9回复本人的回答，会根据指定序号进行渲染。

消息从后端获取的时间表达类型为时间戳，在页面被加载完成时会根据html的标签class属性进行修改，当天的消息则显示时间，否则显示日期，类似于此的时间处理有随说、随说评论、问题、问题回答、聊天列表相关，但有一点不同的是聊天列表的时间并不是通过class属性来进行修改的，但时间处理方式是一样的。同时支持删除系统消息，并且可以根据相应的用户账号、随说id、问答id直接点击跳转到用户资料界面、随说详细界面、问题详细界面。而对于添加好友类型的系统消息，如上图3.13所示的消息，在回复同意添加/拒绝添加时会通知添加的发起方，再次点击则会提示该消息已失效。
### 3.3.4 个人中心模块
该模块负责展示本人的资料、随说、问答。

首先是展示个人资料的页面，这里的处理同设置模块的资料展示基本相同，如图3.14所示。
![图 3.14 我的资料界面](https://img-blog.csdnimg.cn/2d268d6d01434e8290aef3b195da6ca3.png)<center>图 3.14 我的资料界面</center>
图3.15展示的是我的随说界面，同样是将JSONObject放入List交由Thymeleaf渲染，根据发布时间进行降序排序。而显示的时间也就是随说的发布时间，对于时间的处理同上、分页与系统通知相同：同一天则显示时间，否则显示日期；而对于分页，每二十条随说（问题）为一页，只有超过二十条才会显示分页。同时该界面可以对随说进行点赞、评论、查看详细信息。
![图 3.15 我的随说界面](https://img-blog.csdnimg.cn/3a4c9d579fa94a1eaf6018c9b46441c9.png)<center>图 3.15 我的随说界面</center>
图3.16展示的是我的问答界面，该界面同我的随说界面的大体结构相同，只不过问答只有文本类型的，且没有点赞的功能，在该页面同样可进行回答问题、进入到问题详细页面。
![图 3.16 我的问答界面](https://img-blog.csdnimg.cn/a831763f7d3b4f48ae9624899cc16e98.png)<center>图 3.16 我的问答界面</center>
### 3.3.5 管理好友模块
该模块包含查看好友资料、（非）好友随说、（非）好友问答，搜素并添加好友、修改昵称、将好友加入聊天列表的功能。

图3.17好友界面提供了展示了好友列表、搜素添加好友的功能。
![图 3.17 好友界面](https://img-blog.csdnimg.cn/917446b645d74d32bf2acc76db79a5cb.png)<center>图 3.17 好友界面</center>
点击左侧的好友则可进入相应的好友界面，如图3.18所示的。
![图 3.18 好友资料界面](https://img-blog.csdnimg.cn/de6a3fc40dd841c0bc0b1e9c260ae198.png)<center>图 3.18 好友资料界面</center>
点击修改昵称时好友昵称变成可编辑，同时取消修改和确认修改按钮可见、修改昵称按钮不可见，昵称修改完成后变成图3.18的初始状态，而点击取消修改时昵称会通过缓存恢复成原来的；而当点击发送消息时，后台判断该好友是否在本人的好友聊天列表，如果不在则添加到聊天列表，否则将该好友所在的本人的聊天列表的时间修改为当前时间，接着跳转至聊天界面；当删除好友时呼出确认删除对话框，确认删除之后会将好友移除出好友列表以及删除相关聊天记录，考虑到随说（问答）之间的回复关系，因此并不会删除相关评论（回答）。而对于该模块的好友随说界面以及好友问答基本同我的随说以及我的问界面，这里就不再说明，都是展示当前显示资料的用户发布的随说以及问题，如图3.19和3.20所示。
![图 3.19 好友随说界面](https://img-blog.csdnimg.cn/e94cb2c650a548bbb1de77d15854d6ae.png)<center>图 3.19 好友随说界面</center>
![图 3.20 好友问答界面](https://img-blog.csdnimg.cn/aa62eeda47444d3b9fb0445c927dfbd0.png)<center>图 3.20 好友问答界面</center>
而在图3.17的好友界面搜素用户是根据用户名来进行搜素的，可以搜素全部用户，当搜素本人时会跳转到图3.14的我的资料界面；而搜素的用户是本人的好友时，则会跳转到图3.18的好友资料用户界面；搜素的用户不是本人的好友时会跳转至下图3.21所示的非好友资料界面。
![图 3.21 非好友资料界面](https://img-blog.csdnimg.cn/0e85f9e01d5b4d22a857419e6e356b71.png)<center>图 3.21 非好友资料界面</center>
该界面除了展示好友的资料以外，同时可以进行添加好友，并且会对相关逻辑进行验证，如：重复点击时会提示等待对方的回复；而当本人还停留在此界面，而对方已经查收了系统消息并通过，那么此时点击会提示已经是好友，无须重复添加；或者本人已经发起添加该好友的请求、对方还未回复的情况下，对方发起添加本人为好友的请求时，会提示对方不要重复添加，请前往系统消息进行查看消息。

互为好友的情况下，通过输入url的形式进行访问非好友的资料、随说、问答界面时，会重定向到好友的对应界面，反过来也是进行类似的处理。

下图3.22以及3.23分别是非好友的随说、问答页面，除了不能对非好友的随说进行点赞、评论、查看详细以外，其他的也基本同上说明的。
![图 3.22 非好友随说界面](https://img-blog.csdnimg.cn/557dc27129084283b0c51b60e5a05dce.png)<center>图 3.22 非好友随说界面</center>
![图 3.23 非好友问答界面](https://img-blog.csdnimg.cn/2687052844144373af6da0e2d23d39f9.png)<center>图 3.23 非好友问答界面</center>
### 3.3.6 问答模块
问答模块的用户发布的问题是对于全体注册用户可见的，所有的用户均可对问题进行回答、回复回答，当然，对于问题、问题的回答也进行了分页，同时支持模糊搜素，问答页面如图3.24所示。
![图 3.24 问答页面](https://img-blog.csdnimg.cn/87e87cd13b874818a3ecd91c6a471732.png)<center>图 3.24 问答页面</center>
问答的查看同样是根据发布时间进行降序排列的，而当本人在此页面发布问题成功时，会将此问题添加到最上方，同时支持一键清空要搜索/发布的内容。搜素问答时使用使用模糊搜索，将搜素到的结果输出到左侧的界面，同时会清空原有的，如图下3.25所示。
![图 3.25 搜素问题结果](https://img-blog.csdnimg.cn/6c494a2979864dee9c0074e4cab1b178.png)<center>图 3.25 搜素问题结果</center>
点击回答可呼出回答模态框进行回答，回答成功会提示回答成功，而点击查看详情时会进入该回答的详细页面，如图3.26所示，而当该问答属于本人发布的时候，用户可在详细页面对其进行删除，包括该问题是相关回答、系统消息均会被删除。
![图 3.26 问题详细页面](https://img-blog.csdnimg.cn/9b1193ee0df043f7a4e6b6a53d9d6d55.png)<center>图 3.26 问题详细页面</center>
处理可以点击回答呼出如图3.27所示的模态框进行回答以外，这里会根据问题发布者、回答问题者、回复回答这三者（以下的举例说明分别用A、B、C分别指代三者）之间的关系通过系统消息通知指定的用户，例如当三者都不是同一人时，分别通知A、B问题被回答、回答被评论；当A与B为同一人，而与C不同时，会通知A回答被回复等。
![图 3.27 呼出回答问题模态框](https://img-blog.csdnimg.cn/9933caa0f09d4f1690cd28cf217cf8e4.png)<center>图 3.27 呼出回答问题模态框</center>
### 3.3.7 随说模块
![图 3.28 随说界面](https://img-blog.csdnimg.cn/f09aed5266564ab4b01aa980ea10ded2.png)<center>图 3.28 随说界面</center>
随说界面同问答页面从结构上来说大体是相同的，下面只针对不同点来进行说明。首先两者随说是支持发布文字、图片、视频形式的，或者文字与另外两者之一相结合；随说是面向好友的，非好友不可查看详细、评论随说、回复评论、点赞；同时选择图片或视频时依然会在前后端对于文件的格式、大小进行验证；点击清除按钮时会同时清除要发布的随说的文字、图片、视频。

而对于随说详细来说，除了不同用户的查看权限不同，其他均是相同的，如图3.29所示。
![图 3.29 随说详细页面](https://img-blog.csdnimg.cn/6c7cb43aeec74afd8434b308d92a8b67.png)<center>图 3.29 随说详细页面</center>
### 3.3.8 聊天模块
![图 3.30 聊天界面](https://img-blog.csdnimg.cn/2b42baf20b2148108a9bfa5bf27a0e91.png)<center>图 3.30 聊天界面</center>
上图3.30展示了聊天界面，由于SpringMVC无法直接与js文件中的代码进行直接的交互，所以这里先将聊天相关的消息以JSON字符串的格式通过Thymeleaf保存在隐藏的input下，待页面加载完成以后将其取出并转化为JSON格式缓存起来，然后将这些数据进行处理显示。

左侧的当前聊天用户列表界面是动态根据最新消息的发送时间进行实时调整，从上至下为降序排序，当本人与该好友之间没有消息时会根据该列表项的创建时间参与排序。否则根据两者哪个是最新的来进行参与排序，而每次的在好友资料界面点击发送消息会更新该记录的创建时间。而在聊天用户列表界面中也会显示最新消息，消息超过指定的的长度会用…进行替代。

在初始页面加载完成之后，对于聊天消息的接收采用的是ajax轮询发方式进行，将接收的新的数据与处理以后与初次获取的数据缓存放在一起，而出现新的聊天用户会实时添加到左侧的用户列表。

对于右侧的发送、查看聊天消息界面，聊天时需要从左侧的好友选定，否则无法查看、发送消息。同时用户的聊天记录也会进行分页获取，每次至多获取本人与每个好友的十条最新消息，然后通过设置滑动条滑到顶部这一事件，根据本地缓存的第一条信息的发送时间获取该消息以前的记录，获取到的消息显示并缓存下来，避免重复请求。

聊天消息是根据发送的时间由上至下进行升序排列的，接收到新消息时，判断是否是当前聊天的用户，是则缓存并输出显示消息，否则只缓存；而发送消息时，会将消息添加在滑动条底部，同时设置滑动条监听事件，使之保持在底部，同时也可以通过回车键实现消息的发送，对于发送消息的长度也进行了限制。
## 3.4 本章小结
本章对于系统的详细设计进行讲解，首先展示了数据库的E-R图，使得对于数据库的各个表有一定的了解，然后再通过展示十一个数据表的详细，包括字段名、类型、是否可为空、是否为主键、字段描述，使得对于数据库有了全面的了解；紧接着对于SSM框架以及Tomcat的配置进行说明，了解了该项目的骨架是如何搭建起来的；最后便是根据功能分为八大的模块进行讲解，围绕用户界面为中心，去讲解该项目的前后逻辑结构。
# 第四章 性能测试

本章性能测试，利用JMeter（Apache下的一个开放源码的性能测试工具，它具有体积小、功能强大、使用简单等优点，可以对在服务器上部署应用进行性能测试）对三个具有代表性的API（登录、获取聊天信息、发布问题）的TPS（Transactions per Second，每秒事务数）、TRT（Response Times Over Time，事务响应时间）进行低、中、高负载的性能测试，并绘制出相应的折线图，直观的反应稳定性、整体响应时间的走向等，同时也会对其聚合报告进行分析。而在这里用户通过线程来进行模拟，持续时间表示指定用户数下访问的时间段。

该测试为本地环境测试，测试环境除了在第二章说明的开发运行环境，其他的测试环境的参数有：电脑型号：小米笔记本Air 13.3 2018款；CPU：Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz 1.99 GHz；JMeter版本：5.4.3。
## 4.1 登录性能测试
通用测试参数如下：

1）请求方法：PATCH；

2）请求路径：/login；

3）消息体数据：{"username":"root","password":"1234567890"}；

4）持续时间：5s。
### 4.1.1 低负载
专用测试参数如下：

1）线程数：10。

如下图4.1低负载的登录TRT显示，回复次数从开始到900ms左右为0，而在900ms左右达到顶峰26.7次/ms，之后快速下降到1.3s左右的12.1，然后就曲折降低的状态直到结束。
![图 4.1 低负载时登录的 TRT](https://img-blog.csdnimg.cn/58f7fff8ca1c49f797980f773ac65da2.png)<center>图 4.1 低负载时登录的 TRT</center>
如下图4.2的低负载的登录TPS所示，每秒处理的事务从0s到800ms左右为0，从800ms开始缓慢上升到1.7s的1.9并持续在此速度左右直到4.2s，之后便开始降低直到结束时的1。
![图 4.2 低负载时登录的 TPS](https://img-blog.csdnimg.cn/3065a78a812d4d7c9f03f118a7c233ea.png)<center>图 4.2 低负载时登录的 TPS</center>
在聚合报告之后，发现在响应时间当中，平均为12ms、中位数为11ms、最小值为10ms、最大值为27ms，异常率为0%，吞吐量为2.3次/s，接收以及发送速率分别为0.66KB/s、0.50KB/s。
### 4.1.2 中负载
专用测试参数如下：

1）线程数：1000。 

如下图4.3中负载的登录TRT显示，每毫秒的回复次数在开始达到顶峰120次/ms，之后便降低直到1.5s左右才开始有小幅度上升，并在2s达到顶峰的41.1，然后持续下降直到结束时的6.8。
![图 4.3 中负载时登录的 TRT](https://img-blog.csdnimg.cn/e8cea496c6eb48fe9a3e386907afdfeb.png)<center>图 4.3 中负载时登录的 TRT</center>
如下图4.4的中负载登录TPS所示，每秒处理的事务在数目在1s左右达到顶峰的222.4，之后便小幅降低持续到4s便开始较大幅的降低，直到5s的159.7。
![在这里插入图片描述](https://img-blog.csdnimg.cn/545808299565470eb905e064179891dc.png)<center>图 4.4 中负载时登录的 TPS</center>
而在进一步分析得出的聚合报告之后，发现在响应时间当中，平均为26ms、中位数为7ms、最小值为5ms、最大值为192ms，异常率为0%，吞吐量为200.4次/s，接收以及发送速率分别为58.72KB/s、44.83KB/s。
### 4.1.3 高负载
专用测试参数如下：

1）线程数：10000。

上图4.5表明，从开始到2.5s左右的每毫秒回复数为0，在2.5s左右为2433.5，一直持续到结束均是上升的趋势，并在结束时的1min21s达到顶峰74828.9。
![图 4.5 高负载时登录的 TRT](https://img-blog.csdnimg.cn/f9677f6c99924b75bec9cafacf685e23.png)<center>图 4.5 高负载时登录的 TRT</center>

图4.6高负载的登录TPS了解到，在10s~12s这个时间段出现了大量的http请求失败，而成功的请求从2s左右出现，之后呈现上升的趋势直到11s左右，接着就是相对平缓，每秒处理的事务数保持在122.8直到结束，在此期间也会出现偶尔的降低。
![图 4.6 高负载时登录的 TPS](https://img-blog.csdnimg.cn/4c04918b64cb4c1da6d15abc38f32578.png)<center>图 4.6 高负载时登录的 TPS</center>
而在聚合报告表明，平均、中位数、最小值、最大值的响应时间分别是35231、36778、2074、74389ms；异常出现的频率为16.34%；吞吐量为123.1/s，发送和接收的速率分别为83.32、23.03KB/s。
## 4.2 获取聊天信息性能测试
通用测试参数如下：

1）请求方法：GET；

2）请求路径：/ notReceiveChatMessage；

3）持续时间：5s。
### 4.2.1 低负载
专用测试参数如下：

1）线程数：10。 

由下图4.7所示，从0.5s开始出现http请求，同时每毫秒的回复次数达到顶峰50.6，指之后便快速降低到1s的3.7，并持续在此数值左右直到结束。
![图 4.7 低负载时获取聊天信息的 TRT](https://img-blog.csdnimg.cn/ec0d4c9f8a8d487ab0f1f4772b305bb6.png)<center>图 4.7 低负载时获取聊天信息的 TRT</center>

而由下图4.8所示，每秒处理的事务数相对平缓，从开始的1到1s时的2，并持续在2直到4s后开始降低回5s时的1。
![图 4.8 低负载时获取聊天信息的 TPS](https://img-blog.csdnimg.cn/c775cf1b46504ad9ab151f1282d4dadc.png)<center>图 4.8 低负载时获取聊天信息的 TPS</center>
聚合报告表明，在响应时间当中，平均值为10ms，中位数为6ms，最小值为4ms，最大值为51ms；异常率为0%；吞吐量为2.2/s；接收和发送的速率分别为0.43KB/s、0.53KB/s。
### 4.2.2 中负载
专用测试参数如下：

1）线程数：1000。
![图 4.9 中负载时获取聊天信息的 TRT](https://img-blog.csdnimg.cn/b619d4992b5a403ca834fb95af2d01af.png)<center>图 4.9 中负载时获取聊天信息的 TRT</center>
由上图4.9可知，每毫秒的回复次数由开始的58.2降低到1s的3.8，持续在此数值左右直到3s开始快速上升到顶峰3.5s的89.7，便快速下降到3.5s的5.3，并持续在附近的数值直到结束。
![图 4.10 中负载时获取聊天信息的 TPS](https://img-blog.csdnimg.cn/7f756fb72126450d91ebce46f9296748.png)<center>图 4.10 中负载时获取聊天信息的 TPS</center>
上图4.10的中负载时获取聊天信息的TPS可知每秒处理的事务从114.8开始，到达1秒左右达到顶峰199.2，并持续到4秒左右便开始下降，直到5秒的85.2。

根据此时的聚合报告，了解到在响应时间当中，平均值、中位数、最小值、最大值（单位为ms）分别为17、3、2、242，异常率同样为0%，吞吐量为200.8次/s，接收以及发送速率分别为39.23KB/s、48.25KB/s。
### 4.2.3 高负载
专用测试参数如下：

1）线程数：10000。

由下图4.11所示，每毫秒的回复数呈现的是一个上升的态势，从开始时的456.3一直到结束时的53612.2，而在10s~13s这一时间段有明显的下降、上升。
![图 4.11 高负载时获取聊天信息的 TRT](https://img-blog.csdnimg.cn/5613938c85724ab884370693561130b8.png)<center>图 4.11 高负载时获取聊天信息的 TRT</center>
而根据图4.12可知，在10s~12s左右已经出现了较多的http请求失败，呈现快速上升、下降的变化，在成功的http请求中，每秒处理的事务数基本保持在167左右。
![图 4.12 高负载时获取聊天信息的 TPS](https://img-blog.csdnimg.cn/d5293389332f41f48e831e703fd9c3ca.png)<center>图 4.12 高负载时获取聊天信息的 TPS</center>
最小、最大、平均、中位的响应时间分别为270、54039、25675、25630ms；异常率达到了15.75%；接送和发送的速率分别为93.18KB/s、32.48KB/s；吞吐量为160.5/s。
## 4.3 发布问题性能测试
通用测试参数如下：

1）请求方法：POST；

2）请求路径：/qAndA/question；

3）持续时间：5s；

4）消息体数据：{"content":"问答性能测试"}。
### 4.3.1 低负载
专用测试参数如下：

1）线程数：10。

由下图4.13可知，每毫秒的回复次数在开始时达到顶峰8后便开始下降到0.5s时的7，并保持在该数值直到结束。
![图 4.13 低负载时发布问题的 TRT](https://img-blog.csdnimg.cn/e8b504daf1ed43b4a87dd84f245e4726.png)<center>图 4.13 低负载时发布问题的 TRT</center>
而在下图4.14当中，每秒处理的事务数变化比较缓和，开始和结束时为1，而在中间的时间段保持在2左右。
![图 4.14 低负载时发布问题的 TPS](https://img-blog.csdnimg.cn/a52070ffa2534fa997d6309cccd0f761.png)<center>图 4.14 低负载时发布问题的 TPS</center>
而平均、中位数、最小的想响应时间为7ms，最大为8ms；异常率为0%；发送和接收的速率分别为0.72KB/s、0.55KB/s。
### 4.3.2 中负载
专用测试参数如下：

1）线程数：1000。

上图4.15所示的中负载发布问题TRT，从开始到500ms左右的每毫秒回复次数一直为0，直到500ms达到顶峰93.5便开始下降到1s的低谷7.2，并且基本一直保持此数值直到结束。
![图 4.15 中负载时发布问题的 TRT](https://img-blog.csdnimg.cn/26c6cd2fcee84761beaaafbf919c964b.png)
<center>图 4.15 中负载时发布问题的 TRT</center>
图4.16中负载时发布TPS可知每秒处理的事务数从111.9开始，在1秒左右达到顶峰197.9，并持续在此数值左右直到4秒左右便开始下降，一直到5s时的87.6，上升以及下降的过程相对来说较为陡峭。
![图 4.16 中负载时发布问题的 TPS](https://img-blog.csdnimg.cn/1aa770f60dd245338662cd97afbc5d96.png)<center>图 4.16 中负载时发布问题的 TPS</center>
最后，根据聚合报告，了解到它在响应时间当中，平均值、中位数、最小值、最大值（单位为ms）分别为16、7、5、227，最小以及最大差距过大，而异常率同样为0%，吞吐量为200次/s；接收以及发送速率分别为49.60KB/s、64.44KB/s。
### 4.3.3 高负载
专用测试参数如下：

1）线程数：10000。

如下图4.17高负载时发布问题的TRT以及图4.17高负载时发布问题TPS所示，如同上述说明的登录、获取聊天信息时的高负载情况，TRT呈现上升的趋势，且在中间的某一时间段会有明显的变化；而对于TPS，成功的http请求会相对较陡，但是也能清晰看出中位数，失败的http请求同样在中间的时间段，数值变化幅度快速由低到高，在由高到低。
![图 4.17 高负载时发布问题的 TRT](https://img-blog.csdnimg.cn/78ce3c0f87214e6e930d540fe3cc9095.png)<center>图 4.17 高负载时发布问题的 TRT</center>
![图 4.18 高负载时发布问题的 TPS](https://img-blog.csdnimg.cn/ca445ad050e3459d8f970756c639195d.png)<center>图 4.18 高负载时发布问题的 TPS</center>

而在其聚合报告中表明，关于响应时间，最小为1999ms，最大为62701ms，平均为30037ms，中位数为29965ms；异常率为15.55%；吞吐量为141.7/s；接收和发送速率分别为87.90KB/s，38.56KB/s。
## 4.4 本章小结
本章通过测试并分析具有代表性的三个请求（登录、获取聊天信息、发布问题）的性能，受限于本地服务器的性能，仅在低、中负载的情况下表现良好，失败率均为0%，且平均响应时间均在30ms左右，最小响应时间可达到10ms以内；但在高负载的情况下便出现了异常，且在16%左右，且平均响应时间超过了20s甚至30s以上，最大响应时间可有1min以上，在高负载情况下严重影响用户整体体验，针对高负载时出现这些的问题也为今后的改进提供了一个方向。
# 第五章 总结与展望
## 5.1 总结
本论文设计与实现了一款基于SSM框架社交媒体，遵循RESTful API的软件架构风格、MVC框架，使得整体结构清晰明确；依靠Druid数据据库连接池高效强大的管理连接MySQL能力，性能得到较大提升；同时在前后端部分均对数据、请求进行严格验证，且拦截访问的实现，使得系统可靠性、安全性得到了提升。而基于此开发出来发主要功能有如下几项：

1）利用邮箱实现快速注册、忘记密码的验证，保证账户的安全；

2）可以查看用户（本人）的资料，而在修改本人资料时根据修改的部分进行提交，节约带宽；

3）可对好友进行添加、删除、修改昵称，同时可查看（非）好友的发布的随说、问题；

4）可以发布文字/图片/视频类型的随说，也可评论随说、回复评论；

5）用户发布文字类型的问题，可由全部用户查看、回答；

6）可对随说、随说的评论、问题、问题的回答、系统消息进行分页请求查看；

7）与好友进行聊天，同样支持聊天消息的分页查看，以及提供相对友好的聊天体验。
## 5.2 展望
本文所设计的系统仍有一些不足，在未来仍有很大的完善空间，譬如：

1）Spring一开始的理念是简化开发，但发展至今，已逐渐偏离该理念，配置变得越来越繁杂，因此后续会考虑将其换成更优秀的SpringBoot框架；

2）在聊天模块的接收聊天消息时，使用的是Ajax轮询的方式，每次的请求都是一次http(s)连接，如果在线人数一多，会给服务器造成的极大的压力，因此需要将其切换成WebSocket（一种基于TCP的全双工通信协议）以获得良好得到性能。同时相比于Ajax轮询，它的全双工通信实现服务端的消息推送，使得在后续开发中在有系统消息等其他重要消息时可以直接通知用户查看，而不是主动请求；

3）没有区分热数据以及冷数据的存储方案，从而影响系统的响应时间，后期会利用内存数据库，比如Redis（一个由C语言开发、可存储多种数据类型、高性能的非关系型内存数据库）对热数据进行建立缓存，使数据访问得到快速响应；

4）而在用户界面这方面，相对来说过于简单，不够灵动，需要持续优化，且没有根据屏幕大小进行界面的调整，而只是设置当窗口小于一定大小时增加滚动条；

5）目前的聊天只支持好友一对一的聊天，聊天方式不太灵活，后续会新增一对多的群聊模式；

6）问答模块问题的分页目前是根据发布时间来进行展示，且它是所有注册用户均可查看的，但是用户一多、问题一多，会导致很多以前发布的问题容易被忽略，因此需要优化问题的推送算法，可根据用户的兴趣等来进行推送；

7）问题的搜素是根据标题来进行全文模糊搜素，不支持关键字搜素、根据用户搜素等多种搜素方式，这也是一个需要改进的点。

# 源码
[https://gitee.com/zhz000/social_media_based_on_SSM](https://gitee.com/zhz000/social_media_based_on_SSM)
[https://github.com/zhz000/social_media_based_on_SSM](https://github.com/zhz000/social_media_based_on_SSM)
