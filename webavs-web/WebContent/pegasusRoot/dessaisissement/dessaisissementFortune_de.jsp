	<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCParametre"%>
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
<%@page import="ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementFortune"%>
<%@page import="ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortune"%>
<%@page import="ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneAuto"%>
<%@page import="globaz.pegasus.utils.PCDessaisissementHandler"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="globaz.pegasus.utils.PCCommonHandler"%>

<%
	//Les labels de cette page commencent par le préfix "JSP_PC_DESSAISISSEMENT_FORTUNE_D"
	idEcran="PPC0023";

	PCDessaisissementFortuneViewBean viewBean = (PCDessaisissementFortuneViewBean) session.getAttribute("viewBean");
	
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
	String autreMotilLbl = objSession.getCodeLibelle(IPCDroits.CS_AUTRES_MOTIF_DESSAISISSEMENT);
	
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/droit/commonDroit.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/droit/fortuneParticuliere_de.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/droit/dessaisissementFortune_de.css"/>


<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId() %>";
	var ACTION_AJAX="<%=IPCActions.ACTION_DROIT_DESSAISISSEMENT_FORTUNE_AJAX%>";
	var CS_TYPE_VALEUR_AFC = "<%=IPCParametre.CS_1000_DIVISE_VAL%>";
</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/DessaisissementFortune_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/DessaisissementFortune_de.js"/></script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<%=PCCommonHandler.getTitre(objSession,request)%>
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
								<span class="titreTable"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_TITRE_AUTO"/></span>						
								<table class="areaDessaisiAuto">
									<thead>
										<tr>
											<th><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_L_AUTO_TYPE"/></th>
											<th><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_L_AUTO_DESCRIPTION"/></th>
											<th><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_L_AUTO_MONTANT"/></th>
											<th data-g-periodformatter=" "><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_L_AUTO_PERIODE"/></th>
										</tr>										
									</thead>
									<tbody>
									<%
									for(Iterator itDonneeAuto=viewBean.getDonneesAuto(membreFamille.getId()).iterator();itDonneeAuto.hasNext();){
										DessaisissementFortuneAuto donneeComplexe=(DessaisissementFortuneAuto)itDonneeAuto.next();
									
										String[] colonne=PCDessaisissementHandler.formatDessaisissementFortuneAutoDescription(objSession,donneeComplexe);
										if(colonne==null){
											colonne=new String[]{"DF type not found",
													objSession.getCodeLibelle(donneeComplexe.getSimpleDonneeFinanciereHeader().getCsTypeDonneeFinanciere()),
													""};
										}
										%>
									
										<tr>
											<td><%=colonne[0] %></td>
											<td><%=colonne[1] %></td>
											<td><%=new FWCurrency(colonne[2]).toStringFormat() %></td>
											<td><%=donneeComplexe.getSimpleDonneeFinanciereHeader().getDateDebut() %></td>
										</tr>
							<%
									}
										
							%>										
										
									</tbody>
								</table>
								
								<span class="titreTable"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_TITRE_MANUEL"/></span>
								<table class="areaDFDataTable">
									<thead>
										<tr>
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_L_MOTIF"/></td>
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_L_MONTANT_BRUT_DESSAISI"/></td>
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_L_MONTANT_DEDUCTIONS"/></td>
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_L_CONTRE_PRESTATION"/></td>
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_L_PERIODE"/></td>
										</tr>
									</thead>
									<tbody>
							<%
									String idGroup=null;
									for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
										DessaisissementFortune donneeComplexe=(DessaisissementFortune)itDonnee.next();

										SimpleDessaisissementFortune donnee=donneeComplexe.getSimpleDessaisissementFortune();
										SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();
										if(!dfHeader.getIdEntityGroup().equals(idGroup)){
											idGroup=null;
										}
							%>
										<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
											<td><%=(IPCDroits.CS_AUTRES_MOTIF_DESSAISISSEMENT.equals(donnee.getCsMotifDessaisissement()))?autreMotilLbl+" - "+donnee.getAutreMotifDessaisissement():objSession.getCodeLibelle(donnee.getCsMotifDessaisissement()) %></td>
											<td><%=new FWCurrency(donnee.getMontantBrut()).toStringFormat() %></td>
											<td><%=new FWCurrency(donnee.getDeductionMontantDessaisi()).toStringFormat() %></td>
											<td><% if(donnee.getIsContrePrestation().booleanValue()){%>
												<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
												<%} else {
													%>&nbsp;<%
												}%></td>
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
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_MOTIF"/></td>
											<td><ct:select styleClass="csMotif"  name="champTypeDePropriete">
													<ct:optionsCodesSystems csFamille="PCMOTDES"/>
												</ct:select>
											 </td>
											<td class="motifAutreDisplay"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_MOTIF_AUTRE"/></td>
											<td class="motifAutreDisplay"><input type="text" class="motifAutre" /></td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_MONTANT_BRUT_DESSAISI"/></td>
											<td><input type="text" class="montantBrutDessaisi" data-g-amount="mandatory:true, periodicity:Y" /></td>
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_MONTANT_DEDUCTIONS"/></td>
											<td><input type="text" class="montantDeductions" data-g-amount="mandatory:true, periodicity:Y" /></td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CONTRE_PRESTATION"/></td>
											<td>
											  <input type="checkbox" class="contrePrestation" 
											  		 data-g-commutator="context:$(this).parents('.areaDFDetail'),
											  		 					condition:$(this).prop('checked')==true,
											  		 					actionTrue:¦show(context.find('.hideContrePres'))¦,
											  		 					actionFalse:¦hide(context.find('.hideContrePres'))¦"
											   />
											</td>
											<td class='hideContrePres'><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_RENDEMENT_FORTUNE"/></td>
											<td class='hideContrePres'><input type="text" class="rendementFortune" data-g-amount="mandatory:true, periodicity:Y" /></td>
										</tr>
										<tr>
											<td colspan="2"> </td>
											<td class='hideContrePres'><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CHARGES"/></td>
											<td class='hideContrePres'><input type="text" class="charges" data-g-amount="mandatory:true, periodicity:Y" /></td>
										</tr>
										<tr class="ligneAfficheCalcul hideContrePres"> 
											<td colspan="2"> </td>
											<td colspan="4"> 
											   <span class="label resCal"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_DF"/></span>
											   <span class="resultatCalcul" 		    
											         data-g-commutator="context:$(this).parents('.areaDFDetail'),
											  		        		    condition:$.trim(context.find('.resultatCalcul').text())!='',
											  		 		     	    actionTrue:¦show(context.find('.resCal'))¦,
											  		 			        actionFalse:¦hide(context.find('.resCal'))¦"></span>
											   <span class="afficheCalcul resCal"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_AFFICHE_CALCULE"/></span><br />									   
											</td>
										</tr>
										<tr>
											<td><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_DATE_DEBUT"/></td>
											<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
										</tr>
									</table>
									<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_DESSAISISSEMENT_FORTUNE_AJAX%>" crud="cud">
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
							<div title='<ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_TITRE"/>' class="calculContrePrestation"> 
							  	<div class="titre"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_CONTRE_PRESATION"/></div><br />
							    <div id="spanDate"><span class="label"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_DATE_DONATION"/></span><span class="dateDebut"></span></div><br />
							  	<span class="label"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_VALEUR_OFFICIEL"/></span><span class="montantBrutDessaisi valeur"></span><br />
							  	<span class="label"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_DETTE"/></span><span class="valeur montantDeductions"></span><br />
							  	<span class="label"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_MONTANT_NET_DESSAISI"/> </span> <span class="resultat montantNetDuBien"></span><br />
							  	
							  	<div class="titre"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_CONTRE_PRESTATION"/></div><br />
							  	<span class="label"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_RENDEMENT_NET_FORTUNE"/></span> <span class="valeur rendementFortune"></span><br />
							  	<span class="label"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_CHARGES"/></span><span class="valeur charges"></span><br />
							    <span class="label"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_RENDEMENT_FORTUNE"/></span> <span class="resultat rendementNet"></span><br />
							    
							    <div id="facteur">
								    <div class="titre"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_FACTEUR_CAPITALISATION"/></div><br />
								    <span class="label"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_MADAME"/></span><span class="valeur facteur_F"></span><br />
								    <span class="label"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_MONSIEUR"/></span><span class="valeur facteur_H"></span><br />
							    </div>
							    
							    <div class="titre"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_CALCUL"/></div><br />
							    <span class="label"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_MONTANT_NET_BIEN"/></span><span class="valeur montantNetDuBien"></span><br />
							    <span class="label"><span id="textCalcul"></span></span><span class="valeur rendementNetAvecFateur"/></span><br />
							    <span class="label"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_RESULTAT_FINAL"/></span><span class="resultatCalculFinal resultatCalcul"></span><br />
							    <span class="label"><ct:FWLabel key="JSP_PC_DESSAISISSEMENT_FORTUNE_D_CAL_TOTAL_ARRONDI"/></span><span class="totalArrondi resultat"></span><br />
							</div>
						</div>
					</div>
				</TD>
			</TR>
			<tr>
			  <td> 
				
			  </td>
			</tr>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>