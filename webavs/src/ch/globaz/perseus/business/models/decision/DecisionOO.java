/**
 * 
 */
package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.models.creancier.Creancier;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.pcfaccordee.SimplePCFAccordee;
import ch.globaz.perseus.business.models.situationfamille.Enfant;

/**
 * @author MBO
 * 
 */
public class DecisionOO extends JadeComplexModel {

    // private Creancier creancier = null;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String CS_DECISION_TYPE_LOT = CSTypeLot.LOT_DECISION.getCodeSystem();

    private List<Creancier> creancier = null;

    private Demande demande = null;
    private List<AnnexeDecision> listeAnnexe = null;
    private List<CopieDecision> listeCopie = null;
    private List<Enfant> listeEnfants = null;
    private SimplePCFAccordee pcfAccordee = null;
    private Prestation prestation = null;

    private SimpleDecision simpleDecision = null;

    public DecisionOO() {
        super();
        // this.creancier = new Creancier();
        simpleDecision = new SimpleDecision();
        demande = new Demande();
        listeEnfants = new ArrayList<Enfant>();
        pcfAccordee = new SimplePCFAccordee();
        prestation = new Prestation();
        // this.listeCreancier = new ArrayList<Creancier>();
        creancier = new ArrayList<Creancier>();
        listeAnnexe = new ArrayList<AnnexeDecision>();
        listeCopie = new ArrayList<CopieDecision>();
    }

    /**
     * @return the creancier
     */
    public List<Creancier> getCreancier() {
        return creancier;
    }

    public String getCsDecisionTypeLot() {
        return DecisionOO.CS_DECISION_TYPE_LOT;
    }

    public Demande getDemande() {
        return demande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleDecision.getId();
    }

    public List<AnnexeDecision> getListeAnnexe() {
        return listeAnnexe;
    }

    public List<CopieDecision> getListeCopie() {
        return listeCopie;
    }

    public List<Enfant> getListeEnfants() {
        return listeEnfants;
    }

    public SimplePCFAccordee getPcfAccordee() {
        return pcfAccordee;
    }

    public Prestation getPrestation() {
        return prestation;
    }

    public SimpleDecision getSimpleDecision() {
        return simpleDecision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleDecision.getSpy();
    }

    /**
     * @param creancier
     *            the creancier to set
     */
    public void setCreancier(List<Creancier> creancier) {
        this.creancier = creancier;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleDecision.setId(id);

    }

    public void setListeAnnexe(List<AnnexeDecision> listeAnnexe) {
        this.listeAnnexe = listeAnnexe;
    }

    public void setListeCopie(List<CopieDecision> listeCopie) {
        this.listeCopie = listeCopie;
    }

    public void setListeEnfants(List<Enfant> listeEnfants) {
        this.listeEnfants = listeEnfants;
    }

    public void setPcfAccordee(SimplePCFAccordee pcfAccordee) {
        this.pcfAccordee = pcfAccordee;
    }

    public void setPrestation(Prestation prestation) {
        this.prestation = prestation;
    }

    public void setSimpleDecision(SimpleDecision simpleDecision) {
        this.simpleDecision = simpleDecision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleDecision.setSpy(spy);
    }

}
