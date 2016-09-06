/*
 * Créé le 11 juin 07
 */

package globaz.prestation.external;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.prof.JadeProfiler;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.beans.PRTiersDateTime;
import globaz.prestation.db.demandes.PRDemandeManager;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationAPI;
import globaz.prestation.process.PRGenererModificationsTiersProcess;
import globaz.prestation.process.TypeModificationsTiers;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressecourrier.TIAdresseViewBean;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresse;
import globaz.pyxis.db.adressecourrier.TIAvoirAdresseViewBean;
import globaz.pyxis.db.adressecourrier.TILocalite;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HPE
 * 
 *         ExternalService appelé lors de la modification sur le viewBean TITiersViewBean pour gérer certains problèmes
 *         lors de modifications de tiers.
 * 
 */
public class ExternalServiceTiersViewBean extends BAbstractEntityExternalService {

    private static final String SEXE_HOMME_MODIFICATION_TIERS = "SEXE_HOMME_MODIFICATION_TIERS";
    private static final String SEXE_FEMME_MODIFICATION_TIERS = "SEXE_FEMME_MODIFICATION_TIERS";
    private static final String ALREADY_SENT_A_MAIL_FOR_TIERS = "alreadySentAMailForTiers";
    private static final String TIERS_AVANT_MODIFICATION = "ExternalServiceTiersViewBean_TiersAvantModification";

    /**
     * Constructeur du type ExternalServiceTiersViewBean.
     */
    public ExternalServiceTiersViewBean() {
        super();
    }

    @Override
    public void afterAdd(BEntity entity) throws Throwable {

        String oldIdAdresse = "";

        if (entity instanceof TIAdresseViewBean) {
            oldIdAdresse = ((TIAdresseViewBean) entity).getOldIdAdresse();
        }

        if ((entity instanceof TIAdresseViewBean && !JadeNumericUtil.isEmptyOrZero(oldIdAdresse))
                || (entity instanceof TIAvoirAdresseViewBean && JadeNumericUtil.isEmptyOrZero(oldIdAdresse))) {

            String idTiers;
            if (entity instanceof TIAdresseViewBean) {
                idTiers = ((TIAdresseViewBean) entity).getIdTiers();
            } else {
                idTiers = ((TIAvoirAdresseViewBean) entity).getIdTiers();
            }

            if (hasTiersDemandeApiInvaliditeEnCours(entity.getSession(), idTiers)) {

                BSession sessionTiers = entity.getSession();

                TIAdresse ancienneAdresse;
                TypeModificationsTiers typeModification;
                if (JadeNumericUtil.isEmptyOrZero(oldIdAdresse)) {
                    ancienneAdresse = new TIAdresse();
                    typeModification = TypeModificationsTiers.AJOUT_ADRESSE;
                } else {
                    ancienneAdresse = loadAdresse(sessionTiers, oldIdAdresse);
                    typeModification = TypeModificationsTiers.MODIFICATION_ADRESSE;
                }

                TIAdresse nouvelleAdresse;
                if (entity instanceof TIAvoirAdresseViewBean) {
                    nouvelleAdresse = loadAdresse(sessionTiers, ((TIAvoirAdresseViewBean) entity).getIdAdresse());
                } else {
                    nouvelleAdresse = (TIAdresse) entity;
                }

                TILocalite ancienneLocalite = loadLocalite(sessionTiers, ancienneAdresse.getIdLocalite());
                TILocalite nouvelleLocalite = loadLocalite(sessionTiers, nouvelleAdresse.getIdLocalite());

                AdresseModificationsHandler modificationsAdresse = checkModificationAdresse(ancienneAdresse,
                        nouvelleAdresse, ancienneLocalite, nouvelleLocalite);

                if (modificationsAdresse.hasModifications()) {
                    BSession sessionCorvus = getSessionCorvus();

                    TITiersViewBean tiersViewBean = loadTiers(sessionTiers, idTiers);

                    PRGenererModificationsTiersProcess process = new PRGenererModificationsTiersProcess();
                    process.setSession(sessionCorvus);

                    initTiersDataAddressChanges(tiersViewBean, process);

                    process.setIdCantonDomicile(ancienneLocalite.getIdCanton());
                    process.setNewIdCantonDomicile(nouvelleLocalite.getIdCanton());

                    TIPays paysAncienneLocalite = loadPays(sessionTiers, ancienneLocalite.getIdPays());
                    process.setPaysDomicile(paysAncienneLocalite);

                    TIPays paysNouvelleLocalite = loadPays(sessionTiers, nouvelleLocalite.getIdPays());
                    process.setNewPaysDomicile(paysNouvelleLocalite);

                    process.setContainerModificationAdresse(modificationsAdresse);

                    process.setCommunicationOAI(true);
                    process.setTypeModification(typeModification);
                    process.setIdTier(tiersViewBean.getIdTiers());
                    process.setUser(entity.getSession().getUserFullName());
                    process.setDateModification(JACalendar.todayJJsMMsAAAA());
                    process.setHeureModification(JACalendar.formatTime(JACalendar.now()));

                    Map<String, PRTiersDateTime> mapsTiers = (Map<String, PRTiersDateTime>) entity.getSession()
                            .getAttribute(ALREADY_SENT_A_MAIL_FOR_TIERS);

                    PRTiersDateTime tiersDateTime = getTiersDateTime(entity, idTiers, mapsTiers);

                    // Si pas d'id tiers dans cette session, c est que l'on n'a pas fait de traitement dans
                    // l'updateTiers ou qu'il s'est déjà passé plus de quelques secondes, alors nous pouvons envoyé un
                    // mail de modification
                    if (tiersDateTime == null || !tiersDateTime.isDateBetween0To5Seconds(JadeDateUtil.getCurrentTime())) {
                        String idGroupe = sessionCorvus.getApplication().getProperty(
                                REApplication.PROPERTY_GROUPE_COMMUNICATION_OAI_MODIFICATION_ADRESSE_TIERS);
                        process.setIdGroupeNotification(idGroupe);

                        setAttributeForMailSent(entity, tiersViewBean);
                    }

                    entity.getSession().removeAttribute(ExternalServiceTiersViewBean.TIERS_AVANT_MODIFICATION);
                    process.start();
                }
            }
        }
    }

