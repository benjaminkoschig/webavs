package ch.globaz.al.businessimpl.copies.defaut;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.CopieComplexModel;

/**
 * Classe de chargement des copies par défaut pour un non-actif avec paiement direct
 * 
 * @author jts
 * 
 */
public class DefaultCopiesLoaderNonActifAgence extends DefaultCopiesLoaderAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Contexte contenant les informations permettant de générer la liste des copies par défaut
     */
    public DefaultCopiesLoaderNonActifAgence(ContextDefaultCopiesLoader context) {
        this.context = context;
    }

    @Override
    public ArrayList<CopieComplexModel> getListCopies() throws JadePersistenceException, JadeApplicationException {

        ArrayList<CopieComplexModel> liste = new ArrayList<CopieComplexModel>();

        // original à l'affilié (non-actif)
        CopieComplexModel original = getCopieAffilie();
        original.getCopieModel().setOrdreCopie("1");
        liste.add(original);

        // copie à l'agence communale
        CopieComplexModel copie = getCopieAgenceCommunale();
        if (copie != null) {
            copie.getCopieModel().setOrdreCopie("2");
            liste.add(copie);
        }

        return liste;
    }

}
