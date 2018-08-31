package ch.globaz.vulpecula.external.services;

import ch.globaz.vulpecula.external.models.osiris.CompteAnnexe;

public interface CompteAnnexeService {

    /**
     * @param numAffilie numéro d'affilié / id externe rôle du compte annexe
     * @param idTiers
     * @param categorie famille de code système : VETYPEAFFI
     * @return un compte annexe en fonction des paramètres
     */
    CompteAnnexe findByNumAffilieAndIdTiersAndCategorie(String numAffilie, String idTiers, String categorie);
}
