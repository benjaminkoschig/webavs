package ch.globaz.pegasus.business.domaine.pca;

/**
 * pour une période donnée
 * 
 * @author dma
 * 
 */
public class PcaRequerantConjoint {
    private Pca requerant = new Pca();
    private Pca conjoint = new Pca();

    public Pca getRequerant() {
        return requerant;
    }

    public void setRequerant(Pca requerant) {
        this.requerant = requerant;
    }

    public Pca getConjoint() {
        return conjoint;
    }

    public void setConjoint(Pca conjoint) {
        this.conjoint = conjoint;
    }

    public boolean hasConjoint() {
        return !isEmpty(conjoint);
    }

    public boolean hasRequerant() {
        return !isEmpty(requerant);
    }

    private boolean isEmpty(Pca pca) {
        if (pca == null) {
            return true;
        } else if (pca.getId() == null) {
            return true;
        } else if (pca.getId().trim().isEmpty()) {
            return true;
        } else if (pca.getId().trim().equals("0")) {
            return true;
        } else if (pca.getId().trim().equals("null")) {
            return true;
        } else {
            return false;
        }
    }

    public PcaSituation resolveCasPca() {
        PcaSituation pcaCas = PcaSituation.INDEFINIT;
        if (hasRequerant()) {
            if (hasConjoint()) {
                pcaCas = PcaSituation.resolve(requerant.getGenre(), conjoint.getGenre(), requerant
                        .getBeneficiaireConjointDom2R().estInitialisee());
            } else {
                pcaCas = PcaSituation.resolve(requerant.getGenre(), null, requerant.getBeneficiaireConjointDom2R()
                        .estInitialisee());
            }
        } 
        return pcaCas;
    }
}
