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

//使用模板方法设计模式优化项目，解决类爆炸问题（类的数量太大）
//- 以前的设计是一个请求一个Servlet类。1000个请求对应1000个Servlet类。导致类爆炸。
//- 可以这样做：一个请求对应一个方法。一个业务对应一个Servlet类。

// 模板类
// 使用Servlet注解，简化配置，完成Servlet类的注册（即，将请求路径和Servlet之间对应起来）
// 这个value属性和urlPatterns属性一致，都是用来指定Servlet的映射路径的。
// 如果注解的属性名是value的话，属性名也是可以省略的。
// Servlet负责业务的处理
// JSP负责页面的展示。
@WebServlet({"/user/login", "/user/exit"})
public class UserServlet extends HttpServlet {

    // 模板方法
    // 重写service方法（并没有重写doGet或者doPost）
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取servlet path(也就是一般web.xml文件中与“url-pattern”中匹配的路径)
        String servletPath = request.getServletPath();

        if("/user/login".equals(servletPath)){ // 如果请求匹配的路径是  用户登录  则执行用户登录对应的方法
            doLogin(request, response);
        }else if("/user/exit".equals(servletPath)){ // 如果请求匹配的路径是  退出系统  则执行退出系统对应的方法
            doExit(request, response);
        }
    }

    protected void doLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 默认登录失败
        boolean success = false;
        // 这里要做的事情是，验证用户名和密码是否正确，以及用户类型是否正确。
        // 获取用户名、密码和用户类型
        // 前端是这样提交的：username=%E6%9B%BE%E7%92%A8&password=2799393105&user_type=System_administrator&f=1
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String user_type = request.getParameter("user_type");

        // 连接数据库验证用户名和密码
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 注册驱动（注册驱动只需要注册一次，放在我写的DBUtil数据库工具类的静态代码块当中。
            // DBUtil类加载的时候执行。）
            // 获取连接
            conn = DBUtil.getConnection();
            // 获取预编译的数据库操作对象
            String sql = "select * from t_user where(username = ? and password = ? and user_type = ?)";
            // 编译SQL
            ps = conn.prepareStatement(sql);
            // 给所有的？赋值
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, user_type);
            // 执行SQL
            rs = ps.executeQuery();
            // 这个结果集当中最多只有一条数据。
            if (rs.next()) { // 不需要while循环
                // 如果查出来有数据
                // 则表名登录成功
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }

        // 登录成功/失败
        if (success) {
            // 获取session对象（这里的要求是：必须获取到session，没有session也要新建一个session对象。）
            HttpSession session = request.getSession(); // session对象一定不是null
            //session.setAttribute("username", username);

            User user = new User(username, password, user_type);
            session.setAttribute("user", user);

            // 登录成功了，并且用户确实选择了“十天内免登录”功能。
            String f = request.getParameter("f");
            if("1".equals(f)){
                // 创建Cookie对象存储登录名
                Cookie cookie1 = new Cookie("username", username);
                // 创建Cookie对象存储密码
                Cookie cookie2 = new Cookie("password", password); // 真实情况下是加密的。
                // 创建Cookie对象存储用户类型
                Cookie cookie3 = new Cookie("user_type", user_type);
                // 设置Cookie的有效期为十天
                cookie1.setMaxAge(60 * 60 * 24 * 10);
                cookie2.setMaxAge(60 * 60 * 24 * 10);
                cookie3.setMaxAge(60 * 60 * 24 * 10);
                // 设置cookie的path（只要访问这个应用，浏览器就一定要携带这两个cookie）
                cookie1.setPath(request.getContextPath());
                cookie2.setPath(request.getContextPath());
                cookie3.setPath(request.getContextPath());
                // 响应cookie给浏览器
                response.addCookie(cookie1);
                response.addCookie(cookie2);
                response.addCookie(cookie3);
            }

            // 成功，跳转到企业员工在线培训系统首页
            response.sendRedirect(request.getContextPath() + "/home_page.jsp");
        } else {
            // 失败，跳转到失败页面
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }

    }

    protected void doExit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取session对象，销毁session
        HttpSession session = request.getSession(false);
        if (session != null) {

            // 从session域中删除user对象
            session.removeAttribute("user");

            // 手动销毁session对象。
            session.invalidate();

            // 销毁cookie(退出系统将所有的cookie全部销毁)
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    // 设置cookie的有效期为0，表示删除该cookie
                    cookie.setMaxAge(0);
                    // 设置一下cookie的路径
                    cookie.setPath(request.getContextPath()); // 删除cookie的时候注意路径问题。
                    // 响应cookie给浏览器，浏览器端会将之前的cookie覆盖。
                    response.addCookie(cookie);
                }
            }

            // 跳转到登录页面
            response.sendRedirect(request.getContextPath());
        }

    }


}
