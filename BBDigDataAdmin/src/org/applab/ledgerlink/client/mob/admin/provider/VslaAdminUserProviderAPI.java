package org.applab.ledgerlink.client.mob.admin.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 */
public class VslaAdminUserProviderAPI {

    public static final String AUTHORITY = "org.applab.digdata.client.mob.admin.provider.VslaAdminUserProvider";

    private VslaAdminUserProviderAPI() {

    }

    public static final class VslaAdminUserColumns implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/vslaadminuser");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vslaadminusers";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vslaadminuser";
        // public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.org.applab.digdata.client.mob.admin.provider.vsla";
        // public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.org.applab.digdata.client.mob.admin.provider.vsla";

        public static final String USER_NAME = "USER_NAME";
        public static final String PASSWORD = "PASSWORD";
        public static final String LAST_LOGIN = "LAST_LOGIN";
        public static final String ACTIVATION_STATUS = "ACTIVATION_STATUS";
        public static final String IMEI = "IMEI";


        public static final String REGION_CODE = "REGION_ID";
        public static final String SECURITY_TOKEN = "SECURITY_TOKEN";
    }
}