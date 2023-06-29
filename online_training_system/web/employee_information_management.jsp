<%-- 通过page指令来设置响应的内容类型是text/html，采用的字符集UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<%-- 使用taglib指令引入核心标签库--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>员工信息管理页面</title>
    <%--设置整个网页的基础路径是：http://localhost:8080/ots/ --%>
    <%--base标签只对路径前没有/的起作用--%>
    <%--<base href="http://localhost:8080/ots/">--%>
    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">

</head>
<body>

<script type="text/javascript">
    del = function (id){
        // 弹出确认框，用户点击确定，返回true，点击取消返回false
        if(window.confirm("亲，删了不可恢复哦！")){
            /*注意html的base标签可能对JS代码不起作用。所以JS代码最后前面写上"/oa" */
            /*EL表达式获取应用的根路径*/
            document.location.href = "${pageContext.request.contextPath}/delete_a_user?id="+id;
        }
    }
</script>

<h1 align="center">员工列表</h1>
<hr />
<table border="1px" align="center" width="80%">
    <tr>
        <th>用户编号</th>
        <th>名字</th>
        <th>性别</th>
        <th>操作</th>
    </tr>

    <%--使用core标签库中forEach标签。对List集合进行遍历--%>
    <%--EL表达式只能从域中取数据。--%>
    <%--var后面的名字是随意的。var属性代表的是集合中的每一个元素。--%>
    <c:forEach items="${userList}" var="user">
        <tr>
                <%--从请求域中取出用户编号--%>
            <td>${user.id}</td>
                <%--从请求域中取出名字--%>
            <td>${user.name}</td>
                <%--从请求域中取出性别--%>
            <td>${user.sex}</td>
            <td>
                <a href="javascript:void(0)" onclick="del(${user.id})">删除用户</a>
                    <%--因为上面使用了base标签，会在没有/开头的路径前自动加 基础路径：http://localhost:8080/ots/--%>
                <a href="query_user?f=edit_user_information&id=${user.id}">修改员工信息</a>
                <a href="query_user?f=query_user&id=${user.id}">查询员工信息</a>
            </td>
        </tr>

    </c:forEach>

</table>
<hr />
<a href="add_user.jsp">添加用户</a>
</body>
</html>
