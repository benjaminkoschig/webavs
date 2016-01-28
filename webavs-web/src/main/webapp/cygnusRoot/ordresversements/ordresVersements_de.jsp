<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@page import="globaz.cygnus.vb.ordresversements.RFOrdresVersementsViewBean"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.cygnus.api.ordresversements.IRFOrdresVersements"%>
<%@page import="globaz.corvus.api.lots.IRELot"%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	
	//Author : FHA

	// Les labels de cette page commence par la préfix "JSP_OVE_D"
	idEcran="PRF0053";

	RFOrdresVersementsViewBean viewBean = (RFOrdresVersementsViewBean) session.getAttribute("viewBean");	

	selectedIdValue = viewBean.getIdOrdreVersement();	
	
	String idTiersRequerant 		= request.getParameter("idTiers"); 
	String idTiersAdressePaiement	= request.getParameter("idTiersAdressePaiement");
	String montant					= request.getParameter("montant");
	String montantDepassementQD		= request.getParameter("montantDepassementQD");
	String typeVersement			= request.getParameter("typeVersement");
	
	bButtonNew 		= false;
	bButtonDelete 	= false;
	if((viewBean.getTypeVersement().equals(IRFOrdresVersements.CS_TYPE_RESTITUTION) || 
		viewBean.getTypeVersement().equals(IRFOrdresVersements.CS_TYPE_DETTE)) &&
			!viewBean.getCsEtatLot().equals(IRELot.CS_ETAT_LOT_VALIDE) &&
			!viewBean.isPrestationValidee()){
		bButtonUpdate 	= true;
	}else{
		bButtonUpdate 	= false;
	}

	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>	
<script language="JavaScript">

	function init(){
		<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
		  	// mise a jour de la liste du parent
			if (parent.document.forms[0]) {
				parent.document.forms[0].submit();
			}
		<%}%>		
	}
	
	function add() {
	}
	
	function validate() {
	    state = true;
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_ORDRES_VERSEMENTS%>.ajouter";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_ORDRES_VERSEMENTS%>.modifier";
	    }
	    return state;
	}
	
	function upd(){
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
	    	document.forms[0].elements('userAction').value="back";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_ORDRES_VERSEMENTS%>.chercher";
	    }	  
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_ORDRES_VERSEMENTS%>.supprimer";
	        document.forms[0].submit();
	    }
	}
	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_OVE_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<%-- tpl:put name="zoneMain" --%>
			<TR><TD colspan="4">&nbsp;</TD></TR>
			<TR>
				<TD><ct:FWLabel key="JSP_OVE_D_BENEFICIAIRE"/></TD>
				<TD>
					<INPUT size="60" type="text" name="beneficiaire" value="<%=viewBean.getBeneficiaire(idTiersRequerant)%>" readonly class="disabled">
				</TD>
				<TD><ct:FWLabel key="JSP_OVE_D_TYPE"/></TD>
				<TD width="300px">
					<INPUT size="30" type="text" name="typeVersement" value="<%=objSession.getCodeLibelle(typeVersement) %>" readonly class="disabled">
				</TD>
			</TR>
			<TR><TD colspan="4">&nbsp;</TD></TR>
			<TR valign="top">	
				<TD><ct:FWLabel key="JSP_OVE_D_ADRESSE_PMT"/></TD>
				<TD>					
					<PRE><span class="IJAfficheText"><%=viewBean.getAdressePaiement(idTiersAdressePaiement)%></span></PRE>
				</TD>
			</TR>
			<TR>
				<TD><ct:FWLabel key="JSP_OVE_D_MONTANT_INITIAL"/></TD>				
				<TD><INPUT type="text" name="montant" value="<%=new FWCurrency(montant).toStringFormat()%>" readonly class="montantDisabled"></TD>
				<%if(viewBean.getTypeVersement().equals(IRFOrdresVersements.CS_TYPE_RESTITUTION) || viewBean.getTypeVersement().equals(IRFOrdresVersements.CS_TYPE_DETTE)){%>
					<TD><ct:FWLabel key="JSP_OVE_D_A_COMPENSER"/></TD>
					<TD><INPUT type="checkbox" value="on" name="isCompense" <%=viewBean.getIsCompense().booleanValue()?"CHECKED":""%>/></TD>
				<% } %>
			</TR>
			<TR>
				<% if(!JadeStringUtil.isEmpty(montantDepassementQD) 
						&& new BigDecimal(montantDepassementQD).compareTo(new BigDecimal(0))!=0){ %>
					<TD><ct:FWLabel key="JSP_OVE_D_MONTANT_DEPASSEMENT_QD"/></TD>
					<TD><INPUT type="text" name="montantDepassementQD" value="<%=new FWCurrency(montantDepassementQD).toStringFormat()%>" readonly class="montantDisabled"></TD>
				<% } %>
			</TR>
		
				
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty"/>			
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>