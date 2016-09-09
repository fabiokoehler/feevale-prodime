import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import br.com.prodime.Prodime;
import br.com.prodime.log.LogUtil;
import br.com.prodime.plugin.CT_Window_Level;

public class EFat_ implements PlugIn {

	private static Logger LOGGER = LogUtil.getLogger(Prodime.class);
	private static BufferedImage bufferedImage;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public void run(String arg) {

		CT_Window_Level wl = new CT_Window_Level();
		ImagePlus imp = WindowManager.getCurrentImage();
		// imp.getProcessor().setThreshold(wl.HUtoScr(-1024), wl.HUtoScr(-200), 0);

		// processarQuadrante(imp);
		try {
			processarQuadranteThread(imp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		imp.updateAndRepaintWindow();
	}

	public void processarQuadranteThread(ImagePlus image) throws Exception {
		System.out.println("INI: " + SDF.format(new Date()));

		// limit the number of actual threads
		int poolSize = 100;
		ExecutorService service = Executors.newFixedThreadPool(poolSize);
		List<Future<Runnable>> futures = new ArrayList<Future<Runnable>>();

		CT_Window_Level wl = new CT_Window_Level();

		System.out.println(image.getStack().getSize());

		for (int i = 1; i <= image.getStack().getSize(); i++) {

			ImageProcessor processor = image.getStack().getProcessor(i);

			ProcessarImagem processarImagem = new ProcessarImagem(processor);

			Future f = service.submit(processarImagem);
			futures.add(f);

		}

		// wait for all tasks to complete before continuing
		for (Future<Runnable> f : futures) {
			f.get();
		}

		// shut down the executor service so that this thread can exit
		service.shutdownNow();

		image.setDisplayRange(wl.HUtoScr(-250), wl.HUtoScr(250));
		System.out.println("FIM: " + SDF.format(new Date()));

	}

	public void processarQuadrante(ImagePlus image) {

		System.out.println("INI: " + SDF.format(new Date()));

		CT_Window_Level wl = new CT_Window_Level();

		System.out.println(image.getStack().getSize());
		int meioY = image.getHeight() / 2;
		int meioX = image.getWidth() / 2;
		List<Object> valoresPixels = null;
		double valorPixel = 0d;

		for (int i = 1; i <= image.getStack().getSize(); i++) {

			ImageProcessor processor = image.getStack().getProcessor(i);
			int menorY = 1000;
			valoresPixels = new ArrayList<Object>();

			x: for (int x = 0; x < processor.getWidth() - 2; x++) {

				for (int y = 0; y < processor.getHeight() - 2; y++) {

					valorPixel = wl.ScrtoHU(processor.get(x, y));

					if (y <= meioY) {
						// valoresPixels.add( wl.ScrtoHU(image.getPixel(x, y)[0]));
						// System.out.format("%04.2f ", wl.ScrtoHU(image.getPixel(x, y)[0]));

						if (valorPixel < -200 && y > 50 && wl.ScrtoHU(processor.get(x, y - 1)) > -200
								&& wl.ScrtoHU(processor.get(x, y - 5)) > -200 && wl.ScrtoHU(processor.get(x, y - 10)) > -200
								&& wl.ScrtoHU(processor.get(x, y - 20)) > -200 && wl.ScrtoHU(processor.get(x, y - 30)) > -200
								&& wl.ScrtoHU(processor.get(x, y - 50)) > -200) {

							processor.set(x, y, (int) wl.HUtoScr(1000));
							System.out.println("x: " + x + " y: " + y);
							if (menorY > y) {
								menorY = y;
							}
							
							for(int j = 0; j <= y; j++) {
								System.out.println("x: " + x + " y: " + y + " j: " + j);
								processor.set(x, j, (int) wl.HUtoScr(1000));
							}

							continue x;

						}

					}

				}
			}

			for (int x = 0; x < processor.getWidth() - 2; x++) {

				for (int y = 0; y < processor.getHeight() - 2; y++) {

					if (y <= menorY) {
						processor.set(x, y, (int) wl.HUtoScr(1000));
					}

				}
			}

			image.updateImage();

		}

		image.setDisplayRange(wl.HUtoScr(-250), wl.HUtoScr(250));
		System.out.println("FIM: " + SDF.format(new Date()));

	}
}
