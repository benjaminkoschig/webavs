package globaz.prestation.acor.acor2020.mapper;

import acor.rentes.xsd.common.OrganisationAdresseType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.DemandeType;
import globaz.globall.db.BSession;
import globaz.webavs.common.CommonProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
@AllArgsConstructor
public class PRAcorDemandeTypeMapper {

    private final BSession session;

    public DemandeType map() {
        DemandeType demandeType = new DemandeType();

        // NOUVELLES DONNES XSD OBLIGATOIRES
        demandeType.setCaisseAgence(getCaisseAgence(session));
        //TODO adresse de la caisse par des propriétés applications à créer
        demandeType.setAdresseCaisse(createAdresseCaisse());
        demandeType.setMoisRapport(0);
        demandeType.setLangue(StringUtils.lowerCase(session.getIdLangue()));
        return demandeType;
    }

    private OrganisationAdresseType createAdresseCaisse() {
        OrganisationAdresseType adresseCaisse = new OrganisationAdresseType();
        //TODO
        adresseCaisse.setAdresse("test");
        adresseCaisse.setLocalite("test");
        adresseCaisse.setCodePostal("2300");
        adresseCaisse.setPays(100);
        adresseCaisse.setNom("");
        return adresseCaisse;
    }

    private Integer getCaisseAgence(BSession session) {
        Integer caisseAgence = 0;
        try {
            String noCaisse = session.getApplication().getProperty(CommonProperties.KEY_NO_CAISSE);
            String noAgence = session.getApplication().getProperty(CommonProperties.KEY_NO_AGENCE);
            caisseAgence = Integer.valueOf(noCaisse + noAgence);
        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération du numéro de la caisse.", e);
        }
        return caisseAgence;
    }
}
