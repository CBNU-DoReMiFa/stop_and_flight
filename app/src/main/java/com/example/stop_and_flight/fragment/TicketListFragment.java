package com.example.stop_and_flight.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stop_and_flight.R;
import com.example.stop_and_flight.RecyclerItemTouchHelper;
import com.example.stop_and_flight.TicketAdapter;
import com.example.stop_and_flight.TicketDatabaseHandler;
import com.example.stop_and_flight.model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference mDatabase;
    private RecyclerView ticketRecyclerView;
    private TicketAdapter ticketAdapter;
    public ArrayList<Ticket> TicketList = new ArrayList<>();
    private Ticket getTicket;
    private String UID;
    private String ticket_Date;
    private TicketDatabaseHandler db;
    public String strCurYear;
    public String strCurMonth;
    public String strCurDay;
    public String strCurHour;
    public String strCurMinute;
    private int YEAR;
    private int MONTH;
    private int DAY;

    public TicketListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Ticket_list.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketListFragment newInstance(String param1, String param2) {
        TicketListFragment fragment = new TicketListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__ticket_list, container, false);
        Context ct = container.getContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        db = new TicketDatabaseHandler(mDatabase);
        ticketRecyclerView = view.findViewById(R.id.ticketRecyclerView);
        ticketAdapter = new TicketAdapter(db, ct, UID);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(ticketAdapter));
        itemTouchHelper.attachToRecyclerView(ticketRecyclerView);

        YEAR =  Integer.parseInt(strCurYear);
        MONTH =  Integer.parseInt(strCurMonth);
        DAY =  Integer.parseInt(strCurDay);
        ticket_Date = YEAR + "-" + MONTH + "-" + DAY;

        TextView countrytitle = (TextView) view.findViewById(R.id.CountryTitle);
        TextView Departtitle = (TextView) view.findViewById(R.id.DepartTitle);
        TextView arrivetitle = (TextView) view.findViewById(R.id.ArriveTitle);

        mDatabase.child("TICKET").child(UID).child(ticket_Date).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ticketRecyclerView.removeAllViewsInLayout();
                TicketList.clear();
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    if (fileSnapshot != null) {
                        getTicket = new Ticket();
                        getTicket = fileSnapshot.getValue(Ticket.class);
                        getTicket.setDate(ticket_Date);
                        TicketList.add(getTicket);
                    }
                }
                TicketList.sort(new Comparator<Ticket>() {
                    @Override
                    public int compare(Ticket o1, Ticket o2) {
                        String[] departTime1 = o1.getDepart_time().split(":");
                        String[] departTime2 = o2.getDepart_time().split(":");
                        if (Integer.parseInt(departTime1[0]) == Integer.parseInt(departTime2[0])
                                && Integer.parseInt(departTime1[1]) == Integer.parseInt(departTime2[1]))
                            return 0;
                        if (Integer.parseInt(departTime1[0]) >= Integer.parseInt(departTime2[0])
                                && Integer.parseInt(departTime1[1]) >= Integer.parseInt(departTime2[1]))
                            return 1;
                        if (Integer.parseInt(departTime1[0]) <= Integer.parseInt(departTime2[0])
                                && Integer.parseInt(departTime1[1]) <= Integer.parseInt(departTime2[1]))
                            return -1;
                        return 0;
                    }
                });
                ticketAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });

        ticketRecyclerView.setLayoutManager(new LinearLayoutManager(ct, LinearLayoutManager.VERTICAL, false));

        Collections.reverse(TicketList);
        ticketAdapter.setTicket(TicketList);
        ticketRecyclerView.setAdapter(ticketAdapter);

        return view;
    }
}