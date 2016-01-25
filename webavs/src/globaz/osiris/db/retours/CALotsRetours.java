package globaz.osiris.db.retours;

import globaz.framework.translation.FWTranslation;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersCodeManager;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Vector;

/**
 * <H1>Description</H1>
 * 
 * @author acr
 */

public class CALotsRetours extends BEntity {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // un pseudo etat qui regroupe CS_ETAT_LOT_OUVERT et
    // CS_ETAT_LOT_COMPTABILISE
    public static final String CLE_ETAT_LOT_NON_LIQUIDE = "CLE_NON_LIQUIDE";

    public final static String CS_ETAT_LOT_COMPTABILISE = "250002";
    public final static String CS_ETAT_LOT_LIQUIDE = "250003";
    // Codes Système //EtatLot
    public final static String CS_ETAT_LOT_OUVERT = "250001";
    public static final String FIELDNAME_DATE_LOT = "DATELOT";
    public static final String FIELDNAME_ETAT_LOT = "ETATLOT";
    public static final String FIELDNAME_ID_COMPTE_FINANCIER = "IDCMPTFINAN";
    public static final String FIELDNAME_ID_JOURNAL_CA = "IDJOURNALCA";
    public static final String FIELDNAME_ID_LOT = "IDLOT";

    public static final String FIELDNAME_LIBELLE_LOT = "LIBELLELOT";
    public static final String FIELDNAME_MONTANT_TOTAL = "MONTANTRETTOT";
    public static final String FIELDNAME_NATURE_ORDRE = "NATUREORDRE";
    public static final String TABLE_NAME_LOTS_RETOURS = "CALOREP";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String csEtatLot = "";
    private String csNatureOrdre = "";
    private String dateLot = "";
    private transient Vector etatsLot = null;
    private String idCompteFinancier = "";
    private String idJournalCA = "";
    private String idLot = "";
    private String libelleLot = "";

