
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCO2003"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="globaz.aquila.servlet.CODefaultServletAction" %>
<%@ page import="globaz.aquila.db.print.*" %>
<%@ page import="globaz.aquila.db.access.batch.*"%>
<%@ page import="globaz.osiris.db.comptes.CATypeSection" %>
<%@ page import="globaz.osiris.db.comptes.CATypeSectionManager" %>
<%
COListDossiersEtapeViewBean viewBean = (COListDossiersEtapeViewBean) session.getAttribute("viewBean");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.globall.db.BSessionUtil"%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
<%
userActionValue = "aquila.print.listDossiersEtape.executer";
%>
top.document.title = "<ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_TITLE' />  - " + top.location.href;

function griseCase(){
	
	
	if(document.getElementById("forTypeExecutionEE").checked){
		document.getElementById("blocage").disabled=true;
		$("#blocage").attr('checked','checked');
	}else{
		document.getElementById("blocage").disabled=false;
	}
	
}


$(function(){
	
	griseCase();
	
	<%if(!JadeStringUtil.isBlankOrZero(viewBean.getForSelectionRole())){%>
		$("#forSelectionRole").val("<%=viewBean.getForSelectionRole()%>");	
	<%}%>
	
	<%if(!JadeStringUtil.isBlankOrZero(viewBean.getForIdCategorie())){%>
		$("#forIdCategorie").val("<%=viewBean.getForIdCategorie()%>");	
	<%}%>

	<%if(!JadeStringUtil.isBlankOrZero(viewBean.getForIdTypeSection())){%>
		$("#forIdTypeSection").val("<%=viewBean.getForIdTypeSection()%>");	
	<%}%>
	
	<%if(!JadeStringUtil.isBlankOrZero(viewBean.getForIdSequence())){%>
		$("#forIdSequence").val("<%=viewBean.getForIdSequence()%>");	
	<%}%>
	
	<%if(!JadeStringUtil.isBlankOrZero(viewBean.getForSoldeOperator())){%>
		$("#forSoldeOperator").val("<%=viewBean.getForSoldeOperator()%>");	
	<%}%>
	
	<%if(!JadeStringUtil.isBlankOrZero(viewBean.getForIdEtapeForListDossierEtape())){%>
		$("#forIdEtapeForListDossierEtape").val("<%=viewBean.getForIdEtapeForListDossierEtape()%>");	
	<%}%>

});

