package com.example.opencvexample.soundDealwith;

public class CosWave {
    /** 余弦波的高度**/
    public static final int HEIGHT = 127;
    /** 2PI**/
    public static final double TWOPI = 2 * 3.1415;
    /**
     * 生成余弦波
     * @param wave
     * @param waveLen 每段余弦波的长度
     * @param length 总长度
     * @return
     */
    public static byte[] cos(byte[] wave, int waveLen, int length) {
        for (int i = 0; i < length; i++) {
            wave[i] = (byte) (HEIGHT * (1 - Math.cos(TWOPI
                    * ((i % waveLen) * 1.00 / waveLen))));
        }
        return wave;
    }
}
