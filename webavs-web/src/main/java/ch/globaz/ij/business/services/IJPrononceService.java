package ch.globaz.ij.business.services;

import globaz.globall.db.BSession;
import globaz.ij.db.prononces.IJSituationProfessionnelle;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.ij.business.models.IJPrononceJointDemande;
import ch.globaz.ij.business.models.IJPrononceJointDemandeSearchModel;

import java.util.List;

public interface IJPrononceService extends JadeCrudService<IJPrononceJointDemande, IJPrononceJointDemandeSearchModel> {

   public String rechercheCantonAdressePaiementSitProf(BSession session, String domaine, List<IJSituationProfessionnelle> situationsProf, String dateDebut)
           throws Exception;

}
