<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/pegasus.tld" prefix="pe" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
	
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Arrays"%>	

<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCAutreApiViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.pegasus.helpers.droit.PCDroitHelper"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>

<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.RenteIjApi"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.SimpleAutreApi"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCRenteijapi"%>


<%
	//Les labels de cette page commencent par le préfix "JSP_PC_PRET_TIERS_D"
	idEcran="PPC0012";
	PCAutreApiViewBean viewBean = (PCAutreApiViewBean) session.getAttribute("viewBean");
	boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
	
	autoShowErrorPopup = true;
	
	bButtonDelete = false;
	
	if(viewBeanIsNew){
		// change "Valider" action pour
		//userActionValue
	} else {
		bButtonCancel=false;
		bButtonUpdate=false;
		bButtonValidate=false;
	}
	String autreRenteLbl  =objSession.getCodeLibelle(IPCRenteijapi.CS_AUTRES_API_AUTRE);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/droit/commonDroit.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.pegasus.utils.PCCommonHandler"%><link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/renteijapi/renteijapi_de.css"/>
<script type="text/javascript" src="<%=rootPath%>/scripts/renteijapi/AutreApi_MembrePart.js"/></script>
<script type="text/javascript" src="<%=rootPath%>/scripts/renteijapi/AutreApi_de.js"/></script>

<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_DROIT_AUTRES_API_AJAX = "<%=IPCActions.ACTION_DROIT_AUTRES_API_AJAX%>";
	var CS_TYPE_AUTRE ="<%=IPCRenteijapi.CS_AUTRES_API_AUTRE%>";
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<%=PCCommonHandler.getTitre(objSession,request)%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			<TR>		
				<td colspan="4">
					<div class="conteneurDF">
					
						<div class="areaAssure">
							<%=viewBean.getRequerantDetail(objSession) %>
						</div>
					
						<hr />
						
						<%=PCDroitHandler.getOngletHtml(objSession,viewBean,IPCDroits.ONGLETS_RENTES_IJAPI,request,servletContext + mainServletPath)%>
						 
						<div class="conteneurMembres">
						
							<% 
								for(Iterator itMembre=viewBean.getMembres().iterator();itMembre.hasNext();){
									MembreFamilleEtendu membreFamille=(MembreFamilleEtendu)itMembre.next();
							%>
						
							<div class="areaMembre" idMembre="<%=membreFamille.getId() %>">
								<div class="areaTitre">
								<%=PCDroitHandler.getFromattedTitreHTML(objSession,membreFamille)%>
								</div>
								<table class="areaDFDataTable">
									<thead>
										<tr>
											<th data-g-cellformatter="css:formatCellIcon">&nbsp;</th>
											<th><ct:FWLabel key="JSP_PC_AUTRE_API_MONTATN_API"/></th>
											<th><ct:FWLabel key="JSP_PC_AUTRE_API_TYPE_API"/></th>
											<th><ct:FWLabel key="JSP_PC_AUTRE_API_GENRE"/></th>
											<th><ct:FWLabel key="JSP_PC_AUTRE_API_DEGRE"/></th>
											<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_AUTRE_API_DR"/></th>
											<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_AUTRE_API_PERIODE"/></th>
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										RenteIjApi donneeComplexe=(RenteIjApi)itDonnee.next();
										
										SimpleAutreApi donnee=(SimpleAutreApi)donneeComplexe.getDonneeFinanciere();
										SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>
											<td><%=new FWCurrency(donnee.getMontant()).toStringFormat() %></td>
											<td><%=(!IPCRenteijapi.CS_AUTRES_API_AUTRE.equals(donnee.getCsType()))?objSession.getCodeLibelle(donnee.getCsType()):autreRenteLbl +" - "+donnee.getAutre()%></td>
											<td><%=objSession.getCodeLibelle(donnee.getCsGenre()) %></td>
											<td><%=objSession.getCodeLibelle(donnee.getCsDegre()) %></td>
											<td><% if(dfHeader.getIsDessaisissementRevenu().booleanValue()){%>
												<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&nbsp;<%
												}%></td>
											<td ><%=dfHeader.getDateDebut() %> - <%=dfHeader.getDateFin() %></td>
										</tr>
							<%
										idGroup=dfHeader.getIdEntityGroup();
									}
										
							%>
									</tbody>
								</table>
								<div class="areaDFDetail">
									<table>
					
										<tr>
											<td><ct:FWLabel key="JSP_PC_AUTRE_API_MONTATN_API"/></td>
											<td><input type="text" class="montant" data-g-amount="mandatory:true, periodicity:Y"></td>
											<td><ct:FWLabel key="JSP_PC_AUTRE_API_TYPE_API"/></td>

											<td>
											<ct:select name="csTypeApi" wantBlank="false" notation="data-g-select='mandatory:true'">
												<ct:optionsCodesSystems csFamille="PCTYPAAPI"/>
											</ct:select>
											</td>
											<td class="autreToHidden"><ct:FWLabel key="JSP_PC_AUTRE_API_AUTRE_TYPE_API"/></td>
											<td class="autreToHidden"><input type="text" class="autreTypeApi"  data-g-string="mandatory:true" /></td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_AUTRE_API_GENRE"/></td>
											<td>
											  <ct:select name="csGenre" wantBlank="false" notation="data-g-select='mandatory:true'">
												<ct:optionsCodesSystems csFamille="PCGENAAPI"/>
											  </ct:select>
											</td>
											<td><ct:FWLabel key="JSP_PC_AUTRE_API_DEGRE"/></td>
											<td>
											  <ct:select name="csDegre" wantBlank="false" notation="data-g-select='mandatory:true'">
												<ct:optionsCodesSystems csFamille="PCDEGAAPI"/>
											  </ct:select>
											</td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_AUTRE_API_DESSAISISSEMENT_REVENU"/></td>
											<td><input type="checkbox" class="dessaisissementRevenu" /></td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_AUTRE_API_DATEDEBUT"/></td>
											<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
											<td><ct:FWLabel key="JSP_PC_AUTRE_API_DATEFIN"/></td>
											<td><input type="text" name="dateFin" value="" data-g-calendar="type:month"/></td>
										</tr>
									</table>
									<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_AUTRES_API_AJAX%>" crud="cud">
										<%@ include file="/pegasusRoot/droit/commonButtonDF.jspf" %>
									</ct:ifhasright>
								</div>
							</div>
							<%
								}
							%>
						</div>
					</div>
				</TD>
			</TR>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>