    private PRTiersDateTime getTiersDateTime(BEntity entity, String idTiers, Map<String, PRTiersDateTime> mapsTiers) {
        PRTiersDateTime data = null;

        if (mapsTiers != null) {
            data = mapsTiers.get(idTiers);

            if (data != null) {
                mapsTiers.remove(idTiers);
                entity.getSession().setAttribute(ALREADY_SENT_A_MAIL_FOR_TIERS, mapsTiers);
            }
        }

        return data;
    }

    private void initTiersDataAddressChanges(TITiersViewBean tiersViewBean, PRGenererModificationsTiersProcess process) {
        process.setNSS(tiersViewBean.getNumAvsActuel());
        process.setNewNSS(tiersViewBean.getNumAvsActuel());

        process.setNom(tiersViewBean.getDesignation1());
        process.setNewNom(tiersViewBean.getDesignation1());

        process.setPrenom(tiersViewBean.getDesignation2());
        process.setNewPrenom(tiersViewBean.getDesignation2());

        process.setDesignation3(tiersViewBean.getDesignation3());
        process.setNewDesignation3(tiersViewBean.getDesignation3());

        process.setDesignation4(tiersViewBean.getDesignation4());
        process.setNewDesignation4(tiersViewBean.getDesignation4());

        process.setNomJeuneFille(tiersViewBean.getNomJeuneFille());
        process.setNewNomJeuneFille(tiersViewBean.getNomJeuneFille());

        process.setLangue(tiersViewBean.getLangue());
        process.setNewLangue(tiersViewBean.getLangue());

        process.setEtatCivil(tiersViewBean.getEtatCivil());
        process.setNewEtatCivil(tiersViewBean.getEtatCivil());

        process.setNationalite(tiersViewBean.getPays());
        process.setNewNationalite(tiersViewBean.getPays());

        if (PRACORConst.CS_HOMME.equals(tiersViewBean.getSexe())) {
            process.setSexe(SEXE_HOMME_MODIFICATION_TIERS);
            process.setNewSexe(SEXE_HOMME_MODIFICATION_TIERS);
        } else {
            process.setSexe(SEXE_FEMME_MODIFICATION_TIERS);
            process.setNewSexe(SEXE_FEMME_MODIFICATION_TIERS);
        }

        process.setDateNaissance(tiersViewBean.getDateNaissance());
        process.setNewDateNaissance(tiersViewBean.getDateNaissance());

        process.setDateDeces(tiersViewBean.getDateDeces());
        process.setNewDateDeces(tiersViewBean.getDateDeces());
    }

