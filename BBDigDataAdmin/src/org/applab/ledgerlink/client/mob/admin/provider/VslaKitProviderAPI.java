package org.applab.ledgerlink.client.mob.admin.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 *
 */
public class VslaKitProviderAPI {

    public static final String AUTHORITY = "applab.client.digdata.provider.digdata.vslakit";

    private VslaKitProviderAPI() {

    }

    public static final class VslaKitColumns implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/vslakit");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.digdata.vslakit";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.digdata.vslakit";

        public static final String PHONE_NUMBER = "PHONE_NUMBER";
        public static final String PHONE_IMEI = "PHONE_IMEI";
        public static final String PHONE_MODEL = "PHONE_MODEL";
        public static final String PHONE_MANUFACTURER = "PHONE_MANUFACTURER";
        public static final String CHARGE_SET_SERIAL_NUMBER = "CHARGE_SET_SERIAL_NUMBER";
        public static final String CHARGE_SET_MANUFACTURER = "CHARGE_SET_MANUFACTURER";
        public static final String RECEIVED_BY = "RECEIVED_BY";
        public static final String LAST_MODIFIED_DATE = "LAST_MODIFIED_DATE";
    }
}
