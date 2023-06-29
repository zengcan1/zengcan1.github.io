<%-- 通过page指令来设置响应的内容类型是text/html，采用的字符集UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>登录失败</title>
</head>
<body>
登录失败，请<a href="<%=request.getContextPath()%>/index.jsp">重新登录</a>
</body>
</html>
