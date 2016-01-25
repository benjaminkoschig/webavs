/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.calcul.strategie;

/**
 * 
 * @author ECO
 * 
 */
public abstract class StrategieCalculFactory {

    public abstract StrategieCalcul getStrategie(String csTypeDonneeFinanciere, String dateValidite);

}
