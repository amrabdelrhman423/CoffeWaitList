package com.example.coffeewaitlist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coffeewaitlist.Data.DataContract.WaitListEntry;
import com.example.coffeewaitlist.GuestAdapter.GuestViewHolder;

public class GuestAdapter extends RecyclerView.Adapter<GuestViewHolder> {

    Context context;
    Cursor cursor;

    public GuestAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestViewHolder holder, int position) {

        if (!cursor.moveToPosition(position)){
            return;
        }
        String name = cursor.getString(cursor.getColumnIndex(WaitListEntry.COLUMN_GUEST_NAME));
        String size =cursor.getString(cursor.getColumnIndex(WaitListEntry.COLUMN_PARTY_SIZE));
        long id =cursor.getLong(cursor.getColumnIndex(WaitListEntry._ID));
        holder.size.setText(size);
        holder.name.setText(name);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (cursor != null)
        {
            cursor.close();
        }

        cursor = newCursor;

        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public static final class GuestViewHolder extends RecyclerView.ViewHolder{
        TextView name,size;
        public GuestViewHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.name);
            size= itemView.findViewById(R.id.size);
        }
    }
}

