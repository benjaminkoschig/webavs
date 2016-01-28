package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AssuranceVieException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AutresDettesProuveesException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierNonHabitableException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CapitalLPPException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CompteBancaireCCPException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.TitreException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVie;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuvees;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitable;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPP;
import ch.globaz.pegasus.business.models.fortuneusuelle.CompteBancaireCCP;
import ch.globaz.pegasus.business.models.fortuneusuelle.Titre;

public interface FortuneUsuelleService extends JadeApplicationService {

    public void copyFortuneUsuelle(Droit newDroit, Droit oldDroit, DroitMembreFamilleSearch droitMembreFamilleSearch)
            throws DonneeFinanciereException;

    /**
     * Permet de créer une AssuranceVie <b>ATTENTION en utilisant cette fonction on ne passe pas par la validation du
     * droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute connaissance
     * de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param assuranceVie
     * @return AssuranceVie
     * @throws AssuranceVieException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public AssuranceVie createAssuranceVie(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AssuranceVie assuranceVie) throws AssuranceVieException,
            JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer une AutresDettesProuvees <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param autresDettesProuvees
     * @returnAutresDettesProuvees
     * @throws AutresDettesProuveesException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public AutresDettesProuvees createAutresDettesProuvees(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, AutresDettesProuvees autresDettesProuvees)
            throws AutresDettesProuveesException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer un BienImmobilierHabitationNonPrincipale <b>ATTENTION en utilisant cette fonction on ne passe pas
     * par la validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé
     * en toute connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données
     * finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param bienImmobilierHabitationNonPrincipale
     * @return BienImmobilierHabitationNonPrincipale
     * @throws BienImmobilierHabitationNonPrincipaleException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public BienImmobilierHabitationNonPrincipale createBienImmobilierHabitationNonPrincipale(
            SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer un BienImmobilierNonHabitable <b>ATTENTION en utilisant cette fonction on ne passe pas par la
     * validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param bienImmobilierNonHabitable
     * @return BienImmobilierNonHabitable
     * @throws BienImmobilierNonHabitableException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public BienImmobilierNonHabitable createBienImmobilierNonHabitable(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer un BienImmobilierServantHabitationPrincipale <b>ATTENTION en utilisant cette fonction on ne passe
     * pas par la validation du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A
     * utilsé en toute connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des
     * données finacière. BienImmobilierServantHabitationPrincipale
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param bienImmobilierServantHabitationPrincipale
     * @return
     * @throws BienImmobilierServantHabitationPrincipaleException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public BienImmobilierServantHabitationPrincipale createBienImmobilierServantHabitationPrincipale(
            SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException,
            DonneeFinanciereException;

    /**
     * Permet de créer un CapitalLPP <b>ATTENTION en utilisant cette fonction on ne passe pas par la validation du droit
     * (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute connaissance de
     * cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * BienImmobilierServantHabitationPrincipale
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param capitalLPP
     * @return CapitalLPP
     * @throws CapitalLPPException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public CapitalLPP createCapitalLPP(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille,
            CapitalLPP capitalLPP) throws CapitalLPPException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer un CompteBancaireCCP <b>ATTENTION en utilisant cette fonction on ne passe pas par la validation
     * du droit (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute
     * connaissance de cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * BienImmobilierServantHabitationPrincipale
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param compteBancaireCCP
     * @return CompteBancaireCCP
     * @throws CompteBancaireCCPException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public CompteBancaireCCP createCompteBancaireCCP(SimpleVersionDroit simpleVersionDroit,
            DroitMembreFamille droitMembreFamille, CompteBancaireCCP compteBancaireCCP)
            throws CompteBancaireCCPException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Permet de créer un Titre <b>ATTENTION en utilisant cette fonction on ne passe pas par la validation du droit
     * (auncune verification du spy)</b>. De plus aucun trigger ne sera déclanché. A utilsé en toute connaissance de
     * cause! A utiliser surtout dans des traitement de mass tell que la copie des données finacière.
     * BienImmobilierServantHabitationPrincipale
     * 
     * 
     * @param simpleVersionDroit
     * @param droitMembreFamille
     * @param titre
     * @return Titre
     * @throws TitreException
     * @throws JadePersistenceException
     * @throws DonneeFinanciereException
     */
    public Titre createTitre(SimpleVersionDroit simpleVersionDroit, DroitMembreFamille droitMembreFamille, Titre titre)
            throws TitreException, JadePersistenceException, DonneeFinanciereException;

    /**
     * Suppression des entitée fortunUsuelleService en fonction d'une version de droit dans les donneé financière header
     * 
     * @param idsDonneFinanciere
     * @param typeDonneFinianciere
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere, String typeDonneFinianciere)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException;
}
