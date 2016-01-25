package globaz.al.process.decision;

import globaz.al.process.ALAbsrtactProcess;
import globaz.editing.EditingBuilder;
import globaz.editing.EditingHelper;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.context.JadeThread;
import globaz.jade.editing.JadeEditing;
import globaz.jade.jaxb.JAXBServices;
import globaz.jade.log.JadeLogger;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.bind.Marshaller;
import ch.ech.xmlns.ech_0010._4.MailAddressType;
import ch.ech.xmlns.ech_0044._2.PersonIdentificationPartnerType;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.inforom.xmlns.editing_common._1.AffilieType;
import ch.inforom.xmlns.editing_env._1.DestinatairesType;
import ch.inforom.xmlns.editing_env._1.EditionType;
import ch.inforom.xmlns.editing_env._1.EditionsType;
import ch.inforom.xmlns.editing_env._1.EnteteGlobaleType;
import ch.inforom.xmlns.editing_fam_decisions_af._1.DecisionAFRootType;

/**
 * Permet d'ex�cuter le process d'impression des d�cisions
 * 
 * @author PTA
 */

public class ALDecisionEditingProcess extends ALAbsrtactProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * ID du dossier d'allocation
     */
    private String idDossier = null;

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDossier() {
        return idDossier;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void process() {
        boolean errorInProcess = false;
        JAXBServices jaxbService = JAXBServices.getInstance();

        // r�cup�ration des info de l'utilsateurs
        final HashMap<String, String> userInfo = getUserInfo();

        try {
            final Marshaller editingMarshaller = jaxbService.getFragmentMarshaller(MailAddressType.class,
                    EditionsType.class, PersonIdentificationPartnerType.class, AffilieType.class,
                    DecisionAFRootType.class);

            // String path = "d:\\xml";
            String path = JadeEditing.getInstance().getXmlExportPath();

            EditingHelper h = new EditingHelper();

            // lecture du dossier pour lequel il faut cr�er le xml

            final DossierComplexModel dossier = ALServiceLocator.getDossierDecisionComplexeModelService().read(
                    getIdDossier());
            final String uuid = JadeUUIDGenerator.createStringUUID();
            h.with(path, uuid, editingMarshaller, new EditingBuilder() {

                // EnteteGlobaleType enteteGlobale = new EnteteGlobaleType();

                @Override
                public void build(EditingHelper h, EnteteGlobaleType enteteGlobale) throws Exception {

                    enteteGlobale = ALServiceLocator.getDecisionEditingService().buildEntetesDecisions(h,
                            enteteGlobale, dossier, uuid);
                    h.write(enteteGlobale);

                    EditionType edition = h.newEdition();

                    ALServiceLocator.getDecisionEditingService().getEnteteEditionType(edition, dossier);

                    // ajout des destinataires
                    DestinatairesType destinatairType = new DestinatairesType();
                    destinatairType = ALServiceLocator.getDecisionEditingService().builDestinataires(dossier,
                            destinatairType);
                    // setter les destinataires
                    edition.setDestinataires(destinatairType);

                    /*
                     * AJOUT du contenu m�tier de la d�cision AF
                     */
                    DecisionAFRootType contenu = new DecisionAFRootType();

                    // DecisionAFRootType contenu =
                    contenu = ALServiceLocator.getDecisionEditingService().getContent(contenu, dossier);
                    edition.setContenu(contenu);
                    // Ecriture
                    try {
                        h.write(edition);
                    } catch (Exception err) {
                        editingMarshaller.marshal(h.cof.createEdition(edition), System.out);
                        throw new Exception(err);
                    }

                }
            });
        } catch (Exception e) {

            errorInProcess = true;
            e.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });
        }

        // Envoie d'un mail si probl�me pour lancer le traitement
        ArrayList<String> emails = new ArrayList<String>();
        emails.add(JadeThread.currentUserEmail());
        if (errorInProcess) {
            try {
                sendCompletionMail(emails);
            } catch (Exception e1) {
                JadeLogger.error(this,
                        "Impossible d'envoyer le mail de r�sultat du traitement. Raison : " + e1.getMessage() + ", "
                                + e1.getCause());
            }
        }

    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void testProcess() {
        process();

    }
}