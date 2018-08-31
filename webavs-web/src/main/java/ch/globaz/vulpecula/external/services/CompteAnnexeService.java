package ch.globaz.vulpecula.external.services;

import ch.globaz.vulpecula.external.models.osiris.CompteAnnexe;

public interface CompteAnnexeService {

    /**
     * @param numAffilie num�ro d'affili� / id externe r�le du compte annexe
     * @param idTiers
     * @param categorie famille de code syst�me : VETYPEAFFI
     * @return un compte annexe en fonction des param�tres
     */
    CompteAnnexe findByNumAffilieAndIdTiersAndCategorie(String numAffilie, String idTiers, String categorie);
}
