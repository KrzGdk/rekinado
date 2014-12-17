package main.java.trees;

import java.util.Random;
import main.java.gui.TerrainGUI;

/**
 *
 * @author Jacek Pietras
 */
public class Forest {
	private static TreeGUI[] trees;
	private static int length = 0;
	public static int width;
	public static int height;
	public static Type type = Type.Jednorodny; // type
			
	public enum Type {
		Jednorodny, Losowy, Plama;
	}
	
	public static void setSize(int width, int height){
		Forest.width = width;
		Forest.height = height;
		TerrainGUI.setSize(width+6, height+6);
	}

	public static TreeGUI[] getList() {
		return trees;
	}

	public static int getLength() {
		return length;
	}
		
	public static void fillRegular(int spacing){		
		Forest.length = 0;
		trees = new TreeGUI[(int)(width/spacing+1)*(int)(height/spacing+1)];
		int k = 0;
		
		for(int i=spacing/2; i<width;i+=spacing){
			for(int j=spacing/2; j<height;j+=spacing){
				trees[k++] = new TreeGUI(i-width/2,j-height/2,0);
			}
		}
		length = k;
		sortTrees();
	}
	
	public static void fillRandom(int spacing, int length){
		Forest.length = 0;
		Random rand = new Random();
		trees = new TreeGUI[length];
		
		int nX, nY, fails = 0, k = 0;
		
		for(int i=0; i<length;i++){
			while (fails<100 && k<length){
				nX = rand.nextInt(width )- width/2;
				nY = rand.nextInt(height)-height/2;
				if(goodPlace(nX,nY,spacing)){
					trees[k++] = new TreeGUI(nX, nY, 0);
					Forest.length = k;
					fails = 0;
				}else{
					fails++;
				}
			}
		}
		sortTrees();
	}
	private static boolean goodPlace(int nX, int nY, int spacing) {
		TreeGUI temp = new TreeGUI(nX,nY,0);
		if(nX> width/2) return false;
		if(nX<-width/2) return false;
		if(nY> height/2) return false;
		if(nY<-height/2) return false;
		
		for(int i=0; i<length;i++){
			if(trees[i].distance(temp)<spacing) return false;
		}
		return true;
	}

		
	public static void fillPatch(int spacing, int length, int radius){
		Forest.length = 0;
		Random rand = new Random();
		trees = new TreeGUI[length];
		
		int nX, nY, k = 0;
		double mul;
		int cX = rand.nextInt(width )- width/2;
		int cY = rand.nextInt(height)-height/2;
		
		for(int i=0; i<length;i++){
			mul = 1-Math.sqrt(rand.nextDouble());
			if(rand.nextInt(2) == 1) mul*=-1;
			nX = cX + (int)(radius*mul);
			
			mul = 1-Math.sqrt(rand.nextDouble());
			if(rand.nextInt(2) == 1) mul*=-1;
			nY = cY + (int)(radius*mul);
			
			if(goodPlace(nX,nY,spacing)){
				trees[k++] = new TreeGUI(nX, nY, 0);
				Forest.length = k;
			}
		}
		sortTrees();
	}
	
	
	/**
	 * Sortuje drzewa dystansem od ekranu (Zsort)
	 * 
	 * @param tree tablica drzew
	 * @param ntree liczba drzew
	 */
	private static void sortTrees(){
		TreeGUI temp;
		for(int i=0;i<length-1;i++){
			for(int j=0;j<length-i-1;j++){
				if(trees[j].x-trees[j].y>trees[j+1].x-trees[j+1].y){
					temp = trees[j];
					trees[j] = trees[j+1];
					trees[j+1]= temp;
				}
			}			
		}
	}


}
