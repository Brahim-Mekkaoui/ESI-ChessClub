<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ESI Chess Club — Connexion</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="auth-wrapper">
        <div class="auth-card card">
            <div class="auth-header">
                <div class="auth-icon">♔</div>
                <h1>Connexion</h1>
                <p>Accédez à votre espace ESI Chess Club</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/login" class="auth-form">
                <div class="form-group">
                    <label for="username">Nom d'utilisateur</label>
                    <input type="text" id="username" name="username" class="form-control"
                           placeholder="Votre nom d'utilisateur" required autofocus
                           value="${not empty param.username ? param.username : ''}">
                </div>
                <div class="form-group">
                    <label for="password">Mot de passe</label>
                    <input type="password" id="password" name="password" class="form-control"
                           placeholder="Votre mot de passe" required>
                </div>
                <button type="submit" class="btn btn-primary btn-full">Se connecter</button>
            </form>

            <div class="auth-footer">
                <p>Pas encore membre ? <a href="${pageContext.request.contextPath}/register" class="link">Créer un compte</a></p>
            </div>

            <div class="auth-demo">
                <p style="font-size:0.8rem;color:var(--text-muted);margin-top:16px;">
                    Compte démo — Admin : <code>admin</code> / <code>Admin123</code>
                </p>
            </div>
        </div>
    </div>
</div>
<%@ include file="footer.jsp" %>
