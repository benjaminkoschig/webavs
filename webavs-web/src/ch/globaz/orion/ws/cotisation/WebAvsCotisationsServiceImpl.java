package ch.globaz.orion.ws.cotisation;

import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import java.math.BigDecimal;
import java.util.List;
import javax.jws.WebService;
import ch.globaz.orion.ws.service.AFMassesForAffilie;
import ch.globaz.orion.ws.service.AppAffiliationService;
import ch.globaz.orion.ws.service.UtilsService;

@WebService(endpointInterface = "ch.globaz.orion.ws.cotisation.WebAvsCotisationsService")
public class WebAvsCotisationsServiceImpl implements WebAvsCotisationsService {

    @Override
    public MassesForAffilie listerMassesActuelles(String numeroAffilie) {

        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("Unabled to list Masses, numeroAffilie is empty or null");
        }

        // Récupération d'une session
        BSession session = UtilsService.initSession();

        // Récupération de la liste des cotisations
        List<AFMassesForAffilie> listeMasseForAffilie = AppAffiliationService.retrieveListCotisationActiveForNumAffilie(
                session, numeroAffilie);

        // Création des données de retour
        MassesForAffilie massesForAffilie = fillMassesForAffilie(listeMasseForAffilie);

        return massesForAffilie;
    }

    /**
     * Permet de remplir la classe retournée pour le webService
     * 
     * @param listeMasseForAffilie
     * @return
     */
    private MassesForAffilie fillMassesForAffilie(List<AFMassesForAffilie> listeMasseForAffilie) {

        MassesForAffilie massesForAffilie = new MassesForAffilie();

        for (AFMassesForAffilie masseAff : listeMasseForAffilie) {

            massesForAffilie.idAffiliation = Integer.parseInt(masseAff.getIdAffilie());
            massesForAffilie.noAffilieFormatte = masseAff.getNumAffilie();
            massesForAffilie.raisonSociale = masseAff.getRaisonSociale();

            Masse masse = new Masse();
            masse.idCotisation = Integer.parseInt(masseAff.getIdCotisation());
            masse.libelle_fr = masseAff.getLibelleFr();
            masse.libelle_de = masseAff.getLibelleDe();
            masse.libelle_it = masseAff.getLibelleIt();
            masse.valeur = convertirAnnuelToMensuelA5Centimes(masseAff.getMasseCotisation());
            masse.typeCotisation = Integer.parseInt(masseAff.getTypeAssurance());
            masse.codeCanton = Integer.parseInt(masseAff.getCodeCanton());

            massesForAffilie.masses.add(masse);
        }

        return massesForAffilie;
    }

    /**
     * Permet de convertir une masse annuelle en masse mensuelle en arrondissant à 5 centimes pres.
     * 
     * @param masseAnnuelle
     * @return
     */
    public static BigDecimal convertirAnnuelToMensuelA5Centimes(BigDecimal masseAnnuelle) {
        if (masseAnnuelle == null) {
            throw new IllegalStateException("La masse à convertir ne peut être null");
        }

        masseAnnuelle = masseAnnuelle.divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_EVEN);

        return JANumberFormatter.round(masseAnnuelle, 0.05, 2, JANumberFormatter.NEAR);

    }
}
