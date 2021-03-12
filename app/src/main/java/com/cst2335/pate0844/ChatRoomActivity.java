package com.cst2335.pate0844;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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

    //Constants
    private int SEND = 1;
    private int RECEIVE = 0;

    //
    private ListView listView;
    private Button buttonSend;
    private Button buttonReceive;
    private EditText editText;
    MessageStorage messageStorage;
    ArrayList<Message> arrayList = new ArrayList<>();
    ChatAdapter arrayAdapter = new ChatAdapter();





    class Message {
        private String message;
        private int sendOrReceive;
        private  long messageID;

        public Message(String message, int sendOrReceive) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;

        }
        public Message(Long messageID, Message message) {
            this.messageID = messageID;
            this.message = message.getMessage();
            this.sendOrReceive = message.getSendOrReceive();
        }

        public Message(String message, int sendOrReceive, long messageID) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.messageID = messageID;
        }

        public String getMessage() {
            return message;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }
        public long getMessageID(){
            return  messageID;
        }
    }



    class ChatAdapter extends BaseAdapter {
        private List<Message> list;


        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            View thisView = view;
            LayoutInflater inflater = getLayoutInflater();

            if (arrayList.get(position).getSendOrReceive() == RECEIVE) {
                thisView = inflater.inflate(R.layout.receive_layout, parent, false);
            } else if (arrayList.get(position).getSendOrReceive() == SEND) {
                thisView = inflater.inflate(R.layout.send_layout, parent, false);
            }

            TextView messageText = thisView.findViewById(R.id.textView);
            messageText.setText(arrayList.get(position).getMessage());

            return thisView;
        }
    }



    public class MessageDBHelper  extends SQLiteOpenHelper {


        public static final String TABLE_NAME = "MESSAGES";
        public static final String COL_TEXT = "TEXT";
        public static final String COL_TYPE = "TYPE";
        public static final String COL_ID = "_id";
        protected static final String DATABASE_NAME = "AndroidLabs";
        protected static final int VERSION_NUM = 1;

        /*
         * @Constructer
         * @purpose -> create instance for database
         * */
        public MessageDBHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION_NUM);
        }

        @Override
        /*
         *@method -> OnCreate
         * @param -> db
         * @purpose  -> creates table with fields ID->id of message in ListView, TYPE-> If msg Send or receive ,
         *              TEXT -> actual message Text
         * */
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME
                    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_TYPE+ " boolean, "
                    + COL_TEXT + " text);");
        }

        /*
         * @method -> onUpgrade
         * @param -> db- SQLiteDatabse Object,
         *           oldVersion - Current version of table
         *           new Version - Version of the table to be Upgraded
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);

        }
        /*
         * @method -> onDowngrade
         * @param -> db- SQLiteDatabse Object,
         *           oldVersion - Current version of table
         *           new Version - Version of the table to be Downgraded
         */
        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
    /*
     *
     *
     *
     *
     * */
    public class MessageStorage {

        private final MessageDBHelper helper;
        private final Context context;
        private SQLiteDatabase db;

        public MessageStorage(Context context) {
            this.helper = new MessageDBHelper(context);
            this.context = context;
        }

        public Message save(Message message) {
            db = helper.getWritableDatabase();
            ContentValues row = new ContentValues();
            row.put(MessageDBHelper.COL_TEXT, message.getMessage());
            // Using the ordinal to convert the Type enum to 0 or 1.
            row.put(MessageDBHelper.COL_TYPE, message.getSendOrReceive());

            long id = db.insert(MessageDBHelper.TABLE_NAME, null, row);
            db.close();
            return new Message(id, message);
        }

        public List<Message> findAll() {
            db = helper.getReadableDatabase();
            String[] columns = {
                    MessageDBHelper.COL_ID,
                    MessageDBHelper.COL_TYPE,
                    MessageDBHelper.COL_TEXT
            };
            Cursor results = db.query(
                    false,
                    MessageDBHelper.TABLE_NAME,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            Cursor copy = db.query(
                    false,
                    MessageDBHelper.TABLE_NAME,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            printCursor(copy, db.getVersion());
            List<Message> messages = new ArrayList<>();
            if (results.getCount() > 0) {
                while (results.moveToNext()) {
                    Message message = new Message(
                            // Text
                            results.getString(results.getColumnIndex(MessageDBHelper.COL_TEXT)),

                        /* Send or received.
                        1 for send and 0 for recieve
                        */

                            results.getInt(results.getColumnIndex(MessageDBHelper.COL_TYPE)) > 0
                                    ? RECEIVE : SEND,
                            // ID
                            results.getLong(results.getColumnIndex(MessageDBHelper.COL_ID))
                    );
                    messages.add(message);
                }
            }
            // Avoid memory leak
            results.close();
            copy.close();
            db.close();
            return messages;
        }

        public void delete(Message message) {
            db = helper.getWritableDatabase();
            Long msgID =  message.getMessageID();
            String ID = Long.toString(msgID);
            db.delete(MessageDBHelper.TABLE_NAME, "_id=?", new String[]{ID});
            db.close();
        }

        private void printCursor(Cursor c, int version) {
            String activity = ((Activity) context).getComponentName().flattenToString();
            StringBuilder columnNames = new StringBuilder();
            Log.i(activity, "Database Version: " + version);
            Log.i(activity, "Number of Columns: " + c.getColumnCount());
            for (int i = 0; i < c.getColumnCount(); i++) {
                columnNames.append(c.getColumnName(i));
                if (i != c.getColumnCount()) {
                    columnNames.append(", ");
                }
            }
            Log.i(activity, "Columns: " + TextUtils.join(",", c.getColumnNames()));
            Log.i(activity, "Number of Results: " + c.getCount());
            Log.i(activity, "Results:\n");
            while (c.moveToNext()) {
                StringBuilder resultRow = new StringBuilder();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    resultRow
                            .append(c.getColumnName(i))
                            .append(": ")
                            .append(c.getString(i))
                            .append("; ");
                }
                Log.i(activity, resultRow.toString());
            }

        }
    }
    /*
     *
     *
     *
     *
     *
     *
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        listView = (ListView) findViewById(R.id.listView);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonReceive = (Button) findViewById(R.id.buttonReceive);
        editText = (EditText) findViewById(R.id.editText);
        messageStorage = new MessageStorage(this);
        load();


        listView.setAdapter(arrayAdapter);

        buttonReceive.setOnClickListener(e -> {
            addMessage(editText.getText().toString(), 0);
            editText.setText(null); //resetting the text to blank

        });


        buttonSend.setOnClickListener(e -> {
            addMessage(editText.getText().toString(), 1);
            editText.setText(null); //resetting the text to blank
        });

        //shows alert dialogues when a row is clicked on for longer
        listView.setOnItemLongClickListener(((parent, view, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.
                    setTitle(getString(R.string.delete_confirm_msg))
                    .setMessage(
                            getString(R.string.the_selected_row_is) + position + "\n" +
                                    getString(R.string.the_database_id_is) + arrayAdapter.getItemId(position)
                    )
                    .setPositiveButton(getString(R.string.yes), (click, arg) -> {
                        messageStorage.delete((Message) arrayAdapter.getItem(position));
                        arrayList.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.no, (click, arg) -> {
                    })
                    .show();

            return false;
        }));
    }
    //load data from the database and notify the adapter
    private void load(){
        arrayList.addAll(messageStorage.findAll());
        arrayAdapter.notifyDataSetChanged();
    }

    //add a new message to the messages list then notify adapter
    private void addMessage(String  text, int isSend){
        Message message = new Message(text, isSend);
        arrayList.add(messageStorage.save(message));
        arrayAdapter.notifyDataSetChanged();
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