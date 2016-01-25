package globaz.helios.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BTransaction;
import globaz.helios.db.interfaces.CGLibelle;
import globaz.helios.db.interfaces.CGLibelleInterface;
import globaz.helios.db.interfaces.ITreeListable;
import globaz.helios.db.modeles.CGEnteteModeleEcriture;
import globaz.helios.db.modeles.CGLigneModeleEcriture;
import globaz.helios.db.modeles.CGLigneModeleEcritureManager;
import globaz.jade.client.util.JadeStringUtil;

public class CGCentreCharge extends BEntity implements ITreeListable, java.io.Serializable, CGLibelleInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String idCentreCharge = new String();
    private java.lang.String idMandat = new String();
    private java.lang.String libelleDe = new String();
    private java.lang.String libelleFr = new String();
    private java.lang.String libelleIt = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CGCentreCharge
     */
    public CGCentreCharge() {
        super();
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeDelete(BTransaction)
     */
    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        CGEcritureListViewBean ecritureManager = new CGEcritureListViewBean();
        ecritureManager.setSession(getSession());
        ecritureManager.setForIdCentreCharge(getIdCentreCharge());
        ecritureManager.find(transaction);
        if (ecritureManager.size() != 0) {
            _addError(transaction, getSession().getLabel("CENTRE_CH_ERROR_1"));
            return;
        }

        CGLigneModeleEcritureManager managerM = new CGLigneModeleEcritureManager();
        managerM.setSession(getSession());
        managerM.setForIdCentreCharge(getIdCentreCharge());
        managerM.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < managerM.size(); i++) {
            CGLigneModeleEcriture line = (CGLigneModeleEcriture) managerM.getEntity(i);
            CGEnteteModeleEcriture entete = new CGEnteteModeleEcriture();
            entete.setSession(getSession());
            entete.setIdEnteteModeleEcriture(line.getIdEnteteModeleEcriture());
            entete.retrieve(transaction);
            if (entete.getIdMandat().equals(getIdMandat())) {
                _addError(transaction, getSession().getLabel("CENTRE_CH_ERROR_2"));
                return;
            }
        }

        // CGLigneModeleEcritureManager modeleEcritureManager = new
        // CGLigneModeleEcritureManager();
        // modeleEcritureManager.setSession(getSession());
        // modeleEcritureManager.setForIdCentreCharge(getIdCentreCharge());
        // modeleEcritureManager.find(transaction);
        // if (modeleEcritureManager.size() != 0) {
        // _addError(transaction, getSession().getLabel("CENTRE_CH_ERROR_2"));
        // return;
        // }
        //

        CGSoldeManager soldeManager = new CGSoldeManager();
        soldeManager.setSession(getSession());
        soldeManager.setForIdCentreCharge(getIdCentreCharge());
        soldeManager.find(transaction, BManager.SIZE_NOLIMIT);
        for (int i = 0; i < soldeManager.size(); i++) {
            CGSolde solde = (CGSolde) soldeManager.getEntity(i);
            solde.delete(transaction);
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CGCCHAP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idCentreCharge = statement.dbReadNumeric("IDCENTRECHARGE");
        idMandat = statement.dbReadNumeric("IDMANDAT");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {

        // libelle dans la langue de l'application obligatoire
        if (CGLibelle.isAppLanguageLibelleEmpty(this)) {
            _addError(statement.getTransaction(),
                    getSession().getLabel("CENTRE_CH_LIBELLE_EMPTY") + " (" + CGLibelle.getLangueUser(this) + ")");
        }

        /* idCentreCharge - obligatoire */
        if (JadeStringUtil.isIntegerEmpty(getIdCentreCharge())) {
            _addError(statement.getTransaction(), getSession().getLabel("CENTRE_CH_NUMERO_EMPTY"));
        }

        /* idMandat - obligatoire */
        if (JadeStringUtil.isBlank(getIdMandat())) {
            _addError(statement.getTransaction(), getSession().getLabel("CENTRE_CH_MANDAT_EMPTY"));
        }

        CGMandat mandat = new CGMandat();
        mandat.setSession(getSession());
        mandat.setIdMandat(getIdMandat());
        mandat.retrieve(statement.getTransaction());
        if (mandat == null || mandat.isNew()) {
            _addError(statement.getTransaction(), getSession().getLabel("CENTRE_CH_MANDAT_EMPTY"));
        }

    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDCENTRECHARGE", _dbWriteNumeric(statement.getTransaction(), getIdCentreCharge(), ""));
        statement.writeKey("IDMANDAT", _dbWriteNumeric(statement.getTransaction(), getIdMandat(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDCENTRECHARGE",
                _dbWriteNumeric(statement.getTransaction(), getIdCentreCharge(), "idCentreCharge"));
        statement.writeField("IDMANDAT", _dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField("LIBELLEFR", _dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField("LIBELLEDE", _dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField("LIBELLEIT", _dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleit"));

    }

    /**
     * @see globaz.helios.db.interfaces.ITreeListable#getChilds()
     */
    @Override
    public BManager[] getChilds() throws Exception {
        return null;
    }

    /**
     * Getter
     */
    public java.lang.String getIdCentreCharge() {
        return idCentreCharge;
    }

    public java.lang.String getIdMandat() {
        return idMandat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 09:13:47)
     * 
     * @return java.lang.String
     */
    @Override
    public String getLibelle() {
        return CGLibelle.getLibelleUser(this);
    }

    @Override
    public java.lang.String getLibelleDe() {
        return libelleDe;
    }

    @Override
    public java.lang.String getLibelleFr() {
        return libelleFr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 09:14:49)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Setter
     */
    public void setIdCentreCharge(java.lang.String newIdCentreCharge) {
        idCentreCharge = newIdCentreCharge;
    }

    public void setIdMandat(java.lang.String newIdMandat) {
        idMandat = newIdMandat;
    }

    public void setLibelleDe(java.lang.String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    public void setLibelleFr(java.lang.String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.01.2003 09:14:49)
     * 
     * @param newLibelleIt
     *            java.lang.String
     */
    public void setLibelleIt(java.lang.String newLibelleIt) {
        libelleIt = newLibelleIt;
    }

}
