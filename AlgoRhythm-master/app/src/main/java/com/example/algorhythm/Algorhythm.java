package com.example.algorhythm;

import core.be.tarsos.dsp.AudioDispatcher;
import core.be.tarsos.dsp.AudioDispatcherFactory;
import core.be.tarsos.dsp.AudioEvent;
import core.be.tarsos.dsp.onsets.ComplexOnsetDetector;
import core.be.tarsos.dsp.onsets.OnsetHandler;
import core.be.tarsos.dsp.pitch.PitchDetectionHandler;
import core.be.tarsos.dsp.pitch.PitchDetectionResult;
import core.be.tarsos.dsp.pitch.PitchProcessor;

public final class Algorhythm {

    private Algorhythm() { }

    private static String songName;

    public static void importSong(String fileName) {
        songName = fileName;
    }

    public static String songString() {

        final int BUFFER_SIZE = 1024;

        AudioDispatcher audioDisp = AudioDispatcherFactory.fromPipe(songName, 44100, BUFFER_SIZE, 128);

        ComplexOnsetDetector c = new ComplexOnsetDetector(BUFFER_SIZE);
        Printer handler = new Printer();
        c.setHandler(handler);
        audioDisp.addAudioProcessor(c);
        PitchProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.DYNAMIC_WAVELET, 44100, BUFFER_SIZE, handler);
        audioDisp.addAudioProcessor(p);
        audioDisp.run();

        return handler.songString();
    }

    private static class Printer implements OnsetHandler, PitchDetectionHandler {
        private StringBuilder song = new StringBuilder();
        private static final double PITCH_DIFF_THRESHHOLD = 12.5;

        private int numberOfNotes = 0;
        private float currentPitch;
        private char currentType = 't';

        public void handleOnset(double time, double salience) {
            numberOfNotes++;
            System.out.println("Time: " + time + ", type: " + currentType);
            song.append(time + " " + currentType + "\n");

        }

        public String songString() {
            return numberOfNotes + "\n" + song.toString();
        }

        public void handlePitch(PitchDetectionResult result, AudioEvent event) {
            if (result.isPitched()) {
                float pitch = result.getPitch();
                System.out.println(pitch);
                if (pitch - currentPitch > PITCH_DIFF_THRESHHOLD) {
                    //significantly higher now
                    currentType = 'r';
                } else if (currentPitch - pitch > PITCH_DIFF_THRESHHOLD) {
                    //significantly lower now
                    currentType = 'l';
                } else {
                    //roughly the same
                    currentType = 't';
                }
                currentPitch = pitch;
            }
        }
    }
}
