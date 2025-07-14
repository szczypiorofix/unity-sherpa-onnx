@echo off
setlocal

rem --- Configuration ---
rem Project Paths
set ANDROID_PROJECT_PATH=sherpa-android
set CSHARP_WRAPPER_PATH=sherpa-unity-wrapper

rem Android library module name
set ANDROID_MODULE_NAME=SherpaOnnxPlugin

rem !!! IMPORTANT: SET THE PATHS TO UNITY LIBRARIES !!!
rem You might need to provide paths to multiple folders.
rem Find your Unity Editor installation folder first.

rem Path 1: Main managed libraries (UnityEngine.dll, UnityEditor.dll)
rem Example: C:\Program Files\Unity\Hub\Editor\2022.3.5f1\Editor\Data\Managed
set UNITY_MANAGED_PATH=C:\Program Files\Unity\Hub\Editor\2022.3.17f1\Editor\Data\Managed

rem Path 2 (Optional): Platform-specific libraries, e.g., for Windows Standalone.
rem Example: C:\Program Files\Unity\Hub\Editor\2022.3.5f1\Editor\Data\PlaybackEngines\windowsstandalonesupport\Managed
set UNITY_PLATFORM_PATH=

rem Output Paths
set DIST_PATH=dist
set DIST_ANDROID_PATH=%DIST_PATH%\Android
set DIST_CSHARP_PATH=%DIST_PATH%
rem --- End of Configuration ---

echo.
echo [INFO] Creating output directories...
if not exist "%DIST_PATH%" mkdir "%DIST_PATH%"
if not exist "%DIST_ANDROID_PATH%" mkdir "%DIST_ANDROID_PATH%"
echo [INFO] Done.
echo.

rem ==================================================================
rem Building Android Library (.aar)
rem ==================================================================
echo [INFO] Starting Android module build: %ANDROID_MODULE_NAME%...
cd "%ANDROID_PROJECT_PATH%"

call gradlew :%ANDROID_MODULE_NAME%:clean :%ANDROID_MODULE_NAME%:assembleRelease
if errorlevel 1 (
    echo.
    echo [ERROR] Android library build failed.
    cd ..
    exit /b 1
)
echo [INFO] Android module built successfully.
echo.

echo [INFO] Copying .aar file to the destination directory...
set AAR_SOURCE_PATH=%ANDROID_MODULE_NAME%\build\outputs\aar
move /Y "%AAR_SOURCE_PATH%\*.aar" "..\%DIST_ANDROID_PATH%\" >nul
if errorlevel 1 (
    echo.
    echo [ERROR] Failed to move the .aar file.
    cd ..
    exit /b 1
)
echo [INFO] The .aar file has been moved to %DIST_ANDROID_PATH%
cd ..
echo.

rem ==================================================================
rem Building C# Wrapper (.dll)
rem ==================================================================
echo [INFO] Starting C# wrapper build: %CSHARP_WRAPPER_PATH%...
cd "%CSHARP_WRAPPER_PATH%"

rem Checking if the main Unity libraries path is set
if "%UNITY_MANAGED_PATH%"=="C:\Program Files\Unity\Hub\Editor\2022.3.17f1\Editor\Data\Managed" (
    echo.
    echo [WARNING] The main Unity libraries path (UNITY_MANAGED_PATH) is not set!
    echo [WARNING] Please edit the script and provide the correct path to continue.
    echo.
    cd ..
    exit /b 1
)

rem Combine all provided paths into one variable for msbuild
set "ALL_REFERENCE_PATHS=%UNITY_MANAGED_PATH%"
if defined UNITY_PLATFORM_PATH (
    if not "%UNITY_PLATFORM_PATH%"=="" set "ALL_REFERENCE_PATHS=%ALL_REFERENCE_PATHS%;%UNITY_PLATFORM_PATH%"
)

rem Use msbuild, providing all reference paths
echo [INFO] Using combined reference paths: %ALL_REFERENCE_PATHS%
msbuild /p:Configuration=Release /p:ReferencePath="%ALL_REFERENCE_PATHS%" /nologo /v:q

if errorlevel 1 (
    echo.
    echo [ERROR] C# wrapper build failed. Check your UNITY_..._PATH variables.
    cd ..
    exit /b 1
)
echo [INFO] C# wrapper built successfully.
echo.

echo [INFO] Copying .dll files to the destination directory...
set DLL_SOURCE_PATH=bin\Release
move /Y "%DLL_SOURCE_PATH%\*.dll" "..\%DIST_CSHARP_PATH%\" >nul
if errorlevel 1 (
    echo.
    echo [ERROR] Failed to move the .dll files.
    cd ..
    exit /b 1
)
echo [INFO] The .dll files have been moved to %DIST_CSHARP_PATH%
cd ..
echo.

rem ==================================================================
rem Summary
rem ==================================================================
echo [SUCCESS] The script finished successfully.
echo.
echo   -> The final .aar file is located in: %DIST_ANDROID_PATH%
echo   -> The final .dll files are located in: %DIST_CSHARP_PATH%
echo.
echo ==================================================================

endlocal
exit /b 0
