/*
 * Créé le 16 sept. 05
 *
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.vb.famille;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.interfaces.tiers.SFTiersHelper;
import globaz.hera.interfaces.tiers.SFTiersWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * @author jpa
 * <p>
 * Pour changer le modèle de ce commentaire de type généré, allez à :
 * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
@Slf4j
public class SFPeriodeViewBean extends SFPeriode implements FWViewBeanInterface {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String SEPARATOR = " / ";
    private String csDomaine;
    private String dateNaissance;
    private String libellePays;
    private String libelleSexe;
    private String nomPrenom;
    private String numAvs;

    public String getCsDomaine() {
        return csDomaine;
    }

    /**
     * @return
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getLibellePays() {
        return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", libellePays));
    }

    /**
     * @return
     */
    public String getLibelleSexe() {
        return getSession().getCodeLibelle(libelleSexe);
    }

    /**
     * @return
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * @return
     */
    public String getNumAvs() {
        return numAvs;
    }

    // pour retrouver un membre d'après l'idMembreFamille
    public void retrieveMembre(BISession session, String idMembre) {
        SFMembreFamille membre = new SFMembreFamille();
        membre.setSession((BSession) session);
        membre.setIdMembreFamille(idMembre);
        try {
            membre.retrieve();
            if (!membre.isNew()) {
                setNumAvs(membre.getNss());
                setNomPrenom(membre.getNom() + " " + membre.getPrenom());
                setDateNaissance(membre.getDateNaissance());
                setLibellePays(membre.getPays());
                if (!JadeStringUtil.isEmpty(getLibellePays())) {
                    setLibellePays(membre.getCsNationalite());
                }
                setLibelleSexe(membre.getCsSexe());
                setCsDomaine(membre.getCsDomaineApplication());
            }
        } catch (Exception e) {
            JadeLogger.warn("Erreur retrieve periode_rc : retrieveMembre()", e);
        }
    }

    public String getTiersRequerant() {
        SFTiersWrapper tiers;
        StringBuilder nomRecueillant = new StringBuilder();
        if (StringUtils.isNotEmpty(getNoAvsRecueillant())) {
            try {
                tiers = SFTiersHelper.getTiers(getSession(), getNoAvsRecueillant());
                if (Objects.nonNull(tiers)) {
                    addStringWithSeparator(nomRecueillant, tiers.getProperty(SFTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                    addStringWithSeparator(nomRecueillant, new StringBuilder(tiers.getProperty(SFTiersWrapper.PROPERTY_NOM)).append(" ").append(tiers.getProperty(SFTiersWrapper.PROPERTY_PRENOM)).toString());
                    addStringWithSeparator(nomRecueillant, tiers.getProperty(SFTiersWrapper.PROPERTY_DATE_NAISSANCE));
                    addStringWithSeparator(nomRecueillant, getSession().getCodeLibelle(tiers.getProperty(SFTiersWrapper.PROPERTY_SEXE)));
                    addStringWithSeparator(nomRecueillant, getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", tiers.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE))));
                }
            } catch (Exception e) {
                LOG.error("Impossible de récupérer le tiers recueillant. ", e);
                return StringUtils.EMPTY;
            }
        }
        return nomRecueillant.toString();
    }

    private void addStringWithSeparator(StringBuilder nomRecueillant, String param) {
        if (StringUtils.isNotEmpty(param)) {
            if (StringUtils.isNotEmpty(nomRecueillant.toString())) {
                nomRecueillant.append(SEPARATOR);
            }
            nomRecueillant.append(param);
        }
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    /**
     * @param string
     */
    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    /**
     * @param string
     */
    public void setLibellePays(String string) {
        libellePays = string;
    }

    /**
     * @param string
     */
    public void setLibelleSexe(String string) {
        libelleSexe = string;
    }

    /**
     * @param string
     */
    public void setNomPrenom(String string) {
        nomPrenom = string;
    }

    /**
     * @param string
     */
    public void setNumAvs(String string) {
        numAvs = string;
    }

}
