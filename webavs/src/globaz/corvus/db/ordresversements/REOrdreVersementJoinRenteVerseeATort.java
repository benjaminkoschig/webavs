package globaz.corvus.db.ordresversements;

import globaz.corvus.db.rentesaccordees.RERenteVerseeATort;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;
import ch.globaz.corvus.domaine.constantes.TypeRenteVerseeATort;

public class REOrdreVersementJoinRenteVerseeATort extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean compensationInterDecision;
    private boolean compense;
    private String descriptionSaisieManuelleRenteVerseeATort;
    private Long idOrdreVersement;
    private Long idRenteVerseeATort;
    private Long idTiersOrdreVersement;
    private BigDecimal montantCompenseOrdreVersement;
    private BigDecimal montantDetteOrdreVersement;
    private BigDecimal montantRenteVerseeATort;
    private TypeOrdreVersement typeOrdreVersement;
    private TypeRenteVerseeATort typeRenteVerseeATort;

    public REOrdreVersementJoinRenteVerseeATort() {
        super();

        compensationInterDecision = false;
        compense = false;
        descriptionSaisieManuelleRenteVerseeATort = null;
        idOrdreVersement = null;
        idRenteVerseeATort = null;
        idTiersOrdreVersement = null;
        montantCompenseOrdreVersement = null;
        montantDetteOrdreVersement = null;
        montantRenteVerseeATort = null;
        typeOrdreVersement = null;
        typeRenteVerseeATort = null;
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuilder sql = new StringBuilder();

        String tableOrdreVersement = _getCollection() + REOrdresVersements.TABLE_NAME_ORDRES_VERSEMENTS;
        String tableRenteVerseeATort = _getCollection() + RERenteVerseeATort.TABLE_RENTE_VERSEE_A_TORT;

        sql.append(tableOrdreVersement);

        sql.append(" LEFT OUTER JOIN ").append(tableRenteVerseeATort);
        sql.append(" ON ").append(tableOrdreVersement).append(".")
                .append(REOrdresVersements.FIELDNAME_ID_RENTE_VERSEE_A_TORT).append("=").append(tableRenteVerseeATort)
                .append(".").append(RERenteVerseeATort.ID_RENTE_VERSEE_A_TORT);

        return sql.toString();
    }

    @Override
    protected String _getTableName() {
        return REOrdresVersements.TABLE_NAME_ORDRES_VERSEMENTS;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        compensationInterDecision = statement
                .dbReadBoolean(REOrdresVersements.FIELDNAME_IS_COMPENSATION_INTER_DECISION);
        compense = statement.dbReadBoolean(REOrdresVersements.FIELDNAME_IS_COMPENSE);
        idOrdreVersement = Long.parseLong(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_ORDRE_VERSEMENT));
        idTiersOrdreVersement = Long.parseLong(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_TIERS));
        montantCompenseOrdreVersement = new BigDecimal(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_MONTANT));
        montantDetteOrdreVersement = new BigDecimal(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_MONTANT_DETTE));
        typeOrdreVersement = TypeOrdreVersement.parse(statement.dbReadNumeric(REOrdresVersements.FIELDNAME_TYPE));

        String idRenteVerseeATort = statement.dbReadNumeric(REOrdresVersements.FIELDNAME_ID_RENTE_VERSEE_A_TORT);
        if (!JadeStringUtil.isBlankOrZero(idRenteVerseeATort)) {
            descriptionSaisieManuelleRenteVerseeATort = statement
                    .dbReadString(RERenteVerseeATort.DESCRIPTION_SAISIE_MANUELLE);
            this.idRenteVerseeATort = Long.parseLong(idRenteVerseeATort);
            montantRenteVerseeATort = new BigDecimal(statement.dbReadNumeric(RERenteVerseeATort.MONTANT));
            typeRenteVerseeATort = TypeRenteVerseeATort.parse(statement
                    .dbReadNumeric(RERenteVerseeATort.TYPE_RENTE_VERSEE_A_TORT));
        }
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // rien, ne peut pas être persisté
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // rien, ne peut pas être persisté
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        throw new UnsupportedOperationException("can't persist this entity");
    }

    public String getDescriptionSaisieManuelleRenteVerseeATort() {
        return descriptionSaisieManuelleRenteVerseeATort;
    }

    public Long getIdOrdreVersement() {
        return idOrdreVersement;
    }

    public Long getIdRenteVerseeATort() {
        return idRenteVerseeATort;
    }

    public Long getIdTiersOrdreVersement() {
        return idTiersOrdreVersement;
    }

    public BigDecimal getMontantCompenseOrdreVersement() {
        return montantCompenseOrdreVersement;
    }

    public BigDecimal getMontantDetteOrdreVersement() {
        return montantDetteOrdreVersement;
    }

    public BigDecimal getMontantRenteVerseeATort() {
        return montantRenteVerseeATort;
    }

    public TypeOrdreVersement getTypeOrdreVersement() {
        return typeOrdreVersement;
    }

    public TypeRenteVerseeATort getTypeRenteVerseeATort() {
        return typeRenteVerseeATort;
    }

    public boolean isCompensationInterDecision() {
        return compensationInterDecision;
    }

    public boolean isCompense() {
        return compense;
    }

    public void setCompensationInterDecision(boolean compensationInterDecision) {
        this.compensationInterDecision = compensationInterDecision;
    }

    public void setCompense(boolean compense) {
        this.compense = compense;
    }

    public void setDescriptionSaisieManuelleRenteVerseeATort(String descriptionSaisieManuelleRenteVerseeATort) {
        this.descriptionSaisieManuelleRenteVerseeATort = descriptionSaisieManuelleRenteVerseeATort;
    }

    public void setIdOrdreVersement(Long idOrdreVersement) {
        this.idOrdreVersement = idOrdreVersement;
    }

    public void setIdRenteVerseeATort(Long idRenteVerseeATort) {
        this.idRenteVerseeATort = idRenteVerseeATort;
    }

    public void setIdTiersOrdreVersement(Long idTiersOrdreVersement) {
        this.idTiersOrdreVersement = idTiersOrdreVersement;
    }

    public void setMontantCompenseOrdreVersement(BigDecimal montantCompenseOrdreVersement) {
        this.montantCompenseOrdreVersement = montantCompenseOrdreVersement;
    }

    public void setMontantDetteOrdreVersement(BigDecimal montantDetteOrdreVersement) {
        this.montantDetteOrdreVersement = montantDetteOrdreVersement;
    }

    public void setMontantRenteVerseeATort(BigDecimal montantRenteVerseeATort) {
        this.montantRenteVerseeATort = montantRenteVerseeATort;
    }

    public void setTypeOrdreVersement(TypeOrdreVersement typeOrdreVersement) {
        this.typeOrdreVersement = typeOrdreVersement;
    }

    public void setTypeRenteVerseeATort(TypeRenteVerseeATort typeRenteVerseeATort) {
        this.typeRenteVerseeATort = typeRenteVerseeATort;
    }
}
