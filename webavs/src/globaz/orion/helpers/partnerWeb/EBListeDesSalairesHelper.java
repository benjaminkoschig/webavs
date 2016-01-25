package globaz.orion.helpers.partnerWeb;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.leo.constantes.ILEConstantes;
import globaz.leo.db.parametrage.LEFormuleListViewBean;
import globaz.leo.db.parametrage.LEFormuleViewBean;
import globaz.leo.db.parametrage.LERappelViewBean;
import globaz.orion.process.EBGenererListSalaire;
import globaz.orion.vb.partnerWeb.EBListeDesSalairesViewBean;

public class EBListeDesSalairesHelper extends FWHelper {

    private static final String DOCUMENT_RAPPEL_CLASSNAME = "globaz.draco.print.itext.DSContRappel_doc";

    public EBListeDesSalairesHelper() {
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        EBListeDesSalairesViewBean listeDesSalairesViewBean = (EBListeDesSalairesViewBean) viewBean;
        LEFormuleListViewBean formuleManager = new LEFormuleListViewBean();
        formuleManager.setSession((BSession) session);
        formuleManager.setForCategorie(ILEConstantes.CS_CATEGORIE_SUIVI_DS);
        formuleManager.find();
        String idFormuleDeRappel = null;

        // recherche de l'id la formule de rappel
        for (int i = 0; i < formuleManager.size(); i++) {
            LEFormuleViewBean formule = (LEFormuleViewBean) formuleManager.get(i);
            String className = formule.getClasseName();

            // si la formule utilise le doc rappel c'est qu'on est sur la formule de rappel
            if (DOCUMENT_RAPPEL_CLASSNAME.equals(className)) {
                idFormuleDeRappel = formule.getId();
                break;
            }
        }

        // si aucune formule de rappel n'est paramétrée on ne fait rien
        if (idFormuleDeRappel != null) {
            // on boucle à nouveau pour voir si le rappel est utilisé comme étape suivante d'une des formules
            for (int i = 0; i < formuleManager.size(); i++) {
                LEFormuleViewBean formule = (LEFormuleViewBean) formuleManager.get(i);

                // rechercher le détail du rappel pour la formule courante
                LERappelViewBean rappel = new LERappelViewBean();
                rappel.setSession((BSession) session);
                rappel.setId(formule.getId());
                rappel.retrieve();

                // si la formule est utilisée on l'indique au viewbean
                if (rappel.getIdDefinitionFormule().equals(idFormuleDeRappel)) {
                    listeDesSalairesViewBean.setDisplayGenererEtapesRappel(true);
                }
            }
        }
    }

    @Override
    protected void _start(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        EBListeDesSalairesViewBean vb = (EBListeDesSalairesViewBean) viewBean;

        try {
            EBGenererListSalaire process = new EBGenererListSalaire();
            process.setSession((BSession) session);
            process.setGenerateEtapeRappel(vb.getGenererEtapesRappel());
            process.setEmail(vb.getEmail());
            process.setForIsSimulation(vb.getForIsSimulation());
            process.setForDateReference(vb.getForDateReference());
            process.setForDateImpression(vb.getForDateImpression());
            BProcessLauncher.startJob(process);
        } catch (Exception e) {
            vb.setMessage(e.getMessage());
            vb.setMsgType(FWViewBeanInterface.ERROR);
        }
    }
}
