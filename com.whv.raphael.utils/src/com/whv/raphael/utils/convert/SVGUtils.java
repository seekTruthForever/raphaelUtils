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
 * svg的工具类
 * @author wuhuawei
 * @date 2017/12/08
 */
public class SVGUtils {
	private static Log log = LogFactory.getLog(SVGUtils.class);
	/** 模板文件名称 */
	public static final String MODEL_FILE_NAME = getProperties("raphael.modelname.svg");
	/** 模板文件目录 */
	public static final String MODEL_DIR = getProperties("raphael.modeldir.svg");
	/** 模板文件名称 */
	public static final String XSL_SVG2VML = getProperties("raphael.xsl.svg2vml");
	/** 属性文件名称 */
	public static final String PROPERTIES_FILE_NAME = "raphael.properties";
	/** freemaker模板配置 */
	private static Configuration configuration = null;
	static{
		configuration = new Configuration();
		configuration.setDefaultEncoding("utf-8");
	}
	/**
	 * 从属性文件中获取值
	 * @param key 属性名称
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
	 * svg转换为vml
	 * @param svgFile
	 * @return
	 */
	public static File svg2vml(File svgFile) {
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.svg2vml:svg转换为vml");
		}
		//临时文件文件名
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
				log.error("svg转换为vml:文件找不到",e);
			}
		} catch (TransformerConfigurationException e) {
			if(log.isErrorEnabled()){
				log.error("svg转换为vml：TransformerConfigurationException",e);
			}
		} catch (TransformerFactoryConfigurationError e) {
			if(log.isErrorEnabled()){
				log.error("svg转换为vml：TransformerFactoryConfigurationError",e);
			}
		} catch (TransformerException e) {
			if(log.isErrorEnabled()){
				log.error("svg转换为vml：TransformerException",e);
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
					log.error("svg转换为vml:关闭输入输出流时，IO异常",e);
				}
			}
		}
		return vmlFile;
	}
	/**
	 * svg转换为vml
	 * @param dataMap
	 * @return
	 */
	public static File svg2vml(Map dataMap) {
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.svg2vml:svg转换为vml");
		}
		//临时文件文件名
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
				log.error("svg转换为vml:文件找不到",e);
			}
		} catch (TransformerConfigurationException e) {
			if(log.isErrorEnabled()){
				log.error("svg转换为vml：TransformerConfigurationException",e);
			}
		} catch (TransformerFactoryConfigurationError e) {
			if(log.isErrorEnabled()){
				log.error("svg转换为vml：TransformerFactoryConfigurationError",e);
			}
		} catch (TransformerException e) {
			if(log.isErrorEnabled()){
				log.error("svg转换为vml：TransformerException",e);
			}
		} finally{
			try {
				if (svgIn != null) {
					svgIn.close();
				}
				if (vmlOut != null) {
					vmlOut.close();
				}
				if(svgFile != null) svgFile.delete(); // 删除临时文件  
			} catch (IOException e) {
				if(log.isErrorEnabled()){
					log.error("svg转换为vml:关闭输入输出流时，IO异常",e);
				}
			}
		}
		return vmlFile;
	}
	/**
	 * 导出VML
	 * @param rep
	 * @param svgFile 数据
	 * @param outFileName 输出文件名
	 */
	public static void exportVML(HttpServletResponse rep,File svgFile,String outFileName){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportVML:导出VML,文件名："+outFileName);
		}
	    File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // 生成svg  
            file = svg2vml(svgFile);  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("text/html");  
            // 设置浏览器以下载的方式处理该文件
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("导出vml:IO异常",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
            	if(file != null) file.delete(); // 删除临时文件  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("导出vml:关闭输入输出流时异常",e);
				}
			}  
        } 
		
	}
	/**
	 * 导出VML
	 * @param rep
	 * @param dataMap 数据
	 * @param outFileName 输出文件名
	 */
	public static void exportVML(HttpServletResponse rep,Map dataMap,String outFileName){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportVML:导出VML,文件名："+outFileName);
		}
	    File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // 生成svg  
            file = svg2vml(dataMap);  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("text/html");  
            // 设置浏览器以下载的方式处理该文件
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("导出vml:IO异常",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
            	if(file != null) file.delete(); // 删除临时文件  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("导出vml:关闭输入输出流时异常",e);
				}
			}  
        } 
		
	}
	/**
	 * 导出SVG
	 * @param rep
	 * @param dataMap 数据
	 * @param outFileName 输出文件名
	 */
	public static void exportSVG(HttpServletResponse rep,Map dataMap,String outFileName){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportSVG:导出SVG,文件名："+outFileName);
		}
	    File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // 生成svg  
            file = createSVG(dataMap, MODEL_FILE_NAME,MODEL_DIR);  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("text/xml");  
            // 设置浏览器以下载的方式处理该文件
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("导出svg:IO异常",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
            	if(file != null) file.delete(); // 删除临时文件  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("导出svg:关闭输入输出流时异常",e);
				}
			}  
        } 
		
	}
	/**
	 * svg转为jpg
	 * @param svgFile
	 * @param aoi 截取图片的属性：Rectangle(x,y,w,h)从(x,y)截取w宽和h高
	 * @return FIle
	 */
	public static File svg2jpg(File svgFile,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.svg2jpg:svg转为jpg");
		}
		//临时文件文件名
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
				log.error("svg转为jpg:文件找不到",e);
			}
		} catch (TranscoderException e) {
			if(log.isErrorEnabled()){
				log.error("svg转为jpg：TranscoderException",e);
			}
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("svg转为jpg：IO异常",e);
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
					log.error("生成JPG:关闭输入输出流时，IO异常",e);
				}
			}
		}
		return jpgFile;
	}
	/**
	 * svg转为jpg
	 * @param dataMap
	 * @param aoi 截取图片的属性：Rectangle(x,y,w,h)从(x,y)截取w宽和h高
	 * @return FIle
	 */
	public static File svg2jpg(Map dataMap,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.svg2jpg:svg转为jpg");
		}
		//临时文件文件名
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
				log.error("svg转为jpg:文件找不到",e);
			}
		} catch (TranscoderException e) {
			if(log.isErrorEnabled()){
				log.error("svg转为jpg：TranscoderException",e);
			}
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("svg转为jpg：IO异常",e);
			}
		} finally{
			try {
				if (svgIn != null) {
					svgIn.close();
				}
				if (jpgOut != null) {
					jpgOut.close();
				}
				if(svgFile != null) svgFile.delete(); // 删除临时文件  
			} catch (IOException e) {
				if(log.isErrorEnabled()){
					log.error("生成JPG:关闭输入输出流时，IO异常",e);
				}
			}
		}
		return jpgFile;
	}
	/**
	 * 导出JPG
	 * @param rep
	 * @param file svgFile
	 * @param aoi 截取图片的属性：Rectangle(x,y,w,h)从(x,y)截取w宽和h高
	 * @param outFileName 输出文件名
	 */
	public static void exportJPG(HttpServletResponse rep,File file,String outFileName,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportJPG:导出JPG,文件名："+outFileName);
		}
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // 生成vml  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("image/jpeg");  
            // 设置浏览器以下载的方式处理该文件
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("导出jpg:IO异常",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("导出jpg:关闭输入输出流时异常",e);
				}
			}  
        } 
		
	}
	/**
	 * 导出JPG
	 * @param rep
	 * @param dataMap 数据
	 * @param aoi 截取图片的属性：Rectangle(x,y,w,h)从(x,y)截取w宽和h高
	 * @param outFileName 输出文件名
	 */
	public static void exportJPG(HttpServletResponse rep,Map dataMap,String outFileName,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportJPG:导出JPG,文件名："+outFileName);
		}
	    File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // 生成vml  
            file = svg2jpg(dataMap,aoi);  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("image/jpeg");  
            // 设置浏览器以下载的方式处理该文件
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("导出jpg:IO异常",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
            	if(file != null) file.delete(); // 删除临时文件  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("导出jpg:关闭输入输出流时异常",e);
				}
			}  
        } 
		
	}
	/**
	 * svg转为png
	 * @param svgFile
	 * @param aoi 截取图片的属性：Rectangle(x,y,w,h)从(x,y)截取w宽和h高
	 * @return FIle
	 */
	public static File svg2png(File svgFile,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.svg2png:svg转为png");
		}
		//临时文件文件名
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
				log.error("svg转为png:文件找不到",e);
			}
		}catch (TranscoderException e) {
			if(log.isErrorEnabled()){
				log.error("svg转为png：TranscoderException",e);
			}
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("svg转为png：IO异常",e);
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
					log.error("生成PNG:关闭输入输出流时，IO异常",e);
				}
			}
		}
		return pngFile;
	}
	/**
	 * svg转为png
	 * @param dataMap
	 * @param aoi 截取图片的属性：Rectangle(x,y,w,h)从(x,y)截取w宽和h高
	 * @return FIle
	 */
	public static File svg2png(Map dataMap,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.svg2png:svg转为png");
		}
		//临时文件文件名
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
				log.error("svg转为png:文件找不到",e);
			}
		}catch (TranscoderException e) {
			if(log.isErrorEnabled()){
				log.error("svg转为png：TranscoderException",e);
			}
		} catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("svg转为png：IO异常",e);
			}
		} finally{
			try {
				if (svgIn != null) {
					svgIn.close();
				}
				if (pngOut != null) {
					pngOut.close();
				}
				if(svgFile != null) svgFile.delete(); // 删除临时文件  
			} catch (IOException e) {
				if(log.isErrorEnabled()){
					log.error("生成PNG:关闭输入输出流时，IO异常",e);
				}
			}
		}
		return pngFile;
	}
	/**
	 * 导出PNG
	 * @param rep
	 * @param file 数据
	 * @param aoi 截取图片的属性：Rectangle(x,y,w,h)从(x,y)截取w宽和h高
	 * @param outFileName 输出文件名
	 */
	public static void exportPNG(HttpServletResponse rep,File file,String outFileName,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportPNG:导出PNG,文件名："+outFileName);
		}
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("image/png");  
            // 设置浏览器以下载的方式处理该文件
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("导出png:IO异常",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("导出png:关闭输入输出流时异常",e);
				}
			}  
        } 
		
	}
	/**
	 * 导出PNG
	 * @param rep
	 * @param dataMap 数据
	 * @param aoi 截取图片的属性：Rectangle(x,y,w,h)从(x,y)截取w宽和h高
	 * @param outFileName 输出文件名
	 */
	public static void exportPNG(HttpServletResponse rep,Map dataMap,String outFileName,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportPNG:导出PNG,文件名："+outFileName);
		}
	    File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // 生成vml  
            file = svg2png(dataMap,aoi);  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("image/png");  
            // 设置浏览器以下载的方式处理该文件
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("导出png:IO异常",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
            	if(file != null) file.delete(); // 删除临时文件  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("导出png:关闭输入输出流时异常",e);
				}
			}  
        } 
		
	}
	/**
	 * 生成SVG
	 * @param dataMap 数据
	 * @param outFileName 输出文件名
	 * @param templateName 模板文件名
	 * @param templateDir 模板目录
	 */
	public static File createSVG(Map dataMap,String templateName,String templateDir){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.createSVG:生成SVG");
		}
		//临时文件文件名
		String outFileName = "temp" + (int) (Math.random() * 100000) + ".svg";
		if(templateName==null || "".equals(templateName)){
			templateName = MODEL_FILE_NAME;
		}
		if(templateDir==null || "".equals(templateDir)){
			templateDir = MODEL_DIR;
		}
		configuration.setClassForTemplateLoading(SVGUtils.class, templateDir);
		 //输出文档路径及名称  
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
				log.error("生成SVG:模板处理异常",e);
			}
		}catch (IOException e) {
			if(log.isErrorEnabled()){
				log.error("生成SVG:IO异常",e);
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
					log.error("生成SVG:关闭输入输出流时，IO异常",e);
				}
			}
		}
		return outFile;
		
	}
	/**
	 * 转换文件名，解决文件名中文乱码的情况
	 * @param fileName 文件名
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
	 * 图片转为bmp格式
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
	 * 图片转为bmp格式
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
	 * svg转为bmp
	 * @param dataMap
	 * @param aoi 截取图片的属性：Rectangle(x,y,w,h)从(x,y)截取w宽和h高
	 * @return FIle
	 */
	public static File svg2bmp(Map dataMap,Rectangle aoi){
		return image2Bmp(svg2jpg(dataMap, aoi));
	}
	/**
	 * svg转为bmp
	 * @param svgFile
	 * @param aoi 截取图片的属性：Rectangle(x,y,w,h)从(x,y)截取w宽和h高
	 * @return FIle
	 */
	public static File svg2bmp(File svgFile,Rectangle aoi){
		return image2Bmp(svg2jpg(svgFile, aoi));
	}
	/**
	 * 导出BMP
	 * @param rep
	 * @param file 数据
	 * @param aoi 截取图片的属性：Rectangle(x,y,w,h)从(x,y)截取w宽和h高
	 * @param outFileName 输出文件名
	 */
	public static void exportBMP(HttpServletResponse rep,File file,String outFileName,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportBMP:导出BMP,文件名："+outFileName);
		}
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("application/x-bmp");  
            // 设置浏览器以下载的方式处理该文件
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("导出bmp:IO异常",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("导出bmp:关闭输入输出流时异常",e);
				}
			}  
        } 
		
	}
	/**
	 * 导出BMP
	 * @param rep
	 * @param dataMap 数据
	 * @param aoi 截取图片的属性：Rectangle(x,y,w,h)从(x,y)截取w宽和h高
	 * @param outFileName 输出文件名
	 */
	public static void exportBMP(HttpServletResponse rep,Map dataMap,String outFileName,Rectangle aoi){
		if(log.isDebugEnabled()){
			log.debug("VMLUtils.exportBMP:导出BMP,文件名："+outFileName);
		}
	    File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
            // 生成vml  
            file = svg2bmp(dataMap,aoi);  
            fin = new FileInputStream(file);  
            rep.setCharacterEncoding("utf-8");  
            rep.setContentType("application/x-bmp");  
            // 设置浏览器以下载的方式处理该文件
            rep.addHeader("Content-Disposition", "attachment;filename="+encodingFileName(outFileName));  
            out = rep.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch(IOException ex){
        	if(log.isErrorEnabled()){
				log.error("导出bmp:IO异常",ex);
			}
        }finally {  
            try {
            	if(fin != null) fin.close();  
            	if(out != null) out.close();  
            	if(file != null) file.delete(); // 删除临时文件  
			} catch (Exception e) {
				if(log.isErrorEnabled()){
					log.error("导出bmp:关闭输入输出流时异常",e);
				}
			}  
        } 
		
	}
}
