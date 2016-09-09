package br.com.prodime;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.Opener;
import ij.plugin.DICOM;
import ij.process.ImageProcessor;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.itk.simple.SimpleITK;

import prodimelibrary.ProdimePericardiumDetect;
import br.com.prodime.log.LogUtil;
import br.com.prodime.plugin.CT_Window_Level;

public class Prodime extends JPanel {

	private static Logger LOGGER = LogUtil.getLogger(Prodime.class);
	private static BufferedImage bufferedImage;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public static void main(String[] args) {

		CT_Window_Level wl = new CT_Window_Level();

		System.out.println(wl.ScrtoHU(71));

		Prodime prodime = new Prodime();
		try {
			prodime.processarImage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawImage(bufferedImage, 5, 5, this);
		g2d.dispose();
	}

	public void processarImage() throws Exception {

		String path = new java.io.File(".").getCanonicalPath() + "/imagens/IM-0001-0040.dcm";

		// String path = "/../../../imagens/IM-0001-0040.dcm";

		ImageJ ij = new ImageJ(null, 0);
		ij.exitWhenQuitting(true);

		Opener opener = new Opener();
		opener.open(path);

		// CT_Window_Level imp = (CT_Window_Level)
		// IJ.runPlugIn("br.com.prodime.plugin.CT_Window_Level", null);

		ImagePlus image = IJ.getImage();

		ProdimePericardiumDetect ppd = new ProdimePericardiumDetect();
		DICOM dicom = new DICOM();
		dicom.open(path);
		
		DICOM result = ppd.execute(dicom, 0, 512, 0, 512);
		
		result.show();
		
		
		System.out.println(image.getStack().getSize());
		
		
		// ij.plugin.filter.ParticleAnalyzer

		// imp.adjustWindow(image, image.getProcessor(), 350);
		// imp.adjustLevel(image, image.getProcessor(), 0);

		// INICIO DO PROCESSAMENTO DO ARRAY DE BYTES DA IMAGEM

		// processarPulmoes(image);
		//processarGordura(image);
		// processarPericardio(image);
		//processarLimiar(image);
		//processarQuadrante(image);
		
		/*
		 * bufferedImage = image.getBufferedImage();
		 * 
		 * byte[] frame = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
		 * 
		 * 
		 * BufferedImage currentImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY); byte[] imgData
		 * = ((DataBufferByte) currentImage.getRaster().getDataBuffer()).getData(); System.arraycopy(frame, 0, imgData, 0, frame.length);
		 * 
		 * 
		 * MeuPanel panel = new MeuPanel(); panel.setImg(currentImage); //panel.criarJanela();
		 */
		
		image.updateAndRepaintWindow();
		LOGGER.info("final main");

		/*
		 * System.out.println("inicio region"); new RegionGrowing(IJ.getImage().getStack(), image.getWidth(), image.getHeight(), 0, -100,
		 * true, 10, true); System.out.println("fim region");
		 */

	}
	
	public void processarQuadrante(ImagePlus image) {

		System.out.println("INICIO: " + SDF.format(new Date()));

		CT_Window_Level wl = new CT_Window_Level();
		ImageProcessor processor = image.getProcessor();

		int meioY = image.getHeight()/2;
		int meioX = image.getWidth()/2;
		List<Object> valoresPixels = new ArrayList<Object>(); 
		
		
		for (int x = 0; x < image.getWidth(); x++) {
			
			for (int y = 0; y < image.getHeight(); y++) {
				
				
				if(y == meioY) {
					//processor.set(x, meioY, 0);
					
				} 
				if(x == meioX && y <= meioY) {
					valoresPixels.add( wl.ScrtoHU(image.getPixel(x, y)[0]));
					System.out.format("%04.2f ", wl.ScrtoHU(image.getPixel(x, y)[0]));
					processor.set(meioX, y, 0);
					
				} 
				
			}

		}
		
		image.setDisplayRange(wl.HUtoScr(-250), wl.HUtoScr(250));
		System.out.println("INICIO: " + SDF.format(new Date()));

	}

	public void processarLimiar(ImagePlus image) {

		System.out.println("INICIO: " + SDF.format(new Date()));

		CT_Window_Level wl = new CT_Window_Level();
		ImageProcessor processor = image.getProcessor();
		
		processor.setThreshold(wl.HUtoScr(-1024), wl.HUtoScr(-200), 0);
		

		/*for (int x = 0; x < image.getWidth(); x++) {

			for (int y = 0; y < image.getHeight(); y++) {

				double valorPixel = wl.ScrtoHU(processor.getPixel(x, y));

				if (valorPixel < -200 || valorPixel > -20) {
					processor.set(x, y, 200);
				}

			}

		}*/
		
		//image.setDisplayRange(wl.HUtoScr(-250), wl.HUtoScr(250));
		System.out.println("INICIO: " + SDF.format(new Date()));

	}

	public void processarPulmoes(ImagePlus image) {

		System.out.println("INICIO: " + SDF.format(new Date()));

		CT_Window_Level wl = new CT_Window_Level();
		ImageProcessor processor = image.getProcessor();

		for (int x = 0; x < image.getWidth(); x++) {

			for (int y = 0; y < image.getHeight(); y++) {

				double valorPixel = wl.ScrtoHU(processor.getPixel(x, y));

				if (valorPixel < -200 || valorPixel > -20) {
					processor.set(x, y, 200);
				}

				/*
				 * if(count == i) { //System.out.format("%04.2f ", imp.ScrtoHU(image.getPixel(i, j)[0])); System.out.format("%04.2f ",
				 * imp.ScrtoHU(processor.getPixel(i, j))); } else { //System.out.format("\n%04.2f ", imp.ScrtoHU(image.getPixel(i, j)[0]));
				 * System.out.format("\n%04.2f ", imp.ScrtoHU(processor.getPixel(i, j))); count = i; }
				 */

			}

		}
		
		image.setDisplayRange(wl.HUtoScr(-250), wl.HUtoScr(250));
		System.out.println("INICIO: " + SDF.format(new Date()));

	}

	public void processarGordura(ImagePlus image) {
		System.out.println("INICIO: " + SDF.format(new Date()));

		CT_Window_Level wl = new CT_Window_Level();
		ImageProcessor processor = image.getProcessor();
		// threshold na gordura com limites iguais a -250 e -20
		for (int x = 0; x < image.getWidth(); x++) {

			for (int y = 0; y < image.getHeight(); y++) {

				double valorPixel = wl.ScrtoHU(processor.getPixel(x, y));

				if (valorPixel < -190 || valorPixel > -30) {
					processor.set(x, y, 200);
				}

			}

		}
		System.out.println("FIM   : " + SDF.format(new Date()));
	}

	public void processarPericardio(ImagePlus image) {

		CT_Window_Level wl = new CT_Window_Level();
		ImageProcessor processor = image.getProcessor();
		System.out.println(new Date());
		// threshold na gordura com limites iguais a -250 e -20
		for (int x = 0; x < image.getWidth(); x++) {

			for (int y = 0; y < image.getHeight(); y++) {

				double valorPixel = wl.ScrtoHU(processor.getPixel(x, y));

				if (valorPixel < -80 || valorPixel > 100) {
					processor.set(x, y, 200);
				}

				/*
				 * if(count == i) { //System.out.format("%04.2f ", imp.ScrtoHU(image.getPixel(i, j)[0])); System.out.format("%04.2f ",
				 * imp.ScrtoHU(processor.getPixel(i, j))); } else { //System.out.format("\n%04.2f ", imp.ScrtoHU(image.getPixel(i, j)[0]));
				 * System.out.format("\n%04.2f ", imp.ScrtoHU(processor.getPixel(i, j))); count = i; }
				 */

			}

		}
		System.out.println(new Date());
	}

}
