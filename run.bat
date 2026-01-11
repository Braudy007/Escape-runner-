@echo off
echo Compilation du jeu...
if not exist "bin" mkdir bin
javac -d bin -sourcepath src/main/java src/main/java/com/fastescape/*.java

if %errorlevel% neq 0 (
    echo Erreur de compilation !
    pause
    exit /b
)

echo Lancement du jeu...
java -cp bin com.fastescape.Main
pause
