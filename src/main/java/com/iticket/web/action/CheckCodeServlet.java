package com.iticket.web.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CheckCodeServlet extends HttpServlet {
	private static final long serialVersionUID = -2890961731754866376L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws IOException {
		response.setContentType("image/jpeg");
		response.setHeader("Pragma","No-cache");
		response.setHeader("Cache-Control","no-cache");
		response.setDateHeader("Expires", 0); 
		HttpSession session=request.getSession();
		// 在内存中创建图象
		int width=60, height=20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	
		// 获取图形上下文
		Graphics g = image.getGraphics();
	
		//生成随机类
		Random random = new Random();
	
		// 设定背景色
		g.setColor(getRandColor(200,250));
		g.fillRect(0, 0, width, height);
	
		//设定字体
		g.setFont(new Font("Times New Roman",Font.PLAIN,18));
	
		//画边框
		//g.setColor(new Color());
		//g.drawRect(0,0,width-1,height-1);
	
	
		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160,200));
		for (int i=0;i<155;i++) {
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(12);
		int yl = random.nextInt(12);
		g.drawLine(x,y,x+xl,y+yl);
		}
	
		// 取随机产生的认证码(4位数字)
		String sRand="";
		for (int i=0;i<4;i++){
		String rand=String.valueOf(random.nextInt(10));
		sRand+=rand;
		// 将认证码显示到图象中
		g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));//调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
		g.drawString(rand,13*i+6,16);
		}
	
		// 将认证码存入SESSION
		session.setAttribute("randck",sRand);
		// 图象生效
		g.dispose();
		ServletOutputStream responseOutputStream =response.getOutputStream();
		// 输出图象到页面
		ImageIO.write(image, "JPEG", responseOutputStream);
	
		//以下关闭输入流！
		responseOutputStream.flush();
		responseOutputStream.close();
	}
	public Color getRandColor(int s, int e) {
		Random random = new Random();
		if (s > 255)
			s = 255;
		if (e > 255)
			e = 255;
		int r = s + random.nextInt(e - s);
		int g = s + random.nextInt(e - s);
		int b = s + random.nextInt(e - s);
		return new Color(r, g, b);
	}
}