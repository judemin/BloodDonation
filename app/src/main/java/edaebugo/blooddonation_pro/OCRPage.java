package edaebugo.blooddonation_pro;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.appcompat.app.AppCompatActivity;

public class OCRPage extends AppCompatActivity {

    TessBaseAPI tessBaseAPI;

    Button button;
    ImageView imageView;
    CameraSurfaceView surfaceView;
    TextView textView;

    public static String ocrData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("헌혈증 자동인식");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        imageView = findViewById(R.id.imageView);
        surfaceView = findViewById(R.id.surfaceView);
        textView = findViewById(R.id.textView);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                capture();
            }
        });

        tessBaseAPI = new TessBaseAPI();
        String dir = getFilesDir() + "";
        Log.e("OCRPage","" + dir);
        if(checkLanguageFile(dir+"/tessdata")) {
            Log.e("OCRPage","" + dir);
            Log.e("OCRPage","Initiating tessBaseAPI");
            tessBaseAPI.init(dir, "eng");
        }
        else
            Log.e("OCRPage","Check LanguageFile Failed");
    }

    public void inputOCRData(View view){
        Log.e("OCRPage","" + ocrData);
        UploadBillf.fuploadBillBundle.putString("ocrData",ocrData);
        finish();
    }

    boolean checkLanguageFile(String dir)
    {
        File file = new File(dir);
        if(!file.exists() && file.mkdirs()) {
            createFiles(dir);
            Log.e("OCRPage_checkFile","Created Fle Directory");
        }
        else if(file.exists()){
            String filePath = dir + "/eng.traineddata";
            File langDataFile = new File(filePath);
            if(!langDataFile.exists())
                createFiles(dir);
            Log.e("OCRPage_checkLanFile","" + dir + " File Directory Exists");
            return true;
        }
        return false;
    }

    private void createFiles(String dir)
    {
        AssetManager assetMgr = this.getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            Log.e("OCRPage_CreateFile","Opening InputStream");

            inputStream = assetMgr.open("eng.traineddata");

            String destFile = dir + "/eng.traineddata";

            Log.e("OCRPage_CreateFile","Opening OutputStream");

            outputStream = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            Log.e("OCRPage_CreateFile","eng.traineddate IOStream Completed");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void capture()
    {
        surfaceView.capture(new Camera.PictureCallback() { //
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmap = GetRotatedBitmap(bitmap, 90);
                UploadBillf.fuploadBillBundle.putParcelable("BitmapImage",bitmap);
                imageView.setImageBitmap(bitmap);

                button.setEnabled(false);
                button.setText("텍스트 인식중...");
                new AsyncTess().execute(bitmap);

                camera.startPreview();
            }
        });
    }

    public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != b2) {
                    bitmap = b2;
                }
            } catch (OutOfMemoryError ex) {
                ex.printStackTrace();
            }
        }
        return bitmap;
    }

    private class AsyncTess extends AsyncTask<Bitmap, Integer, String> {
        @Override
        protected String doInBackground(Bitmap... mRelativeParams) {
            tessBaseAPI.setImage(mRelativeParams[0]);
            return tessBaseAPI.getUTF8Text();
        }

        protected void onPostExecute(String result) {
            textView.setText(result);
            ocrData = result;
            //Toast.makeText(OCRPage.this, ""+result, Toast.LENGTH_LONG).show();

            button.setEnabled(true);
            button.setText("텍스트 인식");
        }
    }
}
