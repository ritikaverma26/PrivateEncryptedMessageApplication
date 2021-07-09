package in.bmsit.sixsem.privateencryptedmessageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAcrypt extends AppCompatActivity {
    Button encrsa,decrsa,clearrsa;
    ImageButton sendrsa;
    EditText inputrsa,outputrsa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsacrypt);
        encrsa=findViewById(R.id.encrsa);
        decrsa=findViewById(R.id.decrsa);
        inputrsa=findViewById(R.id.inputText);
        outputrsa=findViewById(R.id.outputrsa);
        clearrsa=findViewById(R.id.clearrsa);
        sendrsa=findViewById(R.id.sendrsa);


        KeyPair kp=getkeypair(); //method defined below

        final PublicKey publickey=kp.getPublic();
        final byte[] publickeyByte=publickey.getEncoded();
        final String publicBase64=new String(Base64.encode(publickeyByte,Base64.DEFAULT));

        PrivateKey privatekey=kp.getPrivate();
        final byte[] privatekeyByte=privatekey.getEncoded();
        final String privateBase64=new String(Base64.encode(privatekeyByte,Base64.DEFAULT));
        clearrsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outputrsa.setText("");
                inputrsa.setText("");
            }
        });
        sendrsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(outputrsa.getText().toString().length()>0){
                    Intent sendInt=new Intent();
                    sendInt.setAction(Intent.ACTION_SEND);
                    sendInt.putExtra(Intent.EXTRA_TEXT,outputrsa.getText().toString());
                    sendInt.setType("text/plain");
                    startActivity(sendInt);
                }
                else{
                    Toast.makeText(RSAcrypt.this,"No output",Toast.LENGTH_SHORT).show();
                }
            }
        });

        encrsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String plainText=inputrsa.getText().toString();
               String encText=encryptRSA(plainText,publicBase64);
               outputrsa.setText(encText);
            }
        });
        decrsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cipherText=inputrsa.getText().toString();
                String dectext=decryptRSA(cipherText,privateBase64);
                outputrsa.setText(dectext);
            }
        });

    }
     public static KeyPair getkeypair(){
        KeyPair kp=null;
         try {
             KeyPairGenerator kpg=KeyPairGenerator.getInstance("RSA");
             kpg.initialize(2048);
             kp=kpg.generateKeyPair();
         } catch (NoSuchAlgorithmException e) {
             e.printStackTrace();
         }
         return kp;
     }
     public static String encryptRSA(String plainText,String publickey){
        String encrypted="";
         try {
             KeyFactory keyFac=KeyFactory.getInstance("RSA");
             KeySpec keyspec=new X509EncodedKeySpec(Base64.decode(publickey.trim().getBytes(),Base64.DEFAULT));
             Key key =keyFac.generatePublic(keyspec);
             final  Cipher cipher= Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
             cipher.init(Cipher.ENCRYPT_MODE,key);
             byte[] encBytes=cipher.doFinal(plainText.getBytes("UTF-8"));
             encrypted=new String(Base64.encode(encBytes,Base64.DEFAULT));
         } catch (NoSuchAlgorithmException e) {
             e.printStackTrace();
         } catch (InvalidKeySpecException e) {
             e.printStackTrace();
         } catch (NoSuchPaddingException e) {
             e.printStackTrace();
         } catch (InvalidKeyException e) {
             e.printStackTrace();
         } catch (BadPaddingException e) {
             e.printStackTrace();
         } catch (UnsupportedEncodingException e) {
             e.printStackTrace();
         } catch (IllegalBlockSizeException e) {
             e.printStackTrace();
         }
         return encrypted.replaceAll("(\\r|\\n)","");
     }
     public static String decryptRSA(String cipherText,String privatekey){
        String decrypted="";

         try {
             KeyFactory keyfac=KeyFactory.getInstance("RSA");
             KeySpec keyspec=new PKCS8EncodedKeySpec(Base64.decode(privatekey.trim().getBytes(),Base64.DEFAULT));
             Key key=keyfac.generatePrivate(keyspec);
             final Cipher cipher=Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
             cipher.init(Cipher.DECRYPT_MODE,key);
             byte[] encBytes=Base64.decode(cipherText,Base64.DEFAULT);
             byte[] decBytes=cipher.doFinal(encBytes);
             decrypted=new String(decBytes);
         } catch (NoSuchAlgorithmException e) {
             e.printStackTrace();
         } catch (InvalidKeySpecException e) {
             e.printStackTrace();
         } catch (NoSuchPaddingException e) {
             e.printStackTrace();
         } catch (InvalidKeyException e) {
             e.printStackTrace();
         } catch (BadPaddingException e) {
             e.printStackTrace();
         } catch (IllegalBlockSizeException e) {
             e.printStackTrace();
         }
         return decrypted;
     }


}
