package globaz.corvus.vb.decisions;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.db.demandes.REValiderGroupeDecisionsManager;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.demandes.REDemandeRenteJointDemandeViewBean;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.globall.util.JAVector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author HPE
 */
public class REValiderGroupeDecisionsListViewBean extends REValiderGroupeDecisionsManager implements
        FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static HashMap<String, ArrayList<REDecisionJointDemandeRenteViewBean>> mapDecisionsParDemande = new HashMap<String, ArrayList<REDecisionJointDemandeRenteViewBean>>();

    public static HashMap<String, ArrayList<REDecisionJointDemandeRenteViewBean>> getMapDecisionsParDemande() {
        return REValiderGroupeDecisionsListViewBean.mapDecisionsParDemande;
    }

    public static HashMap<String, ArrayList<REDecisionJointDemandeRenteViewBean>> getMapDecisionsParDemandeOneTime(
            String listIdsDemandeRentes, BSession session, String likeNumeroAVS, String likeNumeroAVSNNSS,
            String likeNom, String likePrenom, String forDateNaissance, String forCsSexe, String preparePar)
            throws Exception {

        REValiderGroupeDecisionsListViewBean.mapDecisionsParDemande = new HashMap<String, ArrayList<REDecisionJointDemandeRenteViewBean>>();

        JADate datePaiement = new JADate(REPmtMensuel.getDateDernierPmt(session));

        REDecisionJointDemandeRenteListViewBean mgr = new REDecisionJointDemandeRenteListViewBean();
        mgr.setSession(session);
        mgr.setLikeNumeroAVS(likeNumeroAVS);
        mgr.setLikeNumeroAVSNNSS(likeNumeroAVSNNSS);
        mgr.setLikeNom(likeNom);
        mgr.setLikePrenom(likePrenom);
        mgr.setForDateNaissance(forDateNaissance);
        mgr.setForCsSexe(forCsSexe);
        mgr.setLikePreparePar(preparePar);
        mgr.setForNoDemandeRenteIn(listIdsDemandeRentes);
        mgr.setForCsEtatDecisions(IREDecision.CS_ETAT_PREVALIDE);
        mgr.changeManagerSize(SIZE_NOLIMIT);
        mgr.find();

        ArrayList<REDecisionJointDemandeRenteViewBean> listDecisionsParDemande = new ArrayList<REDecisionJointDemandeRenteViewBean>();

        for (Iterator<?> iterator = mgr.iterator(); iterator.hasNext();) {

            listDecisionsParDemande = new ArrayList<REDecisionJointDemandeRenteViewBean>();

            REDecisionJointDemandeRenteViewBean decision = (REDecisionJointDemandeRenteViewBean) iterator.next();

            JADate dateDecision = new JADate(decision.getDateDecision());
            int moisDecision = dateDecision.getMonth();
            int moisPaiement = datePaiement.getMonth();
            // BZ 5181 = Afficher la ligne seulement si la date de décision est égale au mois de paiement
            if (moisDecision == moisPaiement) {
                if (REValiderGroupeDecisionsListViewBean.mapDecisionsParDemande.containsKey(decision
                        .getIdDemandeRente())) {

                    listDecisionsParDemande = REValiderGroupeDecisionsListViewBean.mapDecisionsParDemande.get(decision
                            .getIdDemandeRente());
                    listDecisionsParDemande.add(decision);

                } else {

                    listDecisionsParDemande.add(decision);
                    REValiderGroupeDecisionsListViewBean.mapDecisionsParDemande.put(decision.getIdDemandeRente(),
                            listDecisionsParDemande);

                }
            }
        }

        return REValiderGroupeDecisionsListViewBean.mapDecisionsParDemande;
    }

    public static void setMapDecisionsParDemande(
            HashMap<String, ArrayList<REDecisionJointDemandeRenteViewBean>> mapDecisionsParDemande) {
        REValiderGroupeDecisionsListViewBean.mapDecisionsParDemande = mapDecisionsParDemande;
    }

    @Override
    protected REDemandeRenteJointDemandeViewBean _newEntity() throws Exception {
        return new REDemandeRenteJointDemandeViewBean();
    }

    public REDecisionJointDemandeRenteListViewBean getDecisionsForDemande(String idDemande) throws Exception {

        HashMap<String, ArrayList<REDecisionJointDemandeRenteViewBean>> mapDecisionsParDemande = REValiderGroupeDecisionsListViewBean
                .getMapDecisionsParDemande();

        ArrayList<REDecisionJointDemandeRenteViewBean> list = mapDecisionsParDemande.get(idDemande);

        JAVector vector = new JAVector();

        int nbDecisionInvalidable = 0;

        JADate datePaiement = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));

        if (list != null) {
            for (REDecisionJointDemandeRenteViewBean decision : list) {

                if (IREDecision.CS_TYPE_DECISION_STANDARD.equals(decision.getCsTypeDecision())) {

                    // BZ 5181 = Afficher la ligne seulement si la date de décision est égale au mois de paiement
                    JADate dateDecision = new JADate(decision.getDateDecision());

                    int moisDecision = dateDecision.getMonth();
                    int moisPaiement = datePaiement.getMonth();

                    if (moisDecision != moisPaiement) {
                        nbDecisionInvalidable++;
                    }
                }

                vector.add(decision);
            }
        }

        REDecisionJointDemandeRenteListViewBean listVb = new REDecisionJointDemandeRenteListViewBean();
        listVb.setSession(getSession());
        listVb.setContainer(vector);
        listVb.setNbDecisionInvalidables(nbDecisionInvalidable);

        return listVb;
    }
}