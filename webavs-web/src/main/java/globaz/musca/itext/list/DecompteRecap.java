package globaz.musca.itext.list;

import ch.globaz.common.domaine.Montant;

 class DecompteRecap {
    private Montant montantTotal = Montant.ZERO;
    private Montant montantPositif = Montant.ZERO;
    private Montant montantNegatif = Montant.ZERO;
    private Montant compteur = Montant.ZERO;

    public void addMontant(Montant montant) {
        compteur = compteur.add(1);
        montantTotal = montantTotal.add(montant);

        if (montant.isPositive()) {
            montantPositif = montantPositif.add(montant);
        } else {
            montantPositif = montantNegatif.add(montant);
        }
    }

    public Montant getCompteur() {
        return compteur;
    }

    public Montant getMontantTotal() {
        return montantTotal;
    }

    public Montant getMontantPositif() {
        return montantPositif;
    }

    public Montant getMontantNegatif() {
        return montantNegatif;
    }

}
