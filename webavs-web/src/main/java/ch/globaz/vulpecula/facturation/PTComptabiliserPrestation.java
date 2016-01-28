package ch.globaz.vulpecula.facturation;

import globaz.globall.db.BProcess;
import globaz.musca.api.IFAPassage;
import org.apache.commons.lang.NotImplementedException;

/**
 * Module de facturation permettant la comptabilisation des prestations du module M�tier.
 * 
 * @since WebBMS 0.01.04
 */
public class PTComptabiliserPrestation extends PTFacturationGenericImpl {

    @Override
    public PTProcessFacturation getProcessComptabilisation() {
        return new PTProcessFacturationComptabiliserPrestation();
    }

    @Override
    public PTProcessFacturation getProcessGeneration() {
        throw new NotImplementedException("Cette m�thode n'est pas impl�ment�e.");
    }

    @Override
    public boolean generer(final IFAPassage passage, final BProcess context, String idModule) throws Exception {
        return false;
    }

}
