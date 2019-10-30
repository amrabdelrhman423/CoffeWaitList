package com.example.coffeewaitlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coffeewaitlist.Data.DataContract;
import com.example.coffeewaitlist.Data.DataHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WaitList extends AppCompatActivity {
    FloatingActionButton addorder;
    ImageView image_waitlist;
    RecyclerView GuestrecyclerView;
    DataHelper dataHelper;
    GuestAdapter guestAdapter;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_list);
        addorder = findViewById(R.id.bottom_float);
        image_waitlist=findViewById(R.id.image_waitlist);

        GuestrecyclerView =findViewById(R.id.recycler);
        GuestrecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        GuestrecyclerView.setHasFixedSize(true);
        GuestrecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));

        dataHelper =new DataHelper(getApplicationContext());
        sqLiteDatabase =dataHelper.getWritableDatabase();

        cursor=getAllGuest();

        guestAdapter =new GuestAdapter(getApplicationContext(),cursor);
        GuestrecyclerView.setAdapter(guestAdapter);
        if (cursor.getCount()!=0){
            image_waitlist.setVisibility(View.GONE);
        }else {
            image_waitlist.setVisibility(View.VISIBLE);
        }

        addorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//get the id of the item being swiped
                long id = (long) viewHolder.itemView.getTag();
                //remove from DB
                removeGuest(id);
                //update the list
                guestAdapter.swapCursor(getAllGuest());

                if (getAllGuest().getCount() == 0)
                {
                    image_waitlist.setVisibility(View.VISIBLE);
                }
            }

            //COMPLETED (11) attach the ItemTouchHelper to the waitlistRecyclerView
        }).attachToRecyclerView(GuestrecyclerView);
    }

    private long addNewGuest(String name, String partySize)
    {
        ContentValues cv = new ContentValues();
        cv.put(DataContract.WaitListEntry.COLUMN_GUEST_NAME, name);
        cv.put(DataContract.WaitListEntry.COLUMN_PARTY_SIZE, partySize);
        return sqLiteDatabase.insert(DataContract.WaitListEntry.TABLE_NAME, null, cv);
    }
    private boolean removeGuest(long id)
    {
        return sqLiteDatabase.delete(DataContract.WaitListEntry.TABLE_NAME, DataContract.WaitListEntry._ID + "=" + id, null) > 0;
    }

    private void showCustomDialog() {

        final Dialog dialog =new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.newguest_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        WindowManager.LayoutParams ip=new WindowManager.LayoutParams();
        ip.width=WindowManager.LayoutParams.MATCH_PARENT;
        ip.height=WindowManager.LayoutParams.WRAP_CONTENT;
        final Button add_guest = dialog.findViewById(R.id.add_guest_btn);
        final Button back= dialog.findViewById(R.id.back_btn);
        final EditText guest_name=dialog.findViewById(R.id.guest_name);
        final EditText guest_number =dialog.findViewById(R.id.guest_number);
        final View layout = getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout_id));
        TextView text =findViewById(R.id.text);
        LinearLayout lyt_card = findViewById(R.id.lyt_card);
        final Toast toast = new Toast(getApplicationContext());
        add_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String name = guest_name.getText().toString();
                String number = guest_number.getText().toString();

                if (name.length() == 0 || number.length() == 0)
                {
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
                else
                {
                    addNewGuest(name,number);
                    guestAdapter.swapCursor(getAllGuest());

                    GuestrecyclerView.setVisibility(View.GONE);

                    dialog.dismiss();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                toast.cancel();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(ip);

    }

    private Cursor getAllGuest() {
        Cursor cursor =sqLiteDatabase.query(
                DataContract.WaitListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DataContract.WaitListEntry.COLUMN_TIMESTAMP
        );
        return cursor;
    }
}