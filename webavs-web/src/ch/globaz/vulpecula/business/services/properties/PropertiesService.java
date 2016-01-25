package ch.globaz.vulpecula.business.services.properties;

import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;

public interface PropertiesService extends JadeApplicationService {
    final String DIFFERENCE_AUTORISEE_CONTROLE_DECOMPTE = "differenceAutoriseeControleDecompte";
    final String RAPPEL_NOMBRE_JOURS = "rappelNombreJours";
    final String TAXATION_NOMBRE_JOURS_INFERIEURE_ETABLISSEMENT = "taxationNombreJoursInferieureEtablissement";
    final String TAXATION_NOMBRE_JOURS_SUPERIEURE_ETABLISSEMENT = "taxationNombreJoursSuperieureEtablissement";
    final String MONTANT_MENSUEL_FRANCHISE_RENTIER = "franchiseMensuelleRentier";
    final String MONTANT_BASE_TO_SANS_DECOMPTE = "montantBaseToSansDecompte";
    final String ANNEE_PRODUCTION = "anneeProduction";
    final String CAISSES_METIERS = "caissesMetiers";
    final String CAISSES_AF = "caissesAF";
    final String ID_CAISSE_POUR_ANNONCE_SALAIRES = "idCaissePourAnnonceSalaires";
    final String TEXTE_RECTIFICATIF_ALLEMAND = "textRectificatifAllemand";

    /**
     * Permet de récupérer une propriété en DB.
     */
    String findProperties(String propertiesName);

    Montant getDifferenceAutoriseeControleDecompte();

    int getRappelNombreJours();

    int getTaxationNombreJoursInferieureEtablissement();

    int getTaxationNombreJoursSuperieureEtablissement();

    int getMontantBaseTOSansDecompte();

    Annee getAnneeProduction();

    List<String> getCaissesMetiers();

    List<String> getCaissesAF();

    String getTexteRectificatifAllemand();
}
