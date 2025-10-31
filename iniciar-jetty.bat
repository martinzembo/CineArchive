@echo off
echo ====================================
echo DIAGNOSTICO JETTY - CINEARCHIVE
echo ====================================
echo.

echo [1/5] Verificando Maven...
call mvn -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven no esta instalado o no esta en PATH
    pause
    exit /b 1
)
echo OK - Maven encontrado
echo.

echo [2/5] Verificando JAVA_HOME...
if "%JAVA_HOME%"=="" (
    echo ERROR: JAVA_HOME no esta configurado
    pause
    exit /b 1
)
echo OK - JAVA_HOME = %JAVA_HOME%
echo.

echo [3/5] Verificando estructura del proyecto...
if not exist "pom.xml" (
    echo ERROR: No se encuentra pom.xml en el directorio actual
    echo Asegurate de ejecutar este script desde C:\Users\Francisco\Desktop\CineArchive
    pause
    exit /b 1
)
echo OK - pom.xml encontrado
echo.

echo [4/5] Limpiando y compilando proyecto...
call mvn clean compile
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: La compilacion fallo
    pause
    exit /b 1
)
echo OK - Compilacion exitosa
echo.

echo [5/5] Verificando archivos compilados...
if not exist "target\classes\edu\utn\inspt\cinearchive\frontend\controlador\TestController.class" (
    echo ADVERTENCIA: TestController.class no encontrado
) else (
    echo OK - TestController.class encontrado
)

if not exist "target\classes\edu\utn\inspt\cinearchive\frontend\controlador\HealthController.class" (
    echo ADVERTENCIA: HealthController.class no encontrado
) else (
    echo OK - HealthController.class encontrado
)
echo.

echo ====================================
echo INICIANDO JETTY...
echo ====================================
echo.
echo IMPORTANTE:
echo - Espera a ver el mensaje: [INFO] Started Jetty Server
echo - Luego abre tu navegador en: http://localhost:8080/cinearchive/health
echo - Para detener Jetty: Presiona Ctrl+C
echo.
echo Iniciando en 3 segundos...
timeout /t 3 /nobreak >nul
echo.

call mvn jetty:run

