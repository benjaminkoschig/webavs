	<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
	
<%@page import="globaz.corvus.vb.adaptation.RERentesAdapteesJointRATiersViewBean"%><script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/libraRoot/librataglib.tld" prefix="li" %>
<%

	idEcran="PRE0102";

	RERentesAdapteesJointRATiersViewBean viewBean = (RERentesAdapteesJointRATiersViewBean) session.getAttribute("viewBean");

	bButtonDelete = false;
	bButtonUpdate = false;

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="corvus-optionsrentesadaptees">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdRenteAdaptee()%>"/>
	<ct:menuSetAllParams key="idRenteAccordee" value="<%=viewBean.getIdPrestationAccordee()%>"/>  
</ct:menuChange>

<script language="JavaScript">

  function readOnly(flag) {
  	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
    for(i=0; i < document.forms[0].length; i++) {
        if (!document.forms[0].elements[i].readOnly &&
        	document.forms[0].elements[i].className != 'forceDisable' &&
        	document.forms[0].elements[i].type != 'hidden') {
            document.forms[0].elements[i].disabled = flag;
        }
    }
  }

  function add() {
  }

  function upd() {
  }

  function cancel() {	  	
  }
	   
  function validate() { 	    
  }
  
  function init(){
  }

  function postInit(){
  }

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_TITLE_DE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colpsan="6" align="center">
			<table width="95%">
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">							
							<tr>
								<td width="30%"><b><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_DET_ASSURE"/></b></td>
								<td width="70%" colspan="3">
									<b><%=viewBean.getNssRA() +" / "+ viewBean.getNomRA() +" "+ viewBean.getPrenomRA()%></b>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ID"/></td>
								<td width="20%">
									<%=viewBean.getIdRenteAdaptee() %>
									<input type="hidden" name="idRenteAdaptee" value="<%=viewBean.getIdRenteAdaptee()%>">
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NO_RA"/></td>
								<td width="20%">
									<%=viewBean.getIdPrestationAccordee()%>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_CS_TYPE"/></td>
								<td width="70%" colspan="3">
									<%=viewBean.getSession().getCodeLibelle(viewBean.getCsTypeAdaptation())%>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_GENRE_PREST"/></td>
								<td width="70%" colspan="3">
									<%=viewBean.getCodePrestation()+"."+viewBean.getFractionRente()%>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">							
							<tr>
								<td colspan="4" ><b><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_DET_ADA"/></b></td>
							</tr>
						</table>
					</td>
				</tr>									
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">							
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANC_RAM"/></td>
								<td width="20%">
									<input type="text" name="ancienRam" value="<%=viewBean.getAncienRAM()%>" class="montantDisabled" readonly/>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOU_RAM"/></td>
								<td width="20%">
									<input type="text" name="nouveauRam" value="<%=viewBean.getNouveauRAM()%>" class="montantDisabled" readonly/>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANC_MNT_PRE"/></td>
								<td width="20%">
									<input type="text" name="ancienMontantPrestation" value="<%=viewBean.getAncienMontantPrestation()%>" class="montantDisabled" readonly/>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOU_MNT_PRE"/></td>
								<td width="20%">
									<input type="text" name="nouveauMontantPrestation" value="<%=viewBean.getNouveauMontantPrestation()%>" class="montantDisabled" readonly/>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANC_MNT_REN_ORD_REMP"/></td>
								<td width="20%">
									<input type="text" name="ancienMntRenteOrdinaireRempl" value="<%=viewBean.getAncienMntRenteOrdinaireRempl()%>" class="montantDisabled" readonly/>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOU_MNT_REN_ORD_REMP"/></td>
								<td width="20%">
									<input type="text" name="nouveauMntRenteOrdinaireRempl" value="<%=viewBean.getNouveauMntRenteOrdinaireRempl()%>" class="montantDisabled" readonly/>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANC_SUPP_AJOUR"/></td>
								<td width="20%">
									<input type="text" name="ancienSupplementAjournement" value="<%=viewBean.getAncienSupplementAjournement()%>" class="montantDisabled" readonly/>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOU_SUPP_AJOUR"/></td>
								<td width="20%">
									<input type="text" name="nouveauSupplementAjournement" value="<%=viewBean.getNouveauSupplementAjournement()%>" class="montantDisabled" readonly/>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANC_MNT_RED_ANTI"/></td>
								<td width="20%">
									<input type="text" name="ancienMntReductionAnticipation" value="<%=viewBean.getAncienMntReductionAnticipation()%>" class="montantDisabled" readonly/>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOU_MNT_RED_ANTI"/></td>
								<td width="20%">
									<input type="text" name="nouveauMntReductionAnticipation" value="<%=viewBean.getNouveauMntReductionAnticipation()%>" class="montantDisabled" readonly/>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANC_ANNEE_RAM"/></td>
								<td width="20%">
									<input type="text" name="ancienAnneeMontantRAM" value="<%=viewBean.getAncienAnneeMontantRAM()%>" class="montantDisabled" readonly/>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOU_ANNEE_RAM"/></td>
								<td width="20%">
									<input type="text" name="nouveauAnneeMontantRAM" value="<%=viewBean.getNouveauAnneeMontantRAM()%>" class="montantDisabled" readonly/>
								</td>
							</tr>
							<tr><td colspan="4">&nbsp;</td></tr>
							<tr>
								<td width="30%" valign="top"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_REM_ADAP"/></td>
								<td colspan="3">
									<textarea rows="5" cols="70" readonly class="disabled"><%=viewBean.getRemarquesAdaptation()%></textarea>
								</td>
							</tr>
						</table>
					</td>
				</tr>	
			</table>					
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