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
 * Service utile pour les caisses horlogères.
 * 
 * @author jts
 * 
 */
public interface HorlogerBusinessService extends JadeApplicationService {

    /**
     * Vérifie si un canton verse une prestation de naissance/accueil pour un type de résidant
     * 
     * @param canton
     *            Canton de l'affilié {@link ALCSCantons#GROUP_CANTONS}
     * @param typePrest
     *            type de prestation {@link ALCSDroit#TYPE_ACCE} ou {@link ALCSDroit#TYPE_NAIS}
     * @param typeResident
     *            type de résident {@link ALCSTarif#GROUP_RESIDENT}
     * @param date
     *            Date à laquelle déterminer si une allocation de naissance est versée
     * @return <code>true</code> si une allocation de naissance/accueil est versée par le canton, <code>false</code>
     *         sinon.
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public boolean cantonVerseNaissance(String canton, String typePrest, String typeResident, String date)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Cherche si l'application est configurée pour une caisse hologère (Tucana actif)
     * 
     * @return <code>true</code> si c'est une caisse horlogère, <code>false</code> sinon
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public boolean isCaisseHorlogere() throws JadeApplicationException, JadePersistenceException;

    /**
     * Vérifie si l'allocation de naissance/accueil d'un enfant est de type horlogère.
     * 
     * @param dossier
     *            Dossier auquel est lié le droit de l'enfant
     * @param enfant
     *            Enfant pour lequel effectuer la vérification
     * @return <code>true</code> si l'allocation est de type horlogère, <code>false</code> sinon
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public boolean isNaissanceHorlogere(DossierComplexModel dossier, EnfantComplexModel enfant)
            throws JadeApplicationException, JadePersistenceException;
}
