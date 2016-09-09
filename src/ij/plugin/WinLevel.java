package ij.plugin;

/*     */ import ij.gui.ImageWindow;
/*     */ 
/*     */ class WinLevel
/*     */ {
/*     */   private int window;
/*     */   private int level;
/*     */   private int imgWindow;
/*     */   private int imgLevel;
/*     */   private int fullWindow;
/*     */   private int fullLevel;
/* 692 */   private int offSigned = 0; private int preset = -1;
/*     */   private double slope;
/*     */   private double intercept;
/*     */   private ImageWindow win;
/*     */ 
/*     */   WinLevel(ImageWindow paramImageWindow, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
/*     */   {
/* 697 */     this.win = paramImageWindow;
/* 698 */     this.slope = paramDouble1;
/* 699 */     this.intercept = paramDouble2;
/* 700 */     this.window = paramInt1;
/* 701 */     this.level = paramInt2;
/* 702 */     this.imgWindow = paramInt3;
/* 703 */     this.imgLevel = paramInt4;
/* 704 */     this.fullWindow = paramInt5;
/* 705 */     this.fullLevel = paramInt6;
/* 706 */     this.offSigned = paramInt7;
/* 707 */     this.preset = paramInt8;
/*     */   }
/*     */ 
/*     */   WinLevel(WinLevel paramWinLevel) {
/* 711 */     this.win = paramWinLevel.win;
/* 712 */     this.slope = paramWinLevel.slope;
/* 713 */     this.intercept = paramWinLevel.intercept;
/* 714 */     this.window = paramWinLevel.window;
/* 715 */     this.level = paramWinLevel.level;
/* 716 */     this.imgWindow = paramWinLevel.imgWindow;
/* 717 */     this.imgLevel = paramWinLevel.imgLevel;
/* 718 */     this.fullWindow = paramWinLevel.fullWindow;
/* 719 */     this.fullLevel = paramWinLevel.fullLevel;
/* 720 */     this.offSigned = paramWinLevel.offSigned;
/* 721 */     this.preset = paramWinLevel.preset;
/*     */   }
/*     */ 
/*     */   WinLevel()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ImageWindow getWin()
/*     */   {
/* 730 */     return this.win;
/*     */   }
/*     */ 
/*     */   public int getWindow() {
/* 734 */     return this.window;
/*     */   }
/*     */ 
/*     */   public double getSlope() {
/* 738 */     return this.slope;
/*     */   }
/*     */ 
/*     */   public double getIntercept() {
/* 742 */     return this.intercept;
/*     */   }
/*     */ 
/*     */   public int getLevel() {
/* 746 */     return this.level;
/*     */   }
/*     */ 
/*     */   public int getoffSigned() {
/* 750 */     return this.offSigned;
/*     */   }
/*     */ 
/*     */   public int getimgWindow() {
/* 754 */     return this.imgWindow;
/*     */   }
/*     */ 
/*     */   public int getimgLevel() {
/* 758 */     return this.imgLevel;
/*     */   }
/*     */ 
/*     */   public int getfullWindow() {
/* 762 */     return this.fullWindow;
/*     */   }
/*     */ 
/*     */   public int getfullLevel() {
/* 766 */     return this.fullLevel;
/*     */   }
/*     */ 
/*     */   public int getPreset() {
/* 770 */     return this.preset;
/*     */   }
/*     */ }

/* Location:           /Users/koehler/Downloads/ImageJ/source/plugins/CT_Window_Level.jar
 * Qualified Name:     WinLevel
 * JD-Core Version:    0.6.1
 */