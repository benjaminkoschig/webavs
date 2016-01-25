package globaz.osiris.db.irrecouvrable;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.osiris.db.comptes.CACompteurCalculBaseAmortissement;
import globaz.osiris.db.comptes.CACompteurCalculBaseAmortissementManager;
import java.math.BigDecimal;
import java.util.List;

/**
 * Classe permettant le remplissage d'un {@link CARecouvrementBaseAmortissementContainer}
 * 
 * @author jts
 */
public class CARecouvrementBaseAmortissementContainerLoader {

    private static CARecouvrementBaseAmortissementContainer getFilledContainer(
            CACompteurCalculBaseAmortissementManager compteursManager) {

        CARecouvrementBaseAmortissementContainer basesAmortissementContainer = new CARecouvrementBaseAmortissementContainer();

        for (int i = 0; i < compteursManager.size(); i++) {
            CACompteurCalculBaseAmortissement compteur = (CACompteurCalculBaseAmortissement) compteursManager.get(i);

            Integer annee = Integer.parseInt(compteur.getAnneeAmortissement());
            BigDecimal cumulCotisations = new BigDecimal(compteur.getCumulCotisationsForAnnee());

            basesAmortissementContainer.addRecouvrementBaseAmortissement(annee, cumulCotisations);
        }

        return basesAmortissementContainer;
    }

    /**
     * Charge les compteurs des bases d'amortissement liées au compte annexe fourni
     * 
     * @param idCompteAnnexe
     *            id du compte annexe pour lequels les compteurs doivent être chargés
     * @param session
     * 
     * @return Bases d'amortissement
     * @throws Exception
     */
    public static CARecouvrementBaseAmortissementContainer loadCompteurs(BISession session, String idCompteAnnexe)
            throws Exception {

        CACompteurCalculBaseAmortissementManager compteursManager = new CACompteurCalculBaseAmortissementManager();
        compteursManager.setSession((BSession) session);
        compteursManager.setForIdCompteAnnexe(idCompteAnnexe);
        compteursManager.find();

        CARecouvrementBaseAmortissementContainer basesAmortissementContainer = CARecouvrementBaseAmortissementContainerLoader
                .getFilledContainer(compteursManager);

        return basesAmortissementContainer;
    }

    /**
     * Charge les compteurs des bases d'amortissement liées au compte annexe fourni
     * 
     * @param idCompteAnnexe
     *            id du compte annexe pour lequels les compteurs doivent être chargés
     * @param session
     * 
     * @return Bases d'amortissement
     * @throws Exception
     */
    public static CARecouvrementBaseAmortissementContainer loadCompteursForAnnees(BISession session,
            String idCompteAnnexe, List<String> annees) throws Exception {

        CACompteurCalculBaseAmortissementManager compteursManager = new CACompteurCalculBaseAmortissementManager();
        compteursManager.setSession((BSession) session);
        compteursManager.setForIdCompteAnnexe(idCompteAnnexe);
        compteursManager.setForInAnnees(annees);
        compteursManager.find();

        CARecouvrementBaseAmortissementContainer basesAmortissementContainer = CARecouvrementBaseAmortissementContainerLoader
                .getFilledContainer(compteursManager);

        return basesAmortissementContainer;
    }
}
