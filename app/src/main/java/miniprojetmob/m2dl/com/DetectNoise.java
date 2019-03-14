package miniprojetmob.m2dl.com;

import android.media.MediaRecorder;

import java.io.IOException;

public class DetectNoise {
    static final private double EMA_FILTER= 0.6;
    private MediaRecorder mRecorder = null;
    private double mEMA = 0.0;

    public void start(){
        if(mRecorder == null){
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("dev/null");
            try{
                mRecorder.prepare();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            mRecorder.start();
            mEMA = 0.0;
        }
    }

    public void stop(){
        if(mRecorder != null){
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude(){
        if(mRecorder != null){
            return 20*Math.log10(mRecorder.getMaxAmplitude());
        }
        else
            return 0;
    }

}
