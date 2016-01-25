/**
 * 
 */
package ch.globaz.al.business.envois;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jsp.util.GlobazJSPBeanUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.al.business.models.adi.AdiDecompteComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.envoi.business.models.parametrageEnvoi.SignetListModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.SignetListModelSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;
import ch.globaz.envoi.business.utils.EnvoiData;
import ch.globaz.naos.business.model.AffiliationSearchSimpleModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author dhi
 * 
 */
public class ALEnvoiData extends EnvoiData {

    /**
     * @param dataInput
     * @param idFormule
     */
    public ALEnvoiData(HashMap<Object, Object> dataInput, String idFormule) {
        super(dataInput, idFormule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.envoi.business.utils.EnvoiData#loadData()
     */
    @Override
    protected HashMap<Object, Object> loadData() throws Exception {
        // -------------------------------------------------------------------
        // 1) Classe mère fait le travail pour les cas standard
        // -------------------------------------------------------------------
        HashMap<Object, Object> mapSignetValues = super.loadData();

        // -------------------------------------------------------------------
        // 2) Cas spéciaux - Adresse - Listes - check si document en contient
        // -------------------------------------------------------------------
        try {
            // Recherche des signets pour le document en cours
            SignetListModelSearch signetListModelSearch = new SignetListModelSearch();
            signetListModelSearch.setForIdFormule(getIdFormule());
            signetListModelSearch.setDefinedSearchSize(0);
            signetListModelSearch = ENServiceLocator.getSignetListModelService().search(signetListModelSearch);

            // Boucle sur les signets à renseigner
            for (int iSignet = 0; iSignet < signetListModelSearch.getSize(); iSignet++) {
                // Nom de la classe du signet
                SignetListModel currentSignet = (SignetListModel) signetListModelSearch.getSearchResults()[iSignet];
                String signetClassName = currentSignet.getSimpleSignetModel().getModel();
                String signetMethodName = currentSignet.getSimpleSignetModel().getMethode();
                String signetName = currentSignet.getSimpleSignetModel().getSignet();
                String signetCode = currentSignet.getSimpleSignetModel().getCode();
                // -------------------------------------------------------------------
                // 3) Cas spéciaux - adresses, paramètres annuels, listes...
                // -------------------------------------------------------------------
                if (AdresseTiersDetail.class.getName().equals(signetClassName)) {
                    // -------------------------------------------------------------------
                    // 3a) Cas spécial adresse souhaitée
                    // -------------------------------------------------------------------
                    if (signetName.equals("ADR0")) {
                        mapSignetValues = loadDataAdresse(mapSignetValues, signetMethodName, signetName);
                    } else if (signetName.equals("ADR1")) {
                        mapSignetValues = loadDataAdresseAffilie(mapSignetValues, signetMethodName, signetName);
                    } else if (signetName.equals("ADR2")) {
                        mapSignetValues = loadDataAdresseCAFEtrangere(mapSignetValues, signetMethodName, signetName);
                    } else {
                        mapSignetValues = loadDataAdresse(mapSignetValues, signetMethodName, signetName);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }

        return mapSignetValues;
    }

    /**
     * Renseignement de la hashmap signet - value pour le cas de l'adresse Renseignement des signets ADR01
     * (signetLigne0) à ADR06
     * 
     * @param mapSignetValuesInput
     * 
     * @return return l'hash map complétée format ADR01 - Ac Tacim format ADR02 - Et Medine format ADR03 - Rue des
     *         Prejures 23 format ADR04 - VILLAGE
     */
    private HashMap<Object, Object> loadDataAdresse(HashMap<Object, Object> mapSignetValuesInput,
            String signetMethodName, String signetLigne0) {
        // TODO CONSTANTE à AJOUTER DANS constantes de avsservice
        String CS_DOMAINE_AF = ALCSTiers.DOMAINE_AF;
        String CS_TYPE_COURRIER = "508001";
        String CS_TYPE_DOMICILE = "508008";

        // Variables utiles
        DossierComplexModel currentDossier = null;
        AdresseTiersDetail adresseDetail = null;

        // Depuis les objects chargés, on récupère le dossier
        currentDossier = (DossierComplexModel) getLoadedObject(DossierComplexModel.class.getName());

        if (currentDossier != null) {
            // On récupère l'adresse du contribuable et affectation si document famille
            try {
                ArrayList<String> orderTypeAdresse = new ArrayList<String>();
                orderTypeAdresse.add(CS_TYPE_COURRIER);
                orderTypeAdresse.add(CS_TYPE_DOMICILE);
                adresseDetail = TIBusinessServiceLocator.getAdresseService().getAdresseTiersCustomCascade(
                        currentDossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                                .getIdTiers(), JadeDateUtil.getGlobazFormattedDate(new Date()), CS_DOMAINE_AF,
                        orderTypeAdresse, 1);
                // adresseDetail = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                // currentDossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                // .getIdTiers(), true, JadeDateUtil.getGlobazFormattedDate(new Date()), CS_DOMAINE_AF,
                // CS_TYPE_COURRIER, "");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // fill the map avec les lignes de l'adresse
        if (adresseDetail != null) {
            if ((adresseDetail.getAdresseFormate() != null) || (adresseDetail.getFields() != null)) {
                try {
                    Object adresseFormatee = GlobazJSPBeanUtil.getProperty(signetMethodName, adresseDetail);
                    String[] adresseLines = adresseFormatee.toString().split("\\n");
                    for (int iLines = 1; iLines <= adresseLines.length; iLines++) {
                        mapSignetValuesInput.put(signetLigne0 + iLines, adresseLines[iLines - 1]);
                    }
                    for (int iEmptyLine = adresseLines.length + 1; iEmptyLine < 10; iEmptyLine++) {
                        mapSignetValuesInput.put(signetLigne0 + iEmptyLine, "");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return mapSignetValuesInput;
    }

    /**
     * Renseignement de la hashmap signet - value pour le cas de l'adresse Renseignement des signets ADR1 (signetLigne0)
     * à ADR19
     * 
     * @param mapSignetValuesInput
     * 
     * @return return l'hash map complétée format ADR01 - Ac Tacim format ADR02 - Et Medine format ADR03 - Rue des
     *         Prejures 23 format ADR04 - VILLAGE
     */
    private HashMap<Object, Object> loadDataAdresseAffilie(HashMap<Object, Object> mapSignetValuesInput,
            String signetMethodName, String signetLigne0) {
        // TODO CONSTANTE à AJOUTER DANS constantes de avsservice
        String CS_DOMAINE_AF = ALCSTiers.DOMAINE_AF;
        String CS_TYPE_COURRIER = "508001";
        String CS_TYPE_DOMICILE = "508008";

        // Variables utiles
        DossierComplexModel currentDossier = null;
        AdresseTiersDetail adresseDetail = null;

        // Depuis les objects chargés, on récupère le dossier
        currentDossier = (DossierComplexModel) getLoadedObject(DossierComplexModel.class.getName());

        // On recherche l'affilié
        String idTierAffilie = "";
        String numAffilie = currentDossier.getDossierModel().getNumeroAffilie();
        AffiliationSearchSimpleModel searchModel = new AffiliationSearchSimpleModel();
        searchModel.setForNumeroAffilie(numAffilie);
        try {
            searchModel = AFBusinessServiceLocator.getAffiliationService().find(searchModel);
            if (searchModel.getSize() >= 1) {
                AffiliationSimpleModel affil = (AffiliationSimpleModel) searchModel.getSearchResults()[0];
                idTierAffilie = affil.getIdTiers();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!JadeStringUtil.isEmpty(idTierAffilie)) {
            // On récupère l'adresse du contribuable et affectation si document famille
            try {
                ArrayList<String> orderTypeAdresse = new ArrayList<String>();
                orderTypeAdresse.add(CS_TYPE_COURRIER);
                orderTypeAdresse.add(CS_TYPE_DOMICILE);
                adresseDetail = TIBusinessServiceLocator.getAdresseService().getAdresseTiersCustomCascade(
                        idTierAffilie, JadeDateUtil.getGlobazFormattedDate(new Date()), CS_DOMAINE_AF,
                        orderTypeAdresse, 1);
                // adresseDetail = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTierAffilie, true,
                // JadeDateUtil.getGlobazFormattedDate(new Date()), CS_DOMAINE_AF, CS_TYPE_COURRIER, "");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // fill the map avec les lignes de l'adresse
        if (adresseDetail != null) {
            if ((adresseDetail.getAdresseFormate() != null) || (adresseDetail.getFields() != null)) {
                try {
                    Object adresseFormatee = GlobazJSPBeanUtil.getProperty(signetMethodName, adresseDetail);
                    String[] adresseLines = adresseFormatee.toString().split("\\n");
                    for (int iLines = 1; iLines <= adresseLines.length; iLines++) {
                        mapSignetValuesInput.put(signetLigne0 + iLines, adresseLines[iLines - 1]);
                    }
                    for (int iEmptyLine = adresseLines.length + 1; iEmptyLine < 10; iEmptyLine++) {
                        mapSignetValuesInput.put(signetLigne0 + iEmptyLine, "");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return mapSignetValuesInput;
    }

    /**
     * Renseignement de la hashmap signet - value pour le cas de l'adresse Renseignement des signets ADR2 (signetLigne0)
     * à ADR29
     * 
     * @param mapSignetValuesInput
     * 
     * @return return l'hash map complétée format ADR01 - Ac Tacim format ADR02 - Et Medine format ADR03 - Rue des
     *         Prejures 23 format ADR04 - VILLAGE
     */
    private HashMap<Object, Object> loadDataAdresseCAFEtrangere(HashMap<Object, Object> mapSignetValuesInput,
            String signetMethodName, String signetLigne0) {
        // TODO CONSTANTE à AJOUTER DANS constantes de avsservice
        String CS_DOMAINE_AF = ALCSTiers.DOMAINE_AF;
        String CS_TYPE_COURRIER = "508001";
        String CS_TYPE_DOMICILE = "508008";

        // Variables utiles
        DossierComplexModel currentDossier = null;
        AdresseTiersDetail adresseDetail = null;

        // Depuis les objects chargés, on récupère le dossier
        currentDossier = (DossierComplexModel) getLoadedObject(DossierComplexModel.class.getName());

        // On recherche la CAF étrangère via les ADI
        String idTierCAF = "";
        AdiDecompteComplexSearchModel adiSearch = new AdiDecompteComplexSearchModel();
        adiSearch.setForIdDossier(currentDossier.getId());
        try {
            adiSearch = ALServiceLocator.getAdiDecompteComplexModelService().search(adiSearch);
            if (adiSearch.getSize() > 0) {
                AdiDecompteComplexModel currentAdi = (AdiDecompteComplexModel) adiSearch.getSearchResults()[0];
                idTierCAF = currentAdi.getDecompteAdiModel().getIdTiersOrganismeEtranger();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!JadeStringUtil.isEmpty(idTierCAF)) {
            // On récupère l'adresse du contribuable et affectation si document famille
            try {
                ArrayList<String> orderTypeAdresse = new ArrayList<String>();
                orderTypeAdresse.add(CS_TYPE_COURRIER);
                orderTypeAdresse.add(CS_TYPE_DOMICILE);
                adresseDetail = TIBusinessServiceLocator.getAdresseService().getAdresseTiersCustomCascade(idTierCAF,
                        JadeDateUtil.getGlobazFormattedDate(new Date()), CS_DOMAINE_AF, orderTypeAdresse, 1);
                // adresseDetail = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(idTierCAF, true,
                // JadeDateUtil.getGlobazFormattedDate(new Date()), CS_DOMAINE_AF, CS_TYPE_DOMICILE, "");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // fill the map avec les lignes de l'adresse
        if (adresseDetail != null) {
            if ((adresseDetail.getAdresseFormate() != null) || (adresseDetail.getFields() != null)) {
                try {
                    Object adresseFormatee = GlobazJSPBeanUtil.getProperty(signetMethodName, adresseDetail);
                    String[] adresseLines = adresseFormatee.toString().split("\\n");
                    for (int iLines = 1; iLines <= adresseLines.length; iLines++) {
                        mapSignetValuesInput.put(signetLigne0 + iLines, adresseLines[iLines - 1]);
                    }
                    for (int iEmptyLine = adresseLines.length + 1; iEmptyLine < 10; iEmptyLine++) {
                        mapSignetValuesInput.put(signetLigne0 + iEmptyLine, "");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return mapSignetValuesInput;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.envoi.business.utils.EnvoiData#prepareData(java.util.HashMap)
     */
    @Override
    protected ArrayList<Object> prepareData(HashMap<Object, Object> inputMap) {
        ArrayList<Object> objectArray = new ArrayList<Object>();
        // Parcours des éléments et instanciation
        Set<Object> classNames = inputMap.keySet();
        Iterator<Object> it = classNames.iterator();
        while (it.hasNext()) {
            String currentClassName = (String) it.next();
            String currentId = (String) inputMap.get(currentClassName);

            Object objectToAdd = null;
            if (DossierComplexModel.class.getName().equals(currentClassName)) {
                objectToAdd = retrieveDossier(currentId);
            } else if (BSession.class.getName().equals(currentClassName)) {
                objectToAdd = retrieveSession(currentId);
            }
            if (objectToAdd != null) {
                objectArray.add(objectToAdd);
            }
        }
        return objectArray;
    }

    /**
     * Retrieve du dossier en fonction de son id
     * 
     * @param idDossier
     * @return
     */
    protected Object retrieveDossier(String idDossier) {
        DossierComplexModel currentDossier = null;
        try {
            currentDossier = ALServiceLocator.getDossierComplexModelService().read(idDossier);
        } catch (Exception ex) {
            logErrorMsg("Unable to retrieve Dossier id : " + idDossier);
        }
        return currentDossier;
    }

    /**
     * Retrieve de la session
     * 
     * @param currentUserId
     * @return
     */
    protected Object retrieveSession(String currentUserId) {
        BSession currentSession = null;
        try {
            currentSession = BSessionUtil.getSessionFromThreadContext();
            if (currentSession.getUserId().equals(currentUserId)) {
                return currentSession;
            } else {
                return null;
            }
        } catch (Exception ex) {
            logErrorMsg("Unable to retrieve Session id '" + currentUserId + "'");
            ex.printStackTrace();
        }
        return currentSession;
    }

}
