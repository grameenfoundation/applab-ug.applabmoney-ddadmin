package org.applab.ledgerlink.client.mob.admin;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 *
 *
 */
public class DigitizingDataApplication extends Application {

    // Storage paths
    public static final String DIGDATA_ROOT = Environment.getExternalStorageDirectory() + "/digdata";
    public static final String CACHE_PATH = DIGDATA_ROOT + "/.cache";
    public static final String METADATA_PATH = DIGDATA_ROOT + "/databases";

    private static DigitizingDataApplication singleton = null;

    public static DigitizingDataApplication getInstance() {
        return singleton;
    }

    /**
     * Creates required directories on the SDCard (or other external storage)
     *
     * @throws RuntimeException if there is no SDCard or the directory exists as a non directory
     */
    public static void createDigDataDirectories() throws RuntimeException {

        String cardStatus = Environment.getExternalStorageState();
        if (cardStatus.equals(Environment.MEDIA_REMOVED)
                || cardStatus.equals(Environment.MEDIA_UNMOUNTABLE)
                || cardStatus.equals(Environment.MEDIA_UNMOUNTED)
                || cardStatus.equals(Environment.MEDIA_MOUNTED_READ_ONLY)
                || cardStatus.equals(Environment.MEDIA_SHARED)) {
            RuntimeException e =
                    new RuntimeException("Digdata reports :: SDCard error: "
                            + Environment.getExternalStorageState());
            throw e;
        }

        String[] dirs = {
                DIGDATA_ROOT, CACHE_PATH, METADATA_PATH
        };

        for (String dirName : dirs) {
            File dir = new File(dirName);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    RuntimeException e =
                            new RuntimeException("Digdata reports :: Cannot create directory: " + dirName);
                    throw e;
                }
            } else {
                if (!dir.isDirectory()) {
                    RuntimeException e =
                            new RuntimeException("Digdata reports :: " + dirName
                                    + " exists, but is not a directory");
                    throw e;
                }
            }
        }
    }

    @Override
    public void onCreate() {
        singleton = this;
        super.onCreate();
    }

}
