package ch.globaz.pegasus.businessimpl.services.loader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Montant;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.common.sql.converters.DateConverter;
import ch.globaz.pegasus.business.domaine.parametre.Parameters;
import ch.globaz.pegasus.business.domaine.parametre.forfaitPrime.ForfaitsPrimeAssuranceMaladie;
import ch.globaz.pegasus.business.domaine.parametre.home.HomeCategoriArgentPoche;
import ch.globaz.pegasus.business.domaine.parametre.home.HomeCategorie;
import ch.globaz.pegasus.business.domaine.parametre.home.ServiceEtat;
import ch.globaz.pegasus.business.domaine.parametre.home.TypeChambrePrix;
import ch.globaz.pegasus.business.domaine.parametre.home.TypesChambrePrix;
import ch.globaz.pegasus.business.domaine.parametre.monnaieEtrangere.MonnaiesEtrangere;
import ch.globaz.pegasus.business.domaine.parametre.variableMetier.VariablesMetier;
import ch.globaz.pegasus.rpc.businessImpl.RpcTechnicalException;
import ch.globaz.queryexc.converters.Mapper;
import ch.globaz.queryexec.bridge.jade.SCM;

public class ParametersLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ParametersLoader.class);

    private Set<String> idsTypeChambreHome;
    private Set<String> idsLocalite;

    public ParametersLoader() {
    }

    public ParametersLoader(Set<String> idsTypeChambreHome, Set<String> idsLocalite) {
        this.idsTypeChambreHome = idsTypeChambreHome;
        this.idsLocalite = idsLocalite;
    }

    public Parameters load() {

        VariableMetierLoader variableMetierLoader = new VariableMetierLoader();
        VariablesMetier variablesMetier = variableMetierLoader.load();
        LOG.info("Varibles métier loaded nb: {}", variablesMetier.size());

        MonnaieEtrangereLoader etrangereLoader = new MonnaieEtrangereLoader();
        MonnaiesEtrangere monnaiesEtrangere = etrangereLoader.load();
        LOG.info("Monaies étrangère loaded nb: {}", monnaiesEtrangere.size());

        ForfaitsPrimesAssuranceMaladieLoader forfaitsPrimesAssurancLoader = new ForfaitsPrimesAssuranceMaladieLoader(
                idsLocalite);
        ForfaitsPrimeAssuranceMaladie forfaitsPrimesAssuranceMaladie = forfaitsPrimesAssurancLoader.load();
        LOG.info("Forfait primes assurances loaded nb: {}", forfaitsPrimesAssuranceMaladie.nbForfait());

        TypesChambrePrix typesChambrePrix = loadTypesChambrePrix();
        LOG.info("Types chambres prix loaded nb: {}", typesChambrePrix.size());

        return new Parameters(variablesMetier, monnaiesEtrangere, forfaitsPrimesAssuranceMaladie, typesChambrePrix);

    }

    private TypesChambrePrix loadTypesChambrePrix() {
        TypesChambrePrix typesChambrePrix = new TypesChambrePrix();
        if (idsTypeChambreHome == null || !idsTypeChambreHome.isEmpty()) {
            SQLWriter writer = SQLWriter
                    .write()
                    .append("SELECT BMITYP as id,  BMIHOM as id_home, BMTCAT as cs_categorie, BMBAFA as api_facturee, BMTCAA as cs_categorie_argent_poche,"
                            + " BNMPJO as prix, BNDDEB as date_debut, BNDFIN as date_fin, schema.PCPERSET.BPTSET as csServiceEtat"
                            + " FROM schema.PCTYCHAM inner join schema.PCPRCHAM on schema.PCPRCHAM.BNITYP = schema.PCTYCHAM.BMITYP"
                            + " inner join schema.PCPERSET on schema.PCPERSET.BPIHOM = schema.PCTYCHAM.BMIHOM "
                            + " and schema.PCPRCHAM.BNDDEB >= schema.PCPERSET.BPDDEB "
                            + " and (schema.PCPRCHAM.BNDDEB <= schema.PCPERSET.BPDFIN or (schema.PCPERSET.BPDFIN is null or schema.PCPERSET.BPDFIN = 0 ) )");

            if (idsTypeChambreHome != null) {
                writer.where("schema.PCTYCHAM.BMITYP");
                writer.in(idsTypeChambreHome);
            }

            final DateConverter dateConverter = new DateConverter();
            List<TypeChambrePrix> list = SCM.newInstance(TypeChambrePrix.class).query(writer.toSql())
                    .mapper(new Mapper<TypeChambrePrix>() {

                        @Override
                        public TypeChambrePrix map(ResultSet resultSetm, int index) {
                            try {
                                TypeChambrePrix typeChambrePrix = new TypeChambrePrix();
                                typeChambrePrix.setId(resultSetm.getString("id"));
                                typeChambrePrix.setIdHome(resultSetm.getString("id_home"));
                                typeChambrePrix.setCategorie(HomeCategorie.fromValue(resultSetm
                                        .getString("cs_categorie")));
                                typeChambrePrix.setCategorieArgentPoche(HomeCategoriArgentPoche.fromValue(resultSetm
                                        .getString("cs_categorie_argent_poche")));

                                typeChambrePrix.setApiFacturee("1".equals(resultSetm.getString("api_facturee")));
                                typeChambrePrix.setPrix(Montant.newJouranlier(resultSetm.getString("prix")));
                                typeChambrePrix.setDateDebut(dateConverter.convert(resultSetm.getString("date_debut"),
                                        null, null));
                                typeChambrePrix.setDateFin(dateConverter.convert(resultSetm.getString("date_fin"),
                                        null, null));
                                typeChambrePrix.setServiceEtat(ServiceEtat.fromValue(resultSetm
                                        .getString("csServiceEtat")));
                                return typeChambrePrix;
                            } catch (SQLException e) {
                                throw new RpcTechnicalException("Unable to load the prixChambre", e);
                            }
                        }
                    }).execute();

            for (TypeChambrePrix typeChambrePrix : list) {
                typesChambrePrix.add(typeChambrePrix);
            }
        }
        return typesChambrePrix;

    }
}
