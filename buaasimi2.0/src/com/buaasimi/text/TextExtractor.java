package com.buaasimi.text;

import java.io.*;
import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.POITextExtractor;
import org.apache.poi.extractor.ExtractorFactory;
import org.mozilla.universalchardet.UniversalDetector;


/**
 * Convert document(doc, docx, ppt, pptx, xls, xlsx, pdf, html, txt) to text.
 * 这是一个工厂方法，将其他各式的文档转化为文本
 * 
 * 
 */
public class TextExtractor {
	
	private ArrayList<Filter> filters;
	private String content;
	
	public TextExtractor(){
		filters = new ArrayList<Filter>();
	}
	
	public String extract(String filePath) throws ExtractException{
		content = extractDocFile(filePath);
		return content;
	}
	
	public String filtrate(){
		String result = content;
		for(Filter filter : filters){
			result = filter.filtrate(result);
		}
		return result;
	}
	
	public void addFilter(Filter filter){
		this.filters.add(filter);
	}
	
	public String getText(){
		return content;
	}

	/**
	 * @param filePath
	 *            原始文档的文件路径
	 * @return 抽取及过滤后的文本
	 * @throws ExtractException 
	 */
	public static String extractDocFile(String filePath) throws ExtractException {

		String postfix;
		/*
		 * 判断filePath是否存在，如果不存在，抛出IOException异常
		 */
		File doc = new File(filePath);
		if (!doc.exists() || !doc.canRead()){
			throw new ExtractException("no such file:" + filePath, filePath);
		}

		// 根据文件名后缀辨别文档格式

		try {
			int filenameLength = filePath.length();
			int postfixPosStart = filePath.lastIndexOf('.');
			if (postfixPosStart < 0 || postfixPosStart == filenameLength - 1)
				postfix = "txt";
			else {
				postfix = filePath.substring(postfixPosStart + 1,
						filenameLength);
			}
		} catch (IndexOutOfBoundsException ex) {
			throw new ExtractException("format error:" + filePath, filePath);
		}// 没有包含后缀，抛出异常

		String text;
		if (isOfficeFile(postfix)) {
			text = getTextFromOFFICE(filePath);
		} else if (postfix.equalsIgnoreCase("pdf")) {
			text = getTextFromPDF(filePath);
		} else if (postfix.equalsIgnoreCase("txt")) {
			text = getTextFromTXT(filePath);
		} else {
			throw new ExtractException("format:[" +  postfix + "] is not supported.", filePath);
		}
		return text;
	}

	/**
	 * 返回指定的后缀名是否是Office文档
	 * 
	 * @param postfix
	 *            后缀名
	 * 
	 */
	private static boolean isOfficeFile(String postfix) {
		return postfix.equalsIgnoreCase("doc")
				|| postfix.equalsIgnoreCase("xls")
				|| postfix.equalsIgnoreCase("ppt")
				|| postfix.equalsIgnoreCase("docx")
				|| postfix.equalsIgnoreCase("xlsx")
				|| postfix.equalsIgnoreCase("pptx");
	}

	/**
	 * 从给定的Txt文件中抽取文本。
	 * 
	 * @param filePath
	 *            给定的文件路径
	 * @return 抽取出的文本
	 * @throws ExtractException 
	 * @throws IOException
	 */
	public static String getTextFromTXT(String filePath) throws ExtractException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(filePath));
			String charset = getCharset(in);
			br = new BufferedReader(new InputStreamReader(in, charset));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append(System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ExtractException(e.getMessage(), filePath);
		} finally{
			if(in != null)
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			if(br != null)
				try {
					br.close();
				} catch (IOException e) {
				}
		}
		return sb.toString();
	}

	/**
	 * 采用Juniversalchardetector获取给定输入流的字符集，有可能返回错误的结果
	 * 
	 * @param is
	 *            给定的缓冲输入流
	 * @return 可能采用的字符集
	 */
	public static String getCharset(InputStream is) throws IOException {
		byte[] buf = new byte[4096];

		// (1)
		UniversalDetector detector = new UniversalDetector(null);
		// (2)
		int nread;
		while ((nread = is.read(buf)) > 0 && !detector.isDone()) {
			detector.handleData(buf, 0, nread);
		}
		// (3)
		detector.dataEnd();

		// (4)
		String encoding = detector.getDetectedCharset();

		// (5)
		detector.reset();
		return encoding;
	}

	/**
	 * 从给定的Pdf文件中抽取文本。
	 * 
	 * @param filePath
	 *            给定的文件路径
	 * @return 抽取出的文本
	 * @throws ExtractException 
	 * @throws IOException
	 */
	public static String getTextFromPDF(String filePath) throws ExtractException {
		String text = null;
		PDDocument pdfdocument = null;
		try {
			pdfdocument = PDDocument.load(new File(filePath));
			text = new PDFTextStripper().getText(pdfdocument);		
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExtractException(e.getMessage(), filePath);
		} finally{
			try {
				pdfdocument.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text;
	}

	/**
	 * 从给定的Office文件中抽取文本。
	 * 
	 * @param filePath
	 *            给定的文件路径
	 * @return 抽取出的文本
	 * @throws ExtractException 
	 * @throws IOException
	 */
	public static String getTextFromOFFICE(String filePath) throws ExtractException {
		String text = null;
		try {
			POITextExtractor extractor = ExtractorFactory
					.createExtractor(new File(filePath));
			if (extractor != null)
				text = extractor.getText();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExtractException(e.getMessage(), filePath);
		}
		return text;
	}

}
