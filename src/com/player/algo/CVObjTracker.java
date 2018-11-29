package com.player.algo;

import com.player.entity.Frame;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect2d;
import org.opencv.tracking.TrackerKCF;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CVObjTracker {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static List<Frame.Link> trackObj(String filePath, int frameIndex, Rect2d area, int distance)
            throws IOException {
        //add initial frame and bounding box
        List<Frame.Link> ret = new ArrayList<>();
        ret.add(new Frame.Link(frameIndex, area.clone()));

        //initialize first matrix from first frame
        String imgPath = filePath + File.separator + String.format("%04d", frameIndex) + ".rgb";
        File imgFile = new File(imgPath);
        InputStream in = new FileInputStream(imgFile);
        byte[] imgBytes = new byte[in.available()];
        in.read(imgBytes);
        in.close();
        final int height = 288;
        final int width = 352;
        //convert .rgb file to CV Matrix
        Mat imgMat = new Mat(288, 352, CvType.CV_8UC3);
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                byte[] bgr = {imgBytes[y*width+x+2*width*height], imgBytes[y*width+x+width*height], imgBytes[y*width+x]};
                imgMat.put(y, x, bgr);
            }
        }
//        TrackerTest.showImg(TrackerTest.getImg(imgMat, trackingObj));
        //initialize KCF tracker
        boolean ok;
        TrackerKCF tracker = TrackerKCF.create();
        ok = tracker.init(imgMat, area);
        if (!ok){
            System.out.println("Tracker Initialization Failed");
            return ret;
        }

        //track until the limit or the end
        final int maxTrackIndex = distance > 0 ? frameIndex + distance : 9000;
        frameIndex++;
        for (; frameIndex <= maxTrackIndex && frameIndex <= 9000; frameIndex++){
            imgPath = filePath + String.format("%04d", frameIndex) + ".rgb";
            imgFile = new File(imgPath);
            in = new FileInputStream(imgFile);
            imgBytes = new byte[in.available()];
            in.read(imgBytes);
            in.close();
            for(int y = 0; y < height; y++){
                for(int x = 0; x < width; x++){
                    byte[] bgr = {imgBytes[y*width+x+2*width*height], imgBytes[y*width+x+width*height], imgBytes[y*width+x]};
                    imgMat.put(y, x, bgr);
                }
            }

            ok = tracker.update(imgMat, area);
            //if object's bounding box found, add pair; skip otherwise
            if (ok){
                ret.add(new Frame.Link(frameIndex, area.clone()));
            }
        }
//        TrackerTest.showImg(TrackerTest.getImg(imgMat, trackingObj));
        return ret;
    }
}
