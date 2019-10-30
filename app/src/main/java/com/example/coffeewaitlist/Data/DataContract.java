package com.example.coffeewaitlist.Data;

import android.provider.BaseColumns;

public class DataContract
{
    public static final class WaitListEntry implements BaseColumns {
        public static final String TABLE_NAME="waitList";
        public static final String COLUMN_GUEST_NAME="guestName";
        public static final String COLUMN_PARTY_SIZE="partySize";
        public static final String COLUMN_TIMESTAMP="timeTamp";

    }
}
