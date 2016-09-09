package br.com.prodime;

import ij.plugin.DICOM;
import prodimelibrary.ProdimePericardiumDetect;

public class TesteLucas {
	
	public static void main(String[] args) throws Exception {
		
		ProdimePericardiumDetect ppd = new ProdimePericardiumDetect();
		String path = new java.io.File(".").getCanonicalPath() + "/imagens/IM-0001-0040.dcm";
		DICOM dicom = new DICOM();
		dicom.open(path);
		ppd.execute(dicom, 0, dicom.getWidth(), 0, dicom.getHeight());
		dicom.show();
		//result.show();
		
		
		
	}

}
