package ch.globaz.perseus.businessimpl.services;

/**
 * @author vyj
 */
public class PerseusAbstractServiceImpl {

    public Float roundFloat(Float f) {
        return new Float(Math.round(f));
    }

    public Float roundFloat(String f) {
        return new Float(Math.round(Float.parseFloat(f)));
    }

}
