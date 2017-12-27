package com.whv.raphael.utils.convert;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;


/**
 * 图片与字符串互转工具类
 * 功能：图片转换为base64格式或base64格式字符串转为图片
 * @author huawei
 *
 */
public class ImgStrUtils {
	/**
	 * 图片转base64格式字符串
	 * @param img
	 * @return
	 */
	public static String img2str(File img) {
		String base64Str = "";
		ByteArrayOutputStream out = null;
		InputStream in = null;
		try {
			in = new FileInputStream(img);
			byte[] buffer = new byte[100];
			int n = 0;
			while (-1 != (n = in.read(buffer))) {
				out.write(buffer, 0, n);
			}
			out.flush();
			base64Str = new BASE64Encoder().encode(out.toByteArray()) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
				try {
					if(in != null){
						in.close();
					}
					if(out != null){
						out.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return base64Str;
	}
	/**
	 * base64格式字符串转为图片
	 * @param base64Str
	 * @param formatName 图片格式
	 * @return
	 */
	public static File str2img(String base64Str,String formatName ) {
		BASE64Decoder decoder = new BASE64Decoder();
		//临时文件文件名
		String outFileName = "temp" + (int) (Math.random() * 100000) + "."+formatName;
		File outFile = null;
		ByteArrayInputStream bais = null;
		try {
			byte[] b = decoder.decodeBuffer(base64Str);
			bais = new ByteArrayInputStream(b);
			BufferedImage bi = ImageIO.read(bais);
			outFile = new File(outFileName);
			ImageIO.write(bi, formatName, outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
				try {
					if(bais != null) {
						bais.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return outFile;
	}
}
