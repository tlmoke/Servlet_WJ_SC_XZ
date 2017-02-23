<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>下载文件显示页面</title>
</head>
<body>
	<c:out value="${requestScope.message }"></c:out>
	<br /><br />
	<c:forEach var="filename" items="${requestScope.fileNameMap }">
		<c:url value="/DownloadServlet" var="downurl">
			<c:param name="filename" value="${filename.key }"></c:param>
		</c:url>
		${filename.value }<a href="${downurl }">下载</a>
		<br />
	</c:forEach>
</body>
</html>