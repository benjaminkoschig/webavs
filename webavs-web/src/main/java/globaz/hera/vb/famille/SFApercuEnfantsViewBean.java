/*
 * Créé le 19 sept. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.vb.famille;

import com.jcraft.jsch.Logger;
import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.hera.db.famille.SFApercuEnfant;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFMembreFamilleManager;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.hera.interfaces.tiers.SFTiersHelper;
import globaz.hera.interfaces.tiers.SFTiersWrapper;
import globaz.hera.tools.nss.SFUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.pyxis.db.adressecourrier.TIPays;
import globaz.pyxis.db.adressecourrier.TIPaysManager;

/**
 * @author jpa
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class SFApercuEnfantsViewBean extends SFApercuEnfant implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final Object[] METHODES_SEL_BENEFICIAIRE = new Object[] { new String[] { "idTiersDepuisPyxis",
            "idTiers" }, };

    private String conjoint1Infos = "";

    private String conjoint1NomPrenom = "";
    private String conjoint2Infos = "";
    private String conjoint2NomPrenom = "";
    private String dateDebut = "";
    private String dateFin = "";

    private String forIdConjoints = "";
    private String idConjoints = "";

    private boolean retourDepuisPyxis;
    private String typeRelation = "";

    /**
     * @return
     */
    public String getConjoint1Infos() {
        return conjoint1Infos;
    }

    /**
     * @return
     */
    public String getConjoint1NomPrenom() {
        return conjoint1NomPrenom;
    }

    /**
     * @return
     */
    public String getConjoint2Infos() {
        return conjoint2Infos;
    }

    /**
     * @return
     */
    public String getConjoint2NomPrenom() {
        return conjoint2NomPrenom;
    }

    /**
     * @return
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return
     */
    public String getForIdConjoints() {
        return forIdConjoints;
    }

    /**
     * @return
     */
    public String getIdConjoints() {
        return idConjoints;
    }

    /**
     * retourne un tableau de correspondance entre methodes client et provider pour le retour depuis pyxis avec le
     * bouton de selection d'un beneficiaire de paiement.
     * 
     * @return la valeur courante de l'attribut methodes selection beneficiaire
     */
    public Object[] getMethodesSelectionBeneficiaire() {
        return METHODES_SEL_BENEFICIAIRE;
    }

    @Override
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), isNNSS().equals("true") ? true : false);
    }

    /**
     * @return
     */
    public String getTypeRelation() {
        return typeRelation;
    }

    public String getLibelleNationnalite() {
        String libelle = "";
        if (!JadeStringUtil.isIntegerEmpty(getCsNationalite())) {

            TIPaysManager paysManager = new TIPaysManager();
            paysManager.setSession(getSession());
            paysManager.setForIdPays(getCsNationalite());
            try {
                paysManager.find();
                TIPays pays = (TIPays) paysManager.getEntity(0);
                if (pays != null) {
                    libelle = pays.getLibelle();
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }
        return libelle;
    }

    /**
     * @return
     */
    @Override
    public String isNNSS() {

        if (JadeStringUtil.isBlankOrZero(getNss())) {
            return "";
        }
        if (getNss().length() > 14) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * @return
     */
    public boolean isRetourDepuisPyxis() {
        return retourDepuisPyxis;
    }

    public void retrieveRelation(BISession session, String idConjoints) {
        SFConjoint conjoints = new SFConjoint();
        conjoints.setSession((BSession) session);
        conjoints.setIdConjoints(idConjoints);

        try {
            // Il nous faut l'idCOnjoints pour retrouver les enfants de 2
            // conjoints
            // On recherche le nom des 2 conjoints
            conjoints.retrieve();
            SFMembreFamille membre;
            SFMembreFamilleManager membreManager = new SFMembreFamilleManager();
            membreManager.setSession((BSession) session);
            membreManager.setForIdMembreFamille(conjoints.getIdConjoint1());
            membreManager.setWantConjointInconnu(true);
            membreManager.find();
            membre = (SFMembreFamille) membreManager.getFirstEntity();
            setConjoint1NomPrenom(membre.getNom() + " " + membre.getPrenom());
            if (!JadeStringUtil.isEmpty(membre.getDateNaissance())) {
                conjoint1Infos += " - " + membre.getDateNaissance();
            }
            if (!JadeStringUtil.isEmpty(membre.getCsSexe()) && session != null) {
                conjoint1Infos += " - " + membre.getLibelleSexe();
            }
            if ((!JadeStringUtil.isEmpty((membre.getCsNationalite())) || (!JadeStringUtil.isEmpty(membre.getPays())))
                    && session != null) {
                conjoint1Infos += " - " + membre.getLibellePays();
            }
            setConjoint1Infos(conjoint1Infos);

            membreManager = new SFMembreFamilleManager();
            membreManager.setSession((BSession) session);
            membreManager.setForIdMembreFamille(conjoints.getIdConjoint2());
            membreManager.setWantConjointInconnu(true);
            membreManager.find();
            membre = (SFMembreFamille) membreManager.getFirstEntity();
            setConjoint2NomPrenom(membre.getNom() + " " + membre.getPrenom());
            if (!JadeStringUtil.isEmpty(membre.getDateNaissance())) {
                conjoint2Infos += " - " + membre.getDateNaissance();
            }
            if (!JadeStringUtil.isEmpty(membre.getCsSexe()) && session != null) {
                conjoint2Infos += " - " + membre.getLibelleSexe();
            }
            if ((!JadeStringUtil.isEmpty((membre.getCsNationalite())) || (!JadeStringUtil.isEmpty(membre.getPays())))
                    && session != null) {
                conjoint2Infos += " - " + membre.getLibellePays();
            }
            setConjoint2Infos(conjoint2Infos);

            // On recherche le type de relation
            SFRelationConjoint relation = new SFRelationConjoint();
            relation.setSession((BSession) session);
            relation.setAlternateKey(SFRelationConjoint.ALT_KEY_ID_CONJOINTS);
            relation.setIdConjoints(idConjoints);
            relation.retrieve();
            setDateDebut(relation.getDateDebut());
            setDateFin(relation.getDateFin());
            setTypeRelation(relation.getTypeRelation());
        } catch (Exception e) {
            JadeLogger.warn(((BSession) session).getLabel("ERROR_PENDANT_RETRIEVE_CONJOINTS"), e);
        }
    }

    /**
     * @param string
     */
    public void setConjoint1Infos(String string) {
        conjoint1Infos = string;
    }

    /**
     * @param string
     */
    public void setConjoint1NomPrenom(String string) {
        conjoint1NomPrenom = string;
    }

    /**
     * @param string
     */
    public void setConjoint2Infos(String string) {
        conjoint2Infos = string;
    }

    /**
     * @param string
     */
    public void setConjoint2NomPrenom(String string) {
        conjoint2NomPrenom = string;
    }

    /**
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * @param string
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    /**
     * @param string
     */
    public void setForIdConjoints(String string) {
        forIdConjoints = string;
    }

    /**
     * @param string
     */
    public void setIdConjoints(String string) {
        idConjoints = string;
    }

    /**
     * setter pour l'attribut id tiers depuis pyxis
     * 
     * <p>
     * Cette methode initialise un flag interne qui indique que cette methode a ete appellee lors d'un retour depuis
     * pyxis.
     * </p>
     * 
     * @param idTiers
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdTiersDepuisPyxis(String idTiers) throws Exception {
        super.setIdTiers(idTiers);

        SFTiersWrapper tierWrapper = SFTiersHelper.getTiersParId(getSession(), idTiers);

        super.setNss(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
        super.setNom(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_NOM));
        super.setPrenom(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_PRENOM));
        super.setCsSexe(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_SEXE));
        super.setDateNaissance(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_DATE_NAISSANCE));
        super.setDateDeces(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_DATE_DECES));
        super.setCsCantonDomicile(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_ID_CANTON));
        super.setCsNationalite(tierWrapper.getProperty(SFTiersWrapper.PROPERTY_ID_PAYS_DOMICILE));
        super.setIdAssure(idTiers);
        super.setProvenance(SFUtil.PROVENANCE_TIERS);

        retourDepuisPyxis = true;

    }

    /**
     * @param b
     */
    public void setRetourDepuisPyxis(boolean b) {
        retourDepuisPyxis = b;
    }

    /**
     * @param string
     */
    public void setTypeRelation(String string) {
        typeRelation = string;
    }

}
