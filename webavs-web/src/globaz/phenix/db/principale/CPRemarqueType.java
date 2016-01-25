package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

public class CPRemarqueType extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_COMMENTAIRE = "606002";
    /** (IOTEXT) */
    // EmplacementRemarque
    public final static String CS_REMARQUE = "606001";
    /** (IOIDRE) */
    private String emplacement = "";
    /** Fichier CPREMAP */
    private String idRemarqueType = "";
    /** (IOTEMP) */
    private String langue = "";
    /** (IOIDRE) */
    private String oldIdRemarqueType = "";
    /** (IOTLAN) */
    private String texteRemarqueType = "";

    // code systeme
    /**
     * Commentaire relatif au constructeur CPRemarqueType
     */
    public CPRemarqueType() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incr�mente de +1 le num�ro
        // BTC: ne plus incr�menter automatiquement le compteur
        // Si l'idRemarque n'est pas explicitement donn�e, incr�menter
        // automatiquement
        if (JadeStringUtil.isEmpty(getIdRemarqueType())) {
            setIdRemarqueType(_incCounter(transaction, idRemarqueType));
        }
    }

    /*
     * Traitement avant suppresion
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Suppression impossible s'il existe un lien
        CPLienTypeDecRemarqueManager lien = new CPLienTypeDecRemarqueManager();
        lien.setSession(getSession());
        lien.setForIdRemarqueType(getIdRemarqueType());
        lien.find(transaction);
        if (lien.size() > 0) {
            _addError(transaction, getSession().getLabel("CP_MSG_0098"));
        }
        // Test si commentaire associ�
        CPCommentaireRemarqueTypeManager lienComm = new CPCommentaireRemarqueTypeManager();
        lienComm.setSession(getSession());
        lienComm.setForIdRemarqueType(getIdRemarqueType());
        lienComm.find(transaction);
        if (lien.size() > 0) {
            _addError(transaction, getSession().getLabel("CP_MSG_0099"));
        }
    }

    /*
     * Traitement avant mise � jour
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) {
        /*
         * propager les modifications de l'idRemarqueType dans le lien qui lie la d�cision au type de remarque, si
         * celle-ci a �t� modifier
         */
        if (!JadeStringUtil.isEmpty(getOldIdRemarqueType()) && !JadeStringUtil.isEmpty(getIdRemarqueType())) {
            if (!getOldIdRemarqueType().equalsIgnoreCase(getIdRemarqueType())) {
                CPLienTypeDecRemarqueManager lienManager = new CPLienTypeDecRemarqueManager();
                // rechercher les anciens liens
                lienManager.setSession(getSession());
                lienManager.setForIdRemarqueType(getOldIdRemarqueType());
                try {
                    lienManager.find(transaction);
                    for (int i = 0; i < lienManager.size(); i++) {
                        CPLienTypeDecRemarque lien = (CPLienTypeDecRemarque) lienManager.getEntity(i);
                        // r�ajourner avec les nouveaux id
                        lien.setIdRemarqueType(getIdRemarqueType());
                        lien.update(transaction);
                    }
                } catch (Exception e) {
                    _addError(transaction, e.getMessage());
                }
            }
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPREMAP";
    }

    /**
     * Effectue des traitements apr�s une lecture dans la BD <i>
     * <p>
     * A surcharger pour effectuer les traitements apr�s la lecture de l'entit� dans la BD
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _init() {
        // Emplacement = Rmarque par d�faut
        if (JadeStringUtil.isEmpty(getIdRemarqueType())) {
            setEmplacement(CS_REMARQUE);
        }
        // Langue = langue par d�faut de l'utilisateur
        if (JadeStringUtil.isEmpty(getLangue())) {
            FWParametersSystemCodeManager parm = new FWParametersSystemCodeManager();
            parm.setSession(getSession());
            parm.setForCodeUtilisateur(getSession().getIdLangueISO().toUpperCase());
            parm.setForIdGroupe("PYLANGUE");
            try {
                parm.find();
                setLangue(((FWParametersSystemCode) parm.getEntity(0)).getIdCode());
            } catch (Exception e) {
                JadeLogger.error(this, e);
                e.printStackTrace();
                setLangue("");
            }
        }
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRemarqueType = statement.dbReadNumeric("IOIDRE");
        oldIdRemarqueType = idRemarqueType; // permet d'updater la clef primaire
        emplacement = statement.dbReadNumeric("IOTEMP");
        langue = statement.dbReadNumeric("IOTLAN");
        texteRemarqueType = statement.dbReadString("IOTEXT");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     */
    @Override
    protected void _validate(BStatement statement) {
        if (texteRemarqueType.length() > 255) {
            _addError(
                    statement.getTransaction(),
                    "La remarque d�passe la longueur maximale de 255 caract�res, elle est de "
                            + texteRemarqueType.length() + ". ");
        }
    }

    /**
	
	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        if (!JadeStringUtil.isEmpty(getOldIdRemarqueType())) {
            statement.writeKey("IOIDRE", _dbWriteNumeric(statement.getTransaction(), getOldIdRemarqueType(), ""));
        } else {
            statement.writeKey("IOIDRE", _dbWriteNumeric(statement.getTransaction(), getIdRemarqueType(), ""));
        }
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IOIDRE",
                _dbWriteNumeric(statement.getTransaction(), getIdRemarqueType(), "idRemarqueType"));
        statement.writeField("IOTEMP", _dbWriteNumeric(statement.getTransaction(), getEmplacement(), "emplacement"));
        statement.writeField("IOTLAN", _dbWriteNumeric(statement.getTransaction(), getLangue(), "langue"));
        statement.writeField("IOTEXT",
                _dbWriteString(statement.getTransaction(), getTexteRemarqueType(), "texteRemarqueType"));
    }

    public String getEmplacement() {
        return emplacement;
    }

    /**
     * Ins�rez la description de la m�thode ici.
     * 
     * @return String
     */
    public String getIdRemarqueType() {
        return idRemarqueType;
    }

    public String getLangue() {
        return langue;
    }

    /**
     * Returns the oldIdRemarqueType.
     * 
     * @return String
     */
    public String getOldIdRemarqueType() {
        return oldIdRemarqueType;
    }

    public String getTexteRemarqueType() {
        return texteRemarqueType;
    }

    /*
     * Retourne le texte d'une remarque
     * 
     * @parms idRemarque
     */
    public String getTexteRemarqueType(String idRem) throws java.lang.Exception {
        try {
            CPRemarqueType rema = new CPRemarqueType();
            rema.setSession(getSession());
            rema.setIdRemarqueType(idRem);
            rema.retrieve();
            return rema.getTexteRemarqueType();
        } catch (Exception e) {
            JadeLogger.error(this, e);
            e.printStackTrace();
            return "";
        }
    }

    public void setEmplacement(String newEmplacement) {
        emplacement = newEmplacement;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdRemarqueType(String newIdRemarqueType) {
        idRemarqueType = newIdRemarqueType;
    }

    public void setLangue(String newLangue) {
        langue = newLangue;
    }

    /**
     * Sets the oldIdRemarqueType.
     * 
     * @param oldIdRemarqueType
     *            The oldIdRemarqueType to set
     */
    public void setOldIdRemarqueType(String oldIdRemarqueType) {
        this.oldIdRemarqueType = oldIdRemarqueType;
    }

    public void setTexteRemarqueType(String newTexteRemarqueType) {
        texteRemarqueType = newTexteRemarqueType;
    }
}
