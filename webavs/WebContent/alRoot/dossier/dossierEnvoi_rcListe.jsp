
<%@page import="ch.globaz.al.business.constantes.ALCSEnvoi"%>
<%@ page import="globaz.al.vb.dossier.ALDossierEnvoiListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>

<%@page import="globaz.al.vb.dossier.ALDossierEnvoiViewBean"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>

<%-- tpl:insert attribute="zoneScripts" --%>
	<%
	ALDossierEnvoiListViewBean viewBean = (ALDossierEnvoiListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
    detailLink = "al?userAction=al.dossier.dossierMain.afficher&selectedId=";
    String detailLinkJob = "al?userAction=al.envois.envois.afficher&selectedId=";
	
    //pour voir si on ouvre direct le dossier (si un n° dossier retourné)
    String numDossierCriteria = "";
    if(!JadeStringUtil.isEmpty(request.getParameter("searchModel.forIdDossier")))
    	numDossierCriteria=request.getParameter("searchModel.forIdDossier");
	%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>

<%-- tpl:insert attribute="zoneHeaders" --%>

<script type="text/javascript">

function customOnLoad(){
}
//------------------------------------------------------------
//Lancement de word si data est correctement renseigné 
//------------------------------------------------------------
function launchWord(_filePath){
	try{
		var s_filepath=""+_filePath;
		if(s_filepath.length<=0){
			alert("Error, file not found !");
		}else{
			var word=null;
			try {
		  		if(word==null){
		  			word = new ActiveXObject('Word.Application');
		  		}
			    word.application.visible="true";
		  	} catch(e){
			   	word = new ActiveXObject('Word.Application');
			    word.application.visible="true";
		  	}
		    var currentDocument = word.documents.open(s_filepath);
		}
	} catch (err){
		errorMessage+="\r\nError Description : "+err.description;
		alert(errorMessage);
	}
}
</script>
	<th><ct:FWLabel key="AL0100_COLONNE_STATUS"/></th>
	<th><ct:FWLabel key="AL0100_COLONNE_DATE"/></th>
	<th><ct:FWLabel key="AL0100_COLONNE_UTILISATEUR"/></th>
	<th align="left"><ct:FWLabel key="AL0100_COLONNE_DOCUMENT"/></th>
	<th><ct:FWLabel key="AL0100_COLONNE_JOB"/></th>
    <%-- /tpl:insert --%> 

<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>  
	<%    
	ALDossierEnvoiViewBean line = (ALDossierEnvoiViewBean) viewBean.getEntity(i);

	String detailUrlJob = "parent.location.href='" + detailLinkJob + line.getEnvoiComplex().getEnvoiItemSimpleModel().getId()+"'";

	%>  
    <%-- /tpl:insert --%>

<%@ include file="/theme/list/lineStyle.jspf" %>

	<%-- tpl:insert attribute="zoneList" --%>
	<%
	if (isSelection) { // mode sélection
		actionDetail = "document.body.innerHTML='';parent.location.href='" + globaz.fweb.taglib.FWChooserTag.getSelectLink(pageContext, i) + "'";
	} else { // détail "normal"
		actionDetail = targetLocation  + "='" + detailLink + line.getId()+"'";
	}
	%>
	<td class="mtd" nowrap align="center"><%=objSession.getCodeLibelle(line.getEnvoiComplex().getEnvoiItemSimpleModel().getEnvoiStatus())%></td>
	<td class="mtd" nowrap align="center"><%=line.getEnvoiComplex().getEnvoiJobSimpleModel().getJobDate()%></td>
	<td class="mtd" nowrap align="center"><%=line.getEnvoiComplex().getEnvoiJobSimpleModel().getJobUser()%></td>
	<td class="mtd" nowrap >
		<%if(ALCSEnvoi.STATUS_ENVOI_GENERATED.equals(line.getEnvoiComplex().getEnvoiItemSimpleModel().getEnvoiStatus())){ %>
			<a href="javascript:launchWord('<%=line.getFilePathDocument(line.getEnvoiComplex().getEnvoiItemSimpleModel().getEnvoiFileName())%>')">
				<%=line.getDocumentLibelle(line.getEnvoiComplex().getEnvoiItemSimpleModel().getIdFormule())%>
			</a>
		<%}else{ %>
			<%=(line.getDocumentLibelle(line.getEnvoiComplex().getEnvoiItemSimpleModel().getIdFormule()).equals("")?line.getEnvoiComplex().getEnvoiItemSimpleModel().getEnvoiFileName():line.getDocumentLibelle(line.getEnvoiComplex().getEnvoiItemSimpleModel().getIdFormule()))%>
		<%} %>
	</td>
	<td class="mtd" nowrap align="center" onClick="<%=detailUrlJob%>" >
		<a href="#">
			<%=line.getEnvoiComplex().getEnvoiItemSimpleModel().getIdJob()%>
		</a>
			<img
				src="<%=request.getContextPath()%>/images/external.png" 
			>
	</td>
	<%-- /tpl:insert --%>

<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	