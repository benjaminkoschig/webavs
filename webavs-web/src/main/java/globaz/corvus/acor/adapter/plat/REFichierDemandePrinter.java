package globaz.corvus.acor.adapter.plat;

import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import globaz.prestation.acor.plat.PRFichierDemandeDefautPrinter;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe permettant l'écriture des fichiers de demandes de rente ACOR.
 * </p>
 * 
 * @author scr
 */
public class REFichierDemandePrinter extends PRFichierDemandeDefautPrinter {

    public REFichierDemandePrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    @Override
    public void printLigne(StringBuffer writer) throws PRACORException {

        // 1. le no AVS de l'assuré faisant la demande de prestation
        writeAVS(writer, ((REACORDemandeAdapter) parent).nssDemande());

        // 2. le type de demande
        writeChaine(writer, parent.getTypeDemande());

        // 3. la date de traitement
        writeDate(writer, parent.getDateTraitement());

        // 4. la date de depot de la demande
        writeDate(writer, parent.getDateDepot());

        // 5. le type de calcul
        writeChaineSansFinDeChamp(writer, parent.getTypeCalcul());
        hasLignes = false;

    }

}
