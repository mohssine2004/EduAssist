@echo off
REM Script pour exécuter les tests d'intégration d'authentification

cd /d "C:\Users\hp\Jakarta-Project\Projet-ELADDAOUI-MOHSSINE-JADID6HARON"

echo ========================================
echo Tests d'Integration - Authentification
echo ========================================
echo.

echo Compilation et exécution des tests...
call mvnw.cmd clean test -Dtest=AuthServiceIntegrationTest,LoginBeanIntegrationTest

echo.
echo ========================================
echo Tests termines
echo ========================================
pause

