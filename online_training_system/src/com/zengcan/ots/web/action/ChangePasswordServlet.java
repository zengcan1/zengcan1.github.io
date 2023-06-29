package com.zengcan.ots.web.action;

import com.zengcan.ots.utils.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// 修改密码
// 使用Servlet注解，简化配置，完成Servlet类的注册（即，将请求路径和Servlet之间对应起来）
// 这个value属性和urlPatterns属性一致，都是用来指定Servlet的映射路径的。
// 如果注解的属性名是value的话，属性名也是可以省略的。
@WebServlet({"/change_password"})
public class ChangePasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 解决请求体的中文乱码问题。
        request.setCharacterEncoding("UTF-8");

        // 获取表单中的数据
        // 用户名
        String username = request.getParameter("username");
        // 密码
        String password = request.getParameter("password");
        // 修改前的密码
        String password1 = request.getParameter("password1");

        // 连接数据库执行更新语句
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            // 注册驱动（注册驱动只需要注册一次，放在我写的DBUtil数据库工具类的静态代码块当中。
            // DBUtil类加载的时候执行。）
            // 获取连接
            conn = DBUtil.getConnection();
            // 获取预编译的数据库操作对象
            String sql = "update t_user set username = ?, password = ? where (username = ? and password = ?)";
            ps = conn.prepareStatement(sql);
            // 为SQL语句中的所有 ？ 赋值。
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, username);
            ps.setString(4, password1);
            // 执行sql语句，并返回受影响的行数（即更新计数）
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            DBUtil.close(conn, ps, null);
        }

        if (count == 1) {
            //密码修改成功，跳转到修改密码成功页面
            // 这里最好使用重定向（浏览器会发一次全新的请求。）
            // 浏览器在地址栏上发送请求，这个请求是get请求。
            response.sendRedirect(request.getContextPath() + "/password_changed_successfully.jsp");
        }
    }
}
