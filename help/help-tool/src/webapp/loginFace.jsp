<%--
  Created by IntelliJ IDEA.
  User: antoniasimionescu
  Date: 03/07/2021
  Time: 11:40
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%-- Custom tag library just for this tool --%>
<%@ taglib uri="http://sakaiproject.org/jsf/help" prefix="help" %>

<f:view>

    <help:faceRecognitionSet
        faceURL="LoginFaceRecognition"
    ></help:faceRecognitionSet>


</f:view>
