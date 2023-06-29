package com.zengcan.ots.web.action;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.RequestContext;


import java.io.IOException;
import java.io.InputStream;

// 通过自己实现requestContext
// 而传入的request是 jakarta.servlet.http.HttpServletRequest
//通过new Rc(request)
//做到jakarta.*->javax.* 让其在高版本Tomcat10中也可以运行，低版本（Tomcat9）才支持的jar包。
class Rc implements RequestContext {
    HttpServletRequest request = null;
    public Rc(HttpServletRequest request) {
        this.request=request;
    }

    @Override
    public String getCharacterEncoding() {
        return request.getCharacterEncoding();
    }

    @Override
    public String getContentType() {
        return request.getContentType();
    }

    @Override
    public int getContentLength() {
        return request.getContentLength();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return request.getInputStream();
    }
}
