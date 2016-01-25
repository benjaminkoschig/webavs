package ch.globaz.pegasus.process.adaptation.stepRenteDemande;

import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.BSessionUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.api.IHEInputAnnonce;
import globaz.hermes.api.IHELotViewBean;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.EPCRenteAdaptation;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.exceptions.models.process.RenteAdapationDemandeException;
import ch.globaz.pegasus.business.models.process.adaptation.RenteAdapationDemande;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleDemandeCentrale;
import ch.globaz.pegasus.business.models.renteijapi.RenteMembreFamilleCalculeField;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.web.application.PCApplication;

public class PCProcessAdaptationEntityHandler implements JadeProcessEntityInterface {

    private JadeProcessEntity entity;
    private String idProcess;
    private Map<String, List<RenteMembreFamilleCalculeField>> mapRente;

    public PCProcessAdaptationEntityHandler(Map<String, List<RenteMembreFamilleCalculeField>> mapRente, String idProcess) {
        this.mapRente = mapRente;
        this.idProcess = idProcess;
    }

    private void createAnnonceHermes(RenteMembreFamilleCalculeField rente) throws AdaptationException {

        try {

            // création de l'API
            IHEInputAnnonce annonceARC = (IHEInputAnnonce) BSessionUtil.getSessionFromThreadContext().getAPIFor(
                    IHEInputAnnonce.class);

            // attributs standards ARC
            annonceARC.setIdProgramme(PCApplication.DEFAULT_APPLICATION_PEGASUS);
            annonceARC.setUtilisateur(JadeThread.currentUserId());
            annonceARC.setTypeLot(IHELotViewBean.TYPE_ENVOI);

            // Création de l'annonce
            annonceARC.put(IHEAnnoncesViewBean.CODE_APPLICATION, "61");
            annonceARC.put(IHEAnnoncesViewBean.CODE_ENREGISTREMENT, "01");

            annonceARC.put(IHEAnnoncesViewBean.PC_NUMERO_OFFICE_PC, EPCProperties.NUMERO_OFFICE_PC.getValue());

            annonceARC.put(IHEAnnoncesViewBean.PC_NUMERO_AGENCE_PC, CommonProperties.NUMERO_AGENCE.getValue());

            annonceARC.put(IHEAnnoncesViewBean.PC_REFERENCE_INTERNE_OFFICE_PC,
                    JadeStringUtil.fill(rente.getIdDroitMembreFamille(), ' ', 20));

            globaz.webavs.common.CommonNSSFormater formater = new globaz.webavs.common.CommonNSSFormater();

            annonceARC.put(IHEAnnoncesViewBean.CS_NUMERO_ASSURE, formater.unformat(rente.getNss()));
            annonceARC.put(IHEAnnoncesViewBean.NUMERO_ASSURE_AYANT_DROIT, formater.unformat(rente.getNss()));

            annonceARC.add(BSessionUtil.getSessionFromThreadContext().getCurrentThreadTransaction());

        } catch (FWSecurityLoginException e) {
            throw new AdaptationException("SecurityEception Eception", e);
        } catch (Exception e) {
            throw new AdaptationException("Hermes Exception", e);
        }

    }

    private RenteAdapationDemande createDemande(RenteMembreFamilleCalculeField rente,
            SimpleDemandeCentrale simpleDemandeCentrale) throws AdaptationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, RenteAdapationDemandeException {
        RenteAdapationDemande renteAdapationDemande = new RenteAdapationDemande();

        renteAdapationDemande.getSimpleRenteAdaptation().setIdDonneeFinanciereHeaderOld(
                rente.getIdDonneeFinanciereHeader());

        renteAdapationDemande.getSimpleRenteAdaptation().setGenre(
                BSessionUtil.getSessionFromThreadContext().getCode(
                        renteAdapationDemande.getSimpleRenteAdaptation().getGenre()));

        renteAdapationDemande.getSimpleRenteAdaptation().setEtat(EPCRenteAdaptation.WAIT);
        if (IPCDroits.CS_RENTE_API.equals(rente.getCsTypeDonneeFinanciere())) {
            renteAdapationDemande.getSimpleRenteAdaptation().setGenre(rente.getCsTypeRenteAPI());
            renteAdapationDemande.getSimpleRenteAdaptation().setAncienMontant(rente.getMontantApi());
            renteAdapationDemande.getSimpleRenteAdaptation().setCsTypdDonneeFinacire(IPCDroits.CS_RENTE_API);
        } else if (IPCDroits.CS_RENTE_AVS_AI.equals(rente.getCsTypeDonneeFinanciere())) {
            renteAdapationDemande.getSimpleRenteAdaptation().setGenre(rente.getCsTypeRenteAVS());
            renteAdapationDemande.getSimpleRenteAdaptation().setAncienMontant(rente.getMontantAvsAi());
            renteAdapationDemande.getSimpleRenteAdaptation().setCsTypdDonneeFinacire(IPCDroits.CS_RENTE_AVS_AI);
        } else {
            throw new AdaptationException("Error type donnée financière (type:" + rente.getCsTypeDonneeFinanciere()
                    + ")");
        }

        if (simpleDemandeCentrale != null) {
            renteAdapationDemande.setSimpleDemandeCentrale(simpleDemandeCentrale);
        } else {
            renteAdapationDemande.getSimpleDemandeCentrale().setIdDemandePC(rente.getIdDemandePC());
            renteAdapationDemande.getSimpleDemandeCentrale().setIdProcess(idProcess);
            renteAdapationDemande.getSimpleDemandeCentrale().setNss(rente.getNss());
            renteAdapationDemande.getSimpleDemandeCentrale().setReferenceInterne(rente.getIdDroitMembreFamille());
            createAnnonceHermes(rente);
        }

        return PegasusServiceLocator.getRenteAdapationDemandeService().create(renteAdapationDemande);
    }

