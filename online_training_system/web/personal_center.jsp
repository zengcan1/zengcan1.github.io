<%-- 通过page指令来设置响应的内容类型是text/html，采用的字符集UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%-- 使用taglib指令引入核心标签库--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>个人中心</title>
    <style type="text/css">
        ul {
            /* 设置导航栏 */
            /* 从列表中删除项目符号以及外边距和内边距（填充） */
            list-style-type: none;
            margin: 0;
            padding: 0;
            overflow: hidden;/* overflow:hidden 被添加到 ul 元素，以防止 li 元素超出列表。 */
            background-color: #333;/* 添加全宽的暗度灰色背景色 */
            /* 将导航栏固定在顶部 */
            position: fixed;
            top: 0;
            width: 100%;
        }

        /* 设置水平导航栏 */
        /* 通过浮动<li>元素创建水平导航栏，并为导航链接规定布局 */
        li {
            float: left;/* 使用 float 使块元素滑动为彼此相邻 */
        }

        li a {
            display: block;/* 将链接显示为块元素可以使整个链接区域都可单击（不仅是文本） */
            color: white;/* 添加文本颜色为白色 */
            text-align: center; /* 将文本排成直线并居中*/
            padding: 12px 16px; /* 添加内边距（填充）*/
            text-decoration: none;/* 设置添加到文本的修饰为 不添加 */

        }

        /* 当鼠标悬停时把链接颜色更改为 #111（黑色） */
        li a:hover:not(.active) {/* 选择每个非active选择器的元素。 */
            background-color: #111;
        }

        .active {
            /* 向当前链接添加 "active" 类，这样用户就知道他/她在哪个页面上 */
            background-color: #1E90FF;/* 设置背景颜色为道奇蓝 */
        }

        /* 为所有列表项添加灰色右边框*/
        li {
            border-right: 1px solid #bbb;
        }

        /* 设置下方的详细内容图层的css样式 */
        .Detailed_content {
            border-style: none;/* 定义无边框 */
            margin: 0px;/* 设置四个外边距都是0像素 */

            float: left;/* 元素浮动在其容器的右侧 */
            padding: 12px 0px 0px 0px;/* 除了上内边距是15px，所有其他面 内边距都是 0px */
            width: 100%;/* 设置元素的宽度 */
            background-color: aliceblue;/* 设置图层背景为爱丽丝蓝 */

            overflow: auto;/* 如果一个元素比包含它的元素高，并且它是浮动的，它将“溢出”到其容器之外：
								然后我们可以向包含元素添加 overflow: auto;，来解决此问题,如果太长时添加滚动条。 */
        }

    </style>
