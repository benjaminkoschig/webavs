package ch.globaz.orion.ws.cotisation;

import globaz.globall.db.BManager;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.releve.AFApercuReleve;
import globaz.naos.db.releve.AFApercuReleveManager;
import globaz.orion.process.EBDanPreRemplissage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import org.apache.commons.lang.StringUtils;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.business.services.OrionServiceLocator;
import ch.globaz.orion.ws.exceptions.WebAvsException;
import ch.globaz.orion.ws.service.AFMassesForAffilie;
import ch.globaz.orion.ws.service.AppAffiliationService;
import ch.globaz.orion.ws.service.UtilsService;
import ch.globaz.orion.ws.service.manager.AFReleveForSdd;
import ch.globaz.orion.ws.service.manager.AFReleveForSddManager;

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
        List<AFMassesForAffilie> listeMasseForAffilie = AppAffiliationService
                .retrieveListCotisationActiveForNumAffilie(session, numeroAffilie);

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

    @Override
    public boolean executerPreRemplissageDan(String noAffilie, Integer annee, String loginName, String userEmail) {
        // Récupération d'une session
        BSession session = UtilsService.initSession();

        // exécution du process de pré-remplissage
        EBDanPreRemplissage process = new EBDanPreRemplissage();
        process.setSession(session);
        process.setAnnee(annee.toString());
        process.setNumAffilie(noAffilie);
        process.setEmail(userEmail);
        process.setExecuteFromWebAvs(false);
        process.setLoginName(loginName);

        try {
            BProcessLauncher.start(process, false);
        } catch (Exception e) {
            JadeLogger.error("Process EBDanPreRemplissage failed", e);
            return false;
        }
        return true;
    }

    @Override
    public String genererDocumentPucsLisible(String idPucsEntry, String format, String loginName, String userEmail,
            String langue) {
        String filePath = null;

        try {
            filePath = OrionServiceLocator.getPucsService().pucsFileLisibleForEbusiness(idPucsEntry,
                    DeclarationSalaireProvenance.PUCS, format, loginName, userEmail, langue);
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error("Unable to generate the files PUCS ", e);
        }

        return filePath;
    }

    @Override
    public Double findTauxAssuranceForCotisation(Integer idCotisation, BigDecimal montant, String date)
            throws WebAvsException {
        // vérification des paramètres
        if (idCotisation == null) {
            throw new WebAvsException("idCotisation cannot be null ");
        }

        if (montant == null) {
            throw new WebAvsException("montant cannot be null ");
        }

        if (JadeStringUtil.isBlankOrZero(date)) {
            throw new WebAvsException("date cannot be null or empty");
        }

        // récupération de la cotisation en fonction de l'id
        BSession session = UtilsService.initSession();
        AFCotisation cotisation = new AFCotisation();
        cotisation.setSession(session);
        cotisation.setId(Integer.toString(idCotisation));
        try {
            cotisation.retrieve();

            // récupération du taux
            String taux = cotisation.getTaux(date, montant.toString());
            return new Double(taux);
        } catch (Exception e) {
            JadeLogger.error(this.getClass(),
                    "Unable to retrieve cotisation with id : " + idCotisation + " -> " + e.getMessage());
            throw new WebAvsException("Unable to retrieve cotisation with id : " + idCotisation);
        }
    }

    @Override
    public DecompteMensuel findDecompteMois(String numeroAffilie, String mois, String annee) {
        Checkers.checkNotNull(numeroAffilie, "numeroAffilie");
        Checkers.checkNotEmpty(numeroAffilie, "numeroAffilie");
        Checkers.checkNotNull(mois, "mois");
        Checkers.checkNotEmpty(mois, "mois");
        Checkers.checkIsInteger(mois, "mois");
        Checkers.checkNotNull(annee, "annee");
        Checkers.checkNotEmpty(annee, "annee");
        Checkers.checkIsInteger(annee, "annee");

        DecompteMensuel decompte = null;

        BSession session;

        try {

            session = UtilsService.initSession();

            // Récupération des informations des cotisations
            decompte = retrieveCotisationsInformations(numeroAffilie, mois, annee, session);

            // Récupération des relevés
            decompte = fillCotisationsInformationsWithReleve(numeroAffilie, decompte, session);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // TODO Return exception pour cas en dehors affiliation
        return decompte;
    }

    private static DecompteMensuel fillCotisationsInformationsWithReleve(String numeroAffilie,
            DecompteMensuel decompte, BSession session) throws Exception {
        AFApercuReleveManager manager = new AFApercuReleveManager();
        manager.setSession(session);
        manager.setForAffilieNumero(numeroAffilie);
        manager.setForDateDebut("01." + StringUtils.leftPad(decompte.getMoisDecompte(), 2, '0') + "."
                + decompte.getAnneeDecompte());
        manager.find(BManager.SIZE_USEDEFAULT);

        if (!manager.isEmpty()) {

            AFApercuReleve releve = (AFApercuReleve) manager.getFirstEntity();

            AFReleveForSddManager managerSdd = new AFReleveForSddManager();
            managerSdd.setSession(session);
            managerSdd.setForIdReleve(releve.getIdReleve());

            managerSdd.find(BManager.SIZE_USEDEFAULT);

            if (!managerSdd.isEmpty()) {

                decompte.setDejaEtabli(true);

                List<AFReleveForSdd> listReleve = managerSdd.toList();
                for (AFReleveForSdd releveSdd : listReleve) {
                    fillDecompteMensuelWithReleve(decompte, releveSdd);
                }
            }
        }

        return decompte;
    }

    private static DecompteMensuel retrieveCotisationsInformations(String numeroAffilie, String mois, String annee,
            BSession session) {

        // Récupération des cotisations
        List<AFMassesForAffilie> listeMasseForAffilie = AppAffiliationService
                .retrieveListCotisationForNumAffilieForMoisAnnee(session, numeroAffilie, mois, annee);

        DecompteMensuel decompte = null;

        if (!listeMasseForAffilie.isEmpty()) {

            AFMassesForAffilie masseAff = listeMasseForAffilie.get(0);

            decompte = new DecompteMensuelBuilder().withIdAffilie(masseAff.getIdAffilie()).withAnneeDecompte(annee)
                    .withMoisDecompte(mois).withNumeroAffilie(masseAff.getNumAffilie())
                    .withLinesDecompte(fillDecompteMensuelForAffilie(listeMasseForAffilie)).build();
        }

        return decompte;
    }

    public static DecompteMensuel fillDecompteMensuelWithReleve(DecompteMensuel decompte, AFReleveForSdd releveSdd) {

        for (DecompteMensuelLine line : decompte.getLinesDecompte()) {
            int idRubrique = Integer.parseInt(releveSdd.getIdRubrique());

            if (line.getIdRubrique() == idRubrique) {
                line.setMasse(releveSdd.getMasseFacture());
            }
        }

        return decompte;
    }

    private static List<DecompteMensuelLine> fillDecompteMensuelForAffilie(List<AFMassesForAffilie> masses) {

        List<DecompteMensuelLine> lines = new ArrayList<DecompteMensuelLine>();

        for (AFMassesForAffilie masseAff : masses) {

            lines.add(new DecompteMensuelLineBuilder().withIdCotisation(masseAff.getIdCotisation())
                    .withIdRubrique(Integer.parseInt(masseAff.getIdRubrique())).withLibelleDe(masseAff.getLibelleDe())
                    .withLibelleFr(masseAff.getLibelleFr()).withLibelleIt(masseAff.getLibelleIt())
                    .withMasse(BigDecimal.ZERO).withTypeCotisation(masseAff.getTypeAssurance()).build());
        }

        return lines;
    }
}
