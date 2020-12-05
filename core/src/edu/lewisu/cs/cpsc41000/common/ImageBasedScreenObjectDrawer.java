package edu.lewisu.cs.cpsc41000.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ImageBasedScreenObjectDrawer {
    private SpriteBatch batch;
    public ImageBasedScreenObjectDrawer(SpriteBatch batch) {
        this.batch = batch;
    }
    public void draw(ImageBasedScreenObject obj, int status) {
        if (status == 0) {
            batch.draw(obj.getImg(), obj.getXPos(), obj.getYPos(), 
        obj.getXOrigin(), obj.getYOrigin(), 
        obj.getWidth(), obj.getHeight(), obj.getScaleX(), obj.getScaleY(), obj.getRotation(),
        obj.idleGetDrawStartX(),obj.idleGetDrawStartY(),(int)(obj.getWidth()),(int)(obj.getHeight()), obj.getFlipX(), obj.getFlipY());
        } else if (status == 1) {
            batch.draw(obj.getImg(), obj.getXPos(), obj.getYPos(), 
        obj.getXOrigin(), obj.getYOrigin(), 
        obj.getWidth(), obj.getHeight(), obj.getScaleX(), obj.getScaleY(), obj.getRotation(),
        obj.runGetDrawStartX(),obj.runGetDrawStartY(),(int)(obj.getWidth()),(int)(obj.getHeight()), obj.getFlipX(), obj.getFlipY());
        } else if (status == 2) {
            batch.draw(obj.getImg(), obj.getXPos(), obj.getYPos(), 
        obj.getXOrigin(), obj.getYOrigin(), 
        obj.getWidth(), obj.getHeight(), obj.getScaleX(), obj.getScaleY(), obj.getRotation(),
        obj.jumpGetDrawStartX(),obj.jumpGetDrawStartY(),(int)(obj.getWidth()),(int)(obj.getHeight()), obj.getFlipX(), obj.getFlipY());

        } else {
            batch.draw(obj.getImg(), obj.getXPos(), obj.getYPos(), 
        obj.getXOrigin(), obj.getYOrigin(), 
        obj.getWidth(), obj.getHeight(), obj.getScaleX(), obj.getScaleY(), obj.getRotation(),
        obj.getDrawStartX(),obj.getDrawStartY(),(int)(obj.getWidth()),(int)(obj.getHeight()), obj.getFlipX(), obj.getFlipY());
        }
        /*batch.draw(obj.getImg(), obj.getXPos(), obj.getYPos(), 
        obj.getXOrigin(), obj.getYOrigin(), 
        obj.getWidth(), obj.getHeight(), obj.getScaleX(), obj.getScaleY(), obj.getRotation(),
        obj.getDrawStartX(),obj.getDrawStartY(),(int)(obj.getWidth()),(int)(obj.getHeight()), obj.getFlipX(), obj.getFlipY());*/
    }
}

