package com.whv.raphael.utils.convert;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
 * ����vml�Ĺ�����
 * @author wuhuawei
 * @date 2017/12/01
 */
public class VMLUtils {
	private static Log log = LogFactory.getLog(VMLUtils.class);
	/** ģ���ļ����� */
	public static final String MODEL_FILE_NAME = getProperties("raphael.modelname.vml");
	/** ģ���ļ�Ŀ¼ */
	public static final String MODEL_DIR = getProperties("raphael.modeldir.vml");
	/** ģ���ļ����� */
	public static final String XSL_VML2SVG = getProperties("raphael.xsl.vml2svg");
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
		InputStream in = VMLUtils.class.getClassLoader()
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
	 * vmlת��Ϊsvg
	 * @param dataMap
	 * @return
	 */
	public static File vml2svg(Map dataMap) {
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.vml2svg:vmlתΪsvg");
		}
		//��ʱ�ļ��ļ���
		String outFileName = "temp" + (int) (Math.random() * 100000) + ".svg";
		File vmlFile = createVML(dataMap, MODEL_FILE_NAME,MODEL_DIR);  
		File styleSheet = new File(VMLUtils.class.getResource(XSL_VML2SVG).getPath());
		File svgFile = new File(outFileName);
		StreamSource styleSource = new StreamSource(styleSheet);
		InputStream vmlIn = null;
		FileOutputStream svgOut = null;
		try {
			vmlIn = new FileInputStream(vmlFile);
			svgOut = new FileOutputStream(svgFile);
			StreamSource source = new StreamSource(vmlIn);
			StreamResult svgResult = new StreamResult(svgOut);
			Transformer t = TransformerFactory.newInstance().newTransformer(styleSource);
			t.transform(source, svgResult);
			
		} catch (FileNotFoundException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪsvg:�ļ��Ҳ���",e);
			}
		} catch (TransformerConfigurationException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪsvg��TransformerConfigurationException",e);
			}
		} catch (TransformerFactoryConfigurationError e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪsvg��TransformerFactoryConfigurationError",e);
			}
		} catch (TransformerException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪsvg��TransformerException",e);
			}
		}catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪjpg��IO�쳣",e);
			}
		} finally{
			try {
				if (vmlIn != null) {
					vmlIn.close();
				}
				if (svgOut != null) {
					svgOut.close();
				}
				if(vmlFile != null) vmlFile.delete(); // ɾ����ʱ�ļ�  
			} catch (IOException e) {
				if(log.isErrorEnabled()){
					log.error("����svg:�ر����������ʱ��IO�쳣",e);
				}
			}
		}
		return svgFile;
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
            // ����vml  
            file = vml2svg(dataMap);  
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
	 * vmlתΪjpg
	 * @param dataMap
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @return FIle
	 */
	public static File vml2jpg(Map dataMap,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.vml2jpg:vmlתΪjpg");
		}
		//��ʱ�ļ��ļ���
		String outFileName = "temp" + (int) (Math.random() * 100000) + ".jpg";
		File vmlFile = createVML(dataMap, MODEL_FILE_NAME,MODEL_DIR);  
		File styleSheet = new File(VMLUtils.class.getResource(XSL_VML2SVG).getPath());
		File jpgFile = new File(outFileName);
		StreamSource styleSource = new StreamSource(styleSheet);
		InputStream vmlIn = null;
		ByteArrayOutputStream svgOut = null;
		ByteArrayInputStream svgIn = null;
		OutputStream jpgOut = null;
		try {
			vmlIn = new FileInputStream(vmlFile);
			svgOut = new ByteArrayOutputStream();
			jpgOut = new FileOutputStream(jpgFile);
			StreamSource source = new StreamSource(vmlIn);
			StreamResult svgResult = new StreamResult(svgOut);
			Transformer t = TransformerFactory.newInstance().newTransformer(styleSource);
			t.transform(source, svgResult);
			JPEGTranscoder pt = new JPEGTranscoder();
		    //Rectangle aoi = new Rectangle(-10, -20, 200, 200);
			if(aoi != null){
				pt.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, new Float(aoi.width));
				pt.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, new Float(aoi.height));
				pt.addTranscodingHint(JPEGTranscoder.KEY_AOI, aoi);
				pt.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(0.8));
			}
			svgIn = new ByteArrayInputStream(svgOut.toByteArray());
            TranscoderInput input = new TranscoderInput(svgIn);  
            TranscoderOutput output = new TranscoderOutput(jpgOut);
			pt.transcode(input, output);
			jpgOut.flush();  
			
		} catch (FileNotFoundException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪjpg:�ļ��Ҳ���",e);
			}
		} catch (TransformerConfigurationException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪjpg��TransformerConfigurationException",e);
			}
		} catch (TransformerFactoryConfigurationError e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪjpg��TransformerFactoryConfigurationError",e);
			}
		} catch (TransformerException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪjpg��TransformerException",e);
			}
		} catch (TranscoderException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪjpg��TranscoderException",e);
			}
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪjpg��IO�쳣",e);
			}
		} finally{
			try {
				if (vmlIn != null) {
					vmlIn.close();
				}
				if (svgOut != null) {
					svgOut.close();
				}
				if (svgIn != null) {
					svgIn.close();
				}
				if (jpgOut != null) {
					jpgOut.close();
				}
				if(vmlFile != null) vmlFile.delete(); // ɾ����ʱ�ļ�  
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
            file = vml2jpg(dataMap,aoi);  
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
	 * vmlתΪpng
	 * @param dataMap
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @return FIle
	 */
	public static File vml2png(Map dataMap,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.vml2png:vmlתΪpng");
		}
		//��ʱ�ļ��ļ���
		String outFileName = "temp" + (int) (Math.random() * 100000) + ".png";
		File vmlFile = createVML(dataMap, MODEL_FILE_NAME,MODEL_DIR);  
		File styleSheet = new File(VMLUtils.class.getResource(XSL_VML2SVG).getPath());
		File pngFile = new File(outFileName);
		StreamSource styleSource = new StreamSource(styleSheet);
		InputStream vmlIn = null;
		ByteArrayOutputStream svgOut = null;
		ByteArrayInputStream svgIn = null;
		OutputStream pngOut = null;
		try {
			vmlIn = new FileInputStream(vmlFile);
			svgOut = new ByteArrayOutputStream();
			pngOut = new FileOutputStream(pngFile);
			StreamSource source = new StreamSource(vmlIn);
			StreamResult svgResult = new StreamResult(svgOut);
			Transformer t = TransformerFactory.newInstance().newTransformer(styleSource);
			t.transform(source, svgResult);
			PNGTranscoder pt = new PNGTranscoder();
		    //Rectangle aoi = new Rectangle(-10, -20, 200, 200);
			if(aoi != null){
				pt.addTranscodingHint(PNGTranscoder.KEY_WIDTH, new Float(aoi.width));
				pt.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, new Float(aoi.height));
				pt.addTranscodingHint(PNGTranscoder.KEY_AOI, aoi);
			}
			svgIn = new ByteArrayInputStream(svgOut.toByteArray());
            TranscoderInput input = new TranscoderInput(svgIn);  
            TranscoderOutput output = new TranscoderOutput(pngOut);
			pt.transcode(input, output);
			pngOut.flush();  
			
		} catch (FileNotFoundException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪpng:�ļ��Ҳ���",e);
			}
		} catch (TransformerConfigurationException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪpng��TransformerConfigurationException",e);
			}
		} catch (TransformerFactoryConfigurationError e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪpng��TransformerFactoryConfigurationError",e);
			}
		} catch (TransformerException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪpng��TransformerException",e);
			}
		} catch (TranscoderException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪpng��TranscoderException",e);
			}
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("vmlתΪpng��IO�쳣",e);
			}
		} finally{
			try {
				if (vmlIn != null) {
					vmlIn.close();
				}
				if (svgOut != null) {
					svgOut.close();
				}
				if (svgIn != null) {
					svgIn.close();
				}
				if (pngOut != null) {
					pngOut.close();
				}
				if(vmlFile != null) vmlFile.delete(); // ɾ����ʱ�ļ�  
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
            file = vml2png(dataMap,aoi);  
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
	 * ����VML
	 * @param dataMap ����
	 * @param outFileName ����ļ���
	 * @param templateName ģ���ļ���
	 * @param templateDir ģ��Ŀ¼
	 */
	public static File createVML(Map dataMap,String templateName,String templateDir){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.createVML:����VML");
		}
		//��ʱ�ļ��ļ���
		String outFileName = "temp" + (int) (Math.random() * 100000) + ".vml";
		if(templateName==null || "".equals(templateName)){
			templateName = MODEL_FILE_NAME;
		}
		if(templateDir==null || "".equals(templateDir)){
			templateDir = MODEL_DIR;
		}
		configuration.setClassForTemplateLoading(VMLUtils.class, templateDir);
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
				log.error("����VML:ģ�崦���쳣",e);
			}
		}catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("����VML:IO�쳣",e);
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
					log.error("����VML:�ر����������ʱ��IO�쳣",e);
				}
			}
		}
		return outFile;
		
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
            // ����vml  
            file = createVML(dataMap, MODEL_FILE_NAME,MODEL_DIR);  
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
	 * @param templateName ģ������
	 * @param templateDir ģ��Ŀ¼
	 */
	public static void exportVML(HttpServletResponse rep,Map dataMap,String outFileName,String templateName,String templateDir){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportVML:����VML,�ļ�����"+outFileName);
		}
	    File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // ���ù�����WordGenerator��createDoc��������Word�ĵ�  
            file = createVML(dataMap, templateName,templateDir);  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("text/html");  
            // ��������������صķ�ʽ������ļ�Ĭ����Ϊresume.doc  
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
	 * vmlתΪbmp
	 * @param dataMap
	 * @param aoi ��ȡͼƬ�����ԣ�Rectangle(x,y,w,h)��(x,y)��ȡw���h��
	 * @return FIle
	 */
	public static File vml2bmp(Map dataMap,Rectangle aoi){
		return image2Bmp(vml2jpg(dataMap, aoi));
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
            file = vml2bmp(dataMap,aoi);  
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
