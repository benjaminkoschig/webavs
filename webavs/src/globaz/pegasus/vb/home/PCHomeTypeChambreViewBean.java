package globaz.pegasus.vb.home;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCTaxeJournaliere;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;

public class PCHomeTypeChambreViewBean extends BJadePersistentObjectViewBean {

    private String idHome = null;
    private TypeChambre typeChambre = null;

    public PCHomeTypeChambreViewBean() {
        super();
        typeChambre = new TypeChambre();
    }

    public PCHomeTypeChambreViewBean(TypeChambre typeChambre) {
        super();
        this.typeChambre = typeChambre;
    }

    public String getCategorieCodeNonDefini() {
        return IPCTaxeJournaliere.CS_CATEGORIE_NON_DEFINIE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {

    }

    public boolean getIsLvpc() throws PropertiesException {
        return PCproperties.getBoolean(EPCProperties.LVPC);
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
     * Donne la designation d'un type de chambre ("Designation" + si particularite "Nom, prénom")
     * 
     * @return la designation d'un type de chambre
     */
    public String getDesignationTypeChambre() {

        return typeChambre.getDesignationTypeChambre();
    }

    @Override
    public String getId() {
        return typeChambre.getId();
    }

    public String getIdHome() {
        return JadeStringUtil.toNotNullString(idHome);
    }

    /**
     * Donne l'image a afficher dans la rcListe en fonction de isApaFacturee
     * 
     * @return
     */
    public String getImageApiFacturee() {
        Boolean part = typeChambre.getSimpleTypeChambre().getIsApiFacturee();

        if (part != null) {
            return (part.booleanValue() ? "ok.gif" : "erreur.gif");
        } else {
            return "";
        }

    }

    /**
     * Donne l'image a afficher dans la rcListe en fonction de isParticularite
     * 
     * @return
     */
    public String getImageParticularite() {
        Boolean part = typeChambre.getSimpleTypeChambre().getIsParticularite();

        if (part != null) {
            return (part.booleanValue() ? "ok.gif" : "erreur.gif");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le texte "checked" pour un checkbox HTML si le champ apiFacturee est vrai.
     * 
     * @return "checked" ou vide
     */
    public String getLibelleApiFactureeChecked() {
        Boolean part = typeChambre.getSimpleTypeChambre().getIsApiFacturee();

        if ((part != null) && (part.booleanValue() == true)) {
            return "checked";
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le texte "checked" pour un checkbox HTML si le champ particularite est vrai.
     * 
     * @return "checked" ou vide
     */
    public String getLibelleParticulariteChecked() {
        Boolean part = typeChambre.getSimpleTypeChambre().getIsParticularite();

        if ((part != null) && (part.booleanValue() == true)) {
            return "checked";
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(
                getTypeChambre().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel(),
                isNNSS().equals("true") ? true : false);
    }

    private BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (typeChambre != null) && !typeChambre.isNew() ? new BSpy(typeChambre.getSpy()) : new BSpy(getSession());

    }

    /**
     * @return the typeChambre
     */
    public TypeChambre getTypeChambre() {
        return typeChambre;
    }

    /**
     * Méthode qui retourne une string avec true si le NSS dans le vb est un NNSS, sinon false
     * 
     * @return String (true ou false)
     */
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getTypeChambre().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel())) {
            return "";
        }

        if (getTypeChambre().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

    }

    @Override
    public void setId(String newId) {
        typeChambre.setId(newId);
    }

    public void setIdHome(String idHome) {
        this.idHome = idHome;
    }

    /**
     * @param typeChambre
     *            the typeChambre to set
     */
    public void setTypeChambre(TypeChambre typeChambre) {
        this.typeChambre = typeChambre;
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
