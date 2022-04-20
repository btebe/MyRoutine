package com.myroutine.myroutine;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.myroutine.myroutine.DbBitmapUtility.getBytes;
import static com.myroutine.myroutine.R.drawable.list;
import static java.sql.Types.BLOB;

public class NewStep extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_CHOOSE_IMAGE = 4;
    static final int REQUEST_AUDIO_RECORDING = 2;

    EditText name;
    Uri savedUri;
    ImageView addpicview;
    ImageButton voicerecbtn;
    private int  mSec, mMinute, ampm;
    TextView valtimer, steptitle;
    Button donebtn, delete;
    Step item;
    DatabaseHelper mydb;
    //File path;
    String audiopath, imgpath, fileHoler;
    Bitmap imgholder;




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_step);
        //List<Step> list2 = mydb.getAllSteps();
        //showMessage("data", list2.toString());
        item = new Step();
        addpicview= (ImageView)findViewById(R.id.addpicview);
        voicerecbtn = (ImageButton)findViewById(R.id.voicerecbtn);
        valtimer = (TextView)findViewById(R.id.valtimer);
        steptitle = (TextView)findViewById(R.id.stepnamae);
        mydb = new DatabaseHelper(this);
        donebtn = (Button) findViewById(R.id.donebtn);
        donebtn.setTextColor(ContextCompat.getColor(NewStep.this, R.color.lavendergrey));
        donebtn.setEnabled(false);
        name  = (EditText) findViewById(R.id.step_name);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence chars, int i, int i1, int i2) {
                if(chars.length() != 0){
                    donebtn.setTextColor(ContextCompat.getColor(NewStep.this, R.color.black));
                    donebtn.setEnabled(true);
                }else{
                    donebtn.setTextColor(ContextCompat.getColor(NewStep.this, R.color.lavendergrey));
                    donebtn.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        if(getIntent().hasExtra("EditMode")) {
           // String[] steparray = {selectedItem.getName(), selectedItem.getPic(),
                    //selectedItem.getAudio(), selectedItem.getTimer()};
            steptitle.setText("Edit Step");
            String[] returned = getIntent().getExtras().getStringArray("steparray");
            name.setText(returned[0]);
            valtimer.setText(returned[3]);
            audiopath = returned[2];
            imgpath = returned[1];
            if(imgpath != null){
                Bitmap x = BitmapFactory.decodeFile(imgpath);
                addpicview.setImageBitmap(x);
            }
            delete = (Button) findViewById(R.id.delete);
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showMessage2("confirmation", "are you sure?");

                }
            });
        }



    }
    //alos goes back to new task and can edit stuff there
    //checks if task name in db if not it will create one
    public void backFunction(View view){
        //go back to task page
        if(getIntent().hasExtra("taskinfo")){
            Intent x = new Intent(this,NewTask.class);
            String[] task =  getIntent().getExtras().getStringArray("taskinfo");
            x.putExtra("returned",task);
            startActivity(x);
        } else if(getIntent().getExtras().getBoolean("EditMode")){
            //this activity launched from selected item
            int tid = getIntent().getExtras().getInt("taskid");
            Intent x = new Intent(this,ListOfSteps.class);
            x.putExtra("taskid", tid);
            startActivity(x);
        }else{
            //normal adding
            String nametask = getIntent().getExtras().getString("tasknamere");
            int taskid = getIntent().getExtras().getInt("taskidre");
            Intent x = new Intent(this,ListOfSteps.class);
            x.putExtra("taskname", nametask);
            x.putExtra("taskid", taskid);
            startActivity(x);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void timerFunction(View view) {
        /*Intent x = new Intent(this,TimerPage.class);
        startActivity(x);*/
        // Launch Time Picker Dialog
        //NumberPicker np = new NumberPicker(this, NumberPicker)
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, TimePickerDialog.THEME_HOLO_LIGHT,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int min, int sec) {
                        //view.setIs24HourView(true);
                        //String.format("%02d:%02d:%02d", durationMinutes / 60, durationMinutes % 60, 0);
                        valtimer.setText(String.format("%d:%02d", min, sec));
                    }
                }, mMinute, mSec, true);

        timePickerDialog.show();


    }


    public void doneFunction(View view){

        if(getIntent().hasExtra("taskinfo")) {
            //add task and add step
            String[] task = getIntent().getExtras().getStringArray("taskinfo");
            boolean isInserted = mydb.addTask(new Task(task[0], task[1], task[2]));
            if (isInserted) {
                Toast.makeText(this, "inserted in task", Toast.LENGTH_LONG).show();
                Task dbtask = mydb.getTask(task[0]);
                //store picCam and picGallery file to device
                if(imgholder != null){
                   File picpath= storeImage2(imgholder);
                    fileHoler = picpath.getAbsolutePath();
                }

                String namestep = name.getText().toString();
                String time = valtimer.getText().toString();
                boolean isInserted2 =mydb.addStep(new Step(namestep,item.getAudio(),
                        time,fileHoler,dbtask.getId()));
                if(isInserted2){
                    Toast.makeText(this, "inserted in step", Toast.LENGTH_LONG).show();
                    Intent x = new Intent(this, ListOfSteps.class);
                    x.putExtra("taskname", task[0]);
                    x.putExtra("taskid", dbtask.getId());
                    startActivity(x);
                }else{
                    Toast.makeText(this, "not inserted in step", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "not inserted in task", Toast.LENGTH_LONG).show();
            }
        }else if(getIntent().hasExtra("taskidre")){
            //add step from add button
            //store picCam and picGallery file to device
            if(imgholder != null){
                File picpath= storeImage2(imgholder);
                fileHoler = picpath.getAbsolutePath();
            }
            int taskid = getIntent().getExtras().getInt("taskidre");
            String nametask = getIntent().getExtras().getString("tasknamere");
            String namestep = name.getText().toString();
            String time = valtimer.getText().toString();
            boolean isInserted2 =mydb.addStep(new Step(namestep,item.getAudio(),time
                    ,fileHoler,taskid));
            if(isInserted2){
                Toast.makeText(this, "inserted in step", Toast.LENGTH_LONG).show();
                Intent x = new Intent(this, ListOfSteps.class);
                x.putExtra("taskname", nametask);
                x.putExtra("taskid", taskid);
                startActivity(x);
            }else{
                Toast.makeText(this, "not inserted in step", Toast.LENGTH_LONG).show();
            }

        }else if(getIntent().getExtras().getBoolean("EditMode")){
            //update step
            Step step = new Step();
            step.setName(name.getText().toString());
            if(imgholder != null){
                File picpath= storeImage2(imgholder);
                fileHoler = picpath.getAbsolutePath();
                step.setPic(fileHoler);

                if(imgpath != null){
                    File x = new File(imgpath);
                    x.delete();
                }
            }
            step.setAudio(item.getAudio());
           //item-position
            int id = getIntent().getExtras().getInt("item-position");
            step.setId(id);
            step.setTimer(valtimer.getText().toString());
            mydb.updateStep(step);
            int tid = getIntent().getExtras().getInt("taskid");
            Task task = mydb.getTask(tid);
            Intent x = new Intent(this,ListOfSteps.class);
            x.putExtra("taskid", tid);
            x.putExtra("taskname", task.getName());
            startActivity(x);
            //showMessage("result", update+"");


        }


    }
    //app opens camera app from phone
    //once app completed we want it to come back to our app
    public void takePhoto(View view){
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent x = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (x.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(x, REQUEST_IMAGE_CAPTURE);
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CHOOSE_IMAGE);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }


    public void recordVoice(View view){

        //editMode
        if(getIntent().getExtras().getBoolean("EditMode")){
            Intent x = new Intent(this, RecordingPage.class);
            x.putExtra("editMode", audiopath);
            startActivityForResult(x, REQUEST_AUDIO_RECORDING);
        }else{
            Intent x = new Intent(this, RecordingPage.class);
            x.putExtra("playback", item.getAudio());
            startActivityForResult(x, REQUEST_AUDIO_RECORDING);
        }

    }
    //where we retrieve the results from the actions
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        if (requestCode == REQUEST_IMAGE_CAPTURE  && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmapCam = (Bitmap) extras.get("data");
            addpicview.setImageBitmap(imageBitmapCam);
            imgholder = imageBitmapCam;
            //File x = storeImage2(imageBitmapCam);
            //byte[] image = getBytes(imageBitmap);
            //item.setPic(x.getAbsolutePath());
        }
        if(requestCode == REQUEST_CHOOSE_IMAGE && resultCode == RESULT_OK){
            //Bundle extras = data.getExtras();
            //Bitmap src=BitmapFactory.decodeFile(data.getData());
            //Uri imageUri = data.getData();
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }
            addpicview.setImageBitmap(imageBitmap);
            Bitmap imgGallery  = getResizedBitmap(imageBitmap,500);
            imgholder = imgGallery;
            //File x2 = storeImage2(imgGallery);
            //item.setPic(x2.getAbsolutePath());
        }
        if (requestCode == REQUEST_AUDIO_RECORDING) {
            if(resultCode == RESULT_OK) {
                String audiofile = data.getStringExtra("audio");
                //Toast.makeText(this,audiofile,Toast.LENGTH_LONG).show();
                item.setAudio(audiofile);
            }
        }


    }
    String TAG = "Warning";

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }


    }

    private  File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("ddMMyyyy_HHmm");
        String timeStamp = fmt.format(new Date());
        //String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName= timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public void showMessage(String title, String message){
        android.app.AlertDialog.Builder dia = new android.app.AlertDialog.Builder(this);
        dia.setCancelable(true);
        dia.setTitle(title);
        dia.setMessage(message);
        dia.show();
    }

    public void showMessage2(String title, String msg){
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //List <Step> x= mydb.getAllSteps();
                int n = getIntent().getExtras().getInt("item-position");
                Step step = mydb.getStep(n);
                if(step.getAudio() != null){
                    File deleteaudio = new File(step.getAudio());
                    deleteaudio.delete();
                }
                if(step.getPic() != null){
                    File deletepic = new File(step.getPic());
                    deletepic.delete();
                }


               // Toast.makeText(NewStep.this, step.toString(), Toast.LENGTH_LONG).show();
                mydb.deleteStep(n);
                int tid = getIntent().getExtras().getInt("taskid");
                Intent intent = new Intent(NewStep.this, ListOfSteps.class);
                intent.putExtra("taskid", tid);
                startActivity(intent);
            }
        });
        //cancels
        alertDialog.setCancelable(true);
        alertDialog.show();
       // android.app.AlertDialog ald = alertDialog.create();
       // ald.show();
    }


    private File storeImage2(Bitmap imageData) {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/myimgs/";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("MM-dd_HH:mm:ss");
        String timeStamp = fmt.format(new Date());
        File file = new File(dir, "sketchpad" + timeStamp + ".png");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        imageData.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;


    }
}
