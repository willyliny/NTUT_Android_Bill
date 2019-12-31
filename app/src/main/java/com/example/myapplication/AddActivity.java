package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private FirebaseUser user;
    private TextView textViewDate;
    private RadioGroup pay;
    private EditText ed_name,ed_price;
    private String pay_choose= "現金";
    private String pay_type="外食";
    private Button send;
    private SQLiteDatabase dbrw;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private int months=0;
    private int days=0;
    private int years=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Calendar c=Calendar.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String[] typeArray ={"外食","公車","捷運","飲料","加油","房租","娛樂","日常用品","電話費","網購","停車費","房租","家電","家具","水電費","旅行"};
        ed_name=findViewById(R.id.ed_text1);
        ed_price=findViewById(R.id.ed_text2);
        send=findViewById(R.id.button2);
        pay=findViewById(R.id.pay_group);
        spinner = findViewById(R.id.spinner);
        textViewDate=findViewById(R.id.textDate);
        dbrw=new DataBase(this).getWritableDatabase();
        adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,typeArray);
        spinner.setAdapter(adapter);
        Bundle bundle=getIntent().getExtras();
        if (bundle==null)
        {
            months=c.get(Calendar.MONTH)+1;
            days=c.get(Calendar.DAY_OF_MONTH);
        }
        else
        {
            months=bundle.getInt("months");
            days=bundle.getInt("days");
            ed_name.setText(bundle.getString("name"));
            ed_price.setText(Integer.toString(bundle.getInt("price")));
            pay_choose=bundle.getString("pay");
            pay_type=bundle.getString("type");
            for (int i=0;i<typeArray.length;i++)
                if (typeArray[i].equals(pay_type))
                    spinner.setSelection(i);
            if (pay_choose.equals("信用卡"))
                pay.check(R.id.pay_credit);
            else if (pay_choose.equals("電子票證"))
                pay.check(R.id.pay_ticket);
            else if (pay_choose.equals("行動支付"))
                pay.check(R.id.pay_barcode);
            else
                pay.check(R.id.pay_cash);
        }
        years=c.get(Calendar.YEAR);
        textViewDate.setText(years+"年"+months+"月"+days+"日");
        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DataPickerFregment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        pay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.pay_cash:
                        pay_choose="現金";
                        break;
                    case R.id.pay_credit:
                        pay_choose="信用卡";
                        break;
                    case R.id.pay_ticket:
                        pay_choose="電子票證";
                        break;
                    case R.id.pay_barcode:
                        pay_choose="行動支付";
                        break;
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (ed_name.length()<1 || ed_price.length()<1)
                    Toast.makeText(AddActivity.this,"欄位請勿留空",Toast.LENGTH_SHORT).show();
                else
                {
                    try
                    {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("Style",pay_type);
                        map.put("name",ed_name.getText().toString());
                        map.put("cost",ed_price.getText().toString());
                        db.collection(user.getDisplayName())
                                .add(map)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(AddActivity.this,"DocumentSnapshot added with ID: " + documentReference.getId(),Toast.LENGTH_SHORT).show();
                                    }

                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddActivity.this,"Error adding document: "+e,Toast.LENGTH_SHORT).show();

                                    }
                                });
                        Log.e("123", "!");

                        //,months,days
                        dbrw.execSQL("insert into charge(name,type,pay,price,months,days)values(?,?,?,?,?,?)",new Object[]{ed_name.getText().toString(),pay_type,pay_choose,ed_price.getText().toString(),months,days});



                        ed_name.setText("");
                        ed_price.setText("");


                    }catch (Exception e){
                        Toast.makeText(AddActivity.this,"新增失敗"+e.toString(),Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent();
                    intent.setAction("Hello");
                    sendBroadcast(intent);
                    finish();
                }
            }

        });
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDateString= DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        months=c.get(Calendar.MONTH)+1;
        days=c.get(Calendar.DAY_OF_MONTH);
        years=c.get(Calendar.YEAR);
        textViewDate.setText(years+"年"+months+"月"+days+"日");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        dbrw.close();
    }
}
