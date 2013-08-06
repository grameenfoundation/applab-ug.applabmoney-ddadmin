package org.applab.digdata.client.mob.admin.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 */
public class VslaProviderAPI {

    public static final String AUTHORITY = "org.applab.digdata.client.mob.admin.provider.VslaProvider";

    private VslaProviderAPI() {

    }

    public static final class VslaColumns implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/vsla");
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vslas";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vsla";
       // public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.org.applab.digdata.client.mob.admin.provider.vsla";
        //public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.org.applab.digdata.client.mob.admin.provider.vsla";

        public static final String VSLA_NAME = "VSLA_NAME";
        public static final String VSLA_PASS_KEY = "VSLA_PASS_KEY";
        public static final String VSLA_ID = "VSLA_ID";
        public static final String _ID = "_ID";
    }
}