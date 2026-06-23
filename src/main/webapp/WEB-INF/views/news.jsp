<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ESI Chess Club — Actualités</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%@ include file="header.jsp" %>
<div class="container">

    <div class="page-header">
        <h1>📰 Actualités</h1>
        <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/admin/news/create" class="btn btn-primary">+ Publier</a>
        </c:if>
    </div>

    <c:if test="${not empty success}"><div class="alert alert-success">${success}</div></c:if>

    <c:choose>
        <c:when test="${empty newsList}">
            <div class="empty-state card">
                <div class="empty-icon">📰</div>
                <h3>Aucune actualité</h3>
                <p class="text-muted">Il n'y a pas encore d'actualités publiées.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="news-full-list">
                <c:forEach var="article" items="${newsList}">
                    <article class="news-full-card card">
                        <div class="news-full-header">
                            <div>
                                <h2>${fn:escapeXml(article.title)}</h2>
                                <div class="news-meta">
                                    <span>📅 ${article.formattedDate}</span>
                                    <c:if test="${not empty article.authorName}">
                                        <span>✍️ ${fn:escapeXml(article.authorName)}</span>
                                    </c:if>
                                </div>
                            </div>
                            <c:if test="${sessionScope.user.role == 'ADMIN'}">
                                <form method="post" action="${pageContext.request.contextPath}/admin/news/delete">
                                    <input type="hidden" name="newsId" value="${article.id}">
                                    <button type="submit" class="btn btn-danger btn-sm"
                                            onclick="return confirm('Supprimer cet article ?')">🗑</button>
                                </form>
                            </c:if>
                        </div>
                        <div class="news-full-content">
                            ${fn:escapeXml(article.content)}
                        </div>
                    </article>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

</div>
<%@ include file="footer.jsp" %>
