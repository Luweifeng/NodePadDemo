03.-+
        package share.imooc.com.nodepaddemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.zeffect.view.recordbutton.RecordButton;
import me.iwf.photopicker.PhotoPicker;
import share.imooc.com.nodepaddemo.R;
import share.imooc.com.nodepaddemo.model.Message;
import share.imooc.com.nodepaddemo.widget.PictureAndTextEditorView;

public class AddActivity extends AppCompatActivity {
    private TextView tvCommit;
    private ImageView ivBack;
    private ImageView ivPhoto;
    private ImageView ivTuya;
    private PictureAndTextEditorView richEditText;
    private RecordButton recordButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add);
        initView();
        initData();
    }

    private void initData() {
        richEditText.setMovementMethod(LinkMovementMethod.getInstance());
        recordButton.setSavePath(Environment.getExternalStorageDirectory().getAbsolutePath());

        recordButton.setPrefix("myvoice");
        recordButton.setMaxIntervalTime(60000);
        recordButton.setMinIntervalTime(2000);
        recordButton.setTooShortToastMessage("录音时间应该长点");
        recordButton.setOnFinishedRecordListener(new RecordButton.OnFinishedRecordListener() {
            @Override
            public void onFinishedRecord(String s) {
                 //Toast.makeText(AddActivity.this, "储存路径为："+s, Toast.LENGTH_SHORT).show();
                //String img=Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"speak.png";
                richEditText.insertVoiceBitmap(s);
            }

            @Override
            public void readCancel() {

            }

            @Override
            public void noCancel() {

            }

            @Override
            public void onActionDown() {

            }

            @Override
            public void onActionUp() {

            }

            @Override
            public void onActionMove() {

            }
        });
    }

    private void initView() {
        richEditText= (PictureAndTextEditorView) findViewById(R.id.richEditText);
        tvCommit= (TextView) findViewById(R.id.tv_commit);
        ivBack= (ImageView) findViewById(R.id.iv_back);
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=richEditText.getText().toString();
                if (content.length()>0){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    Date curDate =  new Date(System.currentTimeMillis());
                    String   time   =   formatter.format(curDate);
                    Message msg=new Message();
                    msg.setTime(time);
                    msg.setContent(content);;
                    msg.save();
                }
                finish();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivPhoto= (ImageView) findViewById(R.id.iv_photo);
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        .setPhotoCount(9)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(true)
                        .start(AddActivity.this, PhotoPicker.REQUEST_CODE);

        ivTuya= (ImageView) findViewById(R.id.iv_tuya);
        ivTuya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddActivity.this,TuyaActivity.class);
                startActivityForResult(intent,1001);

            }
        });


        recordButton= (RecordButton) findViewById(R.id.recordButton);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                for (int i = 0; i <photos.size() ; i++) {
                    richEditText.insertBitmap(photos.get(i));
                }

            }
        }else if(requestCode==1001&&resultCode==1002){
            String path=data.getStringExtra("path");
            richEditText.insertBitmap(path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
