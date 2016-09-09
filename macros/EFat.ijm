// ---------------------------------- INI PRINCIPAL ---------------------------------------------------------
	
	getTime();
	
	//setBatchMode(true);
	run("Image Sequence...", "open=[/Users/koehler/Documents/work_prodime/feevale-prodime/imagens/CorCTALow__20__B25f_095_19] number=53 starting=1 increment=1 scale=100 file=[] or=[] sort");
	//run("EFat ");
	
	limparLimiteSuperior();
	processarPulmoes();
	processarLimiteInferior();
	processarResto();
	processarParticulasAvulsas();
	processarGordura();
	
	if (isOpen("ROI Manager")) {
		selectWindow("ROI Manager");
		run("Close");
	}
	if (isOpen("Results")) {
		selectWindow("Results");
		run("Close");
	}
	
	//setMinAndMax(849, 1199);
	//roiManager("Reset");
	//resetThreshold();
	//updateDisplay();
	
	getTime();
	
// ---------------------------------- FIM PRINCIPAL ---------------------------------------------------------	

function processarGordura() {

	//(-190, -30) UH
	setThreshold(834, 994;
	
	run("Set Measurements...", "area centroid bounding slice redirect=None decimal=3");
	run("Analyze Particles...", "size=0-Infinity circularity=0.00-1.00 show=Nothing add clear stack");
	
	totalGordura = 0;
	
	for(i = 0; i < nResults(); i++) {

		slice = getResult("Slice",i);
		setSlice(slice);
		
		area = getResult("Area",i);
		
		totalGordura = totalGordura + area;
		
		/*if(area <= 2000.000) {
			roiManager("select", i);
			roiManager("Fill");
		}*/ 
	}
	
	print(totalGordura);
	
	roiManager("Reset");
	resetThreshold();
	
}

function limparLimiteSuperior() {


	setThreshold(0, 824;
	
	run("Set Measurements...", "centroid bounding slice redirect=None decimal=3");
	run("Analyze Particles...", "size=200-Infinity circularity=0.00-1.00 show=Nothing add clear stack");
	
	for(i = 0; i < nResults(); i++) {

		slice = getResult("Slice",i);
		setSlice(slice);
		
		//pixel em mm, deveria pegar da tag DICOM xxxx,xxxx
		y = getResult("BY",i) / 0.4296875;
		
		if(y < 30) {
			roiManager("select", i);
			roiManager("Fill");
		} 
	}
	roiManager("Reset");
	resetThreshold();
	
}

function processarPulmoes() {
		
	setThreshold(24, 824;
	meioY = getHeight / 2;
	width = getWidth();	
	
	run("Set Measurements...", "centroid bounding slice redirect=None decimal=3");
	run("Analyze Particles...", "size=200-Infinity circularity=0.00-1.00 show=Nothing add clear stack");
	
	sliceAnterior = -1;
	
	for(i = 0; i < nResults(); i++) {

		slice = getResult("Slice",i);
		setSlice(slice);
		roiManager("select", i);
		roiManager("Fill");
		
		if(slice == sliceAnterior) {
			pontoMaisAltoPulmoesTemp = getResult("BY",i) / 0.4296875;
			if(pontoMaisAltoPulmoesTemp > pontoMaisAltoPulmoes) {
				pontoMaisAltoPulmoes = pontoMaisAltoPulmoesTemp;
			}
		} else {
		
			//pixel em mm, deveria pegar da tag DICOM xxxx,xxxx
			pontoMaisAltoPulmoes = getResult("BY",i) / 0.4296875;
			
			for (x = 0; x <= getWidth(); x++) {
			
				for (y = 0; y <= meioY; y++) {
					if (y <= pontoMaisAltoPulmoes - 5) {
						setPixel(x, y, 0);
					}
				}
			}
			sliceAnterior = slice;
		}
	}
	
	resetThreshold();

}

function processarParticulasAvulsas() {
	
	setThreshold(24, 2000;
	
	meioY = getHeight / 2;
	width = getWidth();	
	
	run("Set Measurements...", "area centroid bounding slice redirect=None decimal=3");
	run("Analyze Particles...", "size=0-Infinity circularity=0.00-1.00 show=Nothing add clear stack");
	
	for(i = 0; i < nResults(); i++) {

		slice = getResult("Slice",i);
		setSlice(slice);
		
		area = getResult("Area",i);
		if(area <= 2000.000) {
			roiManager("select", i);
			roiManager("Fill");
		} 
	}
	roiManager("Reset");
	resetThreshold();


}

function processarResto() {
		
	meioY = getHeight / 3;
	width = getWidth();	
	for(i = 1; i <= nSlices(); i++) {
		setSlice(i);
		for (x = 0; x <= getWidth(); x++) {
			
			continua = true;
			for (y = 0; y <= meioY; y++) {
				if(continua) {
				
					if (getPixel(x,y) ==  0 && getPixel(x, y-1) > 0) {
						
						for(a = y; a >= 0; a-- ) {
							setPixel(x, a, 0);
						}
						
						continua = false;
					}
				
				}
			}
		}
	}
}

function processarLimiteSuperiorMacro() {
	
	slope = 1; //getInfo(DICOM_TAG);
	intercept = -1024; //getInfo(DICOM_TAG);
	height = getHeight();	
	width = getWidth();	
		
	meioY = height / 3;
	valorPixel = 0;
	validaHistorico = true;
	gordura = 0;
	pulmao = 0;
	
	continueY = true;
	
	for(i = 1; i <= nSlices(); i++) {
		menorY = 1000;
		
		setSlice(i);
		
		for (x = 0; x < width; x++) {
			
			
			continueY = true;
			
			for (y = 0; y <= meioY; y++) {
				//print("x: " + x + " y: " + y);
				if(continueY) {
					valorHU = getPixel(x,y)*slope+intercept;
					valorAnteriorHU = getPixel(x,y)*slope+intercept;
				
					if (valorHU < -200 && valorAnteriorHU > -200 && valorAnteriorHU < 300) {
					
						gordura = 0;
						pulmao = 0;
						
						for(a = y - 10; a <= y + 10; a++) {
							
							valorHUPixelHist = getPixel(x,a)*slope+intercept;
							
							//pixel anterior tem que ser gordura
							if(a < y && valorHUPixelHist > -200 && valorHUPixelHist < 400) {
								gordura++;
							}	
							
							//pixel posterior tem que ser pulmao 
							if(a > y && valorHUPixelHist < -200) {
								pulmao++;
							}
						}
						//print("gordura: " + gordura + " pulmao: " + pulmao);
						if(gordura >=7 && pulmao >= 7 ) {
							
							for(j = 0; j <= y; j++) {
								setPixel(x, j, 2000);
							}
							
							if (menorY > y) {
								menorY = y;
							}
							continueY = false;
						}
					}
				}
			}
		}	
		
		for (x = 0; x < width; x++) {
	
			for (y = 0; y < meioY; y++) {
	
				if (y <= menorY - 5) {
					setPixel(x, y, 2000);
				}
				
			}
		}
	}
	
}
		
function processarLimiteSuperior() {

	slope = 1; //getInfo(DICOM_TAG);
	intercept = -1024; //getInfo(DICOM_TAG);
	height = getHeight();	
	width = getWidth();	
		
	meioY = height / 2;
	valorPixel = 0;
	menorY = 1000;
	validaHistorico = true;
	
	limite = (-800-intercept)/slope;

	//x: 
	for (x = 0; x < width; x++) {

		for (y = 0; y < height; y++) {

			valorPixel = (getPixel(x,y)-intercept)/slope;
			sim = 0; 
			nao = 0;

			if (y <= meioY) {
				
				if (valorPixel < -200) {
					
					
					for(listaValidacao = y - 20; listaValidacao <= y + 20; listaValidacao ++) {
						
						if(y >= 20) {
							
							if(y < listaValidacao) {
								if((getPixel(x, listaValidacao)*slope+intercept) < -200) {
									sim++;
									
								} 
								
							}
							
							//busca valores acima e abaixo de Y para confirmar que o mesmo est‡ em uma zona escura e passou por musculo/gordura
							if(y > listaValidacao) {
								
								if((getPixel(x, listaValidacao)*slope+intercept) > -200) {
									sim++;
								} 
							} 
							
						}
					}
					
					if(sim >= 30) {
						setPixel(x, y, limite);
						if (menorY > y) {
							menorY = y;
						}
						
						//seta cor nos pixels acima da marca definida
						for(j = 0; j <= y; j++) {
							setPixel(x, j, limite);
						}

						//continue x;
					}
					
				}

			}

		}
	}

	menorY = menorY - 10;
	
	for (x = 0; x < width; x++) {

		for (y = 0; y < height; y++) {

			if (y <= menorY) {
				setPixel(x, y, limite);
			}
			
		}
	}
}	

function processarLimiteSuperiorLateral() {
		
	for (int x = 0; x < getWidth(); x++) {

		for (int y = 0; y < getHeight(); y++) {

			if(getPixel(x, y)*slope+intercept) < -200) {
				processor.set(x, y, 0);
			}
		}
	}

}

function processarLimiteInferior() {

	setThreshold(1224,1524;
	
	run("Set Measurements...", "centroid bounding shape slice redirect=None decimal=3");
	run("Analyze Particles...", "size=200-Infinity circularity=0.70-1.00 show=Nothing clear stack");
	
	width = getWidth();
	height = getHeight();
	
	for(i = 0; i < nResults(); i++) {
		
		slice = getResult("Slice",i);
		setSlice(slice);
		
		//calculo do indice de Compactness alterado para a formula utilizada no imagej
		//area = getResult("Area",i);
		//perimetro = getResult("Perim.",i);
		//comp =  (perimetro * perimetro) / area;
		//circ = getResult("Area",i);

		//pixel em mm, deveria pegar da tag DICOM xxxx,xxxx
		y = getResult("BY",i) / 0.4296875;
		aortaX = getResult("X",i) / 0.4296875;
		
		inferior = height - (height / 2.5);
		meioX = width / 2;
		
		//caso for abaixo da metade da imagem, e relativamente ao centro
		if(y > inferior && aortaX >= meioX - 40 && aortaX <=  meioX + 20) {
			for (x = 0; x < width; x++) {
				for (z = y; z < height; z++) {
					setPixel(x, z, 0);
				}
			}
		} 
	}
	
	resetThreshold();
}

function getTime() {
	MonthNames = newArray("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
     DayNames = newArray("Sun", "Mon","Tue","Wed","Thu","Fri","Sat");
     getDateAndTime(year, month, dayOfWeek, dayOfMonth, hour, minute, second, msec);
     TimeString ="Date: "+DayNames[dayOfWeek]+" ";
     if (dayOfMonth<10) {TimeString = TimeString+"0";}
     TimeString = TimeString+dayOfMonth+"-"+MonthNames[month]+"-"+year+"\nTime: ";
     if (hour<10) {TimeString = TimeString+"0";}
     TimeString = TimeString+hour+":";
     if (minute<10) {TimeString = TimeString+"0";}
     TimeString = TimeString+minute+":";
     if (second<10) {TimeString = TimeString+"0";}
     TimeString = TimeString+second;
     print(TimeString);

}
