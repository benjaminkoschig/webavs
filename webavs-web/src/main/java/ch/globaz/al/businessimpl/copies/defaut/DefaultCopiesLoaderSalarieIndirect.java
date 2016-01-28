package ch.globaz.al.businessimpl.copies.defaut;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.CopieComplexModel;

/**
 * Classe de chargement des copies par d�faut pour un salari� avec paiement indirect
 * 
 * @author jts
 * 
 */
public class DefaultCopiesLoaderSalarieIndirect extends DefaultCopiesLoaderAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Contexte contenant les informations permettant de g�n�rer la liste des copies par d�faut
     */
    public DefaultCopiesLoaderSalarieIndirect(ContextDefaultCopiesLoader context) {
        this.context = context;
    }

    @Override
    public ArrayList<CopieComplexModel> getListCopies() throws JadePersistenceException, JadeApplicationException {

        ArrayList<CopieComplexModel> liste = new ArrayList<CopieComplexModel>();

        // original � l'affili�
        CopieComplexModel original = getCopieAffilie();
        original.getCopieModel().setOrdreCopie("1");
        liste.add(original);

        // copie au salari�
        CopieComplexModel copie = getCopieAllocataire();
        copie.getCopieModel().setOrdreCopie("2");
        liste.add(copie);

        return liste;
    }
}
