package tkbal;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;


import javax.microedition.lcdui.*;

public class SnapShotCanvas extends Canvas {

    private Image image;
    public SnapShotCanvas(Image image) {
        this.image = image;
        setFullScreenMode(true);
    }

    public void paint(Graphics g) {
        g.drawImage(image, getWidth() / 2, getHeight() / 2, Graphics.HCENTER | Graphics.VCENTER);
    }
}