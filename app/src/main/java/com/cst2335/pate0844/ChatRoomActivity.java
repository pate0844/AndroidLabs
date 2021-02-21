package com.cst2335.pate0844;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {
    private ListView listView;
    private Button buttonSend;
    private Button buttonReceive;
    private EditText editText;

    class Message {
        private String message;
        private int sendOrReceive;

        public Message(String message, int sendOrReceive) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
        }

        public String getMessage() {
            return message;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }
    }

    class ChatAdapter extends ArrayAdapter {
        private List<Message> list;

        public ChatAdapter(Context context, int layout, List<Message> list) {
            super(context, layout, list);
            this.list = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.list.get(position).getSendOrReceive(), null);
            TextView message = (TextView) convertView.findViewById(R.id.textView);
            message.setText(this.list.get(position).getMessage());

//        return super.getView(position, convertView, parent);
            return convertView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        listView = (ListView) findViewById(R.id.listView);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonReceive = (Button) findViewById(R.id.buttonReceive);
        editText = (EditText) findViewById(R.id.editText);

        ArrayList<Message> arrayList = new ArrayList<>();
        ChatAdapter arrayAdapter = new ChatAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        buttonReceive.setOnClickListener(e -> {
            arrayList.add(new Message(editText.getText().toString(), R.layout.receive_layout));
            arrayAdapter.notifyDataSetChanged();
            editText.getText().clear();
        });


        buttonSend.setOnClickListener(e -> {
            arrayList.add(new Message(editText.getText().toString(), R.layout.send_layout));
            arrayAdapter.notifyDataSetChanged();
            editText.getText().clear();
        });

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setTitle(getString(R.string.delete_confirm_msg))
                    .setMessage(getString(R.string.the_selected_row_is) + " " + position + "\n" +
                            getString(R.string.the_database_id_is) + " " + id)
                    .setPositiveButton(R.string.yes, (DialogInterface dialog, int which) -> {
                        arrayList.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}