<%@ page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@ page import="globaz.globall.db.BSession"%>
<%
	FWViewBeanInterface localViewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
%>
<TR>
	<TD class="label">Email</TD> 
	<TD class="control" colspan="3"><INPUT type="text" name="email" value="<%=((BSession) localViewBean.getISession()).getUserEMail()%>"></TD>
</TR>
