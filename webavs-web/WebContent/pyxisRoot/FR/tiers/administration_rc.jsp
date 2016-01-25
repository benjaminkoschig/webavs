<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
	idEcran ="GTI0005";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
	rememberSearchCriterias = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT>
	usrAction = "pyxis.tiers.administrationAdresse.lister";
	bFind = false;

	function initCheckBox() {
		var cb = new Array("forIncludeInactif");
		for (i=0;i<cb.length;i++) {
			if (document.getElementsByName(cb[i])[0].value=="true") {
				document.getElementsByName("C"+cb[i])[0].checked = true;
			} else {
				document.getElementsByName("C"+cb[i])[0].checked = false;
			}
		}
	}
	function checkInput(el) {
		if(el.checked) {
		document.getElementsByName(el.name.substr(1))[0].value="true"
		} else {
		document.getElementsByName(el.name.substr(1))[0].value="false"
		}
	}


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche d'une administration<%-- /tpl:put  --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
         
   
          <TR>
            <TD nowrap  align="right">Désignation&nbsp;</TD>
            <TD nowrap colspan="2"> 
              <INPUT type="text" name="forDesignationUpper1or2Like"  class="libelleLong" value=''>
              <INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>">
            </TD>
            
            <td nowrap align="right">Code&nbsp;</td>
            <td>
              <INPUT type="text" name="forCodeAdministration"  class="libelleLong" value='<%=(request.getParameter("_pos")==null)?"":request.getParameter("_pos") %>'>
            	
            </td>
            <td nowrap align="right">&nbsp;&nbsp;Trier par : <select name="orderBy"><option value="code">Code</option><option value="name">Désignation</option></select></td>
           
            
          </TR>
          <TR> 
            <TD nowrap width="100">&nbsp;</TD>
            <TD nowrap colspan="2"></TD>
          </TR>
          <TR>
            <TD nowrap  align="right">Canton&nbsp;</TD>
           <TD><ct:FWCodeSelectTag name="forCantonAdministration"
			defaut=""
			codeType="PYCANTON"
			wantBlank="true"
			/>
	    </TD>
            <TD width="50" align="right">&nbsp;</TD>
            <TD nowrap width="64"  align="right" >Genre&nbsp;</TD>
		
		<%
		if (request.getParameter("selGenre")!=null){
     		%>
		<TD nowrap>
		<INPUT type="text" name="GenreAdministrationLibelle" tabindex="-1" class="libelleLongDisabled" value="<%=((globaz.globall.db.BSession)session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION)).getCodeLibelle(request.getParameter("selGenre"))%>" readonly>
		<INPUT type="hidden" name="forGenreAdministration" tabindex="-1" class="libelleLongDisabled" value="<%=request.getParameter("selGenre")%>" readonly>

		</TD> 

		<% } else { %>
            <TD nowrap><ct:FWCodeSelectTag name="forGenreAdministration"
		         defaut=""
			  codeType="PYGENREADM"
			  wantBlank="true"
			/>
		<INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>">

	    </TD>

		<%}%>
		
		
		<td nowrap align="right">Inclure les inactifs
			<input tabindex ="20" type="checkbox" name="CforIncludeInactif" onclick="checkInput(this)" >
			<input type="text" style="display:none" name="forIncludeInactif" value=""  >
			
		</td>
		 <td width="100%">&nbsp;</td>
		
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>

	<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
	</ct:menuChange>

		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>