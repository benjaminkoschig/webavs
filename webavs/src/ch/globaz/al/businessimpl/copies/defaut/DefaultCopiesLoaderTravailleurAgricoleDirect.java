package ch.globaz.al.businessimpl.copies.defaut;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.CopieComplexModel;

/**
 * Classe de chargement des copies par d�faut pour un travailleur agricole avec paiement direct
 * 
 * @author jts
 * 
 */
public class DefaultCopiesLoaderTravailleurAgricoleDirect extends DefaultCopiesLoaderAbstract {

    /**
     * Constructeur
     * 
     * @param context
     *            Contexte contenant les informations permettant de g�n�rer la liste des copies par d�faut
     */
    public DefaultCopiesLoaderTravailleurAgricoleDirect(ContextDefaultCopiesLoader context) {
        this.context = context;
    }

    @Override
    public ArrayList<CopieComplexModel> getListCopies() throws JadePersistenceException, JadeApplicationException {

        ArrayList<CopieComplexModel> liste = new ArrayList<CopieComplexModel>();

        // original au salari� (travailleur agricole)
        CopieComplexModel copie = getCopieAllocataire();
        copie.getCopieModel().setOrdreCopie("1");
        liste.add(copie);

        // copie � l'affili�
        CopieComplexModel original = getCopieAffilie();
        original.getCopieModel().setOrdreCopie("2");
        liste.add(original);

        return liste;
    }

}
