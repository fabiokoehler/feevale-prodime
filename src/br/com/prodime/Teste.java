package br.com.prodime;

import ij.plugin.DICOM;
import ij.process.ImageProcessor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Teste {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		System.out.println("INICIO: " + sdf.format(new Date()));
		
		int slope = 1;
		int intercept = -1024;
		
		//caminho para o arquivo DICOM
		String path = new java.io.File(".").getCanonicalPath() + "/imagens/IM-0001-0040.dcm";

		DICOM dicom = new DICOM();
		dicom.open(path);
		
		ImageProcessor ip = dicom.getProcessor();
		
		ip.setThreshold(0, 600, 0);
		
		
		
		//Properties properties = dicom.getProperties();
		//0028,1052  Rescale Intercept: -1024 
		//0028,1053  Rescale Slope: 1 
		
		//threshold do pericardio com limites iguais a -250 e -20
//		for (int x = 0; x < dicom.getWidth(); x++) {
//			
//			for (int y = 0; y < dicom.getHeight(); y++) {
//				
//				int valorPixelUH = dicom.getPixel(x, y)[0] * slope + intercept;
//				//System.out.println(valorPixelUH);
//				
//				if(valorPixelUH < -50 || valorPixelUH > 0) {
//					dicom.getProcessor().set(x, y, 200);
//				}
//				
//				/*if(count == i) {
//					//System.out.format("%04.2f ", imp.ScrtoHU(image.getPixel(i, j)[0]));
//					System.out.format("%04.2f ", imp.ScrtoHU(processor.getPixel(i, j)));
//				} else {
//					//System.out.format("\n%04.2f ", imp.ScrtoHU(image.getPixel(i, j)[0]));
//					System.out.format("\n%04.2f ", imp.ScrtoHU(processor.getPixel(i, j)));
//					count = i;
//				}*/
//				
//				
//			}
//			
//		}

		MeuPanel panel = new MeuPanel();
		panel.setImg(dicom.getBufferedImage());
		panel.criarJanela();
		
		System.out.println("FIM   : " + sdf.format(new Date()));

		
	}

}
