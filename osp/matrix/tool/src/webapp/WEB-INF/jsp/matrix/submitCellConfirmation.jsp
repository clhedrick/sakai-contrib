<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="msgs" class="org.sakaiproject.util.ResourceLoader" scope="request"><jsp:setProperty name="msgs" property="baseName" value="org.theospi.portfolio.matrix.bundle.Messages"/></jsp:useBean>


    <h3><osp:message key="submit_cell_confirmation"/></h3>
   
      <div class="alertMessage"><c:out value="${msgs.instructions_paragraph1}"/></div>
	  <h4><c:out value="${page.pageDefinition.title}"/></h4>
       <div class="textPanel"><c:out value="${page.pageDefinition.description}" escapeXml="false"/></div>
<form>
<c:choose>
  <c:when test="${numgroups == 1}">
    <input type="hidden" name="selgroup" value="${firstgroup}" />
  </c:when>
  <c:when test="${numgroups > 1}">
    <p>Choose the group with which this information is associated:
    <p><c:forEach var="group" items="${groups}">
      <input type="radio" name="selgroup" value="${group.id}"/><c:out value="${group.description}" /><br/>
    </c:forEach>
  </c:when>
</c:choose>

<script type="text/javascript">
function getGroupValue() {
    for (var i = 0; i < document.getElementsByName("selgroup").length; i++) {
        if (document.getElementsByName("selgroup")[i].checked) {
                return document.getElementsByName("selgroup")[i].value;
        }
    }
    return null;
}

function validgroup() {
  if (getGroupValue() == null) {
    alert("Please choose the group with which the information you are submitting is associated");
    return false;
  }
  return true;
}
</script>

<c:choose>
  <c:when test="${numgroups == 0}">
<script>
var div = document.getElementById("alertMessage");
if(div) {
	div.innerHTML = "Error: you are not in a course that is allowed to use this item. Please consult with your faculty member.";
}
</script>
  </c:when>
</c:choose>

<p class="act">
      <input type="hidden" name="page_id" value="<c:out value="${page_id}" />"/>
      <input type="submit" name="submit" value="<osp:message key="button_submitCell"/>" class="active" accesskey="s" 
<c:if test="${numgroups > 1}">
       onclick="return validgroup()"
</c:if>
       />
      <input type="submit" name="cancel" value="<osp:message key="button_cancel"/>" accesskey="x" />
   </p>

</form>