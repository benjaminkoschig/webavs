package globaz.ij.acor.adapter.plat;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRFichierACORPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRFilterIterator;
import globaz.prestation.tools.PRPredicate;
import globaz.prestation.tools.impl.PRNSS13ChiffresUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class IJACORPrononceAdapter extends PRAbstractPlatAdapter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    private class ISFMembreFamilleRequerantWrapper implements ISFMembreFamilleRequerant {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private ISFMembreFamilleRequerant membre;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Crée une nouvelle instance de la classe ISFMembreFamilleRequerantWrapper.
         * 
         * @param membre
         *            DOCUMENT ME!
         */
        public ISFMembreFamilleRequerantWrapper(ISFMembreFamilleRequerant membre) {
            this.membre = membre;
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * getter pour l'attribut cs canton domicile.
         * 
         * @return la valeur courante de l'attribut cs canton domicile
         */
        @Override
        public String getCsCantonDomicile() {
            return membre.getCsCantonDomicile();
        }

        @Override
        public String getCsEtatCivil() {
            return membre.getCsEtatCivil();
        }

        /**
         * getter pour l'attribut cs nationalite.
         * 
         * @return la valeur courante de l'attribut cs nationalite
         */
        @Override
        public String getCsNationalite() {
            return membre.getCsNationalite();
        }

        /**
         * getter pour l'attribut cs sexe.
         * 
         * @return la valeur courante de l'attribut cs sexe
         */
        @Override
        public String getCsSexe() {
            return membre.getCsSexe();
        }

        /**
         * getter pour l'attribut date deces.
         * 
         * @return la valeur courante de l'attribut date deces
         */
        @Override
        public String getDateDeces() {
            return membre.getDateDeces();
        }

        /**
         * getter pour l'attribut date naissance.
         * 
         * @return la valeur courante de l'attribut date naissance
         */
        @Override
        public String getDateNaissance() {
            return membre.getDateNaissance();
        }

        /**
         * getter pour l'attribut id membre famille.
         * 
         * @return la valeur courante de l'attribut id membre famille
         */
        @Override
        public String getIdMembreFamille() {
            return membre.getIdMembreFamille();
        }

        /*
         * (non-Javadoc)
         * 
         * @see globaz.hera.api.ISFMembreFamilleRequerant#getIdTiers()
         */
        @Override
        public String getIdTiers() {
            return membre.getIdTiers();
        }

        /**
         * getter pour l'attribut nom.
         * 
         * @return la valeur courante de l'attribut nom
         */
        @Override
        public String getNom() {
            return membre.getNom();
        }

        /**
         * Retourne le no AVS du membre de la situation familiale ou un no AVS bidon pour les membres qui ont ete saisi
         * sans no AVS.
         * 
         * @return la valeur courante de l'attribut nss
         */
        @Override
        public String getNss() {

            if (JadeStringUtil.isEmpty(membre.getNss()) || JadeStringUtil.isIntegerEmpty(membre.getNss())) {
                return nssBidon(membre.getNss(), membre.getCsSexe(), membre.getNom() + membre.getPrenom(), !membre
                        .getRelationAuRequerant().equals(ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT));
            } else {
                return membre.getNss();
            }
        }

        /**
         * getter pour l'attribut nss direct.
         * 
         * @return la valeur courante de l'attribut nss direct
         */
        public String getNssDirect() {
            return membre.getNss();
        }

        /**
         * getter pour l'attribut prenom.
         * 
         * @return la valeur courante de l'attribut prenom
         */
        @Override
        public String getPrenom() {
            return membre.getPrenom();
        }

        @Override
        public String getPays() {
            return membre.getPays();
        }

        /**
         * getter pour l'attribut relation au requerant.
         * 
         * @return la valeur courante de l'attribut relation au requerant
         */
        @Override
        public String getRelationAuRequerant() {
            return membre.getRelationAuRequerant();
        }
    }

    // bz-NEW_ACOR_IJ
    // public static final String NF_ASSURES_IJ = "ASSURES_IJ";

    // bz-NEW_ACOR_IJ
    // public static final String NF_DECOMPTE = "DECOMPTE";
    public static final String NF_IJ = "IJ";

    // bz-NEW_ACOR_IJ
    // public static final String NF_REVENU = "REVENU";

    // un prédicat pour retourner les conjoints du requerant
    private static final PRPredicate PREDICATE_CONJOINTS = new PRPredicate() {

        // Le conjoint inconnu n'est pas retourné
        @Override
        public boolean evaluer(Object object) {
            if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(((ISFMembreFamilleRequerant) object)
                    .getIdMembreFamille())) {

                return false;
            } else {
                return ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(((ISFMembreFamilleRequerant) object)
                        .getRelationAuRequerant());
            }
        }
    };

    // un prédicat pour retourner les enfants d'un requerant
    private static final PRPredicate PREDICATE_ENFANTS = new PRPredicate() {
        @Override
        public boolean evaluer(Object object) {
            return ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT.equals(((ISFMembreFamilleRequerant) object)
                    .getRelationAuRequerant());
        }
    };

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // un predicat pour retourner les membres de la famille sans le requerant
    private static final PRPredicate PREDICATE_FAMILLE = new PRPredicate() {
        @Override
        public boolean evaluer(Object object) {
            return !ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(((ISFMembreFamilleRequerant) object)
                    .getRelationAuRequerant());
        }
    };
    private List<PRFichierACORPrinter> fichiers;
    private String idMembreFamilleRequerant;
    private HashMap idNoAVSBidons = new HashMap();

    private HashMap idNSSBidons = new HashMap();
    private List membres;

    private IJPrononce prononce;
    private HashMap relations = new HashMap();

    private ISFMembreFamille requerant;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private ISFSituationFamiliale sf;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJACORPrononceAdapter.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     */
    IJACORPrononceAdapter(BSession session, IJPrononce prononce) {
        super(session);
        this.prononce = prononce;
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

        if (JadeStringUtil.isIntegerEmpty(csSexe)) {
            /*
             * le sexe n'est pas stocke dans la situation familliale, par defaut on va prendre le sexe oppose au
             * requerant, de cette manière, les eventuelles relations de conjoint seront acceptees par ACOR
             */
            try {
                retValue = PRACORConst.CS_FEMME.equals(this.requerant().getCsSexe()) ? PRACORConst.CA_NO_AVS_BIDON_HOMME
                        : PRACORConst.CA_NO_AVS_BIDON_FEMME;
            } catch (PRACORException e) {
                // par defaut une femme
                retValue = PRACORConst.CA_NO_AVS_BIDON_FEMME;
            }
        } else {
            // le sexe est defini dans la situation familiale.
            retValue = PRACORConst.CS_FEMME.equals(csSexe) ? PRACORConst.CA_NO_AVS_BIDON_FEMME
                    : PRACORConst.CA_NO_AVS_BIDON_HOMME;
        }

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

    /**
     * retourne tous les conjoints d'un requerant.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public Iterator conjoints() throws PRACORException {
        return new PRFilterIterator(membres().iterator(), IJACORPrononceAdapter.PREDICATE_CONJOINTS);
    }

    /**
     * retourne tous les enfants d'un requerant.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public Iterator enfants() throws PRACORException {
        return new PRFilterIterator(membres().iterator(), IJACORPrononceAdapter.PREDICATE_ENFANTS);
    }

    /**
     * retourne tous les membres de la famille d'un requerant.
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public Iterator famille() throws PRACORException {
        return new PRFilterIterator(membres().iterator(), IJACORPrononceAdapter.PREDICATE_FAMILLE);
    }

    /**
     * getter pour l'attribut date debut droit
     * 
     * @return la valeur courante de l'attribut date debut droit
     */
    public String getDateDebutDroit() {
        return getPrononce().getDateDebutPrononce();
    }

    /**
     * getter pour l'attribut dateDepot
     * 
     */
    @Override
    public String getDateDepot() {
        return "0";
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
        return JACalendar.todayJJsMMsAAAA();
    }

    /**
     * getter pour l'attribut dateTraitement
     * 
     */
    @Override
    public String getDateTraitement() {
        return null;
    }

    /**
     * getter pour l'attribut fichiers ACOR.
     * 
     * @return la valeur courante de l'attribut fichiers ACOR
     */
    @Override
    public List<PRFichierACORPrinter> getFichiersACOR() {
        if (fichiers == null) {
            fichiers = new LinkedList<PRFichierACORPrinter>();
            fichiers.add(fichierDemandeDefautPrinter());
            // bz-NEW_ACOR_IJ
            fichiers.add(new IJFichierAssuresPrinter(this, PRACORConst.NF_ASSURES));
            fichiers.add(new IJFichierIJPrinter(this, IJACORPrononceAdapter.NF_IJ));
            fichiers.add(new IJFichierFamillePrinter(this, PRACORConst.NF_FAMILLES));
            fichiers.add(new IJFichierEnfantPrinter(this, PRACORConst.NF_ENFANTS));
            fichiers.add(new IJFichierPeriodePrinter(this, PRACORConst.NF_PERIODES));
            fichiers.add(fichierDemGEDOPrinter());
            //
            // // bz-NEW_ACOR_IJ
            // // this.fichiers.add(new IJFichierRevenuPrinter(this, IJACORPrononceAdapter.NF_REVENU));
            //
            fichiers.add(new IJFichierRenteEnCours(this, PRACORConst.NF_RENTES));
            fichiers.add(new IJFichierEuroFormPrinter(this, PRACORConst.NF_EURO_FORM));

            // bz-NEW_ACOR_IJ
            // fichier de decompte vide pour forcer globaz.ij.acor a generere
            // les decomptes justement...
            // this.fichiers.add(new IJFichierDecompteVidePrinter(this, IJACORPrononceAdapter.NF_DECOMPTE));
        }

        return fichiers;
    }

    /**
     * getter pour l'attribut prononce.
     * 
     * @return la valeur courante de l'attribut prononce
     */
    public IJPrononce getPrononce() {
        return prononce;
    }

    /**
     * getter pour l'attribut type calcul.
     * 
     * @return la valeur courante de l'attribut type calcul
     */
    @Override
    public String getTypeCalcul() {
        return PRACORConst.CA_TYPE_CALCUL_STANDARD;
    }

    /**
     * getter pour l'attribut type demande.
     * 
     * @return la valeur courante de l'attribut type demande
     */
    @Override
    public String getTypeDemande() {
        return PRACORConst.CA_TYPE_DEMANDE_IJ;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public String idMembreFamilleRequerant() throws PRACORException {
        if (idMembreFamilleRequerant == null) {
            trouverRequerant(null);
        }

        return idMembreFamilleRequerant;
    }

    /**
     * DOCUMENT ME!
     * 
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
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public List membres() throws PRACORException {
        // charger la situation familliale
        if (membres == null) {
            ISFSituationFamiliale sf = situationFamiliale(idTiersAssure());

            try {
                ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamilleRequerant(idTiersAssure(),
                        getDateDeterminante());

                membres = new LinkedList();

                for (int idMembre = 0; idMembre < membresFamille.length; ++idMembre) {
                    membres.add(new ISFMembreFamilleRequerantWrapper(membresFamille[idMembre]));
                }
            } catch (Exception e) {
                throw new PRACORException("impossible de charger la situation familiale", e);
            }
        }

        return membres;
    }

    public String nssBidon(String nss, String csSexe, String nomPrenom, boolean conjoint) {

        if (!JadeStringUtil.isEmpty(nss)) {
            return nss;
        }

        try {
            // Recherche du tiers requérant...
            String idTiersRequerant = getPrononce().loadDemande(null).getIdTiers();
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
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public String numeroAVSAssure() throws PRACORException {
        return tiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param idM1
     *            DOCUMENT ME!
     * @param idM2
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public ISFRelationFamiliale[] relations(String idTiersRequerant, String idM1, String idM2) throws PRACORException {
        String id = idM1 + "_" + idM2;
        ISFRelationFamiliale[] retValue = (ISFRelationFamiliale[]) relations.get(id);

        if (retValue == null) {
            try {
                retValue = situationFamiliale(idTiersRequerant).getToutesRelationsConjoints(idM1, idM2, Boolean.FALSE);
                relations.put(id, retValue);
            } catch (PRACORException e) {
                throw e;
            } catch (Exception e) {
                throw new PRACORException("impossible de charger toutes les relations entre deux conjoints", e);
            }
        }

        return retValue;
    }

    public ISFMembreFamille requerant() throws PRACORException {
        if (requerant == null) {
            trouverRequerant(null);
        }

        return requerant;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public ISFMembreFamille requerant(String date) throws PRACORException {
        if (requerant == null) {
            trouverRequerant(date);
        }

        return requerant;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public ISFSituationFamiliale situationFamiliale(String idTiers) throws PRACORException {
        if (sf == null) {
            try {
                sf = SFSituationFamilialeFactory.getSituationFamiliale(getSession(),
                        ISFSituationFamiliale.CS_DOMAINE_INDEMNITEE_JOURNALIERE, idTiers);
            } catch (Exception e) {
                throw new PRACORException("impossible de retrouver la situation familiale", e);
            }
        }

        return sf;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    protected PRTiersWrapper tiers() throws PRACORException {
        try {
            return prononce.loadDemande(null).loadTiers();
        } catch (Exception e) {
            throw new PRACORException(session.getLabel("ERREUR_CHARGEMENT_TIERS"), e);
        }
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    private void trouverRequerant(String date) throws PRACORException {
        String noAVS = numeroAVSAssure();

        for (Iterator membres = membres().iterator(); membres.hasNext();) {
            ISFMembreFamilleRequerantWrapper membre = (ISFMembreFamilleRequerantWrapper) membres.next();

            if (noAVS.equals(membre.getNssDirect())) {
                try {
                    if (date == null) {
                        requerant = situationFamiliale(idTiersAssure()).getMembreFamille(membre.getIdMembreFamille());
                    } else {
                        requerant = situationFamiliale(idTiersAssure()).getMembreFamille(membre.getIdMembreFamille(),
                                date);
                    }
                    idMembreFamilleRequerant = membre.getIdMembreFamille();
                } catch (PRACORException e) {
                    throw e;
                } catch (Exception e) {
                    throw new PRACORException("impossible de charger le requerant", e);
                }

                break;
            }
        }
    }
}
