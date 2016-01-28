package globaz.osiris.db.retours;

import globaz.framework.translation.FWTranslation;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersCodeManager;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersUserValue;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author acr
 */

public class CARetours extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // un pseudo etat qui regroupe CS_ETAT_RETOUR_OUVERT et
    // CS_ETAT_RETOUR_SUSPENS
    public static final String CLE_ETAT_RETOUR_NON_LIQUIDE = "CLE_NON_LIQUIDE";

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public final static String CS_ETAT_RETOUR_ANNULE = "251004";
    public final static String CS_ETAT_RETOUR_LIQUIDE = "251003";
    // Codes Système
    // EtatRetour
    public final static String CS_ETAT_RETOUR_OUVERT = "251001";
    public final static String CS_ETAT_RETOUR_SUSPENS = "251002";
    public final static String CS_ETAT_RETOUR_TRAITE = "251005";
    public static final String FIELDNAME_DATE_RETOUR = "DATERETOUR";
    public static final String FIELDNAME_ETAT_RETOUR = "ETATRETOUR";
    public static final String FIELDNAME_ID_COMPTE_ANNEXE = "IDCOMPTEANNEXE";
    public static final String FIELDNAME_ID_JOURNAL = "IDJOURNAL";
    public static final String FIELDNAME_ID_LOT = "IDLOT";
    public static final String FIELDNAME_ID_RETOUR = "IDRETOUR";
    public static final String FIELDNAME_ID_SECTION = "IDSECTION";
    public static final String FIELDNAME_LIBELLE_RETOUR = "LIBELLERETOUR";

    public static final String FIELDNAME_MONTANT_RETOUR = "MONTANTRETOUR";
    public static final String FIELDNAME_MOTIF_RETOUR = "MOTIFRETOUR";
    public static final String FIELDNAME_NATURE_ORDRE = "NATUREORDRE";
    public static final String NOM_CLASSE = "CARetours";
    public static final String TABLE_NAME_RETOURS = "CARETOP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtatRetour = "";
    private String csMotifRetour = "";
    private String csMotifRetourSplit = "";

    private String csNatureOrdre = "";
    private String dateRetour = "";
    private transient Vector etatsRetour = null;
    private String idCompteAnnexe = "";
    private String idExterneRoleEcran = "";
    private String idJournal = "";
    private String idLot = "";
    private String idRetour = "";
    private String idSection = "";
    private boolean isModeCreerRetourSplit = false;
    private String libelleRetour = "";
    private String libelleRetourSplit = "";
    private HashMap<String, String> mapValeurUtilisateur = new HashMap<String, String>();

    private String montantRetour = "";
    private String montantRetourSplit = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        if (!isModeCreerRetourSplit()) {
            saveLastSaisieUtilisateur();
        }

    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        if (!isModeCreerRetourSplit()) {
            saveLastSaisieUtilisateur();
        }
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdRetour(this._incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        // La suppression est inderdite si le lot comptabilisé
        if (CARetours.CS_ETAT_RETOUR_SUSPENS.equals(getCsEtatRetour())
                || CARetours.CS_ETAT_RETOUR_LIQUIDE.equals(getCsEtatRetour())
                || CARetours.CS_ETAT_RETOUR_TRAITE.equals(getCsEtatRetour())) {
            _addError(transaction, getSession().getLabel("SUPPRESSION_RETOUR_IMPOSSIBLE"));
        }
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {

        if (isModeCreerRetourSplit()) {

            CARetours theRetourSplit = clone();

            theRetourSplit.setSession(getSession());

            theRetourSplit.setCsMotifRetour(getCsMotifRetourSplit());
            theRetourSplit.setLibelleRetour(getLibelleRetourSplit());
            theRetourSplit.setMontantRetour(getMontantRetourSplit());

            theRetourSplit.add(transaction);

            FWCurrency theMontantRetourOriginal = new FWCurrency(getMontantRetour());
            FWCurrency theMontantRetourSplit = new FWCurrency(theRetourSplit.getMontantRetour());

            theMontantRetourOriginal.sub(theMontantRetourSplit);

            setMontantRetour(theMontantRetourOriginal.toStringFormat());

            if (getSoldeRetour(transaction).doubleValue() == 0) {
                setCsEtatRetour(CARetours.CS_ETAT_RETOUR_TRAITE);
            }

        }

    }

    @Override
    protected String _getTableName() {
        return CARetours.TABLE_NAME_RETOURS;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRetour = statement.dbReadNumeric(CARetours.FIELDNAME_ID_RETOUR);
        idLot = statement.dbReadNumeric(CARetours.FIELDNAME_ID_LOT);
        idCompteAnnexe = statement.dbReadNumeric(CARetours.FIELDNAME_ID_COMPTE_ANNEXE);
        idSection = statement.dbReadNumeric(CARetours.FIELDNAME_ID_SECTION);
        idJournal = statement.dbReadNumeric(CARetours.FIELDNAME_ID_JOURNAL);
        csEtatRetour = statement.dbReadNumeric(CARetours.FIELDNAME_ETAT_RETOUR);
        dateRetour = statement.dbReadDateAMJ(CARetours.FIELDNAME_DATE_RETOUR);
        csMotifRetour = statement.dbReadNumeric(CARetours.FIELDNAME_MOTIF_RETOUR);
        libelleRetour = statement.dbReadString(CARetours.FIELDNAME_LIBELLE_RETOUR);
        montantRetour = statement.dbReadNumeric(CARetours.FIELDNAME_MONTANT_RETOUR);
        csNatureOrdre = statement.dbReadNumeric(CARetours.FIELDNAME_NATURE_ORDRE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {

        if (JadeStringUtil.isBlankOrZero(getIdLot())) {
            _addError(statement.getTransaction(), getSession().getLabel("ID_LOT_RETOUR_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlankOrZero(getIdCompteAnnexe())) {
            _addError(statement.getTransaction(), getSession().getLabel("COMPTE_ANNEXE_RETOUR_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlankOrZero(getDateRetour())) {
            _addError(statement.getTransaction(), getSession().getLabel("DATE_RETOUR_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlankOrZero(getCsEtatRetour())) {
            _addError(statement.getTransaction(), getSession().getLabel("ETAT_RETOUR_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlankOrZero(getMontantRetour())) {
            _addError(statement.getTransaction(), getSession().getLabel("MONTANT_RETOUR_OBLIGATOIRE"));
        }

        // Si etat<>ouvert -> motif doit être renseigné. Bug 5780
        if (!CARetours.CS_ETAT_RETOUR_OUVERT.equals(getCsEtatRetour()) && JadeStringUtil.isBlank(getCsMotifRetour())) {
            _addError(statement.getTransaction(), getSession().getLabel("MOTIF_RETOUR_OBLIGATOIRE"));
        }

        if (new FWCurrency(getMontantRetour()).doubleValue() <= 0) {
            _addError(statement.getTransaction(), getSession().getLabel("RETOUR_ERREUR_MONTANT_INFERIEUR_EGAL_A_ZERO"));
        }

        if (isModeCreerRetourSplit()) {

            FWCurrency theMontantRetourSplit = new FWCurrency(getMontantRetourSplit());
            FWCurrency theMontantRetourOriginal = new FWCurrency(getMontantRetour());
            FWCurrency theSoldeRetourOriginal = getSoldeRetour(statement.getTransaction());

            if (theMontantRetourSplit.doubleValue() <= 0) {
                _addError(statement.getTransaction(),
                        getSession().getLabel("RETOUR_A_ECLATER_ERREUR_MONTANT_INFERIEUR_EGAL_A_ZERO"));
            }

            if (!CARetours.CS_ETAT_RETOUR_SUSPENS.equalsIgnoreCase(getCsEtatRetour())) {
                _addError(statement.getTransaction(),
                        getSession().getLabel("RETOUR_A_ECLATER_ERREUR_ETAT_RETOUR_ORIGINAL_PAS_SUSPENS"));
            }

            if (theMontantRetourSplit.doubleValue() > theSoldeRetourOriginal.doubleValue()) {
                _addError(statement.getTransaction(),
                        getSession().getLabel("RETOUR_A_ECLATER_ERREUR_MONTANT_SUPERIEUR_SOLDE_RETOUR_ORIGINAL"));
            }

            if (theMontantRetourSplit.doubleValue() >= theMontantRetourOriginal.doubleValue()) {
                _addError(statement.getTransaction(),
                        getSession().getLabel("RETOUR_A_ECLATER_ERREUR_MONTANT_SUPERIEUR_EGAL_MONTANT_RETOUR_ORIGINAL"));
            }

        }

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CARetours.FIELDNAME_ID_RETOUR,
                this._dbWriteNumeric(statement.getTransaction(), idRetour, "idRetour"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CARetours.FIELDNAME_ID_RETOUR,
                this._dbWriteNumeric(statement.getTransaction(), idRetour, "idRetour"));
        statement.writeField(CARetours.FIELDNAME_ID_LOT,
                this._dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
        statement.writeField(CARetours.FIELDNAME_ID_COMPTE_ANNEXE,
                this._dbWriteNumeric(statement.getTransaction(), idCompteAnnexe, "idCompteAnnexe"));
        statement.writeField(CARetours.FIELDNAME_ID_SECTION,
                this._dbWriteNumeric(statement.getTransaction(), idSection, "idSection"));
        statement.writeField(CARetours.FIELDNAME_ID_JOURNAL,
                this._dbWriteNumeric(statement.getTransaction(), idJournal, "idJournal"));
        statement.writeField(CARetours.FIELDNAME_ETAT_RETOUR,
                this._dbWriteNumeric(statement.getTransaction(), csEtatRetour, "csEtatRetour"));
        statement.writeField(CARetours.FIELDNAME_DATE_RETOUR,
                this._dbWriteDateAMJ(statement.getTransaction(), dateRetour, "dateRetour"));
        statement.writeField(CARetours.FIELDNAME_MOTIF_RETOUR,
                this._dbWriteNumeric(statement.getTransaction(), csMotifRetour, "csMotifRetour"));
        statement.writeField(CARetours.FIELDNAME_LIBELLE_RETOUR,
                this._dbWriteString(statement.getTransaction(), libelleRetour, "libelleRetour"));
        statement.writeField(CARetours.FIELDNAME_MONTANT_RETOUR,
                this._dbWriteNumeric(statement.getTransaction(), montantRetour, "montantRetour"));
        statement.writeField(CARetours.FIELDNAME_NATURE_ORDRE,
                this._dbWriteNumeric(statement.getTransaction(), csNatureOrdre, "csNatureOrdre"));
    }

    @Override
    public CARetours clone() {

        CARetours theClone = new CARetours();

        theClone.setIdLot(getIdLot());
        theClone.setIdCompteAnnexe(getIdCompteAnnexe());
        theClone.setIdSection(getIdSection());
        theClone.setIdJournal(getIdJournal());
        theClone.setCsEtatRetour(getCsEtatRetour());
        theClone.setDateRetour(getDateRetour());
        theClone.setCsMotifRetour(getCsMotifRetour());
        theClone.setLibelleRetour(getLibelleRetour());
        theClone.setMontantRetour(getMontantRetour());
        theClone.setCsNatureOrdre(getCsNatureOrdre());

        return theClone;
    }

    public String getCsEtatRetour() {
        return csEtatRetour;
    }

    public String getCsMotifRetour() {
        return csMotifRetour;
    }

    public String getCsMotifRetourSplit() {
        return csMotifRetourSplit;
    }

    public String getCsNatureOrdre() {
        return csNatureOrdre;
    }

    public String getDateRetour() {
        return dateRetour;
    }

    /**
     * Retourne la liste des codes systèmes pour l'état d'un retour augmenté de 'NON_LIQUIDE' et 'vide'.
     * 
     * @return un Vector de String[2]{codeSysteme, libelleCodeSysteme}.
     */
    public final Vector getEtatRetourData() {
        if (etatsRetour == null) {
            etatsRetour = getLibellesPourGroupe("OSIRETOURS");

            // ajout des options custom
            etatsRetour
                    .add(0,
                            new String[] { CARetours.CLE_ETAT_RETOUR_NON_LIQUIDE,
                                    getSession().getLabel("LIBELLE_NON_LIQUIDE") });

            etatsRetour.add(0, new String[] { "", "" });
        }

        return etatsRetour;
    }

    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    public String getIdExterneRoleEcran() {
        return idExterneRoleEcran;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getIdRetour() {
        return idRetour;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getLibelleRetour() {
        return libelleRetour;
    }

    public String getLibelleRetourSplit() {
        return libelleRetourSplit;
    }

    /**
     * renvoie tous les libelles et code utilisateur d'un groupe de codes systemes
     * 
     * @param groupeCodes
     *            le groupe dont on veut les libelles des codes systeme
     * 
     * @return un vecteur de String[3] {codeSysteme, libelle, codeUtilisateur} vecteur vide si erreur
     */
    private Vector getLibellesPourGroupe(String groupeCodes) {
        try {
            // on récupére la liste de tous les codes systèmes
            FWParametersCodeManager mgr = FWTranslation.getSystemCodeList(groupeCodes, getSession());
            Vector retValue = new Vector(mgr.size() + 2);

            // ajout des codes systemes standards
            for (int idCode = 0; idCode < mgr.size(); ++idCode) {
                FWParametersSystemCode code = (FWParametersSystemCode) mgr.getEntity(idCode);

                retValue.add(new String[] { code.getIdCode(), code.getCurrentCodeUtilisateur().getLibelle(),
                        code.getCurrentCodeUtilisateur().getCodeUtilisateur() });
            }

            return retValue;
        } catch (Exception e) {
            return new Vector();
        }
    }

    public HashMap<String, String> getMapValeurUtilisateur() {
        return mapValeurUtilisateur;
    }

    public String getMontantRetour() {
        return montantRetour;
    }

    public String getMontantRetourSplit() {
        return montantRetourSplit;
    }

    public FWCurrency getSoldeRetour(BTransaction transaction) throws Exception {

        CALignesRetoursManager lrManager = new CALignesRetoursManager();
        lrManager.setSession(getSession());
        lrManager.setForIdRetour(getIdRetour());

        BigDecimal sommeLignes = lrManager.getSum(CALignesRetours.FIELDNAME_MONTANT, transaction);

        FWCurrency solde = new FWCurrency(getMontantRetour());
        solde.sub(sommeLignes.toString());

        return solde;

    }

    public boolean isModeCreerRetourSplit() {
        return isModeCreerRetourSplit;
    }

    public void loadLastSaisieUtilisateur() {

        Vector<String> vectorLastSaisieUtilisateur = new Vector<String>(4);

        FWParametersUserValue valUtili = new FWParametersUserValue();
        valUtili.setSession(getSession());
        vectorLastSaisieUtilisateur = valUtili.retrieveValeur(CARetours.NOM_CLASSE, CARetoursViewBean.NOM_ECRAN);

        if (vectorLastSaisieUtilisateur.size() >= 1) {
            getMapValeurUtilisateur().put("idExterneRoleEcran", vectorLastSaisieUtilisateur.get(0));
        }
        if (vectorLastSaisieUtilisateur.size() >= 2) {
            getMapValeurUtilisateur().put("dateRetour", vectorLastSaisieUtilisateur.get(1));
        }
        if (vectorLastSaisieUtilisateur.size() >= 3) {
            getMapValeurUtilisateur().put("csMotifRetour", vectorLastSaisieUtilisateur.get(2));
        }
        if (vectorLastSaisieUtilisateur.size() >= 4) {
            getMapValeurUtilisateur().put("libelleRetour", vectorLastSaisieUtilisateur.get(3));
        }

    }

    private void saveLastSaisieUtilisateur() {

        Vector<String> vectorLastSaisieUtilisateur = new Vector<String>(4);

        vectorLastSaisieUtilisateur.add(0, getIdExterneRoleEcran());
        vectorLastSaisieUtilisateur.add(1, getDateRetour());
        vectorLastSaisieUtilisateur.add(2, getCsMotifRetour());
        vectorLastSaisieUtilisateur.add(3, getLibelleRetour());

        // mise à jour dans le fichier
        FWParametersUserValue valUtili = new FWParametersUserValue();
        valUtili.setSession(getSession());
        valUtili.addValeur(CARetours.NOM_CLASSE, CARetoursViewBean.NOM_ECRAN, vectorLastSaisieUtilisateur);
    }

    public void setCsEtatRetour(String csEtatRetour) {
        this.csEtatRetour = csEtatRetour;
    }

    public void setCsMotifRetour(String csMotifRetour) {
        this.csMotifRetour = csMotifRetour;
    }

    public void setCsMotifRetourSplit(String csMotifRetourSplit) {
        this.csMotifRetourSplit = csMotifRetourSplit;
    }

    public void setCsNatureOrdre(String csNatureOrdre) {
        this.csNatureOrdre = csNatureOrdre;
    }

    public void setDateRetour(String dateRetour) {
        this.dateRetour = dateRetour;
    }

    public void setIdCompteAnnexe(String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    public void setIdExterneRoleEcran(String idExterneRoleEcran) {
        this.idExterneRoleEcran = idExterneRoleEcran;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdRetour(String idRetour) {
        this.idRetour = idRetour;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setLibelleRetour(String libelleRetour) {
        this.libelleRetour = libelleRetour;
    }

    public void setLibelleRetourSplit(String libelleRetourSplit) {
        this.libelleRetourSplit = libelleRetourSplit;
    }

    public void setMapValeurUtilisateur(HashMap<String, String> mapValeurUtilisateur) {
        this.mapValeurUtilisateur = mapValeurUtilisateur;
    }

    public void setModeCreerRetourSplit(boolean isModeCreerRetourSplit) {
        this.isModeCreerRetourSplit = isModeCreerRetourSplit;
    }

    public void setMontantRetour(String montantRetour) {
        this.montantRetour = montantRetour;
    }

    public void setMontantRetourSplit(String montantRetourSplit) {
        this.montantRetourSplit = montantRetourSplit;
    }

}
