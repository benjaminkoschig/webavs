package globaz.corvus.db.rentesaccordees;

import globaz.corvus.exceptions.REBusinessException;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;

/**
 * Entité de base de donnée représentant une rente qui n'aurait pas dû être versée (versée à tort) car un autre droit
 * (plus récent) l'a remplacée.<br/>
 * 
 * @author PBA
 */
public class RERenteVerseeATort extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String CS_TYPE_AVANCE_DEJA_PERCUE = "52861004";
    public final static String CS_TYPE_DETTES = "52861001";
    public final static String CS_TYPE_PRESTATION_EN_SUSPENS = "52861002";
    public final static String CS_TYPE_PRESTATION_NON_VERSEE = "52861005";
    public final static String CS_TYPE_PRESTATION_TOUCHEE_INDUMENT = "52861003";
    public final static String CS_TYPE_SAISIE_MANUELLE = "52861006";
    public final static String DATE_DEBUT = "WKDDEB";
    public final static String DATE_FIN = "WKDFIN";
    public final static String DESCRIPTION_SAISIE_MANUELLE = "WKLDES";
    public final static String FAMILLE_CODE_SYSTEME_TYPE_RENTE_VERSEE_A_TROT = "RETYPERVAT";
    public static final String ID_DEMANDE_RENTE = "WKIDEM";
    public static final String ID_RENTE_ANCIEN_DROIT = "WKIRAD";
    public static final String ID_RENTE_NOUVEAU_DROIT = "WKIRND";
    public static final String ID_RENTE_VERSEE_A_TORT = "WKIRVT";
    public static final String ID_TIERS = "WKITIE";
    public static final String IS_SAISIE_MANUELLE = "WKBSMA";
    public static final String MONTANT = "WKMMON";
    public static final String TABLE_RENTE_VERSEE_A_TORT = "RERENVAT";
    public static final String TYPE_RENTE_VERSEE_A_TORT = "WKTTYP";

    private String dateDebut;
    private String dateFin;
    private String descriptionSaisieManuelle;
    private Long idDemandeRente;
    private Long idRenteAccordeeAncienDroit;
    private Long idRenteAccordeeNouveauDroit;
    private Long idRenteVerseeATort;
    private Long idTiers;
    private BigDecimal montant;
    private boolean saisieManuelle;
    private TypeRenteVerseeATort typeRenteVerseeATort;

    public RERenteVerseeATort() {
        super();

        dateDebut = null;
        dateFin = null;
        descriptionSaisieManuelle = null;
        idDemandeRente = null;
        idRenteAccordeeAncienDroit = null;
        idRenteAccordeeNouveauDroit = null;
        idRenteVerseeATort = null;
        idTiers = null;
        saisieManuelle = false;
        montant = null;
        typeRenteVerseeATort = null;
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        if (idRenteVerseeATort != null) {
            idRenteVerseeATort = Long.parseLong(this._incCounter(transaction, idRenteVerseeATort.toString(),
                    RERenteVerseeATort.TABLE_RENTE_VERSEE_A_TORT));
        } else {
            idRenteVerseeATort = Long.parseLong(this._incCounter(transaction, "",
                    RERenteVerseeATort.TABLE_RENTE_VERSEE_A_TORT));
        }
    }

    @Override
    protected String _getTableName() {
        return RERenteVerseeATort.TABLE_RENTE_VERSEE_A_TORT;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        String idRenteVerseeATort = statement.dbReadNumeric(RERenteVerseeATort.ID_RENTE_VERSEE_A_TORT);
        if (!JadeStringUtil.isBlank(idRenteVerseeATort)) {
            this.idRenteVerseeATort = Long.parseLong(idRenteVerseeATort);
        } else {
            this.idRenteVerseeATort = null;
        }

        String idDemandeRente = statement.dbReadNumeric(RERenteVerseeATort.ID_DEMANDE_RENTE);
        if (!JadeStringUtil.isBlank(idDemandeRente)) {
            this.idDemandeRente = Long.parseLong(idDemandeRente);
        } else {
            this.idDemandeRente = null;
        }

        String idRenteNouveaudroit = statement.dbReadNumeric(RERenteVerseeATort.ID_RENTE_NOUVEAU_DROIT);
        if (!JadeStringUtil.isBlank(idRenteNouveaudroit)) {
            idRenteAccordeeNouveauDroit = Long.parseLong(idRenteNouveaudroit);
        } else {
            idRenteAccordeeNouveauDroit = null;
        }

        String idRenteAncienDroit = statement.dbReadNumeric(RERenteVerseeATort.ID_RENTE_ANCIEN_DROIT);
        if (!JadeStringUtil.isBlank(idRenteAncienDroit)) {
            idRenteAccordeeAncienDroit = Long.parseLong(idRenteAncienDroit);
        } else {
            idRenteAccordeeAncienDroit = null;
        }

        String idTiers = statement.dbReadNumeric(RERenteVerseeATort.ID_TIERS);
        if (!JadeStringUtil.isBlank(idTiers)) {
            this.idTiers = Long.parseLong(idTiers);
        } else {
            this.idTiers = null;
        }

        String montant = statement.dbReadNumeric(RERenteVerseeATort.MONTANT);
        if (!JadeStringUtil.isBlank(montant)) {
            this.montant = new BigDecimal(montant);
        } else {
            this.montant = null;
        }

        String typeRenteVerseeATort = statement.dbReadNumeric(RERenteVerseeATort.TYPE_RENTE_VERSEE_A_TORT);
        if (!JadeStringUtil.isBlank(typeRenteVerseeATort)) {
            this.typeRenteVerseeATort = TypeRenteVerseeATort.parse(typeRenteVerseeATort);
        } else {
            this.typeRenteVerseeATort = TypeRenteVerseeATort.SAISIE_MANUELLE;
        }

        dateDebut = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadString(RERenteVerseeATort.DATE_DEBUT));
        dateFin = PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(statement.dbReadString(RERenteVerseeATort.DATE_FIN));
        descriptionSaisieManuelle = statement.dbReadString(RERenteVerseeATort.DESCRIPTION_SAISIE_MANUELLE);
        saisieManuelle = statement.dbReadBoolean(RERenteVerseeATort.IS_SAISIE_MANUELLE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // si un des identifiant est invalide, une erreur est levée
        if ((idRenteVerseeATort == null) || (idRenteVerseeATort < 0)
                || ((idRenteAccordeeAncienDroit != null) && (idRenteAccordeeAncienDroit < 0))
                || ((idRenteAccordeeNouveauDroit != null) && (idRenteAccordeeNouveauDroit < 0))
                || (idDemandeRente == null) || (idDemandeRente < 0) || (idTiers == null) || (idTiers < 0)) {
            throw new REBusinessException(getSession().getLabel("ERREUR_ID_ABSENT"));
        }

        // si le montant (celui rentré par l'utilistateur) est invalide ou négatif, une erreur est levée
        if ((montant == null) || (montant.doubleValue() < 0.0)) {
            throw new REBusinessException(getSession().getLabel("ERREUR_MONTANT_DEFINITIF_INVALIDE"));
        }

        if (saisieManuelle) {
            if (TypeRenteVerseeATort.SAISIE_MANUELLE.equals(typeRenteVerseeATort)) {
                if ((descriptionSaisieManuelle == null) || JadeStringUtil.isBlank(descriptionSaisieManuelle.trim())) {
                    throw new REBusinessException(getSession().getLabel(
                            "ERREUR_DESCRIPTION_POUR_SAISIE_MANUELLE_OBLIGATOIRE"));
                }
            }
        } else {

            if (!JadeDateUtil.isGlobazDateMonthYear(dateDebut)) {
                throw new REBusinessException(getSession().getLabel("ERREUR_DATE_DEBUT_MOIS_ANNEE_SEULEMENT"));
            }

            if (!JadeDateUtil.isGlobazDateMonthYear(dateFin)) {
                throw new REBusinessException(getSession().getLabel("ERREUR_DATE_FIN_MOIS_ANNEE_SEULEMENT"));
            }

            if (JadeDateUtil.isDateMonthYearBefore(dateFin, dateDebut)) {
                throw new REBusinessException(getSession().getLabel("ERREUR_DATE_FIN_ANTERIEUR_DATE_DEBUT"));
            }
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(RERenteVerseeATort.ID_RENTE_VERSEE_A_TORT,
                this._dbWriteNumeric(statement.getTransaction(), idRenteVerseeATort.toString(), "idRenteVerseeATort"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(RERenteVerseeATort.ID_RENTE_VERSEE_A_TORT,
                this._dbWriteNumeric(statement.getTransaction(), idRenteVerseeATort.toString(), "idRenteVerseeATort"));
        statement.writeField(RERenteVerseeATort.ID_DEMANDE_RENTE,
                this._dbWriteNumeric(statement.getTransaction(), idDemandeRente.toString(), "idDemandeRente"));
        statement.writeField(RERenteVerseeATort.ID_TIERS,
                this._dbWriteNumeric(statement.getTransaction(), idTiers.toString(), "idTiers"));

        if (idRenteAccordeeAncienDroit != null) {
            statement.writeField(RERenteVerseeATort.ID_RENTE_ANCIEN_DROIT, this._dbWriteNumeric(
                    statement.getTransaction(), idRenteAccordeeAncienDroit.toString(), "idRenteAccordeeARestituer"));
        }

        if (idRenteAccordeeNouveauDroit != null) {
            statement.writeField(RERenteVerseeATort.ID_RENTE_NOUVEAU_DROIT, this._dbWriteNumeric(
                    statement.getTransaction(), idRenteAccordeeNouveauDroit.toString(), "idRenteAccordeeNouveauDroit"));
        }

        statement.writeField(RERenteVerseeATort.MONTANT,
                this._dbWriteNumeric(statement.getTransaction(), montant.toString(), "montant"));
        statement.writeField(RERenteVerseeATort.TYPE_RENTE_VERSEE_A_TORT, this._dbWriteNumeric(
                statement.getTransaction(), typeRenteVerseeATort.getCodeSysteme().toString(), "typeRenteVerseeATort"));
        statement.writeField(
                RERenteVerseeATort.DATE_DEBUT,
                this._dbWriteNumeric(statement.getTransaction(),
                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateDebut), "dateDebut"));
        statement.writeField(RERenteVerseeATort.DATE_FIN, this._dbWriteNumeric(statement.getTransaction(),
                PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateFin), "dateFin"));
        statement
                .writeField(RERenteVerseeATort.DESCRIPTION_SAISIE_MANUELLE, this._dbWriteString(
                        statement.getTransaction(), descriptionSaisieManuelle, "descriptionSaisieManuelle"));
        statement.writeField(RERenteVerseeATort.IS_SAISIE_MANUELLE, this._dbWriteBoolean(statement.getTransaction(),
                saisieManuelle, BConstants.DB_TYPE_BOOLEAN_CHAR, "saisieManuelle"));
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getDescriptionSaisieManuelle() {
        return descriptionSaisieManuelle;
    }

    /**
     * Retourne l'ID de la demande ayant créer le nouveau droit entraînant des rentes versées à tort.<br/>
     * Ce n'est pas la demande dont dépend la rente versée à tort mais la demande du nouveau droit.
     */
    public Long getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * Retourne l'ID de la rente ayant été versée à tort (métier, pointe sur une rente dans la table REREACC/REPRACC),
     * cette rente a été versée alors qu'une rente de la nouvelle demande ({@link #getIdDemandeRente()}) couvrait cette
     * période.
     * 
     * @return l'ID de la rente versée à tort (métier)
     */
    public Long getIdRenteAccordeeAncienDroit() {
        return idRenteAccordeeAncienDroit;
    }

    public Long getIdRenteAccordeeNouveauDroit() {
        return idRenteAccordeeNouveauDroit;
    }

    /**
     * Renvoi la clé primaire de l'entrée en base de donnée (n'est pas l'identifiant de la rente ayant été versée à
     * tort, mais un ID "technique")
     */
    public Long getIdRenteVerseeATort() {
        return idRenteVerseeATort;
    }

    public Long getIdTiers() {
        return idTiers;
    }

    /**
     * Retourne le montant définitif retenue pour cette rente versée à tort (lors de la création de la rente versée à
     * tort, ce montant sera le même que celui calculé, mais il peut être modifié ultérieurement par l'utilisateur).
     * 
     * @return
     */
    public BigDecimal getMontant() {
        return montant;
    }

    public TypeRenteVerseeATort getTypeRenteVerseeATort() {
        return typeRenteVerseeATort;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

    @Override
    public boolean hasSpy() {
        return true;
    }

    public boolean isSaisieManuelle() {
        return saisieManuelle;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDescriptionSaisieManuelle(String descriptionSaisieManuelle) {
        this.descriptionSaisieManuelle = descriptionSaisieManuelle;
    }

    public void setIdDemandeRente(Long idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }

    public void setIdRenteAccordeeAncienDroit(Long idRenteAccordeeARestituer) {
        idRenteAccordeeAncienDroit = idRenteAccordeeARestituer;
    }

    public void setIdRenteAccordeeNouveauDroit(Long idRenteAccordeeNouveauDroit) {
        this.idRenteAccordeeNouveauDroit = idRenteAccordeeNouveauDroit;
    }

    public void setIdRenteVerseeATort(Long idRenteVerseeATort) {
        this.idRenteVerseeATort = idRenteVerseeATort;
    }

    public void setIdTiers(Long idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public void setSaisieManuelle(boolean saisieManuelle) {
        this.saisieManuelle = saisieManuelle;
    }

    public void setTypeRenteVerseeATort(TypeRenteVerseeATort typeRenteVerseeATort) {
        this.typeRenteVerseeATort = typeRenteVerseeATort;
    }
}
