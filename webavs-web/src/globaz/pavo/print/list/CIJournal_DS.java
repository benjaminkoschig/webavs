package globaz.pavo.print.list;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.util.CIGeneric_Bean;
import globaz.pavo.util.CIUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Insert the type's description here. Creation date: (09.07.2003 17:05:58)
 * 
 * @author: Administrator
 */
public class CIJournal_DS extends CIExtendeEcritureManager implements net.sf.jasperreports.engine.JRDataSource {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int _index;
    private HashMap beans = new HashMap();
    private Iterator container = null;
    private long cpt = 0;
    private globaz.globall.parameters.FWParametersSystemCodeManager csBrancheEconomiqueManager;
    private globaz.globall.parameters.FWParametersSystemCodeManager csBtaManager;
    private globaz.globall.parameters.FWParametersSystemCodeManager csCodeIrrecouvrableManager;
    private globaz.globall.parameters.FWParametersSystemCodeManager csCodeSpecialManager;
    private globaz.globall.parameters.FWParametersSystemCodeManager csExtourneManager;
    private globaz.globall.parameters.FWParametersSystemCodeManager csGenreManager;
    private globaz.globall.parameters.FWParametersSystemCodeManager csParticulierManager;
    private globaz.globall.parameters.FWParametersSystemCodeManager csTypeCompteManager;
    private globaz.pavo.print.list.CIExtendedEcriture entity;

