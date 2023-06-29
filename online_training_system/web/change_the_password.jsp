<%-- 通过page指令来设置响应的内容类型是text/html，采用的字符集UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%-- 使用taglib指令引入核心标签库--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>修改密码</title>
</head>
<body>
<h1>编辑新密码</h1>
<hr />
<form action="${pageContext.request.contextPath}/change_password" method="post">
    <%--输出动态网页编辑课程信息（因为是修改编辑课程信息，所以课程编号为只读）。--%>
    <%--用EL表达式从会话域中取出用户名--%>
    用户名<input type="text" name="username" value="${user.username}" readonly /><br />
    密码<input type="text" name="password1" value="${user.password}" readonly />（修改前）<br />
    密码<input type="text" name="password" value="${user.password}" />（修改后）<br />
    <input type="submit" value="修改密码" /><br />
</form>

</body>
</html>
