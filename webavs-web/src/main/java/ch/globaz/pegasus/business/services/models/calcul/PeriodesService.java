/**
 * 
 */
package ch.globaz.pegasus.business.services.models.calcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import java.util.Map;
import ch.globaz.hera.business.exceptions.models.PeriodeException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreRenteException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;

/**
 * @author ECO
 * 
 */
public interface PeriodesService extends JadeApplicationService {

    public abstract String getDateDebutSansRetroactif(Droit droit, String dateDebutOriginal) throws CalculException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    public Map<String, JadeAbstractSearchModel> getDonneesCalculDroit(Droit droit, String debutPlage,
            String dateFinPlage) throws CalculException, JadePersistenceException, DroitException, PeriodeException,
            MonnaieEtrangereException, AutreRenteException, ForfaitsPrimesAssuranceMaladieException;

    public List<PeriodePCAccordee> recherchePeriodesCalcul(Droit droit, String debutPlage, String dateFinPlage,
            Map<String, JadeAbstractSearchModel> cacheDonnees, DonneesHorsDroitsProvider containerGlobal,
            boolean isDateFinForce) throws CalculException, JadePersistenceException;

    /**
     * Recherche la date de début de la plage de calcul du droit.
     * 
     * @param droit
     *            Le droit à calculer.
     * @return La date de début de la plage, ou null si pas de rente.
     * @throws JadePersistenceException
     *             s'il y a un problème dans la persistance
     * @throws CalculException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DemandeException
     * @throws DecisionException
     */
    public String[] recherchePlageCalcul(Droit droit) throws JadePersistenceException, CalculException,
            DemandeException, JadeApplicationServiceNotAvailableException;

}
