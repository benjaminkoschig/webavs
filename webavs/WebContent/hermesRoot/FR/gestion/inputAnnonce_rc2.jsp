
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
globaz.hermes.db.gestion.HEInputAnnonceViewBean viewBean = (globaz.hermes.db.gestion.HEInputAnnonceViewBean)session.getAttribute("viewBean");
//System.out.println(viewBean==null);
bButtonFind = false;
bButtonNew = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<script language="JavaScript">
function init(){
	document.forms[0].elements._method.value = "add";
	timeWaiting = -1;
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Choix 
        d'un critère<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr> 
            <td>Critères : </td>
            <td>
              <SELECT name="critere" style="width : 20cm;">
                <%

for(int i=0;i<viewBean.critereSize();i++){
	if(viewBean.critereSize()==1){
		//System.out.println("MOVE");
	}
	globaz.hermes.db.parametrage.HEMotifcodeapplication line = viewBean.getHEMotifcodeapplication(i);
	out.println("<option value=\""+line.getIdCritereMotif()+"\">"+line.getCritereLibelle()+"</option>" );
}
%>
              </SELECT>
              <input type="hidden" name="motif" value="<%=request.getParameter("motif")%>">
            </td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <!--
<TD bgcolor="#FFFFFF" colspan="2" align="right"> <A onClick="init();document.forms[0].submit();"> 
  <IMG name="btnNew" src="file:///C:/Documents and Settings/ado/Mes documents/Studio 3.5 Projects/Nova_app/images/btnNew.gif" border="0"> 
  </A> </TD>
  -->
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>