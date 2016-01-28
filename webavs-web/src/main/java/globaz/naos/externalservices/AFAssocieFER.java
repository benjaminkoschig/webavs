package globaz.naos.externalservices;

import globaz.globall.db.BAbstractEntityExternalService;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;

/**
 * @author dgi
 * 
 *         Recherche du numéro d'affilié correcte pour un associé/commanditaire<br>
 *         Sur la base du numéro saisi par l'utilisateur (no d'affilié de l'entreprise), le système recherche le
 *         prochain numéro valide en ajoutant au numéro l'extension -01 à -99, en fonction du nombre
 *         d'associés/commanditaires déjà présents.
 */
public class AFAssocieFER extends BAbstractEntityExternalService {
    @Override
    public void afterAdd(BEntity entity) throws Throwable {
    }

    @Override
    public void afterDelete(BEntity entity) throws Throwable {
    }

    @Override
    public void afterRetrieve(BEntity entity) throws Throwable {
    }

    @Override
    public void afterUpdate(BEntity entity) throws Throwable {
    }

    @Override
    public void beforeAdd(BEntity entity) throws Throwable {
        // recherche de toutes les affiliations existantes avec le no donné
        if ((entity != null) && (entity instanceof AFAffiliation)) {
            AFAffiliation aff = (AFAffiliation) entity;
            // à effectuer seulement si associé ou commanditaire
            if (!JadeStringUtil.isBlankOrZero(aff.getTypeAssocie())) {
                String like = aff.getAffilieNumero();
                // formatage: si -00 saisi, supprimer extension
                int pos = like.indexOf('-');
                if (pos != -1) {
                    // supression extension
                    like = like.substring(0, pos);
                }
                AFAffiliationManager mgr = new AFAffiliationManager();
                mgr.setSession(entity.getSession());
                mgr.setLikeAffilieNumero(like);
                mgr.find();
                if (mgr.size() == 0) {
                    // aucune affiliation trouvée
                    throw new Exception("L'affilié " + like + " n'existe pas.");
                }
                boolean parentFound = false;
                int associe = 0;
                for (int i = 0; i < mgr.size(); i++) {
                    String noAff = ((AFAffiliation) mgr.getEntity(i)).getAffilieNumero();
                    try {
                        int extension = Integer.parseInt(noAff.substring(noAff.length() - 2, noAff.length()));
                        if (extension == 0) {
                            // affiliation parente
                            parentFound = true;
                        } else if (extension > associe) {
                            // maj dernier associé
                            associe = extension;
                        }
                    } catch (Exception ex) {
                        // impossible de lire l'extension -> ignore le cas
                        continue;
                    }
                } // end for
                if (!parentFound) {
                    // l'affiliation parente n'a pas été trouvée
                    throw new Exception("L'affilié " + like + " n'existe pas.");
                }
                // création nouveau numéro
                aff.setAffilieNumero(like + "-" + JadeStringUtil.rightJustifyInteger(String.valueOf(associe + 1), 2));
            }
        }
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
