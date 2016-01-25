/*
 * créé le 01 octobre 2010
 */
package globaz.cygnus.helpers.motifsDeRefus;

import globaz.cygnus.db.demandes.RFDemandeJointSousTypeDeSoinJointTypeDeSoinJointMotifRefus;
import globaz.cygnus.db.demandes.RFDemandeJointSousTypeDeSoinJointTypeDeSoinJointMotifRefusManager;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemande;
import globaz.cygnus.db.motifsDeRefus.RFAssMotifsRefusDemandeManager;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefus;
import globaz.cygnus.db.motifsDeRefus.RFMotifsDeRefusManager;
import globaz.cygnus.db.typeDeSoins.RFSousTypeDeSoinManager;
import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.motifsDeRefus.RFRechercheMotifsDeRefusViewBean;
import globaz.cygnus.vb.motifsDeRefus.RFSoinMotif;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author fha
 */
public class RFRechercheMotifsDeRefusHelper extends PRAbstractHelper {

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            BITransaction transaction = null;
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                RFRechercheMotifsDeRefusViewBean SMViewBean = (RFRechercheMotifsDeRefusViewBean) viewBean;

                if (validationPourDB((BSession) session, SMViewBean)) {
                    // simple requete RFMOTRE
                    // on met à jour idsSoins à partir de la liste de soin qu'on
                    // a
                    RFMotifsDeRefus motre = new RFMotifsDeRefus();
                    motre.setSession(SMViewBean.getSession());
                    motre.setDescriptionFR(SMViewBean.getDescriptionFR());
                    motre.setDescriptionDE(SMViewBean.getDescriptionDE());
                    motre.setDescriptionIT(SMViewBean.getDescriptionIT());
                    motre.setDescriptionLongueFR(SMViewBean.getDescriptionLongueFR());
                    motre.setDescriptionLongueDE(SMViewBean.getDescriptionLongueDE());
                    motre.setDescriptionLongueIT(SMViewBean.getDescriptionLongueIT());
                    motre.setHasMontant(SMViewBean.getHasMontant());
                    // on met à jour idsSoins avec ce qu'on a dans le viewBean
                    // construction de la chaine idSoins à partir du tableau
                    // SMViewBean.getSoinMotifArray()
                    String chaineIdsSoins = buildIdsSoins(SMViewBean.getSoinMotifArray());
                    motre.setIdsSoin(chaineIdsSoins);

                    motre.add(transaction);

                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.setRollbackOnly();
                }
                throw e;
            } finally {
                if (transaction != null) {
                    try {
                        if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                            transaction.rollback();
                        } else {
                            transaction.commit();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    } finally {
                        transaction.closeTransaction();
                    }
                }
            }
        }
    }

    /**
     * Annulation générale On annule seulement les lignes qui ne sont pas en BDD (indiqués grace au champ
     * isChargeDepuisLaBD)
     */
    @Override
    protected void _delete(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        BITransaction transaction = null;

        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RFRechercheMotifsDeRefusViewBean vb = (RFRechercheMotifsDeRefusViewBean) viewBean;

            // on vérifie dans la table association que ce motif n'est pas
            // utilisé dans une demande
            // si non alors on le supprime
            RFAssMotifsRefusDemandeManager motifDemandeMgr = new RFAssMotifsRefusDemandeManager();
            motifDemandeMgr.setSession((BSession) session);
            motifDemandeMgr.changeManagerSize(0);
            motifDemandeMgr.find();
            Boolean motifUtilise = Boolean.FALSE;
            RFAssMotifsRefusDemande elemMOTDEM = null;
            for (Iterator<RFAssMotifsRefusDemande> it = motifDemandeMgr.iterator(); it.hasNext();) {
                elemMOTDEM = it.next();
                if (elemMOTDEM.getIdMotifsRefus().equals(vb.getIdMotifRefus())) {
                    motifUtilise = Boolean.TRUE;
                    break;
                }
            }
            if (!motifUtilise) {// alors on peut supprimer ce motif
                RFMotifsDeRefus motre = new RFMotifsDeRefus();
                motre.setSession(vb.getSession());
                motre.setIdMotifRefus(vb.getIdMotifRefus());
                motre.retrieve(transaction);
                if (!motre.isNew()) {
                    motre.delete(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(motre, "_delete()", "RFRechercheMotifsDeRefusHelper");
                }
            } else { // on affiche un message d'erreur
                RFUtils.setMsgErreurViewBean(vb, "ERREUR_RF_MOTIF_REFUS_SUPPRESSION_REFUSE");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()
                            || FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        // TODO Auto-generated method stub
        super._retrieve(viewBean, action, session);

        RFRechercheMotifsDeRefusViewBean vb = (RFRechercheMotifsDeRefusViewBean) viewBean;
        // 1er chargement
        vb.setIsFirstLoad(Boolean.TRUE);
        vb.setIsUpdate(Boolean.TRUE);
        // si le motif de refus est lié à des soins alors on ajoute ces soins au
        // tableau motifs - soins
        if (!JadeStringUtil.isEmpty(vb.getIdsSoin())) {
            // nombre de soins (= nombre de point virgule dans idsSoins +1)
            Matcher matcher = Pattern.compile(";").matcher(vb.getIdsSoin());
            int occur = 0;
            while (matcher.find()) {
                occur++;
            }
            // +1 si non vide
            if (!JadeStringUtil.isEmpty(vb.getIdsSoin())) {
                occur++;
            }

            String[] tmpCouplesSoins = vb.getIdsSoin().split(";");

            for (int i = 0; i < occur; i++) {
                if (!"0".equals(tmpCouplesSoins[i])) { // au moins un couple type/sous type de soin
                    vb.getSoinMotifArray().add(
                            new RFSoinMotif(i, vb.getIdMotifRefus(), vb.getDescriptionFR(), vb.getDescriptionIT(), vb
                                    .getDescriptionDE(), vb.getDescriptionLongueFR(), vb.getDescriptionLongueIT(), vb
                                    .getDescriptionLongueDE(), tmpCouplesSoins[i].split("-")[0], "0"
                                    .equals(tmpCouplesSoins[i].split("-")[1]) ? "*" : tmpCouplesSoins[i].split("-")[1],
                                    "idSts", Boolean.TRUE, Boolean.FALSE));
                } else { // pas de couple type/sous type de soin
                    vb.getSoinMotifArray().add(
                            new RFSoinMotif(i, vb.getIdMotifRefus(), vb.getDescriptionFR(), vb.getDescriptionIT(), vb
                                    .getDescriptionDE(), vb.getDescriptionLongueFR(), vb.getDescriptionLongueIT(), vb
                                    .getDescriptionLongueDE(), "", "", "idSts", Boolean.TRUE, Boolean.FALSE));
                }
            }

            // mettre la variable du viewBean displayHasMontant à false si ce
            // motif à une demande
            // on commence par charger le manager
            RFAssMotifsRefusDemandeManager motifDemandeMgr = new RFAssMotifsRefusDemandeManager();
            motifDemandeMgr.setSession((BSession) session);
            motifDemandeMgr.setForIdMotifDeRefus(vb.getIdMotifRefus());
            motifDemandeMgr.changeManagerSize(0);
            motifDemandeMgr.find();
            // on parcours la table et si on trouve un EGIMOT =
            // viewBean.getIdMotifRefus on met displayHasMontant = false
            // et si c'est un motif de refus systeme on fait quoi ????
            RFAssMotifsRefusDemande elemMD;
            vb.setHideHasMontant(Boolean.FALSE);
            for (int i = 0; i < motifDemandeMgr.getSize(); i++) {
                elemMD = (RFAssMotifsRefusDemande) motifDemandeMgr.get(i);
                if (elemMD.getIdMotifsRefus().equals(vb.getIdMotifRefus())) {
                    vb.setHideHasMontant(Boolean.FALSE);
                    break;
                }
            }
        }
    }

    @Override
    protected void _update(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {

            BITransaction transaction = null;
            try {
                transaction = ((BSession) session).newTransaction();
                transaction.openTransaction();

                RFRechercheMotifsDeRefusViewBean SMViewBean = (RFRechercheMotifsDeRefusViewBean) viewBean;

                // sommes nous en update ou ajout?

                if (validationPourDB((BSession) session, SMViewBean)) {
                    // simple requete RFMOTRE
                    // on met à jour idsSoins à partir de la liste de soin qu'on
                    // a
                    RFMotifsDeRefus motre = new RFMotifsDeRefus();
                    motre.setSession((BSession) session);
                    motre.setIdMotifRefus(SMViewBean.getIdMotifRefus());
                    motre.retrieve();
                    if (!motre.isNew()) {
                        // on met à jour idsSoins avec ce qu'on a dans le
                        // viewBean
                        // construction de la chaine idSoins à partir du tableau
                        // SMViewBean.getSoinMotifArray()
                        String chaineIdsSoins = buildIdsSoins(SMViewBean.getSoinMotifArray());
                        // on update hasMontant et la description (seulement si
                        // elle n'est pas déjà utilisé?)
                        motre.setDescriptionFR(SMViewBean.getDescriptionFR());
                        motre.setDescriptionDE(SMViewBean.getDescriptionDE());
                        motre.setDescriptionIT(SMViewBean.getDescriptionIT());
                        motre.setDescriptionLongueFR(SMViewBean.getDescriptionLongueFR());
                        motre.setDescriptionLongueDE(SMViewBean.getDescriptionLongueDE());
                        motre.setDescriptionLongueIT(SMViewBean.getDescriptionLongueIT());
                        if (SMViewBean.getIsMotifRefusSysteme()) {
                            motre.setHasMontant(true);
                            if (JadeStringUtil.isBlankOrZero(chaineIdsSoins)) {
                                motre.setIdsSoin("0");
                            } else {
                                motre.setIdsSoin(chaineIdsSoins);
                            }
                        } else {
                            motre.setHasMontant(SMViewBean.getHasMontant());
                            motre.setIdsSoin(chaineIdsSoins);
                        }

                        motre.update();
                    } else {
                        RFUtils.setMsgErreurInattendueViewBean(SMViewBean, "_update()",
                                "RFRechercheMotifsDeRefusHelper");
                    }
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.setRollbackOnly();
                }
                throw e;
            } finally {
                if (transaction != null) {
                    try {
                        if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                            transaction.rollback();
                        } else {
                            transaction.commit();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                    } finally {
                        transaction.closeTransaction();
                    }
                }
            }
        }
    }

    /**
     * Ajout d'un couple motif de refus/soin
     */
    public void ajouterSoinMotif(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            RFRechercheMotifsDeRefusViewBean soinMotifVB = (RFRechercheMotifsDeRefusViewBean) viewBean;
            // récupération du ArrayList
            List<RFSoinMotif> soinMotifArray = new ArrayList<RFSoinMotif>();
            // création d'un objet RFFournisseurType
            soinMotifArray = soinMotifVB.getSoinMotifArray();

            try {

                RFSoinMotif coupleSM = new RFSoinMotif(new Integer(0), soinMotifVB.getIdMotifRefus(),
                        soinMotifVB.getDescriptionFR(), soinMotifVB.getDescriptionIT(), soinMotifVB.getDescriptionDE(),
                        soinMotifVB.getDescriptionLongueFR(), soinMotifVB.getDescriptionLongueIT(),
                        soinMotifVB.getDescriptionLongueDE(), soinMotifVB.getCodeTypeDeSoinList(),
                        "".equals(soinMotifVB.getCodeSousTypeDeSoinList()) ? "*" : soinMotifVB
                                .getCodeSousTypeDeSoinList(), "idSts", Boolean.FALSE, Boolean.FALSE);

                if (validationMotifSoin(soinMotifArray, coupleSM, soinMotifVB)) {
                    Integer idArray;
                    RFSoinMotif soinMotif = null;
                    // création du prochain id
                    if (soinMotifArray.size() > 0) {
                        for (Iterator<RFSoinMotif> it = soinMotifArray.iterator(); it.hasNext();) {
                            soinMotif = it.next();
                        }
                        idArray = new Integer(soinMotif.getIdSoinMotif().intValue() + 1);
                    } else {
                        idArray = new Integer(soinMotifArray.size() + 1);
                    }
                    // ajout d'une ligne au tableau
                    // attention si ajout d'un TS=X,STS=* alors on enlève toutes
                    // les lignes tq TS=X
                    if ("".equals(soinMotifVB.getCodeSousTypeDeSoinList())) {
                        supprimerSoinsSpecifiques(soinMotifVB, session);
                    } else {
                        supprimerSousTypes(soinMotifVB, session);
                    }
                    soinMotifVB.getSoinMotifArray().add(
                            new RFSoinMotif(idArray, soinMotifVB.getIdMotifRefus(), soinMotifVB.getDescriptionFR(),
                                    soinMotifVB.getDescriptionIT(), soinMotifVB.getDescriptionDE(), soinMotifVB
                                            .getDescriptionLongueFR(), soinMotifVB.getDescriptionLongueIT(),
                                    soinMotifVB.getDescriptionLongueDE(), soinMotifVB.getCodeTypeDeSoinList(), ""
                                            .equals(soinMotifVB.getCodeSousTypeDeSoinList()) ? "*" : soinMotifVB
                                            .getCodeSousTypeDeSoinList(), "idSts", Boolean.FALSE, Boolean.FALSE));
                } else {
                    RFUtils.setMsgErreurViewBean(soinMotifVB, "ERREUR_AJOUT_ID_SOUS_TYPE_SOIN_EXISTANT");
                }
            } catch (Exception e) {
                // afficheErreurCoupleIncomplet(soinMotifVB);
            }
        }
    }

    /**
     * ajout d'un couple motif de refus/soin à partir du manager
     */
    public void ajouterSoinMotifLieDemande(RFRechercheMotifsDeRefusViewBean viewBean,
            RFDemandeJointSousTypeDeSoinJointTypeDeSoinJointMotifRefus liste, BSession session) throws Exception {

        if (!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
            RFRechercheMotifsDeRefusViewBean soinMotifVB = viewBean;
            // récupération du ArrayList
            List<RFSoinMotif> soinMotifArray = new ArrayList<RFSoinMotif>();
            // création d'un objet RFFournisseurType
            soinMotifArray = soinMotifVB.getSoinMotifArray();

            try {

                RFSoinMotif coupleSM = new RFSoinMotif(new Integer(0), soinMotifVB.getIdMotifRefus(),
                        soinMotifVB.getDescriptionFR(), soinMotifVB.getDescriptionIT(), soinMotifVB.getDescriptionDE(),
                        soinMotifVB.getDescriptionLongueFR(), soinMotifVB.getDescriptionLongueIT(),
                        soinMotifVB.getDescriptionLongueDE(), liste.getCodeTypeDeSoin(), liste.getCodeSousTypeDeSoin(),
                        "idSts", Boolean.FALSE, Boolean.FALSE);
                if (validationMotifSoin(soinMotifArray, coupleSM, soinMotifVB)) {
                    Integer idArray;
                    RFSoinMotif soinMotif = null;
                    // création du prochain id
                    if (soinMotifArray.size() > 0) {
                        for (Iterator<RFSoinMotif> it = soinMotifArray.iterator(); it.hasNext();) {
                            soinMotif = it.next();
                        }
                        idArray = new Integer(soinMotif.getIdSoinMotif().intValue() + 1);
                    } else {
                        idArray = new Integer(soinMotifArray.size() + 1);
                    }
                    // ajout d'une ligne au tableau
                    // attention si ajout d'un TS=X,STS=* alors on enlève toutes
                    // les lignes tq TS=X
                    if ("".equals(soinMotifVB.getCodeSousTypeDeSoinList())) {
                        supprimerSoinsSpecifiques(soinMotifVB, session);
                    } else {
                        supprimerSousTypes(soinMotifVB, session);
                    }
                    soinMotifVB.getSoinMotifArray().add(
                            new RFSoinMotif(idArray, soinMotifVB.getIdMotifRefus(), soinMotifVB.getDescriptionFR(),
                                    soinMotifVB.getDescriptionIT(), soinMotifVB.getDescriptionDE(), soinMotifVB
                                            .getDescriptionLongueFR(), soinMotifVB.getDescriptionLongueIT(),
                                    soinMotifVB.getDescriptionLongueDE(), liste.getCodeTypeDeSoin(), liste
                                            .getCodeSousTypeDeSoin(), "idSts", Boolean.FALSE, Boolean.FALSE));
                }
            } catch (Exception e) {
                // afficheErreurCoupleIncomplet(soinMotifVB);
            }
        }
    }

    protected String buildIdsSoins(List<RFSoinMotif> tabSoin) {
        String tmpIdsSoins = "";

        for (Iterator<RFSoinMotif> it = tabSoin.iterator(); it.hasNext();) {
            RFSoinMotif next = it.next();
            if (!"".equals(next.getTypeDeSoin()) && !"0".equals(next.getTypeDeSoin())) {
                tmpIdsSoins += (next.getTypeDeSoin() + "-" + next.getSousTypeDeSoin()).replace("*", "0");
                tmpIdsSoins += ";";
            }
        }
        // on enlève le dernier point virgule si non vide
        if (!JadeStringUtil.isEmpty(tmpIdsSoins)) {
            tmpIdsSoins = tmpIdsSoins.substring(0, tmpIdsSoins.lastIndexOf(";"));
        }

        return tmpIdsSoins;
    }

    /**
     * supprime les enregistrements des couples motifSoin de soin déjà en BDD que l'utilisateur a souhaité supprimer
     * 
     * @param SFViewBean
     * @param session
     * @throws Exception
     */
    protected void clearIdSuppressionMotifArray(RFRechercheMotifsDeRefusViewBean viewBean, BSession session)
            throws Exception {
        BITransaction transaction = null;
        try {
            transaction = (session).newTransaction();
            transaction.openTransaction();
            for (Iterator<String> it = viewBean.getIdSuppressionMotifArray().iterator(); it.hasNext();) {
                // suppression du type, sous type de soin
                // on enlève le couple de idsSoins

                // récupération de la sous-chaine type-sous type de soin
                RFMotifsDeRefus motre = new RFMotifsDeRefus();
                motre.setSession(session);
                motre.setIdMotifRefus(viewBean.getIdMotifRefus());
                motre.retrieve();
                if (!motre.isNew()) {
                    String toReplace = (it.next()).replace("*", "0");

                    // on récupére le dernier soin (dernier point virgule à fin)
                    String lastSoin = "";
                    if (!JadeStringUtil.isEmpty(motre.getIdsSoin())) {
                        // si il y a un point virgule
                        if (motre.getIdsSoin().contains(";")) {
                            lastSoin = motre.getIdsSoin().substring(motre.getIdsSoin().lastIndexOf(";") + 1);
                        } else {
                            // un seul element
                            lastSoin = motre.getIdsSoin();
                        }
                    }

                    if (lastSoin.equals(toReplace)) {
                        motre.setIdsSoin(motre.getIdsSoin().replace(toReplace, ""));
                    } else {
                        motre.setIdsSoin(motre.getIdsSoin().replace(toReplace + ";", ""));
                    }

                    motre.update(transaction);
                } else {
                    RFUtils.setMsgErreurInattendueViewBean(motre, "_update()", "RFRechercheMotifsDeRefusHelper");
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * 
     * @param motifSoinArray
     * @param soinMotif
     * @return vrai si la liste de RFSoinMotif contient soinMotif c'est à dire si il existe un i tel que
     *         motifSoinArray.get(i).getCodeSousTypeSoin = soinMotif.getCodeSousTypeSoin et
     *         motifSoinArray.get(i).getCodeTypeSoin = soinMotif.getCodeTypeSoin
     */
    private boolean containsSousTypeSoin(List<RFSoinMotif> motifSoinArray, RFSoinMotif soinMotif) {
        for (RFSoinMotif tempSoinMotif : motifSoinArray) {
            if (tempSoinMotif.getSousTypeDeSoin().equals(soinMotif.getSousTypeDeSoin())
                    && tempSoinMotif.getTypeDeSoin().equals(soinMotif.getTypeDeSoin())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    /**
     * Méthode qui renvoi le nombre de sous type de soin pour un type de soin donné
     * 
     * @throws Exception
     */
    private int getNbSousType(String codeTypeDeSoin, BSession session) throws Exception {
        RFSousTypeDeSoinManager sousTypeSoin = new RFSousTypeDeSoinManager();
        sousTypeSoin.setSession(session);
        sousTypeSoin.setForIdTypeSoin(RFUtils.getIdTypeDeSoin(codeTypeDeSoin, session));
        sousTypeSoin.changeManagerSize(0);
        sousTypeSoin.find();
        return sousTypeSoin.getSize();
    }

    /**
     * pour le type de soin et le motif de refus donné on teste quels sts peuvent être supprimés
     * 
     * @param viewBean
     * @return
     * @throws Exception
     */
    protected RFDemandeJointSousTypeDeSoinJointTypeDeSoinJointMotifRefusManager haveDemande(
            RFRechercheMotifsDeRefusViewBean viewBean, BSession session) throws Exception {
        // on test que le soin que l'on veut supprimer n'est pas lié à une
        // demande
        RFDemandeJointSousTypeDeSoinJointTypeDeSoinJointMotifRefusManager demandeRefusSoinManager = new RFDemandeJointSousTypeDeSoinJointTypeDeSoinJointMotifRefusManager();
        demandeRefusSoinManager.setSession(session);
        demandeRefusSoinManager.setForCodeTypeDeSoin(viewBean.getCodeTypeDeSoinList());
        demandeRefusSoinManager.setForIdMotifRefus(viewBean.getIdMotifRefus());
        // cas le sous type de soin n'est pas *
        if (!"*".equals(viewBean.getCodeSousTypeDeSoinList())) {
            demandeRefusSoinManager.setForCodeSousTypeDeSoin(viewBean.getCodeSousTypeDeSoinList());
        }
        // renvoi tous les sous types de soin associé à une demande pour ce
        // motif de refus (= à ne pas supprimer)
        demandeRefusSoinManager.changeManagerSize(0);
        demandeRefusSoinManager.find();
        // on renvoi un tableau de String contenant les sous types de soin à
        // supprimer
        return demandeRefusSoinManager;
    }

    // affichage d'un message d'erreur si un des descriptifs du motif de refus
    // est vide
    private boolean isDescriptifIncomplet(RFRechercheMotifsDeRefusViewBean SMViewBean) {
        if (JadeStringUtil.isEmpty(SMViewBean.getDescriptionFR())
                || JadeStringUtil.isEmpty(SMViewBean.getDescriptionDE())
                || JadeStringUtil.isEmpty(SMViewBean.getDescriptionIT())) {
            RFUtils.setMsgErreurViewBean(SMViewBean, "ERREUR_RF_RECHERCHE_MOTIF_REFUS_DESCRIPTIFS_INCOMPLETS");
            return true;
        }
        return false;

    }

    /**
     * Suppression d'un couple motif de refus/soin Si il est déjà en BDD on le supprime de la DB et du tableau Sinon on
     * le supprime que du tableau
     */
    public void supprimerCoupleMotifRefusSTS(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        // on récupére du viewBean l'idFournisseur et l'idSousTypeSoin de la
        // ligne que l'on veut supprimer
        RFRechercheMotifsDeRefusViewBean outputViewBean = (RFRechercheMotifsDeRefusViewBean) viewBean;

        // pour le type de soin et le motif de refus donné on teste quels sts
        // peuvent être supprimés (ou conserver?) -- mettre algo a plat
        RFDemandeJointSousTypeDeSoinJointTypeDeSoinJointMotifRefusManager listeSoinConserver = haveDemande(
                outputViewBean, session);
        // il y en a au moins un a ne pas supprimer
        if (listeSoinConserver.getSize() > 0) {
            // au moins un a supprimer - si ce n'est pas un étoile on ne fait
            // rien
            if ("*".equals(outputViewBean.getCodeSousTypeDeSoinList())
                    && (listeSoinConserver.getSize() != getNbSousType(outputViewBean.getCodeTypeDeSoin(), session))) {
                // on rajoute les soins lié à une demande
                for (int i = 0; i < listeSoinConserver.getSize(); i++) {
                    ajouterSoinMotifLieDemande(outputViewBean,
                            (RFDemandeJointSousTypeDeSoinJointTypeDeSoinJointMotifRefus) listeSoinConserver.get(i),
                            session);
                }
                RFUtils.setMsgErreurViewBean(outputViewBean, "ERREUR_RF_MOTIF_REFUS_SUPPRESSION_IMPOSSIBLE");
            }
            // tous doivent être conservés : affichage d'une erreur
            else {
                RFUtils.setMsgErreurViewBean(outputViewBean, "ERREUR_RF_MOTIF_REFUS_SUPPRESSION_IMPOSSIBLE");
            }
        }
        // tous sont à supprimer
        else {
            // on cherche ceux qui peuvent être supprimés
            for (int i = 0; i < outputViewBean.getSoinMotifArray().size(); i++) {
                RFSoinMotif soinMotif = outputViewBean.getSoinMotifArray().get(i);

                if (soinMotif.getIdMotifRefus().equals(outputViewBean.getIdMotifRefus())
                        && soinMotif.getTypeDeSoin().equals(outputViewBean.getCodeTypeDeSoinList())
                        && soinMotif.getSousTypeDeSoin().equals(outputViewBean.getCodeSousTypeDeSoinList())) {
                    // est-elle en DB?
                    if (soinMotif.getIsChargeDepuisDB()) {// on met la ppté
                        // supprimer = true
                        supprimerMotifSoinEnDB(session, soinMotif, outputViewBean);
                    }
                    outputViewBean.getSoinMotifArray().remove(i);
                    i--;
                }
            }
        }

        // on désinitialise les listes de soin pour un affichage vierge
        outputViewBean.setCodeSousTypeDeSoinList("");
        outputViewBean.setCodeTypeDeSoinList("");
    }

    /*
     * On supprime la ligne passé en paramètre (fournisseurType) de la DB
     */
    protected void supprimerMotifSoinEnDB(BSession session, RFSoinMotif soinMotif,
            RFRechercheMotifsDeRefusViewBean viewBean) throws Exception {
        RFMotifsDeRefusManager motifManager = new RFMotifsDeRefusManager();
        motifManager.setSession(session);
        motifManager.changeManagerSize(0);
        motifManager.find();

        for (Iterator<RFMotifsDeRefus> it = motifManager.iterator(); it.hasNext();) {
            RFMotifsDeRefus motifRefus = it.next();
            if (motifRefus.getIdMotifRefus().equals(soinMotif.getIdMotifRefus())
                    && viewBean.getCodeSousTypeDeSoin().equals(soinMotif.getSousTypeDeSoin())
                    && viewBean.getCodeTypeDeSoinList().equals(soinMotif.getTypeDeSoin())) {

                // suppression de ???
                viewBean.getIdSuppressionMotifArray().add(
                        (soinMotif.getTypeDeSoin() + "-" + soinMotif.getSousTypeDeSoin()));
                break;
            }
        }
    }

    /**
     * Suppression de tous les couple motif de refus/soin valant TS pour leur type de soin Si déjà en BDD suppression de
     * la DB et du tableau Sinon suppression seulement du tableau
     */
    public void supprimerSoinsSpecifiques(RFRechercheMotifsDeRefusViewBean viewBean, BSession session) throws Exception {
        for (int i = 0; i < viewBean.getSoinMotifArray().size(); i++) {
            RFSoinMotif soinMotif = viewBean.getSoinMotifArray().get(i);

            if (soinMotif.getIdMotifRefus().equals(viewBean.getIdMotifRefus())
                    && soinMotif.getTypeDeSoin().equals(viewBean.getCodeTypeDeSoinList())) {
                // est-elle en DB?
                if (soinMotif.getIsChargeDepuisDB()) {// on met la ppté
                    // supprimer = true
                    supprimerMotifSoinEnDB(session, soinMotif, viewBean);
                }
                viewBean.getSoinMotifArray().remove(i);
                i--;
            }
        }
    }

    /**
     * Suppression du STS=* de TS en param
     */
    public void supprimerSousTypes(RFRechercheMotifsDeRefusViewBean viewBean, BSession session) throws Exception {
        for (int i = 0; i < viewBean.getSoinMotifArray().size(); i++) {
            RFSoinMotif soinMotif = viewBean.getSoinMotifArray().get(i);

            if (soinMotif.getIdMotifRefus().equals(viewBean.getIdMotifRefus())
                    && soinMotif.getTypeDeSoin().equals(viewBean.getCodeTypeDeSoinList())
                    && "*".equals(soinMotif.getSousTypeDeSoin())) {
                // est-elle en DB?
                if (soinMotif.getIsChargeDepuisDB()) {// on met la ppté
                    // supprimer = true
                    supprimerMotifSoinEnDB(session, soinMotif, viewBean);
                }
                viewBean.getSoinMotifArray().remove(i);
                i--;
            }
        }
    }

    // vérification des données du tableau Type de soin/Fournisseurs
    private boolean validationMotifSoin(List<RFSoinMotif> motifSoinArray, RFSoinMotif soinMotif,
            RFRechercheMotifsDeRefusViewBean motifSoinVB) {
        if (soinMotif.estValide()) {
            // cette ligne ne marche pas
            if (!containsSousTypeSoin(motifSoinArray, soinMotif)) {
                return true;
            }
        }
        return false;

    }

    // vérifiation des données à entrer en BDD
    private boolean validationPourDB(BSession session, RFRechercheMotifsDeRefusViewBean SMViewBean) throws Exception {
        return !isDescriptifIncomplet(SMViewBean);
    }
}
