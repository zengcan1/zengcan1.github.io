package com.zengcan.ots.web.action;

import com.zengcan.ots.bean.Course;
import com.zengcan.ots.bean.User;
import com.zengcan.ots.utils.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//使用模板方法设计模式优化项目，解决类爆炸问题（类的数量太大）
//- 以前的设计是一个请求一个Servlet类。1000个请求对应1000个Servlet类。导致类爆炸。
//- 可以这样做：一个请求对应一个方法。一个业务对应一个Servlet类。

// 模板类
// 使用Servlet注解，简化配置，完成Servlet类的注册（即，将请求路径和Servlet之间对应起来）
// 这个value属性和urlPatterns属性一致，都是用来指定Servlet的映射路径的。
// 如果注解的属性名是value的话，属性名也是可以省略的。
@WebServlet({"/employee_information_management",
        "/upload_user_information",
        "/delete_a_user",
        "/modifying_user_information",
        "/query_user"})
public class UserManagementServlet extends HttpServlet {

    // 模板方法
    // 重写service方法（并没有重写doGet或者doPost）
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 获取servlet path(也就是一般web.xml文件中与“url-pattern”中匹配的路径)
        String servletPath = request.getServletPath();

        if("/employee_information_management".equals(servletPath)){ // 如果请求匹配的路径是  员工信息管理  则执行员工信息管理对应的方法
            doEmployeeInformationManagement(request, response);
        } else if("/upload_user_information".equals(servletPath)){ // 如果请求匹配的路径是  上传用户信息  则执行上传用户信息对应的方法
            doUploadUserInformation(request, response);
        } else if("/delete_a_user".equals(servletPath)){ // 如果请求匹配的路径是  删除用户  则执行删除用户对应的方法
            doDeleteUser(request, response);
        } else if("/modifying_user_information".equals(servletPath)){ // 如果请求匹配的路径是  修改用户基本信息  则执行修改用户基本信息对应的方法
            doModifyingUserInformation(request, response);
        } else if("/query_user".equals(servletPath)){ // 如果请求匹配的路径是  查询用户基本信息  则执行查询用户基本信息对应的方法
            doQueryUser(request, response);
        }


    }


    // 员工信息管理
    private void doEmployeeInformationManagement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 准备一个容器（List集合，实际上存的是数组），用来专门存储用户基本信息
        List<User> users = new ArrayList<>();

        // 连接数据库，查询所有的课程基本信息
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 注册驱动（注册驱动只需要注册一次，放在我写的DBUtil数据库工具类的静态代码块当中。
            // DBUtil类加载的时候执行。）
            // 获取连接
            conn = DBUtil.getConnection();
            // 获取预编译的数据库操作对象
            String sql = "select id,name,sex from t_user where (user_type = ? or user_type = ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "Non-corporate_employee");
            ps.setString(2, "Enterprise_employee");
            // 执行SQL语句
            rs = ps.executeQuery();
            // 处理查询结果集
            while(rs.next()){//如果结果集中有数据，则将数据取出
                // 取出用户编号
                String id = rs.getString("id");
                // 取出名字
                String name = rs.getString("name");
                // 取出性别
                String sex = rs.getString("sex");

                // 将以上的零散数据封装成java对象。
                User user = new User();
                user.setId(id);
                user.setName(name);
                user.setSex(sex);

                // 将课程基本信息对象放到list集合当中。
                users.add(user);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            DBUtil.close(conn, ps, rs);
        }

        // 将一个集合放到请求域当中
        request.setAttribute("userList", users);

        // 转发(不要重定向,因为要跳转到JSP做数据展示)（转发是一次请求，不管转发了多少次，都在同一个request当中。）
        // 获取请求转发器对象，调用请求转发器对象的forward方法完成转发
        request.getRequestDispatcher("/employee_information_management.jsp").forward(request, response);

    }

    // 上传用户信息
    private void doUploadUserInformation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取上传课程的基本信息
        // 注意乱码问题（Tomcat10不会出现这个问题）
        request.setCharacterEncoding("UTF-8");

        // 获取表单中的数据
        // 用户编号
        String id = request.getParameter("id");
        // 名字
        String name = request.getParameter("name");
        // 用户名
        String username = request.getParameter("username");
        // 密码
        String password = request.getParameter("password");
        // 性别
        String sex = request.getParameter("sex");
        // 用户类型
        String user_type = request.getParameter("user_type");

        // 连接数据库，执行insert语句。
        // 为了简化开发，我为连接数据库写了一个工具类DBUtil
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            // 注册驱动（注册驱动只需要注册一次，放在我写的DBUtil数据库工具类的静态代码块当中。
            // DBUtil类加载的时候执行。）
            // 获取连接
            conn = DBUtil.getConnection();
            // 获取预编译的数据库操作对象
            String sql = "insert into t_user(id,name,username,password,sex,user_type) values(?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            // 为sql语句中所有的 ？ 赋值
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setString(5, sex);
            ps.setString(6, user_type);
            // 执行sql语句，并返回受影响的行数（即更新计数）
            count = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // 用户编号重复错误，跳转到错误页面
            // 这里使用重定向（浏览器会发一次全新的请求。）
            // 浏览器在地址栏上发送请求，这个请求是get请求。
            response.sendRedirect(request.getContextPath() + "/User_number_repeating_error.jsp");
        } finally {
            // 释放资源
            DBUtil.close(conn, ps, null);
        }

        if(count == 1){
            //用户的基本信息上传成功，跳转到员工信息管理页面
            // 这里最好使用重定向（浏览器会发一次全新的请求。）
            // 浏览器在地址栏上发送请求，这个请求是get请求。
            response.sendRedirect(request.getContextPath() + "/employee_information_management");

        }
    }

    // 删除用户
    private void doDeleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 根据用户编号，删除用户。
        // 获取用户编号
        String id = request.getParameter("id");

        // 连接数据库删除数据
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            // 注册驱动（注册驱动只需要注册一次，放在我写的DBUtil数据库工具类的静态代码块当中。
            // DBUtil类加载的时候执行。）
            // 获取连接
            conn = DBUtil.getConnection();
            // 开启事务（自动提交机制关闭）
            conn.setAutoCommit(false);
            String sql = "delete from t_user where id = ?";
            ps = conn.prepareStatement(sql);
            // 为SQL语句中的 ？ 赋值。
            ps.setString(1, id);
            // 返回值是：影响了数据库表当中多少条记录。
            count = ps.executeUpdate();
            // 事务提交
            conn.commit();
        } catch (SQLException e) {
            // 遇到异常要回滚
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, null);
        }

        // 判断用户删除成功了还是失败了。
        if (count == 1) {
            //删除课程基本信息成功
            // 跳转到员工信息管理页面
            // 这里依旧使用重定向
            response.sendRedirect(request.getContextPath() + "/employee_information_management");

        }
    }

    // 修改用户基本信息
    private void doModifyingUserInformation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 解决请求体的中文乱码问题。
        request.setCharacterEncoding("UTF-8");

        // 获取表单中的数据
        // 用户编号
        String id = request.getParameter("id");
        // 名字
        String name = request.getParameter("name");
        // 用户名
        String username = request.getParameter("username");
        // 密码
        String password = request.getParameter("password");
        // 性别
        String sex = request.getParameter("sex");
        // 用户类型
        String user_type = request.getParameter("user_type");

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
            String sql = "update t_user set name = ?, username = ?, password = ?, sex = ?, user_type = ? where id = ?";
            ps = conn.prepareStatement(sql);
            // 为SQL语句中的所有 ？ 赋值。
            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, sex);
            ps.setString(5, user_type);
            ps.setString(6, id);
            // 执行sql语句，并返回受影响的行数（即更新计数）
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            DBUtil.close(conn, ps, null);
        }

        if (count == 1) {
            //用户的基本信息修改成功，跳转到员工信息管理页面
            // 这里最好使用重定向（浏览器会发一次全新的请求。）
            // 浏览器在地址栏上发送请求，这个请求是get请求。
            response.sendRedirect(request.getContextPath() + "/employee_information_management");
        }

    }

    // 根据用户编号，查询用户基本信息
    private void doQueryUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 创建用户对象
        User user = new User();

        // 获取用户编号
        String id = request.getParameter("id");

        // 连接数据库，根据用户编号，查询用户基本信息，将用户基本信息封装到用户对象中。
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
            String sql = "select name,username,password,sex,user_type from t_user where id = ?";
            ps = conn.prepareStatement(sql);
            // 为SQL语句中的所有 ？ 赋值。
            ps.setString(1 , id);
            // 执行SQL语句
            rs = ps.executeQuery();
            // 处理查询结果集(这里肯定只有一条数据所以使用if语句)
            if(rs.next()){//如果结果集中有数据，则将数据取出
                // 取出名字
                String name = rs.getString("name");
                // 取出用户名
                String username = rs.getString("username");
                // 取出密码
                String password = rs.getString("password");
                // 取出性别
                String sex = rs.getString("sex");
                // 取出用户类型
                String user_type = rs.getString("user_type");

                // 封装课程对象
                user.setId(id);
                user.setName(name);
                user.setUsername(username);
                user.setPassword(password);
                user.setSex(sex);
                user.setUser_type(user_type);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            // 在工具类中会分别进行判断 连接对象、数据库操作对象、查询结果集对象是否为空
            // 如果不为空，则会关闭流，并try catch处理SQL异常。
            DBUtil.close(conn, ps, rs);
        }

        // 因为课程编号只有一个，课程对象也只有一个，所以不需要list集合作为容器，直接将课程对象放入request域当中即可。
        request.setAttribute("user", user);

        // 转发(不要重定向,因为要跳转到JSP做数据展示)（转发是一次请求，不管转发了多少次，都在同一个request当中。）
        // 获取请求转发器对象，调用请求转发器对象的forward方法完成转发
        // 根据get请求中参数 f 的值，来判断是转发到查询用户页面（f=query_user）,还是编辑用户信息页面(f=edit_user_information)
        request.getRequestDispatcher("/" + request.getParameter("f") + ".jsp").forward(request, response);


    }


}
