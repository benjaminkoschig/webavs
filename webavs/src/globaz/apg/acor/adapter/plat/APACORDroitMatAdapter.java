package globaz.apg.acor.adapter.plat;

import globaz.apg.api.droits.IAPDroitMaternite;
import globaz.apg.db.droits.APDroitMaternite;
import globaz.apg.db.droits.APSituationFamilialeMat;
import globaz.apg.db.droits.APSituationFamilialeMatManager;
import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRPredicate;
import globaz.prestation.tools.impl.PRNSS13ChiffresUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * n <H1>Description</H1>
 * 
 * <p>
 * Sous-classe spécifique permettant la création des fichiers nécessaires au calcul des prestations materntié. Les
 * fichiers nécessaires et les implémentations utilisées sont:
 * </p>
 * 
 * <table>
 * <tr>
 * <td>fichier DEM_GEDO</td>
 * <td>globaz.prestation.acor.PRFichierVidePrinter</td>
 * </tr>
 * <tr>
 * <td>fichier DEMANDE</td>
 * <td>globaz.prestation.acor.PRFichierDemandeDefautPrinter</td>
 * </tr>
 * <tr>
 * <td>fichier EMPLOYEURS</td>
 * <td>APFichierEmployeurPrinter</td>
 * </tr>
 * <tr>
 * <td>fichier ASSURES_AMAT</td>
 * <td>APFichierAssuresMatPrinter</td>
 * </tr>
 * <tr>
 * <td>fichier ENFANTS</td>
 * <td>APFichierEnfantsMatPrinter</td>
 * </tr>
 * <tr>
 * <td>fichier FAMILLES</td>
 * <td>APFichierFamillesMatPrinter</td>
 * </tr>
 * </table>
 * 
 * @author vre
 */
