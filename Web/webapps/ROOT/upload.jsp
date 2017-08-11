<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>File Upload</title>
</head>
<body>
<center>
    <h1>File Upload</h1>
    <form method="post" action="Servlet/animalProfileUpload"
        enctype="multipart/form-data">
        <input type = "text" name="animalNo" />
        Select file to upload: <input type="file" name="file" size="60" /><br />
        <br /> <input type="submit" value="upload" />
    </form>
</center>
</body>
</html>