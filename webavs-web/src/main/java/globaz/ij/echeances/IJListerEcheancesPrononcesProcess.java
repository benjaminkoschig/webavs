/*
 * Créé le 26 janv. 07
 */
package globaz.ij.echeances;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWLog;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BManager;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.globall.util.JAVector;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.application.IJApplication;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJPrononceManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.lyra.process.LYAbstractDocumentGenerator;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <H1>Description</H1>
 * 
 * Classe exécutée depuis LYRA (Echéances) pour créer un document pdf listant tous les prononcés communiqués et actifs
 * dont la date de début est antérieure de 2 ans à celle du mois traité.
 * 
 * @author hpe
 */
public class IJListerEcheancesPrononcesProcess extends LYAbstractDocumentGenerator {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String FICHIER_MODELE = "IJ_LISTE_ECHEANCES_PRONONCES";
    private static final String FICHIER_RESULTAT = "listeEcheancesPrononces";

    private int count = 0;
    private FWMemoryLog journalLog = new FWMemoryLog();
    private List<Map<String, String>> lignes;
    int nbListe = 0;

    public IJListerEcheancesPrononcesProcess(BProcess parent) throws FWIException {
        super(parent, IJApplication.APPLICATION_IJ_REP, IJListerEcheancesPrononcesProcess.FICHIER_RESULTAT);
    }

    public IJListerEcheancesPrononcesProcess(BSession session) throws FWIException {
        super(session, IJApplication.APPLICATION_IJ_REP, IJListerEcheancesPrononcesProcess.FICHIER_RESULTAT);
    }

    @Override
    protected void _validate() throws Exception {

        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {

            journalLog.logMessage(getSession().getLabel("EMAIL_NON_RENSEIGNE"), FWMessage.ERREUR,
                    "IJListerEcheancesPrononcesProcess");
            getMemoryLog().logMessage(getSession().getLabel("EMAIL_NON_RENSEIGNE"), FWMessage.ERREUR,
                    "IJListerEcheancesPrononcesProcess");
            setSendCompletionMail(false);
            setSendMailOnError(true);
        }

        if (JadeStringUtil.isBlank(getMoisTraitement())) {

            journalLog.logMessage(getSession().getLabel("DATE_ECHEANCE_NON_RENSEIGNEE"), FWMessage.ERREUR,
                    "IJListerEcheancesPrononcesProcess");
            getMemoryLog().logMessage(getSession().getLabel("DATE_ECHEANCE_NON_RENSEIGNEE"), FWMessage.ERREUR,
                    "IJListerEcheancesPrononcesProcess");
            setSendCompletionMail(false);
            setSendMailOnError(true);
        }

        if (getSession().hasErrors()) {

            journalLog.logMessage(getSession().getErrors().toString(), FWMessage.ERREUR,
                    "IJListerEcheancesPrononcesProcess");
            getMemoryLog().logMessage(getSession().getErrors().toString(), FWMessage.ERREUR,
                    "IJListerEcheancesPrononcesProcess");
            setSendCompletionMail(false);
            setSendMailOnError(true);
        }

        if (!journalLog.hasMessages()) {
            journalLog.logMessage(getSession().getLabel("EXECUTION_OK"), FWMessage.INFORMATION, "");
        }

        if (journalLog.hasMessages()) {
            FWLog log = journalLog.saveToFWLog(getTransaction());

            setIdLog(log.getIdLog());
        }
    }

    @Override
    public void afterBuildReport() {

        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.PRONONCES_2_ANS_PLUS_IJ);

        JadePublishDocumentInfo docInfo = getDocumentInfo();

        JADate asJADate = new JADate();
        asJADate.setMonth(Integer.parseInt(getMoisTraitement().substring(0, 2)));
        asJADate.setYear(Integer.parseInt(getMoisTraitement().substring(3)));

        Calendar cal = Calendar.getInstance();

        cal.set(asJADate.getYear(), asJADate.getMonth() - 1, 1);
        DateFormat df = PRDateFormater.getDateFormatInstance(getSession(), " MMMM yyyy ");

