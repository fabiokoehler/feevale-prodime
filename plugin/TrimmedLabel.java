

/*     */ import ij.IJ;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Label;
/*     */ 
/*     */ class TrimmedLabel extends Label
/*     */ {
/* 673 */   int trim = IJ.isMacOSX() ? 0 : 6;
/*     */ 
/*     */   public TrimmedLabel(String paramString) {
/* 676 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/* 681 */     return new Dimension(super.getMinimumSize().width, super.getMinimumSize().height - this.trim);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 686 */     return getMinimumSize();
/*     */   }
/*     */ }

/* Location:           /Users/koehler/Downloads/ImageJ/source/plugins/CT_Window_Level.jar
 * Qualified Name:     TrimmedLabel
 * JD-Core Version:    0.6.1
 */