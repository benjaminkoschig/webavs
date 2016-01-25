package ch.globaz.al.businessimpl.copies.defaut;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.CopieComplexModel;

/**
 * Classe de chargement des copies par défaut pour un salarié avec paiement direct
 * 
 * @author jts
 * 
 */
public class DefaultCopiesLoaderSalarieDirect extends DefaultCopiesLoaderAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Contexte contenant les informations permettant de générer la liste des copies par défaut
     */
    public DefaultCopiesLoaderSalarieDirect(ContextDefaultCopiesLoader context) {
        this.context = context;
    }

    @Override
    public ArrayList<CopieComplexModel> getListCopies() throws JadePersistenceException, JadeApplicationException {

        ArrayList<CopieComplexModel> liste = new ArrayList<CopieComplexModel>();

        // original au salarié
        CopieComplexModel copie = getCopieAllocataire();
        copie.getCopieModel().setOrdreCopie("1");
        liste.add(copie);

        // copie à l'affilié
        CopieComplexModel original = getCopieAffilie();
        original.getCopieModel().setOrdreCopie("2");
        liste.add(original);

        return liste;
    }

}
