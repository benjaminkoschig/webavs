/*
 * Créé le 10 janv. 07
 */
package globaz.corvus.vb.demandes;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointPrestationAccordee;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * @author hpe
 */
public class REDemandeRenteJointDemandeViewBean extends REDemandeRenteJointDemande implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> listeIdDemande = new ArrayList<String>();
    private String nbPostit = "";
    private transient Vector<String[]> orderBy = null;

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nbPostit = statement.dbReadNumeric(REDemandeRenteJointDemandeListViewBean.FIELDNAME_COUNT_POSTIT);
        super._readProperties(statement);
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerant() {

        return PRNSSUtil.formatDetailRequerantListe(getNoAVS(), getNom() + " " + getPrenom(), getDateNaissance(),
                getLibelleCourtSexe(), getLibellePays());

    }

    public String getDetailRequerantDecede() throws Exception {

        if (!JadeStringUtil.isEmpty(getDateDeces())) {
            return PRNSSUtil.formatDetailRequerantListeDecede(getNoAVS(), getNom() + " " + getPrenom(),
                    getDateNaissance(), getLibelleCourtSexe(), getLibellePays(), getDateDeces());
        } else {
            return getDetailRequerant();
        }

    }

    public String getEtatDemande() {
        return getSession().getCodeLibelle(getCsEtatDemande());
    }

    public Vector getEtatsDemande() {

        Vector<String[]> etatsDemande = PRCodeSystem.getLibellesPourGroupe(
                IREDemandeRente.CS_GROUPE_ETAT_DEMANDE_RENTE, getSession());

        // ajout des options custom
        etatsDemande.add(
                1,
                new String[] { REDemandeRenteJointDemande.LABEL_NON_VALIDE,
                        getSession().getLabel("JSP_DRE_R_DEMANDES_NON_VALIDE") });
        etatsDemande.add(0, new String[] { "", "" });

        return etatsDemande;
    }

    public String getIdTierRequerant() throws Exception {

        // normalement on doit avoir la valeur idtiersRequerant
        // mais, on la cherche avec le NSS ???
        // Modifie comme ceci pour minimiser les impacts et surtout diminuer les
        // requetes...
        if (JadeStringUtil.isIntegerEmpty(super.getIdTiersRequerant())) {
            PRTiersWrapper tier = PRTiersHelper.getTiers(getSession(), getNoAVS());

            if (tier != null) {
                return tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
            } else {
                return "";
            }
        } else {
            return super.getIdTiersRequerant();
        }
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe() {

        if (PRACORConst.CS_HOMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexe())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays() {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", getCsNationalite())))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getCsNationalite()));
        }

    }

    public List<String> getListeIdDemande() {
        return listeIdDemande;
    }

    public String getNbPostit() {
        return nbPostit;
    }

    /**
     * Méthode qui retourne le NNSS formaté sans le préfixe (756.) ou alors le NSS normal
     * 
     * @return NNSS formaté sans préfixe ou NSS normal
     */
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNoAVS(), isNNSS().equals("true") ? true : false);
    }

    /**
     * getter pour l'attribut order by data
     * 
     * @return la valeur courante de l'attribut order by data
     */
    public Vector getOrderByData() {
        if (orderBy == null) {
            orderBy = new Vector<String[]>(4);
            orderBy.add(new String[] {
                    REDemandeRenteJointDemande.FIELDNAME_NOM + "," + REDemandeRenteJointDemande.FIELDNAME_PRENOM + ","
                            + " DATE_FIN DESC, " + REDemandeRente.FIELDNAME_DATE_DEBUT + " DESC",
                    getSession().getLabel("JSP_DRE_R_TRIER_PAR_NOM") });
            orderBy.add(new String[] {
                    REDemandeRente.FIELDNAME_DATE_RECEPTION + " DESC" + "," + " DATE_FIN DESC, "
                            + REDemandeRente.FIELDNAME_DATE_DEBUT + " DESC",
                    getSession().getLabel("JSP_DRE_R_TRIER_PAR_DATE_RECEPTION") });
            orderBy.add(new String[] {
                    REDemandeRente.FIELDNAME_CS_ETAT + " DESC" + "," + " DATE_FIN DESC, "
                            + REDemandeRente.FIELDNAME_DATE_DEBUT + " DESC",
                    getSession().getLabel("JSP_DRE_R_TRIER_PAR_ETAT") });
        }

        return orderBy;
    }

    public String getTypeDemande() {
        if (getCsTypeCalcul().equals(IREDemandeRente.CS_TYPE_CALCUL_BILATERALES)) {
            return getSession().getCodeLibelle(getCsTypeDemande()) + " - "
                    + getSession().getLabel("JSP_DRE_R_TYPEDEMANDE_PLUS_BILATERALES");
        } else if (getCsTypeCalcul().equals(IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL)) {
            return getSession().getCodeLibelle(getCsTypeDemande()) + " - "
                    + getSession().getLabel("JSP_DRE_R_TYPEDEMANDE_PLUS_PREVISIONNEL");

            // BZ 5198, si document à éditer de l'information complémentaire est rente de veuve perdure
            // je l'affiche dans le type de demande
        } else if (isInfoComplRenteVeuvePerdure()) {
            return getSession().getCodeLibelle(getCsTypeDemande()) + " - "
                    + getSession().getLabel("JSP_DRE_R_TYPEDEMANDE_PLUS_RENTE_VEUVE_PERDURE");
        } else {
            return getSession().getCodeLibelle(getCsTypeDemande());
        }
    }

    public boolean hasPostit() {
        return JadeStringUtil.isBlank(nbPostit) ? false : Integer.parseInt(nbPostit) > 0;
    }

    /**
     * BZ 5198, teste si le document à éditer est rente de veuve perdure
     * 
     * @return <strong><code>true</code></strong> si l'info complemntaire est de type "Rente de veuve perdure", sinon
     *         <strong><code>false</code></strong>
     */
    public boolean isInfoComplRenteVeuvePerdure() {
        if (IREDemandeRente.CS_TYPE_INFORMATION_COMPLEMENTAIRE_RENTE_VEUVE_PERDURE
                .equals(getCsTypeInfoComplementaire())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Teste si le NSS dans le viewBean est un NNSS
     * 
     * @return une {@link String} avec <strong><code>true</code></strong> si le NSS contenu dans le vieBean est un NNSS,
     *         sinon <strong><code>false</code></strong>
     */
    public String isNNSS() {
        if (JadeStringUtil.isBlankOrZero(getNoAVS())) {
            return "";
        }
        if (getNoAVS().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * Méthode qui contrôle si la préparation de la décision peut s'effectuer
     * 
     * @return <strong><code>true</code></strong>Si la préparation est autorisée, sinon <strong><code>false</code>
     *         </strong>
     */
    public boolean isPreparationDecisionValide(String dateDernierPaiement) {

        try {
            JACalendar cal = new JACalendarGregorian();
            JADate datePmtMensuel = null;

            if (!JadeStringUtil.isBlankOrZero(dateDernierPaiement)) {
                datePmtMensuel = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dateDernierPaiement));
            }

            JADate dateDebutDroit = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(getDateDebut()));
            JADate dateTraitement = new JADate(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(getDateTraitement()));
            JADate dateJour = JACalendar.today();
            dateJour.setDay(1);

            // Si (dt=dj et dt=dpmt) ou (dt<dj et dt<dpmt et ddeb > dpmt) ou
            // (dt>dj && dt==dpmt) la préparation de la décision peut
            // s'effectuer
            if (datePmtMensuel != null) {
                return (((cal.compare(dateTraitement, dateJour) == JACalendar.COMPARE_FIRSTLOWER)
                        && (cal.compare(dateTraitement, datePmtMensuel) == JACalendar.COMPARE_FIRSTLOWER) && (cal
                        .compare(dateDebutDroit, datePmtMensuel) == JACalendar.COMPARE_FIRSTUPPER))
                        ||

                        ((cal.compare(dateTraitement, dateJour) == JACalendar.COMPARE_EQUALS) && (cal.compare(
                                dateTraitement, datePmtMensuel) == JACalendar.COMPARE_EQUALS)) ||

                ((cal.compare(dateTraitement, dateJour) == JACalendar.COMPARE_FIRSTUPPER) && (cal.compare(
                        dateTraitement, datePmtMensuel) == JACalendar.COMPARE_EQUALS)));
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public boolean isOptionOfficeAi(REDemandeRenteJointDemande demande) {

        REDemandeRenteJointPrestationAccordee demJprest = new REDemandeRenteJointPrestationAccordee();
        demJprest.setSession(getSession());
        demJprest.setIdDemandeRente(demande.getIdDemandeRente());

        try {
            demJprest.retrieve();
        } catch (Exception e) {
            return false;
        }

        CodePrestation prestation = CodePrestation.getCodePrestation(Integer.valueOf(demJprest.getCodePrestation()));

        if (prestation.isAPI()) {

        }

        return true;
    }

    public void setListeIdDemande(List<String> listeIdDemande) {
        this.listeIdDemande = listeIdDemande;
    }
}
