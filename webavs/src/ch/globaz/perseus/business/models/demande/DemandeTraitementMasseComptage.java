package ch.globaz.perseus.business.models.demande;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.jade.process.business.models.property.SimplePropriete;

/**
 * Classe pour les demandes de prestations complémentaires familles
 * 
 * @author RCO
 * 
 */
public class DemandeTraitementMasseComptage extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // linked model
    private SimplePropriete simplePropriete = null;

    // select-fields
    private String keyProperty = null;

    @Override
    public String toString() {
        return "DemandeTraitementMasseComptage [keyProperty = " + keyProperty + "]";
    }

    public String getKeyProperty() {
        return keyProperty;
    }

    public void setIdDecision(String keyProperty) {
        this.keyProperty = keyProperty;
    }

    public DemandeTraitementMasseComptage() {
        super();
        simplePropriete = new SimplePropriete();
    }

    @Override
    public String getId() {
        return simplePropriete.getId();
    }

    @Override
    public String getSpy() {
        return simplePropriete.getSpy();
    }

    @Override
    public void setId(String id) {
        simplePropriete.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        simplePropriete.setSpy(spy);
    }

    public SimplePropriete getSimplePropriete() {
        return simplePropriete;
    }

    public void setSimplePropriete(SimplePropriete simplePropriete) {
        this.simplePropriete = simplePropriete;
    }

}
