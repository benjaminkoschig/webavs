<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0035";

	globaz.apg.vb.prestation.APPrestationJointLotTiersDroitViewBean viewBean = (globaz.apg.vb.prestation.APPrestationJointLotTiersDroitViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdPrestationApg();
	String typePrestation = APGUtils.isTypeAllocationPandemie(viewBean.getGenreService()) ? IPRDemande.CS_TYPE_PANDEMIE :viewBean.getNoRevision().equals(globaz.apg.api.droits.IAPDroitMaternite.CS_REVISION_MATERNITE_2005)?globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE:globaz.prestation.api.IPRDemande.CS_TYPE_APG;
	java.util.Vector v = globaz.apg.db.lots.APLotManager.getIdsDescriptionsLotsOuvertsOuCompenses(viewBean.getSession(),typePrestation);
	bButtonDelete = false;
	
	//bButtonNew=true;
	//bButtonDelete=!viewBean.isDefinitif();
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

	<!--si APG -->
	<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
   		<%@page import="java.util.Vector"%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg"/>
	<!--sinon, maternité -->
	<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>	   	
		<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat"/>
	<%}%>
    
    <%if (viewBean.isOkPourMiseEnLot()){%>
			<ct:menuChange displayId="options" menuId="ap-optionprestationlotok" showTab="options">
				<ct:menuSetAllParams key="idPrestationCourante" value="<%=viewBean.getIdPrestationApg()%>"/>
				<ct:menuSetAllParams key="genreService" value="<%=viewBean.getGenreService()%>"/>
				<ct:menuSetAllParams key="idDroit" value="<%=viewBean.getIdDroit()%>"/>
				<ct:menuSetAllParams key="forIdPrestation" value="<%=viewBean.getIdPrestationApg()%>"/>
				<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPrestationApg()%>"/>	
			</ct:menuChange>
	<% } else { %>
			<ct:menuChange displayId="options" menuId="ap-optionprestationlotpasok" showTab="options">
				<ct:menuSetAllParams key="idPrestationCourante" value="<%=viewBean.getIdPrestationApg()%>"/>
				<ct:menuSetAllParams key="genreService" value="<%=viewBean.getGenreService()%>"/>
				<ct:menuSetAllParams key="idDroit" value="<%=viewBean.getIdDroit()%>"/>
				<ct:menuSetAllParams key="forIdPrestation" value="<%=viewBean.getIdPrestationApg()%>"/>
			</ct:menuChange>
	<%}%>

<SCRIPT language="javascript"> 
	function postInit(){
		//va initialiser les "disabled" de certains champs suivant que la checkbox est cochée ou pas.
		clicSurCreerLot();
	}
	
	function add() {
	    //document.forms[0].elements('userAction').value="apg.lots.lot.ajouter";
	}
	
	function upd() {
	}
	
	function validate() {
	    state = true;
	    //Le bouton valider renverra sur l'action mettreDansLot (qui créera un lot au besoin)
	        document.forms[0].elements('userAction').value="apg.prestation.prestationJointLotTiersDroit.actionMettreDansLot";
	    return state;
	
	}
	
	function cancel() {
	
	if (document.forms[0].elements('_method').value == "add")
	  document.forms[0].elements('userAction').value="back";
	 else
	  document.forms[0].elements('userAction').value="apg.prestation.prestationJointLotTiersDroit.chercher";
	  
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="apg.prestation.prestationJointLotTiersDroit.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
		
	}
	
	function mettreDansLot(){
		document.forms[0].elements('userAction').value="apg.prestation.prestationJointLotTiersDroit.actionMettreDansLot";
		document.forms[0].submit();
		
	}
	
	function clicSurCreerLot(){
		if (document.forms[0].elements('creerLot').checked){
			document.forms[0].elements('idLot').disabled=true;
			document.forms[0].elements('idLot').value="";
			document.forms[0].elements('descriptionLot').disabled=false;
		} else{
			document.forms[0].elements('idLot').disabled=false;
			<%if (v.size()>0){%>
			document.forms[0].elements('idLot').value="<%=((String[])v.get(0))[0]%>";
			<%}%>
			document.forms[0].elements('descriptionLot').disabled=true;
			document.forms[0].elements('descriptionLot').value="";
		}
	}	
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_AJOUTER_PRESTATION_A_UN_LOT"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_NO_PRESTATION"/></TD>
							<TD><INPUT type="text" class="disabled" readonly value="<%=viewBean.getIdPrestationApg()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_LOTS_OUVERTS"/></TD>
							<TD>
								
								<%if (v!=null && v.size()>0){%>

									<% String defaut =  ((String[])v.get(0))[0]; %>				

									<ct:FWListSelectTag 
											data="<%=v%>"
											defaut="<%=defaut%>"																																												 										
											name="idLot"/>	
								<%}else{%>
									<ct:FWLabel key="JSP_AUCUN_LOT_OUVERT"/>
								<%}%>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_CREER_NOUVEAU_LOT"/></TD>
							<TD><INPUT type="checkbox" name="creerLot" onclick="clicSurCreerLot()" <%=v.size()==0?"CHECKED readonly disabled":""%>></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DESCRIPTION"/></TD>
							<TD><INPUT type="text" name="descriptionLot" size="40" maxlength="40"></TD>
						</TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>