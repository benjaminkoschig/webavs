package ch.globaz.pegasus.business.services.models.calcul;

import ch.globaz.corvus.business.exceptions.CorvusException;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import java.util.Map;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplace;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplaceSearch;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeePlanCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

public interface CalculPersistanceService extends JadeApplicationService {

    void clearPCAccordee(Droit droit) throws JadePersistenceException, PCAccordeeException,
     JadeApplicationServiceNotAvailableException, JadeApplicationException;

    TupleDonneeRapport deserialiseDonneesCcXML(String donneeSerialisee);

    void recupereAnciensPCAccordee(String dateDebutPlage, Droit droit,
     Map<String, JadeAbstractSearchModel> cacheDonneesBD) throws PCAccordeeException, JadePersistenceException,
     CalculException, PropertiesException;

    List<PCAccordeePlanCalcul> sauvePCAccordee(Droit droit, PeriodePCAccordee periode,
     CalculPcaReplaceSearch anciennesPCAccordees) throws PCAccordeeException,
     JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException;

    void sauvePCAccordeeToCopie(CalculPcaReplace pcaToSave) throws PCAccordeeException,
     JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException;

    String serialiseDonneesCcXML(CalculComparatif cc);

    void updateVentilationPartCantonalePC(PeriodePCAccordee.TypeSeparationCC typeSeparationCC, String idPrestatoinAccordee, boolean isConjoint, String montantPartCantonale) throws CorvusException, JadeApplicationServiceNotAvailableException, JadePersistenceException, AdaptationException;

}
