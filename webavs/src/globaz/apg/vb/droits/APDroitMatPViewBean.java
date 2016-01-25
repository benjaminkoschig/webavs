package globaz.apg.vb.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.commons.nss.NSUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * <H1>Description</H1> Créé le 5 juil. 05
 * 
 * @author vre
 */
public class APDroitMatPViewBean extends APAbstractDroitProxyViewBean {

    private String dateDeces = null;

    /**
     * Crée une nouvelle instance de la classe APDroitMatPViewBean.
     */
    public APDroitMatPViewBean() {
        super(new APDroitMaternite());
        getDroit().setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
        getDroit().setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        getDroit().setDateDepot(JACalendar.todayJJsMMsAAAA());
        getDroit().setDateReception(JACalendar.todayJJsMMsAAAA());
    }

    @Override
    public String getDateDeces() {
        if (JadeStringUtil.isEmpty(dateDeces)) {
            setDateDeces(getProprieteTiers(PRTiersWrapper.PROPERTY_DATE_DECES));
        }
        return dateDeces;
    }

    /**
     * getter pour l'attribut date reprise activ
     * 
     * @return la valeur courante de l'attribut date reprise activ
     */
    public String getDateRepriseActiv() {
        return ((APDroitMaternite) getDroit()).getDateRepriseActiv();
    }

    /**
     * getter pour l'attribut droit acquis
     * 
     * @return la valeur courante de l'attribut droit acquis
     */
    public String getDroitAcquis() {
        return getDroit().getDroitAcquis();
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), isNNSS().equals("true") ? true : false);
    }

    /**
     * setter pour l'attribut date deces
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setDateDeces(String string) {
        dateDeces = string;
    }

    /**
     * setter pour l'attribut date reprise activ
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateRepriseActiv(String string) {
        ((APDroitMaternite) getDroit()).setDateRepriseActiv(string);
    }

    /**
     * setter pour l'attribut droit acquis
     * 
     * @param droitAcquis
     *            une nouvelle valeur pour cet attribut
     */
    public void setDroitAcquis(String droitAcquis) {
        getDroit().setDroitAcquis(droitAcquis);
    }

}
