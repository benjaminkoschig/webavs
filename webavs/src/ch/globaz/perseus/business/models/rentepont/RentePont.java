package ch.globaz.perseus.business.models.rentepont;

import globaz.globall.db.BSpy;
import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.situationfamille.SituationFamiliale;

/**
 * Classe pour les demandes de prestations complémentaires familles
 * 
 * @author DDE
 * 
 */
public class RentePont extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Dossier dossier = null;
    private SimpleRentePont simpleRentePont = null;
    private SituationFamiliale situationFamiliale = null;

    public RentePont() {
        super();
        simpleRentePont = new SimpleRentePont();
        dossier = new Dossier();
        situationFamiliale = new SituationFamiliale();
    }

    public String getCreationDate() {
        return (new BSpy(simpleRentePont.getCreationSpy())).getDate();
    }

    /**
     * @return the dossier
     */
    public Dossier getDossier() {
        return dossier;
    }

    @Override
    public String getId() {
        return simpleRentePont.getId();
    }

    /**
     * @return the simpleRentePont
     */
    public SimpleRentePont getSimpleRentePont() {
        return simpleRentePont;
    }

    /**
     * @return the situationFamiliale
     */
    public SituationFamiliale getSituationFamiliale() {
        return situationFamiliale;
    }

    @Override
    public String getSpy() {
        return simpleRentePont.getSpy();
    }

    /**
     * @param dossier
     *            the dossier to set
     */
    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    @Override
    public void setId(String id) {
        simpleRentePont.setId(id);

    }

    /**
     * @param simpleRentePont
     *            the simpleRentePont to set
     */
    public void setSimpleRentePont(SimpleRentePont simpleRentePont) {
        this.simpleRentePont = simpleRentePont;
    }

    /**
     * @param situationFamiliale
     *            the situationFamiliale to set
     */
    public void setSituationFamiliale(SituationFamiliale situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    @Override
    public void setSpy(String spy) {
        simpleRentePont.setSpy(spy);
    }

}
