package toni.lib.animation;


import net.minecraft.util.FastColor;

public class AnimationKeyframe {
    public float rotX;
    public float rotY;
    public float rotZ;
    public float posX;
    public float posY;
    public float posZ;
    public float size = 1f;
    public float alpha = 1f;
    public float color = FastColor.ARGB32.color(255, 255, 255, 255);

    public void setValue(Binding key, float value) {
        switch (key) {
            case xRot -> rotX = value;
            case yRot -> rotY = value;
            case zRot -> rotZ = value;
            case xPos -> posX = value;
            case yPos -> posY = value;
            case zPos -> posZ = value;
            case Size -> size = value;
            case Alpha -> alpha = value;
            case Color -> color = value;
        }
    }

    public float getValue(Binding key) {
        return switch (key) {
            case xRot -> rotX;
            case yRot -> rotY;
            case zRot -> rotZ;
            case xPos -> posX;
            case yPos -> posY;
            case zPos -> posZ;
            case Size -> size;
            case Alpha -> alpha;
            case Color -> color;
        };
    }
}
