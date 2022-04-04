package com.example.lab3_1;

import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView = null;
    Button button = null;
    Button save = null;
    TextInputEditText textInput = null;
    List<GuestModel> guestsGlobal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                startActivityForResult(intent, 999);
            }
        }

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        save = findViewById(R.id.saveBtn);
        textInput = findViewById(R.id.textInputLayout1);

        button.setOnClickListener(view -> {
            GuestModel guest;
            try {
                guest = new GuestModel(
                        -1,
                        textInput.getText().toString(),
                        Calendar.getInstance().getTime().toString()
                );
            } catch (Exception ex) {
                guest = new GuestModel(
                        -1,
                        "Error creating guest",
                        Calendar.getInstance().getTime().toString()
                );
            }
            SqlOpenHelper helper = new SqlOpenHelper(MainActivity.this);

            boolean saved = helper.add(guest);

            List<GuestModel> guests = helper.fetch();
            guestsGlobal.add(guests.get(guests.size() - 1));
            guestsGlobal.add(guests.get(guests.size() - 2));
            guestsGlobal.add(guests.get(guests.size() - 3));

            ArrayAdapter<GuestModel> itemsAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, guests);

            ListView listView = findViewById(R.id.lvItems);

            listView.setAdapter(itemsAdapter);
        });

        save.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, "db.txt");
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, Environment.DIRECTORY_DOCUMENTS);
            startActivityForResult(intent, 111);
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        if (resultCode != Activity.RESULT_OK && resultData == null)
            return;
        switch (requestCode) {
            case 111:
                Uri uri = resultData.getData();
                ParcelFileDescriptor pfd;
                try {
                    pfd = getContentResolver().openFileDescriptor(uri, "w");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }
                FileOutputStream fs = new FileOutputStream(pfd.getFileDescriptor());
                try {
                    fs.write(guestsGlobal.toString().getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fs.flush();
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 999:
                break;
        }

    }
}