/**
 *
 */
package globaz.vulpecula.vb.registre;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.log.JadeLogger;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TITiers;

/**
 * @author JPA
 * 
 */
public class PTSectionViewBean extends BJadePersistentObjectViewBean {
    public static final String DEFAULT_ID_ADRESSE_SECTION = "1";

    public PTSectionViewBean() {
        super();
    }

    @Override
    public void add() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete() throws Exception {
        throw new UnsupportedOperationException();
    }

    /**
     * Retourne l'adresse de la caisse, tiers numéro 1
     * 
     * @return
     */
    public String getDefaultAdresseSection() {

        TITiers tiers = new TITiers();
        tiers.setIdTiers(PTSectionViewBean.DEFAULT_ID_ADRESSE_SECTION);
        try {
            return tiers.getAdresseAsString(IConstantes.CS_AVOIR_ADRESSE_COURRIER).replace("\n", "<br>");
        } catch (Exception e) {
            JadeLogger.error(this, "Error loading adresse for administration "
                    + PTSectionViewBean.DEFAULT_ID_ADRESSE_SECTION + " : " + e.toString());
        }

        return "";
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
    }

    @Override
    public void update() throws Exception {
        throw new UnsupportedOperationException();
    }
}