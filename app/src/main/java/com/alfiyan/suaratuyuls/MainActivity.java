package com.alfiyan.suaratuyuls;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {
    com.gc.materialdesign.views.ButtonRectangle load, camera;
    private static int LOAD_IMAGE = 1;
    private static int IMAGE_CAPTURE = 2;
    Bitmap data;
    File photo;
    File folder = new File(Environment.getExternalStorageDirectory()+"/tuyul");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.load = (com.gc.materialdesign.views.ButtonRectangle) this.findViewById(R.id.btnLoad);
        this.camera = (com.gc.materialdesign.views.ButtonRectangle) this.findViewById(R.id.btnCamera);

        if(!folder.exists()){
            folder.mkdir();
        }

        this.load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, LOAD_IMAGE);
            }
        });

        this.camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager()) != null){
                    File exportDir = new File(Environment.getExternalStorageDirectory(),"tmp");
                    if(!exportDir.exists()){
                        exportDir.mkdirs();
                    }else{
                        exportDir.delete();
                    }
                    photo = new File(exportDir,"/"+ UUID.randomUUID().toString().replaceAll("-","")+".jpg");
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                    startActivityForResult(i, IMAGE_CAPTURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int reqs, int res, Intent data){
        super.onActivityResult(reqs,res,data);
        if(reqs == LOAD_IMAGE && res == RESULT_OK && data != null){
            Uri imageUri = data.getData();

            String[] filepath = { MediaStore.Images.Media.DATA };
            Cursor cursor = this.getContentResolver().query(imageUri,filepath,null,null,null);
            cursor.moveToFirst();
            String imgpath = cursor.getString(cursor.getColumnIndex(filepath[0]));
            cursor.close();

            Intent edit = new Intent(this, EditActivity.class);
            edit.putExtra("action", 1);
            edit.putExtra("imagePath",imgpath);
            startActivity(edit);
        }else if(reqs == IMAGE_CAPTURE && res == RESULT_OK){
            String path = photo.getPath();

            Intent i = new Intent(this, EditActivity.class);
            i.putExtra("action",1);
            i.putExtra("imagePath",path);

            startActivity(i);
        }else{
            Toast.makeText(getApplicationContext(),"Error loading the file",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
