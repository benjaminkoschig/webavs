package globaz.pegasus.vb.home;

import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCHomeHelper;

import java.util.ArrayList;
import java.util.List;

import ch.globaz.pegasus.business.models.home.Home;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtat;
import ch.globaz.pegasus.business.models.home.PeriodeServiceEtatSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

import static globaz.externe.IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE;

public class PCHomeViewBean extends BJadePersistentObjectViewBean {

    private static final Object[] METHODES_SEL_TIERS = new Object[]{new String[]{"idTiersHomePyxis", "getIdTiers"}};

    public static Object[] getMethodesSelTiers() {
        return PCHomeViewBean.METHODES_SEL_TIERS;
    }

    private PCHomeAjaxViewBean ajaxViewBean = null;

    /**
     * Modele contenant le home
     */
    private Home home = null;

    /**
     * L'adresse formatee du home
     */
    private String homeAdresseFormatee = null;

    /**
     * L'adresse formatee du home
     */
    private String homeAdressePaiementFormatee = null;

    /**
     * La liste des modele de periodes de services de l'etat
     */
    private List listePeriodeServiceEtat = new ArrayList();

    /**
     * Memorise le retour depuis pyxis
     */
    private boolean retourDepuisPyxis;

    public PCHomeViewBean() {
        super();
        home = new Home();
    }

    public PCHomeViewBean(Home home) {
        super();
        this.home = home;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    /**
     * @return the ajaxViewBean
     */
    public PCHomeAjaxViewBean getAjaxViewBean() {
        return ajaxViewBean;
    }

    /**
     * Donne le modele du home
     *
     * @return the home
     */
    public Home getHome() {
        return home;
    }

    /**
     * Méthode qui retourne l'adresse formatee d'un home par son tiers
     *
     * @return adresse de domicile du tiers
     */
    public String getHomeAdresseFormatee() {
        return JadeStringUtil.toNotNullString(homeAdresseFormatee);
    }
    /**
     * Méthode qui retourne l'adresse de paiement formatee d'un home par son tiers
     *
     * @return adresse de paiement du tiers
     */
    public String getHomeAdressePaiementFormatee() {
        return JadeStringUtil.toNotNullString(homeAdressePaiementFormatee);
    }

    /**
     * Méthode qui retourne la description du home
     *
     * @return la description du home
     */
    public String getHomeDescription() {
        return PCHomeHelper.getHomeDescription(home);

    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return home.getId();
    }

    /**
     * Méthode qui retourne le libellé du canton par rapport a l'id canton
     *
     * @return le libellé du canton
     */
    public String getLibelleCanton() {
        return getSession().getCodeLibelle(home.getAdresse().getLocalite().getIdCanton());
    }

    /**
     * @return the listePeriodeServiceEtat
     */
    public List getListePeriodeServiceEtat() {
        return listePeriodeServiceEtat;
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (home != null) && !home.isNew() ? new BSpy(home.getSpy()) : new BSpy(getSession());

    }

    /**
     * Charge l'adresse de domicile formatee d'un home
     *
     * @throws Exception
     */
    public void initHomeAdresseFormatee() throws Exception {
        // on recherche l'adresse formatee du home
        homeAdresseFormatee = TIBusinessServiceLocator
                .getAdresseService()
                .getAdresseTiers(home.getSimpleHome().getIdTiersHome(), Boolean.TRUE, JACalendar.todayJJsMMsAAAA(),
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, AdresseService.CS_TYPE_COURRIER, null)
                .getAdresseFormate();
    }

    private void initHomeAdressePaiementFormatee() throws JadeApplicationException, JadePersistenceException {
        homeAdressePaiementFormatee = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(home.getSimpleHome().getIdTiersHome(), Boolean.TRUE,   TIERS_CS_DOMAINE_APPLICATION_RENTE,JACalendar.todayJJsMMsAAAA(),"").getAdresseFormate();
    }
    /*
     * Initialisation de l'adresse du home et de la liste des periodes de service de l'etat
     *
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */

    /**
     * @return the retourDepuisPyxis
     */
    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    @Override
    public void retrieve() throws Exception {

        // réinitialisalition de la liste des périodes, entre autres pour le cas
        // de retour de la page des tiers
        listePeriodeServiceEtat.clear();

        home = PegasusServiceLocator.getHomeService().read(home.getId());

        ajaxViewBean = new PCHomeAjaxViewBean();
        ajaxViewBean.setHome(home);

        if(home != null && home.getSimpleHome().getIdTiersHome() != null){
            // on recherche l'adresse formatee du home
            initHomeAdresseFormatee();
            // on recherche l'adresse du paiement formaté du home
            initHomeAdressePaiementFormatee();
        }





        // les periodes de service de l'etat du home
        PeriodeServiceEtatSearch periodeServiceEtatSearch = new PeriodeServiceEtatSearch();
        periodeServiceEtatSearch.setForIdHome(home.getSimpleHome().getIdHome());
        periodeServiceEtatSearch = PegasusServiceLocator.getHomeService().searchPeriode(periodeServiceEtatSearch);

        for (int i = 0; i < periodeServiceEtatSearch.getSearchResults().length; i++) {
            PeriodeServiceEtat periode = (PeriodeServiceEtat) periodeServiceEtatSearch.getSearchResults()[i];
            listePeriodeServiceEtat.add(periode);
        }
    }


    /**
     * @param ajaxViewBean the ajaxViewBean to set
     */
    public void setAjaxViewBean(PCHomeAjaxViewBean ajaxViewBean) {
        this.ajaxViewBean = ajaxViewBean;
    }

    /**
     * @param home the home to set
     */
    public void setHome(Home home) {
        this.home = home;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        home.setId(newId);
    }

    /**
     * Mise a jour de l'idTiersHome apres le retour de pyxis
     *
     * @param idTiersHome
     */
    public void setIdTiersHomePyxis(String idTiersHome) {
        retourDepuisPyxis = true;
        getHome().getSimpleHome().setIdTiersHome(idTiersHome);
    }

    /**
     * @param retourDepuisPyxis the retourDepuisPyxis to set
     */
    public void setRetourDepuisPyxis(boolean retourDepuisPyxis) {
        this.retourDepuisPyxis = retourDepuisPyxis;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
    }

}
