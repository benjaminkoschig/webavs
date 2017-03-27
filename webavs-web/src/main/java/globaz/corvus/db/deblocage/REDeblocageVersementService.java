/*
 * Globaz SA.
 */
package globaz.corvus.db.deblocage;

import globaz.globall.db.BSession;

public class REDeblocageVersementService {

    private BSession session;

    public REDeblocageVersementService(BSession session) {
        this.session = session;
    }

    public REDeblocageVersement searchByIdRenteAccordee(Long forIdRenteAccordee) {

        if (forIdRenteAccordee == null) {
            throw new IllegalArgumentException("To perform a search by id rente, idRentePrestation must be not null");
        }

        return new REDeblocageVersement();
    }

}
