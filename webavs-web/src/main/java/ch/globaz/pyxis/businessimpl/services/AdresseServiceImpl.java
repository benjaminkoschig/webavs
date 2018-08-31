package ch.globaz.pyxis.businessimpl.services;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import ch.globaz.pyxis.business.model.AdresseComplexModel;
import ch.globaz.pyxis.business.model.AdressePaiementSearchComplexModel;
import ch.globaz.pyxis.business.model.AdresseSearch;
import ch.globaz.pyxis.business.model.AdresseSearchComplexModel;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.AvoirPaiementSearchSimpleModel;
import ch.globaz.pyxis.business.model.LocaliteSearchSimpleModel;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.services.AdresseService;
import ch.globaz.pyxis.common.Messages;
import ch.globaz.pyxis.exception.PyxisException;
import globaz.globall.db.BManager;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementFormater;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.adressecourrier.TIAbstractAdresseData;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressecourrier.TIAdresseDataManager;
import globaz.pyxis.db.adressecourrier.TIAdresseManager;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.adressecourrier.TILocaliteManager;
import globaz.pyxis.db.divers.TICascadeDomaineByDomaine;
import globaz.pyxis.db.divers.TICascadeDomaineByDomaineManager;
import globaz.pyxis.util.TIAdressePmtResolver;
import globaz.pyxis.util.TIAdresseResolver;

public class AdresseServiceImpl implements AdresseService {

