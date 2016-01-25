/*
 * Créé le 7 sept. 05
 */
package globaz.ij.db.prestations;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJCotisation extends BEntity implements IPRCloneable {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 
     */
    public static final String FIELDNAME_DATEDEBUT = "XSDDEB";

    /** 
     */
    public static final String FIELDNAME_DATEFIN = "XSDFIN";

    /** 
     */
    public static final String FIELDNAME_IDCOTISATION = "XSICOT";

    /** 
     */
    public static final String FIELDNAME_IDEXTERNE = "XSIEXT";

    /** 
     */
    public static final String FIELDNAME_IDREPARTITIONPAIEMENTS = "XSIREP";

    /** 
     */
    public static final String FIELDNAME_ISIMPOTSOURCE = "XSBIMS";

    /** 
     */
    public static final String FIELDNAME_MONTANT = "XSMMON";

    /** 
     */
    public static final String FIELDNAME_MONTANTBRUT = "XSMMBR";

    /** 
     */
    public static final String FIELDNAME_TAUXIMPOSITIONSOURCE = "XSMTIM";

    /** 
     */
    public static final String TABLE_NAME = "IJCOTIS";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String dateDebut = "";
    private String dateFin = "";
    private String idCotisation = "";
    private String idExterne = "";
    private String idRepartitionPaiement = "";
    private Boolean isImpotSource = Boolean.FALSE;
    private boolean miseAJourMontantRepartition = true;
    private String montant = "";
    private String montantBrut = "";

    private String nomExterne = "";
    private String taux = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        if (miseAJourMontantRepartition) {
            mettreAJourPrestation(transaction, true);
        }
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        if (isImpotSource.booleanValue()) {
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
        } else {
            // il s'agit d'une assurance, on va charger son nom et son taux
            List tauxList = PRAffiliationHelper.getTauxAssurance(getSession(), idExterne, "", dateDebut, dateFin);

            if ((tauxList != null) && !tauxList.isEmpty()) {
                nomExterne = ((AFTauxAssurance) tauxList.get(0)).getAssurance().getAssuranceLibelleCourt();
                taux = ((AFTauxAssurance) tauxList.get(0)).getValeurEmployeur();
            }
        }
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        if (miseAJourMontantRepartition) {
            mettreAJourPrestation(transaction, false);
        }
    }

    /**
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        setIdCotisation(_incCounter(transaction, "0"));
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idCotisation = statement.dbReadNumeric(FIELDNAME_IDCOTISATION);
        montant = statement.dbReadNumeric(FIELDNAME_MONTANT, 2);
        idExterne = statement.dbReadNumeric(FIELDNAME_IDEXTERNE);
        dateDebut = statement.dbReadDateAMJ(FIELDNAME_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(FIELDNAME_DATEFIN);
        montantBrut = statement.dbReadNumeric(FIELDNAME_MONTANTBRUT, 2);
        isImpotSource = statement.dbReadBoolean(FIELDNAME_ISIMPOTSOURCE);
        taux = statement.dbReadNumeric(FIELDNAME_TAUXIMPOSITIONSOURCE, 2);
        idRepartitionPaiement = statement.dbReadNumeric(FIELDNAME_IDREPARTITIONPAIEMENTS);
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_IDCOTISATION,
                _dbWriteNumeric(statement.getTransaction(), idCotisation, "idCotisation"));
    }

    /**
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(FIELDNAME_IDCOTISATION,
                _dbWriteNumeric(statement.getTransaction(), idCotisation, "idCotisation"));
        statement.writeField(FIELDNAME_MONTANT, _dbWriteNumeric(statement.getTransaction(), montant, "montant"));
        statement.writeField(FIELDNAME_IDEXTERNE, _dbWriteNumeric(statement.getTransaction(), idExterne, "idExterne"));
        statement.writeField(FIELDNAME_DATEDEBUT, _dbWriteDateAMJ(statement.getTransaction(), dateDebut, "dateDebut"));
        statement.writeField(FIELDNAME_DATEFIN, _dbWriteDateAMJ(statement.getTransaction(), dateFin, "dateFin"));
        statement.writeField(FIELDNAME_MONTANTBRUT,
                _dbWriteNumeric(statement.getTransaction(), montantBrut, "montantBrut"));
        statement.writeField(
                FIELDNAME_ISIMPOTSOURCE,
                _dbWriteBoolean(statement.getTransaction(), isImpotSource,
                        globaz.globall.db.BConstants.DB_TYPE_BOOLEAN_CHAR, "isImpotSource"));
        statement.writeField(FIELDNAME_TAUXIMPOSITIONSOURCE, _dbWriteNumeric(statement.getTransaction(), taux, "taux"));
        statement.writeField(FIELDNAME_IDREPARTITIONPAIEMENTS,
                _dbWriteNumeric(statement.getTransaction(), idRepartitionPaiement, "idRepartitionPaiement"));
    }

    /**
     * @param action
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public IPRCloneable duplicate(int action) throws Exception {
        IJCotisation clone = new IJCotisation();

        clone.setDateDebut(getDateDebut());
        clone.setDateFin(getDateFin());
        clone.setIdExterne(getIdExterne());
        clone.setIdRepartitionPaiement(getIdRepartitionPaiement());
        clone.setIsImpotSource(getIsImpotSource());
        clone.setMontant(getMontant());
        clone.setMontantBrut(getMontantBrut());
        clone.setNomExterne(getNomExterne());
        clone.setTaux(getTaux());

        clone.wantCallValidate(false);

        return clone;
    }

    /**
     * getter pour l'attribut date debut.
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin.
     * 
     * @return la valeur courante de l'attribut date fin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut id assurance.
     * 
     * @return la valeur courante de l'attribut id assurance
     */
    public String getIdCotisation() {
        return idCotisation;
    }

    /**
     * getter pour l'attribut id assurance generale.
     * 
     * @return la valeur courante de l'attribut id assurance generale
     */
    public String getIdExterne() {
        return idExterne;
    }

    /**
     * getter pour l'attribut id repartition paiements.
     * 
     * @return la valeur courante de l'attribut id repartition paiements
     */
    public String getIdRepartitionPaiement() {
        return idRepartitionPaiement;
    }

    /**
     * getter pour l'attribut is impot source.
     * 
     * @return la valeur courante de l'attribut is impot source
     */
    public Boolean getIsImpotSource() {
        return isImpotSource;
    }

    /**
     * getter pour l'attribut montant.
     * 
     * @return la valeur courante de l'attribut montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * getter pour l'attribut montant brut.
     * 
     * @return la valeur courante de l'attribut montant brut
     */
    public String getMontantBrut() {
        return montantBrut;
    }

    /**
     * getter pour l'attribut nom externe.
     * 
     * @return la valeur courante de l'attribut nom externe
     */
    public String getNomExterne() {
        return nomExterne;
    }

    /**
     * getter pour l'attribut taux imposition source.
     * 
     * @return la valeur courante de l'attribut taux imposition source
     */
    public String getTaux() {
        return taux;
    }

    /**
     * getter pour l'attribut unique primary key.
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdCotisation();
    }

    /**
     * getter pour l'attribut mise AJour montant repartition.
     * 
     * @return la valeur courante de l'attribut mise AJour montant repartition
     */
    public boolean isMiseAJourMontantRepartition() {
        return miseAJourMontantRepartition;
    }

    private void mettreAJourPrestation(BTransaction transaction, boolean delete) throws Exception {
        // effacement des montants ventiles de la repartition de paiement
        IJRepartitionPaiementsManager repartitions = new IJRepartitionPaiementsManager();

        repartitions.setForIdParent(idRepartitionPaiement);
        repartitions.setSession(getSession());
        repartitions.find();

        for (int idVentilation = 0; idVentilation < repartitions.size(); ++idVentilation) {
            IJRepartitionPaiements repartition = (IJRepartitionPaiements) repartitions.get(idVentilation);

            repartition.delete(transaction);
        }

        // calcul du montant total des cotisations
        IJCotisationManager cotisations = new IJCotisationManager();

        cotisations.setForIdRepartitionPaiements(idRepartitionPaiement);
        cotisations.setNotForIdCotisation(idCotisation);
        cotisations.setSession(getSession());

        FWCurrency total = new FWCurrency(cotisations.getSum(FIELDNAME_MONTANT).toString());

        if (!delete) {
            total.add(montant);
        }

        // Mise à jour du montant net de la répartition de paiement
        IJRepartitionPaiements repartitionPaiements = new IJRepartitionPaiements();

        repartitionPaiements.setSession(getSession());
        repartitionPaiements.setIdRepartitionPaiement(idRepartitionPaiement);
        repartitionPaiements.retrieve(transaction);

        total.add(repartitionPaiements.getMontantBrut());
        repartitionPaiements.setMontantNet(total.toString());
        repartitionPaiements.wantCallValidate(false);
        repartitionPaiements.update(transaction);
    }

    /**
     * setter pour l'attribut date debut.
     * 
     * @param dateDebut
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * setter pour l'attribut date fin.
     * 
     * @param dateFin
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * setter pour l'attribut id assurance.
     * 
     * @param idAssurance
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdCotisation(String idAssurance) {
        idCotisation = idAssurance;
    }

    /**
     * setter pour l'attribut id assurance generale.
     * 
     * @param idAssuranceGenerale
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdExterne(String idAssuranceGenerale) {
        idExterne = idAssuranceGenerale;
    }

    /**
     * setter pour l'attribut id repartition paiements.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRepartitionPaiement(String string) {
        idRepartitionPaiement = string;
    }

    /**
     * setter pour l'attribut is impot source.
     * 
     * @param isImpotSource
     *            une nouvelle valeur pour cet attribut
     */
    public void setIsImpotSource(Boolean isImpotSource) {
        this.isImpotSource = isImpotSource;
    }

    /**
     * setter pour l'attribut montant.
     * 
     * @param montant
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * setter pour l'attribut montant brut.
     * 
     * @param montantBrut
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrut(String montantBrut) {
        this.montantBrut = montantBrut;
    }

    /**
     * setter pour l'attribut nom externe.
     * 
     * @param nomExterne
     *            une nouvelle valeur pour cet attribut
     */
    public void setNomExterne(String nomExterne) {
        this.nomExterne = nomExterne;
    }

    /**
     * setter pour l'attribut taux imposition source.
     * 
     * @param tauxImpositionSource
     *            une nouvelle valeur pour cet attribut
     */
    public void setTaux(String tauxImpositionSource) {
        taux = tauxImpositionSource;
    }

    /**
     * setter pour l'attribut unique primary key.
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdCotisation(pk);
    }

    /**
     * @param miseAJourMontantRepartition
     *            DOCUMENT ME!
     */
    public void wantMiseAJourMontantRepartition(boolean miseAJourMontantRepartition) {
        this.miseAJourMontantRepartition = miseAJourMontantRepartition;
    }
}
