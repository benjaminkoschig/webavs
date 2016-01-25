package ch.globaz.corvus.business.models.rentesaccordees;

import globaz.globall.db.BConstants;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.echeances.REMotifEcheance;
import ch.globaz.hera.business.models.famille.SimpleMembreFamille;
import ch.globaz.hera.business.models.famille.SimplePeriode;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class RERenteJoinPeriode extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleMembreFamille membreFamille;
    private REMotifEcheance motif;
    private SimplePeriode periode;
    private PersonneSimpleModel personne;
    private SimplePrestationsAccordees prestationAccordee;
    private SimpleRenteAccordee renteAccordee;
    private TiersSimpleModel tiers;

    public RERenteJoinPeriode() {
        super();

        membreFamille = new SimpleMembreFamille();
        periode = new SimplePeriode();
        personne = new PersonneSimpleModel();
        prestationAccordee = new SimplePrestationsAccordees();
        renteAccordee = new SimpleRenteAccordee();
        tiers = new TiersSimpleModel();
        motif = null;
    }

    public String getCodeCasSpeciaux1() {
        return renteAccordee.getCodeCasSpeciaux1();
    }

    public String getCodeCasSpeciaux2() {
        return renteAccordee.getCodeCasSpeciaux2();
    }

    public String getCodeCasSpeciaux3() {
        return renteAccordee.getCodeCasSpeciaux3();
    }

    public String getCodeCasSpeciaux4() {
        return renteAccordee.getCodeCasSpeciaux4();
    }

    public String getCodeCasSpeciaux5() {
        return renteAccordee.getCodeCasSpeciaux5();
    }

    public String getCodePrestation() {
        return prestationAccordee.getCodePrestation();
    }

    public String getCsTypePeriode() {
        return periode.getType();
    }

    public String getDateDebutPeriode() {
        return periode.getDateDebut();
    }

    public String getDateDeces() {
        return personne.getDateDeces();
    }

    public String getDateEcheance() {
        return prestationAccordee.getDateEcheance();
    }

    public String getDateFinPeriode() {
        return periode.getDateFin();
    }

    public String getDateNaissance() {
        return personne.getDateNaissance();
    }

    @Override
    public String getId() {
        return prestationAccordee.getId();
    }

    public String getIdEnteteBlocage() {
        return prestationAccordee.getIdEnteteBlocage();
    }

    public String getIdMembreFamille() {
        return membreFamille.getIdMembreFamille();
    }

    public String getIdPays() {
        return tiers.getIdPays();
    }

    public String getIdPeriode() {
        return periode.getIdPeriode();
    }

    public String getIdPrestationAccordee() {
        return prestationAccordee.getIdPrestationAccordee();
    }

    public String getIdTiers() {
        return tiers.getIdTiers();
    }

    public String getIsPrestationBloquee() {
        return prestationAccordee.get_isPrestationBloquee();
    }

    public boolean getIsPrestationBloqueeBoolean() {
        return BConstants.DB_BOOLEAN_CHAR_TRUE.toString().equals(getIsPrestationBloquee());
    }

    public String getMontant() {
        return prestationAccordee.getMontantPrestation();
    }

    public REMotifEcheance getMotif() {
        return motif;
    }

    public String getNomTiers() {
        return tiers.getDesignation1();
    }

    public String getPrenomTiers() {
        return tiers.getDesignation2();
    }

    public String getSexe() {
        return personne.getSexe();
    }

    @Override
    public String getSpy() {
        return null;
    }

    public void setCodeCasSpeciaux1(String codeCasSpeciaux1) {
        renteAccordee.setCodeCasSpeciaux1(codeCasSpeciaux1);
    }

    public void setCodeCasSpeciaux2(String codeCasSpeciaux2) {
        renteAccordee.setCodeCasSpeciaux2(codeCasSpeciaux2);
    }

    public void setCodeCasSpeciaux3(String codeCasSpeciaux3) {
        renteAccordee.setCodeCasSpeciaux3(codeCasSpeciaux3);
    }

    public void setCodeCasSpeciaux4(String codeCasSpeciaux4) {
        renteAccordee.setCodeCasSpeciaux4(codeCasSpeciaux4);
    }

    public void setCodeCasSpeciaux5(String codeCasSpeciaux5) {
        renteAccordee.setCodeCasSpeciaux5(codeCasSpeciaux5);
    }

    public void setCodePrestation(String codePrestation) {
        prestationAccordee.setCodePrestation(codePrestation);
    }

    public void setCsSexe(String csSexe) {
        personne.setSexe(csSexe);
    }

    public void setCsTypePeriode(String csTypePeriode) {
        periode.setType(csTypePeriode);
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        periode.setDateDebut(dateDebutPeriode);
    }

    public void setDateDeces(String dateDeces) {
        personne.setDateDeces(dateDeces);
    }

    public void setDateEcheance(String dateEcheance) {
        prestationAccordee.setDateEcheance(dateEcheance);
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        periode.setDateFin(dateFinPeriode);
    }

    public void setDateNaissance(String dateNaissance) {
        personne.setDateNaissance(dateNaissance);
    }

    @Override
    public void setId(String id) {
        prestationAccordee.setId(id);
    }

    public void setIdEnteteBlocage(String idEnteteBlocage) {
        prestationAccordee.setIdEnteteBlocage(idEnteteBlocage);
    }

    public void setIdPays(String idPays) {
        tiers.setIdPays(idPays);
    }

    public void setIdPeriode(String idPeriode) {
        periode.setIdPeriode(idPeriode);
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        prestationAccordee.setIdPrestationAccordee(idPrestationAccordee);
    }

    public void setIdTiers(String idTiers) {
        tiers.setIdTiers(idTiers);
    }

    public void setIsPrestationBloquee(String isPrestationBloquee) {
        prestationAccordee.set_isPrestationBloquee(isPrestationBloquee);
    }

    public void setMembreFamille(SimpleMembreFamille membreFamille) {
        this.membreFamille = membreFamille;
    }

    public void setMontant(String montant) {
        prestationAccordee.setMontantPrestation(montant);
    }

    public void setMotif(REMotifEcheance motif) {
        this.motif = motif;
    }

    public void setNom(String nom) {
        tiers.setDesignation1(nom);
    }

    public void setPeriode(SimplePeriode periode) {
        this.periode = periode;
    }

    public void setPersonne(PersonneSimpleModel personne) {
        this.personne = personne;
    }

    public void setPrenom(String prenom) {
        tiers.setDesignation2(prenom);
    }

    public void setPrestationAccordee(SimplePrestationsAccordees prestationAccordee) {
        this.prestationAccordee = prestationAccordee;
    }

    public void setSexe(String csSexe) {
        personne.setSexe(csSexe);
    }

    @Override
    public void setSpy(String spy) {
    }

    public void setTiers(TiersSimpleModel tiers) {
        this.tiers = tiers;
    }
}