    /**
     * Cherche si il existe deja une adresse semblable. (pour éviter les doublons)
     * 
     * @param model
     */
    private String _findAdresseSemblable(AdresseComplexModel model) throws Exception {

        TIAdresseManager adrMgr = new TIAdresseManager();
        adrMgr.setForLigneAdresse1N(model.getAdresse().getLigneAdresse1());
        adrMgr.setForLigneAdresse2(model.getAdresse().getLigneAdresse2());
        adrMgr.setForLigneAdresse3(model.getAdresse().getLigneAdresse3());
        adrMgr.setForLigneAdresse4(model.getAdresse().getLigneAdresse4());
        adrMgr.setForTitreAdresseN(model.getAdresse().getTitreAdresse());
        adrMgr.setForAttention(model.getAdresse().getAttention());
        adrMgr.setForCasePostale(model.getAdresse().getCasePostale());
        adrMgr.setForRue(model.getAdresse().getRue());
        adrMgr.setForNumeroRue(model.getAdresse().getNumeroRue());
        adrMgr.setForIdLocalite(model.getAdresse().getIdLocalite());
        adrMgr.find();
        if (adrMgr.size() > 0) {
            TIAdresse adr = (TIAdresse) adrMgr.getFirstEntity();
            return adr.getIdAdresseUnique();
        }
        return "";

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pyxis.business.service.AdresseService#addAdresse(ch.globaz.pyxis.business.model.AdresseComplexModel,
     * java.lang.String, java.lang.String)
     */
    @Override
    public AdresseComplexModel addAdresse(AdresseComplexModel model, String idApplication, String typeAdresse,
            Boolean wantUpdatePaiement) throws JadePersistenceException, JadeApplicationException {
        return addAdresse(model, idApplication, typeAdresse, wantUpdatePaiement, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pyxis.business.service.AdresseService#updateAdresse(ch.globaz.pyxis.business.model.AdresseComplexModel,
     * java.lang.String, java.lang.String)
     */
    @Override
    public AdresseComplexModel updateAdresse(AdresseComplexModel model, String idApplication, String typeAdresse,
            Boolean wantUpdatePaiement) throws JadePersistenceException, JadeApplicationException {
        return addAdresse(model, idApplication, typeAdresse, wantUpdatePaiement, false);
    }

    private AdresseComplexModel addAdresse(AdresseComplexModel model, String idApplication, String typeAdresse,
            Boolean wantUpdatePaiement, Boolean isNewAdresse) throws JadePersistenceException, JadeApplicationException {

        // On contrôle que le model existe bien...
        if (model == null) {
            throw new PyxisException("Unable to create adresse, the given model is null");
        }

        // ...qu'un idTiers soit référencé....
        if (JadeStringUtil.isBlankOrZero(model.getTiers().getId())) {
            throw new PyxisException("AdresseServiceImpl#addAdresse: idTiers is not defined");
        }

        // ...qu'on ai un numéro postal...
        if (JadeStringUtil.isBlankOrZero(model.getLocalite().getNumPostal())) {
            throw new PyxisException("AdresseServiceImpl#addAdresse: NumPostal is not defined");
        }

        // ...un domaine...
        if (JadeStringUtil.isBlankOrZero(idApplication)) {
            throw new PyxisException("AdresseServiceImpl#addAdresse: idApplication can't be empty");
        }

        // ... et un type !
        if (JadeStringUtil.isBlankOrZero(typeAdresse)) {
            throw new PyxisException("AdresseServiceImpl#addAdresse: typeAdresse can't be empty");
        }

        AdresseComplexModel adresseComplexModel = new AdresseComplexModel();

        try {
            // On vérifie tout d'abord que la localité existe grâce au npa
            TIAdresse adresse = null;
            TILocaliteManager mgr = new TILocaliteManager();
            mgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            mgr.setInclureInactif(Boolean.TRUE);

            if (model.getLocalite().getNumPostal().length() == 4) {
                mgr.setForNumPostal(model.getLocalite().getNumPostal() + "00");
            } else {
                mgr.setForNumPostal(model.getLocalite().getNumPostal());
            }
            mgr.find(1);

            // Si on trouve une localité...
            if (mgr.size() > 0) {

                TILocalite localite = (TILocalite) mgr.getFirstEntity();
                // On set l'idLocalite dans le model
                model.getAdresse().setIdLocalite(localite.getIdLocalite());

                adresse = new TIAdresse();

                // On set les infos du model dans l'entity
                adresse.setIdLocalite(model.getAdresse().getIdLocalite());

                Boolean noRueOrNotNumeroRue = false;

                if (!JadeStringUtil.isBlank(model.getAdresse().getRue())) {
                    adresse.setRue(model.getAdresse().getRue());
                } else {
                    noRueOrNotNumeroRue = true;
                }

                if (!JadeStringUtil.isBlank(model.getAdresse().getNumeroRue())) {
                    adresse.setNumeroRue(model.getAdresse().getNumeroRue());
                } else {
                    noRueOrNotNumeroRue = true;
                }

                if (!JadeStringUtil.isBlank(model.getAdresse().getCasePostale())) {
                    adresse.setCasePostale(model.getAdresse().getCasePostale());
                }

                if ((model.getAdresse().getTitreAdresse() != null)
                        && !JadeStringUtil.isBlankOrZero(model.getAdresse().getTitreAdresse())) {
                    adresse.setTitreAdresse(model.getAdresse().getTitreAdresse());
                }

                if ((model.getAdresse().getAttention() != null)
                        && !JadeStringUtil.isBlankOrZero(model.getAdresse().getAttention())) {
                    adresse.setAttention(model.getAdresse().getAttention());
                }

                if ((model.getAdresse().getLigneAdresse1() != null)
                        && !JadeStringUtil.isBlank(model.getAdresse().getLigneAdresse1())) {
                    adresse.setLigneAdresse1(model.getAdresse().getLigneAdresse1());
                }

                if ((model.getAdresse().getLigneAdresse2() != null)
                        && !JadeStringUtil.isBlank(model.getAdresse().getLigneAdresse2())) {
                    adresse.setLigneAdresse2(model.getAdresse().getLigneAdresse2());
                }

                if ((model.getAdresse().getLigneAdresse3() != null)
                        && !JadeStringUtil.isBlank(model.getAdresse().getLigneAdresse3())) {
                    adresse.setLigneAdresse3(model.getAdresse().getLigneAdresse3());
                }

                if ((model.getAdresse().getLigneAdresse4() != null)
                        && !JadeStringUtil.isBlank(model.getAdresse().getLigneAdresse4())) {
                    adresse.setLigneAdresse4(model.getAdresse().getLigneAdresse4());
                }

                // Check si on trouve une adresse semblable
                String idAdresse = _findAdresseSemblable(model);

                // Si aucune adresse n'est trouvée, on la crée.
                if ("".equals(idAdresse)) {
                    adresse.wantCallValidate(true);
                    adresse.add();

                    if (adresse.hasErrors() || adresse.getSession().getCurrentThreadTransaction().hasErrors()) {
                        StringBuffer errorMsg = new StringBuffer();
                        if (adresse.hasErrors()) {
                            errorMsg = adresse.getErrors();
                        } else {
                            errorMsg = adresse.getSession().getCurrentThreadTransaction().getErrors();
                        }
                        adresseComplexModel = null;
                        JadeThread.logError(this.getClass().getName(), "Error while creating adresse !");
                        throw new Exception(errorMsg.toString());
                    }

                    idAdresse = adresse.getIdAdresse();
                }
                // On crée la relation "AvoirAdresse"
                TIAvoirAdresse avoirAdresseNew = new TIAvoirAdresse();
                // On ne veut pas d'autocommit pour pouvoir rollbacker le tout en cas d'erreur
                avoirAdresseNew.setWantCommit(false);
                avoirAdresseNew.setAllowModificationAdressePaiement(true);
                avoirAdresseNew.setWantUpdatePaiement(wantUpdatePaiement);
                avoirAdresseNew.setIdTiers(model.getTiers().getId());
                avoirAdresseNew.setIdAdresse(idAdresse);
                avoirAdresseNew.setIdApplication(idApplication);
                avoirAdresseNew.setTypeAdresse(typeAdresse);
                avoirAdresseNew.setTitreAdresse(model.getAdresse().getTitreAdresse());

                // Si on a pas de date de début défini dans le model, on prend la date du jour
                String dateDebutRelation = JadeDateUtil.getGlobazFormattedDate(new Date());
                if (!JAUtil.isDateEmpty(model.getAvoirAdresse().getDateDebutRelation())) {
                    dateDebutRelation = model.getAvoirAdresse().getDateDebutRelation();
                }

                if (isNewAdresse) {
                    avoirAdresseNew.setDateDebutRelation(dateDebutRelation);
                    avoirAdresseNew.add();
                } else {
                    avoirAdresseNew = avoirAdresseNew.findCurrentRelation();
                    avoirAdresseNew.setIdAdresse(idAdresse);
                    avoirAdresseNew.update();
                }

                if (avoirAdresseNew.hasErrors()
                        || avoirAdresseNew.getSession().getCurrentThreadTransaction().hasErrors()) {
                    StringBuffer errorMsg = new StringBuffer();
                    if (adresse.hasErrors()) {
                        errorMsg = avoirAdresseNew.getErrors();
                    } else {
                        errorMsg = avoirAdresseNew.getSession().getCurrentThreadTransaction().getErrors();
                    }

                    adresseComplexModel = null;
                    JadeThread.logError(this.getClass().getName(), "Error while searching avoirAdresse !");
                    throw new Exception(errorMsg.toString());
                }

                AdresseSearchComplexModel searchModel = new AdresseSearchComplexModel();
                searchModel.setForIdAvoirAdresseInterneUnique(avoirAdresseNew.getIdAdresseIntUnique());
                searchModel = findAdresse(searchModel);
                if (searchModel.getSearchResults().length == 1) {
                    adresseComplexModel = (AdresseComplexModel) searchModel.getSearchResults()[0];
                } else {
                    adresseComplexModel = null;
                    JadeThread.logError(this.getClass().getName(), "Error while searching AdresseSearchComplexModel !");
                    throw new Exception("Error while searching AdresseSearchComplexModel !");
                }

            } else {
                adresseComplexModel = null;
                JadeThread.logError(this.getClass().getName(), "Error localite not found !");
                throw new Exception("Error localite not found !");
            }
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), "Error while creating adresse tiers ! (" + e.getMessage()
            + ")");
            adresseComplexModel = null;
        }

