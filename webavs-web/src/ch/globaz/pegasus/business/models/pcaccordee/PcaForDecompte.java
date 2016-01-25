package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;

public class PcaForDecompte extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String etatPC;
    private String montantPCMensuelle;
    private SimpleInformationsComptabilite simpleInformationsComptabilite = null;
    private SimpleInformationsComptabilite simpleInformationsComptabiliteConjoint = null;
    private SimplePCAccordee simplePCAccordee = null;

    private SimplePrestationsAccordees simplePrestationsAccordees = null;
    private SimplePrestationsAccordees simplePrestationsAccordeesConjoint = null;

    public PcaForDecompte() {
        super();
        simplePCAccordee = new SimplePCAccordee();
        simplePrestationsAccordees = new SimplePrestationsAccordees();
        simplePrestationsAccordeesConjoint = new SimplePrestationsAccordees();
        simpleInformationsComptabiliteConjoint = new SimpleInformationsComptabilite();
        simpleInformationsComptabilite = new SimpleInformationsComptabilite();
    }

    public String getEtatPC() {
        return etatPC;
    }

    @Override
    public String getId() {
        return simplePCAccordee.getId();
    }

    public String getMontantPCMensuelle() {
        return montantPCMensuelle;
    }

    public SimpleInformationsComptabilite getSimpleInformationsComptabilite() {
        return simpleInformationsComptabilite;
    }

    public SimpleInformationsComptabilite getSimpleInformationsComptabiliteConjoint() {
        return simpleInformationsComptabiliteConjoint;
    }

    public SimplePCAccordee getSimplePCAccordee() {
        return simplePCAccordee;
    }

    public SimplePrestationsAccordees getSimplePrestationsAccordees() {
        return simplePrestationsAccordees;
    }

    public SimplePrestationsAccordees getSimplePrestationsAccordeesConjoint() {
        return simplePrestationsAccordeesConjoint;
    }

    @Override
    public String getSpy() {
        return simplePCAccordee.getSpy();
    }

    public void setEtatPC(String etatPC) {
        this.etatPC = etatPC;
    }

    @Override
    public void setId(String id) {
        simplePCAccordee.setId(id);
    }

    public void setMontantPCMensuelle(String montantPCMensuelle) {
        this.montantPCMensuelle = montantPCMensuelle;
    }

    public void setSimpleInformationsComptabilite(SimpleInformationsComptabilite simpleInformationsComptabilite) {
        this.simpleInformationsComptabilite = simpleInformationsComptabilite;
    }

    public void setSimpleInformationsComptabiliteConjoint(
            SimpleInformationsComptabilite simpleInformationsComptabiliteConjoint) {
        this.simpleInformationsComptabiliteConjoint = simpleInformationsComptabiliteConjoint;
    }

    public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
        this.simplePCAccordee = simplePCAccordee;
    }

    public void setSimplePrestationsAccordees(SimplePrestationsAccordees simplePrestationsAccordees) {
        this.simplePrestationsAccordees = simplePrestationsAccordees;
    }

    public void setSimplePrestationsAccordeesConjoint(SimplePrestationsAccordees simplePrestationsAccordeesConjoint) {
        this.simplePrestationsAccordeesConjoint = simplePrestationsAccordeesConjoint;
    }

    @Override
    public void setSpy(String spy) {
        simplePCAccordee.setSpy(spy);
    }

    @Override
    public String toString() {
        return "PcaForDecompte [etatPC=" + etatPC + ", montantPCMensuelle=" + montantPCMensuelle + ", dateDebut="
                + simplePCAccordee.getDateDebut() + ", dateFin=" + simplePCAccordee.getDateFin() + "]";
    }
}