var jsonDataMultiSelectSequenceEtapes = <%=viewBean.createJsonDataMultiSelectSequenceEtapes() %>;
var jsonInitValueMultiSelectSequenceEtapes = <%=viewBean.createJsonInitValueMultiSelectSequenceEtapes() %>;

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_TITLE' /><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<tr>
		<td class="label"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_MAIL' /></td>
		<td class="control" colspan="5">
			<input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
		</td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_NUMERO_AFFILIE_DEPART' /></td>
		<td class="control"><input type="text" name="fromNumAffilie" value="<%=viewBean.getFromNumAffilie()%>" ></td>
		<td class="label"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_NUMERO_AFFILIE_FIN' /></td>
		<td class="control"><input type="text" name="beforeNumAffilie" value="<%=viewBean.getBeforeNumAffilie()%>" ></td>
		<td class="label"></td>
		<td class="control"></td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_ROLE' /></td>
		<td class="control">
			<select id="forSelectionRole" name="forSelectionRole"  >
              	<%=globaz.aquila.db.irrecouvrables.CORoleViewBean.createOptionsTags(viewBean.getSession(), request.getParameter("forIdRole"))%>
            </select>
        </td>
		<td class="label"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_CATEGORIE' /></td>
		<td class="control">
              	<%
              		String selectCategorieSelect = globaz.osiris.parser.CASelectBlockParser.getForIdCategorieSelectBlock(viewBean.getSession(), true);

				if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCategorieSelect)) {
					out.print(selectCategorieSelect);
				}
              	%>
		</td>
		<TD class="label"></TD>
		<TD class="control">
		</TD>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_TYPE_SECTION' /></td>
		<TD class="control">
			<select id="forIdTypeSection" name="forIdTypeSection" tabindex="3">
                <option value=""><ct:FWLabel key='TOUS' /></option>
                <%CATypeSection tempTypeSection;
					 		CATypeSectionManager manTypeSection = new CATypeSectionManager();
							manTypeSection.setSession(viewBean.getSession());
							manTypeSection.find();
							for(int i = 0; i < manTypeSection.size(); i++){
								tempTypeSection = (CATypeSection)manTypeSection.getEntity(i); %>
                <option value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
                <% } %>
              </select>
		</TD>
		
		<td class="label"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_NUMERO_SECTION' /></td>
		<td class="control"><input type="text" name="likeNumSection" id="idSection" title="<ct:FWLabel key='DOSSIER_ETAPE_INFO_BULLE_NUMERO_SECTION'/>" value="<%=viewBean.getLikeNumSection()%>" ></td>
	</tr>
	<tr>	
		<TD class="label"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_SEQUENCE' /></TD>
		<TD class="control">
			
			<select id=forIdSequence name="forIdSequence" 
				data-g-multiselect=" dataList:jsonDataMultiSelectSequenceEtapes,initValues :jsonInitValueMultiSelectSequenceEtapes, subLevelId:forIdEtapeForListDossierEtape">
			</select>
	
		</TD>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_DATE_DEBUT' /></td>
		<td class="control"><ct:FWCalendarTag name="fromDateDebut" doClientValidation="CALENDAR" value="<%=viewBean.getFromDateDebut()%>"/></td>
		<td class="label"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_DATE_FIN' /></td>
		<td class="control">
			<ct:FWCalendarTag name="toDateFin" doClientValidation="CALENDAR" value="<%=viewBean.getToDateFin()%>"/>
		</td>
		<td>
			<input type="radio"  name="forTypeDate" value="DateE" <%= (JadeStringUtil.isBlankOrZero(viewBean.getForTypeDate()) || "DateE".equals(viewBean.getForTypeDate()))?"checked":"" %> ><label><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_DATE_EXECUTION' /></label>
			<br/>
			<input type="radio"  name="forTypeDate" value="DateD" <%= "DateD".equals(viewBean.getForTypeDate())?"checked":"" %>  ><label><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_DATE_DECLENCHEMENT' /></label>
		</td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_SOLDE' /></td>
		<td class="control" colspan="5">
			<select  id="forSoldeOperator" name="forSoldeOperator">
				<option value="=" >=</option>
				<option value="&gt;=">&gt;=</option>
				<option value="&lt;=">&lt;=</option>
				<option value="&lt;&gt;" selected>&lt;&gt;</option>
			</select>
			<ct:inputText name="forSolde" id="forSolde" defaultValue="0.00" notation="data-g-amount='blankAsZero:false'" />
		</td>
	</tr>
	<tr>
		<TD class="label"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_ETAPE' /></TD>
		<TD class="control" colspan="3">
			<select id="forIdEtapeForListDossierEtape" name="forIdEtapeForListDossierEtape"></select>
		</TD>	
		<td>
				<input type="radio" id="forTypeExecutionEE" name="forTypeExecution" value="EE" <%= (JadeStringUtil.isBlankOrZero(viewBean.getForTypeExecution()) || "EE".equals(viewBean.getForTypeExecution()))?"checked":"" %> onClick='griseCase();'><label for="forTypeExecutionEE" ><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_DERNIERE_ETAPE_EXECUTEE' /></label>	
				<br/>
				<input type="radio" id="forTypeExecutionEP" name="forTypeExecution" value="EP" <%= "EP".equals(viewBean.getForTypeExecution())?"checked":"" %>  onClick='griseCase();'><label for="forTypeExecutionEP"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_PROCHAINE_ETAPE_POTENTIELLE' /></label>
		</td>	
	</tr>
	<tr>
		<TD class="label"><ct:FWLabel key='JSP_LISTE_DOSSIER_ETAPE_BLOCAGE_INACTIF' /></TD>
		<td><input id="blocage" type="checkbox" name="blocageInactif" <%= viewBean.getBlocageInactif().booleanValue()?"checked":"" %> >  </td>
	</tr>       
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>