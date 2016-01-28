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
     * Retourne une collection d'objet de type CASectionCompensee. La recherche est effectu�e sur les sections des
     * comptes annexes du tiers pass� en param�tre
     * 
     * @param idTiers
     *            id du tiers pour lequel il faut effectuer la compensation
     * @param montant
     *            globaz.framework.util.FWCurrency le montant � compenser sign�
     * @param ordre
     *            java.lang.int l'ordre dans lequel les factures doivent �tre compens�es
     * @return java.util.Collection une collection de Sections � compenser
     */
    @Override
    public Collection<APISection> propositionCompensation(int idTiers, FWCurrency montant, int ordre) throws Exception {

        // Si le montant vaut 0 ou est null
        if ((montant == null) || montant.isZero()) {
            throw new Exception(getSession().getLabel("5050"));
        }

        // liste pour le retour de la m�thode
        List<APISection> listeSections = new ArrayList<APISection>();

        // compteurs pour les boucles
        int i = 0;

        // R�cup�ration des comptes annexes pour le tiers pass� en param�tre
        CACompteAnnexeManager managerCA = new CACompteAnnexeManager();
        managerCA.setSession(getSession());
        managerCA.setForIdTiers(String.valueOf(idTiers));
        managerCA.setForIdGenreCompte("0");
        managerCA.setOrderBy(CACompteAnnexeManager.ORDER_SOLDE);
        managerCA.find(BManager.SIZE_NOLIMIT);

        // boucle sur les comptes annexes tant qu'il reste des section ou un
        // montant � compenser
        while (!montant.equals(new FWCurrency(0)) && (i < managerCA.size())) {

            CACompteAnnexe compte = (CACompteAnnexe) managerCA.getEntity(i);

            Iterator<CASection> it = compte.propositionCompensation(APICompteAnnexe.PC_TYPE_MONTANT, ordre,
                    montant.toString()).iterator();

            /*
             * Traitement des sections retourn�es par la m�thode propositionCompensation(...) de la classe
             * CACompteAnnexe Enregistrement des sections et de leur �tat dans un objet de type CASectionCompensee
             */
            while (it.hasNext()) {
                CASection section = it.next();

                listeSections.add(section);

                /*
                 * Calcul du montant restant apr�s compensation du compte annexe en cours
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
