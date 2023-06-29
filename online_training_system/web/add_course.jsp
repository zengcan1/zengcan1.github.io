<%-- 通过page指令来设置响应的内容类型是text/html，采用的字符集UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%-- 使用taglib指令引入核心标签库--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>添加课程</title>
</head>
<body>

<h3>上传课程的基本信息</h3>
            <%--EL表达式获取应用的根路径--%>
<form action="${pageContext.request.contextPath}/upload_course_information" method="post">
    课程编号<input type="text" name="course_number" /><br />
    课程名称<input type="text" name="course_name" />课程名称应该与后续上传的课程视频文件名称一致（不包括文件后缀）<br />
    课程类型<input type="radio" name="course_type" value="ENTERPRISE_COURSE" checked/>企业课程
    <input type="radio" name="course_type" value="OPEN_COURSE" />公开课程<br />
    开课时间<input type="text" name="class_start_time" />（如：2023-06-08 至 2023-09-30）<br />
    <input type="submit" value="上传" />
</form>

<%--接收重定向传过来的值,/add_a_course?upload_successfully=1--%>
<%--如果课程的基本信息上传成功（upload_successfully == 1）--%>
<%--则展现：文件上传--%>
<%--核心标签库中的if标签--%>
<%--test属性是必须的：test属性支持EL表达式。test属性值只能是boolean类型。--%>
<c:if test="${1 == param.upload_successfully}">
    <h4>课程基本信息上传成功！请继续上传视频文件。</h4>

    <h3>文件上传：</h3>
    请选择要上传的文件：<br />
    <!-- 设置提交表单的URL。 -->
    <!-- 提交表单的方式为 post -->
    <!-- 指定要发送到服务器的元素数据的MIME类型 为multipart/form-data媒体类型（用于提交包含文件、非ascii数据和二进制数据的表单。） -->
    <!-- MIME（描述消息内容类型的标准，用来表示文档、文件或字节流的性质和格式。） -->
    <form action="${pageContext.request.contextPath}/file_upload" method="post" enctype="multipart/form-data">
        <input type="file" name="file" size="50" />
        <br />
        <input type="submit" value="上传文件" />
    </form>
    </body>
    </html>

</c:if>
