package ch.globaz.al.businessimpl.copies.defaut;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.CopieComplexModel;

/**
 * Classe de chargement des copies par défaut pour un salarié avec paiement indirect
 * 
 * @author jts
 * 
 */
public class DefaultCopiesLoaderSalarieIndirect extends DefaultCopiesLoaderAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Contexte contenant les informations permettant de générer la liste des copies par défaut
     */
    public DefaultCopiesLoaderSalarieIndirect(ContextDefaultCopiesLoader context) {
        this.context = context;
    }

    @Override
    public ArrayList<CopieComplexModel> getListCopies() throws JadePersistenceException, JadeApplicationException {

        ArrayList<CopieComplexModel> liste = new ArrayList<CopieComplexModel>();

        // original à l'affilié
        CopieComplexModel original = getCopieAffilie();
        original.getCopieModel().setOrdreCopie("1");
        liste.add(original);

        // copie au salarié
        CopieComplexModel copie = getCopieAllocataire();
        copie.getCopieModel().setOrdreCopie("2");
        liste.add(copie);

        return liste;
    }
}
