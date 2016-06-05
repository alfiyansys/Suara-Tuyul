package com.alfiyan.suaratuyuls;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alfiyan.suaratuyuls.core.Histogram;
import com.alfiyan.suaratuyuls.core.Tuyul;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Locale;


public class EditActivity extends ActionBarActivity implements TextToSpeech.OnInitListener {
    Bitmap mainBitmap;
    ImageView mainView;
    String imgPath;
    String fname;
    Histogram histo;
    DataPoint[] histod = new DataPoint[361];
    TextView warna, nominal;
    TextToSpeech tts;

    int nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        this.mainView = (ImageView)this.findViewById(R.id.imageMain);
        this.warna = (TextView) this.findViewById(R.id.txtWarna);
        this.nominal = (TextView) this.findViewById(R.id.txtNominal);

        tts = new TextToSpeech(this,this);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setLayerType(GraphView.LAYER_TYPE_SOFTWARE, null);
        graph.removeAllSeries();

        Bundle ex = getIntent().getExtras();
        if(ex.getInt("action") == 1){
            String path = ex.getString("imagePath");
            this.mainBitmap = BitmapFactory.decodeFile(path);

            /* resize */
            int maxHeight = 800;
            int maxWidth = 2000;
            float scale = Math.min(((float)maxHeight / mainBitmap.getWidth()), ((float)maxWidth / mainBitmap.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            mainBitmap = Bitmap.createBitmap(mainBitmap, 0, 0, mainBitmap.getWidth(), mainBitmap.getHeight(), matrix, true);
        }else if(ex.getInt("action") == 2){
            byte[] byteArray = ex.getByteArray("image");
            this.mainBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        }
        mainView.setImageBitmap(mainBitmap);

        histo = new Histogram(mainBitmap);

        // debug
        Log.i("Tuyul","Histograms");
        for(int i=0;i<=360;i++) {
            Log.i("Tuyul", "Histo : " + i + "-" + histo.h[i]);
            int ha = histo.h[i]/2;
            DataPoint d = new DataPoint(i,histo.h[i]);
            //DataPoint d = new DataPoint(i,ha);
            histod[i] = d;
        }
        int c = 10;
        int[][] best = histo.getTop(c);
        Log.i("Tuyul", "Top hue index");
        for(int j = 0; j < 10; j++){
            Log.i("Tuyul","#"+(j+1)+" : "+best[j][0]+" idx "+best[j][1]);
        }

        warna.setText(Tuyul.determine(best,c));
        nom = Tuyul.determine(best, c, true);

        if(nom == 0){
            nominal.setText("Uang tidak terdeteksi");
        }else {
            nominal.setText("Rp. "+nom+",-");
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        /*
        for(int i=0;i<=360;i++) {
            series.appendData(new DataPoint(i,histo.h[i]),true,360);
        }*/
        series.resetData(histod);

        graph.addSeries(series);

        speak("This is "+nom+" rupiah");
    }

    private void speak(String text){
        if(tts != null){
            if (!tts.isSpeaking()){
                //tts.speak(nominal.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                //tts.setLanguage()
                //tts.speak(text, TextToSpeech.QUEUE_FLUSH,null,null);
                if (Build.VERSION.RELEASE.startsWith("5")) {
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                }else {
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_speak) {
            speak("This is "+nom+" rupiah");;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            Locale locInd = new Locale("ind-IDN");
            tts.setLanguage(locInd);
        }else{
            Toast.makeText(this, "TTS error", Toast.LENGTH_SHORT).show();
        }
    }
}
