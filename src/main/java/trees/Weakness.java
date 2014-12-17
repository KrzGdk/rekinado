package main.java.trees;

/**
 * @author Jacek
 */
public class Weakness {
	public static void calc(){
		int W = Forest.width/10+1;
		int H = Forest.height/10+1;
		TreeGUI[] tree = Forest.getList();
		int[][] map = new int [W][H];
		double[][] force1 = new double [W][H];
		double[][] force2 = new double [W][H];
		double[][] force3 = new double [W][H];
		double[][] force4 = new double [W][H];
		double[][] force = new double [W][H];
		
		for(int i=0; i<Forest.getLength();i++){
			map[(int)(tree[i].x+Forest.width/2)/10][(int)(tree[i].y+Forest.height/2)/10]+=1;
		}
		
		
		double maxF = 10;
		double remM = 7;
		double addF = 4;
		double F;
		
		for(int j=0; j<H; ++j){
			F = maxF;
			for(int i=0; i<W; ++i){
				//wieje lewo -> prawo
				force1[i][j] = F;
				F -= map[i][j]*remM;
				F += addF;
				if(F<0)    F = 0;
				if(F>maxF) F = maxF;
			}
			F = maxF;
			for(int i=0; i<W; ++i){
				//wieje prawo -> lewo
				force2[W-i-1][j] = F;
				F -= map[W-i-1][j]*remM;
				F += addF;
				if(F<0)    F = 0;
				if(F>maxF) F = maxF;
			}
		}
		for(int i=0; i<W; ++i){
			F = maxF;
			for(int j=0; j<H; ++j){
				//wieje dol->gora
				force3[i][j] = F;
				F -= map[i][j]*remM;
				F += addF;
				if(F<0)    F = 0;
				if(F>maxF) F = maxF;
			}
			F = maxF;
			for(int j=0; j<H; ++j){
				//wieje gora->prawo
				force4[i][H-j-1] = F;
				F -= map[i][H-j-1]*remM;
				F += addF;
				if(F<0)    F = 0;
				if(F>maxF) F = maxF;
			}
		}
		for(int i=0; i<W; ++i){
			for(int j=0; j<H; ++j){
				force[i][j] = force1[i][j]+force2[i][j]+force3[i][j]+force4[i][j];
			}
		}

		for(int i=0; i<Forest.getLength();i++){
			tree[i].weakness = force[(int)(tree[i].x+Forest.width/2)/10][(int)(tree[i].y+Forest.height/2)/10]/40;
		}
	}
}
