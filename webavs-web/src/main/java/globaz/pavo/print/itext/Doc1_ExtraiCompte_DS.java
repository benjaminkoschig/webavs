package globaz.pavo.print.itext;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.util.JACalendar;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.translation.CodeSystem;
import globaz.pavo.util.CIUtil;
import globaz.pyxis.api.ITIAdministration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;

/**
 * BManager implémenté du JRDataSource pour que JasperReport puissent l'utiliser comme dataSource. implémenté les
 * méthodes next(), getF9ieldValue() et clone() Date de création : (01.04.2003 14:44:33)
 * 
 * @author: Administrator
 */
public class Doc1_ExtraiCompte_DS extends globaz.pavo.db.compte.CIEcritureManager { // implements
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int LONGUEUR_MAX_EXTRAIT_CI = 76;
    private java.lang.String AVS = null;
    private ITIAdministration caisse;
    private java.lang.String codePays = null;
    private java.lang.String companyName = null;
    private java.lang.String compteIndividuelId = "";
    private List<Map<String, Object>> container = null;
    private java.lang.String dateLieux = null;
    private java.lang.String dateNaissance = null;
    private globaz.pavo.db.compte.CIEcriture entity = null;
    private Iterator<?> itcontainer = null;
    private String langueImp = "";
    private java.lang.String nomPrenom = null;
    private BProcess process = null;
    private java.lang.String tempAffile = null;
    private java.lang.String tempAnnee = null;

