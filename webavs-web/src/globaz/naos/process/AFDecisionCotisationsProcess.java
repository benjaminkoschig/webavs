package globaz.naos.process;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.decisionCotisations.AFDecisionCotisationsHtmlOut;
import globaz.naos.db.decisionCotisations.AFDecisionCotisationsLog;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.itext.AFDecisionCotisations_Doc;
import globaz.naos.translation.CodeSystem;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AFDecisionCotisationsProcess extends BProcess {

    private static final long serialVersionUID = -7488147990089362701L;

    private String affiliationId;
    private transient List<AFAffiliation> affiliations;
    private String dateDebut;
    private String dateEnvoi = JACalendar.todayJJsMMsAAAA();
    private String dateFin;
    private String dateImprime = "";
    private AFDecisionCotisationsHtmlOut doc;
    private String fromIdExterneRole = "";
    private List<AFDecisionCotisationsLog> listAffilieLog;
    private List<AFCotisation> listCotisation = null;
    private String planAffiliationId;

    private String tillIdExterneRole = "";

    public AFDecisionCotisationsProcess() {
    }

    public AFDecisionCotisationsProcess(BProcess parent) {
        super(parent);
    }

    public AFDecisionCotisationsProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            affiliations = loadAffiliations(getDateEnvoi());

            if (affiliations == null) {
                getMemoryLog().logMessage(getSession().getLabel("3000"), FWMessage.FATAL, this.getClass().getName());
                abort();
                return false;
            }

            int taille = affiliations.size();
            int counter = 1;
            setProgressScaleValue(taille);

            // document html des log
            doc = new AFDecisionCotisationsHtmlOut();
            doc.setSession(getSession());

            // liste des affiliés logés
            listAffilieLog = new ArrayList<AFDecisionCotisationsLog>();

            for (Iterator<AFAffiliation> affiIterator = affiliations.iterator(); affiIterator.hasNext() && !isAborted();) {

                AFAffiliation affiliation = affiIterator.next();

                setProgressDescription(affiliation.getAffilieNumero() + "<br>" + counter + "/" + affiliations.size()
                        + "<br>");

                String dateDebutFinal = getDateDebutTraite(getDateEnvoi(), affiliation);

                if (dateDebutFinal != null) {

                    String dateFinFinale = getDateFinTraite(affiliation, dateDebutFinal);

                    if (dateFinFinale != null) {

                        List<AFPlanAffiliation> listPlan = getPlanAffiliationSelected(affiliation);

                        for (Iterator<AFPlanAffiliation> planIterator = listPlan.iterator(); planIterator.hasNext();) {

                            AFPlanAffiliation plan = planIterator.next();
                            String masseAnnuelle = getMasseAnnuelle(affiliation, plan, dateDebutFinal);

                            if (masseAnnuelle != null) {

                                listCotisation = null;

                                loadCotisations(affiliation, plan.getId(), dateDebutFinal);

                                if (listCotisation != null) {
                                    AFDecisionCotisations_Doc doc = new AFDecisionCotisations_Doc();
                                    doc.setDateImprime(getDateImprime());
                                    doc.setMasseSalariale(masseAnnuelle);

                                    doc.setCotisation(giveAcompteCotisationTotal(affiliation, getDateEnvoi()));
                                    doc.setPeriodeDebut(dateDebutFinal);
                                    doc.setPeriodeFin(dateFinFinale);
                                    if (getDateEnvoi().length() == 10) {
                                        doc.setAnneeEcran(Integer.toString(JACalendar.getYear(getDateEnvoi())));
                                    } else {
                                        doc.setAnneeEcran(getDateEnvoi());
                                    }

                                    if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
                                        doc.setPeriodicite(getSession().getApplication().getLabel(
                                                "ACOMPTE_TRIMESTRIEL_COTI", affiliation.getTiers().getLangueIso()));
                                    }
                                    if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_MENSUELLE)) {
                                        doc.setPeriodicite(getSession().getApplication().getLabel(
                                                "ACOMPTE_MENSUEL_COTI", affiliation.getTiers().getLangueIso()));
                                    }
                                    if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_ANNUELLE)) {
                                        doc.setPeriodicite(getSession().getApplication().getLabel(
                                                "ACOMPTE_ANNUEL_COTI", affiliation.getTiers().getLangueIso()));
                                    }
                                    doc.setAffilie(affiliation);
                                    doc.setParent(this);
                                    doc.executeProcess();
                                } else {
                                    // log erreur cotisations non chargées
                                    listAffilieLog.add(new AFDecisionCotisationsLog(affiliation.getAffilieNumero(),
                                            affiliation.getTiers().getDesignation1(), affiliation.getTiers()
                                                    .getDesignation2(), plan.getLibelleFacture(), getSession()
                                                    .getLabel("ERREUR_CHARGEMENT_COTISATION")));
                                }

                            } else {
                                // log erreur masseAnnuelle non trouvée
                                listAffilieLog.add(new AFDecisionCotisationsLog(affiliation.getAffilieNumero(),
                                        affiliation.getTiers().getDesignation1(), affiliation.getTiers()
                                                .getDesignation2(), plan.getLibelleFacture(), getSession().getLabel(
                                                "MASSE_ANNUELLE_VIDE")));
                            }
                        }
                    } else {
                        // log erreur dateFin
                        listAffilieLog.add(new AFDecisionCotisationsLog(affiliation.getAffilieNumero(), affiliation
                                .getTiers().getDesignation1(), affiliation.getTiers().getDesignation2(), "",
                                getSession().getLabel("PERIODE_INVALIDE")));
                    }
                } else {
                    // log erreur dateDebut
                    listAffilieLog.add(new AFDecisionCotisationsLog(affiliation.getAffilieNumero(), affiliation
                            .getTiers().getDesignation1(), affiliation.getTiers().getDesignation2(), "", getSession()
                            .getLabel("PERIODE_INVALIDE")));
                }
                counter++;
                setProgressCounter(counter);
            }

            if (isAborted()) {
                setProgressDescription("Traitement interrompu");
            }

            // fusionner tous les documents en 1 seul fichier
            JadePublishDocumentInfo docInfo = createDocumentInfo();
            docInfo.setDocumentTypeNumber(AFDecisionCotisations_Doc.NUM_INFOROM);
            docInfo.setPublishDocument(true);
            docInfo.setArchiveDocument(false);
            this.mergePDF(docInfo, false, 500, false, null);

            if (listAffilieLog.size() > 0) {
                // si que 1 affilié dans la liste on met simplement le message
                // dans le mail
                if (listAffilieLog.size() == 1) {
                    AFDecisionCotisationsLog log = listAffilieLog.get(0);
                    String motifRefus = log.getMotifRefus();
                    getMemoryLog().logMessage(motifRefus, FWMessage.ERREUR, this.getClass().getName());
                    abort();
                    return false;
                } else {
                    // création du fichier HTML de log
                    doc.setData(listAffilieLog);
                    doc.setFilename("listeAffiliesNonTraites.html");
                    if (doc.getOutputFile() != null) {
                        JadePublishDocument publishDoc = new JadePublishDocument(doc.getOutputFile(),
                                createDocumentInfo());
                        this.registerAttachedDocument(publishDoc);
                    }
                }
            }

        } catch (Exception e) {
            JadeLogger.error(this, e);
            return false;
        }
        return true;
    }

    private String findMasseAnnuelle(AFCotisationManager mgr, String dateDebutFinale) throws Exception {
        for (Iterator<?> iter = mgr.iterator(); iter.hasNext();) {
            AFCotisation coti = (AFCotisation) iter.next();
            if (!(JadeStringUtil.isBlankOrZero(coti.getMasseAnnuelle())
                    || BSessionUtil.compareDateFirstGreater(getSession(), coti.getDateDebut(),
                            new JACalendarGregorian().addYears(JACalendar.todayJJsMMsAAAA(), 1))
                    || (!JadeStringUtil.isBlankOrZero(coti.getDateFin()) && !BSessionUtil.compareDateFirstGreater(
                            getSession(), coti.getDateFin(), coti.getDateDebut())) || (!JadeStringUtil
                    .isBlankOrZero(coti.getDateFin()) && (JACalendar.getMonth(coti.getDateFin()) < JACalendar
                    .getMonth("01." + dateDebutFinale))))) {

                return coti.getMasseAnnuelle();
            }
        }
        return null;
    }

    /**
     * méthode permettant de calculer l'acompte de cotisation en fonction de la périodicité de l'affiliation
     * 
     * @param affiliation
     * @param masseAnnuelle
     * @param tauxCotisation
     * @return l'acompte de cotisation ARRONDI A 5CT
     */
    private String getAcompteCotisation(AFAffiliation affiliation, String masseAnnuelle, double tauxCotisation) {
        String acompteCotisation = "";
        if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_TRIMESTRIELLE)) {
            acompteCotisation = String.valueOf((Double.parseDouble(masseAnnuelle) * tauxCotisation) / 4);
        } else if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_MENSUELLE)) {
            acompteCotisation = String.valueOf((Double.parseDouble(masseAnnuelle) * tauxCotisation) / 12);
        } else if (affiliation.getPeriodicite().equals(CodeSystem.PERIODICITE_ANNUELLE)) {
            acompteCotisation = String.valueOf(Double.parseDouble(masseAnnuelle) * tauxCotisation);
        }

        FWCurrency acompteCotisationInCurrency = new FWCurrency(acompteCotisation);
        acompteCotisationInCurrency.round(FWCurrency.ROUND_5CT);

        return acompteCotisationInCurrency.getBigDecimalValue().toString();
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * méthode permettant de trouver la date de début adéquate pour la décision de cotisations sur salaires en fonction
     * de la date d'envoi et la date de début d'adhésion
     * 
     * @param dateEnvoi
     * @param affiliation
     * @return
     * @throws Exception
     */
    private String getDateDebutTraite(String dateEnvoi, AFAffiliation affiliation) throws Exception {

        String dateDebutAdhesion = "";
        int anneeSel = JACalendar.getYear(dateEnvoi);
        // PO5402 - Recherche date début de l'adhésion
        BStatement statement = null;
        try {
            String sql = "";
            statement = new BStatement(affiliation.getSession().getCurrentThreadTransaction());
            statement.createStatement();
            String schema = Jade.getInstance().getDefaultJdbcSchema();
            ResultSet res = null;

            sql = "SELECT MRDDEB FROM " + schema + ".AFAFFIP aff" + " inner join " + schema
                    + ".AFADHEP adh on aff.maiaff=adh.maiaff" + " inner join " + schema
                    + ".AFCOUVP cou on adh.msiplc=cou.msiplc" + " inner join " + schema
                    + ".AFASSUP ass on cou.mbiass=ass.mbiass" + " where aff.maiaff=" + affiliation.getAffiliationId()
                    + " and ass.mbtgen=" + CodeSystem.GENRE_ASS_PARITAIRE + " and mrddeb< " + anneeSel
                    + "1231 and (mrdfin between " + anneeSel + "0000 and " + anneeSel
                    + "1231 or mrdfin=0) and mrddeb<>mrdfin order by adh.MRDDEB ASC";

            res = statement.executeQuery(sql);
            if (res.next()) {
                dateDebutAdhesion = res.getString("MRDDEB");
                // Si date vide (cas éventuel de reprise) => prendre 01+ année demandée
                if (JadeStringUtil.isBlankOrZero(dateDebutAdhesion)) {
                    return "01." + anneeSel;
                } else {
                    dateDebutAdhesion = JadeStringUtil.substring(dateDebutAdhesion, 6, 2) + "."
                            + JadeStringUtil.substring(dateDebutAdhesion, 4, 2) + "."
                            + JadeStringUtil.substring(dateDebutAdhesion, 0, 4);
                }
            }
        } catch (Exception e) {
            dateDebutAdhesion = affiliation.getDateDebut();
        } finally {
            if (statement != null) {
                statement.closeStatement();
            }
        }

        int anneeAdhesion = JACalendar.getYear(dateDebutAdhesion);
        // Si année de début d'adhésion > année demandée => null
        if (anneeAdhesion > anneeSel) {
            return null;
        } else if (anneeAdhesion < anneeSel) {
            // Si année de début adhésion < année demandée => prendre 01+ année demandée
            return "01." + anneeSel;

        } else {
            // Si année de début adhésion < année demandée => prendre mois et année demandée
            return JadeStringUtil.fillWithZeroes(String.valueOf(JACalendar.getMonth(dateDebutAdhesion)), 2) + "."
                    + anneeSel;
        }
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getDateFin() {
        return dateFin;
    }

    // même fonctionnement que getDateDebutTraite() mais pour la date de fin
    private String getDateFinTraite(AFAffiliation affiliation, String debutPeriode) throws Exception {
        String dateFinAdhesion = "";
        int anneeSel = JACalendar.getYear(debutPeriode);
        // PO5402 - Recherche date début de l'adhésion
        BStatement statement = null;
        try {
            String sql = "";
            statement = new BStatement(affiliation.getSession().getCurrentThreadTransaction());
            statement.createStatement();
            String schema = Jade.getInstance().getDefaultJdbcSchema();
            ResultSet res = null;

            // Affecter 99999999 lorsque la date de fin d'adhésion est 0.
            // Permet ainsi de trier par date desc pour avoir la date la plus grande

            sql = "SELECT  DECODE(MRDFIN, 0, 99999999, MRDFIN) as DATFIN FROM " + schema + ".AFAFFIP aff"
                    + " inner join " + schema + ".AFADHEP adh on aff.maiaff=adh.maiaff" + " inner join " + schema
                    + ".AFCOUVP cou on adh.msiplc=cou.msiplc" + " inner join " + schema
                    + ".AFASSUP ass on cou.mbiass=ass.mbiass" + " where aff.maiaff=" + affiliation.getAffiliationId()
                    + " and ass.mbtgen=" + CodeSystem.GENRE_ASS_PARITAIRE + " and mrddeb< " + anneeSel
                    + "1231 and (mrdfin between " + anneeSel + "0000 and " + anneeSel
                    + "1231 or mrdfin=0) and mrddeb<>mrdfin order by DATFIN DESC";

            res = statement.executeQuery(sql);
            // Prendre la date de fin la plus haute ou 0. Comme la plupart du temps la date de fin peut être à 0, le
            // manager est trié par
            // date de fin ascendante.

            if (res.next()) {
                dateFinAdhesion = res.getString("DATFIN");
                if (dateFinAdhesion.equalsIgnoreCase("99999999")) {
                    return "12." + anneeSel;
                } else {
                    dateFinAdhesion = JadeStringUtil.substring(dateFinAdhesion, 6, 2) + "."
                            + JadeStringUtil.substring(dateFinAdhesion, 4, 2) + "."
                            + JadeStringUtil.substring(dateFinAdhesion, 0, 4);
                }
            }
        } catch (Exception e) {
            dateFinAdhesion = affiliation.getDateFin();
        } finally {
            if (statement != null) {
                statement.closeStatement();
            }
        }

        int anneeAdhesion = JACalendar.getYear(dateFinAdhesion);
        // Si année de fin d'adhésion < année demandée => null
        if (anneeAdhesion < anneeSel) {
            return null;
        } else if (anneeAdhesion > anneeSel) {
            // Si année de fin adhésion > année demandée => prendre 12+ année demandée
            return "12." + anneeSel;
        } else {
            // Si année de fin adhésion = année demandée => mois+ année demandée
            return JadeStringUtil.fillWithZeroes(String.valueOf(JACalendar.getMonth(dateFinAdhesion)), 2) + "."
                    + anneeSel;
        }
    }

    public String getDateImprime() {
        return dateImprime;
    }

    @Override
    protected String getEMailObject() {
        if (isOnError()) {
            return getSession().getLabel("MSG_COTISATION_SALAIRE_ECHOUE");
        } else {
            return getSession().getLabel("MSG_COTISATION_SALAIRE_OK");
        }
    }

    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    // méthode permettant de récupérer la masse salariale annuelle depuis une
    // assurance type cotisation avs ai apg
    private String getMasseAnnuelle(AFAffiliation affiliation, AFPlanAffiliation plan, String dateDebutFinale)
            throws Exception {
        String masseAnnuelle = null;
        AFCotisationManager mgr = new AFCotisationManager();
        mgr.setForPlanAffiliationId(plan.getId());
        mgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        mgr.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        mgr.setForAnneeActive(String.valueOf(JACalendar.getYear(dateDebutFinale)));
        mgr.setForDateFin("0");
        mgr.setSession(getSession());
        mgr.find();

        masseAnnuelle = findMasseAnnuelle(mgr, dateDebutFinale);

        if (masseAnnuelle == null) {
            mgr.setForTypeAssurance("");
            mgr.find();
            masseAnnuelle = findMasseAnnuelle(mgr, dateDebutFinale);
        }

        return masseAnnuelle;
    }

    public String getPlanAffiliationId() {
        return planAffiliationId;
    }

    // méthode permettant de récupérer les plans d'affiliation pour une
    // affiliation fournie en paramètre
    private List<AFPlanAffiliation> getPlanAffiliationSelected(AFAffiliation affiliation) throws Exception {

        List<AFPlanAffiliation> plansAffiliation = new ArrayList<AFPlanAffiliation>();
        String id = affiliation.getAffiliationId();
        AFPlanAffiliationManager mgr = new AFPlanAffiliationManager();
        mgr.setSession(getSession());
        mgr.setForAffiliationId(id);
        mgr.setForPlanActif(true);
        mgr.changeManagerSize(BManager.SIZE_NOLIMIT);
        mgr.find();
        if (mgr.size() > 0) {
            for (Iterator<?> iter = mgr.iterator(); iter.hasNext();) {
                AFPlanAffiliation plan = (AFPlanAffiliation) iter.next();
                plansAffiliation.add(plan);
            }
        }
        return plansAffiliation;
    }

    public String getTillIdExterneRole() {
        return tillIdExterneRole;
    }

    private String giveAcompteCotisationTotal(AFAffiliation theAffiliation, String theDateEnvoi) throws Exception {
        double acompteCotisationTotal = 0;

        for (AFCotisation entityCotisation : listCotisation) {

            String theMasseAnnuelle = entityCotisation.getMasseAnnuelle();

            if (theMasseAnnuelle == null) {
                theMasseAnnuelle = "0";
            }

            AFTauxAssurance theTaux = entityCotisation.findTauxWithRecalcul(theDateEnvoi, theMasseAnnuelle, true,
                    false, "");

            double theTauxDouble = 0;
            if (theTaux != null) {
                theTauxDouble = theTaux.getTauxDouble();
            }

            String theAcompteCotisation = getAcompteCotisation(theAffiliation, theMasseAnnuelle, theTauxDouble);

            if (theAcompteCotisation == null) {
                theAcompteCotisation = "0";
            }

            acompteCotisationTotal += Double.valueOf(theAcompteCotisation);
        }
        return String.valueOf(acompteCotisationTotal);
    }

    private boolean isCotisationValide(AFAffiliation theAffiliation, AFCotisation theCotisation, String theDateDebut)
            throws Exception {

        return (theCotisation != null)
                && !(JadeStringUtil.isBlankOrZero(theCotisation.getMasseAnnuelle())
                        || BSessionUtil.compareDateFirstGreater(getSession(), theCotisation.getDateDebut(),
                                new JACalendarGregorian().addYears(JACalendar.todayJJsMMsAAAA(), 1))
                        || (!JadeStringUtil.isBlankOrZero(theCotisation.getDateFin()) && !BSessionUtil
                                .compareDateFirstGreater(getSession(), theCotisation.getDateFin(),
                                        theCotisation.getDateDebut())) || (!JadeStringUtil.isBlankOrZero(theCotisation
                        .getDateFin()) && (JACalendar.getMonth(theCotisation.getDateFin()) < JACalendar.getMonth("01."
                        + theDateDebut))));
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    // méthode permettant de charger les affiliations concernées
    private List<AFAffiliation> loadAffiliations(String dateEnvoi) throws Exception {
        String[] typeAffiliation = new String[2];
        typeAffiliation[0] = CodeSystem.TYPE_AFFILI_EMPLOY;
        typeAffiliation[1] = CodeSystem.TYPE_AFFILI_INDEP_EMPLOY;

        if (affiliations == null) {
            AFAffiliationManager mgr = new AFAffiliationManager();
            mgr.setForTypeAffiliation(typeAffiliation);

            if (JadeStringUtil.isIntegerEmpty(affiliationId)) {
                mgr.setFromAffilieNumero(getFromIdExterneRole());
                mgr.setToAffilieNumero(getTillIdExterneRole());
            } else {
                mgr.setForAffiliationId(affiliationId);
            }

            mgr.forIsTraitement(false);
            mgr.setForIsNotReleveParitaire(Boolean.TRUE);
            mgr.setSession(getSession());
            mgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            mgr.setNotInCodeFacturation(CodeSystem.CODE_FACTU_MONTANT_LIBRE);
            mgr.setOrderBy(AFAffiliationManager.ORDER_AFFILIENUMERO);
            mgr.find();

            if (mgr.size() > 0) {
                affiliations = new ArrayList<AFAffiliation>();
                for (Iterator<?> iterator = mgr.iterator(); iterator.hasNext();) {
                    AFAffiliation aff = (AFAffiliation) iterator.next();
                    if (BSessionUtil.compareDateFirstLower(getSession(), aff.getDateDebut(),
                            new JACalendarGregorian().addYears(JACalendar.todayJJsMMsAAAA(), 1))
                            && (JadeStringUtil.isBlank(aff.getDateFin()) || (BSessionUtil.compareDateFirstLower(
                                    getSession(), aff.getDateDebut(), aff.getDateFin()) && BSessionUtil
                                    .compareDateFirstLower(getSession(), dateEnvoi, aff.getDateFin())))) {

                        if (!AFParticulariteAffiliation.existeParticularite(getSession(), aff.getAffiliationId(),
                                CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL, dateEnvoi)) {
                            affiliations.add(aff);
                        }
                    }
                }
            }
        }
        return affiliations;
    }

    private void loadCotisations(AFAffiliation theAffiliation, String planAffiliationId, String theDateDebut)
            throws Exception {

        if (listCotisation == null) {
            listCotisation = new ArrayList<AFCotisation>();
        }

        AFCotisationManager mgrCotisation = new AFCotisationManager();
        mgrCotisation.setSession(getSession());
        mgrCotisation.setForPlanAffiliationId(planAffiliationId);
        mgrCotisation.setForAnneeActive(String.valueOf(JACalendar.getYear(theDateDebut)));
        mgrCotisation.setForDateFin("0");

        mgrCotisation.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < mgrCotisation.size(); i++) {
            AFCotisation entityCotisation = (AFCotisation) mgrCotisation.getEntity(i);

            if (isCotisationValide(theAffiliation, entityCotisation, theDateDebut)) {
                listCotisation.add(entityCotisation);
            }
        }

    }

    public void setAffiliationId(String string) {
        affiliationId = string;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateEnvoi(String string) {
        dateEnvoi = string;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDateImprime(String newDateImprime) {
        dateImprime = newDateImprime;
    }

    public void setFromIdExterneRole(String fromIdExterneRole) {
        this.fromIdExterneRole = fromIdExterneRole;
    }

    public void setPlanAffiliationId(String string) {
        planAffiliationId = string;
    }

    public void setTillIdExterneRole(String tillIdExterneRole) {
        this.tillIdExterneRole = tillIdExterneRole;
    }
}
