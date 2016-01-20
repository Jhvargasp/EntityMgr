<%@page contentType="text/html" isErrorPage="true"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c-rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<html>
	<fmt:bundle basename="labels">
		<head>
			<title>Error</title>
		</head>


		<body>
			<jsp:include flush="true" page="head1.jsp" />
			<TABLE class=wcmSignInBorderBackground cellSpacing=1 width="95%">
				<TR>

					<TD width="80%">
						Error
					</TD>


				</TR>
				<TR>
					<td>
						<form action="/reports">
							--${message}--
							<%
								if (exception != null) {
								out.println(exception.getMessage());
						%>
							<!-- <%=exception%>
    <%exception.printStackTrace(new java.io.PrintWriter(out));%>
    -->
							<%
							}
							%>
						</form>

					</td>
				</tr>

				<tr>
					<td colspan="3" align="center" class="wcmFormInput">
						<form action="exit.html">
							<h3>
								<INPUT class="button" type="button"
									value='<fmt:message key="app.back" />'
									onclick="javascript:history.go(-1);" />
								&nbsp; &nbsp;
								<INPUT class="button" type="submit"
									value='<fmt:message key="app.exit" />' />
							</h3>
						</form>
					</td>
				</tr>

			</table>
		</body>
	</fmt:bundle>
</html>

