package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.ArrayList;
import ch.globaz.perseus.business.models.demande.Demande;

/**
 * 
 * @author MBO
 * 
 */

public class Decision extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Demande demande = null;
    private ArrayList<AnnexeDecision> listAnnexes = null;
    private ArrayList<CopieDecision> listCopies = null;
    private SimpleDecision simpleDecision = null;

    public Decision() {
        super();
        simpleDecision = new SimpleDecision();
        demande = new Demande();
        listAnnexes = new ArrayList<AnnexeDecision>();
        listCopies = new ArrayList<CopieDecision>();
    }

    public void addToListAnnexes(AnnexeDecision annexe) {
        listAnnexes.add(annexe);
    }

    public void addToListCopies(CopieDecision copie) {
        listCopies.add(copie);
    }

    public Demande getDemande() {
        return demande;
    }

    @Override
    public String getId() {
        return simpleDecision.getId();

    }

    public ArrayList<AnnexeDecision> getListAnnexes() {
        return listAnnexes;
    }

    public ArrayList<CopieDecision> getListCopies() {
        return listCopies;
    }

    public SimpleDecision getSimpleDecision() {
        return simpleDecision;
    }

    @Override
    public String getSpy() {
        return simpleDecision.getSpy();

    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    @Override
    public void setId(String id) {
        simpleDecision.setId(id);

    }

    public void setListAnnexes(ArrayList<AnnexeDecision> listAnnexes) {
        this.listAnnexes = listAnnexes;
    }

    public void setListCopies(ArrayList<CopieDecision> listCopies) {
        this.listCopies = listCopies;
    }

    public void setSimpleDecision(SimpleDecision simpleDecision) {
        this.simpleDecision = simpleDecision;
    }

    @Override
    public void setSpy(String spy) {
        simpleDecision.setSpy(spy);

    }

}
