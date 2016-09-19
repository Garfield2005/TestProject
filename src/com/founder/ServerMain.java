package com.founder;

import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.founder.common.file.FileUtils;
import com.founder.image.asset.ImageAsset;
import com.founder.image.support.DisplayImage;
import com.founder.similar.hash.Similarity;
import com.founder.sort.Sort;

import de.lmu.ifi.dbs.jfeaturelib.LibProperties;
import de.lmu.ifi.dbs.jfeaturelib.features.PHOG;
import de.lmu.ifi.dbs.jfeaturelib.features.ReferenceColorSimilarity;
import de.lmu.ifi.dbs.jfeaturelib.features.SURF;
import de.lmu.ifi.dbs.jfeaturelib.features.Tamura;

/**
 * @author mm.zhang
 * @说明： 从视频字幕的目录中读取相关信息，然后存入 数据库
 * 				若没有字幕信息，则将其存入一个字幕重试队列，每隔一段时间重试该队列
 */
public class ServerMain {
	final static Log LOG = LogFactory.getLog(ServerMain.class);
	static{
		PropertyConfigurator.configure("./config/log4j.properties");
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
//		new LoadConfig();
//		if (!LoadConfig.loadSucess) {
//			LOG.error("ERROR: Load config file failed");
//			return;
//		}
		
		String dstImageDir = "G:\\Code\\Founder\\Image\\ImageSimilar\\Data\\data1";
		String srcImage = dstImageDir + File.separator + "-7789596057982224421_-1612629907220908840_3689352056849555295_20160621180229.jpeg";
		
		List<String> dstImages = FileUtils.getFiles(dstImageDir, ".jpeg", 0);   
        
//		Tamura cedd= new Tamura();
//        SURF cedd= new SURF();
        PHOG cedd = new PHOG();
		try {
			cedd.run(new ColorProcessor(ImageIO.read(new File(srcImage))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		double[] sourceHashCode = cedd.getFeatures().get(0);
        LOG.info("特征维数: " + sourceHashCode.length);  
		
        @SuppressWarnings("rawtypes")
		Map resultMap = new HashMap();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < dstImages.size(); i++)  
        {  
        	try {
        		cedd = new PHOG();
				cedd.run(new ColorProcessor(ImageIO.read(new File(dstImages.get(i)))));
			} catch (IOException e) {
				e.printStackTrace();
			}
    		double[] hashCode = cedd.getFeatures().get(0);
        	
//            int difference = Similarity.hammingDistance(sourceHashCode, hashCode);
            float difference = Similarity.cosDistance(sourceHashCode, hashCode); 
            resultMap.put(dstImages.get(i),difference);
        }  
       long endTime = System.currentTimeMillis();
       System.out.println("提取指纹并比较耗时: " + (endTime - startTime) + "ms");
        
       startTime = System.currentTimeMillis();
       resultMap = Sort.mapSort(resultMap, true);
       endTime = System.currentTimeMillis();
       System.out.println("排序耗时: " + (endTime - startTime) + "ms");
       
        List<ImageAsset> imageAssets = new ArrayList<ImageAsset>();
        ImageAsset imageAsset1 = new ImageAsset();
        imageAsset1.setImageName(FileUtils.getFileName(srcImage, false));
    	imageAsset1.setScore(0);
    	imageAsset1.setImagePath(srcImage);
    	imageAssets.add(imageAsset1);
        
        @SuppressWarnings("rawtypes")
		Iterator<Entry> itor = resultMap.entrySet().iterator();
        while(itor.hasNext()){
        	ImageAsset imageAsset = new ImageAsset();
        	@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) itor.next();
        	String key = (String) entry.getKey();
        	float value =  (Float) entry.getValue();
        	imageAsset.setImageName(FileUtils.getFileName(key, false));
        	imageAsset.setScore(value);
        	imageAsset.setImagePath(key);
        	imageAssets.add(imageAsset);
//        	System.out.println(value + ": " + key);
        }
        
        DisplayImage.ShowImage(imageAssets, 1600, 800, 5);
	
	}
}

