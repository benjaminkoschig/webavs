package globaz.apg.vb.droits;

import globaz.apg.menu.MenuPrestation;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.prestation.api.PRTypeDemande;


public interface APRecapitulatifDroitViewBean extends FWViewBeanInterface {

    void setAfficherBoutonSimulerPmtBPID(boolean afficherBoutonSimulerPmtBPID);

    void setPidAnnonce(String pidAnnonce);

    void setIdDroit(String idDroit);

    String getDateDebutDroit();

    String getGenreService();

    String getNoAVS();

    String getNomPrenom();

    default APRecapitulatifDroitPaiViewBean setTypeDemande(PRTypeDemande typeDemande) {
        return null;
    }

    default PRTypeDemande getTypeDemande() {
        return null;
    }

    default String resolveMenuOptionDroit() {
        return MenuPrestation.of(this.getTypeDemande()).getMenuIdOptionDroit();
    }

    default String resolveMenuPrincipale() {
        return MenuPrestation.of(this.getTypeDemande()).getMenuIdPrincipal();
    }
}
