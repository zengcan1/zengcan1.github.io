<%-- 通过page指令来设置响应的内容类型是text/html，采用的字符集UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%-- 使用taglib指令引入核心标签库--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>课程管理页面</title>
        <%--设置整个网页的基础路径是：http://localhost:8080/ots/ --%>
        <%--base标签只对路径前没有/的起作用--%>
        <%--<base href="http://localhost:8080/ots/">--%>
        <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">

    </head>
    <body>

        <script type="text/javascript">
            del = function (course_number, course_name){
                // 弹出确认框，用户点击确定，返回true，点击取消返回false
                if(window.confirm("亲，删了不可恢复哦！")){
                    /*注意html的base标签可能对JS代码不起作用。所以JS代码最后前面写上"/oa" */
                    /*EL表达式获取应用的根路径*/
                    document.location.href = "${pageContext.request.contextPath}/delete_a_course?course_number="+course_number+"&course_name="+course_name;
                }
            }
        </script>

        <h1 align="center">课程列表</h1>
        <hr />
        <table border="1px" align="center" width="80%">
            <tr>
                <th>课程编号</th>
                <th>课程名称</th>
                <th>课程类型</th>
                <th>操作</th>
            </tr>

            <%--使用core标签库中forEach标签。对List集合进行遍历--%>
            <%--EL表达式只能从域中取数据。--%>
            <%--var后面的名字是随意的。var属性代表的是集合中的每一个元素。--%>
            <c:forEach items="${courseList}" var="course">
                <tr>
                    <%--从请求域中取出课程编号--%>
                    <td>${course.course_number}</td>
                    <%--从请求域中取出课程名称--%>
                    <td>${course.course_name}</td>
                    <%--从请求域中取出课程类型--%>
                    <td>${course.course_type}</td>
                    <td>
                        <a href="javascript:void(0)" onclick="del(${course.course_number},'${course.course_name}')">删除课程</a>
                        <%--因为上面使用了base标签，会在没有/开头的路径前自动加 基础路径：http://localhost:8080/ots/--%>
                        <a href="query_course?f=edit_course_information&course_number=${course.course_number}">修改课程</a>
                        <a href="query_course?f=query_course&course_number=${course.course_number}">查询课程</a>
                    </td>
                </tr>

            </c:forEach>

        </table>
        <hr />
        <a href="add_course.jsp">添加课程</a>
    </body>
</html>
