package com.zengcan.ots.web.action;

import com.zengcan.ots.bean.Course;
import com.zengcan.ots.utils.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//使用模板方法设计模式优化项目，解决类爆炸问题（类的数量太大）
//- 以前的设计是一个请求一个Servlet类。1000个请求对应1000个Servlet类。导致类爆炸。
//- 可以这样做：一个请求对应一个方法。一个业务对应一个Servlet类。

// 模板类
// 使用Servlet注解，简化配置，完成Servlet类的注册（即，将请求路径和Servlet之间对应起来）
// 这个value属性和urlPatterns属性一致，都是用来指定Servlet的映射路径的。
// 如果注解的属性名是value的话，属性名也是可以省略的。
@WebServlet({"/course_management",
        "/upload_course_information", "/file_upload",
        "/delete_a_course",
        "/modify_the_course", "/modify_video",
        "/query_course", "/play/video"})
public class CourseServlet extends HttpServlet {

    // 模板方法
    // 重写service方法（并没有重写doGet或者doPost）
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取servlet path(也就是一般web.xml文件中与“url-pattern”中匹配的路径)
        String servletPath = request.getServletPath();

        if("/course_management".equals(servletPath)){ // 如果请求匹配的路径是  课程管理  则执行课程管理对应的方法
            doCourseManagement(request, response);
        } else if("/upload_course_information".equals(servletPath)){ // 如果请求匹配的路径是  上传课程基本信息  则执行上传课程基本信息对应的方法
            doUploadCourseInformation(request, response);
        } else if("/file_upload".equals(servletPath)){ // 如果请求匹配的路径是  视频文件上传  则执行视频文件上传对应的方法
            doFileUpload(request, response);
        } else if("/delete_a_course".equals(servletPath)){ // 如果请求匹配的路径是  删除课程  则执行删除课程对应的方法
            doDeleteCourse(request, response);
        } else if("/modify_the_course".equals(servletPath)){ // 如果请求匹配的路径是  修改课程  则执行修改课程对应的方法
            doModifyTheCourse(request, response);
        } else if("/modify_video".equals(servletPath)){ // 如果请求匹配的路径是  修改课程视频  则执行修改课程视频对应的方法
            doModifyVideo(request, response);
        } else if("/query_course".equals(servletPath)){ // 如果请求匹配的路径是  查询课程基本信息  则执行查询课程基本信息对应的方法
            doQueryCourse(request, response);
        } else if("/play/video".equals(servletPath)){ // 如果请求匹配的路径是  开始学习/播放视频  则执行开始学习/播放视频对应的方法
            doPlayVideo(request, response);
        }
    }

    /**
     * 课程管理列表页面，连接数据库，查询所有课程基本信息，将课程基本信息收集好，然后跳转到JSP做页面展示。
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doCourseManagement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 准备一个容器（List集合，实际上存的是数组），用来专门存储课程基本信息
        List<Course> courses = new ArrayList<>();

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
            String sql = "select course_number,course_name,course_type from t_course";
            ps = conn.prepareStatement(sql);
            // 执行SQL语句
            rs = ps.executeQuery();
            // 处理查询结果集
            while(rs.next()){//如果结果集中有数据，则将数据取出
                // 取出课程编号
                String course_number = rs.getString("course_number");
                // 取出课程名称
                String course_name = rs.getString("course_name");
                // 取出课程类型
                String course_type = rs.getString("course_type");

                // 将以上的零散数据封装成java对象。
                Course course = new Course();
                course.setCourse_number(course_number);
                course.setCourse_name(course_name);
                course.setCourse_type(course_type);

                // 将课程基本信息对象放到list集合当中。
                courses.add(course);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            DBUtil.close(conn, ps, rs);
        }

        // 将一个集合放到请求域当中
        request.setAttribute("courseList", courses);

        // 转发(不要重定向,因为要跳转到JSP做数据展示)（转发是一次请求，不管转发了多少次，都在同一个request当中。）
        // 获取请求转发器对象，调用请求转发器对象的forward方法完成转发
        request.getRequestDispatcher("/course_management.jsp").forward(request, response);

    }

    // 上传课程的基本信息
    private void doUploadCourseInformation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取上传课程的基本信息
        // 注意乱码问题（Tomcat10不会出现这个问题）
        request.setCharacterEncoding("UTF-8");
        //课程编号
        String course_number = request.getParameter("course_number");
        //课程名称
        String course_name = request.getParameter("course_name");
        //课程类型
        String course_type = request.getParameter("course_type");
        //文件路径
        String file_path = request.getParameter("course_name");
        //开课时间
        String class_start_time = request.getParameter("class_start_time");

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
            String sql = "insert into t_course(course_number,course_name,course_type,file_path,class_start_time) values(?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            // 为sql语句中所有的 ？ 赋值
            ps.setString(1, course_number);
            ps.setString(2, course_name);
            ps.setString(3, course_type);
            ps.setString(4, file_path);
            ps.setString(5, class_start_time);
            // 执行sql语句，并返回受影响的行数（即更新计数）
            count = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // 课程编号重复错误，跳转到错误页面
            // 这里使用重定向（浏览器会发一次全新的请求。）
            // 浏览器在地址栏上发送请求，这个请求是get请求。
            response.sendRedirect(request.getContextPath() + "/Course_number_repeating_error.jsp");
        } finally {
            // 释放资源
            DBUtil.close(conn, ps, null);
        }

        if(count == 1){
            //课程的基本信息上传成功，跳转到添加课程界面，并传值代表课程基本信息上传成功
            // 这里最好使用重定向（浏览器会发一次全新的请求。）
            // 浏览器在地址栏上发送请求，这个请求是get请求。
            response.sendRedirect(request.getContextPath() + "/add_course.jsp?upload_successfully=1");

        }
    }

    // 视频文件上传
    private void doFileUpload(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应的内容类型以及字符集，防止中文乱码问题。
        response.setContentType("text/html;charset=utf-8");
        // 获取标准的输出流，输出到浏览器
        PrintWriter out = response.getWriter();

        // 设置保存上传文件的目录
        String uploadDir = "d:/course/course/javaweb/online_training_system/web/classes"; // 文件上传后的保存路径(这个路径正好在项目内)
        out.println("上传文件存储目录！" + uploadDir);
        /*Java文件类以抽象的方式代表文件名和目录路径名*/
        File fUploadDir = new File(uploadDir);

        if(!fUploadDir.exists()){ // 如果此抽象路径名表示的文件或目录 不存在，则
            if(!fUploadDir.mkdir()){ // 创建由此抽象路径名命名的目录，如果该目录创建失败，则输出下面文字并退出。
                out.println("无法创建存储目录 d:/open_classes !");
                return;
            }
        }

        /*判断请求中的内容是否是“multipart/form—data”（多部分/表单数据）类型，如果不是则输出*/
        if(!DiskFileUpload.isMultipartContent(new Rc(request))){
            out.println("只能处理 multipart/form—data 类型的数据！");
            return;
        }

        /*创建磁盘文件上传类*/
        /*DiskFileUpload 类是 Apache 文件上传组件的核心类*/
        DiskFileUpload fu = new DiskFileUpload();
        fu.setSizeMax(1024*1024*200); // 设置最多上传 200MB 数据
        fu.setSizeThreshold(1024*1024); // 设置超过 1MB 的数据采用临时文件缓存
        // 这里不设置临时文件存储位置，会采用默认位置
        fu.setHeaderEncoding("utf-8"); // 设置上传的文件字段的文件名所用的字符集编码

        // 创建文件集合，用于保存浏览器表单传来的文件
        List fileItems = null;
        try {
            /*它对 HTTP 请求消息进行解析，如果请求消息中的实体内容的类型不是“multipart/form-data”，
            该方法将抛出 FileUploadException异常*/
            /*parseRequest()方法解析出 FORM 表单中的每个字段的数据，并将它们分别包装成独立的 FileItem 对象（普通表单类型，或上传文件类型），
            然后将这些 FileItem 对象加入进一个 List 类型的集合对象中返回。*/
            fileItems = fu.parseRequest(new Rc(request));
        } catch (FileUploadException e) {
            out.println("解析数据时出现如下问题：");
            e.printStackTrace(); // 打印异常堆栈追踪信息
            return;
        }

        // 下面通过迭代器逐个将集合中的文件取出，保存到服务器上
        Iterator it = fileItems.iterator(); // 创建迭代器对象 it
        while(it.hasNext()){ // 如果迭代器中有数据则
            FileItem fitem = (FileItem)it.next(); // 由迭代器取出文件项

            /*判断 FileItem 类对象封装的数据是否属于一个普通表单字段，还是属于一个文件表单字段，
            如果是普通表单字段则返回 true，否则返回 false*/
            if(!fitem.isFormField()){ // 忽略其他不属于文件域的那些表单信息（只要文件表单字段）
                try {
                    String pathSrc = fitem.getName();/*获得文件上传字段中的文件名。*/
                    // 文件名为空的文件项不处理  (trim() 函数移除字符串两侧的空白字符或其他预定义字符。)
                    if(pathSrc.trim().equals(""))continue;
                    // 确定最后的“\”位置，以此获取不含路径的文件名
                    int start = pathSrc.lastIndexOf("\\");
                    // 获取不含路径的文件名
                    String fileName = pathSrc.substring(start + 1);
                    File pathDest = new File(uploadDir, fileName); // 构建目标文件对象
                    fitem.write(pathDest); //将文件保存到服务器上
                    /*FileItem 对象中保存的主体内容保存到某个指定的文件中。
                    主要用途是将上传的文件内容保存在本地文件系统中。*/
                } catch (Exception e) {
                    out.println("存储文件时出现如下问题：");
                    e.printStackTrace(out);// 打印异常堆栈追踪信息
                    return;
                } finally { // 及时删除保存表单字段内容的临时文件
                    fitem.delete();
                }
            }
        }
        // 重定向
        // 浏览器发送请求，请求路径上是需要添加项目名的。
        // 以下这一行代码会将请求路径“/ots/file_uploaded_successfully.jsp”发送给浏览器
        // 浏览器会自发的向服务器发送一次全新的请求：/ots/file_uploaded_successfully.jsp
        response.sendRedirect("/ots/file_uploaded_successfully.jsp");
    }

    // 删除课程
    private void doDeleteCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 根据课程编号，删除课程基本信息。
        // 获取课程编号
        String course_number = request.getParameter("course_number");
        // 根据课程名称对应的文件保存路径，删除课程视频。
        // 获取课程名称
        String course_name = request.getParameter("course_name");

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
            String sql = "delete from t_course where course_number = ?";
            ps = conn.prepareStatement(sql);
            // 为SQL语句中的 ？ 赋值。
            ps.setString(1, course_number);
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

        // 判断课程基本信息删除成功了还是失败了。
        if (count == 1) {
            //删除课程基本信息成功

            // 继续删除课程基本信息对应的视频。
            try{
                /*Java文件类以抽象的方式代表文件名和目录路径名。该类主要用于文件和目录的创建、文件的查找和文件的删除等。*/
                /*通过将给定路径名字符串转换成抽象路径名来创建一个新 File 实例*/
                File file = new File("d:/course/course/javaweb/online_training_system/web/classes/"+course_name+".mp4");
                if(file.delete()){/* 删除此抽象路径名表示的文件或目录*/
                    // 如果删除成功则
                    // 跳转到课程管理列表页面
                    // 这里依旧使用重定向
                    response.sendRedirect(request.getContextPath() + "/course_management");
                }else{
                    System.out.println("文件删除失败！");
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    // 修改课程基本信息
    private void doModifyTheCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 解决请求体的中文乱码问题。
        request.setCharacterEncoding("UTF-8");

        // 获取表单中的数据
        // 课程编号
        String course_number = request.getParameter("course_number");
        // 课程名称
        String course_name = request.getParameter("course_name");
        // 课程类型
        String course_type = request.getParameter("course_type");
        //文件路径
        String file_path = request.getParameter("course_name");
        // 开课时间
        String class_start_time = request.getParameter("class_start_time");
        // 修改前的课程名称
        String course_name1 = request.getParameter("course_name1");

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
            String sql = "update t_course set course_name = ?, course_type = ?, file_path = ?, class_start_time = ? where course_number = ?";
            ps = conn.prepareStatement(sql);
            // 为SQL语句中的所有 ？ 赋值。
            ps.setString(1, course_name);
            ps.setString(2, course_type);
            ps.setString(3, file_path);
            ps.setString(4, class_start_time);
            ps.setString(5, course_number);
            // 执行sql语句，并返回受影响的行数（即更新计数）
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            DBUtil.close(conn, ps, null);
        }

        if (count == 1) {
            //课程的基本信息修改成功，跳转到编辑课程信息页面，并传值代表课程基本信息修改成功
            // 这里最好使用重定向（浏览器会发一次全新的请求。）
            // 浏览器在地址栏上发送请求，这个请求是get请求。
            //解决重定向参数中文乱码问题(将中文参数以UTF-8的方式编码)
            course_name = URLEncoder.encode(course_name, "UTF-8");
            course_name1 = URLEncoder.encode(course_name1, "UTF-8");
            response.sendRedirect(request.getContextPath() + "/modify_video?name=1&course_name="+course_name+"&course_name1="+course_name1+"");
        }
    }

    // 修改课程视频
    private void doModifyVideo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应的内容类型以及字符集，防止中文乱码问题。
        response.setContentType("text/html;charset=UTF-8");
        // 获取标准的输出流，输出到浏览器
        PrintWriter out = response.getWriter();
        out.print("<!DOCTYPE html>");
        out.print("<html>");
        out.print("	<head>");
        out.print("		<meta charset='utf-8'>");
        out.print("		<title>修改课程视频页面</title>");
        out.print("	</head>");
        out.print("	<body>");
        out.print("		<h1 align='center'>修改课程视频</h1>");
        out.print("		<hr />");
        /*上面一部分是死的*/

        // 接收重定向传过来的值
        String modified_successfully = request.getParameter("name");
        // 修改前的课程名称
        String course_name1 = request.getParameter("course_name1");
        // 修改后的课程名称
        String course_name2 = request.getParameter("course_name");
        // 如果课程的基本信息修改成功（modified_successfully == 1）
        // 并且 修改前查询出来的课程名称 course_name1 不等于 修改后的课程名称 course_name2
        // 也就是课程名称进行了修改（即课程本质发生了修改，则需要上传全新的视频）
        // 则展现：进行文件修改
        if("1".equals(modified_successfully)&&!(course_name1.equals(course_name2))){
            out.print("");
            out.print("");
            out.print("<h4>课程基本信息修改成功！</h4><br />");
            out.print("因为课程名称进行了修改（即课程本质发生了修改，则需要上传与课程名称完全匹配的全新视频）<br />");
            out.print("上传前需要先删除 与 原课程名称 相匹配的原视频。<br />");
            // 删除 原课程基本信息对应的视频。
            try{
                /*Java文件类以抽象的方式代表文件名和目录路径名。该类主要用于文件和目录的创建、文件的查找和文件的删除等。*/
                /*通过将给定路径名字符串转换成抽象路径名来创建一个新 File 实例*/
                File file = new File("d:/course/course/javaweb/online_training_system/web/classes/"+course_name1+".mp4");
                if(file.delete()){/* 删除此抽象路径名表示的文件或目录*/
                    // 如果删除成功则
                    out.print("原课程名称对应的课程在此刻删除，请继续上传新的视频文件。<br />");
                    out.print("<h3>文件上传：</h3><br />");
                    out.print("                请选择要上传的文件：<br />");
                    out.print("                在选择要上传的文件时，请先将候选的文件名称前缀修改为，与修改后的课程名称相同<br />");
                    out.print("<!-- 设置提交表单的URL。 -->");
                    out.print("<!-- 提交表单的方式为 post -->");
                    out.print("<!-- 指定要发送到服务器的元素数据的MIME类型 为multipart/form-data媒体类型（用于提交包含文件、非ascii数据和二进制数据的表单。） -->");
                    out.print("<!-- MIME（描述消息内容类型的标准，用来表示文档、文件或字节流的性质和格式。） -->");
                    out.print("<form action='/ots/file_upload' method='post' enctype='multipart/form-data'>");
                    out.print("    <input type='file' name='file' size='50' />");
                    out.print("    <br />");
                    out.print("    <input type='submit' value='上传文件' />");
                    out.print("</form>");
                }else{
                    // 如果删除失败
                    out.print("原视频文件删除失败！在原视频文件删除之前不能上传新的视频<br />");
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        } else if("1".equals(modified_successfully)){
            out.print("");
            out.print("");
            out.print("<h4>课程基本信息修改成功！。</h4><br />");
            out.print("");
        }

        /*下面一部分是死的*/
        out.print("	</body>");
        out.print("</html>");
    }

    //根据课程编号 查询课程基本信息
    private void doQueryCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 创建课程对象
        Course course = new Course();

        // 获取课程编号
        String course_number = request.getParameter("course_number");

        // 连接数据库，根据课程编号，查询课程基本信息，讲课程基本信息封装到课程对象中。
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
            String sql = "select course_name,course_type,class_start_time,file_path from t_course where course_number = ?";
            ps = conn.prepareStatement(sql);
            // 为SQL语句中的所有 ？ 赋值。
            ps.setString(1 , course_number);
            // 执行SQL语句
            rs = ps.executeQuery();
            // 处理查询结果集(这里肯定只有一条数据所以使用if语句)
            if(rs.next()){//如果结果集中有数据，则将数据取出
                // 取出课程名称
                String course_name = rs.getString("course_name");
                // 取出课程类型
                String course_type = rs.getString("course_type");
                // 取出开课时间
                String class_start_time = rs.getString("class_start_time");

                // 封装课程对象
                course.setCourse_number(course_number);
                course.setCourse_name(course_name);
                course.setCourse_type(course_type);
                course.setClass_start_time(class_start_time);
                // 存储文件的路径都在同一个文件夹中，是固定不变的，所以可以用课程名称来作为课程的文件路径
                course.setFile_path(course_name);
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
        request.setAttribute("course", course);

        // 转发(不要重定向,因为要跳转到JSP做数据展示)（转发是一次请求，不管转发了多少次，都在同一个request当中。）
        // 获取请求转发器对象，调用请求转发器对象的forward方法完成转发
        // 根据get请求中参数 f 的值，来判断是转发到查询课程页面（f=query_course）,还是编辑课程信息页面(f=edit_course_information)
        request.getRequestDispatcher("/" + request.getParameter("f") + ".jsp").forward(request, response);


    }

    //开始学习/播放视频
    private void doPlayVideo(HttpServletRequest request, HttpServletResponse response)
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
        out.print("     <meta charset='utf-8'>");
        out.print("	    <title>获取发送过来的文件路径，并进行播放</title>");
        out.print("</head>");
        out.print("<body align='center'><!-- 元素居中 -->");
        out.print("<!-- <video> 标签的作用是在 HTML 页面中嵌入视频元素,并设置显示的区域 以及自动播放-->");
        out.print("<video width='70%' controls='controls' autoplay='autoplay'>");
        out.print("	    <!-- HTML 5 <video> 元素会尝试播放以 mp4、ogg 或 webm 格式中的一种来播放视频。");
        out.print("        如果均失败，则回退到 <embed> 元素。 -->");
        out.print("	    <!-- 为了在绝大多数浏览器上都能打开，需要把视频转换为很多不同的格式 -->");
        /* 上面一部分是死的 */

        /*获取的是用户在浏览器上提交的数据。*/
        /*这里是文件的课程编号*/
        String course_number = request.getParameter("name");

        /*根据课程编号获取文件的存储路径*/
        // 连接数据库，获取文件存储路径。
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
            String sql = "select file_path from t_course where course_number = ?";
            ps = conn.prepareStatement(sql);
            // 为SQL语句中的所有 ？ 赋值。
            ps.setString(1, course_number);
            // 执行SQL语句
            rs = ps.executeQuery();
            // 处理查询结果集(这里肯定只有一条数据所以使用if语句)
            if(rs.next()){//如果结果集中有数据，则将数据取出
                //取出文件路径
                String file_path = rs.getString("file_path");

                out.print("	    <source src='/ots/classes/"+file_path+".mp4' type='video/mp4' />");
                out.print("	    <source src='/ots/classes/"+file_path+".ogg' type='video/ogg' />");
                out.print("	    <source src='/ots/classes/"+file_path+".webm' type='video/webm' />");
                out.print("	    <!-- <object>和<embed> 标签的作用都是在 HTML 页面中嵌入多媒体元素。");
                out.print("        下面的 HTML 片段显示嵌入网页的一段 Flash 视频： -->");
                out.print("	    <object data='/ots/classes/"+file_path+".mp4' width='70%' >");
                out.print("		    <embed src='/ots/classes/"+file_path+".swf' width='70%' />");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            DBUtil.close(conn, ps, null);
        }


        /* 下面一部分是死的*/
        out.print("	    </object>");
        out.print("</video>");
        out.print("</body>");
        out.print("</html>");
    }


}
