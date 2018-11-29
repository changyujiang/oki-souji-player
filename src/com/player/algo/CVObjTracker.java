import javafx.util.Pair;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect2d;
import org.opencv.tracking.TrackerKCF;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CVObjTracker {
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public List<Pair<Integer, Rect2d>> trackObj(String filePath, int frameIndex, Rect2d trackingObj, int trackDis)throws IOException {
        //add initial frame and bounding box
        List<Pair<Integer, Rect2d>> ret = new ArrayList<>();
        ret.add(new Pair<>(frameIndex, trackingObj.clone()));

        //initialize first matrix from fisrt frame
        String imgPath = filePath + String.format("%04d", frameIndex) + ".rgb";
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
        ok = tracker.init(imgMat, trackingObj);
        if (!ok){
            System.out.println("Tracker Initialization Failed");
            return ret;
        }

        //track until the limit or the end
        final int maxTrackIndex = trackDis > 0 ? frameIndex + trackDis : 9000;
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

            ok = tracker.update(imgMat, trackingObj);
            //if object's bounding box found, add pair; skip otherwise
            if (ok){
                ret.add(new Pair<>(frameIndex, trackingObj.clone()));
            }

        }
//        TrackerTest.showImg(TrackerTest.getImg(imgMat, trackingObj));
        return ret;
    }
}
