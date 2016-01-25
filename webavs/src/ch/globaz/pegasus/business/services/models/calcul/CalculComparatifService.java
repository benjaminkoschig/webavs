/**
 * 
 */
package ch.globaz.pegasus.business.services.models.calcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.exceptions.models.home.HomeException;
import ch.globaz.pegasus.business.models.calcul.CalculMembreFamille;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplaceSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;

/**
 * @author ECO
 */
public interface CalculComparatifService extends JadeApplicationService {

    public void calculePCAccordes(Droit droit, List<PeriodePCAccordee> listePCAccordes) throws CalculException;

    public void calculJoursAppoint(List<PeriodePCAccordee> listePCAccordes,
            CalculPcaReplaceSearch calculPcaReplaceSearch) throws CalculException;

    public void consolideCacheDonneesPersonnes(List<PeriodePCAccordee> listePCAccordes,
            Map<String, JadeAbstractSearchModel> cacheDonneesBD, Map<String, CalculMembreFamille> listePersonnes,
            String dateFinPlageCalcul, DonneesHorsDroitsProvider containerGlobal) throws CalculException;

    public void loadDonneesCalculComparatif(Droit droit, Map<String, JadeAbstractSearchModel> cacheDonnees,
            List<PeriodePCAccordee> periodes, Map<String, CalculMembreFamille> listePersonnes, String debutPlage)
            throws CalculException, JadePersistenceException, TaxeJournaliereHomeException, HomeException;
}
