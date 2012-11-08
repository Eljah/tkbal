package tkbal;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.Control.*;
import javax.microedition.media.control.VideoControl;
import javax.microedition.lcdui.*;
import javax.microedition.media.control.VideoControl;
import javax.microedition.media.*;


public class Capture extends MIDlet {

    private Display display;
    ImageCaptureCanvas canvas;
    Player player;
    VideoControl videoControl;

    public void startApp() {
        display = Display.getDisplay(this);
        try {
            player = Manager.createPlayer("capture://video");
            player.realize();
            videoControl = (VideoControl) player.getControl("VideoControl");
        } catch (Exception e) {
        }
        canvas = new ImageCaptureCanvas(this, videoControl);
        display.setCurrent(canvas);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        notifyDestroyed();
    }
}





