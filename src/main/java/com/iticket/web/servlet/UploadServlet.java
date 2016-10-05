package com.iticket.web.servlet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.iticket.Config;
import com.iticket.util.DateUtil;
import com.iticket.util.JsonUtils;
@WebServlet(name="UploadServlet" ,urlPatterns={"/imageuploader/processupload"})  
@MultipartConfig
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = -7687679436635732291L;
	@Autowired@Qualifier("config")
	private Config config;
	private String uploadPath;
	@Override
	public void init(ServletConfig cfg) throws ServletException {
		super.init(cfg); 
		uploadPath = getServletContext().getRealPath("/upload");
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, cfg.getServletContext());  
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		Part part = req.getPart("qqfile");
		String fileName = getFileName(part);
		writeTo(fileName, part);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("message", config.getAbsPath() + "upload/" + fileName);
		out.println(JsonUtils.writeObjectToJson(map));
		out.close();
	}
	// 存储文件
	private void writeTo(String fileName, Part part) throws IOException,
			FileNotFoundException {
		InputStream in = part.getInputStream();
		String fullPath = uploadPath + "/" + fileName;
		OutputStream out = new FileOutputStream(fullPath);
		byte[] buffer = new byte[1024];
		int length = -1;
		while ((length = in.read(buffer)) != -1) {
			out.write(buffer, 0, length);
		}
		in.close();
		out.close();
	}
	// 取得上传文件名
	private String getFileName(Part part) {
		String name = part.getHeader("content-disposition");  
        String fileNameTmp = name.substring(name.indexOf("filename=")+10);  
        String type = fileNameTmp.substring(fileNameTmp.indexOf(".")+1,fileNameTmp.indexOf("\""));
		return getFileTimename() +"." + type;
	}
	private final String getFileTimename() {
		return DateUtil.format(DateUtil.getMillTimestamp(), "yyyyMMddHHmmss");
	}
}
