<%-- 通过page指令来设置响应的内容类型是text/html，采用的字符集UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%--访问jsp的时候不生成session对象。--%>
<%@page session="false"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>欢迎使用企业员工在线培训系统</title>
</head>

<body>

    <h1>Login page 登录页面</h1>
    <hr />
    <form action="/ots/user/login" method="post">
        username: <input type="text" name="username" /><br />
        password: <input type="password" name="password" /><br />
        user_type用户类型:
        <select name="user_type"><%--用下拉列表选择 用户类型选项--%>
            <option value="System_administrator">系统管理员</option>
            <option value="Enterprise_administrator">企业管理员</option>
            <option value="Enterprise_employee">企业员工</option>
            <option value="Non-corporate_employee">非企业员工</option>
        </select><br />
        <%--用复选框来设置十天内免登录--%>
        <input type="checkbox" name="f" value="1">十天内免登录<br>
        <input type="submit" value="login">

    </form>

</body>
</html>
