package ch.globaz.vulpecula.business.services.congepaye;

import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;

public interface CongePayeService {
    CongePaye create(CongePaye congePaye) throws UnsatisfiedSpecificationException;

    /**
     * Regarde si on doit tenir compte des cotisations pour les congés payés.
     * 
     * @param Id du poste de travail sur lequel recherché la caisse métier qui déterminera si l'on doit tenir ou non en
     *            compte les cotisations.
     * @return true si le fichier de paramétrage spécifie que l'on doit prendre en compte les cotisations.
     */
    boolean tenirCompteDesCotisations(String idPosteTravail);

    /**
     * Charge le congé payé correspondant à l'id passé en paramètre
     * 
     * @param idCongePaye
     * @return le congé payé ou null
     */
    CongePaye findCongePayeById(String idCongePaye);
}
