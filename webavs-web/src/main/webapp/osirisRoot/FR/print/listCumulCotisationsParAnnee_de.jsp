<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.osiris.db.print.CAListCumulCotisationsParAnneeViewBean"%>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>

<%
	idEcran = "GCA2029"; 
	CAListCumulCotisationsParAnneeViewBean viewBean = (CAListCumulCotisationsParAnneeViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.listCumulCotisationsParAnnee.executer";
	String pdfChecked = "pdf".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
	String xlsChecked = "xls".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
%>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
top.document.title = "Web@Avs - <ct:FWLabel key='GCA2029_TITRE_ECRAN'/> - " + top.location.href;
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="GCA2029_TITRE_ECRAN"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

    <tr> 
      <td width="128"><ct:FWLabel key="EMAIL"/></td>
      <td><input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong"/></td>
    </tr>		
	<tr>
       	<td width="128"><ct:FWLabel key="GCA2029_DATE_VALEUR"/></td>
       	<td><ct:FWCalendarTag name="fromDateValeur" doClientValidation="CALENDAR" value="<%=viewBean.getFromDateValeur()%>"/>
       	&nbsp;<ct:FWLabel key="GCA2029_A"/>&nbsp;
       	<ct:FWCalendarTag name="toDateValeur" doClientValidation="CALENDAR" value="<%=viewBean.getToDateValeur()%>"/>
       	</td>
    </tr>
	<tr>
       	<td width="128"><ct:FWLabel key="GCA2029_RUBRIQUE"/></td>
       	<td><input type="text" name="fromIdExterne" value="<%=viewBean.getFromIdExterne()%>" class="libelleLong"/>
       	&nbsp;<ct:FWLabel key="GCA2029_A"/>&nbsp;
       	<input type="text" name="toIdExterne" value="<%=viewBean.getToIdExterne()%>" class="libelleLong"/>
       	</td>
    </tr>	        		        		
	<TR>
		<td><ct:FWLabel key="TYPE_IMPRESSION"/></td>
  		<TD>
   			<input type="radio" name="typeImpression" value="pdf" <%=pdfChecked%>/>PDF&nbsp;
   			<input type="radio" name="typeImpression" value="xls" <%=xlsChecked%>/>Excel
   		</TD>
    </TR> 
    						
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>