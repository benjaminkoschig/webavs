package globaz.aquila.jsp.taglib;

/**
 * Affiche une liste d'options sous forme de "RADIO"
 * 
 * @author Pascal Lovy, 18-oct-2004
 */
public class COListRadioTag extends COListTag {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** L'orientation du contrôle */
    private String orientation = "V";

    /**
     * @see globaz.jsp.taglib.FWList#drawTag()
     */
    @Override
    public void drawTag() throws java.io.IOException {
        drawRadio(orientation);
    }

    /**
     * @return La valeur courante de la propriété
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * @param string
     *            La nouvelle valeur de la propriété
     */
    public void setOrientation(String string) {
        orientation = string;
    }

}
