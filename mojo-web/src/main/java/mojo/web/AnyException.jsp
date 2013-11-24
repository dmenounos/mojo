<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<p class="exception">
	<fmt:message key="error.message" />
</p>

<!--
Exception: ${exception.message}
<c:forEach items="${exception.stackTrace}" var="ste">
	${ste}
</c:forEach>
-->
