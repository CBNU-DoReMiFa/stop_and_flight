package com.example.stop_and_flight;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.DatePicker;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.stop_and_flight.model.CalendarModel;
import com.example.stop_and_flight.singleton.CalendarSingle;
import com.example.stop_and_flight.singleton.Schedule;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.String.valueOf;

public class CalendarActivity extends AppCompatActivity {
    public static final String RESULT = "result";
    public static final String EVENT = "event";
    private static final int ADD_NOTE = 44;
    private CalendarView mCalendarView;
    //private TextView textView;
    private List<EventDay> mEventDays = new ArrayList<>();
    private List<CalendarSingle> gCalendarSingles;
    private CalendarModel calendarModel = new CalendarModel("캘린더");
    private List<EventDay> gEventDats = new ArrayList <>();
    private List<Schedule> mSchedules = new ArrayList <>();
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;



    public String getPostType() {
        return "캘린더";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2021,5-1,13);
        List<EventDay> eventDay = new ArrayList <>();
        mCalendarView.setDate(calendar1);
        mCalendarView.setEvents(eventDay);

        /* this.mSchedules = scheduleModel.getSchedules();*/

        this.gCalendarSingles = calendarModel.getmCalendars();
        Log.i("가져xxf옴",valueOf(gCalendarSingles.size()));
        for(int i = 0; i< gCalendarSingles.size(); i++) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(gCalendarSingles.get(i).getYear(), gCalendarSingles.get(i).getMonth(), gCalendarSingles.get(i).getDay());
            gEventDats.add(new EventDay(calendar,R.drawable.ic_message_black_48dp)); //이벤트가 있는 날짜에 빨간원 표시
            mCalendarView.setDate(gEventDats.get(i).getCalendar());
            mCalendarView.setEvents(gEventDats);
        }
        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) { //날짜를 클릭할때마다 해당 날짜의 목표,별점,메모 뷰 보여주는 이벤트
                previewNote(eventDay);
                // setTextPreview(eventDay);
            }
        });
        // textView = findViewById(R.id.preview_note);
/*        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });*/

        ValueEventListener calendarlistener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get CalendarSingle object and use the values to update the UI
//                CalendarSingle calendarSingle = dataSnapshot.getValue(CalendarSingle.class);
//                MyEventDay myEventDay;
//                Intent returnIntent = new Intent();
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(calendarSingle.getYear(),calendarSingle.getMonth(),calendarSingle.getDay());
//                myEventDay = new MyEventDay(calendar, R.drawable.ic_message_black_48dp, calendarSingle.getNote());
//                returnIntent.putExtra(CalendarActivity.RESULT, myEventDay);
//                setResult(Activity.RESULT_OK, returnIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("cancelled", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        // databaseReference.addValueEventListener(calendarlistener);


    }

    private void previewNote(EventDay eventDay) {
        /*FrameLayout frame = (FrameLayout) findViewById(R.id.frame) ; //"목표,별점,메모" 프레임레이아웃*/
        Intent intent=new Intent(CalendarActivity.this,CalendarlistActivity.class);
        startActivity(intent);
        finish();
    }

    //note페이지로 이동
/*        Intent intent = new Intent(this, NotePreviewActivity.class);
        if(eventDay instanceof MyEventDay){
            intent.putExtra(EVENT, (MyEventDay) eventDay);
        }
        startActivity(intent);*/


/*    DatePickerBuilder builder = new DatePickerBuilder(this, listener)
            .pickerType(CalendarView.ONE_DAY_PICKER);

    DatePicker datePicker = builder.build();
    datePicker.show();*/

    /*    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
                MyEventDay myEventDay = data.getParcelableExtra(RESULT);
                mCalendarView.setDate(myEventDay.getCalendar());
                mEventDays.add(myEventDay);
                mCalendarView.setEvents(mEventDays);
            }
        }*/
    private void addNote() {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE);
    }


//    private void setTextPreview(EventDay eventDay) {
//        textView.setText();
//        //이후에 캘린더 텍스트뷰에 미리보기 추가
//    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.show();
    }

    private  void invalidateRecursive(ViewGroup layout){

        int count = layout.getChildCount();
        View child ;
        for (int i = 0; i<count ;i++){
            child = layout.getChildAt(i);
            if (child instanceof ViewPager){
                ViewPager pager = (ViewPager)child;
                pager.getAdapter().notifyDataSetChanged();
            }
            if (child instanceof ViewGroup){
                ViewGroup group = (ViewGroup)child;
                invalidateRecursive(group);
            }
        }
    }
}