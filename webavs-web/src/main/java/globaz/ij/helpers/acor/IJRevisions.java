package globaz.ij.helpers.acor;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author SCR
 * 
 */
public class IJRevisions {

    private List revisions = new ArrayList();

    public void addRevision(IJRevision rev) {

        revisions.add(rev);
    }

    public IJRevision[] getRevisions() {
        return (IJRevision[]) revisions.toArray(new IJRevision[revisions.size()]);
    }

}
