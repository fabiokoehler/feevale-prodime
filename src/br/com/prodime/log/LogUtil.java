package br.com.prodime.log;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogUtil {
	
	private static Logger logger;
	
	public static Logger getLogger(Class clazz) {
		
		PropertyConfigurator.configure(new File("").getAbsolutePath() + "/log4j.properties");
		
		logger = Logger.getLogger(clazz);
		
		return logger;
	}

}
