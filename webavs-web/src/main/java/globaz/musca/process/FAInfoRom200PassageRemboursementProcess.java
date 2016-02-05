package globaz.musca.process;

import globaz.globall.db.BSession;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author: MMO 11.10.2010
 */
public class FAInfoRom200PassageRemboursementProcess extends FAPassageRemboursementProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void fixIdForModeRecouvrement(CACompteAnnexe compteAnnexe, IFAPassage passage,
            FAEnteteFacture entFacture, Collection<?> sections, BSession session) throws Exception {

        // par defaut on rembourse.
        entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_REMBOURSEMENT);

        String annee = passage.getDateFacturation().substring(3);

        if (isCAContentieux(compteAnnexe, annee)) {
            entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RETENU_COMPTE_ANNEX_BLOQUE);
        } else {
            // si il y a une section avec un solde positif on ne rembourse pas
            Iterator<?> it = sections.iterator();
            while (it.hasNext()) {
                CASection section = (CASection) it.next();
                if (Double.valueOf(section.getSolde()).doubleValue() > 0) {
                    entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RETENU_COMPTE_ANNEX_BLOQUE);
                    break;
                }
            }
        }
    }
}
