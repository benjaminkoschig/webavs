package globaz.corvus.acor.adapter.plat;

import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import globaz.prestation.acor.plat.PRFichierDemandeDefautPrinter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.hera.business.constantes.ISFMembreFamille;
import ch.globaz.hera.business.constantes.ISFRelationConjoint;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Classe permettant l'écriture des fichiers de demandes de rente ACOR.
 * </p>
 * 
 * @author scr
 */
public class REFichierDemandePrinter extends PRFichierDemandeDefautPrinter {

    private static class DateDeNaissanceComparator implements Comparator<ISFMembreFamilleRequerant> {

        @Override
        public int compare(ISFMembreFamilleRequerant o1, ISFMembreFamilleRequerant o2) {
            String ddn1 = o1.getDateNaissance();
            String ddn2 = o2.getDateNaissance();

            // 2 dates vides
            if (JadeStringUtil.isBlankOrZero(ddn1) && JadeStringUtil.isBlankOrZero(ddn2)) {
                return 0;
            }

            if (JadeStringUtil.isBlankOrZero(ddn1)) {
                return -1;
            }

            if (JadeStringUtil.isBlankOrZero(ddn2)) {
                return 1;
            }

            SimpleDateFormat reader = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat writer = new SimpleDateFormat("yyyyMMdd");

            int d1 = 0;
            int d2 = 0;
            try {
                d1 = Integer.valueOf(writer.format(reader.parse(ddn1)));
                d2 = Integer.valueOf(writer.format(reader.parse(ddn2)));
            } catch (Exception e) {
                // What can I do :(
            }

            if (d1 > d2) {
                return -1;
            } else if (d2 < d1) {
                return -1;
            } else {
                return 0;
            }
        }

    }

    public REFichierDemandePrinter(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }


