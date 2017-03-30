/*
 * Globaz SA.
 */
package globaz.corvus.db.deblocage;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import java.util.List;

public class REDeblocageVersementService {

    private BSession session;

    public REDeblocageVersementService(BSession session) {
        this.session = session;
    }

    public List<REDeblocageVersement> searchByIdRenteAccordee(Long forIdRenteAccordee) {

        if (forIdRenteAccordee == null) {
            throw new IllegalArgumentException(
                    "To perform a search by id rente accordée, forIdRenteAccordee must be not null");
        }

        REDeblocageVersementManager manager = new REDeblocageVersementManager();
        manager.setSession(session);
        manager.setForIdRenteAccordee(forIdRenteAccordee.toString());
        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new REDeblocageException("Unabled to search deblocage versement for id rente accordee : "
                    + forIdRenteAccordee, e);
        }

        return manager.toList();
    }

    public List<REDeblocageVersement> searchByIdLot(Long forIdLot) {

        if (forIdLot == null) {
            throw new IllegalArgumentException("To perform a search by id lot, forIdLot must be not null");
        }

        REDeblocageVersementManager manager = new REDeblocageVersementManager();
        manager.setSession(session);
        manager.setForIdLot(forIdLot.toString());
        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new REDeblocageException("Unabled to search deblocage versement for id lot: " + forIdLot, e);
        }

        return manager.toList();
    }

}
