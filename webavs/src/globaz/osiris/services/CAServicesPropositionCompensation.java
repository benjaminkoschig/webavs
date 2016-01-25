package globaz.osiris.services;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIPropositionCompensation;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author JTS
 */
public class CAServicesPropositionCompensation implements APIPropositionCompensation {

    private BSession session;

    public BSession getSession() {
        return session;
    }

    /**
     * Retourne une collection d'objet de type CASectionCompensee. La recherche est effectuée sur les sections des
     * comptes annexes du tiers passé en paramètre
     * 
     * @param idTiers
     *            id du tiers pour lequel il faut effectuer la compensation
     * @param montant
     *            globaz.framework.util.FWCurrency le montant à compenser signé
     * @param ordre
     *            java.lang.int l'ordre dans lequel les factures doivent être compensées
     * @return java.util.Collection une collection de Sections à compenser
     */
    @Override
    public Collection<APISection> propositionCompensation(int idTiers, FWCurrency montant, int ordre) throws Exception {

        // Si le montant vaut 0 ou est null
        if ((montant == null) || montant.isZero()) {
            throw new Exception(getSession().getLabel("5050"));
        }

        // liste pour le retour de la méthode
        List<APISection> listeSections = new ArrayList<APISection>();

        // compteurs pour les boucles
        int i = 0;

        // Récupération des comptes annexes pour le tiers passé en paramètre
        CACompteAnnexeManager managerCA = new CACompteAnnexeManager();
        managerCA.setSession(getSession());
        managerCA.setForIdTiers(String.valueOf(idTiers));
        managerCA.setForIdGenreCompte("0");
        managerCA.setOrderBy(CACompteAnnexeManager.ORDER_SOLDE);
        managerCA.find(BManager.SIZE_NOLIMIT);

        // boucle sur les comptes annexes tant qu'il reste des section ou un
        // montant à compenser
        while (!montant.equals(new FWCurrency(0)) && (i < managerCA.size())) {

            CACompteAnnexe compte = (CACompteAnnexe) managerCA.getEntity(i);

            Iterator<CASection> it = compte.propositionCompensation(APICompteAnnexe.PC_TYPE_MONTANT, ordre,
                    montant.toString()).iterator();

            /*
             * Traitement des sections retournées par la méthode propositionCompensation(...) de la classe
             * CACompteAnnexe Enregistrement des sections et de leur état dans un objet de type CASectionCompensee
             */
            while (it.hasNext()) {
                CASection section = it.next();

                listeSections.add(section);

                /*
                 * Calcul du montant restant après compensation du compte annexe en cours
                 */
                FWCurrency soldeSection = section.getSoldeToCurrency();
                if (soldeSection.signum() != montant.signum()) {
                    soldeSection.negate();
                    if ((soldeSection.isPositive() && (soldeSection.compareTo(montant) <= 0))
                            || (soldeSection.isNegative() && (soldeSection.compareTo(montant) >= 0))) {
                        montant.sub(soldeSection);
                    } else {
                        montant.sub(montant);
                    }
                }

            }

            i++;
        }

        return listeSections;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
