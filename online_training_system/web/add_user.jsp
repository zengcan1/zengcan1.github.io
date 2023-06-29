<%-- 通过page指令来设置响应的内容类型是text/html，采用的字符集UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%-- 使用taglib指令引入核心标签库--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>添加用户</title>
</head>
<body>

<h3>上传用户的基本信息</h3>
<%--EL表达式获取应用的根路径--%>
<form action="${pageContext.request.contextPath}/upload_user_information" method="post">
    用户编号<input type="text" name="id" /><br />
    名字<input type="text" name="name" /><br />
    用户名<input type="text" name="username" /><br />
    密码<input type="text" name="password" /><br />
    性别<input type="radio" name="sex" value="MEN" checked/>男
    <input type="radio" name="sex" value="WOMEN" />女<br />
    用户类型<input type="radio" name="user_type" value="Enterprise_employee" checked/>企业员工用户
    <input type="radio" name="user_type" value="Non-corporate_employee" />非企业员工用户<br />

    <input type="submit" value="上传" />

</form>
</body>
</html>

