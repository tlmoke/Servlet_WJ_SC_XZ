package com.tl.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取上传文件的目录
		String uploadFilePath = this.getServletContext().getRealPath("/WEB-INF/upload");
		//存储要下载的文件名
		Map<String, String> fileNameMap = new HashMap<String, String>();
		//递归遍历filepath目录下的所有文件和目录，将文件的文件名存储到map集合中
		listfile(new File(uploadFilePath), fileNameMap);
		//将Map集合发送到listfile.jsp页面进行显示
		request.setAttribute("fileNameMap", fileNameMap);
		request.getRequestDispatcher("/listfile.jsp").forward(request, response);
	}
	
	/**
	 * 递归遍历指定目录下的所有文件
	 * @param file	既代表一个文件，也代表一个文件目录
	 * @param map	存储文件名的Map集合
	 */
	public void listfile(File file, Map<String, String> map){
		//如果file代表的不是一个文件，而是一个目录
		if(!file.isFile()){
			//列出该目录下的所有文件和目录
			File[] files = file.listFiles();
			//遍历files[]数组
			for (File f: files) {
				listfile(f, map);
			}
		}else{
			//处理文件名，上传后的文件是以uuid_文件名的形式去重新命名的，去除文件名的uuid_部分
			String realName = file.getName().substring(file.getName().indexOf("_")+1);
			//file.getName()得到的是文件的原始名称，这个名称是唯一的，因此可以作为key，realName是处理后的名称，有可能重复
			map.put(file.getName(), realName);
		}
	}

}













