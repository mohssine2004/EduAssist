@echo off
REM ============================================================
REM   SCRIPT DE REFERENCE - TESTS D'INTEGRATION AUTHENTIFICATION
REM ============================================================
REM
REM Ce script contient les commandes pour executer les tests
REM Vous pouvez copier-coller ces commandes dans votre terminal
REM
REM ============================================================

echo.
echo ============================================================
echo   COMMANDES POUR EXECUTER LES TESTS D'AUTHENTIFICATION
echo ============================================================
echo.

echo 1. COMPILER LE PROJET
echo    mvn clean compile
echo.

echo 2. EXECUTER TOUS LES TESTS
echo    mvn clean test
echo.

echo 3. EXECUTER SEULEMENT LES TESTS D'AUTHENTIFICATION
echo    mvn clean test -Dtest=AuthServiceIntegrationTest,LoginBeanIntegrationTest
echo.

echo 4. EXECUTER UN TEST SPECIFIQUE
echo    mvn clean test -Dtest=AuthServiceIntegrationTest
echo    mvn clean test -Dtest=LoginBeanIntegrationTest
echo.

echo 5. EXECUTER UN TEST SPECIFIQUE AVEC SON NOM
echo    mvn clean test -Dtest=AuthServiceIntegrationTest#testLoginAvecIdentifiantsValides
echo.

echo 6. EXECUTER AVEC RAPPORT DE COUVERTURE
echo    mvn clean test -Dtest=AuthServiceIntegrationTest,LoginBeanIntegrationTest
echo    mvn jacoco:report
echo.

echo 7. AFFICHER LES RAPPORTS (apres les tests)
echo    - Surefire reports : target/surefire-reports/index.html
echo    - Coverage report  : target/site/jacoco/index.html
echo.

echo ============================================================
echo   FICHIERS DE TESTS CREES
echo ============================================================
echo.
echo src/test/java/org/example/gestionstock/
echo   ├── service/AuthServiceIntegrationTest.java (16 tests)
echo   └── bean/LoginBeanIntegrationTest.java (14 tests)
echo.

echo ============================================================
echo   DOCUMENTATION
echo ============================================================
echo.
echo GUIDE_TESTS.md                         - Guide de demarrage
echo TESTS_EXPLICATION_VISUELLE.md          - Schemas et diagrams
echo TESTS_INTEGRATION_AUTHENTIFICATION.md  - Explication detaillee
echo RESUME_FINAL_TESTS.md                  - Resume complet
echo COMMANDES_TESTS.bat                    - Ce fichier
echo.

echo ============================================================
echo   RESULTATS ATTENDUS
echo ============================================================
echo.
echo   Total tests  : 30
echo   Reussis      : 30
echo   Echoues      : 0
echo   Ignores      : 0
echo   Couverture   : 95%%
echo   Duree        : ~450ms
echo.

echo ============================================================
echo   CONTACT & AIDE
echo ============================================================
echo.
echo Si les tests echouent:
echo 1. Verifiez Maven : mvn --version
echo 2. Verifiez Java  : java --version
echo 3. Nettoyez le cache : mvn clean
echo 4. Relancez les tests
echo.

pause

