package globaz.corvus.acor.parser.xml.rev10.fcalcul;

/**
 * 
 * @author SCR
 * 
 */
public class REXmlDataContainer {

    REAssureXmlDataStructure[] assures = null;
    REBaseCalculXmlDataStructure[] basesCalcul = null;

    RECarriereAssuranceXmlDataStructure[] carrieresAssurance = null;
    REComparaisonXmlDataStructure comparaison = null;
    REDecisionXmlDataStructure[] decisions = null;
    /* not used data */
    REDemandeXmlDataStructure demande = null;
    RESplittingXmlDataStructure splitting = null;

    public REAssureXmlDataStructure[] getAssures() {
        return assures;
    }

    public REBaseCalculXmlDataStructure[] getBasesCalcul() {
        return basesCalcul;
    }

    public RECarriereAssuranceXmlDataStructure[] getCarrieresAssurance() {
        return carrieresAssurance;
    }

    public REComparaisonXmlDataStructure getComparaison() {
        return comparaison;
    }

    public REDecisionXmlDataStructure[] getDecisions() {
        return decisions;
    }

    public REDemandeXmlDataStructure getDemande() {
        return demande;
    }

    public RESplittingXmlDataStructure getSplitting() {
        return splitting;
    }

    public void setAssures(REAssureXmlDataStructure[] ass) {
        assures = ass;
    }

    public void setBasesCalcul(REBaseCalculXmlDataStructure[] basesCalcul) {
        this.basesCalcul = basesCalcul;
    }

    public void setCarrieresAssurance(RECarriereAssuranceXmlDataStructure[] carrieresAssurance) {
        this.carrieresAssurance = carrieresAssurance;
    }

    public void setComparaison(REComparaisonXmlDataStructure comparaison) {
        this.comparaison = comparaison;
    }

    public void setDecisions(REDecisionXmlDataStructure[] decisions) {
        this.decisions = decisions;
    }

    public void setDemande(REDemandeXmlDataStructure demande) {
        this.demande = demande;
    }

    public void setSplitting(RESplittingXmlDataStructure splitting) {
        this.splitting = splitting;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tREXmlDataContainer").append("\n");
        sb.append("==================================================>>>\n");

        if (demande != null) {
            sb.append("demande = " + demande.toStringgg()).append("\n");
        }

        if (assures != null) {
            sb.append("assures = \n\n\n");
            for (int i = 0; i < assures.length; i++) {
                sb.append(assures[i].toStringgg());
            }
        }

        if (comparaison != null) {
            sb.append("comparaison = \n\n" + comparaison.toStringgg()).append("\n");
        }

        if (splitting != null) {
            sb.append("splitting = \n\n" + splitting.toStringgg()).append("\n");
        }

        if (carrieresAssurance != null) {
            sb.append("carrieresAssurance = \n\n");
            for (int i = 0; i < carrieresAssurance.length; i++) {
                sb.append(carrieresAssurance[i].toStringgg());
            }
        }

        if (decisions != null) {
            sb.append("decisions = \n\n");
            for (int i = 0; i < decisions.length; i++) {
                sb.append(decisions[i].toStringgg());
            }
        }

        if (basesCalcul != null) {
            sb.append("basesCalcul = \n\n");
            for (int i = 0; i < basesCalcul.length; i++) {
                sb.append(basesCalcul[i].toStringgg());
            }
        }

        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }

}
