package stackexchange.tlab.com.tlaboverflow;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Character.TYPE;

public class RangeDateActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtStartDate)
    TextView txtStartDate;
    @BindView(R.id.txtEndDate)
    TextView txtEndDate;
    @BindView(R.id.btnStartDate)
    LinearLayout btnStartDate;
    @BindView(R.id.btnEndDate)
    LinearLayout btnEndDate;
    @BindView(R.id.btnFilter)
    Button btnFilter;

    public int TYPE = 0;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog dialog;
    String before,after,start=null,end=null;
    long timestampStart,timestampEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_date);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.range_date);

//        Locale id = new Locale("in", "ID");
//        final String pattern = "dd-MM-yyyy";
        Date today = new Date();
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//        SimpleDateFormat sdf = new SimpleDateFormat(pattern, id);
        before = sdf.format(today);

//        Log.d("TGL"," pertama"+today.getTime());
        try {
            Date mDate = sdf.parse(before);
            Date mDate2 = sdf.parse(before);
            long timeInMilliseconds = mDate.getTime();
            long timeInMilliseconds2 = mDate2.getTime();
            timestampStart=timeInMilliseconds/1000;
            timestampEnd=timeInMilliseconds2/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref",0);
        start = preferences.getString("start",before);
        end = preferences.getString("end",before);
        txtStartDate.setText(start);
        txtEndDate.setText(end);


//        myCalendar = Calendar.getInstance();
//        date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
////                updateLabel(TYPE);
//            }
//        };

        final Calendar myCalendar = Calendar.getInstance();
        final Calendar myCalendar2 = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                timestampStart = myCalendar.getTimeInMillis() / 1000;
                Log.d("imesti", "updateLabel: "+timestampStart);
                txtStartDate.setText(sdf.format(myCalendar.getTime()));
                start = sdf.format(myCalendar.getTime());
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref",0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("timestampStart",String.valueOf(timestampStart));
                editor.putString("timestampEnd",String.valueOf(timestampEnd));
                editor.putString("start",start);
                editor.putString("end",end);
                editor.commit();
            }

        };
        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                timestampEnd = myCalendar2.getTimeInMillis() / 1000;
                Log.d("imestii", "updateLabel: "+timestampEnd);
                txtEndDate.setText(sdf.format(myCalendar2.getTime()));
                end = sdf.format(myCalendar2.getTime());
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref",0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("timestampStart",String.valueOf(timestampStart));
                editor.putString("timestampEnd",String.valueOf(timestampEnd));
                editor.putString("start",start);
                editor.putString("end",end);
                editor.commit();
            }

        };
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TYPE = 0;
                dialog = new DatePickerDialog(RangeDateActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                myCalendar.add(Calendar.MONTH, -6);

                dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                dialog.show();
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TYPE = 1;
                dialog = new DatePickerDialog(RangeDateActivity.this, date2, myCalendar2
                        .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                        myCalendar2.get(Calendar.DAY_OF_MONTH));
                myCalendar2.add(Calendar.MONTH, -6);

                dialog.getDatePicker().setMinDate(myCalendar2.getTimeInMillis());
                dialog.show();
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }



    private void updateLabel(int type) {

        if(type ==0) {
//            Locale id = new Locale("in", "ID");
//            final String pattern = "dd-MM-yyyy";
//            SimpleDateFormat sdf = new SimpleDateFormat(pattern, id);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            after = sdf.format(myCalendar.getTime());
            try {
                Date mDate = sdf.parse(after);
                long timeInMilliseconds = mDate.getTime();
                timestampStart=timeInMilliseconds/1000;
                txtStartDate.setText(after);
                start= after;
                Log.d("imestii", "updateLabel: "+timestampEnd);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            txtStartDate.setText(after);
//            after = sdf.format(myCalendar.getTime());
//            start= after;
//            timestampStart = myCalendar.getTimeInMillis();

        }else if(type == 1) {

//            Locale id = new Locale("in", "ID");
//            final String pattern = "dd-MM-yyyy";
//            SimpleDateFormat sdf2 = new SimpleDateFormat(pattern, id);
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
            after = sdf2.format(myCalendar.getTime());
            try {
                Date mDate2 = sdf2.parse(after);
                long timeInMilliseconds = mDate2.getTime();
                timestampEnd=timeInMilliseconds/1000;
                Log.d("imesti", "updateLabel: "+timestampEnd);
                txtEndDate.setText(after);
                end =after;
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

//        Toast.makeText(this, "" + after, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
