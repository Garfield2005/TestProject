//package com.founder.support;
//
//import java.io.File;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//public class LoadConfig {
//	final Log log = LogFactory.getLog(LoadConfig.class);
//	// 以下成员变量都是需要从配置文件中读取的参数
//	private static String	pathConfigFile	= "./config/Config.config";	//可能不需要给出，直接读取相对路径下的配置文件即可
//	
//	public static boolean boolReIndexer = false;//是否重新索引
//	public static String imageFeatureDir;//图像特征文件目录
//	public static String imageIndexDir;//索引文件保存目录
//	
//	public static boolean	loadSucess = true;
//	
//	
//
//	// constructor fucntion: load config file
//	public LoadConfig() {
//		try {
//			File fid = new File(pathConfigFile);
//			// 得到DOM解析器的工厂实例
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			// 从DOM工厂获得DOM解析器
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//			// 解析XML文档的输入流，得到一个Document
//			Document doc = docBuilder.parse(fid);
//			NodeList settingList = doc.getElementsByTagName("setting");
//
//			for (int i = 0; i < settingList.getLength(); ++i) {
//				Node setting = settingList.item(i);
//				String name = setting.getAttributes().getNamedItem("name").getNodeValue();
//				switch (name) {
//				case "boolReIndexer": {
//					for (Node node = setting.getFirstChild(); node != null; node = node.getNextSibling()) {
//						if (node.getNodeName().equals("value")) {
//							boolReIndexer = Boolean.valueOf(node.getFirstChild().getNodeValue());
//							log.info("load boolReIndexer sucess");
//							loadSucess = true;
//						}
//					}
//					continue;
//				}
//				
//				
//				case "imageFeatureDir": {
//					for (Node node = setting.getFirstChild(); node != null; node = node.getNextSibling()) {
//						if (node.getNodeName().equals("value")) {
//							imageFeatureDir = node.getFirstChild().getNodeValue();
//							log.info("load imageFeatureDir sucess");
//							loadSucess = true;
//						}
//					}
//					if (imageFeatureDir == null || imageFeatureDir.isEmpty()) {
//						log.error("ERROR: load imageFeatureDir failed******");
//						loadSucess = false;
//						return;
//					}
//
//					continue;
//				}
//				
//				case "imageIndexDir": {
//					for (Node node = setting.getFirstChild(); node != null; node = node.getNextSibling()) {
//						if (node.getNodeName().equals("value")) {
//							imageIndexDir = node.getFirstChild().getNodeValue();
//							log.info("load imageIndexDir sucess");
//							loadSucess = true;
//						}
//					}
//					if (imageIndexDir == null || imageIndexDir.isEmpty()) {
//						log.error("ERROR: load imageIndexDir failed******");
//						loadSucess = false;
//						return;
//					}
//
//					continue;
//				}
//
//				default: {
//					log.warn("WARNING: no this value");
//					return;
//				}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}
//	}
//}
