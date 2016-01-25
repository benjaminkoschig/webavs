/*
 * Créé le 6 sept. 05
 */
package globaz.ij.db.prestations;

import globaz.globall.api.BITransaction;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.ij.api.prestations.IIJPrestation;
import globaz.ij.db.annonces.IJAnnonce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class IJPrestation extends BEntity implements IPRCloneable, Comparable<IJPrestation> {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 
     */
    public static final String FIELDNAME_CS_ETAT = "XLTETA";

    public static final String FIELDNAME_CS_TYPE = "XLTTYP";

    /** 
     */
    public static final String FIELDNAME_DATEDEBUT = "XLDDEB";

    /** 
     */
    public static final String FIELDNAME_DATEDECOMPTE = "XLDDEC";

    /** 
     */
    public static final String FIELDNAME_DATEFIN = "XLDFIN";

    /** 
     */
    public static final String FIELDNAME_DROITACQUIS = "XLMDAC";

    public static final String FIELDNAME_ID_BASEINDEMNISATION = "XLIBIN";

    public static final String FIELDNAME_ID_IJCALCULEE = "XLIIJC";

    /** 
     */
    public static final String FIELDNAME_IDANNONCE = "XLIANN";

    /** 
     */
    public static final String FIELDNAME_IDLOT = "XLILOT";

    /** 
     */
    public static final String FIELDNAME_IDPRESTATION = "XLIPRE";

    public static final String FIELDNAME_MONTANT_JOURNALIER_EXT = "XLMMJE";

    public static final String FIELDNAME_MONTANT_JOURNALIER_INT = "XLMMJI";

    /** 
     */
    public static final String FIELDNAME_MONTANTBRUT = "XLMMBR";
    /** 
     */
    public static final String FIELDNAME_MONTANTBRUTEXTERNE = "XLMMBE";
    /** 
     */
    public static final String FIELDNAME_MONTANTBRUTINCAPPARTIELLE = "XLMBIP";
    /** 
     */
    public static final String FIELDNAME_MONTANTBRUTINTERNE = "XLMMBI";
    public static final String FIELDNAME_NOMBRE_JOURS_EXT = "XLNJIN";
    public static final String FIELDNAME_NOMBRE_JOURS_INT = "XLNJEX";
    /** 
     */
    public static final String TABLE_NAME = "IJPRESTA";

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private transient IJBaseIndemnisation base;
    private String csEtat = IIJPrestation.CS_VALIDE;
    private String csType = IIJPrestation.CS_NORMAL;
    private String dateDebut = "";
    private String dateDecompte = "";
    private String dateFin = "";
    private String droitAcquis = "";
    private String idAnnonce = "";
    private String idBaseIndemnisation = "";
    private String idIJCalculee = "";
    private String idLot = "";
    private String idPrestation = "";
    private String montantBrut = "";
    private String montantBrutExterne = "";
    private String montantBrutIncapPartielle = "";
    private String montantBrutInterne = "";
    private String montantJournalierExterne = "";
    private String montantJournalierInterne = "";
    private String nombreJoursExt = "";

    private String nombreJoursInt = "";

    // non persistent
    private String communePolitique = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getCommunePolitique() {
        return communePolitique;
    }

    public void setCommunePolitique(String communePolitique) {
        this.communePolitique = communePolitique;
    }

    /**
     * (non-Javadoc).
     * 
     * @param transaction
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        super._afterDelete(transaction);

        // effacement des répartitions de paiement
        IJRepartitionPaiementsManager repartitionPaiementsManager = new IJRepartitionPaiementsManager();

        repartitionPaiementsManager.setSession(getSession());
        repartitionPaiementsManager.setForIdPrestation(idPrestation);
        repartitionPaiementsManager.find(transaction, BManager.SIZE_NOLIMIT);

        for (int i = 0; i < repartitionPaiementsManager.size(); ++i) {
            IJRepartitionPaiements repartitionPaiements = (IJRepartitionPaiements) repartitionPaiementsManager
                    .getEntity(i);

            repartitionPaiements.delete(transaction);
        }

        // effacement de l'annonce
        if (!JadeStringUtil.isIntegerEmpty(idAnnonce)) {
            IJAnnonce annonce = new IJAnnonce();

            annonce.setSession(getSession());
            annonce.setIdAnnonce(idAnnonce);
            annonce.retrieve(transaction);

            if (!annonce.isNew()) {
                annonce.delete(transaction);
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
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdPrestation(_incCounter(transaction, "0"));
    }

    /**
     * (non-Javadoc).
     * 
     * @return DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_NAME;
    }

    /**
     * (non-Javadoc).
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        dateDebut = statement.dbReadDateAMJ(FIELDNAME_DATEDEBUT);
        dateFin = statement.dbReadDateAMJ(FIELDNAME_DATEFIN);
        idPrestation = statement.dbReadNumeric(FIELDNAME_IDPRESTATION);
        dateDecompte = statement.dbReadDateAMJ(FIELDNAME_DATEDECOMPTE);
        montantBrut = statement.dbReadNumeric(FIELDNAME_MONTANTBRUT, 2);
        montantBrutInterne = statement.dbReadNumeric(FIELDNAME_MONTANTBRUTINTERNE, 2);
        montantBrutExterne = statement.dbReadNumeric(FIELDNAME_MONTANTBRUTEXTERNE, 2);
        montantBrutIncapPartielle = statement.dbReadNumeric(FIELDNAME_MONTANTBRUTINCAPPARTIELLE, 2);
        idLot = statement.dbReadNumeric(FIELDNAME_IDLOT);
        droitAcquis = statement.dbReadNumeric(FIELDNAME_DROITACQUIS, 2);
        csEtat = statement.dbReadNumeric(FIELDNAME_CS_ETAT);
        idAnnonce = statement.dbReadNumeric(FIELDNAME_IDANNONCE);
        nombreJoursExt = statement.dbReadNumeric(FIELDNAME_NOMBRE_JOURS_EXT);
        nombreJoursInt = statement.dbReadNumeric(FIELDNAME_NOMBRE_JOURS_INT);
        csType = statement.dbReadNumeric(FIELDNAME_CS_TYPE);
        idIJCalculee = statement.dbReadNumeric(FIELDNAME_ID_IJCALCULEE);
        idBaseIndemnisation = statement.dbReadNumeric(FIELDNAME_ID_BASEINDEMNISATION);
        montantJournalierExterne = statement.dbReadNumeric(FIELDNAME_MONTANT_JOURNALIER_EXT);
        montantJournalierInterne = statement.dbReadNumeric(FIELDNAME_MONTANT_JOURNALIER_INT);

    }

    /**
     * (non-Javadoc).
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * (non-Javadoc).
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELDNAME_IDPRESTATION,
                _dbWriteNumeric(statement.getTransaction(), idPrestation, "idPrestation"));
    }

    /**
     * (non-Javadoc).
     * 
     * @param statement
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        BTransaction transaction = statement.getTransaction();

        statement.writeField(FIELDNAME_DATEDEBUT, _dbWriteDateAMJ(transaction, dateDebut, "dateDebut"));
        statement.writeField(FIELDNAME_DATEFIN, _dbWriteDateAMJ(transaction, dateFin, "dateFin"));
        statement.writeField(FIELDNAME_IDPRESTATION, _dbWriteNumeric(transaction, idPrestation, "idPrestation"));
        statement.writeField(FIELDNAME_DATEDECOMPTE, _dbWriteDateAMJ(transaction, dateDecompte, "dateDecompte"));
        statement.writeField(FIELDNAME_MONTANTBRUT, _dbWriteNumeric(transaction, montantBrut, "montantBrut"));
        statement.writeField(FIELDNAME_MONTANTBRUTINTERNE,
                _dbWriteNumeric(transaction, montantBrutInterne, "montantBrutInterne"));
        statement.writeField(FIELDNAME_MONTANTBRUTEXTERNE,
                _dbWriteNumeric(transaction, montantBrutExterne, "montantBrutExterne"));
        statement.writeField(FIELDNAME_MONTANTBRUTINCAPPARTIELLE,
                _dbWriteNumeric(transaction, montantBrutIncapPartielle, "montantBrutIncapPartielle"));
        statement.writeField(FIELDNAME_IDLOT, _dbWriteNumeric(transaction, idLot, "idLot"));
        statement.writeField(FIELDNAME_DROITACQUIS, _dbWriteNumeric(transaction, droitAcquis, "droitAcquis"));
        statement.writeField(FIELDNAME_CS_ETAT, _dbWriteNumeric(transaction, csEtat, "csEtat"));
        statement.writeField(FIELDNAME_IDANNONCE, _dbWriteNumeric(transaction, idAnnonce, "idAnnonce"));
        statement
                .writeField(FIELDNAME_NOMBRE_JOURS_EXT, _dbWriteNumeric(transaction, nombreJoursExt, "nombreJoursExt"));
        statement
                .writeField(FIELDNAME_NOMBRE_JOURS_INT, _dbWriteNumeric(transaction, nombreJoursInt, "nombreJoursInt"));
        statement.writeField(FIELDNAME_CS_TYPE, _dbWriteNumeric(transaction, csType, "csType"));
        statement.writeField(FIELDNAME_ID_IJCALCULEE, _dbWriteNumeric(transaction, idIJCalculee, "idIJCalculee"));
        statement.writeField(FIELDNAME_ID_BASEINDEMNISATION,
                _dbWriteNumeric(transaction, idBaseIndemnisation, "idBaseIndemnisation"));
        statement.writeField(FIELDNAME_MONTANT_JOURNALIER_EXT,
                _dbWriteNumeric(transaction, montantJournalierExterne, "montantJournalierExterne"));
        statement.writeField(FIELDNAME_MONTANT_JOURNALIER_INT,
                _dbWriteNumeric(transaction, montantJournalierInterne, "montantJournalierInterne"));

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
        IJPrestation clone = new IJPrestation();

        clone.setCsEtat(getCsEtat());
        clone.setCsType(getCsType());
        clone.setDateDebut(getDateDebut());
        clone.setDateDecompte(getDateDecompte());
        clone.setDateFin(getDateFin());
        clone.setDroitAcquis(getDroitAcquis());
        clone.setIdAnnonce(getIdAnnonce());
        clone.setIdBaseIndemnisation(getIdBaseIndemnisation());
        clone.setIdIJCalculee(getIdIJCalculee());
        clone.setIdLot(getIdLot());
        clone.setMontantBrut(getMontantBrut());
        clone.setMontantBrutExterne(getMontantBrutExterne());
        clone.setMontantBrutIncapPartielle(getMontantBrutIncapPartielle());
        clone.setMontantBrutInterne(getMontantBrutInterne());
        clone.setMontantJournalierExterne(getMontantJournalierExterne());
        clone.setMontantJournalierInterne(getMontantJournalierInterne());
        clone.setNombreJoursExt(getNombreJoursExt());
        clone.setNombreJoursInt(getNombreJoursInt());

        clone.wantCallValidate(false);

        return clone;
    }

    /**
     * getter pour l'attribut cs etat.
     * 
     * @return la valeur courante de l'attribut cs etat
     */
    public String getCsEtat() {
        return csEtat;
    }

    /**
     * getter pour l'attribut cs type.
     * 
     * @return la valeur courante de l'attribut cs type
     */
    public String getCsType() {
        return csType;
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
     * getter pour l'attribut date decompte.
     * 
     * @return la valeur courante de l'attribut date decompte
     */
    public String getDateDecompte() {
        return dateDecompte;
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
     * getter pour l'attribut droit acquis.
     * 
     * @deprecated Remplacé par le champ noRevisionGaranti dans IJCalcul. Utilisé pour la génération des annonces
     * @return la valeur courante de l'attribut droit acquis
     */
    @Deprecated
    public String getDroitAcquis() {
        return droitAcquis;
    }

    /**
     * getter pour l'attribut id annonce.
     * 
     * @return la valeur courante de l'attribut id annonce
     */
    public String getIdAnnonce() {
        return idAnnonce;
    }

    /**
     * getter pour l'attribut id base indemnisation.
     * 
     * @return la valeur courante de l'attribut id base indemnisation
     */
    public String getIdBaseIndemnisation() {
        return idBaseIndemnisation;
    }

    /**
     * getter pour l'attribut id IJCalculee.
     * 
     * @return la valeur courante de l'attribut id IJCalculee
     */
    public String getIdIJCalculee() {
        return idIJCalculee;
    }

    /**
     * getter pour l'attribut id lot.
     * 
     * @return la valeur courante de l'attribut id lot
     */
    public String getIdLot() {
        return idLot;
    }

    /**
     * getter pour l'attribut id decompte global.
     * 
     * @return la valeur courante de l'attribut id decompte global
     */
    public String getIdPrestation() {
        return idPrestation;
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
     * getter pour l'attribut montant brut externe.
     * 
     * @return la valeur courante de l'attribut montant brut externe
     */
    public String getMontantBrutExterne() {
        return montantBrutExterne;
    }

    /**
     * getter pour l'attribut montant brut incap partielle.
     * 
     * @return la valeur courante de l'attribut montant brut incap partielle
     */
    public String getMontantBrutIncapPartielle() {
        return montantBrutIncapPartielle;
    }

    /**
     * getter pour l'attribut montant brut interne.
     * 
     * @return la valeur courante de l'attribut montant brut interne
     */
    public String getMontantBrutInterne() {
        return montantBrutInterne;
    }

    /**
     * getter pour l'attribut montant journalier externe.
     * 
     * @return la valeur courante de l'attribut montant journalier externe
     */
    public String getMontantJournalierExterne() {
        return montantJournalierExterne;
    }

    /**
     * getter pour l'attribut montant journalier interne.
     * 
     * @return la valeur courante de l'attribut montant journalier interne
     */
    public String getMontantJournalierInterne() {
        return montantJournalierInterne;
    }

    /**
     * getter pour l'attribut nombre jours ext.
     * 
     * @return la valeur courante de l'attribut nombre jours ext
     */
    public String getNombreJoursExt() {
        return nombreJoursExt;
    }

    /**
     * getter pour l'attribut nombre jours int.
     * 
     * @return la valeur courante de l'attribut nombre jours int
     */
    public String getNombreJoursInt() {
        return nombreJoursInt;
    }

    /**
     * getter pour l'attribut unique primary key.
     * 
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdPrestation();
    }

    /**
     * getter pour l'attribut restitution.
     * 
     * @return la valeur courante de l'attribut restitution
     */
    public boolean isRestitution() {
        return IIJPrestation.CS_RESTITUTION.equals(csType);
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public IJBaseIndemnisation loadBaseIndemnisation(BITransaction transaction) throws Exception {
        if ((base == null) && !JadeStringUtil.isIntegerEmpty(idBaseIndemnisation)) {
            base = new IJBaseIndemnisation();
            base.setIdBaseIndemisation(idBaseIndemnisation);
            base.setSession(getSession());
            if (transaction == null) {
                base.retrieve();
            } else {
                base.retrieve(transaction);
            }
        }

        return base;
    }

    /**
     * setter pour l'attribut cs etat.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsEtat(String string) {
        csEtat = string;
    }

    /**
     * setter pour l'attribut cs type.
     * 
     * @param csType
     *            une nouvelle valeur pour cet attribut
     */
    public void setCsType(String csType) {
        this.csType = csType;
    }

    /**
     * setter pour l'attribut date debut.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * setter pour l'attribut date decompte.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDecompte(String string) {
        dateDecompte = string;
    }

    /**
     * setter pour l'attribut date fin.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    /**
     * setter pour l'attribut droit acquis.
     * 
     * @deprecated Remplacé par le champ noRevisionGaranti dans IJCalcul. Utilisé pour la génération des annonces
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Deprecated
    public void setDroitAcquis(String string) {
        droitAcquis = string;
    }

    /**
     * setter pour l'attribut id annonce.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdAnnonce(String string) {
        idAnnonce = string;
    }

    /**
     * setter pour l'attribut id base indemnisation.
     * 
     * @param idBaseIndemnisation
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdBaseIndemnisation(String idBaseIndemnisation) {
        this.idBaseIndemnisation = idBaseIndemnisation;
    }

    /**
     * setter pour l'attribut id IJCalculee.
     * 
     * @param idIJCalculee
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdIJCalculee(String idIJCalculee) {
        this.idIJCalculee = idIJCalculee;
    }

    /**
     * setter pour l'attribut id lot.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdLot(String string) {
        idLot = string;
    }

    /**
     * setter pour l'attribut id decompte global.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdPrestation(String string) {
        idPrestation = string;
    }

    /**
     * setter pour l'attribut montant brut.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrut(String string) {
        montantBrut = string;
    }

    /**
     * setter pour l'attribut montant brut externe.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrutExterne(String string) {
        montantBrutExterne = string;
    }

    /**
     * setter pour l'attribut montant brut incap partielle.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrutIncapPartielle(String string) {
        montantBrutIncapPartielle = string;
    }

    /**
     * setter pour l'attribut montant brut interne.
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrutInterne(String string) {
        montantBrutInterne = string;
    }

    /**
     * setter pour l'attribut montant journalier externe.
     * 
     * @param montantJournalierExterne
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantJournalierExterne(String montantJournalierExterne) {
        this.montantJournalierExterne = montantJournalierExterne;
    }

    /**
     * setter pour l'attribut montant journalier interne.
     * 
     * @param montantJournalierInterne
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantJournalierInterne(String montantJournalierInterne) {
        this.montantJournalierInterne = montantJournalierInterne;
    }

    /**
     * setter pour l'attribut nombre jours ext.
     * 
     * @param nombreJoursExt
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursExt(String nombreJoursExt) {
        this.nombreJoursExt = nombreJoursExt;
    }

    /**
     * setter pour l'attribut nombre jours int.
     * 
     * @param nombreJoursInt
     *            une nouvelle valeur pour cet attribut
     */
    public void setNombreJoursInt(String nombreJoursInt) {
        this.nombreJoursInt = nombreJoursInt;
    }

    /**
     * setter pour l'attribut unique primary key.
     * 
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdPrestation(pk);
    }

    @Override
    public int compareTo(IJPrestation o) {
        return getCommunePolitique().compareToIgnoreCase(o.getCommunePolitique());
    }

}
