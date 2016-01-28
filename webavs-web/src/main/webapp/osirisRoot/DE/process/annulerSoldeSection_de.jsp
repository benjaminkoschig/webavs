 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA3007"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
globaz.osiris.db.process.CAAnnulerSoldeSectionViewBean viewBean = (globaz.osiris.db.process.CAAnnulerSoldeSectionViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris.process.annulerSoldeSection.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="options" menuId="CA-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/autocomplete.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/selectionPopup.js"></SCRIPT>

<SCRIPT language="JavaScript">

function updateRubrique(el) {
	if (el == null || el.value== "" || el.options[el.selectedIndex] == null)
		rubriqueManuelleOn();
	else {
		var elementSelected = el.options[el.selectedIndex];
		document.forms[0].idExterneRubriqueEcran.value = elementSelected.idExterneRubriqueEcran;
		document.forms[0].idCompte.value = elementSelected.idCompte;
	}
}

function rubriqueManuelleOn(){
	document.forms[0].idCompte.value="";
}


top.document.title = "Prozess - Annullieren der Saldo der Sektionen " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Annullieren der Saldo der Sektionen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="30%" height="14">E-Mail</TD>
            <TD nowrap width="50%" height="14"> 
              <INPUT type="text" name="eMailAddress" class="libelleLong" value="<%= viewBean.getEMailAddress()%>">
            </TD>
            <TD nowrap width="20%" height="14">&nbsp;</TD>
          </TR>

          <TR> 
            <TD nowrap width="30%">Datum</TD>
            <TD nowrap width="50%" height="14"> 
              <ct:FWCalendarTag name="date" value="<%=viewBean.getDate()%>" doClientValidation="CALENDAR" value="<%=viewBean.getDate()%>"/>
            </TD>
			<TD nowrap width="20%" height="14">&nbsp;</TD>
          </TR>

          <TR> 
            <TD nowrap width="30%" height="14">Mindestbetrag</TD>
            <TD nowrap width="50%" height="14">               
				<input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="libelle" style="text-align : right" name="montantMinime" value="<%=viewBean.getMontantMinime()%>"/>
			</TD>              
            <TD nowrap width="20%" height="14">&nbsp;</TD>
          </TR>


          <TR> 
            <TD nowrap width="30%" height="14">Art der Sektion</TD>
            <TD nowrap width="50%" height="14">               
              <select name="idTypeSection" style="width:60%">
                <%globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
                  globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
                  CATypeSection tempTypeSection;
				  CATypeSectionManager manTypeSection = new CATypeSectionManager();
				  manTypeSection.setSession(objSession);
				  manTypeSection.find();
				  for(int i = 0; i < manTypeSection.size(); i++){
				    	tempTypeSection = (CATypeSection)manTypeSection.getEntity(i);
						if(viewBean.getIdTypeSection() == null) { %>
			                <option value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
		                <%} else if  (viewBean.getIdTypeSection().equalsIgnoreCase(tempTypeSection.getIdTypeSection())) { %>
			                <option selected value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
		                <% } else { %>
        		        <option value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>				    						
                		<% } 
                  }%>
              </select>
            </TD>
            <TD nowrap width="20%" height="14">&nbsp;</TD>
          </TR>


			<%
			String jspLocation = request.getContextPath()+ mainServletPath + "Root/rubrique_select.jsp";
			%>			          
          <TR>
           <TD nowrap width="30%" height="14">Rubrik</TD> 
            <TD width="147"> <ct:FWPopupList name="idExterneRubriqueEcran"
				onChange="updateRubrique(tag.select);"
				value="<%=viewBean.getIdExterneRubriqueEcran()%>"
				className="libelle" 
				jspName="<%=jspLocation%>"
				minNbrDigit="1" 
				forceSelection="true" 
				validateOnChange="false"/>
			</TD>
			<TD nowrap>&nbsp;
			<input type="hidden" name="idCompte" value="<%=viewBean.getIdCompte()%>" tabindex="-1" >
			</TD>            
          <TR> 

			<TR>
			<TD nowrap width="30%" height="14">Aktive Abrechnungskonti</TD>
            <TD colspan="2">
              <input type="checkbox" name="forCompteAnnexeActif" value="on" checked/>
            </TD>
			</TR>
			
			<TR>
			<TD nowrap width="30%" height="14">Abgängige Abrechnungskonti</TD>
            <TD colspan="2">
              <input type="checkbox" name="forCompteAnnexeRadie" value="on" checked/>
            </TD>
			</TR>	

			<TR>
			<TD nowrap width="30%" height="14">Verzeichnis</TD>
            <TD colspan="2">
				<SELECT name="forSelectionRole">
					<%=CARoleViewBean.createOptionsTags(objSession, null)%>
				</SELECT>
            </TD>
			</TR>	

          <TR> 
            <TD nowrap width="30%" height="14">Simulation</TD>
            <TD nowrap width="50%" height="14">
              <input type="checkbox" name="simulation" value="on">
            </TD>
			<TD nowrap width="20%" height="14">&nbsp;</TD>
		</TR>


          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>