package globaz.ccvd.hermes;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceLotListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.service.IHEExtractCopyFileTraitement;
import globaz.hermes.utils.HENNSSUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.io.BufferedWriter;

public class HEExtractRentesData implements IHEExtractCopyFileTraitement {
    public String[] forMotifs = null;
    private String referenceFixe = "";

    protected void commitTreatReference(BSession session, BTransaction transaction) throws Exception {
        // ne rien daire puisque on transmet toujorus toutes les r�f�rences
    }

    private String createReference(BSession session) throws Exception {
        StringBuffer reference = new StringBuffer();
        reference.append("   ");
        reference.append('Z');
        reference.append(getCodeProvenance(session));
        reference.append("    ");
        return reference.toString();
    }

    private String creerReferenceCCVD(String numeroAvs, BSession session) throws Exception {
        // traitement du NNSS, si c'est un num�ro AVS --> conversion en n�gatif
        if (HENNSSUtils.isNNSSLength(numeroAvs)) {
            numeroAvs = HENNSSUtils.convertNNSStoNegatif(numeroAvs);
        }
        StringBuffer reference = new StringBuffer(numeroAvs);
        if (JadeStringUtil.isEmpty(referenceFixe)) {
            referenceFixe = createReference(session);
        }
        return reference.append(referenceFixe).toString();
    }

    @Override
    public void genererFichier(BufferedWriter file, boolean hasCarriageReturns, BSession session,
            BTransaction transaction) throws Exception {
        // principe d'extraction de tous les extraits demand�s connus dans la
        // base de donn�es
        // ceci afin d'alimenter la file d'attente de la CCVD
        // TODO : am�lioration � apporter lorsque une solution sera trouv�e pour
        // les r�vocations
        // a priori ajout d'un tag permettant de g�rer les r�vocations re�ues de
        // la centrale

        HEOutputAnnonceLotListViewBean outputAnnonceListViewBean = new HEOutputAnnonceLotListViewBean();
        outputAnnonceListViewBean.setSession(session);
        outputAnnonceListViewBean.setForCodesApps(" '11' , '38' , '39' ");
        outputAnnonceListViewBean.setForMotifs(forMotifs);
        outputAnnonceListViewBean.setForStatut(IHEAnnoncesViewBean.CS_TERMINE);
        outputAnnonceListViewBean.wantCallMethodAfter(false);
        outputAnnonceListViewBean.setForDateReceptionVide(isSelectDateReceptionVide());
        outputAnnonceListViewBean.setOrder(" CAST(RNREFU AS INTEGER), RNIANN");
        BStatement statement = outputAnnonceListViewBean.cursorOpen(transaction);
        HEOutputAnnonceViewBean outputAnnonceViewBean = null;

        String refRefUnique = "";
        String refSerie = "";
        String refExclue = "";
        // on part du principe que les s�ries se suivent dans l'ordre avec un
        // break
        // d�termin� � partir du changement de r�f�rence unique
        while ((outputAnnonceViewBean = (HEOutputAnnonceViewBean) outputAnnonceListViewBean.cursorReadNext(statement)) != null) {
            String crtRefUnique = outputAnnonceViewBean.getRefUnique();
            String crtEnr = outputAnnonceViewBean.getChampEnregistrement();

            if (JadeStringUtil.isEmpty(crtEnr)) {
                throw new Exception("Erreur critique, impossible de d�terminer le code application");
            }
            if (JadeStringUtil.isEmpty(crtRefUnique)) {
                throw new Exception("Erreur critique, impossible de traiter une r�f�rence non renseign�e");
            }
            // la r�f�rence et le champ enregistrement sont renseign� !

            if (!crtEnr.equals(refExclue)) {

                if (!refRefUnique.equals(crtRefUnique)) {
                    // break de serie, on recalcule la r�f�rence
                    if (isReferenceInterneATraiter(crtEnr, outputAnnonceViewBean.getMotif(), session, transaction)) {
                        refSerie = creerReferenceCCVD(outputAnnonceViewBean.getNumeroAVS(), session);
                        refRefUnique = crtRefUnique;
                    } else {
                        refExclue = crtRefUnique;
                    }
                    // on m�morise de toute fa�on la s�rie puisqu'on la trait�e
                    // m�me si elle est exclue...
                    memoryReference(crtRefUnique);
                }

                if (crtEnr.startsWith("38") || crtEnr.startsWith("39")) {
                    if (JadeStringUtil.isEmpty(refSerie)) {
                        throw new Exception(
                                "Erreur critique, impossible d'extraire une s�rie dans mentionner une r�f�rence !");
                    }
                    // Ecrire seulement s'il s'agit d'une annonce effectu�e par
                    // HERMES
                    // sinon annonce � double puisque extrait g�n�r� par PAVO et
                    // un extrait g�n�r� par HERMES
                    if ("HERMES".equals(outputAnnonceViewBean.getIdProgramme().trim())) {
                        file.write(JadeStringUtil.leftJustify(updateRefArc(crtEnr, refSerie), 120));
                        if (hasCarriageReturns) {
                            file.write("\n");
                        }
                    }
                }
            }
        }
        outputAnnonceListViewBean.cursorClose(statement);

        commitTreatReference(session, transaction);

    }

    private String getCodeProvenance(BSession session) throws Exception {
        String numCaisse = session.getApplication().getProperty("noCaisse");
        if (JadeStringUtil.isEmpty(numCaisse)) {
            throw new Exception(
                    "Erreur critique, impossible de d�terminer le code origine, numeroCaisse n'est pas renseign�");
        }
        String numAgence = session.getApplication().getProperty("noAgence");
        if (JadeStringUtil.isEmpty(numAgence)) {
            throw new Exception(
                    "Erreur critique, impossible de d�terminer le code origine, numeroAgence n'est pas renseign�");
        }
        if ("116".equals(numCaisse)) {
            // AGRIVIT, code 5
            return "5";
        } else if ("022".equals(numCaisse)) {
            if ("132".equals(numAgence)) {
                // AGLSNE, code 4
                return "4";
            } else if ("000".equals(numAgence)) {
                // CCVD, code 1
                return "1";
            } else {
                throw new Exception(
                        "Erreur critique, impossible de d�terminer le code origine, numeroAgence doit corresponde � 000 ou 132");
            }
        } else {
            throw new Exception(
                    "Erreur critique, impossible de d�terminer le code origine, numeroCaisse doit corresponde � 116 ou 022");
        }
    }

    protected boolean isReferenceInterneATraiter(String crtEnr, String motif, BSession session, BTransaction transaction) {
        // traiter toutes les r�f�rences !!
        return true;
    }

    protected boolean isSelectDateReceptionVide() {
        return false;
    }

    protected void memoryReference(String crtRefUnique) {
        // ne rien faire puisque on transmet toujours toutes les r�f�rences
    }

    @Override
    public void setForListMotif(String[] motifs) throws Exception {
        forMotifs = motifs;
    }

    private String updateRefArc(String crtEnr, String refSerie) {
        StringBuffer s = new StringBuffer();
        s.append(JadeStringUtil.substring(crtEnr, 0, 11));
        s.append(refSerie);
        s.append(JadeStringUtil.substring(crtEnr, 31));
        return s.toString();
    }

}
