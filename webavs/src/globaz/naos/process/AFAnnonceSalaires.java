package globaz.naos.process;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.itext.masse.AFAnnonceSalaires_Doc;
import globaz.naos.translation.CodeSystem;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Cree les debuts de suivis pour les documents d'annonces de salaires.
 * </p>
 * 
 * <p>
 * Genere les documents pour tous les affilies non radies qui ont la case 'envois automatiques des annonces de salaires'
 * ou pour l'affilie dont l'identifiant est renseigne.
 * </p>
 * 
 * @author vre
 */
public class AFAnnonceSalaires extends BProcess {

    private static final long serialVersionUID = 2745167626586554687L;

    private String affiliationId;
    private transient List affiliations;
    private String dateDebut;
    private String dateEnvoi = JACalendar.todayJJsMMsAAAA();
    private String dateFin;
    private String dateRetour;
    private String fromIdExterneRole = "";
    private boolean impressionMasse = false;

    private String periode;
    private String planAffiliationId;

    private String tillIdExterneRole = "";

    /**
     * Crée une nouvelle instance de la classe AFAnnonceSalaires.
     */
    public AFAnnonceSalaires() {
    }

    /**
     * Crée une nouvelle instance de la classe AFAnnonceSalaires.
     * 
     * @param parent
     */
    public AFAnnonceSalaires(BProcess parent) {
        super(parent);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe AFAnnonceSalaires.
     * 
     * @param session
     */
    public AFAnnonceSalaires(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        try {
            AFAnnonceSalaires_Doc annonceSalaires_Doc;
            AFPlanAffiliationManager plansManager = new AFPlanAffiliationManager();

            plansManager.setSession(getSession());

            loadAffiliations();

            if (affiliations.isEmpty()) {
                log(getSession().getLabel("NAOS_PAS_DE_FORMULAIRES"), FWMessage.AVERTISSEMENT);
            } else {
                if (JACalendar.isNull(getDateFin().toString())) {
                    log("La date de période est obligatoire", FWMessage.AVERTISSEMENT);
                }

                // charger les affiliations dont on veut generer les debuts de
                // suivis
                setProgressScaleValue(affiliations.size());
                int cpt = 0;
                for (Iterator iter = affiliations.iterator(); iter.hasNext();) {
                    AFAffiliation affiliation = (AFAffiliation) iter.next();
                    // Dernier en cours de traitement
                    cpt++;
                    setProgressDescription(affiliation.getAffilieNumero() + "<br>" + cpt + "/" + affiliations.size()
                            + "<br>");
                    if (isAborted()) {
                        setProgressDescription("Traitement interrompu<br>Prochain numéro à traiter : "
                                + affiliation.getAffilieNumero() + "<br>" + cpt + "/" + affiliations.size() + "<br>");
                        break;
                    } else {

                        incProgressCounter();

                        // Test s'il a du personnel
                        if (AFParticulariteAffiliation.existeParticulariteDateDonnee(getSession(),
                                affiliation.getAffiliationId(), CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL, "01."
                                        + getDateFin())) {
                            continue;
                        }

                        // si periodicité annuelle,
                        // annonceSalaires_Doc.resetFull();

                        // determiner les dates de debut et de fin de la periode
                        // du document
                        // si date de début rensignée, il s'agit d'une
                        // impression isolée -> garder les dates
                        // si aucune date de début, impression de masse ->
                        // calculer les dates
                        JADate dateFin = new JADate(this.dateFin);
                        JADate dateDebut, date;

                        if (!JadeStringUtil.isEmpty(this.dateDebut)) {
                            // impression manuelle
                            dateDebut = new JADate(this.dateDebut);
                        } else {
                            // impression de masse pour une période
                            impressionMasse = true;
                            // determiner les dates de debut et de fin de la
                            // periode du document
                            JADate periode = new JADate(this.dateFin);
                            int year = periode.getYear();

                            if (CodeSystem.PERIODICITE_MENSUELLE.equals(affiliation.getPeriodicite())) {
                                dateDebut = new JADate(1, periode.getMonth(), year);
                            } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(affiliation.getPeriodicite())) {
                                int trimestre = (periode.getMonth() - 1) / 3;
                                int moisDebut = (trimestre * 3) + 1;
                                dateDebut = new JADate(1, moisDebut, year);
                            } else {
                                dateDebut = new JADate(1, 1, year);
                                dateFin = new JADate(31, 12, year);
                            }
                        }

                        // tenir compte des dates d'affiliation
                        date = new JADate(affiliation.getDateDebut());

                        if (calendar().compare(date, dateFin) == JACalendar.COMPARE_FIRSTUPPER) {
                            // l'affiliation n'est pas encore active pour cette
                            // période
                            continue;
                        } else if (calendar().compare(date, dateDebut) == JACalendar.COMPARE_FIRSTUPPER) {
                            dateDebut = date;
                        }

                        date = new JADate(affiliation.getDateFin());

                        if (!JACalendar.isNull(date)) {
                            if (calendar().compare(date, dateDebut) == JACalendar.COMPARE_FIRSTLOWER) {
                                /*
                                 * il y a une date de fin pour cette affiliation, ce qui signifie probablement que
                                 * l'affilie est radié. On ne veut pas traiter les formulaires pour ces affilies la ...
                                 */
                                continue;
                            } else if (calendar().compare(date, dateFin) == JACalendar.COMPARE_FIRSTLOWER) {
                                dateFin = date;
                            }
                        }

                        if (JadeStringUtil.isEmpty(planAffiliationId)) {
                            // pas d'impression pour un plan précis, itérer sur
                            // chaque plan

                            // charger les plans d'affiliations pour cet affilie
                            plansManager.setForAffiliationId(affiliation.getAffiliationId());
                            plansManager.find();

                            for (int idPlan = 0; idPlan < plansManager.size(); ++idPlan) {
                                AFPlanAffiliation planAffiliation = (AFPlanAffiliation) plansManager.get(idPlan);

                                // créer le document
                                // annonceSalaires_Doc.resetPlan();
                                annonceSalaires_Doc = new AFAnnonceSalaires_Doc(getSession());
                                // annonceSalaires_Doc.setSession(getSession());
                                annonceSalaires_Doc.setDateEnvoi(dateEnvoi);
                                annonceSalaires_Doc.setDateRetour(dateRetour);
                                annonceSalaires_Doc.setImpressionMasse(impressionMasse);

                                Boolean bloquerEnvoi = planAffiliation.isBlocageEnvoi();
                                if (bloquerEnvoi == null) {
                                    bloquerEnvoi = new Boolean(false);
                                }
                                annonceSalaires_Doc.setBloquerEnvoi(bloquerEnvoi.booleanValue());
                                creerDocument(annonceSalaires_Doc, affiliation.getAffiliationId(),
                                        planAffiliation.getPlanAffiliationId(), dateDebut.toStr("."),
                                        dateFin.toStr("."));

                            }
                        } else {

                            AFPlanAffiliation planAffiliation = new AFPlanAffiliation();
                            planAffiliation.setSession(getSession());
                            planAffiliation.setPlanAffiliationId(planAffiliationId);
                            planAffiliation.retrieve();

                            annonceSalaires_Doc = new AFAnnonceSalaires_Doc(getSession());

                            Boolean bloquerEnvoi = planAffiliation.isBlocageEnvoi();
                            if (bloquerEnvoi == null) {
                                bloquerEnvoi = new Boolean(false);
                            }
                            annonceSalaires_Doc.setBloquerEnvoi(bloquerEnvoi.booleanValue());

                            // annonceSalaires_Doc.setSession(getSession());
                            annonceSalaires_Doc.setDateEnvoi(dateEnvoi);
                            annonceSalaires_Doc.setDateRetour(dateRetour);
                            annonceSalaires_Doc.setImpressionMasse(impressionMasse);
                            creerDocument(annonceSalaires_Doc, affiliation.getAffiliationId(), planAffiliationId,
                                    dateDebut.toStr("."), dateFin.toStr("."));
                        }
                    }
                }// fin de la boucle sur les affililés
            }

            this.mergePDF(createDocumentInfo(), true, 0, false, null);

            return true;
        } catch (Exception e) {
            this._addError("erreur durant la génération des documents: " + e.getMessage());
            abort();

            return false;
        }
    }

