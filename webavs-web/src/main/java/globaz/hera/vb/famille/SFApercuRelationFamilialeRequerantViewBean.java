/*
 * Créé le 10 juin 05
 */
package globaz.hera.vb.famille;

import globaz.commons.nss.NSUtil;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.hera.db.famille.SFApercuRelationFamilialeRequerant;
import globaz.hera.db.famille.SFApercuRequerant;
import globaz.hera.db.famille.SFApercuRequerantManager;
import globaz.hera.db.famille.SFConjointManager;
import globaz.hera.db.famille.SFEnfantManager;
import globaz.hera.db.famille.SFRelationFamilialeRequerant;
import globaz.hera.db.famille.SFRequerantDTO;
import globaz.hera.helpers.famille.SFRequerantHelper;
import globaz.hera.tools.nss.SFUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.util.Vector;
import javax.servlet.http.HttpSession;

/**
 * <H1>Description</H1>
 * 
 * 
 * @author dvh
 */
public class SFApercuRelationFamilialeRequerantViewBean extends SFApercuRelationFamilialeRequerant implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean lookedDTO = false;
    private SFRequerantDTO requerantDTO = null;

    public Vector getCsDomaines() throws Exception {
        String[] elm;
        Vector v = new Vector();
        SFApercuRequerantManager mgr = new SFApercuRequerantManager();
        mgr.setSession(getSession());
        mgr.setForIdTiers(getIdTiers());
        mgr.find();
        for (int i = 0; i < mgr.size(); i++) {
            SFApercuRequerant req = (SFApercuRequerant) mgr.getEntity(i);

            elm = new String[2];
            elm[0] = req.getIdDomaineApplication();
            elm[1] = getSession().getCodeLibelle(req.getIdDomaineApplication());
            v.add(elm);
        }

        return v;

    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerantNormal() {

        if (JadeStringUtil.isEmpty(getNss())) {
            setNss("000.00.000.000");
        }

        return SFUtil.formatDetailRequerantListe(getNss(), getNom() + " " + getPrenom(), getDateNaissance(),
                getLibelleSexe(), getLibellePays());

    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerantSpecial() {

        if (JadeStringUtil.isEmpty(getNss())) {
            setNss("000.00.000.000");
        }

        return SFUtil.formatDetailRequerantListeSpecial(getNss(), getNom() + " " + getPrenom(), getDateNaissance(),
                getLibelleSexe(), getLibellePays());

    }

    public String getIdRequerant(HttpSession session) {
        if (hasRequerantDTO(session)) {
            return getRequerantDTO(session).getIdRequerant();
        } else {
            return null;
        }
    }

    @Override
    public String getLibellePays() {

        String pays = "";

        if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(getPays())) {
            pays = getPays();
        } else {
            pays = getCsNationalite();
        }

        return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", pays));
    }

    public String getLibelleRelationAuRequerant(String idRequerant, String idMembreFamille) {
        return getSession().getCodeLibelle(getRelationAuRequerant(idRequerant));
    }

    @Override
    public String getLibelleSexe() {
        return getSession().getCodeLibelle(getCsSexe());
    }

    @Override
    public String getNumeroAvsFormateSansPrefixe() {
        return NSUtil.formatWithoutPrefixe(getNss(), isNNSS().equals("true") ? true : false);
    }

    public SFRequerantDTO getRequerantDTO(HttpSession session) {
        if (!lookedDTO) {
            if (requerantDTO == null) {
                SFRequerantHelper rh = new SFRequerantHelper();
                requerantDTO = rh.getRequerantDTO(session);
            }
            lookedDTO = true;
        }
        return requerantDTO;
    }

    /**
     * determine si le tiers dispose d'une adresse de domicile dans Pyxis
     * 
     * @return
     */
    public boolean hasAdresseDomicile() {

        boolean result = false;

        if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {

            try {
                if (!JadeStringUtil.isEmpty(PRTiersHelper
                        .getAdresseCourrierFormatee(getSession(), getIdTiers(), "", ""))) {
                    result = true;
                }

            } catch (Exception e) {
                result = false;
            }
        }
        return result;
    }

    public boolean hasRequerantDTO(HttpSession session) {
        return (getRequerantDTO(session) != null);
    }

    /**
     * true si l'effacement du membre est possible: - pas de numero avs - pas dans la table SFCONJOI : WJICO1 et WJICO2
     * - pas dans la table SFENFANT : WIIMEF
     * 
     * @return
     */
    public boolean isDeletable() {

        // pas de num AVS
        if (JadeStringUtil.isEmpty(getNSS())) {

            SFConjointManager conjointManager = new SFConjointManager();
            conjointManager.setSession(getSession());
            conjointManager.setForIdConjoint(getId());

            try {
                // pas dans la table des conjoints
                if (conjointManager.getCount() == 0) {

                    SFEnfantManager enfantManager = new SFEnfantManager();
                    enfantManager.setSession(getSession());
                    enfantManager.setForIdMembreFamille(getId());

                    // pas dans la table des enfants
                    if (enfantManager.getCount() == 0) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * détermine si le membre fait partie de la famille du requerant
     * 
     * @return
     */
    public boolean isMembreFamilleRequerant(String idMembreFamille, String idRequerant) {
        if (!JadeStringUtil.isNull(idRequerant)) {
            SFRelationFamilialeRequerant requerant = new SFRelationFamilialeRequerant();
            requerant.setSession(getSession());
            requerant.setIdMembreFamille(idMembreFamille);
            requerant.setIdRequerant(idRequerant);
            requerant.setAlternateKey(SFRelationFamilialeRequerant.ALTERNATE_KEY_MEMBRE_REQ);
            try {
                requerant.retrieve();
                if (requerant.isNew()) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
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

    public void retrieveMembre(String idMembreFamille) {
        try {
            setIdMembreFamille(idMembreFamille);
            retrieve();
        } catch (Exception e) {
            JadeLogger.warn(getSession().getLabel("ERROR_MEMBRE_FAMILLE_NON_TROUVE") + getIdMembreFamille(), e);
        }
    }

    /**
     * Fixe les parametres du requerant DTO sur le viewBean
     * 
     * @param requerantDTO
     */
    public void setParameters(SFRequerantDTO requerant) {
        setIdRequerant(requerant.getIdRequerant());
        setIdMembreFamille(requerant.getIdMembreFamille());
        setIdTiers(requerant.getIdTiers());
        setNss(requerant.getNss());
        setNom(requerant.getNom());
        setPrenom(requerant.getPrenom());
        setCsSexe(requerant.getCsSexe());
        setDateNaissance(requerant.getDateNaissance());
        setDateDeces(requerant.getDateDeces());
        setCsCantonDomicile(requerant.getCsCantonDomicile());
        setPays(requerant.getPays());
        setCsNationalite(requerant.getCsNationalite());
        setCsDomaineApplication(requerant.getIdDomaineApplication());
    }
}
