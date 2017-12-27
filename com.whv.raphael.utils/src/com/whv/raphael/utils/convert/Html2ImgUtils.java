package com.whv.raphael.utils.convert;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;

import gui.ava.html.image.generator.HtmlImageGenerator;
/**
 * html���뱣��ΪͼƬ�Ĺ�����
 * @author huawei
 *
 */
public class Html2ImgUtils{

	/**
	 * htmlתͼƬ��ͼƬ����Ϊ��ʱ�ļ�
	 * @param htmlStr
	 * @param formatName ͼƬ��׺��
	 * @return File
	 */
	public static File html2img(String htmlStr,String formatName) {
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		imageGenerator.loadHtml(htmlStr);  
		imageGenerator.getBufferedImage();
		String outFileName = "temp" + (int) (Math.random() * 100000) + "."+formatName;
		File file = new File(outFileName);
		imageGenerator.saveAsImage(file);
		return file;
	}
	/**
	 * htmlתΪͼƬ��ͼƬ���浽������ָ��Ŀ¼��
	 * @param htmlStr
	 * @param fileName �ļ���
	 * @param path �ļ�����Ŀ¼
	 * @return File
	 */
	public static File html2img(String htmlStr,String fileName,String path) {
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		imageGenerator.loadHtml(htmlStr);  
		imageGenerator.getBufferedImage();
		File file = getRenameFile(fileName, path);
		imageGenerator.saveAsImage(file);
		return file;
	}
	/**
	 * htmlתͼƬ��ͼƬ����Ϊ��ʱ�ļ�
	 * @param url
	 * @param formatName ͼƬ��׺��
	 * @return File
	 */
	public static File html2imgByUrl(String url,String formatName) {
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		imageGenerator.loadUrl(url);  
		imageGenerator.getBufferedImage();
		String outFileName = "temp" + (int) (Math.random() * 100000) + "."+formatName;
		File file = new File(outFileName);
		imageGenerator.saveAsImage(file);
		return file;
	}
	/**
	 * htmlתΪͼƬ��ͼƬ���浽������ָ��Ŀ¼��
	 * @param url
	 * @param fileName �ļ���
	 * @param path �ļ�����Ŀ¼
	 * @return File
	 */
	public static File html2imgByUrl(String url,String fileName,String path) {
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		imageGenerator.loadUrl(url);  
		imageGenerator.getBufferedImage();
		File file = getRenameFile(fileName, path);
		imageGenerator.saveAsImage(file);
		return file;
	}
	/**
	 * htmlתͼƬ��ͼƬ����Ϊ��ʱ�ļ�
	 * @param url
	 * @param formatName ͼƬ��׺��
	 * @return File
	 */
	public static File html2imgByUrl(URL url,String formatName) {
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		imageGenerator.loadUrl(url);  
		imageGenerator.getBufferedImage();
		String outFileName = "temp" + (int) (Math.random() * 100000) + "."+formatName;
		File file = new File(outFileName);
		imageGenerator.saveAsImage(file);
		return file;
	}
	/**
	 * htmlתΪͼƬ��ͼƬ���浽������ָ��Ŀ¼��
	 * @param url
	 * @param fileName �ļ���
	 * @param path �ļ�����Ŀ¼
	 * @return File
	 */
	public static File html2imgByUrl(URL url,String fileName,String path) {
		HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
		imageGenerator.loadUrl(url);  
		imageGenerator.getBufferedImage();
		File file = getRenameFile(fileName, path);
		imageGenerator.saveAsImage(file);
		return file;
	}
	/**
	 * ��ȡ����������ļ�
	 * @param fileName �ļ���
	 * @param path �ļ�����Ŀ¼
	 * @return File
	 */
	public static File getRenameFile(String fileName,String path) {
		if(path==null) path="";
		if(path.endsWith(fileName)) {
			path = path.substring(0, path.lastIndexOf(fileName));
		}
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}else {
			if(!dir.isDirectory()) {
				dir = new File(path+"_DIR");
				if(!dir.exists()) {
					dir.mkdirs();
				}
			}
		}
		String type = fileName.substring(fileName.lastIndexOf("."));
		String fileNamePre = fileName.substring(0, fileName.lastIndexOf("."));
		String nameRegex = "\\Q"+fileNamePre+"\\E(\\(\\d+\\))?\\Q"+type+"\\E";
		ImgFilter imgFilter = new  ImgFilter(type, nameRegex);
		File[] files = dir.listFiles(imgFilter);
		String fileNameStr = fileName;
		int i=0;
		for(File file :files) {
			String subName = file.getName();
			if(subName.equals(fileName)) {
				if(i==0) {
					i=1;
				}
			}else {
				int fileNum = Integer.valueOf(subName.substring(subName.lastIndexOf("(")+1, subName.lastIndexOf(")")));
				if(i<=fileNum) {
					i=fileNum+1;
				}
			}
		}
		if(i>0) {
			fileNameStr = fileNamePre+"("+i+")"+type;
		}
		return new File(dir.getPath()+File.separator+fileNameStr);
	}
	/**
	 * �ļ��б������
	 * @author huawei
	 *
	 */
	protected static class ImgFilter implements FilenameFilter{  
        private String type;  
        private String nameRegex;
        /**
         * �ļ��б���˹��췽��
         * @param type �ļ���չ��
         * @param nameRegex �ļ�����ƥ������
         */
        public ImgFilter(String type,String nameRegex){  
            this.type = type;  
            this.nameRegex = nameRegex==null?"*":nameRegex;
        }  
        public boolean accept(File dir,String name){  
            return name.endsWith(type)&&name.matches(nameRegex);  
        }  
    }  
	
}
