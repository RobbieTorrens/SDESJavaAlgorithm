package desalgorithm;

/**
 *
 * @author Robbie Torrens
 */
public class plaintextEncryption {
    
    //the keys
    private int[] K1;
    private int[] K2;
    //These are used to store the left and right most bits of the int array
    private int[] leftmost = new int[8];
    private int[] rightmost = new int[8];
    
    //These values are used in the bit manipulations
    private int[] IP = {2, 6, 3, 1, 4, 8, 5, 7};
    private int[] IPP1 = {4, 1, 3, 5, 7, 2, 8, 6};
    private int[] EP = {4, 1, 2, 3, 2, 3, 4, 1};
    private int[] P4 = {2, 4, 3, 1};
    
    //SBox 0
    private int[][] SB0 = new int[][]{
        {1, 0, 3, 2},
        {3, 2, 1, 0},
        {0, 2, 1, 3},
        {3, 1, 3, 2}
    };
    //SBox 1
    private int[][] SB1 = new int[][]{
        {0, 1, 2, 3},
        {2, 0, 1, 3},
        {3, 0, 1, 0},
        {2, 1, 0, 3}
    };
    
    public plaintextEncryption(int[] K1, int[] K2)
    {
        //set the two bit keys
        this.K1 = K1;
        this.K2 = K2;
    }
    
    //This is the main method for the plain text encryption
    public int[] EncryptPT(int[] plainText)
    {
        //used to store the ciphertext after the encryption steps.
        int[] ciphertext;
        
        //preform the IP permutation on the plaine text array
        ciphertext = bitManipulation(IP, plainText);
        
        //preform the key encryption step on the rearranged bits with k1
        ciphertext = keyEncryption(K1, ciphertext);
        
        //swap the left and right most bits in the ciphertext array
        ciphertext = swap(ciphertext);
        
        //preform the key encryption step on the the ciphertext array with K2
        ciphertext = keyEncryption(K2, ciphertext);
        
        //preform the final permutation on the ciphertext array using IPP1
        ciphertext = bitManipulation(IPP1, ciphertext);
        
        return ciphertext;
    }
    
    public int[] DecryptCT(int[] ciphertext)
    {
        //used to store the ciphertext after the encryption steps.
        int[] plainText;
        
        //preform the IP permutation on the plaine text array
        plainText = bitManipulation(IP, ciphertext);
        
        //preform the key encryption step on the the ciphertext array with K2
        plainText = keyEncryption(K2, plainText);
        
        //swap the left and right most bits in the ciphertext array
        plainText = swap(plainText);
        
        //preform the key encryption step on the the ciphertext array with K1
        plainText = keyEncryption(K1, plainText);
        
        //preform the final permutation on the ciphertext array using IPP1
        plainText = bitManipulation(IPP1, plainText);
        
        //printbytes(plainText);
        
        return plainText;
    }
    
    //This method splits the right and left most bits and preforms a permutation
    //on them before preforming the FK Function on them.
    private int[] keyEncryption(int[] key, int[] bits)
    {
        //used to store the right most bits after the EP permutation has been 
        //preformed on them
        int[] EPRightmost = new int[4];
        
        //used to store the bits after the Fk function has been preformed on them
        int[] FRK = new int[4];

        //split the bits in to right most and left most, storing them in the 
        //splitCipher array
        int[][] splitCipher = splitKey(bits);
        
        //preforme the EP permutation on the right most bits
        EPRightmost = bitManipulation(EP, splitCipher[1]);
        
        //XOR the result of the EP permutation on the right most bits and the 
        //encryption key.
        EPRightmost = XOR(EPRightmost, key);
        
        //get the results of the SBox Manapulation
        FRK = SBoxManapulation(EPRightmost);
        
        //XOR the left most bits and FRK
        FRK = XOR(splitCipher[0], FRK);
        
        bits = joinArrays(FRK, splitCipher[1]);
        
        return bits;
    }
    //This method splits the bit array passed to it in to its left and right most
    //bits then uses the getSboxNumbers function to get the required bits before
    //joining them again and preforming P4 permutation.
    private int[] SBoxManapulation(int []bits )
    {
        //split the bits in to the right and leftmost bits
        int[][] leftRightbits = splitKey(bits);
        //using the left most bits call the getSboxNumbers function with SBox0
        int[] leftBits= getSboxNumbers(leftRightbits[0], SB0);
        //Using the right most bits call the getSboxNumbers function with SBox 1
        int[] righBits = getSboxNumbers(leftRightbits[1], SB1);
        
        //rejoin the arrays
        bits = joinArrays(leftBits, righBits);
        //preform the P4 permutation on the bits array
        int[] temp = bitManipulation(P4, bits);
        //return the result
        return temp;
    }
    
