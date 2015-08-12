package desalgorithm;

/**
 *
 * @author Robbie Torrens
 */
public class keyGeneration {
    
    private int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    private int[] P8 = {6,3,7,4,8,5,10,9};
    private int[] key;
    private int[] leftKey = new int[5];
    private int[] rightKey = new int[5];
    private int[] origionalKey;
    private int[] bits;
    
    
    public keyGeneration(int bytes[])
    {
        this.origionalKey = bytes;
    }
    
    public int[] generate(int shifts)
    {
        this.bits = origionalKey;
        this.bits = bitManapulation(P10);
        splitKey();
        leftKey = shiftBits(shifts, leftKey);
        rightKey = shiftBits(shifts, rightKey);
        joinKey();
        key = bitManapulation(P8);
        //printbytes(key);
        
        return key;
    }
    
    private void splitKey()
    {
        for(int i = 0; i<5; i++)
        {
            leftKey[i] = bits[i];
            rightKey[i] = bits[i+5];
        }
    }
    
    private void joinKey()
    {
        for(int i = 0; i < 5; i++)
        {
            bits[i] = leftKey[i];
            bits[i+5] = rightKey[i];
        }
    }
    
    private int[] shiftBits(int shifts, int[] shiftBytes)
    {
        int temp[] = new int[5];
        int shiftPosition;
        
        for(int i=0; i<5; i++){
            
            shiftPosition = i-shifts;

            if(shiftPosition>-1)
            {
                temp[shiftPosition] = shiftBytes[i];
            }else
            {
                temp[shiftBytes.length+shiftPosition] = shiftBytes[i];
            }
        }
       
       return temp;
    }
    
    private int[] bitManapulation(int[] bitPlacement)
    {
        int[] temp = new int[bitPlacement.length];
        
        for(int i = 0; i<bitPlacement.length; i++)
            temp[i]=bits[bitPlacement[i]-1];
        
        return temp;
    }
   
}
