package globaz.musca.db.facturation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.api.IFAPrintManageDoc;
import globaz.musca.application.FAApplication;
import globaz.musca.process.FAPassageRecouvrementLSVProcess;
import globaz.osiris.api.APISection;
import java.io.Serializable;

public class FAEnteteFactureManager extends BManager implements Serializable, IFAPrintManageDoc {

    private static final long serialVersionUID = -5329603203801916336L;
    private String conditionInfoRom336 = new String();
    // active
    private Boolean forEstRentierNa = new Boolean(false);
    private String forIdAdresse = new String();
    private String forIdAdressePaiement = new String();
    private String forIdEntete = new String();
    private String forIdExterneFacture = new String();
    private String forIdExterneRole = new String();
    private String forIdModeRecouvrement = new String();
    private String forIdPassage = new String();
    private String forIdRemarque = new String();
    private String forIdRole = new String();
    private String forIdSoumisInteretsMoratoires = new String();
    private String forIdSousType = new String();

    private String forIdTiers = new String();

    private String forIdTypeFacture = new String();
    private String forNonImprimable = new String();
    private Integer forNonImprimable2 = new Integer(-1); // -1 => forNonImprimable2 = null, 0 => false, 1 => true
    private String forTillIdExterneRole = new String();
    private String forTillTotalFacture = new String();
    private String forTotalFacture = new String();
    private String forTotalFactureBigger = new String();
    private boolean forTotalFactureNotBetween = false;
    private String forTriDecompte = new String();
    private String fromDateFacturation = "";
    private String fromIdExterneFacture = new String();
    private String fromIdExterneRole = new String();
    private String fromNom = new String();
    private String fromNumCommune = new String();
    private String fromTotalFacture = new String();
    private String groupBy = "";
    private String inIdSousType = new String();
    private String inStatusPassage = "";
    private String likeIdExterneFacture = new String();
    private String notInIdSousType = new String();
    private String orderBy = new String();
    private boolean useManagerForCompensation = false;
    private boolean useManagerForCompensationAnnexe = false;
    private boolean useManagerForLSV = false; // Le manager sur le LSV recherche une adresse de paiment avec le un LSV
    private Boolean useManagerWhitPassage = Boolean.FALSE;
    private boolean wantForIdSousTypeTaxeSomDS = false;
    private boolean wantModeRecouvrementAutomatiqueOuDirect = false;
    private Boolean wantOnlyFileEntete = Boolean.FALSE;
    private String forNotModeImpression = "";

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        if (isUseManagerForCompensation()) {
            return _getFieldsForCompensation(statement);
        } else if (isUseManagerForCompensationAnnexe()) {
            return _getFieldsForCompensationAnnexe(statement);
        } else if (isUseManagerForLSV()) {
            return _getFieldsForLSV(statement);
        } else if (getUseManagerWhitPassage()) {
            return _getFieldsWhitPassage(statement);
        } else {
            return FAEnteteFacture.TABLE_FIELDS + ", TITIERP.HTLDE1, TITIERP.HTLDE2, FAENTFP.MODIMP";
        }
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    protected String _getFieldsForCompensation(BStatement statement) {
        return "DISTINCT FAENTFP2.IDENTETEFACTURE, FAENTFP2.NONIMPRIMABLE, FAENTFP2.TOTALFACTURE, FAENTFP2.IDEXTERNEFACTURE, FAENTFP2.IDTYPEFACTURE, FAENTFP2.IDEXTERNEROLE, FAENTFP2.IDTIERS, FAENTFP2.IDSOUSTYPE, TITIERP.HTLDE1, TITIERP.HTLDE2, FAENTFP2.IDROLE, FAENTFP2.MODIMP";
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    protected String _getFieldsForCompensationAnnexe(BStatement statement) {
        return " FAENTFP.IDTIERS, FAENTFP.NONIMPRIMABLE, FAENTFP.IDEXTERNEROLE, FAENTFP.IDROLE, FAENTFP.MODIMP, SUM(FAENTFP.TOTALFACTURE) AS TOTALDECOMPTES";
    }

    private String _getFieldsForLSV(BStatement statement) {
        return FAEnteteFacture.TABLE_FIELDS + ", TITIERP.HTLDE1, TITIERP.HTLDE2,"
                + FAEnteteFacture.TABLE_AVOIR_ADRESSE_PAYEMENT + "." + FAEnteteFacture.FIELD_AAP_ADRESSE_PAYEMENT
                + ", FAENTFP.MODIMP";
    }

    /**
     * Renvoie la liste des champs lorsque le passage est joint
     * 
     * @return la liste des champs
     */
    protected String _getFieldsWhitPassage(BStatement statement) {
        return FAEnteteFacture.TABLE_FIELDS + ", FAPASSP.STATUS";
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String from = new String();
        if (getWantOnlyFileEntete().equals(Boolean.TRUE)) {
            from = _getCollection() + "FAENTFP AS FAENTFP";
        } else if (getUseManagerWhitPassage()) {
            from = _getCollection() + "FAENTFP AS FAENTFP " + "INNER JOIN " + _getCollection()
                    + "FAPASSP AS FAPASSP ON (FAENTFP.IDPASSAGE=FAPASSP.IDPASSAGE) ";
        } else if (isUseManagerForCompensation()) {
            from = _getFromForCompensation(statement);
        } else if (isUseManagerForLSV()) {
            from = _getFromForLSV(statement);
        } else {
            from = _getCollection() + "FAENTFP AS FAENTFP " + "INNER JOIN " + _getCollection()
                    + "TITIERP AS TITIERP ON (FAENTFP.IDTIERS=TITIERP.HTITIE) ";
        }
        return from;
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    protected String _getFromForCompensation(BStatement statement) {

        // traitement du positionnement

        String table1 = "FAENTFP";
        String table12 = "FAENTFP2";
        String table2 = "TITIERP";
        String from = _getCollection() + table1 + " AS " + table1 + " INNER JOIN " + _getCollection() + table2 + " AS "
                + table2 + " ON (" + table1 + ".IDTIERS=" + table2 + ".HTITIE) ";
        from = from.concat(" RIGHT OUTER JOIN " + _getCollection() + table1 + " AS " + table12 + " ON ");

        // la jointure sur idrole a été rajoutée (une même personne pouvant
        // avoir plusieurs rôle)
        from = from.concat(table12 + ".IDEXTERNEROLE = " + table1 + ".IDEXTERNEROLE AND " + table12 + ".IDROLE = "
                + table1 + ".IDROLE AND " + table12 + ".IDPASSAGE = " + table1 + ".IDPASSAGE");
        return from;

    }

    private String _getFromForLSV(BStatement statement) {
        return _getCollection() + "FAENTFP AS FAENTFP " + "INNER JOIN " + _getCollection()
                + "TITIERP AS TITIERP ON (FAENTFP.IDTIERS=TITIERP.HTITIE) " + "INNER JOIN " + _getCollection()
                + FAEnteteFacture.TABLE_AVOIR_ADRESSE_PAYEMENT + " AS " + FAEnteteFacture.TABLE_AVOIR_ADRESSE_PAYEMENT
                + " ON (" + FAEnteteFacture.TABLE_AVOIR_ADRESSE_PAYEMENT + ".HTITIE = TITIERP.HTITIE )";
    }

    @Override
    protected String _getGroupBy(BStatement statement) {
        return groupBy;
    }

    /**
     * retourne la clause ORDER BY de la requete SQL (la table)
     */
    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        if (getOrderBy().length() != 0) {
            if (getOrderBy().equals(FAEnteteFacture.CS_TRI_DEBITEUR)) {
                return "IDEXTERNEROLE ASC";
            } else if (getOrderBy().equals(FAEnteteFacture.CS_TRI_NUMERO_DECOMTPE)) {
                return "IDEXTERNEFACTURE ASC";
            } else if (getOrderBy().equals(FAEnteteFacture.CS_TRI_NOM)) {
                return "HTLDE1 ASC, HTLDE2 ASC";
            } else {
                return getOrderBy();
            }
        } else if (isUseManagerForLSV()) {
            return "IDPASSAGE, IDEXTERNEROLE";
        }
        // Tri par défaut
        return "";
    }

    /**
     * retourne la clause WHERE de la requete SQL
     */
    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        // composant de la requete initialises avec les options par defaut
        String sqlWhere = "";

        // Test effectué lorsqu'il y a la jointure avec le passage
        if (getUseManagerWhitPassage().equals(Boolean.TRUE)) {
            // Pour un etat
            if (getInStatusPassage().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "STATUS IN (" + getInStatusPassage() + ")";
            }
        }

        if (!JadeStringUtil.isEmpty(getConditionInfoRom336())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " " + getConditionInfoRom336() + " ";
        }

        if (!JadeStringUtil.isEmpty(getInIdSousType())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " FAENTFP.IDSOUSTYPE IN(" + getInIdSousType() + ")";
        }

        if (!JadeStringUtil.isEmpty(getNotInIdSousType())) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += " FAENTFP.IDSOUSTYPE NOT IN(" + getNotInIdSousType() + ")";
        }

