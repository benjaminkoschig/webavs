package globaz.orion.mappingXmlml;

import globaz.globall.util.JACalendar;
import globaz.orion.process.EBImprimerListeDemandesTransmisesProcess;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.List;
import ch.globaz.orion.business.models.adi.RecapDemandesTransmises;

public final class EBXmlmlMappingListAdi {

    private EBXmlmlMappingListAdi() {
        super();
    }

    public static CommonExcelmlContainer loadResults(List<RecapDemandesTransmises> listeRecapDemandesTransmises,
            EBImprimerListeDemandesTransmisesProcess process, int nbCasTraites) {
        CommonExcelmlContainer container = new CommonExcelmlContainer();

        EBXmlmlMappingListAdi.loadHeader(container, process, nbCasTraites);

        for (RecapDemandesTransmises recapDemandesTransmises : listeRecapDemandesTransmises) {
            EBXmlmlMappingListAdi.loadDetail(container, recapDemandesTransmises);
            process.incProgressCounter();
        }

        return container;
    }

    private static void loadDetail(CommonExcelmlContainer container, RecapDemandesTransmises demandesTransmises) {

        container.put(IEBListeAdi.CELL_NUMERO_AFFILIE, demandesTransmises.getNoAffilie());
        container.put(IEBListeAdi.CELL_NOM, demandesTransmises.getNom());
        container.put(IEBListeAdi.CELL_PRENOM, demandesTransmises.getPrenom());
        container.put(IEBListeAdi.CELL_ANNEE, demandesTransmises.getAnnee());
        container.put(IEBListeAdi.CELL_BENEFICE_NET_NEW, demandesTransmises.getBeneficeNetNew());
        container.put(IEBListeAdi.CELL_CAPITAL_NEW, demandesTransmises.getCapitalNew());
        container.put(IEBListeAdi.CELL_BENEFICE_NET_CURRENT, demandesTransmises.getBeneficeNetCurrent());
        container.put(IEBListeAdi.CELL_CAPITAL_CURRENT, demandesTransmises.getCapitalCurrent());
        // container.put(IEBListeAdi.CELL_STATUT, demandesTransmises.getStatut());
        container.put(IEBListeAdi.CELL_TYPE, demandesTransmises.getType());
        container.put(IEBListeAdi.CELL_MESSAGE, demandesTransmises.getMessage());

    }

    private static void loadHeader(CommonExcelmlContainer container, EBImprimerListeDemandesTransmisesProcess process,
            int nbCasTraites) {
        container.put(IEBListeAdi.HEADER_TITRE,
                process.getSession().getLabel("HEADER_ADI_TITRE_LISTE_DEMANDES_TRANSMISES"));
        container.put(IEBListeAdi.HEADER_BLANK_1, "");
        container.put(IEBListeAdi.HEADER_LABEL_NOMBRE_CAS_TRAITE,
                process.getSession().getLabel("HEADER_ADI_NOMBRE_CAS_TRAITE"));
        container.put(IEBListeAdi.HEADER_NOMBRE_CAS_TRAITE, Integer.toString(nbCasTraites));
        container.put(IEBListeAdi.HEADER_LABEL_DATE, process.getSession().getLabel("HEADER_ADI_LABEL_DATE"));
        container.put(IEBListeAdi.HEADER_DATE, JACalendar.todayJJsMMsAAAA());

        container.put(IEBListeAdi.HEADER_COLONNE_NUMERO_AFFILIE,
                process.getSession().getLabel("HEADER_ADI_COLONNE_NUMERO_AFFILIE"));
        container.put(IEBListeAdi.HEADER_COLONNE_NOM, process.getSession().getLabel("HEADER_ADI_COLONNE_NOM"));
        container.put(IEBListeAdi.HEADER_COLONNE_PRENOM, process.getSession().getLabel("HEADER_ADI_COLONNE_PRENOM"));
        container.put(IEBListeAdi.HEADER_COLONNE_ANNEE, process.getSession().getLabel("HEADER_ADI_COLONNE_ANNEE"));
        container.put(IEBListeAdi.HEADER_COLONNE_NOUVELLE_DEMANDE,
                process.getSession().getLabel("HEADER_ADI_COLONNE_NOUVELLE_DEMANDE"));
        container.put(IEBListeAdi.HEADER_COLONNE_BENEFICE_NET_NEW,
                process.getSession().getLabel("HEADER_ADI_COLONNE_BENEFICE_NET"));
        container.put(IEBListeAdi.HEADER_COLONNE_CAPITAL_NEW,
                process.getSession().getLabel("HEADER_ADI_COLONNE_CAPITAL"));
        container.put(IEBListeAdi.HEADER_COLONNE_DECISION_ACTUELLE,
                process.getSession().getLabel("HEADER_ADI_COLONNE_DECISION_ACTUELLE"));
        container.put(IEBListeAdi.HEADER_COLONNE_BENEFICE_NET_CURRENT,
                process.getSession().getLabel("HEADER_ADI_COLONNE_BENEFICE_NET"));
        container.put(IEBListeAdi.HEADER_COLONNE_CAPITAL_CURRENT,
                process.getSession().getLabel("HEADER_ADI_COLONNE_CAPITAL"));
        // container.put(IEBListeAdi.HEADER_COLONNE_STATUT, process.getSession().getLabel("HEADER_ADI_COLONNE_STATUT"));
        container.put(IEBListeAdi.HEADER_COLONNE_MESSAGE, process.getSession().getLabel("HEADER_ADI_COLONNE_MESSAGE"));
    }
}