    /**
     * CIJournal_DS constructor comment.
     */
    public CIJournal_DS(BSession session) {
        setSession(session);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.03.2003 14:40:10)
     * 
     * @return java.lang.Object
     * @exception java.lang.CloneNotSupportedException
     *                La description de l'exception.
     */
    @Override
    public Object clone() throws java.lang.CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsBrancheEconomiqueManager(BSession session) throws Exception {
        // Pour chargement des CS en mémoire
        if ((csBrancheEconomiqueManager == null) || csBrancheEconomiqueManager.isEmpty()
                || !csBrancheEconomiqueManager.getSession().getIdLangue().equals(session.getIdLangue())) {
            csBrancheEconomiqueManager = new FWParametersSystemCodeManager();
            csBrancheEconomiqueManager.setForIdGroupe("CIBRAECO");
            csBrancheEconomiqueManager.setForIdTypeCode("10300014");
            csBrancheEconomiqueManager.setSession(session);
            csBrancheEconomiqueManager.find(BManager.SIZE_NOLIMIT);
        }
        return csBrancheEconomiqueManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsBtaManager(BSession session) throws Exception {
        // Pour chargement des CS en mémoire
        if ((csBtaManager == null) || csBtaManager.isEmpty()
                || !csBtaManager.getSession().getIdLangue().equals(session.getIdLangue())) {
            csBtaManager = new FWParametersSystemCodeManager();
            csBtaManager.setForIdGroupe("CIPARBTA");
            csBtaManager.setForIdTypeCode("10300019");
            csBtaManager.setSession(session);
            csBtaManager.find(BManager.SIZE_NOLIMIT);
        }
        return csBtaManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsCodeIrrecouvrableManager(BSession session) throws Exception {
        // Pour chargement des CS en mémoire
        if ((csCodeIrrecouvrableManager == null) || csCodeIrrecouvrableManager.isEmpty()
                || !csCodeIrrecouvrableManager.getSession().getIdLangue().equals(session.getIdLangue())) {
            csCodeIrrecouvrableManager = new FWParametersSystemCodeManager();
            csCodeIrrecouvrableManager.setForIdGroupe("CICODAMO");
            csCodeIrrecouvrableManager.setForIdTypeCode("10300013");
            csCodeIrrecouvrableManager.setSession(session);
            csCodeIrrecouvrableManager.find(BManager.SIZE_NOLIMIT);
        }
        return csCodeIrrecouvrableManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsCodeSpecialManager(BSession session) throws Exception {
        // Pour chargement des CS en mémoire
        if ((csCodeSpecialManager == null) || csCodeSpecialManager.isEmpty()
                || !csCodeSpecialManager.getSession().getIdLangue().equals(session.getIdLangue())) {
            csCodeSpecialManager = new FWParametersSystemCodeManager();
            csCodeSpecialManager.setForIdGroupe("CICODSPE");
            csCodeSpecialManager.setForIdTypeCode("10300012");
            csCodeSpecialManager.setSession(session);
            csCodeSpecialManager.find(BManager.SIZE_NOLIMIT);
        }
        return csCodeSpecialManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsExtourneManager(BSession session) throws Exception {
        // Pour chargement des CS en mémoire
        if ((csExtourneManager == null) || csExtourneManager.isEmpty()
                || !csExtourneManager.getSession().getIdLangue().equals(session.getIdLangue())) {
            csExtourneManager = new FWParametersSystemCodeManager();
            csExtourneManager.setForIdGroupe("CICODEXT");
            csExtourneManager.setForIdTypeCode("10300011");
            csExtourneManager.setSession(session);
            csExtourneManager.find(BManager.SIZE_NOLIMIT);
        }
        return csExtourneManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsGenreManager(BSession session) throws Exception {
        // Pour chargement des CS en mémoire
        if ((csGenreManager == null) || csGenreManager.isEmpty()
                || !csGenreManager.getSession().getIdLangue().equals(session.getIdLangue())) {
            csGenreManager = new FWParametersSystemCodeManager();
            csGenreManager.setForIdGroupe("CICODGEN");
            csGenreManager.setForIdTypeCode("10300010");
            csGenreManager.setSession(session);
            csGenreManager.find(BManager.SIZE_NOLIMIT);
        }
        return csGenreManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsParticulierManager(BSession session) throws Exception {
        // Pour chargement des CS en mémoire
        if ((csParticulierManager == null) || csParticulierManager.isEmpty()
                || !csParticulierManager.getSession().getIdLangue().equals(session.getIdLangue())) {
            csParticulierManager = new FWParametersSystemCodeManager();
            csParticulierManager.setForIdGroupe("CIGENSPL");
            csParticulierManager.setForIdTypeCode("10300006");
            csParticulierManager.setSession(session);
            csParticulierManager.find(BManager.SIZE_NOLIMIT);
        }
        return csParticulierManager;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (23.01.2003 08:41:15)
     * 
     * @return FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsTypeCompteManager(BSession session) throws Exception {
        // Pour chargement des CS en mémoire
        if ((csTypeCompteManager == null) || csTypeCompteManager.isEmpty()
                || !csTypeCompteManager.getSession().getIdLangue().equals(session.getIdLangue())) {
            csTypeCompteManager = new FWParametersSystemCodeManager();
            csTypeCompteManager.setForIdGroupe("CITYPCOM");
            csTypeCompteManager.setForIdTypeCode("10300003");
            csTypeCompteManager.setSession(session);
            csTypeCompteManager.find(BManager.SIZE_NOLIMIT);
        }
        return csTypeCompteManager;
    }

    /**
	 *
	 */
    @Override
    public Object getFieldValue(net.sf.jasperreports.engine.JRField jrField)
            throws net.sf.jasperreports.engine.JRException {

        String entityID = entity.getId();

        CIGeneric_Bean currentBean = (beans.containsKey(entityID)) ? (CIGeneric_Bean) beans.get(entityID)
                : new CIGeneric_Bean();

        // On renseigne la clé de tri
        if (currentBean.getSortKey() == null) {
            currentBean.setSortKey(new Double(cpt));
            cpt++;
        }

        // retourne chaque champ
        // AVS
        if (jrField.getName().equals("COL_1")) {
            currentBean.setCol("", 1, entity.getNssFormate());
            beans.put(entityID, currentBean);
            return entity.getNssFormate();
        }
        // Nom, prénom
        if (jrField.getName().equals("COL_1a")) {
            currentBean.setCol("", 2, entity.getNomPrenomCompteIndividuel());
            beans.put(entityID, currentBean);
            return entity.getNomPrenomCompteIndividuel();
        }
        if (jrField.getName().equals("COL_1b")) {
            currentBean.setCol("", 3, entity.getEtatFormate());
            beans.put(entityID, currentBean);
            return entity.getEtatFormate();
        }
        // Période
        if (jrField.getName().equals("COL_2")) {
            currentBean.setCol("", 2, entity.getMoisDebutPad() + "-" + entity.getMoisFinPad());
            beans.put(entityID, currentBean);
            return entity.getMoisDebutPad() + "-" + entity.getMoisFinPad();
        }
        // Année
        if (jrField.getName().equals("COL_3")) {
            currentBean.setCol("", 3, entity.getAnnee());
            beans.put(entityID, currentBean);
            return entity.getAnnee();
        }
        // Genre
        if (jrField.getName().equals("COL_4")) {
            currentBean.setCol("", 4, entity.getGreFormat());
            beans.put(entityID, currentBean);
            return entity.getGreFormat();
            /*
             * try { // Extourne FWParametersSystemCodeManager csManager = getCsExtourneManager(getSession());
             * FWParametersSystemCode csExtourne = csManager.getCodeSysteme(entity.getExtourne()); String extourne = "";
             * if (csExtourne != null) extourne = csExtourne.getCurrentCodeUtilisateur().getCodeUtilisateur(); // Genre
             * écriture csManager = getCsGenreManager(getSession()); FWParametersSystemCode csGenre =
             * csManager.getCodeSysteme(entity.getGenreEcriture()); String genre = ""; if (csGenre == null) genre = "0";
             * else genre = csGenre.getCurrentCodeUtilisateur().getCodeUtilisateur(); // Particulier csManager =
             * getCsParticulierManager(getSession()); FWParametersSystemCode csParticulier =
             * csManager.getCodeSysteme(entity.getParticulier()); String bufPart =
             * csParticulier.getCurrentCodeUtilisateur().getCodeUtilisateur(); if
             * (globaz.globall.util.JAUtil.isIntegerEmpty(bufPart)) bufPart = ""; return extourne + genre + bufPart; }
             * catch (Exception e) { return null; }
             */
        }
        // Revenu
        if (jrField.getName().equals("COL_5")) {
            try {
                currentBean
                        .setCol("", 5, new Double(new FWCurrency(entity.getMontantSigne()).doubleValue()).toString());
                beans.put(entityID, currentBean);
                return new Double(new FWCurrency(entity.getMontantSigne()).doubleValue());
            } catch (Exception ex) {
                // montant caché
                entity.setHidden(true);
                currentBean.setCol("", 5, "0");
                beans.put(entityID, currentBean);
                return new Double(0);
            }
        }
        // Employeur, conjoint
        if (jrField.getName().equals("COL_6")) {
            String theNoNomEmployeur = entity.getNoNomEmployeur();
            if (!JadeStringUtil.isEmpty(theNoNomEmployeur) && (theNoNomEmployeur.indexOf(" ") >= 1)) {
                theNoNomEmployeur = theNoNomEmployeur.substring(0, theNoNomEmployeur.indexOf(" "));
            } else {
                theNoNomEmployeur = "";
            }
            currentBean.setCol("", 6, theNoNomEmployeur);
            beans.put(entityID, currentBean);
            return theNoNomEmployeur;
        }
        // Code spécial
        if (jrField.getName().equals("COL_7")) {
            try {
                FWParametersSystemCodeManager csManager = getCsCodeSpecialManager(getSession());
                FWParametersSystemCode cs = csManager.getCodeSysteme(entity.getCodeSpecial());
                currentBean.setCol("", 7, cs.getCurrentCodeUtilisateur().getCodeUtilisateur());
                beans.put(entityID, currentBean);
                return cs.getCurrentCodeUtilisateur().getCodeUtilisateur();
            } catch (Exception e) {
                currentBean.setCol("", 7, "");
                beans.put(entityID, currentBean);
                return null;
            }
        }
        // Branche économique
        /*
         * if (jrField.getName().equals("COL_8")) { try { FWParametersSystemCodeManager csManager =
         * getCsBrancheEconomiqueManager(getSession()); FWParametersSystemCode cs =
         * csManager.getCodeSysteme(entity.getBrancheEconomique()); return
         * cs.getCurrentCodeUtilisateur().getCodeUtilisateur(); } catch (Exception e) { return null; } }
         */
        // BTA
        /*
         * if (jrField.getName().equals("COL_9")) { try { FWParametersSystemCodeManager csManager =
         * getCsBtaManager(getSession()); //FWParametersSystemCode cs = csManager.getCodeSysteme(entity.getPartBta());
         * return entity.getPartBta(); } catch (Exception e) { return null; } }
         */
        // Code irrécouvrable
        if (jrField.getName().equals("COL_10")) {
            try {
                FWParametersSystemCodeManager csManager = getCsCodeIrrecouvrableManager(getSession());
                FWParametersSystemCode cs = csManager.getCodeSysteme(entity.getCode());
                currentBean.setCol("", 8, cs.getCurrentCodeUtilisateur().getCodeUtilisateur());
                beans.put(entityID, currentBean);
                return cs.getCurrentCodeUtilisateur().getCodeUtilisateur();
            } catch (Exception e) {
                currentBean.setCol("", 8, "");
                beans.put(entityID, currentBean);
                return null;
            }
        }
        // Caisse chômage
        /*
         * if (jrField.getName().equals("COL_11")) { return entity.getCaisseChomageStr(); }
         */
        // Date inscription
        if (jrField.getName().equals("COL_12")) {
            /*
             * String bufDate = entity.getDateInscriptionJournal(); if
             * (!globaz.globall.util.JAUtil.isStringEmpty(bufDate)) return bufDate; else
             */
            if (!JadeStringUtil.isEmpty(entity.getEspionSaisie())) {
                // SInon l'espion de saisie
                currentBean.setCol("", 9, CIUtil.formatDate(entity.getEspionSaisie().substring(0, 8)));
                beans.put(entityID, currentBean);
                return CIUtil.formatDate(entity.getEspionSaisie().substring(0, 8));
                // getEspionSaisie();
            } else {
                currentBean.setCol("", 9, "");
                beans.put(entityID, currentBean);
                return "";
            }
        }
        // Type de compte
        if (jrField.getName().equals("COL_13")) {
            try {
                if (CIEcriture.CS_CI_SUSPENS_SUPPRIMES.equals(entity.getIdTypeCompte())
                        && !JadeStringUtil.isBlankOrZero(entity.getEspionSaisieSup())) {
                    String dateSupFormat = "";
                    if (entity.getEspionSaisieSup().trim().length() > 7) {
                        dateSupFormat = CIUtil.formatDate(entity.getEspionSaisieSup().substring(0, 8));
                        System.out.println(dateSupFormat);
                    }
                    currentBean.setCol("", 10, getSession().getLabel("SUSPENS_SUP_JOURNAL") + " " + dateSupFormat);
                    beans.put(entityID, currentBean);
                    return getSession().getLabel("SUSPENS_SUP_JOURNAL") + " " + dateSupFormat;
                }
                FWParametersSystemCodeManager csManager = getCsTypeCompteManager(getSession());
                FWParametersSystemCode cs = csManager.getCodeSysteme(entity.getIdTypeCompte());
                currentBean.setCol("", 10, cs.getCurrentCodeUtilisateur().getLibelle()
                        + (entity.isHidden() ? " (" + getSession().getLabel("MSG_ECRITURE_CACHE") + ")" : ""));
                beans.put(entityID, currentBean);
                return cs.getCurrentCodeUtilisateur().getLibelle()
                        + (entity.isHidden() ? " (" + getSession().getLabel("MSG_ECRITURE_CACHE") + ")" : "");
            } catch (Exception e) {
                currentBean.setCol("", 10, "");
                beans.put(entityID, currentBean);
                return null;
            }
        }
        if ("COL_14".equals(jrField.getName())) {
            try {
                // Si suspens supprimé et qu' on a le spy de sup
                if (CIEcriture.CS_CI_SUSPENS_SUPPRIMES.equals(entity.getIdTypeCompte())) {
                    if (!JadeStringUtil.isEmpty(entity.getEspionSaisieSup())
                            && (entity.getEspionSaisieSup().length() >= 14)) {
                        currentBean.setCol("", 11, entity.getEspionSaisieSup().substring(14));
                        beans.put(entityID, currentBean);
                        return entity.getEspionSaisieSup().substring(14);
                    }
                }
                currentBean.setCol("", 11, entity.getEspionSaisie().substring(14));
                beans.put(entityID, currentBean);
                return entity.getEspionSaisie().substring(14);
            } catch (Exception e) {
                currentBean.setCol("", 11, "");
                beans.put(entityID, currentBean);
                return "";
            }
        }
        currentBean.setCol("", 11, "");
        beans.put(entityID, currentBean);
        return null;
    }

    public ArrayList getListBeans() {
        return new ArrayList(beans.values());
    }

    /**
	 *
	 */
    @Override
    public boolean next() throws net.sf.jasperreports.engine.JRException {
        entity = null;
        try {
            if (container == null) {
                this.find(0);
                container = getContainer().iterator();
            }
            // lit le nouveau entity
            if (container.hasNext()) {
                entity = (CIExtendedEcriture) container.next();
            }
            _index++;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // vrai : il existe une entity, faux: fin du select
        return (entity != null);
    }
}
