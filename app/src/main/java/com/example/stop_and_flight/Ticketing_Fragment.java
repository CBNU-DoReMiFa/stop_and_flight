package com.example.stop_and_flight;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webianks.library.scroll_choice.ScrollChoice;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ticketing_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ticketing_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<String> Todo = new ArrayList<>();
    TextView textView;
    ScrollChoice scrollChoice;


    public Ticketing_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ticketing_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Ticketing_Fragment newInstance(String param1, String param2) {
        Ticketing_Fragment fragment = new Ticketing_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        initVeiws();
        loadTodo();

        scrollChoice.addItems(Todo, 3);
        scrollChoice.setOnItemSelectedListener(new ScrollChoice.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ScrollChoice scrollChoice, int position, String name) {
                textView.setText("choice" + name);
            }
        });
    }

    private void loadTodo() {
        Todo.add("Todo1");
        Todo.add("Todo2");
        Todo.add("Todo3");
        Todo.add("Todo4");
        Todo.add("Todo5");
    }

    private void initVeiws() {
        textView = (TextView) getView().findViewById(R.id.choice_result);
        scrollChoice = (ScrollChoice) getView().findViewById(R.id.scoll_chice);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticketing, container, false);
    }
}