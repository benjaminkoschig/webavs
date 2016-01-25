package globaz.prestation.ged;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeManager;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuEnfant;
import globaz.hera.db.famille.SFApercuEnfantManager;
import globaz.hera.db.famille.SFApercuRequerant;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFConjointManager;
import globaz.hera.db.famille.SFEnfant;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.hera.impl.standard.SFRelationFamilialeManagerStd;
import globaz.hera.impl.standard.SFRelationFamilialeStd;
import globaz.hera.wrapper.SFRelationFamilialeWrapper;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PRGedHelper {

    public static final String GED_FOLDER_EXTRA_NSS = "folder.extra.nss";
    public static final String PARAM_NSS_MF = "nssMF";

    // workaround
    protected ISFRelationFamiliale[] _getRelationsConjoints(BSession session, String idTiersRequerant,
            String csDomaine, String date) throws Exception {

        SFApercuRequerant requerant = new SFApercuRequerant();
        requerant.setSession(session);
        requerant.setIdTiers(idTiersRequerant);
        requerant.setIdDomaineApplication(csDomaine);
        requerant.setAlternateKey(SFApercuRequerant.ALT_KEY_IDTIERS);
        requerant.retrieve();

        if (requerant.isNew()) {
            return null;
        }

        SFRelationFamilialeStd[] relations = getRelFam(session, requerant.getIdMembreFamille(), date);
        ISFRelationFamiliale[] result = null;
        if (relations != null) {

            result = new ISFRelationFamiliale[relations.length];
            for (int i = 0; i < relations.length; i++) {

                SFRelationFamilialeStd relation = relations[i];

                SFRelationFamilialeWrapper wrapper = new SFRelationFamilialeWrapper();
                wrapper.setDateDebut(relation.getDateDebut());
                wrapper.setDateDebutRelation(relation.getDateDebutRelation());
                wrapper.setDateFin(relation.getDateFin());
                wrapper.setIdMembreFamilleFemme(relation.getIdMembreFamilleFemme());
                wrapper.setIdMembreFamilleHomme(relation.getIdMembreFamilleHomme());
                wrapper.setNoAvsFemme(relation.getNoAvsFemme());
                wrapper.setNoAvsHomme(relation.getNoAvsHomme());
                wrapper.setNomFemme(relation.getNomFemme());
                wrapper.setNomHomme(relation.getNomHomme());
                wrapper.setPrenomFemme(relation.getPrenomFemme());
                wrapper.setPrenomHomme(relation.getPrenomHomme());

                // Lors de la récupération de la date de début, le type de lien est initialisé avec
                // avec comme référence, la date actuelle.
                // On reset ce type de lien pour reforcer son calcul à la date de fin de la relation.
                relation.setTypeLien(null);
                if (JadeStringUtil.isBlankOrZero(relation.getDateFin())) {
                    relation.setDateFin(date);
                }
                wrapper.setTypeLien(relation.getTypeLien(date));
                wrapper.setTypeRelation(relation.getTypeRelation());

                result[i] = wrapper;

            }
        }
        return result;
    }

    // private String doTraitementCas2EntreParents(BSession session, ISFSituationFamiliale sf,
    // ISFMembreFamille parentRequerant,
    // ISFMembreFamille parent2) throws Exception {
    //
    // ISFRelationFamiliale[] relations = sf.getToutesRelationsConjointsParIdTiers(parentRequerant.getIdTiers(),
    // parent2.getIdTiers(), true);
    //
    // // 2) Si marié(e), il faut vérifier si du conjoint (non-décédé) ou du requérant,
    // // celui qui a la plus vieille demande (date de dépôt) et choisir cette personne
    // // comme requérante à passer pour la GED.
    //
    // // ET
    //
    // // 3) Si divorcé(e) ou séparé(e), il faut vérifier si du dernier conjoint
    // // (non-décédé) ou du requérant, celui qui a la plus vieille demande (date de
    // // dépôt) et choisir cette personne comme requérante à passer pour la GED.
    //
    // // La première relation est la plus récente
    // if (((relations != null) && (relations.length > 0)) &&
    // (ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(relations[0].getTypeLien()) ||
    // ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(relations[0].getTypeLien()) ||
    // ISFSituationFamiliale.CS_TYPE_LIEN_DIVORCE.equals(relations[0].getTypeLien()) ||
    // ISFSituationFamiliale.CS_TYPE_LIEN_SEPARE.equals(relations[0].getTypeLien()) ||
    // ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DISSOUT.equals(relations[0].getTypeLien()))) {
    //
    // REDemandePrestationJointDemandeRenteManager mgr = new REDemandePrestationJointDemandeRenteManager();
    // mgr.setSession(session);
    // mgr.setForIdTiersRequerant(parentRequerant.getIdTiers());
    // mgr.setOrderBy(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_DEPOT + " DESC ");
    // mgr.find();
    //
    // JADate dateDepotDemRequerant = null;
    // JADate dateDepotDemBeneficiaire = null;
    // for (int j = 0; j < mgr.size(); j++) {
    // REDemandePrestationJointDemandeRente dem = (REDemandePrestationJointDemandeRente) mgr
    // .get(j);
    // if (!JadeStringUtil.isBlankOrZero(dem.getDateDepot())) {
    // dateDepotDemRequerant = new JADate(dem.getDateDepot());
    // break;
    // }
    // }
    //
    // mgr.setForIdTiersRequerant(parent2.getIdTiers());
    // mgr.setOrderBy(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_DEPOT + " DESC ");
    // mgr.find();
    //
    // for (int j = 0; j < mgr.size(); j++) {
    // REDemandePrestationJointDemandeRente dem = (REDemandePrestationJointDemandeRente) mgr
    // .get(j);
    // if (!JadeStringUtil.isBlankOrZero(dem.getDateDepot())) {
    // dateDepotDemBeneficiaire = new JADate(dem.getDateDepot());
    // break;
    // }
    // }
    //
    // if ((dateDepotDemRequerant == null) && (dateDepotDemBeneficiaire != null)) {
    //
    // if (JadeStringUtil.isBlankOrZero(parent2.getDateDeces())) {
    // return parent2.getNss();
    // } else {
    // return null;
    // }
    //
    // } else if ((dateDepotDemRequerant != null) && (dateDepotDemBeneficiaire == null)) {
    //
    // if (JadeStringUtil.isBlankOrZero(parentRequerant.getDateDeces())) {
    // return parentRequerant.getNss();
    // } else {
    // return null;
    // }
    //
    // } else if ((dateDepotDemRequerant == null) && (dateDepotDemBeneficiaire == null)) {
    // return "";
    // } else {
    // JACalendar cal = new JACalendarGregorian();
    // // On retourne le nss pour la plus vieille date de dépôt du conjoint non décédé !!!
    // if ((cal.compare(dateDepotDemRequerant, dateDepotDemBeneficiaire) == JACalendar.COMPARE_FIRSTLOWER) ||
    // (cal.compare(dateDepotDemRequerant, dateDepotDemBeneficiaire) == JACalendar.COMPARE_EQUALS)) {
    //
    // if (JadeStringUtil.isBlankOrZero(parentRequerant.getDateDeces())) {
    // return parentRequerant.getNss();
    // } else {
    // return parent2.getNss();
    // }
    // } else {
    //
    // if (JadeStringUtil.isBlankOrZero(parent2.getDateDeces())) {
    // return parent2.getNss();
    // } else {
    // return parentRequerant.getNss();
    // }
    // }
    // }
    //
    // } else {
    // return null;
    // }
    // }

    private String doTraitementCelibataireOuEnfant(BSession session, String csDomaine, ISFSituationFamiliale sf,
            String idTiersBeneficiaire, String nssTiersBenef, ISFMembreFamilleRequerant mfBeneficiaire)
            throws Exception {

        if (mfBeneficiaire != null) {

            boolean isRentePrincPourEnfant = isRentePrincipaleForIdTiers(session, mfBeneficiaire.getIdTiers(), null,
                    null);

            // Traité comme un célibataire
            if (isRentePrincPourEnfant) {
                return mfBeneficiaire.getNss();
            }
            SFEnfant enfant = new SFEnfant();
            enfant.setSession(session);
            enfant.setAlternateKey(SFEnfant.ALTERNATE_KEY_IDMEMBREFAMILLE);
            enfant.setIdMembreFamille(mfBeneficiaire.getIdMembreFamille());
            enfant.retrieve();

            if ((enfant != null) && !enfant.isNew()) {
                // On récupère les parents !!!
                globaz.hera.api.ISFMembreFamille[] parents = sf.getParentsParMbrFamille(mfBeneficiaire
                        .getIdMembreFamille());

                SFMembreFamille mfP1 = null;

                if ((parents[0] != null) && !JadeStringUtil.isBlankOrZero(parents[0].getIdTiers())) {
                    mfP1 = new SFMembreFamille();
                    mfP1.setSession(session);
                    mfP1.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
                    mfP1.setIdTiers(parents[0].getIdTiers());
                    mfP1.setCsDomaineApplication(csDomaine);
                    mfP1.retrieve();
                }

                SFMembreFamille mfP2 = null;

                if ((parents[1] != null) && !JadeStringUtil.isBlankOrZero(parents[1].getIdTiers())) {
                    mfP2 = new SFMembreFamille();
                    mfP2.setSession(session);
                    mfP2.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
                    mfP2.setIdTiers(parents[1].getIdTiers());
                    mfP2.setCsDomaineApplication(csDomaine);
                    mfP2.retrieve();
                }

                boolean isRentePrincipaleP1 = false;
                boolean isRentePrincipaleP2 = false;

                if ((mfP1 != null) && isRentePrincipaleForIdTiers(session, mfP1.getIdTiers(), null, null)) {
                    isRentePrincipaleP1 = true;
                }
                if ((mfP2 != null) && isRentePrincipaleForIdTiers(session, mfP2.getIdTiers(), null, null)) {
                    isRentePrincipaleP2 = true;
                }

                // On retourne l'enfant le plus jeune
                if (!isRentePrincipaleP1 && !isRentePrincipaleP2) {
                    SFApercuEnfant enfantLePlusJeune = retrieveEnfantLePlusJeune(session, mfP1, mfP2);
                    if (enfantLePlusJeune != null) {
                        return enfantLePlusJeune.getNss();
                    } else {
                        return mfBeneficiaire.getNss();
                    }
                } else {
                    if ((mfP1 != null) && (mfP2 != null)) {

                        // PRDemandeManager m = new PRDemandeManager();
                        // m.setSession(session);
                        // m.setForIdTiers(parents[0].getIdTiers());
                        // m.setForTypeDemande(IPRDemande.CS_TYPE_RENTE);
                        // m.find(1);
                        // boolean isRequerantP0 = false;
                        // if (!m.isEmpty()) {
                        // isRequerantP0 = true;
                        // }
                        //
                        // nss de celui qui touche la rente principale.
                        if (isRentePrincipaleP1 && !isRentePrincipaleP2) {
                            return mfP1.getNss();
                        } else if (isRentePrincipaleP2 && !isRentePrincipaleP1) {
                            return mfP2.getNss();
                        } else if (isRentePrincipaleP1 && isRentePrincipaleP2) {
                            // (les deux parents ont une rente principale)
                            // -> nss du mari
                            if (PRACORConst.CS_HOMME.equals(mfP1.getCsSexe())) {
                                return mfP1.getNss();
                            } else {
                                return mfP2.getNss();
                            }
                        }
                        // ne devrait pas arriver !!!
                        else {
                            return nssTiersBenef;
                        }

                    }
                    // seul parent 1 a un NSS et une rente principale
                    else if ((mfP1 != null) && isRentePrincipaleP1) {
                        return mfP1.getNss();
                    }
                    // seul parent 2 a un NSS et une rente principale
                    else if ((mfP2 != null) && isRentePrincipaleP2) {
                        return mfP2.getNss();
                    } else {
                        return mfBeneficiaire.getNss();
                    }
                }
            } else {
                return mfBeneficiaire.getNss();
            }
        } else {
            return nssTiersBenef;
        }

    }

    /*
     * 
     * 
     * 1) si le requérant est célibataire, sur la base du NSS du requérant;
     * 2) si marié, toujours sur la base du NSS du mari;
     * 3) si divorcé ou séparé, sur la base de chaque numéro (soit GED sur la base du NSS de Monsieur et GED sur la base
     * du NSS de Madame) ;
     * 4) si décédé, sur la base du NSS du conjoint qui touche la rente de conjoint survivant;
     * 4a) s'il s'agit d'un enfant dont un des parents est décédé, sur la base du NSS du conjoint qui touche la rente
     * (point N° 4)
     * 5) s'il s'agit d'un enfant dont les parents n'ont pas de rentes principales, il y a lieu de prendre en compte le
     * plus jeune des enfants (NSS) pour la GED;
     * 6) s'il s'agit d'un enfant dont au moins un des parents a reçu une rente principale:
     * - si les deux parents touchent une rente, sous le N° NSS du mari (point N° 2)
     * - si seul un des deux conjoints touchent une rente sur la base du N° NSS du conjoint qui touche la rente.
     */
    public String getNssExtraFolder(BSession session, String idTiersBeneficiaire) {

        ISFMembreFamilleRequerant mfBeneficiaire = null;
        PRTiersWrapper twBeneficiaire = null;
        try {
            if (JadeStringUtil.isBlankOrZero(idTiersBeneficiaire)) {
                return "";
            } else {
                twBeneficiaire = PRTiersHelper.getTiersParId(session, idTiersBeneficiaire);
            }

            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersBeneficiaire);

            String csDomaine = ISFSituationFamiliale.CS_DOMAINE_STANDARD;

            if (sf.isRequerant(idTiersBeneficiaire, ISFSituationFamiliale.CS_DOMAINE_RENTES)) {
                csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
            }

            // Recherche du bénéficiaire en tant que membre de la famille. (nécessaire pour les traitements avec
            // l'idMbrFam)
            ISFMembreFamilleRequerant[] mfs = sf.getMembresFamille(idTiersBeneficiaire);

            for (int i = 0; i < mfs.length; i++) {
                ISFMembreFamilleRequerant mf = mfs[i];
                if (idTiersBeneficiaire.equals(mf.getIdTiers())) {
                    mfBeneficiaire = mf;
                    break;
                }
            }

            // on récupère la liste des relations du tiers
            // ISFRelationFamiliale[] relations = sf.getRelationsConjoints(idTiersBeneficiaire, null);
            ISFRelationFamiliale[] relations = _getRelationsConjoints(session, idTiersBeneficiaire, csDomaine,
                    JACalendar.todayJJsMMsAAAA());

            if ((relations == null) || (relations.length == 0)) {

                return doTraitementCelibataireOuEnfant(session, csDomaine, sf, idTiersBeneficiaire,
                        twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), mfBeneficiaire);
            } else {
                ISFRelationFamiliale lastRelation = null;
                // on parcourt la liste des relations du tiers en cherchant celle ayant la date de début
                // la plus récente (donc l'actuelle)
                for (ISFRelationFamiliale relation : relations) {

                    if (lastRelation == null) {
                        lastRelation = relation;
                        continue;
                    }

                    if (JadeDateUtil.isDateBefore(lastRelation.getDateDebutRelation(), relation.getDateDebutRelation())) {
                        lastRelation = relation;
                    }
                }

                PRTiersWrapper twConjoint = null;
                // on récupère le conjoint du tiers (en regardant les numéro AVS de la relation
                // pour définir qui est qui dans la table, homme ou femme)
                if (twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL).equalsIgnoreCase(
                        lastRelation.getNoAvsFemme())) {
                    twConjoint = PRTiersHelper.getTiers(session, lastRelation.getNoAvsHomme());
                } else {
                    twConjoint = PRTiersHelper.getTiers(session, lastRelation.getNoAvsFemme());
                }

                // Si décédé :

                if (ISFSituationFamiliale.CS_TYPE_LIEN_VEUF.equals(lastRelation.getTypeLien())) {

                    // Le bénéficiaire est décédé
                    if (!JadeStringUtil.isBlankOrZero(twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES))) {

                        boolean isRentePrincipaleForConjoint = isRentePrincipaleForIdTiers(session,
                                twConjoint.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), null, null);

                        if (isRentePrincipaleForConjoint) {
                            return twConjoint.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                        }

                        // Les deux sont décédés... on prend le plus jeune des enfants
                        else {
                            globaz.hera.api.ISFMembreFamille[] parents = sf.getParentsParMbrFamille(mfBeneficiaire
                                    .getIdMembreFamille());

                            SFMembreFamille mfP1 = null;

                            if ((parents[0] != null) && !JadeStringUtil.isBlankOrZero(parents[0].getIdTiers())) {
                                mfP1 = new SFMembreFamille();
                                mfP1.setSession(session);
                                mfP1.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
                                mfP1.setIdTiers(parents[0].getIdTiers());
                                mfP1.setCsDomaineApplication(csDomaine);
                                mfP1.retrieve();
                            }

                            SFMembreFamille mfP2 = null;

                            if ((parents[1] != null) && !JadeStringUtil.isBlankOrZero(parents[1].getIdTiers())) {
                                mfP2 = new SFMembreFamille();
                                mfP2.setSession(session);
                                mfP2.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
                                mfP2.setIdTiers(parents[1].getIdTiers());
                                mfP2.setCsDomaineApplication(csDomaine);
                                mfP2.retrieve();
                            }

                            if ((mfP1 != null) && (mfP2 != null)) {
                                SFApercuEnfant enfantLePlusJeune = retrieveEnfantLePlusJeune(session, mfP1, mfP2);
                                if (enfantLePlusJeune != null) {
                                    return enfantLePlusJeune.getNss();
                                } else {
                                    // Cas en principe impossible.
                                    return twConjoint.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                                }
                            } else {
                                return twConjoint.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                            }
                        }
                    } else {
                        return twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                    }
                } else {

                    // on vérifie que le conjoint est marié ou séparé, mais pas divorcé
                    if (ISFSituationFamiliale.CS_REL_CONJ_MARIE.equalsIgnoreCase(lastRelation.getTypeRelation())
                            || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT.equalsIgnoreCase(lastRelation
                                    .getTypeRelation())) {

                        if (PRACORConst.CS_HOMME.equals(twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_SEXE))) {
                            return twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                        } else {
                            return twConjoint.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                        }

                    } else if (ISFSituationFamiliale.CS_REL_CONJ_DIVORCE.equalsIgnoreCase(lastRelation
                            .getTypeRelation())
                            || ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT.equalsIgnoreCase(lastRelation
                                    .getTypeRelation())) {

                        // Cas 3)
                        return twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                    }
                    // relation indéfinie !!! ne devrait pas arriver
                    else {
                        return twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            if (twBeneficiaire != null) {
                return twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
            } else {
                return "";
            }
        }
    }

    protected SFRelationFamilialeStd[] getRelFam(BSession session, String idMembreFamille, String date)
            throws Exception {

        SFRelationFamilialeManagerStd relMgr = new SFRelationFamilialeManagerStd();
        relMgr.setSession(session);
        relMgr.setForIdConjoint(idMembreFamille);
        relMgr.setNoWantEnfantCommun(true);
        relMgr.setNoWantRelationIndefinie(true);
        relMgr.setUntilDateDebut(date);
        relMgr.setOrderByDateDebutDsc(true);
        relMgr.find();

        Set idFound = new HashSet();

        // On ne garde que la relation la plus recente entre des conjoints
        Iterator it = relMgr.iterator();
        List relList = new ArrayList();
        while (it.hasNext()) {
            SFRelationFamilialeStd rel = (SFRelationFamilialeStd) it.next();
            if (!idFound.contains(rel.getIdConjoints())) {
                relList.add(rel);
                idFound.add(rel.getIdConjoints());
            }
        }

        SFRelationFamilialeStd[] result = new SFRelationFamilialeStd[relList.size()];
        for (int i = 0; i < relList.size(); i++) {
            result[i] = (SFRelationFamilialeStd) relList.get(i);
        }
        return result;
    }

    public boolean isExtraNSS(BSession session) {

        try {
            if ("66.2".equals(CaisseHelperFactory.getInstance().getNoCaisseFormatee(session.getApplication()))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    private boolean isRenteForIdTiers(BSession session, String idTiers, String fromDate, String forCsEtatIn)
            throws Exception {

        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            return false;
        }

        REPrestationAccordeeManager mgr = new REPrestationAccordeeManager();
        mgr.setSession(session);
        mgr.setForIdTiersBeneficiaire(idTiers);
        if (!JadeStringUtil.isBlankOrZero(fromDate)) {
            mgr.setFromDateFin(fromDate);
        }
        if (!JadeStringUtil.isBlankOrZero(forCsEtatIn)) {
            mgr.setForCsEtatIn(forCsEtatIn);
        }
        mgr.find(1);
        return !mgr.isEmpty();

    }

    private boolean isRentePrincipaleForIdTiers(BSession session, String idTiers, String fromDate, String forCsEtatIn)
            throws Exception {

        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            return false;
        }

        REPrestationAccordeeManager mgr = new REPrestationAccordeeManager();
        mgr.setSession(session);
        mgr.setForIdTiersBeneficiaire(idTiers);

        mgr.setForCodesPrestationsIn("'" + REGenresPrestations.GENRE_10 + "', '" + REGenresPrestations.GENRE_13
                + "', '" + REGenresPrestations.GENRE_20 + "', '" + REGenresPrestations.GENRE_23 + "', '"
                + REGenresPrestations.GENRE_50 + "', '" + REGenresPrestations.GENRE_70 + "', '"
                + REGenresPrestations.GENRE_72 + "' ");
        if (!JadeStringUtil.isBlankOrZero(fromDate)) {
            mgr.setFromDateFin(fromDate);
        }
        if (!JadeStringUtil.isBlankOrZero(forCsEtatIn)) {
            mgr.setForCsEtatIn(forCsEtatIn);
        }
        mgr.find(1);
        return !mgr.isEmpty();

    }

    private SFApercuEnfant retrieveEnfantLePlusJeune(BSession session, SFMembreFamille p1, SFMembreFamille p2)
            throws Exception {

        SFConjointManager mgr = new SFConjointManager();
        mgr.setSession(session);
        mgr.setForIdsConjoints(p1.getIdMembreFamille(), p2.getIdMembreFamille());
        mgr.find();

        SFApercuEnfant enfantLePlusJeune = null;

        if (!mgr.isEmpty()) {
            SFConjoint c = (SFConjoint) mgr.getEntity(0);

            SFApercuEnfantManager mgr2 = new SFApercuEnfantManager();
            mgr2.setSession(session);
            mgr2.setForIdConjoint(c.getIdConjoints());
            mgr2.find();

            for (int i = 0; i < mgr2.size(); i++) {
                SFApercuEnfant enfant = (SFApercuEnfant) mgr2.get(i);
                if (!JadeStringUtil.isBlankOrZero(enfant.getDateNaissance())) {
                    if (enfantLePlusJeune == null) {
                        if (isRenteForIdTiers(session, enfant.getIdTiers(), null, null)) {
                            enfantLePlusJeune = enfant;
                        }
                    } else {
                        JADate dateNaissanceEnfantCourant = new JADate(enfant.getDateNaissance());
                        JADate dateNaissanceEnfantLePlusJeune = new JADate(enfantLePlusJeune.getDateNaissance());

                        JACalendar cal = new JACalendarGregorian();
                        if (cal.compare(dateNaissanceEnfantLePlusJeune, dateNaissanceEnfantCourant) == JACalendar.COMPARE_FIRSTUPPER) {
                            if (isRenteForIdTiers(session, enfant.getIdTiers(), null, null)) {
                                enfantLePlusJeune = enfant;
                            }
                        }
                    }
                }
            }
        }
        return enfantLePlusJeune;
    }

    // Traitement uniquement pour la cass '066' (CCB)
    // Pas top comme test, mais utilisé pour éviter de faire ces lourds traitement inutile pour les autres caisses.
    public JadePublishDocumentInfo setNssExtraFolderToDocInfo(BSession session, JadePublishDocumentInfo docInfo,
            String idTiersBeneficiaire) {

        String nss = "";
        try {
            if (!isExtraNSS(session)) {
                return docInfo;
            }
            nss = getNssExtraFolder(session, idTiersBeneficiaire);
            if (!JadeStringUtil.isBlankOrZero(nss)) {
                docInfo.setDocumentProperty(PRGedHelper.GED_FOLDER_EXTRA_NSS, nss);
            }
            System.out.println("NSS Extra Folder = " + nss);
        } catch (Exception e) {
            e.printStackTrace();
            return docInfo;
        }
        return docInfo;
    }

    // OLD CODE !!!

    // boolean isRentePrincipaleForBeneficaire = this.isRentePrincipaleForIdTiers(session,
    // twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), null, null);
    // boolean isRentePrincipaleForConjoint = this.isRentePrincipaleForIdTiers(session,
    // twConjoint.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS), null, null);
    // // Traitement des différents cas !!!
    // if (isRentePrincipaleForBeneficaire || isRentePrincipaleForConjoint) {
    //
    // // SI RP pour benef uniquement, retourner son nss
    // if (isRentePrincipaleForBeneficaire && !isRentePrincipaleForConjoint) {
    // return twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    // }
    // // SI RP pour conjoint uniquement, retourner son nss
    // else if (!isRentePrincipaleForBeneficaire && isRentePrincipaleForConjoint) {
    // return twConjoint.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    // }
    // // SI RP pour les d3ux, retourner nss de celui qui a la plus vieille date de dépôt
    // else if (isRentePrincipaleForBeneficaire && isRentePrincipaleForConjoint) {
    //
    // REDemandePrestationJointDemandeRenteManager mgr = new REDemandePrestationJointDemandeRenteManager();
    // mgr.setSession(session);
    // mgr.setForIdTiersRequerant(twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
    // mgr.setOrderBy(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_DEPOT + " DESC ");
    // mgr.find();
    //
    // JADate dateDepotDemBenef = null;
    // JADate dateDepotDemConjoint = null;
    // for (int j = 0; j < mgr.size(); j++) {
    // REDemandePrestationJointDemandeRente dem = (REDemandePrestationJointDemandeRente) mgr
    // .get(j);
    // if (!JadeStringUtil.isBlankOrZero(dem.getDateDepot())) {
    // dateDepotDemBenef = new JADate(dem.getDateDepot());
    // break;
    // }
    // }
    //
    // mgr.setForIdTiersRequerant(twConjoint.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
    // mgr.setOrderBy(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_DEPOT + " DESC ");
    // mgr.find();
    //
    // for (int j = 0; j < mgr.size(); j++) {
    // REDemandePrestationJointDemandeRente dem = (REDemandePrestationJointDemandeRente) mgr
    // .get(j);
    // if (!JadeStringUtil.isBlankOrZero(dem.getDateDepot())) {
    // dateDepotDemConjoint = new JADate(dem.getDateDepot());
    // break;
    // }
    // }
    //
    // if ((dateDepotDemConjoint == null) && (dateDepotDemBenef != null)) {
    // return twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    // } else if ((dateDepotDemConjoint != null) && (dateDepotDemBenef == null)) {
    // return twConjoint.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    // } else if ((dateDepotDemConjoint == null) && (dateDepotDemBenef == null)) {
    // return twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    // } else {
    // JACalendar cal = new JACalendarGregorian();
    // // On retourne la plus vieille date de dépôt !!!
    // if ((cal.compare(dateDepotDemBenef, dateDepotDemConjoint) == JACalendar.COMPARE_FIRSTLOWER)
    // ||
    // (cal.compare(dateDepotDemBenef, dateDepotDemConjoint) == JACalendar.COMPARE_EQUALS)) {
    // return twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    // } else {
    // return twConjoint.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    // }
    // }
    // } else {
    // return twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    // }
    // }
    // // Pas de rente principale, ni pour le conjoint ni pour le benef
    // else {
    // return twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    // }
    //
    //
    // }
    // // DIVORCE
    // else {
    // return twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    // }

    // private String doTraitementCas2(BSession session, ISFSituationFamiliale sf, ISFMembreFamilleRequerant requerant,
    // ISFMembreFamilleRequerant beneficiaire) throws Exception {
    // ISFRelationFamiliale[] relations = sf.getToutesRelationsConjoints(requerant.getIdMembreFamille(),
    // beneficiaire.getIdMembreFamille(), true);
    //
    // // 2) Si marié(e), il faut vérifier si du conjoint (non-décédé) ou du requérant,
    // // celui qui a la plus vieille demande (date de dépôt) et choisir cette personne
    // // comme requérante à passer pour la GED.
    //
    // // ET
    //
    // // 3) Si divorcé(e) ou séparé(e), il faut vérifier si du dernier conjoint
    // // (non-décédé) ou du requérant, celui qui a la plus vieille demande (date de
    // // dépôt) et choisir cette personne comme requérante à passer pour la GED.
    //
    // // La première relation est la plus récente
    // if (ISFSituationFamiliale.CS_TYPE_LIEN_MARIE.equals(relations[0].getTypeLien()) ||
    // ISFSituationFamiliale.CS_TYPE_LIEN_LPART_ENREGISTRE.equals(relations[0].getTypeLien()) ||
    // ISFSituationFamiliale.CS_TYPE_LIEN_DIVORCE.equals(relations[0].getTypeLien()) ||
    // ISFSituationFamiliale.CS_TYPE_LIEN_SEPARE.equals(relations[0].getTypeLien()) ||
    // ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DISSOUT.equals(relations[0].getTypeLien())) {
    //
    // REDemandePrestationJointDemandeRenteManager mgr = new REDemandePrestationJointDemandeRenteManager();
    // mgr.setSession(session);
    // mgr.setForIdTiersRequerant(requerant.getIdTiers());
    // mgr.setOrderBy(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_DEPOT + " DESC ");
    // mgr.find();
    //
    // JADate dateDepotDemRequerant = null;
    // JADate dateDepotDemBeneficiaire = null;
    // for (int j = 0; j < mgr.size(); j++) {
    // REDemandePrestationJointDemandeRente dem = (REDemandePrestationJointDemandeRente) mgr
    // .get(j);
    // if (!JadeStringUtil.isBlankOrZero(dem.getDateDepot())) {
    // dateDepotDemRequerant = new JADate(dem.getDateDepot());
    // break;
    // }
    // }
    //
    // mgr.setForIdTiersRequerant(beneficiaire.getIdTiers());
    // mgr.setOrderBy(REDemandePrestationJointDemandeRente.FIELDNAME_DATE_DEPOT + " DESC ");
    // mgr.find();
    //
    // for (int j = 0; j < mgr.size(); j++) {
    // REDemandePrestationJointDemandeRente dem = (REDemandePrestationJointDemandeRente) mgr
    // .get(j);
    // if (!JadeStringUtil.isBlankOrZero(dem.getDateDepot())) {
    // dateDepotDemBeneficiaire = new JADate(dem.getDateDepot());
    // break;
    // }
    // }
    //
    // if ((dateDepotDemBeneficiaire == null) || !JadeStringUtil.isBlankOrZero(beneficiaire.getDateDeces())) {
    // return requerant.getNss();
    // }
    //
    // JACalendar cal = new JACalendarGregorian();
    // // On retourne la plus vieille date de dépôt !!!
    // if ((cal.compare(dateDepotDemRequerant, dateDepotDemBeneficiaire) == JACalendar.COMPARE_FIRSTLOWER) ||
    // (cal.compare(dateDepotDemRequerant, dateDepotDemBeneficiaire) == JACalendar.COMPARE_EQUALS)) {
    // return requerant.getNss();
    // } else {
    // return beneficiaire.getNss();
    // }
    // }
    // // 4° Si décès, choisir le dernier conjoint comme requérant à passer pour la GED.
    // else if (!JadeStringUtil.isBlankOrZero(beneficiaire.getDateDeces())) {
    // if (JadeStringUtil.isBlankOrZero(beneficiaire.getDateDeces())) {
    // return beneficiaire.getNss();
    // } else {
    // return requerant.getNss();
    // }
    // } else {
    // return null;
    // }
    //
    // }

    // Règles spécifiques pour CCB

    // Exception e

    // public String getNssExtraFolder(BSession session, String idTiersRequerant)
    // throws Exception {
    //
    // if (JadeStringUtil.isBlankOrZero(idTiersRequerant)) {
    // return "";
    // }
    // // if (JadeStringUtil.isBlankOrZero(idTiersBeneficiaire)) {
    // // return "";
    // // }
    //
    // String result = "";
    // ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
    // ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);
    //
    // ISFMembreFamilleRequerant[] mfs = sf.getMembresFamilleRequerant(idTiersRequerant, JACalendar.todayJJsMMsAAAA());
    //
    // ISFMembreFamilleRequerant requerant = null;
    // ISFMembreFamilleRequerant beneficiaire = null;
    // ISFMembreFamilleRequerant beneficiaireEnfant = null;
    // ISFMembreFamilleRequerant enfantLePlusJeune = null;
    //
    // for (int i = 0; i < mfs.length; i++) {
    // ISFMembreFamilleRequerant mf = mfs[i];
    // if (ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(mf.getRelationAuRequerant())) {
    // requerant = mf;
    // }
    // if (idTiersBeneficiaire.equals(mf.getIdTiers())) {
    // beneficiaire = mf;
    // if (ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT.equals(mf.getRelationAuRequerant())) {
    // beneficiaireEnfant = mf;
    // }
    // }
    // }
    //
    // // Traitement des différents cas !!!
    //
    // // 1) Bénéficiaire célibataire et n'est pas un enfant ->on retourne son NSS
    // if ((beneficiaireEnfant == null)
    // && ISFSituationFamiliale.CS_ETAT_CIVIL_CELIBATAIRE.equals(beneficiaire.getCsEtatCivil())) {
    // result = beneficiaire.getNss();
    // }
    //
    // // Si le requérant est également le bénéficiaire...
    // else if (requerant.getIdTiers().equals(beneficiaire.getIdTiers())) {
    // result = requerant.getNss();
    // }
    //
    // else if (beneficiaireEnfant != null) {
    //
    // globaz.hera.api.ISFMembreFamille[] parents = sf.getParentsParMbrFamille(beneficiaireEnfant
    // .getIdMembreFamille());
    //
    // // 5° Si enfant dont les parents n'ont pas eu de rente principale, prendre le plus
    // // jeune enfant comme requérant à passer pour la GED.
    // if (!this.isRentePrincipaleForIdTiers(session, parents[0].getIdTiers(), null, null) &&
    // !this.isRentePrincipaleForIdTiers(session, parents[1].getIdTiers(), null, null)) {
    //
    // enfantLePlusJeune = this.retrieveEnfantLePlusJeune(session, mfs);
    // if (enfantLePlusJeune != null) {
    // result = enfantLePlusJeune.getNss();
    // } else {
    // result = beneficiaireEnfant.getNss();
    // }
    // }
    //
    // // 6° Si enfant dont au moins un des parents a reçu une rente principale,
    // // idem tests du n° 2.
    // else {
    // // Un des deux parents doit être requérant
    //
    // boolean isRequerantP0 = sf
    // .isRequerant(parents[0].getIdTiers(), ISFSituationFamiliale.CS_DOMAINE_RENTES);
    // if (!isRequerantP0) {
    // isRequerantP0 = sf.isRequerant(parents[0].getIdTiers(), ISFSituationFamiliale.CS_DOMAINE_STANDARD);
    // }
    //
    // if (isRequerantP0) {
    // // TODO contrôler avec RJE si le test doit être refait avec les parents !!!
    // result = this.doTraitementCas2EntreParents(session, sf, parents[0], parents[1]);
    // } else {
    // result = this.doTraitementCas2EntreParents(session, sf, parents[1], parents[0]);
    // }
    // }
    // } else {
    // if (ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT.equals(beneficiaire.getRelationAuRequerant())) {
    //
    // result = this.doTraitementCas2(session, sf, requerant, beneficiaire);
    // }
    // }
    //
    // if (JadeStringUtil.isBlankOrZero(result)) {
    // return requerant.getNss();
    // } else {
    // return result;
    // }
    // }
    //
    // {
    // this.e.printStackTrace();
    // if (twBeneficiaire != null) {
    // return twBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
    // } else {
    // return "";
    // }
    // } catch (}) /*
    // * Les règles pour l'envoi à la GED ou les appels de la GED depuis l'application
    // * des rentes sont changées pour la caisse CCB à Genève.
    // *

    // 1) si le requérant est célibataire, sur la base du NSS du requérant;
    // 2) si marié, toujours sur la base du NSS du mari;
    // 3) si divorcé ou séparé, sur la base de chaque numéro (soit GED sur la base du NSS de Monsieur et GED sur la base
    // du NSS de Madame) ;
    // 4) si décédé, sur la base du NSS du conjoint qui touche la rente de conjoint survivant;
    // 4a) s'il s'agit d'un enfant dont un des parents est décédé, sur la base du NSS du conjoint qui touche la rente
    // (point N° 4)
    // 5) s'il s'agit d'un enfant dont les parents n'ont pas de rentes principales, il y a lieu de prendre en compte le
    // plus jeune des enfants (NSS) pour la GED;
    // 6) s'il s'agit d'un enfant dont au moins un des parents a reçu une rente principale:
    // - si les deux parents touchent une rente, sous le N° NSS du mari (point N° 2)
    // - si seul un des deux conjoints touchent une rente sur la base du N° NSS du conjoint qui touche la rente.
    // */

}
