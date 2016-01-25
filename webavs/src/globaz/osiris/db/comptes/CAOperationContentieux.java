package globaz.osiris.db.comptes;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEvenementContentieux;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIOperationContentieux;
import globaz.osiris.api.APIParametreEtape;
import globaz.osiris.db.contentieux.CAEvenementContentieux;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (26.06.2002 13:30:44)
 * 
 * @author: Administrator
 */
public class CAOperationContentieux extends CAOperation implements APIOperationContentieux {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CAEvenementContentieux evct = new CAEvenementContentieux();
    private boolean operSaveEvenContentieux = false;

    /**
     * Commentaire relatif au constructeur CAOperationContentieux.
     */
    public CAOperationContentieux() {
        super();
        setIdTypeOperation(APIOperation.CAOPERATIONCONTENTIEUX);
    }

    public CAOperationContentieux(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CAOPERATIONCONTENTIEUX);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.01.2002 17:06:20)
     */
    @Override
    protected void _afterAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        evct.setIdOperation(getIdOperation());
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (11.02.2002 14:04:58)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Laisser la superclasse effectuer son traitement
        super._afterRetrieve(transaction);

        initEvenementContentieux(transaction);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.01.2002 17:06:20)
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        // Laisser la superclasse s'initialiser
        super._beforeAdd(transaction);

        // Forcer le type d'op�ration
        setIdTypeOperation(APIOperation.CAOPERATIONCONTENTIEUX);

        // Charger les zones par d�faut � partir de l'�v�nement
        if (evct != null) {
            if (JadeStringUtil.isIntegerEmpty(getIdCompteAnnexe()) && (evct.getSection() != null)) {
                setIdCompteAnnexe(evct.getSection().getIdCompteAnnexe());
            }
            if (JadeStringUtil.isIntegerEmpty(getIdSection())) {
                setIdSection(evct.getIdSection());
            }
            if (JAUtil.isDateEmpty(getDate())) {
                setDate(evct.getDateExecution());
            }
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.01.2002 17:06:20)
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws Exception {

        // Laisser la superclasse effectuer son traitement
        super._beforeDelete(transaction);

        // Lecture de l'�v�nement contentieux
        evct.setAlternateKey(CAEvenementContentieux.AK_IDOPERATION);
        evct.retrieve(transaction);
        if (!evct.isNew()) {
            // Supprimer l'�v�nement contentieux si non modifi�
            if (!evct.getEstModifie().booleanValue() && !isOperSaveEvenContentieux()) {
                evct.delete(transaction);
                if (evct.hasErrors()) {
                    _addError(transaction, getSession().getLabel("7111"));
                }
            }
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.01.2002 17:06:20)
     */
    @Override
    protected void _beforeDesactiver(BTransaction transaction) {

        // Laisser la superclasse effectuer son traitement
        super._beforeDesactiver(transaction);

        // Sous contr�le d'exceptions
        try {

            // Annuler le d�clenchement
            evct.annulerDeclenchement();
            evct.update(transaction);
            if (evct.isNew() || evct.hasErrors()) {
                _addError(null, getSession().getLabel("7112"));
            }
        }

        // Traiter les exceptions
        catch (Exception e) {
            _addError(null, e.getMessage());
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.01.2002 17:06:20)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {

        // Laisser la superclasse effectuer son traitement
        super._validate(statement);

        // V�rifier le type d'op�ration
        if (!isInstanceOrSubClassOf(APIOperation.CAOPERATIONCONTENTIEUX)) {
            _addError(statement.getTransaction(), getSession().getLabel("5182"));
        }

    }

    /**
     * Validation des donn�es Date de cr�ation : (30.01.2002 07:52:07)
     */
    @Override
    protected void _valider(globaz.globall.db.BTransaction transaction) {

        // Valider les donn�es de la superclasse
        super._valider(transaction);

        // V�rifier que l'�v�nement contentieux est correct
        if (evct == null) {
            getMemoryLog().logMessage("5183", null, FWMessage.ERREUR, this.getClass().getName());
        } else if (!evct.getIdOperation().equals(getIdOperation())) {
            getMemoryLog().logMessage("5184", null, FWMessage.ERREUR, this.getClass().getName());
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 10:44:09)
     * 
     * @param oper
     *            globaz.osiris.db.comptes.CAOperationOrdreVersement
     */
    public void dupliquer(CAOperationContentieux oper) {

        // Refuser la duplication
        _addError(null, getSession().getLabel("5186"));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 13:32:59)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDateDeclenchement() {
        return evct.getDateDeclenchement();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 13:32:59)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDateExecution() {
        return evct.getDateExecution();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 15:16:26)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDescription(String codeISOLangue) {
        if (evct.getParametreEtape() != null) {
            return evct.getParametreEtape().getEtape().getDescription(codeISOLangue);
        } else {
            return super.getDescription();
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 13:32:59)
     * 
     * @return java.lang.String
     */
    @Override
    public Boolean getEstDeclenche() {
        return evct.getEstDeclenche();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 13:32:59)
     * 
     * @return java.lang.String
     */
    @Override
    public Boolean getEstExtourne() {
        return evct.getEstExtourne();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 13:32:59)
     * 
     * @return java.lang.String
     */
    @Override
    public Boolean getEstIgnoree() {
        return evct.getEstIgnoree();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 13:32:59)
     * 
     * @return java.lang.String
     */
    @Override
    public Boolean getEstModifie() {
        return evct.getEstModifie();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 15:30:30)
     * 
     * @return globaz.osiris.db.contentieux.CAEvenementContentieux
     */
    public CAEvenementContentieux getEvenementContentieux() {
        return evct;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 13:32:59)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdAdresse() {
        return evct.getIdAdresse();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.07.2002 08:49:40)
     * 
     * @return java.lang.String
     */
    public String getIdEvenementContentieux() {
        return evct.getIdEvenementContentieux();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 13:43:24)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdParametreEtape() {
        return evct.getIdParametreEtape();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 13:32:59)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdTiers() {
        return evct.getIdTiers();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 13:32:59)
     * 
     * @return java.lang.String
     */
    @Override
    public String getIdTiersOfficePoursuites() {
        return evct.getIdTiersOfficePoursuites();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 13:32:59)
     * 
     * @return java.lang.String
     */
    @Override
    public String getMontant() {
        return evct.getMontant();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.09.2002 11:49:29)
     * 
     * @return java.lang.String
     */
    public String getMotif() {
        return evct.getMotif();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.07.2002 09:06:01)
     * 
     * @return globaz.osiris.db.contentieux.CAParametreEtape
     */
    public APIParametreEtape getParametreEtape() {
        return evct.getParametreEtape();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 13:32:59)
     * 
     * @return java.lang.String
     */
    @Override
    public String getTaxes() {
        return evct.getTaxes();
    }

    /**
     * R�cup�rer l'ordre de versement associ�
     * 
     * @param transaction
     */
    public void initEvenementContentieux(BTransaction transaction) {
        evct = new CAEvenementContentieux();
        evct.setSession(getSession());
        evct.setIdOperation(getIdOperation());
        evct.setAlternateKey(CAEvenementContentieux.AK_IDOPERATION);
        try {
            evct.retrieve(transaction);
            if (transaction.hasErrors()) {
                _addError(transaction, getSession().getLabel("7110"));
            }
            evct.setAlternateKey(0);
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (09.09.2002 08:56:01)
     * 
     * @return boolean
     */
    public boolean isOperSaveEvenContentieux() {
        return operSaveEvenContentieux;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (26.06.2002 16:17:07)
     * 
     * @param newEvct
     *            globaz.osiris.db.contentieux.CAEvenementContentieux
     */
    @Override
    public void setEvenementContentieux(APIEvenementContentieux newEvct) {
        // if (isUpdatable())
        evct = (CAEvenementContentieux) newEvct;
        // else
        // _addError(null, getSession().getLabel("5187"));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.09.2002 11:50:43)
     */
    public void setMotif(String newMotif) {
        evct.setMotif(newMotif);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (09.09.2002 08:56:01)
     * 
     * @param newOperSaveEvenContentieux
     *            boolean
     */
    public void setOperSaveEvenContentieux(boolean newOperSaveEvenContentieux) {
        operSaveEvenContentieux = newOperSaveEvenContentieux;
    }
}
