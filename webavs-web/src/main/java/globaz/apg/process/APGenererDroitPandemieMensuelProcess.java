package globaz.apg.process;

import com.google.common.collect.Lists;
import globaz.apg.ApgServiceLocator;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.api.prestation.IAPRepartitionPaiements;
import globaz.apg.db.droits.*;
import globaz.apg.db.liste.APListePrestationsMensuellesModel;
import globaz.apg.db.prestation.*;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APPandemieServiceCalcul;
import globaz.apg.excel.APListeDroitPrestationMensuelExcel;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Process qui créé des prestations pour les droits avec des périodes sans date de fin
 * pour les genres de service 400 et 402
 *
 */

public class APGenererDroitPandemieMensuelProcess extends BProcess {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(APGenererDroitPandemieMensuelProcess.class);

    public final static String LABEL_TITRE_DOCUMENT = "DOC_LIST_PRESTATIONS_MENSUELLES_PA";
    public final static String NUM_INFOROM = "5046PAP";

    private static final String PROCESS_DATE_FIN_MANDATORY = "PROCESS_DATE_FIN_MANDATORY";
    private static final String PROCESS_PERIODE_PAYEE_MANDATORY = "PROCESS_PERIODE_PAYEE_MANDATORY";
    private static final String PROCESS_CALCUL_PRESTATION_DATES_DEBUT_MANDATORY = "PROCESS_CALCUL_PRESTATION_DATES_DEBUT_MANDATORY";
    private static final String PROCESS_CALCUL_PRESTATION_DATES_DEPART_ARRIVEE_MANDATORY = "PROCESS_CALCUL_PRESTATION_DATES_DEPART_ARRIVEE_MANDATORY";
    public static final String PROCESS_CALCUL_PRESTATION_MENSUELLE_PROVISOIRE_FAILED = "PROCESS_CALCUL_PRESTATION_MENSUELLE_PROVISOIRE_FAILED";
    public static final String PROCESS_CALCUL_PRESTATION_MENSUELLE_PROVISOIRE_SUCCESS = "PROCESS_CALCUL_PRESTATION_MENSUELLE_PROVISOIRE_SUCCESS";
    public static final String PROCESS_CALCUL_PRESTATION_MENSUELLE_DEFINITIF_FAILED = "PROCESS_CALCUL_PRESTATION_MENSUELLE_DEFINITIF_FAILED";
    public static final String PROCESS_CALCUL_PRESTATION_MENSUELLE_DEFINITIF_SUCCESS = "PROCESS_CALCUL_PRESTATION_MENSUELLE_DEFINITIF_SUCCESS";

    private String dateDepart = "";
    private String dateArrivee = "";
    private String dateFin = "";
    private String idDroit = "";
    private String categorie = "";
    private Boolean isDefinitif = null;

    private APPandemieServiceCalcul genreService;

    private List<APDroitLAPG> listDroit;

    private List<APListePrestationsMensuellesModel> listPrestation = new ArrayList<>();

    private boolean finPandemie = false;

    private String errors = "";

    /**
     * Crée une nouvelle instance de la classe APGenererDroitPandemieMensuelProcess.
     */
    public APGenererDroitPandemieMensuelProcess() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APGenererDroitPandemieMensuelProcess.
     *
     * @param parent
     */
    public APGenererDroitPandemieMensuelProcess(BProcess parent) {
        super(parent);
    }

