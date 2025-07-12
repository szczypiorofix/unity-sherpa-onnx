using System;
using UnityEngine;

namespace SherpaUnityWrapper
{
    public class SherpaSTTWrapper
    {
        public static readonly string DEBUG_NAME = "SHERPA_UNITY_STT_WRAPPER_DEBUG";
        private static readonly string UNITY_PLAYER_CLASS_NAME = "com.unity3d.player.UnityPlayer";
        private static readonly string SHERPA_ONNX_PLUGIN_JAVA_CLASS_NAME = "com.k2fsa.sherpa.onnx.SherpaOnnxSTTPlugin";
       
        /// <summary>
        /// Sherpa ONNX Java object
        /// </summary>
        private AndroidJavaObject sherpaOnnx = null;
        private string lastText = "";
        private bool debugMode = false;

        public SherpaSTTWrapper()
        {
            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: SherpaSTTWrapper constructor called.");
            }
            lastText = "";
        }

        public void Initialize()
        {
            AndroidJavaClass unityPlayerClass = null;
            try
            {
                unityPlayerClass = new AndroidJavaClass(UNITY_PLAYER_CLASS_NAME);
            }
            catch (Exception upcE)
            {
                Debug.LogError($"{DEBUG_NAME}: [Initialize] An error occurred while creating AndroidJavaClass(${UNITY_PLAYER_CLASS_NAME})");
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: error content: {upcE}");
                return;
            }

            AndroidJavaObject unityCurrentActivityObject = null;
            try
            {
                unityCurrentActivityObject = unityPlayerClass.GetStatic<AndroidJavaObject>("currentActivity");
            }
            catch (Exception ucaoE)
            {
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: An error occurred while retrieving Android current activity object!");
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: error content: {ucaoE}");
                return;
            }

            try
            {
                sherpaOnnx = new AndroidJavaObject(SHERPA_ONNX_PLUGIN_JAVA_CLASS_NAME);
            }
            catch (Exception swE)
            {
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: An error occurred while creating SherpaOnnx Java object!");
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: error content: {swE}");
                return;
            }

            if (sherpaOnnx == null)
            {
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: SherpaOnnx object is null after creation attempt.");
                return;
            }

            if (unityCurrentActivityObject == null)
            {
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: Unity current activity object is null!");
                return;
            }

            try
            {
                sherpaOnnx.Call("setActivity", unityCurrentActivityObject);
            }
            catch (Exception setActivityException)
            {
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: An error occurred during plugin setup (setActivity).");
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: error content: {setActivityException}");
                sherpaOnnx = null;
                return;
            }

            try
            {
                sherpaOnnx.Call("initialize"); // TODO: shoud return a value or throw an error
            }
            catch (Exception initializeException)
            {
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: An error occurred during plugin setup (initialize).");
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: error content: {initializeException}");
                sherpaOnnx = null;
                return;
            }

            Debug.Log($"{DEBUG_NAME}: [Initialize]: SherpaOnnxPlugin initialized successfully.");
        }

        /// <summary>
        /// Start recording audio from microphone
        /// </summary>
        public void StartRecording()
        {
            if (sherpaOnnx == null)
            {
                return;
            }

            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: [StartRecording]: Start recording");
            }
            sherpaOnnx.Call("startRecording");
        }

        /// <summary>
        /// Stop recording audio from microphone
        /// </summary>
        public void StopRecording()
        {
            if (sherpaOnnx == null)
            {
                return;
            }

            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: [StopRecording]: Stop recording");
            }
            sherpaOnnx.Call("stopRecording");
        }

        /// <summary>
        /// Returns <c>true</c> if audio is currently being recorded
        /// </summary>
        /// <returns></returns>
        public bool IsRecording()
        {
            if (sherpaOnnx == null)
            {
                return false;
            }

            bool isRecording = sherpaOnnx.Call<bool>("isRecording");
            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: [IsRecording]: isRecording: {isRecording}");
            }            
            return isRecording;
        }

        public void SetRecording(bool recording)
        {
            if (sherpaOnnx == null)
            {
                return;
            }

            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: [SetRecording]: calling setRecording(boolean recording)");
            }
            sherpaOnnx.Call("setRecording", recording);
        }

        public string GetLastText()
        {
            if (sherpaOnnx == null)
            {
                return null;
            }

            lastText = sherpaOnnx.Call<string>("getLastText");
            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: [GetLastText]: recognizedText: {lastText}");
            }
            return lastText;
        }

        public bool IsDebugMode()
        {
            if (sherpaOnnx == null)
            {
                return false;
            }

            bool isDebugMode = sherpaOnnx.Call<bool>("isDebugMode");
            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: [IsDebugMode]: isDebugMode: {isDebugMode}");
            }
            return isDebugMode;
        }

        public void OnRecognitionResult(string recognizedText)
        {
            if (debugMode)
            {
                Debug.Log($"Recognized text from callback: {recognizedText}");
            }
            this.lastText = recognizedText.Trim();
        }

        // Opcjonalna metoda do obsługi błędów
        public void OnRecognitionError(string errorMessage)
        {
            Debug.LogError($"Błąd w module rozpoznawania mowy: {errorMessage}");
        }

        public void SetRecognitionCallback(string gameObjectName, string methodName, string errorMethodName)
        {
            if (sherpaOnnx == null)
            {
                return;
            }
            sherpaOnnx.Call("setUnityCallback", gameObjectName, methodName, errorMethodName);
        }

        /// <summary>
        /// Dispose an object when is not needed.
        /// </summary>
        public void Dispose()
        {
            if (sherpaOnnx != null)
            {
                sherpaOnnx.Call("release");
                if (debugMode)
                {
                    Debug.Log($"{DEBUG_NAME}: [Stop]: Calling method to destroy an object.");
                }
                sherpaOnnx.Dispose();
            }
        }
    }
}
