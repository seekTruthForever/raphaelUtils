package com.whv.raphael.utils.convert;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.whv.pubutil.string.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * svg�Ĺ�����
 * @author wuhuawei
 * @date 2017/12/08
 */
public class SVGUtils {
	private static Log log = LogFactory.getLog(SVGUtils.class);
	/** ģ���ļ����� */
	public static final String MODEL_FILE_NAME = getProperties("raphael.modelname.svg");
	/** ģ���ļ�Ŀ¼ */
	public static final String MODEL_DIR = getProperties("raphael.modeldir.svg");
	/** ģ���ļ����� */
	public static final String XSL_SVG2VML = getProperties("raphael.xsl.svg2vml");
	/** �����ļ����� */
	public static final String PROPERTIES_FILE_NAME = "raphael.properties";
	/** freemakerģ������ */
	private static Configuration configuration = null;
	static{
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
	}
	/**
	 * �������ļ��л�ȡֵ
	 * @param key ��������
	 * @return
	 */
	public static String getProperties(String key) {
		Properties properties = new Properties();
		String value = null;
		InputStream in = SVGUtils.class.getClassLoader()
				.getResourceAsStream(PROPERTIES_FILE_NAME);
		try {
			properties.load(in);
			value = properties.getProperty(key).trim();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	/**
	 * svgת��Ϊvml
	 * @param svgFile
	 * @return
	 */
	public static File svg2vml(File svgFile) {
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.svg2vml:svgת��Ϊvml");
		}
		//��ʱ�ļ��ļ���
		String outFileName = "temp" + (int) (Math.random() * 100000) + ".html";
		File styleSheet = new File(SVGUtils.class.getResource(XSL_SVG2VML).getPath());
		File vmlFile = new File(outFileName);
		StreamSource styleSource = new StreamSource(styleSheet);
		InputStream svgIn = null;
		FileOutputStream vmlOut = null;
		try {
			svgIn = new FileInputStream(svgFile);
			vmlOut = new FileOutputStream(vmlFile);
			StreamSource source = new StreamSource(svgIn);
			StreamResult svgResult = new StreamResult(vmlOut);
			Transformer t = TransformerFactory.newInstance().newTransformer(styleSource);
			t.transform(source, svgResult);
			
		} catch (FileNotFoundException e) {
			if(log.isErrorEnabled()){
				log.error("svgת��Ϊvml:�ļ��Ҳ���",e);
			}
		} catch (TransformerConfigurationException e) {
			if(log.isErrorEnabled()){
				log.error("svgת��Ϊvml��TransformerConfigurationException",e);
			}
		} catch (TransformerFactoryConfigurationError e) {
			if(log.isErrorEnabled()){
				log.error("svgת��Ϊvml��TransformerFactoryConfigurationError",e);
			}
		} catch (TransformerException e) {
			if(log.isErrorEnabled()){
				log.error("svgת��Ϊvml��TransformerException",e);
			}
		} finally{
			try {
				if (svgIn != null) {
					svgIn.close();
				}
				if (vmlOut != null) {
					vmlOut.close();
				}
			} catch (IOException e) {
				if(log.isErrorEnabled()){
					log.error("svgת��Ϊvml:�ر����������ʱ��IO�쳣",e);
				}
			}
		}
		return vmlFile;
	}
	/**
	 * svgת��Ϊvml
	 * @param dataMap
	 * @return
	 */
	public static File svg2vml(Map dataMap) {
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.svg2vml:svgת��Ϊvml");
		}
		//��ʱ�ļ��ļ���
		String outFileName = "temp" + (int) (Math.random() * 100000) + ".html";
		File svgFile = createSVG(dataMap, MODEL_FILE_NAME,MODEL_DIR);  
		File styleSheet = new File(SVGUtils.class.getResource(XSL_SVG2VML).getPath());
		File vmlFile = new File(outFileName);
		StreamSource styleSource = new StreamSource(styleSheet);
		InputStream svgIn = null;
		FileOutputStream vmlOut = null;
		try {
			svgIn = new FileInputStream(svgFile);
			vmlOut = new FileOutputStream(vmlFile);
			StreamSource source = new StreamSource(svgIn);
			StreamResult svgResult = new StreamResult(vmlOut);
			Transformer t = TransformerFactory.newInstance().newTransformer(styleSource);
			t.transform(source, svgResult);
			
		} catch (FileNotFoundException e) {
			if(log.isErrorEnabled()){
				log.error("svgת��Ϊvml:�ļ��Ҳ���",e);
			}
		} catch (TransformerConfigurationException e) {
			if(log.isErrorEnabled()){
				log.error("svgת��Ϊvml��TransformerConfigurationException",e);
			}
		} catch (TransformerFactoryConfigurationError e) {
			if(log.isErrorEnabled()){
				log.error("svgת��Ϊvml��TransformerFactoryConfigurationError",e);
			}
		} catch (TransformerException e) {
			if(log.isErrorEnabled()){
				log.error("svgת��Ϊvml��TransformerException",e);
			}
		} finally{
			try {
				if (svgIn != null) {
					svgIn.close();
				}
				if (vmlOut != null) {
					vmlOut.close();
				}
				if(svgFile != null) svgFile.delete(); // ɾ����ʱ�ļ�  
			} catch (IOException e) {
				if(log.isErrorEnabled()){
					log.error("svgת��Ϊvml:�ر����������ʱ��IO�쳣",e);
				}
			}
		}
		return vmlFile;
	}
	/**
	 * ����VML
	 * @param rep
	 * @param svgFile ����
	 * @param outFileName ����ļ���
	 */
	public static void exportVML(HttpServletResponse rep,File svgFile,String outFileName){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportVML:����VML,�ļ�����"+outFileName);
		}
	    File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // ����svg  
            file = svg2vml(svgFile);  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("text/html");  
            // ��������������صķ�ʽ������ļ�
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // ������  
            int bytesToRead = -1;  
            // ͨ��ѭ���������Word�ļ�������������������  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("����vml:IO�쳣",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
            	if(file != null) file.delete(); // ɾ����ʱ�ļ�  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("����vml:�ر����������ʱ�쳣",e);
				}
			}  
        } 
		
	}
	/**
	 * ����VML
	 * @param rep
	 * @param dataMap ����
	 * @param outFileName ����ļ���
	 */
	public static void exportVML(HttpServletResponse rep,Map dataMap,String outFileName){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportVML:����VML,�ļ�����"+outFileName);
		}
	    File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // ����svg  
            file = svg2vml(dataMap);  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("text/html");  
            // ��������������صķ�ʽ������ļ�
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // ������  
            int bytesToRead = -1;  
            // ͨ��ѭ���������Word�ļ�������������������  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("����vml:IO�쳣",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
            	if(file != null) file.delete(); // ɾ����ʱ�ļ�  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("����vml:�ر����������ʱ�쳣",e);
				}
			}  
        } 
		
	}
	/**
	 * ����SVG
	 * @param rep
	 * @param dataMap ����
	 * @param outFileName ����ļ���
	 */
	public static void exportSVG(HttpServletResponse rep,Map dataMap,String outFileName){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportSVG:����SVG,�ļ�����"+outFileName);
		}
	    File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // ����svg  
            file = createSVG(dataMap, MODEL_FILE_NAME,MODEL_DIR);  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("text/xml");  
            // ��������������صķ�ʽ������ļ�
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // ������  
            int bytesToRead = -1;  
            // ͨ��ѭ���������Word�ļ�������������������  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("����svg:IO�쳣",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
            	if(file != null) file.delete(); // ɾ����ʱ�ļ�  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("����svg:�ر����������ʱ�쳣",e);
				}
			}  
        } 
		
	}
	/**
	 * svgתΪjpg
	 * @param svgFile
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @return FIle
	 */
	public static File svg2jpg(File svgFile,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.svg2jpg:svgתΪjpg");
		}
		//��ʱ�ļ��ļ���
		String outFileName = "temp" + (int) (Math.random() * 100000) + ".jpg";
		File jpgFile = new File(outFileName);
		InputStream svgIn = null;
		OutputStream jpgOut = null;
		try {
			jpgOut = new FileOutputStream(jpgFile);
			JPEGTranscoder pt = new JPEGTranscoder();
		    //Rectangle aoi = new Rectangle(-10, -20, 200, 200);
			if(aoi != null){
				pt.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, new Float(aoi.width));
				pt.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, new Float(aoi.height));
				pt.addTranscodingHint(JPEGTranscoder.KEY_AOI, aoi);
				pt.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(0.8));
			}
			svgIn = new FileInputStream(svgFile);
            TranscoderInput input = new TranscoderInput(svgIn);  
            TranscoderOutput output = new TranscoderOutput(jpgOut);
			pt.transcode(input, output);
			jpgOut.flush();  
			
		} catch (FileNotFoundException e) {
			if(log.isErrorEnabled()){
				log.error("svgתΪjpg:�ļ��Ҳ���",e);
			}
		} catch (TranscoderException e) {
			if(log.isErrorEnabled()){
				log.error("svgתΪjpg��TranscoderException",e);
			}
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("svgתΪjpg��IO�쳣",e);
			}
		} finally{
			try {
				if (svgIn != null) {
					svgIn.close();
				}
				if (jpgOut != null) {
					jpgOut.close();
				}
			} catch (IOException e) {
				if(log.isErrorEnabled()){
					log.error("����JPG:�ر����������ʱ��IO�쳣",e);
				}
			}
		}
		return jpgFile;
	}
	/**
	 * svgתΪjpg
	 * @param dataMap
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @return FIle
	 */
	public static File svg2jpg(Map dataMap,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.svg2jpg:svgתΪjpg");
		}
		//��ʱ�ļ��ļ���
		String outFileName = "temp" + (int) (Math.random() * 100000) + ".jpg";
		File svgFile = createSVG(dataMap, MODEL_FILE_NAME,MODEL_DIR);  
		File jpgFile = new File(outFileName);
		InputStream svgIn = null;
		OutputStream jpgOut = null;
		try {
			jpgOut = new FileOutputStream(jpgFile);
			JPEGTranscoder pt = new JPEGTranscoder();
		    //Rectangle aoi = new Rectangle(-10, -20, 200, 200);
			if(aoi != null){
				pt.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, new Float(aoi.width));
				pt.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, new Float(aoi.height));
				pt.addTranscodingHint(JPEGTranscoder.KEY_AOI, aoi);
				pt.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(0.8));
			}
			svgIn = new FileInputStream(svgFile);
            TranscoderInput input = new TranscoderInput(svgIn);  
            TranscoderOutput output = new TranscoderOutput(jpgOut);
			pt.transcode(input, output);
			jpgOut.flush();  
			
		} catch (FileNotFoundException e) {
			if(log.isErrorEnabled()){
				log.error("svgתΪjpg:�ļ��Ҳ���",e);
			}
		} catch (TranscoderException e) {
			if(log.isErrorEnabled()){
				log.error("svgתΪjpg��TranscoderException",e);
			}
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("svgתΪjpg��IO�쳣",e);
			}
		} finally{
			try {
				if (svgIn != null) {
					svgIn.close();
				}
				if (jpgOut != null) {
					jpgOut.close();
				}
				if(svgFile != null) svgFile.delete(); // ɾ����ʱ�ļ�  
			} catch (IOException e) {
				if(log.isErrorEnabled()){
					log.error("����JPG:�ر����������ʱ��IO�쳣",e);
				}
			}
		}
		return jpgFile;
	}
	/**
	 * ����JPG
	 * @param rep
	 * @param file svgFile
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @param outFileName ����ļ���
	 */
	public static void exportJPG(HttpServletResponse rep,File file,String outFileName,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportJPG:����JPG,�ļ�����"+outFileName);
		}
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // ����vml  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("image/jpeg");  
            // ��������������صķ�ʽ������ļ�
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // ������  
            int bytesToRead = -1;  
            // ͨ��ѭ���������Word�ļ�������������������  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("����jpg:IO�쳣",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("����jpg:�ر����������ʱ�쳣",e);
				}
			}  
        } 
		
	}
	/**
	 * ����JPG
	 * @param rep
	 * @param dataMap ����
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @param outFileName ����ļ���
	 */
	public static void exportJPG(HttpServletResponse rep,Map dataMap,String outFileName,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportJPG:����JPG,�ļ�����"+outFileName);
		}
	    File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // ����vml  
            file = svg2jpg(dataMap,aoi);  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("image/jpeg");  
            // ��������������صķ�ʽ������ļ�
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // ������  
            int bytesToRead = -1;  
            // ͨ��ѭ���������Word�ļ�������������������  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("����jpg:IO�쳣",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
            	if(file != null) file.delete(); // ɾ����ʱ�ļ�  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("����jpg:�ر����������ʱ�쳣",e);
				}
			}  
        } 
		
	}
	/**
	 * svgתΪpng
	 * @param svgFile
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @return FIle
	 */
	public static File svg2png(File svgFile,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.svg2png:svgתΪpng");
		}
		//��ʱ�ļ��ļ���
		String outFileName = "temp" + (int) (Math.random() * 100000) + ".png";
		File pngFile = new File(outFileName);
		InputStream svgIn = null;
		OutputStream pngOut = null;
		try {
			pngOut = new FileOutputStream(pngFile);
			PNGTranscoder pt = new PNGTranscoder();
		    //Rectangle aoi = new Rectangle(-10, -20, 200, 200);
			if(aoi != null){
				pt.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Float(aoi.width));
				pt.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, new Float(aoi.height));
				pt.addTranscodingHint(PNGTranscoder.KEY_AOI, aoi);
			}
			svgIn = new FileInputStream(svgFile);
            TranscoderInput input = new TranscoderInput(svgIn);  
            TranscoderOutput output = new TranscoderOutput(pngOut);
			pt.transcode(input, output);
			pngOut.flush();  
			
		} catch (FileNotFoundException e) {
			if(log.isErrorEnabled()){
				log.error("svgתΪpng:�ļ��Ҳ���",e);
			}
		}catch (TranscoderException e) {
			if(log.isErrorEnabled()){
				log.error("svgתΪpng��TranscoderException",e);
			}
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("svgתΪpng��IO�쳣",e);
			}
		} finally{
			try {
				if (svgIn != null) {
					svgIn.close();
				}
				if (pngOut != null) {
					pngOut.close();
				}
			} catch (IOException e) {
				if(log.isErrorEnabled()){
					log.error("����PNG:�ر����������ʱ��IO�쳣",e);
				}
			}
		}
		return pngFile;
	}
	/**
	 * svgתΪpng
	 * @param dataMap
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @return FIle
	 */
	public static File svg2png(Map dataMap,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.svg2png:svgתΪpng");
		}
		//��ʱ�ļ��ļ���
		String outFileName = "temp" + (int) (Math.random() * 100000) + ".png";
		File svgFile = createSVG(dataMap, MODEL_FILE_NAME,MODEL_DIR);  
		File pngFile = new File(outFileName);
		InputStream svgIn = null;
		OutputStream pngOut = null;
		try {
			pngOut = new FileOutputStream(pngFile);
			PNGTranscoder pt = new PNGTranscoder();
		    //Rectangle aoi = new Rectangle(-10, -20, 200, 200);
			if(aoi != null){
				pt.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Float(aoi.width));
				pt.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, new Float(aoi.height));
				pt.addTranscodingHint(PNGTranscoder.KEY_AOI, aoi);
			}
			svgIn = new FileInputStream(svgFile);
            TranscoderInput input = new TranscoderInput(svgIn);  
            TranscoderOutput output = new TranscoderOutput(pngOut);
			pt.transcode(input, output);
			pngOut.flush();  
			
		} catch (FileNotFoundException e) {
			if(log.isErrorEnabled()){
				log.error("svgתΪpng:�ļ��Ҳ���",e);
			}
		}catch (TranscoderException e) {
			if(log.isErrorEnabled()){
				log.error("svgתΪpng��TranscoderException",e);
			}
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("svgתΪpng��IO�쳣",e);
			}
		} finally{
			try {
				if (svgIn != null) {
					svgIn.close();
				}
				if (pngOut != null) {
					pngOut.close();
				}
				if(svgFile != null) svgFile.delete(); // ɾ����ʱ�ļ�  
			} catch (IOException e) {
				if(log.isErrorEnabled()){
					log.error("����PNG:�ر����������ʱ��IO�쳣",e);
				}
			}
		}
		return pngFile;
	}
	/**
	 * ����PNG
	 * @param rep
	 * @param file ����
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @param outFileName ����ļ���
	 */
	public static void exportPNG(HttpServletResponse rep,File file,String outFileName,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportPNG:����PNG,�ļ�����"+outFileName);
		}
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("image/png");  
            // ��������������صķ�ʽ������ļ�
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // ������  
            int bytesToRead = -1;  
            // ͨ��ѭ���������Word�ļ�������������������  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("����png:IO�쳣",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("����png:�ر����������ʱ�쳣",e);
				}
			}  
        } 
		
	}
	/**
	 * ����PNG
	 * @param rep
	 * @param dataMap ����
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @param outFileName ����ļ���
	 */
	public static void exportPNG(HttpServletResponse rep,Map dataMap,String outFileName,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportPNG:����PNG,�ļ�����"+outFileName);
		}
	    File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // ����vml  
            file = svg2png(dataMap,aoi);  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("image/png");  
            // ��������������صķ�ʽ������ļ�
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // ������  
            int bytesToRead = -1;  
            // ͨ��ѭ���������Word�ļ�������������������  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("����png:IO�쳣",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
            	if(file != null) file.delete(); // ɾ����ʱ�ļ�  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("����png:�ر����������ʱ�쳣",e);
				}
			}  
        } 
		
	}
	/**
	 * ����SVG
	 * @param dataMap ����
	 * @param outFileName ����ļ���
	 * @param templateName ģ���ļ���
	 * @param templateDir ģ��Ŀ¼
	 */
	public static File createSVG(Map dataMap,String templateName,String templateDir){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.createSVG:����SVG");
		}
		//��ʱ�ļ��ļ���
		String outFileName = "temp" + (int) (Math.random() * 100000) + ".svg";
		if(templateName==null || "".equals(templateName)){
			templateName = MODEL_FILE_NAME;
		}
		if(templateDir==null || "".equals(templateDir)){
			templateDir = MODEL_DIR;
		}
		configuration.setClassForTemplateLoading(SVGUtils.class, templateDir);
		 //����ĵ�·��������  
        File outFile = new File(outFileName);  
        Writer out = null;  
        FileOutputStream fos=null;  
        Template template = null;
		try {
			template = configuration.getTemplate(templateName);
			 fos = new FileOutputStream(outFile);  
	         OutputStreamWriter oWriter = new OutputStreamWriter(fos,"UTF-8");  
	         out = new BufferedWriter(oWriter);
	         template.process(dataMap, out);
		}catch (TemplateException e){
			if(log.isErrorEnabled()){
				log.error("����SVG:ģ�崦���쳣",e);
			}
		}catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("����SVG:IO�쳣",e);
			}
		}finally{
			try {
				if (out != null) {
					out.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				if(log.isErrorEnabled()){
					log.error("����SVG:�ر����������ʱ��IO�쳣",e);
				}
			}
		}
		return outFile;
		
	}
	/**
	 * ת���ļ���������ļ���������������
	 * @param fileName �ļ���
	 * @return
	 */
	public static String encodingFileName(String fileName) {
        String returnFileName = "";
        try {
            returnFileName = URLEncoder.encode(fileName, "UTF-8");
            returnFileName = StringUtils.replace(returnFileName, "+", "%20");
            if (returnFileName.length() > 150) {
                returnFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
                returnFileName = StringUtils.replace(returnFileName, " ", "%20");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnFileName;
	}
	/**
	 * ͼƬתΪbmp��ʽ
	 * @param filePath
	 * @param saveFileName
	 * @return
	 */
	public static File image2Bmp(String filePath, String saveFileName) {
		File inFile = new File(filePath);
		File outFile = new File(saveFileName);
        try {
            BufferedImage sourceImg = ImageIO.read(inFile);
            int h = sourceImg.getHeight(), w = sourceImg.getWidth();
            int[] pixel = new int[w * h];
            PixelGrabber pixelGrabber = new PixelGrabber(sourceImg, 0, 0, w, h, pixel, 0, w);
            pixelGrabber.grabPixels();
            MemoryImageSource m = new MemoryImageSource(w, h, pixel, 0, w);
            Image image = Toolkit.getDefaultToolkit().createImage(m);
            BufferedImage buff = new BufferedImage(w, h, BufferedImage.TYPE_USHORT_565_RGB);
            buff.createGraphics().drawImage(image, 0, 0 ,null);
            ImageIO.write(buff, "bmp", outFile);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	if(inFile != null) inFile.delete();
        }
        return outFile;
    }
	/**
	 * ͼƬתΪbmp��ʽ
	 * @param filePath
	 * @param saveFileName
	 * @return
	 */
	public static File image2Bmp(File inFile) {
		if(inFile==null) return null;
		String filePath = inFile.getPath();
		filePath = filePath.substring(0, filePath.lastIndexOf("."))+".bmp";
		File outFile = new File(filePath);
        try {
            BufferedImage sourceImg = ImageIO.read(inFile);
            int h = sourceImg.getHeight(), w = sourceImg.getWidth();
            int[] pixel = new int[w * h];
            PixelGrabber pixelGrabber = new PixelGrabber(sourceImg, 0, 0, w, h, pixel, 0, w);
            pixelGrabber.grabPixels();
            MemoryImageSource m = new MemoryImageSource(w, h, pixel, 0, w);
            Image image = Toolkit.getDefaultToolkit().createImage(m);
            BufferedImage buff = new BufferedImage(w, h, BufferedImage.TYPE_USHORT_565_RGB);
            buff.createGraphics().drawImage(image, 0, 0 ,null);
            ImageIO.write(buff, "bmp", outFile);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	if(inFile != null) inFile.delete();
        }
        return outFile;
    }
	/**
	 * svgתΪbmp
	 * @param dataMap
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @return FIle
	 */
	public static File svg2bmp(Map dataMap,Rectangle aoi){
		return image2Bmp(svg2jpg(dataMap, aoi));
	}
	/**
	 * svgתΪbmp
	 * @param svgFile
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @return FIle
	 */
	public static File svg2bmp(File svgFile,Rectangle aoi){
		return image2Bmp(svg2jpg(svgFile, aoi));
	}
	/**
	 * ����BMP
	 * @param rep
	 * @param file ����
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @param outFileName ����ļ���
	 */
	public static void exportBMP(HttpServletResponse rep,File file,String outFileName,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportBMP:����BMP,�ļ�����"+outFileName);
		}
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("application/x-bmp");  
            // ��������������صķ�ʽ������ļ�
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // ������  
            int bytesToRead = -1;  
            // ͨ��ѭ���������Word�ļ�������������������  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("����bmp:IO�쳣",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("����bmp:�ر����������ʱ�쳣",e);
				}
			}  
        } 
		
	}
	/**
	 * ����BMP
	 * @param rep
	 * @param dataMap ����
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @param outFileName ����ļ���
	 */
	public static void exportBMP(HttpServletResponse rep,Map dataMap,String outFileName,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportBMP:����BMP,�ļ�����"+outFileName);
		}
	    File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // ����vml  
            file = svg2bmp(dataMap,aoi);  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("application/x-bmp");  
            // ��������������صķ�ʽ������ļ�
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // ������  
            int bytesToRead = -1;  
            // ͨ��ѭ���������Word�ļ�������������������  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("����bmp:IO�쳣",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
            	if(file != null) file.delete(); // ɾ����ʱ�ļ�  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("����bmp:�ر����������ʱ�쳣",e);
				}
			}  
        } 
		
	}
}
