<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <title>CineArchive - Categorías</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .categories-page {
            max-width: 1200px;
            margin: 0 auto;
            padding: 40px 20px;
        }

        .categories-header {
            text-align: center;
            margin-bottom: 50px;
            position: relative;
        }

        .categories-header::after {
            content: '';
            position: absolute;
            bottom: -10px;
            left: 50%;
            transform: translateX(-50%);
            width: 100px;
            height: 3px;
            background: linear-gradient(90deg, transparent, #e50914, transparent);
        }

        .categories-title {
            font-size: 2.5rem;
            color: #ffffff;
            margin-bottom: 15px;
        }

        .categories-subtitle {
            color: #888;
            font-size: 1.1rem;
        }

        .categories-section {
            margin-bottom: 40px;
            background: #1a1a1a;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
        }

        .categories-section h2 {
            color: #e50914;
            font-size: 1.8rem;
            margin-bottom: 25px;
            padding-bottom: 10px;
            border-bottom: 2px solid #333;
        }

        .categories-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
        }

        .category-card {
            background: linear-gradient(45deg, #242424, #1a1a1a);
            border: 1px solid #333;
            border-radius: 10px;
            padding: 20px;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }

        .category-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 20px rgba(229, 9, 20, 0.15);
            border-color: #e50914;
        }

        .category-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 4px;
            height: 100%;
            background: #e50914;
            transform: scaleY(0);
            transition: transform 0.3s ease;
        }

        .category-card:hover::before {
            transform: scaleY(1);
        }

        .category-name {
            font-size: 1.2rem;
            color: #ffffff;
            margin-bottom: 10px;
            font-weight: 600;
        }

        .category-description {
            color: #888;
            font-size: 0.95rem;
            line-height: 1.5;
        }

        .back-btn {
            position: fixed;
            bottom: 30px;
            right: 30px;
            background: linear-gradient(45deg, #1a1a1a, #2d2d2d);
            color: #ffffff;
            border: 1px solid #333;
            padding: 15px 25px;
            border-radius: 8px;
            font-size: 1rem;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: flex;
            align-items: center;
            gap: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
        }

        .back-btn:hover {
            transform: translateY(-2px);
            background: linear-gradient(45deg, #2d2d2d, #1a1a1a);
            border-color: #e50914;
            box-shadow: 0 6px 20px rgba(229, 9, 20, 0.2);
        }

        .back-btn i {
            font-size: 1.1rem;
            color: #e50914;
        }

        @media (max-width: 768px) {
            .categories-grid {
                grid-template-columns: 1fr;
            }

            .categories-title {
                font-size: 2rem;
            }

            .back-btn {
                bottom: 20px;
                right: 20px;
                padding: 12px 20px;
            }
        }
    </style>
</head>
<body>
    <div class="categories-page">
        <div class="categories-header">
            <h1 class="categories-title">Categorías de CineArchive</h1>
            <p class="categories-subtitle">Explora nuestra colección por géneros, etiquetas y clasificaciones</p>
        </div>

        <c:forEach items="${categoriasPorTipo}" var="entry">
            <div class="categories-section">
                <h2>
                    <c:choose>
                        <c:when test="${entry.key == 'GENERO'}">
                            <i class="fas fa-film"></i> Géneros
                        </c:when>
                        <c:when test="${entry.key == 'TAG'}">
                            <i class="fas fa-tags"></i> Etiquetas
                        </c:when>
                        <c:when test="${entry.key == 'CLASIFICACION'}">
                            <i class="fas fa-star"></i> Clasificaciones
                        </c:when>
                    </c:choose>
                </h2>
                <div class="categories-grid">
                    <c:forEach items="${entry.value}" var="categoria">
                        <div class="category-card">
                            <h3 class="category-name">${categoria.nombre}</h3>
                            <p class="category-description">${categoria.descripcion}</p>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:forEach>

        <a href="${pageContext.request.contextPath}/login" class="back-btn">
            <i class="fas fa-arrow-left"></i>
            Volver al Login
        </a>
    </div>
</body>
</html>
