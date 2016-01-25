<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.helios.parser.CGPieceIncrementor"%>
<style type="text/css">
<!--
.txtLink {font-family: Verdana, Arial, Helvetica, sans-serif; font-size:10px; font-weight:bold; text-decoration: none; margin-left: 0px}
-->
</style>

<!-- Creer l'enregitrement s'il n'existe pas -->

<%@ page import="globaz.helios.db.modeles.*" %>
<%@ page import="globaz.helios.db.comptes.CGEnteteEcritureViewBean" %>
<%
	idEcran = "GCF0022";
	CGEnteteEcritureViewBean viewBean = (CGEnteteEcritureViewBean) session.getAttribute("viewBean");

	globaz.helios.tools.CGSessionDataContainerHelper sessionContainerHelper = new globaz.helios.tools.CGSessionDataContainerHelper();
	CGEnteteEcritureViewBean lastEcritureAdded = (CGEnteteEcritureViewBean)sessionContainerHelper.getData(session,globaz.helios.tools.CGSessionDataContainerHelper.KEY_LAST_ECRITURE_DOUBLE_ADDED);

	boolean prefillData = false;
	if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add")) && lastEcritureAdded!=null)  {
		prefillData = true;
	}

	userActionValue = "helios.comptes.ecritureModele.chercher";

	if ((request.getParameter("idJournal") == null) || ("".equals(request.getParameter("idJournal")))) {
		bButtonCancel = false;
	}

	bButtonDelete = false;
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
function validate() {
    state = validateFields();
    document.forms[0].elements('userAction').value="<%=userActionValue%>";

    return state;
}

function cancel() {
  document.forms[0].elements('userAction').value="back";
}

function init() {
}

function add() {}

function fillCell(cell) {

		/**
		* touche '=' pressée
		*/

		<%if (prefillData) {%>



		if (event.keyCode==61 && cell.value=='') {
			if (cell.name=='date') {
				cell.value="<%=lastEcritureAdded.getDate()%>";
			}
			else if (cell.name=='libelle') {
				cell.value="<%=lastEcritureAdded.getLibelle()%>";
			}
			else if (cell.name=='piece') {
				cell.value="<%=lastEcritureAdded.getPiece()%>";
			}
			else if (cell.name=='idLivre') {
				cell.value="<%=lastEcritureAdded.getIdLivre()%>";
			}
			else if (cell.name=='idExterneCompteDebite') {
				cell.value="<%=lastEcritureAdded.getIdExterneCompteDebite()%>";
			}
			else if (cell.name=='idExterneCompteCredite') {
				cell.value="<%=lastEcritureAdded.getIdExterneCompteCredite()%>";
			}
			event.keyCode=null;
		}
		<%}%>
}

	function updateSelectedIdModel(tag){
		if(tag.select && tag.select.selectedIndex != -1){
			if (tag.select[tag.select.selectedIndex].selectedIdModel) {
				document.getElementById('selectedIdModel').value = tag.select[tag.select.selectedIndex].selectedIdModel;
			}
		}
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ajout d'écritures par modèles<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
				<tr><td><table>
				<%
					if ((request.getParameter("idJournal") != null) && (!"".equals(request.getParameter("idJournal")))) {
				%>
				<tr>
					<td>Numéro d'écriture</td>
					<td width="424"><input type="text" name="idEnteteEcriture" class="libelleDisabled" readonly value=""/></td>
				</tr>
				<tr>
					<td>Numéro du journal</td>
					<td width="424"><input type="text" name="idJournal" class="libelleDisabled" readonly tabindex="-1" value="<%=request.getParameter("idJournal")%>"/>
					</TD>
				</tr>
				<%
					} else {
				%>
				<tr>
					<td>Libellé journal</td>
					<td width="424">
					<input type="hidden" name="idJournal" value=""/>
					<input type="text" name="libelleJournal" class="libelle" value="<%=request.getParameter("libelleJournal")%>" size="25"/>
					</TD>
				</tr>
				<%
					}
				%>

				<tr>
					<td>Date [=]</td>
					<%
						String date = "";
						if (request.getParameter("date") != null) {
							date = request.getParameter("date");
						}
					%>

					<TD width="424"><ct:FWCalendarTag name="date" value="<%=date%>"/> *</TD>
				</tr>
				<script language="javascript">
					 	  	element = document.getElementById("date");
					 	  	element.onkeypress=function() {fillCell(this);}
			 	</script>

				<tr>
					<td>Modèle</td>
					<td>
					<input type="hidden" name="interMandat" value="<%=request.getParameter("interMandat")%>"/>
					<%
						int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();
						String jspEcrModelesSelectLocation = servletContext + mainServletPath + "Root/ecrmodeles_select.jsp";

						String params = "idMandat=";
						String interMandat = request.getParameter("interMandat");
						globaz.helios.db.comptes.CGExerciceComptableViewBean exerciceComptable = (globaz.helios.db.comptes.CGExerciceComptableViewBean) session.getAttribute(globaz.helios.db.interfaces.CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
						if ((exerciceComptable != null) && !((interMandat != null) && ("true".equals(interMandat)))) {
							params += exerciceComptable.getIdMandat();
						}

						String selectedIdModel = "";
						if (request.getParameter("selectedIdModel") != null) {
							selectedIdModel = request.getParameter("selectedIdModel");
						}

						String selectedModelTitle = "";
						if (request.getParameter("selectedModelTitle") != null) {
							selectedModelTitle = request.getParameter("selectedModelTitle");
						}
					%>

					<input type="hidden" name="selectedIdModel" value="<%=selectedIdModel%>"/>

			        <ct:FWPopupList
				    	name="selectedModelTitle"
				        value="<%=selectedModelTitle%>"
				        className="libelle"
				        jspName="<%=jspEcrModelesSelectLocation%>"
				        autoNbrDigit="<%=autoCompleteStart%>"
				        size="25"
				        onChange="updateSelectedIdModel(tag);"
				        minNbrDigit="1"
				        params="<%=params%>"
				    />&nbsp;*
					</td>
				</tr>

				<tr>
					<td>Pièce [=]</td>
					<%
						String piece = "";
						if (request.getParameter("piece") != null) {
							piece = request.getParameter("piece");
						}
					%>

					<TD width="424"><input name="piece" class="libelle" size="10" maxlength="10" value="<%=(!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(piece) || exerciceComptable == null)?piece:CGPieceIncrementor.getNextNumero(viewBean.getSession(), exerciceComptable.getIdExerciceComptable())%>"/>
					<script language="javascript">
					 	  	element = document.getElementById("piece");
					 	  	element.onkeypress=function() {fillCell(this);}
				 	</script>
					</TD>
				</tr>
				</table>
				<tr>
				<td><HR/></td>
				</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>