package ch.globaz.al.businessimpl.copies.libelles;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.exceptions.document.ALDocumentAddressException;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Classe de récupération de libellé de copie pour un cas standard
 * 
 * @author jts
 */
public class LibelleCopieLoader extends LibelleCopieLoaderAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Contexte contenant les informations permettant de récupérer le libellé à utiliser
     */
    public LibelleCopieLoader(ContextLibellesCopiesLoader context) {
        this.context = context;
    }

    @Override
    public String getLibelle() throws JadePersistenceException, JadeApplicationException {

        AdresseTiersDetail adressTiers = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                (context.getIdTiers()), true, JadeDateUtil.getGlobazFormattedDate(new Date()), ALCSTiers.DOMAINE_AF,
                AdresseService.CS_TYPE_COURRIER, "");

        if (JadeStringUtil.isEmpty(adressTiers.getAdresseFormate())) {
            throw new ALDocumentAddressException("LibelleCopieLoader#getLibelle : address not found (ID tiers "
                    + context.getIdTiers() + ")");
        }

        return adressTiers.getAdresseFormate().replace('\n', ' ');
    }
}
