<%-- 通过page指令来设置响应的内容类型是text/html，采用的字符集UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>首页</title>

    <!-- 设置页面图标或收藏夹图标-->
    <link rel="shortcut icon" href="favicon.ico">

    <!-- 通过style块 用样式块方式来设置元素的CSS样式 -->
    <style type="text/css">

        * {
            /* 设置所有的元素 宽度和高度会包括内边距和边框： */
            box-sizing: border-box;
        }

        .top {
            /* 设置边框宽度为5像素 类型为实线边框 边框颜色为道奇蓝 */
            border: 5px solid dodgerblue;
            border-radius: 8px;/* 为边框添加圆角，变为圆角边框 */
            margin: 1px;/* 设置四个外边距都是1像素 */
            height: 100px;/* 设置元素的高度和宽度 */
            width: auto;
            background-color: aliceblue;/* 设置图层背景为爱丽丝蓝 */
            padding: 0px 15px 5px 15px;/* 设置上右下左的内边距 */
        }

        .Main_content::after {
            content: "";
            clear: both;/* 设置该元素外，左侧或右侧均不允许浮动元素 */
            display: table;/* 此元素会作为块级表格来显示（类似 <table>），表格前后带有换行符。 */
        }

        /* 设置左侧功能列表图层的css样式 */
        .List_of_features {
            /* 设置边框宽度为5像素 类型为实线边框 边框颜色为道奇蓝 */
            border: 5px solid dodgerblue;
            border-radius: 8px;/* 为边框添加圆角，变为圆角边框 */
            margin: 0px;/* 设置四个外边距都是0像素 */

            float: left;/* 元素浮动到其容器的左侧 */
            padding: 15px;/* 所有 4 个内边距都是 15px */
            width: 25%;/* 设置元素的高度和宽度 */
            height: 300px;

            background-color: aliceblue;/* 设置图层背景为爱丽丝蓝 */

            overflow: auto;/* 如果一个元素比包含它的元素高，并且它是浮动的，它将“溢出”到其容器之外：
								然后我们可以向包含元素添加 overflow: auto;，来解决此问题 */
        }

        /* 设置右侧的详细内容图层的css样式 */
        .Detailed_content {
            /* 设置边框宽度为5像素 类型为实线边框 边框颜色为道奇蓝 */
            border: 5px solid dodgerblue;
            border-radius: 8px;/* 为边框添加圆角，变为圆角边框 */
            margin: 0px;/* 设置四个外边距都是0像素 */

            float: left;/* 元素浮动在其容器的右侧 */
            padding: 15px;/* 所有 4 个内边距都是 15px */
            width: 75%;/* 设置元素的宽度 */
            background-color: aliceblue;/* 设置图层背景为爱丽丝蓝 */

            overflow: auto;/* 如果一个元素比包含它的元素高，并且它是浮动的，它将“溢出”到其容器之外：
								然后我们可以向包含元素添加 overflow: auto;，来解决此问题,如果太长时添加滚动条。 */
        }

        /* 创建背景色为灰色的基础垂直导航栏 */
        ul {
            /* 导航条不需要列表项标记 */
            /* 从列表中删除项目符号以及外边距和内边距（填充） */
            list-style-type: none;
            margin: 0;
            padding: 0;
            width: 100%;/* 设置 <ul> 的宽度 */
            background-color: #f1f1f1;/* 背景色为灰色 */
            border: 1px solid #555; /* 在导航栏周围添加实线灰色边框*/

        }

        li {
            text-align: center; /* 设置列表项的文本居中*/
            border-bottom: 1px solid #555;
            /* 通过在列表项底部添加实现灰色边框，以实现在导航栏内添加边框*/
        }

        /* 请为所有 <li> 元素添加 border-bottom，最后一个元素除外 */
        li:last-child {
            border-bottom: none;
        }

        li a {
            /* 后代选择器设置无序列表中超链接<a>元素的css样式 */
            display: block; /* 将链接显示为块元素可以使整个链接区域都可以被单击（而不仅仅是文本）*/
            /* 当显示为块元素时，它们将占据<ul>可用的全部宽度 */
            color: #000;/* 设置颜色为黑色 */
            padding: 8px 16px;/* 设置上下，左右的内边距分别为8 和 16 */
            text-decoration: none;/* 设置添加到文本的修饰为 不添加 */
        }

        li a.active {
            /* 我们创建一个具有绿色背景色和白色文本的 "active" 类。该类将添加到 "欢迎页面" 链接。 */
            /* 向当前链接添加 "active" 类，以使用户知道他/她在哪个页面上 */
            background-color: #4CAF50;
            color: white;
        }

        /* 鼠标悬停时改变链接颜色  */
        li a:hover:not(.active) {/* 选择每个非active选择器的元素。 */
            background-color: #555;/* 设置背景色为灰色 */
            color: white;/* 设置文本的颜色为白色 */
        }

        /* 设置页脚的样式 */
        .footer {
            background-color: #1E90FF;/* 设置页脚的背景色为道奇蓝 */
            padding: 5px;
            text-align: center;/* 将文本排成直线并居中 */
            margin: 3px;/* 设置四个外边距都是3像素 */
        }

    </style>
</head>
<body>

<!-- 页眉,顶部的图层（大盒子，块级元素） -->
<div class="top">
    <h1>企业员工在线培训系统</h1>

</div>

<!-- 主内容图层 -->
<div class="Main_content">

    <!-- 左侧的功能列表图层 -->
    <div class="List_of_features">
        左侧的功能列表图层
        <!-- 用标准的HTML列表构建导航栏，这里导航栏就是链接列表 -->
        <ul>
            <!-- 由于链接的目标匹配 iframe 的名称，所以链接会在 iframe（内联框架） 中打开。 -->
                                        <%--在JSP中动态的获取应用的根路径。--%>
            <li><a class="active" href="<%=request.getContextPath()%>/welcome_page.jsp" target="iframe_welcome">欢迎页面</a></li>
            <!--前端超链接发送请求的时候，请求路径以“/”开始，并且要带着项目名。-->
            <li><a href="<%=request.getContextPath()%>/free_courses.jsp?" target="iframe_welcome">免费课程</a></li>
            <li><a href="<%=request.getContextPath()%>/personal_center.jsp" target="iframe_welcome">个人中心</a></li>

        </ul>

        <h3>欢迎${user.username}</h3>
        <a href="user/exit">[退出系统]</a>
    </div>

    <!-- 右侧的详细内容图层 -->
    <div class="Detailed_content">
        右侧的详细内容图层，由于电脑太卡，没有设置服务器及时更新大文件，当涉及视频文件的上传修改时，需要在IDEA手动更新Tomcat服务器数据（以Debug的方式重启服务器），方可正常观看上传或修改后的视频<br>
        <!-- iframe（内联框架）：定义内联的子窗口（框架），可以理解为窗口里的子窗口 -->
        <!-- 设置内联框架iframe及其高度和宽度。-->
        <iframe src="<%=request.getContextPath()%>/welcome_page.jsp" name="iframe_welcome" width="100%" height="400px"></iframe>
        <br>

    </div>
</div>

<div class="footer">
    20199403 软件工程二班 曾璨
</div>

</body>
</html>