    private AdresseModificationsHandler checkModificationAdresse(TIAdresse ancienneAdresse, TIAdresse nouvelleAdresse,
            TILocalite ancienneLocalite, TILocalite nouvelleLocalite) throws Exception {

        AdresseModificationsHandler modificationsAdresse = new AdresseModificationsHandler();

        modificationsAdresse
                .setAttention(new LigneAdresseExternalService(ancienneAdresse.getAttention(), nouvelleAdresse
                        .getAttention(), !ancienneAdresse.getAttention().equals(nouvelleAdresse.getAttention())));

        modificationsAdresse.setCasePostale(new LigneAdresseExternalService(ancienneAdresse.getCasePostale(),
                nouvelleAdresse.getCasePostale(), !ancienneAdresse.getCasePostale().equals(
                        nouvelleAdresse.getCasePostale())));

        modificationsAdresse.setLigneAdresse1(new LigneAdresseExternalService(ancienneAdresse.getLigneAdresse1(),
                nouvelleAdresse.getLigneAdresse1(), !ancienneAdresse.getLigneAdresse1().equals(
                        nouvelleAdresse.getLigneAdresse1())));

        modificationsAdresse.setLigneAdresse2(new LigneAdresseExternalService(ancienneAdresse.getLigneAdresse2(),
                nouvelleAdresse.getLigneAdresse2(), !ancienneAdresse.getLigneAdresse2().equals(
                        nouvelleAdresse.getLigneAdresse2())));

        modificationsAdresse.setLigneAdresse3(new LigneAdresseExternalService(ancienneAdresse.getLigneAdresse3(),
                nouvelleAdresse.getLigneAdresse3(), !ancienneAdresse.getLigneAdresse3().equals(
                        nouvelleAdresse.getLigneAdresse3())));

        modificationsAdresse.setLigneAdresse4(new LigneAdresseExternalService(ancienneAdresse.getLigneAdresse4(),
                nouvelleAdresse.getLigneAdresse4(), !ancienneAdresse.getLigneAdresse4().equals(
                        nouvelleAdresse.getLigneAdresse4())));

        modificationsAdresse
                .setNumeroRue(new LigneAdresseExternalService(ancienneAdresse.getNumeroRue(), nouvelleAdresse
                        .getNumeroRue(), !ancienneAdresse.getNumeroRue().equals(nouvelleAdresse.getNumeroRue())));

        modificationsAdresse.setRue(new LigneAdresseExternalService(ancienneAdresse.getRue(), nouvelleAdresse.getRue(),
                !ancienneAdresse.getRue().equals(nouvelleAdresse.getRue())));

        modificationsAdresse.setRueRepertoire(new LigneAdresseExternalService(ancienneAdresse.getRueRepertoire(),
                nouvelleAdresse.getRueRepertoire(), !ancienneAdresse.getRueRepertoire().equals(
                        nouvelleAdresse.getRueRepertoire())));

        modificationsAdresse.setTitreAdresse(new LigneAdresseExternalService(ancienneAdresse.getTitreAdresse(),
                nouvelleAdresse.getTitreAdresse(), !ancienneAdresse.getTitreAdresse().equals(
                        nouvelleAdresse.getTitreAdresse())));

        modificationsAdresse
                .setLocalite(new LigneAdresseExternalService(ancienneLocalite.getLocalite(), nouvelleLocalite
                        .getLocalite(), !ancienneLocalite.getLocalite().equals(nouvelleLocalite.getLocalite())));

        modificationsAdresse.setNumPostal(new LigneAdresseExternalService(ancienneLocalite.getNumPostal(),
                nouvelleLocalite.getNumPostal(), !ancienneLocalite.getNumPostal().equals(
                        nouvelleLocalite.getNumPostal())));

        return modificationsAdresse;

    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {

        if (entity instanceof TIAvoirAdresseViewBean
                && hasTiersDemandeApiInvaliditeEnCours(entity.getSession(),
                        ((TIAvoirAdresseViewBean) entity).getIdTiers())) {

            BSession sessionTiers = entity.getSession();

            TIAdresse ancienneAdresse = loadAdresse(sessionTiers, ((TIAvoirAdresseViewBean) entity).getIdAdresse());
            TIAdresse nouvelleAdresse = new TIAdresse();

            TILocalite ancienneLocalite = loadLocalite(sessionTiers, ancienneAdresse.getIdLocalite());
            TILocalite nouvelleLocalite = new TILocalite();

            AdresseModificationsHandler modificationsAdresse = checkModificationAdresse(ancienneAdresse,
                    nouvelleAdresse, ancienneLocalite, nouvelleLocalite);

            if (modificationsAdresse.hasModifications()) {
                BSession sessionCorvus = getSessionCorvus();

                TITiersViewBean tiersViewBean = loadTiers(sessionTiers, ((TIAvoirAdresseViewBean) entity).getIdTiers());

                PRGenererModificationsTiersProcess process = new PRGenererModificationsTiersProcess();
                process.setSession(sessionCorvus);

                initTiersDataAddressChanges(tiersViewBean, process);

                process.setIdCantonDomicile(ancienneLocalite.getIdCanton());
                process.setNewIdCantonDomicile(nouvelleLocalite.getIdCanton());

                TIPays paysAncienneLocalite = loadPays(sessionTiers, ancienneLocalite.getIdPays());
                process.setPaysDomicile(paysAncienneLocalite);

                TIPays paysNouvelleLocalite = loadPays(sessionTiers, nouvelleLocalite.getIdPays());
                process.setNewPaysDomicile(paysNouvelleLocalite);

                process.setContainerModificationAdresse(modificationsAdresse);

                process.setCommunicationOAI(true);
                process.setTypeModification(TypeModificationsTiers.SUPPRESSION_ADRESSE);
                process.setIdTier(tiersViewBean.getIdTiers());
                process.setUser(entity.getSession().getUserFullName());
                process.setDateModification(JACalendar.todayJJsMMsAAAA());
                process.setHeureModification(JACalendar.formatTime(JACalendar.now()));

                String idGroupe = sessionCorvus.getApplication().getProperty(
                        REApplication.PROPERTY_GROUPE_COMMUNICATION_OAI_MODIFICATION_ADRESSE_TIERS);

                process.setIdGroupeNotification(idGroupe);

                entity.getSession().removeAttribute(ExternalServiceTiersViewBean.TIERS_AVANT_MODIFICATION);
                process.start();
            }
        }
    }

    @Override
    public void afterRetrieve(BEntity arg0) throws Throwable {
        // Nothing to do
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {

        // on s'assure que l'on ait le on viewBean and if is personne physique
        if (entity instanceof TITiersViewBean
                && getTiersViewBeanAvantModification(entity.getSession()).getPersonnePhysique()) {

            TITiersViewBean tiersViewBean = (TITiersViewBean) entity;

            if (hasTiersDemandeEnCours(tiersViewBean.getSession(), tiersViewBean.getIdTiers())) {

                BSession sessionCorvus = getSessionCorvus();

                TIPays ancienPays = getAncienPays(tiersViewBean.getSession());
                TIPays nouveauPays = getNouveauPays(tiersViewBean.getSession(), tiersViewBean);

                HashMap<String, String> modificationsMap = initModificationMap(tiersViewBean, ancienPays.getIdPays(),
                        nouveauPays.getIdPays());

                if (modificationsMap.size() > 0) {

                    PRGenererModificationsTiersProcess process = initProcess(tiersViewBean, sessionCorvus, ancienPays,
                            nouveauPays, modificationsMap);

                    process.setTypeModification(TypeModificationsTiers.MODIFICATION_TIERS);

                    String idGroupe = sessionCorvus.getApplication().getProperty("groupeNotification");
                    process.setIdGroupeNotification(idGroupe);
                    process.setCommunicationOAI(false);

                    entity.getSession().removeAttribute(ExternalServiceTiersViewBean.TIERS_AVANT_MODIFICATION);
                    process.start();

                    // Si le tiers modifié a une demande de type API/Invalidité en cours, on envois un mail à un
                    // autre groupe de notification
                    if (hasTiersDemandeApiInvaliditeEnCours(tiersViewBean.getSession(), tiersViewBean.getIdTiers())) {
                        idGroupe = sessionCorvus.getApplication().getProperty(
                                REApplication.PROPERTY_GROUPE_COMMUNICATION_OAI_MODIFICATION_ADRESSE_TIERS);
                        process.setIdGroupeNotification(idGroupe);
                        process.setCommunicationOAI(true);
                        process.start();

                        setAttributeForMailSent(entity, tiersViewBean);
                    }
                }
            }
        }
    }

    private void setAttributeForMailSent(BEntity entity, TITiersViewBean tiersViewBean) {
        Map<String, PRTiersDateTime> maps = (HashMap<String, PRTiersDateTime>) entity.getSession().getAttribute(
                ALREADY_SENT_A_MAIL_FOR_TIERS);

        if (maps == null) {
            maps = new HashMap<String, PRTiersDateTime>();
        }

        maps.put(tiersViewBean.getIdTiers(), new PRTiersDateTime(tiersViewBean.getIdTiers()));
        entity.getSession().setAttribute(ALREADY_SENT_A_MAIL_FOR_TIERS, maps);
    }

    private PRGenererModificationsTiersProcess initProcess(TITiersViewBean tiersViewBean, BSession sessionCorvus,
            TIPays ancienPays, TIPays nouveauPays, HashMap<String, String> modificationsMap) throws Exception {
        PRGenererModificationsTiersProcess process = new PRGenererModificationsTiersProcess();
        process.setSession(sessionCorvus);

        TITiersViewBean tiersAvantModification = getTiersViewBeanAvantModification(tiersViewBean.getSession());

        process.setNSS(tiersAvantModification.getNumAvsActuel());
        process.setEtatCivil(tiersAvantModification.getEtatCivil());
        process.setNom(tiersAvantModification.getDesignation1());
        process.setPrenom(tiersAvantModification.getDesignation2());
        process.setDesignation3(tiersAvantModification.getDesignation3());
        process.setDesignation4(tiersAvantModification.getDesignation4());
        process.setNomJeuneFille(tiersAvantModification.getNomJeuneFille());

        process.setIdCantonDomicile(tiersAvantModification.getIdCantonDomicile());
        process.setNationalite(tiersAvantModification.getPays());
        if (PRACORConst.CS_HOMME.equals(tiersAvantModification.getSexe())) {
            process.setSexe(SEXE_HOMME_MODIFICATION_TIERS);
        } else {
            process.setSexe(SEXE_FEMME_MODIFICATION_TIERS);
        }
        process.setDateNaissance(tiersAvantModification.getDateNaissance());
        process.setDateDeces(tiersAvantModification.getDateDeces());
        process.setPaysDomicile(ancienPays);
        process.setLangue(tiersAvantModification.getLangue());

        // Setter les nouvelles valeurs
        process.setNewNationalite(tiersViewBean.getPays());
        process.setNewEtatCivil(tiersViewBean.getEtatCivil());
        process.setNewNom(tiersViewBean.getDesignation1());
        process.setNewPrenom(tiersViewBean.getDesignation2());
        process.setNewDesignation3(tiersViewBean.getDesignation3());
        process.setNewDesignation4(tiersViewBean.getDesignation4());
        process.setNewNomJeuneFille(tiersViewBean.getNomJeuneFille());
        process.setNewIdCantonDomicile(tiersViewBean.getIdCantonDomicile());
        process.setNewNSS(tiersViewBean.getNumAvsActuel());
        if (PRACORConst.CS_HOMME.equals(tiersViewBean.getSexe())) {
            process.setNewSexe(SEXE_HOMME_MODIFICATION_TIERS);
        } else {
            process.setNewSexe(SEXE_FEMME_MODIFICATION_TIERS);
        }
        process.setNewDateNaissance(tiersViewBean.getDateNaissance());
        process.setNewDateDeces(tiersViewBean.getDateDeces());
        process.setNewPaysDomicile(nouveauPays);
        process.setNewLangue(tiersViewBean.getLangue());

        // Setter le reste des données nécessaires
        process.setIdTier(tiersAvantModification.getIdTiers());
        process.setModifications(modificationsMap);
        process.setUser(tiersViewBean.getSession().getUserFullName());
        process.setDateModification(JACalendar.todayJJsMMsAAAA());
        process.setHeureModification(JACalendar.formatTime(JACalendar.now()));
        return process;
    }

    private TITiersViewBean loadTiers(BSession session, String idTiers) throws Exception {
        TITiersViewBean tiersViewBean = new TITiersViewBean();
        tiersViewBean.setSession(session);
        tiersViewBean.setIdTiers(idTiers);
        tiersViewBean.retrieve();
        return tiersViewBean;
    }

    private TILocalite loadLocalite(BSession session, String idLocalite) throws Exception {
        TILocalite localite = new TILocalite();
        localite.setSession(session);
        localite.setId(idLocalite);
        localite.retrieve();
        return localite;
    }

    private TIAdresse loadAdresse(BSession session, String idAdresse) throws Exception {
        TIAdresse adresse = new TIAdresse();
        adresse.setSession(session);
        adresse.setId(idAdresse);
        adresse.retrieve();
        return adresse;
    }

    private TIPays getNouveauPays(BSession session, TITiersViewBean tiersViewBean) throws Exception {

        TIPays pays = new TIPays();

        TIAdresseDataSource adresseDataSource = tiersViewBean.getAdresseAsDataSource(
                IConstantes.CS_AVOIR_ADRESSE_COURRIER, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                JACalendar.todayJJsMMsAAAA(), true);

        if (adresseDataSource != null) {
            String codeIso = adresseDataSource.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_PAYS_ISO);
            pays.setSession(session);
            pays.setCodeIso(codeIso);
            pays.retrieve();
        }

        return pays;
    }

    private TIPays getAncienPays(BSession session) throws Exception {
        TIPays pays = new TIPays();

        TIAdresseDataSource adresseDataSource = getTiersViewBeanAvantModification(session).getAdresseAsDataSource(
                IConstantes.CS_AVOIR_ADRESSE_COURRIER, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                JACalendar.todayJJsMMsAAAA(), true);

        if (adresseDataSource != null) {
            String codeIso = adresseDataSource.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_PAYS_ISO);
            pays.setSession(session);
            pays.setCodeIso(codeIso);
            pays.retrieve();
        }

        return pays;
    }

