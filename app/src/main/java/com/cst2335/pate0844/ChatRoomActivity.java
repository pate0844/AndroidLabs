package com.cst2335.pate0844;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {
    ArrayList<Message> listItems;
    MyListAdapter adapter = new MyListAdapter();
    MessageDataB db;
    public static boolean IsPhone;
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        db=new MessageDataB(this);

        ListView myList = (ListView)findViewById(R.id.ListView1);
        Button sndButton = findViewById(R.id.SndBtn);
        Button rcvButton = findViewById(R.id.RcvBtn);
        EditText editText =(EditText)findViewById(R.id.Txt1);

        listItems =  new ArrayList<Message>();
        FrameLayout frameLayout = findViewById(R.id.frame1);

        if (frameLayout == null) {
            IsPhone = true;
            Log.i("Currunt", "Phone");
        }else{
            IsPhone = false;
            Log.i("Currunt", "Not Phone");
        }
        //  LoadDataFromDataBase();
        // adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        myList.setAdapter(adapter);
        loadMessage();
        myList.setOnItemLongClickListener((p, b, pos, id) -> {

            Message message=listItems.get(pos);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.Dialog)

                    //What is the message:
                    .setMessage(getString(R.string.selection)+pos+getString(R.string.database)+id)

                    .setPositiveButton("Yes", (click, arg) -> {
                        db.deleteMessage(listItems.get(pos));
                        loadMessage();
                    })
                    .setNegativeButton("No", (click, arg) -> { }).create().show();
            return true;
        });
        myList.setOnItemClickListener((parent, view, position, id)->{
            Message Msg = listItems.get(position);
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, listItems.get(position).getMsg() );
            System.out.println(listItems.get(position).toString());
            //dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, id);
            boolean sendSide;
            dataToPass.putBoolean(ITEM_POSITION, listItems.get(position).getSend());

            if(!IsPhone)
            {
                DetailsFragmentt dFragment = new DetailsFragmentt(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame1, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });

        sndButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Message message= new Message(((EditText) findViewById(R.id.Txt1)).getText().toString(), 1, true);
                db.addMessage(message);
                loadMessage();
                editText.getText().clear();
            }
        });
        rcvButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Message message= new Message(((EditText) findViewById(R.id.Txt1)).getText().toString(), 1, false);
                db.addMessage(message);
                loadMessage();
                editText.getText().clear();
            }
        });

    }

    public void loadMessage(){
        listItems=db.getAll();
        adapter.notifyDataSetChanged();
    }

    private class MyListAdapter extends BaseAdapter {

        public int getCount() { return listItems.size();}

        public Object getItem(int position) { return "This is row " + position; }

        public long getItemId(int position) { return listItems.get(position).id; }

        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();
            Message message=listItems.get(position);
            View newView=null;
            if(message.send){
                newView = inflater.inflate(R.layout.sender_message, parent, false);
                TextView tView = newView.findViewById(R.id.SndTxt);
                tView.setText(message.msg );
            }else{
                newView = inflater.inflate(R.layout.receiver_message, parent, false);
                TextView tView = newView.findViewById(R.id.RcvTxt);
                tView.setText(message.msg );
            }
            return newView;
        }
    }

}