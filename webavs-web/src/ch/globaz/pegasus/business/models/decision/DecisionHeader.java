/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;
import java.util.ArrayList;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * @author SCE
 * 
 *         15 juil. 2010
 */
public class DecisionHeader extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Liste contenant les annexes et copies liés aux décision
    private ArrayList<SimpleAnnexesDecision> listeAnnexes = new ArrayList();
    private ArrayList<CopiesDecision> listeCopies = new ArrayList();
    private PersonneEtendueComplexModel personneEtendue = null;
    private SimpleDecisionHeader simpleDecisionHeader = null;

    /**
     * Constructeur
     */
    public DecisionHeader() {
        super();
        simpleDecisionHeader = new SimpleDecisionHeader();
        personneEtendue = new PersonneEtendueComplexModel();
    }

    /**
     * Ajout d'un élément dans la liste des copies de décision
     * 
     * @param copiesDecision
     */
    public void addToListCopies(CopiesDecision copiesDecision) {
        listeCopies.add(copiesDecision);
    }

    /**
     * Ajout d'un élément dans la liste des annexes
     * 
     * @param annexesDecision
     */
    public void addToListeAnnexes(SimpleAnnexesDecision annexesDecision) {
        listeAnnexes.add(annexesDecision);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleDecisionHeader.getId();
    }

    /**
     * @return the listeAnnexes
     */
    public ArrayList<SimpleAnnexesDecision> getListeAnnexes() {
        return listeAnnexes;
    }

    /**
     * @return the listeCopies
     */
    public ArrayList<CopiesDecision> getListeCopies() {
        return listeCopies;
    }

    /**
     * @return the personneEtendue
     */
    public PersonneEtendueComplexModel getPersonneEtendue() {
        return personneEtendue;
    }

    /**
     * @return the simpleDecisionHeader
     */
    public SimpleDecisionHeader getSimpleDecisionHeader() {
        return simpleDecisionHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleDecisionHeader.getSpy();
    }

    /**
     * Suppression d'un élément dans la liste
     * 
     * @TODO
     */
    public void removeFromListeAnnexes() {

    }

    /**
     * Suppression d'un élément de la liste des copies
     */
    public void removeFromListeCopies() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleDecisionHeader.setId(id);

    }

    /**
     * @param listeAnnexes
     *            the listeAnnexes to set
     */
    public void setListeAnnexes(ArrayList<SimpleAnnexesDecision> listeAnnexes) {
        this.listeAnnexes = listeAnnexes;
    }

    /**
     * @param listeCopies
     *            the listeCopies to set
     */
    public void setListeCopies(ArrayList<CopiesDecision> listeCopies) {
        this.listeCopies = listeCopies;
    }

    /**
     * @param personneEtendue
     *            the personneEtendue to set
     */
    public void setPersonneEtendue(PersonneEtendueComplexModel personneEtendue) {
        this.personneEtendue = personneEtendue;
    }

    /**
     * @param simpleDecisionHeader
     *            the simpleDecisionHeader to set
     */
    public void setSimpleDecisionHeader(SimpleDecisionHeader simpleDecisionHeader) {
        this.simpleDecisionHeader = simpleDecisionHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleDecisionHeader.setSpy(spy);

    }

}
