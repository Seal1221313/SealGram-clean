package org.telegram.messenger;

import java.nio.ByteBuffer;

public final class SealVoice {

    public static final int OFF = 0, DEEP = 1, HIGH = 2, CHIPMUNK = 3, MONSTER = 4, ROBOT = 5, ALIEN = 6;
    public static final String[] KEYS = {"VoiceOff", "VoiceDeep", "VoiceHigh", "VoiceChipmunk", "VoiceMonster", "VoiceRobot", "VoiceAlien"};

    private SealVoice() {
    }

    public static int preset() {
        int p = SealGram.intPref("voice_preset", OFF);
        return p < 0 || p >= KEYS.length ? OFF : p;
    }

    public static void setPreset(int p) {
        SealGram.setIntPref("voice_preset", p);
    }

    public static boolean forCalls() {
        return preset() != OFF && SealGram.pref("voice_calls", true);
    }

    public static boolean forRound() {
        return preset() != OFF && SealGram.pref("voice_round", true);
    }

    public static boolean forMessages() {
        return preset() != OFF && SealGram.pref("voice_messages", true);
    }

    public static Processor create(int sampleRate) {
        return new Processor(sampleRate);
    }

    public static final class Processor {
        private final int rate;
        private final int win;
        private final float[] ring;
        private int w;
        private float d1;
        private int appliedPreset = -1;
        private float ratio = 1f;
        private float ringHz;
        private double phase;

        Processor(int sampleRate) {
            rate = sampleRate;
            win = Math.max(64, sampleRate * 30 / 1000);
            ring = new float[win * 2 + 4];
        }

        private void configure(int p) {
            if (p == appliedPreset) return;
            appliedPreset = p;
            ringHz = 0f;
            switch (p) {
                case DEEP: ratio = 0.78f; break;
                case HIGH: ratio = 1.30f; break;
                case CHIPMUNK: ratio = 1.65f; break;
                case MONSTER: ratio = 0.60f; break;
                case ROBOT: ratio = 1f; ringHz = 70f; break;
                case ALIEN: ratio = 1.25f; ringHz = 30f; break;
                default: ratio = 1f; break;
            }
        }

        public void process(ByteBuffer buf, int bytes) {
            int p = preset();
            buf.order(java.nio.ByteOrder.nativeOrder());
            configure(p);
            if (p == OFF) return;
            int n = bytes / 2;
            int size = ring.length;
            float step = 1f - ratio;
            double inc = ringHz * 2.0 * Math.PI / rate;
            for (int i = 0; i < n; i++) {
                ring[w] = buf.getShort(i * 2);
                float out;
                if (ratio == 1f) {
                    out = ring[w];
                } else {
                    float d2 = d1 + win * 0.5f;
                    if (d2 >= win) d2 -= win;
                    out = tap(d1) * gain(d1) + tap(d2) * gain(d2);
                    d1 += step;
                    if (d1 >= win) d1 -= win;
                    if (d1 < 0) d1 += win;
                }
                if (ringHz > 0f) {
                    out *= (float) Math.sin(phase);
                    phase += inc;
                    if (phase > 6.283185307179586) phase -= 6.283185307179586;
                    out *= 1.6f;
                }
                if (out > 32767f) out = 32767f;
                if (out < -32768f) out = -32768f;
                buf.putShort(i * 2, (short) out);
                if (++w >= size) w = 0;
            }
        }

        private float tap(float delay) {
            int size = ring.length;
            float pos = w - delay;
            if (pos < 0) pos += size;
            int i0 = (int) pos;
            float f = pos - i0;
            int i1 = i0 + 1;
            if (i1 >= size) i1 = 0;
            return ring[i0] * (1f - f) + ring[i1] * f;
        }

        private float gain(float delay) {
            return 0.5f * (1f - (float) Math.cos(2.0 * Math.PI * delay / win));
        }
    }
}
