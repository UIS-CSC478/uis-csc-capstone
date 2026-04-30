@echo off
setlocal

for /f "delims=" %%i in ('powershell -NoProfile -Command "[Environment]::GetFolderPath('Desktop')"') do set "DESKTOP=%%i"

set "SOURCE=%~dp0Scrabble App"
set "DEST=%DESKTOP%\Scrabble App"

echo Installing Scrabble App...
echo.

echo Desktop detected as:
echo %DESKTOP%
echo.

if not exist "%SOURCE%" (
    echo ERROR: Could not find the Scrabble App folder.
    echo Make sure this batch file is in the same folder as "Scrabble App".
    pause
    exit /b 1
)

if exist "%DEST%" (
    echo Existing installation found. Replacing it...
    rmdir /s /q "%DEST%"
)

xcopy "%SOURCE%" "%DEST%\" /e /i /h /y >nul

if errorlevel 1 (
    echo Installation failed.
    pause
    exit /b 1
)

echo Installation complete.
echo Installed to:
echo %DEST%
echo.
echo Launching Scrabble App...
start "" "%DEST%\Scrabble App.exe"

endlocal
pause