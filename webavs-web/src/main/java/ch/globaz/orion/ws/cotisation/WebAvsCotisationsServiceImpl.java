package ch.globaz.orion.ws.cotisation;

import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.api.IAFCotisation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.releve.AFApercuReleve;
import globaz.naos.db.releve.AFApercuReleveManager;
import globaz.naos.exceptions.AFTechnicalException;
import globaz.naos.translation.CodeSystem;
import globaz.orion.process.EBDanPreRemplissage;
import globaz.orion.utils.EBSddUtils;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPCotisationManager;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.process.CPProcessCalculCotisation;
import globaz.phenix.process.documentsItext.CPProcessImprimerDecisionAgence;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.jws.WebService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.common.sql.converters.DateConverter;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.exceptions.OrionPucsException;
import ch.globaz.orion.business.services.OrionServiceLocator;
import ch.globaz.orion.utils.StringConverter;
import ch.globaz.orion.ws.enums.CalculateurPeriodiciteEnumWebAvs;
import ch.globaz.orion.ws.enums.OrderByDirWebAvs;
import ch.globaz.orion.ws.enums.SexeSalarie;
import ch.globaz.orion.ws.enums.converters.TypeDecisionAcompteIndConverter;
import ch.globaz.orion.ws.exceptions.WebAvsException;
import ch.globaz.orion.ws.service.AFMassesForAffilie;
import ch.globaz.orion.ws.service.AppAffiliationService;
import ch.globaz.orion.ws.service.EBSalarie;
import ch.globaz.orion.ws.service.EBSalarieManager;
import ch.globaz.orion.ws.service.UtilsService;
import ch.globaz.orion.ws.service.manager.AFReleveForSdd;
import ch.globaz.orion.ws.service.manager.AFReleveForSddManager;
import ch.globaz.queryexec.bridge.jade.SCM;

@WebService(endpointInterface = "ch.globaz.orion.ws.cotisation.WebAvsCotisationsService")
public class WebAvsCotisationsServiceImpl implements WebAvsCotisationsService {

    private static Logger logger = LoggerFactory.getLogger(WebAvsCotisationsServiceImpl.class);

    @Override
    public MassesForAffilie listerMassesActuelles(String numeroAffilie) {
        logger.debug("start operation listerMassesActuelles with param numeroAfiflie=" + numeroAffilie);

        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("Unabled to list Masses, numeroAffilie is empty or null");
        }

        // Récupération d'une session
        logger.debug("init session");
        BSession session = UtilsService.initSession();
        logger.debug("init session done");

        // Récupération de la liste des cotisations
        List<AFMassesForAffilie> listeMasseForAffilie = AppAffiliationService
                .retrieveListCotisationActiveForNumAffilie(session, numeroAffilie);

