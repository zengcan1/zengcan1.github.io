package com.zengcan.ots.web.action;

/*import com.zengcan.ots.utils.DBUtil;*/
import com.zengcan.ots.utils.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//查询课程基本信息(只查询所有可公开课程，或，只查询所有的企业课程)
// 使用Servlet注解，简化配置，完成Servlet类的注册（即，将请求路径和Servlet之间对应起来）
// 这个value属性和urlPatterns属性一致，都是用来指定Servlet的映射路径的。
// 如果注解的属性名是value的话，属性名也是可以省略的。
@WebServlet({"/classes"})
public class OpenEnterpriseClassesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取应用的根路径
        String contextPath = request.getContextPath();


        // 设置响应的内容类型以及字符集，防止中文乱码问题。
        response.setContentType("text/html;charset=UTF-8");

        // 获取标准的输出流，输出到浏览器
        PrintWriter out = response.getWriter();
        out.print("<!DOCTYPE html>");
        out.print("<html>");
        out.print("<head>");
        out.print("    <meta charset='utf-8'>");
        out.print("    <title>公开课</title>");
        out.print("");
        out.print("    <!-- 通过style块 用样式块方式来设置元素的CSS样式 -->");
        out.print("    <style type='text/css'>");
        out.print("        /* 在 CSS 中设置表格边框 */");
        out.print("        table, th, td {");
        out.print("            border: 1px solid black;");
        out.print("        }");
        out.print("        /* 设置覆盖整个子页面（全宽）的表格 */");
        out.print("        table {");
        out.print("            width: 100%;");
        out.print("        }");
        out.print("        /* border-collapse 属性设置是否将表格边框折叠为单一边框 */");
        out.print("        table {");
        out.print("            border-collapse: collapse;");
        out.print("        }");
        out.print("        /* 设置 <th> 元素的高度，背景颜色和文本颜色： */");
        out.print("        th {");
        out.print("            height: 50px;");
        out.print("            background-color: #1E90FF;/* 设置图层背景为爱丽丝蓝 */");
        out.print("            color: white;");
        out.print("        }");
        out.print("        /* 设置td的高度，使 <td> 元素的内容也居中对齐 */");
        out.print("        td {");
        out.print("            height: 50px;");
        out.print("            text-align: center;");
        out.print("        }");
        out.print("    </style>");
        out.print("</head>");
        out.print("<body>");
        out.print("                ");
        out.print("<table>");
        out.print("    <tr>");
        out.print("        <th>课程编号</th>");
        out.print("        <th>课程名称</th>");
        out.print("        <th>课程类型</th>");
        out.print("        <th>开课时间</th>");
        out.print("        <th>课程内容</th>");
        out.print("    </tr>");
        /* 上面一部分是死的 */

        String course_type1 = request.getParameter("course_type");

        // 连接数据库，查询课程基本信息。
        // 为了简化开发，我为连接数据库写了一个工具类DBUtil
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 注册驱动（注册驱动只需要注册一次，放在我写的DBUtil数据库工具类的静态代码块当中。
            // DBUtil类加载的时候执行。）
            // 获取连接
            conn = DBUtil.getConnection();
            // 获取预编译的数据库操作对象.
            String sql = "select course_number,course_name,course_type,class_start_time,file_path from t_course where course_type = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, course_type1);
            // 执行SQL语句
            rs = ps.executeQuery();
            // 处理结果集
            while(rs.next()){//如果结果集中有数据，则将数据取出
                // 取出课程编号
                String course_number = rs.getString("course_number");
                // 取出课程名称
                String course_name = rs.getString("course_name");
                // 取出课程类型
                String course_type = rs.getString("course_type");
                // 取出开课时间
                String class_start_time = rs.getString("class_start_time");

                out.print("    <tr>");
                out.print("        <td>"+course_number+"</td>");
                out.print("        <td>"+course_name+"</td>");
                out.print("        <td>"+course_type+"</td>");
                out.print("        <td>"+class_start_time+"</td>");
                out.print("        <td><a href='/ots/play/video?name="+course_number+"'>开始学习</a></td>");
                out.print("    </tr>");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            // 在工具类中会分别进行判断 连接对象、数据库操作对象、查询结果集对象是否为空
            // 如果不为空，则会关闭流，并try catch处理SQL异常。
            DBUtil.close(conn, ps, rs);
        }


        /* 下面一部分是死的*/
        out.print("</table>");
        out.print("</body>");
        out.print("</html>");

    }
}
