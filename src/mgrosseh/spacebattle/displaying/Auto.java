package mgrosseh.spacebattle.displaying;

import mgrosseh.spacebattle.io.Sprite;

public class Auto extends WindowAction {

    private WindowAction action;

    public Auto(Sprite sprite) {
        super(0, 0);
        switch (sprite.type) {
            case NONE:
                action = new WindowAction(sprite.x, sprite.y) {
                    @Override
                    public void display() {
                        parent.rect(x, y, 2, 2, 1, 255, 0);
                        parent.text("?", x + 1, y + 1, 0);
                    }
                };
                break;
            case ANIMATION:
                action = new Animation(sprite);
                break;
            case IMAGE:
                action = new Image(sprite);
                break;
            case SHAPE:
                action = new Shape(sprite);
                break;
            case TEXT:
                action = new Text(sprite);
                break;
            case RECT:
                action = new Rectangle(sprite);
                break;
            case ELLIPSE:
                action = new Ellipse(sprite);
                break;
            case LINE:
                action = new Line(sprite);
                break;
            case POINT:
                action = new Point(sprite);
                break;
            case GROUP:
                action = new Group(sprite);
                break;
        }
    }

    public void display() {
        action.display();
    }

    public void setParent(Window window) {
        super.setParent(window);
        action.setParent(window);
    }

    public void setTx(float tx) {
        super.setTx(tx);
        action.setTx(tx);
    }

    public void setTy(float ty) {
        super.setTy(ty);
        action.setTy(ty);
    }
}
