package globaz.osiris.db.comptes;

import globaz.globall.db.BTransaction;
import globaz.norma.db.fondation.IntTranslatable;
import globaz.norma.db.fondation.PATraductionHelper;
import globaz.osiris.api.APITypeSection;

/**
 * Insérez la description du type ici. Date de création : (12.12.2001 07:31:11)
 * 
 * @author: Administrator
 */
public class CATypeSection extends globaz.globall.db.BEntity implements java.io.Serializable, IntTranslatable,
        APITypeSection {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idSequenceContentieux = new String();
    private java.lang.String idTraduction = new String();
    private java.lang.String idTypeSection = new String();
    private java.lang.String nomClasse = new String();
    private java.lang.String nomPageDetail = new String();
    private globaz.osiris.api.APISectionDescriptor sectionDescriptor = null;
    private PATraductionHelper trLibelles = null;

    // code systeme

    /**
     * Commentaire relatif au constructeur CATypeSection
     */
    public CATypeSection() {
        super();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 15:03:05)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Mise à jour des libellés
        getTraductionHelper().add(transaction);
    }

    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Suppression de tous les libellés
        getTraductionHelper().delete(transaction);
    }

    @Override
    protected void _beforeRetrieve(BTransaction transaction) throws Exception {
        trLibelles = null;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 15:03:05)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Mise à jour des libellés
        getTraductionHelper().update(transaction);
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CATSECP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idTypeSection = statement.dbReadNumeric("IDTYPESECTION");
        idSequenceContentieux = statement.dbReadNumeric("IDSEQCON");
        idTraduction = statement.dbReadNumeric("IDTRADUCTION");
        nomClasse = statement.dbReadString("NOMCLASSE");
        nomPageDetail = statement.dbReadString("NOMPAGEDETAIL");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
        _propertyMandatory(statement.getTransaction(), getIdTypeSection(), getSession().getLabel("7170"));
        _propertyMandatory(statement.getTransaction(), getIdTraduction(), getSession().getLabel("7171"));
        _propertyMandatory(statement.getTransaction(), getNomClasse(), getSession().getLabel("7172"));
        _propertyMandatory(statement.getTransaction(), getNomPageDetail(), getSession().getLabel("7173"));
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDTYPESECTION", this._dbWriteNumeric(statement.getTransaction(), getIdTypeSection(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDTYPESECTION",
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeSection(), "idTypeSection"));
        statement.writeField("IDSEQCON",
                this._dbWriteNumeric(statement.getTransaction(), getIdSequenceContentieux(), "idSeqCon"));
        statement.writeField("IDTRADUCTION",
                this._dbWriteNumeric(statement.getTransaction(), getIdTraduction(), "idTraduction"));
        statement.writeField("NOMCLASSE", this._dbWriteString(statement.getTransaction(), getNomClasse(), "nomClasse"));
        statement.writeField("NOMPAGEDETAIL",
                this._dbWriteString(statement.getTransaction(), getNomPageDetail(), "nomPageDetail"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDescription() {
        // Description dans la langue de l'utilisateur
        return this.getDescription(null);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDescription(String codeIsoLangue) {

        String s = "";
        try {
            s = PATraductionHelper.translate(getSession(), getIdTraduction(), codeIsoLangue);
        } catch (Exception e) {
            _addError(null, e.toString());
        }
        return s;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (19.12.2001 11:02:39)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdentificationSource() {
        return _getTableName();
    }

    @Override
    public java.lang.String getIdSequenceContentieux() {
        return idSequenceContentieux;
    }

    @Override
    public java.lang.String getIdTraduction() {
        return idTraduction;
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdTypeSection() {
        return idTypeSection;
    }

    @Override
    public java.lang.String getNomClasse() {
        return nomClasse;
    }

    @Override
    public java.lang.String getNomPageDetail() {
        return nomPageDetail;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (16.12.2002 18:26:06)
     * 
     * @return globaz.osiris.api.APISectionDescriptor
     */
    @Override
    public globaz.osiris.api.APISectionDescriptor getSectionDescriptor() {
        // Tenter un chargement de la classe
        if (sectionDescriptor == null) {
            try {
                Class cl = Class.forName(getNomClasse());
                sectionDescriptor = (globaz.osiris.api.APISectionDescriptor) cl.newInstance();
                sectionDescriptor.setISession(getSession());
            } catch (Exception e) {
                _addError(null, getSession().getLabel("7001") + " " + e.getMessage());
                e.printStackTrace();
                sectionDescriptor = null;
            }
        }
        return sectionDescriptor;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2002 14:57:23)
     * 
     * @return globaz.norma.db.fondation.PATraductionHelper
     */
    private PATraductionHelper getTraductionHelper() {
        if (trLibelles == null) {
            try {
                trLibelles = new PATraductionHelper(this);
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }

        return trLibelles;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @param newDescription
     *            java.lang.String
     */
    @Override
    public void setDescription(java.lang.String newDescription) throws Exception {
        this.setDescription(newDescription, null);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2002 09:33:33)
     * 
     * @param newDescription
     *            java.lang.String
     */
    @Override
    public void setDescription(String newDescription, String codeISOLangue) {
        getTraductionHelper().setDescription(newDescription, codeISOLangue);
        if (getTraductionHelper().getError() != null) {
            _addError(null, getTraductionHelper().getError().getMessage());
        }
    }

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    @Override
    public void setDescriptionDe(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "DE");
    }

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    @Override
    public void setDescriptionFr(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "FR");
    }

    /*
     * Description dans la langue fournie Date de création : (19.12.2001 10:56:02) @param newDescription
     * java.lang.String @param codeISOLangue java.lang.String
     */
    @Override
    public void setDescriptionIt(String newDescription) throws Exception {
        // Mise à jour du libellé
        this.setDescription(newDescription, "IT");
    }

    @Override
    public void setIdSequenceContentieux(java.lang.String newIdSequenceContentieux) {
        idSequenceContentieux = newIdSequenceContentieux;
    }

    @Override
    public void setIdTraduction(java.lang.String newIdTraduction) {
        idTraduction = newIdTraduction;
    }

    /**
     * Setter
     */
    @Override
    public void setIdTypeSection(java.lang.String newIdTypeSection) {
        idTypeSection = newIdTypeSection;
    }

    @Override
    public void setNomClasse(java.lang.String newNomClasse) {
        nomClasse = newNomClasse;
    }

    @Override
    public void setNomPageDetail(java.lang.String newNomPageDetail) {
        nomPageDetail = newNomPageDetail;
    }
}
