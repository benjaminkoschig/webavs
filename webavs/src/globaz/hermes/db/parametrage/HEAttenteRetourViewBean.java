package globaz.hermes.db.parametrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.gestion.HEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.utils.ChampsMap;
import globaz.hermes.utils.HEUtil;
import globaz.hermes.utils.StringUtils;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Insérez la description du type ici. Date de création : (19.12.2002 11:27:29)
 * 
 * @author: Administrator
 */
public class HEAttenteRetourViewBean extends BEntity implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** première clé étrangère, HEA_RNIANN * */
    public static int FK_HEA_RNIANN = 1;
    /** L'annonce initiatrice de cet enregistrement */
    private HEOutputAnnonceListViewBean annonce = new HEOutputAnnonceListViewBean();
    /** les champs des annonces correspondant */
    private ChampsMap champs = new ChampsMap();
    /** RODRAP */
    private String dateDernierRappel = new String();
    /** (RNIANN) */
    private String idAnnonce = new String();
    /** (HEA_RNIANN) */
    private String idAnnonceRetour = new String();
    /** (ROTATT) */
    private String idAnnonceRetourAttendue = new String();
    /** Fichier HEAREAP */
    /** (ROIARA) */
    private String idAttenteRetour = new String();
    /** les libellés des champs */
    private HashMap libTable = new HashMap();
    /** motif ROMOT */
    protected String motif = new String();
    /** modif NNSS */
    private Boolean nnss = new Boolean(false);
    /** numéro avs ROAVS */
    protected String numeroAvs = new String();
    private String numeroAvsNNSS = "";
    /** caisse ROCAIS */
    protected String numeroCaisse = new String();
    /** parametrage de l'annonce attendue */
    private HEParametrageannonce paramAnnonce = new HEParametrageannonce();
    /** (ROLRUN) */
    private String referenceUnique = new String();

    /**
     * Commentaire relatif au constructeur HEAttenteRetourViewBean.
     */
    public HEAttenteRetourViewBean() {
        super();
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
        annonce.setSession(getSession());
        annonce.setForRefUnique(getReferenceUnique());
        annonce.wantCallMethodAfter(true);
        annonce.find(transaction);
        for (int i = 0; i < annonce.size(); i++) {
            HEOutputAnnonceViewBean output = (HEOutputAnnonceViewBean) annonce.getEntity(i);
            HashMap table = output.getInputTable();
            Iterator setIte = table.keySet().iterator();
            while (setIte.hasNext()) {
                Object o = setIte.next();
                if (!HEAnnoncesViewBean.isForbiddenField((String) o) && !champs.containsKey(o)) {
                    champs.put(o, table.get(o));
                    HEChampsViewBean champsLabel = new HEChampsViewBean();
                    champsLabel.setSession(getSession());
                    champsLabel.setIdCode((String) o);
                    champsLabel.retrieve(transaction);
                    libTable.put(o, champsLabel.getLibelle());
                }
            }
        }
        paramAnnonce.setSession(getSession());
        paramAnnonce.setIdParametrageAnnonce(getIdAnnonceRetourAttendue());
        paramAnnonce.retrieve(transaction);
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
        //
        // ICI modifier les annonces !
        for (int i = 0; i < annonce.size(); i++) {
            HEAnnoncesViewBean monAnnonce = (HEAnnoncesViewBean) annonce.getEntity(i);
            HashMap m = monAnnonce.getInputTable(); // ancienne valeurs
            // balaye les anciennes valeurs
            // si l'ancienne valeur est présente et différente on remplace
            Iterator it = m.keySet().iterator();
            while (it.hasNext()) {
                Object oldKey = it.next();
                if (champs.containsKey(oldKey) && !HEAnnoncesViewBean.isForbiddenField((String) oldKey)) { // m.remove(oldKey);
                    m.put(oldKey, champs.get(oldKey));
                }
            }
            monAnnonce.setInputTable(m);
            monAnnonce.update(transaction);
        }
    }

    /**
     * Effectue des traitements avant un ajout dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements avant l'ajout de l'entité dans la BD
     * <p>
     * L'exécution de l'ajout n'est pas effectuée si le buffer d'erreurs n'est pas vide après l'exécution de
     * <code>_beforeAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        setIdAttenteRetour(_incCounter(transaction, getIdAnnonceRetour()));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HEAREAP";
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
        idAnnonce = statement.dbReadNumeric("RNIANN");
        idAnnonceRetour = statement.dbReadNumeric("HEA_RNIANN");
        idAnnonceRetourAttendue = statement.dbReadNumeric("ROTATT");
        referenceUnique = statement.dbReadString("ROLRUN");
        // ///////
        /** numéro avs ROAVS */
        numeroAvs = statement.dbReadString("ROAVS");
        /** motif ROMOT */
        motif = statement.dbReadString("ROMOT");
        /** caisse ROCAIS */
        numeroCaisse = statement.dbReadString("ROCAIS");
        /** date de rappel RODRAP */
        dateDernierRappel = statement.dbReadDateAMJ("RODRAP");
        // Modification NNSS
        nnss = statement.dbReadBoolean("ROBNNS");
        if (nnss.booleanValue()) {
            numeroAvsNNSS = "true";
        } else {
            numeroAvsNNSS = "false";
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
        // NNSS
        if ("true".equalsIgnoreCase(getNumeroAvsNNSS())) {
            setNnss(new Boolean("true"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writeAlternateKey(BStatement, int)
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == FK_HEA_RNIANN) {
            statement.writeKey("HEA_RNIANN", _dbWriteNumeric(statement.getTransaction(), getIdAnnonceRetour(), ""));
        }
    }

    /**

	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("ROIARA", _dbWriteNumeric(statement.getTransaction(), getIdAttenteRetour(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("ROIARA",
                _dbWriteNumeric(statement.getTransaction(), getIdAttenteRetour(), "idAttenteRetour"));
        statement.writeField("RNIANN", _dbWriteNumeric(statement.getTransaction(), getIdAnnonce(), "idAnnonce"));
        statement.writeField("HEA_RNIANN",
                _dbWriteNumeric(statement.getTransaction(), getIdAnnonceRetour(), "idAnnonceRetour"));
        statement.writeField("ROTATT",
                _dbWriteNumeric(statement.getTransaction(), getIdAnnonceRetourAttendue(), "idAnnonceRetourAttendue"));
        statement.writeField("ROLRUN",
                _dbWriteString(statement.getTransaction(), getReferenceUnique(), "referenceUnique"));
        // ///////
        /** numéro avs ROAVS */
        if (HEUtil.isNNSSActif(getSession())) {
            statement.writeField("ROAVS",
                    _dbWriteString(statement.getTransaction(), StringUtils.removeDots(getNumeroAvs()).trim()));
        } else {
            statement.writeField(
                    "ROAVS",
                    _dbWriteString(statement.getTransaction(),
                            StringUtils.padAfterString(StringUtils.removeDots(getNumeroAvs()).trim(), "0", 11),
                            "num avs"));
        }
        /** motif ROMOT */
        statement.writeField("ROMOT", _dbWriteString(statement.getTransaction(), getMotif(), "motif"));
        /** caisse ROCAIS */
        statement.writeField("ROCAIS", _dbWriteString(statement.getTransaction(), getNumeroCaisse(), "caisse"));
        /** date de rappel RODRAP */
        statement.writeField("RODRAP",
                _dbWriteDateAMJ(statement.getTransaction(), getDateDernierRappel(), "dateDernierRappel"));
        statement.writeField("ROBNNS",
                _dbWriteBoolean(statement.getTransaction(), getNnss(), BConstants.DB_TYPE_BOOLEAN_CHAR, "nnss"));
    }

    public void addValues(HashMap request) {
        Set s = request.keySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            String attributeName = (String) it.next();
            // for (Enumeration e =
            // request.keySet().values().getParameterNames();
            // e.hasMoreElements();) {
            // String attributeName = (String) e.nextElement();
            if (attributeName.startsWith("118") || attributeName.startsWith("TOSTR")) { // c'est
                // un
                // champs
                String value = String.valueOf(request.get(attributeName));
                if (attributeName.startsWith("TOSTR")) {
                    attributeName = attributeName.substring(5, attributeName.length());
                }
                if (!HEAnnoncesViewBean.isForbiddenField(attributeName)) {
                    champs.replace(attributeName, value);
                }
            }
        }
    }

    private boolean forbiddenFields(String idChamps) {
        /*
         * for (int i = 0; i < forbiddenFields.length; i++) { if (idChamps.equals(forbiddenFields[i])) return true; }
         */
        return false;
    }

    public String getAnnonceAttendue() {
        try {
            return paramAnnonce.getCsCodeApplication().getCurrentCodeUtilisateur().getCode();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public void getChamps() {
        for (int i = 0; i < annonce.getSize(); i++) {
        }
    }

    public String getChampsKey(int i) {
        return champs.keyAt(i);
    }

    public String getChampsLabel(String code) {
        return (String) libTable.get(code);
    }

    public int getChampsSize() {
        return champs.size();
    }

    public String getChampsValueAt(int index) {
        return champs.valueAt(index);
    }

    /** le créateur de l'annonce */
    public String getCreator() {
        HEOutputAnnonceViewBean annonceEntity = (HEOutputAnnonceViewBean) annonce.getEntity(0);
        String utilisateur = annonceEntity.getUtilisateur();
        if (JAUtil.isStringEmpty(utilisateur)) {
            utilisateur = "inconnu";
        }
        String prog = annonceEntity.getIdProgramme();
        return prog + " (" + utilisateur + ")";
    }

    public String getDateCreation() {
        HEOutputAnnonceViewBean annonceEntity = (HEOutputAnnonceViewBean) annonce.getEntity(0);
        try {
            JADate date = new JADate();
            date.fromAMJ(new BigDecimal(annonceEntity.getDateAnnonce()));
            return date.getDay() + "/" + date.getMonth() + "/" + date.getYear();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * Returns the dateDernierRappel.
     * 
     * @return String
     */
    public String getDateDernierRappel() {
        return dateDernierRappel;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public String getIdAnnonceRetour() {
        return idAnnonceRetour;
    }

    public String getIdAnnonceRetourAttendue() {
        return idAnnonceRetourAttendue;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdAttenteRetour() {
        return idAttenteRetour;
    }

    /** */
    public String getIdStatut() {
        HEOutputAnnonceViewBean annonceEntity = (HEOutputAnnonceViewBean) annonce.getEntity(0);
        return annonceEntity.getStatut();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 09:53:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMotif() {
        return motif;
    }

    public Boolean getNnss() {
        return nnss;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 09:53:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumeroAvs() {
        return numeroAvs;
    }

    public String getNumeroAVSArcOrigine() {
        try {
            HEOutputAnnonceViewBean annonceEntity = (HEOutputAnnonceViewBean) annonce.getEntity(0);
            return JAUtil.formatAvs(annonceEntity.getField(IHEAnnoncesViewBean.NUMERO_ASSURE));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getNumeroAvsNNSS() {
        return numeroAvsNNSS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 09:53:34)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumeroCaisse() {
        return numeroCaisse;
    }

    public String getReference() {
        return getIdAttenteRetour() + " (" + getReferenceUnique() + ")";
    }

    public String getReferenceUnique() {
        return referenceUnique;
    }

    public String getStatut() {
        try {
            HEOutputAnnonceViewBean annonceEntity = (HEOutputAnnonceViewBean) annonce.getEntity(0);
            return annonceEntity.getCSStatutLibelle();
        } catch (Exception e) {
            e.printStackTrace();
            return "erreur";
        }
    }

    public FWParametersSystemCodeManager getStatutLibList() throws Exception {
        FWParametersSystemCodeManager lcsStatut = new FWParametersSystemCodeManager();
        lcsStatut.setForIdGroupe("HESTATUT");
        lcsStatut.setForIdTypeCode("11100007");
        lcsStatut.setSession(getSession());
        try {
            lcsStatut.find();
            return lcsStatut;
        } catch (Exception e) {
            e.printStackTrace();
            return lcsStatut;
        }
    }

    public String getTypeAnnonce() {
        try {
            HEOutputAnnonceViewBean annonceEntity = (HEOutputAnnonceViewBean) annonce.getEntity(0);
            String type = annonceEntity.getCodeAppLibelle();
            if (annonceEntity.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE).trim().length() != 0) {
                type += " (motif " + annonceEntity.getField(IHEAnnoncesViewBean.MOTIF_ANNONCE) + ")";
            }
            return type;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public boolean isRevenuCache() {
        try {
            return ((HEApplication) getSession().getApplication()).isRevenuCache(getSession().getUserId(),
                    getSession(), StringUtils.removeDots(getNumeroAvs()));
        } catch (Exception e) {
            setMessage(e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    public void replaceValues(HashMap request) {
        Set s = request.keySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            // for (Enumeration e = request.getParameterNames();
            // e.hasMoreElements();) {
            // String attributeName = (String) e.nextElement();
            String attributeName = String.valueOf(it.next());
            if (attributeName.startsWith("118")) { // c'est un champs
                champs.replace(attributeName, String.valueOf(request.get(attributeName)));
            }
        }
    }

    /**
     * Sets the dateDernierRappel.
     * 
     * @param dateDernierRappel
     *            The dateDernierRappel to set
     */
    public void setDateDernierRappel(String dateDernierRappel) {
        this.dateDernierRappel = dateDernierRappel;
    }

    public void setIdAnnonce(String newIdAnnonce) {
        idAnnonce = newIdAnnonce;
    }

    public void setIdAnnonceRetour(String newIdAnnonceRetour) {
        idAnnonceRetour = newIdAnnonceRetour;
    }

    public void setIdAnnonceRetourAttendue(String newIdAnnonceRetourAttendue) {
        idAnnonceRetourAttendue = newIdAnnonceRetourAttendue;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newH
     *            String
     */
    public void setIdAttenteRetour(String newIdAttenteRetour) {
        idAttenteRetour = newIdAttenteRetour;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 09:53:34)
     * 
     * @param newMotif
     *            java.lang.String
     */
    public void setMotif(java.lang.String newMotif) {
        motif = newMotif;
    }

    public void setNnss(Boolean nnss) {
        this.nnss = nnss;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 09:53:34)
     * 
     * @param newNumeroAvs
     *            java.lang.String
     */
    public void setNumeroAvs(java.lang.String newNumeroAvs) {
        numeroAvs = newNumeroAvs;
    }

    public void setNumeroAvsNNSS(String numeroAvsNNSS) {
        this.numeroAvsNNSS = numeroAvsNNSS;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (09.07.2003 09:53:34)
     * 
     * @param newNumeroCaisse
     *            java.lang.String
     */
    public void setNumeroCaisse(java.lang.String newNumeroCaisse) {
        newNumeroCaisse = StringUtils.unPad(newNumeroCaisse);
        newNumeroCaisse = StringUtils.removeUnsignigicantZeros(newNumeroCaisse);
        numeroCaisse = newNumeroCaisse;
    }

    public void setPrimaryKey() {
        setAlternateKey(0);
    }

    public void setReferenceUnique(String newReferenceUnique) {
        referenceUnique = newReferenceUnique;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toMyString() {
        return getIdAttenteRetour() + "-" + getIdAnnonce() + "-" + getIdAnnonceRetour() + "-"
                + getIdAnnonceRetourAttendue() + "-" + getReferenceUnique() + "-" + getNumeroAvs() + "-" + getMotif()
                + "-" + getNumeroCaisse();
    }
}