</head>
<body>
个人中心包括：我的课程和课程管理
<ul>

    <%--核心标签库中的choose标签--%>
    <%--test属性是必须的：test属性支持EL表达式。test属性值只能是boolean类型。--%>
    <%--相当于if  else if--%>
    <c:choose>
        <%--如果用户类型是 System_administrator（系统管理员），则展示的功能页面是“我的课程”、“课程管理”、“修改密码”、员工信息管理--%>
        <c:when test="${'System_administrator' == user.user_type}">
            <!-- 由于链接的目标匹配 iframe 的名称，所以链接会在下方 iframe（内联框架） 中打开。 -->
            <li><a class="active" href="<%=request.getContextPath()%>/classes?course_type=ENTERPRISE_COURSE" target="iframe_Personal">我的课程</a></li>
            <%--在JSP中动态的获取应用的根路径。--%>
            <li><a href="<%=request.getContextPath()%>/course_management" target="iframe_Personal">课程管理</a></li>
            <li><a href="<%=request.getContextPath()%>/change_the_password.jsp" target="iframe_Personal">修改密码</a></li>
            <li><a href="<%=request.getContextPath()%>/employee_information_management" target="iframe_Personal">员工信息管理</a></li>
        </c:when>
        <%--如果用户类型是 Enterprise_administrator（企业管理员），则展示的功能页面是“我的课程”、“课程管理”、“修改密码”、员工信息管理--%>
        <c:when test="${'Enterprise_administrator' == user.user_type}">
            <!-- 由于链接的目标匹配 iframe 的名称，所以链接会在下方 iframe（内联框架） 中打开。 -->
            <li><a class="active" href="<%=request.getContextPath()%>/classes?course_type=ENTERPRISE_COURSE" target="iframe_Personal">我的课程</a></li>
            <%--在JSP中动态的获取应用的根路径。--%>
            <li><a href="<%=request.getContextPath()%>/course_management" target="iframe_Personal">课程管理</a></li>
            <li><a href="<%=request.getContextPath()%>/change_the_password.jsp" target="iframe_Personal">修改密码</a></li>
            <li><a href="<%=request.getContextPath()%>/employee_information_management" target="iframe_Personal">员工信息管理</a></li>
        </c:when>
        <%--如果用户类型是 Enterprise_employee（企业员工），则展示的功能页面是“我的课程”、“修改密码”--%>
        <c:when test="${'Enterprise_employee' == user.user_type}">
            <!-- 由于链接的目标匹配 iframe 的名称，所以链接会在下方 iframe（内联框架） 中打开。 -->
            <li><a class="active" href="<%=request.getContextPath()%>/classes?course_type=ENTERPRISE_COURSE" target="iframe_Personal">我的课程</a></li>
            <%--在JSP中动态的获取应用的根路径。--%>
            <li><a href="<%=request.getContextPath()%>/change_the_password.jsp" target="iframe_Personal">修改密码</a></li>
        </c:when>
        <%--如果用户类型是 Non-corporate_employee（非企业员工），则展示的功能页面是“我的课程”、“修改密码”--%>
        <c:when test="${'Non-corporate_employee' == user.user_type}">
            <!-- 由于链接的目标匹配 iframe 的名称，所以链接会在下方 iframe（内联框架） 中打开。 -->
            <li><a class="active" href="<%=request.getContextPath()%>/classes?course_type=OPEN_COURSE" target="iframe_Personal">我的课程</a></li>
            <%--在JSP中动态的获取应用的根路径。--%>
            <li><a href="<%=request.getContextPath()%>/change_the_password.jsp" target="iframe_Personal">修改密码</a></li>
        </c:when>
    </c:choose>
</ul>

<!-- 下方的详细内容图层 -->
<div class="Detailed_content">

    <%--核心标签库中的choose标签--%>
    <%--test属性是必须的：test属性支持EL表达式。test属性值只能是boolean类型。--%>
    <%--相当于if  else if--%>
    <c:choose>
        <%--如果用户类型是 System_administrator（系统管理员）--%>
        <c:when test="${'System_administrator' == user.user_type}">
            <!-- iframe（内联框架）：定义内联的子窗口（框架），可以理解为窗口里的子窗口 -->
            <!-- 设置内联框架iframe及其高度和宽度。并移除边框-->
            <iframe src="<%=request.getContextPath()%>/classes?course_type=ENTERPRISE_COURSE" name="iframe_Personal" width="100%" height="350px" frameborder="0"></iframe>
            <br>
        </c:when>
        <%--如果用户类型是 Enterprise_administrator（企业管理员）--%>
        <c:when test="${'Enterprise_administrator' == user.user_type}">
            <!-- iframe（内联框架）：定义内联的子窗口（框架），可以理解为窗口里的子窗口 -->
            <!-- 设置内联框架iframe及其高度和宽度。并移除边框-->
            <iframe src="<%=request.getContextPath()%>/classes?course_type=ENTERPRISE_COURSE" name="iframe_Personal" width="100%" height="350px" frameborder="0"></iframe>
            <br>
        </c:when>
        <%--如果用户类型是 Enterprise_employee（企业员工）--%>
        <c:when test="${'Enterprise_employee' == user.user_type}">
            <!-- iframe（内联框架）：定义内联的子窗口（框架），可以理解为窗口里的子窗口 -->
            <!-- 设置内联框架iframe及其高度和宽度。并移除边框-->
            <iframe src="<%=request.getContextPath()%>/classes?course_type=ENTERPRISE_COURSE" name="iframe_Personal" width="100%" height="350px" frameborder="0"></iframe>
            <br>
        </c:when>
        <%--如果用户类型是 Non-corporate_employee（非企业员工）--%>
        <c:when test="${'Non-corporate_employee' == user.user_type}">
            <!-- iframe（内联框架）：定义内联的子窗口（框架），可以理解为窗口里的子窗口 -->
            <!-- 设置内联框架iframe及其高度和宽度。并移除边框-->
            <iframe src="<%=request.getContextPath()%>/classes?course_type=OPEN_COURSE" name="iframe_Personal" width="100%" height="350px" frameborder="0"></iframe>
            <br>
        </c:when>
    </c:choose>

</div>

</body>
</html>