        if (getForIdEntete().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDENTETEFACTURE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdEntete());
        }

        if (getForIdPassage().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDPASSAGE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdPassage());
        }

        if (getForIdRemarque().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDREMARQUE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRemarque());
        }

        if (getForIdTiers().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDTIERS=" + this._dbWriteNumeric(statement.getTransaction(), getForIdTiers());
        }

        // Traitement pour une sélection du rôle
        if ((!JadeStringUtil.isBlank(getForIdRole())) && !getForIdRole().equals("1000")) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            if (getForIdRole().indexOf(',') != -1) {
                String[] roles = JadeStringUtil.split(getForIdRole(), ',', Integer.MAX_VALUE);

                sqlWhere += "IDROLE IN (";

                for (int idRole = 0; idRole < roles.length; ++idRole) {
                    if (idRole > 0) {
                        sqlWhere += ",";
                    }
                    sqlWhere += this._dbWriteNumeric(statement.getTransaction(), roles[idRole]);
                }

                sqlWhere += ")";
            } else {
                sqlWhere += "IDROLE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdRole());
            }
        }

        if (getForIdExterneRole().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDEXTERNEROLE="
                    + this._dbWriteString(statement.getTransaction(), getForIdExterneRole());
        }

        if (getForIdAdresse().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDADRESSE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdAdresse());
        }

        if (getForIdTypeFacture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDTYPEFACTURE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdTypeFacture());
        }
        if (wantForIdSousTypeTaxeSomDS) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(FAENTFP.IDSOUSTYPE="
                    + this._dbWriteNumeric(statement.getTransaction(),
                            APISection.ID_CATEGORIE_SECTION_DECISION_DE_TAXATION_OFFICE);
            sqlWhere += " OR FAENTFP.IDSOUSTYPE="
                    + this._dbWriteNumeric(statement.getTransaction(), APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL);
            sqlWhere += " OR FAENTFP.IDSOUSTYPE="
                    + this._dbWriteNumeric(statement.getTransaction(), APISection.ID_CATEGORIE_SECTION_LTN);
            sqlWhere += ")";
        }

        if (getForIdSousType().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDSOUSTYPE=" + this._dbWriteNumeric(statement.getTransaction(), getForIdSousType());
        }

        if (getForIdExterneFacture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDEXTERNEFACTURE="
                    + this._dbWriteString(statement.getTransaction(), getForIdExterneFacture());
        }

        if (getForNotModeImpression().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.MODIMP <> "
                    + this._dbWriteNumeric(statement.getTransaction(), getForNotModeImpression());
        }

        if (getLikeIdExterneFacture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDEXTERNEFACTURE like "
                    + this._dbWriteString(statement.getTransaction(), getLikeIdExterneFacture() + "%");
        }

        if (getForIdSoumisInteretsMoratoires().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDSOUMISINTMOR="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdSoumisInteretsMoratoires());
        }

        if (getForIdAdressePaiement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDADRESSEPAIEMENT="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdAdressePaiement());
        }

        if (getForTotalFacture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.TOTALFACTURE="
                    + this._dbWriteNumeric(statement.getTransaction(), getForTotalFacture());
        }

        if (getForIdModeRecouvrement().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDMODERECOUVREMENT="
                    + this._dbWriteNumeric(statement.getTransaction(), getForIdModeRecouvrement());
        }

        if (getFromNumCommune().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.NOMCOMMUNE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromNumCommune());
        }

        if (getFromIdExterneFacture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDEXTERNEFACTURE>="
                    + this._dbWriteString(statement.getTransaction(), getFromIdExterneFacture());
        }
        // traitement du numéro d'affilié
        if (getFromIdExterneRole().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDEXTERNEROLE>="
                    + this._dbWriteString(statement.getTransaction(), getFromIdExterneRole());
        }
        // traitement du numéro d'affilié, fonctionne comme un between
        if (getForTillIdExterneRole().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.IDEXTERNEROLE<="
                    + this._dbWriteString(statement.getTransaction(), getForTillIdExterneRole());
        }

        if (getFromTotalFacture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.TOTALFACTURE>="
                    + this._dbWriteNumeric(statement.getTransaction(), getFromTotalFacture());
        }
        // equivalent à smaller than
        if (getForTillTotalFacture().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.TOTALFACTURE <"
                    + this._dbWriteNumeric(statement.getTransaction(), getForTillTotalFacture());
        }
        // equivalent à bigger than
        if (getForTotalFactureBigger().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.TOTALFACTURE >"
                    + this._dbWriteNumeric(statement.getTransaction(), getForTotalFactureBigger());
        }
        //
        if (getForTotalFactureNotBetween()) {
            try {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                if (isUseManagerForCompensation()) {
                    sqlWhere += "(FAENTFP2.TOTALFACTURE <"
                            + this._dbWriteNumeric(statement.getTransaction(), ((FAApplication) getSession()
                                    .getApplication()).getMontantMinimeNeg())
                            + " OR FAENTFP2.TOTALFACTURE >"
                            + this._dbWriteNumeric(statement.getTransaction(), ((FAApplication) getSession()
                                    .getApplication()).getMontantMinimePos()) + ")";
                }
                if (isUseManagerForCompensationAnnexe()) {
                    sqlWhere += "(FAENTFP.TOTALFACTURE <"
                            + this._dbWriteNumeric(statement.getTransaction(), ((FAApplication) getSession()
                                    .getApplication()).getMontantMinimeNeg())
                            + " OR FAENTFP.TOTALFACTURE >"
                            + this._dbWriteNumeric(statement.getTransaction(), ((FAApplication) getSession()
                                    .getApplication()).getMontantMinimePos()) + ")";
                }
            } catch (Exception e) {
                _addError(null, "Erreur lors de la construction de la requête: " + e.getMessage());
            }
        }

        if (getFromNom().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "TITIERP.HTLDE1 LIKE " + this._dbWriteString(statement.getTransaction(), getFromNom() + "%");
        }

        if (getFromNumCommune().length() != 0) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "FAENTFP.NOMCOMMUNE>=" + this._dbWriteNumeric(statement.getTransaction(), getFromNumCommune());
        }

        // Pour que le champ orNonImprimable soit pris en compte,
        // ForNonImprimable2 doit égallement avoir été modifié !

        if ((getForNonImprimable().length() != 0) && !getForNonImprimable2().equals(new Integer(-1))) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "NONIMPRIMABLE="
                    + this._dbWriteBoolean(statement.getTransaction(), new Boolean(forNonImprimable),
                            BConstants.DB_TYPE_BOOLEAN_CHAR, "nonImprimable");
        }

        if (getForEstRentierNa().booleanValue() && (getForEstRentierNa() != null)) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "ESTRENTIER="
                    + this._dbWriteBoolean(statement.getTransaction(), getForEstRentierNa(),
                            BConstants.DB_TYPE_BOOLEAN_CHAR, "forEstRentierNa");
        }

        if (wantModeRecouvrementAutomatiqueOuDirect) {
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere += "(FAENTFP.IDMODERECOUVREMENT="
                    + this._dbWriteNumeric(statement.getTransaction(), FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT)
                    + " OR FAENTFP.IDMODERECOUVREMENT="
                    + this._dbWriteNumeric(statement.getTransaction(), FAEnteteFacture.CS_MODE_AUTOMATIQUE) + ")";
        }

        if (isUseManagerForLSV()) {
            // La clause sur le domaine LSV est toujours executée (fait partie
            // de la jointure)
            if (sqlWhere.length() != 0) {
                sqlWhere += " AND ";
            }
            sqlWhere +=
            // on recherche l'adresse de paiement dans le domaine donné, si elle
            // n'existe pas on recheche dans un domaine par défaut
            "((" + FAEnteteFacture.TABLE_AVOIR_ADRESSE_PAYEMENT + "." + FAEnteteFacture.FIELD_AAP_DOMAINE_APPLICATION
                    + " = " + FAPassageRecouvrementLSVProcess.DOMAINE_APPLICATION_DEFAUT + " AND FAENTFP."
                    + FAEnteteFacture.FIELD_ID_DOMAINE_LVS + "=0) OR (" + FAEnteteFacture.TABLE_AVOIR_ADRESSE_PAYEMENT
                    + "." + FAEnteteFacture.FIELD_AAP_DOMAINE_APPLICATION + " = FAENTFP."
                    + FAEnteteFacture.FIELD_ID_DOMAINE_LVS + " AND FAENTFP." + FAEnteteFacture.FIELD_ID_DOMAINE_LVS
                    + "<>0))";

            if (getFromDateFacturation().length() != 0) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                // on recherche l'adresse de paiement qui sont valides à la date
                // données (date = 0 --> n'expire pas)
                sqlWhere += "(" + FAEnteteFacture.FIELD_AAP_FIN_VALIDITE + " >= "
                        + this._dbWriteDateAMJ(statement.getTransaction(), getFromDateFacturation()) + " OR "
                        + FAEnteteFacture.FIELD_AAP_FIN_VALIDITE + " = "
                        + this._dbWriteNumeric(statement.getTransaction(), "0") + " ) ";
            }

        }

        // Choix du type de décompte
        if (getForTriDecompte().length() != 0) {
            if (getForTriDecompte().equals(FAEnteteFacture.CS_TRI_DECOMPTE_POSITIF)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "FAENTFP.TOTALFACTURE>0";
            }
            if (getForTriDecompte().equals(FAEnteteFacture.CS_TRI_DECOMPTE_ZERO)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "FAENTFP.TOTALFACTURE=0";
            }
            if (getForTriDecompte().equals(FAEnteteFacture.CS_TRI_NOTECREDIT)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "FAENTFP.TOTALFACTURE<0";
            }
            if (getForTriDecompte().equals(FAEnteteFacture.CS_TRI_DECOMPTE_RECOUVREMENT)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "FAENTFP.IDMODERECOUVREMENT="
                        + this._dbWriteNumeric(statement.getTransaction(), FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT);
            }
            if (getForTriDecompte().equals(FAEnteteFacture.CS_TRI_DECOMPTE_SANSRECOUVREMENT)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "FAENTFP.IDMODERECOUVREMENT<>"
                        + this._dbWriteNumeric(statement.getTransaction(), FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT);
            }
            if (getForTriDecompte().equals(FAEnteteFacture.CS_TRI_NOTECREDIT_REMBOURSEMENT)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "(FAENTFP.IDMODERECOUVREMENT="
                        + this._dbWriteNumeric(statement.getTransaction(), FAEnteteFacture.CS_MODE_REMBOURSEMENT);
                sqlWhere += " OR FAENTFP.IDMODERECOUVREMENT="
                        + this._dbWriteNumeric(statement.getTransaction(), FAEnteteFacture.CS_MODE_FORCEE_REMBOURSEMENT);
                sqlWhere += ") AND FAENTFP.TOTALFACTURE<0";
            }
            if (getForTriDecompte().equals(FAEnteteFacture.CS_TRI_NOTECREDIT_SANSREMBOURSEMENT)) {
                if (sqlWhere.length() != 0) {
                    sqlWhere += " AND ";
                }
                sqlWhere += "FAENTFP.IDMODERECOUVREMENT<>"
                        + this._dbWriteNumeric(statement.getTransaction(), FAEnteteFacture.CS_MODE_REMBOURSEMENT);
                sqlWhere += " AND FAENTFP.IDMODERECOUVREMENT<>"
                        + this._dbWriteNumeric(statement.getTransaction(), FAEnteteFacture.CS_MODE_FORCEE_REMBOURSEMENT);
                sqlWhere += " AND FAENTFP.TOTALFACTURE<0";
            }
        }

        // traitement du GROUP BY
        if (_getGroupBy(statement).length() != 0) {
            sqlWhere += " GROUP BY " + _getGroupBy(statement);
        }

        return sqlWhere;
    }

    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        if (isUseManagerForCompensationAnnexe()) {
            return new FAEnteteCompteAnnexe();
        } else if (isUseManagerForLSV()) {
            FAEnteteFacture entete = new FAEnteteFacture();
            entete.setUseEntityForLSV(true);
            return entete;
        } else {
            return new FAEnteteFacture();
        }
    }

    public void _setGroupBy(String newGroupBy) {
        groupBy = newGroupBy;
    }

    public String getConditionInfoRom336() {
        return conditionInfoRom336;
    }

    public Boolean getForEstRentierNa() {
        return forEstRentierNa;
    }

    public String getForIdAdresse() {
        return forIdAdresse;
    }

    public String getForIdAdressePaiement() {
        return forIdAdressePaiement;
    }

    /**
     * Getter
     */
    public String getForIdEntete() {
        return forIdEntete;
    }

    public String getForIdExterneFacture() {
        return forIdExterneFacture;
    }

    public String getForIdExterneRole() {
        return forIdExterneRole;
    }

    public String getForIdModeRecouvrement() {
        return forIdModeRecouvrement;
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public String getForIdRemarque() {
        return forIdRemarque;
    }

    public String getForIdRole() {
        return forIdRole;
    }

    public String getForIdSoumisInteretsMoratoires() {
        return forIdSoumisInteretsMoratoires;
    }

    public String getForIdSousType() {
        return forIdSousType;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public String getForIdTypeFacture() {
        return forIdTypeFacture;
    }

    /**
     * @return
     */
    public String getForNonImprimable() {
        return forNonImprimable;
    }

    /**
     * @return
     */
    public Integer getForNonImprimable2() {
        return forNonImprimable2;
    }

    public String getForTillIdExterneRole() {
        return forTillIdExterneRole;
    }

    public String getForTillTotalFacture() {
        return forTillTotalFacture;
    }

    public String getForTotalFacture() {
        return forTotalFacture;
    }

    /**
     * @return
     */
    public String getForTotalFactureBigger() {
        return forTotalFactureBigger;
    }

    public boolean getForTotalFactureNotBetween() {
        return forTotalFactureNotBetween;
    }

    public String getForTriDecompte() {
        if (forTriDecompte == null) {
            return "";
        } else {
            return forTriDecompte;
        }
    }

    public String getFromDateFacturation() {
        return fromDateFacturation;
    }

    public String getFromIdExterneFacture() {
        return fromIdExterneFacture;
    }

    public String getFromIdExterneRole() {
        return fromIdExterneRole;
    }

    public String getFromNom() {
        return fromNom;
    }

    public String getFromNumCommune() {
        return fromNumCommune;
    }

    public String getFromTotalFacture() {
        return fromTotalFacture;
    }

    public String getInIdSousType() {
        return inIdSousType;
    }

    public String getInStatusPassage() {
        return inStatusPassage;
    }

    /**
     * @return
     */
    public String getLikeIdExterneFacture() {
        return likeIdExterneFacture;
    }

    /**
     * @return the notInIdSousType
     */
    public String getNotInIdSousType() {
        return notInIdSousType;
    }

    public String getOrderBy() {
        if (orderBy == null) {
            return "";
        } else {
            return orderBy;
        }
    }

    public Boolean getUseManagerWhitPassage() {
        return useManagerWhitPassage;
    }

    public Boolean getWantOnlyFileEntete() {
        return wantOnlyFileEntete;
    }

    public boolean isUseManagerForCompensation() {
        return useManagerForCompensation;
    }

    public boolean isUseManagerForCompensationAnnexe() {
        return useManagerForCompensationAnnexe;
    }

    public boolean isUseManagerForLSV() {
        return useManagerForLSV;
    }

    public boolean isWantForIdSousTypeTaxeSomDS() {
        return wantForIdSousTypeTaxeSomDS;
    }

    public boolean isWantModeRecouvrementAutomatiqueOuDirect() {
        return wantModeRecouvrementAutomatiqueOuDirect;
    }

    public void setConditionInfoRom336(String conditionInfoRom336) {
        this.conditionInfoRom336 = conditionInfoRom336;
    }

    public void setForEstRentierNa(Boolean forEstRentierNa) {
        this.forEstRentierNa = forEstRentierNa;
    }

    public void setForIdAdresse(String newForIdAdresse) {
        forIdAdresse = newForIdAdresse;
    }

    public void setForIdAdressePaiement(String newForIdAdressePaiement) {
        forIdAdressePaiement = newForIdAdressePaiement;
    }

    /**
     * Setter
     */
    public void setForIdEntete(String newForIdEntete) {
        forIdEntete = newForIdEntete;
    }

    public void setForIdExterneFacture(String newForIdExterneFacture) {
        forIdExterneFacture = newForIdExterneFacture;
    }

    public void setForIdExterneRole(String newForIdExterneRole) {
        forIdExterneRole = newForIdExterneRole;
    }

    public void setForIdModeRecouvrement(String newForIdModeRecouvrement) {
        forIdModeRecouvrement = newForIdModeRecouvrement;
    }

    public void setForIdPassage(String newForIdPassage) {
        forIdPassage = newForIdPassage;
    }

    public void setForIdRemarque(String newForIdRemarque) {
        forIdRemarque = newForIdRemarque;
    }

    public void setForIdRole(String newForIdRole) {
        forIdRole = newForIdRole;
    }

    public void setForIdSoumisInteretsMoratoires(String newForIdSoumisInteretsMoratoires) {
        forIdSoumisInteretsMoratoires = newForIdSoumisInteretsMoratoires;
    }

    public void setForIdSousType(String newForIdSousType) {
        forIdSousType = newForIdSousType;
    }

    public void setForIdTiers(String newForIdTiers) {
        forIdTiers = newForIdTiers;
    }

    public void setForIdTypeFacture(String newForIdTypeFacture) {
        forIdTypeFacture = newForIdTypeFacture;
    }

    /**
     * Setter for the variable forNonImprimable Warning: to modify if it, it is required to switch also
     * forNonImprimable!
     * 
     * @param boolean1
     */
    public void setForNonImprimable(Boolean bool) {
        forNonImprimable = bool.toString();
    }

    /**
     * Setter for the variable forNonImprimable2 i = -1 for the value "null" i = 0 for the value "false" i = 1 for the
     * value "true"
     * 
     * @param i
     */
    public void setForNonImprimable2(Integer i) {
        forNonImprimable2 = i;
    }

    public void setForTillIdExterneRole(String newForTillIdExterneRole) {
        forTillIdExterneRole = newForTillIdExterneRole;
    }

    public void setForTillTotalFacture(String newForTillTotalFacture) {
        forTillTotalFacture = newForTillTotalFacture;
    }

    public void setForTotalFacture(String newForTotalFacture) {
        forTotalFacture = newForTotalFacture;
    }

    /**
     * @param string
     */
    public void setForTotalFactureBigger(String string) {
        forTotalFactureBigger = string;
    }

    public void setForTotalFactureNotBetween(boolean forTotalFactureNotBetween) {
        this.forTotalFactureNotBetween = forTotalFactureNotBetween;
    }

    public void setForTriDecompte(String newForTriDecompte) {
        forTriDecompte = newForTriDecompte;
    }

    public void setFromDateFacturation(String string) {
        fromDateFacturation = string;
    }

    public void setFromIdExterneFacture(String newFromIdExterneFacture) {
        fromIdExterneFacture = newFromIdExterneFacture;
    }

    public void setFromIdExterneRole(String newFromIdExterneRole) {
        fromIdExterneRole = newFromIdExterneRole;
    }

    public void setFromNom(String newFromNom) {
        fromNom = newFromNom;
    }

    public void setFromNumCommune(String newFromNumCommune) {
        fromNumCommune = newFromNumCommune;
    }

    public void setFromTotalFacture(String newFromTotalFacture) {
        fromTotalFacture = newFromTotalFacture;
    }

    public void setInIdSousType(String inIdSousType) {
        this.inIdSousType = inIdSousType;
    }

    public void setInStatusPassage(String inStatusPassage) {
        this.inStatusPassage = inStatusPassage;
    }

    /**
     * @param string
     */
    public void setLikeIdExterneFacture(String string) {
        likeIdExterneFacture = string;
    }

    /**
     * @param notInIdSousType
     *            the notInIdSousType to set
     */
    public void setNotInIdSousType(String notInIdSousType) {
        this.notInIdSousType = notInIdSousType;
    }

    public void setOrderBy(String newOrderBy) {
        orderBy = newOrderBy;
    }

    public void setUseManagerForCompensation(boolean new_useManagerForCompensation) {
        useManagerForCompensation = new_useManagerForCompensation;
    }

    public void setUseManagerForCompensationAnnexe(boolean new_useManagerForCompensationAnnexe) {
        useManagerForCompensationAnnexe = new_useManagerForCompensationAnnexe;
    }

    public void setUseManagerForLSV(boolean b) {
        useManagerForLSV = b;
    }

    public void setUseManagerWhitPassage(Boolean useManagerWhitPassage) {
        this.useManagerWhitPassage = useManagerWhitPassage;
    }

    public void setWantForIdSousTypeTaxeSomDS(boolean wantForIdSousTypeTaxeSomDS) {
        this.wantForIdSousTypeTaxeSomDS = wantForIdSousTypeTaxeSomDS;
    }

    /**
     * Setter pour ne selectionner que les entetes de factures avec comme mode de recouvrement directe et automatique
     */
    public void setWantModeRecouvrementAutomatiqueOuDirect(boolean b) {
        wantModeRecouvrementAutomatiqueOuDirect = b;
    }

    public void setWantOnlyFileEntete(Boolean wantOnlyFileEntete) {
        this.wantOnlyFileEntete = wantOnlyFileEntete;
    }

    public String getForNotModeImpression() {
        return forNotModeImpression;
    }

    public void setForNotModeImpression(String forNotModeImpression) {
        this.forNotModeImpression = forNotModeImpression;
    }
}
