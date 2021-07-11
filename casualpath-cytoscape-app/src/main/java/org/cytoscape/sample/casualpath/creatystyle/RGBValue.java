package org.cytoscape.sample.casualpath.creatystyle;

public class RGBValue {
    int R,G,B;

    public RGBValue(int parseInt, int parseInt1, int parseInt2) {
        this.R=parseInt;
        this.G=parseInt1;
        this.B=parseInt2;
    }




    public int getB() {
        return B;
    }

    public int getG() {
        return G;
    }
    public int getR() {
        return R;
    }

    public void setB(int b) {
        B = b;
    }
    public void setG(int g) {
        G = g;
    }
    public void setR(int r){
        R = r;
    }


}
