package globaz.orion.mappingXmlml;

import globaz.globall.util.JACalendar;
import globaz.orion.process.EBImprimerSaisieDecompteProcess;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.List;
import ch.globaz.orion.business.models.sdd.RecapSaisieDecompte;

public final class EBXmlmlRecapSaisieDecompte {

    private static final String CELL_NUMERO_AFFILIE = "numeroAffilie";
    private static final String CELL_NOM_PRENOM = "nomPrenom";
    private static final String CELL_LOCALITE = "localite";
    private static final String CELL_PERIODE = "periode";
    private static final String CELL_TYPE = "type";
    private static final String CELL_SAISIE_LE = "saisieLe";
    private static final String CELL_ERREUR = "erreur";

    private EBXmlmlRecapSaisieDecompte() {
        super();
    }

    public static CommonExcelmlContainer loadResults(List<RecapSaisieDecompte> listeRecapSaisieDecompte,
            EBImprimerSaisieDecompteProcess process, int nbCasTraites) {
        CommonExcelmlContainer container = new CommonExcelmlContainer();

        EBXmlmlRecapSaisieDecompte.loadHeader(container, process, nbCasTraites);

        for (RecapSaisieDecompte recapSaisieDecompte : listeRecapSaisieDecompte) {
            try {
                EBXmlmlRecapSaisieDecompte.loadDetail(container, recapSaisieDecompte, process);
            } catch (Exception e) {

            }
        }

        return container;
    }

    private static void loadDetail(CommonExcelmlContainer container, RecapSaisieDecompte saisie,
            EBImprimerSaisieDecompteProcess process) throws Exception {

        container.put(CELL_NUMERO_AFFILIE, saisie.getNumeroAffilie());
        container.put(CELL_NOM_PRENOM, saisie.getNomPrenom());

        container.put(CELL_LOCALITE, saisie.getLocalite());
        container.put(CELL_PERIODE, saisie.getPeriode());
        container.put(CELL_TYPE, saisie.getType());
        container.put(CELL_SAISIE_LE, saisie.getDateSaisie());
        container.put(CELL_ERREUR, saisie.getErreur());

    }

    private static void loadHeader(CommonExcelmlContainer container, EBImprimerSaisieDecompteProcess process,
            int nbCasTraites) {
        container.put(IEBListeAcl.HEADER_NUM_INFOROM, EBImprimerSaisieDecompteProcess.NUMERO_INFOROM);
        container.put(IEBListeAcl.HEADER_DATE, JACalendar.todayJJsMMsAAAA());
        container.put(IEBListeAcl.HEADER_TITRE, process.getSession().getLabel("HEADER_TITRE_LISTE_SAISIE_DECOMPTE"));
        container.put(IEBListeAcl.HEADER_BLANK_1, "");
        container.put(IEBListeAcl.HEADER_LABEL_NOMBRE_CAS_TRAITE,
                process.getSession().getLabel("HEADER_NOMBRE_CAS_TRAITE"));
        container.put(IEBListeAcl.HEADER_NOMBRE_CAS_TRAITE, Integer.toString(nbCasTraites));
        container.put(IEBListeAcl.HEADER_BLANK_2, "");
        container.put(IEBListeAcl.HEADER_BLANK_3, "");

        container.put(IEBListeAcl.HEADER_COLONNE_NUMERO_AFFILIE,
                process.getSession().getLabel("HEADER_COLONNE_NUMERO_AFFILIE"));
        container
                .put(IEBListeAcl.HEADER_COLONNE_NOM_PRENOM, process.getSession().getLabel("HEADER_COLONNE_NOM_PRENOM"));
        container.put(IEBListeAcl.HEADER_COLONNE_LOCALITE, process.getSession().getLabel("HEADER_COLONNE_LOCALITE"));
        container.put(IEBListeAcl.HEADER_COLONNE_PERIODE, process.getSession().getLabel("HEADER_COLONNE_PERIODE"));
        container.put(IEBListeAcl.HEADER_COLONNE_TYPE, process.getSession().getLabel("HEADER_COLONNE_TYPE"));
        container.put(IEBListeAcl.HEADER_COLONNE_SAISIE_LE, process.getSession().getLabel("HEADER_COLONNE_SAISIE_LE"));
        container.put(IEBListeAcl.HEADER_COLONNE_ERREUR, process.getSession().getLabel("HEADER_COLONNE_ERREUR"));
    }
}
