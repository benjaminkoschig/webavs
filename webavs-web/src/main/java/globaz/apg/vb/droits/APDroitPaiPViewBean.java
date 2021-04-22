package globaz.apg.vb.droits;

import ch.globaz.common.util.Dates;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitProcheAidant;
import globaz.apg.enums.APModeEditionDroit;
import globaz.apg.properties.APParameter;
import globaz.apg.util.APGSeodorErreurListEntities;
import globaz.commons.nss.NSUtil;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.beans.PRPeriode;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.utils.PRDateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * <H1>Description</H1> Créé le 5 juil. 05
 *
 * @author vre
 */
public class APDroitPaiPViewBean extends APAbstractDroitProxyViewBean {

    private String dateDeces = null;
    private String dateDebutPeriode = "";
    private String dateFinPeriode = "";
    private List<PRPeriode> periodes;
    private String remarque = "";
    private APGSeodorErreurListEntities messagesError = new APGSeodorErreurListEntities();
    private boolean hasMessagePropError = false;
    private String messagesWarn = "";
    private boolean hasMessageWarn = false;


    private String dateFromJS = "";

    /**
     * Crée une nouvelle instance de la classe APDroitPaiPViewBean.
     */
    public APDroitPaiPViewBean() {
        super(new APDroitProcheAidant());
        getDroit().setGenreService(IAPDroitLAPG.CS_ALLOCATION_PROCHE_AIDANT);
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
    public final APDroitProcheAidant getDroit() {
        return (APDroitProcheAidant) super.getDroit();
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
     * @param string une nouvelle valeur pour cet attribut
     */
    @Override
    public void setDateDeces(String string) {
        dateDeces = string;
    }

    /**
     * setter pour l'attribut droit acquis
     *
     * @param droitAcquis une nouvelle valeur pour cet attribut
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

    public int calculerNbjourTotalIndemnise() {
        if (JadeStringUtil.isBlankOrZero(this.getDroit().getIdDroit())) {
            return 0;
        }
        return this.getDroit().calculerNbjourTotalIndemnise();
    }

    public String getDelaiCadre() {
        return this.getDroit().calculerDelai()
                   .map(Dates::formatSwiss)
                   .orElse("");
    }

    public int calculerNbJourDisponible() {
        return this.getDroit().calculerNbJourDisponible();
    }

    public String getMessagesWarn() {
        StringBuilder msgHTML = new StringBuilder();
        if (Objects.nonNull(messagesWarn)) {
            msgHTML.append(messagesWarn+"<br>");
        }
        return msgHTML.toString();
    }

    public void setMessagesWarn(String messagesWarn) {
        this.messagesWarn = messagesWarn;
    }

    public boolean hasMessageWarn() {
        return hasMessageWarn;
    }

    public void setMessageWarn(boolean hasMessageWarn) {
        this.hasMessageWarn = hasMessageWarn;
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
                if (dates.length > 5) {
                    periode.setNbJoursupplementaire(dates[5]);
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
     * @param string une nouvelle valeur pour cet attribut
     */
    public void setNbrJourSoldes(String string) {
        getDroit().setNbrJourSoldes(string);
    }

    public void setDroit(APDroitProcheAidant droit) {
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

    public APGSeodorErreurListEntities getMessagesErrorList() {
        return messagesError;
    }

    public void setMessagesError(APGSeodorErreurListEntities messagesError) {
        this.messagesError = messagesError;
    }

    public String getMessagesError() {
        StringBuilder msgHTML = new StringBuilder();
        if (Objects.nonNull(messagesError.getMessageErreur())) {
            msgHTML.append("<p>" + messagesError.getMessageErreur() + "<p><br>");
        }
        return msgHTML.toString();
    }

    public String getName() {
        return APDroitPaiPViewBean.class.getName();
    }

    public String getDateValidite() {
        String dateMin = "01/01/2021";
        String parameterName = APParameter.PATERNITE_JOUR_MAX.getParameterName();
        FWFindParameterManager manager = new FWFindParameterManager();
        manager.setSession(getSession());
        manager.setIdCleDiffere(parameterName);
        try {
            manager.find(BManager.SIZE_NOLIMIT);
            if (manager.size() > 0) {
                FWFindParameter param = (FWFindParameter) manager.getFirstEntity();
                Date d = new SimpleDateFormat("yyyyMMdd").parse(param.getDateDebutValidite());
                dateMin = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(d);
            } else {
                dateMin = "01/01/2021";
            }

        } catch (Exception e) {
           return  "01/01/2021";
        }
        return dateMin;
    }


    public int getJourPaternite(BSession session) {
        try {
            return Integer.parseInt(FWFindParameter.findParameter(session.getCurrentThreadTransaction(), "1", APParameter.PATERNITE_JOUR_MAX.getParameterName(), "0", "", 0));
        } catch (Exception e) {
            return 14;
        }
    }

    public void checkWarningVerifJour(BSession session){
        int nbJourSolde = 0;
        int nbJourMax = getJourPaternite(session);
        for(PRPeriode p: periodes) {
            nbJourSolde += Integer.valueOf(p.getNbJour());
        }
        hasMessageWarn = nbJourSolde < nbJourMax;
        if(hasMessageWarn) {
            messagesWarn = session.getLabel("JSP_WARN_PATERNITE_NB_JOUR");
            messagesWarn = messagesWarn.replace("{0}", Integer.toString(nbJourMax));
        } else {
            messagesWarn = "";
        }
    }


}
