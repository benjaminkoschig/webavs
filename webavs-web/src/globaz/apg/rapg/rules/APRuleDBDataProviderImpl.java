package globaz.apg.rapg.rules;

import globaz.apg.db.annonces.APAnnonceAPGManager;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.pyxis.db.adressecourrier.TIPaysManager;

/**
 * Implementation 'Standard' de l'interface APRuleDBDataProvider. C'est l'implémentation par défaut pour la production
 * qui est explicitement déclarée dans la classe mère Rule
 * 
 * @author lga
 */
public class APRuleDBDataProviderImpl implements APRuleDBDataProvider {

    public APRuleDBDataProviderImpl() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.rapg.rules.APRuleDBDataProvider#isTimeStampUnique(java.lang.String)
     */
    @Override
    public boolean isTimeStampUnique(String timeStamp, BSession session) throws APRuleExecutionException {
        APAnnonceAPGManager manager = new APAnnonceAPGManager();
        manager.setSession(session);
        manager.setForTimeStamp(timeStamp);
        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception exception) {
            throw new APRuleExecutionException(exception);
        }
        if (manager.size() > 0) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.rapg.rules.APRuleDBDataProvider#isCodePaysExistant(java.lang.String)
     */
    @Override
    public boolean isCodePaysExistant(String insurantDomicileCountry, BSession session) throws APRuleExecutionException {
        TIPaysManager manager = new TIPaysManager();
        manager.setSession(session);
        manager.setForIdPays(insurantDomicileCountry);
        try {
            manager.find();
        } catch (Exception e) {
            throwRuleExecutionException(e);
        }
        if (manager.getSize() != 1) {
            return false;
        }
        return true;
    }

    private void throwRuleExecutionException(Exception exception) throws APRuleExecutionException {
        throw new APRuleExecutionException("An internal exception was thrown on rule ["
                + this.getClass().getSimpleName() + "] execution : " + exception.toString());
    }
}
