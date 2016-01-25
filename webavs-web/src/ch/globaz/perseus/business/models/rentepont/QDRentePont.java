/**
 * 
 */
package ch.globaz.perseus.business.models.rentepont;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.perseus.business.models.dossier.Dossier;

/**
 * @author JSI
 * 
 */
public class QDRentePont extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Dossier dossier = null;
    private SimpleQDRentePont simpleQDRentePont = null;

    /**
	 * 
	 */
    public QDRentePont() {
        super();
        dossier = new Dossier();
        simpleQDRentePont = new SimpleQDRentePont();
    }

    public Dossier getDossier() {
        return dossier;
    }

    @Override
    public String getId() {
        return simpleQDRentePont.getId();
    }

    public SimpleQDRentePont getSimpleQDRentePont() {
        return simpleQDRentePont;
    }

    @Override
    public String getSpy() {
        return simpleQDRentePont.getSpy();
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    @Override
    public void setId(String id) {
        simpleQDRentePont.setId(id);
    }

    public void setSimpleQDRentePont(SimpleQDRentePont simpleQDRentePont) {
        this.simpleQDRentePont = simpleQDRentePont;
    }

    @Override
    public void setSpy(String spy) {
        simpleQDRentePont.setSpy(spy);
    }

}
