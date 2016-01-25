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
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFPeriodeManager;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.application.IJApplication;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJPrononceManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.lyra.process.LYAbstractDocumentGenerator;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <H1>Description</H1>
 * 
 * Classe exécutée depuis LYRA (Echéances) pour créer un document pdf listant toutes les échéances IJAI
 * 
 * @author hpe & scr
 */
public class IJListerEcheancesIJProcess extends LYAbstractDocumentGenerator {

    private static final long serialVersionUID = 1L;
    private static final String FICHIER_MODELE = "IJ_LISTE_ECHEANCES";
    private static final String FICHIER_MODELE_COMMUNE = "IJ_LISTE_ECHEANCES_COMMUNE";
    private static final String FICHIER_RESULTAT = "listeEcheances";

    private FWMemoryLog journalLog = new FWMemoryLog();
    private int nbListe = 0;
    private boolean ajouterCommunePolitique = false;

    private class POJORequerant implements Comparable<POJORequerant> {
        private String idTiers;
        private String communePolitique;
        private String nom;
        private String prenom;
        private List<POJO> lstPojo;

        public POJORequerant() {
            super();
            lstPojo = new ArrayList<POJO>();
        }

        public final String getIdTiers() {
            return idTiers;
        }

        public final void setIdTiers(String idTiers) {
            this.idTiers = idTiers;
        }

        public final String getCommunePolitique() {
            return communePolitique;
        }

        public final void setCommunePolitique(String communePolitique) {
            this.communePolitique = communePolitique;
        }

        public List<POJO> getLstPojo() {
            return lstPojo;
        }

        @Override
        public int compareTo(POJORequerant o) {
            if (isAjouterCommunePolitique()) {
                int result1 = getCommunePolitique().compareTo(o.getCommunePolitique());
                if (result1 != 0) {
                    return result1;
                }
            }

            if (getNom() != null) {
                int nomComp = getNom().compareTo(o.getNom());

                if (nomComp != 0) {
                    return nomComp;
                } else {

                    if (getPrenom() != null) {
                        return getPrenom().compareTo(o.getPrenom());
                    } else {
                        return 0;
                    }
                }
            } else {
                return 0;
            }

        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }
    }

    private class POJO {

        private String requerantNom;
        private String requerantDetail;
        private String typeEcheance;
        private String enfantNom;
        private String enfantDetail;

        public String getRequerantNom() {
            return requerantNom;
        }

        public void setRequerantNom(String requerantNom) {
            this.requerantNom = requerantNom;
        }

        public String getRequerantDetail() {
            return requerantDetail;
        }

        public void setRequerantDetail(String requerantDetail) {
            this.requerantDetail = requerantDetail;
        }

        public String getTypeEcheance() {
            return typeEcheance;
        }

        public void setTypeEcheance(String typeEcheance) {
            this.typeEcheance = typeEcheance;
        }

        public String getEnfantNom() {
            return enfantNom;
        }

        public void setEnfantNom(String enfantNom) {
            this.enfantNom = enfantNom;
        }

        public String getEnfantDetail() {
            return enfantDetail;
        }

        public void setEnfantDetail(String enfantDetail) {
            this.enfantDetail = enfantDetail;
        }
    }

    public IJListerEcheancesIJProcess(BProcess parent) throws FWIException {
        super(parent, IJApplication.APPLICATION_IJ_REP, IJListerEcheancesIJProcess.FICHIER_RESULTAT);
    }

    public IJListerEcheancesIJProcess(BSession session) throws FWIException {
        super(session, IJApplication.APPLICATION_IJ_REP, IJListerEcheancesIJProcess.FICHIER_RESULTAT);
    }

