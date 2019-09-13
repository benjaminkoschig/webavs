package ch.globaz.orion.db;

import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import com.fasterxml.jackson.databind.ObjectMapper;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;

import java.util.*;

import ch.globaz.common.jadedb.JadeManager;
import ch.globaz.common.sql.SQLWriter;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import org.apache.poi.hssf.record.formula.functions.Upper;
import org.springframework.web.servlet.ModelAndView;

public class EBPucsFileManager extends JadeManager<EBPucsFileEntity> {

    private static final long serialVersionUID = 1L;
    private String forDateSoumission;
    private String likeAffilie;
    private String statut;
    private DeclarationSalaireProvenance forProvenance = DeclarationSalaireProvenance.UNDEFINDED;
    private String fullText;
    private Collection<String> inIds;
    private String forFilename;
    private String forTypeDeclaration;
    private String forUser;
    private List <String> users = new ArrayList();

    private String forDateDebut;
    private String forDateFin;

    private Boolean isSelectionnerAllHandling = false;
    private Boolean isSelectionnerToHandle = false;


    @Override
    protected void createWhere(SQLWriter sqlWhere) {
        String provenance = null;
        if (!forProvenance.isUndefinded()) {
            provenance = forProvenance.getValue();
        }


        sqlWhere.and(EBPucsFileDefTable.DATE_RECEPTION).equalForDate(forDateSoumission);
        sqlWhere.and(EBPucsFileDefTable.DATE_RECEPTION).greaterOrEqualForDate(forDateDebut);
        sqlWhere.and(EBPucsFileDefTable.DATE_RECEPTION).beforeOrEqualForDate(forDateFin);
        sqlWhere.and("UPPER("+EBPucsFileDefTable.HANDLING_USER+")").equal(Objects.isNull(forUser)? forUser : forUser.toUpperCase());
        sqlWhere.and(EBPucsFileDefTable.NUMERO_AFFILIE).fullLike(likeAffilie);
        sqlWhere.and(EBPucsFileDefTable.STATUS).in(statut);
        sqlWhere.and(EBPucsFileDefTable.PROVENANCE).equal(provenance);
        sqlWhere.and(EBPucsFileDefTable.ID).in(inIds);
        if (!JadeStringUtil.isEmpty(forTypeDeclaration)) {
            sqlWhere.and(EBPucsFileDefTable.TYPE_DECLARATION).equal(Integer.parseInt(forTypeDeclaration));
        }
        if (fullText != null) {
            sqlWhere.and(EBPucsFileDefTable.SEARCH_STRING).fullLike(
                    JadeStringUtil.convertSpecialChars(fullText).toUpperCase());
        }
        sqlWhere.and(EBPucsFileDefTable.ID_FILE_NAME).equal(forFilename);
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new EBPucsFileEntity();
    }

    public DeclarationSalaireProvenance getForProvenance() {
        return forProvenance;
    }

    public void setForProvenance(DeclarationSalaireProvenance forProvenance) {
        this.forProvenance = forProvenance;
    }

    public void setOrderBy(String orderBy) {
        defineOrderBy(orderBy);
    }

    public String getForDateSoumission() {
        return forDateSoumission;
    }

    public void setForDateSoumission(String forDateSoumission) {
        this.forDateSoumission = forDateSoumission;
    }

    public String getLikeAffilie() {
        return likeAffilie;
    }

    public void setLikeAffilie(String likeAffilie) {
        this.likeAffilie = likeAffilie;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public void setInIds(Collection<String> ids) {
        inIds = ids;
    }

    public String getForFilename() {
        return forFilename;
    }

    public void setForFilename(String forFilename) {
        this.forFilename = forFilename;
    }

    public String getForTypeDeclaration() {
        return forTypeDeclaration;
    }

    public void setForTypeDeclaration(String forTypeDeclaration) {
        this.forTypeDeclaration = forTypeDeclaration;
    }

    public String getForDateDebut() {
        return forDateDebut;
    }

    public void setForDateDebut(String forDateDebut) {
        this.forDateDebut = forDateDebut;
    }

    public String getForDateFin() {
        return forDateFin;
    }

    public void setForDateFin(String forDateFin) {
        this.forDateFin = forDateFin;
    }

    public List getUsers() {
        return users;
    }

    public Map  getUsersJson() {
        ObjectMapper mapper = new ObjectMapper();

        ModelAndView usersJson = new ModelAndView("pucsFile_rc");

        String json = "";
        try {
            json = mapper.writeValueAsString(users);
        } catch (Exception e) {
            e.printStackTrace();
        }

        usersJson.addObject("usersJson",json);

        return usersJson.getModel();
    }

    public void setUsers(String user) {
        if (!users.contains(user)) {
            users.add(user);
            Collections.sort(users);
        }
    }

    public String getForUser(){
        return forUser;
    }

    public void setForUser(String forUser) {
        this.forUser = forUser;
    }

    public Boolean getIsSelectionnerAllHandling() {
        return isSelectionnerAllHandling;
    }

    public void setIsSelectionnerAllHandling(Boolean isSelectionnerAllHandling) {
        this.isSelectionnerAllHandling = isSelectionnerAllHandling;
    }

    public Boolean getIsSelectionnerToHandle() {
        return isSelectionnerToHandle;
    }

    public void setIsSelectionnerToHandle(Boolean isSelectionnerToHandle) {
        this.isSelectionnerToHandle = isSelectionnerToHandle;
    }
}
