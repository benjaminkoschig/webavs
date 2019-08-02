package ch.globaz.naos.ws.contact;

import ch.globaz.orion.ws.exceptions.WebAvsException;
import ch.globaz.orion.ws.service.UtilsService;
import globaz.naos.db.contactFpv.AFContactFPV;
import globaz.naos.db.contactFpv.AFContactFPVManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.exceptions.AFTechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService(endpointInterface = "ch.globaz.naos.ws.contact.WebAvsContactService")
public class WebAvsContactServiceImpl implements WebAvsContactService {

    private static Logger logger = LoggerFactory.getLogger(WebAvsContactServiceImpl.class);

    @Override
    public Contact getContactFPV(String numeroAffilie) throws WebAvsException {
        BSession session = UtilsService.initSession();
        AFContactFPVManager manager = new AFContactFPVManager();
        manager.setForNumeroAffilier(numeroAffilie);
        manager.setSession(session);
        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            logger.error("Impossible de récupérer le contact pour le numéro d'affilié : " + numeroAffilie + " \n" + e.getLocalizedMessage());
            throw new WebAvsException("Impossible de récupérer le contact pour le numéro d'affilié : " + numeroAffilie + " \n" + e.getLocalizedMessage(), e);
        }

        if(manager.size() > 0) {
            return convertFromEntity(session, (AFContactFPV) manager.getFirstEntity());
        }
        return null;
    }

    @Override
    public List<Contact> getListContactFPV() throws WebAvsException {
        BSession session = UtilsService.initSession();
        AFContactFPVManager manager = new AFContactFPVManager();
        manager.setSession(session);
        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            logger.error("Impossible de récupérer la liste des contacts" + " \n" + e.getLocalizedMessage());
            throw new WebAvsException("Impossible de récupérer la liste des contacts" + " \n" + e.getLocalizedMessage());
        }

        if(manager.size() > 0) {
            List<Contact> listContact = new ArrayList<>();
            List<AFContactFPV> result = manager.toList();
            for (AFContactFPV entity : result) {
                listContact.add(convertFromEntity(session, entity));
            }
            return listContact;
        }
        return null;
    }

    @Override
    public Boolean setContactFPV(String numeroAffilie, String nom, String prenom, EnumSexe sexe, String email, boolean stopProspection) throws WebAvsException {
        BSession session = UtilsService.initSession();
        AFAffiliation affiliation = getAffiliation(numeroAffilie, session);

        if(affiliation == null || affiliation.getAffiliationId() == null) {
            logger.error("Impossible de récupérer l'affilié : " + numeroAffilie);
            throw new WebAvsException("Impossible de récupérer l'affilié : " + numeroAffilie);
        }

        AFContactFPV entity = new AFContactFPV();
        entity.setId(affiliation.getAffiliationId());
        entity.setSession(session);
        try {
            entity.retrieve();
        } catch (Exception e) {
            logger.error("Impossible de récupérer le contact pour le numéro d'affilié : " + numeroAffilie + " \n" + e.getLocalizedMessage());
            throw new WebAvsException("Impossible de récupérer le contact pour le numéro d'affilié : " + numeroAffilie + " \n" + e.getLocalizedMessage(), e);
        }

        entity.setSession(session);
        entity.setAffiliationNumero(numeroAffilie);
        entity.setNom(nom);
        entity.setPrenom(prenom);
        entity.setSexe(getCodeSexe(session,sexe.getSexe()));
        entity.setEmail(email);
        entity.setStopProspection(stopProspection);
        entity.setContactId(affiliation.getAffiliationId());
        try {
            if(entity.isNew()){
                entity.add();
            } else {
                entity.update();
            }
        } catch (Exception e) {
            logger.error("Impossible d'enregistrer le contact pour l'affilié : " + numeroAffilie + " \n" + e.getLocalizedMessage());
            throw new WebAvsException("Impossible d'enregistrer le contact pour l'affilié : " + numeroAffilie + " \n" + e.getLocalizedMessage(), e);
        }
        if (session.hasErrors()) {
            throw new WebAvsException("Impossible d'insérer le contact en raison des erreurs suivantes : " + session.getErrors());
        }

        return true;
    }

    private Contact convertFromEntity(BSession session, AFContactFPV entity) {
        Contact contact = new Contact();
        contact.setNumeroAffilie(entity.getAffiliationNumero());
        contact.setNom(entity.getNom());
        contact.setPrenom(entity.getPrenom());
        contact.setSexe(EnumSexe.valueOf(getLibelleSexe(session,entity.getSexe())));
        contact.setEmail(entity.getEmail());
        contact.setStopProspection(entity.isStopProspection());
        return contact;
    }

    private AFAffiliation getAffiliation(String numeroAffilie, BSession session) {
        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new IllegalArgumentException("numeroAffilie is null or empty");
        }

        if (session == null) {
            throw new IllegalArgumentException("session is null");
        }

        AFAffiliationManager affiliationManager = new AFAffiliationManager();
        affiliationManager.setForAffilieNumero(numeroAffilie);
        affiliationManager.setForActif(Boolean.TRUE);
        affiliationManager.setSession(session);

        try {
            affiliationManager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new AFTechnicalException("Impossible de récupérer les affiliation, numéros : " + numeroAffilie, e);
        }
        return (AFAffiliation) affiliationManager.getFirstEntity();
    }

    private static final String getCodeSexe(BSession session, String sexe) {
        return session.getSystemCode("PYSEXE", sexe);
    }

    private static final String getLibelleSexe(BSession session, String codeSexe) {
        return session.getCode(codeSexe);
    }

}
