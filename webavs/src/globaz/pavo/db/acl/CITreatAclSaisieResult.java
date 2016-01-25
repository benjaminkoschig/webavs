package globaz.pavo.db.acl;

import globaz.commons.nss.NSUtil;
import globaz.commons.nss.db.NSSinfo;
import globaz.globall.db.BSession;
import globaz.pavo.db.compte.CICompteIndividuel;

/**
 * Objet permettant d'encapsuler des informations que le service "CIAnnonceCollaborateurService.genererArcAndException"
 * doit retourner
 * 
 * @author BJO
 */
public class CITreatAclSaisieResult {
    // service genererArcAndException a
    // trouvé une concordance dans la table
    // NSSRA en fonction de l'ancien numéro
    private String attestationAssuranceLocation = "";
    private String dateNaissance = "";
    private String nationnalite = "";
    private String nnssFindInNssra = "";// représente le nnss de l'assuré si le
    private String nomPrenom = "";
    private boolean success = false;
    private String typeArc = "";

    public String getAttestationAssuranceLocation() {
        return attestationAssuranceLocation;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getNationnalite() {
        return nationnalite;
    }

    public String getNnssFindInNssra() {
        return nnssFindInNssra;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getTypeArc() {
        return typeArc;
    }

    public boolean isSuccess() {
        return success;
    }

    /**
     * Permet de charger dans l'objet de résultat des informations (nom, prénom, nationnalité, date de naissance, nss)
     * depuis un CI
     * 
     * @param ci
     * @param session
     * @throws Exception
     */
    public void loadInfoFromCi(CICompteIndividuel ci, BSession session) throws Exception {
        if (ci == null) {
            throw new Exception("the parameter ci cannot be null!");
        }

        nomPrenom = ci.getNomPrenom();
        nationnalite = session.getCodeLibelle(ci.getPaysOrigineId());
        dateNaissance = ci.getDateNaissance();
        nnssFindInNssra = ci.getNssFormate();
    }

    /**
     * Permet de charger dans l'objet de résultat des informations (nom, prénom, nationnalité, date de naissance, nss)
     * depuis un objet de type NssInfo (table NSSRA)
     * 
     * @param nssInfo
     * @param session
     */
    public void loadInfoFromNssInfo(NSSinfo nssInfo, BSession session) throws Exception {
        if (nssInfo == null) {
            throw new Exception("the parameter nssInfo cannot be null!");
        }

        nomPrenom = nssInfo.getNomPrenom();
        nationnalite = session.getCodeLibelle("315" + nssInfo.getCodeNationnalite());
        dateNaissance = nssInfo.getDateNaissance();
        nnssFindInNssra = NSUtil.formatAVSNewNum(nssInfo.getNNSS());
    }

    public void setAttestationAssuranceLocation(String attestationAssuranceLocation) {
        this.attestationAssuranceLocation = attestationAssuranceLocation;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setNationnalite(String nationnalite) {
        this.nationnalite = nationnalite;
    }

    public void setNnssFindInNssra(String nnssFindInNssra) {
        this.nnssFindInNssra = nnssFindInNssra;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setTypeArc(String typeArc) {
        this.typeArc = typeArc;
    }
}
