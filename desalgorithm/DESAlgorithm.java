package desalgorithm;

import org.omg.CORBA.PERSIST_STORE;

/**
 *
 * @author Robbie Torrens
 */
public class DESAlgorithm {
    
    //An int array representing the binary key used for the encryptoin
    private static int[] bitKey = {1,1,1,1,0,1,1,0,0,0};
    //int arrays to store the K1 and K2 values after they have been generated
    //from the bitKey array
    private int[] K1;
    private int[] K2;
    //This class is used to Encrypt int array representations of the bits
    private static plaintextEncryption PE;
    //this value is set to flase if the bitKey array has a value other than a 0
    //or a 1
    private boolean keyIsValid;
    
    private static DESAlgorithm DES;
    
    //The construstor of this class takes a bit key, checks it's valid and 
    //generates the K1 and K2 values from it.
    public DESAlgorithm(int[] bitKey)
    {
        //check the bitKey array
        if(checkIntArray(bitKey))
        {
            //set keyIsValid to true
            keyIsValid = true;
            //
            this.bitKey = bitKey;
            //generate the keys
            generateKey();
            //initalise the plaintextEncryption class
            PE = new plaintextEncryption(K1, K2);
        }else
        {
            //if the key is not valid set the keyIsValid to false
            keyIsValid = false;
            System.out.println("Error: the Key supplied is incorrect.");
        }
    }
    
    public static void main(String[] args) {
        
        DES = new DESAlgorithm(bitKey);
        
        String username = "MARK";
        String Password = "password";
        byte[] usernameBytes = username.getBytes();
        byte[] passwordBytes = Password.getBytes();
        
        System.out.println("User name bytes Pre-encription: ");
        for(int i =0; i<username.length();i++)
            System.out.println(usernameBytes[i] & 0xFF);
        
        System.out.println("Password bytes Pre-encription: ");
        for(int i =0; i<Password.length();i++)
            System.out.println(passwordBytes[i] & 0xFF);
        
        usernameBytes = DES.EncryptBytes(usernameBytes);
        passwordBytes = DES.EncryptBytes(passwordBytes);
        
        System.out.println("User name encrypted bytes: ");
        for(int i =0; i<username.length();i++)
            System.out.println(usernameBytes[i] & 0xFF);
        
        System.out.println("Password encrypted bytes");
        for(int i =0; i<Password.length();i++)
            System.out.println(passwordBytes[i] & 0xFF);
        
        usernameBytes = DES.Decrypt(usernameBytes);
        passwordBytes = DES.Decrypt(passwordBytes);
        
        System.out.println("User name Decrypted bytes: ");
        for(int i =0; i<username.length();i++)
            System.out.println(usernameBytes[i] & 0xFF);
        
        System.out.println("Password Decrypted bytes: ");
        for(int i =0; i<Password.length();i++)
            System.out.println(passwordBytes[i] & 0xFF);
        
        
    }
    
    //This method generates the K1 and K2 int array from the bitKey 
    private void generateKey()
    {
        //initalise the keyGeneration class
        keyGeneration KG = new keyGeneration(bitKey);
        
        //
        K1 = KG.generate(1);
        K2 = KG.generate(3);
    }
    
    
    public byte[] EncryptBytes(byte[] PTBytes)
    {
        try{
            
            byte[] encryptedBytes = new byte[PTBytes.length];

            for(int i = 0; i<PTBytes.length; i++)
            {
                int[] PT =  byteToIntArray(PTBytes[i]);
                encryptedBytes[i] = intArrayToByte(PE.EncryptPT(PT));
            }
            return encryptedBytes;
            
        }catch(Exception e)
        {
            System.out.println("Error: "+e.getMessage());
            System.out.println("Message failed to encrypt.");
            return null;
        }
    }
    
    public byte[] Decrypt(byte[] bytes)
    {
        try{
            int[] PT;
            byte[] decryptBytes = new byte[bytes.length];

            for(int i = 0; i<bytes.length; i++)
            {
                PT =  byteToIntArray(bytes[i]);
                decryptBytes[i] = intArrayToByte(PE.DecryptCT(PT));
            }

            return decryptBytes;
            
        }catch(Exception e)
        {
            System.out.println("Error: "+e.getMessage());
            System.out.println("Message failed to decrypt.");
            return null;
        }
    }
    
    private int[] byteToIntArray(byte b)
    {
        String PTString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        int[] Bytes = new int[PTString.length()];
        for(int i =0; i< PTString.length(); i++)
            Bytes[i] = Character.getNumericValue(PTString.charAt(i));
        
        
        return Bytes;
    }
    
    private byte intArrayToByte(int[] intArray)
    {
        String StrRep = "";
        
        for(int i = 0; i<intArray.length; i++)
            StrRep += intArray[i];
        
        return (byte)Short.parseShort(StrRep, 2);
        
    }
    
    private boolean checkIntArray(int[] bytes)
    {
        for(int i = 0; i < bytes.length; i++)
            if(bytes[i]!=1&&bytes[i]!=0)
                return false;
        
        return true;
    }
    
    private static void printbytes(int[] printBytes)
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~");
        for(int i = 0; i< printBytes.length; i++)
            System.out.println(printBytes[i]);
    }
}
