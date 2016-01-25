package ch.globaz.al.business.services.horloger;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.constantes.ALCSCantons;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.EnfantComplexModel;

/**
 * Service utile pour les caisses horlog�res.
 * 
 * @author jts
 * 
 */
public interface HorlogerBusinessService extends JadeApplicationService {

    /**
     * V�rifie si un canton verse une prestation de naissance/accueil pour un type de r�sidant
     * 
     * @param canton
     *            Canton de l'affili� {@link ALCSCantons#GROUP_CANTONS}
     * @param typePrest
     *            type de prestation {@link ALCSDroit#TYPE_ACCE} ou {@link ALCSDroit#TYPE_NAIS}
     * @param typeResident
     *            type de r�sident {@link ALCSTarif#GROUP_RESIDENT}
     * @param date
     *            Date � laquelle d�terminer si une allocation de naissance est vers�e
     * @return <code>true</code> si une allocation de naissance/accueil est vers�e par le canton, <code>false</code>
     *         sinon.
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public boolean cantonVerseNaissance(String canton, String typePrest, String typeResident, String date)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Cherche si l'application est configur�e pour une caisse holog�re (Tucana actif)
     * 
     * @return <code>true</code> si c'est une caisse horlog�re, <code>false</code> sinon
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public boolean isCaisseHorlogere() throws JadeApplicationException, JadePersistenceException;

    /**
     * V�rifie si l'allocation de naissance/accueil d'un enfant est de type horlog�re.
     * 
     * @param dossier
     *            Dossier auquel est li� le droit de l'enfant
     * @param enfant
     *            Enfant pour lequel effectuer la v�rification
     * @return <code>true</code> si l'allocation est de type horlog�re, <code>false</code> sinon
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public boolean isNaissanceHorlogere(DossierComplexModel dossier, EnfantComplexModel enfant)
            throws JadeApplicationException, JadePersistenceException;
}