    private TIPays loadPays(BSession session, String idPays) throws Exception {

        TIPays pays = new TIPays();
        pays.setSession(session);
        pays.setIdPays(idPays);
        pays.retrieve();

        return pays;
    }

    private HashMap<String, String> initModificationMap(TITiersViewBean tiersViewBean, String ancienPays,
            String nouveauPays) throws Exception {

        HashMap<String, String> hashMapModif = new HashMap<String, String>();

        TITiersViewBean tiersAvantModification = getTiersViewBeanAvantModification(tiersViewBean.getSession());

        if (!tiersAvantModification.getPays().getIdPays().equals(tiersViewBean.getPays().getIdPays())) {
            hashMapModif.put("nationalite", "true");
        }

        if (!tiersAvantModification.getDesignation1().equals(tiersViewBean.getDesignation1())) {
            hashMapModif.put("nom", "true");
        }

        if (!tiersAvantModification.getDesignation2().equals(tiersViewBean.getDesignation2())) {
            hashMapModif.put("prenom", "true");
        }

        if (!tiersAvantModification.getDesignation3().equals(tiersViewBean.getDesignation3())) {
            hashMapModif.put("designation3", "true");
        }

        if (!tiersAvantModification.getDesignation4().equals(tiersViewBean.getDesignation4())) {
            hashMapModif.put("designation4", "true");
        }

        if (!tiersAvantModification.getIdCantonDomicile().equals(tiersViewBean.getIdCantonDomicile())) {
            hashMapModif.put("canton", "true");
        }

        if (!tiersAvantModification.getNumAvsActuel().equals(tiersViewBean.getNumAvsActuel())) {
            hashMapModif.put("nss", "true");
        }

        if (!tiersAvantModification.getSexe().equals(tiersViewBean.getSexe())) {
            hashMapModif.put("sexe", "true");
        }

        if (!tiersAvantModification.getDateNaissance().equals(tiersViewBean.getDateNaissance())) {
            hashMapModif.put("dnaissance", "true");
        }

        if (!tiersAvantModification.getDateDeces().equals(tiersViewBean.getDateDeces())) {
            hashMapModif.put("ddeces", "true");
        }

        if (!ancienPays.equals(nouveauPays)) {
            hashMapModif.put("pays", "true");
        }

        if (!tiersAvantModification.getEtatCivil().equals(tiersViewBean.getEtatCivil())) {
            hashMapModif.put("etatCivil", "true");
        }

        if (!tiersAvantModification.getNomJeuneFille().equals(tiersViewBean.getNomJeuneFille())) {
            hashMapModif.put("nomJeuneFille", "true");
        }

        if (!tiersAvantModification.getLangue().equals(tiersViewBean.getLangue())) {
            hashMapModif.put("langue", "true");
        }

        return hashMapModif;
    }

