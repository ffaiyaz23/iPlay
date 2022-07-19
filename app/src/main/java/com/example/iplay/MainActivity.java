package com.example.iplay;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();

        ArrayList<File> mySong = findSong(Environment.getExternalStorageDirectory());
        String [] items = new String[mySong.size()];

        for(int i=0; i<mySong.size(); i++)
            items[i] = mySong.get(i).getName().replace(".mp3", "");

        MyAdapter adapter = new MyAdapter(MainActivity.this,
                R.layout.mylagout,
                items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            Intent intent = new Intent(MainActivity.this, PlaySong.class);
            String currentSong = listView.getItemAtPosition(position).toString();
            intent.putExtra("songList", mySong);
            intent.putExtra("currentSong", currentSong);
            intent.putExtra("position", position);
            startActivity(intent);
        });
    }

    public ArrayList<File> findSong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File [] files = file.listFiles();
        if(files != null) {
            for (File myFile : files)
                if (myFile.isDirectory() && !myFile.isHidden()) {
                    arrayList.addAll(findSong(myFile));
                } else if (myFile.getName().endsWith(".mp3")) {
                    arrayList.add(myFile);
                }
        }
        return arrayList;
    }
}