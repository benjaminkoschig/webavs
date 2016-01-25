package globaz.aquila.process.batch.utils;

import globaz.aquila.db.access.batch.COTransition;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.print.list.COListParOP;
import globaz.aquila.print.list.COListParOPException;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;

public class COImprimerListPourOP {

    private COListParOP listePourOP;

    public COImprimerListPourOP(BProcess parent, String dateSurDocument, String dateReference, boolean previsionnel)
            throws Exception {
        listePourOP = new COListParOP();

        listePourOP.setParent(parent);
        listePourOP.setDate(dateSurDocument);
        listePourOP.setDateReference(dateReference);
        listePourOP.setModePrevisionnel(previsionnel);
    }

    public COListParOP getListePourOP() {
        return listePourOP;
    }

    /**
     * Ajoute une ligne pour une transition effectuée avec succès.
     * 
     * @param session
     * @param contentieux
     * @param transition
     * @param totalTaxeListe
     * @param totalFraisListe
     * @throws Exception
     */
    public void insertRowTransitionEffectue(BSession session, COContentieux contentieux, COTransition transition,
            FWCurrency totalTaxeListe, FWCurrency totalFraisListe) throws COListParOPException {
        listePourOP.insertRow(contentieux.getSection(), contentieux.getDateDeclenchement(), contentieux.getSection()
                .getSolde(), totalFraisListe.toString());
    }
}