    private BSession getSessionCorvus() throws Exception {
        BSession s = new BSession();
        s.setApplication("CORVUS");
        return s;
    }

    /**
     * Exécute un service externe avant ajout d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
        // Nothing to do

    }

    /**
     * Exécute un service externe avant suppression d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeDelete(BEntity arg0) throws Throwable {
        // Nothing to do
    }

    /**
     * Exécute un service externe avant chargement d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void beforeRetrieve(BEntity arg0) throws Throwable {
        // Nothing to do
    }

    /**
     * Exécute un service externe avant mise à jour d'une entité
     * 
     * @param l
     *            'entité
     * @exception Throwable
     *                en cas d'erreur
     */

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {

        JadeLogger.trace(this, "beforeUpdate(" + entity + ")");
        JadeProfiler.begin(this, "beforeUpdate()");

        if (isSessionOk(entity)) {

            // Il faut d'abord voir quel genre de modifications il s'agit,
            // car le traitement ne doit se faire que lors de la modification
            // tiers physique / tiers moral

            if (entity instanceof TITiersViewBean) {
                // Modification d'un tiers
                beforeUpdateTiers(entity);
            } else if (entity instanceof TIAvoirAdresseViewBean) {
                // Modification d'une adresse
                beforeUpdateAdresse(entity);

            } else {
                throw new IllegalArgumentException(
                        "the BEntity passed is not from the roght type [TITiersViewBean, TIAvoirAdresseViewBean]");
            }
        }
    }

