package globaz.corvus.vb.decisions;

import globaz.corvus.vb.REAbstractViewBean;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.jade.common.Jade;
import java.util.Iterator;

/**
 * ViewBean pour l'affichage des différents type de préparation de décision :
 * <ul>
 * <li>
 * Normal (sans cas particulier)
 * <li>
 * Avec ajournement (une rente a un code cas spécial 08)
 * </ul>
 */
public abstract class REPreparerDecisionViewBean extends REAbstractViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String PAGE_PREPARATION_DECISION_AVEC_AJOURNEMENT = "/corvusRoot/decisions/preparerDecisionAvecAjournement_de.jsp";
    public static final String PAGE_PREPARATION_DECISION_STANDARD = "/corvusRoot/decisions/preparerDecisions_de.jsp";

    private String destination;
    private String detailRequerant;
    private Long idDemandeRente;
    private boolean interdirePreparationCarMelangeRentesAvecEtSansCodeCasSpecial;

    public REPreparerDecisionViewBean() {
        this(null, null, null);
    }

    public REPreparerDecisionViewBean(Long idDemandeRente, String destination, String detailRequerant) {
        super();

        this.destination = destination;
        this.detailRequerant = detailRequerant;
        this.idDemandeRente = idDemandeRente;
    }

    public String getAppColor() {
        return Jade.getInstance().getWebappBackgroundColor();
    }

    /**
     * @return la page JSP de destination pour ce type de préparation de décision
     */
    public final String getDestination() {
        return destination;
    }

    /**
     * @return le détail, mis en forme, du requérant (NSS, nom, prénom, etc...)
     */
    public final String getDetailRequerant() {
        return detailRequerant;
    }

    /**
     * @return l'ID (de BDD) de la demande de rente sur laquelle on aimerait préparer la/les décision/s
     */
    public final Long getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * @return l'ID (de BDD) de la demande de rente sur laquelle on aimerait préparer la/les décision/s
     */
    public final String getIdDemandeRenteToString() {
        if (idDemandeRente != null) {
            return idDemandeRente.toString();
        }
        return "";
    }

    public String getIdLangue() {
        return FWDefaultServletAction.getIdLangueIso(getSession());
    }

    @Override
    public final FWListViewBeanInterface getListViewBean() {
        return null;
    }

    @Override
    public final boolean hasList() {
        return false;
    }

    @Override
    public final Iterator<?> iterator() {
        return null;
    }

    /**
     * (re-)défini la page JSP de destination pour ce type de préparation de décision
     * 
     * @param destination
     */
    public final void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * (re-)défini les informations concernant le requérant qui seront affichées à l'utilisateur sur l'écran
     * 
     * @param detailRequerant
     *            le détail, mis en forme, du requérant (NSS, nom, prénom, etc...)
     */
    public final void setDetailRequerant(String detailRequerant) {
        this.detailRequerant = detailRequerant;
    }

    @Override
    public final void setGetListe(boolean getListe) {
        // rien
    }

    /**
     * (re-)défini l'ID de la demande de rente sur laquelle on aimerait préparer la/les décision/s
     * 
     * @param idDemandeRente
     *            une chaîne de caractère représentant l'ID de la demande de rente
     */
    public final void setIdDemandeRente(String idDemandeRente) {
        this.idDemandeRente = Long.parseLong(idDemandeRente);
    }

    @Override
    public final void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        // rien
    }

    public boolean isInterdirePreparationCarMelangeRentesAvecEtSansCodeCasSpecial() {
        return interdirePreparationCarMelangeRentesAvecEtSansCodeCasSpecial;
    }

    public void setInterdirePreparationCarMelangeRentesAvecEtSansCodeCasSpecial(
            boolean interdirePreparationCarMelangeRentesAvecEtSansCodeCasSpecial) {
        this.interdirePreparationCarMelangeRentesAvecEtSansCodeCasSpecial = interdirePreparationCarMelangeRentesAvecEtSansCodeCasSpecial;
    }
}
