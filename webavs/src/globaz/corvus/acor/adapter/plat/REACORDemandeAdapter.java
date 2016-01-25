package globaz.corvus.acor.adapter.plat;

import globaz.commons.nss.NSUtil;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRFilterIterator;
import globaz.prestation.tools.PRPredicate;
import globaz.prestation.tools.impl.PRNSS13ChiffresUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <H1>Description</H1>
 * 
 * @author scr
 */
public class REACORDemandeAdapter extends PRAbstractPlatAdapter {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    public class ImplMembreFamilleRequerantWrapper implements ISFMembreFamilleRequerant {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        public final static String NO_CS_RELATION_BLANK = "no-relation";

        public final static String NO_CS_RELATION_EX_CONJOINT_DU_CONJOINT = "ex-conjoint";
        private String idMFDuConjoint = "";

        private ISFMembreFamilleRequerant membre;
        private String nssConjoint = "";
        private String relationAuRequerant = null;

        /**
         * Crée une nouvelle instance de la classe ISFMembreFamilleRequerantWrapper.
         * 
         * @param membre
         *            DOCUMENT ME!
         */
        public ImplMembreFamilleRequerantWrapper(ISFMembreFamilleRequerant membre) {
            this.membre = membre;
        }

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

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * getter pour l'attribut id membre famille.
         * 
         * @return la valeur courante de l'attribut id membre famille
         */
        @Override
        public String getIdMembreFamille() {
            return membre.getIdMembreFamille();
        }

        public String getIdMFDuConjoint() {
            return idMFDuConjoint;
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
            if (JadeStringUtil.isBlank(membre.getNss()) || JadeStringUtil.isIntegerEmpty(membre.getNss())) {
                return nssBidon(membre.getNss(), membre.getCsSexe(), membre.getNom() + membre.getPrenom(), !membre
                        .getRelationAuRequerant().equals(ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT));
            } else {
                return membre.getNss();
            }
        }

        public String getNssConjoint() {
            return nssConjoint;
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

        // On surcharge cette methode set/get de manière à biaiser le système, pour éviter que lors
        // du traitement des exConjoints du conjoint du requérant, ils apparaissent comme conjoint du requérant et non
        // comme ex-conjoints.
        @Override
        public String getRelationAuRequerant() {
            if (JadeStringUtil.isBlankOrZero(relationAuRequerant)) {
                return membre.getRelationAuRequerant();
            } else {
                return relationAuRequerant;
            }
        }

        public void setIdMFDuConjoint(String idMFDuConjoint) {
            this.idMFDuConjoint = idMFDuConjoint;
        }

        public void setNssConjoint(String nssConjoint) {
            this.nssConjoint = nssConjoint;
        }

        public void setRelationAuRequerant(String relationAuRequerant) {
            this.relationAuRequerant = relationAuRequerant;
        }

        @Override
        public String getPays() {
            return membre.getPays();
        }

    }

    public static final String NF_ASSURES_RE = "ASSURES";
    public static final String NF_CI = "CI";

    public static final String NF_DEMANDE_RE = "DEMANDE";

