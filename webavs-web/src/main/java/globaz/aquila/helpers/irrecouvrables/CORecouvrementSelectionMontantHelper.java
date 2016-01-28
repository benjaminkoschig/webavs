package globaz.aquila.helpers.irrecouvrables;

import globaz.aquila.db.irrecouvrables.CORecouvrementSectionsListViewBean;
import globaz.aquila.db.irrecouvrables.CORecouvrementSectionsViewBean;
import globaz.aquila.db.irrecouvrables.CORecouvrementSelectionMontantViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.osiris.db.irrecouvrable.CARecouvrementBaseAmortissementContainer;
import globaz.osiris.db.irrecouvrable.CARecouvrementBaseAmortissementContainerLoader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CORecouvrementSelectionMontantHelper extends FWHelper {

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {

        String idCompteAnnexe = ((CORecouvrementSelectionMontantViewBean) viewBean).getIdCompteAnnexe();

        List<String> idSectionsString = ((CORecouvrementSelectionMontantViewBean) viewBean).getIdSectionsList();
        String[] idSectionsArray = idSectionsString.get(0).split(",");
        List<String> idSectionsList = new ArrayList<String>(Arrays.asList(idSectionsArray));
        ((CORecouvrementSelectionMontantViewBean) viewBean).setIdSectionsList(idSectionsList);

        try {
            CARecouvrementBaseAmortissementContainer basesContainer = CARecouvrementBaseAmortissementContainerLoader
                    .loadCompteurs(session, idCompteAnnexe);
            ((CORecouvrementSelectionMontantViewBean) viewBean).setBasesAmortissement(basesContainer);

            String montantDisponible = computeMontantDisponible(idSectionsList, session);
            ((CORecouvrementSelectionMontantViewBean) viewBean).setMontantDisponible(montantDisponible);
            ((CORecouvrementSelectionMontantViewBean) viewBean).setMontantARecouvrir(montantDisponible);

        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.toString());
        }

    }

    /**
     * Calcule le montant disponible pour le recouvrement. Le montant correspond au total des soldes des sections
     * passées en paramètre
     * 
     * @param idSections
     *            liste des sections sélectionnées
     * @param session
     * @return
     * @throws Exception
     */
    private String computeMontantDisponible(List<String> idSections, BISession session) throws Exception {

        BigDecimal montantDisponible = new BigDecimal("0.0");

        CORecouvrementSectionsListViewBean recouvrementSections = new CORecouvrementSectionsListViewBean();
        recouvrementSections.setSession((BSession) session);
        recouvrementSections.setForIdSectionIn(idSections);
        recouvrementSections.find();

        for (int i = 0; i < recouvrementSections.size(); i++) {
            CORecouvrementSectionsViewBean section = (CORecouvrementSectionsViewBean) recouvrementSections.get(i);
            montantDisponible = montantDisponible.add(new BigDecimal(section.getSolde()));
        }

        return montantDisponible.abs().toPlainString();
    }
}
