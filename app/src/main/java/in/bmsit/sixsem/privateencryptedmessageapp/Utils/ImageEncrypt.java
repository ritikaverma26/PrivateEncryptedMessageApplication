package in.bmsit.sixsem.privateencryptedmessageapp.Utils;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import in.bmsit.sixsem.privateencryptedmessageapp.AES_Image;

public class ImageEncrypt {
    private static final int READ_WRITE_BLOCK_BUFFER=1024;

    public static void encryptToFile(String keystring, String spec, InputStream in, OutputStream out) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        try {

            IvParameterSpec iv= new IvParameterSpec(spec.getBytes("UTF-8"));
            SecretKeySpec keySpec=new SecretKeySpec(keystring.getBytes("UTF-8"),"AES");
            Cipher c=Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE,keySpec,iv);
            out=new CipherOutputStream(out,c);
            int count=0;
            byte[] buffer=new byte[READ_WRITE_BLOCK_BUFFER];
            while((count=in.read(buffer))>0)
                out.write(buffer,0,count);
            Log.d("encryptToFile","done");
        }
        finally {
            out.close();
        }
    }

    public static void decryptToFile(String keystring, String spec, InputStream in, OutputStream out) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        try {
            IvParameterSpec iv= new IvParameterSpec(spec.getBytes("UTF-8"));
            SecretKeySpec keySpec=new SecretKeySpec(keystring.getBytes("UTF-8"),"AES");
            Cipher c=Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE,keySpec,iv);
            out=new CipherOutputStream(out,c);
            int count=0;
            byte[] buffer=new byte[READ_WRITE_BLOCK_BUFFER];
            while((count=in.read(buffer))>0)
                out.write(buffer,0,count);
        }
        finally {
            out.close();
        }
    }
}