    private void beforeUpdateAdresse(BEntity entity) throws Exception {

        TIAvoirAdresseViewBean adresseViewBean = (TIAvoirAdresseViewBean) entity;

        TIAvoirAdresse adresse = new TIAvoirAdresse();
        adresse.setSession(adresseViewBean.getSession());
        adresse.setId(adresseViewBean.getId());
        adresse.retrieve();

    }

    private void beforeUpdateTiers(BEntity entity) throws Exception {

        TITiersViewBean tiersViewBean = (TITiersViewBean) entity;
        BSession session = tiersViewBean.getSession();

        TITiersViewBean tiersViewBeanAvantUpdate = new TITiersViewBean();
        tiersViewBeanAvantUpdate.setSession(session);
        tiersViewBeanAvantUpdate.setIdTiers(tiersViewBean.getIdTiers());
        tiersViewBeanAvantUpdate.retrieve();

        TITiers tiers = new TITiers();
        tiers.setSession(session);
        tiers.setIdTiers(tiersViewBean.getIdTiers());
        tiers.retrieve();

        session.setAttribute(ExternalServiceTiersViewBean.TIERS_AVANT_MODIFICATION, tiersViewBeanAvantUpdate);

        // Modification tiers physique en tiers moral
        if (tiers.getPersonnePhysique().booleanValue() && tiersViewBean.getPersonneMorale().booleanValue()) {

            // BLOQUER LA MODIF SI DEMANDE EN COURS AU NIVEAU PRESTATIONS

            PRDemandeManager demandePrestationMan = new PRDemandeManager();
            demandePrestationMan.setSession(session);
            demandePrestationMan.setForIdTiers(tiersViewBean.getIdTiers());
            int nb = demandePrestationMan.getCount();

            if ((nb > 0) || session.hasErrors()) {
                throw new Exception(
                        "Impossible de modifier ce tiers (Moral en Physique) car il a une demande en cours. (APG, IJ ou CORVUS)");
            }
        }
    }

