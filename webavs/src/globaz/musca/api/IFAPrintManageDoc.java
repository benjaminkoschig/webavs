package globaz.musca.api;

import globaz.globall.api.BIContainer;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public interface IFAPrintManageDoc extends BIContainer {
    public void cursorClose(BStatement statement) throws java.lang.Exception;

    public BStatement cursorOpen(BTransaction transaction) throws java.lang.Exception;

    public BEntity cursorReadNext(BStatement statement) throws java.lang.Exception;
}
