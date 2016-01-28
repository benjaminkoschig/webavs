package globaz.apg.vb.prestation;

import globaz.apg.db.prestation.APRepartitionPaiementsManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APRepartitionPaiementsListViewBean extends APRepartitionPaiementsManager implements
        FWListViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String genreService = "";
    private String idDroit = "";
    private String noAVS = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APRepartitionPaiementsViewBean();
    }

    /**
     * getter pour l'attribut genre service
     * 
     * @return la valeur courante de l'attribut genre service
     */
    public String getGenreService() {
        return genreService;
    }

    /**
     * getter pour l'attribut id droit
     * 
     * @return la valeur courante de l'attribut id droit
     */
    public String getIdDroit() {
        return idDroit;
    }

    public String getNoAVS() {
        return noAVS;
    }

    /**
     * setter pour l'attribut genre service
     * 
     * @param genreService
     *            une nouvelle valeur pour cet attribut
     */
    public void setGenreService(String genreService) {
        this.genreService = genreService;
    }

    /**
     * setter pour l'attribut id droit
     * 
     * @param idDroit
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setNoAVS(String noAVS) {
        this.noAVS = noAVS;
    }
}
