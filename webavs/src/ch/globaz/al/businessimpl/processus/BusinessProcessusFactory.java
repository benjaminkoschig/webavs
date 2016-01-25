package ch.globaz.al.businessimpl.processus;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.exceptions.processus.ALProcessusException;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationCotisations;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationCotisationsPar;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationCotisationsParPartiel;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationCotisationsPartiel;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationCotisationsPers;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationCotisationsPersPartiel;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationCotisationsRecapProv;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationCotisationsRecapProvPartiel;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationGenerationGlobaleHorlo;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationGenerationGlobaleHorloPar;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationGenerationGlobaleHorloParPartiel;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationGenerationGlobaleHorloPartiel;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationGenerationGlobaleHorloPers;
import ch.globaz.al.businessimpl.processus.processusimpl.CompensationGenerationGlobaleHorloPersPartiel;
import ch.globaz.al.businessimpl.processus.processusimpl.EcheancesProcessus;
import ch.globaz.al.businessimpl.processus.processusimpl.PaiementDirectGenerationFictive;
import ch.globaz.al.businessimpl.processus.processusimpl.PaiementDirectGenerationFictivePartiel;
import ch.globaz.al.businessimpl.processus.processusimpl.PaiementDirectGenerationFictiveRecap;
import ch.globaz.al.businessimpl.processus.processusimpl.PaiementDirectGenerationFictiveRecapPartiel;
import ch.globaz.al.businessimpl.processus.processusimpl.PaiementDirectGenerationGlobaleHorlo;
import ch.globaz.al.businessimpl.processus.processusimpl.PaiementDirectGenerationGlobaleHorloPartiel;
import ch.globaz.al.businessimpl.processus.processusimpl.PaiementDirectProcessus;
import ch.globaz.al.businessimpl.processus.processusimpl.PaiementDirectProcessusPartiel;
import ch.globaz.al.businessimpl.processus.processusimpl.PaiementDirectRecapProv;
import ch.globaz.al.businessimpl.processus.processusimpl.PaiementDirectRecapProvPartiel;
import ch.globaz.al.businessimpl.processus.processusimpl.PaiementDirectSansRecapProcessus;
import ch.globaz.al.businessimpl.processus.processusimpl.PaiementDirectSansRecapProcessusPartiel;

/**
 * Classe fournissant la bonne instance de processus en fonction du CS processus
 * 
 * @author gmo
 * 
 */
public abstract class BusinessProcessusFactory {

    /**
     * Retourne une instance d'un processus implémenté en fonction du CS
     * 
     * @param CSProcessus
     *            Le CS qui détermine le processus à utiliser
     * @param getPartielProcessus
     *            indique si on veut le processus partiel ou principal
     * @return une instance de BusinessProcessus
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public static BusinessProcessus getProcessus(String CSProcessus, boolean getPartielProcessus)
            throws JadePersistenceException, JadeApplicationException {

        if (ALCSProcessus.NAME_PROCESSUS_ECHEANCES.equals(CSProcessus)) {
            return new EcheancesProcessus();
        }
        if (ALCSProcessus.NAME_PROCESSUS_PAIEMENT_DIRECT.equals(CSProcessus)) {
            if (getPartielProcessus) {
                return new PaiementDirectProcessusPartiel();
            } else {
                return new PaiementDirectProcessus();
            }
        }
        if (ALCSProcessus.NAME_PROCESSUS_COMPENSATION.equals(CSProcessus)) {
            if (getPartielProcessus) {
                return new CompensationCotisationsPartiel();
            } else {
                return new CompensationCotisations();
            }
        }

        if (ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PAR.equals(CSProcessus)) {
            if (getPartielProcessus) {
                return new CompensationCotisationsParPartiel();
            } else {
                return new CompensationCotisationsPar();
            }
        }

        if (ALCSProcessus.NAME_PROCESSUS_COMPENSATION_PERS.equals(CSProcessus)) {
            if (getPartielProcessus) {
                return new CompensationCotisationsPersPartiel();
            } else {
                return new CompensationCotisationsPers();
            }
        }
        if (ALCSProcessus.NAME_PROCESSUS_COMPENSATION_RECAP_PROV.equals(CSProcessus)) {
            if (getPartielProcessus) {
                return new CompensationCotisationsRecapProvPartiel();
            } else {
                return new CompensationCotisationsRecapProv();
            }
        }
        if (ALCSProcessus.NAME_PROCESSUS_PAIEMENT_DIRECT_RECAP_PROV.equals(CSProcessus)) {
            if (getPartielProcessus) {
                return new PaiementDirectRecapProvPartiel();
            } else {
                return new PaiementDirectRecapProv();
            }
        }

        if (ALCSProcessus.NAME_PROCESSUS_PAIEMENT_DIRECT_SANS_RECAP.equals(CSProcessus)) {
            if (getPartielProcessus) {
                return new PaiementDirectSansRecapProcessusPartiel();
            } else {
                return new PaiementDirectSansRecapProcessus();
            }
        }
        if (ALCSProcessus.NAME_PROCESSUS_DIRECT_GENERATION_FICTIVE.equals(CSProcessus)) {
            if (getPartielProcessus) {
                return new PaiementDirectGenerationFictivePartiel();
            }
            return new PaiementDirectGenerationFictive();
        }

        if (ALCSProcessus.NAME_PROCESSUS_DIRECT_GENERATION_FICTIVE_RECAP.equals(CSProcessus)) {
            if (getPartielProcessus) {
                return new PaiementDirectGenerationFictiveRecapPartiel();
            }
            return new PaiementDirectGenerationFictiveRecap();
        }

        if (ALCSProcessus.NAME_PROCESSUS_FACTURATION_HORLO.equals(CSProcessus)) {

            if (getPartielProcessus) {
                return new CompensationGenerationGlobaleHorloPartiel();
            } else {
                return new CompensationGenerationGlobaleHorlo();
            }
        }

        if (ALCSProcessus.NAME_PROCESSUS_FACTURATION_HORLO_PERS.equals(CSProcessus)) {

            if (getPartielProcessus) {
                return new CompensationGenerationGlobaleHorloPersPartiel();
            } else {
                return new CompensationGenerationGlobaleHorloPers();
            }
        }
        if (ALCSProcessus.NAME_PROCESSUS_FACTURATION_HORLO_PAR.equals(CSProcessus)) {

            if (getPartielProcessus) {
                return new CompensationGenerationGlobaleHorloParPartiel();
            } else {
                return new CompensationGenerationGlobaleHorloPar();
            }
        }

        if (ALCSProcessus.NAME_PROCESSUS_DIRECT_GENERATION_GLOBALE.equals(CSProcessus)) {
            if (getPartielProcessus) {
                return new PaiementDirectGenerationGlobaleHorloPartiel();
            } else {
                return new PaiementDirectGenerationGlobaleHorlo();
            }
        }

        throw new ALProcessusException(
                "BusinessProcessusFactory#getProcessus: aucun processus correspondant à ce code ");
    }

}
