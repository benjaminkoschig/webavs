/*
 * Créé le 12 fevr. 07
 */
package globaz.corvus.vb.basescalcul;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.nnss.PRNSSUtil;

/**
 * @author bsc
 * 
 */

public class REBasesCalculViewBean extends REBasesCalcul implements FWViewBeanInterface {

    // ~ Methods
    // -----------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String getAnneeTraitement() {

        if (JadeStringUtil.isBlankOrZero(super.getAnneeTraitement())) {
            return "";
        } else {
            return super.getAnneeTraitement();
        }

    }

    public String getDateCreation() {

        REBasesCalculDixiemeRevision bc10 = new REBasesCalculDixiemeRevision();
        bc10.setSession(getSession());
        bc10.setIdBasesCalcul(getIdBasesCalcul());

        try {
            bc10.retrieve();
            PRAssert.notIsNew(bc10, null);
            return bc10.getCreationDate();
        } catch (Exception e) {
            REBasesCalculNeuviemeRevision bc9 = new REBasesCalculNeuviemeRevision();
            bc9.setSession(getSession());
            bc9.setIdBasesCalcul(getIdBasesCalcul());

            try {
                bc9.retrieve();
                PRAssert.notIsNew(bc9, null);
                return bc9.getCreationDate();
            } catch (Exception e2) {
                return "";
            }
        }
    }

    @Override
    public String getDegreInvalidite() {

        if (JadeStringUtil.isBlankOrZero(super.getDegreInvalidite())) {
            return "";
        } else {
            return super.getDegreInvalidite();
        }

    }

    public String getDetailRequerant(String idTiers) throws Exception {

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(getSession(), idTiers);

        if (tiers != null) {
            return PRNSSUtil.formatDetailRequerantDetail(
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tiers.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                    tiers.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                    getSession().getCodeLibelle(tiers.getProperty(PRTiersWrapper.PROPERTY_SEXE)),
                    getLibellePays(tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
        } else {
            return "";
        }

    }

    @Override
    public String getEchelleRente() {

        if (JadeStringUtil.isBlankOrZero(super.getEchelleRente())) {
            return "";
        } else {
            return super.getEchelleRente();
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays(String idPays) {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", idPays)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", idPays));
        }

    }

    @Override
    public String getRevenuAnnuelMoyen() {

        if (JadeStringUtil.isBlankOrZero(super.getRevenuAnnuelMoyen())) {
            return "";
        } else {
            return super.getRevenuAnnuelMoyen();
        }

    }

    public boolean isNouveau(String idRC) throws Exception {

        REDemandeRente demande = new REDemandeRente();
        demande.setSession(getSession());
        demande.setIdRenteCalculee(idRC);
        demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        demande.retrieve();

        if (demande.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)
                || demande.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE)
                || demande.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE)
                || demande.getCsEtat().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE)) {
            return false;
        } else {
            return true;
        }

    }

}
