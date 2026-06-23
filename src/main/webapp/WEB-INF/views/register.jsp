<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ESI Chess Club — Inscription</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">
    <div class="auth-wrapper">
        <div class="auth-card card" style="max-width:520px;">
            <div class="auth-header">
                <div class="auth-icon">♟</div>
                <h1>Créer un compte</h1>
                <p>Rejoignez le club d'échecs de l'ESI</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/register" class="auth-form">
                <div class="form-row">
                    <div class="form-group">
                        <label for="firstName">Prénom</label>
                        <input type="text" id="firstName" name="firstName" class="form-control"
                               placeholder="Prénom" value="${not empty param.firstName ? param.firstName : ''}">
                    </div>
                    <div class="form-group">
                        <label for="lastName">Nom</label>
                        <input type="text" id="lastName" name="lastName" class="form-control"
                               placeholder="Nom" value="${not empty param.lastName ? param.lastName : ''}">
                    </div>
                </div>
                <div class="form-group">
                    <label for="username">Nom d'utilisateur *</label>
                    <input type="text" id="username" name="username" class="form-control"
                           placeholder="Choisissez un nom d'utilisateur" required
                           value="${not empty param.username ? param.username : ''}">
                </div>
                <div class="form-group">
                    <label for="email">Email *</label>
                    <input type="email" id="email" name="email" class="form-control"
                           placeholder="votre@email.com" required
                           value="${not empty param.email ? param.email : ''}">
                </div>
                <div class="form-group">
                    <label for="password">Mot de passe *</label>
                    <input type="password" id="password" name="password" class="form-control"
                           placeholder="Min 8 caractères, lettre + chiffre" required>
                    <small class="form-hint">Au moins 8 caractères avec une lettre et un chiffre.</small>
                </div>
                <div class="form-group">
                    <label for="chessLevel">Niveau d'échecs</label>
                    <select id="chessLevel" name="chessLevel" class="form-control">
                        <option value="BEGINNER" ${param.chessLevel == 'BEGINNER' ? 'selected' : ''}>Débutant</option>
                        <option value="INTERMEDIATE" ${param.chessLevel == 'INTERMEDIATE' ? 'selected' : ''}>Intermédiaire</option>
                        <option value="ADVANCED" ${param.chessLevel == 'ADVANCED' ? 'selected' : ''}>Avancé</option>
                        <option value="EXPERT" ${param.chessLevel == 'EXPERT' ? 'selected' : ''}>Expert</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary btn-full">Créer mon compte</button>
            </form>

            <div class="auth-footer">
                <p>Déjà membre ? <a href="${pageContext.request.contextPath}/login" class="link">Se connecter</a></p>
            </div>
        </div>
    </div>
</div>
<%@ include file="footer.jsp" %>
