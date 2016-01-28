package globaz.hera.impl;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.util.JAUtil;
import globaz.hera.api.ISFEnfant;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFPeriode;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuEnfant;
import globaz.hera.db.famille.SFApercuEnfantManager;
import globaz.hera.db.famille.SFApercuRequerant;
import globaz.hera.db.famille.SFApercuRequerantManager;
import globaz.hera.db.famille.SFConjoint;
import globaz.hera.db.famille.SFConjointManager;
import globaz.hera.db.famille.SFEnfant;
import globaz.hera.db.famille.SFEnfantManager;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.db.famille.SFPeriode;
import globaz.hera.db.famille.SFPeriodeManager;
import globaz.hera.db.famille.SFRelationConjoint;
import globaz.hera.db.famille.SFRelationConjointManager;
import globaz.hera.db.famille.SFRequerant;
import globaz.hera.helpers.famille.SFRequerantHelper;
import globaz.hera.impl.standard.SFMembreFamilleStd;
import globaz.hera.impl.standard.SFRelationFamilialeManagerStd;
import globaz.hera.impl.standard.SFRelationFamilialeStd;
import globaz.hera.interfaces.tiers.SFTiersHelper;
import globaz.hera.interfaces.tiers.SFTiersWrapper;
import globaz.hera.vb.famille.SFApercuRelationConjointListViewBean;
import globaz.hera.vb.famille.SFApercuRelationConjointViewBean;
import globaz.hera.wrapper.SFEnfantWrapper;
import globaz.hera.wrapper.SFMembreFamilleRequerantWrapper;
import globaz.hera.wrapper.SFMembreFamilleWrapper;
import globaz.hera.wrapper.SFPeriodeWrapper;
import globaz.hera.wrapper.SFRelationFamilialeWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.tools.PRAssert;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author mmu, scr
 * 
 */
public abstract class ASFSituationFamiliale extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    class Key {

        private String idC1 = "";
        private String idC2 = "";

        public Key(String idC1, String idC2) {
            this.idC1 = idC1;
            this.idC2 = idC2;
        }