        return adresseComplexModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pyxis.business.service.AdresseService#countAdresse(ch.globaz
     * .pyxis.business.model.AdresseSearchComplexModel)
     */
    @Override
    public int countAdresse(AdresseSearchComplexModel model) throws JadePersistenceException, JadeApplicationException {

        // on force la recherche avec le search-literal
        if (!JadeStringUtil.isEmpty(model.getForLocaliteUpperLike())) {
            model.setWhereKey(AdresseSearchComplexModel.WHERE_KEY_FOR_LOCAL_UPPER_LIKE);
        }

        return JadePersistenceManager.count(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pyxis.business.service.AdresseService#countAdressePaiement(
     * ch.globaz.pyxis.business.model.AdressePaiementSearchComplexModel)
     */
    @Override
    public int countAdressePaiement(AdressePaiementSearchComplexModel model) throws JadePersistenceException,
    JadeApplicationException {
        return JadePersistenceManager.count(model);
    }

    @Override
    public int countAdresseWithSimpleTiers(AdresseSearch adresseSearch) throws JadePersistenceException,
    JadeApplicationException {
        return JadePersistenceManager.count(adresseSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pyxis.business.service.AdresseService#countLocalite(ch.globaz
     * .pyxis.business.model.LocaliteSearchSimpleModel)
     */
    @Override
    public int countLocalite(LocaliteSearchSimpleModel model) throws JadePersistenceException, JadeApplicationException {

        // on force le count avec le search-literal
        if (!JadeStringUtil.isEmpty(model.getForLocaliteUpperLike())) {
            model.setWhereKey(LocaliteSearchSimpleModel.WHERE_KEY_FOR_LOCAL_UPPER_LIKE);
        }

        return JadePersistenceManager.count(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pyxis.business.service.AdresseService#countPays(ch.globaz.pyxis
     * .business.model.LocaliteSearchSimpleModel)
     */
    @Override
    public int countPays(PaysSearchSimpleModel model) throws JadePersistenceException, JadeApplicationException {

        // on force la recherche avec le search-literal
        if (!JadeStringUtil.isEmpty(model.getForLibelleFrUpperLike())) {
            model.setWhereKey(PaysSearchSimpleModel.WHERE_KEY_FOR_LIBELLE_FR_UPPER_LIKE);
        } else if (!JadeStringUtil.isEmpty(model.getForLibelleAlUpperLike())) {
            model.setWhereKey(PaysSearchSimpleModel.WHERE_KEY_FOR_LIBELLE_AL_UPPER_LIKE);
        } else if (!JadeStringUtil.isEmpty(model.getForLibelleItUpperLike())) {
            model.setWhereKey(PaysSearchSimpleModel.WHERE_KEY_FOR_LIBELLE_IT_UPPER_LIKE);
        }

        return JadePersistenceManager.count(model);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pyxis.business.service.AdresseService#findAdresse(ch.globaz
     * .pyxis.business.model.AdresseSearchComplexModel)
     */
    @Override
    public AdresseSearchComplexModel findAdresse(AdresseSearchComplexModel searchModel)
            throws JadePersistenceException, JadeApplicationException {

        // on force la recherche avec le search-literal
        if (!JadeStringUtil.isEmpty(searchModel.getForLocaliteUpperLike())) {
            searchModel.setWhereKey(AdresseSearchComplexModel.WHERE_KEY_FOR_LOCAL_UPPER_LIKE);
        }

        return (AdresseSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pyxis.business.service.AdresseService#findAdressePaiement(ch
     * .globaz.pyxis.business.model.AdressePaiementSearchComplexModel)
     */
    @Override
    public AdressePaiementSearchComplexModel findAdressePaiement(AdressePaiementSearchComplexModel searchModel)
            throws JadePersistenceException, JadeApplicationException {
        // Set de la recherche par défaut pour pas avoir d'erreur quand on à pas préciser le compte IBAN
        if (searchModel.getForNumCompteBancaireLike() != null && !searchModel.getForNumCompteBancaireLike().isEmpty()) {
            searchModel.setWhereKey("widgetSearchWithforNumCompteBancaireLike");
        }
        return (AdressePaiementSearchComplexModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pyxis.business.service.AdresseService#findLocalite(ch.globaz
     * .pyxis.business.model.LocaliteSearchSimpleModel)
     */
    @Override
    public LocaliteSearchSimpleModel findLocalite(LocaliteSearchSimpleModel searchModel)
            throws JadePersistenceException, JadeApplicationException {

        // on force la recherche avec le search-literal
        if (!JadeStringUtil.isEmpty(searchModel.getForLocaliteUpperLike())) {
            searchModel.setWhereKey(LocaliteSearchSimpleModel.WHERE_KEY_FOR_LOCAL_UPPER_LIKE);
        }

        return (LocaliteSearchSimpleModel) JadePersistenceManager.search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pyxis.business.service.AdresseService#findPays(ch.globaz.pyxis
     * .business.model.PaysSearchSimpleModel)
     */
    @Override
    public PaysSearchSimpleModel findPays(PaysSearchSimpleModel searchModel) throws JadePersistenceException,
    JadeApplicationException {

        // on force la recherche avec le search-literal
        if (!JadeStringUtil.isEmpty(searchModel.getForLibelleFrUpperLike())) {
            searchModel.setWhereKey(PaysSearchSimpleModel.WHERE_KEY_FOR_LIBELLE_FR_UPPER_LIKE);
        } else if (!JadeStringUtil.isEmpty(searchModel.getForLibelleAlUpperLike())) {
            searchModel.setWhereKey(PaysSearchSimpleModel.WHERE_KEY_FOR_LIBELLE_AL_UPPER_LIKE);
        } else if (!JadeStringUtil.isEmpty(searchModel.getForLibelleItUpperLike())) {
            searchModel.setWhereKey(PaysSearchSimpleModel.WHERE_KEY_FOR_LIBELLE_IT_UPPER_LIKE);
        }

        return (PaysSearchSimpleModel) JadePersistenceManager.search(searchModel);
    }

    /**
     * Renvoi une entité TiersAdresse contenant divers informations sur l'adresse de paiement du tiers
     */
    @Override
    public AdresseTiersDetail getAdressePaiementTiers(String idTiers, Boolean herite, String idApplication,
            String date, String numeroExterne) throws JadePersistenceException, JadeApplicationException {
        AdresseTiersDetail result = new AdresseTiersDetail();
        try {
            TIAdressePaiementDataSource dataSource = TIAdressePmtResolver.dataSourceAdrPmt(null, idTiers,
                    idApplication, numeroExterne, date, herite, "FR");
            if (dataSource != null) {
                result.setAdresseFormate(new TIAdressePaiementFormater().format(dataSource));
                rechercheFields(result, TIAdressePaiementDataSource.class, dataSource);
            } // if
        } catch (Exception e) {
            throw new PyxisException(Messages.TECHNICAL, e);
        }

        return result;
    } // getAdressePaiementTiers

    /**
     * Renvoi une entité AdresseTiersDetail contenant divers informations sur l'adresse du tiers
     */
    @Override
    public AdresseTiersDetail getAdresseTiers(String idTiers, Boolean herite, String date, String idApplication,
            String typeAdresse, String numeroExterne) throws JadePersistenceException, JadeApplicationException {
        AdresseTiersDetail result = new AdresseTiersDetail();

        if (!JadeStringUtil.isIntegerEmpty(idTiers)) {
            try {
                TIAdresseDataSource dataSource = TIAdresseResolver.dataSourceAdr(null, idTiers, idApplication,
                        typeAdresse, numeroExterne, date, herite.booleanValue(), null);
                if (dataSource != null) {
                    result.setAdresseFormate(new TIAdresseFormater().format(dataSource));
                    rechercheFields(result, TIAdresseDataSource.class, dataSource);
                } // if
            } catch (Exception e) {
                throw new PyxisException(Messages.TECHNICAL, e);
            } // catch
        } // if

        return result;
    } // getAdresseTiers

    /**
     * Implemente par GMO le 16.12.2010
     */
    @Override
    public AdresseTiersDetail getAdresseTiersCustomCascade(String idTiers, String date, String idApplication,
            Collection<String> orderTypeAdresse, int modePriority) throws JadePersistenceException,
    JadeApplicationException {

        if (JadeStringUtil.isEmpty(idTiers)) {
            throw new PyxisException("AdresseServiceImpl#getAdresseTiersCustomCascade: idTiers is not defined");
        }

        if (JadeStringUtil.isEmpty(date)) {
            throw new PyxisException("AdresseServiceImpl#getAdresseTiersCustomCascade: date is not defined");
        }

        if (JadeStringUtil.isEmpty(idApplication)) {
            throw new PyxisException("AdresseServiceImpl#getAdresseTiersCustomCascade: idApplication is not defined");
        }

        if ((modePriority != AdresseService.MODE_CASC_PRIORITY_DOMAINE)
                && (modePriority != AdresseService.MODE_CASC_PRIORITY_TYPE)) {
            throw new PyxisException(
                    "AdresseServiceImpl#getAdresseTiersCustomCascade: modePriority value can't be handled");
        }

        if (orderTypeAdresse == null) {
            throw new PyxisException("AdresseServiceImpl#getAdresseTiersCustomCascade: orderTypeAdresse is not defined");
        }

        // on charge toutes les adresses actives pour ce tiers
        TIAdresseDataManager mgr = new TIAdresseDataManager();
        mgr.setSession(null);
        mgr.setForIdTiers(idTiers);
        mgr.setForDateEntreDebutEtFin(date);
        mgr.changeManagerSize(0);
        try {
            mgr.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new PyxisException(Messages.TECHNICAL, e);
        }

        Map<String, Map<String, TIAbstractAdresseData>> adressesFound_kTypeAdr = new HashMap<String, Map<String, TIAbstractAdresseData>>();

        for (Iterator<TIAbstractAdresseData> it = mgr.iterator(); it.hasNext();) {
            TIAbstractAdresseData data = it.next();

            for (String type : orderTypeAdresse) {

                Map<String, TIAbstractAdresseData> adressesCurrentType = new HashMap<String, TIAbstractAdresseData>();

                // on récupère la map liée au type stockée dans la liste mère
                // (liste clé=type d'adresses)
                if (adressesFound_kTypeAdr.containsKey(type)) {
                    adressesCurrentType = adressesFound_kTypeAdr.get(type);
                }

                if ((data.getTypeAdresse().equals(type)) && ("".equals(data.getIdExterne()))
                        && (data.getIdApplication().equals(idApplication))) {

                    adressesCurrentType.put(idApplication, data);

                }

                if ((data.getTypeAdresse().equals(type)) && ("".equals(data.getIdExterne()))
                        && (data.getIdApplication().equals(AdresseService.CS_DOMAINE_DEFAUT))) {
                    adressesCurrentType.put(AdresseService.CS_DOMAINE_DEFAUT, data);
                }

                // on ajoute les adresses trouvées pour ce type
                if (adressesCurrentType.size() > 0) {
                    adressesFound_kTypeAdr.put(type, adressesCurrentType);
                }

            }

        }

        // on parse les adresses trouvées pour retourner celle dont on veut en
        // priorité
        // adressesFound contient tous les adresses standards et applic définie
        // (paramètre idApplication)
        // trouvées dans les types définis (param orderType)
        String keyTypeAdr = "";
        String keyAppliAdr = "";

        if (modePriority == AdresseService.MODE_CASC_PRIORITY_TYPE) {
            // on prend les adresses selon ordre des types choisis
            for (String type : orderTypeAdresse) {
                // dès qu'on trouve des adresses correspondantes à un type, on
                // choisit parmi ces deux adresses, si adresse applic => on
                // prend, sinon celle défaut
                if (adressesFound_kTypeAdr.containsKey(type)) {
                    keyTypeAdr = type;
                    if ((adressesFound_kTypeAdr.get(type)).containsKey(idApplication)) {
                        keyAppliAdr = idApplication;
                    } else {
                        keyAppliAdr = AdresseService.CS_DOMAINE_DEFAUT;
                    }
                    break;
                }
            }
        }

        if (modePriority == AdresseService.MODE_CASC_PRIORITY_DOMAINE) {
            // compteur sur le nombre de type ayant une adresse quelconque
            // (standard ou applic définie), pour savoir quel est le premier
            // type d'adresse contenant une adresse standard
            int cptTypeOk = 0;

            // on prend les adresses selon ordre des types choisis
            for (String type : orderTypeAdresse) {
                // dès qu'on trouve des adresses correspondantes à un type, on
                // choisit on prend celle de l'applic, si elle existe pas, on
                // passe au type suivant
                if (adressesFound_kTypeAdr.containsKey(type)) {
                    cptTypeOk++;
                    // on mémorise le premier type contenant une adresse std ou
                    // applic
                    if (cptTypeOk == 1) {
                        keyTypeAdr = type;
                    }
                    keyAppliAdr = "";
                    // si on trouve adresse du domaine applic, on écrase aussi
                    // le type (car sinon sera tjrs le premier type ayant une
                    // adresse)
                    if ((adressesFound_kTypeAdr.get(type)).containsKey(idApplication)) {
                        keyTypeAdr = type;
                        keyAppliAdr = idApplication;
                        break;

                    }

                }
            }
            // si on a pas trouvé d'adresse avec notre domaine voulu, on défini
            // qu'on prend le domaine standard
            if (JadeStringUtil.isBlankOrZero(keyAppliAdr)) {
                keyAppliAdr = AdresseService.CS_DOMAINE_DEFAUT;
            }
        }
        // si on a pas de type défini,c'est qu'on a rien trouvé dans les
        // adresses nous intéressant
        if (JadeStringUtil.isBlankOrZero(keyTypeAdr)) {
            throw new PyxisException(
                    "AdresseServiceImpl#getAdresseTiersCustomCascade:aucune adresse trouvé pour ce tiers" + idTiers);
        }

        // on a récupéré les clés type, domaine de l'adresse voulu
        TIAbstractAdresseData bestAdresse = ((adressesFound_kTypeAdr.get(keyTypeAdr)).get(keyAppliAdr));

        /*
         * Formatage de l'adresse
         */

        TIAdresseDataSource dataSource = new TIAdresseDataSource();
        dataSource.setLangue(null);
        dataSource.load(bestAdresse, "");

        AdresseTiersDetail result = new AdresseTiersDetail();
        if (dataSource != null) {
            result.setAdresseFormate(new TIAdresseFormater().format(dataSource));
            rechercheFields(result, TIAdresseDataSource.class, dataSource);
        }

        return result;

    }

    /**
     * Renvoie true si le tiers a au moin une adresse de paiement pour le doamine et la date passé la date doit être au
     * format jj.mm.aaaa le domaine est un code system de la famille PYAPPLICAT
     */
    @Override
    public Boolean hasAdressePaiement(String idTiers, String idApplication, String date)
            throws JadePersistenceException, JadeApplicationException {
        Collection<String> inApplication = new ArrayList<String>();
        inApplication.add(idApplication);
        TICascadeDomaineByDomaineManager cascadesDomaineManger = new TICascadeDomaineByDomaineManager();
        try {
            cascadesDomaineManger.setForCsDomaineClef(idApplication);
            cascadesDomaineManger.find();
            for (int i = 0; i < cascadesDomaineManger.getSize(); i++) {
                inApplication.add(((TICascadeDomaineByDomaine) cascadesDomaineManger.get(i)).getCsDomaine());
            }
        } catch (Exception e) {
            JadeLogger.warn(TIApplication.class,
                    "AdresseServiceImpl hasAdressePaiement got an Exception : " + e.toString());
        } // catch

        AvoirPaiementSearchSimpleModel searchModel = new AvoirPaiementSearchSimpleModel();
        searchModel.setForDate(date);
        searchModel.setInIdApplication(inApplication);
        searchModel.setForIdTiers(idTiers);
        int nbRecords = JadePersistenceManager.count(searchModel);
        return new Boolean(nbRecords > 0);
    }

    /**
     * Chargement d'une localité avec son id
     * 
     * @author sce (pc)
     * @param idLocalite
     * @return la localite correspondant a l'id passé en parametre
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    @Override
    public LocaliteSimpleModel readLocalite(String idLocalite) throws JadePersistenceException,
    JadeApplicationException {
        if (JadeStringUtil.isEmpty(idLocalite)) {
            throw new PyxisException(
                    "AdresseServiceImpl#readlocalite: Unable to find localite, the id passed is null empty");
        }
        LocaliteSimpleModel localite = new LocaliteSimpleModel();
        localite.setId(idLocalite);
        return (LocaliteSimpleModel) JadePersistenceManager.read(localite);
    }

    /**
     * Recherche tous les fields d'une classe et les ajoute dasn le champ fields de la objet AdresseTiersDetail
     * 
     * @param adresseTiersDetail
     *            : objet à remplir
     * @param facadeImplementationClass
     *            : la class d'implémentation
     * @param dataSource
     *            : les données
     */
    private void rechercheFields(AdresseTiersDetail adresseTiersDetail, Class<?> facadeImplementationClass,
            TIAbstractAdresseDataSource dataSource) {
        // Récupère les fields de la classe
        Field[] fieldsClass = facadeImplementationClass.getFields();
        HashMap<String, String> fields = new HashMap<String, String>();
        for (int i = 0; i < fieldsClass.length; i++) {
            Field field = fieldsClass[i];
            int compte = Modifier.PUBLIC + Modifier.STATIC + Modifier.FINAL;
            if (field.getModifiers() == compte) {
                try {
                    fields.put((String) field.get(dataSource), dataSource.getData().get(field.get(dataSource)));
                } catch (IllegalArgumentException e) {
                    JadeLogger.warn(this,
                            "IllegalArgumentException -> Erreur dans la rechreche d 'un champ : " + field.getName());
                } catch (IllegalAccessException e) {
                    JadeLogger.warn(this,
                            "IllegalAccessException -> Erreur dans la rechreche d 'un champ : " + field.getName());
                } // catch
            } // if
        } // for
        adresseTiersDetail.setFields(fields);
    } // rechercheFields

    @Override
    public AdresseSearch searchAdresseWithSimpleTiers(AdresseSearch adresseSearch) throws JadePersistenceException,
    JadeApplicationException {
        return (AdresseSearch) JadePersistenceManager.search(adresseSearch);
    }

} // class AdresseServiceImpl