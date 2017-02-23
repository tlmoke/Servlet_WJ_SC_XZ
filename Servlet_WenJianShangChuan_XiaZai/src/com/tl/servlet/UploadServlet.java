package com.tl.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
		String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
		//上传时生成的临时文件保存目录
		String tempPath = this.getServletContext().getRealPath("/WEB-INF/temp");
		File tempFile = new File(tempPath);
		if(!tempFile.exists()){
			//创建临时目录
			tempFile.mkdir();
		}
		
		//消息提示
		String message = "";
		try {
			//1.创建一个factory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//设置工厂的缓冲区的大小，当上传的文件大小超过缓冲区的大小时，就会生成一个临时文件存放到指定的临时目录中
			//设置缓冲区的大小为100k，如果不指定，默认缓冲区的大小为10k
			factory.setSizeThreshold(1024*100);
			//设置上传时生成的临时文件保存目录
			factory.setRepository(tempFile);
			
			//2.创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			//监听文件上传进度
			upload.setProgressListener(new ProgressListener() {
				@Override
				public void update(long pBytesRead, long pContentLength, int arg2) {
					System.out.println("文件大小为："+pContentLength+", 当前已处理："+pBytesRead);
				}
			});
			//解决上传文件名的中文乱码
			upload.setHeaderEncoding("UTF-8");
			//设置上传单个文件的大小的最大值，目前是设置为1024*1024，也就是1MB
			upload.setFileSizeMax(1024*1024);
			//设置上传文件总量的最大值，最大值=同事上传多个文件的大小的最大值的和
			upload.setSizeMax(1024*1024*10);
			
			//3.判断提交上来的数据是否是上传表单的数据
			if(!ServletFileUpload.isMultipartContent(request)){
				return;
			}
			
			//4.使用ServletFileUpload解析器解析上传数据，解析结果为一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem fileItem : list) {
				//如果fileItem中封装的是普通输入项的数据
				if(fileItem.isFormField()){
					String name = fileItem.getFieldName();
					//解决普通输入项中文乱码问题
					String value = fileItem.getString("UTF-8");
//					value = new String(value.getBytes("ISO8859-1"), "UTF-8");
					System.out.println(name+" = "+value);
				}else{
					//如果fileItem中封装的是上传文件
					//得到上传的文件名称
					String filename = fileItem.getName();
					if(filename==null || filename.trim().equals("")){
						continue;
					}
					//得到上传文件的扩展名
					String fileExtName = filename.substring(filename.lastIndexOf(".")+1);
					//获取fileItem中的上传文件的输入流
					InputStream in = fileItem.getInputStream();
					//得到文件保存的名称
					String saveFilename = makeFileName(filename);
					//得到文件的保存目录
					String realSavePath = makePath(saveFilename, savePath);
					//创建一个文件输出流
					FileOutputStream out = new FileOutputStream(realSavePath+"\\"+saveFilename);
					//创建一个缓冲区
					byte[] buffer = new byte[1024];
					//判断输入流中的数据是否已经读完的标识
					int len = 0;
					while((len=in.read(buffer)) > 0){
						out.write(buffer, 0, len);
					}
					
					//关闭输入流
					in.close();
					//关闭输入流
					out.close();
					//删除处理文件上传时生成的临时文件
//					fileItem.delete();
					
					message = "文件上传成功!";
				}
			}//end for
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		
		request.setAttribute("message", message);
		request.getRequestDispatcher("/ListFileServlet").forward(request, response);
	}
	
	/**
	 * 生成上传文件的文件名，文件名：uuid+"_"+文件的原始名称
	 * @param filename	文件的原始名称
	 * @return			uuid+"_"+文件的原始名称
	 */
	private String makeFileName(String filename){
		return UUID.randomUUID().toString()+"_"+filename;
	}
	
	/**
	 * 为防止一个目录下面出现太多文件，要使用hash算法打散存储
	 * @param filename	文件名，要根据文件名生成存储目录
	 * @param savePath	文件存储路径
	 * @return			新的存储目录
	 */
	private String makePath(String filename, String savePath){
		//得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
		int hashCode = filename.hashCode();
		int dir1 = hashCode & 0xf;
		int dir2 = (hashCode & 0xf0) >> 4;
		//构造新的保存目录
		String dir = savePath+"\\"+dir1+"\\"+dir2;
		//File既可以代表文件也可以代表目录
		File file = new File(dir);
		//如果目录不存在
		if(!file.exists()){
			file.mkdirs();
		}
		return dir;
	}
	
}










