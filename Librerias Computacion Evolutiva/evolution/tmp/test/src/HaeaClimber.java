//Services

//Optimization classes

//Evolution classes


//Additional classes

import lsgo_benchmark.*;

/**
 * <p>Title: HAEATest</p>
 * <p>Description: Test of Hybrid Adaptive Genetic Algorithm</p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: Kunsamu</p>
 * @author Jonatan Gomez
 * @version 1.0
 */
public class HaeaClimber{
    public static int opers;
    public static int D;
    public static double[] min;
    public static double[] max;
    public static double[] delta;
    public static double[] rates = null; 
    
    public static int nextDim(){
        return (int)(D*Math.random());
    }
    
    public static int nextInt( int n ){
        return (int)(n*Math.random());
    }
    
    public static int nextOper(){
        if( rates != null ){
            return roulette(rates);
        }else{
            return nextInt(opers);
        }
    }
    
    /**
     * Returns a set of random double numbers
     * @param m The total number of random numbers
     */
    public static void raw(double[] v, int m) {
        for (int i = 0; i < m; i++) {
            v[i] = Math.random();
        }
    }

    /**
     * Returns a set of random double numbers
     * @param m The total number of random numbers
     * @return A set of m random double numbers
     */
    public static double[] random(int m) {
        double[] v = null;
        if (m > 0) {
            v = new double[m];
            raw(v, m);
        }
        return v;
    }
    
    
    /**
     * Returns a random double number following the standar gaussian distribution
     * @param x Inverse value (cumulative probability)
     * @return A random double number
     */
    public static double nextGaussian() {
        double x = Math.random();
        double y;
        double r;
        do {
            x = 2.0 * Math.random() - 1.0;
            y = 2.0 * Math.random() - 1.0;
            r = x * x + y * y;
        } while (r >= 1.0);

        double z = Math.sqrt( -2.0 * Math.log(r) / r);
        return (y * z);
    }
    
    public static double[] one_dim_gauss_mutation( double[] a, double sigma ){        
          double[] b = a.clone();
          int pos = nextDim();
          double x = b[pos];
          double y = nextGaussian() * sigma;
          x += y;
          if (x < 0.0) {
              x = 0.0;
          } else {
              if (x > 1.0) {
                  x = 1.0;
              }
          }
          b[pos] = x;
          return b;
    }
    
  /**
   * Modifies the number in a random position for a guassian value with mean
   * thevalue encoded in the genome and sigma given as attribute
   * @param gen Genome to be modified
   * @return Index of the real modified
   */
   public static double[] gauss_mutation(double[] a, double sigma) {
          double[] b = a.clone();
          double[] d = random(a.length);
          for( int i=0; i<d.length; i++ ){
              if( Math.random() > 0.02 ){
                  d[i] = 0.0;
              }
          }
          sphere_normalize(d);
          double gNumber = nextGaussian();
          for( int pos=0; pos<b.length; pos++ ){
              double x = b[pos];
              double y = gNumber * d[pos] * sigma;
              x += y;
              if (x < 0.0) {
                  x = 0.0;
              } else {
                  if (x > 1.0) {
                      x = 1.0;
                  }
              }
              b[pos] = x;
          }
          return b;
    }
   
   public static double[] create( int n, double val ){
       double[] x = new double[n];
       for( int i=0; i<n; i++ ){
           x[i] = val;
       }
       return x;
   }
    
    
    public static void init( double[] x ){
        double v = 1.0 / x.length;
        for( int i=0; i<x.length; i++ ){
            x[i] = v;
        }
    }
    
    public static double norm( double[] x ){
        double s = 0.0;
        for( int i=0; i<x.length; i++ ){
            s += x[i]*x[i];
        }
        return Math.sqrt(s);
    }
    
    public static double[] dif( double[] y, double[] x ){
        double[] d = new double[x.length];
        for( int i=0; i<d.length; i++ ){
            d[i] = y[i] - x[i];
        }
        return d;
    }    
    

    public static void sphere_normalize( double[] x ){
        double s = norm(x);
        for( int i=0; i<x.length; i++ ){
           x[i] /= s;
        }
    }

    public static void normalize( double[] x ){
        double s = 0.0;
        for( int i=0; i<x.length; i++ ){
            s += x[i];
        }
        for( int i=0; i<x.length; i++ ){
           x[i] /= s;
        }
    }

    public static int roulette( double[] rates ){
        double x = Math.random();
        int i = 0;
        while( i<rates.length && x >= rates[i] ){
            x -= rates[i];
            i++;
        }
        return i;
    }

    public static void reward( double[] x, int i ){
        x[i] *= (1.0+Math.random());
        normalize(x);
    }

    public static void punish( double[] x, int i ){
        x[i] *= Math.random();
        normalize(x);
    }
    
    public static double[] scale( double[] x){
        double[] y = x.clone();
        auto_scale(y);
        return y;
    }

    public static void auto_scale( double[] x){
        for( int i=0; i<x.length; i++){
            x[i] = min[i] + delta[i] * x[i];
        }
    }
    
    //public static void update( double[] x, double[] y, double[] delta )
    
    public static void main( String[] args ){
        // Function being optimized
        // OptimizationFunction function = new MaxOnes();
        // OptimizationFunction function = new Rastrigin();
        Function lsgo_f = new F11();        
        D = lsgo_f.getDimension();
        min = create(D, lsgo_f.getMin());
        max = create(D, lsgo_f.getMax());
        delta = dif(max, min);
        
        int nSigmas = 7;
        double[] sigma = new double[nSigmas];
        double s = 0.1;
        for(int i=0; i<nSigmas; i++ ){
            sigma[i] = s;
            s *= 0.1;
        }
        
        opers = nSigmas * 2;
        
        boolean haea = false;
        if( haea ){
            rates = new double[opers];
            init(rates);
        }
        
        boolean trace = false;
        // Evolution generations
        int MAXITER = 3000000;
        double[] x = random(D);
        double fx = lsgo_f.compute(scale(x));

        System.out.println( 1 + "," + fx + "," + x[0] );
        for( int i=2; i<=MAXITER; i++){
            s = 1.0 + Math.random() * nSigmas;
            s = Math.pow(0.1, s);
            double[] y;            
            if( Math.random() < 0.5 ){
                y = gauss_mutation(x, s);
            }else{
                y = one_dim_gauss_mutation(x, s);
            }
            
            /*int op = nextOper();
            double[] y;            
            if( op < nSigmas ){
                //y = one_dim_gauss_mutation(x, sigma[op]);
                y = gauss_mutation(x, sigma[op]);
            }else{
                y = one_dim_gauss_mutation(x, sigma[op-nSigmas]);
                //y = gauss_mutation(x, sigma[op-nSigmas]);
            }*/
            
            //System.out.println(x[0]);
            double fy = lsgo_f.compute(scale(y));
            if( fy < fx ){
                x = y;
                fx = fy;
                //if( haea ) reward(rates, op);
            }else{
                if(trace) System.out.print("-");
                //if( haea ) punish(rates, op);
                if( fy == fx ){
                    x = y;
                }
            }
            
            /*if( i % 500000 == 0 ){
                for( int k=0; k<sigma.length; k++ ){
                    for( int l=0; l<sigma[k].length; l++ ){
                        sigma[k][l] /= 0.1;
                    }
                }
            }*/
            if(trace) System.out.println();
            if( i % 100000 == 0 ){
                System.out.print( i + "," + fx + "," + x[0] );                
                System.out.println();
            }
            
        }
        
        System.out.print(fx + ":=" );
        System.out.println();
        //tracer.close();
    }
}