        public String getKey() {
            long id1 = Long.parseLong(idC1);
            long id2 = Long.parseLong(idC2);

            if (id1 < id2) {
                return idC1 + "-" + idC2;
            } else {
                return idC2 + "-" + idC1;
            }

        }

    }

    protected ISFMembreFamille _addMembreCelibataire(String idTiers, String csDomaine) throws Exception {

        // Voir si le membre existe !

        SFMembreFamille mf = new SFMembreFamille();
        mf.setSession(getSession());
        mf.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
        mf.setIdTiers(idTiers);
        mf.setCsDomaineApplication(csDomaine);
        mf.retrieve();
        SFRequerantHelper rh = new SFRequerantHelper();
        ISFMembreFamille iMembre;

        // S'il n'existe pas
        if (mf.isNew()) {
            // Vérifie que le Tiers soit bien dans les tiers
            if (JadeStringUtil.isEmpty(idTiers)) {
                throw new Exception(getSession().getLabel("ERROR_TIERS_NON_TROUVE"));
            }
            SFTiersWrapper tWrapper = SFTiersHelper.getTiersParId(getSession(), idTiers);
            if (tWrapper == null) {
                throw new Exception(getSession().getLabel("ERROR_TIERS_NON_TROUVE"));
            }

            // Crée le membre
            SFMembreFamille membre = new SFMembreFamille();
            membre.setSession(getSession());
            membre.setIdTiers(idTiers);
            membre.setCsDomaineApplication(csDomaine);
            membre.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
            membre.retrieve();
            if (membre.isNew()) {
                membre.add();
            }
            rh.throwExceptionIfError(null, membre);

            SFApercuRequerant requerant = new SFApercuRequerant();
            requerant.setISession(getSession());
            requerant.setAlternateKey(SFApercuRequerant.ALT_KEY_IDMEMBRE);
            requerant.setIdMembreFamille(membre.getIdMembreFamille());
            requerant.setIdDomaineApplication(csDomaine);
            requerant.retrieve();

            // Si le membre n'existe pas encore dans la table de requerant,
            if (requerant.isNew()) {
                // On crée le requérant
                SFRequerant req = new SFRequerant();
                req.setISession(getSession());
                req.setIdDomaineApplication(csDomaine);
                req.setIdMembreFamille(membre.getIdMembreFamille());
                req.add();

                rh.throwExceptionIfError(null, req);

            }

            SFMembreFamilleWrapper membreWrapper = new SFMembreFamilleWrapper();
            membreWrapper.setIdTiers(membre.getIdTiers());

            iMembre = membreWrapper;

            // S'il existe
        } else {

            SFMembreFamilleWrapper membreWrapper = new SFMembreFamilleWrapper();
            membreWrapper.setIdTiers(mf.getIdTiers());

            iMembre = membreWrapper;

        }

        return iMembre;
    }

    @Override
    protected boolean _allowAdd() {
        return false;
    }

    @Override
    protected boolean _allowDelete() {
        return false;
    }

    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getMembresFamille(java.lang.String, java.lang.String)
     */
    protected ISFMembreFamilleRequerant[] _getMembresFamille(String idTiers, String csDomaine) throws Exception {

        // Pour rechercher tous les membres de la famille, même l'idTiers passé en paramètre
        // n'est pas requérant

        // Liste des membres de famille
        List membreList = new ArrayList();

        // 1. Rechercher le membre famille dans la table des membresFamilles
        // --> Ajouter le membre famille dans la liste
        SFMembreFamille mbrFam = new SFMembreFamille();
        mbrFam.setSession(getSession());
        mbrFam.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
        mbrFam.setIdTiers(idTiers);
        mbrFam.setCsDomaineApplication(csDomaine);
        mbrFam.retrieve();

        if (mbrFam.isNew()) {
            throw new Exception(getSession().getLabel("ERROR_MEMBRE_FAMILLE_INTROUVABLE") + idTiers);
        }

        SFMembreFamilleRequerantWrapper wrapper = new SFMembreFamilleRequerantWrapper();
        wrapper.setCsCantonDomicile(mbrFam.getCsCantonDomicile());
        wrapper.setCsEtatCivil(mbrFam.getCsEtatCivil());
        wrapper.setCsNationalite(mbrFam.getCsNationalite());
        wrapper.setCsSexe(mbrFam.getCsSexe());
        wrapper.setDateDeces(mbrFam.getDateDeces());
        wrapper.setDateNaissance(mbrFam.getDateNaissance());
        wrapper.setIdMembreFamille(mbrFam.getIdMembreFamille());
        wrapper.setNom(mbrFam.getNom());
        wrapper.setNss(mbrFam.getNss());
        wrapper.setPrenom(mbrFam.getPrenom());
        wrapper.setRelationAuRequerant(mbrFam.getRelationAuRequerant());
        wrapper.setIdTiers(mbrFam.getIdTiers());
        wrapper.setPays(mbrFam.getPays());
        membreList.add(wrapper);

        // 2. Rechercher dans la table des conjoints l'(es) autre(s) conjoint(s) du membre 1
        // --> Ajouter le(s) membre(s) famille(s) dans la liste
        // --> Retourner le(s) id(s) conjoint

        List idsConjoints = new ArrayList();

        SFConjointManager conjMgr = new SFConjointManager();
        conjMgr.setSession(getSession());
        conjMgr.setForIdConjoint(mbrFam.getIdMembreFamille());
        conjMgr.find(BManager.SIZE_NOLIMIT);

        for (Iterator iterator = conjMgr.iterator(); iterator.hasNext();) {
            SFConjoint conjoint = (SFConjoint) iterator.next();

            SFMembreFamille mbrFam1 = new SFMembreFamille();

            if (!conjoint.getIdConjoint1().equals(mbrFam.getIdMembreFamille())) {
                mbrFam1.setSession(getSession());
                mbrFam1.setIdMembreFamille(conjoint.getIdConjoint1());
                mbrFam1.retrieve();
            } else {
                mbrFam1.setSession(getSession());
                mbrFam1.setIdMembreFamille(conjoint.getIdConjoint2());
                mbrFam1.retrieve();
            }

            if (mbrFam1.isNew()) {
                throw new Exception(getSession().getLabel("ERROR_MEMBRE_FAMILLE_INTROUVABLE")
                        + mbrFam.getIdMembreFamille());
            }

            SFMembreFamilleRequerantWrapper wrapper1 = new SFMembreFamilleRequerantWrapper();
            wrapper1.setCsCantonDomicile(mbrFam1.getCsCantonDomicile());
            wrapper1.setCsEtatCivil(mbrFam1.getCsEtatCivil());
            wrapper1.setCsNationalite(mbrFam1.getCsNationalite());
            wrapper1.setCsSexe(mbrFam1.getCsSexe());
            wrapper1.setDateDeces(mbrFam1.getDateDeces());
            wrapper1.setDateNaissance(mbrFam1.getDateNaissance());
            wrapper1.setIdMembreFamille(mbrFam1.getIdMembreFamille());
            wrapper1.setNom(mbrFam1.getNom());
            wrapper1.setNss(mbrFam1.getNss());
            wrapper1.setPrenom(mbrFam1.getPrenom());
            wrapper1.setRelationAuRequerant(mbrFam1.getRelationAuRequerant());
            wrapper1.setIdTiers(mbrFam1.getIdTiers());

            membreList.add(wrapper1);

            String key = conjoint.getIdConjoints();

            if (!idsConjoints.contains(key)) {
                idsConjoints.add(key);
            }
        }

        // 3. Rechercher dans la table des enfants tous les enfants avec idConjoint de 2
        // --> Ajouter tous les membres familles avec idConjoint retournés sous 2 dans la liste

        for (Iterator iterator = idsConjoints.iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();

            SFEnfantManager enfantMgr = new SFEnfantManager();
            enfantMgr.setSession(getSession());
            enfantMgr.setForIdConjoints(key);
            enfantMgr.find(BManager.SIZE_NOLIMIT);

            for (Iterator iterator2 = enfantMgr.iterator(); iterator2.hasNext();) {
                SFEnfant enfant = (SFEnfant) iterator2.next();

                SFMembreFamille mbrFam1 = new SFMembreFamille();
                mbrFam1.setSession(getSession());
                mbrFam1.setIdMembreFamille(enfant.getIdMembreFamille());
                mbrFam1.retrieve();

                if (mbrFam1.isNew()) {
                    throw new Exception(getSession().getLabel("ERROR_MEMBRE_FAMILLE_INTROUVABLE")
                            + enfant.getIdMembreFamille());
                }

                SFMembreFamilleRequerantWrapper wrapper1 = new SFMembreFamilleRequerantWrapper();
                wrapper1.setCsCantonDomicile(mbrFam1.getCsCantonDomicile());
                wrapper1.setCsEtatCivil(mbrFam1.getCsEtatCivil());
                wrapper1.setCsNationalite(mbrFam1.getCsNationalite());
                wrapper1.setCsSexe(mbrFam1.getCsSexe());
                wrapper1.setDateDeces(mbrFam1.getDateDeces());
                wrapper1.setDateNaissance(mbrFam1.getDateNaissance());
                wrapper1.setIdMembreFamille(mbrFam1.getIdMembreFamille());
                wrapper1.setNom(mbrFam1.getNom());
                wrapper1.setNss(mbrFam1.getNss());
                wrapper1.setPrenom(mbrFam1.getPrenom());
                wrapper1.setRelationAuRequerant(mbrFam1.getRelationAuRequerant());
                wrapper1.setIdTiers(mbrFam1.getIdTiers());

                membreList.add(wrapper1);

            }

        }

        // 4. On retourne la liste de tous les membres trouvés
        // --> On met la liste des membres ds une tables d'interface

        ISFMembreFamilleRequerant[] iMembres = new ISFMembreFamilleRequerant[membreList.size()];
        for (int i = 0; i < membreList.size(); i++) {
            iMembres[i] = (ISFMembreFamilleRequerant) membreList.get(i);
        }

        return iMembres;

    }

    protected ISFMembreFamilleRequerant[] _getMembresFamilleRequerant(String idTiers, String csDomaine)
            throws Exception {
        return this._getMembresFamilleRequerant(idTiers, csDomaine, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getMembresFamille(java.lang.String, java.lang.String)
     */
    protected ISFMembreFamilleRequerant[] _getMembresFamilleRequerant(String idTiers, String csDomaine, String date)
            throws Exception {

        SFApercuRequerantManager reqMgr = new SFApercuRequerantManager();
        reqMgr.setSession(getSession());
        reqMgr.setForIdDomaineApplication(csDomaine);
        reqMgr.setForIdTiers(idTiers);
        reqMgr.find();
        SFApercuRequerant requerant = (SFApercuRequerant) reqMgr.getFirstEntity();
        if (requerant == null) {
            return null;
        }
        // String idRequerant = requerant.getIdRequerant();
        String idMembreRequerant = requerant.getIdMembreFamille();

        // Liste des membres de famille
        List membreList = new ArrayList();

        // On recherche les conjoints du requerant
        SFConjointManager conjointMgr = new SFConjointManager();
        conjointMgr.setSession(getSession());
        conjointMgr.setForIdConjoint(idMembreRequerant);
        conjointMgr.find();

        for (Iterator it = conjointMgr.iterator(); it.hasNext();) {
            SFConjoint conjoint = (SFConjoint) it.next();
            String idConjoints = conjoint.getIdConjoints();
            // recherche le type de relation
            SFRelationConjointManager relMgr = new SFRelationConjointManager();
            relMgr.setSession(getSession());
            relMgr.setForIdDesConjoints(idConjoints);
            relMgr.setOrderByDateDebutDsc(false);
            relMgr.find();
            // On garde la relation la plus ancienne
            SFRelationConjoint relation = (SFRelationConjoint) relMgr.getFirstEntity();
            // Si la relation entre les conjoints est de type indéfinie, on ne l'ajoute pas, ni leurs enfants
            if ((relation == null)
                    || ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE.equals(relation.getTypeRelation())) {
                continue;
            }

            // On ajoute les enfants et les conjoints à la famille

            // Ajoute les enfants des conjoints dans la liste
            boolean enfantAjoute = false;
            for (Iterator itEnfants = conjoint.getEnfants(null); itEnfants.hasNext();) {
                SFApercuEnfant enfant = (SFApercuEnfant) itEnfants.next();
                enfant.setRelationAuRequerant(ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT);

                if (JAUtil.isDateEmpty(date)) {
                    // si la date n'est pas renseignée, on ajoute toujours l'enfant

                    enfantAjoute = true;
                } else {
                    // la date est renseingée
                    if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), enfant.getDateNaissance(), date)) {
                        // Si l'enfant est né avant la date donnée
                        enfantAjoute = true;
                    }
                }
                if (enfantAjoute) {
                    // On va récupéré la nationalité de l'enfant dans les tiers.
                    if (!JadeStringUtil.isIntegerEmpty(enfant.getIdTiers())) {
                        ISFMembreFamille mf = this.getMembreFamille(enfant.getIdMembreFamille());
                        enfant.setCsNationalite(mf.getCsNationalite());
                    }

                    SFMembreFamilleRequerantWrapper wrapper = new SFMembreFamilleRequerantWrapper();

                    // point ouvert 935
                    // si l'enfant est dans les tiers et qu'il a une adresse de domicile
                    // on ecrase l'adresse de la sit. fam.
                    wrapper.setCsCantonDomicile(enfant.getCsCantonDomicile());
                    if (!JadeStringUtil.isIntegerEmpty(enfant.getIdTiers())) {
                        SFTiersWrapper tw = SFTiersHelper.getTiersAdresseDomicileParId(getSession(),
                                enfant.getIdTiers());

                        if (!JadeStringUtil.isEmpty(tw.getProperty(SFTiersWrapper.PROPERTY_ID_CANTON))) {
                            wrapper.setCsCantonDomicile(tw.getProperty(SFTiersWrapper.PROPERTY_ID_CANTON));
                        }
                    }

                    wrapper.setCsEtatCivil(enfant.getCsEtatCivil());
                    wrapper.setCsNationalite(enfant.getCsNationalite());
                    wrapper.setCsSexe(enfant.getCsSexe());
                    wrapper.setDateDeces(enfant.getDateDeces());
                    wrapper.setDateNaissance(enfant.getDateNaissance());
                    wrapper.setIdMembreFamille(enfant.getIdMembreFamille());
                    wrapper.setNom(enfant.getNom());
                    wrapper.setNss(enfant.getNss());
                    wrapper.setPrenom(enfant.getPrenom());
                    wrapper.setRelationAuRequerant(enfant.getRelationAuRequerant());
                    wrapper.setIdTiers(enfant.getIdTiers());
                    wrapper.setPays(enfant.getPays());
                    membreList.add(wrapper);
                }

            }

            // Recherche le conjoint
            String idMembreConjoint = conjoint.getIdMembreFamilleConjoint(idMembreRequerant);
            SFMembreFamille membre = new SFMembreFamille();
            membre.setSession(getSession());
            membre.setIdMembreFamille(idMembreConjoint);
            membre.retrieve();
            membre.setRelationAuRequerant(ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT);
            if (membre.isNew()) {
                continue;
            }

            SFMembreFamilleRequerantWrapper wrapper = new SFMembreFamilleRequerantWrapper();
            wrapper.setCsCantonDomicile(membre.getCsCantonDomicile());
            wrapper.setCsEtatCivil(membre.getCsEtatCivil());
            wrapper.setCsNationalite(membre.getCsNationalite());
            wrapper.setCsSexe(membre.getCsSexe());
            wrapper.setDateDeces(membre.getDateDeces());
            wrapper.setDateNaissance(membre.getDateNaissance());
            wrapper.setIdMembreFamille(membre.getIdMembreFamille());
            wrapper.setNom(membre.getNom());
            wrapper.setNss(membre.getNss());
            wrapper.setPrenom(membre.getPrenom());
            wrapper.setRelationAuRequerant(membre.getRelationAuRequerant());
            wrapper.setIdTiers(membre.getIdTiers());
            wrapper.setPays(membre.getPays());

            // Si la date n'est pas renseignée, on prend le conjoint
            if (JAUtil.isDateEmpty(date)) {
                membreList.add(wrapper);

            } else {
                // date date est renseignée,

                if (!JAUtil.isDateEmpty(relation.getDateDebut())
                        && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), relation.getDateDebut(), date)) {
                    // La relation commence avant la date donnée, on ajoute le conjoint
                    membreList.add(wrapper);
                } else if (enfantAjoute) {
                    // Si enfant est né avant la date, on ajoute le conjoint
                    membreList.add(wrapper);
                }
            }
        }

        // On met la liste des membres ds une tables d'interface
        ISFMembreFamilleRequerant[] iMembres = new ISFMembreFamilleRequerant[membreList.size() + 1];
        for (int i = 0; i < membreList.size(); i++) {
            iMembres[i] = (ISFMembreFamilleRequerant) membreList.get(i);
        }
        // On ajoute le requerant lui-même au résultat
        ajouterRequerant(iMembres, idMembreRequerant);
        return iMembres;
    }

    protected ISFMembreFamilleRequerant[] _getMembresFamilleRequerantParMbrFamille(String idMembreFamille,
            String csDomaine) throws Exception {

        SFApercuRequerantManager reqMgr = new SFApercuRequerantManager();
        reqMgr.setSession(getSession());
        reqMgr.setForIdDomaineApplication(csDomaine);
        reqMgr.setForIdMembreFamille(idMembreFamille);
        reqMgr.find();
        SFApercuRequerant requerant = (SFApercuRequerant) reqMgr.getFirstEntity();

        if (requerant == null) {

            // SFMembreFamilleManager mbrFam = new SFMembreFamilleManager();
            reqMgr.setSession(getSession());
            reqMgr.setForIdDomaineApplication(csDomaine);
            reqMgr.setForIdMembreFamille(idMembreFamille);
            reqMgr.find();
            SFMembreFamille mbrFamille = (SFMembreFamille) reqMgr.getFirstEntity();

            // liste des membres de famille
            List membreList = new ArrayList();

            if (mbrFamille == null) {

                // On met la liste des membres ds une tables d'interface
                ISFMembreFamilleRequerant[] iMembres = new ISFMembreFamilleRequerant[membreList.size() + 1];
                for (int i = 0; i < membreList.size(); i++) {
                    iMembres[i] = (ISFMembreFamilleRequerant) membreList.get(i);
                }

                return iMembres;

            }
            String idMbrFamille = mbrFamille.getIdMembreFamille();

            // On recherche les conjoints du membre de famille
            SFConjointManager conjointMgr = new SFConjointManager();
            conjointMgr.setSession(getSession());
            conjointMgr.setForIdConjoint(idMbrFamille);
            conjointMgr.find();

            for (Iterator it = conjointMgr.iterator(); it.hasNext();) {
                SFConjoint conjoint = (SFConjoint) it.next();
                String idConjoints = conjoint.getIdConjoints();
                // recherche le type de relation
                SFRelationConjointManager relMgr = new SFRelationConjointManager();
                relMgr.setSession(getSession());
                relMgr.setForIdDesConjoints(idConjoints);
                relMgr.setOrderByDateDebutDsc(false);
                relMgr.find();
                // On garde la relation la plus ancienne
                SFRelationConjoint relation = (SFRelationConjoint) relMgr.getFirstEntity();
                // Si la relation entre les conjoints est de type indéfinie, on ne l'ajoute pas, ni leurs enfants
                if ((relation == null)
                        || ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE.equals(relation.getTypeRelation())) {
                    continue;
                }

                // On ajoute les enfants et les conjoints à la famille

                // Ajoute les enfants des conjoints dans la liste
                boolean enfantAjoute = false;
                for (Iterator itEnfants = conjoint.getEnfants(null); itEnfants.hasNext();) {
                    SFApercuEnfant enfant = (SFApercuEnfant) itEnfants.next();
                    enfant.setRelationAuRequerant(ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT);

                    if (JAUtil.isDateEmpty(null)) {
                        // si la date n'est pas renseignée, on ajoute toujours l'enfant

                        enfantAjoute = true;
                    } else {
                        // la date est renseingée
                        if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), enfant.getDateNaissance(), null)) {
                            // Si l'enfant est né avant la date donnée
                            enfantAjoute = true;
                        }
                    }
                    if (enfantAjoute) {
                        // On va récupéré la nationalité de l'enfant dans les tiers.
                        if (!JadeStringUtil.isIntegerEmpty(enfant.getIdTiers())) {
                            ISFMembreFamille mf = this.getMembreFamille(enfant.getIdMembreFamille());
                            enfant.setCsNationalite(mf.getCsNationalite());
                        }

                        SFMembreFamilleRequerantWrapper wrapper = new SFMembreFamilleRequerantWrapper();

                        // point ouvert 935
                        // si l'enfant est dans les tiers et qu'il a une adresse de domicile
                        // on ecrase l'adresse de la sit. fam.
                        wrapper.setCsCantonDomicile(enfant.getCsCantonDomicile());
                        if (!JadeStringUtil.isIntegerEmpty(enfant.getIdTiers())) {
                            SFTiersWrapper tw = SFTiersHelper.getTiersAdresseDomicileParId(getSession(),
                                    enfant.getIdTiers());

                            if (!JadeStringUtil.isEmpty(tw.getProperty(SFTiersWrapper.PROPERTY_ID_CANTON))) {
                                wrapper.setCsCantonDomicile(tw.getProperty(SFTiersWrapper.PROPERTY_ID_CANTON));
                            }
                        }

                        wrapper.setCsEtatCivil(enfant.getCsEtatCivil());
                        wrapper.setCsNationalite(enfant.getCsNationalite());
                        wrapper.setCsSexe(enfant.getCsSexe());
                        wrapper.setDateDeces(enfant.getDateDeces());
                        wrapper.setDateNaissance(enfant.getDateNaissance());
                        wrapper.setIdMembreFamille(enfant.getIdMembreFamille());
                        wrapper.setNom(enfant.getNom());
                        wrapper.setNss(enfant.getNss());
                        wrapper.setPrenom(enfant.getPrenom());
                        wrapper.setRelationAuRequerant(enfant.getRelationAuRequerant());
                        wrapper.setIdTiers(enfant.getIdTiers());
                        wrapper.setPays(enfant.getPays());
                        membreList.add(wrapper);
                    }

                }

                // Recherche le conjoint
                String idMembreConjoint = conjoint.getIdMembreFamilleConjoint(idMbrFamille);
                SFMembreFamille membre = new SFMembreFamille();
                membre.setSession(getSession());
                membre.setIdMembreFamille(idMembreConjoint);
                membre.retrieve();
                membre.setRelationAuRequerant(ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT);
                if (membre.isNew()) {
                    continue;
                }

                SFMembreFamilleRequerantWrapper wrapper = new SFMembreFamilleRequerantWrapper();
                wrapper.setCsCantonDomicile(membre.getCsCantonDomicile());
                wrapper.setCsEtatCivil(membre.getCsEtatCivil());
                wrapper.setCsNationalite(membre.getCsNationalite());
                wrapper.setCsSexe(membre.getCsSexe());
                wrapper.setDateDeces(membre.getDateDeces());
                wrapper.setDateNaissance(membre.getDateNaissance());
                wrapper.setIdMembreFamille(membre.getIdMembreFamille());
                wrapper.setNom(membre.getNom());
                wrapper.setNss(membre.getNss());
                wrapper.setPrenom(membre.getPrenom());
                wrapper.setRelationAuRequerant(membre.getRelationAuRequerant());
                wrapper.setIdTiers(membre.getIdTiers());

                // Si la date n'est pas renseignée, on prend le conjoint
                if (JAUtil.isDateEmpty(null)) {
                    membreList.add(wrapper);

                } else {
                    // date date est renseignée,

                    if (!JAUtil.isDateEmpty(relation.getDateDebut())
                            && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), relation.getDateDebut(), null)) {
                        // La relation commence avant la date donnée, on ajoute le conjoint
                        membreList.add(wrapper);
                    } else if (enfantAjoute) {
                        // Si enfant est né avant la date, on ajoute le conjoint
                        membreList.add(wrapper);
                    }
                }
            }

            // On met la liste des membres ds une tables d'interface
            ISFMembreFamilleRequerant[] iMembres = new ISFMembreFamilleRequerant[membreList.size() + 1];
            for (int i = 0; i < membreList.size(); i++) {
                iMembres[i] = (ISFMembreFamilleRequerant) membreList.get(i);
            }

            return iMembres;

        } else {

            // String idRequerant = requerant.getIdRequerant();
            String idMembreRequerant = requerant.getIdMembreFamille();

            // Liste des membres de famille
            List membreList = new ArrayList();

            // On recherche les conjoints du requerant
            SFConjointManager conjointMgr = new SFConjointManager();
            conjointMgr.setSession(getSession());
            conjointMgr.setForIdConjoint(idMembreRequerant);
            conjointMgr.find();

            for (Iterator it = conjointMgr.iterator(); it.hasNext();) {
                SFConjoint conjoint = (SFConjoint) it.next();
                String idConjoints = conjoint.getIdConjoints();
                // recherche le type de relation
                SFRelationConjointManager relMgr = new SFRelationConjointManager();
                relMgr.setSession(getSession());
                relMgr.setForIdDesConjoints(idConjoints);
                relMgr.setOrderByDateDebutDsc(false);
                relMgr.find();
                // On garde la relation la plus ancienne
                SFRelationConjoint relation = (SFRelationConjoint) relMgr.getFirstEntity();
                // Si la relation entre les conjoints est de type indéfinie, on ne l'ajoute pas, ni leurs enfants
                if ((relation == null)
                        || ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE.equals(relation.getTypeRelation())) {
                    continue;
                }

                // On ajoute les enfants et les conjoints à la famille

                // Ajoute les enfants des conjoints dans la liste
                boolean enfantAjoute = false;
                for (Iterator itEnfants = conjoint.getEnfants(null); itEnfants.hasNext();) {
                    SFApercuEnfant enfant = (SFApercuEnfant) itEnfants.next();
                    enfant.setRelationAuRequerant(ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT);

                    if (JAUtil.isDateEmpty(null)) {
                        // si la date n'est pas renseignée, on ajoute toujours l'enfant

                        enfantAjoute = true;
                    } else {
                        // la date est renseingée
                        if (BSessionUtil.compareDateFirstLowerOrEqual(getSession(), enfant.getDateNaissance(), null)) {
                            // Si l'enfant est né avant la date donnée
                            enfantAjoute = true;
                        }
                    }
                    if (enfantAjoute) {
                        // On va récupéré la nationalité de l'enfant dans les tiers.
                        if (!JadeStringUtil.isIntegerEmpty(enfant.getIdTiers())) {
                            ISFMembreFamille mf = this.getMembreFamille(enfant.getIdMembreFamille());
                            enfant.setCsNationalite(mf.getCsNationalite());
                        }

                        SFMembreFamilleRequerantWrapper wrapper = new SFMembreFamilleRequerantWrapper();

                        // point ouvert 935
                        // si l'enfant est dans les tiers et qu'il a une adresse de domicile
                        // on ecrase l'adresse de la sit. fam.
                        wrapper.setCsCantonDomicile(enfant.getCsCantonDomicile());
                        if (!JadeStringUtil.isIntegerEmpty(enfant.getIdTiers())) {
                            SFTiersWrapper tw = SFTiersHelper.getTiersAdresseDomicileParId(getSession(),
                                    enfant.getIdTiers());

                            if (!JadeStringUtil.isEmpty(tw.getProperty(SFTiersWrapper.PROPERTY_ID_CANTON))) {
                                wrapper.setCsCantonDomicile(tw.getProperty(SFTiersWrapper.PROPERTY_ID_CANTON));
                            }
                        }

                        wrapper.setCsEtatCivil(enfant.getCsEtatCivil());
                        wrapper.setCsNationalite(enfant.getCsNationalite());
                        wrapper.setCsSexe(enfant.getCsSexe());
                        wrapper.setDateDeces(enfant.getDateDeces());
                        wrapper.setDateNaissance(enfant.getDateNaissance());
                        wrapper.setIdMembreFamille(enfant.getIdMembreFamille());
                        wrapper.setNom(enfant.getNom());
                        wrapper.setNss(enfant.getNss());
                        wrapper.setPrenom(enfant.getPrenom());
                        wrapper.setRelationAuRequerant(enfant.getRelationAuRequerant());
                        wrapper.setIdTiers(enfant.getIdTiers());

                        membreList.add(wrapper);
                    }

                }

                // Recherche le conjoint
                String idMembreConjoint = conjoint.getIdMembreFamilleConjoint(idMembreRequerant);
                SFMembreFamille membre = new SFMembreFamille();
                membre.setSession(getSession());
                membre.setIdMembreFamille(idMembreConjoint);
                membre.retrieve();
                membre.setRelationAuRequerant(ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT);
                if (membre.isNew()) {
                    continue;
                }

                SFMembreFamilleRequerantWrapper wrapper = new SFMembreFamilleRequerantWrapper();
                wrapper.setCsCantonDomicile(membre.getCsCantonDomicile());
                wrapper.setCsEtatCivil(membre.getCsEtatCivil());
                wrapper.setCsNationalite(membre.getCsNationalite());
                wrapper.setCsSexe(membre.getCsSexe());
                wrapper.setDateDeces(membre.getDateDeces());
                wrapper.setDateNaissance(membre.getDateNaissance());
                wrapper.setIdMembreFamille(membre.getIdMembreFamille());
                wrapper.setNom(membre.getNom());
                wrapper.setNss(membre.getNss());
                wrapper.setPrenom(membre.getPrenom());
                wrapper.setRelationAuRequerant(membre.getRelationAuRequerant());
                wrapper.setIdTiers(membre.getIdTiers());

                // Si la date n'est pas renseignée, on prend le conjoint
                if (JAUtil.isDateEmpty(null)) {
                    membreList.add(wrapper);

                } else {
                    // date date est renseignée,

                    if (!JAUtil.isDateEmpty(relation.getDateDebut())
                            && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), relation.getDateDebut(), null)) {
                        // La relation commence avant la date donnée, on ajoute le conjoint
                        membreList.add(wrapper);
                    } else if (enfantAjoute) {
                        // Si enfant est né avant la date, on ajoute le conjoint
                        membreList.add(wrapper);
                    }
                }
            }

            // On met la liste des membres ds une tables d'interface
            ISFMembreFamilleRequerant[] iMembres = new ISFMembreFamilleRequerant[membreList.size() + 1];
            for (int i = 0; i < membreList.size(); i++) {
                iMembres[i] = (ISFMembreFamilleRequerant) membreList.get(i);
            }
            // On ajoute le requerant lui-même au résultat
            ajouterRequerant(iMembres, idMembreRequerant);
            return iMembres;

        }

    }

    protected ISFMembreFamille[] _getParents(String idTiers, String csDomaine) throws Exception {

        SFMembreFamille mbr = new SFMembreFamille();
        mbr.setSession(getSession());
        mbr.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
        mbr.setIdTiers(idTiers);
        mbr.setCsDomaineApplication(csDomaine);
        mbr.retrieve();

        SFApercuEnfant enf = new SFApercuEnfant();
        enf.setSession(getSession());
        enf.setIdMembreFamille(mbr.getIdMembreFamille());
        enf.retrieve();

        // Liste des parents
        List parentList = new ArrayList();

        SFMembreFamilleWrapper wrapperMere = new SFMembreFamilleWrapper();

        if (enf.getMere() != null) {

            wrapperMere.setCsCantonDomicile(enf.getMere().getCsCantonDomicile());
            wrapperMere.setCsEtatCivil(enf.getMere().getCsEtatCivil());
            wrapperMere.setCsNationalite(enf.getMere().getCsNationalite());
            wrapperMere.setCsSexe(enf.getMere().getCsSexe());
            wrapperMere.setDateDeces(enf.getMere().getDateDeces());
            wrapperMere.setDateNaissance(enf.getMere().getDateNaissance());
            wrapperMere.setIdTiers(enf.getMere().getIdTiers());
            wrapperMere.setNom(enf.getMere().getNom());
            wrapperMere.setNss(enf.getMere().getNss());
            wrapperMere.setPrenom(enf.getMere().getPrenom());
            wrapperMere.setPays(enf.getMere().getPays());
            parentList.add(wrapperMere);

        }

        if (enf.getPere() != null) {

            SFMembreFamilleWrapper wrapperPere = new SFMembreFamilleWrapper();
            wrapperPere.setCsCantonDomicile(enf.getPere().getCsCantonDomicile());
            wrapperPere.setCsEtatCivil(enf.getPere().getCsEtatCivil());
            wrapperPere.setCsNationalite(enf.getPere().getCsNationalite());
            wrapperPere.setCsSexe(enf.getPere().getCsSexe());
            wrapperPere.setDateDeces(enf.getPere().getDateDeces());
            wrapperPere.setDateNaissance(enf.getPere().getDateNaissance());
            wrapperPere.setIdTiers(enf.getPere().getIdTiers());
            wrapperPere.setNom(enf.getPere().getNom());
            wrapperPere.setNss(enf.getPere().getNss());
            wrapperPere.setPrenom(enf.getPere().getPrenom());

            parentList.add(wrapperPere);

        }

        if ((enf.getPere() == null) && (enf.getMere() == null)) {

            ISFMembreFamille[] iParents = new ISFMembreFamille[0];
            return iParents;

        } else {

            // On met la liste des membres ds une tables d'interface
            ISFMembreFamille[] iParents = new ISFMembreFamille[parentList.size()];
            for (int i = 0; i < parentList.size(); i++) {
                if (parentList.get(i) != null) {
                    iParents[i] = (ISFMembreFamille) parentList.get(i);
                }
            }

            return iParents;

        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getRelationFamiliale(java.lang.String, java.lang.String)
     */
    protected ISFRelationFamiliale[] _getRelationsConjoints(String idTiersRequerant, String csDomaine, String date)
            throws Exception {
        SFApercuRequerant requerant = _getRequerant(idTiersRequerant, csDomaine);
        if ((requerant == null) && !ISFSituationFamiliale.CS_DOMAINE_STANDARD.equals(csDomaine)) {
            requerant = _getRequerant(idTiersRequerant, ISFSituationFamiliale.CS_DOMAINE_STANDARD);
        }
        if (requerant == null) {
            return null;
        }

        SFRelationFamilialeStd[] relations = getRelFam(requerant.getIdMembreFamille(), date);
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
                wrapper.setTypeLien(relation.getTypeLien(relation.getDateFin()));
                wrapper.setTypeRelation(relation.getTypeRelation());

                result[i] = wrapper;

            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getRelationFamilialeEtendue(java.lang.String, java.lang.String)
     */
    protected ISFRelationFamiliale[] _getRelationsConjointsEtendues(String idTiersRequerant, String csDomaine,
            String date) throws Exception {
        Hashtable relTbl = new Hashtable(); // Set de toutes les relations
        TreeSet membreSet = new TreeSet(); // Set des membres de la famille proche

        SFApercuRequerant requerant = _getRequerant(idTiersRequerant, csDomaine);
        if (requerant == null) {
            return null;
        }
        SFRelationFamilialeStd[] relFamProche = getRelFam(requerant.getIdMembreFamille(), date);

        // Met les membre de la famille proche excepté du tiers lui-même
        for (int i = 0; i < relFamProche.length; i++) {
            if (!relFamProche[i].getIdTiers1().equals(idTiersRequerant)) {
                membreSet.add(relFamProche[i].getIdConjoint1() + "." + relFamProche[i].getIdTiers1());
            }
            if (!relFamProche[i].getIdTiers2().equals(idTiersRequerant)) {
                membreSet.add(relFamProche[i].getIdConjoint2() + "." + relFamProche[i].getIdTiers2());
            }
        }

        // Pour tous les membres, on ajoute leurs propres membres
        Iterator it = membreSet.iterator();
        while (it.hasNext()) {
            String code = ((String) it.next());
            String idMembreFamille = code.substring(0, code.indexOf('.'));
            SFRelationFamilialeStd[] relations = getRelFam(idMembreFamille, date);
            addArrayToTbl(relations, relTbl);
        }

        // Met le resultat dans un tableau
        ISFRelationFamiliale[] result = new ISFRelationFamiliale[relTbl.size()];
        int i = 0;
        for (it = relTbl.entrySet().iterator(); it.hasNext(); i++) {
            SFRelationFamilialeStd relation = (SFRelationFamilialeStd) ((Map.Entry) it.next()).getValue();

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
            wrapper.setTypeLien(relation.getTypeLien(relation.getDateFin()));
            wrapper.setTypeRelation(relation.getTypeRelation());
            // wrapper.setTypeLien(relation.getTypeLien());

            result[i] = wrapper;
        }
        return result;
    }

    protected SFApercuRequerant _getRequerant(String idTiersRequerant, String csDomaine) throws Exception {

        SFApercuRequerant requerant = new SFApercuRequerant();
        requerant.setSession(getSession());
        requerant.setIdTiers(idTiersRequerant);
        requerant.setIdDomaineApplication(csDomaine);
        requerant.setAlternateKey(SFApercuRequerant.ALT_KEY_IDTIERS);
        requerant.retrieve();

        if (requerant.isNew()) {
            return null;
        } else {
            return requerant;
        }
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getRelationsConjointsParIdTiers(java.lang.String, java.lang.String)
     */
    protected ISFRelationFamiliale[] _getToutesRelationsConjointsParIdTiers(String idTiers1, String idTiers2,
            String csDomaine, Boolean triDateDebutDecroissant) throws Exception {

        SFMembreFamille mf1 = new SFMembreFamille();
        mf1.setSession(getSession());
        mf1.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
        mf1.setIdTiers(idTiers1);
        mf1.setCsDomaineApplication(csDomaine);
        mf1.retrieve();

        SFMembreFamille mf2 = new SFMembreFamille();
        mf2.setSession(getSession());
        mf2.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
        mf2.setIdTiers(idTiers2);
        mf2.setCsDomaineApplication(csDomaine);
        mf2.retrieve();

        SFRelationFamilialeManagerStd relMgr = new SFRelationFamilialeManagerStd();
        relMgr.setSession(getSession());
        relMgr.forIdConjoints = new String[] { mf1.getIdMembreFamille(), mf2.getIdMembreFamille() };
        relMgr.setNoWantEnfantCommun(false);
        relMgr.setNoWantRelationIndefinie(false);
        relMgr.setOrderByDateDebutDsc(triDateDebutDecroissant.booleanValue());
        relMgr.find();

        SFRelationFamilialeStd relSup = null;

        ISFRelationFamiliale[] result = new SFRelationFamilialeWrapper[relMgr.size()];
        int i = 0;
        for (Iterator it = relMgr.iterator(); it.hasNext();) {
            SFRelationFamilialeStd relation = (SFRelationFamilialeStd) it.next();

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
            wrapper.setTypeLien(relation.getTypeLien(relation.getDateFin()));
            wrapper.setTypeRelation(relation.getTypeRelation());

            // wrapper.setTypeLien(relation.getTypeLien());

            result[i++] = wrapper;

            // S'il y a un relation de type veuve, il faut ajouter la relation du type d'où la relation de veuvage a été
            // tirée
            // En effet, le veuvage, n'est pas un relation en soit mais est déduit à partir d'une relation
            if (ISFSituationFamiliale.CS_TYPE_LIEN_VEUF.equals(relation.getTypeLien())
                    || ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DECES.equals(relation.getTypeLien())) {
                relSup = new SFRelationFamilialeStd();
                relSup.copyDataFromEntity(relation);

                relSup.setTypeLien(relation.relConjToTypeLien(relation.getTypeRelation(),
                        !isPartenariatEntrePersonneDuMemeSexe(relation.getIdConjoint1(), relation.getIdConjoint2()))); // On
                // prend
                // la
                // relation
                // conjugale
                // originale
                relSup.setDateDebut(relation.getDateDebutRelation());
                relSup.setDateFin(relation.getDateFinRelation());
            }
        }
        // Si une relation de mariage a été crée, on ajoute cette relation au resultat
        if (relSup != null) {
            ISFRelationFamiliale[] result2 = null;
            result2 = new SFRelationFamilialeWrapper[relMgr.size() + 1];
            // Croissant
            if (!triDateDebutDecroissant.booleanValue()) {

                for (int j = 1; j <= result.length; j++) {
                    result2[j] = result[j - 1];
                }
            }
            // Décroissant
            else {
                for (int j = 0; j < result.length; j++) {
                    result2[j] = result[j];
                }
            }

            SFRelationFamilialeWrapper wrapper = new SFRelationFamilialeWrapper();
            wrapper.setDateDebut(relSup.getDateDebut());
            wrapper.setDateDebutRelation(relSup.getDateDebut());
            wrapper.setDateFin(relSup.getDateFin());
            wrapper.setIdMembreFamilleFemme(relSup.getIdMembreFamilleFemme());
            wrapper.setIdMembreFamilleHomme(relSup.getIdMembreFamilleHomme());
            wrapper.setNoAvsFemme(relSup.getNoAvsFemme());
            wrapper.setNoAvsHomme(relSup.getNoAvsHomme());
            wrapper.setNomFemme(relSup.getNomFemme());
            wrapper.setNomHomme(relSup.getNomHomme());
            wrapper.setPrenomFemme(relSup.getPrenomFemme());
            wrapper.setPrenomHomme(relSup.getPrenomHomme());
            wrapper.setTypeLien(relSup.getTypeLien());
            wrapper.setTypeRelation(relSup.getTypeRelation());

            // Croissant
            if (!triDateDebutDecroissant.booleanValue()) {
                result2[0] = wrapper;
            }
            // Décroissant
            else {
                result2[result.length] = wrapper;
            }
            return result2;
        } else {
            return result;
        }
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    protected void addArrayToTbl(SFRelationFamilialeStd[] rel, Hashtable relTbl) {
        for (int i = 0; i < rel.length; i++) {
            SFRelationFamilialeStd relation = rel[i];
            relTbl.put(relation.getIdConjoints(), relation);
        }
    }

    // protected void addArrayToTbl(SFRelationFamilialeStd[] rel, Hashtable relTbl) {
    // for (int i = 0; i < rel.length; i++) {
    // relSet.add(rel[i]);
    // }
    // }

    /*
     * Ajoute le requeraant à la dernière position du tableau
     */
    private void ajouterRequerant(ISFMembreFamilleRequerant[] iMembres, String idMembre) throws Exception {
        globaz.hera.db.famille.SFMembreFamille requerant = new globaz.hera.db.famille.SFMembreFamille();
        requerant.setSession(getSession());
        requerant.setIdMembreFamille(idMembre);
        requerant.retrieve();
        requerant.setRelationAuRequerant(ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT);

        SFMembreFamilleRequerantWrapper wrapper = new SFMembreFamilleRequerantWrapper();
        wrapper.setCsCantonDomicile(requerant.getCsCantonDomicile());
        wrapper.setCsEtatCivil(requerant.getCsEtatCivil());
        wrapper.setCsNationalite(requerant.getCsNationalite());
        wrapper.setCsSexe(requerant.getCsSexe());
        wrapper.setDateDeces(requerant.getDateDeces());
        wrapper.setDateNaissance(requerant.getDateNaissance());
        wrapper.setIdMembreFamille(requerant.getIdMembreFamille());
        wrapper.setNom(requerant.getNom());
        wrapper.setNss(requerant.getNss());
        wrapper.setPrenom(requerant.getPrenom());
        wrapper.setRelationAuRequerant(requerant.getRelationAuRequerant());
        wrapper.setIdTiers(requerant.getIdTiers());
        wrapper.setPays(requerant.getPays());
        iMembres[iMembres.length - 1] = wrapper;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getDetailEnfant(java.lang.String)
     */
    public ISFEnfant getEnfant(String idMembreFamille) throws Exception {
        SFApercuEnfant enfant = new SFApercuEnfant();
        enfant.setSession(getSession());
        enfant.setIdMembreFamille(idMembreFamille);
        enfant.retrieve();

        SFEnfantWrapper enfantW = new SFEnfantWrapper();
        enfantW.setDateAdoption(enfant.getDateAdoption());
        enfantW.setNoAvsMere(enfant.getNoAvsMere());
        enfantW.setNoAvsPere(enfant.getNoAvsPere());
        enfantW.setNomMere(enfant.getNomMere());
        enfantW.setNomPere(enfant.getNomPere());
        enfantW.setNss(enfant.getNss());
        enfantW.setPrenomMere(enfant.getPrenomMere());
        enfantW.setPrenomPere(enfant.getPrenomPere());
        enfantW.setRecueilli(enfant.isRecueilli());

        return enfantW;
    }

    /**
     * @return le dernier état civil connu ou null si inconnu
     */
    public ISFMembreFamille getMembreFamille(String idMembre) throws Exception {
        return this.getMembreFamille(idMembre, null);
    }

    /**
     * @return l'état civil à la date donné ou null si inconnu
     */
    public ISFMembreFamille getMembreFamille(String idMembre, String date) throws Exception {
        SFMembreFamilleStd membre = new SFMembreFamilleStd();
        membre.setSession(getSession());
        membre.setIdMembreFamille(idMembre);
        membre.retrieve();

        SFMembreFamilleWrapper mf = new SFMembreFamilleWrapper();
        mf.setCsCantonDomicile(membre.getCsCantonDomicile());
        mf.setCsNationalite(membre.getCsNationalite());
        mf.setCsSexe(membre.getCsSexe());
        mf.setDateDeces(membre.getDateDeces());
        mf.setDateNaissance(membre.getDateNaissance());
        mf.setNom(membre.getNom());
        mf.setNss(membre.getNss());
        mf.setPrenom(membre.getPrenom());
        mf.setIdTiers(membre.getIdTiers());
        mf.setPays(membre.getPays());

        membre.setDateEtatCivil(date); // Fixe la date à laquelle l'état civil doit être calculé
        mf.setCsEtatCivil(membre.getCsEtatCivil());

        return mf;
    }

    public ISFMembreFamille[] getMembresFamilleEtendue(String idMembreFamille, Boolean inclureParents) throws Exception {

        List result = new ArrayList();
        /*
         * 
         * phase préliminaire On récupère les parents du liant....
         */
        if ((inclureParents != null) && inclureParents.booleanValue()) {
            SFApercuEnfantManager mgr0 = new SFApercuEnfantManager();
            mgr0.setSession(getSession());
            mgr0.setForIdMembreFamille(idMembreFamille);
            mgr0.find();
            if (mgr0.size() > 1) {
                throw new Exception(getSession().getLabel("ERROR_INCOHERENCE_PLUSIEURS_ENFANTS") + idMembreFamille);
            }
            if (!mgr0.isEmpty()) {
                SFApercuEnfant enfant = (SFApercuEnfant) mgr0.getFirstEntity();

                SFConjoint conjoint = new SFConjoint();
                conjoint.setIdConjoints(enfant.getIdConjoint());
                conjoint.setSession(getSession());
                conjoint.retrieve();
                PRAssert.notIsNew(conjoint, null);

                SFMembreFamille p1 = new SFMembreFamille();
                p1.setSession(getSession());
                p1.setIdMembreFamille(conjoint.getIdConjoint1());
                p1.retrieve();

                SFMembreFamille p2 = new SFMembreFamille();
                p2.setSession(getSession());
                p2.setIdMembreFamille(conjoint.getIdConjoint2());
                p2.retrieve();

                SFMembreFamilleWrapper mvo = new SFMembreFamilleWrapper();
                mvo.setCsNationalite(p1.getCsNationalite());
                mvo.setCsSexe(p1.getCsSexe());
                mvo.setDateDeces(p1.getDateDeces());
                mvo.setDateNaissance(p1.getDateNaissance());
                mvo.setIdTiers(p1.getIdTiers());
                mvo.setNom(p1.getNom());
                mvo.setNss(p1.getNss());
                mvo.setPrenom(p1.getPrenom());
                mvo.setRelationAuLiant(ISFSituationFamiliale.CS_TYPE_RELATION_PARENT);
                mvo.setPays(p1.getPays());
                result.add(mvo);

                mvo = new SFMembreFamilleWrapper();
                mvo.setCsDomaine(p2.getCsDomaineApplication());
                mvo.setCsNationalite(p2.getCsNationalite());
                mvo.setCsSexe(p2.getCsSexe());
                mvo.setDateDeces(p2.getDateDeces());
                mvo.setDateNaissance(p2.getDateNaissance());
                mvo.setIdTiers(p2.getIdTiers());
                mvo.setNom(p2.getNom());
                mvo.setNss(p2.getNss());
                mvo.setPrenom(p2.getPrenom());
                mvo.setPays(p2.getPays());
                mvo.setRelationAuLiant(ISFSituationFamiliale.CS_TYPE_RELATION_PARENT);
                result.add(mvo);
            }
        }

        Map conjoints = new HashMap();

        /*
         * 
         * 1ère passe, construction de la famille proche
         */
        SFApercuRelationConjointListViewBean mgr = new SFApercuRelationConjointListViewBean();
        mgr.setSession(getSession());
        mgr.setForIdConjoint(idMembreFamille);
        mgr.find();

        // Ajout des conjoints
        for (Iterator iterator = mgr.iterator(); iterator.hasNext();) {
            SFApercuRelationConjointViewBean elm = (SFApercuRelationConjointViewBean) iterator.next();
            SFMembreFamilleWrapper w = new SFMembreFamilleWrapper();
            Key k = new Key(elm.getIdConjoint1(), elm.getIdConjoint2());
            if (!conjoints.containsKey(k)) {
                if (idMembreFamille.equals(elm.getIdConjoint2())) {
                    conjoints.put(k, elm.getIdConjoint1());
                    w.setCsDomaine(elm.getCsDomaine());
                    w.setCsNationalite(elm.getNationalite1());
                    w.setCsSexe(elm.getSexe1());
                    w.setDateDeces(elm.getDateDeces1());
                    w.setDateNaissance(elm.getDateNaissance1());
                    w.setIdTiers(elm.getIdTiers1());
                    w.setNom(elm.getNom1());
                    w.setNss(elm.getNoAvs1());
                    w.setPrenom(elm.getPrenom1());
                    w.setIdTiers(elm.getIdTiers1());
                } else {
                    conjoints.put(k, elm.getIdConjoint2());
                    w.setCsDomaine(elm.getCsDomaine());
                    w.setCsNationalite(elm.getNationalite2());
                    w.setCsSexe(elm.getSexe2());
                    w.setDateDeces(elm.getDateDeces2());
                    w.setDateNaissance(elm.getDateNaissance2());
                    w.setIdTiers(elm.getIdTiers2());
                    w.setNom(elm.getNom2());
                    w.setNss(elm.getNoAvs2());
                    w.setPrenom(elm.getPrenom2());
                    w.setIdTiers(elm.getIdTiers2());
                }
                w.setRelationAuLiant(ISFSituationFamiliale.CS_TYPE_RELATION_CONJOINT);
                result.add(w);

                // Ajout des enfants
                for (Iterator iter = elm.getEnfants(null); iter.hasNext();) {
                    SFApercuEnfant enfant = (SFApercuEnfant) iter.next();

                    w = new SFMembreFamilleWrapper();
                    w.setRelationAuLiant(ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT);
                    w.setCsDomaine(enfant.getCsDomaineApplication());
                    w.setCsNationalite(enfant.getCsNationalite());
                    w.setCsSexe(enfant.getCsSexe());
                    w.setDateDeces(enfant.getDateDeces());
                    w.setDateNaissance(enfant.getDateNaissance());
                    w.setIdTiers(enfant.getIdTiers());
                    w.setNom(enfant.getNom());
                    w.setNss(enfant.getNss());
                    w.setPrenom(enfant.getPrenom());
                    result.add(w);
                }
            }
        }

        /*
         * 
         * 2ème passe, construction de la famille étendue
         * 
         * Conjoints des conjoints du liant !!!
         */
        // Pour chaque conjoint, on récupère ses ex-conjoints.
        Set keys = conjoints.keySet();

        Map exConjoints = new HashMap();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            Key k = (Key) iterator.next();
            String idDuConjointDuLiant = (String) conjoints.get(k);

            // On skip le conjoint inconnu...
            if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(idDuConjointDuLiant)) {
                continue;
            }

            mgr = new SFApercuRelationConjointListViewBean();
            mgr.setSession(getSession());
            mgr.setForIdConjoint(idDuConjointDuLiant);
            mgr.find();

            for (iterator = mgr.iterator(); iterator.hasNext();) {
                SFApercuRelationConjointViewBean elm = (SFApercuRelationConjointViewBean) iterator.next();
                if (idMembreFamille.equals(elm.getIdConjoint1()) || idMembreFamille.equals(elm.getIdConjoint2())) {
                    // On ne reprend pas le liant, car déjà traité dans
                    // la phase 1
                    continue;
                }

                Key kk = new Key(elm.getIdConjoint1(), elm.getIdConjoint2());
                if (!exConjoints.containsKey(kk)) {

                    SFMembreFamilleWrapper w = new SFMembreFamilleWrapper();
                    exConjoints.put(kk, elm.getIdConjoint1());
                    w.setCsDomaine(elm.getCsDomaine());
                    w.setCsNationalite(elm.getNationalite1());
                    w.setCsSexe(elm.getSexe1());
                    w.setDateDeces(elm.getDateDeces1());
                    w.setDateNaissance(elm.getDateNaissance1());
                    w.setIdTiers(elm.getIdTiers1());
                    w.setNom(elm.getNom1());
                    w.setNss(elm.getNoAvs1());
                    w.setPrenom(elm.getPrenom1());
                    w.setIdTiers(elm.getIdTiers1());
                    // Pas de relation définie pour les ex-conjoints !!!
                    w.setRelationAuLiant(null);
                    result.add(w);

                    w = new SFMembreFamilleWrapper();
                    w.setCsDomaine(elm.getCsDomaine());
                    w.setCsNationalite(elm.getNationalite2());
                    w.setCsSexe(elm.getSexe2());
                    w.setDateDeces(elm.getDateDeces2());
                    w.setDateNaissance(elm.getDateNaissance2());
                    w.setIdTiers(elm.getIdTiers2());
                    w.setNom(elm.getNom2());
                    w.setNss(elm.getNoAvs2());
                    w.setPrenom(elm.getPrenom2());
                    w.setIdTiers(elm.getIdTiers2());
                    // Pas de relation définie pour les ex-conjoints !!!
                    w.setRelationAuLiant(null);
                    result.add(w);

                    for (Iterator iter = elm.getEnfants(null); iter.hasNext();) {
                        SFApercuEnfant enfant = (SFApercuEnfant) iter.next();

                        w = new SFMembreFamilleWrapper();
                        w.setRelationAuLiant(ISFSituationFamiliale.CS_TYPE_RELATION_ENFANT);
                        w.setCsDomaine(enfant.getCsDomaineApplication());
                        w.setCsNationalite(enfant.getCsNationalite());
                        w.setCsSexe(enfant.getCsSexe());
                        w.setDateDeces(enfant.getDateDeces());
                        w.setDateNaissance(enfant.getDateNaissance());
                        w.setIdTiers(enfant.getIdTiers());
                        w.setNom(enfant.getNom());
                        w.setNss(enfant.getNss());
                        w.setPrenom(enfant.getPrenom());
                        result.add(w);
                    }
                }
            }
        }
        return (ISFMembreFamille[]) result.toArray(new ISFMembreFamille[result.size()]);
    }

    public ISFMembreFamille[] getParentsParMbrFamille(String idMembreFamille) throws Exception {

        SFMembreFamille mbr = new SFMembreFamille();
        mbr.setSession(getSession());
        mbr.setIdMembreFamille(idMembreFamille);
        mbr.retrieve();

        SFApercuEnfant enf = new SFApercuEnfant();
        enf.setSession(getSession());
        enf.setIdMembreFamille(mbr.getIdMembreFamille());
        enf.retrieve();

        // Liste des parents
        List parentList = new ArrayList();

        SFMembreFamilleWrapper wrapperMere = new SFMembreFamilleWrapper();

        if (enf.getMere() != null) {

            wrapperMere.setCsCantonDomicile(enf.getMere().getCsCantonDomicile());
            wrapperMere.setCsEtatCivil(enf.getMere().getCsEtatCivil());
            wrapperMere.setCsNationalite(enf.getMere().getCsNationalite());
            wrapperMere.setCsSexe(enf.getMere().getCsSexe());
            wrapperMere.setDateDeces(enf.getMere().getDateDeces());
            wrapperMere.setDateNaissance(enf.getMere().getDateNaissance());
            wrapperMere.setIdTiers(enf.getMere().getIdTiers());
            wrapperMere.setNom(enf.getMere().getNom());
            wrapperMere.setNss(enf.getMere().getNss());
            wrapperMere.setPrenom(enf.getMere().getPrenom());
            wrapperMere.setPays(enf.getMere().getPays());

            parentList.add(wrapperMere);

        }

        if (enf.getPere() != null) {

            SFMembreFamilleWrapper wrapperPere = new SFMembreFamilleWrapper();
            wrapperPere.setCsCantonDomicile(enf.getPere().getCsCantonDomicile());
            wrapperPere.setCsEtatCivil(enf.getPere().getCsEtatCivil());
            wrapperPere.setCsNationalite(enf.getPere().getCsNationalite());
            wrapperPere.setCsSexe(enf.getPere().getCsSexe());
            wrapperPere.setDateDeces(enf.getPere().getDateDeces());
            wrapperPere.setDateNaissance(enf.getPere().getDateNaissance());
            wrapperPere.setIdTiers(enf.getPere().getIdTiers());
            wrapperPere.setNom(enf.getPere().getNom());
            wrapperPere.setNss(enf.getPere().getNss());
            wrapperPere.setPrenom(enf.getPere().getPrenom());

            parentList.add(wrapperPere);

        }

        if ((enf.getPere() == null) && (enf.getMere() == null)) {

            ISFMembreFamille[] iParents = new ISFMembreFamille[0];
            return iParents;

        } else {

            // On met la liste des membres ds une tables d'interface
            ISFMembreFamille[] iParents = new ISFMembreFamille[parentList.size()];
            for (int i = 0; i < parentList.size(); i++) {
                if (parentList.get(i) != null) {
                    iParents[i] = (ISFMembreFamille) parentList.get(i);
                }
            }

            return iParents;

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getPeriode(java.lang.String)
     */
    public ISFPeriode[] getPeriodes(String idMembreFamille) throws Exception {
        return this.getPeriodes(idMembreFamille, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getPeriode(java.lang.String, java.lang.String)
     */
    public ISFPeriode[] getPeriodes(String idMembreFamille, String typePeriode) throws Exception {
        SFPeriodeManager periodeMgr = new SFPeriodeManager();
        periodeMgr.setSession(getSession());
        periodeMgr.setForIdMembreFamille(idMembreFamille);
        if (!JadeStringUtil.isIntegerEmpty(typePeriode)) {
            periodeMgr.setForType(typePeriode);
        }
        periodeMgr.find();

        // On met la liste des periodes ds une tables d'interface
        ISFPeriode[] iPeriodes = new ISFPeriode[periodeMgr.size()];

        for (int i = 0; i < periodeMgr.size(); i++) {
            SFPeriode periode = (SFPeriode) periodeMgr.get(i);
            SFPeriodeWrapper wrapper = new SFPeriodeWrapper();
            wrapper.setDateDebut(periode.getDateDebut());
            wrapper.setDateFin(periode.getDateFin());
            wrapper.setIdDetenteurBTE(periode.getIdDetenteurBTE());
            wrapper.setNoAvs(periode.getNoAvs());
            wrapper.setNoAvsDetenteurBTE(periode.getNoAvsDetenteurBTE());
            wrapper.setPays(periode.getPays());
            wrapper.setType(periode.getType());
            wrapper.setCsTypeDeDetenteur(periode.getCsTypeDeDetenteur());
            iPeriodes[i] = wrapper;
        }

        return iPeriodes;
    }

    protected SFRelationFamilialeStd[] getRelFam(String idMembreFamille, String date) throws Exception {

        SFRelationFamilialeManagerStd relMgr = new SFRelationFamilialeManagerStd();
        relMgr.setSession(getSession());
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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.hera.api.ISFSituationFamiliale#getRelationsConjoints(java.lang.String, java.lang.String)
     */
    public ISFRelationFamiliale[] getToutesRelationsConjoints(String membreFamille1, String membreFamille2,
            Boolean triDateDebutDecroissant) throws Exception {
        SFRelationFamilialeManagerStd relMgr = new SFRelationFamilialeManagerStd();
        relMgr.setSession(getSession());
        relMgr.forIdConjoints = new String[] { membreFamille1, membreFamille2 };
        relMgr.setNoWantEnfantCommun(false);
        relMgr.setNoWantRelationIndefinie(false);
        relMgr.setOrderByDateDebutDsc(triDateDebutDecroissant.booleanValue());
        relMgr.find();

        SFRelationFamilialeStd relSup = null;

        ISFRelationFamiliale[] result = new SFRelationFamilialeWrapper[relMgr.size()];
        int i = 0;
        for (Iterator it = relMgr.iterator(); it.hasNext();) {
            SFRelationFamilialeStd relation = (SFRelationFamilialeStd) it.next();

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
            // avec comme référence, la date avec valeur == null.
            // On reset ce type de lien pour reforcer son calcul à la date de fin de la relation.
            relation.setTypeLien(null);
            wrapper.setTypeLien(relation.getTypeLien(relation.getDateFin()));
            wrapper.setTypeRelation(relation.getTypeRelation());

            result[i++] = wrapper;

            // S'il y a un relation de type veuve, il faut ajouter la relation du type d'où la relation de veuvage a été
            // tirée
            // En effet, le veuvage, n'est pas un relation en soit mais est déduit à partir d'une relation
            if (ISFSituationFamiliale.CS_TYPE_LIEN_VEUF.equals(relation.getTypeLien())
                    || ISFSituationFamiliale.CS_TYPE_LIEN_LPART_DECES.equals(relation.getTypeLien())) {
                relSup = new SFRelationFamilialeStd();
                relSup.copyDataFromEntity(relation);

                relSup.setTypeLien(relation.relConjToTypeLien(relation.getTypeRelation(),
                        !isPartenariatEntrePersonneDuMemeSexe(relation.getIdConjoint1(), relation.getIdConjoint2()))); // On
                // prend
                // la
                // relation
                // conjugale
                // originale
                relSup.setDateDebut(relation.getDateDebutRelation());
                relSup.setDateFin(relation.getDateFinRelation());
            }
        }
        // Si une relation de mariage a été crée, on ajoute cette relation au resultat
        if (relSup != null) {
            ISFRelationFamiliale[] result2 = null;
            result2 = new SFRelationFamilialeWrapper[relMgr.size() + 1];
            // Croissant
            if (!triDateDebutDecroissant.booleanValue()) {

                for (int j = 1; j <= result.length; j++) {
                    result2[j] = result[j - 1];
                }
            }
            // Décroissant
            else {
                for (int j = 0; j < result.length; j++) {
                    result2[j] = result[j];
                }
            }

            SFRelationFamilialeWrapper wrapper = new SFRelationFamilialeWrapper();
            wrapper.setDateDebut(relSup.getDateDebut());
            wrapper.setDateDebutRelation(relSup.getDateDebut());
            wrapper.setDateFin(relSup.getDateFin());
            wrapper.setIdMembreFamilleFemme(relSup.getIdMembreFamilleFemme());
            wrapper.setIdMembreFamilleHomme(relSup.getIdMembreFamilleHomme());
            wrapper.setNoAvsFemme(relSup.getNoAvsFemme());
            wrapper.setNoAvsHomme(relSup.getNoAvsHomme());
            wrapper.setNomFemme(relSup.getNomFemme());
            wrapper.setNomHomme(relSup.getNomHomme());
            wrapper.setPrenomFemme(relSup.getPrenomFemme());
            wrapper.setPrenomHomme(relSup.getPrenomHomme());
            wrapper.setTypeLien(relSup.getTypeLien());
            wrapper.setTypeRelation(relSup.getTypeRelation());

            // Croissant
            if (!triDateDebutDecroissant.booleanValue()) {
                result2[0] = wrapper;
            }
            // Décroissant
            else {
                result2[result.length] = wrapper;
            }

            return result2;
        } else {
            return result;
        }
    }

    /**
     * Vrais si les deux partenaires donnes sont du meme sexe
     * 
     * @param idMembreFamille1
     * @param idMembreFamille2
     * @return
     * @throws Exception
     */
    private boolean isPartenariatEntrePersonneDuMemeSexe(String idMF1, String idMF2) {

        try {
            SFMembreFamille mf1 = new SFMembreFamille();
            mf1.setSession(getSession());
            mf1.setIdMembreFamille(idMF1);
            mf1.retrieve();

            SFMembreFamille mf2 = new SFMembreFamille();
            mf2.setSession(getSession());
            mf2.setIdMembreFamille(idMF2);
            mf2.retrieve();

            String sexeMF1 = mf1.getCsSexe();
            String sexeMF2 = mf2.getCsSexe();

            return !JadeStringUtil.isIntegerEmpty(sexeMF1) && !JadeStringUtil.isIntegerEmpty(sexeMF2)
                    && sexeMF1.equals(sexeMF2);
        } catch (Exception e) {
            return false;
        }
    }

    // Check si le requérant existe pour le domaine concerné.
    public Boolean isRequerant(String idTiersRequerant, String csDomaine) throws Exception {
        try {
            if (_getRequerant(idTiersRequerant, csDomaine) != null) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }

        } catch (Exception e) {
            return Boolean.FALSE;
        }

    }

}
