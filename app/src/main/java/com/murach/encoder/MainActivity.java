package com.murach.encoder;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import android.widget.Spinner;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements OnClickListener, OnItemSelectedListener {
    private EditText editText1, editText2;
    private Button encodeBtn, copyBtn;
    private EditText pickKey;
    private Spinner spinner;
    public boolean encode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        encodeBtn = (Button) findViewById(R.id.encodeBtn);
        copyBtn = (Button) findViewById(R.id.copyBtn);
        pickKey = (EditText) findViewById(R.id.pickKey);
        spinner = (Spinner) findViewById(R.id.spinner);

        pickKey.setOnClickListener(this);
        encodeBtn.setOnClickListener(this);
        copyBtn.setOnClickListener(this);
        encodeBtn.setText(R.string.Encode);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.spinner_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void encode() {
        if ((pickKey.getText().toString() == null || (pickKey.getText().toString()).equals("")) &&
                (editText1.getText().toString() == null || (editText1.getText().toString()).equals(""))) {
            Toast noInput = Toast.makeText(this, R.string.noInput, Toast.LENGTH_SHORT);
            noInput.show();
        } else if (pickKey.getText().toString() == null || (pickKey.getText().toString()).equals("")) {
            Toast noKey = Toast.makeText(this, R.string.noKeySelected, Toast.LENGTH_SHORT);
            noKey.show();
        } else if (editText1.getText().toString() == null ||
                (editText1.getText().toString()).equals("")) {
            Toast noText = Toast.makeText(this, R.string.noTextEntered, Toast.LENGTH_SHORT);
            noText.show();
        } else {
            String originalText = editText1.getText().toString();
            String key = pickKey.getText().toString();
            while (key.length() < originalText.length()) {
                key += key;
            }
            StringReader reader = new StringReader(originalText);
            StringReader keyReader = new StringReader(key);
            StringBuilder newString = new StringBuilder();
            ArrayList<Integer> set = new ArrayList<>();

            int r;
            try {
                while ((r = reader.read()) != -1) {
                    set.add(r);
                }
                for (int i = 0; i < set.size(); i++) {
                    int j = keyReader.read();
                    int z = set.get(i) + j;
                    while (z > 126) {
                        z -= 95;
                    }
                    newString.append((char) z);
                }
                editText2.setText(newString.toString());

            } catch (IOException e) {

            }
        }
    }


    public void decode() {
        if ((pickKey.getText().toString() == null || (pickKey.getText().toString()).equals("")) &&
                (editText1.getText().toString() == null || (editText1.getText().toString()).equals(""))) {
            Toast noInput = Toast.makeText(this, R.string.noInput, Toast.LENGTH_SHORT);
            noInput.show();
        } else if (pickKey.getText().toString() == null || (pickKey.getText().toString()).equals("")) {
            Toast noKey = Toast.makeText(this, R.string.noKeySelected, Toast.LENGTH_SHORT);
            noKey.show();
        } else if (editText1.getText().toString() == null ||
                (editText1.getText().toString()).equals("")) {
            Toast noText = Toast.makeText(this, R.string.noTextEntered, Toast.LENGTH_SHORT);
            noText.show();
        } else {
            String originalText = editText1.getText().toString();
            String key = pickKey.getText().toString();
            while (key.length() < originalText.length()) {
                key += key;
            }
            StringReader reader = new StringReader(originalText);
            StringReader keyReader = new StringReader(key);
            StringBuilder newString = new StringBuilder();
            ArrayList<Integer> set = new ArrayList<>();

            int r;
            try {
                while ((r = reader.read()) != -1) {
                    set.add(r);
                }
                for (int i = 0; i < set.size(); i++) {
                    int j = keyReader.read();
                    int z = set.get(i) - j;
                    while (z < 32) {
                        z += 95;
                    }
                    newString.append((char) z);
                }
                editText2.setText(newString.toString());
            } catch (IOException e) {

            }
        }
    }

    public void copy() {
        ClipboardManager cb = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        cb.setPrimaryClip(ClipData.newPlainText("Copied Text", editText2.getText()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (position == 0) {
            encode = true;
            encodeBtn.setText(R.string.Encode);
        }
        if (position == 1) {
            encode = false;
            encodeBtn.setText(R.string.Decode);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.encodeBtn:
                if (encode) {
                    encode();
                }
                if (!encode) {
                    decode();
                }
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;
            case R.id.copyBtn:
                copy();
                Toast copied = Toast.makeText(this, R.string.copied, Toast.LENGTH_SHORT);
                copied.show();
                break;
            }
        }
    }

