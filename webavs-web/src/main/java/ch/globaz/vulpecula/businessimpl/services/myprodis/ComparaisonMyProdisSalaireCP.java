package ch.globaz.vulpecula.businessimpl.services.myprodis;

import globaz.globall.db.BSession;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import ch.globaz.common.sql.QueryExecutor;

public class ComparaisonMyProdisSalaireCP implements Comparable<ComparaisonMyProdisSalaireCP> {

    private String id;
    @NotNull
    @NotBlank
    @NotEmpty
    private String noAffilie;
    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "756(.)[0-9]{4}(.)[0-9]{4}(.)[0-9]{2}")
    private String nss;
    @NotNull
    @NotBlank
    @NotEmpty
    private String dateDebut;
    @NotNull
    @NotBlank
    @NotEmpty
    private String dateFin;
    @NotNull
    @NotBlank
    @NotEmpty
    private String salaire;
    @NotNull
    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 1)
    private String cp;

    private String dateVersement;

    private boolean isDeduction;

    private String tauxCp;
    private boolean mustBeAnnonced;
    private boolean mustBeAnnoncedCP;
    private String source;
    private String dateFinActivite;
    private String dateDebutPoste;
    private String montantNet;

    public String getNoAffilie() {
        return noAffilie;
    }

    public void setNoAffilie(String noAffilie) {
        this.noAffilie = noAffilie;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getSalaire() {
        return salaire;
    }

    public void setSalaire(String salaire) {
        this.salaire = salaire;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Méthode qui définit s'il y a des absences dans la ligne de salaires
     * 
     * @param session
     * @return vrai s'il y a des absences
     */
    private boolean hasAbsence(BSession session) {
        BigDecimal nbAbsence = QueryExecutor.executeAggregate(
                "SELECT count(*) FROM schema.PT_ABSENCES where ID_PT_DECOMPTE_LIGNES = " + id, session);
        ComparaisonMyProdisService service = new ComparaisonMyProdisService();
        return service.hasNbSup0(nbAbsence);

    }

    /**
     * Méthode qui définit s'il y a des cotis LPP pour la ligne de salaire
     * 
     * @param session
     * @return vrai s'il y a des cotisations
     */
    private Boolean hasLpp(BSession session) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(*) FROM schema.PT_COTISATION_DECOMPTES cotdec ");
        sql.append("join schema.afcotip cot on cotdec.ID_AFCOTIP = cot.meicot ");
        sql.append("join schema.afassup ass on ass.mbiass = cot.mbiass ");
        sql.append(" where MBTTYP = 68904004 and ID_PT_DECOMPTE_LIGNES = " + id);
        BigDecimal nbCotiLpp = QueryExecutor.executeAggregate(sql.toString(), session);
        ComparaisonMyProdisService service = new ComparaisonMyProdisService();
        return service.hasNbSup0(nbCotiLpp);
    }

    /**
     * Méthode qui définit s'il y a de la LPP dans la ligne de CP
     * 
     * @param session
     * @return vrai s'il y a des absences
     */
    public void hasLppCP(BSession session) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(*) FROM SCHEMA.PT_TAUX_CONGES_PAYES cp ");
        sql.append("join SCHEMA.afassup ass on ass.MBIASS = cp.ID_AFASSUP ");
        sql.append("where MBTTYP = 68904004 and ID_PT_CONGES_PAYES = " + id);
        BigDecimal nbCotiLpp = QueryExecutor.executeAggregate(sql.toString(), session);
        ComparaisonMyProdisService service = new ComparaisonMyProdisService();
        mustBeAnnoncedCP = service.hasNbSup0(nbCotiLpp);
    }

    /**
     * Méthode qui met à jour le montant brut pour les CP
     */
    public void determineMontantBrut() {

        BigDecimal montant = new BigDecimal(salaire).multiply(new BigDecimal(tauxCp)).divide(new BigDecimal(100));
        BigDecimal increment = new BigDecimal("0.05");
        montant = montant.divide(increment, 0, BigDecimal.ROUND_HALF_EVEN);
        montant = montant.multiply(increment);

        isDeduction = montant.compareTo(new BigDecimal(montantNet)) < 0;
        salaire = montant.setScale(2).toString();
    }

    /**
     * Méthode qui met à jour les périodes en appelant les services.
     */
    public void miseAjourPeriode() {
        ComparaisonMyProdisService service = new ComparaisonMyProdisService();
        dateDebut = service.transformeDateDebut(dateVersement, dateDebutPoste, dateFinActivite);

        dateFin = service.transformeDateFin(dateVersement, dateFinActivite);

    }

    public void setMustBeAnnonced(BSession session) {
        mustBeAnnonced = hasLpp(session);
    }

    public boolean mustBeAnnonced() {
        return mustBeAnnonced;
    }

    public boolean isMustBeAnnoncedCP() {
        return mustBeAnnoncedCP;
    }

    public void setMustBeAnnoncedCP(boolean mustBeAnnoncedCP) {
        this.mustBeAnnoncedCP = mustBeAnnoncedCP;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "Nss : " + nss + " No Affilie : " + noAffilie + " Periode : " + dateDebut + " - " + dateFin
                + " Salaire : " + salaire + " CP : " + cp + " Source : " + source + " Date Fin : " + dateFinActivite;
    }

    public String getTauxCp() {
        return tauxCp;
    }

    public void setTauxCp(String tauxCp) {
        this.tauxCp = tauxCp;
    }

    public String getDateVersement() {
        return dateVersement;
    }

    public void setDateVersement(String dateVersement) {
        this.dateVersement = dateVersement;
    }

    public boolean isDeduction() {
        return isDeduction;
    }

    public void setDeduction(boolean isDeduction) {
        this.isDeduction = isDeduction;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDateFinActivite() {
        return dateFinActivite;
    }

    public void setDateFinActivite(String dateFinActivite) {
        this.dateFinActivite = dateFinActivite;
    }

    public String getDateDebutPoste() {
        return dateDebutPoste;
    }

    public void setDateDebutPoste(String dateDebutPoste) {
        this.dateDebutPoste = dateDebutPoste;
    }

    public String getMontantNet() {
        return montantNet;
    }

    public void setMontantNet(String montantNet) {
        this.montantNet = montantNet;
    }

    @Override
    public int compareTo(ComparaisonMyProdisSalaireCP o) {
        int result = noAffilie.compareTo(o.getNoAffilie());
        if (result == 0) {
            result = nss.compareTo(o.getNss());
        }
        return result;
    }

}
