<%-- 通过page指令来设置响应的内容类型是text/html，采用的字符集UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%-- 使用taglib指令引入核心标签库--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>编辑用户信息</title>
</head>
<body>
<h1>编辑用户信息</h1>
<hr />
<form action="${pageContext.request.contextPath}/modifying_user_information" method="post">
    <%--输出动态网页编辑课程信息（因为是修改编辑课程信息，所以课程编号为只读）。--%>
    <%--用EL表达式从请求域中取出用户编号--%>
        用户编号<input type="text" name="id" value="${user.id}" readonly /><br />
        名字<input type="text" name="name" value="${user.name}" readonly /><br />
        用户名<input type="text" name="username" value="${user.username}" /><br />
        密码<input type="text" name="password" value="${user.password}" /><br />
        <%--核心标签库中的choose标签--%>
        <%--test属性是必须的：test属性支持EL表达式。test属性值只能是boolean类型。--%>
        <%--相当于if  else if--%>
        <c:choose>
            <%--如果性别是男，则设置单选按钮默认值为男--%>
            <c:when test="${'MEN' == user.sex}">
                性别<input type="radio" name="sex" value="MEN" checked/>男
                <input type="radio" name="sex" value="WOMEN" />女<br />
            </c:when>
            <%--如果性别是女，则设置单选按钮默认值为女--%>
            <c:when test="${'WOMEN' == user.sex}">
                性别<input type="radio" name="sex" value="MEN" />男
                <input type="radio" name="sex" value="WOMEN" checked/>女<br />
            </c:when>
        </c:choose>
        <%--核心标签库中的choose标签--%>
        <%--test属性是必须的：test属性支持EL表达式。test属性值只能是boolean类型。--%>
        <%--相当于if  else if--%>
        <c:choose>
            <%--如果用户类型是企业员工用户，则设置单选按钮默认值为企业员工用户--%>
            <c:when test="${'Enterprise_employee' == user.user_type}">
                用户类型<input type="radio" name="user_type" value="Enterprise_employee" checked/>企业员工用户
                <input type="radio" name="user_type" value="Non-corporate_employee" />非企业员工用户<br />
            </c:when>
            <%--如果课程类型是非企业员工用户，则设置单选按钮默认值为非企业员工用户--%>
            <c:when test="${'Non-corporate_employee' == user.user_type}">
                用户类型<input type="radio" name="user_type" value="Enterprise_employee" />企业员工用户
                <input type="radio" name="user_type" value="Non-corporate_employee" checked/>非企业员工用户<br />
            </c:when>
        </c:choose>

    <input type="submit" value="修改用户基本信息" /><br />

</form>

</body>
</html>
