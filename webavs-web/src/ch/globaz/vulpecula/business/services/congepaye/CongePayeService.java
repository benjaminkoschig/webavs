package ch.globaz.vulpecula.business.services.congepaye;

import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;

public interface CongePayeService {
    CongePaye create(CongePaye congePaye) throws UnsatisfiedSpecificationException;

    /**
     * Regarde si on doit tenir compte des cotisations pour les cong�s pay�s.
     * 
     * @param Id du poste de travail sur lequel recherch� la caisse m�tier qui d�terminera si l'on doit tenir ou non en
     *            compte les cotisations.
     * @return true si le fichier de param�trage sp�cifie que l'on doit prendre en compte les cotisations.
     */
    boolean tenirCompteDesCotisations(String idPosteTravail);

    /**
     * Charge le cong� pay� correspondant � l'id pass� en param�tre
     * 
     * @param idCongePaye
     * @return le cong� pay� ou null
     */
    CongePaye findCongePayeById(String idCongePaye);
}
