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
	private static int width;
	private static int height;
	
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
		Random rand = new Random();
		trees = new TreeGUI[length];
		
		int nX, nY, fails = 0, k = 0;
		
		for(int i=0; i<length;i++){
			while (fails<100){
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
		
		for(int i=0; i<length;i++){
			if(trees[i].distance(temp)<spacing) return false;
		}
		return true;
	}

		
	public static void fillPaths(int spacing, int length){
		
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
