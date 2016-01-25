/*
 * Créé le 27 juil. 07
 */
package globaz.corvus.vb.ordresversements;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.ordresversements.RECompensationInterDecisions;
import globaz.corvus.db.ordresversements.RECompensationInterDecisionsManager;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.ordresversements.RESoldePourRestitution;
import globaz.corvus.db.prestations.REPrestations;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;

/**
 * @author SCR
 * 
 */
public class RESoldePourRestitutionViewBean extends RESoldePourRestitution implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getBeneficiaire() {

        PRTiersWrapper tier;
        try {
            REPrestations prst = new REPrestations();
            prst.setSession(getSession());
            prst.setIdPrestation(getIdPrestation());
            prst.retrieve();
            REOrdresVersements ovs[] = prst.getOrdresVersement(null);

            String idTiers = "";
            for (int i = 0; i < ovs.length; i++) {
                REOrdresVersements ordresVersements = ovs[i];
                if (IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL.equals(ordresVersements.getCsType())) {
                    idTiers = ordresVersements.getIdTiers();
                }
            }
            prst.getOrdresVersement(null);
            tier = PRTiersHelper.getTiersParId(getSession(), idTiers);

            if (tier == null) {
                tier = PRTiersHelper.getAdministrationParId(getSession(), idTiers);
            }

            if (tier != null) {
                if (JadeStringUtil.isEmpty(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL))) {
                    return tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                } else {
                    return tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " / "
                            + tier.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                            + tier.getProperty(PRTiersWrapper.PROPERTY_PRENOM);
                }
            } else {
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 
     * @return true si l'OV peut etre modifie
     * 
     *         L'OV peut être modifié, si la décision auquel il est lié est en attente, ainsi que toutes les décisions
     *         liées via les compensations inter-décisions, le cas échéant.
     * 
     * 
     */
    public boolean isModifierPermis() {
        try {
            REPrestations p = new REPrestations();
            p.setSession(getSession());
            p.setIdPrestation(getIdPrestation());
            p.retrieve();
            PRAssert.notIsNew(p, null);

            REDecisionEntity d = new REDecisionEntity();
            d.setSession(getSession());
            d.setIdDecision(p.getIdDecision());
            d.retrieve();
            PRAssert.notIsNew(d, null);

            if (IREDecision.CS_ETAT_VALIDE.equals(d.getCsEtat())) {
                return false;
            } else {
                // contrôle qu'il n'y ait pas de compensation inter-décision
                // prévalidée ou validée.
                // On fait le check dans les 2 sens :
                // 1) Dettes -> Dettes de type CID
                // 2) Dettes de type CID -> Dettes

                REOrdresVersements[] ovs = p.getOrdresVersement(null);

                // Sens 1)
                for (int j = 0; j < ovs.length; j++) {
                    if (IREOrdresVersements.CS_TYPE_DETTE.equals(ovs[j].getCsType())) {
                        RECompensationInterDecisionsManager mgr = new RECompensationInterDecisionsManager();
                        mgr.setSession(getSession());
                        mgr.setForIdOV(ovs[j].getIdOrdreVersement());
                        mgr.find();

                        for (int i = 0; i < mgr.size(); i++) {
                            RECompensationInterDecisions cid = (RECompensationInterDecisions) mgr.getEntity(i);

                            REOrdresVersements ov = new REOrdresVersements();
                            ov.setSession(getSession());
                            ov.setIdOrdreVersement(cid.getIdOVCompensation());
                            ov.retrieve();
                            PRAssert.notIsNew(ov, null);

                            REPrestations prst = new REPrestations();
                            prst.setSession(getSession());
                            prst.setIdPrestation(ov.getIdPrestation());
                            prst.retrieve();

                            PRAssert.notIsNew(prst, null);

                            REDecisionEntity dec = new REDecisionEntity();
                            dec.setSession(getSession());
                            dec.setIdDecision(prst.getIdDecision());
                            dec.retrieve();
                            PRAssert.notIsNew(dec, null);

                            if (IREDecision.CS_ETAT_VALIDE.equals(dec.getCsEtat())) {
                                return false;
                            }
                        }
                    }
                }

                // Sens 2)
                for (int j = 0; j < ovs.length; j++) {
                    if (IREOrdresVersements.CS_TYPE_DETTE.equals(ovs[j].getCsType())) {
                        RECompensationInterDecisionsManager mgr = new RECompensationInterDecisionsManager();
                        mgr.setSession(getSession());
                        mgr.setForIdOVCompensation(ovs[j].getIdOrdreVersement());
                        mgr.find();

                        for (int i = 0; i < mgr.size(); i++) {
                            RECompensationInterDecisions cid = (RECompensationInterDecisions) mgr.getEntity(i);

                            REOrdresVersements ov = new REOrdresVersements();
                            ov.setSession(getSession());
                            ov.setIdOrdreVersement(cid.getIdOrdreVersement());
                            ov.retrieve();
                            PRAssert.notIsNew(ov, null);

                            REPrestations prst = new REPrestations();
                            prst.setSession(getSession());
                            prst.setIdPrestation(ov.getIdPrestation());
                            prst.retrieve();

                            PRAssert.notIsNew(prst, null);

                            REDecisionEntity dec = new REDecisionEntity();
                            dec.setSession(getSession());
                            dec.setIdDecision(prst.getIdDecision());
                            dec.retrieve();
                            PRAssert.notIsNew(dec, null);

                            if (IREDecision.CS_ETAT_VALIDE.equals(dec.getCsEtat())) {
                                return false;
                            }
                        }
                    }
                }

                return true;

            }

        } catch (Exception e) {
            return false;
        }
    }

}
