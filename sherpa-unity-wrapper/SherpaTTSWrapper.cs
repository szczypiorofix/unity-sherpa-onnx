using System;
using UnityEngine;

namespace SherpaUnityWrapper
{
    /// <summary>
    /// Sherps ONNX Unity TTS (Text-to-Speech) Wrapper 
    /// </summary>
    /// <remarks>
    /// This class is for communicate with Java ONNX library (.AAR by default) for standard TTS (text-to-speech) feature.
    /// <para>
    /// Build and tested for Unity 2022.3.17f1 version
    /// </para>
    /// </remarks>
    public class SherpaTTSWrapper
    {
        public static readonly string DEBUG_NAME = "SHERPA_UNITY_TTS_WRAPPER_DEBUG";

        private readonly string UNITY_PLAYER_CLASS_NAME = "com.unity3d.player.UnityPlayer";
        private readonly string SHERPA_ONNX_PLUGIN_JAVA_CLASS_NAME = "com.k2fsa.sherpa.onnx.SherpaOnnxTTSPlugin";

        /// <summary>
        /// Sherpa ONNX Java object
        /// </summary>
        private AndroidJavaObject sherpaOnnx = null;
        private bool debugMode = false;

        /// <summary>
        /// Basic SherpaTTSWrapper constructor
        /// </summary>
        /// <remarks>
        /// Just for creating an object.
        /// </remarks>
        public SherpaTTSWrapper()
        {
            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: SherpaTTSWrapper constructor called.");
            }
        }

        /// <summary>
        /// Initialize library
        /// </summary>
        public void Initialize()
        {
            AndroidJavaClass unityPlayerClass = null;
            try
            {
                unityPlayerClass = new AndroidJavaClass(UNITY_PLAYER_CLASS_NAME);
            } catch (Exception upcE)
            {
                Debug.LogError($"{DEBUG_NAME}: [Initialize] An error occurred while creating AndroidJavaClass(${UNITY_PLAYER_CLASS_NAME})");
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: error content: {upcE}");
                return;
            }

            AndroidJavaObject unityCurrentActivityObject = null;
            try
            {
                unityCurrentActivityObject = unityPlayerClass.GetStatic<AndroidJavaObject>("currentActivity");
            } catch (Exception ucaoE)
            {
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: An error occurred while retrieving Android current activity object!");
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: error content: {ucaoE}");
                return;
            }

            try
            {
                sherpaOnnx = new AndroidJavaObject(SHERPA_ONNX_PLUGIN_JAVA_CLASS_NAME);
            } catch (Exception swE)
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
                sherpaOnnx.Call("initialize"); // TODO: shoud return a value or throw an error

                if (debugMode)
                {
                    Debug.Log($"{DEBUG_NAME}: [Initialize]: SherpaOnnxPlugin initialized successfully.");
                }
            }
            catch (Exception arg4)
            {
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: An error occurred during plugin setup (setContext or initialize).");
                Debug.LogError($"{DEBUG_NAME}: [Initialize]: error content: {arg4}");
                sherpaOnnx = null;
                return;
            }
        }

        /// <summary>
        /// Generate audio from text and play it
        /// </summary>
        /// <param name="speakerId">SpeakerID - <c>0</c> by default</param>
        /// <param name="speed">Speed of speeking - <c>1.0f</c> by default. The bigger, the faster</param>
        /// <param name="text">Text whick should be converted to audio and the played.</param>
        public void GenerateAndPlay(int speakerId, float speed, string text)
        {
            if (string.IsNullOrEmpty(text) || sherpaOnnx == null)
            {
                return;
            }

            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: [GenerateAndPlay]: Generate audio and play it");
            }
            sherpaOnnx.Call("generateAndPlay", speakerId, speed, text);
        }

        /// <summary>
        /// Stop current audio
        /// </summary>
        public void Stop()
        {
            if (sherpaOnnx == null)
            {
                return;
            }

            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: [Stop]: Stop audio");
            }
            sherpaOnnx.Call("stop");
        }

        /// <summary>
        /// Check if audio is stopped at the moment
        /// </summary>
        /// <returns>Returns <c>true</c> if audio is stopped</returns>
        public bool IsStopped()
        {
            if (sherpaOnnx == null)
            {
                return true;
            }

            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: [IsStopped]: Is audio stopped?");
            }
            bool isStopped = sherpaOnnx.Call<bool>("isStopped");
            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: [IsStopped]: stopped? {isStopped}");
            }
            return isStopped;
        }

        /// <summary>
        /// Check if audio is playing at the moment
        /// </summary>
        /// <returns>Returns <c>true</c> if audio is playing</returns>
        public bool IsPlaying()
        {
            if (sherpaOnnx == null)
            {
                return true;
            }

            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: [IsPlaying]: Is audio playing?");
            }
            bool isStopped = sherpaOnnx.Call<bool>("isStopped");
            if (debugMode)
            {
                Debug.Log($"{DEBUG_NAME}: [IsPlaying]: isPlaying? {!isStopped}");
            }
            return !isStopped;
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
