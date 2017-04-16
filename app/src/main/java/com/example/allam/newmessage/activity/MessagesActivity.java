package com.example.allam.newmessage.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allam.newmessage.R;
import com.example.allam.newmessage.Utiles.Utiles;
import com.example.allam.newmessage.database.DatabaseQueries;
import com.example.allam.newmessage.model.Message;

import java.util.List;

public class MessagesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MessagesAdapter mMessagesAdapter;
    private List<Message> mMessagesList;
    private DatabaseQueries mDatabase;
    private ImageButton mRefreshButton, mDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Utiles.getUserName(getApplicationContext()));
        mRefreshButton = (ImageButton) findViewById(R.id.message_refresh);
        mDeleteButton = (ImageButton) findViewById(R.id.message_delete);

        mDatabase = new DatabaseQueries(getBaseContext());
        mMessagesList = mDatabase.getAllMessagesOfNumber(Utiles.getPhoneNumber(getApplicationContext()));

        mRecyclerView = (RecyclerView) findViewById(R.id.messages_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        mMessagesAdapter = new MessagesAdapter();
        mMessagesAdapter.setList(mMessagesList);
        mRecyclerView.setAdapter(mMessagesAdapter);

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessagesList = mDatabase.getAllMessagesOfNumber(Utiles.getPhoneNumber(getApplicationContext()));
                mMessagesAdapter.setList(mMessagesList);
                mMessagesAdapter.notifyDataSetChanged();
                Toast.makeText(MessagesActivity.this, "Refreshed!", Toast.LENGTH_SHORT).show();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessagesActivity.this, "press long on the message to delete it.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private class MessagesHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private TextView mBodyTextView, mDateTextView;
        private ImageButton mPopUpDeleteButton;

        public MessagesHolder(View view) {
            super(view);
            mBodyTextView = (TextView) view.findViewById(R.id.message_body);
            mDateTextView = (TextView) view.findViewById(R.id.message_date);
            mPopUpDeleteButton = (ImageButton) view.findViewById(R.id.popup_delete);
            mBodyTextView.setOnLongClickListener(this);
            mPopUpDeleteButton.setOnClickListener(this);

        }

        public void onBindViewHolder(String message, String date) {
            mBodyTextView.setText(message);
            mDateTextView.setText(date);
        }

        @Override
        public boolean onLongClick(View v) {
            mPopUpDeleteButton.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public void onClick(View v) {
            String message = mMessagesList.get(getLayoutPosition()).getBody();
            boolean deleted = new DatabaseQueries(getBaseContext()).deleteMessage(message);
            if (deleted) {
                Toast.makeText(MessagesActivity.this, "Message deleted", Toast.LENGTH_SHORT).show();
                mMessagesList.remove(getLayoutPosition());
                mMessagesAdapter.notifyDataSetChanged();
            } else
                Toast.makeText(MessagesActivity.this, "Message not deleted, something wrong!", Toast.LENGTH_SHORT).show();
            mPopUpDeleteButton.setVisibility(View.GONE);
        }
    }

    private class MessagesAdapter extends RecyclerView.Adapter<MessagesHolder> {
        private List<Message> mList;

        public void setList(List<Message> list) {
            mList = list;
        }

        @Override
        public MessagesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getBaseContext());
            View view = inflater.inflate(R.layout.recyclerview_items, parent, false);
            return new MessagesHolder(view);
        }

        @Override
        public void onBindViewHolder(MessagesHolder holder, int position) {
            Message message = mList.get(position);
            holder.onBindViewHolder(message.getBody(), message.getDate());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                boolean isOn = Utiles.IsNotificationOn(getApplicationContext());
                if (isOn) {
                    item.setTitle(R.string.turn_notification_on);
                    Utiles.setNotificationOn(getApplicationContext(), false);
                } else {
                    item.setTitle(R.string.turn_notification_off);
                    Utiles.setNotificationOn(getApplicationContext(), true);
                }
                return true;

            case R.id.change_number:
                Intent intent = new Intent(MessagesActivity.this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
