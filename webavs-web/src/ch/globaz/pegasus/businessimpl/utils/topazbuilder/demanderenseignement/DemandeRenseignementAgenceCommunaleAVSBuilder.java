package ch.globaz.pegasus.businessimpl.utils.topazbuilder.demanderenseignement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.babel.business.services.BabelServiceLocator;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.demanderenseignement.IPCDemandeRenseignementBuilderType;
import ch.globaz.pegasus.business.exceptions.models.demanderenseignement.DemandeRenseignementException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PegasusUtil;

public class DemandeRenseignementAgenceCommunaleAVSBuilder extends AbstractDemandeRenseignementBuilder {

    public static final String ANNEXES = "ANNEXES";
    public static final String COPIES = "COPIES";
    public static final String ZONES_TEXTE_LIBRE = "AGENCE_COMM_AVS_ZONES_TEXTE_LIBRE";

    protected List<String> annexes = new ArrayList<String>();
    protected List<String> copies = new ArrayList<String>();

    private List<String> listeTexteLibre = new ArrayList<String>();
    private final String noDocument = "XXX";

    @Override
    protected JadePrintDocumentContainer getDocumentBuilded(JadePrintDocumentContainer allDoc, String idGestionnaire)
            throws DemandeRenseignementException {

        return new SingleDemandeRenseignementAgenceCommunaleAVSBuilder().build(babelDoc, babelDocCommun,
                babelDocPageGarde, allDoc, idGestionnaire, requerant, listeTexteLibre, annexes, copies);
    }

    @Override
    protected String getEmailTitle() {

        String title = getSession().getLabel("PROCESS_DEMANDE_RENSEIGNEMENT_AGENCE_COMMUNALE");
        String nomPrenomTiers = PegasusUtil.formatNomPrenom(requerant.getTiers());
        title = PRStringUtils.replaceString(title, "{0}", nomPrenomTiers);

        return title;
    }

    @Override
    protected void loadDBEntity() throws DemandeRenseignementException {

        try {
            Demande demande = PegasusServiceLocator.getDemandeService().read(idDemandePC);
            requerant = demande.getDossier().getDemandePrestation().getPersonneEtendue();

        } catch (JadePersistenceException e) {
            throw new DemandeRenseignementException("A persistence error happened!", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DemandeRenseignementException("Service not available!", e);
        } catch (JadeApplicationException e) {
            throw new DemandeRenseignementException("A Jade error happened!", e);
        }

        // Chargement Babel
        try {
            // On récupère la langue du tiers requérant
            Langues tiersLangue = LanguageResolver.resolveISOCode(requerant.getTiers().getLangue());
            Map<Langues, CTDocumentImpl> documentsBabel;
            documentsBabel = BabelServiceLocator.getPCCatalogueTexteService().searchForDemandeRenseignement(
                    IPCDemandeRenseignementBuilderType.BABEL_AGENCE_COMMUNALE_AVS);
            babelDoc = documentsBabel.get(tiersLangue);
            documentsBabel = BabelServiceLocator.getPCCatalogueTexteService().searchForDemandeRenseignement(
                    IPCDemandeRenseignementBuilderType.BABEL_COMMUN);
            babelDocCommun = documentsBabel.get(tiersLangue);
            documentsBabel = BabelServiceLocator.getPCCatalogueTexteService().searchForPageGardeCopiePC();
            babelDocPageGarde = documentsBabel.get(tiersLangue);
        } catch (Exception e) {
            throw new DemandeRenseignementException("Error while loading catalogue Babel!", e);
        }

    }

    @Override
    public void loadParameters(Map<String, List<String>> parameters) {
        // String zonesTexteLibre = parameters.get(DemandeRenseignementAgenceCommunaleAVSBuilder.ZONES_TEXTE_LIBRE);

        // récupération des paramètres zone texte libre, en fait une chaine de caractère, --> get(0), et on le set dans
        // la liste
        listeTexteLibre = parameters.get(DemandeRenseignementAgenceCommunaleAVSBuilder.ZONES_TEXTE_LIBRE);

        // récupération des annexes et copies, qui eux sont des listes
        copies = parameters.get(DemandeRenseignementAgenceCommunaleAVSBuilder.COPIES);
        annexes = parameters.get(DemandeRenseignementAgenceCommunaleAVSBuilder.ANNEXES);

        // this.copies.addAll(Arrays.asList(parameters.get(DemandeRenseignementAgenceCommunaleAVSBuilder.COPIES)
        // .split("¦")));
        // this.annexes.addAll(Arrays.asList(parameters.get(DemandeRenseignementAgenceCommunaleAVSBuilder.ANNEXES).split(
        // "¦")));
    }

}