        // Création des données de retour
        logger.debug("end of operation listerMassesActuelles");
        return fillMassesForAffilie(listeMasseForAffilie);
    }

    /**
     * Permet de remplir la classe retournée pour le webService
     * 
     * @param listeMasseForAffilie
     * @return
     */
    private MassesForAffilie fillMassesForAffilie(List<AFMassesForAffilie> listeMasseForAffilie) {

        MassesForAffilie massesForAffilie = new MassesForAffilie();

        for (AFMassesForAffilie masseAff : listeMasseForAffilie) {

            massesForAffilie.idAffiliation = Integer.parseInt(masseAff.getIdAffilie());
            massesForAffilie.noAffilieFormatte = masseAff.getNumAffilie();
            massesForAffilie.raisonSociale = masseAff.getRaisonSociale();

            Masse masse = new Masse();
            masse.idCotisation = Integer.parseInt(masseAff.getIdCotisation());
            masse.libelle_fr = masseAff.getLibelleFr();
            masse.libelle_de = masseAff.getLibelleDe();
            masse.libelle_it = masseAff.getLibelleIt();
            masse.valeur = convertirAnnuelToMensuelA5Centimes(masseAff.getMasseCotisation());
            masse.typeCotisation = Integer.parseInt(masseAff.getTypeAssurance());
            masse.codeCanton = Integer.parseInt(masseAff.getCodeCanton());
            masse.genreCoti = Integer.parseInt(masseAff.getGenreCoti());

            masse.periodicite = definePeriodicite(masseAff.getCsPeriodicite());

            massesForAffilie.masses.add(masse);
        }

        return massesForAffilie;
    }

    private CalculateurPeriodiciteEnumWebAvs definePeriodicite(String csPeriodicite) {
        if (csPeriodicite != null) {
            if (IAFCotisation.PERIODICITE_MENSUELLE.equals(csPeriodicite)) {
                return CalculateurPeriodiciteEnumWebAvs.MENSUEL;
            } else if (IAFCotisation.PERIODICITE_TRIMESTRIELLE.equals(csPeriodicite)) {
                return CalculateurPeriodiciteEnumWebAvs.TRIMESTRIEL;
            } else {
                return CalculateurPeriodiciteEnumWebAvs.ANNUEL;
            }
        } else {
            return null;
        }
    }

    /**
     * Permet de convertir une masse annuelle en masse mensuelle en arrondissant à 5 centimes pres.
     * 
     * @param masseAnnuelle
     * @return
     */
    public static BigDecimal convertirAnnuelToMensuelA5Centimes(BigDecimal masseAnnuelle) {
        if (masseAnnuelle == null) {
            throw new IllegalStateException("La masse à convertir ne peut être null");
        }

        BigDecimal masseAnnuelleConvertie = masseAnnuelle.divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_EVEN);
        return JANumberFormatter.round(masseAnnuelleConvertie, 0.05, 2, JANumberFormatter.NEAR);
    }

    @Override
    public boolean executerPreRemplissageDan(String noAffilie, Integer annee, String loginName, String userEmail) {
        // Récupération d'une session
        BSession session = UtilsService.initSession();

        // exécution du process de pré-remplissage
        EBDanPreRemplissage process = new EBDanPreRemplissage();
        process.setSession(session);
        process.setAnnee(annee.toString());
        process.setNumAffilie(noAffilie);
        process.setEmail(userEmail);
        process.setExecuteFromWebAvs(false);
        process.setLoginName(loginName);

        try {
            BProcessLauncher.start(process, false);
        } catch (Exception e) {
            JadeLogger.error("Process EBDanPreRemplissage failed", e);
            return false;
        }
        return true;
    }

    @Override
    public String genererDocumentPucsLisible(String id, DeclarationSalaireProvenance provenance, String format,
            String loginName, String userEmail, String langue) throws WebAvsException {
        String filePath = null;

        try {
            filePath = OrionServiceLocator.getPucsService().pucsFileLisibleForEbusiness(id, provenance, format,
                    loginName, userEmail, langue);
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error("Unable to generate the files PUCS ", e);
        } catch (OrionPucsException e) {
            throw new WebAvsException(e.getMessage(), e);
        }

        return filePath;
    }

    @Override
    public Double findTauxAssuranceForCotisation(Integer idCotisation, BigDecimal montant, String date)
            throws WebAvsException {
        // vérification des paramètres
        if (idCotisation == null) {
            throw new WebAvsException("idCotisation cannot be null ");
        }

        if (montant == null) {
            throw new WebAvsException("montant cannot be null ");
        }

        if (JadeStringUtil.isBlankOrZero(date)) {
            throw new WebAvsException("date cannot be null or empty");
        }

        // récupération de la cotisation en fonction de l'id
        BSession session = UtilsService.initSession();
        AFCotisation cotisation = new AFCotisation();
        cotisation.setSession(session);
        cotisation.setId(Integer.toString(idCotisation));
        try {
            cotisation.retrieve();

            // récupération du taux
            String taux = cotisation.getTaux(date, montant.toString());
            return new Double(taux);
        } catch (Exception e) {
            JadeLogger.error(this.getClass(),
                    "Unable to retrieve cotisation with id : " + idCotisation + " -> " + e.getMessage());
            throw new WebAvsException("Unable to retrieve cotisation with id : " + idCotisation);
        }
    }

    @Override
    public DecompteMensuel findDecompteMois(String numeroAffilie, String mois, String annee) {
        Checkers.checkNotNull(numeroAffilie, "numeroAffilie");
        Checkers.checkNotEmpty(numeroAffilie, "numeroAffilie");
        Checkers.checkNotNull(mois, "mois");
        Checkers.checkNotEmpty(mois, "mois");
        Checkers.checkIsInteger(mois, "mois");
        Checkers.checkNotNull(annee, "annee");
        Checkers.checkNotEmpty(annee, "annee");
        Checkers.checkIsInteger(annee, "annee");

        DecompteMensuel decompte = null;

        BSession session;

        try {

            session = UtilsService.initSession();

            // Récupération des informations des cotisations
            decompte = retrieveCotisationsInformations(numeroAffilie, mois, annee, session);

            // Récupération des relevés
            decompte = fillCotisationsInformationsWithReleve(numeroAffilie, decompte, session);

            // Préparation des données utiles pour SDD
            decompte = EBSddUtils.prepareDataForEbusiness(decompte);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return decompte;
    }

    private static DecompteMensuel fillCotisationsInformationsWithReleve(String numeroAffilie,
            DecompteMensuel decompte, BSession session) throws Exception {
        AFApercuReleveManager manager = new AFApercuReleveManager();
        manager.setSession(session);
        manager.setForAffilieNumero(numeroAffilie);
        manager.setForDateDebut("01." + StringUtils.leftPad(decompte.getMoisDecompte(), 2, '0') + "."
                + decompte.getAnneeDecompte());
        manager.find(BManager.SIZE_USEDEFAULT);

        if (!manager.isEmpty()) {

            List<AFApercuReleve> lstReleves = manager.toList();
            for (AFApercuReleve releve : lstReleves) {

                AFReleveForSddManager managerSdd = new AFReleveForSddManager();
                managerSdd.setSession(session);
                managerSdd.setForIdReleve(releve.getIdReleve());
                managerSdd.find(BManager.SIZE_USEDEFAULT);

                if (!managerSdd.isEmpty()) {

                    decompte.setDejaEtabli(true);

                    List<AFReleveForSdd> listReleve = managerSdd.toList();
                    for (AFReleveForSdd releveSdd : listReleve) {
                        fillDecompteMensuelWithReleve(decompte, releveSdd);
                    }
                }
            }
        }

        return decompte;
    }

    private static DecompteMensuel retrieveCotisationsInformations(String numeroAffilie, String mois, String annee,
            BSession session) {

        // Récupération des cotisations
        List<AFMassesForAffilie> listeMasseForAffilie = AppAffiliationService
                .retrieveListCotisationForNumAffilieForMoisAnnee(session, numeroAffilie, mois, annee);

        DecompteMensuel decompte = null;

        if (!listeMasseForAffilie.isEmpty()) {

            AFMassesForAffilie masseAff = listeMasseForAffilie.get(0);

            decompte = new DecompteMensuelBuilder().withIdAffilie(masseAff.getIdAffilie()).withAnneeDecompte(annee)
                    .withMoisDecompte(mois).withNumeroAffilie(masseAff.getNumAffilie())
                    .withLinesDecompte(fillDecompteMensuelForAffilie(listeMasseForAffilie)).build();
        }

        return decompte;
    }

    public static DecompteMensuel fillDecompteMensuelWithReleve(DecompteMensuel decompte, AFReleveForSdd releveSdd) {

        for (DecompteMensuelLine line : decompte.getLinesDecompte()) {
            int idRubrique = Integer.parseInt(releveSdd.getIdRubrique());

            if (line.getIdRubrique() == idRubrique) {
                BigDecimal masseCalcule = line.getMasse().add(releveSdd.getMasseFacture());
                line.setMasse(masseCalcule);
            }
        }

        return decompte;
    }

    private static List<DecompteMensuelLine> fillDecompteMensuelForAffilie(List<AFMassesForAffilie> masses) {

        List<DecompteMensuelLine> lines = new ArrayList<DecompteMensuelLine>();

        for (AFMassesForAffilie masseAff : masses) {

            lines.add(new DecompteMensuelLineBuilder().withIdCotisation(masseAff.getIdCotisation())
                    .withIdRubrique(Integer.parseInt(masseAff.getIdRubrique())).withLibelleDe(masseAff.getLibelleDe())
                    .withLibelleFr(masseAff.getLibelleFr()).withLibelleIt(masseAff.getLibelleIt())
                    .withMasse(BigDecimal.ZERO).withTypeCotisation(masseAff.getTypeAssurance()).build());
        }

        return lines;
    }

    @Override
    public MassesForAffilie listerMassesActuellesConfigurable(String noAffilie, boolean cotParitaire, boolean cotPers) {
        if (JadeStringUtil.isEmpty(noAffilie)) {
            throw new IllegalArgumentException("Unabled to list Masses, numeroAffilie is empty or null");
        }

        // Récupération d'une session
        BSession session = UtilsService.initSession();

        // Récupération de la liste des cotisations paritaires et personnelles
        List<AFMassesForAffilie> listeMasseForAffilie = AppAffiliationService
                .retrieveListCotisationConfigurableForNumAffilie(session, noAffilie, cotParitaire, cotPers);

        return fillMassesForAffilie(listeMasseForAffilie);
    }

    @Override
    public String getPathDucplicataDecisionAcompteInd(Integer idDecision) throws WebAvsException {
        if (idDecision == null || idDecision <= 0) {
            throw new IllegalArgumentException("Unabled to get document's path, idDecision is empty or null");
        }

        String pathFile = null;
        BSession session = UtilsService.initSession();
        CPDecision dec = new CPDecision();

        try {
            dec.setSession(session);
            dec.setIdDecision(idDecision.toString());
            dec.wantCallMethodAfter(false);
            dec.retrieve();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (JadeStringUtil.isEmpty(dec.getId())) {
            throw new WebAvsException("Unable to retrieve decision with id : " + idDecision.toString());
        } else {

            CPProcessImprimerDecisionAgence process = new CPProcessImprimerDecisionAgence();
            try {

                process.setSession(session);
                process.setIdTiers(dec.getIdTiers());
                process.setIdAffiliation(dec.getIdAffiliation());
                process.setIdDecision(dec.getIdDecision());
                process.setSendMailOnError(false);
                process.setSendCompletionMail(false);
                process.setControleTransaction(true);
                process.setDuplicata(true);
                process.setEnvoiGed(Boolean.FALSE);
                process.setAffichageEcran(Boolean.TRUE);
                process.setEbusiness(true);
                process.executeProcess();
            } catch (Exception e) {
                process.setMessage(e.toString());
                process.setMsgType(FWViewBeanInterface.ERROR);
            }

            if (!process.getAttachedDocuments().isEmpty()) {
                for (Iterator<?> iter = process.getAttachedDocuments().iterator(); iter.hasNext();) {
                    JadePublishDocument document = (JadePublishDocument) iter.next();
                    pathFile = document.getDocumentLocation();
                }
            }
        }

        return pathFile;
    }

    @Override
    public List<DecisionAcompteInd> listerDecisionsAcomptesInd(String numeroAffilie) throws WebAvsException {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroAffilie is null or empty");
        }

        SQLWriter writer = SQLWriter
                .write()
                .select("dec.IAIDEC as id_Decision_Web_Avs, aff.MALNAF as num_Affilie, dec.IAANNE as annee, doe.IDREV1 as ancien_Resultat_Net, doe.IDCAPI as ancien_Capital_Investit, dec.IATTDE as type_Web_Avs, dec.IADFAC as date_Facturation_Web_Avs, dec.PSPY as pspy")
                .from("schema.CPDECIP dec").join("schema.CPDOENP doe on dec.IAIDEC = doe.IAIDEC")
                .join("schema.AFAFFIP aff on aff.MAIAFF = dec.MAIAFF").where().and("dec.IAANNE > 2012")
                .and("aff.MALNAF = '?' ", numeroAffilie).and("dec.IATTDE IN (605001, 605002, 605003, 605004, 605007)");

        List<DecisionAcompteInd> listDecisions = SCM.newInstance(DecisionAcompteInd.class)
                .session(UtilsService.initSession()).query(writer.toSql()).execute();

        for (DecisionAcompteInd dec : listDecisions) {

            try {
                dec.formatDateMisAJour(dec.getPspy().substring(0, 8));
                dec.setStatus(ch.globaz.orion.ws.enums.StatusDecisionAcompteIndEnum.TRAITEE);
                dec.setSource(ch.globaz.orion.ws.enums.SourceDecisionAcompteInd.WEBAVS);
                dec.defineTypeDec();
                dec.formatDateFacturation();
            } catch (ParseException e) {
                throw new WebAvsException("Unable to list decisions for affiliate number : " + numeroAffilie, e);
            }

        }

        return listDecisions;
    }

    @Override
    public CalculAcomptesInd calculerAcompteIndForAnnee(String numeroAffilie, Integer annee, BigDecimal resultatNet,
            BigDecimal capitalInvesti, String language) {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroDossier is null or empty");
        }

        CalculAcomptesInd calcul = new CalculAcomptesInd();

        BSession session = UtilsService.initSession();

        try {

            AFAffiliation affiliation = getAffiliation(numeroAffilie, session);

            if (affiliation != null) {

                CPDecision decision = initDecision(annee, affiliation, session);

                CPProcessCalculCotisation process = new CPProcessCalculCotisation();
                process.setSession(session);
                process.setTransaction(session.newTransaction());
                process.setAffiliation(affiliation);
                process.setDecision(decision);
                process.setTiers(affiliation.getTiers());
                process.setLanguage(language);
                process.setEbusiness(true);

                calcul = process.calculIndependantForCalculateur(process, 0, annee, resultatNet, capitalInvesti);
            } else {
                throw new WebAvsException("Unable to retrieve affiliate with affiliate number : " + numeroAffilie);
            }
        } catch (Exception e) {
            JadeLogger.error("Process CPProcessCalculCotisation failed", e);
        }

        return calcul;
    }

    @Override
    public InfosDerniereDecisionActive findInfosDerniereDecisionActive(String numeroAffilie, Integer anneeDecision)
            throws WebAvsException {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroDossier is null or empty");
        }

        BSession session = UtilsService.initSession();
        InfosDerniereDecisionActive infosDerniereDecisionActive = null;
        CPDecision decision = null;

        AFAffiliation affiliation = getAffiliation(numeroAffilie, session);

        // Retrouver la dernière décision active
        CPDecisionManager decisionManager = new CPDecisionManager();

        try {
            decisionManager.setSession(session);
            decisionManager.setForIdAffiliation(affiliation.getAffiliationId());
            decisionManager.setForIsActive(true);
            if (anneeDecision != null && anneeDecision != 0) {
                decisionManager.setForAnneeDecision(anneeDecision.toString());
            }
            decisionManager.setOrder("IAANNE DESC, IAIDEC DESC");
            decisionManager.find(BManager.SIZE_USEDEFAULT);

            if (!decisionManager.isEmpty()) {
                decision = (CPDecision) decisionManager.getFirstEntity();

                CPDonneesBase donneesBase = new CPDonneesBase();
                donneesBase.setSession(session);
                donneesBase.setIdDecision(decision.getIdDecision());
                donneesBase.retrieve();

                infosDerniereDecisionActive = getInfosDerniereDecisionActive(decision, donneesBase);
            }
        } catch (Exception e) {
            throw new WebAvsException("Unable to retrieve decision with affiliate number : " + numeroAffilie, e);
        }

        return infosDerniereDecisionActive;
    }

    private CPDecision initDecision(Integer annee, AFAffiliation affiliation, BSession session) throws Exception {
        CPDecision decision = new CPDecision();

        // Récupération des dates de débuts et fin afin de déterminer la bonne période.
        String dateDebutAffiliation = affiliation.getDateDebut();
        String dateFinAffiliatString = affiliation.getDateFin();

        String dateDebutEbu = "01.01." + String.valueOf(annee);
        String dateFinEbu = "31.12." + String.valueOf(annee);

        decision.setAffiliation(affiliation);

        // Définition de la date de début de la décision
        // Si la date d'affiliation est avant la date Ebu, on set la date ebu
        if (JadeDateUtil.isDateBefore(dateDebutAffiliation, dateDebutEbu)) {
            decision.setDebutDecision(dateDebutEbu);
        } else {
            decision.setDebutDecision(dateDebutAffiliation);
        }

        // Définition de la date de fin de la décision
        // Si la date d'affiliation est avant la date Ebu, on prend la date d'affiliation
        if (JadeDateUtil.isDateBefore(dateFinAffiliatString, dateFinEbu)) {
            decision.setFinDecision(dateFinAffiliatString);
        } else {
            decision.setFinDecision(dateFinEbu);
        }

        decision.setNombreMoisTotalDecision(String.valueOf(JadeDateUtil.getNbMonthsBetween(decision.getDebutDecision(),
                decision.getFinDecision())));
        decision.setAnneeDecision(String.valueOf(annee));

        decision.setGenreAffilie(CPDecision.CS_INDEPENDANT);

        String dateAgeAvs = affiliation.getTiers().getDateAvs();
        int anneeAvs = JACalendar.getYear(dateAgeAvs);

        // Détermination si rentier
        if ((anneeAvs < annee)
                || ((anneeAvs == annee) && BSessionUtil.compareDateFirstGreater(session, decision.getFinDecision(),
                        dateAgeAvs))) {
            decision.setGenreAffilie(CPDecision.CS_RENTIER);
        }

        decision.setIdAffiliation(affiliation.getId());
        decision.setIdTiers(affiliation.getIdTiers());
        decision.setTiers(affiliation.getTiers());

        return decision;

    }

    @Override
    public List<CotisationPersonnelleWebAvs> listerCotisationsPersonnellesActives(String numeroAffilie, String language)
            throws WebAvsException {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroDossier is null or empty");
        }

        List<CotisationPersonnelleWebAvs> listeCotPers = new ArrayList<CotisationPersonnelleWebAvs>();
        BSession session = UtilsService.initSession();
        CPDecision decision = null;

        // Retrouver l'affiliation
        AFAffiliation affiliation = getAffiliation(numeroAffilie, session);

        // Retrouver la dernière décision active
        CPDecisionManager decisionManager = new CPDecisionManager();

        try {
            decisionManager.setSession(session);
            decisionManager.setForIdAffiliation(affiliation.getAffiliationId());
            decisionManager.setForIsActive(true);
            decisionManager.setOrder("IAANNE DESC, IAIDEC DESC");
            decisionManager.find(BManager.SIZE_USEDEFAULT);

            if (!decisionManager.isEmpty()) {
                decision = (CPDecision) decisionManager.getFirstEntity();

                CPDonneesCalcul donneesCalcul = new CPDonneesCalcul();
                donneesCalcul.setSession(session);
                String revenunet = donneesCalcul.getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_NET);

                CPCotisationManager cotiMgr = new CPCotisationManager();
                cotiMgr.setSession(session);
                cotiMgr.setForIdDecision(decision.getId());
                cotiMgr.find(BManager.SIZE_USEDEFAULT);

                if (!cotiMgr.isEmpty()) {
                    String montantCotiAvs = null;

                    // On commence par récupérer le montant de la cotisation AVS
                    for (int i = 0; i < cotiMgr.size(); i++) {
                        CPCotisation coti = (CPCotisation) cotiMgr.getEntity(i);
                        if (CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equals(coti.getGenreCotisation())) {

                            if (CodeSystem.PERIODICITE_MENSUELLE.equals(coti.getPeriodicite())) {
                                montantCotiAvs = coti.getMontantMensuel();
                            } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(coti.getPeriodicite())) {
                                montantCotiAvs = coti.getMontantTrimestriel();
                            } else {
                                montantCotiAvs = coti.getMontantAnnuel();
                            }
                        }
                    }

                    CotisationPersonnelleWebAvs cotiPersAF = new CotisationPersonnelleWebAvs();
                    cotiPersAF.setCotisation(new BigDecimal("0"));

                    for (int i = 0; i < cotiMgr.size(); i++) {

                        CPCotisation coti = (CPCotisation) cotiMgr.getEntity(i);
                        CotisationPersonnelleWebAvs cotiPers = new CotisationPersonnelleWebAvs();

                        AFCotisation cotiAf = new AFCotisation();
                        cotiAf.setSession(session);
                        cotiAf.setCotisationId(coti.getIdCotiAffiliation());
                        cotiAf.retrieve();
                        if (!cotiAf.isNew() && (cotiAf != null)) {
                            coti.setCotiAffiliation(cotiAf);
                        }

                        if (CodeSystem.TYPE_ASS_COTISATION_AF.equals(coti.getGenreCotisation())) {
                            cotiPersAF.setMontantDeterminant(!JadeStringUtil.isEmpty(revenunet) ? new BigDecimal(
                                    JANumberFormatter.deQuote(revenunet)) : new BigDecimal(0));
                            cotiPersAF.setPeriodicite(coti.getPeriodicite());

                            BigDecimal montantCotisationAfTemp = cotiPersAF.getCotisation();

                            if (CodeSystem.PERIODICITE_MENSUELLE.equals(coti.getPeriodicite())) {
                                montantCotisationAfTemp = montantCotisationAfTemp.add(new BigDecimal(JANumberFormatter
                                        .deQuote(coti.getMontantMensuel())));
                            } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(coti.getPeriodicite())) {
                                montantCotisationAfTemp = montantCotisationAfTemp.add(new BigDecimal(JANumberFormatter
                                        .deQuote(coti.getMontantTrimestriel())));
                            } else {
                                montantCotisationAfTemp = montantCotisationAfTemp.add(new BigDecimal(JANumberFormatter
                                        .deQuote(coti.getMontantAnnuel())));
                            }

                            cotiPersAF.setCotisation(montantCotisationAfTemp);

                            Double taux = cotiPersAF.getTaux() + Double.valueOf(coti.getTaux());
                            cotiPersAF.setTaux(Double.valueOf(JANumberFormatter.formatNoRound(taux.toString(), 3)));

                            cotiPersAF.setPeriodicite(coti.getPeriodicite());

                            cotiPersAF.setLibelle(getCotiLibelle(session, coti.getCotiAffiliation(), language));

                        } else if (CodeSystem.TYPE_ASS_CPS_AUTRE.equals(coti.getGenreCotisation())
                                || CodeSystem.TYPE_ASS_CPS_GENERAL.equals(coti.getGenreCotisation())) {

                            BigDecimal montantCotisationAfTemp = cotiPersAF.getCotisation();
                            cotiPersAF.setPeriodicite(coti.getPeriodicite());

                            if (CodeSystem.PERIODICITE_MENSUELLE.equals(coti.getPeriodicite())) {
                                montantCotisationAfTemp = cotiPersAF.getCotisation().add(
                                        new BigDecimal(JANumberFormatter.deQuote(coti.getMontantMensuel())));
                            } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(coti.getPeriodicite())) {
                                montantCotisationAfTemp = cotiPersAF.getCotisation().add(
                                        new BigDecimal(JANumberFormatter.deQuote(coti.getMontantTrimestriel())));
                            } else {
                                montantCotisationAfTemp = cotiPersAF.getCotisation().add(
                                        new BigDecimal(JANumberFormatter.deQuote(coti.getMontantAnnuel())));
                            }

                            cotiPersAF.setCotisation(montantCotisationAfTemp);

                            Double taux = cotiPersAF.getTaux() + Double.valueOf(coti.getTaux());
                            cotiPersAF.setTaux(Double.valueOf(JANumberFormatter.formatNoRound(taux.toString(), 3)));

                        } else {
                            cotiPers.setPeriodicite(coti.getPeriodicite());

                            cotiPers.setLibelle(getCotiLibelle(session, coti.getCotiAffiliation(), language));

                            if (CodeSystem.TYPE_ASS_FRAIS_ADMIN.equals(coti.getGenreCotisation())) {
                                cotiPers.setMontantDeterminant(new BigDecimal(JANumberFormatter.deQuote(montantCotiAvs)));
                            } else {
                                cotiPers.setMontantDeterminant(!JadeStringUtil.isEmpty(revenunet) ? new BigDecimal(
                                        JANumberFormatter.deQuote(revenunet)) : new BigDecimal(0));
                            }

                            if (CodeSystem.PERIODICITE_MENSUELLE.equals(cotiPers.getPeriodicite())) {
                                cotiPers.setCotisation(new BigDecimal(JANumberFormatter.deQuote(coti
                                        .getMontantMensuel())));
                            } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(cotiPers.getPeriodicite())) {
                                cotiPers.setCotisation(new BigDecimal(JANumberFormatter.deQuote(coti
                                        .getMontantTrimestriel())));
                            } else {
                                cotiPers.setCotisation(new BigDecimal(
                                        JANumberFormatter.deQuote(coti.getMontantAnnuel())));
                            }

                            cotiPers.setTaux(Double.valueOf(coti.getTaux()));

                            listeCotPers.add(cotiPers);
                        }
                    }

                    listeCotPers.add(cotiPersAF);
                }
            }
        } catch (Exception e) {
            throw new WebAvsException("Unable to retrieve decision with affiliate number : " + numeroAffilie, e);
        }

        return listeCotPers;
    }

    @Override
    public InfosDeclarationSalaire findInfosDeclarationSalaire(String numeroAffilie, Integer annee)
            throws WebAvsException {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroAffilie is null or empty");
        }

        StringBuilder requete = new StringBuilder("SELECT ");
        requete.append("count(kaiind) as nb_Salaires, ");
        requete.append("sum(SUMMONTANT) as masse_Salariale_Declaree, ");
        requete.append("KBNANN as annee ");
        requete.append("FROM ( ");
        requete.append("SELECT kaiind, KBNANN, SUM(kbmmon) as SUMMONTANT from ( ");
        requete.append("SELECT ");
        requete.append("ecr.KAIIND, ");
        requete.append("ecr.KBNANN, ");
        requete.append("case when kbtext <> 0 then -kbmmon else kbmmon end as KBMMON ");
        requete.append("FROM ");
        requete.append("schema.CIECRIP ecr ");
        requete.append("inner join schema.afaffip aff on aff.maiaff = ecr.KBITIE ");
        requete.append("inner join schema.ciindip ind on ind.KAIIND = ecr.KAIIND ");
        requete.append("where ");
        requete.append("aff.malnaf = '").append(numeroAffilie).append("' ");
        requete.append("and (ecr.KBTGEN=310001 or (ecr.KBTGEN=310007 and ecr.KBTSPE=312003)) ");
        requete.append("and ecr.KBNANN = ");

        // Si annee renseignée
        if (annee != null && annee != 0) {
            requete.append(annee).append(" ");
        } else {
            // Sinon on va chercher la derniere année renseignée dans les CI
            requete.append("(SELECT MAX(ecr2.KBNANN) FROM schema.CIECRIP ecr2 where ecr2.KBITIE = aff.maiaff ");
            requete.append("and (ecr2.KBTGEN=310001 or (ecr2.KBTGEN=310007 and ecr2.KBTSPE=312003))) ");
        }
        requete.append(") group by kaiind, kbnann having sum(KBMMON)> 0 ");
        requete.append(") group by KBNANN ");

        List<InfosDeclarationSalaire> listInfosDS = SCM.newInstance(InfosDeclarationSalaire.class)
                .session(UtilsService.initSession()).query(requete.toString()).execute();

        if (!listInfosDS.isEmpty()) {
            return listInfosDS.get(0);
        }

        return null;
    }

    @Override
    public String genererDocumentPucsLisibleFromByteCode(byte[] pucsFile, DeclarationSalaireProvenance provenance,
            String format, String langue) throws WebAvsException {

        String filePath = null;
        try {
            filePath = OrionServiceLocator.getPucsService().pucsFileLisibleForEbusinessFromByteCode(pucsFile,
                    provenance, format, langue);
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error("Unable to generate document ", e);
            throw new WebAvsException(e.getMessage(), e);
        } catch (OrionPucsException e) {
            JadeLogger.error("Unable to generate document ", e);
            throw new WebAvsException(e.getMessage(), e);
        }

        return filePath;
    }

    @Override
    public DernierRevenuDeterminantEtBase retrieveDernierRevenuDeterminantEtBase(String numeroAffilie)
            throws WebAvsException {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroDossier is null or empty");
        }

        BSession session = UtilsService.initSession();
        CPDecision decision = null;

        // Retrouver l'affiliation
        AFAffiliation affiliation = getAffiliation(numeroAffilie, session);

        // Retrouver la dernière décision active
        CPDecisionManager decisionManager = new CPDecisionManager();

        BigDecimal revenuDeterminant = new BigDecimal(0);
        BigDecimal revenuBase = new BigDecimal(0);
        try {
            decisionManager.setSession(session);
            decisionManager.setForIdAffiliation(affiliation.getAffiliationId());
            decisionManager.setForIsActive(true);
            decisionManager.setOrder("IAANNE DESC, IAIDEC DESC");
            decisionManager.find(BManager.SIZE_USEDEFAULT);

            if (!decisionManager.isEmpty()) {
                decision = (CPDecision) decisionManager.getFirstEntity();

                // recherche du revenu déterminant
                CPDonneesCalcul donneesCalcul = new CPDonneesCalcul();
                donneesCalcul.setSession(session);
                String revenuDeterminantStr = donneesCalcul.getMontant(decision.getIdDecision(),
                        CPDonneesCalcul.CS_REV_NET);
                if (!JadeStringUtil.isEmpty(revenuDeterminantStr)) {
                    revenuDeterminant = new BigDecimal(JANumberFormatter.deQuote(revenuDeterminantStr));
                }

                // recherche du revenu de base
                CPDonneesBase donneesBase = new CPDonneesBase();
                donneesBase.setSession(session);
                donneesBase.setIdDecision(decision.getIdDecision());
                donneesBase.retrieve();
                if (!donneesBase.isNew()) {
                    if (donneesBase.getRevenu1() != null && !donneesBase.getRevenu1().isEmpty()) {
                        revenuBase = new BigDecimal(Double.valueOf(JANumberFormatter.deQuote(donneesBase.getRevenu1())));
                    }
                }
            }
            return new DernierRevenuDeterminantEtBase(revenuDeterminant, revenuBase);
        } catch (Exception e) {
            throw new WebAvsException("Unable to retrieve decision with affiliate number : " + numeroAffilie, e);
        }
    }

    private String getCotiLibelle(BSession session, AFCotisation coti, String language) throws WebAvsException {
        String libelle = null;

        AFAssurance entityAssurance = new AFAssurance();
        entityAssurance.setSession(session);
        entityAssurance.setAssuranceId(coti.getAssuranceId());

        try {
            entityAssurance.retrieve();

            if (!entityAssurance.isNew()) {
                if ("de".equals(language)) {
                    libelle = entityAssurance.getAssuranceLibelleAl();
                } else if ("it".equals(language)) {
                    libelle = entityAssurance.getAssuranceLibelleIt();
                } else {
                    libelle = entityAssurance.getAssuranceLibelleFr();
                }
            }
        } catch (Exception e) {
            throw new WebAvsException(
                    "Unable to retrieve cotisation label for assurance id : " + coti.getAssuranceId(), e);
        }

        return libelle;
    }

    private AFAffiliation getAffiliation(String numeroAffilie, BSession session) {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroAffilie is null or empty");
        }

        if (session == null) {
            throw new IllegalArgumentException("session is null");
        }

        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setForAffilieNumero(numeroAffilie);
        affiliationManager.setSession(session);

        try {
            affiliationManager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new AFTechnicalException("Impossible de récupérer les affiliation, numéros : " + numeroAffilie, e);
        }
        return (AFAffiliation) affiliationManager.getFirstEntity();
    }

    private InfosDerniereDecisionActive getInfosDerniereDecisionActive(CPDecision decision, CPDonneesBase donneesBase) {
        InfosDerniereDecisionActive infosDerniereDecisionActive = new InfosDerniereDecisionActive();

        if (!decision.isNew() && !JadeStringUtil.isEmpty(decision.getTypeDecision())) {
            infosDerniereDecisionActive.setTypeDecisionAcompte(TypeDecisionAcompteIndConverter
                    .convertTypeDecisionAcompteInd(new Integer(decision.getTypeDecision())));
        }

        if (!donneesBase.isNew()) {
            if (donneesBase.getRevenu1() != null && !donneesBase.getRevenu1().isEmpty()) {
                infosDerniereDecisionActive.setResultatNet(new BigDecimal(Double.valueOf(JANumberFormatter
                        .deQuote(donneesBase.getRevenu1()))));
            }

            if (donneesBase.getCapital() != null && !donneesBase.getCapital().isEmpty()) {
                infosDerniereDecisionActive.setCapitalInvesti(new BigDecimal(Double.valueOf(JANumberFormatter
                        .deQuote(donneesBase.getCapital()))));
            }
        }

        return infosDerniereDecisionActive;
    }

    @Override
    public SalarieResultSearch searchSalaries(String numeroAffilie, Integer annee, String likeNss, String likeNom,
            String likePrenom, Integer from, Integer nb, SalarieOrderBy orderBy, OrderByDirWebAvs orderByDir)
            throws WebAvsException {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroAffilie is null or empty");
        }

        final DateConverter dateConverter = new DateConverter();
        List<Salarie> salaries = new ArrayList<Salarie>();
        Long nbAllRows = 0L;
        Long nbMatchingRows = 0L;
        BSession session = UtilsService.initSession();

        // création du manager
        EBSalarieManager salarieManager = new EBSalarieManager();
        salarieManager.setSession(session);
        salarieManager.setForNumeroAffilie(numeroAffilie);
        if (annee != null && annee != 0) {
            salarieManager.setForAnnee(annee);
        }
        salarieManager.changeManagerSize(BManager.SIZE_NOLIMIT);

        // récupération du nombre total de salariés
        try {
            nbAllRows += salarieManager.getCount();

            // application des filtres et des ordres
            if (!JadeStringUtil.isEmpty(likeNom)) {
                salarieManager.setLikeNom(StringConverter.toUpperWithoutAccent(likeNom));
            }
            if (!JadeStringUtil.isEmpty(likePrenom)) {
                salarieManager.setLikePrenom(StringConverter.toUpperWithoutAccent(likePrenom));
            }
            salarieManager.setOrderBy(orderBy);
            salarieManager.setOrderByDir(orderByDir);

            // exécution de la recherche finale
            salarieManager.find(BManager.SIZE_NOLIMIT);
            nbMatchingRows += salarieManager.getSize();

            // gestion de la limite et de l'offset
            List<EBSalarie> ebSalaries = salarieManager.toList();
            int nbMatchingsRowsInt = nbMatchingRows.intValue();
            int fromIndex = from > nbMatchingsRowsInt ? nbMatchingsRowsInt : from;
            int toIndex = (from + nb) > nbMatchingsRowsInt ? nbMatchingsRowsInt : (from + nb);
            ebSalaries = ebSalaries.subList(fromIndex, toIndex);

            // parcours des salariés
            for (EBSalarie ebSalarie : ebSalaries) {
                // exécution du filtre sur le likeNss -> doit être fait en java en raison de problème de perf en sql
                if (!JadeStringUtil.isEmpty(likeNss)
                        && !JadeStringUtil.startsWith(ebSalarie.getNss(), likeNss.replace(".", ""))) {
                    nbMatchingRows--;
                    continue;
                }
                Salarie salarie = new Salarie();
                salarie.setNomPrenom(ebSalarie.getNomPrenom());

                String nss = null;
                if (!JadeStringUtil.isEmpty(ebSalarie.getNss())) {
                    nss = NSUtil.formatAVSUnknown(ebSalarie.getNss());
                }
                salarie.setNss(nss);

                SexeSalarie sexe = SexeSalarie.valueOf(ebSalarie.getSexeCs());
                salarie.setSexe(sexe);

                if (!JadeStringUtil.isEmpty(ebSalarie.getDateNaissance())) {
                    Date dateNaissance = dateConverter.convert(ebSalarie.getDateNaissance(), null, null).getDate();
                    salarie.setDateNaissance(dateNaissance);
                    salarie.setDateRetraite(calculerDateRetraite(dateNaissance, sexe));
                }

                salaries.add(salarie);
            }
        } catch (Exception e) {
            logger.error("unable to search salarie for affilie " + numeroAffilie);
            throw new WebAvsException("unable to search salarie for affilie " + numeroAffilie, e);
        }

        return new SalarieResultSearch(salaries, nbMatchingRows, nbAllRows);
    }

    private static Date calculerDateRetraite(Date dateNaissance, SexeSalarie sexe) throws ParseException {
        if (dateNaissance == null) {
            throw new IllegalArgumentException("dateNaissance is null !");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateNaissance);

        int anneeNaissance = cal.get(Calendar.YEAR);
        int ageAvs;

        // si femme
        if (sexe.equals(SexeSalarie.FEMME)) {
            if (anneeNaissance <= 1938) {
                ageAvs = anneeNaissance + 62;
            } else {
                if (anneeNaissance <= 1941) {
                    ageAvs = anneeNaissance + 63;
                } else {
                    ageAvs = anneeNaissance + 64;
                }
            }
        }
        // si Homme
        else if (sexe.equals(SexeSalarie.HOMME)) {
            ageAvs = anneeNaissance + 65;
        }
        // si undefined on ne calcul pas la date de retraite
        else {
            return null;
        }

        int month = cal.get(Calendar.MONTH) + 1;
        if (month == 13) {
            month = 1;
            ageAvs++;
        }

        // on construit la date d'age AVS
        String dateRetraite = "01." + (month < 10 ? "0" + month : month) + "." + ageAvs;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.parse(dateRetraite);
    }
}
