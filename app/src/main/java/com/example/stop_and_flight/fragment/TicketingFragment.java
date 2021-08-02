package com.example.stop_and_flight.fragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.stop_and_flight.AlarmReceiver;
import com.example.stop_and_flight.MainActivity;
import com.example.stop_and_flight.R;
import com.example.stop_and_flight.TicketDatabaseHandler;
import com.example.stop_and_flight.model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketingFragment extends Fragment {

    Context mContext;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String Todo = null;
    private String UID;
    private AlarmManager AM;
    private PendingIntent ServicePending;
    int dptH;
    int dptM;
    private DatabaseReference mDatabase;
    private int YEAR;
    private int MONTH;
    private int DAY;
    private int flag = 1;
    private int id = 0;
    private int updateId;
    private String updateDepart;
    private String updateArrive;
    private String ticket_Date;
    public String strCurYear;
    public String strCurMonth;
    public String strCurDay;
    public String strCurHour;
    public String strCurMinute;
    private HashMap<String, Object> TicketMap;

    public TicketingFragment() {
        // Required empty public constructor
    }

    private TextView textView;
    private ScrollChoice scrollChoice;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ticketing_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketingFragment newInstance(String param1, String param2, Bundle ticket) {
        TicketingFragment fragment = new TicketingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putBundle("ticket", ticket);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            if (getArguments().getBundle("ticket") != null)
                Todo = getArguments().getBundle("ticket").getString("Todo");
            if (mParam1 != null)
            {
                updateId = getArguments().getBundle("ticket").getInt("Id");
                updateDepart = getArguments().getBundle("ticket").getString("Depart_time");
                updateArrive = getArguments().getBundle("ticket").getString("Arrive_time");
            }
        }

        /* 현재 시간과 날짜를 받아오는 부분 */
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat CurHourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat CurMinuteFormat = new SimpleDateFormat("mm");

        strCurYear = CurYearFormat.format(date);
        strCurMonth = CurMonthFormat.format(date);
        strCurDay = CurDayFormat.format(date);
        strCurHour = CurHourFormat.format(date);
        strCurMinute = CurMinuteFormat.format(date);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext= context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticketing, container, false);

        Button select_data_button = (Button)view.findViewById(R.id.select_data_button);
        Button select_todo_button = (Button) view.findViewById(R.id.select_todo_button);
        TimePicker depart_time = (TimePicker)view.findViewById(R.id.depart_time);
        TimePicker arrive_time = (TimePicker)view.findViewById(R.id.arrive_time);
        Button ticketing_button = (Button) view.findViewById(R.id.ticketing_button);

        select_todo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mParam1 != null)
                    ((MainActivity) getActivity()).replaceFragment(SelectTodoFragment.newInstance(mParam1, null, getArguments().getBundle("ticket")));
                else
                    ((MainActivity) getActivity()).replaceFragment(SelectTodoFragment.newInstance(null, null, null));
            }
        });

        if (Todo != null)
        {
            select_todo_button.setText(Todo);
        }

        YEAR =  Integer.parseInt(strCurYear);
        MONTH =  Integer.parseInt(strCurMonth);
        DAY =  Integer.parseInt(strCurDay);
        ticket_Date = YEAR + "-" + MONTH + "-" + DAY;
        select_data_button.setText(YEAR + " 년 " + MONTH + " 월 " + DAY + " 일");

        select_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c=Calendar.getInstance();
                int mYear=c.get(Calendar.YEAR);
                int mMonth=c.get(Calendar.MONTH);
                int mDay=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog =new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        select_data_button.setText(year+ "년 "+ (monthOfYear + 1) + " 월 " + dayOfMonth + " 일");
                        YEAR = year;
                        MONTH = monthOfYear + 1;
                        DAY = dayOfMonth;
                        String str_year = Integer.toString(year);
                        String str_month = Integer.toString(monthOfYear+1);
                        String str_day = Integer.toString(dayOfMonth);
                        ticket_Date = str_year  + "-" + str_month  + "-" + str_day;
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        ticketing_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int depart_hour = Integer.parseInt(strCurHour);
                int depart_min = Integer.parseInt(strCurMinute);
                int arrive_hour = Integer.parseInt(strCurHour);
                int arrive_min = Integer.parseInt(strCurMinute);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    depart_hour = depart_time.getHour();
                    depart_min = depart_time.getMinute();
                    arrive_hour = arrive_time.getHour();
                    arrive_min = arrive_time.getMinute();
                    dptH = depart_hour;
                    dptM = depart_min;
                }
                if (YEAR >= Integer.parseInt(strCurYear) && MONTH >= Integer.parseInt(strCurMonth))
                {
                    if (DAY > Integer.parseInt(strCurDay))
                        check_Schedule(ticket_Date, depart_hour,depart_min, arrive_hour, arrive_min);
                    else if (DAY == Integer.parseInt(strCurDay))
                    {
                        if((Integer.parseInt(strCurHour) < depart_hour) || ((Integer.parseInt(strCurHour) == depart_hour) && (Integer.parseInt(strCurMinute) <= depart_min)))
                            check_Schedule(ticket_Date, depart_hour,depart_min, arrive_hour, arrive_min);
                        else
                            Toast.makeText(getContext(), "현재 보다 이전 시간을 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(getContext(), "현재 보다 이전 시간을 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void check_Schedule(String Date, int depart_hour , int depart_min, int arrive_hour, int arrive_min)
    {
        mDatabase.child("TICKET").child(UID).child(Date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                flag = 0;
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    if (fileSnapshot != null) {
                        TicketMap = (HashMap<String, Object>) fileSnapshot.getValue();

                        String depart = String.valueOf(TicketMap.get("depart_time"));
                        String arrive = String.valueOf(TicketMap.get("arrive_time"));
                        id = Integer.parseInt(String.valueOf(TicketMap.get("id")));
                        String[] depart_arr = depart.split(":");
                        String[] arrive_arr = arrive.split(":");

                        if (Integer.parseInt(depart_arr[0]) > arrive_hour ||
                                (Integer.parseInt(depart_arr[0]) == arrive_hour &&
                                        Integer.parseInt(depart_arr[1]) > arrive_min))
                            continue;
                        else if (Integer.parseInt(arrive_arr[0]) < depart_hour ||
                                (Integer.parseInt(arrive_arr[0]) == depart_hour &&
                                        Integer.parseInt(arrive_arr[1]) < depart_min))
                            continue;
                        else {
                            flag = 1;
                            break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });
        if (flag == 0)
        {
            time_Validity(depart_hour, depart_min, arrive_hour, arrive_min);
            Toast.makeText(getContext(),  "예약 되었습니다.", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getContext(),  "이미 예약된 일정이 있습니다.", Toast.LENGTH_SHORT).show();
    }


    private void time_Validity(int depart_hour , int depart_min, int arrive_hour, int arrive_min)
    {
        TicketDatabaseHandler db = new TicketDatabaseHandler(mDatabase);
        if (Todo == null)
            Todo = "default";
        if (depart_hour > 12 && arrive_hour < 12)
        {
            String depart_time =  depart_hour + ":" + depart_min;
            String arrive_time =  arrive_hour + ":" + arrive_min;
            Ticket ticket = new Ticket(depart_time, arrive_time, Todo, ++id , "true");

            if (mParam1 != null)
            {
                db.update_ticketDB(UID, ticket_Date, depart_time, arrive_time, Todo, updateId);
                ((MainActivity) getActivity()).replaceFragment(TicketListFragment.newInstance(null, null));
            }
            else
                db.insert_ticketDB(UID, ticket_Date, ticket);
            onDestroy();
        }
        else
        {
            if((depart_hour < arrive_hour) || ((depart_hour == arrive_hour) && (depart_min <= arrive_min)))
            {
                String depart_time =  depart_hour + ":" + depart_min;
                String arrive_time =  arrive_hour + ":" + arrive_min;
                Ticket ticket = new Ticket(depart_time, arrive_time, Todo, ++id , "true");

                if (mParam1 != null)
                {
                    db.update_ticketDB(UID, ticket_Date, depart_time, arrive_time, Todo, updateId);
                    ((MainActivity) getActivity()).replaceFragment(TicketListFragment.newInstance(null, null));
                }
                else
                    db.insert_ticketDB(UID, ticket_Date, ticket);
                onDestroy();
            }
            else {
                Toast.makeText(getContext(), "출발 시간이 도착 시간 보다 빨라야 합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        AM = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);

//        String date_time= YEAR+"-"+(MONTH+1)+"-"+DAY+" "+ ticket_dpt+":"+00;
//        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


//        Date datetime = null;
//        try {
//            datetime = new Date(dateFormat.parse(date_time).getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println(datetime+"확인datetime");
        int dpth=dptH;
        int dptm=dptM;
        Calendar cal = Calendar.getInstance();
        cal.clear();
//        System.out.println(dptH+"확인 ticketdpt");
//        cal.set(Calendar.YEAR,YEAR);
//        cal.set(Calendar.MONTH,MONTH+1);
//        cal.set(Calendar.DAY_OF_MONTH,DAY);
//
//
//        cal.set(Calendar.HOUR_OF_DAY, dpth);
//        cal.set(Calendar.MINUTE, dptm);
//        cal.set(Calendar.SECOND, 0);

        cal.set(YEAR, MONTH - 1, DAY, dpth, dptm);
        System.out.println(cal.getTime() + "확인 cal에 셋된시간");


//        cal.setTime(datetime);
        //Receiver로 보내기 위한 인텐트
        Intent intent_alarm = new Intent(mContext, AlarmReceiver.class);

        ServicePending = PendingIntent.getBroadcast(
                mContext, 0, intent_alarm, PendingIntent.FLAG_ONE_SHOT);
        long calc_time=cal.getTimeInMillis();
        if (Build.VERSION.SDK_INT < 23) {
            // 19 이상
            if (Build.VERSION.SDK_INT >= 19) {
                AM.setExact(AlarmManager.RTC_WAKEUP,calc_time , ServicePending);
                System.out.println("확인 19이상");

            }
            // 19 미만
            else {
                // pass
                // 알람셋팅
                AM.set(AlarmManager.RTC_WAKEUP, calc_time, ServicePending);
                System.out.println("확인 19미만");

            }
            // 23 이상
        } else {
            AM.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calc_time, ServicePending);
            System.out.println("확인 23이상");
        }
        System.out.println("확인 알람설정 ok");

        super.onDestroy();
    }
}