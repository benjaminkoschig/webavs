package ch.globaz.perseus.business.models.qd;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * Définition des éléments qui composent un object Facture
 * 
 * @author JSI
 * 
 */
public class Facture extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private QD qd = null;
    private SimpleFacture simpleFacture = null;

    public Facture() {
        super();
        simpleFacture = new SimpleFacture();
        qd = new QD();
    }

    @Override
    public String getId() {
        return simpleFacture.getId();
    }

    /**
     * @return the qd
     */
    public QD getQd() {
        return qd;
    }

    /**
     * @return the simpleFacture
     */
    public SimpleFacture getSimpleFacture() {
        return simpleFacture;
    }

    @Override
    public String getSpy() {
        return simpleFacture.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleFacture.setId(id);
    }

    /**
     * @param qd
     *            the qd to set
     */
    public void setQd(QD qd) {
        this.qd = qd;
    }

    /**
     * @param simpleFacture
     *            the simpleFacture to set
     */
    public void setSimpleFacture(SimpleFacture simpleFacture) {
        this.simpleFacture = simpleFacture;
    }

    @Override
    public void setSpy(String spy) {
        simpleFacture.setSpy(spy);
    }

}