    /**
     * Défini si la session est en erreur
     * 
     * @param entity l'instance de BEntity dans laquelle on v ainterroger la session
     * @return l'état (session contient des erreurs) inversé
     */
    private boolean isSessionOk(BEntity entity) {
        return !entity.getSession().hasErrors();
    }

    /**
     * Test afin de savoir si le tiers a une demande en cours
     * <b>Regler la question de la gestion des erreurs: pquoi ?</b>
     * 
     * @param session la session en cours
     * @param idTiers l'id du Tiers désiré
     * @return l'état du test (nombre de demande en cours > 0)
     * @throws Exception exception générique suite à l'interrogation sur la base de donnée
     */
    private boolean hasTiersDemandeEnCours(BSession session, String idTiers) throws Exception {

        PRDemandeManager demandePrestationMan = new PRDemandeManager();
        demandePrestationMan.setSession(session);
        demandePrestationMan.setForIdTiers(idTiers);
        int nb = demandePrestationMan.getCount();

        return nb > 0 || session.hasErrors();
    }

    /**
     * Methode pour savoir si le tiers a une rente de veuf/veuve en cours avec une demande de rente d'invalidité en état
     * "Terminé" avec le motif "Rente de survivant perdure" OU que le tiers ait une rente invalidité/impotence en cours.
     * 
     * @param session session.
     * @param idTiers l'id tiers.
     * @return True si les conditions sont fournis.
     * @throws Exception exceptions.
     */
    private boolean hasTiersDemandeApiInvaliditeEnCours(BSession session, String idTiers) throws Exception {
        boolean hasRente = hasRenteInvaliditerImpotenceEnCours(session, idTiers);

        if (!hasRente) {
            hasRente = hasRenteVeuveEnCours(session, idTiers)
                    && hasDemandeInvaliditeTerminerAvecMotifVeuve(session, idTiers);
        }

        return hasRente;
    }

    private boolean hasRenteInvaliditerImpotenceEnCours(BSession session, String idTiers) throws Exception {
        boolean hasConditions = false;

        // Recherche des rentes invalidités / API en cours.
        REPrestationAccordeeManager prestationInvaliditeImpotenceEnCours = new REPrestationAccordeeManager();
        prestationInvaliditeImpotenceEnCours.setSession(session);
        prestationInvaliditeImpotenceEnCours.setForIdTiersBeneficiaire(idTiers);
        prestationInvaliditeImpotenceEnCours.setForCodesPrestationsIn(buildForCodesPrestationInString());
        prestationInvaliditeImpotenceEnCours.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", "
                + IREPrestationAccordee.CS_ETAT_DIMINUE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);

