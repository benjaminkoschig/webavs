/*
 * Créé le 7 juin 05
 */
package globaz.apg.vb.prestation;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.api.prestation.IAPPrestation;
import globaz.apg.db.prestation.APPrestationJointLotTiersDroit;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.HashSet;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APPrestationJointLotTiersDroitViewBean extends APPrestationJointLotTiersDroit implements
        FWViewBeanInterface {

    private static final long serialVersionUID = 4999792656221111635L;
    private Boolean creerLot = Boolean.FALSE;
    private String montantNet = "";
    private String nbPostit = "";

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        nbPostit = statement.dbReadNumeric(APPrestationJointLotTiersDroitListViewBean.FIELDNAME_COUNT_POSTIT);
        montantNet = statement.dbReadNumeric(APPrestationJointLotTiersDroitListViewBean.FIELDNAME_SUM_MONTANT_NET);

        super._readProperties(statement);

        setIdTiers(statement.dbReadNumeric(PRDemande.FIELDNAME_IDTIERS));
    }

    /**
     * getter pour l'attribut creer lot
     * 
     * @return la valeur courante de l'attribut creer lot
     */
    public Boolean getCreerLot() {
        return creerLot;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerant() {

        return PRNSSUtil.formatDetailRequerantListe(getNoAVS(), getNom() + " " + getPrenom(), getDateNaissance(),
                getLibelleCourtSexe(), getLibellePays());

    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les détails
     * 
     * @return le détail du requérant formaté
     * @throws Exception
     */
    public String getDetailRequerantDetail() throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiers(getSession(), getNoAVS());

        if (tiers != null) {

            String nationalite = "";

            if (!"999".equals(getSession()
                    .getCode(
                            getSession().getSystemCode("CIPAYORI",
                                    tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))))) {
                nationalite = getSession().getCodeLibelle(
                        getSession().getSystemCode("CIPAYORI",
                                tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            }

            return PRNSSUtil.formatDetailRequerantDetail(getNoAVS(), getNom() + " " + getPrenom(),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)), nationalite);

        } else {
            return "";
        }
    }

    /**
     * Crée le Hashset contenant les codes systemes a ne pas afficher suivant l'état de la prestation
     * 
     * @return un hashset contenant les CS a ne pas afficher
     */
    public HashSet<String> getExceptEtat() {
        HashSet<String> except = new HashSet<String>();

        if (isMisEnLot()) {
            except.add(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF);
        } else if (isControle()) {
            except.add(IAPPrestation.CS_ETAT_PRESTATION_MIS_LOT);
            except.add(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF);
        } else if (isValide()) {
            except.add(IAPPrestation.CS_ETAT_PRESTATION_MIS_LOT);
            except.add(IAPPrestation.CS_ETAT_PRESTATION_CONTROLE);
            except.add(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF);
        } else if (isOuvert()) {
            except.add(IAPPrestation.CS_ETAT_PRESTATION_MIS_LOT);
            except.add(IAPPrestation.CS_ETAT_PRESTATION_CONTROLE);
            except.add(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF);
        } else if (isAnnule()) {
            except.add(IAPPrestation.CS_ETAT_PRESTATION_MIS_LOT);
            except.add(IAPPrestation.CS_ETAT_PRESTATION_CONTROLE);
            except.add(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF);
        }

        if (isMisEnLot() && getNoRevision().equals(IAPDroitMaternite.CS_REVISION_MATERNITE_2005)) {
            except.add(IAPPrestation.CS_ETAT_PRESTATION_CONTROLE);
        }

        return except;
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

    public String getLibelleGenrePrestation() {
        return getSession().getCodeLibelle(getGenre());
    }

    /**
     * getter pour l'attribut libelle genre service
     * 
     * @return la valeur courante de l'attribut libelle genre service
     */
    public String getLibelleGenreService() {
        return getSession().getCodeLibelle(getGenreService());
    }

    public String getLibelleNoRevision() {
        return getSession().getCodeLibelle(getNoRevision());
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

    /**
     * Méthode qui retourne le libellé du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé du sexe (homme ou femme)
     */
    public String getLibelleSexe() {
        return getSession().getCodeLibelle(getCsSexe());
    }

    public String getMontantNet() {
        return montantNet;
    }

    /**
     * 
     * @return
     */
    public String getMontantTotalAllocExploitation() {

        if (!JadeStringUtil.isEmpty(getMontantAllocationExploitation())
                && !JadeStringUtil.isEmpty(getNombreJoursSoldes())) {
            float m1 = Float.parseFloat(getMontantAllocationExploitation());
            float nbrJours = Float.parseFloat(getNombreJoursSoldes());
            return JANumberFormatter.format(m1 * nbrJours);
        } else {
            return "";
        }
    }

    public String getNbPostit() {
        return nbPostit;
    }

    /**
     * getter pour l'attribut nom prenom
     * 
     * @return la valeur courante de l'attribut nom prenom
     */
    public String getNomPrenom() {
        return getNom() + " " + getPrenom();
    }

    public boolean hasPostit() {
        return Integer.parseInt(nbPostit) > 0;
    }

    /**
     * teste si la prestation est annulee
     * 
     * @return true si annule, false sinon
     */
    public boolean isAnnule() {
        return getEtat().equals(IAPPrestation.CS_ETAT_PRESTATION_ANNULE);
    }

    /**
     * teste si la prestation est contrôlé
     * 
     * @return true si contrôlée, false sinon;
     */
    public boolean isControle() {
        return getEtat().equals(IAPPrestation.CS_ETAT_PRESTATION_CONTROLE);
    }

    /**
     * teste si la prestation est définitive
     * 
     * @return true si définitive, false sinon
     */
    public boolean isDefinitif() {
        return getEtat().equals(IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF);
    }

    /**
     * getter pour l'attribut mis en lot
     * 
     * @return la valeur courante de l'attribut mis en lot
     */
    public boolean isMisEnLot() {
        return getEtat().equals(IAPPrestation.CS_ETAT_PRESTATION_MIS_LOT);
    }

    /**
     * getter pour l'attribut ok pour mise en lot
     * 
     * @return la valeur courante de l'attribut ok pour mise en lot
     */
    public boolean isOkPourMiseEnLot() {
        // // ok pour mise en lot si etat=validé et que c pas un apg ou si c'est
        // controlé
        // return (getEtat().equals(IAPPrestation.CS_ETAT_PRESTATION_VALIDE) &&
        // (!getNoRevision().equals(IAPDroitAPG.CS_REVISION_APG_1999) &&
        // !getNoRevision().equals(IAPDroitAPG.CS_REVISION_APG_2005))) ||
        // getEtat().equals(IAPPrestation.CS_ETAT_PRESTATION_CONTROLE);

        // true (si dans l'etat controle
        // ou si valide et prestation maternite)
        // et pas une prestation annule
        return (isControle() || (isValide() && getNoRevision().equals(
                globaz.apg.api.droits.IAPDroitMaternite.CS_REVISION_MATERNITE_2005)))
                && !isAnnule();
    }

    /**
     * getter pour l'attribut ouvert
     * 
     * @return la valeur courante de l'attribut ouvert
     */
    public boolean isOuvert() {
        return getEtat().equals(IAPPrestation.CS_ETAT_PRESTATION_OUVERT);
    }

    /**
     * teste si la prestation est validée
     * 
     * @return true si validée, false sinon
     */
    public boolean isValide() {
        return getEtat().equals(IAPPrestation.CS_ETAT_PRESTATION_VALIDE);
    }

    /**
     * setter pour l'attribut creer lot
     * 
     * @param boolean1
     *            une nouvelle valeur pour cet attribut
     */
    public void setCreerLot(Boolean boolean1) {
        creerLot = boolean1;
    }

    /**
     * setter pour l'attribut montant allocation exploitation
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setMontantAllocationExploitation(String string) {
        super.setMontantAllocationExploitation(JANumberFormatter.deQuote(string));
    }

    /**
     * setter pour l'attribut montant brut
     * 
     * @param montantBrut
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setMontantBrut(String montantBrut) {
        if (!getMontantBrut().equals(montantBrut)) {
            wantMiseAJourRepartitions(true);
            setIsModifiedByUser(Boolean.TRUE);
        }

        super.setMontantBrut(JANumberFormatter.deQuote(montantBrut));
    }

    /**
     * setter pour l'attribut montant journalier
     * 
     * @param tauxJournalier
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setMontantJournalier(String tauxJournalier) {
        super.setMontantJournalier(JANumberFormatter.deQuote(tauxJournalier));
    }

    public void setMontantNet(String montantNet) {
        this.montantNet = montantNet;
    }

}
