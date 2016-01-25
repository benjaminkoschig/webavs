<%@page import="globaz.fweb.taglib.FWCodeSelectTag"%>
<%@page import="globaz.jade.client.util.JadeCodesSystemsUtil"%>
<%@page import="globaz.al.vb.ajax.ALDossiersLiesListViewBean"%>
<%@page import="ch.globaz.al.business.constantes.ALCSPays"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Map.Entry"%>


<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/plain;charset=UTF-8" %>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=UTF-8" --%>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=UTF-8" --%>
<%-- taglib uri="/WEB-INF/taglib.tld" prefix="ct" --%>
	<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	ALDossiersLiesListViewBean viewBean = (ALDossiersLiesListViewBean) request.getAttribute("viewBean");
	%>
	
{"search":{
	"typeSearch" :	"<%=((Object)viewBean.getDossierLieComplexResult(0)).getClass()%>",
	"totalResults" : "<%=viewBean.getSize()%>",
	"results" : [
		<% for(int i=0;i<viewBean.getSize();i++){%>
		{
		"id_lien":"<%=viewBean.getDossierLieComplexResult(i).getIdLien()%>",	
		"dossier_id":"<%=viewBean.getDossierLieComplexResult(i).getIdDossierLie()%>",
        "allocataire_nom":"<%=viewBean.getDossierLieComplexResult(i).getNomAllocataire()%>",
		"allocataire_prenom":"<%=viewBean.getDossierLieComplexResult(i).getPrenomAllocataire()%>",
		"allocataire_nss":"<%=viewBean.getDossierLieComplexResult(i).getNssAllocataire()%>",
		"affilie_numero":"<%=viewBean.getDossierLieComplexResult(i).getNumAffilie()%>",
		"dossier_activite":"<%=JadeCodesSystemsUtil.getCodeLibelle(objSession,viewBean.getDossierLieComplexResult(i).getDossierActivite())%>",
		"dossier_etat":"<%=JadeCodesSystemsUtil.getCodeLibelle(objSession,viewBean.getDossierLieComplexResult(i).getDossierEtat())%>",
		"dossier_statut":"<%=JadeCodesSystemsUtil.getCodeLibelle(objSession,viewBean.getDossierLieComplexResult(i).getDossierStatut())%>",
		"dossier_radiation":"<%=viewBean.getDossierLieComplexResult(i).getDossierRadiation()%>",
		"droit_debut":"<%=viewBean.getDossierLieComplexResult(i).getDroitDebut()%>",
		"csTypeLien":{
			"value":"<%=viewBean.getDossierLieComplexResult(i).getTypeLien()%>",
			"libelle":"<%=JadeCodesSystemsUtil.getCodeLibelle(objSession,viewBean.getDossierLieComplexResult(i).getTypeLien())%>",
			"collection":[	
			<%
			int cpt = 0;
			for (Entry<String, String> entry : viewBean.getAllCsTypeLien().entrySet()){
				cpt++;
			%>
					{
						"value":"<%=entry.getKey() %>",
						"libelle":"<%=entry.getValue() %>"
					}<% if(cpt<viewBean.getAllCsTypeLien().size()){%>,<%} %>
			<%}%>		
			]}	
		}<% if(i!=viewBean.getSize()-1){%>,<%}%>
		<%}%>
		]
	}
}
	
	