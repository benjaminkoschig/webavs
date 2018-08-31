package ch.globaz.vulpecula.external.services;

import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.external.models.osiris.CompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;

public class CompteAnnexeServiceImpl implements CompteAnnexeService {

    @Override
    public CompteAnnexe findByNumAffilieAndIdTiersAndCategorie(String numAffilie, String idTiers, String categorie) {
        CompteAnnexe compteAnnexe = new CompteAnnexe();

        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setForIdExterneRole(numAffilie);
        manager.setForIdTiers(idTiers);
        manager.setForIdCategorie(categorie);

        try {
            manager.find();
            if (!manager.hasErrors()) {
                CACompteAnnexe ca = (CACompteAnnexe) manager.getFirstEntity();
                /*
                 * Si ca est null, c'est qu'il s'agit d'un cas de radiation. on return un compte annexe null.
                 */
                if (ca != null) {
                    compteAnnexe.setId(ca.getId());
                    compteAnnexe.setCategorie(ca.getIdCategorie());
                    compteAnnexe.setIdCompteAnnexe(ca.getIdCompteAnnexe());
                    compteAnnexe.setIdTiers(ca.getIdTiers());
                    compteAnnexe.setNumAffilie(ca.getIdExterneRole());
                    compteAnnexe.setSolde(new Montant(ca.getSolde()));
                    // TODO ...
                }
            }

            return compteAnnexe;
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

}
