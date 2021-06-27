package in.bmsit.sixsem.privateencryptedmessageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button AES,imageEnc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AES=findViewById(R.id.aes);
        imageEnc=findViewById(R.id.imageEnc);

        AES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent myIntent=new Intent(MainActivity.this,AESEncryption.class);
              startActivity(myIntent);
            }
        });
        imageEnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myInt=new Intent(MainActivity.this,AES_Image.class);
                startActivity(myInt);
            }
        });
    }
}
