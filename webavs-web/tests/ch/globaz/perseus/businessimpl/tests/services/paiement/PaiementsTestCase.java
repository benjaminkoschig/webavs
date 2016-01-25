package ch.globaz.perseus.businessimpl.tests.services.paiement;

import globaz.jade.client.util.JadeDateUtil;
import java.util.Date;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.osiris.business.service.CABusinessServiceLocator;
import ch.globaz.pegasus.tests.util.BaseTestCase;

public class PaiementsTestCase extends BaseTestCase {

    // @Override
    // public void setUp() throws Exception {
    // super.setUp();
    // }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.tests.util.BaseTestCase#tearDown()
     */
    // @Override
    // public void tearDown() {
    // // genere erreur pour provoquer un rollback de la db
    // try {
    // // JadeThread.rollbackSession();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // super.tearDown();
    // }

    public final void test01TestPaiement() throws Exception {

        String dateComptable = JadeDateUtil.getGlobazFormattedDate(new Date());

        JournalSimpleModel journal;
        journal = CABusinessServiceLocator.getJournalService().createJournal("PF Test", dateComptable);

        // PerseusServiceLocator.getPmtDecisionService().comptabiliserLot(PerseusServiceLocator.getLotService().read("6"));

        // PerseusServiceLocator.getPmtMensuelService().executerPaiementMensuel();

        // try {
        // String dateComptable = JadeDateUtil.getGlobazFormattedDate(new Date());
        //
        // // création du journal
        // JournalSimpleModel journal;
        // journal = CABusinessServiceLocator.getJournalService().createJournal("PF Journal test", dateComptable);
        //
        // JournalConteneur jc = new JournalConteneur();
        // jc.AddJournal(journal);
        //
        // AdresseTiersDetail adr = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers("1069581",
        // true, IPFConstantes.CS_DOMAINE_ADRESSE, dateComptable, null);
        // if ((adr == null) || (adr.getFields() == null)) {
        // throw new ALPaiementPrestationException(
        // "Erreur dans le paiement : impossible de créer l'ordre de versement sans adresse de paiement");
        // }
        //
        // // compte annexe
        // CompteAnnexeSimpleModel ca = CABusinessServiceLocator.getCompteAnnexeService().getCompteAnnexeByRole(
        // journal.getIdJournal(), "1069581", IntRole.ROLE_PCF, "756.3959.5655.92");
        //
        // String idExterne = this.get
        // // section
        // SectionSimpleModel section = CABusinessServiceLocator.getSectionService().getSectionByIdExterne(
        // ca.getIdCompteAnnexe(), APISection.CATEGORIE_SECTION_PCF_PP, "201178000", journal);
        //
        // // écriture
        // jc.addEcriture("756.3959.5655.92", APIEcriture.DEBIT, ca.getIdCompteAnnexe(), section.getId(),
        // dateComptable, IPFConstantes.COMPTA_COMPTE_PCF, "1985.0");
        //
        // // ordre de versement
        // jc.addOrdreVersement(journal.getId(), ca.getId(), section.getId(),
        // adr.getFields().get(AdresseTiersDetail.ADRESSEP_ID_AVOIR_PAIEMENT_UNIQUE), dateComptable, "1985.0",
        // "CHF", "CHF", APIOperationOrdreVersement.VIREMENT, CAOrdreGroupe.NATURE_PCF,
        // "Test versement paiement mensuel PC Famille 756.3959.5655.92");
        //
        // journal = CABusinessServiceLocator.getJournalService().createJournalAndOperations(jc);
        // } catch (JadeApplicationServiceNotAvailableException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (JadeApplicationException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

    }

}