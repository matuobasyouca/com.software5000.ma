<%@ page contentType="text/html; charset=UTF-8"  import="org.apache.commons.logging.*" isErrorPage="true"%>
<%
    Log log = LogFactory.getLog("Root");
    log.error("页面捕获异常！",exception);
    response.getWriter().println("{\"code\":\"ZS012002\",\"data\":\"\",\"msg\":\"操作失败\"}");
%>
