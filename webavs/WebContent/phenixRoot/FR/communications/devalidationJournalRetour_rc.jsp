<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
rememberSearchCriterias = true;
boolean bButtonCustom = true;
String btnCustomLabel = "Dévalider sélection";
String actionCustom =  "phenix.communications.validationJournalRetour.devaliderSelection";
globaz.phenix.db.communications.CPValidationJournalRetourViewBean viewBean = new globaz.phenix.db.communications.CPValidationJournalRetourViewBean();
idEcran = "CCP1026";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.phenix.db.communications.CPParametrePlausibilite"%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT>
// menu 
top.document.title = "Validation des journaux des communications retour"
usrAction = "phenix.communications.devalidationJournalRetour.lister";
bFind = true;
<% bButtonNew = false; %>
</SCRIPT>
<%String actionSuppression =  servletContext + mainServletPath + "?userAction=" + request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".executerSuppression&selectedId="+request.getParameter("selectedId");%>
<%String actionChercher =  servletContext + mainServletPath + "?userAction=" + request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".chercher";%>
<script type="text/javascript">
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer la communication fiscale! Voulez-vous continuer?"))
	{
		document.location.href='<%=actionSuppression%>'
	}else{
		document.location.href='<%=actionChercher%>'
	}
}
function onClickCustom() {
    document.forms[0].elements('userAction').value="phenix.communications.validationJournalRetour.validerSelection";
	document.forms[0].submit();
}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Dévalidation des journaux de communications en retour<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<%
		   	// display error message if needed
			if(session.getAttribute("errorMessage")!=null){
		  %>
          <TR>
            <TD nowrap colspan="6"><br><b><%=session.getAttribute("errorMessage")%></b></TD>
          </TR>		  
		  <%
			  // delete error message
				session.removeAttribute("errorMessage");
			}
		  %>
 		<TR>
		<TD nowrap>N° Affilié&nbsp;&nbsp;</TD>
		<TD width=50%>
			<INPUT type="text" name="likeNumAffilie"  class="libelleCourt" value="<%=viewBean.getForNumAffilie()%>" />
		</TD>
	</TR>
	<TR>
		<TD nowrap width="80">Journal</TD>
		<TD width=50%>
			<INPUT name="forJournal" 
				   class="numeroCourt"
				   value="<%=viewBean.getForJournal()%>"
			/>
		</TD>
		<TD width=50 nowrap="nowrap"></TD>
		<td nowrap width="80">Année&nbsp;&nbsp;</td>
		<TD width=50%>
			<INPUT name="forAnnee" 
				   class="numeroCourt"
				   value="<%=viewBean.getForAnnee()%>"
			/>
		</TD>
	</TR>
	<TR>
		<TD nowrap width="80">Genre</TD>
		<TD width=50%>
		<%
			java.util.HashSet except = new java.util.HashSet();
			except.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
			except.add(globaz.phenix.db.principale.CPDecision.CS_ETUDIANT);
			except.add(globaz.phenix.db.principale.CPDecision.CS_ACT_LUCRATIVE);
			except.add(globaz.phenix.db.principale.CPDecision.CS_ACT_NON_LUCRATIVE);
			except.add(globaz.phenix.db.principale.CPDecision.CS_FICHIER_CENTRAL);
		%>
		<ct:FWCodeSelectTag name="forGenreAffilie"
					defaut=""
					wantBlank="<%=true%>"
			        codeType="CPGENDECIS"
		            except="<%=except%>"
		/>
		</TD>
		<TD width=50 nowrap="nowrap"></TD>
		<td nowrap width="80">Type</td>
		<TD width=50%>
		<%
		java.util.HashSet except1 = new java.util.HashSet();
		except1.add(globaz.phenix.db.principale.CPDecision.CS_IMPUTATION);
		except1.add(globaz.phenix.db.principale.CPDecision.CS_ACOMPTE);
		except1.add(globaz.phenix.db.principale.CPDecision.CS_REMISE);
		except1.add(globaz.phenix.db.principale.CPDecision.CS_REDUCTION);
		%>
		<ct:FWCodeSelectTag name="forTypeDecision"
				defaut=""
				wantBlank="<%=true%>"
			    codeType="CPTYPDECIS"
		        except="<%=except1%>"
	    />
		</TD>
	</TR>
	<TR>
		<TD nowrap width="80">Groupe Extraction</TD>
		<TD colspan="4" width=50%>
				<ct:FWListSelectTag name="forGrpExtraction"
						defaut=""
	            		data='<%=globaz.phenix.db.communications.CPValidationCalculCommunication.getListPlausibilites(session, CPParametrePlausibilite.CS_MSG_AVERTISSEMENT,"")%>'/>
		</TD>
	</tr>
	
	<tr>
		<TD nowrap width="80">Groupe Taxation</TD>
		<TD colspan="4" width=50%>
		<ct:FWCodeSelectTag name="forGrpTaxation" 
	      		defaut=""
	      		wantBlank="true"
	      		libelle="libelle"
	    	    codeType="CPTAXATION"/>
		</TD>
	</TR>	
	<input type="hidden" name="isDevalidation" value="true">
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%if (bButtonCustom) {%> <INPUT type="button" name="btnCustom" value="<%=btnCustomLabel%>" onClick="document.forms[0].target='fr_main';document.forms[0].elements.userAction.value='<%=actionCustom%>';document.forms[0].submit();"><%}%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%
if(request.getParameter("supprimer") != null)
{
//On vient de l'action supprimer, on demande à l'utilisateur si il veut vraiment effacer la communication
%>
<script type="text/javascript">
del();
</script>
<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>