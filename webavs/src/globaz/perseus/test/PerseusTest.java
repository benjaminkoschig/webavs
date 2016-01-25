package globaz.perseus.test;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.web.application.PFApplication;

public class PerseusTest {

    private static Object obj = new Object();

    private static JadeContext getContext() throws Exception {
        BSession session = (BSession) GlobazSystem.getApplication(PFApplication.DEFAULT_APPLICATION_PERSEUS)
                .newSession("globazf", "globazf");
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(PFApplication.DEFAULT_APPLICATION_PERSEUS);
        ctxtImpl.setLanguage(session.getIdLangueISO());
        ctxtImpl.setUserEmail(session.getUserEMail());
        ctxtImpl.setUserId(session.getUserId());
        ctxtImpl.setUserName(session.getUserName());

        return ctxtImpl;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Start");
        PerseusTest testService = new PerseusTest();
        testService.process();
        System.out.println("End");
        System.exit(0);
    } // main

    private void process() {
        try {

            Lot lot = PerseusServiceLocator.getLotService().read("68");

            // Map<String, List<Facture>> mfs = PerseusServiceLocator.getPmtFactureService()
            // .groupListFactureByMembreFamille(lot);
            //
            // for (String mf : mfs.keySet()) {
            // System.out.println("--- Membre Famille : " + mf + " ---");
            //
            // for (Facture facture : mfs.get(mf)) {
            // System.out.println(" - Facture : " + facture.getSimpleFacture().getMontant());
            // }
            //
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
    } // process
} // class TestService