        String moisPaiement = getMoisPaiement(session);

        prestationInvaliditeImpotenceEnCours.setForEnCoursAtMois(moisPaiement);
        prestationInvaliditeImpotenceEnCours.find(2);

        if (prestationInvaliditeImpotenceEnCours.size() > 0) {
            hasConditions = true;
        }

        return hasConditions;
    }

    private boolean hasRenteVeuveEnCours(BSession session, String idTiers) throws Exception {
        boolean hasConditions = false;

        // Recherche si le tiers contient une rente veuve/veuf en cours.
        REPrestationAccordeeManager prestationVeuveEnCours = new REPrestationAccordeeManager();
        prestationVeuveEnCours.setSession(session);
        prestationVeuveEnCours.setForIdTiersBeneficiaire(idTiers);
        prestationVeuveEnCours.setForCodesPrestationsIn("'13'");
        prestationVeuveEnCours.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", "
                + IREPrestationAccordee.CS_ETAT_DIMINUE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);

        String moisPaiement = getMoisPaiement(session);

        prestationVeuveEnCours.setForEnCoursAtMois(moisPaiement);
        prestationVeuveEnCours.find(2);

        if (prestationVeuveEnCours.size() > 0) {
            hasConditions = true;
        }

        return hasConditions;
    }

    private String getMoisPaiement(BSession session) {
        String moisPaiement = REPmtMensuel.getDateDernierPmt(session);

        if (REPmtMensuel.DATE_NON_TROUVEE_POUR_DERNIER_PAIEMENT.equals(moisPaiement)) {
            String message = session.getLabel("ERREUR_IMPOSSIBLE_RETROUVER_DATE_DERNIER_PAIEMENT");
            throw new IllegalArgumentException(message);
        }

        return moisPaiement;
    }

    private boolean hasDemandeInvaliditeTerminerAvecMotifVeuve(BSession session, String idTiers) throws Exception {

        boolean hasConditions = false;

        // Recherche si le tiers contient au moins une demande de rente d'invalidité en état "TERMINER" qui a un motif
        // de type
        // "Rente de survivant perdure" dans les informations complémentaires
        REDemandeRenteJointDemandeManager manager = new REDemandeRenteJointDemandeManager();
        manager.setSession(session);
        manager.setForCsEtatDemande(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE);
        manager.setForCsTypeDemande(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE);
        manager.setForIdTiersRequ(idTiers);
        manager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.size(); i++) {
            REDemandeRenteJointDemande demandeRente = (REDemandeRenteJointDemande) manager.get(i);

            if (!JadeStringUtil.isEmpty(demandeRente.getIdInfoComplementaire())) {
                PRInfoCompl infoCompl = new PRInfoCompl();
                infoCompl.setSession(session);
                infoCompl.setIdInfoCompl(demandeRente.getIdInfoComplementaire());
                infoCompl.retrieve();

                // Si il contient le motif de type "rente de survivant perdure", alors nous remplissons la
                // condition
                if (IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_RENTE_VEUVE_PERDURE.equals(infoCompl
                        .getTypeInfoCompl())) {
                    hasConditions = true;
                    break;
                }
            }
        }

        return hasConditions;
    }

    private String buildForCodesPrestationInString() {
        StringBuilder codesString = new StringBuilder();

        for (PRCodePrestationAPI code : PRCodePrestationAPI.values()) {
            codesString.append("'").append(code.getCodePrestationAsString()).append("', ");
        }
        codesString.append("'50', '70'");
        return codesString.toString();
    }

    /**
     * Exécute un service externe pour initialiser une entité
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void init(BEntity arg0) throws Throwable {
        // Nothing to do
    }

    /**
     * Exécute un service externe pour valider le contenu d'une entité
     * 
     * @param entity
     *            l'entité
     * @exception Throwable
     *                en cas d'erreur
     */
    @Override
    public void validate(BEntity arg0) throws Throwable {
        // Nothing to do
    }

    private TITiersViewBean getTiersViewBeanAvantModification(BSession session) throws Exception {

        Object tiersViewBean = session.getAttribute(ExternalServiceTiersViewBean.TIERS_AVANT_MODIFICATION);

        if (tiersViewBean == null) {
            throw new Exception("Les données du tiers avant modification n'ont pas pu être chargées");
        }

        return (TITiersViewBean) tiersViewBean;
    }
}