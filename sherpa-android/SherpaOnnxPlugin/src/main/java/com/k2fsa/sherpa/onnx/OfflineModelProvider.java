package com.k2fsa.sherpa.onnx;

public final class OfflineModelProvider {
    private OfflineModelProvider() {}

    public static OfflineModelConfig getOfflineModelConfig(int type) {
        OfflineModelConfig modelConfig;
        String modelDir;

        switch (type) {
            case 0:
                modelDir = "sherpa-onnx-paraformer-zh-2023-09-14";
                modelConfig = new OfflineModelConfig();
                modelConfig.paraformer.model = modelDir + "/model.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "paraformer";
                return modelConfig;

            case 1:
                modelDir = "icefall-asr-multidataset-pruned_transducer_stateless7-2023-05-04";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder-epoch-30-avg-4.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder-epoch-30-avg-4.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner-epoch-30-avg-4.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "transducer";
                return modelConfig;

            case 2:
                modelDir = "sherpa-onnx-whisper-tiny.en";
                modelConfig = new OfflineModelConfig();
                modelConfig.whisper.encoder = modelDir + "/tiny.en-encoder.int8.onnx";
                modelConfig.whisper.decoder = modelDir + "/tiny.en-decoder.int8.onnx";
                modelConfig.tokens = modelDir + "/tiny.en-tokens.txt";
                modelConfig.modelType = "whisper";
                return modelConfig;

            case 3:
                modelDir = "sherpa-onnx-whisper-base.en";
                modelConfig = new OfflineModelConfig();
                modelConfig.whisper.encoder = modelDir + "/base.en-encoder.int8.onnx";
                modelConfig.whisper.decoder = modelDir + "/base.en-decoder.int8.onnx";
                modelConfig.tokens = modelDir + "/base.en-tokens.txt";
                modelConfig.modelType = "whisper";
                return modelConfig;

            case 4:
                modelDir = "icefall-asr-zipformer-wenetspeech-20230615";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder-epoch-12-avg-4.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder-epoch-12-avg-4.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner-epoch-12-avg-4.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "transducer";
                return modelConfig;

            case 5:
                modelDir = "sherpa-onnx-zipformer-multi-zh-hans-2023-9-2";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder-epoch-20-avg-1.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder-epoch-20-avg-1.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner-epoch-20-avg-1.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "transducer";
                return modelConfig;

            case 6:
                modelDir = "sherpa-onnx-nemo-ctc-en-citrinet-512";
                modelConfig = new OfflineModelConfig();
                modelConfig.nemo.model = modelDir + "/model.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                return modelConfig;

            case 7:
                modelDir = "sherpa-onnx-nemo-fast-conformer-ctc-be-de-en-es-fr-hr-it-pl-ru-uk-20k";
                modelConfig = new OfflineModelConfig();
                modelConfig.nemo.model = modelDir + "/model.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                return modelConfig;

            case 8:
                modelDir = "sherpa-onnx-nemo-fast-conformer-ctc-en-24500";
                modelConfig = new OfflineModelConfig();
                modelConfig.nemo.model = modelDir + "/model.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                return modelConfig;

            case 9:
                modelDir = "sherpa-onnx-nemo-fast-conformer-ctc-en-de-es-fr-14288";
                modelConfig = new OfflineModelConfig();
                modelConfig.nemo.model = modelDir + "/model.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                return modelConfig;

            case 10:
                modelDir = "sherpa-onnx-nemo-fast-conformer-ctc-es-1424";
                modelConfig = new OfflineModelConfig();
                modelConfig.nemo.model = modelDir + "/model.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                return modelConfig;

            case 11:
                modelDir = "sherpa-onnx-telespeech-ctc-int8-zh-2024-06-04";
                modelConfig = new OfflineModelConfig();
                modelConfig.teleSpeech = modelDir + "/model.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "telespeech_ctc";
                return modelConfig;

            case 12:
                modelDir = "sherpa-onnx-zipformer-thai-2024-06-20";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder-epoch-12-avg-5.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder-epoch-12-avg-5.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner-epoch-12-avg-5.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "transducer";
                return modelConfig;

            case 13:
                modelDir = "sherpa-onnx-zipformer-korean-2024-06-24";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder-epoch-99-avg-1.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder-epoch-99-avg-1.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner-epoch-99-avg-1.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "transducer";
                return modelConfig;

            case 14:
                modelDir = "sherpa-onnx-paraformer-zh-small-2024-03-09";
                modelConfig = new OfflineModelConfig();
                modelConfig.paraformer.model = modelDir + "/model.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "paraformer";
                return modelConfig;

            case 15:
                modelDir = "sherpa-onnx-sense-voice-zh-en-ja-ko-yue-2024-07-17";
                modelConfig = new OfflineModelConfig();
                modelConfig.senseVoice.model = modelDir + "/model.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                return modelConfig;

            case 16:
                modelDir = "sherpa-onnx-zipformer-ja-reazonspeech-2024-08-01";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder-epoch-99-avg-1.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder-epoch-99-avg-1.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner-epoch-99-avg-1.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "transducer";
                return modelConfig;

            case 17:
                modelDir = "sherpa-onnx-zipformer-ru-2024-09-18";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "transducer";
                return modelConfig;

            case 18:
                modelDir = "sherpa-onnx-small-zipformer-ru-2024-09-18";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "transducer";
                return modelConfig;

            case 19:
                modelDir = "sherpa-onnx-nemo-ctc-giga-am-russian-2024-10-24";
                modelConfig = new OfflineModelConfig();
                modelConfig.nemo.model = modelDir + "/model.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                return modelConfig;

            case 20:
                modelDir = "sherpa-onnx-nemo-transducer-giga-am-russian-2024-10-24";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "nemo_transducer";
                return modelConfig;

            case 21:
                modelDir = "sherpa-onnx-moonshine-tiny-en-int8";
                modelConfig = new OfflineModelConfig();
                modelConfig.moonshine.preprocessor = modelDir + "/preprocess.onnx";
                modelConfig.moonshine.encoder = modelDir + "/encode.int8.onnx";
                modelConfig.moonshine.uncachedDecoder = modelDir + "/uncached_decode.int8.onnx";
                modelConfig.moonshine.cachedDecoder = modelDir + "/cached_decode.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                return modelConfig;

            case 22:
                modelDir = "sherpa-onnx-moonshine-base-en-int8";
                modelConfig = new OfflineModelConfig();
                modelConfig.moonshine.preprocessor = modelDir + "/preprocess.onnx";
                modelConfig.moonshine.encoder = modelDir + "/encode.int8.onnx";
                modelConfig.moonshine.uncachedDecoder = modelDir + "/uncached_decode.int8.onnx";
                modelConfig.moonshine.cachedDecoder = modelDir + "/cached_decode.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                return modelConfig;

            case 23:
                modelDir = "sherpa-onnx-zipformer-zh-en-2023-11-22";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder-epoch-34-avg-19.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder-epoch-34-avg-19.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner-epoch-34-avg-19.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "transducer";
                return modelConfig;

            case 24:
                modelDir = "sherpa-onnx-fire-red-asr-large-zh_en-2025-02-16";
                modelConfig = new OfflineModelConfig();
                modelConfig.fireRedAsr.encoder = modelDir + "/encoder.int8.onnx";
                modelConfig.fireRedAsr.decoder = modelDir + "/decoder.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                return modelConfig;

            case 25:
                modelDir = "sherpa-onnx-dolphin-base-ctc-multi-lang-int8-2025-04-02";
                modelConfig = new OfflineModelConfig();
                modelConfig.dolphin.model = modelDir + "/model.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                return modelConfig;

            case 26:
                modelDir = "sherpa-onnx-zipformer-vi-int8-2025-04-20";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder-epoch-12-avg-8.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder-epoch-12-avg-8.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner-epoch-12-avg-8.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "transducer";
                return modelConfig;

            case 27:
                modelDir = "sherpa-onnx-nemo-ctc-giga-am-v2-russian-2025-04-19";
                modelConfig = new OfflineModelConfig();
                modelConfig.nemo.model = modelDir + "/model.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                return modelConfig;

            case 28:
                modelDir = "sherpa-onnx-nemo-transducer-giga-am-v2-russian-2025-04-19";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "nemo_transducer";
                return modelConfig;

            case 29:
                modelDir = "sherpa-onnx-zipformer-ru-int8-2025-04-20";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "transducer";
                return modelConfig;

            case 30:
                modelDir = "sherpa-onnx-nemo-parakeet-tdt-0.6b-v2-int8";
                modelConfig = new OfflineModelConfig();
                modelConfig.transducer.encoder = modelDir + "/encoder.int8.onnx";
                modelConfig.transducer.decoder = modelDir + "/decoder.int8.onnx";
                modelConfig.transducer.joiner = modelDir + "/joiner.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                modelConfig.modelType = "nemo_transducer";
                return modelConfig;

            case 31:
                modelDir = "sherpa-onnx-zipformer-ctc-zh-int8-2025-07-03";
                modelConfig = new OfflineModelConfig();
                modelConfig.zipformerCtc.model = modelDir + "/model.int8.onnx";
                modelConfig.tokens = modelDir + "/tokens.txt";
                return modelConfig;

            default:
                return null;
        }
    }
}
