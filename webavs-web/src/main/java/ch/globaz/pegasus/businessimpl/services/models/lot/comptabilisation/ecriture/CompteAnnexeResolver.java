package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;

/**
 * 
 * Permet de retrouver facilement des comptesAnnexe. Pour l'utiliser il faut en premier ajouter les comtpesAnnexes avec
 * la fonction addComptesAnnexes, ceci initialisera la class.
 * 
 * Une fois le traitement fini, il est bien de faire un clear() afin vider toutes les instance des comtpteAnnexe.
 * 
 * 
 * @author dma
 * 
 */
public class CompteAnnexeResolver {

    private static CompteAnnexeResolver instance;

    public static void addComptesAnnexes(List<CompteAnnexeSimpleModel> comptesAnnexes) {

        if (comptesAnnexes == null) {
            throw new IllegalArgumentException(
                    "Unable to addCompteAnnexes compteAnnexes the comptesAnnexes passed is null!");
        }

        CompteAnnexeResolver.instance = new CompteAnnexeResolver();
        CompteAnnexeResolver.instance.byIdTiers = CompteAnnexeResolver.instance.groupByIdTiers(comptesAnnexes);
        CompteAnnexeResolver.instance.byIdCompteAnnexe = CompteAnnexeResolver.instance
                .groupByIdCompteAnnexe(comptesAnnexes);

        // CompteAnnexeResolver resolver = new CompteAnnexeResolver();
        // resolver.byIdTiers = resolver.groupByIdTiers(comptesAnnexes);
        // resolver.byIdCompteAnnexe = resolver.groupByIdCompteAnnexe(comptesAnnexes);
        //
        // JadeThread.storeTemporaryObject(CompteAnnexeResolver.class.getName(), resolver);
    }

    private static void checkParam(String idTiers, String libelleId) throws ComptabiliserLotException {

        if (JadeStringUtil.isBlankOrZero(idTiers)) {
            throw new IllegalArgumentException("Unable to resolveByIdTiers , the " + libelleId + " is null or empty");
        }

        if ((CompteAnnexeResolver.instance == null) || (CompteAnnexeResolver.instance.byIdTiers == null)) {
            throw new ComptabiliserLotException(
                    "You must add all the compteAnnexe before to resolved. Please add the comptesAnnexe whit this funciton: addCompteAnnexes ");
        }
    }

    public static void clear() {
        // JadeThread.storeTemporaryObject(CompteAnnexeResolver.class.getName(), null);
        CompteAnnexeResolver.instance = null;
    }

    // private static CompteAnnexeResolver getInstance {
    // return (CompteAnnexeResolver) JadeThread.getTemporaryObject(CompteAnnexeResolver.class.getName());
    // }

    /**
     * 
     * @param idCompteAnnexe
     * @return le compteAnnexes
     * @throws ComptabiliserLotException
     *             , Une exception est lancé si aucun compteAnnexe n'a été trouvé.
     */
    public static CompteAnnexeSimpleModel resolveByIdCompteAnnexe(String idCompteAnnexe)
            throws ComptabiliserLotException {

        CompteAnnexeResolver.checkParam(idCompteAnnexe, "idCompteAnnexe");

        if (CompteAnnexeResolver.instance.byIdCompteAnnexe.containsKey(idCompteAnnexe)) {
            return CompteAnnexeResolver.instance.byIdCompteAnnexe.get(idCompteAnnexe);
        }
        throw new ComptabiliserLotException("Any comptesAnnexe founded for this idCompteAnnexe :" + idCompteAnnexe);
    }

    /**
     * @param idTiers
     * @param csRole
     * @return le compteAnnexes
     * @throws ComptabiliserLotException
     *             , Une exception est lancé si aucun compteAnnexe n'a été trouvé.
     */
    public static CompteAnnexeSimpleModel resolveByIdTiers(String idTiers, String idRole)
            throws ComptabiliserLotException {

        CompteAnnexeResolver.checkParam(idTiers, "idTiers");
        CompteAnnexeResolver.checkParam(idRole, "idRole");

        List<CompteAnnexeSimpleModel> list = CompteAnnexeResolver.instance.byIdTiers.get(idTiers);
        if (list == null) {
            throw new ComptabiliserLotException("Any comptesAnnexe founded for this idTiers :" + idTiers);
        }

        Map<String, List<CompteAnnexeSimpleModel>> mapByRole = CompteAnnexeResolver.instance.groupByRole(list);
        list = mapByRole.get(idRole);

        if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new ComptabiliserLotException("Too many comptesAnnexe founded for this idTiers " + idTiers
                    + " and with this idRole " + idRole);
        }

    }

    private Map<String, CompteAnnexeSimpleModel> byIdCompteAnnexe;
    private Map<String, List<CompteAnnexeSimpleModel>> byIdTiers;

    private Map<String, CompteAnnexeSimpleModel> groupByIdCompteAnnexe(List<CompteAnnexeSimpleModel> comptesAnnexes) {
        Map<String, CompteAnnexeSimpleModel> map = new HashMap<String, CompteAnnexeSimpleModel>();
        for (CompteAnnexeSimpleModel compteAnnexe : comptesAnnexes) {
            map.put(compteAnnexe.getIdCompteAnnexe(), compteAnnexe);
        }
        return map;
    }

    private Map<String, List<CompteAnnexeSimpleModel>> groupByIdTiers(List<CompteAnnexeSimpleModel> compteAnnexes) {
        return JadeListUtil.groupBy(compteAnnexes, new JadeListUtil.Key<CompteAnnexeSimpleModel>() {
            @Override
            public String exec(CompteAnnexeSimpleModel e) {
                return e.getIdTiers();
            }
        });
    }

    private Map<String, List<CompteAnnexeSimpleModel>> groupByRole(List<CompteAnnexeSimpleModel> compteAnnexes) {
        return JadeListUtil.groupBy(compteAnnexes, new JadeListUtil.Key<CompteAnnexeSimpleModel>() {
            @Override
            public String exec(CompteAnnexeSimpleModel e) {
                return e.getIdRole();
            }
        });
    }

}
