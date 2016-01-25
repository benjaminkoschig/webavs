package globaz.hercule.service;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.service.declarationSalaire.CEDecSalInfosReprises;
import globaz.hercule.service.declarationSalaire.CEDecSalInfosReprisesManager;
import globaz.hercule.service.declarationSalaire.CEDecSalMassesReprises;
import globaz.hercule.service.declarationSalaire.CEDecSalMassesReprisesManager;
import globaz.hercule.service.dto.CEMassesReprisesByAnneeDTO;
import java.util.Iterator;

/**
 * Service d'accé au déclaration de salaire
 * 
 * @author Sullivann Corneille
 * @since 20 février 2014
 */
public class CEDeclarationSalaireService {

    /**
     * <pre>
     * Retourne un objet qui contient le nombre de CI des 4 dernières années
     * ainsi que l'année passée en paramètre.
     * 
     * <pre>
     * 
     * @param session
     * @param transaction
     * @param numeroAffilie
     * @param annee
     * @return
     * @throws HerculeException
     */
    public static CEMassesReprisesByAnneeDTO getInfosReprises5DernieresAnnees(BSession session,
            BTransaction transaction, String idControle, int annee) throws HerculeException {
        if (idControle == null) {
            throw new NullPointerException("Unabled to retrieve infos. Id controle is null");
        }
        if (session == null) {
            throw new NullPointerException("Unabled to retrieve infos. Session is null");
        }

        // Recherche des bornes
        int anneeFin = annee;
        int anneeDebut = annee - 4;

        // Recherche des masses
        CEDecSalInfosReprisesManager manager = new CEDecSalInfosReprisesManager();
        manager.setAnneeDebut(anneeDebut);
        manager.setAnneeFin(anneeFin);
        manager.setIdControle(idControle);
        manager.setSession(session);

        try {
            manager.find(transaction);

        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to retrieve infos for controle id " + idControle
                    + " and anneeDeb = " + anneeDebut + " and anneeFin = " + anneeFin, e);
        }

        CEMassesReprisesByAnneeDTO masseRepriseDTO = new CEMassesReprisesByAnneeDTO();

        Iterator<CEDecSalInfosReprises> iteInfos = manager.iterator();
        while (iteInfos.hasNext()) {
            CEDecSalInfosReprises infos = iteInfos.next();
            masseRepriseDTO.addDonneesAVS(Integer.valueOf(infos.getAnnee()), infos.getMasseAvs());
            masseRepriseDTO.addDonneesCI(Integer.valueOf(infos.getAnnee()), infos.getNbCI());
        }

        return masseRepriseDTO;
    }

    /**
     * Retroune les montants repris lors d'un contrôle employeur.
     * Retourne null si aucune données
     * 
     * @param session
     * @param transaction
     * @param idControle
     * @return
     * @throws HerculeException
     */
    public static CEDecSalMassesReprises retrieveInfosMassesReprises(BSession session, BTransaction transaction,
            String idControle) throws HerculeException {

        if (idControle == null) {
            throw new NullPointerException("Unabled to retrieve infos reprises. Id controle is null");
        }
        if (session == null) {
            throw new NullPointerException("Unabled to retrieve infos reprises. Session is null");
        }

        CEDecSalMassesReprisesManager manager = new CEDecSalMassesReprisesManager();
        manager.setSession(session);
        manager.setIdControle(idControle);

        try {
            manager.find(transaction);

        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to retrieve infos reprises for controle id "
                    + idControle, e);
        }

        if (manager.getSize() > 0) {
            CEDecSalMassesReprises massesReprises = (CEDecSalMassesReprises) manager.getFirstEntity();
            return massesReprises;
        }

        return null;
    }

    /**
     * Constructeur de CEComptesIndividuelsService
     */
    protected CEDeclarationSalaireService() {
        throw new UnsupportedOperationException(); // prevents calls from subclass
    }
}
