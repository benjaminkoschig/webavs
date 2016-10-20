package globaz.orion.mappingXmlml;

import globaz.framework.util.FWMessage;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.orion.process.EBImprimerPrevisionAcompte;
import globaz.webavs.common.CommonExcelmlContainer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import ch.globaz.xmlns.eb.pac.StatusSaisiePAC;

/**
 * @revision SCO 15 déc. 2010
 */
public final class EBXmlmlRecapPrevisionAcompte {

    private EBXmlmlRecapPrevisionAcompte() {
        super();
    }

    private static void loadDetail(CommonExcelmlContainer container, Map<String, String> m,
            EBImprimerPrevisionAcompte process) throws Exception {
        process.incProgressCounter();
        container.put(IEBListeAcl.NUMERO_AFFILIE, m.get(EBImprimerPrevisionAcompte.NUM_AFFILIE));
        container.put(IEBListeAcl.NOM_PRENOM, m.get(EBImprimerPrevisionAcompte.NOM));

        container.put(IEBListeAcl.ANCIENNE_MASSE,
                JANumberFormatter.fmt(m.get(EBImprimerPrevisionAcompte.ANCIENNE_MASSE), true, false, false, 0));
        container.put(IEBListeAcl.NOUVELLE_MASSE, JANumberFormatter.fmt(
                m.get(EBImprimerPrevisionAcompte.NOUVELLE_MASSE.toString()), true, false, false, 0));
        // indique si la nouvelle masse est > à 200'000 (valeur définie dans la properties
        // naos.prevision.masseAnnuelleMax
        container.put(IEBListeAcl.MASSE_MAX_AUTORISEE, m.get(EBImprimerPrevisionAcompte.SUP_MASSE_MAX_AUTORISEE));
        // indique si c'est un relevé à contribution?????
        container.put(IEBListeAcl.RELEVER_A_CONTROLER, m.get(EBImprimerPrevisionAcompte.RELEVE_A_CONTROLER));
        if (StatusSaisiePAC.PROBLEME.toString().equalsIgnoreCase(m.get(EBImprimerPrevisionAcompte.STATUT))) {
            container.put(IEBListeAcl.STATUT, process.getSession().getLabel("STATUT_ERREUR"));
        } else {
            container.put(IEBListeAcl.STATUT, process.getSession().getLabel("STATUT_TERMINEE"));
        }
    }

    private static void loadHeader(CommonExcelmlContainer container, EBImprimerPrevisionAcompte process,
            BigDecimal masseAnnuelleMaxPourPeriodiciteAnnuelle) throws Exception {
        container.put(IEBListeAcl.HEADER_NUM_INFOROM, EBImprimerPrevisionAcompte.NUMERO_INFOROM);
        container.put(IEBListeAcl.HEADER_DATE, JACalendar.todayJJsMMsAAAA());
        container
                .put(IEBListeAcl.LIBELLE_MASSE_MAX,
                        "> "
                                + JANumberFormatter.fmt(masseAnnuelleMaxPourPeriodiciteAnnuelle.toString(), true,
                                        false, false, 0));
        container.put(IEBListeAcl.HEADER_BLANK_1, "");
        container.put(IEBListeAcl.HEADER_BLANK_2, "");
    }

    public static CommonExcelmlContainer loadResults(List<Map<String, String>> manager,
            EBImprimerPrevisionAcompte process, BigDecimal masseAnnuelleMaxPourPeriodiciteAnnuelle) throws Exception,
            Exception {
        CommonExcelmlContainer container = new CommonExcelmlContainer();

        EBXmlmlRecapPrevisionAcompte.loadHeader(container, process, masseAnnuelleMaxPourPeriodiciteAnnuelle);

        if (manager.size() > 0) {
            for (Map<String, String> m : manager) {
                if (process.isAborted()) {
                    process.getMemoryLog()
                            .logMessage("Process aborded!", FWMessage.FATAL, process.getClass().getName());
                    return container;
                }
                EBXmlmlRecapPrevisionAcompte.loadDetail(container, m, process);
            }
        }
        return container;
    }
}
