package tech.supdev.perlinnoiser;

import java.util.Random;

public class PerlinNoiser {
	
	static int[] permutationsTable = new int[] {
			 235,249,14,239,107,49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,
			 127, 4,150,254,138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,
			 112,104,218,246,97,228,251,34,242,193,238,210,144,12,191,179,162,241,81,51,145,
			 153,101,155,167, 43,172,9,129,22,39,253, 19,98,108,110,79,113,224,232,178,185,
			 74,165,71,134,139,48,27,166,77,146,158,231,83,111,229,122,60,211,133,230,220,
			 187,208, 89,18,169,200,196,135,130,116,188,159,86,164,100,109,198,173,186,3,
			 47,16,58,17,182,189,28,42,223,183,170,213,119,248,152, 2,44,154,163, 70,221,
			 64,52,217,226,250,124,123,5,202,38,147,118,126,255,82,85,212,207,206,59,227,
			 203,117,35,11,32,57,177,33,88,237,149,56,87,174,20,125,136,171,168, 68,175,
			 105,92,41,55,46,245,40,244,102,143,54, 65,25,63,161,1,216,80,73,209,76,132,
			 142,8,99,37,240,21,10,23,190, 6,148,247,120,234,75,0,26,197,62,94,252,219,
			 151,160,137,91,90,15,131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,
			 156,180};
	
	private long seed;
	
	public PerlinNoiser(long seed) {
		this.seed = seed;
	}
	
	public PerlinNoiser(String dimension) {
		this.seed = new Random().nextLong();
	}
	
	
	public double noise2D(double x, double y){
    	int xi = (int) Math.floor(x) & 255;
    	int yi = (int) Math.floor(y) & 255;
    	int g1 = permutationsTable[permutationsTable[xi] + yi];
    	int g2 = permutationsTable[permutationsTable[xi + 1] + yi];
    	int g3 = permutationsTable[permutationsTable[xi] + yi + 1];
    	int g4 = permutationsTable[permutationsTable[xi + 1] + yi + 1];
    	
    	double xf = x - Math.floor(x);
    	double yf = y - Math.floor(y);
    	
    	double d1 = grad2D(g1, xf, yf);
    	double d2 = grad2D(g2, xf - 1, yf);
    	double d3 = grad2D(g3, xf, yf - 1);
    	double d4 = grad2D(g4, xf - 1, yf - 1);
    	
    	double u = fade2D(xf);
    	double v = fade2D(yf);
    	
    	double x1Inter = lerp2D(u, d1, d2);
    	double x2Inter = lerp2D(u, d3, d4);
    	double yInter = lerp2D(v, x1Inter, x2Inter);
    	
    	return yInter;
    	
    }
	
	public double noise3D(double x, double y, double z) {
	      int X = (int)Math.floor(x) & 255,
	          Y = (int)Math.floor(y) & 255,
	          Z = (int)Math.floor(z) & 255;
	      x -= Math.floor(x);
	      y -= Math.floor(y);
	      z -= Math.floor(z);
	      double u = fade3D(x),
	             v = fade3D(y),
	             w = fade3D(z);
	      int A = permutationsTable[X  ]+Y, AA = permutationsTable[A]+Z, AB = permutationsTable[A+1]+Z,
	          B = permutationsTable[X+1]+Y, BA = permutationsTable[B]+Z, BB = permutationsTable[B+1]+Z;

	      return lerp3D(w, lerp3D(v, lerp3D(u, grad3D(permutationsTable[AA  ], x  , y  , z   ),
	                                     grad3D(permutationsTable[BA  ], x-1, y  , z   )), 
	                             lerp3D(u, grad3D(permutationsTable[AB  ], x  , y-1, z   ), 
	                                     grad3D(permutationsTable[BB  ], x-1, y-1, z   ))),
	                     lerp3D(v, lerp3D(u, grad3D(permutationsTable[AA+1], x  , y  , z-1 ), 
	                                     grad3D(permutationsTable[BA+1], x-1, y  , z-1 )), 
	                             lerp3D(u, grad3D(permutationsTable[AB+1], x  , y-1, z-1 ),
	                                     grad3D(permutationsTable[BB+1], x-1, y-1, z-1 ))));
	}
	
	private static double fade3D(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}
	
	private static double lerp3D(double t, double a, double b) {
		return a + t * (b - a);
	}
	
	private static double grad3D(int hash, double x, double y, double z) {
		int h = hash & 15;                      
	    double u = h<8 ? x : y,                
	    v = h<4 ? y : h==12||h==14 ? x : z;
	    return ((h&1) == 0 ? u : -u) + ((h&2) == 0 ? v : -v);
	}
    
    private static double lerp2D(double amount, double left, double right) {
		return ((1 - amount) * left + amount * right);
	}
    
    private static double fade2D(double t) { 
    	return t * t * t * (t * (t * 6 - 15) + 10); 
    }
    
    private static double grad2D(int hash, double x, double y){
    	switch(hash & 3){
    	case 0: return x + y;
    	case 1: return -x + y;
    	case 2: return x - y;
    	case 3: return -x - y;
    	default: return 0;
    	}
    }
	
	
	public long getSeed() {
		return this.seed;
	}

}
