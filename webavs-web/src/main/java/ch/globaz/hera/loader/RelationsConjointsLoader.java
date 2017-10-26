package ch.globaz.hera.loader;

import globaz.hera.api.ISFSituationFamiliale;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.hera.domaine.relationconjoint.RelationConjoint;
import ch.globaz.hera.domaine.relationconjoint.RelationsConjoints;
import ch.globaz.hera.domaine.relationconjoint.TypeRelation;
import ch.globaz.pyxis.domaine.NumeroSecuriteSociale;
import ch.globaz.pyxis.domaine.PersonneAVS;
import ch.globaz.pyxis.domaine.Sexe;
import ch.globaz.queryexc.converters.Mapper;
import ch.globaz.queryexec.bridge.jade.SCM;

public class RelationsConjointsLoader {
    private static final Logger LOG = LoggerFactory.getLogger(RelationsConjointsLoader.class);
    private List<RelationConjoint> list;

    public static RelationsConjointsLoader build() {
        return new RelationsConjointsLoader();
    }

    public RelationsConjointsLoader load(Collection<String> idsTiers) {
        if (idsTiers == null || idsTiers.isEmpty()) {
            throw new RuntimeException("Unable to load the relation conjoint with a empty list of idsTiers");
        }
        SQLWriter sql = SQLWriter
                .write()
                .append("select SCHEMA.SFRELCON.WKTTYP AS RELATION_TYPE, SCHEMA.SFRELCON.WKDDEB AS DATE_DEBUT, SCHEMA.SFRELCON.WKDFIN AS DATE_FIN,  TITIERP_REQ.HTITIE AS REQ_ID_TIERS, "
                        + "TIPAVSP_REQ.HXNAVS AS REQ_NSS, TITIERP_REQ.HTLDE1 AS REQ_NOM, TITIERP_REQ.HTLDE2 AS REQ_PRENOM, TIPERSP_REQ.HPDNAI AS REQ_DATE_NAISS, TIPERSP_REQ.HPDDEC AS REQ_DATE_DECE, "
                        + "TIPERSP_REQ.HPTSEX AS REQ_CS_SEX, TITIERP_CONJ.HTITIE AS CONJ_ID_TIERS, TIPAVSP_CONJ.HXNAVS AS CONJ_NSS, TITIERP_CONJ.HTLDE1 AS CONJ_NOM, TITIERP_CONJ.HTLDE2 AS CONJ_PRENOM, "
                        + "TIPERSP_CONJ.HPDNAI AS CONJ_DATE_NAISS, TIPERSP_CONJ.HPDDEC AS CONJ_DATE_DECE, TIPERSP_CONJ.HPTSEX AS CONJ_CS_SEX "
                        + "from SCHEMA.SFREQUER inner join SCHEMA.SFMBRFAM as SFMBRFAM_REQ on SFMBRFAM_REQ.WGIMEF = SCHEMA.SFREQUER.WDIMEF "
                        + "inner join SCHEMA.TITIERP as TITIERP_REQ on TITIERP_REQ.HTITIE = SFMBRFAM_REQ.WGITIE "
                        + "inner join SCHEMA.TIPERSP as TIPERSP_REQ on TIPERSP_REQ.HTITIE = TITIERP_REQ.HTITIE "
                        + "inner join SCHEMA.TIPAVSP as TIPAVSP_REQ on TIPAVSP_REQ.HTITIE = TITIERP_REQ.HTITIE "
                        + "left join SCHEMA.SFCONJOI on (SCHEMA.SFCONJOI.WJICO1 = SFMBRFAM_REQ.WGIMEF or SCHEMA.SFCONJOI.WJICO2 = SFMBRFAM_REQ.WGIMEF ) "
                        + "left join SCHEMA.SFRELCON on SCHEMA.SFRELCON.WKICON = SCHEMA.SFCONJOI.WJICON "
                        + "left join SCHEMA.SFMBRFAM as SFMBRFAM_CONJ  "
                        + " on (SFMBRFAM_CONJ.WGIMEF = SCHEMA.SFCONJOI.WJICO1 or SFMBRFAM_CONJ.WGIMEF = SCHEMA.SFCONJOI.WJICO2 ) and SFMBRFAM_CONJ.WGIMEF <> SFMBRFAM_REQ.WGIMEF "
                        + "left join SCHEMA.TITIERP as TITIERP_CONJ on TITIERP_CONJ.HTITIE = SFMBRFAM_CONJ.WGITIE left join SCHEMA.TIPERSP as TIPERSP_CONJ on TIPERSP_CONJ.HTITIE = TITIERP_CONJ.HTITIE "
                        + "left join SCHEMA.TIPAVSP as TIPAVSP_CONJ on TIPAVSP_CONJ.HTITIE = TITIERP_CONJ.HTITIE")
                .where("TITIERP_REQ.HTITIE")
                .in(idsTiers)
                .and("(TITIERP_REQ.HTITIE <> 0 and TITIERP_CONJ.HTITIE is null or TITIERP_CONJ.HTITIE <> 0)")
                .and("(SCHEMA.SFRELCON.WKTTYP")
                .append("not in (" + ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE + ","
                        + ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN + ") or SCHEMA.SFRELCON.WKTTYP is null)");
        list = SCM.newInstance(RelationConjoint.class).mapper(buildMapper()).query(sql.toSql()).execute();
        LOG.info("RelationConjoint loaded : {}", list.size());
        return this;
    }

