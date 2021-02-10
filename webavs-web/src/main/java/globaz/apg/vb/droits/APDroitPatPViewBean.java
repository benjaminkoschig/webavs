package globaz.apg.vb.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitPaternite;
import globaz.apg.enums.APModeEditionDroit;
import globaz.apg.util.APGSeodorErreurListEntities;
import globaz.commons.nss.NSUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.utils.PRDateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <H1>Description</H1> Créé le 5 juil. 05
 * 
 * @author vre
 */
public class APDroitPatPViewBean extends APAbstractDroitProxyViewBean {

    private String dateDeces = null;
    private String dateDebutPeriode = "";
    private String dateFinPeriode = "";
    private List<PRPeriode> periodes;
    private String remarque = "";
    private APGSeodorErreurListEntities messagesError = new APGSeodorErreurListEntities();
    private boolean hasMessagePropError = false;

    /**
     * Crée une nouvelle instance de la classe APDroitPatPViewBean.
     */
    public APDroitPatPViewBean() {
        super(new APDroitPaternite());
        getDroit().setGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_PATERNITE);
        getDroit().setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        getDroit().setDateDepot(JACalendar.todayJJsMMsAAAA());
        getDroit().setDateReception(JACalendar.todayJJsMMsAAAA());
        periodes = new ArrayList<>();
    }

    @Override
    public String getDateDeces() {
        if (JadeStringUtil.isEmpty(dateDeces)) {
            setDateDeces(getProprieteTiers(PRTiersWrapper.PROPERTY_DATE_DECES));
        }
        return dateDeces;
    }

    @Override
    public final APDroitPaternite getDroit() {
        return (APDroitPaternite) super.getDroit();
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
     * setter pour l'attribut droit acquis
     * 
     * @param droitAcquis
     *            une nouvelle valeur pour cet attribut
     */
    public void setDroitAcquis(String droitAcquis) {
        getDroit().setDroitAcquis(droitAcquis);
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    public List<PRPeriode> getPeriodes() {
        return periodes;
    }

    public void setPeriodes(List<PRPeriode> periodes) {
        this.periodes = periodes;
    }

    public boolean hasMessagePropError() {
        return hasMessagePropError;
    }

    public void setMessagePropError(boolean hasMessagePropError) {
        this.hasMessagePropError = hasMessagePropError;
    }
    private boolean isPeriodeDejaExistante(PRPeriode newPeriode) {
        for (PRPeriode periode : periodes) {
            PRDateUtils.PRDateEquality equality1 = PRDateUtils.compare(periode.getDateDeDebut(), newPeriode.getDateDeDebut());
            PRDateUtils.PRDateEquality equality2 = PRDateUtils.compare(periode.getDateDeFin(), newPeriode.getDateDeFin());
            if (equality1.equals(PRDateUtils.PRDateEquality.EQUALS) || equality2.equals(PRDateUtils.PRDateEquality.EQUALS)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Cette méthode est appelée lors du submit du formulaire de saisie d'un droit APG 1 : saisie des périodes</br> 2 :
     * envoi au serveur, différents tests sont effectués pour la validation des infos</br> 3 : retour cas des info sont
     * manquantes, les périodes sont récupérées depuis la liste de PRPeriode via getPeriodes() et renseigné dans le
     * tableau HTML</br> 4 : edition des valeurs et renvoi au serveur, on clean la liste et on récupère à nouveau les
     * périodes depuis le tableau sous forme de string à parser</br>
     *
     * @param periodesAsString
     */
    public final void setPeriodesAsString(String periodesAsString) {
        periodes.clear();
        if (!JadeStringUtil.isEmpty(periodesAsString)) {
            String[] values = periodesAsString.split(";");
            for (int ctr = 0; ctr < values.length; ctr++) {
                String[] dates = values[ctr].split("-");
                PRPeriode periode = new PRPeriode();
                periode.setDateDeDebut(dates[0]);
                periode.setDateDeFin("");
                if (dates.length > 1) {
                    periode.setDateDeFin(dates[1]);
                }
                if (dates.length > 2) {
                    periode.setNbJour(dates[2]);
                }
                if (dates.length > 3) {
                    periode.setTauxImposition(dates[3]);
                }
                if (dates.length > 4) {
                    periode.setCantonImposition(dates[4]);
                }
                if (!isPeriodeDejaExistante(periode)) {
                    periodes.add(periode);
                }
            }
        }
    }

    public final void setModeEditionDroit(String modeEditionDroit) {
        APModeEditionDroit mode = Enum.valueOf(APModeEditionDroit.class, modeEditionDroit);
        this.setModeEditionDroit(mode);
    }

    /**
     * getter pour l'attribut nbr jour soldes
     *
     * @return la valeur courante de l'attribut nbr jour soldes
     */
    public String getNbrJourSoldes() {
        return getDroit().getNbrJourSoldes();
    }

    /**
     * setter pour l'attribut nbr jour soldes
     *
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbrJourSoldes(String string) {
        getDroit().setNbrJourSoldes(string);
    }

    public void setDroit(APDroitPaternite droit) {
        super.setDroit(droit);
    }

    /**
     * getter pour l'attribut duplicata
     *
     * @return la valeur courante de l'attribut duplicata
     */
    public Boolean getDuplicata() {
        return getDroit().getDuplicata();
    }

    /**
     * Donne le code utilisateur du genre de service du droit
     *
     * @return le CU du genre de service
     */
    public String getCuGenreService() {
        return getSession().getCode(getDroit().getGenreService());
    }

    /**
     * getter pour l'attribut no compte
     *
     * @return la valeur courante de l'attribut no compte
     */
    public String getNoCompte() {
        return getDroit().getNoCompte();
    }

    /**
     * setter pour l'attribut no compte
     *
     * @param string une nouvelle valeur pour cet attribut
     */
    public void setNoCompte(String string) {
        getDroit().setNoCompte(string);
    }

    /**
     * getter pour l'attribut no controle pers
     *
     * @return la valeur courante de l'attribut no controle pers
     */
    public String getNoControlePers() {
        return getDroit().getNoControlePers();
    }

    /**
     * setter pour l'attribut no controle pers
     *
     * @param string une nouvelle valeur pour cet attribut
     */
    public void setNoControlePers(String string) {
        getDroit().setNoControlePers(string);
    }

    /**
     * getter pour l'attribut no revision
     *
     * @return la valeur courante de l'attribut no revision
     */
    public String getNoRevision() {
        return getDroit().getNoRevision();
    }

    /**
     * setter pour l'attribut no revision
     *
     * @param string une nouvelle valeur pour cet attribut
     */
    public void setNoRevision(String string) {
        getDroit().setNoRevision(string);
    }

    @Override
    public String getRemarque() {
        return remarque;
    }

    @Override
    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }
    public APGSeodorErreurListEntities getMessagesErrorList(){
        return messagesError;
    }

    public void setMessagesError(APGSeodorErreurListEntities messagesError) {
        this.messagesError = messagesError;
    }
    public String getMessagesError() {
        StringBuilder msgHTML = new StringBuilder();
        if (Objects.nonNull(messagesError.getMessageErreur())) {
            msgHTML.append("<p>"+messagesError.getMessageErreur()+"<p><br>");
        }
        return msgHTML.toString();
    }
}
