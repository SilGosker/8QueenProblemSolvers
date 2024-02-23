public class Square {
    private final int x;
    private final int y;
    private boolean hasQueen;
    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public boolean tryPlaceQueen() {
        if (hasQueen) return false;
        return hasQueen = true;
    }

    public void removeQueen(){
        hasQueen = false;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasQueen() {
        return hasQueen;
    }
}
