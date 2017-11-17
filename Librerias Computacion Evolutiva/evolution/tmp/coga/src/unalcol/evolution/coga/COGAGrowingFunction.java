package unalcol.evolution.coga;
import unalcol.optimization.Population;
import unalcol.evolution.coea.*;
import unalcol.evolution.*;
import unalcol.evolution.genotype.binary.*;
import unalcol.types.collection.bitarray.*;
import unalcol.types.collection.vector.Vector;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: Kunsamu </p>
 *
 * @author Jonatan Gomez
 * @version 1.0
 */
public class COGAGrowingFunction<P> extends GrowingFunction<BitArray, P> {
    protected GrowingFunction<BitArray,P> lowLevel;
    protected CoEvolutionaryAlgorithm optimizer= null;
    protected int friendsNumber;
    protected int gene_size;
    protected int friendIndexSize;
    protected int gene_pos;
    

    public COGAGrowingFunction( GrowingFunction<BitArray,P> _lowLevel,
                                int _gene_pos, int _gene_size,
                                int friendsNumber,
                                int _friendIndexSize ) {
        lowLevel = _lowLevel;
        gene_size = _gene_size;
        gene_pos = _gene_pos;
        friendIndexSize = _friendIndexSize;
        this.friendsNumber = friendsNumber;
    }

    public void setCoEA( CoEvolutionaryAlgorithm optimizer ){
        this.optimizer = optimizer;
    }

    /**
     * Generates a thing from the given genome
     * @param genome Genome of the thing to be expressed
     * @return A thing expressed from the genome
     */
    public P get( BitArray genome ) {
        if( optimizer != null ){
            BitArray gene = genome.subBitArray(0, gene_size);
            BitArray friends = genome.subBitArray(gene_size);
            int[] pos = BitArrayConverter.getIntArray( friends, friendIndexSize, false );
            int n = pos.length + 1;
            BitArray growthGenome = new BitArray(0, false);
            int k = 0;
            for( int i=0; i<n; i++ ){
                if( i == gene_pos ){
                    growthGenome.add( gene );
                    k++;
                }else{
                    BitArray fr = (BitArray)((Individual)optimizer.pop(i).get(pos[i-k] % optimizer.pop(i).size())).genome();
                    growthGenome.add( fr.subBitArray(0, gene_size) );
    //                System.out.print( growthGenome + " " );
                }
            }
    //        System.out.println( growthGenome );
            return lowLevel.get(growthGenome);
        }
        return null;
    }

    /**
     * Generates a genome from the given thing
     * @param thing A thing expressed from the genome
     * @return Genome of the thing
     */
    public BitArray set(P thing) { return (BitArray)thing; }

    public BinaryGenotype genotype(){
        return new BinaryGenotype( gene_size + friendsNumber*friendIndexSize);
    }
    
  /**
   * Generates a population of individuals from the given collection of genomes
   * @param genomes Genomes used for building the population of individuals
   * @return Population of individuals built uppon the given genomes
   */
  public Population<P> build( Vector<BitArray> genomes ){
      BitArray genome;
      Vector<Individual<BitArray,P>> v = new Vector<Individual<BitArray,P>>();
      int n = genomes.size();
      for( int i=0; i<n; i++ ){
          genome = genomes.get(i);
          v.add(new Individual<BitArray,P>(genome, this));
      }
      return new Population(v);
  }    
}
