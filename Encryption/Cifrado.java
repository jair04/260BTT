package Encryption;
import DAO.Mensaje;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;

public class Cifrado 
{
    static Cipher ecipher = null;
    static Cipher dcipher = null;
    // private static final byte[] SHARED_KEY = hexstringToByteArray("BAC464B197083EE626273DC4C9EBD8AE82E0897E3D8388EE06CB3EF7BCFFF458");
    
    private static Logger logger = Logger.getLogger(Cifrado.class);

    public Cifrado(String path)
    {
        try 
        {
            Key key = getKey(path);
            ecipher = Cipher.getInstance("AES");
            dcipher = Cipher.getInstance("AES");

            /*byte[] iv = new byte[16];
            System.arraycopy(SHARED_KEY, 0, iv, 0, 16);
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

            //String myAES = getSecretKey().toString();
            //System.out.println("hello key is"+myAES.toString());

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);*/

            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } 
        catch (javax.crypto.NoSuchPaddingException e) 
        {
            e.printStackTrace();
        } 
        catch (java.security.NoSuchAlgorithmException e)
        {
        e.printStackTrace();
        }
        catch (java.security.InvalidKeyException e) 
        {
        e.printStackTrace();
        } catch (Exception e) 
        {
        e.printStackTrace();
        }
    }
    
    //Generar Key
    public static void generarKey() 
    {
        KeyGenerator kg = null;
        ObjectOutputStream out = null;

        try 
        {
            kg = KeyGenerator.getInstance("AES");
            kg.init(256);

            // BASE64Decoder bd = new sun.misc.BASE64Decoder();
            // SecretKeyFactory desFactory = SecretKeyFactory.getInstance("AES");
            // DESKeySpec keyspec = new DESKeySpec(bd.decodeBuffer("JIJO"));
            // Key key = new SecretKeySpec(SHARED_KEY, "AES");

            // SecretKey key1 = desFactory.generateSecret(keyspec); 
            SecretKey key = kg.generateKey(); 
            out = new ObjectOutputStream(new FileOutputStream("C:\\Users\\Jair\\Desktop\\aes.key")); //Almancenar el objeto Key
            out.writeObject(key);
            out.close();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        } 
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        } 
    }
    
    private static Key getKey(String keyFile) throws Exception
    {
        ObjectInputStream in = null;
        Key key = null; 
        try
        {
            if(keyFile != null)
            {
                in = new ObjectInputStream(new FileInputStream(keyFile));
                key = (Key) in.readObject();
                in.close();
            }
        }
        catch (Exception e) {
        e.printStackTrace(); 
        }
        return key; 
    }
    
    //Cifrar objeto
    public Object encrypt(Mensaje msg) 
    {
        SealedObject so = null;
        try 
        {
            so = new SealedObject(msg, ecipher); 
        } 
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) 
        {
            e.printStackTrace();
        } 
        catch (java.io.IOException e)
        {
            e.printStackTrace();
        }
        return so;
    }
    
    //Descifrar objeto
    public Mensaje decrypt(Object obj)
    {
        Mensaje msg = null;
        try 
        {
            SealedObject sealedObject = (SealedObject)obj;
            msg = (Mensaje)sealedObject.getObject(dcipher);
        } 
        catch (javax.crypto.BadPaddingException e) 
        {
            e.printStackTrace();
        } 
        catch (IllegalBlockSizeException e) 
        {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) 
        {
            e.printStackTrace();
        } catch (java.io.IOException e)
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return msg;
    }
    
    //Test
    /*
    public static void main(String args[])
    {
        try
        {
            Mensaje msg = new Mensaje();
            msg.setTipo(5);
            generarKey(); 
            Cifrado encrypter = new Cifrado("C:\\Users\\Jair\\Desktop\\aes.key");
            System.out.println(msg);
            Object obj = encrypter.encrypt(msg); //encrypt
            System.out.println(obj.getClass());
            Mensaje msg2 = encrypter.decrypt(obj); //decrypt
            System.out.println(msg2.getTipo());
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }*/
}

