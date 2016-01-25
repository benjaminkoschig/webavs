package globaz.apg.db.prestation;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import java.util.List;

/**
 * Descpription
 * 
 * @author scr Date de création 1 juin 05
 */
public class APCotisation extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDEBUT = "VLDDEB";
    public static final String FIELDNAME_DATEFIN = "VLDFIN";
    public static final String FIELDNAME_ID_COTISATION = "VLICOT";
    public static final String FIELDNAME_ID_EXTERNE = "VLIEXT";
    public static final String FIELDNAME_IDREPARTITIONBENEFICIAIREPAIEMENT = "VLIRBP";
    public static final String FIELDNAME_MONTANT = "VLMMON";
    public static final String FIELDNAME_MONTANTBRUT = "VLMMOB";
    public static final String FIELDNAME_TAUX = "VLMTAU";
    public static final String FIELDNAME_TYPE = "VLLTYP";
    public static final String TABLE_NAME = "APCOTIS";
    public static final String TYPE_ASSURANCE = "A";
    public static final String TYPE_IMPOT = "I";

    private String dateDebut = "";
    private String dateFin = "";
    private String idCotisation = "";
    private String idExterne = "";
    private String idRepartitionBeneficiairePaiement = "";
    private String montant = "";
    private String montantBrut = "";
    private String nomExterne = "";
    private String taux = "";
    private String type = "";

    public static String createFieldsClause(final String schema) {
        StringBuffer fields = new StringBuffer();
        fields.append(schema);
        fields.append(APCotisation.TABLE_NAME);
        fields.append(".");
        fields.append(BSpy.FIELDNAME);
        fields.append(",");
        fields.append(APCotisation.FIELDNAME_ID_COTISATION);
        fields.append(",");
        fields.append(APCotisation.FIELDNAME_MONTANT);
        fields.append(",");
        fields.append(APCotisation.FIELDNAME_ID_EXTERNE);
        fields.append(",");
        fields.append(APCotisation.FIELDNAME_DATEDEBUT);
        fields.append(",");
        fields.append(APCotisation.FIELDNAME_DATEFIN);
        fields.append(",");
        fields.append(APCotisation.FIELDNAME_MONTANTBRUT);
        fields.append(",");
        fields.append(APCotisation.FIELDNAME_IDREPARTITIONBENEFICIAIREPAIEMENT);
        fields.append(",");
        fields.append(APCotisation.FIELDNAME_TYPE);
        fields.append(",");
        fields.append(APCotisation.FIELDNAME_TAUX);
        return fields.toString();
    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {

        // effacement des montants ventiles de la repartition de paiement
        APRepartitionPaiementsManager repartitions = new APRepartitionPaiementsManager();

        repartitions.setForIdParent(idRepartitionBeneficiairePaiement);
        repartitions.setSession(getSession());
        repartitions.find();

        for (int idVentilation = 0; idVentilation < repartitions.size(); ++idVentilation) {
            APRepartitionPaiements repartition = (APRepartitionPaiements) repartitions.get(idVentilation);

            repartition.delete(transaction);
        }

        // calcul du montant total des cotisations
        APCotisationManager cotisations = new APCotisationManager();

        cotisations.setForIdRepartitionBeneficiairePaiement(idRepartitionBeneficiairePaiement);
        cotisations.setNotForIdCotisation(idCotisation);
        cotisations.setSession(getSession());

        FWCurrency total = new FWCurrency(cotisations.getSum(APCotisation.FIELDNAME_MONTANT).toString());

        // Mise à jour du montant net de la répartition de paiement
        APRepartitionPaiements repartitionPaiements = new APRepartitionPaiements();

        repartitionPaiements.setSession(getSession());
        repartitionPaiements.setIdRepartitionBeneficiairePaiement(idRepartitionBeneficiairePaiement);
        repartitionPaiements.retrieve(transaction);

        total.add(repartitionPaiements.getMontantBrut());
        repartitionPaiements.setMontantNet(total.toString());
        repartitionPaiements.wantCallValidate(false);
        repartitionPaiements.update(transaction);
    }

    /**
     * @see globaz.globall.db.BEntity#_afterRetrieve(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if (APCotisation.TYPE_ASSURANCE.equals(type)) {
            // il s'agit d'une assurance, on va charger son nom et son taux
            List tauxList = PRAffiliationHelper.getTauxAssurance(getSession(), idExterne, "", dateDebut, dateFin);

            if ((tauxList != null) && !tauxList.isEmpty()) {
                nomExterne = ((AFTauxAssurance) tauxList.get(0)).getAssurance().getAssuranceLibelleCourt();
                taux = ((AFTauxAssurance) tauxList.get(0)).getValeurEmployeur();
            }
        } else if (APCotisation.TYPE_IMPOT.equals(type)) {
            // il s'agit d'un impot a la source, on donne le nom par defaut et
            // on verifie le taux
            nomExterne = getSession().getLabel("IMPOT_SOURCE");

            if (JadeStringUtil.isDecimalEmpty(taux)) {
                // le taux d'imposition est stocke dans la table des taux par
                // canton
                PRTauxImpositionManager mgr = new PRTauxImpositionManager();

                mgr.setSession(getSession());
                mgr.setForIdTauxImposition(idExterne);
                mgr.setForPeriode(dateDebut, dateFin);
                mgr.find();

                if (!mgr.isEmpty()) {
                    taux = ((PRTauxImposition) mgr.get(0)).getTaux();
                }
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_afterUpdate(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        // effacement des montants ventiles de la repartition de paiement
        APRepartitionPaiementsManager repartitions = new APRepartitionPaiementsManager();

        repartitions.setForIdParent(idRepartitionBeneficiairePaiement);
        repartitions.setSession(getSession());
        repartitions.find();

        for (int idVentilation = 0; idVentilation < repartitions.size(); ++idVentilation) {
            APRepartitionPaiements repartition = (APRepartitionPaiements) repartitions.get(idVentilation);
            repartition.delete(transaction);
        }

        // calcul du montant total des cotisations
        APCotisationManager cotisations = new APCotisationManager();
        cotisations.setForIdRepartitionBeneficiairePaiement(idRepartitionBeneficiairePaiement);
        cotisations.setNotForIdCotisation(idCotisation);
        cotisations.setSession(getSession());

        FWCurrency total = new FWCurrency(cotisations.getSum(APCotisation.FIELDNAME_MONTANT).toString());
        total.add(montant);

        // Mise à jour du montant net de la répartition de paiement
        APRepartitionPaiements repartitionPaiements = new APRepartitionPaiements();
        repartitionPaiements.setSession(getSession());
        repartitionPaiements.setIdRepartitionBeneficiairePaiement(idRepartitionBeneficiairePaiement);
        repartitionPaiements.retrieve(transaction);

        total.add(repartitionPaiements.getMontantBrut());
        repartitionPaiements.setMontantNet(total.toString());
        repartitionPaiements.update(transaction);
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdCotisation(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return APCotisation.TABLE_NAME;
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idCotisation = statement.dbReadNumeric(APCotisation.FIELDNAME_ID_COTISATION);
        montant = statement.dbReadNumeric(APCotisation.FIELDNAME_MONTANT, 2);
        idExterne = statement.dbReadNumeric(APCotisation.FIELDNAME_ID_EXTERNE);
        dateDebut = statement.dbReadDateAMJ(APCotisation.FIELDNAME_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(APCotisation.FIELDNAME_DATEFIN);
        montantBrut = statement.dbReadNumeric(APCotisation.FIELDNAME_MONTANTBRUT, 2);
        idRepartitionBeneficiairePaiement = statement
                .dbReadNumeric(APCotisation.FIELDNAME_IDREPARTITIONBENEFICIAIREPAIEMENT);
        type = statement.dbReadString(APCotisation.FIELDNAME_TYPE);
        taux = statement.dbReadNumeric(APCotisation.FIELDNAME_TAUX, 2);
    }

    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(APCotisation.FIELDNAME_ID_COTISATION,
                this._dbWriteNumeric(statement.getTransaction(), idCotisation, "idCotisation"));
    }

    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(APCotisation.FIELDNAME_ID_COTISATION,
                this._dbWriteNumeric(statement.getTransaction(), idCotisation, "idCotisation"));
        statement.writeField(APCotisation.FIELDNAME_MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), montant, "montant"));
        statement.writeField(APCotisation.FIELDNAME_ID_EXTERNE,
                this._dbWriteNumeric(statement.getTransaction(), idExterne, "idExterne"));
        statement.writeField(APCotisation.FIELDNAME_DATEDEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(APCotisation.FIELDNAME_DATEFIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
        statement.writeField(APCotisation.FIELDNAME_MONTANTBRUT,
                this._dbWriteNumeric(statement.getTransaction(), montantBrut, "montantBrut"));
        statement.writeField(APCotisation.FIELDNAME_IDREPARTITIONBENEFICIAIREPAIEMENT, this._dbWriteNumeric(
                statement.getTransaction(), idRepartitionBeneficiairePaiement, "idRepartitionBeneficiairePaiement"));
        statement
                .writeField(APCotisation.FIELDNAME_TYPE, this._dbWriteString(statement.getTransaction(), type, "type"));
        statement.writeField(APCotisation.FIELDNAME_TAUX,
                this._dbWriteNumeric(statement.getTransaction(), taux, "taux"));
    }

    /**
     * getter pour l'attribut date debut
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin
     * 
     * @return la valeur courante de l'attribut date fin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut id cotisation
     * 
     * @return la valeur courante de l'attribut id cotisation
     */
    public String getIdCotisation() {
        return idCotisation;
    }

    /**
     * getter pour l'attribut id externe
     * 
     * @return la valeur courante de l'attribut id externe
     */
    public String getIdExterne() {
        return idExterne;
    }

    /**
     * getter pour l'attribut id repartition beneficiaire paiement
     * 
     * @return la valeur courante de l'attribut id repartition beneficiaire paiement
     */
    public String getIdRepartitionBeneficiairePaiement() {
        return idRepartitionBeneficiairePaiement;
    }

    /**
     * getter pour l'attribut montant
     * 
     * @return la valeur courante de l'attribut montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * getter pour l'attribut montant brut
     * 
     * @return la valeur courante de l'attribut montant brut
     */
    public String getMontantBrut() {
        return montantBrut;
    }

    /**
     * getter pour l'attribut nom externe
     * 
     * @return la valeur courante de l'attribut nom externe
     */
    public String getNomExterne() {
        return nomExterne;
    }

    /**
     * getter pour l'attribut taux
     * 
     * @return la valeur courante de l'attribut taux
     */
    public String getTaux() {
        return taux;
    }

    /**
     * getter pour l'attribut type
     * 
     * @return la valeur courante de l'attribut type
     */
    public String getType() {
        return type;
    }

    /**
     * setter pour l'attribut date debut
     * 
     * @param dateDebut
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * setter pour l'attribut date fin
     * 
     * @param dateFin
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * setter pour l'attribut id cotisation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCotisation(String string) {
        idCotisation = string;
    }

    /**
     * setter pour l'attribut id externe
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdExterne(String string) {
        idExterne = string;
    }

    /**
     * setter pour l'attribut id repartition beneficiaire paiement
     * 
     * @param idRepartitionBeneficiairePaiement
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRepartitionBeneficiairePaiement(String idRepartitionBeneficiairePaiement) {
        this.idRepartitionBeneficiairePaiement = idRepartitionBeneficiairePaiement;
    }

    /**
     * setter pour l'attribut montant
     * 
     * @param montant
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * setter pour l'attribut montant brut
     * 
     * @param montantBrut
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrut(String montantBrut) {
        this.montantBrut = montantBrut;
    }

    /**
     * setter pour l'attribut taux
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setTaux(String string) {
        taux = string;
    }

    /**
     * setter pour l'attribut type
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setType(String string) {
        type = string;
    }
}
