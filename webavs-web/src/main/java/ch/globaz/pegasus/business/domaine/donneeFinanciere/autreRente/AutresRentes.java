package ch.globaz.pegasus.business.domaine.donneeFinanciere.autreRente;

import java.util.Arrays;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresList;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.Each;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaiesEtrangere;

public class AutresRentes extends DonneesFinancieresList<AutreRente, AutresRentes> {

    public AutresRentes() {
        super(AutresRentes.class);
    }

    public AutresRentes getAutresRentesByGenre(AutreRenteGenre genre) {
        AutresRentes autresRentesForGenre = new AutresRentes();
        for (AutreRente autreRente : getList()) {
            if (genre.equals(autreRente.getAutreRenteGenre())) {
                autresRentesForGenre.add(autreRente);
            }
        }
        return autresRentesForGenre;
    }

    public AutresRentes filtreByGenre(AutreRenteGenre... genres) {
        AutresRentes autresRentesForGenre = new AutresRentes();
        List<AutreRenteGenre> list = Arrays.asList(genres);
        for (AutreRente autreRente : getList()) {
            if (list.contains(autreRente.getAutreRenteGenre())) {
                autresRentesForGenre.add(autreRente);
            }
        }
        return autresRentesForGenre;
    }

    public Montant sumAndComputeDevise(MonnaiesEtrangere monnaiesEtrangere, final Date dateDebut) {
        final ConversionDevise conversionDevise = new ConversionDevise(monnaiesEtrangere);
        return this.sum(new Each<AutreRente>() {
            @Override
            public Montant getMontant(AutreRente autreRente) {
                return conversionDevise.compute(autreRente, dateDebut);
            }
        });
    }

}