    // un prédicat pour retourner les conjoints du requerant
    private static final PRPredicate PREDICATE_CONJOINTS = new PRPredicate() {

        @Override
        public boolean evaluer(Object object) {

            if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(((ISFMembreFamilleRequerant) object)
                    .getIdMembreFamille())) {

                return false;
            }

            return ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(((ISFMembreFamilleRequerant) object)
                    .getRelationAuRequerant());

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

    // un prédicat pour retourner les conjoints du requerant
    private static final PRPredicate PREDICATE_EX_CONJOINT_DU_CONJOINT = new PRPredicate() {

        @Override
        public boolean evaluer(Object object) {

            if (object instanceof ImplMembreFamilleRequerantWrapper) {
                if (ImplMembreFamilleRequerantWrapper.NO_CS_RELATION_EX_CONJOINT_DU_CONJOINT
                        .equals(((ImplMembreFamilleRequerantWrapper) object).getRelationAuRequerant())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
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
    private REDemandeRente demande;
    private List fichiers;
    private String idMembreFamilleRequerant;

    private HashMap idNoAVSBidons = new HashMap();
    private HashMap idNSSBidons = new HashMap();

    private List membres;
    private HashMap relations = new HashMap();

    private ISFMembreFamille requerant;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private ISFSituationFamiliale sf;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REACORDemandeAdapter.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param prononce
     *            DOCUMENT ME!
     */
    REACORDemandeAdapter(BSession session, REDemandeRente demande) {
        super(session);
        this.demande = demande;
    }

    /**
     * @param noAVS
     *            DOCUMENT ME!
     * @param csSexe
     *            DOCUMENT ME!
     * @param nomPrenom
     *            DOCUMENT ME!
     * @param conjoint
     *            DOCUMENT ME!
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
     * @param noAVS
     *            DOCUMENT ME!
     * @param csSexe
     *            DOCUMENT ME!
     * @param nomPrenom
     *            DOCUMENT ME!
     * @param conjoint
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private String _nssBidon(String nss, String csSexe, String nomPrenom, boolean conjoint) {

        if (!JadeStringUtil.isEmpty(nss)) {
            return nss;
        }

        String idNssBidon = conjoint + "_" + nomPrenom;

        // Prendre un nss de la liste des 25 et voir s'il existe déjà dans la map (itérer),
        // s'il existe, prendre un autre et retest, s'il existe pas, le retourner et l'insérer.

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
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public Iterator conjoints() throws PRACORException {
        return new PRFilterIterator(membres().iterator(), REACORDemandeAdapter.PREDICATE_CONJOINTS);
    }

    /**
     * retourne tous les enfants d'un requerant.
     * 
     * @return DOCUMENT ME!
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public Iterator enfants() throws PRACORException {
        return new PRFilterIterator(membres().iterator(), REACORDemandeAdapter.PREDICATE_ENFANTS);
    }

    public Iterator exConjointsDuConjoint() throws PRACORException {
        return new PRFilterIterator(membres().iterator(), REACORDemandeAdapter.PREDICATE_EX_CONJOINT_DU_CONJOINT);
    }

    /**
     * retourne tous les membres de la famille d'un requerant.
     * 
     * @return DOCUMENT ME!
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public Iterator famille() throws PRACORException {
        return new PRFilterIterator(membres().iterator(), REACORDemandeAdapter.PREDICATE_FAMILLE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.plat.PRAbstractPlatAdapter#getDateDepot()
     */
    @Override
    public String getDateDepot() {
        return getDemande().getDateDepot();
    }

    /**
     * getter pour l'attribut date determinante.
     * 
     * @return la valeur courante de l'attribut date determinante
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    @Override
    public String getDateDeterminante() throws PRACORException {
        return getDateTraitement();
    }

    /**
     * Retourne la date de traitement La date de traitement envoyée à acor est la plus grande entre 'date traitement
     * saisie dans la demande' et la date du dernier pmt. Il n'est possible de saisir une date de traitement dans la
     * demande, que pour les cas de vieillesse. Elle est utile pour traiter la demande en avance, uniquement. Donc si
     * elle est saisie, elle sera obligatoirement plus grande que la date du jours.
     * 
     * @param demande
     * @return max (date jours, date traitement de la demande) format : jj.mm.aaaa
     */
    @Override
    public String getDateTraitement() {
        JADate dateTraitement = null;
        JADate datePmt = null;
        try {
            // -- BZ6830 --//
            if (JadeDateUtil.isGlobazDate(getDemande().getDateTraitement())) {
                String dateDuJour = JadeDateUtil.getGlobazFormattedDate(new Date());
                // Si date traitement < date du jour ==> On prend la date du jour
                if (JadeDateUtil.isDateBefore(getDemande().getDateTraitement(), dateDuJour)) {
                    dateTraitement = new JADate(dateDuJour);
                } else {
                    dateTraitement = new JADate(getDemande().getDateTraitement());
                }
            } else {
                dateTraitement = new JADate(JadeDateUtil.getGlobazFormattedDate(new Date()));
            }
            // -- BZ6830 --//

            datePmt = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));

            JACalendarGregorian cal = new JACalendarGregorian();
            if (cal.compare(datePmt, dateTraitement) == JACalendar.COMPARE_FIRSTUPPER) {
                return PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(datePmt.toStrAMJ());
            } else {
                return PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateTraitement.toStrAMJ());
            }

        } catch (Exception e) {
            return null;
        }
    }

    public REDemandeRente getDemande() {
        return demande;
    }

    /**
     * getter pour l'attribut fichiers ACOR.
     * 
     * @return la valeur courante de l'attribut fichiers ACOR
     */
    @Override
    public List getFichiersACOR() {
        if (fichiers == null) {
            fichiers = new LinkedList();
            fichiers.add(new REFichierDemandePrinter(this, REACORDemandeAdapter.NF_DEMANDE_RE));
            fichiers.add(new REFichierAssuresPrinter(this, REACORDemandeAdapter.NF_ASSURES_RE)); // OK
            fichiers.add(new REFichierFamillePrinter(this, PRACORConst.NF_FAMILLES)); // OK
            fichiers.add(new REFichierEnfantPrinter(this, PRACORConst.NF_ENFANTS)); // OK
            fichiers.add(new REFichierCIPrinter(this, PRACORConst.NF_CI)); //
            fichiers.add(new REFichierPeriodePrinter(this, PRACORConst.NF_PERIODES)); //
            fichiers.add(new REFichierRentesEnCoursPrinter(this, PRACORConst.NF_RENTES)); //
        }

        return fichiers;
    }

    String getKey(ISFMembreFamilleRequerant mf) {
        if (JadeStringUtil.isBlankOrZero(mf.getIdTiers())) {
            return "imf-" + mf.getIdMembreFamille();
        } else {
            return "iti-" + mf.getIdTiers();
        }
    }

    /**
     * getter pour l'attribut type calcul.
     * 
     * @return la valeur courante de l'attribut type calcul
     */
    @Override
    public String getTypeCalcul() {
        if (IREDemandeRente.CS_TYPE_CALCUL_PMT_PROVISOIRE.equals(demande.getCsTypeCalcul())) {
            return "2";
        }
        if (IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(demande.getCsTypeCalcul())) {
            return "1";
        } else if (IREDemandeRente.CS_TYPE_CALCUL_STANDARD.equals(demande.getCsTypeCalcul())) {
            return "0";
        } else {
            return "0";
        }
    }

    /**
     * getter pour l'attribut type demande.
     * 
     * @return la valeur courante de l'attribut type demande
     */
    @Override
    public String getTypeDemande() {
        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(demande.getCsTypeDemandeRente())) {
            return "i";
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(demande.getCsTypeDemandeRente())) {
            return "s";
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(demande.getCsTypeDemandeRente())) {
            return "v";
        } else {
            return "";
        }

    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public String idMembreFamilleRequerant() throws PRACORException {
        if (idMembreFamilleRequerant == null) {
            trouverRequerant(idTiersAssure(), null);
        }

        return idMembreFamilleRequerant;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
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
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public List membres() throws PRACORException {
        // charger la situation familliale
        if (membres == null) {
            ISFSituationFamiliale sf = situationFamiliale();

            try {

                List antiDoublon = new ArrayList();
                ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamilleRequerant(idTiersAssure(),
                        getDateDeterminante());

                membres = new LinkedList();
                for (int i = 0; i < membresFamille.length; ++i) {
                    if (!antiDoublon.contains(getKey(membresFamille[i]))) {
                        membres.add(new ImplMembreFamilleRequerantWrapper(membresFamille[i]));
                        antiDoublon.add(getKey(membresFamille[i]));

                        if (ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(membresFamille[i]
                                .getRelationAuRequerant())) {

                            // On récupére également les éventuelles conjoints des conjoints

                            ISFMembreFamilleRequerant membresFamilleEtendue[] = sf
                                    .getMembresFamilleRequerantParMbrFamille(membresFamille[i].getIdMembreFamille());

                            if (membresFamilleEtendue != null) {
                                for (int j = 0; j < membresFamilleEtendue.length; j++) {
                                    if (membresFamilleEtendue[j] != null) {
                                        if (!antiDoublon.contains(getKey(membresFamilleEtendue[j]))) {

                                            ImplMembreFamilleRequerantWrapper m = new ImplMembreFamilleRequerantWrapper(
                                                    membresFamilleEtendue[j]);

                                            // On parle du (ex)conjoint du conjoint du requérant.
                                            if (ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(m
                                                    .getRelationAuRequerant())
                                                    && !idTiersAssure().equals(m.getIdTiers())) {
                                                m.setRelationAuRequerant(ImplMembreFamilleRequerantWrapper.NO_CS_RELATION_EX_CONJOINT_DU_CONJOINT);
                                                m.setIdMFDuConjoint(membresFamille[i].getIdMembreFamille());
                                                m.setNssConjoint(membresFamille[i].getNss());
                                            } else {

                                                m.setIdMFDuConjoint(null);
                                                if (idTiersAssure().equals(m.getIdTiers())) {
                                                    m.setRelationAuRequerant(ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT);
                                                } else if (ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT.equals(m
                                                        .getRelationAuRequerant())) {
                                                    ;
                                                } else {
                                                    m.setRelationAuRequerant(ImplMembreFamilleRequerantWrapper.NO_CS_RELATION_BLANK);
                                                }
                                            }
                                            membres.add(m);
                                            antiDoublon.add(getKey(membresFamilleEtendue[j]));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new PRACORException(session.getLabel("ERREUR_CHARGEMENT_SF"), e);
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
            String idTiersRequerant = getDemande().loadDemandePrestation(null).getIdTiers();

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

    /*
     * Retourne le nss de l'assuré. Dans les cas de demande de survivant, retourne le nss du conjoint ()
     */
    public String nssDemande() throws PRACORException {

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // bz-5082
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // try {
        // //Pour les demandes de type survivant, il faut saisir le nss du membre vivant dans le fichier des demandes
        // //de rentes.
        // //Si les deux sont décédés... sénégal.
        //
        // if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(demande.getCsTypeDemandeRente())) {
        // //Le demandeur est décédé...
        // if (!JadeStringUtil.isBlankOrZero(tiers().getProperty(PRTiersWrapper.PROPERTY_DATE_DECES))) {
        //
        // Iterator conjoints = conjoints();
        //
        // JADate dateRelationLaPlusRecente = new JADate("31.12.2999");
        // String nssConjointVivant = null;
        //
        // while (conjoints.hasNext()) {
        // // charger l'historique de toutes les relations pour le conjoint
        // ISFMembreFamilleRequerant conjoint = (ISFMembreFamilleRequerant) conjoints.next();
        //
        // //Les relations sont ordonnées de la plus ancienne à la plus récente.
        //
        // ISFRelationFamiliale[] relationsAll = relations(idMembreFamilleRequerant(),
        // conjoint.getIdMembreFamille());
        //
        // //On trie les rélations de la plus récente à la plus ancienne.
        // for (int idRelation = relationsAll.length-1; idRelation >=0; --idRelation) {
        //
        // ISFRelationFamiliale relation = relationsAll[idRelation];
        //
        //
        // //Ce cas apparait pour les types de relations ENFANT_COMMUN ou RELATION_INDEFINIE.
        // if (relation.getTypeLien()==null) {
        // continue;
        // }
        //
        // //On ne traite que la relation la plus récente trouvée, exceptée les relations ENFANT_COMMUN ou
        // RELATION_INDEFINIE
        //
        //
        // //On ne traitre que si la dernière relation est de type divorce, pour autant que le conjoint ne soit pas
        // décédé.
        // if (ISFSituationFamiliale.CS_TYPE_LIEN_DIVORCE.equals(relation.getTypeLien()) &&
        // JadeStringUtil.isBlankOrZero(conjoint.getDateDeces())) {
        //
        // JADate currentDate = new JADate(relation.getDateDebutRelation());
        // JACalendar cal = new JACalendarGregorian();
        // if (cal.compare(currentDate, dateRelationLaPlusRecente)==JACalendar.COMPARE_FIRSTLOWER) {
        // dateRelationLaPlusRecente = new
        // JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(currentDate.toStrAMJ()));
        // nssConjointVivant = conjoint.getNss();
        // break;
        // }
        // }
        // }
        // }
        // if (!JadeStringUtil.isBlankOrZero(nssConjointVivant)) {
        // return nssConjointVivant;
        // }
        // else {
        // return tiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
        // }
        // }
        // }
        //
        // }
        // catch (Exception e) {
        // throw new PRACORException (e.toString());
        // }
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        // bz-5082
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

        return tiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
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
     * @return DOCUMENT ME!
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public ISFRelationFamiliale[] relations(String idM1, String idM2) throws PRACORException {
        String id = idM1 + "_" + idM2;
        ISFRelationFamiliale[] retValue = (ISFRelationFamiliale[]) relations.get(id);

        if (retValue == null) {
            try {
                retValue = situationFamiliale().getToutesRelationsConjoints(idM1, idM2, Boolean.FALSE);
                relations.put(id, retValue);
            } catch (PRACORException e) {
                throw e;
            } catch (Exception e) {
                throw new PRACORException(session.getLabel("ERREUR_CHARGEMENT_RELATIONS_CONJOINTS"), e);
            }
        }

        return retValue;
    }

    public ISFMembreFamille requerant() throws PRACORException {
        if (requerant == null) {

            trouverRequerant(idTiersAssure(), null);
        }

        return requerant;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public ISFMembreFamille requerant(String idTiersRequerant, String date) throws PRACORException {
        if (requerant == null) {
            trouverRequerant(idTiersRequerant, date);
        }

        return requerant;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    public ISFSituationFamiliale situationFamiliale() throws PRACORException {
        if (sf == null) {
            try {
                String csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
                try {
                    if (IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(demande.getCsTypeCalcul())) {
                        csDomaine = ISFSituationFamiliale.CS_DOMAINE_CALCUL_PREVISIONNEL;
                    }
                } catch (Exception e) {
                    csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
                }
                sf = SFSituationFamilialeFactory.getSituationFamiliale(getSession(), csDomaine, idTiersAssure());
            } catch (Exception e) {
                throw new PRACORException(session.getLabel("ERREUR_CHARGEMENT_SF"), e);
            }
        }

        return sf;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * @throws PRACORException
     *             DOCUMENT ME!
     */
    protected PRTiersWrapper tiers() throws PRACORException {
        try {
            return demande.loadDemandePrestation(null).loadTiers();
        } catch (Exception e) {
            throw new PRACORException(session.getLabel("ERREUR_CHARGEMENT_TIERS"), e);
        }
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    private void trouverRequerant(String idTiersRequerant, String date) throws PRACORException {
        String noAVS = numeroAVSAssure();

        for (Iterator membres = membres().iterator(); membres.hasNext();) {
            ImplMembreFamilleRequerantWrapper membre = (ImplMembreFamilleRequerantWrapper) membres.next();

            if (noAVS.equals(membre.getNssDirect())) {
                try {
                    if (date == null) {
                        requerant = situationFamiliale().getMembreFamille(membre.getIdMembreFamille());
                    } else {
                        requerant = situationFamiliale().getMembreFamille(membre.getIdMembreFamille(), date);
                    }
                    idMembreFamilleRequerant = membre.getIdMembreFamille();
                } catch (PRACORException e) {
                    throw e;
                } catch (Exception e) {
                    throw new PRACORException(session.getLabel("ERREUR_CHARGEMENT_REQUERANT"), e);
                }

                break;
            }
        }
    }

}
