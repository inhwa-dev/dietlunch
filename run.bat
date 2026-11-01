@echo off
chcp 65001 >nul
cd /d "%~dp0"
if not exist out mkdir out
javac -encoding UTF-8 -d out -sourcepath src/main/java src/main/java/com/project/Main.java
if errorlevel 1 (
  echo.
  echo [ERROR] Java compile failed. JDK 17 or newer is required.
  pause
  exit /b 1
)
java -cp out com.project.Main
echo.
pause
