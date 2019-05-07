package tong.ziwei.phonecommon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;

public class MainActivity extends AppCompatActivity {
    private EditText etPhone;
    private TextView mTvPhoneAddress;
    private Button mBtnSearch;
    private String phoneNumber;
    private String phoneAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etPhone = (EditText)findViewById(R.id.et_phone);
        mTvPhoneAddress = (TextView)findViewById(R.id.tv_phone_address);
        mBtnSearch = (Button)findViewById(R.id.btn_search);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = etPhone.getText().toString();
                if(!phoneNumber.equals("")){
                    phoneAddress = PhoneUtil.getGeoDescription(MainActivity.this,phoneNumber); //手机号必须前面加上+86
                    /*try {
                        phoneAddress = PhoneUtil.GetCarrier(phoneNumber);
                    } catch (NumberParseException e) {
                        e.printStackTrace();
                    }*/
                    if(phoneAddress!=null){
                        mTvPhoneAddress.setText(phoneAddress);
                    }
                }

            }
        });
    }
}
