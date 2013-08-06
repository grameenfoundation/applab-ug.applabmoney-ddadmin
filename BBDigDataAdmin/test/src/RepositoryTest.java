import android.database.Cursor;
import android.test.AndroidTestCase;
import org.applab.digdata.client.mob.admin.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: Sarahk
 * Date: 7/4/13
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class RepositoryTest extends AndroidTestCase {

    public void testGetVslaCount() {
        Repository repo = new Repository(getContext());

        int testCursor = repo.getVslaCount();
        assertEquals(0, testCursor);
    }
}