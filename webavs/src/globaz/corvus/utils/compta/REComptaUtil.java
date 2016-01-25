package globaz.corvus.utils.compta;

import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIPropositionCompensation;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.prestation.tools.PRSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class REComptaUtil {

    /**
     * Retourne la section
     * 
     * @param session
     * @param idSection
     * @return
     * @throws Exception
     */
    public static APISection getSection(final BSession session, final String idSection) throws Exception {
        BISession sessionOsiris = PRSession.connectSession(session, CAApplication.DEFAULT_APPLICATION_OSIRIS);
        APISection section = null;
        // instanciation de la section
        section = (APISection) sessionOsiris.getAPIFor(APISection.class);
        section.setIdSection(idSection);
        section.retrieve();

        return section;

    }

    /**
     * Retourne une map des sections de blocage non sold�es. Les sections sont retourn�e dans l'ordre des plus anciennes
     * au plus r�cente. Cl� : idSection -> Content : String (libell� des la section + solde)
     * 
     * @param session
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static Map<String, String> getSectionsBloqueesACompenser(final BSession session, final String idTiers)
            throws Exception {

        Collection<APISection> compensations;
        BISession sessionOsiris = PRSession.connectSession(session, CAApplication.DEFAULT_APPLICATION_OSIRIS);

        /*
         * La m�thode retourne toutes les sections suceptible d'�tre compens�e Jusqu'� concurrence du montant pass� en
         * param�tre. Etant donn� que'elle ne retourne pas que les sections de type blocage (celles qui nous int�resse)
         * je luis passe un montant �l�v� pour s'assurer que toutes les sections de blocage soient bien retourn�e.
         */
        APIPropositionCompensation propositionCompensation = (APIPropositionCompensation) sessionOsiris
                .getAPIFor(APIPropositionCompensation.class);
        compensations = propositionCompensation.propositionCompensation(Integer.parseInt(idTiers), new FWCurrency(
                "99999999"), APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN);

        Map<String, String> result = new HashMap<String, String>();

        // On filtre les sections de blocage uniquement
        for (APISection section : compensations) {
            if (APISection.ID_TYPE_SECTION_BLOCAGE.equals(section.getIdTypeSection())) {
                result.put(section.getIdSection(),
                        section.getIdExterne() + " (" + JANumberFormatter.format(section.getSolde()) + ")");
            }
        }
        return result;

    }

    /**
     * Retourne une liste des sections de blocage non sold�es. Les sections sont retourn�e jusqu'� concurence du montant
     * � conpenser.
     * 
     * @param session
     * @param idTiers
     * @param montant
     * @param order
     * @return
     * @throws Exception
     */
    public static Collection<APISection> getSectionsBloqueesACompenser(final BSession session, final String idTiers,
            final String montant, final int order) throws Exception {

        Collection<APISection> compensations;
        BISession sessionOsiris = PRSession.connectSession(session, CAApplication.DEFAULT_APPLICATION_OSIRIS);

        /*
         * La m�thode retourne toutes les sections suceptible d'�tre compens�e Jusqu'� concurrence du montant pass� en
         * param�tre. Etant donn� que'elle ne retourne pas que les sections de type blocage (celles qui nous int�resse)
         * je luis passe un montant �l�v� pour s'assurer que toutes les sections de blocage soient bien retourn�e.
         */
        APIPropositionCompensation propositionCompensation = (APIPropositionCompensation) sessionOsiris
                .getAPIFor(APIPropositionCompensation.class);
        compensations = propositionCompensation.propositionCompensation(Integer.parseInt(idTiers), new FWCurrency(
                montant), order);

        Collection<APISection> result = new ArrayList<APISection>();

        // On filtre les sections de blocage uniquement
        for (APISection section : compensations) {
            if (APISection.ID_TYPE_SECTION_BLOCAGE.equals(section.getIdTypeSection())) {
                result.add(section);
            }
        }
        return result;

    }

}
