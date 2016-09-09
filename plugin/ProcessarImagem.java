import ij.process.ImageProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import br.com.prodime.plugin.CT_Window_Level;

public class ProcessarImagem implements Runnable {

	private ImageProcessor processor;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public ProcessarImagem(ImageProcessor processor) {
		this.processor = processor;
	}

	public void run() {
		processarLimiteSuperior();
		processarLimiteLateral();
		processarLimiteInferior();


	}
	
	public void processarLimiteSuperior () {
		
		System.out.println("INI SUP: " + SDF.format(new Date()));

		CT_Window_Level wl = new CT_Window_Level();

		int meioY = processor.getHeight() / 3;
		double valorPixel = 0d;
		int menorY = 1000;
		boolean validaHistorico = true;
		
		int limite = (int) wl.HUtoScr(1000);

		x: for (int x = 0; x < processor.getWidth(); x++) {

			for (int y = 0; y < processor.getHeight(); y++) {

				valorPixel = wl.ScrtoHU(processor.get(x, y));
				int sim = 0, nao = 0;

				if (y <= meioY) {
					// valoresPixels.add( wl.ScrtoHU(image.getPixel(x, y)[0]));
					// System.out.format("%04.2f ", wl.ScrtoHU(image.getPixel(x, y)[0]));

					if (valorPixel < -200) {
						
						
						for(int listaValidacao = y - 20; listaValidacao <= y + 20; listaValidacao ++) {
							
							if(y >= 20) {
								
								if(y < listaValidacao) {
									if(wl.ScrtoHU(processor.get(x, listaValidacao)) < -200) {
										sim++;
										
									} /*else {
										nao++;
									}*/
									
								}
								
								//busca valores acima e abaixo de Y para confirmar que o mesmo est‡ em uma zona escura e passou por musculo/gordura
								if(y > listaValidacao) {
									
									if(wl.ScrtoHU(processor.get(x, listaValidacao)) > -200) {
										sim++;
									} /*else {
										nao++;
									}*/
								} 
								
							}
						}
						
						//System.out.println("sim: " + sim + "nao: " + nao);
						
						if(sim >= 30) {
							processor.set(x, y, limite);
							// System.out.println("x: " + x + " y: " + y);
							if (menorY > y) {
								menorY = y;
							}
							
							//seta cor nos pixels acima da marca definida
							for(int j = 0; j <= y; j++) {
								processor.set(x, j, limite);
							}
	
							continue x;
						}
						
					}

				}

			}
		}

		menorY = menorY - 10;
		
		for (int x = 0; x < processor.getWidth(); x++) {

			for (int y = 0; y < processor.getHeight(); y++) {

				if (y <= menorY) {
					processor.set(x, y, limite);
				}
				
			}
		}

		
		System.out.println("FIM SUP: " + SDF.format(new Date()));
		

	}
	
	public void processarLimiteLateral () {
		
		System.out.println("INI LAT: " + SDF.format(new Date()));

		CT_Window_Level wl = new CT_Window_Level();

		int limite = (int) wl.HUtoScr(1000);
		
		for (int x = 0; x < processor.getWidth(); x++) {

			for (int y = 0; y < processor.getHeight(); y++) {

				if(wl.ScrtoHU(processor.get(x, y)) < -200) {
					processor.set(x, y, limite);
				}
				
			}
		}

		
		System.out.println("FIM LAT: " + SDF.format(new Date()));
		
	}
	
	public void processarLimiteInferior () {
		
		System.out.println("INI INF: " + SDF.format(new Date()));

		CT_Window_Level wl = new CT_Window_Level();

		int meioY = processor.getHeight() / 3;
		int limite = (int) wl.HUtoScr(1000);
		
		x:
		for (int x = processor.getWidth() - 1; x > 0; x--) {

			for (int y = processor.getHeight() - 1 ; y > 0; y--) {
				
				if(y >= meioY) {
					
					
				
					if(wl.ScrtoHU(processor.get(x, y)) == 1000 && wl.ScrtoHU(processor.get(x, y - 20)) == 1000) {
						for(int j = y; j <= processor.getHeight() - 1; j++) {
							processor.set(x, j, limite);
						}
						continue x;
						
					}
				}
				
			}
		}
		
		System.out.println("FIM INF: " + SDF.format(new Date()));
		
	}


}
