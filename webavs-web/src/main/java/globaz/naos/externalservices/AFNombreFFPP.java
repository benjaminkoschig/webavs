package globaz.naos.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.nombreAssures.AFNombreAssures;
import globaz.naos.db.nombreAssures.AFNombreAssuresManager;
import globaz.naos.translation.CodeSystem;
import java.util.ArrayList;

/**
 * @author dgi
 * 
 */
public class AFNombreFFPP extends BAbstractEntityExternalService {
    @Override
    public void afterAdd(BEntity entity) throws Throwable {
        // ajouter l'enregistrement correspondant avec un nombre négatif et
        // l'assurance correspondante si l'affilié est pris en charge

        if ((entity != null) && (entity instanceof AFNombreAssures)) {
            AFNombreAssures nbr = (AFNombreAssures) entity;
            if (nbr.isCallExternal()) {
                ArrayList idAssurances = new ArrayList();
                idAssurances.add(nbr.getAssuranceId());
                if (CodeSystem.TYPE_ASS_FFPP.equals(nbr.getAssurance().getTypeAssurance())) {
                    // type FFPP
                    // rechercher toutes les coti FFPP de l'affilié
                    AFCotisationManager mgr = new AFCotisationManager();
                    mgr.setSession(nbr.getSession());
                    mgr.setForAffiliationId(nbr.getAffiliationId());
                    mgr.setForTypeAssurance(CodeSystem.TYPE_ASS_FFPP);
                    mgr.find();

                    for (int i = 0; i < mgr.size(); i++) {
                        AFCotisation coti = (AFCotisation) mgr.getEntity(i);
                        // System.out.println(" -> "+coti.getAssurance().getAssuranceLibelle());
                        if (!idAssurances.contains(coti.getAssuranceId())
                                && coti.getAssurance().getAssuranceCanton()
                                        .equals(nbr.getAssurance().getAssuranceCanton())) {
                            // si assurance pas encore traité et pour même
                            // canton, l'adapter
                            AFNombreAssures newNbr = new AFNombreAssures();
                            newNbr.setSession(nbr.getSession());
                            newNbr.setAffiliationId(nbr.getAffiliationId());
                            newNbr.setAnnee(nbr.getAnnee());
                            newNbr.setAssuranceId(coti.getAssuranceId());
                            newNbr.setNbrAssures(nbr.getNbrAssures());
                            newNbr.setNoCallExternal(true);
                            newNbr.add();
                            // System.out.println("Add "+coti.getAssurance().getAssuranceLibelle());
                            idAssurances.add(coti.getAssuranceId());
                        }
                    }
                }
            }
        }

    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
        // effacer l'enegistrement contenant le nombre négatif si l'affilié est
        // pris en charge
        if ((entity != null) && (entity instanceof AFNombreAssures)) {
            AFNombreAssures nbr = (AFNombreAssures) entity;
            if (nbr.isCallExternal()) {
                if (CodeSystem.TYPE_ASS_FFPP.equals(nbr.getAssurance().getTypeAssurance())) {
                    // type FFPP
                    // rechercher toutes les coti FFPP de l'affilié
                    AFNombreAssuresManager mgr = new AFNombreAssuresManager();
                    mgr.setSession(nbr.getSession());
                    mgr.setForAffiliationId(nbr.getAffiliationId());
                    mgr.setForAnnee(nbr.getAnnee());
                    mgr.find();
                    for (int i = 0; i < mgr.size(); i++) {
                        AFNombreAssures ffpp = (AFNombreAssures) mgr.getEntity(i);
                        if (!ffpp.getAssuranceId().equals(nbr.getAssuranceId())
                                && ffpp.getAssurance().getTypeAssurance().equals(nbr.getAssurance().getTypeAssurance())
                                && ffpp.getAssurance().getAssuranceCanton()
                                        .equals(nbr.getAssurance().getAssuranceCanton())) {
                            ffpp.setNoCallExternal(true);
                            ffpp.delete();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
        // modifier l'enregistrement contenant le nombre négatif si l'affilié
        // est pris en charge
        if ((entity != null) && (entity instanceof AFNombreAssures)) {
            AFNombreAssures nbr = (AFNombreAssures) entity;
            if (nbr.isCallExternal()) {
                if (CodeSystem.TYPE_ASS_FFPP.equals(nbr.getAssurance().getTypeAssurance())) {
                    // type FFPP
                    // rechercher toutes les coti FFPP de l'affilié
                    AFNombreAssuresManager mgr = new AFNombreAssuresManager();
                    mgr.setSession(nbr.getSession());
                    mgr.setForAffiliationId(nbr.getAffiliationId());
                    mgr.setForAnnee(nbr.getAnnee());
                    mgr.find();
                    for (int i = 0; i < mgr.size(); i++) {
                        AFNombreAssures ffpp = (AFNombreAssures) mgr.getEntity(i);
                        if (!ffpp.getAssuranceId().equals(nbr.getAssuranceId())
                                && ffpp.getAssurance().getTypeAssurance().equals(nbr.getAssurance().getTypeAssurance())
                                && ffpp.getAssurance().getAssuranceCanton()
                                        .equals(nbr.getAssurance().getAssuranceCanton())) {
                            ffpp.setNbrAssures(nbr.getNbrAssures());
                            ffpp.setNoCallExternal(true);
                            ffpp.update();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeUpdate(BEntity entity) throws Throwable {
    }

    @Override
    public void init(BEntity entity) throws Throwable {
    }

    @Override
    public void validate(BEntity entity) throws Throwable {
    }
}
