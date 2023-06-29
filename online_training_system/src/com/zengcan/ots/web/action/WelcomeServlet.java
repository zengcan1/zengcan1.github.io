package com.zengcan.ots.web.action;

import com.zengcan.ots.bean.User;
import com.zengcan.ots.utils.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取cookie
        // 这个Cookie[]数组可能是null，如果不是null，数组的长度一定是大于0的。
        Cookie[] cookies = request.getCookies();
        String username = null;
        String password = null;
        String user_type = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if("username".equals(name)){
                    username = cookie.getValue();
                }else if("password".equals(name)){
                    password = cookie.getValue();
                }else if("user_type".equals(name)){
                    user_type = cookie.getValue();
                }
            }
        }

        // 要在这里使用username、password和user_type
        if(username != null && password != null && user_type != null){
            // 验证用户名、密码和用户类型是否正确
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            boolean success = false;
            try {
                conn = DBUtil.getConnection();
                String sql = "select * from t_user where(username = ? and password = ? and user_type = ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, user_type);
                rs = ps.executeQuery();
                if (rs.next()) {
                    // 登录成功
                    success = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn, ps, rs);
            }

            if (success) {
                // 获取session对象（这里的要求是：必须获取到session，没有session也要新建一个session对象。）
                HttpSession session = request.getSession(); // session对象一定不是null
                //session.setAttribute("username", username);

                User user = new User(username, password, user_type);
                session.setAttribute("user", user);

                // 正确，表示登录成功
                response.sendRedirect(request.getContextPath() + "/home_page.jsp");
            }else{
                // 错误，表示登录失败
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }

        }else{
            // 跳转到登录页面
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }


    }
}