    public RelationsConjoints toListMetiers() {
        RelationsConjoints relationsConjoints = new RelationsConjoints();
        relationsConjoints.addAll(list);
        return relationsConjoints;
    }

    public List<RelationConjoint> toList() {
        return list;
    }

    private Mapper<RelationConjoint> buildMapper() {

        final Map<String, TypeRelation> mapRelation = new HashMap<String, TypeRelation>();
        mapRelation.put(ISFSituationFamiliale.CS_REL_CONJ_RELATION_INDEFINIE, TypeRelation.INDEFINIE);
        mapRelation.put(ISFSituationFamiliale.CS_REL_CONJ_DIVORCE, TypeRelation.DIVORCE);
        mapRelation.put(ISFSituationFamiliale.CS_REL_CONJ_ENFANT_COMMUN, TypeRelation.ENFANT_COMMUN);
        mapRelation.put(ISFSituationFamiliale.CS_REL_CONJ_MARIE, TypeRelation.MARIE);
        mapRelation.put(ISFSituationFamiliale.CS_REL_CONJ_SEPARE_DE_FAIT, TypeRelation.SEPARE_DE_FAIT);
        mapRelation.put(ISFSituationFamiliale.CS_REL_CONJ_SEPARE_JUDICIAIREMENT, TypeRelation.SEPARE_JUDICIAIREMENT);

        return new Mapper<RelationConjoint>() {
            @Override
            public RelationConjoint map(ResultSet rs, int index) {
                try {
                    String dateDebut = rs.getString("DATE_DEBUT");
                    String dateFin = rs.getString("DATE_FIN");
                    String typeRelation = rs.getString("RELATION_TYPE");
                    Date debut = new Date("31.12.1000");
                    Date fin = null;

                    PersonneAVS requerant = buildPersonneAvs(rs, "REQ_");
                    PersonneAVS conjoint = buildPersonneAvs(rs, "CONJ_");

                    if (dateDebut != null && !"0".equals(dateDebut)) {
                        debut = Date.forPartialDate(dateDebut);
                    }
                    if (dateFin != null && !"0".equals(dateFin)) {
                        fin = Date.forPartialDate(dateFin);
                    }

                    TypeRelation relation = TypeRelation.NONE;
                    if (typeRelation != null && !"0".equals(typeRelation)) {
                        relation = mapRelation.get(typeRelation);
                    }
                    return new RelationConjoint(requerant, conjoint, relation, debut, fin);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            private PersonneAVS buildPersonneAvs(ResultSet rs, String alias) throws SQLException {
                // personne.setTitreParLangue(convertTitre(personneEtendue.getTiers()));
                String idTiers = rs.getString(alias + "ID_TIERS");
                if (idTiers != null && !"0".equals(idTiers) && !idTiers.isEmpty()) {
                    PersonneAVS personne = new PersonneAVS();

                    personne.setId(Long.valueOf(idTiers));
                    String dateNaissance = rs.getString(alias + "DATE_NAISS");
                    if (dateNaissance != null && !dateNaissance.isEmpty() && !"0".equals(dateNaissance)) {
                        // FIXME : on fait cette manip car la date de naissance peux être une vrais date (19780000)
                        String day = "00".equals(dateNaissance.substring(6, 8)) ? "01" : dateNaissance.substring(6, 8);
                        String month = "00".equals(dateNaissance.substring(4, 6)) ? "01" : dateNaissance
                                .substring(4, 6);
                        String year = dateNaissance.substring(0, 4);
                        personne.setDateNaissance(day + "." + month + "." + year);
                    }

                    String dateDece = rs.getString(alias + "DATE_DECE");
                    if (dateDece != null && !dateDece.isEmpty() && !"0".equals(dateDece)) {
                        // FIXME : on fait cette manip car la date de décés peux être une vrais date (19780000)
                        String day = "00".equals(dateDece.substring(6, 8)) ? "01" : dateDece.substring(6, 8);
                        String month = "00".equals(dateDece.substring(4, 6)) ? "01" : dateDece.substring(4, 6);
                        String year = dateDece.substring(0, 4);
                        personne.setDateDeces(day + "." + month + "." + year);
                    }
                    String nom = rs.getString(alias + "NOM");
                    String prenom = rs.getString(alias + "PRENOM");

                    if (nom != null) {
                        personne.setNom(nom.trim());
                    }
                    if (prenom != null) {
                        personne.setPrenom(prenom.trim());
                    }

                    String nss = rs.getString(alias + "NSS");
                    if (nss != null && !nss.trim().isEmpty()) {
                        try {
                            personne.setNss(new NumeroSecuriteSociale(nss.trim()));
                        } catch (Exception e) {
                            LOG.warn(e.getMessage());
                        }
                    }

                    personne.setSexe(Sexe.parseAllowEmpyOrZeroValue(rs.getString(alias + "CS_SEX")));
                    // if (pays != null && !pays.isEmpty() && personneEtendue.getTiers().getIdPays() != null
                    // && !"0".equals(personneEtendue.getTiers().getIdPays())) {
                    // personne.setPays(pays.get(personneEtendue.getTiers().getIdPays()));
                    // }
                    return personne;
                }
                return null;
            }
        };
    }
}
