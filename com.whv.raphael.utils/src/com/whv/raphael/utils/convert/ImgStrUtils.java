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
 * ͼƬ���ַ�����ת������
 * ���ܣ�ͼƬת��Ϊbase64��ʽ��base64��ʽ�ַ���תΪͼƬ
 * @author huawei
 *
 */
public class ImgStrUtils {
	/**
	 * ͼƬתbase64��ʽ�ַ���
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
	 * base64��ʽ�ַ���תΪͼƬ
	 * @param base64Str
	 * @param formatName ͼƬ��ʽ
	 * @return
	 */
	public static File str2img(String base64Str,String formatName ) {
		BASE64Decoder decoder = new BASE64Decoder();
		//��ʱ�ļ��ļ���
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
