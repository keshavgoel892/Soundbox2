package com.example.soundbox2;

import androidx.appcompat.app.AppCompatActivity;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.Locale;
import android.widget.EditText;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private EditText editAmount,bankName;
    Button resetButton;

    String sender_name,if_credited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetButton = findViewById(R.id.btnReset);
        editAmount = (EditText) findViewById(R.id.edtAmount);
        bankName = (EditText) findViewById(R.id.bankName);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankName.setText("");
                editAmount.setText("");
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                } else {
                    Toast.makeText(MainActivity.this, "Error initializing TextToSpeech.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                speakOut(editAmount.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bind_L();
    }

    private void speakOut(String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    public void bind_L(){
        SmsReciever.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText,String nameText) {
                Log.d("d3", "messageReceived: d3");
                sender_name=nameText;
                if_credited=messageText;

                setAmount();
            }
        });

    }

    public void setAmount(){
        Log.d("d1","setAmount");
        if (sender_name.endsWith(bankName.getText().toString())){
            Log.d("d2", "bank name true");
            editAmount.setText(if_credited);
        }
    }
}