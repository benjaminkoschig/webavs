package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.translation.CodeSystem;
import globaz.norma.db.fondation.PATraduction;
import globaz.osiris.db.comptes.CARubrique;

public class FAOrdreAttribuer extends globaz.globall.db.BEntity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String TABLE_FIELDS = "FAORLIP.ECIDOA, FAORLIP.EHIDOR, FAORLIP.IDRUBRIQUE, FAORLIP.PSPY ";
    private java.lang.String idExterneRubrique = new String();

    private java.lang.String idOrdreAttribuer = new String();
    private java.lang.String idOrdreRegroupement = new String();
    private java.lang.String idRubrique = new String();
    private java.lang.String idTraduction = new String();
    private java.lang.String libelleDe = new String();
    private java.lang.String libelleFr = new String();
    private java.lang.String libelleIt = new String();
    private java.lang.String libelleRubrique = new String();
    private String nature = new String();
    private java.lang.String numCaisse = new String();

    private java.lang.String numOrdreRegroupement = new String();

    /**
     * Commentaire relatif au constructeur FAModulePassage
     */
    public FAOrdreAttribuer() {
        super();
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        setIdOrdreAttribuer(this._incCounter(transaction, "0"));
        FAOrdreRegroupement ordre = new FAOrdreRegroupement();
        if (JadeStringUtil.isEmpty(getIdOrdreRegroupement()) && getIdOrdreRegroupement().equals(null)) {
            if (getNumOrdreRegroupement().equals(null) || getNumOrdreRegroupement().equals("")) {
                setIdOrdreRegroupement("");
            } else {
                FAOrdreRegroupementManager ordreManager = new FAOrdreRegroupementManager();
                ordreManager.setSession(getSession());
                ordreManager.setForOrdreRegroupement(getNumOrdreRegroupement());
                ordreManager.setForNumCaisse(getNumCaisse());
                ordreManager.find(transaction);
                if (ordreManager.size() > 0) {
                } else {
                    ordre.setSession(getSession());
                    ordre.setIdOrdreRegroupement(this._incCounter(transaction, "0"));
                    setIdOrdreRegroupement(ordre.getIdOrdreRegroupement());
                    ordre.setOrdreRegroupement(getNumOrdreRegroupement());
                    ordre.add(transaction);
                }
            }
        }
        setIdOrdreRegroupement(getIdOrdreRegroupement());
    }

    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        FAOrdreRegroupement ordre = new FAOrdreRegroupement();
        if (JadeStringUtil.isEmpty(getIdOrdreRegroupement()) && getIdOrdreRegroupement().equals(null)) {
            if (getNumOrdreRegroupement().equals(null) || getNumOrdreRegroupement().equals("")) {
                setIdOrdreRegroupement("");
            } else {
                FAOrdreRegroupementManager ordreManager = new FAOrdreRegroupementManager();
                ordreManager.setSession(getSession());
                ordreManager.setForOrdreRegroupement(getNumOrdreRegroupement());
                ordreManager.setForNumCaisse(getNumCaisse());
                ordreManager.find(transaction);
                if (ordreManager.size() > 0) {
                } else {
                    ordre.setSession(getSession());
                    ordre.setIdOrdreRegroupement(this._incCounter(transaction, "0"));
                    setIdOrdreRegroupement(ordre.getIdOrdreRegroupement());
                    ordre.setOrdreRegroupement(getNumOrdreRegroupement());
                    ordre.add(transaction);
                }
            }
        } else {
        }
        setIdOrdreRegroupement(getIdOrdreRegroupement());
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "FAORLIP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idOrdreRegroupement = statement.dbReadNumeric("EHIDOR");
        idOrdreAttribuer = statement.dbReadNumeric("ECIDOA");
        idRubrique = statement.dbReadNumeric("IDRUBRIQUE");
        idExterneRubrique = statement.dbReadNumeric("IDEXTERNE");
        idTraduction = statement.dbReadNumeric("IDTRADUCTION");
        numCaisse = statement.dbReadNumeric("EHNCAI");
        numOrdreRegroupement = statement.dbReadNumeric("EHNORD");
        libelleFr = statement.dbReadString("EHLLIF");
        libelleDe = statement.dbReadString("EHLLID");
        libelleIt = statement.dbReadString("EHLLII");
        nature = statement.dbReadNumeric("NATURE");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        String numCaisse = new String();
        FAOrdreAttribuerManager manaOrdre = new FAOrdreAttribuerManager();
        manaOrdre.setSession(getSession());
        manaOrdre.setForIdRubrique(getIdRubrique());
        manaOrdre.find();
        if (manaOrdre.size() > 0) {
            for (int i = 0; i < manaOrdre.size(); i++) {
                numCaisse = ((FAOrdreAttribuer) manaOrdre.get(i)).getNumCaisse();
                if (numCaisse.equals(getNumCaisse())) {
                    _addError(statement.getTransaction(),
                            "Il existe déjà un ordre avec ce numéro de caisse pour cette rubrique");
                } else if (numCaisse.equals("0") || numCaisse.equals("")) {
                    _addError(statement.getTransaction(),
                            "Il existe déjà un ordre pour cette rubrique sans numéro de caisse, veuillez le supprimer avant ");
                }
            }
        }

    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        // Traitement par défaut : pas de clé alternée
        if (alternateKey == 1) {
            statement.writeKey(_getBaseTable() + "IDRUBRIQUE",
                    this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), ""));
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("ECIDOA", this._dbWriteNumeric(statement.getTransaction(), getIdOrdreAttribuer(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("ECIDOA",
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdreAttribuer(), "idOrdreAttribuer"));
        statement.writeField("EHIDOR",
                this._dbWriteNumeric(statement.getTransaction(), getIdOrdreRegroupement(), "idOrdreRegroupement"));
        statement.writeField("IDRUBRIQUE",
                this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
    }

    /**
     * @return
     */
    public java.lang.String getIdExterneRubrique() {
        if (idExterneRubrique.equals(null) || idExterneRubrique.equals("")) {
            CARubrique rubrique = new CARubrique();
            rubrique.setSession(getSession());
            rubrique.setIdRubrique(getIdRubrique());
            try {
                rubrique.retrieve();
            } catch (Exception e) {
                _addError(null, "Erreur lors de la récupération de l'id externe de la rubrique: " + e.getMessage());
            }

            return rubrique.getIdExterne();
        }
        return idExterneRubrique;
    }

    /**
     * @return
     */
    public java.lang.String getIdOrdreAttribuer() {
        return idOrdreAttribuer;
    }

    /**
     * @return
     */
    public java.lang.String getIdOrdreRegroupement() {
        return idOrdreRegroupement;
    }

    /**
     * @return
     */
    public java.lang.String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @return
     */
    public java.lang.String getIdTraduction() {
        return idTraduction;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 17:12:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelle() {
        if (libelleFr.equals("") || libelleFr.equals(null)) {
            FAOrdreRegroupement ordre = new FAOrdreRegroupement();
            ordre.setSession(getSession());
            ordre.setIdOrdreRegroupement(getIdOrdreRegroupement());
            try {
                ordre.retrieve();
            } catch (Exception e) {
                _addError(null, "Erreur lors de la récupération du numéro de caisse de l'ordre: " + e.getMessage());
            }
            String langue = new String();
            langue = getSession().getIdLangueISO();
            if (langue.equalsIgnoreCase("fr")) {
                return ordre.getLibelleFR();
            }
            if (langue.equalsIgnoreCase("de")) {
                return ordre.getLibelleDE();
            } else {
                return ordre.getLibelleIT();
            }
        }
        String langue = new String();
        langue = getSession().getIdLangueISO();
        if (langue.equalsIgnoreCase("fr")) {
            return libelleFr;
        }
        if (langue.equalsIgnoreCase("de")) {
            return libelleDe;
        } else {
            return libelleIt;
        }
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 17:12:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleDe() {
        return libelleDe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 17:12:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleFr() {
        return libelleFr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (18.06.2003 17:12:03)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleIt() {
        return libelleIt;
    }

    /**
     * @return
     */
    public java.lang.String getLibelleRubrique() {
        String idTraduction = "";
        if (libelleRubrique.equals(null) || libelleRubrique.equals("")) {
            if (getIdTraduction().equals(null) || getIdTraduction().equals("")) {
                CARubrique rubrique = new CARubrique();
                rubrique.setSession(getSession());
                rubrique.setIdRubrique(getIdRubrique());
                try {
                    rubrique.retrieve();
                } catch (Exception e) {
                    _addError(null, "Erreur lors de la récupération de la rubrique: " + e.getMessage());
                }
                idTraduction = rubrique.getIdTraduction();
            } else {
                idTraduction = getIdTraduction();
            }
            PATraduction libelle = new PATraduction();
            libelle.setSession(getSession());
            libelle.setIdTraduction(idTraduction);
            libelle.setCodeISOLangue(getSession().getIdLangueISO());
            try {
                libelle.retrieve();
            } catch (Exception e) {
                _addError(null, "Erreur lors de la récupération du libellé de la rubrique: " + e.getMessage());
            }
            return libelle.getLibelle();
        }
        return libelleRubrique;
    }

    public String getNature() {
        return nature;
    }

    public java.lang.String getNatureFromRegroupement() throws Exception {

        FAOrdreRegroupement ordre = new FAOrdreRegroupement();
        ordre.setSession(getSession());
        ordre.setIdOrdreRegroupement(getIdOrdreRegroupement());
        try {
            ordre.retrieve();
        } catch (Exception e) {
            _addError(null, "Erreur lors de la récupération du numéro de caisse de l'ordre: " + e.getMessage());
        }
        String nature = ordre.getNature();

        return CodeSystem.getLibelle(getSession(), nature);
    }

    /**
     * @return
     */
    public java.lang.String getNumCaisse() {
        if (numCaisse.equals("") || numCaisse.equals(null)) {
            FAOrdreRegroupement ordre = new FAOrdreRegroupement();
            ordre.setSession(getSession());
            ordre.setIdOrdreRegroupement(getIdOrdreRegroupement());
            try {
                ordre.retrieve();
            } catch (Exception e) {
                _addError(null, "Erreur lors de la récupération du numéro de caisse de l'ordre: " + e.getMessage());
            }
            return ordre.getNumCaisse();
        }
        return numCaisse;

    }

    /**
     * @return
     */
    public java.lang.String getNumOrdreRegroupement() {
        if (numOrdreRegroupement.equals("") || numOrdreRegroupement.equals(null)) {
            FAOrdreRegroupement ordre = new FAOrdreRegroupement();
            ordre.setSession(getSession());
            ordre.setIdOrdreRegroupement(getIdOrdreRegroupement());
            try {
                ordre.retrieve();
            } catch (Exception e) {
                _addError(null, "Erreur lors de la récupération du numéro de caisse de l'ordre: " + e.getMessage());
            }
            return ordre.getOrdreRegroupement();
        }
        return numOrdreRegroupement;
    }

    /**
     * @param string
     */
    public void setIdExterneRubrique(java.lang.String string) {
        idExterneRubrique = string;
    }

    /**
     * @param string
     */
    public void setIdOrdreAttribuer(java.lang.String string) {
        idOrdreAttribuer = string;
    }

    /**
     * @param string
     */
    public void setIdOrdreRegroupement(java.lang.String string) {
        idOrdreRegroupement = string;
    }

    /**
     * @param string
     */
    public void setIdRubrique(java.lang.String string) {
        idRubrique = string;
    }

    /**
     * @param string
     */
    public void setIdTraduction(java.lang.String string) {
        idTraduction = string;
    }

    /**
     * @param string
     */
    public void setLibelleDe(java.lang.String string) {
        libelleDe = string;
    }

    /**
     * @param string
     */
    public void setLibelleFr(java.lang.String string) {
        libelleFr = string;
    }

    /**
     * @param string
     */
    public void setLibelleIt(java.lang.String string) {
        libelleIt = string;
    }

    /**
     * @param string
     */
    public void setLibelleRubrique(java.lang.String string) {
        libelleRubrique = string;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    /**
     * @param string
     */
    public void setNumCaisse(java.lang.String string) {
        numCaisse = string;
    }

    /**
     * @param string
     */
    public void setNumOrdreRegroupement(java.lang.String string) {
        numOrdreRegroupement = string;
    }

}
