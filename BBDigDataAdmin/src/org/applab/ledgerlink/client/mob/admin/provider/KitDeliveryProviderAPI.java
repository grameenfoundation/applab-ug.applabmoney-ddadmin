package org.applab.ledgerlink.client.mob.admin.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 */
public class KitDeliveryProviderAPI {

    public static final String AUTHORITY = "org.applab.digdata.client.mob.admin.provider.KitDeliveryProvider";

    private KitDeliveryProviderAPI() {

    }

    public static final class KitDeliveryColumns implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/kitdelivery");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/kitdeliveries";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/kitdelivery";
        // public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.org.applab.digdata.client.mob.admin.provider.vsla";
        // public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.org.applab.digdata.client.mob.admin.provider.vsla";

        public static final String VSLA_CODE = "VSLA_CODE";
        public static final String ADMIN_USER_NAME = "ADMIN_USER_NAME";
        public static final String VSLA_PHONE_IMEI = "VSLA_PHONE_IMEI";
        public static final String VSLA_PHONE_MODEL = "VSLA_PHONE_MODEL";
        public static final String VSLA_PHONE_MANUFACTURER = "VSLA_PHONE_MANUFACTURER";
        public static final String RECEIVED_BY = "RECEIVED_BY";
        public static final String RECIPIENT_ROLE = "RECIPIENT_ROLE";
        public static final String DELIVERY_DATE = "DELIVERY_DATE";
        public static final String VSLA_PHONE_SERIAL_NUMBER = "VSLA_PHONE_SERIAL_NUMBER";
        public static final String VSLA_PHONE_MSISDN = "VSLA_PHONE_MSISDN";
        public static final String DELIVERY_STATUS = "DELIVERY_STATUS";
    }
}