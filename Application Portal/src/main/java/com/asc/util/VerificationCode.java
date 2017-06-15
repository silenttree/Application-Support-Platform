package com.asc.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller

public class VerificationCode extends HttpServlet{

	private static final long serialVersionUID = 1L;
	//定义可选择的字符
	public static final char[] CHARS = {'1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z'};
	static Random random = new Random();
	
	@RequestMapping(value="/resources/VerifyCode",method=RequestMethod.GET)
	public void doGet (HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		response.setContentType("image/jpeg");  //设置输出类型
		response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Cache-Control", "no-cache");
		String randomString = getRandomString();
		//将getSession（）设置为true，当会话不存在是返回null
		request.getSession(true).setAttribute("randomString",randomString);
		
		//设置图片的宽、高
		int width = 90;
		int height = 30;
		
		Color bcolor = getRandomColor(); //前景色
		Color fcolor=getReverseColor(getRandomColor()); //设置背景色
		
		//创建一个彩色图片
		BufferedImage bimage=new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
		//创建绘图对象
		Graphics2D g=bimage.createGraphics();
		//字体样式为宋体,加粗，20磅
		g.setFont(new Font("宋体",Font.BOLD,20));
		//先画出背景色
		g.setColor(bcolor);
		g.fillRect(0,0,width,height);
		//再画出前景色
		g.setColor(fcolor);
		//绘制随机字符
		g.drawString(randomString, 20,22);
		g.rotate(30.6d);
		//画出干扰点
		for(int i = 0,n = random.nextInt(100);i < n;i++){
			g.drawRect(random.nextInt(width), random.nextInt(height),1, 1);
		}
		// 随机产生15条干扰线，使图象中的认证码不易被其它程序探测到。 
		g.setColor(new Color(65, 71, 96));
		for (int i=0;i<15;i++){
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x,y,x+xl,y+yl);
		}
		
		ServletOutputStream outstream = response.getOutputStream();
		//JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outstream);
		//encoder.encode(bimage);
		ImageIO.write(bimage, "JPEG", outstream);
		outstream.flush();
	}
	 private String getRandomString(){
		StringBuffer buffer = new StringBuffer();
		for(int i = 0;i <4 ;i++){ //生成四个字符
			if(random.nextBoolean()){
				buffer.append(String.valueOf(CHARS[random.nextInt(CHARS.length)]).toLowerCase());
			}else{
				buffer.append(CHARS[random.nextInt(CHARS.length)]);
			}
		}
		return buffer.toString();
	}
	
	private Color getRandomColor(){  //得到随机颜色
		return new Color(247,245,245);  
	}
		 
	private Color getReverseColor(Color c){ //得到颜色的反色
		return new Color(38,99,162);
	}
}
