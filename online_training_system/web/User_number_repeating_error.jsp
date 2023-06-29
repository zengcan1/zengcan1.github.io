<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2023/5/26
  Time: 0:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%--在JSP中动态的获取应用的根路径。--%>
<a href="<%=request.getContextPath()%>/add_user.jsp">用户编号重复，请重新上传并使用新的课程编号</a>
</body>
</html>