    private String montantTotal = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdLot(this._incCounter(transaction, "0"));
    }

    @Override
    protected void _beforeDelete(BTransaction transaction) throws Exception {
        // La suppression est inderdite si le lot comptabilisé
        if (CALotsRetours.CS_ETAT_LOT_COMPTABILISE.equals(getCsEtatLot())) {
            _addError(transaction, getSession().getLabel("SUPPRESSION_LOT_IMPOSSIBLE"));
        }

        // on supprime tous les retours du lot
        CARetoursManager rManager = new CARetoursManager();
        rManager.setSession(getSession());
        rManager.setForIdLot(getIdLot());
        rManager.delete(transaction);

    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
    }

    @Override
    protected String _getTableName() {
        return CALotsRetours.TABLE_NAME_LOTS_RETOURS;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idLot = statement.dbReadNumeric(CALotsRetours.FIELDNAME_ID_LOT);
        dateLot = statement.dbReadDateAMJ(CALotsRetours.FIELDNAME_DATE_LOT);
        libelleLot = statement.dbReadString(CALotsRetours.FIELDNAME_LIBELLE_LOT);
        csEtatLot = statement.dbReadNumeric(CALotsRetours.FIELDNAME_ETAT_LOT);
        idCompteFinancier = statement.dbReadNumeric(CALotsRetours.FIELDNAME_ID_COMPTE_FINANCIER);
        montantTotal = statement.dbReadNumeric(CALotsRetours.FIELDNAME_MONTANT_TOTAL);
        idJournalCA = statement.dbReadNumeric(CALotsRetours.FIELDNAME_ID_JOURNAL_CA);
        csNatureOrdre = statement.dbReadNumeric(CALotsRetours.FIELDNAME_NATURE_ORDRE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (JadeStringUtil.isBlank(getDateLot())) {
            _addError(statement.getTransaction(), getSession().getLabel("DATE_LOT_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlank(getLibelleLot())) {
            _addError(statement.getTransaction(), getSession().getLabel("LIBELLE_LOT_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlankOrZero(getCsEtatLot())) {
            _addError(statement.getTransaction(), getSession().getLabel("ETAT_LOT_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlankOrZero(getIdCompteFinancier())) {
            _addError(statement.getTransaction(), getSession().getLabel("COMPTE_FINANCIER_OBLIGATOIRE"));
        }
        if (JadeStringUtil.isBlankOrZero(getCsNatureOrdre())) {
            _addError(statement.getTransaction(), getSession().getLabel("NATURE_ORDRE_LOT_OBLIGATOIRE"));
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(CALotsRetours.FIELDNAME_ID_LOT,
                this._dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(CALotsRetours.FIELDNAME_ID_LOT,
                this._dbWriteNumeric(statement.getTransaction(), idLot, "idLot"));
        statement.writeField(CALotsRetours.FIELDNAME_DATE_LOT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateLot, "dateLot"));
        statement.writeField(CALotsRetours.FIELDNAME_LIBELLE_LOT,
                this._dbWriteString(statement.getTransaction(), libelleLot, "libelleLot"));
        statement.writeField(CALotsRetours.FIELDNAME_ETAT_LOT,
                this._dbWriteNumeric(statement.getTransaction(), csEtatLot, "csEtatLot"));
        statement.writeField(CALotsRetours.FIELDNAME_ID_COMPTE_FINANCIER,
                this._dbWriteNumeric(statement.getTransaction(), idCompteFinancier, "idCompteFinancier"));
        statement.writeField(CALotsRetours.FIELDNAME_MONTANT_TOTAL,
                this._dbWriteNumeric(statement.getTransaction(), montantTotal, "montantTotal"));
        statement.writeField(CALotsRetours.FIELDNAME_ID_JOURNAL_CA,
                this._dbWriteNumeric(statement.getTransaction(), idJournalCA, "idJournalCA"));
        statement.writeField(CALotsRetours.FIELDNAME_NATURE_ORDRE,
                this._dbWriteNumeric(statement.getTransaction(), csNatureOrdre, "csNatureOrdre"));
    }

    public String getCsEtatLot() {
        return csEtatLot;
    }

    public String getCsNatureOrdre() {
        return csNatureOrdre;
    }

    public String getDateLot() {
        return dateLot;
    }

    /**
     * Retourne la liste des codes systèmes pour l'état d'un lot augmenté de 'NON_LIQUIDE' et 'vide'.
     * 
     * @return un Vector de String[2]{codeSysteme, libelleCodeSysteme}.
     */
    public final Vector getEtatLotData() {
        if (etatsLot == null) {
            etatsLot = getLibellesPourGroupe("OSILOTRET");

            // ajout des options custom
            etatsLot.add(
                    0,
                    new String[] { CALotsRetours.CLE_ETAT_LOT_NON_LIQUIDE, getSession().getLabel("LIBELLE_NON_LIQUIDE") });

            etatsLot.add(0, new String[] { "", "" });
        }

        return etatsLot;
    }

    public String getIdCompteFinancier() {
        return idCompteFinancier;
    }

    public String getIdJournalCA() {
        return idJournalCA;
    }

    public String getIdLot() {
        return idLot;
    }

    public String getLibelleLot() {
        return libelleLot;
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

    public String getMontantTotal() {
        return montantTotal;
    }

    public void setCsEtatLot(String csEtatLot) {
        this.csEtatLot = csEtatLot;
    }

    public void setCsNatureOrdre(String csNatureOrdre) {
        this.csNatureOrdre = csNatureOrdre;
    }

    public void setDateLot(String dateLot) {
        this.dateLot = dateLot;
    }

    public void setIdCompteFinancier(String idCompteFinancier) {
        this.idCompteFinancier = idCompteFinancier;
    }

    public void setIdJournalCA(String idJournalCA) {
        this.idJournalCA = idJournalCA;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setLibelleLot(String libelleLot) {
        this.libelleLot = libelleLot;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

}
