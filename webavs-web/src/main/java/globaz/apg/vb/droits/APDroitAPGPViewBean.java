/*
 * Créé le 7 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.enums.APModeEditionDroit;
import globaz.apg.util.APGSeodorErreurEntity;
import globaz.apg.util.APGSeodorErreurListEntities;
import globaz.commons.nss.NSUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.utils.PRDateUtils;
import globaz.prestation.utils.PRDateUtils.PRDateEquality;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APDroitAPGPViewBean extends APAbstractDroitProxyViewBean {

    private String dateDebutPeriode = "";
    private String dateFinPeriode = "";
    private List<PRPeriode> periodes;

    private APGSeodorErreurListEntities messagesError = new APGSeodorErreurListEntities();
    private boolean hasMessagePropError = false;

    /**
     * Crée une nouvelle instance de la classe APDroitAPGPViewBean.
     */
    public APDroitAPGPViewBean() {
        super(new APDroitAPG());
        getDroit().setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        getDroit().setDateDepot(JACalendar.todayJJsMMsAAAA());
        getDroit().setDateReception(JACalendar.todayJJsMMsAAAA());
        periodes = new ArrayList<PRPeriode>();
    }

    /**
     * Donne le code utilisateur du genre de service du droit
     * 
     * @return le CU du genre de service
     */
    public String getCuGenreService() {
        return getSession().getCode(getDroit().getGenreService());
    }

    @Override
    public void setDroit(APDroitLAPG droit) {
        super.setDroit(droit);
    }

    /**
     * getter pour l'attribut date debut periode
     * 
     * @return la valeur courante de l'attribut date debut periode
     */
    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    /**
     * getter pour l'attribut date fin periode
     * 
     * @return la valeur courante de l'attribut date fin periode
     */
    public String getDateFinPeriode() {
        return dateFinPeriode;
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
     * getter pour l'attribut duplicata
     * 
     * @return la valeur courante de l'attribut duplicata
     */
    public Boolean getDuplicata() {
        return getDroit().getDuplicata();
    }

    @Override
    public String getIdGestionnaire() {
        return getDroit().getIdGestionnaire();
    }

    /**
     * getter pour l'attribut id situation fam
     * 
     * @return la valeur courante de l'attribut id situation fam
     */
    public String getIdSituationFam() {
        return getDroit().getIdSituationFam();
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
     * getter pour l'attribut no compte
     * 
     * @return la valeur courante de l'attribut no compte
     */
    public String getNoCompte() {
        return getDroit().getNoCompte();
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
     * getter pour l'attribut no revision
     * 
     * @return la valeur courante de l'attribut no revision
     */
    public String getNoRevision() {
        return getDroit().getNoRevision();
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), isNNSS().equals("true") ? true : false);
    }

    public final List<PRPeriode> getPeriodes() {
        return periodes;
    }

    /**
     * @return vrai si l'etat du droit est égal à {@link IAPDroitLAPG#CS_ETAT_DROIT_ATTENTE}.
     */
    @Override
    public boolean isModifiable() {
        return getDroit().isModifiable();
    }

    /**
     * setter pour l'attribut date debut periode
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebutPeriode(String string) {
        dateDebutPeriode = string;
    }

    /**
     * setter pour l'attribut date fin periode
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFinPeriode(String string) {
        dateFinPeriode = string;
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

    /**
     * setter pour l'attribut duplicata
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setDuplicata(Boolean boolean1) {
        getDroit().setDuplicata(boolean1);
    }

    /**
     * setter pour l'attribut id situation fam
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSituationFam(String string) {
        getDroit().setIdSituationFam(string);
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

    /**
     * setter pour l'attribut no compte
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoCompte(String string) {
        getDroit().setNoCompte(string);
    }

    /**
     * setter pour l'attribut no controle pers
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoControlePers(String string) {
        getDroit().setNoControlePers(string);
    }

    @Override
    public final APDroitAPG getDroit() {
        return (APDroitAPG) super.getDroit();
    }

    public void setDroit(APDroitAPG droit) {
        super.setDroit(droit);
    }

    /**
     * setter pour l'attribut no revision
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoRevision(String string) {
        getDroit().setNoRevision(string);
    }

    private boolean isPeriodeDejaExistante(PRPeriode newPeriode) {
        for (PRPeriode periode : periodes) {
            PRDateEquality equality1 = PRDateUtils.compare(periode.getDateDeDebut(), newPeriode.getDateDeDebut());
            PRDateEquality equality2 = PRDateUtils.compare(periode.getDateDeFin(), newPeriode.getDateDeFin());
            if (equality1.equals(PRDateEquality.EQUALS) || equality2.equals(PRDateEquality.EQUALS)) {
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
                periode.setDateDeFin(dates[1]);
                if (!isPeriodeDejaExistante(periode)) {
                    periodes.add(periode);
                }
            }
        }
    }

    public final void setPeriodes(List<PRPeriode> periodes) {
        this.periodes = periodes;
    }

    public final void setModeEditionDroit(String modeEditionDroit) {
        APModeEditionDroit mode = Enum.valueOf(APModeEditionDroit.class, modeEditionDroit);
        this.setModeEditionDroit(mode);
    }


    public void setMessagePropError(boolean b) {
        hasMessagePropError = b;
    }
    public boolean hasMessagePropError() {
        return hasMessagePropError;
    }

    public String getMessagesError() {
        StringBuilder msgHTML = new StringBuilder();
        if (Objects.nonNull(messagesError.getMessageErreur())) {
            msgHTML.append("<p>"+messagesError.getMessageErreur()+"<p><br>");
        }
        return msgHTML.toString();
    }

    public APGSeodorErreurListEntities getMessagesErrorList(){
        return messagesError;
    }

    public void setMessagesError(APGSeodorErreurListEntities messagesError) {
        this.messagesError = messagesError;
    }

    public int getNombreRetourLigneRemarque(){
        return 3;
    }
}
