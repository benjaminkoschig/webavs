package globaz.musca.process;

import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.musca.api.IFAPassage;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAAfactManager;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import ch.globaz.common.domaine.Date;

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
            FAEnteteFacture entFacture, Collection<?> sections, BSession session, BTransaction transaction)
            throws Exception {

        // par defaut on rembourse.
        entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_REMBOURSEMENT);

        Date dateFacturation = new Date(passage.getDateFacturation());
        String annee = dateFacturation.getAnnee();

        ArrayList<FAAfact> listAfacts = findAfacts(entFacture, transaction, session);

        if (isCAContentieux(compteAnnexe, annee)) {
            entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RETENU_COMPTE_ANNEX_BLOQUE);
        } else {
            // si il y a une section avec un solde positif on ne rembourse pas
            Iterator<?> it = sections.iterator();
            while (it.hasNext()) {
                CASection section = (CASection) it.next();
                if (Double.valueOf(section.getSolde()).doubleValue() > 0) {
                    if (!checkSiRemboursement(listAfacts, section)) {
                        entFacture.setIdModeRecouvrement(FAEnteteFacture.CS_MODE_RETENU_COMPTE_ANNEX_BLOQUE);
                        break;
                    }
                }
            }
        }
    }

    /**
     * @param listAfacts
     * @param section
     * @return Retourne true si le remboursement doit s'effectuer
     */
    private boolean checkSiRemboursement(ArrayList<FAAfact> listAfacts, CASection section) {
        BigDecimal montantAfact;
        BigDecimal soldeSection;
        for (FAAfact afact : listAfacts) {
            montantAfact = new BigDecimal(afact.getMontantFactureToCurrency().toString());
            soldeSection = new BigDecimal(section.getSolde());
            if (afact.getIdExterneFactureCompensation().equals(section.getIdExterne())
                    && montantAfact.compareTo(soldeSection) >= 0) {
                return true;
            }
        }
        // Si une autre section avec un numéro différent de celui des lignes de factures
        // Le remboursement ne doit pas être fait
        return false;
    }

    /**
     * @param entFacture Entête de la facture
     * @param transaction
     * @param session
     * @return ArrayList<FAAfact> : Retourne une liste de ligne de facture correspondant à l'entete-facture
     * @throws Exception
     */
    private ArrayList<FAAfact> findAfacts(FAEnteteFacture entFacture, BTransaction transaction, BSession session)
            throws Exception {
        ArrayList<FAAfact> listAfacts = new ArrayList<FAAfact>();
        FAAfactManager managerAfacts = new FAAfactManager();
        managerAfacts.setForIdEnteteFacture(entFacture.getId());
        managerAfacts.setForIdExterneDebiteurCompensation(entFacture.getIdExterneRole());
        managerAfacts.setForIdRoleDebCom(entFacture.getIdRole());
        managerAfacts.setForIdTypeAfact(FAAfact.CS_AFACT_COMPENSATION);
        managerAfacts.setSession(session);
        managerAfacts.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < managerAfacts.size(); i++) {
            listAfacts.add((FAAfact) managerAfacts.getEntity(i));
        }
        return listAfacts;
    }
}
