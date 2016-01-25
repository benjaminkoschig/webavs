package globaz.perseus.test;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.jade.context.JadeContext;
import globaz.jade.context.JadeContextImplementation;
import globaz.jade.context.JadeThreadActivator;
import globaz.pyxis.application.TIApplication;
import java.rmi.RemoteException;
import ch.globaz.pyxis.business.model.AdressePaiementComplexModel;
import ch.globaz.pyxis.business.model.AdressePaiementSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class TestServiceAdressePaiement {

    private static Object obj = new Object();

    private static JadeContext getContext() throws Exception {
        BSession session = (BSession) GlobazSystem.getApplication(TIApplication.DEFAULT_APPLICATION_PYXIS).newSession(
                "globazf", "globazf");
        JadeContextImplementation ctxtImpl = new JadeContextImplementation();
        ctxtImpl.setApplicationId(TIApplication.DEFAULT_APPLICATION_PYXIS);
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
        TestServiceAdressePaiement testService = new TestServiceAdressePaiement();
        testService.process();
        System.out.println("End");
        System.exit(0);
    } // main

    private void afficheAdressePaiement(int nbAdressePaiement, AdressePaiementSearchComplexModel adressePaiementSearch) {
        System.out.println("------------------------------");
        System.out.println("ADRESSE PAIEMENT : " + nbAdressePaiement);

        for (int i = 0; i < adressePaiementSearch.getSearchResults().length; i++) {
            AdressePaiementComplexModel array_element = (AdressePaiementComplexModel) adressePaiementSearch
                    .getSearchResults()[i];
            System.out.println("No compte :" + array_element.getAdressePaiement().getNumCompteBancaire()
                    + " Nom,prénom :" + array_element.getTiers().getDesignation1() + ", "
                    + array_element.getTiers().getDesignation2() + " Banque :"
                    + array_element.getBanque().getTiersBanque().getDesignation1() + " avoirPaiement.idTiers: "
                    + array_element.getAvoirPaiement().getIdTiers() + " avoirPaiement.idApplication: "
                    + array_element.getAvoirPaiement().getIdApplication());
        }
        System.out.println("------------------------------");
    }

    private void process() {
        try {

            JadeThreadActivator.startUsingJdbcContext(this, TestServiceAdressePaiement.getContext());

            // Recherche de adresse de paiement par Designation
            AdressePaiementSearchComplexModel adressePaiementSearch = new AdressePaiementSearchComplexModel();
            adressePaiementSearch.setForDesignation1BeneficiaireLike("CSR");
            adressePaiementSearch.setForDate("07.09.2011");
            adressePaiementSearch.setWhereKey("widgetSearch");
            int nbAdressePaiement = TIBusinessServiceLocator.getAdresseService().countAdressePaiement(
                    adressePaiementSearch);
            adressePaiementSearch = TIBusinessServiceLocator.getAdresseService().findAdressePaiement(
                    adressePaiementSearch);
            afficheAdressePaiement(nbAdressePaiement, adressePaiementSearch);

            JadeThreadActivator.stopUsingContext(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } // process

} // class TestService
