package com.cst2335.pate0844;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragmentt#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DetailsFragmentt extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragmentt newInstance(String param1, String param2) {
        DetailsFragmentt fragment = new DetailsFragmentt();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailsFragmentt() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_details, container, false);


        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(ChatRoomActivity.ITEM_ID );

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.activity_details_fragmentt, container, false);

        //show the message
        TextView message = (TextView)result.findViewById(R.id.Messagelbl);
        message.setText(dataFromActivity.getString(ChatRoomActivity.ITEM_SELECTED));

        //show the id:
        TextView idView = (TextView)result.findViewById(R.id.IDlbl);
        idView.setText("ID=" + id);

        CheckBox checkBox = result.findViewById(R.id.IsSend);
        checkBox.setChecked(dataFromActivity.getBoolean(ChatRoomActivity.ITEM_POSITION));
        // get the delete button, and add a click listener:
        Button finishButton = result.findViewById(R.id.Hidebtn);
        finishButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();

            if (ChatRoomActivity.IsPhone) getActivity().onBackPressed();
        });

        return result;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}