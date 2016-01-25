package globaz.corvus.db.echeances;

import java.util.HashSet;
import java.util.Set;
import ch.globaz.corvus.business.models.echeances.IRERelationEcheances;
import ch.globaz.corvus.business.models.echeances.IRERenteEcheances;

/**
 * Conteneur de données pour une relation conjugale d'un tiers, dans le cadre des échéances
 * 
 * @author PBA
 */
public class RERelationEcheances implements IRERelationEcheances {

    private String csSexeConjoint;
    private String csTypeRelation;
    private String dateDebut;
    private String dateDecesConjoint;
    private String dateFin;
    private String dateNaissanceConjoint;
    private String idRelation;
    private String idTiersConjoint;
    private String nomConjoint;
    private String prenomConjoint;
    private Set<IRERenteEcheances> rentesDuConjoint;

    public RERelationEcheances() {
        csSexeConjoint = "";
        csTypeRelation = "";
        dateDebut = "";
        dateDecesConjoint = "";
        dateFin = "";
        dateNaissanceConjoint = "";
        idRelation = "";
        idTiersConjoint = "";
        nomConjoint = "";
        prenomConjoint = "";
        rentesDuConjoint = new HashSet<IRERenteEcheances>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RERelationEcheances) {
            return ((IRERelationEcheances) obj).getIdRelation().equals(getIdRelation());
        }
        return false;
    }

    @Override
    public String getCsSexeConjoint() {
        return csSexeConjoint;
    }

    @Override
    public String getCsTypeRelation() {
        return csTypeRelation;
    }

    @Override
    public String getDateDebut() {
        return dateDebut;
    }

    @Override
    public String getDateDecesConjoint() {
        return dateDecesConjoint;
    }

    @Override
    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getDateNaissanceConjoint() {
        return dateNaissanceConjoint;
    }

    @Override
    public String getIdRelation() {
        return idRelation;
    }

    @Override
    public String getIdTiersConjoint() {
        return idTiersConjoint;
    }

    @Override
    public String getNomConjoint() {
        return nomConjoint;
    }

    @Override
    public String getPrenomConjoint() {
        return prenomConjoint;
    }

    @Override
    public Set<IRERenteEcheances> getRentesDuConjoint() {
        return rentesDuConjoint;
    }

    @Override
    public int hashCode() {
        return (this.getClass().getName() + " (" + idTiersConjoint + " - " + idRelation + ")").hashCode();
    }

    public void setCsSexeConjoint(String csSexeConjoint) {
        this.csSexeConjoint = csSexeConjoint;
    }

    public void setCsTypeRelation(String csTypeRelation) {
        this.csTypeRelation = csTypeRelation;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateDecesConjoint(String dateDecesConjoint) {
        this.dateDecesConjoint = dateDecesConjoint;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setDateNaissanceConjoint(String dateNaissanceConjoint) {
        this.dateNaissanceConjoint = dateNaissanceConjoint;
    }

    public void setIdRelation(String idRelation) {
        this.idRelation = idRelation;
    }

    public void setIdTiersConjoint(String idTiersConjoint) {
        this.idTiersConjoint = idTiersConjoint;
    }

    public void setNomConjoint(String nomConjoint) {
        this.nomConjoint = nomConjoint;
    }

    public void setPrenomConjoint(String prenomConjoint) {
        this.prenomConjoint = prenomConjoint;
    }

    public void setRentesDuConjoint(Set<IRERenteEcheances> rentesDuConjoint) {
        this.rentesDuConjoint = rentesDuConjoint;
    }
}
