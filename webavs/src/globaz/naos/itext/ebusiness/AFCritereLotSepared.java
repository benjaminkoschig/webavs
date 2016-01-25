/**
 * 
 */
package globaz.naos.itext.ebusiness;

/**
 * Enum des criteres peremettant de déterminé si une lettre d'inscription doit être mise dans le lot à vérifier ou non. <br/>
 * Description des criteres :
 * <ul>
 * <li><strong>1 : </strong>Test le champ remplacer par "Designation 1"</li>
 * <li><strong>2 : </strong>Test le champ remplacer par "Designation 2"</li>
 * <li><strong>3 : </strong>Test le champ remplacer par "Designation 3"</li>
 * <li><strong>4 : </strong>Test le champ remplacer par "Designation 4"</li>
 * <li><strong>5 : </strong>Test le champ "Nom (suite) 1"</li>
 * <li><strong>6 : </strong>Test le champ "Nom (suite) 2"</li>
 * <li><strong>7 : </strong>Test le champ "à l'attention de"</li>
 * </ul>
 * 
 * @author sel
 * 
 */
public enum AFCritereLotSepared {
    attention("7"),
    remplaceAdresseDes1("1"),
    remplaceAdresseDes2("2"),
    remplaceAdresseDes3("3"),
    remplaceAdresseDes4("4"),
    tiersDes3("5"),
    tiersDes4("6");

    protected String label;

    /** Constructeur */
    AFCritereLotSepared(String pLabel) {
        label = pLabel;
    }

    public String getLabel() {
        return label;
    }
}
