package in.bmsit.sixsem.privateencryptedmessageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import in.bmsit.sixsem.privateencryptedmessageapp.Utils.ImageEncrypt;

public class AES_Image extends AppCompatActivity {
    public static final String FILE_NAME_ENC="pp_enc";
    public static final String FILE_NAME_DEC="pp_dec.jpg";
    Button enc,dec;
    ImageView imageview;
    File myDir;
    String my_key="12345678abcdefgh";
    String my_spec_key="1a2b3c4d5e6f7g8h";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aes__image);
        enc=findViewById(R.id.enc);
        dec=findViewById(R.id.dec);
        imageview=findViewById(R.id.imageView);
        myDir= new File(Environment.getExternalStorageDirectory().toString()+"/saved_images");
        myDir.mkdirs();
        Dexter.withActivity(this)
                .withPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                })
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                      enc.setEnabled(true);
                      dec.setEnabled(true);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(AES_Image.this, "You must enable permission", Toast.LENGTH_SHORT).show();
                    }
                }).check();
        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable= ContextCompat.getDrawable(AES_Image.this,R.drawable.pp);
                BitmapDrawable bitmapDrawable=(BitmapDrawable)drawable;
                Bitmap bitmap =bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                InputStream is=new ByteArrayInputStream((stream.toByteArray()));

                File outputFile= new File(myDir,FILE_NAME_ENC);

                try{
                    ImageEncrypt.encryptToFile(my_key,my_spec_key,is,new FileOutputStream(outputFile));
                    Toast.makeText(AES_Image.this, "Encrypted", Toast.LENGTH_SHORT).show();
                    Log.d("encryption","done");
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }

            }
        });
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputFiledec=new File(myDir,FILE_NAME_DEC);
                File encFile=new File(myDir,FILE_NAME_ENC);
                try {
                    ImageEncrypt.decryptToFile(my_key,my_spec_key,new FileInputStream(encFile),new FileOutputStream(outputFiledec));
                    //setting for image view
                    imageview.setImageURI(Uri.fromFile(outputFiledec));
                    //if you want to delete file after decryption do this
                    outputFiledec.delete();
                    Toast.makeText(AES_Image.this,"Decrypt",Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
