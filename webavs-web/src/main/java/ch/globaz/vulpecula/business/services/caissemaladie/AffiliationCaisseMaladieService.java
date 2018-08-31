package ch.globaz.vulpecula.business.services.caissemaladie;

import globaz.globall.db.BSession;
import globaz.vulpecula.business.exception.SaisiePeriodeException;
import java.util.Collection;
import java.util.Map;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.caissemaladie.AffiliationCaisseMaladie;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public interface AffiliationCaisseMaladieService {
    void create(AffiliationCaisseMaladie affiliationCaisseMaladie) throws UnsatisfiedSpecificationException;

    void update(AffiliationCaisseMaladie affiliationCaisseMaladie) throws UnsatisfiedSpecificationException;

    void delete(AffiliationCaisseMaladie affiliationCaisseMaladie) throws UnsatisfiedSpecificationException;

    /**
     * M�thode qui permet de cr�er une entr�e dans les caisses maladies lors de l'ajout d'un nouveau poste de travail
     * 
     * @param dateDebut
     * @param dateFin
     * @param idTravailleur
     * @param idTiersAdministration
     */
    void createForPosteTravail(PosteTravail poste);

    /**
     * Suppression du lien sur la caisse maladie lors de la suppression du poste de travail
     * 
     * @param poste
     */
    void deleteForPosteTravail(PosteTravail poste);

    String translateEbuCodeToWMCode(int codeSysteme);

    /**
     * Permet de pr�parer la map des cas non annonc�s en caisse maladie.
     * 
     * @param dateAnnonceFrom
     * @param dateAnnonceTo
     * @return Map des cas non annonc�s en caisse maladie
     */
    Map<Administration, Collection<AffiliationCaisseMaladie>> prepareMapCasNonAnnonces(String dateAnnonceFrom,
            String dateAnnonceTo);

    /**
     * Permet de contr�ler la validit� d'une p�riode (date de fin ne doit pas �tre inf�rieur � la date de d�but)
     * 
     * @param dateFrom
     * @param dateTo
     * @param session
     * @throws SaisiePeriodeException
     */
    void checkPeriodValidty(String dateFrom, String dateTo, BSession session) throws SaisiePeriodeException;
}
