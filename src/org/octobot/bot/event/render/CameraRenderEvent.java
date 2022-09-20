package org.octobot.bot.event.render;

import org.octobot.script.ScriptContext;

import java.awt.*;

/**
 * CameraRenderEvent
 *
 * @author Pat-ji
 */
public class CameraRenderEvent extends TextRenderEvent {

    public CameraRenderEvent(final ScriptContext context) {
        super(context);
    }

    @Override
    public void render(final Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("[Camera] - [x=" + context().camera.getX() + ", y=" + context().camera.getY() + ", z=" + context().camera.getZ()
                + "] [xCurve=" + context().camera.getCurveX() + ", yCurve=" + context().camera.getCurveY()
                + "] [angle=" + context().camera.getAngle() + ", pitch=" + context().camera.getPitch() + "]", 10, getY());
    }

}