    /**
     * Commentaire relatif au constructeur Doc1_ExtraiCompte_DS.
     */
    public Doc1_ExtraiCompte_DS() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.04.2003 15:35:26)
     * 
     * @return java.lang.String
     * @param ecriture
     *            globaz.pavo.db.compte.CIEcriture
     */
    private String _getAVS() {
        if (AVS == null) {
            try {
                AVS = entity.getNssFormate();
            } catch (Exception e) {
                AVS = null;
            }
        }
        return AVS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 07:56:16)
     * 
     * @return java.lang.String
     */
    protected ITIAdministration _getCaisseCompensation() throws Exception {
        if (caisse == null) {
            CIApplication applic = (CIApplication) getSession().getApplication();
            caisse = applic.getAdministrationLocale(getSession());
        }
        return caisse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.04.2003 15:45:49)
     * 
     * @return java.lang.String
     */
    protected String _getCodePays() {
        if (codePays == null) {
            try {
                codePays = globaz.pavo.translation.CodeSystem.getCodeUtilisateur(entity.getPaysOrigine(), getSession());
                // codePays = entity.getPaysOrigine();
            } catch (Exception e) {
                codePays = null;
            }
        }
        return codePays;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.03.2003 11:14:39)
     * 
     * @return java.lang.String
     */
    protected java.lang.String _getDateLieux() {
        if (dateLieux == null) {
            try {
                // todo getLocaliteCourt de PYXIS
                dateLieux = _getCaisseCompensation().getLocalite();
                if ((dateLieux == null) || "null".equals(dateLieux)) {
                    dateLieux = "n/a";
                }
                dateLieux = dateLieux + ", " + JACalendar.todayJJsMMsAAAA();
            } catch (Exception e) {
                dateLieux = "n/a";
            }
        }
        return dateLieux;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.04.2003 15:37:30)
     * 
     * @return java.lang.String
     */
    private String _getDateNaissance() {
        if (dateNaissance == null) {
            try {
                dateNaissance = entity.getDateDeNaissance();
            } catch (Exception e) {
                dateNaissance = null;
            }
        }
        return dateNaissance;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.04.2003 15:48:30)
     * 
     * @return java.lang.String
     * @param affilieId
     *            java.lang.String
     * @param ecriture
     *            globaz.pavo.db.compte.CIEcriture
     * @param annee
     *            java.lang.String
     */
    private String _getGenreRevenu(String affilieId, CIEcriture ecriture, String annee) {
        int codeRevenu = 100;
        int extourne = 0;
        try {
            codeRevenu = (new Integer("" + ecriture.getGreFormat().charAt(1))).intValue();
            extourne = (new Integer("" + ecriture.getGreFormat().charAt(0))).intValue();
        } catch (Exception e) {
        }
        CIApplication application = null;
        try {
            application = (CIApplication) getSession().getApplication();
        } catch (Exception e1) {
            // TODO Bloc catch auto-généré
            e1.printStackTrace();
        }
        switch (codeRevenu) {
            case 0:
                if (CIUtil.unFormatAVS(affilieId).equals("11111111111")) {
                    // "Bonification pour tâches d'assistance";
                    return application.getLabel("PRTGENRE3", langueImp);
                } else {
                    // return getSession().getLabel("PRTGENRE2");
                    // //"Assurance facultative des Suisses à l'étranger";
                    return application.getLabel("PRTGENRE2", langueImp);
                }
            case 1:
                if (CIUtil.unFormatAVS(affilieId).equals("88888888888")) {
                    // return getSession().getLabel("PRTGENRE6"); //
                    // "Indemnité journalière AI";
                    return application.getLabel("PRTGENRE6", langueImp);
                } else if (CIUtil.unFormatAVS(affilieId).equals("77777777777")) {
                    // return getSession().getLabel("PRTGENRE7"); //
                    // "Allocation pour perte de gain";
                    return application.getLabel("PRTGENRE7", langueImp);
                } else if (CIUtil.unFormatAVS(affilieId).equals("55555555555")) {
                    // "Allocation pour perte de gain - pandémie";
                    return application.getLabel("PRTGENRE17", langueImp);
                } else if (CIUtil.unFormatAVS(affilieId).equals("66666666666")) {
                    // return getSession().getLabel("PRTGENRE8"); //
                    // "Indemnité journalière de l'assurance militaire";
                    return application.getLabel("PRTGENRE8", langueImp);
                } else if (CIUtil.unFormatAVS(affilieId).startsWith("999999")) {
                    // return getSession().getLabel("PRTGENRE5"); //
                    // "Indemnité de chômage";
                    return application.getLabel("PRTGENRE5", langueImp);
                } else {
                    try {

                        String noNomEmpl = ecriture.getNoNomEmployeur(true);

                        // Si pas de localité, ne pas afficher la virgule
                        String retour = noNomEmpl.substring(noNomEmpl.indexOf(" ") + 1).trim();
                        try {
                            // if(retour.length()>0){
                            // return retour.substring(0,retour.length()-1);
                            // Bug Itext remettre à la ligne manuellement pour
                            // éviter les lignes vides
                            if (retour.length() > 0) {
                                if (retour.charAt(retour.length() - 1) == ',') {
                                    retour = retour.substring(0, retour.length() - 1);
                                }
                            }
                            if (retour.length() > Doc1_ExtraiCompte_DS.LONGUEUR_MAX_EXTRAIT_CI) {
                                String retourBase = retour.substring(0, Doc1_ExtraiCompte_DS.LONGUEUR_MAX_EXTRAIT_CI);
                                retour = retour.substring(0, retourBase.lastIndexOf(" ")) + "\n"
                                        + retour.substring(retourBase.lastIndexOf(" "));
                            }
                        } catch (Exception e) {
                            return noNomEmpl.substring(noNomEmpl.indexOf(" ") + 1);
                        }
                        return retour;
                        // return
                        // noNomEmpl.substring(noNomEmpl.indexOf(" ")+1,noNomEmpl.indexOf(","));
                        // return noNomEmpl;
                    } catch (Exception e) {
                        return "";
                    }
                }
            case 2:
                // return getSession().getLabel("PRTGENRE9"); //
                // "Salarié/e dont l'employeur n'est pas soumis à cotisations";
                return application.getLabel("PRTGENRE9", langueImp);
            case 3:
                // return getSession().getLabel("PRTGENRE10"); //
                // "Personne de condition indépendante";
                return application.getLabel("PRTGENRE10", langueImp);
            case 4:
                if (annee != null) {
                    if ("313002".equals(ecriture.getCode())) {
                        // return getSession().getLabel("MSG_CONJOINT_ETRANGER");
                        return application.getLabel("MSG_CONJOINT_ETRANGER", langueImp);
                    } else {
                        // return getSession().getLabel("PRTGENRE1"); //
                        // "Conjoint non actif à l'étranger";
                        return application.getLabel("PRTGENRE1", langueImp);
                    }
                } else {
                    // return getSession().getLabel("PRTGENRE11"); //
                    // "Personne sans activité lucrative";
                    return application.getLabel("PRTGENRE11", langueImp);
                }
            case 5:
                // return getSession().getLabel("PRTGENRE12"); //
                // "Timbres-cotisations";
                return application.getLabel("PRTGENRE12", langueImp);
            case 7:
                // return getSession().getLabel("PRTGENRE13"); //
                // "Revenu soumis à cotisations de personnes retraitées";
                return application.getLabel("PRTGENRE13", langueImp);
            case 8:
                if (extourne == 1) {
                    // return getSession().getLabel("PRTGENRE15"); //
                    // "Part de revenu destinée au conjoint";
                    return application.getLabel("PRTGENRE15", langueImp);
                } else {
                    // return getSession().getLabel("PRTGENRE14"); //
                    // "Part de revenu provenant du conjoint";
                    return application.getLabel("PRTGENRE14", langueImp);
                }
            case 9:
                // return getSession().getLabel("PRTGENRE16");
                // //"Personne de condition indépendante dans l'agriture";
                return application.getLabel("PRTGENRE16", langueImp);
            default:
                return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 07:56:16)
     * 
     * @return java.lang.String
     */
    protected java.lang.String _getNoCaisseCompensation() {
        try {
            String nom = _getCaisseCompensation().getCodeAdministration();
            if ((nom == null) || "null".equals(nom)) {
                nom = "n/a";
            }
            return nom;
        } catch (Exception e) {
            return "n/a";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.04.2003 15:42:02)
     * 
     * @return java.lang.String
     */
    protected String _getNomPrenom() {
        if (nomPrenom == null) {
            try {
                nomPrenom = entity.getNomPrenom();
            } catch (Exception e) {
                nomPrenom = null;
            }
        }
        return nomPrenom;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.03.2003 07:56:16)
     * 
     * @return java.lang.String
     */
    protected java.lang.String _getTextCaisseCompensation() {
        // if (companyName == null) {
        String temp = "";
        try {
            temp = _getCaisseCompensation().getCodeAdministration();
            if ((temp == null) || "null".equals(temp)) {
                temp = "n/a";
            }
        } catch (Exception ex) {
            // laisser vide
            temp = "n/a";
        }
        try {
            CIApplication applic = (CIApplication) getSession().getApplication();
            companyName = FWMessageFormat.format(applic.getCaisseCompForExtrait(), temp);
        } catch (Exception e) {
        }
        // }
        return companyName;
    }

    // /**
    // * Copiez la méthode tel quel, permet la copy de l'objet
    // * Date de création : (01.04.2003 14:45:18)
    // * @return java.lang.Object
    // * @exception java.lang.CloneNotSupportedException La description de
    // l'exception.
    // */
    // public Object clone() throws java.lang.CloneNotSupportedException {
    // return super.clone();
    // }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.04.2003 15:33:47)
     */
    @Override
    protected void _init() {
    }

    public Collection<Map<String, Object>> getCollectionData() throws JRException {
        if (container == null) {
            container = new ArrayList<Map<String, Object>>();

            while (next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("COL_1", this.getFieldValue("COL_1"));
                map.put("COL_2", this.getFieldValue("COL_2"));
                map.put("COL_3", this.getFieldValue("COL_3"));
                map.put("COL_4", this.getFieldValue("COL_4"));
                map.put("COL_5", this.getFieldValue("COL_5"));
                map.put("COL_6", this.getFieldValue("COL_6"));
                map.put("COL_7", this.getFieldValue("COL_7"));
                map.put("COL_8", this.getFieldValue("COL_8"));
                map.put("COL_9", this.getFieldValue("COL_9"));
                map.put("COL_10", this.getFieldValue("COL_10"));
                map.put("COL_11", this.getFieldValue("COL_11"));
                map.put("COL_12", this.getFieldValue("COL_12"));
                map.put("COL_13", this.getFieldValue("COL_13"));
                String col_14 = this.getFieldValue("COL_14").toString();
                if (col_14.length() < 61) {
                    map.put("COL_14", col_14);
                } else {
                    map.put("COL_14", col_14.substring(0, 60));
                }
                map.put("COL_15", this.getFieldValue("COL_15"));
                map.put("COL_16", this.getFieldValue("COL_16"));
                map.put("COL_17", this.getFieldValue("COL_17"));
                container.add(map);
            }
            if (0 == size()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("COL_1", "1");
                map.put("COL_2", "");
                map.put("COL_3", "");
                map.put("COL_4", "");
                map.put("COL_5", "");
                map.put("COL_6", "");
                map.put("COL_7", "");
                map.put("COL_8", "");
                map.put("COL_9", "");
                map.put("COL_10", "");
                map.put("COL_11", "");
                map.put("COL_12", "");
                map.put("COL_13", new Double(0));
                map.put("COL_14", "");
                map.put("COL_15", "");
                map.put("COL_16", _getNoCaisseCompensation());
                map.put("COL_17", "");
                container.add(map);
            }
        }
        return container;
    }

    /**
     * @see net.sf.jasperreports.engine.JRCloneableDataSource#getContext()
     */
    public BProcess getContext() {
        return process;
    }

    /**
     * Appele chaque champ du modèle JRField : Field appeler
     */
    public Object getFieldValue(net.sf.jasperreports.engine.JRField jrField)
            throws net.sf.jasperreports.engine.JRException {
        return this.getFieldValue(jrField.getName());
    }

    private Object getFieldValue(String fieldName) throws JRException {
        // Verify si le compteIndividuel change -> nouveau document pour
        // l'impression de liste de document
        if (!compteIndividuelId.equals(entity.getCompteIndividuelId())) {
            compteIndividuelId = entity.getCompteIndividuelId();
            _init();
        }
        // retourne chaque champ
        if (fieldName.equals("COL_1")) {
            // return entity.getCompteIndividuelId();
            return "1";
        }
        if (fieldName.equals("COL_2")) {
            return _getAVS();
        }
        if (fieldName.equals("COL_3")) {
            return _getTextCaisseCompensation();
        }
        if (fieldName.equals("COL_4")) {
            return _getNomPrenom();
        }
        if (fieldName.equals("COL_5")) {
            return _getCodePays();
        }
        if (fieldName.equals("COL_6")) {
            return _getDateNaissance();
        }
        if (fieldName.equals("COL_7")) {
            return _getDateLieux();
        }
        if (fieldName.equals("COL_8")) {
            tempAffile = entity.getNoNomEmployeur();
            int pos = 0;
            if ((pos = tempAffile.indexOf(" ")) > 0) {
                tempAffile = tempAffile.substring(0, pos).trim();
            }
            return tempAffile;
        }
        if (fieldName.equals("COL_9")) {
            String gre = entity.getGreForExtrait();
            if (gre.startsWith("0")) {
                return gre.substring(1);
            } else {
                return gre;
            }
        }
        if (fieldName.equals("COL_10")) {
            if (entity.getPartBta().equals("0")) {
                return "";
            } else {
                return entity.getPartBta();
            }
        }
        if (fieldName.equals("COL_11")) {
            return entity.getMoisDebutPad() + "-" + entity.getMoisFinPad();
        }
        if (fieldName.equals("COL_12")) {
            tempAnnee = entity.getAnnee();
            return tempAnnee;
        }
        if (fieldName.equals("COL_13")) {
            try {
                if ("313002".equals(entity.getCode())) {
                    return new Double(new FWCurrency(entity.getMontantSigne()).doubleValue());
                } else if (entity.getCacherMontant()) {
                    return new Double(0);
                } else {
                    return new Double(new FWCurrency(entity.getMontantSigne()).doubleValue());
                }

            } catch (Exception inEx) {
                return new Double(0);
            }
        }
        if (fieldName.equals("COL_14")) {
            if (entity.getCacherMontant()) {
                return "(" + getSession().getLabel("MSG_ECRITURE_CACHE") + ") : "
                        + _getGenreRevenu(tempAffile, entity, tempAnnee);
            }
            return _getGenreRevenu(tempAffile, entity, tempAnnee);
        }
        if (fieldName.equals("COL_16")) {
            return _getNoCaisseCompensation();
        }
        if (fieldName.equals("COL_17")) {
            if ("313001".equals(entity.getCode()) || "313002".equals(entity.getCode())
                    || "313003".equals(entity.getCode())) {
                return CodeSystem.getCodeUtilisateur(entity.getCode(), getSession());
            }
        }

        return "";
    }

    /**
     * @return
     */
    public String getLangueImp() {
        return langueImp;
    }

    /**
     * @see net.sf.jasperreports.engine.JRRewindableDataSource#moveFirst()
     */
    public void moveFirst() throws JRException {
        itcontainer = null;
    }

    /**
     * Copier le contenu de cette méthode, elle devrait pas trop changer entre chaque class Retourne vrais si il existe
     * encore une entité.
     */
    public boolean next() throws net.sf.jasperreports.engine.JRException {
        entity = null;
        try {
            if (itcontainer == null) {
                this.find(0);
                itcontainer = getContainer().iterator();
            }
            // lit le nouveau entity
            if (itcontainer.hasNext()) {
                entity = (globaz.pavo.db.compte.CIEcriture) itcontainer.next();
            }

        } catch (Exception e) {
        }
        // vraix : il existe une entity, faux fin du select
        if ((process != null) && process.isAborted()) {
            return false;
        } else {
            return (entity != null);
        }
    }

    /**
     * @see net.sf.jasperreports.engine.JRCloneableDataSource#setContext(BProcess)
     */
    public void setContext(BProcess process) {
        this.process = process;
    }

    /**
     * @param string
     */
    public void setLangueImp(String string) {
        langueImp = string;
    }

}