    //this function is used to split an array in to its left and rightmost bits
    private int[][] splitKey(int [] key)
    {
        //create an array to store the left most bits
        int[]left = new int[key.length/2];
        //create an array to store the right most bits
        int[]right = new int [key.length/2];
        
        //this forloop sorts the bits in to the correct arrays
        for(int i = 0; i<key.length/2; i++)
        {
            left[i] = key[i];
            right[i] = key[i+(key.length/2)];
        }
        //creat an array to store the right and left most bit arrays
        int [][]temp ={left, right};
        
        //return the 2D array
        return temp;
    }
    
    //This method rearranges the array passed to it according to the bit
    //placement array.
    private int[] bitManipulation(int[] bitPlacement, int[] bits)
    {
        //creat an array to store the manipulated bit array
        int[] temp = new int[bitPlacement.length];
        
        //sort the bits array in to the new array using bitPlacement as an index
        for(int i = 0; i<bitPlacement.length; i++)
            temp[i]=bits[bitPlacement[i]-1];
        
        //return the new array
        return temp;
    }
    
    //this method is used to preform an XOR on two arrays representing bits
    private int[] XOR(int[] bits1, int[] bits2)
    {
        //creat a new array to store the XOR result
        int[] temp = new int[bits1.length];
        for(int i = 0; i< bits1.length; i++)
        {
            //if the bits are the same
            if(bits1[i] == bits2[i])
            {
                //the new value is 0
                temp[i]=0;
            }else
            {
                //other wise the new value is 1
                temp[i]=1;
            }
        }
        //return the result
        return temp;
    }
    
    //this function is used to work out what numbers to get form the 2D SBox array
    //and pulles out the values and returnes the value as an int arry representing
    //the number in binary
    private int[] getSboxNumbers(int[] bytes, int[][] SBox)
    {
        //convert the values at 0 and 3 in the bytes array to a string in order
        //to get a binary representation of the row value for the SBox
        String row = bytes[0]+""+bytes[3];
        //convert the values at 1 and 2 in the bytes array to a string in order
        //to get a binary representation of the colunm value for the SBox
        String col = bytes[1]+""+bytes[2];
        //convert the binary values to decimal and store them 
        int SBoxR = Integer.parseInt(row, 2);
        int SBoxC = Integer.parseInt(col, 2);
        //using the decimal values, get the correct number from the SBox
        int SBoxNum = SBox[SBoxR][SBoxC];
        //convert the value pulled from the SBox to binary and store it
        String SBoxNumBin = Integer.toBinaryString(SBoxNum);
        //This if statment adds a 0 to the binary value if it only contains one 
        //character and returns
        if(SBoxNumBin.length()<2)
            return new int [] {0,Character.getNumericValue(SBoxNumBin.charAt(0))};
        //convert the string representation of the binary value to an int array
        int [] temp = {Character.getNumericValue(SBoxNumBin.charAt(0)),
                       Character.getNumericValue(SBoxNumBin.charAt(1))};
        //return the value
        return temp;
    }
    
    //this method joins two int arrays together
    private int[] joinArrays(int[] bits1, int[] bits2)
    {
        //create the new array to store the combined arrays
        int[] temp = new int [bits1.length + bits2.length];
        //this forloop joins the two, bits1 on the left and bits2 on the right
        for(int i =0; i<bits1.length; i++)
        {
            temp[i] = bits1[i];
            temp[i+bits1.length] = bits2[i];
        }
        //return the new array
        return temp;
    }
    
    //this method swaps the left and right most values in an array around
    private int[] swap(int[] bits)
    {
        int temp[][] = splitKey(bits);
        bits = joinArrays(temp[1], temp[0]);
        
        return bits;
    }
    
    /*
    private void printbytes(int[] printBytes)
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~");
        for(int i = 0; i< printBytes.length; i++)
            System.out.println(printBytes[i]);
    }
    */
}
