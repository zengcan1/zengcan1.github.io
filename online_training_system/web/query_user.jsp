<%-- 通过page指令来设置响应的内容类型是text/html，采用的字符集UTF-8--%>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>查询用户基本信息</title>

    <!-- 通过style块 用样式块方式来设置元素的CSS样式 -->
    <style type="text/css">
        /* 在 CSS 中设置表格边框 */
        table, th, td {
            border: 1px solid black;
        }
        /* 设置覆盖整个子页面（全宽）的表格 */
        table {
            width: 100%;
        }
        /* border-collapse 属性设置是否将表格边框折叠为单一边框 */
        table {
            border-collapse: collapse;
        }
        /* 设置 <th> 元素的高度，背景颜色和文本颜色： */
        th {
            height: 50px;
            background-color: #1E90FF;/* 设置图层背景为爱丽丝蓝 */
            color: white;
        }
        /* 设置td的高度，使 <td> 元素的内容也居中对齐 */
        td {
            height: 50px;
            text-align: center;
        }
    </style>
</head>
<body>
<table>
    <tr>
        <th>用户编号</th>
        <th>名字</th>
        <th>用户名</th>
        <th>密码</th>
        <th>性别</th>
        <th>用户类型</th>
    </tr>
    <%--上面一部分是死的 --%>

    <%--输出动态网页。--%>
    <tr>
        <%--通过EL表达式--%>
        <%--从请求域中获取用户编号--%>
        <td>${user.id}</td>
        <%--从请求域中获取名字--%>
        <td>${user.name}</td>
        <%--从请求域中获取用户名--%>
        <td>${user.username}</td>
        <%--从请求域中获取密码--%>
        <td>${user.password}</td>
        <%--从请求域中获取性别--%>
        <td>${user.sex}</td>
        <%--从请求域中获取用户类型--%>
        <td>${user.user_type}</td>
    </tr>


</table>
</body>
</html>