    /**
     * Crée une nouvelle instance de la classe APGenererDroitPandemieMensuelProcess.
     *
     * @param session
     */
    public APGenererDroitPandemieMensuelProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
        if(!isDefinitif.booleanValue() || !JadeStringUtil.isEmpty(errors)) {
            try {
                getTransaction().rollback();
            } catch (Exception e) {
                LOG.error("APGenererDroitPandemieMensuelProcess#_executeCleanUp : erreur lors du rollback de la transaction", e);
            }
        }
    }

    @Override
    protected boolean _executeProcess() throws Exception {

        try {
            if(!JadeStringUtil.isEmpty(idDroit)) {
                setDateFinDroit();
            } else if(!JAUtil.isDateEmpty(dateDepart) && !JAUtil.isDateEmpty(dateArrivee)) {
                calculDateArriveeDepart();
                printList();
            } else if(!JAUtil.isDateEmpty(dateFin)) {
                finPandemie = true;
                calculDateFin();
                printList();
            }

        } catch (Exception e) {
            try {
                getTransaction().rollback();
            } catch (Exception e1) {
                LOG.error("Une erreur est intervenue lors du traitement APGenererDroitPandemieMensuelProcess : " + e.getMessage(), e);
            }

            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, e.getMessage());

            return false;
        }

        return true;
    }

    private void printList() throws IOException {
        APListeDroitPrestationMensuelExcel listeExcel = new APListeDroitPrestationMensuelExcel(getSession());
        listeExcel.setList(listPrestation);
        listeExcel.creerDocument();
        JadePublishDocumentInfo docInfoExcel= JadePublishDocumentInfoProvider.newInstance(this);
        docInfoExcel.setDocumentTypeNumber(NUM_INFOROM);
        docInfoExcel.setPublishDocument(true);
        docInfoExcel.setArchiveDocument(false);
        JadePublishDocument documentAPublier = new JadePublishDocument(listeExcel.getOutputFile(),docInfoExcel);
        this.registerAttachedDocument(documentAPublier);
    }

    private void setDateFinDroit() throws Exception {
        dateArrivee=dateFin;
        dateDepart=JadeDateUtil.getFirstDateOfMonth(dateFin);
        listDroit = new ArrayList<>();
        APDroitLAPGManager manager = new APDroitLAPGManager();
        manager.setForIdDroit(idDroit);
        manager.setSession(getSession());
        manager.find(BManager.SIZE_USEDEFAULT);

        if(manager.size() > 0) {
            listDroit.add((APDroitLAPG)manager.getFirstEntity());
            calculDate(listDroit);
            updateLastPeriode();
        }

    }

    private void calculDateFin() throws Exception {
        dateArrivee=dateFin;
        dateDepart=JadeDateUtil.getFirstDateOfMonth(dateFin);
        calculDateArriveeDepart();
        if(!listDroit.isEmpty()) {
            updateLastPeriode();
        }
    }

    private void calculDateArriveeDepart() throws Exception {
        listDroit = getListDroit();
        if(!listDroit.isEmpty()) {
            setProgressScaleValue(listDroit.size());
            calculDate(listDroit);
            if(!isDefinitif.booleanValue()) {
                try {
                    getTransaction().rollback();
                } catch (Exception e) {
                    LOG.error("APGenererDroitPandemieMensuelProcess#_executeCleanUp : erreur lors du rollback de la transaction", e);
                }
            } else {
                try {
                    if (getTransaction().hasErrors()) {
                        getTransaction().rollback();
                        LOG.error("APGenererDroitPandemieMensuelProcess#_executeCleanUp : erreur lors du commit de la transaction", getTransaction().getErrors());
                    } else {
                        getTransaction().commit();
                    }
                } catch (Exception e) {
                    LOG.error("APGenererDroitPandemieMensuelProcess#_executeCleanUp : erreur lors du commit de la transaction", e);
                }
            }
        }
    }

    private void calculDate(List<APDroitLAPG> listDroit) throws Exception {
        Map<String, List<APPrestationJointLotTiersDroit>> map = getListPrestation(listDroit);
        for (Map.Entry<String, List<APPrestationJointLotTiersDroit>> entry : map.entrySet()) {
            boolean dejaPeriode = false;
            APPrestationJointLotTiersDroit prestationMois = null;
            LOG.info(getProgressCounter() + 1 + " - Traitement droit  : " + entry.getKey());
            for (APPrestationJointLotTiersDroit prestation : entry.getValue()) {
                if (isPrestationDejaCalcule(prestation)) {
                    dejaPeriode = true;
                } else if (isPrestationisPrestationDansMois(prestation)) {
                    prestationMois = prestation;
                } else if (finPandemie && isDateFuture(prestation)) {
                    // Si date Fin Pandémie supprimer les périodes des mois futures
                    prestation.delete();
                    addModel(prestation, prestation, APListeDroitPrestationMensuelExcel.ACTION_SUPPRIME);
                }
            }
            if (!dejaPeriode) {
                APPrestation newPrestation;
                String action = "";
                if (prestationMois != null) {
                    if(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestationMois.getEtat())
                            && JadeDateUtil.isDateBefore(prestationMois.getDateFin(), dateDepart)) {
                        newPrestation = ajoutePrestationDroit(prestationMois);
                        action = APListeDroitPrestationMensuelExcel.ACTION_AJOUTE;
                    } else {
                        newPrestation = modifyPrestationDroit(prestationMois);
                        action = APListeDroitPrestationMensuelExcel.ACTION_MODIFIE;
                    }
                } else {
                    prestationMois = getLastPrestation(entry.getValue());
                    newPrestation = ajoutePrestationDroit(prestationMois);
                    action = APListeDroitPrestationMensuelExcel.ACTION_AJOUTE;
                }
                addModel(prestationMois, newPrestation, action);
            }

            incProgressCounter();
            if (isAborted()) {
                break;
            }
            if(getTransaction().hasErrors()) {
                errors = getTransaction().getErrors().toString();
                break;
            }
        }
    }

    private boolean isDateFuture(APPrestation prestation) {
        return JadeDateUtil.isDateAfter(prestation.getDateDebut(), dateFin);
    }

    private boolean isPrestationDejaCalcule(APPrestation prestation){
        return dateDepart.equals(prestation.getDateDebut()) && dateArrivee.equals(prestation.getDateFin());
    }

    private boolean isPrestationisPrestationDansMois(APPrestation prestation){
        return dateDepart.substring(3).equals(prestation.getDateDebut().substring(3));
    }

    private APPrestationJointLotTiersDroit getLastPrestation(List<APPrestationJointLotTiersDroit> listPrestation){
        APPrestationJointLotTiersDroit last = null;
        for(APPrestationJointLotTiersDroit prestation : listPrestation){
            if(last == null || JadeDateUtil.getGlobazDate(prestation.getDateDebut()).after(JadeDateUtil.getGlobazDate(last.getDateDebut()))) {
                last = prestation;
            }
        }
        return last;
    }

    private APPrestation ajoutePrestationDroit(APPrestationJointLotTiersDroit prestationToCopy) throws Exception {
        if(prestationToCopy != null) {
            APPrestation prestation = new APPrestation(prestationToCopy);
            prestation.setDatePaiement("");
            prestation.setIdLot("");
            prestation.setIdPrestationApg(null);
            prestation.setEtat(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
            prestation.populateSpy(null);
            prestation.add();
            prestation.setDateDebut(dateDepart);
            setValeursPrestation(prestation);
            if(copyRepartition(prestationToCopy.getIdPrestationApg(), prestation.getIdPrestationApg())) {
                prestation.update();
                return prestation;
            }
        }
        return null;
    }

    private APPrestation modifyPrestationDroit(APPrestation prestation) throws Exception {
        if(!IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestation.getEtat())) {
            if (finPandemie
                    && !JadeStringUtil.isEmpty(prestation.getDateFin())
                    && JadeDateUtil.isDateAfter(dateArrivee, prestation.getDateFin())) {
                // Ne pas modifier si déjà une date de fin et que date de fin pandémie aprés date de fin prestation
                return null;
            }
            if (JadeDateUtil.isDateBefore(prestation.getDateDebut(), dateDepart)) {
                prestation.setDateDebut(dateDepart);
            }

            setValeursPrestation(prestation);
            prestation.update();
            return prestation;
        }
        return null;
    }

    private void setValeursPrestation(APPrestation prestation) {
        prestation.setDateFin(dateArrivee);
        Integer nbjourSoldes = JadeDateUtil.getNbDaysBetween(dateDepart, dateArrivee) + 1;
        prestation.setNombreJoursSoldes(nbjourSoldes.toString());
        BigDecimal mb = new BigDecimal(prestation.getMontantJournalier());
        prestation.setMontantBrut(mb.multiply(new BigDecimal(nbjourSoldes)).toString());
        prestation.setIsModifiedByUser(true);
        prestation.wantMiseAJourRepartitions(true);
    }

    private boolean copyRepartition(String oldId, String newId) throws Exception {
        APRepartitionPaiementsManager repartitions = new APRepartitionPaiementsManager();
        APCotisationManager cotisations = new APCotisationManager();

        cotisations.setSession(getSession());

        repartitions.setForIdPrestation(oldId);
        repartitions.setParentOnly(true);
        repartitions.setForTypePrestation(IAPRepartitionPaiements.CS_NORMAL);
        repartitions.setSession(getSession());
        repartitions.find(BManager.SIZE_NOLIMIT);

        for (int idRepartion = 0; idRepartion < repartitions.size(); ++idRepartion) {
            APRepartitionPaiements repartition = (APRepartitionPaiements) repartitions.get(idRepartion);

            if(JadeStringUtil.isEmpty(repartition.getIdRepartitionBeneficiairePaiement())) {
                LOG.error("Répartition sans ID pour la prestation : "+oldId);
                return false;
            }
            String oldIdRepartition = repartition.getIdRepartitionBeneficiairePaiement();
            repartition.loadSituationProfessionnelle();

            repartition.setIdPrestationApg(newId);
            repartition.setIdRepartitionBeneficiairePaiement(null);
            repartition.setId(null);
            repartition.populateSpy(null);
            repartition.add();

            cotisations.setForIdRepartitionBeneficiairePaiement(oldIdRepartition);
            cotisations.find(BManager.SIZE_NOLIMIT);
            for (int idCotisation = 0; idCotisation < cotisations.size(); ++idCotisation) {
                APCotisation cotisation = (APCotisation) cotisations.get(idCotisation);
                cotisation.setIdRepartitionBeneficiairePaiement(repartition.getIdRepartitionBeneficiairePaiement());
                cotisation.setId(null);
                cotisation.populateSpy(null);
                cotisation.add();
            }
        }
        return true;
    }

    private List<APDroitLAPG> getListDroit() throws Exception {
        APDroitPandemieDateFinManager manager = new APDroitPandemieDateFinManager();
        switch (genreService) {
            case INDEPENDANT_AVEC_MANIFESTATION:
                manager.setForGenreService(APGenreServiceAPG.IndependantPandemie.getCodeSysteme());
                manager.setForManifestationAnnulee(true);
                break;
            case INDEPENDANT_SANS_MANIFESTATION:
                manager.setForGenreService(APGenreServiceAPG.IndependantPandemie.getCodeSysteme());
                manager.setForManifestationAnnulee(false);
                if(!JadeStringUtil.isEmpty(categorie)) {
                    manager.setForCategorie(categorie);
                }
                break;
            case GARDE_PARENTAL:
                manager.setForGenreService(APGenreServiceAPG.GardeParentale.getCodeSysteme());
                break;
            case GARDE_PARENTAL_HANDICAP:
                manager.setForGenreService(APGenreServiceAPG.GardeParentaleHandicap.getCodeSysteme());
                break;
            case INDEPENDANT_MANIFESTATION_ANNULEE:
                manager.setForGenreService(APGenreServiceAPG.IndependantManifAnnulee.getCodeSysteme());

                break;
            default:
                return new ArrayList<>();
        }
        if(finPandemie) {
            manager.setForDateDeFin(dateFin);
        } else {
            manager.setSansDateDeFin(true);
        }

        manager.setSession(getSession());
        manager.find(BManager.SIZE_NOLIMIT);

        if(manager.size() > 0) {
            List<APDroitLAPG> list = manager.getContainerAsList().stream()
                    .map(obj -> (APDroitLAPG) obj)
                    .collect(Collectors.toList());
            return list;
        }

        return new ArrayList<>();
    }

    private Map<String, List<APPrestationJointLotTiersDroit>> getListPrestation(List<APDroitLAPG> listDroit) throws Exception {
        List<String> listId = listDroit.stream().map(APDroitLAPG::getIdDroit).collect(Collectors.toList());
        APPrestationJointLotTiersDroitManager manager = new APPrestationJointLotTiersDroitManager();
        manager.setSession(getSession());
        manager.setForIdDroitIn(listId);
        manager.setForTypeDifferentDe(IAPPrestation.CS_TYPE_ANNULATION);
        manager.find(BManager.SIZE_NOLIMIT);

        Map<String, List<APPrestationJointLotTiersDroit>> map = new HashMap<>();
        if(manager.size() > 0) {
            List<APPrestationJointLotTiersDroit> list = manager.getContainerAsList().stream()
                    .map(obj -> (APPrestationJointLotTiersDroit) obj)
                    .collect(Collectors.toList());
            for (APPrestationJointLotTiersDroit ap : list){
                if(!JadeStringUtil.isBlankOrZero(ap.getMontantBrut())
                        && (new FWCurrency(ap.getMontantBrut())).isPositive()
                        && !IAPPrestation.CS_ETAT_PRESTATION_ANNULE.equals(ap.getEtat())) {
                    map.computeIfAbsent(ap.getIdDroit(), val -> new ArrayList<>()).add(ap);
                }
            }
        }

        return map;
    }


    private Map<String, List<APPrestationJointLotTiersDroit>> getListPrestation(int maxIn) throws Exception {
        List<String> listId = listDroit.stream().map(APDroitLAPG::getIdDroit).collect(Collectors.toList());
        List<List<String>> splitLists = Lists.partition(listId, maxIn);
        Map<String, List<APPrestationJointLotTiersDroit>> map = new HashMap<>();
        for(List<String> splitList: splitLists) {
            APPrestationJointLotTiersDroitManager manager = new APPrestationJointLotTiersDroitManager();
            manager.setSession(getSession());
            manager.setForIdDroitIn(splitList);
            manager.setForTypeDifferentDe(IAPPrestation.CS_TYPE_ANNULATION);
            manager.find(BManager.SIZE_NOLIMIT);

            if (manager.size() > 0) {
                List<APPrestationJointLotTiersDroit> list = manager.getContainerAsList().stream()
                        .map(obj -> (APPrestationJointLotTiersDroit) obj)
                        .collect(Collectors.toList());
                for (APPrestationJointLotTiersDroit ap : list) {
                    if (!JadeStringUtil.isBlankOrZero(ap.getMontantBrut())
                            && (new FWCurrency(ap.getMontantBrut())).isPositive()
                            && !IAPPrestation.CS_ETAT_PRESTATION_ANNULE.equals(ap.getEtat())) {
                        map.computeIfAbsent(ap.getIdDroit(), val -> new ArrayList<>()).add(ap);
                    }
                }
            }
        }

        return map;
    }

    private void updateLastPeriode() throws Exception {
        for(APDroitLAPG droit : listDroit) {
            updateDroit(droit);
            updatePeriode(getLastPerdideOfDroit(droit.getIdDroit()));
        }
    }

    private APPeriodeComparable getLastPerdideOfDroit(String idDroit) throws Exception {
        APPeriodeAPGManager mgr = new APPeriodeAPGManager();

        mgr.setSession(getSession());
        mgr.setForIdDroit(idDroit);
        mgr.find(getSession().getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);

        List<APPeriodeComparable> listPeriode = mgr.toList().stream().map(obj -> (APPeriodeAPG) obj).map(ap -> new APPeriodeComparable(ap)).collect(Collectors.toList());
        Collections.sort(listPeriode);
        return listPeriode.get(listPeriode.size()-1);
    }

    private void updateDroit(APDroitLAPG droit) throws Exception {
        if(JadeStringUtil.isEmpty(droit.getDateFinDroit())) {
            droit.setDateFinDroit(dateFin);
            droit.update();
        }
    }

    private void updatePeriode(APPeriodeComparable periode) throws Exception {
        if(JadeStringUtil.isEmpty(periode.getDateFinPeriode())) {
            periode.getOri().setDateFinPeriode(dateFin);
            periode.getOri().update();
        }
    }

    private void addModel(APPrestationJointLotTiersDroit prestationMois, APPrestation newPrestation, String action) throws Exception {
        if(newPrestation != null) {
            APListePrestationsMensuellesModel model = new APListePrestationsMensuellesModel();
            model.setIdDroit(newPrestation.getIdDroit());
            model.setMontant(newPrestation.getMontantBrut());
            model.setService(getSession().getCodeLibelle(prestationMois.getGenreService()));
            model.setDateDebut(newPrestation.getDateDebut());
            model.setDateFin(newPrestation.getDateFin());
            model.setNom(prestationMois.getNom()+ " "+prestationMois.getPrenom());
            model.setNss(prestationMois.getNoAVS());
            if(!JadeStringUtil.isEmpty(action)) {
                model.setAction(getSession().getLabel(action));
            }
            checkIsIndependant(model);
            listPrestation.add(model);
        }
    }

    private void checkIsIndependant(APListePrestationsMensuellesModel model) throws Exception {
        if(APPandemieServiceCalcul.GARDE_PARENTAL.equals(genreService)
                || APPandemieServiceCalcul.GARDE_PARENTAL_HANDICAP.equals(genreService)){
            if(isIndependant(model.getIdDroit())) {
                model.setIndependant(true);
            }
        }
    }

    private boolean isIndependant(String idDroit) throws Exception {
        List<APSitProJointEmployeur> list = ApgServiceLocator.getEntityService().getSituationProfJointEmployeur(getSession(), null, idDroit);
        for (APSitProJointEmployeur employeur : list) {
            if(employeur.getIndependant()){
                return true;
            }
        }
        return false;
    }


    @Override
    protected void _validate() throws Exception {

        if(idDroit != null) {
            if (JAUtil.isDateEmpty(dateFin)) {
                getSession().addError(getSession().getLabel(PROCESS_DATE_FIN_MANDATORY));
            }

            APPrestationManager manager = new APPrestationManager();
            manager.setSession(getSession());
            manager.setForIdDroit(idDroit);
            manager.find(BManager.SIZE_NOLIMIT);

            Map<String, List<APPrestation>> map = new HashMap<>();
            if(manager.size() > 0) {
                List<APPrestation> list = manager.getContainerAsList().stream()
                        .map(obj -> (APPrestation) obj)
                        .collect(Collectors.toList());
                for (APPrestation prestation : list){
                    if((IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF.equals(prestation.getEtat())
                            || !JadeStringUtil.isBlankOrZero(prestation.getIdLot()))
                            && JadeDateUtil.isDateBefore(dateFin, prestation.getDateFin())) {
                        getSession().addError(getSession().getLabel(PROCESS_PERIODE_PAYEE_MANDATORY));
                    }
                }
            }
        } else {
            if ((!JAUtil.isDateEmpty(dateArrivee) && JAUtil.isDateEmpty(dateDepart))
                    || (JAUtil.isDateEmpty(dateArrivee) && !JAUtil.isDateEmpty(dateDepart))) {
                getSession().addError(getSession().getLabel(PROCESS_CALCUL_PRESTATION_DATES_DEPART_ARRIVEE_MANDATORY));
            } else if (JAUtil.isDateEmpty(dateArrivee) && JAUtil.isDateEmpty(dateDepart) && JAUtil.isDateEmpty(dateFin)) {
                getSession().addError(getSession().getLabel(PROCESS_CALCUL_PRESTATION_DATES_DEBUT_MANDATORY));
            } else if (!JAUtil.isDateEmpty(dateArrivee) && !JAUtil.isDateEmpty(dateDepart) && !JAUtil.isDateEmpty(dateFin)) {
                getSession().addError(getSession().getLabel(PROCESS_CALCUL_PRESTATION_DATES_DEBUT_MANDATORY));
            }
            if (!JAUtil.isDateEmpty(dateArrivee) && !JadeDateUtil.isGlobazDate(dateArrivee)) {
                getSession().addError(getSession().getLabel(PROCESS_CALCUL_PRESTATION_DATES_DEBUT_MANDATORY));
            }

            if (!JAUtil.isDateEmpty(dateDepart) && !JadeDateUtil.isGlobazDate(dateDepart)) {
                getSession().addError(getSession().getLabel(PROCESS_CALCUL_PRESTATION_DATES_DEBUT_MANDATORY));
            }

            if (JadeDateUtil.isDateBefore(dateArrivee, dateDepart)) {
                getSession().addError(getSession().getLabel(PROCESS_CALCUL_PRESTATION_DATES_DEPART_ARRIVEE_MANDATORY));
            }

            if (!JAUtil.isDateEmpty(dateFin) && !JadeDateUtil.isGlobazDate(dateFin)) {
                getSession().addError(getSession().getLabel(PROCESS_CALCUL_PRESTATION_DATES_DEBUT_MANDATORY));
            }
        }

    }

    @Override
    protected String getEMailObject() {
        if(!JadeStringUtil.isEmpty(errors)){
            getMemoryLog().logMessage(errors, FWMessage.ERREUR, errors);
        }

        if (getMemoryLog().isOnErrorLevel() || getMemoryLog().isOnFatalLevel()) {
            if(isDefinitif) {
                return getSession().getLabel(PROCESS_CALCUL_PRESTATION_MENSUELLE_DEFINITIF_FAILED);
            } else {
                return getSession().getLabel(PROCESS_CALCUL_PRESTATION_MENSUELLE_PROVISOIRE_FAILED);
            }
        } else {
            if(isDefinitif) {
                return getSession().getLabel(PROCESS_CALCUL_PRESTATION_MENSUELLE_DEFINITIF_SUCCESS);
            } else {
                return getSession().getLabel(PROCESS_CALCUL_PRESTATION_MENSUELLE_PROVISOIRE_SUCCESS);
            }
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public String getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }

    public String getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(String dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public APPandemieServiceCalcul getGenreService() {
        return genreService;
    }

    public void setGenreService(APPandemieServiceCalcul genreService) {
        this.genreService = genreService;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setIsDefinitif(final Boolean boolean1) {
        isDefinitif = boolean1;
    }
    public Boolean getIsDefinitif() {
        return isDefinitif;
    }
}
