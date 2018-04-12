package ch.globaz.orion.db;

import globaz.globall.db.BSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import ch.globaz.common.jadedb.JadeEntity;
import ch.globaz.common.jadedb.TableDefinition;

public class EBDemandeModifAcompteEntity extends JadeEntity {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String idDemandePortail;
    private String idAffiliation;
    private String numAffilie;
    private Integer annee;
    private BigDecimal revenu;
    private BigDecimal capital;
    private String csStatut;
    private String remarque;
    private String remarqueCp;
    private Date dateReception;
    private String idDecision;

    @Override
    protected void writeProperties() {
        writeStringAsNumeric(EBDemandeModifAcompteDefTable.ID_DEMANDE_PORTAIL, idDemandePortail);
        writeStringAsNumeric(EBDemandeModifAcompteDefTable.ID_AFFILIATION, idAffiliation);
        write(EBDemandeModifAcompteDefTable.NUM_AFFILIE, numAffilie);
        write(EBDemandeModifAcompteDefTable.ANNEE, annee);
        write(EBDemandeModifAcompteDefTable.REVENU, revenu);
        write(EBDemandeModifAcompteDefTable.CAPITAL, capital);
        writeStringAsNumeric(EBDemandeModifAcompteDefTable.CS_STATUT, csStatut);
        write(EBDemandeModifAcompteDefTable.REMARQUE, remarque);
        write(EBDemandeModifAcompteDefTable.DATE_RECEPTION, dateReception);
        writeStringAsNumeric(EBDemandeModifAcompteDefTable.ID_DECISION_REF, idDecision);
        write(EBDemandeModifAcompteDefTable.REMARQUE_CP, remarqueCp);
    }

    @Override
    protected void readProperties() {
        id = this.read(EBDemandeModifAcompteDefTable.ID);
        idDemandePortail = this.readString(EBDemandeModifAcompteDefTable.ID_DEMANDE_PORTAIL);
        idAffiliation = this.readString(EBDemandeModifAcompteDefTable.ID_AFFILIATION);
        numAffilie = read(EBDemandeModifAcompteDefTable.NUM_AFFILIE);
        annee = read(EBDemandeModifAcompteDefTable.ANNEE);
        revenu = read(EBDemandeModifAcompteDefTable.REVENU);
        capital = read(EBDemandeModifAcompteDefTable.CAPITAL);
        csStatut = readString(EBDemandeModifAcompteDefTable.CS_STATUT);
        remarque = read(EBDemandeModifAcompteDefTable.REMARQUE);
        dateReception = read(EBDemandeModifAcompteDefTable.DATE_RECEPTION);
        idDecision = readString(EBDemandeModifAcompteDefTable.ID_DECISION_REF);
        remarqueCp = readString(EBDemandeModifAcompteDefTable.REMARQUE_CP);
    }

    public static int returnNbDemandeInstatusForIdAffiliation(BSession session, String idAffiliation,
            List<String> listStatut) throws Exception {

        EBDemandeModifAcompteJoinDecisionManager demandeEncours = new EBDemandeModifAcompteJoinDecisionManager();
        demandeEncours.setSession(session);
        demandeEncours.setForIdAffiliation(idAffiliation);
        demandeEncours.setInStatut(listStatut);

        return demandeEncours.getCount();
    }

    @Override
    protected Class<? extends TableDefinition> getTableDef() {
        return EBDemandeModifAcompteDefTable.class;
    }

    @Override
    public String getIdEntity() {
        return String.valueOf(id);
    }

    @Override
    public void setIdEntity(String id) {
        this.id = Integer.valueOf(id);
    }

    public String getIdDemandePortail() {
        return idDemandePortail;
    }

    public void setIdDemandePortail(String idDemandePortail) {
        this.idDemandePortail = idDemandePortail;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public BigDecimal getRevenu() {
        return revenu;
    }

    public void setRevenu(BigDecimal revenu) {
        this.revenu = revenu;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public String getCsStatut() {
        return csStatut;
    }

    public void setCsStatut(String csStatut) {
        this.csStatut = csStatut;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Date getDateReception() {
        return dateReception;
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public String getRemarqueCp() {
        return remarqueCp;
    }

    public void setRemarqueCp(String remarqueCp) {
        this.remarqueCp = remarqueCp;
    }

    @Override
    public boolean hasCreationSpy() {
        return true;
    }

}
