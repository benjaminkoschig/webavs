/**
 *
 */
package globaz.osiris.db.mapping;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableManager;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import java.util.HashMap;

/**
 * @author sel
 * 
 */
public class CAJournalDebit extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_COMPTECOURANTDEST = "COMPTECOURANTDEST";
    public static final String FIELD_COMPTECOURANTSRC = "COMPTECOURANTSRC";
    public static final String FIELD_CONTREPARTIEDEST = "CONTREPARTIEDEST";
    public static final String FIELD_CONTREPARTIESRC = "CONTREPARTIESRC";
    public static final String FIELD_IDLINK = "IDLINK";
    public static final String FIELD_IDMANDAT = "IDMANDAT";

    public static final String TABLE_NAME = "CALINKP";

    private String compteCourantDest = "";
    private String compteCourantSrc = "";
    private String contrePartieDest = "";
    private String contrePartieSrc = "";
    /**
     * Cache local des exercices
     */
    private HashMap<String, CGExerciceComptable> exercicesMap = null;
    private String idLink = "";

    private String idMandat = "";

    /**
	 *
	 */
    public CAJournalDebit() {
        super();
        exercicesMap = new HashMap<String, CGExerciceComptable>();
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdLink(this._incCounter(transaction, idLink));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CAJournalDebit.TABLE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idLink = statement.dbReadNumeric(CAJournalDebit.FIELD_IDLINK);
        idMandat = statement.dbReadNumeric(CAJournalDebit.FIELD_IDMANDAT);
        compteCourantDest = statement.dbReadString(CAJournalDebit.FIELD_COMPTECOURANTDEST);
        compteCourantSrc = statement.dbReadString(CAJournalDebit.FIELD_COMPTECOURANTSRC);
        contrePartieDest = statement.dbReadString(CAJournalDebit.FIELD_CONTREPARTIEDEST);
        contrePartieSrc = statement.dbReadString(CAJournalDebit.FIELD_CONTREPARTIESRC);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        _propertyMandatory(statement.getTransaction(), getIdMandat(),
                getSession().getLabel("JOURNALDEBIT_ERREUR_IDMANDAT_OBLIGATOIRE"));
        _propertyMandatory(statement.getTransaction(), getCompteCourantDest(),
                getSession().getLabel("JOURNALDEBIT_ERREUR_COMPTECOURANTDEST_OBLIGATOIRE"));
        _propertyMandatory(statement.getTransaction(), getCompteCourantSrc(),
                getSession().getLabel("JOURNALDEBIT_ERREUR_COMPTECOURANTSRC_OBLIGATOIRE"));
        _propertyMandatory(statement.getTransaction(), getContrePartieDest(),
                getSession().getLabel("JOURNALDEBIT_ERREUR_CONTREPARTIEDEST_OBLIGATOIRE"));
        _propertyMandatory(statement.getTransaction(), getContrePartieSrc(),
                getSession().getLabel("JOURNALDEBIT_ERREUR_CONTREPARTIEDEST_OBLIGATOIRE"));

        CGMandat mandat = retrieveMandat();
        if (mandat.isEstComptabiliteAVS()) {
            _addError(statement.getTransaction(), getSession().getLabel("JOURNALDEBIT_ERREUR_COMPTABILITE_AVS") + " ["
                    + mandat.getIdMandat() + "]");
        }

        if (!getIdMandat().equals(getCompteContrePartieDest().getIdMandat())) {
            _addError(statement.getTransaction(),
                    getSession().getLabel("JOURNALDEBIT_ERREUR_CONTREPARTIEDEST_NOT_IN_PLAN") + " ["
                            + getCompteContrePartieDest() + "-" + getIdMandat() + "]");
        }
        if (!getIdMandat().equals(getCompteCompteCourantDest().getIdMandat())) {
            _addError(statement.getTransaction(),
                    getSession().getLabel("JOURNALDEBIT_ERREUR_COMPTECOURANTDEST_NOT_IN_PLAN") + " ["
                            + getCompteCompteCourantDest() + "-" + getIdMandat() + "]");
        }

        if (getContrePartieDest().equals(getCompteCourantDest())) {
            _addError(statement.getTransaction(),
                    getSession().getLabel("JOURNALDEBIT_ERREUR_COMPTECOURANT_CONTREPARTIE_IDENTIQUE") + " ["
                            + getContrePartieDest() + "]");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CAJournalDebit.FIELD_IDLINK,
                this._dbWriteNumeric(statement.getTransaction(), getIdLink(), ""));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CAJournalDebit.FIELD_IDLINK,
                this._dbWriteNumeric(statement.getTransaction(), getIdLink(), "idLink"));
        statement.writeField(CAJournalDebit.FIELD_IDMANDAT,
                this._dbWriteNumeric(statement.getTransaction(), getIdMandat(), "idMandat"));
        statement.writeField(CAJournalDebit.FIELD_COMPTECOURANTDEST,
                this._dbWriteString(statement.getTransaction(), getCompteCourantDest(), "compteCourantDest"));
        statement.writeField(CAJournalDebit.FIELD_COMPTECOURANTSRC,
                this._dbWriteString(statement.getTransaction(), getCompteCourantSrc(), "compteCourantSrc"));
        statement.writeField(CAJournalDebit.FIELD_CONTREPARTIEDEST,
                this._dbWriteString(statement.getTransaction(), getContrePartieDest(), "contrePartieDest"));
        statement.writeField(CAJournalDebit.FIELD_CONTREPARTIESRC,
                this._dbWriteString(statement.getTransaction(), getContrePartieSrc(), "compteCourantSrc"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Retourne l'exercice comptable du jour correspondant au mandat.
     * 
     * @param idMandat
     * @return
     * @throws Exception
     */
    protected CGExerciceComptable findExerciceComptable(String idMandat) throws Exception {
        if (!exercicesMap.containsKey(idMandat)) {
            CGExerciceComptableManager mgr = new CGExerciceComptableManager();
            mgr.setSession(getSession());
            mgr.setForIdMandat(idMandat);
            // Bug 6310
            mgr.setBetweenDateDebutDateFin(JACalendar.todayJJsMMsAAAA());
            mgr.find();

            if (mgr.size() == 0) {
                throw new Exception(getSession().getLabel("JOURNALDEBIT_ERREUR_EXERCICE_NOT_FIND") + " " + idMandat);
            }
            exercicesMap.put(idMandat, (CGExerciceComptable) mgr.getFirstEntity());
        }

        return exercicesMap.get(idMandat);
    }

    /**
     * @return
     * @throws Exception
     */
    public CGPlanComptableViewBean getCompteCompteCourantDest() throws Exception {
        CGPlanComptableViewBean compte = new CGPlanComptableViewBean();
        compte.setSession(getSession());
        compte.setIdExterne(getCompteCourantDest());
        compte.setIdExerciceComptable(findExerciceComptable(getIdMandat()).getIdExerciceComptable());
        compte.setAlternateKey(CGPlanComptableViewBean.AK_EXTERNE);
        compte.retrieve();
        if (compte.isNew()) {
            throw new Exception(getSession().getLabel("JOURNALDEBIT_ERREUR_COMPTECOURANT_INEXISTANT_DANS_MANDAT")
                    + " [" + getCompteCourantDest() + "-" + getIdMandat() + "]");
        }

        return compte;
    }

    /**
     * @return
     * @throws Exception
     */
    public CGPlanComptableViewBean getCompteContrePartieDest() throws Exception {
        CGPlanComptableViewBean compte = new CGPlanComptableViewBean();
        compte.setSession(getSession());
        compte.setIdExterne(getContrePartieDest());
        compte.setIdExerciceComptable(findExerciceComptable(getIdMandat()).getIdExerciceComptable());
        compte.setAlternateKey(CGPlanComptableViewBean.AK_EXTERNE);
        compte.retrieve();

        if (compte.isNew()) {
            throw new Exception(getSession().getLabel("JOURNALDEBIT_ERREUR_COMPTE_INEXISTANT_DANS_MANDAT") + " ["
                    + getContrePartieDest() + "-" + getIdMandat() + "] !");
        }

        return compte;
    }

    /**
     * @return the compteCourantDest
     */
    public String getCompteCourantDest() {
        return compteCourantDest;
    }

    /**
     * @return the compteCourantSrc
     */
    public String getCompteCourantSrc() {
        return compteCourantSrc;
    }

    /**
     * @return the contrePartieDest
     */
    public String getContrePartieDest() {
        return contrePartieDest;
    }

    /**
     * @return the contrePartieSrc
     */
    public String getContrePartieSrc() {
        return contrePartieSrc;
    }

    /**
     * @return the idLink
     */
    public String getIdLink() {
        return idLink;
    }

    /**
     * @return the idMandat
     */
    public String getIdMandat() {
        return idMandat;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#hasCreationSpy()
     */
    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    /**
     * @return
     * @throws Exception
     */
    private CGMandat retrieveMandat() throws Exception {
        CGMandat mandat = new CGMandat();
        mandat.setSession(getSession());
        mandat.setIdMandat(getIdMandat());
        mandat.retrieve();

        if (mandat.isNew()) {
            throw new Exception(getSession().getLabel("JOURNALDEBIT_ERREUR_MANDAT_NOT_FIND") + " " + getIdMandat());
        }
        return mandat;
    }

    /**
     * @param compteCourantDest
     *            the compteCourantDest to set
     */
    public void setCompteCourantDest(String compteCourantDest) {
        this.compteCourantDest = compteCourantDest;
    }

    /**
     * @param compteCourantSrc
     *            the compteCourantSrc to set
     */
    public void setCompteCourantSrc(String compteCourantSrc) {
        this.compteCourantSrc = compteCourantSrc;
    }

    /**
     * @param contrePartieDest
     *            the contrePartieDest to set
     */
    public void setContrePartieDest(String contrePartieDest) {
        this.contrePartieDest = contrePartieDest;
    }

    /**
     * @param contrePartieSrc
     *            the contrePartieSrc to set
     */
    public void setContrePartieSrc(String contrePartieSrc) {
        this.contrePartieSrc = contrePartieSrc;
    }

    /**
     * @param idLink
     *            the idLink to set
     */
    public void setIdLink(String idLink) {
        this.idLink = idLink;
    }

    /**
     * @param idMandat
     *            the idMandat to set
     */
    public void setIdMandat(String idMandat) {
        this.idMandat = idMandat;
    }
}