    private JACalendar calendar() throws Exception {
        return getSession().getApplication().getCalendar();
    }

    /**
     * démarre le suivi pour un document.
     * 
     * @param annonceSalaires_Doc
     *            DOCUMENT ME!
     * @param affiliationId
     *            DOCUMENT ME!
     * @param planAffiliationId
     *            DOCUMENT ME!
     * @param dateDebut
     *            DOCUMENT ME!
     * @param dateFin
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    private void creerDocument(AFAnnonceSalaires_Doc annonceSalaires_Doc, String affiliationId,
            String planAffiliationId, String dateDebut, String dateFin) throws Exception {
        annonceSalaires_Doc.setParent(this);
        annonceSalaires_Doc.setDateDebut(dateDebut);
        annonceSalaires_Doc.setDateFin(dateFin);
        annonceSalaires_Doc.setAffiliationId(affiliationId);
        annonceSalaires_Doc.setPlanAffiliationId(planAffiliationId);
        annonceSalaires_Doc.setDateEnvoi(dateEnvoi);
        annonceSalaires_Doc.executeProcess();
    }

    protected String format(String message, String[] args) {
        StringBuffer msgBuf = new StringBuffer();

        msgBuf.append(message.charAt(0));

        for (int idChar = 1; idChar < message.length(); ++idChar) {
            if ((message.charAt(idChar - 1) == '\'') && (message.charAt(idChar) != '\'')) {
                msgBuf.append('\'');
            }

            msgBuf.append(message.charAt(idChar));
        }

        return MessageFormat.format(msgBuf.toString(), args).toString();
    }

    /**
     * getter pour l'attribut affiliation id.
     * 
     * @return la valeur courante de l'attribut affiliation id
     */
    public String getAffiliationId() {
        return affiliationId;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date envoi.
     * 
     * @return la valeur courante de l'attribut date envoi
     */
    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDateRetour() {
        return dateRetour;
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("NAOS_ANNONCE_SALAIRES_NOMDOC");
    }

    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    public String getPeriode() {
        return periode;
    }

    /**
     * getter pour l'attribut plan affiliation.
     * 
     * @return la valeur courante de l'attribut date envoi
     */
    public String getPlanAffiliationId() {
        return planAffiliationId;
    }

    public String getTillIdExterneRole() {
        return tillIdExterneRole;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    /**
     * charge tous les affilies a qui l'on envoie les documents automatiquement.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    private List loadAffiliations() throws Exception {

        String[] typeAffiliation = new String[3];
        typeAffiliation[0] = CodeSystem.TYPE_AFFILI_INDEP_EMPLOY;
        typeAffiliation[1] = CodeSystem.TYPE_AFFILI_EMPLOY;
        typeAffiliation[2] = CodeSystem.TYPE_AFFILI_EMPLOY_D_F;
        if (affiliations == null) {
            if (JadeStringUtil.isIntegerEmpty(affiliationId)) {
                // rechercher les affilies
                AFAffiliationManager affiliation = new AFAffiliationManager();
                if (!JADate.getMonth(dateFin).equals(new BigDecimal(3))
                        && !JADate.getMonth(dateFin).equals(new BigDecimal(6))
                        && !JADate.getMonth(dateFin).equals(new BigDecimal(9))
                        && !JADate.getMonth(dateFin).equals(new BigDecimal(12))) {
                    affiliation.setForPeriodicite(CodeSystem.PERIODICITE_MENSUELLE);

                } else {
                    // on exclut les annuelles qui passent pas les déclarations
                    // anuelles nominatives
                    affiliation.setForPeriodiciteIn(new String[] { CodeSystem.PERIODICITE_MENSUELLE,
                            CodeSystem.PERIODICITE_TRIMESTRIELLE });
                }
                affiliation.setForTypeAffiliation(typeAffiliation);
                affiliation.forEnvoiAutoAnnSal(Boolean.TRUE); // avec envois
                // automatique
                // des annonces
                affiliation.setFromDateFin(dateFin);
                affiliation.setFromAffilieNumero(getFromIdExterneRole());
                affiliation.setToAffilieNumero(getTillIdExterneRole());
                affiliation.forIsTraitement(false);
                affiliation.setSession(getSession());
                affiliation.changeManagerSize(BManager.SIZE_NOLIMIT);
                affiliation.find();

                affiliations = affiliation.getContainer();
            } else {
                LinkedList retValue = new LinkedList();
                AFAffiliation affiliation = new AFAffiliation();

                affiliation.setAffiliationId(affiliationId);
                affiliation.setSession(getSession());
                affiliation.retrieve();
                retValue.add(affiliation);

                affiliations = retValue;
            }
        }

        return affiliations;
    }

    private void log(String message, String type) {
        getMemoryLog().logMessage(message, type, this.getClass().getName());
    }

    /**
     * setter pour l'attribut affiliation id.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setAffiliationId(String string) {
        affiliationId = string;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * setter pour l'attribut date envoi.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateEnvoi(String string) {
        dateEnvoi = string;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDateRetour(String dateRetour) {
        this.dateRetour = dateRetour;
    }

    public void setFromIdExterneRole(String fromIdExterneRole) {
        this.fromIdExterneRole = fromIdExterneRole;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    /**
     * setter pour l'attribut plan affiliation.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setPlanAffiliationId(String string) {
        planAffiliationId = string;
    }

    public void setTillIdExterneRole(String tillIdExterneRole) {
        this.tillIdExterneRole = tillIdExterneRole;
    }
}
