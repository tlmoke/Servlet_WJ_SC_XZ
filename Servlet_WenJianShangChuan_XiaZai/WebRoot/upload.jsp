<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${pageContext.request.contextPath }/UploadServlet" 
		method="post" enctype="multipart/form-data">
			上传用户名：<input type="text" name="username"><br>
			上传文件1：<input type="file" name="file1"><br>
			上传文件2：<input type="file" name="file2"><br>
			上传文件3：<input type="file" name="file3"><br>
			上传文件4：<input type="file" name="file4"><br>
			上传文件5：<input type="file" name="file5"><br>
			上传文件6：<input type="file" name="file6"><br>
			<input type="submit" value="提交">
	</form>
	<br /><br />
	<a href="${pageContext.request.contextPath }/ListFileServlet">下载页面</a>
	
</body>
</html>