        try {

            docInfo.setDocumentSubject(getSession().getLabel("TITRE_EMAIL_PRONONCES_2ANS") + df.format(cal.getTime()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.afterBuildReport();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {

        try {
            Map<String, String> parametres = getImporter().getParametre();

            if (parametres == null) {
                parametres = new HashMap<String, String>();
                getImporter().setParametre(parametres);
            } else {
                parametres.clear();
            }

            // le titre
            // ---------------------------------------------------------------------------------------------------

            JADate asJADate = new JADate();
            asJADate.setMonth(Integer.parseInt(getMoisTraitement().substring(0, 2)));
            asJADate.setYear(Integer.parseInt(getMoisTraitement().substring(3)));

            Calendar cal = Calendar.getInstance();

            cal.set(asJADate.getYear(), asJADate.getMonth() - 1, 1);
            DateFormat df = PRDateFormater.getDateFormatInstance(getSession(), " MMMM yyyy ");

            parametres.put("PARAM_TITRE",
                    getSession().getLabel("TITRE_DOCUMENT_PRONONCES_2ANS") + df.format(cal.getTime()));

            // Le n° de caisse
            // ---------------------------------------------------------------------------------------------------
            parametres.put("PARAM_NO_CAISSE",
                    CaisseHelperFactory.getInstance().getNoCaisse(getSession().getApplication()) + "."
                            + CaisseHelperFactory.getInstance().getNoAgence(getSession().getApplication()));

            // La date du jour
            // ---------------------------------------------------------------------------------------------------
            Date auj = new Date();
            DateFormat dfauj = PRDateFormater.getDateFormatInstance(getSession(), "dd.MM.yyyy ");
            parametres.put("PARAM_DATE", dfauj.format(auj));

            // les en-têtes de colonne
            // ---------------------------------------------------------------------------------------
            parametres.put("PARAM_NO_PRONONCE", getSession().getLabel("LISTE_FORMULAIRE_NO_PRONONCE"));
            parametres.put("PARAM_NSS", getSession().getLabel("LISTE_FORMULAIRE_DETAIL_REQUERANT"));
            parametres.put("PARAM_DATE_DEBUT_PRONONCE", getSession().getLabel("JSP_DATE_DEBUT_PRONONCE"));
            parametres.put("PARAM_ETAT_PRONONCE", getSession().getLabel("ETAT_PRONONCE"));
            parametres.put("PARAM_TYPE_IJ", getSession().getLabel("JSP_TYPE_IJ"));

        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "IJListeEcheancesPrononces");
            abort();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.printing.itext.FWIDocumentManager#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        journalLog.setSession(getSession());
        setTemplateFile(IJListerEcheancesPrononcesProcess.FICHIER_MODELE);
    }

    @Override
    public void createDataSource() throws Exception {

        Map<String, String> champs;

        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        BTransaction transaction = getTransaction();

        if (lignes == null) {
            lignes = new ArrayList<Map<String, String>>();
        }

        // Récupération de tous les prononcés IJAI actif (-- validé ou
        // communiqué)
        IJPrononceManager mgr = new IJPrononceManager();
        mgr.setSession(getSession());
        try {
            IJPrononce prononce;

            // Date courante début/fin du mois(date saisie par l'utilisateur)
            cal.set(Calendar.MONTH, Integer.parseInt(getMoisTraitement().substring(0, 2)) - 1);
            cal.set(Calendar.YEAR, Integer.parseInt(getMoisTraitement().substring(3)));
            cal.set(Calendar.DAY_OF_MONTH, 1);

            String dateCouranteDM = getDateFormatted(cal);

            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            // String dateCouranteFM = getDateFormatted(cal);

            String[] etatsPrononce = new String[2];
            etatsPrononce[0] = new String(IIJPrononce.CS_COMMUNIQUE);
            mgr.setForCsEtats(etatsPrononce);
            mgr.setFromDateFinSiRenseigne(dateCouranteDM);

            cal2.set(Calendar.MONTH, Integer.parseInt(getMoisTraitement().substring(0, 2)) - 1);
            cal2.set(Calendar.YEAR, Integer.parseInt(getMoisTraitement().substring(3)) - 2);
            cal2.set(Calendar.DAY_OF_MONTH, 1);

            String date2AnsAnterieurADateCourante = getDateFormatted(cal2);

            mgr.setForDateDebutAnterieurA(date2AnsAnterieurADateCourante);

            mgr.find(BManager.SIZE_NOLIMIT);

            JAVector mgrVector = mgr.getContainer();

            Collections.sort(mgrVector, new Comparator<IJPrononce>() {
                @Override
                public int compare(IJPrononce objIJPrononce1, IJPrononce objIJPrononce2) {

                    String nom1 = getNom(objIJPrononce1, PRTiersWrapper.PROPERTY_NOM);
                    String nom2 = getNom(objIJPrononce2, PRTiersWrapper.PROPERTY_NOM);

                    if (nom1 != null) {
                        int nomComp = nom1.compareTo(nom2);
                        if (nomComp != 0) {
                            return nomComp;
                        } else {
                            String prenom1 = getNom(objIJPrononce1, PRTiersWrapper.PROPERTY_PRENOM);
                            String prenom2 = getNom(objIJPrononce2, PRTiersWrapper.PROPERTY_PRENOM);

                            if (prenom1 != null) {
                                return prenom1.compareTo(prenom2);
                            } else {
                                return 0;
                            }
                        }
                    } else {
                        return 0;
                    }
                }

                private String getNom(IJPrononce prononce, String propriete) {
                    try {
                        if (null != prononce) {
                            PRDemande demande = prononce.loadDemande(IJListerEcheancesPrononcesProcess.this
                                    .getTransaction());
                            PRTiersWrapper tiers = PRTiersHelper.getTiersParId(
                                    IJListerEcheancesPrononcesProcess.this.getSession(), demande.getIdTiers());

                            return tiers.getProperty(propriete);
                        } else {
                            return "";
                        }
                    } catch (Exception e) {
                        return "";
                    }
                }
            });

            Iterator<IJPrononce> itrMgrVector = mgrVector.iterator();

            while (itrMgrVector.hasNext()) {

                prononce = itrMgrVector.next();

                if (prononce != null) {

                    String idTiers = prononce.loadDemande(transaction).getIdTiers();

                    PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), idTiers);

                    champs = new HashMap<String, String>();
                    champs.put("FIELD_NO_PRONONCE", prononce.getIdPrononce());

                    String detailRequerant = PRNSSUtil.formatDetailRequerantDetail(
                            tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                            tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                                    + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                            tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                            getLibelleCourtSexe(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)),
                            getLibellePays(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));

                    champs.put("FIELD_DETAIL_REQUERANT", detailRequerant);
                    champs.put("FIELD_DATE_DEBUT_PRONONCE", prononce.getDateDebutPrononce());
                    champs.put("FIELD_ETAT_PRONONCE", getSession().getCodeLibelle(prononce.getCsEtat()));
                    champs.put("FIELD_TYPE_IJ", getSession().getCodeLibelle(prononce.getCsTypeIJ()));

                    lignes.add(champs);
                    count++;
                }

            }
            if (count > 0) {
                this.setDataSource(lignes);
            }

        } catch (Exception e) {
            this._addError(transaction, e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "IJListerEcheancesPrononcesProcess");
        }
    }

