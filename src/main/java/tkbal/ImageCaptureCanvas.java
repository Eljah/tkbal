package tkbal;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;

public class ImageCaptureCanvas extends Canvas {

    Capture midlet;
    VideoControl videoControl;
    int width = getWidth();
    int height = getHeight();
    Player player;
    SnapShotCanvas snap;
    private Display display;

    public ImageCaptureCanvas(Capture midlet, VideoControl videoControl) {
        this.midlet = midlet;
        this.videoControl = videoControl;
        this.display = Display.getDisplay(midlet);
        videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO, this);
        try {
            videoControl.setDisplayLocation(2, 2);
            videoControl.setDisplaySize(width - 4, height - 4);
        } catch (MediaException me) {
            try {
                videoControl.setDisplayFullScreen(true);
            } catch (MediaException me2) {
            }
        }
        videoControl.setVisible(true);
    }

    public void paint(Graphics g) {
    }

    protected void keyPressed(int keyCode) {
        switch (getGameAction(keyCode)) {
            case 1:
                Thread t = new Thread() {
                    public void run() {
                        try {
                            byte[] raw = videoControl.getSnapshot(null);
                            Image image = Image.createImage(raw, 0, raw.length);
                            snap = new SnapShotCanvas(image);
                            display.setCurrent(snap);
                        } catch (Exception e) {
                        }
                    }
                };
                t.start();
        }
    }
}


