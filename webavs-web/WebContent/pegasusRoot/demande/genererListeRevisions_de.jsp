<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.pegasus.vb.demande.PCGenererListeRevisionsViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LOV_D"

	idEcran="PPC2008";
	
	PCGenererListeRevisionsViewBean viewBean = (PCGenererListeRevisionsViewBean)session.getAttribute("viewBean");

	userActionValue =IPCActions.ACTION_DEMANDE_GENERER_LISTE_REVISIONS  + ".executer";	
	
	String rootPath = servletContext+(mainServletPath+"Root");
	
%>
<%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>


<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=rootPath %>/scripts/jadeBaseFormulaire.js"></script>

<SCRIPT language="javascript">
$(function(){
	$("#btnCtrlJade").hide();
})

var f_paramDynamique = function () {
  var annee = $('#annee').val();
  var moisAnnee = $('#moisAnnee').val();
  
  
  if(!annee.length){
	  annee = " ";
  }
  if(!moisAnnee.length){
	  moisAnnee = " ";
  }
  
  return ","+annee+","+moisAnnee;
}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LRE_D_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tbody class="blockDetail">
						<tr data-g-commutator="context:$(this).parents('.blockDetail'),
								   condition:¦context.find('[name=periodicite]:checked').val()=='annee'¦,
								   actionTrue:¦show(context.find('.blocAnnee')),hide(context.find('.blocMoisAnnee')),clear(context.find('#moisAnnee'))¦,
								   actionFalse:¦show(context.find('.blocMoisAnnee')),hide(context.find('.blocAnnee')),clear(context.find('#annee'))¦">
							<td>
								<ct:FWLabel key="JSP_LRE_D_PERIODICITE"/>
								<br/>
								
							</td>
							<td >
								<input type="radio" name="periodicite" value="annee" id="periodicite1" checked="checked"/> <label for="periodicite1"><ct:FWLabel key="JSP_LRE_D_ANNEE" /></label><br />
								<input type="radio" name="periodicite" value="moisAnnee" id="periodicite2" /> <label for="periodicite2"><ct:FWLabel key="JSP_LRE_D_MOIS_ANNEE" /></label><br />
							<td/>
						<tr/>
						<tr>
							<td  colspan="4">&nbsp;</td>
						</tr>
						<tr class="blocAnnee">
							<td>&nbsp;</td>
							<td><input type="text" id="annee" name="annee" value="" size="4"/></td>
						</tr>
						<tr class="blocMoisAnnee">
							<td>&nbsp;</td>
							<td><input type="text" id="moisAnnee" name="moisAnnee" value="" data-g-calendar="type:month" /></td>
						</tr>
						<tr>
							<td  colspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<a data-g-download="docType:xls,
													dynParametres:f_paramDynamique,
								                    serviceClassName:ch.globaz.pegasus.business.services.revisionquadriennale.RevisionQuadriennaleService,
								                    serviceMethodName:generateListRevisionSimple,
								                    docName:listeRevisions"
								><ct:FWLabel key="JSP_LRE_D_LISTE_SIMPLE"/></a>	
						</tr>
						<tr>
						<!--  
							<td colspan="2" align="center">
								<a data-g-download="docType:csv,
													dynParametres:f_paramDynamique,
								                    serviceClassName:ch.globaz.pegasus.business.services.revisionquadriennale.RevisionQuadriennaleService,
								                    serviceMethodName:generateListRevisionComplete,
								                    docName:listeRevisionsComplete"
								><ct:FWLabel key="JSP_LRE_D_LISTE_COMPLETE"/></a>	
								-->
						</tr>
						</tbody>		
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>