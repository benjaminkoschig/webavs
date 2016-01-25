/**
 * 
 */
package ch.globaz.perseus.business.models.rentepont;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author JSI
 * 
 */
public class FactureRentePont extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    QDRentePont qdRentePont = null;
    SimpleFactureRentePont simpleFactureRentePont = null;

    /**
	 * 
	 */
    public FactureRentePont() {
        super();
        qdRentePont = new QDRentePont();
        simpleFactureRentePont = new SimpleFactureRentePont();
    }

    @Override
    public String getId() {
        return simpleFactureRentePont.getId();
    }

    public QDRentePont getQdRentePont() {
        return qdRentePont;
    }

    public SimpleFactureRentePont getSimpleFactureRentePont() {
        return simpleFactureRentePont;
    }

    @Override
    public String getSpy() {
        return simpleFactureRentePont.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleFactureRentePont.setId(id);
    }

    public void setQdRentePont(QDRentePont qdRentePont) {
        this.qdRentePont = qdRentePont;
    }

    public void setSimpleFactureRentePont(SimpleFactureRentePont simpleFactureRentePont) {
        this.simpleFactureRentePont = simpleFactureRentePont;
    }

    @Override
    public void setSpy(String spy) {
        simpleFactureRentePont.setSpy(spy);
    }

}
