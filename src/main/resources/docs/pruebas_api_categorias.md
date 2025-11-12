# Pruebas de API REST - CategoriaController

## Requisitos previos
- La aplicación debe estar corriendo (usar `mvn jetty:run`)
- Tener instalado cURL o Postman

## Endpoints disponibles

### 1. Listar todas las categorías
```bash
curl http://localhost:8080/cinearchive/api/categorias
```

### 2. Crear nueva categoría
```bash
curl -X POST http://localhost:8080/cinearchive/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Aventura",
    "tipo": "GENERO",
    "descripcion": "Películas de aventuras y acción"
  }'
```

### 3. Obtener categoría por ID
```bash
curl http://localhost:8080/cinearchive/api/categorias/1
```

### 4. Actualizar categoría
```bash
curl -X PUT http://localhost:8080/cinearchive/api/categorias/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Aventura Actualizado",
    "tipo": "GENERO",
    "descripcion": "Descripción actualizada"
  }'
```

### 5. Eliminar categoría
```bash
curl -X DELETE http://localhost:8080/cinearchive/api/categorias/1
```

### 6. Obtener categorías por tipo
```bash
curl http://localhost:8080/cinearchive/api/categorias/tipo/GENERO
```

### 7. Buscar por nombre
```bash
curl http://localhost:8080/cinearchive/api/categorias/nombre/Aventura
```

### 8. Listar géneros
```bash
curl http://localhost:8080/cinearchive/api/categorias/generos
```

### 9. Listar tags
```bash
curl http://localhost:8080/cinearchive/api/categorias/tags
```

### 10. Listar clasificaciones
```bash
curl http://localhost:8080/cinearchive/api/categorias/clasificaciones
```

## Notas
- Todos los endpoints que envían datos (POST/PUT) requieren el header `Content-Type: application/json`
- Las respuestas vendrán en formato JSON
- Los códigos de estado HTTP indican el resultado de la operación:
  - 200: Operación exitosa
  - 201: Recurso creado exitosamente
  - 204: Operación exitosa sin contenido (para DELETE)
  - 400: Error en la solicitud
  - 404: Recurso no encontrado