    @Override
    protected void _validate() throws Exception {

        if ((getEMailAddress() == null) || getEMailAddress().equals("")) {

            journalLog.logMessage(getSession().getLabel("EMAIL_NON_RENSEIGNE"), FWMessage.ERREUR,
                    "IJListerEcheancesIJProcess");
            getMemoryLog().logMessage(getSession().getLabel("EMAIL_NON_RENSEIGNE"), FWMessage.ERREUR,
                    "IJListerEcheancesIJProcess");
            setSendCompletionMail(false);
            setSendMailOnError(true);
        }

        if (JadeStringUtil.isBlank(getMoisTraitement())) {

            journalLog.logMessage(getSession().getLabel("DATE_ECHEANCE_NON_RENSEIGNEE"), FWMessage.ERREUR,
                    "IJListerEcheancesIJProcess");
            getMemoryLog().logMessage(getSession().getLabel("DATE_ECHEANCE_NON_RENSEIGNEE"), FWMessage.ERREUR,
                    "IJListerEcheancesIJProcess");
            setSendCompletionMail(false);
            setSendMailOnError(true);
        }

        if (getSession().hasErrors()) {

            journalLog.logMessage(getSession().getErrors().toString(), FWMessage.ERREUR, "IJListerEcheancesIJProcess");
            getMemoryLog().logMessage(getSession().getErrors().toString(), FWMessage.ERREUR,
                    "IJListerEcheancesIJProcess");
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

        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.ECHEANCES_18_25_ANS_IJ);

        JadePublishDocumentInfo docInfo = getDocumentInfo();

        try {

            docInfo.setDocumentSubject(getSession().getLabel("TITRE_DOCUMENT_1825_ANS"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        super.afterBuildReport();
    }

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

            JADate asJADate = new JADate();
            asJADate.setMonth(Integer.parseInt(getMoisTraitement().substring(0, 2)));
            asJADate.setYear(Integer.parseInt(getMoisTraitement().substring(3)));

            Calendar cal = Calendar.getInstance();

            cal.set(asJADate.getYear(), asJADate.getMonth() - 1, 1);
            DateFormat df = PRDateFormater.getDateFormatInstance(getSession(), " MMMM yyyy ");

            parametres.put("PARAM_TITRE", getSession().getLabel("TITRE_LISTE_ECHEANCES") + df.format(cal.getTime()));

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
            parametres.put("PARAM_TITRE_REQUERANT", getSession().getLabel("TITRE_REQUERANT_LISTE_ECHEANCES"));
            parametres.put("PARAM_TITRE_ECHEANCE", getSession().getLabel("TYPE_ECHEANCE_LISTE_ECHEANCES"));
            parametres.put("PARAM_TITRE_ENFANT", getSession().getLabel("TITRE_ENFANT_LISTE_ECHEANCES"));
            parametres.put("PARAM_PAGE", getSession().getLabel("PAGE_LISTE_ECHEANCES"));

            if (isAjouterCommunePolitique()) {
                parametres.put("PARAM_TITRE_COMMUNE",
                        getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()));
                parametres.put("PARAM_USER",
                        getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey()) + " : "
                                + getSession().getUserId());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "SFListeEcheances");
            abort();
        }
    }

    @Override
    public void beforeExecuteReport() throws FWIException {
        journalLog.setSession(getSession());
        if (isAjouterCommunePolitique()) {
            setTemplateFile(IJListerEcheancesIJProcess.FICHIER_MODELE_COMMUNE);
        } else {
            setTemplateFile(IJListerEcheancesIJProcess.FICHIER_MODELE);
        }
    }

    @Override
    public void createDataSource() throws Exception {

        Calendar cal = Calendar.getInstance();
        BTransaction transaction = getTransaction();

        List<POJORequerant> listPojoRequerant = new ArrayList<POJORequerant>();

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
            String dateCouranteFM = getDateFormatted(cal);

            String[] etatsPrononce = new String[2];
            // point ouvert 00658
            // ajout de l'etat decide
            // etatsPrononce[0] = new String(IIJPrononce.CS_VALIDE);
            etatsPrononce[0] = new String(IIJPrononce.CS_DECIDE);
            etatsPrononce[1] = new String(IIJPrononce.CS_COMMUNIQUE);
            mgr.setForCsEtats(etatsPrononce);
            mgr.setFromDateFinSiRenseigne(dateCouranteDM);

            mgr.find(BManager.SIZE_NOLIMIT);

            List<IJPrononce> listIJ = mgr.getContainerAsList();

            Iterator<IJPrononce> itrMgrVector = listIJ.iterator();
            Set<String> setIdTiers = new HashSet<String>();

            while (itrMgrVector.hasNext()) {

                prononce = itrMgrVector.next();

                if (null != prononce) {

                    String idTiers = prononce.loadDemande(transaction).getIdTiers();
                    POJORequerant pojoRequerant = new POJORequerant();
                    pojoRequerant.setIdTiers(idTiers);
                    pojoRequerant.setNom(getNom(prononce, PRTiersWrapper.PROPERTY_NOM));
                    pojoRequerant.setPrenom(getNom(prononce, PRTiersWrapper.PROPERTY_PRENOM));

                    setIdTiers.add(idTiers);

                    // a faire en libellé et a affiché sur le document
                    if (JadeStringUtil.isEmpty(idTiers)) {
                        getMemoryLog().logMessage(
                                getSession().getLabel("ERREUR_LISTE_ECHEANCE_TIERS_NON_TROUVE")
                                        + prononce.getIdPrononce(), FWMessage.AVERTISSEMENT, this.getClass().getName());
                        continue;
                    }
                    SFMembreFamille membre = new SFMembreFamille();
                    membre.setSession(getSession());
                    membre.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
                    membre.setCsDomaineApplication(globaz.hera.api.ISFSituationFamiliale.CS_DOMAINE_INDEMNITEE_JOURNALIERE);
                    membre.setIdTiers(idTiers);
                    membre.retrieve(transaction);
                    if (membre.isNew()) {
                        membre.setCsDomaineApplication(globaz.hera.api.ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                        membre.setIdTiers(idTiers);
                        membre.retrieve(transaction);
                    }

                    // a faire en libellé et a affiché sur le document
                    if (membre.isNew()) {
                        getMemoryLog().logMessage(
                                getSession().getLabel("ERREUR_LISTE_ECHEANCE_REQUERANT_NON_TROUVE ") + idTiers
                                        + getSession().getLabel("ERREUR_LISTE_ECHEANCE_NO_PRONONCE")
                                        + prononce.getIdPrononce(), FWMessage.AVERTISSEMENT, this.getClass().getName());
                        continue;
                    }

                    globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(
                            getSession(), globaz.hera.api.ISFSituationFamiliale.CS_DOMAINE_INDEMNITEE_JOURNALIERE,
                            idTiers);

                    ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamilleRequerant(idTiers, dateCouranteDM);
                    for (int i = 0; i < membresFamille.length; i++) {
                        ISFMembreFamilleRequerant mf = membresFamille[i];
                        if (globaz.hera.api.ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT.equals(mf
                                .getRelationAuRequerant())) {

                            String dateNaissance = mf.getDateNaissance();

                            // On contrôle si l'enfant à plus de 25 ans.

                            cal.set(Calendar.DAY_OF_MONTH, 1);
                            cal.set(Calendar.MONTH, Integer.parseInt(getMoisTraitement().substring(0, 2)) - 1);
                            cal.set(Calendar.YEAR, Integer.parseInt(getMoisTraitement().substring(3)));
                            cal.add(Calendar.YEAR, -25);
                            String dateDebut = getDateFormatted(cal);

                            cal.set(Calendar.MONTH, Integer.parseInt(getMoisTraitement().substring(0, 2)) - 1);
                            cal.set(Calendar.YEAR, Integer.parseInt(getMoisTraitement().substring(3)));
                            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                            cal.add(Calendar.YEAR, -25);
                            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                            String dateFin = getDateFormatted(cal);

                            if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateDebut, dateNaissance)
                                    && BSessionUtil
                                            .compareDateFirstGreaterOrEqual(getSession(), dateFin, dateNaissance)) {

                                if (pojoRequerant == null) {
                                    pojoRequerant = new POJORequerant();
                                }
                                POJO pojo = new POJO();
                                pojo.setRequerantNom(membre.getNom() + " " + membre.getPrenom());
                                pojo.setRequerantDetail(PRNSSUtil.formatDetailRequerantDetail(membre.getNss(),
                                        membre.getNom() + " " + membre.getPrenom(), membre.getDateNaissance(),
                                        getLibelleCourtSexe(membre.getCsSexe()),
                                        getLibellePays(membre.getCsNationalite())));
                                pojo.setTypeEcheance(getSession().getLabel("PLUSPETIT_25_ANS"));
                                pojo.setEnfantNom(mf.getNom() + " " + mf.getPrenom());
                                pojo.setEnfantDetail(PRNSSUtil.formatDetailRequerantDetail(mf.getNss(), mf.getNom()
                                        + " " + mf.getPrenom(), mf.getDateNaissance(),
                                        getLibelleCourtSexe(mf.getCsSexe()), getLibellePays(mf.getCsNationalite())));

                                pojoRequerant.getLstPojo().add(pojo);

                            }

                            // Si l'enfant à plus de 18 ans sans période
                            // d'études...
                            cal.set(Calendar.DAY_OF_MONTH, 1);
                            cal.set(Calendar.MONTH, Integer.parseInt(getMoisTraitement().substring(0, 2)) - 1);
                            cal.set(Calendar.YEAR, Integer.parseInt(getMoisTraitement().substring(3)));
                            cal.add(Calendar.YEAR, -18);

                            dateDebut = getDateFormatted(cal);

                            cal.set(Calendar.MONTH, Integer.parseInt(getMoisTraitement().substring(0, 2)) - 1);
                            cal.set(Calendar.YEAR, Integer.parseInt(getMoisTraitement().substring(3)));
                            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                            cal.add(Calendar.YEAR, -18);
                            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

                            dateFin = getDateFormatted(cal);

                            // Sans périodes d'études :
                            SFPeriodeManager periodeEtudesMgr = new SFPeriodeManager();
                            periodeEtudesMgr.setSession(getSession());
                            periodeEtudesMgr.setForType(globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE);
                            periodeEtudesMgr.setForIdMembreFamille(mf.getIdMembreFamille());
                            periodeEtudesMgr.find(transaction);
                            if (periodeEtudesMgr.size() == 0) {
                                if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), dateDebut, dateNaissance)
                                        && BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), dateFin,
                                                dateNaissance)) {

                                    if (pojoRequerant == null) {
                                        pojoRequerant = new POJORequerant();
                                    }
                                    POJO pojo = new POJO();
                                    pojo.setRequerantNom(membre.getNom() + " " + membre.getPrenom());
                                    pojo.setRequerantDetail(PRNSSUtil.formatDetailRequerantDetail(membre.getNss(),
                                            membre.getNom() + " " + membre.getPrenom(), membre.getDateNaissance(),
                                            getLibelleCourtSexe(membre.getCsSexe()),
                                            getLibellePays(membre.getCsNationalite())));

                                    pojo.setTypeEcheance(getSession().getLabel("PLUSGRAND_18_ANS"));
                                    pojo.setEnfantNom(mf.getNom() + " " + mf.getPrenom());
                                    pojo.setEnfantDetail(PRNSSUtil.formatDetailRequerantDetail(mf.getNss(), mf.getNom()
                                            + " " + mf.getPrenom(), mf.getDateNaissance(),
                                            getLibelleCourtSexe(mf.getCsSexe()), getLibellePays(mf.getCsNationalite())));

                                    pojoRequerant.getLstPojo().add(pojo);
                                }
                            }
                            // Avec période d'études... on ne prends que les
                            // enfants ayant des périodes d'études échues...
                            else {
                                for (int j = 0; j < periodeEtudesMgr.size(); j++) {
                                    SFPeriode periode = (SFPeriode) periodeEtudesMgr.getEntity(j);
                                    // on ne prend que les périodes d'études en
                                    // compte.
                                    if (!globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE.equals(periode
                                            .getType())) {
                                        break;
                                    }

                                    if (BSessionUtil.compareDateFirstGreaterOrEqual(getSession(), periode.getDateFin(),
                                            dateCouranteDM)
                                            && BSessionUtil.compareDateFirstLowerOrEqual(getSession(),
                                                    periode.getDateFin(), dateCouranteFM)) {

                                        if (pojoRequerant == null) {
                                            pojoRequerant = new POJORequerant();
                                        }
                                        POJO pojo = new POJO();
                                        pojo.setRequerantNom(membre.getNom() + " " + membre.getPrenom());
                                        pojo.setRequerantDetail(PRNSSUtil.formatDetailRequerantDetail(membre.getNss(),
                                                membre.getNom() + " " + membre.getPrenom(), membre.getDateNaissance(),
                                                getLibelleCourtSexe(membre.getCsSexe()),
                                                getLibellePays(membre.getCsNationalite())));

                                        pojo.setTypeEcheance(getSession().getLabel("PERIODE_ETUDE"));
                                        pojo.setEnfantNom(mf.getNom() + " " + mf.getPrenom());
                                        pojo.setEnfantDetail(PRNSSUtil.formatDetailRequerantDetail(mf.getNss(),
                                                mf.getNom() + " " + mf.getPrenom(), mf.getDateNaissance(),
                                                getLibelleCourtSexe(mf.getCsSexe()),
                                                getLibellePays(mf.getCsNationalite())));

                                        pojoRequerant.getLstPojo().add(pojo);
                                    }
                                }

                            }
                        }
                    }

                    if (pojoRequerant != null) {
                        listPojoRequerant.add(pojoRequerant);
                    }
                }
            }

            // Renseigne la commune politique en fonction de l'idTiers pour chaque pojo
            if (isAjouterCommunePolitique()) {
                Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dateCouranteFM);
                Map<String, String> mapCommuneParIdTiers = PRTiersHelper.getCommunePolitique(setIdTiers, date,
                        getSession());

                for (POJORequerant pojo : listPojoRequerant) {
                    pojo.setCommunePolitique(mapCommuneParIdTiers.get(pojo.getIdTiers()));
                }
            }

            // Tri et génération de la liste
            Collections.sort(listPojoRequerant);

            // Génération des lignes
            List<Map<String, String>> lignes = createLignesDocument(listPojoRequerant);

            if (lignes.size() > 0) {
                this.setDataSource(lignes);
            }

        } catch (Exception e) {
            this._addError(transaction, e.getMessage());
            getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, "");
        }
    }

    private List<Map<String, String>> createLignesDocument(List<POJORequerant> listPojoRequerant) {

        List<Map<String, String>> lignes = new ArrayList<Map<String, String>>();

        if (!listPojoRequerant.isEmpty()) {

            Iterator<POJORequerant> itePojoRequerant = listPojoRequerant.iterator();

            while (itePojoRequerant.hasNext()) {

                POJORequerant pojoRequerant = itePojoRequerant.next();

                Iterator<POJO> itePojo = pojoRequerant.getLstPojo().iterator();

                while (itePojo.hasNext()) {

                    POJO pojo = itePojo.next();

                    Map<String, String> champs = new HashMap<String, String>();
                    champs.put("FIELD_REQUERANT_COMMUNE", pojoRequerant.getCommunePolitique());
                    champs.put("FIELD_REQUERANT_NOM", pojo.getRequerantNom());
                    champs.put("FIELD_REQUERANT_DETAIL", pojo.getRequerantDetail());
                    champs.put("FIELD_TYPE_ECHEANCE", pojo.getTypeEcheance());
                    champs.put("FIELD_ENFANT_NOM", pojo.getEnfantNom());
                    champs.put("FIELD_ENFANT_DETAIL", pojo.getEnfantDetail());

                    lignes.add(champs);
                }
            }
        }

        return lignes;
    }

    private String getNom(IJPrononce prononce, String propriete) {
        try {
            if (null != prononce) {
                PRDemande demande = prononce.loadDemande(IJListerEcheancesIJProcess.this.getTransaction());
                PRTiersWrapper tiers = PRTiersHelper.getTiersParId(IJListerEcheancesIJProcess.this.getSession(),
                        demande.getIdTiers());

                return tiers.getProperty(propriete);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    private String getDateFormatted(Calendar cal) {
        DateFormat df = PRDateFormater.getDateFormatInstance(getSession(), "dd.MM.yyyy");
        return df.format(cal.getTime());
    }

    @Override
    protected String getEMailObject() {
        String documentFileTitle = (JadeStringUtil.isBlank(getFileTitle()) ? getDocumentTitle() : getFileTitle());
        StringBuffer buffer = new StringBuffer();
        buffer.append(documentFileTitle);
        if (isOnError()) {
            buffer.append(getSession().getLabel("ERREUR_IMPRESSION"));
        } else {
            try {
                JADate asJADate = new JADate(getMoisTraitement());

                Calendar cal = Calendar.getInstance();

                cal.set(asJADate.getYear(), asJADate.getMonth() - 1, 1);
                DateFormat df = PRDateFormater.getDateFormatInstance(getSession(), " MMMM yyyy ");

                buffer.append(getSession().getLabel("REUSSITE_IMPRESSION") + df.format(cal.getTime()));
            } catch (Exception ex) {
                JadeLogger.error(this, ex.getMessage());
            }
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
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public boolean next() throws FWIException {
        nbListe++;
        if (nbListe == 1) {
            return true;
        }
        return false;
    }

    public boolean isAjouterCommunePolitique() {
        return ajouterCommunePolitique;
    }

    public void setAjouterCommunePolitique(boolean ajouterCommunePolitique) {
        this.ajouterCommunePolitique = ajouterCommunePolitique;
    }
}