public class APACORDroitMatAdapter extends APAbstractACORDroitAdapter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private static class EnfantPredicate implements PRPredicate {

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * DOCUMENT ME!
         * 
         * @param object
         *            DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         */
        @Override
        public boolean evaluer(Object object) {
            if (object instanceof APSituationFamilialeMat) {
                return IAPDroitMaternite.CS_TYPE_ENFANT.equals(((APSituationFamilialeMat) object).getType());
            } else {
                return false;
            }
        }
    }

    public static PRPredicate ENFANTS_PREDICATE = new EnfantPredicate();

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private static String NF_ASSURES_MAT = "ASSURES_AMAT";
    private List fichiers = null;
    private HashMap idNoAVSBidons = new HashMap();

    private HashMap idNSSBidons = new HashMap();
    private APSituationFamilialeMat mari = null;

    private PRTiersWrapper mariTiers = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private List situationsFamiliales = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APACORDroitMatAdapter.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param droit
     *            DOCUMENT ME!
     */
    APACORDroitMatAdapter(BSession session, APDroitMaternite droit) {
        super(session, droit);
    }

    /**
     * 
     * 
     * @param noAVS
     *            DOCUMENT ME!
     * @param csSexe
     *            DOCUMENT ME!
     * @param nomPrenom
     *            DOCUMENT ME!
     * @param conjoint
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    private String _noAVSBidon(String noAVS, String csSexe, String nomPrenom, boolean conjoint) {

        if (!JadeStringUtil.isEmpty(noAVS)) {
            return noAVS;
        }

        String retValue;

        // le sexe est defini dans la situation familiale.
        retValue = PRACORConst.CS_FEMME.equals(csSexe) ? PRACORConst.CA_NO_AVS_BIDON_FEMME
                : PRACORConst.CA_NO_AVS_BIDON_HOMME;

        /*
         * comme a la fois les conjoints et les enfants peuvent avoir un no avs vide, il est possible qu'un enfant et un
         * conjoint ait le meme no AVS dans le fichier ACOR, ce qui fait qu'ACOR ne pourra pas determiner qui est
         * l'enfant et qui est le conjoint. Pour regler ce probleme, on differencie les no AVS bidon en se basant sur le
         * type de relation et le nomPrenom
         */
        String idNoAVSBidon = conjoint + "_" + nomPrenom;
        String noUnique = (String) idNoAVSBidons.get(idNoAVSBidon);

        if (noUnique == null) {
            noUnique = String.valueOf(idNoAVSBidons.size() + 1);
            idNoAVSBidons.put(idNoAVSBidon, noUnique);
        }

        return noUnique + retValue.substring(noUnique.length());
    }

    /**
     * 
     * 
     * @param noAVS
     *            DOCUMENT ME!
     * @param csSexe
     *            DOCUMENT ME!
     * @param nomPrenom
     *            DOCUMENT ME!
     * @param conjoint
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    private String _nssBidon(String nss, String csSexe, String nomPrenom, boolean conjoint) {

        if (!JadeStringUtil.isEmpty(nss)) {
            return nss;
        }

        String idNssBidon = conjoint + "_" + nomPrenom;

        // Prendre un nss de la liste des 25 et voir s'il existe déjà dans la
        // map (itérer),
        // s'il existe, prendre un autre et retest, s'il existe pas, le
        // retourner et l'insérer.

        boolean isOK = false;
        boolean isEqual = false;
        String nss13 = "";
        int increment = 0;

        while (!isOK) {

            nss13 = PRNSS13ChiffresUtils.getNSSErrone(increment);

            Set keys = idNSSBidons.keySet();

            for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                String nssKey = (String) idNSSBidons.get(key);

                if (nssKey.equals(nss13)) {
                    isEqual = true;
                    break;
                }

            }

            if (!isEqual) {
                isOK = true;
                idNoAVSBidons.put(idNssBidon, nss13);
            } else {
                increment++;
                isEqual = false;
            }

        }

        // le sexe est defini dans la situation familiale.
        // String nss13 = PRNSS13ChiffresUtils.getRandomNSS(getSession());

        /*
         * comme a la fois les conjoints et les enfants peuvent avoir un no avs vide, il est possible qu'un enfant et un
         * conjoint ait le meme no AVS dans le fichier ACOR, ce qui fait qu'ACOR ne pourra pas determiner qui est
         * l'enfant et qui est le conjoint. Pour regler ce probleme, on differencie les no AVS bidon en se basant sur le
         * type de relation et le nomPrenom
         */

        String noUnique = (String) idNSSBidons.get(idNssBidon);

        if (noUnique == null) {
            noUnique = nss13;
            idNSSBidons.put(idNssBidon, noUnique);
        }
        return noUnique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.plat.PRAbstractPlatAdapter#getDateDepot()
     */
    @Override
    public String getDateDepot() {
        return droit.getDateDepot();
    }

    /**
     * getter pour l'attribut date determinante.
     * 
     * @return la valeur courante de l'attribut date determinante
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public String getDateDeterminante() throws PRACORException {
        return droit.getDateDebutDroit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.plat.PRAbstractPlatAdapter#getDateTraitement()
     */
    @Override
    public String getDateTraitement() {
        return null;
    }

    // /**
    // * DOCUMENT ME!
    // *
    // * @param index DOCUMENT ME!
    // *
    // * @return DOCUMENT ME!
    // *
    // * @throws PRACORException DOCUMENT ME!
    // */
    // public String noAVSBidon(int index) throws PRACORException {
    // if (noAVSBidons == null) {
    // try {
    // noAVSBidons =
    // PRAVSUtils.getInstance(PRACORConst.CA_NSS_VIDE).iteratorNoAVSBidon();
    // } catch (JAException e) {
    // throw new PRACORException("impossible de trouver une liste de no AVS",
    // e);
    // }
    // }
    //
    // if (listeNoAVS == null) {
    // listeNoAVS = new ArrayList();
    // }
    //
    // for (int idNoAVS = listeNoAVS.size(); idNoAVS <= index; ++idNoAVS) {
    // listeNoAVS.add(noAVSBidons.next());
    // }
    //
    // return (String) listeNoAVS.get(index);
    // }
    //
    //

    /**
     * getter pour l'attribut fichiers ACOR.
     * 
     * @return la valeur courante de l'attribut fichiers ACOR
     */
    @Override
    public List getFichiersACOR() {
        if (fichiers == null) {
            fichiers = new LinkedList();
            fichiers.add(fichierDemGEDOPrinter());
            fichiers.add(fichierDemandeDefautPrinter());
            fichiers.add(fichierEmployeurPrinter());
            fichiers.add(new APFichierAssuresMatPrinter(this, NF_ASSURES_MAT));
            fichiers.add(new APFichierEnfantsMatPrinter(this, PRACORConst.NF_ENFANTS));
            fichiers.add(new APFichierFamillesMatPrinter(this, PRACORConst.NF_FAMILLES));
        }

        return fichiers;
    }

    /**
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public String idTiersAssure() throws PRACORException {
        return tiers().getProperty(PRTiersWrapper.PROPERTY_ID_TIERS);
    }

    /**
     * getter pour l'attribut mari saisi.
     * 
     * @return la valeur courante de l'attribut mari saisi
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public boolean isMariSaisi() throws PRACORException {
        return mari() != null;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public APSituationFamilialeMat mari() throws PRACORException {
        if (mari == null) {
            for (Iterator situations = situationsFamiliales().iterator(); situations.hasNext();) {
                APSituationFamilialeMat situation = (APSituationFamilialeMat) situations.next();

                if (IAPDroitMaternite.CS_TYPE_PERE.equals(situation.getType())) {
                    mari = situation;

                    break;
                }
            }
        }

        return mari;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public PRTiersWrapper mariTiers() throws PRACORException {
        if (isMariSaisi() && (mariTiers == null)) {
            try {
                mariTiers = PRTiersHelper.getTiers(getSession(), mari().getNoAVS());
            } catch (Exception e) {
                throw new PRACORException(getSession().getLabel("ERROR_CHARGEMENT_TIERS_MARI"), e);
            }
        }

        return mariTiers;
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * @return le numero AVS du pere s'il existe. le numeroAVSPereBidon sinon
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public String noAVSPere() throws PRACORException {
        if ((mari() != null) && !JadeStringUtil.isIntegerEmpty(mari().getNoAVS())) {
            return mari().getNoAVS();
        } else {
            return nssBidon("", PRACORConst.CS_HOMME, "Pere", true);
        }
    }

    public String nssBidon(String nss, String csSexe, String nomPrenom, boolean conjoint) {

        if (!JadeStringUtil.isEmpty(nss)) {
            return nss;
        }

        try {
            // Recherche du tiers requérant...
            String idTiersRequerant = getDroit().loadDemande().getIdTiers();
            PRTiersWrapper tw = PRTiersHelper.getTiersParId(getSession(), idTiersRequerant);
            String nssRequerant = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            nssRequerant = NSUtil.unFormatAVS(nssRequerant);

            // NNSS
            if (nssRequerant.length() > 11) {
                return _nssBidon(nss, csSexe, nomPrenom, conjoint);
            }
            // NAVS
            else {
                return _noAVSBidon(nss, csSexe, nomPrenom, conjoint);
            }

        } catch (Exception e) {
            return _nssBidon(nss, csSexe, nomPrenom, conjoint);
        }

    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public List situationsFamiliales() throws PRACORException {
        if (situationsFamiliales == null) {
            APSituationFamilialeMatManager mgr = new APSituationFamilialeMatManager();

            mgr.setSession(session);
            mgr.setForIdDroitMaternite(droit.getIdDroit());

            // on veut que les enfants soient regroupés (en particulier, on ne
            // veut que le pere soit entre deux enfants)
            mgr.setOrderBy(APSituationFamilialeMat.FIELDNAME_TYPE);

            try {
                mgr.find();
            } catch (Exception e) {
                throw new PRACORException(session.getLabel("ERREUR_CHARGEMENT_SITUATION_FAMILIALE"), e);
            }

            situationsFamiliales = mgr.getContainer();
        }

        return situationsFamiliales;
    }
}
