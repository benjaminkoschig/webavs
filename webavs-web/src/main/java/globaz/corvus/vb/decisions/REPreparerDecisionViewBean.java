package globaz.corvus.vb.decisions;

import globaz.corvus.vb.REAbstractViewBean;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.jade.common.Jade;
import java.util.Iterator;

/**
 * ViewBean pour l'affichage des diff�rents type de pr�paration de d�cision :
 * <ul>
 * <li>
 * Normal (sans cas particulier)
 * <li>
 * Avec ajournement (une rente a un code cas sp�cial 08)
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
     * @return la page JSP de destination pour ce type de pr�paration de d�cision
     */
    public final String getDestination() {
        return destination;
    }

    /**
     * @return le d�tail, mis en forme, du requ�rant (NSS, nom, pr�nom, etc...)
     */
    public final String getDetailRequerant() {
        return detailRequerant;
    }

    /**
     * @return l'ID (de BDD) de la demande de rente sur laquelle on aimerait pr�parer la/les d�cision/s
     */
    public final Long getIdDemandeRente() {
        return idDemandeRente;
    }

    /**
     * @return l'ID (de BDD) de la demande de rente sur laquelle on aimerait pr�parer la/les d�cision/s
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
     * (re-)d�fini la page JSP de destination pour ce type de pr�paration de d�cision
     * 
     * @param destination
     */
    public final void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * (re-)d�fini les informations concernant le requ�rant qui seront affich�es � l'utilisateur sur l'�cran
     * 
     * @param detailRequerant
     *            le d�tail, mis en forme, du requ�rant (NSS, nom, pr�nom, etc...)
     */
    public final void setDetailRequerant(String detailRequerant) {
        this.detailRequerant = detailRequerant;
    }

    @Override
    public final void setGetListe(boolean getListe) {
        // rien
    }

    /**
     * (re-)d�fini l'ID de la demande de rente sur laquelle on aimerait pr�parer la/les d�cision/s
     * 
     * @param idDemandeRente
     *            une cha�ne de caract�re repr�sentant l'ID de la demande de rente
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
