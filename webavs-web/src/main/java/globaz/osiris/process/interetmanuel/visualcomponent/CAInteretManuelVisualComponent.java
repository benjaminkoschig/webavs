package globaz.osiris.process.interetmanuel.visualcomponent;

import globaz.globall.db.BSession;
import globaz.osiris.db.interet.util.planparsection.CAPlanParSection;
import globaz.osiris.db.interets.CADetailInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CAPlanCalculInteret;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class utilitaire permettant l'affiche des informations de la cr�ation manuelle d'un int�r�t moratoire avec inserction
 * r�elle.<br/>
 * Utili� pour la cr�ation d'un Wizard.
 * 
 * @author DDA
 */
public class CAInteretManuelVisualComponent implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<CADetailInteretMoratoire> detailInteretMoratoire = new LinkedList<CADetailInteretMoratoire>();
    private CAInteretMoratoire interetMoratoire;
    private CAPlanParSection plan;

    public CAInteretManuelVisualComponent(CAInteretMoratoire interet) {
        interetMoratoire = interet;
    }

    public void addDetailInteretMoratoire(CADetailInteretMoratoire ligne) {
        detailInteretMoratoire.add(ligne);
    }

    public List<CADetailInteretMoratoire> getDetailInteretMoratoire() {
        return detailInteretMoratoire;
    }

    public CADetailInteretMoratoire getDetailInteretMoratoire(int i) {
        return detailInteretMoratoire.get(i);
    }

    public CAInteretMoratoire getInteretMoratoire() {
        return interetMoratoire;
    }

    public CAPlanParSection getPlan() {
        return plan;
    }

    public String getPlanLibelle(BSession session) {
        CAPlanCalculInteret planCalcul = new CAPlanCalculInteret();
        planCalcul.setSession(session);

        planCalcul.setIdPlanCalculInteret(plan.getIdPlan());

        try {
            planCalcul.retrieve();

            if (!planCalcul.hasErrors() && !planCalcul.isNew()) {
                return planCalcul.getLibelle();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * M�thode utilis�e pour les interets calcul�s � l'ex�cution d'une �tape du contentieux
     * 
     * @return le montant total des interets calcul�s
     */
    public String montantInteretTotalCalcule() {
        BigDecimal total = new BigDecimal("0");
        for (CADetailInteretMoratoire detail : detailInteretMoratoire) {
            try {
                total = total.add(new BigDecimal(detail.getMontantInteret()));
            } catch (Exception e) {
                return "";
            }
        }
        return total.toString();
    }

    public void setInteretMoratoire(CAInteretMoratoire interetMoratoire) {
        this.interetMoratoire = interetMoratoire;
    }

    public void setPlan(CAPlanParSection plan) {
        this.plan = plan;
    }
}
