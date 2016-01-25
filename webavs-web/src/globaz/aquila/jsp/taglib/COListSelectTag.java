package globaz.aquila.jsp.taglib;

import java.io.IOException;

/**
 * Affiche une liste d'options sous forme de "SELECT"
 * 
 * @author Pascal Lovy, 18-oct-2004
 */
public class COListSelectTag extends COListTag {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see globaz.jsp.taglib.FWList#drawTag()
     */
    @Override
    public void drawTag() throws IOException {
        drawSelect();
    }

}
