<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%	

	idEcran="PRF0047";

	RFPrestationsAccordeesViewBean viewBean = (RFPrestationsAccordeesViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getId();
	bButtonDelete = false;
	bButtonUpdate = true;
	bButtonCancel = true;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.cygnus.vb.prestationsaccordees.RFPrestationsAccordeesViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" />
<ct:menuChange displayId="options" menuId="cygnus-optionsprestationacc" showTab="options" />
<SCRIPT language="javascript"> 
	
	function add(){
	}
	
	function upd(){
		document.getElementsByName("dateDebut")[0].disabled=true;
		//document.getElementsByName("dateFin")[0].disabled=true;
	}
	
	function validate() {
	    state = true;
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_PRESTATION_ACCORDEE%>.ajouter";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_PRESTATION_ACCORDEE%>.modifier";
	    }
	    return state;
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
		  document.forms[0].elements('userAction').value="back";
		}else{
		  document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_PRESTATION_ACCORDEE%>" + ".chercher";
		}
	}

	function init(){
	}
	
	function postInit(){
	    if (document.forms[0].elements('_method').value == "add"){		
			action('add');
	    }	         
	}	
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
		<%-- tpl:put name="zoneTitle" --%>
		<ct:FWLabel key="JSP_RF_PRE_ACC_D_TITRE"/>				
		<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain" --%>
	<!-- informations sur le bénéficiaire de la prestation accordée -->
	<TR><TD colspan="4">&nbsp;</TD></TR>
	<TR valign="top">
		<TD><ct:FWLabel key="JSP_OVE_D_BENEFICIAIRE"/></TD>
		<TD colspan="4" >
			<%=viewBean.getDetailAssure()%>
		</TD>		
	</TR>
	<TR><TD colspan="4">&nbsp;</TD></TR>
	<TR valign="top">	
		<TD><ct:FWLabel key="JSP_OVE_D_ADRESSE_TIERS"/></TD>
		<TD>
			<%=viewBean.getAdresse(viewBean.getIdTiers())%>
		</TD>
		<TD>&nbsp;</TD>	
		<TD><ct:FWLabel key="JSP_OVE_D_ADRESSE_PAIEMENT"/></TD>
		<TD >
			<%=viewBean.getAdresse(viewBean.getIdAdressePaiement())%>
		</TD>		
	</TR>
	<TR><TD colspan="4">&nbsp;</TD></TR>
	<TR>
		<TD><ct:FWLabel key="JSP_OVE_D_RENTE_ACCORDEE"/></TD>
		<TD >
			<INPUT width="500px" type="text" name="idRFMAccordee" value="<%=viewBean.getIdRFMAccordee()%>" readonly class="disabled" >
		</TD>	
	</TR>
	<TR><TD colspan="4">&nbsp;</TD></TR>
	<TR valign="top">
		<TD><ct:FWLabel key="JSP_OVE_D_DATE_DECISION"/></TD>
		<TD >
			<INPUT width="500px" type="text" name="dateValidationDecision" value="<%=viewBean.getDateValidationDecision()%>" readonly class="disabled" >
		</TD>
		<TD>&nbsp;</TD>
		<TD><ct:FWLabel key="JSP_OVE_D_REFERENCE_PAIEMENT"/></TD>
		<TD >
			<textarea name="referencePaiement" cols="70" rows="3"><%=viewBean.getReferencePaiement()%></textarea>			
		</TD>			
	</TR>	
	<TR><TD colspan="6"><hr/></TD></TR>
	<TR>
		<TD><ct:FWLabel key="JSP_OVE_D_GENRE_PRESTATION"/></TD>
		<TD >
			<INPUT width="500px" type="text" name="genrePrestation" value="<%=objSession.getCodeLibelle(viewBean.getCsGenrePrestationAccordee())%>" readonly class="disabled" >
		</TD>
	</TR>
	<TR><TD colspan="4">&nbsp;</TD></TR>	
	<TR>
		<TD><ct:FWLabel key="JSP_OVE_D_MONTANT_PRESTATION"/></TD>
		<TD>
			<INPUT width="500px" type="text" name="montantPrestation" value="<%=viewBean.getMontantPrestation()%>" readonly class="disabled" >
		</TD>
	</TR>	
	<TR><TD colspan="4">&nbsp;</TD></TR>
	<TR>		
		<TD><ct:FWLabel key="JSP_OVE_D_DATE_DEBUT" /></TD>
		<TD><input data-g-calendar="type:month"  name="dateDebutDroit" value="<%=viewBean.getDateDebutDroit()%>" /></TD>		
		<TD>&nbsp;</TD>
		<TD><ct:FWLabel key="JSP_OVE_D_DATE_FIN" /></TD>
		<TD><input data-g-calendar="type:month"  name="dateFinDroit" value="<%=viewBean.getDateFinDroit()%>"/></TD>		
	</TR>	
	<TR><TD colspan="4">&nbsp;</TD></TR>
	<TR>
		<TD><ct:FWLabel key="JSP_OVE_D_ETAT"/></TD>
		<TD>
			<INPUT width="500px" type="text" name="etatPrestation" value="<%=objSession.getCodeLibelle(viewBean.getCsEtatRE())%>" readonly class="disabled" >
		</TD>
		<TD><ct:FWLabel key="JSP_OVE_D_SOURCE"/></TD>
		<TD>
			<INPUT width="500px" type="text" name="source" value="<%=objSession.getCodeLibelle(viewBean.getCs_source())%>" readonly class="disabled" >
		</TD>
	</TR>		
		
				
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>