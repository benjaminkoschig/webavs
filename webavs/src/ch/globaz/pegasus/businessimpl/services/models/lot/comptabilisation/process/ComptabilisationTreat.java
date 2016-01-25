package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.corvus.api.lots.IRELot;
import globaz.globall.util.JAException;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.db.comptes.CAJournal;
import java.util.List;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.GeneratePrestationOperations;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.PCLotTypeOperationFactory;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.PrestationOperations;

class ComptabilisationTreat {

    private ComptabilisationData data;
    /** Type d'operation, match avec le type de lot **/
    private PCLotTypeOperationFactory operation;

    public ComptabilisationTreat(ComptabilisationData data) throws ComptabiliserLotException {
        this.data = data;
        verifDonneesData();
        setTypeOperation();
    }

    private JournalSimpleModel generateJournal() throws JadeApplicationServiceNotAvailableException,
            ComptabiliserLotException {
        JournalSimpleModel journal = new JournalSimpleModel();
        journal.setLibelle(data.getLibelleJournal());
        journal.setDateValeurCG(data.getDateValeur().toStr("."));
        journal.setTypeJournal(CAJournal.TYPE_AUTOMATIQUE);
        return journal;
    }

    private PegasusJournalConteneur generatePegasusJournal() throws JadeApplicationException,
            JadeApplicationServiceNotAvailableException, ComptabiliserLotException {
        PegasusJournalConteneur conteneur = new PegasusJournalConteneur();
        conteneur.setJournal(generateJournal());
        conteneur.setOperations(generatePresationsOperations());
        conteneur.setLot(data.getSimpleLot());
        conteneur.setDateEchance(data.getDateEchance().toStr("."));
        conteneur.setDateValeur(data.getDateEchance().toStr("."));
        return conteneur;
    }

    public List<PrestationOperations> generatePresationsOperations() throws JadeApplicationException {

        GeneratePrestationOperations generateOperations = new GeneratePrestationOperations();
        return generateOperations.generateAllOperationsPrestations(data.getListOV(), data.getSections(),
                data.getComptesAnnexes(), data.getDateValeur().toStr("."), operation);
    }

    public boolean hasError() {
        return JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR);
    }

    private void setTypeOperation() {
        operation = PCLotTypeOperationFactory.csTypeOf(data.getSimpleLot().getCsTypeLot());
    }

    public ComptabilisationData treat() throws ComptabiliserLotException, JAException {
        if (data.getLibelleJournal() == null) {
            data.setLibelleJournal(data.getSimpleLot().getDescription());
        }
        try {
            if (!hasError()) {
                data.getSimpleLot().setCsEtat(IRELot.CS_ETAT_LOT_EN_TRAITEMENT);

                data.setJournalConteneur(generatePegasusJournal());
                data.getSimpleLot().setCsEtat(IRELot.CS_ETAT_LOT_VALIDE);
                data.getSimpleLot().setDateEnvoi(data.getDateValeur().toStr("."));
                return data;
            } else {
                data.getSimpleLot().setCsEtat(IRELot.CS_ETAT_LOT_ERREUR);
                return null;
            }
        } catch (Throwable e) {
            data.getSimpleLot().setCsEtat(IRELot.CS_ETAT_LOT_ERREUR);
            throw new ComptabiliserLotException("Unable comptabilise", e);
        }
    }

    private void verifDonneesData() throws ComptabiliserLotException {
        if (data == null) {
            throw new ComptabiliserLotException("Unable to comptabliserLoti data is null!");
        }

        if (data.getSimpleLot() == null) {
            throw new ComptabiliserLotException("Unable to comptabliserLoti the lot is null!");
        }

        if (data.getDateValeur() == null) {
            throw new ComptabiliserLotException("Unable to comptabliserLot the dateValeur is null!");
        }

        if (data.getDateDernierPmt() == null) {
            throw new ComptabiliserLotException("Unable to comptabliserLot the dateDernierPmt is null!");
        }

        if (data.getListOV() == null) {
            throw new ComptabiliserLotException("Unable to comptabliserLot the listOv is null!");
        }
    }

}