    private String getDateFormatted(Calendar cal) {
        DateFormat df = PRDateFormater.getDateFormatInstance(getSession(), "dd.MM.yyyy");
        return df.format(cal.getTime());

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#getEMailObject()
     */
    @Override
    protected String getEMailObject() {
        String documentFileTitle = (JadeStringUtil.isBlank(getFileTitle()) ? getDocumentTitle() : getFileTitle());
        StringBuffer buffer = new StringBuffer();
        buffer.append(documentFileTitle);
        if (isOnError()) {
            buffer.append(getSession().getLabel("ERREUR_IMPRESSION"));
        } else {

            JADate asJADate = new JADate();
            asJADate.setMonth(Integer.parseInt(getMoisTraitement().substring(0, 2)));
            asJADate.setYear(Integer.parseInt(getMoisTraitement().substring(3)));

            Calendar cal = Calendar.getInstance();

            cal.set(asJADate.getYear(), asJADate.getMonth() - 1, 1);
            DateFormat df = PRDateFormater.getDateFormatInstance(getSession(), " MMMM yyyy ");

            buffer.append(getSession().getLabel("REUSSITE_IMPRESSION") + df.format(cal.getTime()));
        }
        return buffer.toString();
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est passé en paramètre
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe(String csSexe) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est passé en paramètre
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays(String csNationalite) {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", csNationalite));
        }

    }

    @Override
    public boolean next() throws FWIException {
        nbListe++;
        if (nbListe == 1) {
            return true;
        }
        return false;
    }
}
