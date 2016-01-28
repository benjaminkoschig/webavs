package globaz.hera.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.util.JAUtil;
import globaz.hera.api.ISFEnfant;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFPeriode;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Implémentation des méthodes de l'interface du domaine d'application standard, Les implémentation des autres domaines
 * sont à voir dans le package: globaz.hera.api.indemniteJournaliere/.../ISFSituationFamilialeHelper
 * 
 * @author mmu
 * 
 *         <p>
 *         13 oct. 05
 *         </p>
 */

public class ISFSituationFamilialeHelper extends GlobazHelper implements ISFSituationFamiliale {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * *
     */
    public static final String ENTITY_CLASS_NAME = "globaz.hera.impl.standard.SFSituationFamiliale";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe ISFSituationFamilialeHelper.
     */
    public ISFSituationFamilialeHelper() {
        super(ISFSituationFamilialeHelper.ENTITY_CLASS_NAME);
    }

    /**
     * Crée une nouvelle instance de la classe ISFSituationFamilialeHelper.
     * 
     * @param entity_class_name
     *            DOCUMENT ME!
     */
    public ISFSituationFamilialeHelper(String entity_class_name) {
        super(entity_class_name);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    @Override
    public ISFMembreFamille addMembreCelibataire(String idTiers) throws Exception {
        Object result = _getObject("addMembreCelibataire", new Object[] { idTiers });
        ISFMembreFamille membre = (ISFMembreFamille) result;
        return membre;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getDetailEnfant(java.lang.String)
     * 
     * @param idMembreFamille
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut enfant
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public ISFEnfant getEnfant(String idMembreFamille) throws Exception {
        Object result = _getObject("getEnfant", new Object[] { idMembreFamille });
        ISFEnfant enfant = (ISFEnfant) result;

        return enfant;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getMembresFamille(java.lang.String)
     * 
     * @param idMembre
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut membre famille
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public ISFMembreFamille getMembreFamille(String idMembre) throws Exception {
        Object result = _getObject("getMembreFamille", new Object[] { idMembre });
        ISFMembreFamille mbrFam = (ISFMembreFamille) result;

        return mbrFam;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getMembresFamille(java.lang.String, java.lang.String)
     * 
     * @param idMembre
     *            DOCUMENT ME!
     * @param date
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut membre famille
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public ISFMembreFamille getMembreFamille(String idMembre, String date) throws Exception {
        Object result = _getObject("getMembreFamille", new Object[] { idMembre, date });
        ISFMembreFamille mbrFam = (ISFMembreFamille) result;

        return mbrFam;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getMembresFamille(java.lang.String, java.lang.String)
     * 
     * @param idTiers
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut membre famille
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public ISFMembreFamilleRequerant[] getMembresFamille(String idTiers) throws Exception {
        Object result = _getObject("getMembresFamille", new Object[] { idTiers });
        ISFMembreFamilleRequerant[] mbrFam = (ISFMembreFamilleRequerant[]) result;

        return mbrFam;
    }

    @Override
    public ISFMembreFamille[] getMembresFamilleEtendue(String idMembreFamille, Boolean inclureParents) throws Exception {
        Object result = _getObject("getMembresFamilleEtendue", new Object[] { idMembreFamille, inclureParents });
        ISFMembreFamille[] fam = (ISFMembreFamille[]) result;
        return fam;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getMembresFamille(java.lang.String)
     * 
     * @param idTiers
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut membre famille requerant
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public ISFMembreFamilleRequerant[] getMembresFamilleRequerant(String idTiers) throws Exception {
        Object result = _getObject("getMembresFamilleRequerant", new Object[] { idTiers });
        ISFMembreFamilleRequerant[] mbrFam = (ISFMembreFamilleRequerant[]) result;

        return mbrFam;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getMembresFamille(java.lang.String, java.lang.String)
     * 
     * @param idTiers
     *            DOCUMENT ME!
     * @param date
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut membre famille requerant
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public ISFMembreFamilleRequerant[] getMembresFamilleRequerant(String idTiers, String date) throws Exception {
        Object result = _getObject("getMembresFamilleRequerant", new Object[] { idTiers, date });
        ISFMembreFamilleRequerant[] mbrFam = (ISFMembreFamilleRequerant[]) result;

        return mbrFam;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getMembresFamilleRequerantParMbrFamille (java.lang.String)
     */
    @Override
    public ISFMembreFamilleRequerant[] getMembresFamilleRequerantParMbrFamille(String idMembreFamille) throws Exception {
        Object result = _getObject("getMembresFamilleRequerantParMbrFamille", new Object[] { idMembreFamille });
        ISFMembreFamilleRequerant[] mbrFam = (ISFMembreFamilleRequerant[]) result;

        return mbrFam;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getParentsRequerant(java.lang.String)
     * 
     * @param idTiers
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut parent famille
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public ISFMembreFamille[] getParents(String idTiers) throws Exception {
        Object result = _getObject("getParents", new Object[] { idTiers });
        ISFMembreFamille[] parFam = (ISFMembreFamille[]) result;

        return parFam;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getParentsRequerantParMbrFamille (java.lang.String)
     */
    @Override
    public ISFMembreFamille[] getParentsParMbrFamille(String idMembreFamille) throws Exception {
        Object result = _getObject("getParentsParMbrFamille", new Object[] { idMembreFamille });
        ISFMembreFamille[] parFam = (ISFMembreFamille[]) result;

        return parFam;
    }

    /**
     * @see globaz.hera.api.ISFSituationFamiliale#getPeriode(java.lang.String, java.lang.String)
     * 
     * @param idMembreFamille
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut periode
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public ISFPeriode[] getPeriodes(String idMembreFamille) throws Exception {
        Object result = _getObject("getPeriodes", new Object[] { idMembreFamille });
        ISFPeriode[] periodes = (ISFPeriode[]) result;

        return periodes;
    }

    /**
     * @see globaz.hera.api.ISFSituationFamiliale#getPeriode(java.lang.String, java.lang.String)
     * 
     * @param idMembreFamille
     *            DOCUMENT ME!
     * @param typePeriode
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut periode
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public ISFPeriode[] getPeriodes(String idMembreFamille, String typePeriode) throws Exception {
        if (typePeriode == null) {
            typePeriode = "";
        }

        Object result = _getObject("getPeriodes", new Object[] { idMembreFamille, typePeriode });
        ISFPeriode[] periodes = (ISFPeriode[]) result;

        return periodes;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getRelationFamiliale(java.lang.String, java.lang.String)
     * 
     * @param idTiersRequerant
     *            DOCUMENT ME!
     * @param date
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut relation familiale
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public ISFRelationFamiliale[] getRelationsConjoints(String idTiersRequerant, String date) throws Exception {
        if (JAUtil.isDateEmpty(date) || JadeStringUtil.isIntegerEmpty(date)) {
            date = "";
        }

        Object result = _getObject("getRelationsConjoints", new Object[] { idTiersRequerant, date });
        ISFRelationFamiliale[] relFam = (ISFRelationFamiliale[]) result;

        return relFam;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getRelationFamilialeEtendue(java.lang.String, java.lang.String)
     * 
     * @param idTiersRequerant
     *            DOCUMENT ME!
     * @param date
     *            DOCUMENT ME!
     * 
     * @return la valeur courante de l'attribut relation familiale etendue
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    public ISFRelationFamiliale[] getRelationsConjointsEtendues(String idTiersRequerant, String date) throws Exception {
        if (JAUtil.isDateEmpty(date) || JadeStringUtil.isIntegerEmpty(date)) {
            date = "";
        }

        Object result = _getObject("getRelationsConjointsEtendues", new Object[] { idTiersRequerant, date });
        ISFRelationFamiliale[] relFam = (ISFRelationFamiliale[]) result;

        return relFam;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getRelationsConjoints(java.lang .String, java.lang.String)
     */
    @Override
    public ISFRelationFamiliale[] getToutesRelationsConjoints(String membreFamille1, String membreFamille2,
            Boolean triDateDebutDecroissant) throws Exception {
        Object result = _getObject("getToutesRelationsConjoints", new Object[] { membreFamille1, membreFamille2,
                triDateDebutDecroissant });
        ISFRelationFamiliale[] relFam = (ISFRelationFamiliale[]) result;
        return relFam;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getRelationsConjoints(java.lang .String, java.lang.String)
     */
    @Override
    public ISFRelationFamiliale[] getToutesRelationsConjointsParIdTiers(String idTiers1, String idTiers2,
            Boolean triDateDebutDecroissant) throws Exception {
        Object result = _getObject("getToutesRelationsConjointsParIdTiers", new Object[] { idTiers1, idTiers2,
                triDateDebutDecroissant });
        ISFRelationFamiliale[] relFam = (ISFRelationFamiliale[]) result;
        return relFam;
    }

    @Override
    public Boolean isRequerant(String idTiersRequerant, String csDomaine) throws Exception {
        Object result = _getObject("isRequerant", new Object[] { idTiersRequerant, csDomaine });
        Boolean b = (Boolean) result;
        return b;
    }

    @Override
    public void setDomaine(String domaine) throws Exception {
        _getValueObject().setProperty("domaine", domaine);

    }

}
