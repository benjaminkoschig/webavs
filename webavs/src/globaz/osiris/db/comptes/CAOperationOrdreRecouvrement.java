package globaz.osiris.db.comptes;

import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIOperationOrdreRecouvrement;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.ordres.CAOrdreRecouvrement;
import globaz.osiris.db.ordres.format.CAOrdreFormateur;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdressePaiement;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.api.osiris.TITiersOSI;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (07.02.2002 11:13:25)
 * 
 * @author: Administrator
 */
public class CAOperationOrdreRecouvrement extends CAOperation implements APIOperationOrdreRecouvrement {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CAOrdreRecouvrement _ordreRecouvrement = new CAOrdreRecouvrement();
    private globaz.osiris.db.utils.CAAdressePaiementFormatter adressePaiementFormatter = null;

    /**
     * Commentaire relatif au constructeur CAOrdreVersement.
     */
    public CAOperationOrdreRecouvrement() {
        super();
        setIdTypeOperation(APIOperation.CAOPERATIONORDRERECOUVREMENT);
    }

    public CAOperationOrdreRecouvrement(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CAOPERATIONORDRERECOUVREMENT);
    }

    /**
     * Effectue des traitements apr�s un ajout dans la BD et apr�s avoir vid� le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements apr�s l'ajout de l'entit� dans la BD
     * <p>
     * La transaction n'est pas valid�e si le buffer d'erreurs n'est pas vide apr�s l'ex�cution de
     * <code>_afterAdd()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Laisser la superclasse traiter l'�v�nement
        super._afterAdd(transaction);

        // Mettre � jour l'ordre associ�
        _ordreRecouvrement.setIdOrdre(getIdOperation());
        _ordreRecouvrement.add(transaction);
        if (_ordreRecouvrement.hasErrors()) {
            _addError(transaction, getSession().getLabel("7113"));
        }
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

        initOrderRecouvrement(transaction);
    }

    /**
     * Effectue des traitements apr�s une mise � jour dans la BD et apr�s avoir vid� le tampon de lecture <i>
     * <p>
     * A surcharger pour effectuer les traitements apr�s la mise � jour de l'entit� dans la BD
     * <p>
     * La transaction n'est pas valid�e si le buffer d'erreurs n'est pas vide apr�s l'ex�cution de
     * <code>_afterUpdate()</code>
     * <p>
     * Ne pas oublier de partager la connexion avec les autres DAB !!! </i>
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur fatale
     */
    @Override
    protected void _afterUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // mise � jour du fichier FWParametersUserValue - AJPPVUT
        this._synchroValUtili(transaction);

        if (getSection().getAttenteLSVDD().booleanValue() && getEstBloque().booleanValue()) {
            getSection().setAttenteLSVDD(new Boolean(false));
            getSection().update(transaction);
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.01.2002 17:06:20)
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        // Laisser la superclasse s'initialiser
        super._beforeAdd(transaction);

        // Forcer le type d'op�ration
        setIdTypeOperation(APIOperation.CAOPERATIONORDRERECOUVREMENT);

        // Monnaie de d�p�t par d�faut
        setCodeISOMonnaieDepot("CHF");

        // Monnaie de bonification par d�faut
        setCodeISOMonnaieBonification("CHF");

        // Type de virement par d�faut
        setTypeVirement(APIOperationOrdreRecouvrement.VIREMENT);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 15:17:35)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _beforeDelete(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {

        // Laisser la superclasse traiter l'�v�nement
        super._beforeDelete(transaction);

        // Supprimer l'ordre de recouvrement
        if (!isNew() && !hasErrors()) {
            _ordreRecouvrement.delete(transaction);
            if (_ordreRecouvrement.hasErrors()) {
                _addError(transaction, getSession().getLabel("7115"));
            }
        }
    }

    @Override
    protected void _beforeUpdate(globaz.globall.db.BTransaction transaction) throws Exception {

        // Laisser la superclasse traiter l'�v�nement
        super._beforeUpdate(transaction);

        // Mettre � jour l'ordre associ�
        _ordreRecouvrement.update(transaction);
        if (_ordreRecouvrement.isNew() || _ordreRecouvrement.hasErrors()) {
            _addError(transaction, getSession().getLabel("7116"));
        }
    }

    /**
     * Chargement des valeurs par d�faut par utilisateur
     */
    protected void _synchroChgValUtili(globaz.globall.db.BTransaction transaction) {
        if (isNew()) {
            if (!JadeStringUtil.isBlank(getNomEcran()) && (valeurUtilisateur == null)) {
                valeurUtilisateur = new java.util.Vector(11);
                // lecture du fichier
                globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
                valUtili.setSession(getSession());
                valeurUtilisateur = valUtili.retrieveValeur("CAOperationOrdreRecouvrement", getNomEcran());
                // chargement des propri�t�s internes si idExterneRoleEcran est
                // vide

                if (JadeStringUtil.isBlank(getIdExterneRoleEcran()) && !valeurUtilisateur.isEmpty()) {
                    if (valeurUtilisateur.size() >= 1) {
                        setIdExterneRoleEcran(valeurUtilisateur.elementAt(0));
                    }
                    if (valeurUtilisateur.size() >= 2) {
                        setIdRoleEcran(valeurUtilisateur.elementAt(1));
                    }
                    if (valeurUtilisateur.size() >= 3) {
                        setIdExterneSectionEcran(valeurUtilisateur.elementAt(2));
                    }
                    if (valeurUtilisateur.size() >= 4) {
                        setIdTypeSectionEcran(valeurUtilisateur.elementAt(3));
                    }
                    if (valeurUtilisateur.size() >= 5) {
                        setTypeVirement(valeurUtilisateur.elementAt(4));
                    }
                    if (valeurUtilisateur.size() >= 6) {
                        setDate(valeurUtilisateur.elementAt(5));
                    }
                    if (valeurUtilisateur.size() >= 7) {
                        setCodeISOMonnaieBonification(valeurUtilisateur.elementAt(6));
                    }
                    if (valeurUtilisateur.size() >= 8) {
                        setNatureOrdre(valeurUtilisateur.elementAt(7));
                    }
                    if (valeurUtilisateur.size() >= 9) {
                        setIdOrganeExecution(valeurUtilisateur.elementAt(8));
                    }
                    if (valeurUtilisateur.size() >= 10) {
                        setCodeISOMonnaieDepot(valeurUtilisateur.elementAt(9));
                    }
                    if (valeurUtilisateur.size() >= 11) {
                        setMotif(valeurUtilisateur.elementAt(10));
                    }
                }
            }
        }
    }

    /**
     * mise � jour du fichier AJPPVUT pour les valeur par d�faut par utilisateur
     */
    protected void _synchroValUtili(globaz.globall.db.BTransaction transaction) {
        // mise � jour du fichier FWParametersUserValue - AJPPVUT
        if (valeurUtilisateur == null) {
            valeurUtilisateur = new java.util.Vector(11);
        }
        if (!JadeStringUtil.isBlank(getNomEcran())) {
            // chargement des donn�es � m�moriser dans le vecteur
            valeurUtilisateur.removeAllElements();
            valeurUtilisateur.add(0, getIdExterneRoleEcran());
            valeurUtilisateur.add(1, getIdRoleEcran());
            valeurUtilisateur.add(2, getIdExterneSectionEcran());
            valeurUtilisateur.add(3, getIdTypeSectionEcran());
            valeurUtilisateur.add(4, getTypeVirement());
            valeurUtilisateur.add(5, getDate());
            valeurUtilisateur.add(6, getCodeISOMonnaieBonification());
            valeurUtilisateur.add(7, getNatureOrdre());
            valeurUtilisateur.add(8, getIdOrganeExecution());
            valeurUtilisateur.add(9, getCodeISOMonnaieDepot());
            valeurUtilisateur.add(10, getMotif());
            // mise � jour dans le fichier
            globaz.globall.parameters.FWParametersUserValue valUtili = new globaz.globall.parameters.FWParametersUserValue();
            valUtili.setSession(getSession());
            valUtili.addValeur("CAOperationOrdreRecouvrement", getNomEcran(), valeurUtilisateur);
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (21.02.2002 08:50:10)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {

        // Laisser la superclasse effectuer son traitement
        super._validate(statement);

        // V�rifier le type d'op�ration
        if (!isInstanceOrSubClassOf(APIOperation.CAOPERATIONORDRERECOUVREMENT)) {
            _addError(statement.getTransaction(), getSession().getLabel("5251"));
        }

    }

    /**
     * Validation des donn�es Date de cr�ation : (30.01.2002 07:52:07)
     */
    @Override
    protected void _valider(globaz.globall.db.BTransaction transaction) {

        // Valider les donn�es de la superclasse
        super._valider(transaction);

        // Contr�ler le montant
        if ((getMontant() == null) || JadeStringUtil.isIntegerEmpty(getMontant())) {
            getMemoryLog().logMessage("5129", null, FWMessage.ERREUR, this.getClass().getName());
        } else {
            float _mont = 0f;
            try {
                _mont = Float.parseFloat(getMontant());
            } catch (Exception e) {
            }

            if (_mont <= 0f) {
                getMemoryLog().logMessage("5136", null, FWMessage.ERREUR, this.getClass().getName());
            }
        }

        // V�rifier la section
        if (!isNewSection()) {
            if (JadeStringUtil.isIntegerEmpty(getIdSection())) {
                getMemoryLog().logMessage("5125", null, FWMessage.ERREUR, this.getClass().getName());
            } else if (getSection() == null) {
                getMemoryLog().logMessage("5126", getIdSection(), FWMessage.ERREUR, this.getClass().getName());
            }
        }

        setDefaultIdAdressePaiement();

        if (getCompteAnnexe() != null) {

            // Valider l'ordre de recouvrement
            _ordreRecouvrement.setMemoryLog(getMemoryLog());
            _ordreRecouvrement.setSession(getSession());
            _ordreRecouvrement._valider();
        }

        // v�rification du pays de l'adresse
        if ((getAdressePaiement() != null) && !getAdressePaiement().getCodeISOPays().equalsIgnoreCase("CH")) {
            getMemoryLog().logMessage("Test Code ISO pays", FWMessage.ERREUR, this.getClass().getName());
        }
        // TODO sch A voir s'il faut conserver ce test, le contr�le devrait �tre
        // effectu� lors de la pr�paration de l'ordre group� et non � la saisie
        // de l'ordre de recouvrement
        // // Contr�ler si l'organe d'execution correspond au compte
        // try {
        // CAOrganeExecution org = new CAOrganeExecution();
        // org.setIdOrganeExecution(getIdOrganeExecution());
        // org.setSession(getSession());
        // org.retrieve(transaction);
        //
        // if (!org.isNew()) {
        // // Contr�ler s'il s'agit d'un CCP ou d'une banque
        // if (org.getGenre().equals(CAOrganeExecution.BANQUE) &&
        // !getAdressePaiement().getTypeAdresse().equals(IntAdressePaiement.BANQUE))
        // {
        // getMemoryLog().logMessage("5253", null, FWMessage.ERREUR,
        // getClass().getName());
        // } else if (org.getGenre().equals(CAOrganeExecution.POSTE) &&
        // !getAdressePaiement().getTypeAdresse().equals(IntAdressePaiement.CCP))
        // {
        // getMemoryLog().logMessage("5252", null, FWMessage.ERREUR,
        // getClass().getName());
        // }
        // } else {
        // getMemoryLog().logMessage("5159", null, FWMessage.ERREUR,
        // getClass().getName());
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

    }

    /**
     * Valider l'ordre avant la g�n�ration du paiement Date de cr�ation : (08.02.2002 15:44:08)
     */
    public void _validerAvantRecouvrement(globaz.globall.db.BTransaction transaction) {
        if (!getEtat().equals(APIOperation.ETAT_COMPTABILISE) && !getEtat().equals(APIOperation.ETAT_ERREUR_VERSEMENT)
                && !getEtat().equals(APIOperation.ETAT_VERSE)) {
            getMemoryLog().logMessage("5149", null, FWMessage.ERREUR, this.getClass().getName());
        } else {
            _valider(transaction);

            // V�rifier le num�ro de transaction
            if (JadeStringUtil.isIntegerEmpty(getNumTransaction())) {
                getMemoryLog().logMessage("5150", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // V�rifier l'ordre group�
            if (JadeStringUtil.isIntegerEmpty(getIdOrdreGroupe())) {
                getMemoryLog().logMessage("5151", null, FWMessage.ERREUR, this.getClass().getName());
            }

            // V�rification de la validit� de l'adresse de paiement
            try {
                CAAdressePaiementFormatter.checkAdressePaiementData(getAdressePaiementData(), getSession());
            } catch (Exception e) {
                getMemoryLog().logMessage(e.getMessage(), FWMessage.ERREUR, this.getClass().getName());
            }
        }

        // S'il y a des erreurs
        if (getMemoryLog().getErrorLevel().compareTo(FWMessage.ERREUR) >= 0) {
            setEtat(APIOperation.ETAT_ERREUR_VERSEMENT);
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (05.03.2002 09:29:02)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws java.lang.Exception {

        // Laisser la superclasse effectuer son traitement
        super._writeProperties(statement);

        // Traitement interne
        statement.writeField("MONTANT", this._dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));

    }

    /**
     * Annuler le versement de l'ordre Date de cr�ation : (29.04.2002 09:04:08)
     */
    public void annulerVerser() {

        // V�rifier l'�tat
        if (!getEtat().equals(APIOperation.ETAT_VERSE) && !getEtat().equals(APIOperation.ETAT_ERREUR_VERSEMENT)) {
            _addError(null, getSession().getLabel("5407"));
        } else {
            setEtat(APIOperation.ETAT_COMPTABILISE);
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 10:44:09)
     * 
     * @param oper
     *            globaz.osiris.db.comptes.CAOperationOrdreRecouvrement
     */
    public void dupliquer(CAOperationOrdreRecouvrement oper) {

        // Dupliquer les param�tres de la superclasse
        super.dupliquer(oper);

        // Copier les autres param�tres
        if (oper != null) {
            setCodeISOMonnaieBonification(oper.getCodeISOMonnaieBonification());
            setCodeISOMonnaieDepot(oper.getCodeISOMonnaieDepot());
            setCoursConversion(oper.getCoursConversion());
            setEstBloque(oper.getEstBloque());
            setEstRetire(oper.getEstRetire());
            setIdAdressePaiement(oper.getIdAdressePaiement());
            setIdOrdreGroupe(oper.getIdOrdreGroupe());
            setIdOrganeExecution(oper.getIdOrganeExecution());
            setMontant(oper.getMontant());
            setMotif(oper.getMotif());
            setNatureOrdre(oper.getNatureOrdre());
            setReferenceBVR(oper.getReferenceBVR());
            setTypeVirement(oper.getTypeVirement());
            setValeurConversion(oper.getValeurConversion());
        }
    }

    public String getAdresseCourrier() {
        try {
            TIAdresseDataSource d = getCompteAnnexe().getTiers().getDataSourceAdresseCourrier();
            String adresse = d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_RUE) + " "
                    + d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NUMERO) + " "
                    + d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_NPA) + " "
                    + d.getData().get(TIAbstractAdresseDataSource.ADRESSE_VAR_LOCALITE);
            return adresse;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 11:10:16)
     * 
     * @return globaz.osiris.interfaceext.tiers.IntAdressePaiement
     */
    @Override
    public IntAdressePaiement getAdressePaiement() {
        return _ordreRecouvrement.getAdressePaiement();
    }

    public TIAdressePaiementData getAdressePaiementData() throws Exception {
        return _ordreRecouvrement.getAdressePaiementData();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.03.2002 14:17:07)
     * 
     * @return globaz.osiris.db.utils.CAAdressePaiementFormatter
     */
    public globaz.osiris.db.utils.CAAdressePaiementFormatter getAdressePaiementFormatter() {
        if (adressePaiementFormatter == null) {

            adressePaiementFormatter = new CAAdressePaiementFormatter();
            adressePaiementFormatter.setAdressePaiement(getAdressePaiement());
        }
        return adressePaiementFormatter;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:25:37)
     * 
     * @return String
     */
    @Override
    public String getCodeISOMonnaieBonification() {
        return _ordreRecouvrement.getCodeISOMonnaieBonification();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:25:06)
     * 
     * @return String
     */
    @Override
    public String getCodeISOMonnaieDepot() {
        return _ordreRecouvrement.getCodeISOMonnaieDepot();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:27:43)
     * 
     * @return String
     */
    public String getCoursConversion() {
        return _ordreRecouvrement.getCoursConversion();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 17:08:25)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsMonnaies() {
        _ordreRecouvrement.setSession(getSession());
        return _ordreRecouvrement.getCsMonnaies();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 17:08:25)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsNatureOrdres() {
        _ordreRecouvrement.setSession(getSession());
        return _ordreRecouvrement.getCsNatureOrdres();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (10.01.2002 17:08:25)
     * 
     * @return globaz.globall.parameters.FWParametersSystemCodeManager
     */
    public FWParametersSystemCodeManager getCsTypeVirements() {
        _ordreRecouvrement.setSession(getSession());
        return _ordreRecouvrement.getCsTypeVirements();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:30:04)
     * 
     * @return java.lang.Boolean
     */
    @Override
    public java.lang.Boolean getEstBloque() {
        return _ordreRecouvrement.getEstBloque();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:33:12)
     * 
     * @return java.lang.Boolean
     */
    @Override
    public java.lang.Boolean getEstRetire() {
        return _ordreRecouvrement.getEstRetire();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (18.04.2002 13:30:32)
     * 
     * @return boolean
     */
    public boolean getEstVerse() {
        return getEtat().equals(APIOperation.ETAT_VERSE);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:22:43)
     * 
     * @return String
     */
    @Override
    public String getIdAdressePaiement() {
        return _ordreRecouvrement.getIdAdressePaiement();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 13:35:47)
     * 
     * @return String
     */
    @Override
    public String getIdOrdreGroupe() {
        return _ordreRecouvrement.getIdOrdreGroupe();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 09:16:15)
     * 
     * @return String
     */
    @Override
    public String getIdOrganeExecution() {
        return _ordreRecouvrement.getIdOrganeExecution();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:19:02)
     * 
     * @return String
     */
    @Override
    public String getMontant() {
        return JANumberFormatter.deQuote(montant);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31.05.2002 08:41:11)
     * 
     * @return globaz.framework.util.FWCurrency
     */
    public FWCurrency getMontantToCurrency() {
        return new FWCurrency(getMontant());
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:28:44)
     * 
     * @return String
     */
    @Override
    public String getMotif() {
        return removeBlankLines(_ordreRecouvrement.getMotif());
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:32:42)
     * 
     * @return String
     */
    @Override
    public String getNatureOrdre() {
        return _ordreRecouvrement.getNatureOrdre();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:34:10)
     * 
     * @return String
     */
    public String getNomCache() {
        return _ordreRecouvrement.getNomCache();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:31:49)
     * 
     * @return String
     */
    @Override
    public String getNumTransaction() {
        return _ordreRecouvrement.getNumTransaction();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 13:37:19)
     * 
     * @return globaz.osiris.db.ordres.CAOrdreGroupe
     */
    @Override
    public CAOrdreGroupe getOrdreGroupe() {
        _ordreRecouvrement.setSession(getSession());
        return _ordreRecouvrement.getOrdreGroupe();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:29:17)
     * 
     * @return String
     */
    @Override
    public String getReferenceBVR() {
        return _ordreRecouvrement.getReferenceBVR();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:30:42)
     * 
     * @return String
     */
    @Override
    public String getTypeVirement() {
        return _ordreRecouvrement.getTypeVirement();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:26:59)
     * 
     * @return String
     */
    public String getValeurConversion() {
        return _ordreRecouvrement.getValeurConversion();
    }

    /**
     * R�cup�rer l'ordre de Recouvrement associ�
     * 
     * @param transaction
     */
    public void initOrderRecouvrement(BTransaction transaction) {
        _ordreRecouvrement = new CAOrdreRecouvrement();
        _ordreRecouvrement.setSession(getSession());
        _ordreRecouvrement.setIdOrdre(getIdOperation());
        try {
            _ordreRecouvrement.retrieve(transaction);
            if (transaction.hasErrors()) {
                _addError(transaction, getSession().getLabel("7114"));
            }
        } catch (Exception e) {
            _addError(transaction, e.getMessage());
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:25:37)
     * 
     * @param newCodeISOMonnaieBonification
     *            String
     */
    @Override
    public void setCodeISOMonnaieBonification(String newCodeISOMonnaieBonification) {
        // if (isUpdatable())
        _ordreRecouvrement.setCodeISOMonnaieBonification(newCodeISOMonnaieBonification);
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:25:06)
     * 
     * @param newCodeISOMonnaieDepot
     *            String
     */
    @Override
    public void setCodeISOMonnaieDepot(String newCodeISOMonnaieDepot) {
        // if (isUpdatable())
        _ordreRecouvrement.setCodeISOMonnaieDepot(newCodeISOMonnaieDepot);
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:27:43)
     * 
     * @param newCoursConverstion
     *            String
     */
    public void setCoursConversion(String newCoursConversion) {
        // if (isUpdatable())
        _ordreRecouvrement.setCoursConversion(newCoursConversion);
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Adresse de paiement par d�faut
     */
    public void setDefaultIdAdressePaiement() {
        if (JadeStringUtil.isIntegerEmpty(getIdAdressePaiement())) {
            if (getCompteAnnexe() != null) {
                if (getCompteAnnexe().getTiers() != null) {
                    setIdAdressePaiement(getCompteAnnexe().getTiers().getIdAdressePaiement(
                            TITiersOSI.DOMAINE_RECOUVREMENT, getCompteAnnexe().getIdExterneRole(),
                            JACalendar.today().toString()));
                }
            }
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:30:04)
     * 
     * @param newEstBloque
     *            java.lang.Boolean
     */
    @Override
    public void setEstBloque(java.lang.Boolean newEstBloque) {
        _ordreRecouvrement.setEstBloque(newEstBloque);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:33:12)
     * 
     * @param newEstRetire
     *            java.lang.Boolean
     */
    @Override
    public void setEstRetire(java.lang.Boolean newEstRetire) {
        _ordreRecouvrement.setEstRetire(newEstRetire);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:22:43)
     * 
     * @param newIdAdressePaiement
     *            String
     */
    @Override
    public void setIdAdressePaiement(String newIdAdressePaiement) {
        _ordreRecouvrement.setIdAdressePaiement(newIdAdressePaiement);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 13:36:40)
     * 
     * @param newIdOrdreGroupe
     *            String
     */
    public void setIdOrdreGroupe(String newIdOrdreGroupe) {
        _ordreRecouvrement.setIdOrdreGroupe(newIdOrdreGroupe);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 09:15:23)
     * 
     * @param newIdOrganeExecution
     *            String
     */
    @Override
    public void setIdOrganeExecution(String newIdOrganeExecution) {
        // if (isUpdatable())
        _ordreRecouvrement.setIdOrganeExecution(newIdOrganeExecution);
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (30.01.2002 07:36:40)
     * 
     * @param newMontant
     *            String
     */
    @Override
    public void setMontant(String newMontant) {
        // if (isUpdatable())
        montant = newMontant;
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:28:44)
     * 
     * @param newMotif
     *            String
     */
    @Override
    public void setMotif(String newMotif) {
        if (newMotif != null) {
            newMotif = removeBlankLines(newMotif);
        }

        _ordreRecouvrement.setMotif(newMotif);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:32:42)
     * 
     * @param newNatureOrdre
     *            String
     */
    @Override
    public void setNatureOrdre(String newNatureOrdre) {
        _ordreRecouvrement.setNatureOrdre(newNatureOrdre);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:34:10)
     * 
     * @param newNomCache
     *            String
     */
    public void setNomCache(String newNomCache) {
        _ordreRecouvrement.setNomCache(newNomCache);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:31:49)
     * 
     * @param newNumTransaction
     *            String
     */
    public void setNumTransaction(String newNumTransaction) {
        _ordreRecouvrement.setNumTransaction(newNumTransaction);
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:29:17)
     * 
     * @param newReference
     *            String
     */
    @Override
    public void setReferenceBVR(String newReferenceBVR) {
        // if (isUpdatable())
        _ordreRecouvrement.setReferenceBVR(newReferenceBVR);
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:30:42)
     * 
     * @param newTypeOrdre
     *            String
     */
    @Override
    public void setTypeVirement(String newTypeVirement) {
        // if (isUpdatable())
        _ordreRecouvrement.setTypeVirement(newTypeVirement);
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (07.02.2002 11:26:59)
     * 
     * @param newValeurConversion
     *            String
     */
    public void setValeurConversion(String newValeurConversion) {
        // if (isUpdatable())
        _ordreRecouvrement.setValeurConversion(newValeurConversion);
        // else
        // _addError(null, getSession().getLabel("5133"));
    }

    /**
     * G�n�rer le recouvrement de l'ordre Date de cr�ation : (13.02.2002 09:18:26)
     * 
     * @param of
     *            globaz.osiris.db.ordres.IntOrdreFormateur format � appliquer pour l'ordre de recouvrement
     * @param journal
     *            CAJournal le journal sur lequel le recouvrement doit �tre effectu�
     * @return StringBuffer un string buffer correspondant � l'ordre formatt� pour le recouvrement
     */
    public StringBuffer verser(CAOrdreFormateur of, CAJournal journal, BTransaction transaction) throws Exception {

        // Initialiser
        StringBuffer sb = null;

        // Si l'on d�sire formatter l'ordre
        if (of != null) {
            // G�n�rer le recouvrement
            sb = of.format(this);
        }
        if (this.getOrdreGroupe().getOrganeExecution().getGenre().equals(APIOrganeExecution.BANQUE)) {
            // Si l'on d�sire g�n�rer le recouvrement
            if (journal != null) {
                // Si l'ordre n'est pas d�j� vers�
                if (!getEtat().equals(APIOperation.ETAT_VERSE)) {

                    // Instancier une �criture de recouvrement
                    CARecouvrement rec = new CARecouvrement();
                    rec.setSession(getSession());

                    // Charger les attributs
                    rec.dupliquer(this);
                    rec.setDate(this.getOrdreGroupe().getDateEcheance());
                    rec.setIdJournal(journal.getIdJournal());
                    rec.setIdCompte(this.getOrdreGroupe().getOrganeExecution().getIdRubrique());
                    rec.setMontant(getMontant());
                    rec.setCodeDebitCredit(APIEcriture.CREDIT);

                    // Ins�rer l'op�ration et pi�ger les erreurs
                    try {
                        rec.add(transaction);
                        if (rec.hasErrors()) {
                            _addError(transaction, getSession().getLabel("5156"));
                        } else {
                            setEtat(APIOperation.ETAT_VERSE);
                        }
                    } catch (Exception e) {
                        _addError(transaction, e.getMessage());
                    }
                }
            }
        }

        // Retourer le string buffer
        return sb;
    }
}
