package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.gestion.HEAnnoncesListViewBean;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.utils.StringUtils;
import java.util.HashMap;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (19.12.2002 11:27:29)
 * 
 * @author: Administrator
 */
public class HEAttenteRetourOptimizedViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String caisseCI = "";
    /** Liste des champs pour l'écran _de */
    protected HEAttenteRetourChampsOptimizedListViewBean champs = new HEAttenteRetourChampsOptimizedListViewBean();
    protected String creator = "";
    protected String dateCreation = "";
    //
    protected String enregistrement = "";
    private final String HEAREAP_ARCHIVE = "HEAREAR";
    private final String HEAREAP_EN_COURS = "HEAREAP";
    private String idAnnonceDepart = "";
    /** Liste des champs pour l'écran rcListe */
    protected String idAttenteRetour = "";
    private String idRetour = "";
    private String idTypeAnnonceAttendue = "";
    //
    protected String idTypeRetour = "";
    private boolean isArchivage = false;
    // modif NNSS
    private Boolean nnss = new Boolean(false);
    private Boolean nnss_attendu = new Boolean(false);
    protected String numavs = "";
    //
    protected String numCaisse = "";
    // numero avs pour lequel on attend
    protected String numeroAvsAttendu = "";
    private String numeroAvsNNSS = "";
    private String numeroAvsNNSS_attendu = "";
    //
    protected String refUnique = "";

    protected String statut = "";
    protected HashMap tmpRequest;

    protected String typeAnnonce = "";
    protected String typeAnnonceAttendue = "";

    /**
     * Effectue des traitements après une suppression de la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements après la suppression de l'entité de la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterDelete()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws java.lang.Exception {
        // ensuite les retours
        HEAttenteRetourListViewBean retours = new HEAttenteRetourListViewBean();
        retours.setSession(getSession());
        retours.setForReferenceUnique(getRefUnique());
        retours.wantCallMethodAfter(false);
        retours.wantCallMethodAfterFind(false);
        retours.wantCallMethodBefore(false);
        retours.wantCallMethodBeforeFind(false);
        retours.find(transaction);
        for (int i = 0; i < retours.size(); i++) {
            HEAttenteRetourViewBean entity = (HEAttenteRetourViewBean) retours.getEntity(i);
            entity.setSession(getSession());
            // entity.retrieve(transaction);
            entity.delete(transaction);
        }
    }

    /**
     * Effectue des traitements après une lecture dans la BD et après avoir vidé le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements après la lecture de l'entité dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws java.lang.Exception {
        // numavs = JAUtil.formatAvs(numavs);
        // il me faut l'annonce ARC d'origine pour cet enregistrement
        /*
         * HEOutputAnnonceListViewBean out = new HEOutputAnnonceListViewBean(); out.setSession(getSession());
         * out.setForRefUnique(getRefUnique()); out.setLikeEnregistrement("1101"); out.wantCallMethodBefore(false);
         * out.wantCallMethodAfter(true); out.wantCallMethodBeforeFind(false); out.wantCallMethodAfterFind(false);
         * out.find(transaction); if (out.size() >= 1) { HEOutputAnnonceViewBean output = (HEOutputAnnonceViewBean)
         * out.getEntity(0); typeAnnonce = output.getField(HEAnnoncesViewBean.MOTIF_ANNONCE); } else { typeAnnonce = "";
         * }
         */
        // Attention, le statut est peut-être null !!!
        if (!JAUtil.isStringEmpty(getStatut())) {
            setStatut(((HEApplication) getSession().getApplication()).getCsStatutListe(getSession())
                    .getCodeSysteme(getStatut()).getCurrentCodeUtilisateur().getLibelle());
        }
        // pour afficher la caisse
        if (idTypeRetour.equals("38") || idTypeRetour.equals("39")) {
            // je cherche un 25 avec cette reference unique
            String t = "";
        }
        try {
            HEParametrageannonce paramAnnonce = new HEParametrageannonce();
            paramAnnonce.setIdParametrageAnnonce(typeAnnonceAttendue);
            caisseCI = "";
            if (typeAnnonceAttendue.trim().equals("9")) {
                caisseCI = numCaisse;
            }
            paramAnnonce.retrieve(transaction);
            typeAnnonceAttendue = ((HEApplication) getSession().getApplication())
                    .getCsCodeApplicationListe(getSession()).getCodeSysteme(paramAnnonce.getIdCSCodeApplication())
                    .getCurrentCodeUtilisateur().getLibelle();
            if (caisseCI.length() != 0) {
                typeAnnonceAttendue += " (" + caisseCI + ")";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ERROR - REPORT TO GLOBAZ : \n" + e.toString());
            typeAnnonceAttendue = "ERROR - REPORT TO GLOBAZ";
        }
    }

    /**
     * Effectue des traitements après une lecture dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements après la lecture de l'entité dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterRetrieveWithResultSet(BStatement statement) throws java.lang.Exception {
    }

    /**
     * Effectue des traitements après une mise à jour dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements après la mise à jour de l'entité dans la BD
     * <p>
     * La transaction n'est pas validée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_afterUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws java.lang.Exception {
        HEAttenteRetourViewBean attente = new HEAttenteRetourViewBean();
        attente.setSession(getSession());
        attente.setIdAttenteRetour(getIdAttenteRetour());
        attente.retrieve(transaction);
        attente.addValues(tmpRequest);
        attente.update(transaction);
    }

    /**
     * Effectue des traitements avant une suppression de la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant la suppression de l'entité de la BD
     * <p>
     * L'exécution de la suppression n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeDelete()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws java.lang.Exception {
        // on supprime d'abord l'annonce
        HEAnnoncesListViewBean annonces = new HEAnnoncesListViewBean();
        annonces.setSession(getSession());
        annonces.wantCallMethodAfter(false);
        annonces.setForRefUnique(getRefUnique());
        annonces.find(transaction);
        for (int i = 0; i < annonces.size(); i++) {
            HEAnnoncesViewBean annonce = (HEAnnoncesViewBean) annonces.getEntity(i);
            annonce.wantCallMethodAfter(false);
            annonce.wantCallMethodBefore(false);
            annonce.wantCallValidate(false);
            // annonce.retrieve(transaction);
            annonce.delete(transaction);
        }
    }

    /**
     * Effectue des traitements avant une lecture de la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant la lecture de l'entité de la BD
     * <p>
     * L'exécution de la lecture n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeRetrieve()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeRetrieve(BTransaction transaction) throws java.lang.Exception {
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        if (isArchivage()) {
            return HEAREAP_ARCHIVE;
        } else {
            return HEAREAP_EN_COURS;
        }
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idAttenteRetour = statement.dbReadNumeric("ROIARA");
        dateCreation = statement.dbReadDateAMJ("RNDDAN");
        enregistrement = statement.dbReadString("RNLENR");
        enregistrement = JAUtil.padString(enregistrement, 120);
        // numavs = enregistrement.substring(36, 47);

        // numavs = statement.dbReadString("RNAVS");
        // le motif
        typeAnnonce = statement.dbReadNumeric("ROMOT");
        typeAnnonceAttendue = statement.dbReadNumeric("ROTATT");
        idTypeAnnonceAttendue = statement.dbReadNumeric("ROTATT");
        idRetour = statement.dbReadNumeric("HEA_RNIANN");
        statut = statement.dbReadNumeric("RNTSTA");
        String uti = statement.dbReadString("RNLUTI");
        //
        refUnique = statement.dbReadString("ROLRUN");
        //
        if (refUnique.length() == 0) {
            refUnique = statement.dbReadString("ROLRUN");
        }
        creator = statement.dbReadString("RNLUTI"); // + "@" +
        // statement.dbReadString("RNTPRO");
        /*
         * if (uti.trim().length() != 0) { creator = uti; }
         */
        /*
         * idTypeRetour = statement.dbReadString("IDRETOUR"); typeAnnonceAttendue = statement.dbReadString("LIBRETOUR");
         * if (idTypeRetour.trim().equals("39")) { typeAnnonceAttendue += "(" + statement.dbReadString("ROCAIS") + ")";
         * }
         */

        // numeroAvsAttendu = statement.dbReadString("ROAVS");
        numCaisse = statement.dbReadString("ROCAIS");
        idAnnonceDepart = statement.dbReadNumeric("RNIANN");

        // Modification NNSS
        nnss = statement.dbReadBoolean("RNBNNS");
        if (nnss.booleanValue()) {
            numeroAvsNNSS = "true";
            setNumavs(statement.dbReadString("RNAVS"));
        } else {
            numeroAvsNNSS = "false";
            setNumavs(statement.dbReadAVS("RNAVS"));
        }

        nnss_attendu = statement.dbReadBoolean("ROBNNS");
        if (nnss_attendu.booleanValue()) {
            numeroAvsNNSS_attendu = "true";
            setNumeroAvsAttendu(statement.dbReadString("ROAVS"));
        } else {
            numeroAvsNNSS_attendu = "false";
            setNumeroAvsAttendu(statement.dbReadAVS("ROAVS"));
        }

    }

    @Override
    protected void _validate(BStatement statement) {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("ROIARA", _dbWriteNumeric(statement.getTransaction(), getIdAttenteRetour(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // statement.writeField("ROIARA",
        // _dbWriteNumeric(statement.getTransaction(), getIdAttenteRetour(),
        // "idAttenteRetour"));
    }

    public String getArcEtatNominatif() {
        // System.out.println(this.isConfirmed()?"":"2");
        return getArcEtatNominatif(20);
    }

    public String getArcEtatNominatif(int limit) {
        try {
            String name = enregistrement.substring(47, 86).trim();
            if (name.length() > limit) {
                return name.substring(0, limit - 1) + "...";
            }
            return name;
        } catch (ArrayIndexOutOfBoundsException aie) {
            aie.printStackTrace();
            return getArcEtatNominatif();
        } catch (StringIndexOutOfBoundsException se) {
            se.printStackTrace();
            return "";
        }
    }

    /**
     * Returns the caisseCI.
     * 
     * @return String
     */
    public String getCaisseCI() {
        return caisseCI;
    }

    public Vector getChampsAsCodeSystem(String keyChamp) {
        long s = System.currentTimeMillis();
        Vector vList = new Vector();
        // ajoute un blanc
        String[] list = new String[2];
        list[0] = "";
        list[1] = "";
        if (keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE) || keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE_1)
                || keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE_2)
                || keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE_3)) {
            vList.add(list);
            try {
                HEApplication app = (HEApplication) getSession().getApplication();
                FWParametersSystemCodeManager manager = app.getCsPaysListe(getSession());
                for (int i = 0; i < manager.size(); i++) {
                    list = new String[2];
                    FWParametersSystemCode entity = (FWParametersSystemCode) manager.getEntity(i);
                    list[0] = entity.getCurrentCodeUtilisateur().getCodeUtilisateur();
                    list[1] = entity.getCurrentCodeUtilisateur().getLibelle();
                    vList.add(list);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // si probleme, retourne list vide.
            }
            // System.out.println((System.currentTimeMillis() - s) + " ms !!!");
            return vList;
        }
        return new Vector();
    }

    public String getChampsAsCodeSystemDefaut(String keyChamp) {
        HEAttenteRetourChampsOptimizedViewBean entity;
        for (int i = 0; i < champs.size(); i++) {
            entity = (HEAttenteRetourChampsOptimizedViewBean) champs.getEntity(i);
            if (entity.getIdChamp().equals(keyChamp)) {
                return entity.getValeur();
            }
        }
        return "";
        // return HEAnnoncesViewBean.getChampsAsCodeSystemDefaut(keyChamp);
    }

    public HEAttenteRetourChampsOptimizedViewBean getChampsAt(int index) {
        return (HEAttenteRetourChampsOptimizedViewBean) champs.getEntity(index);
    }

    public int getChampsSize() {
        return champs.size();
    }

    public String getCodeApplication() {
        try {
            return enregistrement.substring(0, 2);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCreator() {
        return creator;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateCreation() {
        return dateCreation;
    }

    /**
     * Returns the idAnnonceDepart.
     * 
     * @return String
     */
    public String getIdAnnonceDepart() {
        return idAnnonceDepart;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAttenteRetour() {
        return idAttenteRetour;
    }

    /**
     * Returns the idRetour.
     * 
     * @return String
     */
    public String getIdRetour() {
        return idRetour;
    }

    /**
     * Returns the idTypeAnnonceAttendue.
     * 
     * @return String
     */
    public String getIdTypeAnnonceAttendue() {
        return idTypeAnnonceAttendue;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumavs() {
        return getNumavs(true);
    }

    public String getNumavs(boolean wantZeros) {
        if (wantZeros) {
            return numavs;
        } else {
            String tmpAvs = StringUtils.removeDots(numavs);
            tmpAvs = JAUtil.formatAvs(tmpAvs);
            if (tmpAvs.length() == 14) {
                if (tmpAvs.endsWith("000")) {
                    return tmpAvs.substring(0, 10);
                } else if (tmpAvs.endsWith("00")) {
                    return tmpAvs.substring(0, 12);
                }
            }
            return numavs;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2003 10:29:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumCaisse() {
        return numCaisse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.07.2003 14:15:01)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumeroAvsAttendu() {
        return numeroAvsAttendu;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.07.2003 14:15:01)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumeroAvsAttenduFormatted() {
        return JAUtil.formatAvs(numeroAvsAttendu);
    }

    // modif NNSS
    public String getNumeroAvsNNSS() {
        return numeroAvsNNSS;
    }

    public String getNumeroAvsNNSS_attendu() {
        return numeroAvsNNSS_attendu;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.01.2003 12:00:42)
     * 
     * @return java.lang.String
     */
    public java.lang.String getRefUnique() {
        return refUnique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getStatut() {
        return statut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getStatutLibelle() {
        try {
            return ((HEApplication) getSession().getApplication()).getCsStatutListe(getSession())
                    .getCodeSysteme(statut).getCurrentCodeUtilisateur().getLibelle();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTypeAnnonce() {
        return typeAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTypeAnnonceAttendue() {
        return typeAnnonceAttendue;
    }

    /**
     * Returns the isArchivage.
     * 
     * @return boolean
     */
    public boolean isArchivage() {
        return isArchivage;
    }

    public boolean isConfirmed() {
        return !JAUtil.isIntegerEmpty(idRetour);
    }

    public void loadChamps() {
        champs.setSession(getSession());
        champs.setForIdAttenteRetour(idAttenteRetour);
        champs.setIsArchivage(isArchivage());
        try {
            champs.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putFieldsToUpdate(HashMap req) {
        tmpRequest = req;
        /*
         * for (Enumeration e = req.getParameterNames(); e.hasMoreElements();) { String attributeName = (String)
         * e.nextElement(); if (attributeName.startsWith("118")) { // c'est un champs fieldsToUpdate.put(attributeName,
         * req.getParameter(attributeName)); } }
         */
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @param newCreator
     *            java.lang.String
     */
    public void setCreator(java.lang.String newCreator) {
        creator = newCreator;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @param newDateCreation
     *            java.lang.String
     */
    public void setDateCreation(java.lang.String newDateCreation) {
        dateCreation = newDateCreation;
    }

    /**
     * Sets the idAnnonceDepart.
     * 
     * @param idAnnonceDepart
     *            The idAnnonceDepart to set
     */
    public void setIdAnnonceDepart(String idAnnonceDepart) {
        this.idAnnonceDepart = idAnnonceDepart;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @param newIdAttenteRetour
     *            java.lang.String
     */
    public void setIdAttenteRetour(java.lang.String newIdAttenteRetour) {
        idAttenteRetour = newIdAttenteRetour;
    }

    /**
     * Sets the isArchivage.
     * 
     * @param isArchivage
     *            The isArchivage to set
     */
    public void setIsArchivage(boolean isArchivage) {
        this.isArchivage = isArchivage;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @param newNumavs
     *            java.lang.String
     */
    public void setNumavs(java.lang.String newNumavs) {
        numavs = newNumavs;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (08.07.2003 10:29:26)
     * 
     * @param newNumCaisse
     *            java.lang.String
     */
    public void setNumCaisse(java.lang.String newNumCaisse) {
        numCaisse = newNumCaisse;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.07.2003 14:15:01)
     * 
     * @param newNumeroAvsAttendu
     *            java.lang.String
     */
    public void setNumeroAvsAttendu(java.lang.String newNumeroAvsAttendu) {
        numeroAvsAttendu = newNumeroAvsAttendu;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.07.2003 14:15:01)
     * 
     * @param newNumeroAvsAttendu
     *            java.lang.String
     */
    public void setNumeroAvsAttenduFormatted(java.lang.String newNumeroAvsAttendu) {
        numeroAvsAttendu = StringUtils.removeDots(newNumeroAvsAttendu);
    }

    public void setNumeroAvsNNSS(String numeroAvsNNSS) {
        this.numeroAvsNNSS = numeroAvsNNSS;
    }

    public void setNumeroAvsNNSS_attendu(String numeroAvsNNSS_attendu) {
        this.numeroAvsNNSS_attendu = numeroAvsNNSS_attendu;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.01.2003 12:00:42)
     * 
     * @param newRefUnique
     *            java.lang.String
     */
    public void setRefUnique(java.lang.String newRefUnique) {
        refUnique = newRefUnique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @param newStatut
     *            java.lang.String
     */
    public void setStatut(java.lang.String newStatut) {
        statut = newStatut;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @param newTypeAnnonce
     *            java.lang.String
     */
    public void setTypeAnnonce(java.lang.String newTypeAnnonce) {
        typeAnnonce = newTypeAnnonce;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 11:05:27)
     * 
     * @param newTypeAnnonceAttendue
     *            java.lang.String
     */
    public void setTypeAnnonceAttendue(java.lang.String newTypeAnnonceAttendue) {
        typeAnnonceAttendue = newTypeAnnonceAttendue;
    }

    public String toMyString() {
        return idAttenteRetour + "-" + numavs + "-" + dateCreation + "-" + typeAnnonce + "-" + statut + "-" + creator
                + "-" + typeAnnonceAttendue;
    }

}
