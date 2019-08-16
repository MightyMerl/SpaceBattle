package mgrosseh.spacebattle.displaying;

import mgrosseh.spacebattle.io.Sprite;

public class Group extends WindowAction {

    private WindowAction[] children;
    private int index, childrenNum;

    public Group(Sprite sprite) {
        super(sprite.x, sprite.y);
        childrenNum = sprite.childrenNum;
        children = new WindowAction[childrenNum];
        index = 0;
        for (Sprite child : sprite.children) {
            addChild(new Auto(child));
        }
    }

    @Override
    public void display() {
        for (int i = 0; i < children.length; i++) {
            children[i].display();
        }
    }

    public void addChild(WindowAction child) {
        if (index >= childrenNum) return;
        children[index] = child;
        index++;
        child.x += x;
        child.y += y;
    }

    public void setParent(Window window) {
        super.setParent(window);
        for (WindowAction action : children) {
            action.setParent(window);
        }
    }

    @Override
    public void setTx(float tx) {
        super.setTx(tx);
        for (int i = 0; i < childrenNum; i++) {
            children[i].setTx(tx);
        }
    }

    @Override
    public void setTy(float ty) {
        super.setTy(ty);
        for (int i = 0; i < childrenNum; i++) {
            children[i].setTy(ty);
        }
    }
}
