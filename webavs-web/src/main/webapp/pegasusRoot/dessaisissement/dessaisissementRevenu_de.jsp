	<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/pegasus.tld" prefix="pe" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.vb.home.PCHomeViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>

<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.FortuneParticuliere"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.vb.dessaisissement.PCDessaisissementFortuneViewBean"%>
<%@page import="globaz.pegasus.utils.PCDessaisissementHandler"%>

<%@page import="ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementRevenu"%>
<%@page import="ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenu"%>
<%@page import="ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuAuto"%>
<%@page import="globaz.pegasus.vb.dessaisissement.PCDessaisissementRevenuViewBean"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_DESSAISISSEMENT_REVENU_D"
	idEcran="PPC0023";

	PCDessaisissementRevenuViewBean viewBean = (PCDessaisissementRevenuViewBean) session.getAttribute("viewBean");
	
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
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/droit/commonDroit.jspf" %>

<%@page import="globaz.pegasus.utils.PCCommonHandler"%><link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/droit/fortuneParticuliere_de.css"/>
<%-- tpl:put name="zoneScripts" --%>

<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX="<%=IPCActions.ACTION_DROIT_DESSAISISSEMENT_REVENU_AJAX%>";	
</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/DessaisissementRevenu_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/DessaisissementRevenu_de.js"/></script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<%=PCCommonHandler.getTitre(objSession,request)%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
			<TR>		
				<td colspan="4">
					<div class="conteneurDF">
					
						<div class="areaAssure">
							<%=viewBean.getRequerantDetail(objSession) %>
						</div>
					
						<hr/>
						
						<%=PCDroitHandler.getOngletHtml(objSession,viewBean,IPCDroits.ONGLETS_DESSAISISSEMENTS,request,servletContext + mainServletPath)%>	
						
						<div class="conteneurMembres">
						
							<% 
								for(Iterator itMembre=viewBean.getMembres().iterator();itMembre.hasNext();){
									MembreFamilleEtendu membreFamille=(MembreFamilleEtendu)itMembre.next();
							%>
						
						
							<div class="areaMembre" idMembre="<%=membreFamille.getId() %>">
								<div class="areaTitre">
									<%=PCDroitHandler.getFromattedTitreHTML(objSession,membreFamille)%>
								</div>
								<span class="titreTable"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_D_TITRE_AUTO"/></span>
								<table class="areaDessaisiAuto">
									<thead>
										<tr>
											<th><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_L_AUTO_TYPE"/></th>
											<th><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_L_AUTO_DESCRIPTION"/></th>
											<th data-g-amountformatter="blankAsZero:false"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_L_AUTO_MONTANT"/></th>
											<th data-g-periodformatter=" " ><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_L_AUTO_PERIODE"/></th>
										</tr>
									</thead>
									<tbody>
									<%
									for(Iterator itDonneeAuto=viewBean.getDonneesAuto(membreFamille.getId()).iterator();itDonneeAuto.hasNext();){
										DessaisissementRevenuAuto donneeComplexe=(DessaisissementRevenuAuto)itDonneeAuto.next();
									
										String[] colonne=PCDessaisissementHandler.formatDessaisissementRevenuAutoDescription(objSession,donneeComplexe);
										if(colonne==null){
											colonne=new String[]{"DF type not found",
													objSession.getCodeLibelle(donneeComplexe.getSimpleDonneeFinanciereHeader().getCsTypeDonneeFinanciere()),
													""};
										}
										%>
									
										<tr>
											<td title="<%=colonne[0] %>"><%=colonne[3] %></td>
											<td><%=colonne[1] %></td>
											<td><%=colonne[2] %></td>
											<td class="insecable"><%=donneeComplexe.getSimpleDonneeFinanciereHeader().getDateDebut() %> - <%=donneeComplexe.getSimpleDonneeFinanciereHeader().getDateFin() %></td>
										</tr>
							<%
									}
										
							%>										
										
									</tbody>
								</table>
								
								<span class="titreTable"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_D_TITRE_MANUEL"/></span>
								<table class="areaDFDataTable">
									<thead>
										<tr>
											<th data-g-cellformatter="css:formatCellIcon">&nbsp;</th>
											<th><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_L_LIBELLE"/></th>
											<th><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_L_MONTANT_BRUT_DESSAISI"/></th>
											<th><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_L_MONTANT_DEDUCTIONS"/></th>
											<th><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_L_PERIODE"/></th>
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										DessaisissementRevenu donneeComplexe=(DessaisissementRevenu)itDonnee.next();
										
										SimpleDessaisissementRevenu donnee=donneeComplexe.getSimpleDessaisissementRevenu();
										SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td>&#160;</td>
											<td><%=donnee.getLibelleDessaisissement() %></td>
											<td><%=new FWCurrency(donnee.getMontantBrut()).toStringFormat() %></td>
											<td><%=new FWCurrency(donnee.getDeductionMontantDessaisi()).toStringFormat() %></td>
											<td><%=dfHeader.getDateDebut() %> - <%=dfHeader.getDateFin() %></td>
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
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_D_LIBELLE"/></td>
											<td><input type="text" class="libelle" data-g-string="mandatory:true"/>
											 </td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_D_MONTANT_BRUT_DESSAISI"/></td>
											<td><input type="text" class="montantBrutDessaisi" data-g-amount="mandatory:true,periodicity:Y" /></td>
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_D_MONTANT_DEDUCTIONS"/></td>
											<td><input type="text" class="montantDeductions"  data-g-amount="mandatory:false,periodicity:Y" /></td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_D_DATE_DEBUT"/></td>
											<td><INPUT type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_REVENU_D_DATE_FIN"/></td>
											<td><INPUT type="text" name="dateFin" value=""/ data-g-calendar="type:month"></td>
										</tr>
									</table>
									<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_DESSAISISSEMENT_REVENU_AJAX%>" crud="cud">
										<!--<pe:DroitDFButtons droitName="viewBean.droit">
											<input class="btnAjaxDelete" type="button" value="<%=btnDelLabel%>">
											<input class="btnAjaxAdd" type="button" value="<%=btnNewLabel%>">
											<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
											<input class="btnAjaxCancel" type="button" value="<%=objSession.getLabel("JSP_PC_SGL_D_ANNULER")%>">
											<input class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
										</pe:DroitDFButtons>-->
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