/**
 * 
 */
package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * @author eco
 * 
 */
public class CommunicationOCC extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleCommunicationOCC simpleCommunicationOCC = null;
    private LocaliteSimpleModel localite = null;
    private PersonneEtendueComplexModel tiers = null;
    private PersonneEtendueComplexModel tiersRequerant = null;
    private SimpleVersionDroit simpleVersionDroit = new SimpleVersionDroit();
    private SimpleDonneesPersonnelles donneePersonnelle = new SimpleDonneesPersonnelles();
    private SimpleDonneesPersonnelles donneePersonnelleRquerant = new SimpleDonneesPersonnelles();

    public CommunicationOCC() {
        super();
        simpleCommunicationOCC = new SimpleCommunicationOCC();
        tiers = new PersonneEtendueComplexModel();
        tiersRequerant = new PersonneEtendueComplexModel();
        setLocalite(new LocaliteSimpleModel());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleCommunicationOCC.getId();
    }

    public SimpleCommunicationOCC getSimpleCommunicationOCC() {
        return simpleCommunicationOCC;
    }

    // public SimpleDecisionHeader getSimpleDecisionHeader() {
    // return simpleDecisionHeader;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleCommunicationOCC.getSpy();
    }

    public PersonneEtendueComplexModel getTiers() {
        return tiers;
    }

    public PersonneEtendueComplexModel getTiersRequerant() {
        return tiersRequerant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleCommunicationOCC.setId(id);
    }

    public void setSimpleCommunicationOCC(SimpleCommunicationOCC simpleCommunicationOCC) {
        this.simpleCommunicationOCC = simpleCommunicationOCC;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleCommunicationOCC.setSpy(spy);
    }

    public void setTiers(PersonneEtendueComplexModel tiers) {
        this.tiers = tiers;
    }

    public void setTiersRequerant(PersonneEtendueComplexModel tiersRequerant) {
        this.tiersRequerant = tiersRequerant;
    }

    public LocaliteSimpleModel getLocalite() {
        return localite;
    }

    public void setLocalite(LocaliteSimpleModel localite) {
        this.localite = localite;
    }

    public SimpleVersionDroit getSimpleVersionDroit() {
        return simpleVersionDroit;
    }

    public void setSimpleVersionDroit(SimpleVersionDroit simpleVersionDroit) {
        this.simpleVersionDroit = simpleVersionDroit;
    }

    public SimpleDonneesPersonnelles getDonneePersonnelle() {
        return donneePersonnelle;
    }

    public void setDonneePersonnelle(SimpleDonneesPersonnelles donneePersonnelle) {
        this.donneePersonnelle = donneePersonnelle;
    }

    public SimpleDonneesPersonnelles getDonneePersonnelleRquerant() {
        return donneePersonnelleRquerant;
    }

    public void setDonneePersonnelleRquerant(SimpleDonneesPersonnelles donneePersonnelleRquerant) {
        this.donneePersonnelleRquerant = donneePersonnelleRquerant;
    }

}