    /*
     * Ceci va créer une demande pour chaque personne qui est comprit dans le calcule. Il se peut donc que l'on demande
     * une rente pour un enfant qui n'en n'a pas. Ceci est normale car on veut détecter s'il y a des nouvelles rentes
     * On vas créer une demande qui sera lié à une ou 3 rentes max
     */
    private void createDemandesCentral(List<RenteMembreFamilleCalculeField> rentes) throws AdaptationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, RenteAdapationDemandeException {

        Map<String, List<RenteMembreFamilleCalculeField>> mapAnnonceRentes = JadeListUtil.groupBy(rentes,
                new JadeListUtil.Key<RenteMembreFamilleCalculeField>() {
                    @Override
                    public String exec(RenteMembreFamilleCalculeField e) {
                        return e.getIdDroitMembreFamille();
                    }
                });

        for (Entry<String, List<RenteMembreFamilleCalculeField>> entry : mapAnnonceRentes.entrySet()) {

            // On regroupe les rentes par leur type pour déterminer si on a deux fois la même rente
            Map<String, List<RenteMembreFamilleCalculeField>> mapTypeRente = JadeListUtil.groupBy(entry.getValue(),
                    new JadeListUtil.Key<RenteMembreFamilleCalculeField>() {
                        @Override
                        public String exec(RenteMembreFamilleCalculeField e) {
                            return (JadeStringUtil.isBlankOrZero(e.getCsTypeRenteAPI())) ? e.getCsTypeRenteAVS() : e
                                    .getCsTypeRenteAPI();
                        }
                    });

            for (Entry<String, List<RenteMembreFamilleCalculeField>> listTypeRente : mapTypeRente.entrySet()) {
                if (listTypeRente.getValue().size() > 1) {
                    throw new AdaptationException(
                            "Trop de rente de même type ont été trouvées pour cette ce type de rente"
                                    + BSessionUtil.getSessionFromThreadContext()
                                            .getCodeLibelle(
                                                    (JadeStringUtil.isBlankOrZero(entry.getValue().get(0)
                                                            .getCsTypeRenteAPI())) ? entry.getValue().get(0)
                                                            .getCsTypeRenteAVS() : entry.getValue().get(0)
                                                            .getCsTypeRenteAPI()) + "(Nss: "
                                    + entry.getValue().get(0).getNss() + ")");
                }
            }

            Map<String, Integer> mapNss = new HashMap<String, Integer>();

            if (entry.getValue().size() >= 1) {
                RenteAdapationDemande renteAdaptation = null;
                for (RenteMembreFamilleCalculeField rente : entry.getValue()) {

                    // si il y a déjà un nss cela signifie que l'on a une rente et une API
                    if (renteAdaptation == null) {
                        mapNss.put(rente.getNss(), 1);
                        renteAdaptation = createDemande(rente, null);
                    } else {
                        if (mapNss.get(rente.getNss()) > 3) {
                            throw new AdaptationException(
                                    "Trop de rentes valables trouvées pour cette entité. Nb rente trouvé: "
                                            + entry.getValue().size());
                        } else {
                            // On peut avoir 3 rentes (1API est de rente(54 et 55))
                            // ICI on lie les rentes à la demande.
                            createDemande(rente, renteAdaptation.getSimpleDemandeCentrale());
                        }

                        mapNss.put(rente.getNss(), mapNss.get(rente.getNss()) + 1);
                    }
                }
            } else {
                throw new AdaptationException("Zero rente valable pour cette entité");
            }
        }
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        // Permet de trouve la listes des rente en fonction d'un idDemande
        List<RenteMembreFamilleCalculeField> rentes = mapRente.get(entity.getIdRef());

        if (rentes == null) {
            throw new AdaptationException("Aucune rente valable trouvée pour cette entité");
        }

        createDemandesCentral(rentes);

    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        this.entity = entity;
    }

}
