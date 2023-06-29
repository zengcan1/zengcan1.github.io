<%-- 通过page指令来设置响应的内容类型是text/html，采用的字符集UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<%--在JSP中动态的获取应用的根路径。--%>
<a href="<%=request.getContextPath()%>/add_course.jsp">课程编号重复，请重新上传并使用新的课程编号</a>
</body>
</html>
