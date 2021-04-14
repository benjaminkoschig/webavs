package globaz.apg.vb.droits;

import globaz.framework.bean.FWViewBeanInterface;


public interface APRecapitulatifDroitViewBean extends FWViewBeanInterface {

    void setAfficherBoutonSimulerPmtBPID(boolean afficherBoutonSimulerPmtBPID);

    void setPidAnnonce(String pidAnnonce);

    void setIdDroit(String idDroit);

    String getDateDebutDroit();

    String getGenreService();

    String getNoAVS();

    String getNomPrenom();
}
