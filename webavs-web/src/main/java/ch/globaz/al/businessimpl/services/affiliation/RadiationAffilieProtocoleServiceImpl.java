package ch.globaz.al.businessimpl.services.affiliation;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.enumerations.affiliation.ALEnumProtocoleRadiationAffilie;
import ch.globaz.al.business.exceptions.document.ALDocumentException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.services.affiliation.RadiationAffilieProtocoleService;
import ch.globaz.al.business.services.dossiers.RadiationAutomatiqueService;
import ch.globaz.al.businessimpl.documents.excel.ALAbstractExcelServiceImpl;

/**
 * Implémentation du service permettant la génération d'un protocole lors de la radiation automatique de dossier (
 * {@link RadiationAutomatiqueService})
 * 
 * @author jts
 * 
 */
public class RadiationAffilieProtocoleServiceImpl extends ALAbstractExcelServiceImpl implements
        RadiationAffilieProtocoleService {

    public static final String ERRORS = "errors";
    public static final String LOG = "log";
    public static final String PRESTATIONS = "prestations";

    @Override
    public String createProtocole(
            List<Map<ALEnumProtocoleRadiationAffilie, Map<ALEnumProtocoleRadiationAffilie, Object>>> log,
            Map<String, DetailPrestationComplexSearchModel> prestations, Map<String, String> errors)
            throws JadeApplicationException {
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put(RadiationAffilieProtocoleServiceImpl.LOG, log);
            param.put(RadiationAffilieProtocoleServiceImpl.PRESTATIONS, prestations);
            param.put(RadiationAffilieProtocoleServiceImpl.ERRORS, errors);
            return createDocAndSave(param);
        } catch (Exception e) {
            throw new ALDocumentException(
                    "RadiationAutomatiqueDossiersProtocoleServiceImpl#createProtocole : Unable to create the protocole : "
                            + e.getMessage(), e);
        }
    }

    @Override
    protected String getModelName() {
        return "protocoleRadiationAffilie.xml";
    }

    @Override
    protected String getOutputName() {
        return "Protocole_RadiationAffilie_" + JadeDateUtil.getGlobazFormattedDate(new Date());
    }

    @Override
    protected IMergingContainer prepareData(Map<String, Object> parameters)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {

        CommonExcelmlContainer container = new CommonExcelmlContainer();

        List<Map<ALEnumProtocoleRadiationAffilie, Map<ALEnumProtocoleRadiationAffilie, Object>>> listeDossier = (List<Map<ALEnumProtocoleRadiationAffilie, Map<ALEnumProtocoleRadiationAffilie, Object>>>) parameters
                .get(RadiationAffilieProtocoleServiceImpl.LOG);

        int nbDossiers = listeDossier.size();
        int nbDroitRadies = 0;
        int nbAnnonces = 0;
        int nbNouveauxDroitsDossier = 0;
        int nbNouveauxDroitsActifsDossier = 0;
        int nbNouvellesAnnoncesDossier = 0;

        int nbDroitRadiesDossier = 0;
        int nbAnnoncesDossier = 0;
        int nbNouveauxDossiers = 0;
        int nbNouveauxDroits = 0;
        int nbNouveauxDroitsActifs = 0;
        int nbNouvellesAnnonces = 0;

        for (Map<ALEnumProtocoleRadiationAffilie, Map<ALEnumProtocoleRadiationAffilie, Object>> result : listeDossier) {

            Map<ALEnumProtocoleRadiationAffilie, Object> rad = result.get(ALEnumProtocoleRadiationAffilie.RAD);
            Map<ALEnumProtocoleRadiationAffilie, Object> copie = result.get(ALEnumProtocoleRadiationAffilie.COPIE);

            nbDroitRadiesDossier = (Integer) rad.get(ALEnumProtocoleRadiationAffilie.NB_DROITS_RADIES);
            nbAnnoncesDossier = (Integer) rad.get(ALEnumProtocoleRadiationAffilie.NB_ANNONCES);

            container.put("LABEL_TOTAL", "");
            container.put("VALUE_DOSSIER_RADIE",
                    ((DossierComplexModel) rad.get(ALEnumProtocoleRadiationAffilie.DOSSIER)).getId());
            container.put("VALUE_NB_DROITS_RADIE", String.valueOf(nbDroitRadiesDossier));
            container.put("VALUE_NB_ANNONCES_MUTATION", String.valueOf(nbAnnoncesDossier));

            nbDroitRadies += nbDroitRadiesDossier;
            nbAnnonces += nbAnnoncesDossier;

            if (copie != null) {

                nbNouveauxDroitsDossier = (Integer) copie.get(ALEnumProtocoleRadiationAffilie.NB_DROITS);
                nbNouveauxDroitsActifsDossier = (Integer) copie.get(ALEnumProtocoleRadiationAffilie.NB_DROITS_ACTIFS);
                nbNouvellesAnnoncesDossier = (Integer) copie.get(ALEnumProtocoleRadiationAffilie.NB_ANNONCES);

                container.put("VALUE_DOSSIER_COPIE",
                        ((DossierComplexModel) copie.get(ALEnumProtocoleRadiationAffilie.DOSSIER)).getId());
                container.put("VALUE_NB_DROITS_COPIES", String.valueOf(nbNouveauxDroitsDossier));
                container.put("VALUE_NB_DROITS_ACTIFS", String.valueOf(nbNouveauxDroitsActifsDossier));
                container.put("VALUE_NB_ANNONCES_CREATION", String.valueOf(nbNouvellesAnnoncesDossier));

                nbNouveauxDossiers++;
                nbNouveauxDroits += nbNouveauxDroitsDossier;
                nbNouveauxDroitsActifs += nbNouveauxDroitsActifsDossier;
                nbNouvellesAnnonces += nbNouvellesAnnoncesDossier;
            } else {
                container.put("VALUE_DOSSIER_COPIE", "");
                container.put("VALUE_NB_DROITS_COPIES", "");
                container.put("VALUE_NB_DROITS_ACTIFS", "");
                container.put("VALUE_NB_ANNONCES_CREATION", "");
            }
        }

        // ligne des totaux
        container.put("LABEL_TOTAL", "Total");
        container.put("VALUE_DOSSIER_RADIE", String.valueOf(nbDossiers));
        container.put("VALUE_NB_DROITS_RADIE", String.valueOf(nbDroitRadies));
        container.put("VALUE_NB_ANNONCES_MUTATION", String.valueOf(nbAnnonces));
        container.put("VALUE_DOSSIER_COPIE", String.valueOf(nbNouveauxDossiers));
        container.put("VALUE_NB_DROITS_COPIES", String.valueOf(nbNouveauxDroits));
        container.put("VALUE_NB_DROITS_ACTIFS", String.valueOf(nbNouveauxDroitsActifs));
        container.put("VALUE_NB_ANNONCES_CREATION", String.valueOf(nbNouvellesAnnonces));

        for (Entry<String, DetailPrestationComplexSearchModel> entry : ((Map<String, DetailPrestationComplexSearchModel>) parameters
                .get(RadiationAffilieProtocoleServiceImpl.PRESTATIONS)).entrySet()) {

            if (entry.getValue().getSearchResults() != null) {
                for (JadeAbstractModel item : entry.getValue().getSearchResults()) {

                    DetailPrestationComplexModel prest = (DetailPrestationComplexModel) item;

                    container.put("VALUE_PREST_DOSSIER", entry.getKey());
                    container.put("VALUE_PREST_DROIT", prest.getDroitComplexModel().getEnfantComplexModel()
                            .getPersonneEtendueComplexModel().getTiers().getDesignation1()
                            + " "
                            + prest.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel()
                                    .getTiers().getDesignation2()
                            + " - "
                            + prest.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel()
                                    .getPersonneEtendue().getNumAvsActuel());
                    container.put("VALUE_PREST_PERIODE", prest.getDetailPrestationModel().getPeriodeValidite());
                    container.put("VALUE_PREST_MONTANT", prest.getDetailPrestationModel().getMontant());
                    container.put("VALUE_PREST_RESTITUTION", ALCSPrestation.BONI_RESTITUTION.equals(prest
                            .getEntetePrestationModel().getBonification()) ? "oui" : "");
                }
            }
        }

        for (Entry<String, String> entry : ((Map<String, String>) parameters
                .get(RadiationAffilieProtocoleServiceImpl.ERRORS)).entrySet()) {
            container.put("VALUE_ERR_DOSSIER", entry.getKey());
            container.put("VALUE_ERR", entry.getValue());
        }

        return container;
    }
}
