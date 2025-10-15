@echo off
REM Build and run script for Interpreter Pattern implementation

echo ========================================
echo Daily Operations Dashboard
echo Interpreter Pattern Implementation
echo ========================================
echo.

REM Navigate to project root if not already there
cd /d "%~dp0.."

echo Compiling Java files from src/ to build/...
javac -d build src\*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.
echo Running InterpreterDashboard...
echo ========================================
echo.

java -cp build InterpreterDashboard

echo.
echo ========================================
echo Execution complete!
pause

