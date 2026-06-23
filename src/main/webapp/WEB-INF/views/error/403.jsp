<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>ESI Chess Club — Accès refusé</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="../header.jsp" %>
<div class="container">
    <div class="error-page">
        <div class="error-code">403</div>
        <div class="error-icon">♚</div>
        <h1>Accès refusé</h1>
        <p>Vous n'avez pas les droits nécessaires pour accéder à cette page.</p>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Retour à l'accueil</a>
    </div>
</div>
<%@ include file="../footer.jsp" %>
