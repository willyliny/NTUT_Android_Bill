package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    protected Button btn_add;
    protected ListView listView;
    private ArrayList<String> item = new ArrayList<>();
    protected ArrayAdapter<String> adapter_list;
    private SQLiteDatabase dbrw;
    private Cursor cursor;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Hello");
        getActivity().registerReceiver(new ListFragment.MyBroadcastReceiver() , intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list, container, false);
        final String[] confirmArray = {"修改","刪除","取消"};
        listView=view.findViewById(R.id.listView);
        adapter_list=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,item);
        listView.setAdapter(adapter_list);
        dbrw=new DataBase(getActivity()).getWritableDatabase();
        cursor=dbrw.rawQuery("select * from charge",null);
        cursor.moveToFirst();
        item.clear();
        Toast.makeText(getActivity(),"共有"+cursor.getCount()+"筆資料",Toast.LENGTH_SHORT).show();
        for (int i=0;i<cursor.getCount();i++)
        {
            //
            item.add(cursor.getString(4)+"/"+cursor.getString(5)+"\t類別:"+cursor.getString(1)+" \t支付方式:"+cursor.getString(2)+"\n名稱:"+cursor.getString(0)+
                    " \n金額:"+cursor.getString(3));
            cursor.moveToNext();
        }
        adapter_list.notifyDataSetChanged();
        cursor.close();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder confirm = new AlertDialog.Builder(getActivity());
                confirm.setTitle("消費紀錄");
                confirm.setItems(confirmArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0:
                                cursor=dbrw.rawQuery("select * from charge",null);
                                cursor.moveToPosition(position);
                                Bundle data_all =new Bundle();
                                data_all.putString("name",cursor.getString( 0));
                                data_all.putString("type",cursor.getString(1));
                                data_all.putString("pay",cursor.getString(2));
                                data_all.putInt("price",cursor.getInt(3));
                                data_all.putInt("months",cursor.getInt(4));
                                data_all.putInt("days",cursor.getInt(5));
                                Intent i=new Intent(getActivity(),AddActivity.class);
                                i.putExtras(data_all);
                                dbrw.execSQL("delete from charge where name like '"+cursor.getString(0)+"'");
                                cursor.close();
                                startActivityForResult(i,1);
                                break;
                            case 1:
                                cursor=dbrw.rawQuery("select * from charge",null);
                                cursor.moveToPosition(position);
                                dbrw.execSQL("delete from charge where name like '"+cursor.getString(0)+"'");
                                cursor.close();
                                break;
                        }
                        cursor=dbrw.rawQuery("select * from charge",null);
                        cursor.moveToFirst();
                        item.clear();
                        Toast.makeText(getActivity(),"共有"+cursor.getCount()+"筆資料",Toast.LENGTH_SHORT).show();
                        for (int j=0;j<cursor.getCount();j++)
                        {
                            item.add(cursor.getString(4)+"/"+cursor.getString(5)+"\t類別:"+cursor.getString(1)+" \t支付方式:"+cursor.getString(2)+"\n名稱:"+cursor.getString(0)+
                                    " \n金額:"+cursor.getString(3));
                            cursor.moveToNext();
                        }
                        adapter_list.notifyDataSetChanged();
                        cursor.close();
                    }
                });
                AlertDialog dialog=confirm.create();
                dialog.show();
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        dbrw.close();
    }
    public  class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("Hello")){
                Cursor c;
                c=dbrw.rawQuery("select * from charge",null);
                c.moveToFirst();
                item.clear();
                Toast.makeText(getActivity(),"共有"+c.getCount()+"筆資料",Toast.LENGTH_SHORT).show();
                for (int i=0;i<c.getCount();i++)
                {
                    item.add(c.getInt(4)+"/"+c.getInt(5)+"\t類別:"+c.getString(1)+" \t支付方式:"+c.getString(2)+"\n名稱:"+c.getString(0)+
                            " \n金額:"+c.getInt(3));
                    c.moveToNext();
                }
                adapter_list.notifyDataSetChanged();
                c.close();
            }
        }
    }
}
