package globaz.orion.process;

import globaz.draco.db.preimpression.DSPreImpressionDeclarationListViewBean;
import globaz.draco.db.preimpression.DSPreImpressionViewBean;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationForDSManager;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationManager;
import globaz.orion.utils.EBDanUtils;
import globaz.pavo.util.CIUtil;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.orion.business.constantes.PreRemplissageStatus;
import ch.globaz.orion.businessimpl.services.dan.DanServiceImpl;
import ch.globaz.xmlns.eb.dan.EBDanException_Exception;

/**
 * Process de pré-remplissage des déclarations de salaire pour l'ebusiness
 * 
 * @author SCO
 * @since 06 avr. 2011
 */
public class EBDanPreRemplissage extends EBAbstractJadeJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String GENRE_CAISSE_LAA = "830004";
    public static final String GENRE_CAISSE_LPP = "830003";
    public final static String SEXE_FEMME_AVS = "316001";
    public final static String SEXE_FEMME_EBUSINESS = "2";
    public final static String SEXE_HOMME_AVS = "316000";
    public final static String SEXE_HOMME_EBUSINESS = "1";

    private String annee = null;
    private StringBuffer bodyMailBuffError = null;

    private StringBuffer bodyMailBuffInfo = null;

    private String dateRadiation = null;
    private String email = null;
    private String numAffilie = null;
    private Boolean preRemplissageForRadiation = new Boolean(false);
    private String subjectMail = null;

    private String typeDeclaration = null;

    public void addMailError(String message) {
        if (bodyMailBuffError == null) {
            bodyMailBuffError = new StringBuffer();
        }

        bodyMailBuffError.append(message + "\n");
    }

    public void addMailInfo(String message) {
        if (bodyMailBuffInfo == null) {
            bodyMailBuffInfo = new StringBuffer();
        }

        bodyMailBuffInfo.append(message + "\n");
    }

    private void checkDataEntry() throws Exception {

        if (getSession() == null) {
            throw new Exception("Unabled to execute the process, session is null");
        }

        if (JadeStringUtil.isEmpty(getAnnee())) {
            throw new Exception("Unabled to execute the process, annee is null or empty");
        }

        if (JadeStringUtil.isEmpty(getNumAffilie()) && JadeStringUtil.isEmpty(getTypeDeclaration())) {
            throw new Exception("Unabled to execute the process, type declaration or Numero affilie is null or empty");
        }
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateRadiation() {
        return dateRadiation;
    }

    public String getDayForRadiation(String dateRadiation) {
        if (dateRadiation.length() == 10) {
            return dateRadiation.substring(0, 2);
        }
        return "";
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("PROCESS_PRE_REMPLISSAGE_DAN");
    }

    public String getEmail() {
        return email;
    }

    public String getMonthForRadiation(String dateRadiation) {
        if (dateRadiation.length() == 10) {
            return dateRadiation.substring(3, 5);
        }
        return "";
    }

    @Override
    public String getName() {
        return getSession().getLabel("PROCESS_PRE_REMPLISSAGE_DAN");
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public Boolean getPreRemplissageForRadiation() {
        return preRemplissageForRadiation;
    }

    public String getTypeDeclaration() {
        return typeDeclaration;
    }

    @Override
    protected void process() throws Exception {

        // On regarde les infos en entrée
        checkDataEntry();

        try {

            // Recherche des affiliés sujet au pré remplissage.
            AFAffiliationForDSManager affManager = new AFAffiliationForDSManager();
            affManager.setSession(getSession());

            // Type d'affiliation
            affManager.setForTypeFacturation(AFAffiliationManager.PARITAIRE);

            // On fixe la date de début et la date de fin
            Map<String, String> date = EBDanUtils.calculBorneDate(getAnnee());
            if (preRemplissageForRadiation && (dateRadiation != null)) {
                affManager.setForDateFin(dateRadiation);
            } else {
                affManager.setFromDateFin(date.get("dateFin"));
            }
            affManager.setForTillDateDebut(date.get("dateDebut"));

            // Si un numéro d'affilié est renseigné, on le spécifie
            if (!JadeStringUtil.isEmpty(getNumAffilie())) {
                affManager.setFromAffilieNumero(getNumAffilie());
                affManager.setToAffilieNumero(getNumAffilie());
            } else {
                // /On fixe le type de déclaration car on veut tous les affiliés suivant ce type de déclaration
                affManager.setForTypeDeclaration(getTypeDeclaration());
            }

            // On effectue la recherche
            affManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            // Progress bar
            getProgressHelper().setMax(affManager.size());

            for (int i = 0; i < affManager.size(); i++) {
                String instLaa = "";
                String instLpp = "";

                // Gestion de l'annulation du process
                if (isAborted()) {
                    return;
                }

                AFAffiliation aff = (AFAffiliation) affManager.getEntity(i);

                // Information pour le process
                getProgressHelper().setCurrent(i);

                // Création de l'object DAN
                ch.globaz.xmlns.eb.dan.Dan dan = new ch.globaz.xmlns.eb.dan.Dan();
                // dan.setIdAffilie(aff.getAffiliationId());
                // dan.setNumeroAffilie(aff.getAffilieNumero());
                dan.setAnnee(Integer.parseInt(getAnnee()));

                // ****************************************
                // Récupération des insitutions LAA/LPP
                // ****************************************

                // On s'occupe du suivi LAA
                AFSuiviCaisseAffiliationManager caisseLaaManager = new AFSuiviCaisseAffiliationManager();
                caisseLaaManager.setSession(getSession());
                caisseLaaManager.setForAffiliationId(aff.getAffiliationId());
                caisseLaaManager.setForGenreCaisse(EBDanPreRemplissage.GENRE_CAISSE_LAA);
                caisseLaaManager.setOrder("MYDDEB desc");
                caisseLaaManager.setForAnnee(annee);
                caisseLaaManager.find();

                if (!caisseLaaManager.isEmpty()) {
                    AFSuiviCaisseAffiliation caisse = (AFSuiviCaisseAffiliation) caisseLaaManager.getFirstEntity();
                    instLaa = caisse.getIdTiersCaisse();
                }

                // On s'occupe du suivi LPP
                AFSuiviCaisseAffiliationManager caisseLppManager = new AFSuiviCaisseAffiliationManager();
                caisseLppManager.setSession(getSession());
                caisseLppManager.setForAffiliationId(aff.getAffiliationId());
                caisseLppManager.setForGenreCaisse(EBDanPreRemplissage.GENRE_CAISSE_LPP);
                caisseLppManager.setOrder("MYDDEB desc");
                caisseLppManager.setForAnnee(annee);
                caisseLppManager.find();

                if (!caisseLppManager.isEmpty()) {
                    AFSuiviCaisseAffiliation caisse = (AFSuiviCaisseAffiliation) caisseLppManager.getFirstEntity();
                    instLpp = caisse.getIdTiersCaisse();
                }

                // Récupération du canton par defaut pour cet affilié
                String codeCanton = EBDanUtils.retrieveCodeCantonOFASForAff(aff, "31.12." + annee);
                dan.setCanton(Integer.parseInt(codeCanton));

                // ****************************************
                // Recherche des salariés
                // ****************************************

                // Récupération des salariés de l'affilié
                DSPreImpressionDeclarationListViewBean dsManager = new DSPreImpressionDeclarationListViewBean();
                dsManager.setSession(getSession());
                dsManager.setAnnee(annee);
                dsManager.setForAffiliesNumero(aff.getAffiliationId());
                dsManager.find(getTransaction(), BManager.SIZE_NOLIMIT);

                List<ch.globaz.xmlns.eb.dan.Salaire> listeSalaire = new ArrayList<ch.globaz.xmlns.eb.dan.Salaire>();

                for (int j = 0; j < dsManager.size(); j++) {
                    DSPreImpressionViewBean dsPre = (DSPreImpressionViewBean) dsManager.getEntity(j);

                    ch.globaz.xmlns.eb.dan.Salaire salaire = new ch.globaz.xmlns.eb.dan.Salaire();

                    salaire.setNom(dsPre.getNomPrenom());

                    // Parser la date de naissance
                    int dateNaiss = Integer.parseInt(CIUtil.formatDateAMJ(dsPre.getDateNaissance()));
                    salaire.setDateNaissance(dateNaiss);

                    // On set le canton
                    salaire.setIdCanton(Integer.parseInt(codeCanton));

                    // On set le code sexe
                    salaire.setSexe(EBDanUtils.returnCodeSexeEBusiness(dsPre.getSexe()));

                    // Récupération du nss
                    salaire.setNss(EBDanUtils.checkAnReturnNSS(getSession(), dsPre.getNumeroAvs()));

                    // //////////////////////
                    // Calcul des periodes
                    // //////////////////////
                    JADate dateEngagement = JADate.newDateFromAMJ(dsPre.getDateEngagement());
                    JADate dateAnniversaire = new JADate(dsPre.getDateNaissance());
                    DecimalFormat df = new DecimalFormat("00");

                    // On regarde si l'année courante est l'année de retraite du salarié
                    if (EBDanUtils.isAnneeRetraite(getAnnee(), dsPre.getSexe(), dsPre.getDateNaissance())) {
                        // On détermine le jour de retraite
                        String dernierJourTravail = df.format(EBDanUtils.lastDayInMonth(dsPre.getDateNaissance()))
                                + "." + df.format(dateAnniversaire.getMonth()) + "." + getAnnee();

                        // Si la date de début est après la date de retraite => création d'une seule périod || date de
                        // && pas supérieur à la radiation
                        if (BSessionUtil.compareDateFirstGreater(getSession(),
                                JACalendar.format(dateEngagement, JACalendar.FORMAT_DDsMMsYYYY), dernierJourTravail)) {

                            salaire.setJourDebut(df.format(dateEngagement.getDay()));
                            salaire.setMoisDebut(df.format(dateEngagement.getMonth()));
                            listeSalaire.add(salaire);

                        } else {

                            // 1er Periode
                            // -----------------
                            if (dateEngagement.getDay() != 0) {
                                // On set la date du début uniquement quand on la connait
                                salaire.setJourDebut(df.format(dateEngagement.getDay()));
                                salaire.setMoisDebut(df.format(dateEngagement.getMonth()));

                            }

                            salaire.setJourFin(df.format(EBDanUtils.lastDayInMonth(dsPre.getDateNaissance())));
                            salaire.setMoisFin(df.format(dateAnniversaire.getMonth()));
                            if (preRemplissageForRadiation) {
                                salaire.setJourFin(getDayForRadiation(aff.getDateFin()));
                                salaire.setMoisFin(getMonthForRadiation(aff.getDateFin()));
                            }

                            // Ajout de la période
                            listeSalaire.add(salaire);

                            // 2ieme Perriode
                            // -----------------
                            // Pas de 2e période si dernier jour travail <= date de radiation
                            if ((dateAnniversaire.getMonth() != 12)
                                    && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dernierJourTravail,
                                            aff.getDateFin())) {

                                ch.globaz.xmlns.eb.dan.Salaire salaireRetraite = new ch.globaz.xmlns.eb.dan.Salaire();
                                salaireRetraite.setDateNaissance(salaire.getDateNaissance());
                                salaireRetraite.setIdCanton(salaire.getIdCanton());
                                salaireRetraite.setNom(salaire.getNom());
                                salaireRetraite.setNss(salaire.getNss());
                                salaireRetraite.setSexe(salaire.getSexe());

                                // Si le mois est le mois de décembre, il n'a pas de 2ieme période
                                int mois = dateAnniversaire.getMonth() + 1;
                                salaireRetraite.setJourDebut("01");
                                salaireRetraite.setMoisDebut(df.format(mois));
                                if (mois == 12) {
                                    // le mois de cette période est décembre, on peut donc mettre directement la date de
                                    // fin
                                    salaireRetraite.setJourFin("31");
                                    salaireRetraite.setMoisFin("12");
                                }

                                // Ajout de la période
                                listeSalaire.add(salaireRetraite);
                            }
                        }
                    } else {

                        // Il n'a pas sa retraite dans l'année
                        if (dateEngagement.getDay() != 0) {
                            // On set la date du début uniquement quand on la connait
                            salaire.setJourDebut(df.format(dateEngagement.getDay()));
                            salaire.setMoisDebut(df.format(dateEngagement.getMonth()));
                            if (preRemplissageForRadiation) {
                                salaire.setJourFin(getDayForRadiation(aff.getDateFin()));
                                salaire.setMoisFin(getMonthForRadiation(aff.getDateFin()));
                            }
                        }

                        // Ajout de la période
                        listeSalaire.add(salaire);
                    }

                }

                // ****************************************
                // Insertion en base ebusiness grace aux services
                // ****************************************
                boolean override = false;
                if ((JadeStringUtil.isEmpty(getNumAffilie()) == false) && (getPreRemplissageForRadiation() == false)) {
                    override = true;
                }
                try {
                    DanServiceImpl.preRempliDan(dan, aff.getAffilieNumero(), listeSalaire, Integer.parseInt(annee),
                            instLaa, instLpp, getSession().getUserId(), override, getSession());
                } catch (EBDanException_Exception e) {
                    List<String> errors = (e.getFaultInfo().getErrors());
                    for (String erreur : errors) {
                        if (bodyMailBuffError == null) {
                            bodyMailBuffError = new StringBuffer();
                        }
                        bodyMailBuffError.append(erreur + "\n");
                    }
                }

            }

        } catch (Exception e) {

            addMailError(getSession().getLabel("BODY_ERROR"));

            String messageInformation = "\n\n***** INFORMATIONS POUR GLOBAZ *****\n";
            messageInformation += "Annee : " + getAnnee() + "\n";
            messageInformation += "Type déclaration : " + getTypeDeclaration() + "\n";
            messageInformation += EBDanUtils.stack2string(e);

            addMailError(messageInformation);

        } finally {
            sendEmail();
        }
    }

    private void sendEmail() {
        StringBuffer body = new StringBuffer();

        // Construction du sujet du message
        if (isAborted()) {
            subjectMail = getSession().getLabel("SUBJECT_MAIL_ABORT");
        } else if (bodyMailBuffError != null) {
            subjectMail = getSession().getLabel("SUBJECT_MAIL_ERROR");
        } else if (!JadeStringUtil.isEmpty(getNumAffilie()) && (bodyMailBuffInfo != null)) {
            subjectMail = getSession().getLabel("SUBJECT_MAIL_ERROR");
        } else {
            subjectMail = getSession().getLabel("SUBJECT_MAIL_SUCCESS");
        }

        // Construction du corps du message
        if (bodyMailBuffError != null) {
            body.append("Error(s) : \n");
            body.append(bodyMailBuffError);
            body.append("\n\n");
        }

        if (bodyMailBuffInfo != null) {
            body.append("Information(s) : \n");
            body.append(bodyMailBuffInfo);
        }

        JadeSmtpClient mail = JadeSmtpClient.getInstance();
        try {
            mail.sendMail(getEmail(), subjectMail, body.toString(), null);
        } catch (Exception e) {
            JadeLogger.error("Unabled to send mail to " + getEmail(), e);
        }
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateRadiation(String dateRadiation) {
        this.dateRadiation = dateRadiation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setPreRemplissageForRadiation(Boolean preRemplissageForRadiation) {
        this.preRemplissageForRadiation = preRemplissageForRadiation;
    }

    public void setTypeDeclaration(String typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

    private void traiteStatus(PreRemplissageStatus status, String numAffilie) {
        if (PreRemplissageStatus.COMPTE_EBUSINESS_ABSENT.equals(status)) {
            addMailInfo("- " + numAffilie + " - " + getSession().getLabel("STATUS_COMPTE_EBUSINESS_ABSENT"));
        } else if (PreRemplissageStatus.PRE_REMPLISSAGE_DAN_PRESENTE.equals(status)) {
            addMailInfo("- " + numAffilie + " - " + getSession().getLabel("STATUS_DAN_EXISTANTE"));
        } else if (PreRemplissageStatus.PRE_REMPLISSAGE_DAN_KO.equals(status)) {
            addMailInfo("- " + numAffilie + " - " + getSession().getLabel("STATUS_PRE_REMPLISSAGE_ERROR"));
        }
    }

}
