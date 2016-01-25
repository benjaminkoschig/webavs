package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;

public class DonneesFinancieresBase {
    public static final ProprieteType PROPRIETAIRE = ProprieteType.PROPRIETAIRE;
    public static final Part PART_1_2 = new Part(1, 2);
    public static final Montant M_500 = new Montant(500);
    public static final Montant M_1000 = new Montant(1000);
    public static final Montant M_6000 = new Montant(6000);
    public static final Montant M_55 = new Montant(55);
    public static final Montant M_50 = new Montant(50);
    public static final Montant M_10 = new Montant(10);
    public static final Montant M_5 = new Montant(5);
    public static final Montant M_8 = new Montant(8);

    public static DonneeFinanciere build() {
        return BuilderDf.createDF();
    }
}
