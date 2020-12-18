package com.av.gltrans.mediacodec;

import android.media.MediaCodecInfo;
import android.media.MediaFormat;

public class TrackFormats {

    public static MediaFormat createVideoFormat(int width, int height, int bitrate) {
        MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
        format.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);
        return format;
    }

    public static MediaFormat createAudioFormat(int
            sampleRate, int bitrate, int channelCount) {
        MediaFormat format =
                MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, sampleRate, channelCount);
        format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 1024 * 9);
        format.setString(MediaFormat.KEY_MIME, MediaFormat.MIMETYPE_AUDIO_AAC);
        format.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);
        format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectHE);
        return format;
    }
}
