@echo off
setlocal

rem --- Konfiguracja ---
rem Sciezki do projektow
set ANDROID_PROJECT_PATH=sherpa-android
set CSHARP_WRAPPER_PATH=sherpa-unity-wrapper

rem Nazwa modulu biblioteki Android
set ANDROID_MODULE_NAME=SherpaOnnxPlugin

rem Sciezki wyjsciowe
set DIST_PATH=dist
set DIST_ANDROID_PATH=%DIST_PATH%\Android
set DIST_CSHARP_PATH=%DIST_PATH%
rem --- Koniec Konfiguracji ---

echo.
echo [INFO] Tworzenie katalogow wyjsciowych...
if not exist "%DIST_PATH%" mkdir "%DIST_PATH%"
if not exist "%DIST_ANDROID_PATH%" mkdir "%DIST_ANDROID_PATH%"
echo [INFO] Gotowe.
echo.

rem ==================================================================
rem Budowanie biblioteki Android (.aar)
rem ==================================================================
echo [INFO] Rozpoczynanie budowania modulu Android: %ANDROID_MODULE_NAME%...
cd "%ANDROID_PROJECT_PATH%"

rem Uruchomienie Gradle do wyczyszczenia i zbudowania modulu w wersji Release
call gradlew :%ANDROID_MODULE_NAME%:clean :%ANDROID_MODULE_NAME%:assembleRelease

rem Sprawdzenie, czy budowanie sie powiodlo
if errorlevel 1 (
    echo.
    echo [ERROR] Budowanie biblioteki Android nie powiodlo sie.
    cd ..
    exit /b 1
)
echo [INFO] Budowanie modulu Android zakonczone pomyslnie.
echo.

echo [INFO] Kopiowanie pliku .aar do katalogu docelowego...
set AAR_SOURCE_PATH=%ANDROID_MODULE_NAME%\build\outputs\aar

rem Przenies plik .aar do docelowego katalogu
move /Y "%AAR_SOURCE_PATH%\*.aar" "..\%DIST_ANDROID_PATH%\" >nul
if errorlevel 1 (
    echo.
    echo [ERROR] Nie udalo sie przeniesc pliku .aar.
    cd ..
    exit /b 1
)
echo [INFO] Plik .aar zostal przeniesiony do %DIST_ANDROID_PATH%
cd ..
echo.

rem ==================================================================
rem Budowanie wrappera C# (.dll)
rem ==================================================================
echo [INFO] Rozpoczynanie budowania wrappera C#: %CSHARP_WRAPPER_PATH%...
cd "%CSHARP_WRAPPER_PATH%"

rem Uzyj msbuild do zbudowania projektu w konfiguracji Release
msbuild /p:Configuration=Release /nologo /v:q

rem Sprawdzenie, czy budowanie sie powiodlo
if errorlevel 1 (
    echo.
    echo [ERROR] Budowanie wrappera C# nie powiodlo sie.
    cd ..
    exit /b 1
)
echo [INFO] Budowanie C# zakonczone pomyslnie.
echo.

echo [INFO] Kopiowanie plikow .dll do katalogu docelowego...
rem Sciezka, gdzie msbuild umieszcza pliki .dll w konfiguracji Release
set DLL_SOURCE_PATH=bin\Release

rem Przenies pliki .dll do docelowego katalogu
move /Y "%DLL_SOURCE_PATH%\*.dll" "..\%DIST_CSHARP_PATH%\" >nul
if errorlevel 1 (
    echo.
    echo [ERROR] Nie udalo sie przeniesc plikow .dll.
    cd ..
    exit /b 1
)
echo [INFO] Pliki .dll zostaly przeniesione do %DIST_CSHARP_PATH%
cd ..
echo.

rem ==================================================================
rem Podsumowanie
rem ==================================================================
echo [SUCCESS] Skrypt zakonczyl dzialanie pomyslnie.
echo.
echo   -> Gotowy plik .aar znajduje sie w: %DIST_ANDROID_PATH%
echo   -> Gotowe pliki .dll znajduja sie w: %DIST_CSHARP_PATH%
echo.
echo ==================================================================

endlocal
exit /b 0
