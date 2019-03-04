package ch.globaz.al.utils;

import java.util.ArrayList;
import java.util.Map;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALConstEcheances;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class ALEcheanceUtils {
    
    public static String getLibellePays(String idPays, String idLangueISO) throws AnnonceException {
        String libellePays = "";

        PaysSearchSimpleModel pssmReq = new PaysSearchSimpleModel();
        pssmReq.setForIdPays(idPays);
        try {
            PaysSearchSimpleModel search = TIBusinessServiceLocator.getAdresseService().findPays(pssmReq);

            if (search.getSize() > 0) {
                PaysSimpleModel paysSimpleModel = (PaysSimpleModel) search.getSearchResults()[0];
                if (Langues.Francais.getCodeIso().equals(idLangueISO)) {
                    libellePays = paysSimpleModel.getLibelleFr();
                } else if (Langues.Allemand.getCodeIso().equals(idLangueISO)) {
                    libellePays = paysSimpleModel.getLibelleAl();
                } else {
                    libellePays = paysSimpleModel.getLibelleIt();
                }
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AnnonceException("JadeApplicationServiceNotAvailableException", e);
        } catch (JadePersistenceException e) {
            throw new AnnonceException("JadePersistenceException", e);
        } catch (JadeApplicationException e) {
            throw new AnnonceException("JadeApplicationException", e);
        }
        return libellePays;
    }
    
    
    public static void createMap(Map<String, ArrayList<DroitEcheanceComplexModel>> map, DroitEcheanceComplexModel droit,
            String key) {
        if (map.containsKey(key)) {
            ArrayList<DroitEcheanceComplexModel> list = map.get(key);
            list.add(droit);
        } else  {
            ArrayList<DroitEcheanceComplexModel> list = new ArrayList<>();
            list.add(droit);
            map.put(key, list);
        }
    }
    
    public static void changeMotif(ArrayList<DroitEcheanceComplexModel> listeDroits) {
        String motifE411 = JadePropertiesService.getInstance().getProperty(ALConstEcheances.MOTIF_E411);
        if("true".equals(motifE411)) {
            for(DroitEcheanceComplexModel droit : listeDroits) {
                if("212".equals(droit.getIdPaysResidence())) {
                    droit.getDroitModel().setMotifFin(ALCSDroit.MOTIF_FIN_E411);
                }
            }
        }
    }
    
    public static boolean isMotifMotifE411(DroitEcheanceComplexModel droit) {
        String motifE411 = JadePropertiesService.getInstance().getProperty(ALConstEcheances.MOTIF_E411);
        return "true".equals(motifE411) && "212".equals(droit.getIdPaysResidence());
    }

}
