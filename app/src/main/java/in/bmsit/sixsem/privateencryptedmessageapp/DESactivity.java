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
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESactivity extends AppCompatActivity {
    EditText inputText,outputText;
    Button enc,dec,clearText;
    ImageButton send;

    private static final String UNICODE_FORMAT="UTF8";
    public static final String DES_ENCRYPTION_SCHEME="DES";
    private KeySpec myKeySpec;
    private SecretKeyFactory mySecretKeyFactory;
    private Cipher cipher;

    byte[] KeyAsBytes;
    private String myEncryptionkey;
    private String getMyEncryptionscheme;
    SecretKey key;
    String myEnckey="This is a key";
    String ans="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desactivity);
        inputText=findViewById(R.id.inputText);
        enc=findViewById(R.id.enc);
        outputText=findViewById(R.id.outputText);
        dec=findViewById(R.id.dec);
        clearText=findViewById(R.id.clearText);
        send=findViewById(R.id.sendButton);

        myEncryptionkey=myEnckey;
        getMyEncryptionscheme=DES_ENCRYPTION_SCHEME;
        try {
            KeyAsBytes=myEncryptionkey.getBytes(UNICODE_FORMAT);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        try{
            myKeySpec=new DESKeySpec(KeyAsBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            mySecretKeyFactory=SecretKeyFactory.getInstance(getMyEncryptionscheme);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            cipher=Cipher.getInstance(getMyEncryptionscheme);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            key=mySecretKeyFactory.generateSecret(myKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String ans="";
                String unencrypted=inputText.getText().toString();
                ans=encrypt(unencrypted);
                outputText.setText(ans);
            }
        });
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String a="";
                String decrypted=inputText.getText().toString();
                ans=decrypt(decrypted);
                outputText.setText(ans);
            }
        });
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText.setText("");
                outputText.setText("");
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ans.length()>0){
                    Intent sendIntent=new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,ans);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
                Toast.makeText(DESactivity.this,"No output",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String encrypt(String unencryptedText){
        String encText="";

        try {
            cipher.init(Cipher.ENCRYPT_MODE,key);

            byte[] plainText=unencryptedText.getBytes(UNICODE_FORMAT);
            byte[] ciphertext=cipher.doFinal(plainText);

            encText= Base64.encodeToString(ciphertext,Base64.DEFAULT); //converts byte to base64 class
        } catch (InvalidKeyException | UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return encText;
    }

    public String decrypt(String encryptedText){
        String decText="";
        try {
            cipher.init(Cipher.DECRYPT_MODE,key);

            byte[] cipherText=Base64.decode(encryptedText,Base64.DEFAULT);
            byte[] plainText=cipher.doFinal(cipherText);

            decText=new String(plainText);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e){
        }
        return decText;
    }
}
