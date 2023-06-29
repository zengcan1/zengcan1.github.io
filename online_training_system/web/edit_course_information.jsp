<%-- 通过page指令来设置响应的内容类型是text/html，采用的字符集UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%-- 使用taglib指令引入核心标签库--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
	<head>
        <meta charset="utf-8">
        <title>编辑课程信息</title>
    </head>
	<body>
		<h1>编辑课程信息</h1>
		<hr />
		<form action="${pageContext.request.contextPath}/modify_the_course" method="post">
            <%--输出动态网页编辑课程信息（因为是修改编辑课程信息，所以课程编号为只读）。--%>
            <%--用EL表达式从请求域中取出课程编号--%>
            课程编号<input type="text" name="course_number" value="${course.course_number}" readonly /><br />
            课程名称<input type="text" name="course_name1" value="${course.course_name}" readonly />（修改前）<br />
            课程名称<input type="text" name="course_name" value="${course.course_name}" />（修改后）<br />

            <%--核心标签库中的choose标签--%>
            <%--test属性是必须的：test属性支持EL表达式。test属性值只能是boolean类型。--%>
            <%--如果课程类型是企业课程，则设置单选按钮默认值为企业课程，反之则设置为公开课程--%>
            <%--相当于if  else if--%>
            <c:choose>
                <%--如果课程类型是企业课程，则设置单选按钮默认值为企业课程--%>
                <c:when test="${'ENTERPRISE_COURSE' == course.course_type}">
                    课程类型<input type="radio" name="course_type" value="ENTERPRISE_COURSE" checked/>企业课程
                    <input type="radio" name="course_type" value="OPEN_COURSE" />公开课程<br />
                </c:when>
                <%--如果课程类型是公开课程，则设置单选按钮默认值为公开课程--%>
                <c:when test="${'OPEN_COURSE' == course.course_type}">
                    课程类型<input type="radio" name="course_type" value="ENTERPRISE_COURSE" />企业课程
                    <input type="radio" name="course_type" value="OPEN_COURSE" checked/>公开课程<br />
                </c:when>
            </c:choose>

            开课时间<input type="text" name="class_start_time" value="${course.class_start_time}" /><br />
            <input type="submit" value="修改课程基本信息" /><br />
        </form>

	</body>
</html>
