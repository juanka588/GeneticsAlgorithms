/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.util.Vector;
//import sun.misc.VM;
/**
 *
 * @author jgomez
 */
public class RunDividerWCCI2012 {
    public static String path = "/home/jgomez/projects/wcci2012/";
    
    public static void concat(){
        try{
                for( int i=1; i<=20; i++ ){
                    String name = "F" + i + ".txt";
                    String path_in = path + "results-laptop/";
                    String path_out = path + "un-results/";
                    BufferedReader reader =
                        new BufferedReader( new FileReader(path_in + name) );
                    FileWriter out = 
                            new FileWriter(path_out + name, true);
                    System.out.println( "F" + i );
                    String line = reader.readLine();
                    while( line != null ){
                        out.write(line);
                        out.write("\n");
                        line = reader.readLine();
                    }
                    out.close();
                    reader.close();
                }
        }catch( Exception e ){
            e.printStackTrace();
        }
    }
    
    public static String format( double x ){
        System.out.print(x);
        int exp = 0;
        while( x >= 10.0 ){
            exp++;
            x /= 10.0;
        }
        while( x < 1.0 ){
            exp--;
            x *= 10.0;
        }
        int y = (int)Math.round(x*100.0);
        int h = y / 100;
        int l = y % 100;
        String text = "" + h + "." +((l<10)?"0":"") + l + "e"
                +((exp>=0)?"+":"-") + ((Math.abs(exp)<10)?"0":"") + Math.abs(exp);
        System.out.println( "->" + text);
        return text;
    }
    
    public static void stat(){
        try{
            for( int i=1; i<=20; i++ ){
                String name = path + "results/F" + i + ".txt";
                System.out.println(name);
                File f = new File(name);
                BufferedReader reader =
                        new BufferedReader( new FileReader(f) );
                Vector<Double> v = new Vector<>();
                String line = reader.readLine();
                while( line != null ){
                    try{
                        v.add(Double.parseDouble(line));
                    }catch( NumberFormatException ne ){
                    }
                    line = reader.readLine();
                }
                double[] fx = new double[v.size()];
                int runs = fx.length;
                for( int k=0; k<runs; k++ ){
                    fx[k] = v.get(k);
                }
                for( int k=0; k<runs-1; k++ ){
                    for( int l=k+1; l<runs; l++ ){
                        if( fx[k] > fx[l] ){
                            double t = fx[k];
                            fx[k] = fx[l];
                            fx[l] = t;
                        }
                    }
                }
                double avg = 0.0;
                for( int k=0; k<runs; k++ ){
                    avg += fx[k];
                }
                avg /= runs;
                double std = 0.0;
                for( int k=0; k<runs; k++ ){                    
                    std += (fx[k]-avg)*(fx[k]-avg);
                }
                std = Math.sqrt(std/(runs-1));
                double best = fx[0];
                double worst = fx[runs-1];
                double median = fx[runs/2];
                FileWriter out = 
                        new FileWriter(path + "F" + i + "-stat.txt" );
                System.out.println( "F" + i + " done.. ");
                out.write( ""+format(best) );
                out.write("\n");
                out.write( ""+format(median) );
                out.write("\n");
                out.write( ""+format(worst) );
                out.write("\n");
                out.write( ""+format(avg) );
                out.write("\n");
                out.write( ""+format(std) );
                out.close();
                reader.close();
            }
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    public static void graph_stat(){
        try{
            int ITERS = 31;
            int REPS = 14;
            for( int i=1; i<=20; i++ ){                
                double[] min = new double[ITERS];
                double[] max = new double[ITERS];
                double[] avg = new double[ITERS];
                for( int k=0; k<ITERS; k++){
                    min[k] = Double.MAX_VALUE;
                    max[k] = Double.MIN_VALUE;
                    avg[k] = 0.0;
                }
                    
                for( int r=0; r<REPS; r++ ){
                    String name = path + "results/F" + i + "-" + r + "-iter.txt";
                    BufferedReader reader = new BufferedReader( new FileReader(name) );
                    for( int k=0;k<ITERS;k++){
                        String line = reader.readLine();
                        String[] split = line.split(",");
                        double fx = Double.parseDouble(split[1]);
                        if( fx < min[k] ){
                            min[k] = fx;
                        }else{
                            if( fx > max[k] ){
                                max[k] = fx;
                            }
                        }
                        avg[k] += fx;
                    }
                    reader.close();
                }
                for( int k=0; k<ITERS; k++){
                    avg[k] /= REPS;
                }
                String name = path + "F" + i + "-iter.txt";
                System.out.println(name);
                FileWriter out = 
                        new FileWriter(name);
                for( int k=0; k<ITERS; k++ ){
                    out.write( ""+k + "," + min[k] + "," + avg[k] + "," + max[k] + "\n");
                }
                out.close();
                System.out.println( "F" + i + " done.. ");
            }
        }catch( Exception e ){
            e.printStackTrace();
        }
    }
    
    
    public static void divider2(){
        try{
            String name = path + "results-eliza/runs.txt";
            File f = new File(name);
            if( f.exists() ){
                BufferedReader reader =
                        new BufferedReader( new FileReader(f) );
                for( int i=1; i<=20; i++ ){
                    FileWriter out = 
                            new FileWriter(path + "F" + i + "-10-iter.txt" );
                    System.out.println( "F" + i + " " + reader.readLine());
                    for( int clines=0; clines<=30; clines++ ){
                        String line = reader.readLine();
                        String[] split = line.split(",");
                        out.write( split[0]);
                        out.write(",");
                        out.write( split[1]);
                        out.write("\n");
                    }
                    out.close();
                }
                reader.close();
            }
        }catch( Exception e ){
            e.printStackTrace();
        }
    }
    
    public static void divider(){
        try{
            for( int i=14; i<=14; i++ ){
                String name = path + "tmp/run-" + i + ".txt";
                File f = new File(name);
                if( f.exists() ){
                    BufferedReader reader =
                            new BufferedReader( new FileReader(f) );
                    for( int k=0; k<10; k++ ){
                        FileWriter out = 
                                new FileWriter(path + "results/F" + i + "-" + k + "-iter.txt" );
                        System.out.println( "F" + i + " " + reader.readLine());
                        for( int clines=0; clines<=30; clines++ ){
                            String line = reader.readLine();
                            String[] split = line.split(",");
                            out.write( split[0]);
                            out.write(",");
                            out.write( split[1]);
                            out.write("\n");
                        }
                        out.close();
                    }
                    reader.close();
                }
            }        
        }catch( Exception e ){
            e.printStackTrace();
        }
    }
    
    public static void main( String[] args ){
        //divider();
        //divider2();
        //concat();
        //stat();
        graph_stat();
    }
}
