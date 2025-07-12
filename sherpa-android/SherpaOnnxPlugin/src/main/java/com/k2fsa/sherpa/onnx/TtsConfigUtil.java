package com.k2fsa.sherpa.onnx;

// please refer to
// https://k2-fsa.github.io/sherpa/onnx/tts/pretrained_models/index.html
// to download models
public final class TtsConfigUtil {
    private TtsConfigUtil() {}

    public static OfflineTtsConfig getOfflineTtsConfig(
            String modelDir, String modelName, String acousticModelName,
            String vocoder, String voices, String lexicon, String dataDir,
            String dictDir, String ruleFsts, String ruleFars) {
        return getOfflineTtsConfig(modelDir, modelName, acousticModelName, vocoder, voices, lexicon, dataDir, dictDir, ruleFsts, ruleFars, null);
    }

    public static OfflineTtsConfig getOfflineTtsConfig(
            String modelDir, String modelName, String acousticModelName,
            String vocoder, String voices, String lexicon, String dataDir,
            String dictDir, String ruleFsts, String ruleFars, Integer numThreads) {

        // For Matcha TTS, please set acousticModelName, vocoder
        // For Kokoro TTS, please set modelName, voices
        // For VITS, please set modelName

        int numberOfThreads;
        if (numThreads != null) {
            numberOfThreads = numThreads;
        } else if (voices != null && !voices.isEmpty()) {
            // for Kokoro TTS models, we use more threads
            numberOfThreads = 4;
        } else {
            numberOfThreads = 2;
        }

        if (modelName.isEmpty() && acousticModelName.isEmpty()) {
            throw new IllegalArgumentException(SherpaOnnxTTSPlugin.TAG + " Please specify a TTS model");
        }

        if (!modelName.isEmpty() && !acousticModelName.isEmpty()) {
            throw new IllegalArgumentException(SherpaOnnxTTSPlugin.TAG + " Please specify either a VITS or a Matcha model, but not both");
        }

        if (!acousticModelName.isEmpty() && vocoder.isEmpty()) {
            throw new IllegalArgumentException(SherpaOnnxTTSPlugin.TAG + " Please provide vocoder for Matcha TTS");
        }

        OfflineTtsVitsModelConfig vits;
        if (!modelName.isEmpty() && voices.isEmpty()) {
            vits = new OfflineTtsVitsModelConfig();
            vits.model = modelDir + "/" + modelName;
            vits.lexicon = modelDir + "/" + lexicon;
            vits.tokens = modelDir + "/tokens.txt";
            vits.dataDir = dataDir;
            vits.dictDir = dictDir;
        } else {
            vits = new OfflineTtsVitsModelConfig();
        }

        OfflineTtsMatchaModelConfig matcha;
        if (!acousticModelName.isEmpty()) {
            matcha = new OfflineTtsMatchaModelConfig();
            matcha.acousticModel = modelDir + "/" + acousticModelName;
            matcha.vocoder = vocoder;
            matcha.lexicon = modelDir + "/" + lexicon;
            matcha.tokens = modelDir + "/tokens.txt";
            matcha.dictDir = dictDir;
            matcha.dataDir = dataDir;
        } else {
            matcha = new OfflineTtsMatchaModelConfig();
        }

        OfflineTtsKokoroModelConfig kokoro;
        if (!voices.isEmpty()) {
            kokoro = new OfflineTtsKokoroModelConfig();
            kokoro.model = modelDir + "/" + modelName;
            kokoro.voices = modelDir + "/" + voices;
            kokoro.tokens = modelDir + "/tokens.txt";
            kokoro.dataDir = dataDir;

            if ("".equals(lexicon)) {
                kokoro.lexicon = lexicon;
            } else if (lexicon.contains(",")) {
                kokoro.lexicon = lexicon;
            } else {
                kokoro.lexicon = modelDir + "/" + lexicon;
            }

            kokoro.dictDir = dictDir;
        } else {
            kokoro = new OfflineTtsKokoroModelConfig();
        }

        OfflineTtsModelConfig modelConfig = new OfflineTtsModelConfig();
        modelConfig.vits = vits;
        modelConfig.matcha = matcha;
        modelConfig.kokoro = kokoro;
        modelConfig.numThreads = numberOfThreads;
        modelConfig.debug = true;
        modelConfig.provider = "cpu";

        OfflineTtsConfig ttsConfig = new OfflineTtsConfig();
        ttsConfig.model = modelConfig;
        ttsConfig.ruleFsts = ruleFsts;
        ttsConfig.ruleFars = ruleFars;

        return ttsConfig;
    }
}
