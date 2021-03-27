package com.example.stop_and_flight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_flight1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_flight1 extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // Required empty public constructor

    public Fragment_flight1() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_flight1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_flight1 newInstance(String param1, String param2) {
        Fragment_flight1 fragment = new Fragment_flight1();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flight1, container, false);

        Button emergencybutton = v.findViewById(R.id.button_emergency);
        Button appaccessbutton = v.findViewById(R.id.button_app);

        List<PackageInfo> packlist = new List<PackageInfo>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<PackageInfo> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(PackageInfo packageInfo) {
                return false;
            }

            @Override
            public boolean remove(@Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends PackageInfo> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends PackageInfo> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public PackageInfo get(int index) {
                return null;
            }

            @Override
            public PackageInfo set(int index, PackageInfo element) {
                return null;
            }

            @Override
            public void add(int index, PackageInfo element) {

            }

            @Override
            public PackageInfo remove(int index) {
                return null;
            }

            @Override
            public int indexOf(@Nullable Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(@Nullable Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<PackageInfo> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<PackageInfo> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<PackageInfo> subList(int fromIndex, int toIndex) {
                return null;
            }
        };

        emergencybutton.setOnClickListener(this);
        appaccessbutton.setOnClickListener(this);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_emergency:
                ShowEmergencyMessage(v);
                break;
            case R.id.button_app:
                ShowAccessApplist(v);
                break;
        }
    }

    private void ShowAccessApplist(View v) {

        ArrayList<String> applist = new ArrayList<>();

        installedApplist(applist);

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.app_dialog_searchable_spinner);
        dialog.getWindow().setLayout(800, 800);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText editText = dialog.findViewById(R.id.app_edit_text);
        ListView listView = dialog.findViewById(R.id.app_list_view);
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, applist);
        listView.setAdapter(adapter);
        dialog.show();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(adapter.getItem(position).toString());
                startActivity(launchIntent);
                dialog.dismiss();
            }
        });
    }

    private void ShowEmergencyMessage(View v) {
        AlertDialog.Builder embuilder = new AlertDialog.Builder(getActivity());
        embuilder.setTitle("손님!! 비상 탈출 하시겠습니까?");
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View view = factory.inflate(R.layout.emergency_dialog, null);
        embuilder.setView(view);
        embuilder.setPositiveButton("살고싶어요..", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "끔", Toast.LENGTH_SHORT).show();
            }
        });
        embuilder.setNegativeButton("조금 더 해볼래요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "안 끔", Toast.LENGTH_SHORT).show();
            }
        });
        embuilder.show();
    }




    private void installedApplist(List<String> applist) {
        List<PackageInfo> packList = getActivity().getPackageManager().getInstalledPackages(0);
        PackageInfo packInfo = null;
        for (int i=0; i < packList.size(); i++)
        {
            packInfo = packList.get(i);
            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                applist.add(packInfo.packageName);
            }
        }
    }
}