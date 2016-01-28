package ch.globaz.perseus.business.models.echeance;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.perseus.business.models.dossier.Dossier;

public class EcheanceLibre extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Dossier dossier = null;
    private SimpleEcheanceLibre simpleEcheanceLibre = null;

    public EcheanceLibre() {
        super();
        dossier = new Dossier();
        simpleEcheanceLibre = new SimpleEcheanceLibre();
    }

    public Dossier getDossier() {
        return dossier;
    }

    @Override
    public String getId() {
        return simpleEcheanceLibre.getId();
    }

    public SimpleEcheanceLibre getSimpleEcheanceLibre() {
        return simpleEcheanceLibre;
    }

    @Override
    public String getSpy() {
        return simpleEcheanceLibre.getSpy();
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    @Override
    public void setId(String id) {
        simpleEcheanceLibre.setId(id);
    }

    public void setSimpleEcheanceLibre(SimpleEcheanceLibre simpleEcheanceLibre) {
        this.simpleEcheanceLibre = simpleEcheanceLibre;
    }

    @Override
    public void setSpy(String spy) {
        simpleEcheanceLibre.setSpy(spy);
    }

}