    /**
     * Analyse les relations avec les conjoints du requérant et retourne <code>true</code> s'il ne possède que des
     * relations de types enfants communs
     * 
     * @param membreFamille
     * @param idTiersRequerant
     * @return
     * @throws Exception
     */
    protected boolean hasUniquementRelationEnfantCommun(ISFMembreFamilleRequerant[] membreFamille,
            String idTiersRequerant) throws Exception {

        boolean result = false;
        String idMembreFamilleRequerant = null;
        // On récupère tous les conjoints
        List<ISFMembreFamilleRequerant> conjoints = new ArrayList<ISFMembreFamilleRequerant>();
        for (ISFMembreFamilleRequerant tiers : membreFamille) {
            // On ne veut pas traiter le tiers requérant
            if (idTiersRequerant.equals(tiers.getIdTiers())) {
                idMembreFamilleRequerant = tiers.getIdMembreFamille();
                continue;
            }
            if (ISFMembreFamille.CS_TYPE_RELATION_CONJOINT.equals(tiers.getRelationAuRequerant())) {
                conjoints.add(tiers);
            }
        }

        // On poursuit uniquement si on à trouver l'idMembreFamilleRequerant et que la liste des conjoints n'est pas
        // vide
        if (!conjoints.isEmpty() && !JadeStringUtil.isEmpty(idMembreFamilleRequerant)) {

            // On recherche la sit famille du tiers requérant
            globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(getSession(),
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);

            // On récupère les relations au tiers pour chaque conjoint
            for (ISFMembreFamilleRequerant conjoint : conjoints) {
                ISFRelationFamiliale[] relations = sf.getToutesRelationsConjoints(idMembreFamilleRequerant,
                        conjoint.getIdMembreFamille(), false);
                for (ISFRelationFamiliale relation : relations) {
                    // On anaylse ses relations par rapport au tiers requerant
                    if (ISFRelationConjoint.CS_REL_CONJ_ENFANT_COMMUN.equals(relation.getTypeRelation())) {
                        result = true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Recherche les membres de la famille du tiers requérant
     * 
     * @param idTiersRequerant
     * @return
     * @throws Exception
     */
    protected ISFMembreFamilleRequerant[] getToutesLesMembresFamilles(String idTiersRequerant) throws Exception {
        Map<ISFMembreFamilleRequerant, ISFRelationFamiliale[]> relations = new HashMap<ISFMembreFamilleRequerant, ISFRelationFamiliale[]>();

        // On recherche la sit famille du tiers requérant
        globaz.hera.api.ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(getSession(),
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);

        // On récupère tous les membres de la famille
        ISFMembreFamilleRequerant[] membresFamille = sf.getMembresFamilleRequerant(idTiersRequerant);
        return membresFamille;
    }

    private String recupererNSSEnfantPlusJeune(ISFMembreFamilleRequerant[] membresFamille, String idTiersRequerant)
            throws Exception {

        String nss = null;
        // On récupère tous les enfants
        List<ISFMembreFamilleRequerant> enfants = new ArrayList<ISFMembreFamilleRequerant>();
        for (ISFMembreFamilleRequerant tiers : membresFamille) {
            // On ne veut pas traiter le tiers requérant
            if (idTiersRequerant.equals(tiers.getIdTiers())) {
                continue;
            }
            if (ISFMembreFamille.CS_TYPE_RELATION_ENFANT.equals(tiers.getRelationAuRequerant())) {
                enfants.add(tiers);
            }
        }

        if (!enfants.isEmpty()) {
            Collections.sort(enfants, new DateDeNaissanceComparator());
            nss = enfants.get(0).getNss();
        }
        return nss;
    }

    private String recupererNSSConjoint(ISFMembreFamilleRequerant[] membresFamille, String idTiersRequerant)
            throws Exception {

        String nssConjoint = null;
        List<ISFMembreFamilleRequerant> conjoints = new ArrayList<ISFMembreFamilleRequerant>();

        for (ISFMembreFamilleRequerant tiers : membresFamille) {
            // On ne veut pas traiter le tiers requérant
            if (idTiersRequerant.equals(tiers.getIdTiers())) {
                continue;
            }
            if (ISFMembreFamille.CS_TYPE_RELATION_CONJOINT.equals(tiers.getRelationAuRequerant())) {
                conjoints.add(tiers);
            }
        }

        if (!conjoints.isEmpty()) {
            // Dans tous les cas on prend le premier conjoint
            nssConjoint = conjoints.get(0).getNss();
        }

        if (!JadeStringUtil.isEmpty(nssConjoint)) {
            nssConjoint = null;
        }
        return nssConjoint;
    }

    @Override
    public void printLigne(StringBuffer writer) throws PRACORException {

        // Récupération original du NSS
        String nssDemande = ((REACORDemandeAdapter) parent).nssDemande();

        // Traitement particulier que les demandes de survivants
        if (((REACORDemandeAdapter) parent).isDemandeSurvivant()) {
            try {
                String idTiersRequerant = parent.idTiersAssure();
                // Gestion de cas spéciaux pour ACOR
                ISFMembreFamilleRequerant[] membreFamille = getToutesLesMembresFamilles(idTiersRequerant);
                if (hasUniquementRelationEnfantCommun(membreFamille, idTiersRequerant)) {
                    // On prend le NSS su conjoint si il existe
                    nssDemande = recupererNSSConjoint(membreFamille, idTiersRequerant);
                    // Sinon on prend le NSS de l'enfant le plus jeune
                    if (JadeStringUtil.isBlankOrZero(nssDemande)) {
                        nssDemande = recupererNSSEnfantPlusJeune(membreFamille, idTiersRequerant);
                    }
                }

            } catch (Exception e) {
                throw new PRACORException(
                        "Exception lancée lors de l'analyse des relations conjoins du tiers requérant : "
                                + e.toString(), e);
            }
        }

        if (JadeStringUtil.isBlankOrZero(nssDemande)) {
            throw new PRACORException("Exception lancée lors de la recherche du NSS pour le fichier demande");
        }
        // 1. le no AVS de l'assuré faisant la demande de prestation
        writeAVS(writer, nssDemande);

        // 2. le type de demande
        writeChaine(writer, parent.getTypeDemande());

        // 3. la date de traitement
        writeDate(writer, parent.getDateTraitement());

        // 4. la date de depot de la demande
        writeDate(writer, parent.getDateDepot());

        // 5. le type de calcul
        writeChaineSansFinDeChamp(writer, parent.getTypeCalcul());
        hasLignes = false;

    }

}
