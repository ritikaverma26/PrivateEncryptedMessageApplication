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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryption extends AppCompatActivity {
    EditText input,output;
    Button enc,dec,clear;
    ImageButton send;
    String outputstring="";
    String inputstring;
    public static String keytext="qwerty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aesencryption);
        input=findViewById(R.id.inputtext);
        output=findViewById(R.id.outputtext);
        enc=findViewById(R.id.encrypt);
        dec=findViewById(R.id.decrypt);
        clear=findViewById(R.id.clear);
        send=findViewById(R.id.send);
        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    inputstring=input.getText().toString();
                    outputstring=encrypt(inputstring,keytext);
                    output.setText(outputstring);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputstring=input.getText().toString();
                try {
                    outputstring=decrypt(inputstring,keytext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                output.setText(outputstring);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.setText("");
                output.setText("");
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(outputstring.length()>0){
                    Intent sendIntent=new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,outputstring);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }else
                Toast.makeText(AESEncryption.this,"No output",Toast.LENGTH_SHORT).show();

            }
        });
    }
    private String encrypt(String plaintext,String keytext) throws Exception {
        SecretKeySpec key=generatekey(keytext);
        Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal=cipher.doFinal(plaintext.getBytes("UTF-8"));
        String encryptedText = Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedText;
    }
    private  String decrypt(String encdata,String keytext) throws Exception{
        SecretKeySpec key=generatekey(keytext);
        Cipher cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] decoded=Base64.decode(encdata,Base64.DEFAULT);
        byte[] decrypted=cipher.doFinal(decoded);
        String decryptedText=new String(decrypted,"UTF-8");
        return decryptedText;
    }
    private SecretKeySpec generatekey(String keytext) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256"); //to increse security we calculate hash value of key using MessageDigest
        byte[] bytes=keytext.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest(); //we convert the hash value into byte arrray to generate the key
        SecretKeySpec secretkeyspec=new SecretKeySpec(key,"AES");
        return secretkeyspec;
    }
}
