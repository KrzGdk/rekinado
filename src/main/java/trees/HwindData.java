package main.java.trees;


/**
 * Created by Mariusz on 2014-11-16.
 */

/**
 * todo remove hardcoded values
 */
public class HwindData {
	//12m ? pień? really?

    public double diameter = 12;     /** srednica drzewa na wysokosci piersi czlowieka (1.3m) */
    public double mor = 39.1;          /** wspolczynnik pekania drewna */
    public double moe = 7000;          /** wspolczynnik elastycznosci */
    public double friction = 0.29;     /** wspolczynnik tarcia */
    public double crownMass = 18;
//    public double stemMass;
	public double f_RW = 0.3;         /** stosunek wagi gleby wokol korzeni do masy calego drzewa*/
    public double rootMass = 485;
	public double rootDepth = 0.48;
	public int crownHeight = 5;
	public double crownWidth = 3.5;
	public int spacing = 5;
	public double freeFrontSpace = 15; /** wolna przestrzen przed sciana lasu */
//    public double maxTreeHeight;
    public double airDensity = 1.226;
}
