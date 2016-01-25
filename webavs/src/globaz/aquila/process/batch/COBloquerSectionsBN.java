/**
 *
 */
package globaz.aquila.process.batch;

import globaz.aquila.db.process.COSectionBulletinNeutreABloquerManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CASection;
import java.util.Iterator;

/**
 * @author SEL
 * 
 */
public class COBloquerSectionsBN {

    /**
     * @throws Exception
     * 
     */
    public static void bloquerSectionsBN(BTransaction transaction, String dateReference) throws Exception {

        Iterator<CASection> it = COBloquerSectionsBN.findSectionBNABloquer(transaction, dateReference);

        while (it.hasNext()) {
            CASection section = it.next();
            section.retrieve(transaction);
            section.setStatutBN(APISection.STATUTBN_DECOMPTE_FINAL);
            section.update(transaction);
        }
    }

    /**
     * @param dateReference
     * @return le premier janvier de l'année en paramètre.
     */
    private static String buildDateReference(String dateReference) {
        return "01.01." + JadeStringUtil.substring(dateReference, 6, 4);
    }

    /**
     * @param transaction
     * @param dateReference
     * @return
     * @throws Exception
     */
    private static Iterator<CASection> findSectionBNABloquer(BTransaction transaction, String dateReference)
            throws Exception {
        COSectionBulletinNeutreABloquerManager manager = new COSectionBulletinNeutreABloquerManager();
        manager.setSession(transaction.getSession());
        manager.setForSommation(true);
        manager.setDateReference(COBloquerSectionsBN.buildDateReference(dateReference));
        manager.find(transaction, BManager.SIZE_NOLIMIT);
        return manager.iterator();
    }

    /**
     * Non instanciable
     */
    private COBloquerSectionsBN() {
    }

}
