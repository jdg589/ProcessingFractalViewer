package Backend;
import java.util.ArrayList;

public class FractalGenerator {

    private double[][] frame;
    private int[] dimensions;
    private double[][] potentialMap;
    int maxIterations;
    int aaConstant;
    private Mode mode;
    private ArrayList<double[][]> previousFrames;
    private double colorConstant;

    public enum Mode {POTENTIAL, BLURRED_POTENTIAL, AA_POTENTIAL}

    public FractalGenerator() {
        previousFrames = new ArrayList<>();
        maxIterations = 200;
        mode = Mode.POTENTIAL;
        aaConstant = 3;
        colorConstant = 1;
        setFrame(new double[][]{{-2, 1}, {1, -1}}, new int[]{900,600});
    }

    public void previousFrame(){
        goToFrame(previousFrames.size()-1);
    }

    public double changeColorConstant(double d){
        return colorConstant += d + colorConstant > 0 ? d : 0;
    }
    public double setColorConstant(double d){
        return colorConstant = d > 0 ? d : 0;
    }

    private void goToFrame(int i){
        if(previousFrames.size() > 0) {
            this.frame = previousFrames.get(i);
            for (int j = previousFrames.size() - 1; j >= i; j--) {
                previousFrames.remove(j);
            }
        }
    }

    public void setFrame(double[][] frame, int[] dimensions) {
        this.frame = frame;
        this.dimensions = dimensions;
    }

    public void setFrame(int y1, int x1, int y2, int x2) {
        previousFrames.add(this.frame);
        if (y2 > y1) {
            int temp = y2;
            y2 = y1;
            y1 = temp;
        }
        if (x2 < x1) {
            int temp = x2;
            x2 = x1;
            x1 = temp;
        }
        double xStep = (frame[1][0] - frame[0][0]) / dimensions[0];
        double yStep = (frame[0][1] - frame[1][1]) / dimensions[1];
        double[][] newFrame = new double[2][2];
        newFrame[0] = new double[]{frame[0][0] + xStep * x1, frame[1][1] + yStep * y1};
        newFrame[1] = new double[]{frame[0][0] + xStep * x2, frame[1][1] + yStep * y2};
        this.frame = newFrame;
    }

    public int changeIterationDepth(int i){
        return maxIterations += maxIterations + i > 0 ? i : 0;
    }

    public String setMode(Mode mode){
        return (this.mode = mode).toString();
    }

    public String iterateMode(){
        return setMode(Mode.values()[(mode.ordinal() + 1) % Mode.values().length]);
    }

    public void generate() {
        switch (mode) {
            case POTENTIAL: {
                double xStep = (frame[1][0] - frame[0][0]) / dimensions[0];
                double yStep = (frame[0][1] - frame[1][1]) / dimensions[1];
                potentialMap = new double[dimensions[1]][dimensions[0]];
                for (int i = 0; i < dimensions[1]; i++) {
                    for (int j = 0; j < dimensions[0]; j++) {
                        potentialMap[i][j] = potentialTest(frame[0][0] + j * xStep, frame[1][1] + i * yStep);
                    }
                }
            }
            break;
            case BLURRED_POTENTIAL: {
                //reuse above code
                double xStep = (frame[1][0] - frame[0][0]) / dimensions[0];
                double yStep = (frame[0][1] - frame[1][1]) / dimensions[1];
                potentialMap = new double[dimensions[1]][dimensions[0]];
                for (int i = 0; i < dimensions[1]; i++) {
                    for (int j = 0; j < dimensions[0]; j++) {
                        potentialMap[i][j] = potentialTest(frame[0][0] + j * xStep, frame[1][1] + i * yStep);
                    }
                }

                //now blur everything
                double[][] POMA = new double[dimensions[1]][dimensions[0]];
                for (int i = 0; i < dimensions[1]; i++) {
                    for (int j = 0; j < dimensions[0]; j++) {
                        double total = 0;
                        int n = 0;
                        for (int k = i - 1; k < i + 2; k++) {
                            for (int l = j - 1; l < j + 2; l++) {
                                if (k == -1 || l == -1 || k == dimensions[1] || l == dimensions[0]) {
                                } else if (l == j && k == i) {
                                    //maybe do something special
                                } else {
                                    total += potentialMap[k][l];
                                    n += 1;
                                }
                            }
                        }
                        POMA[i][j] = potentialMap[i][j] + (total / n - potentialMap[i][j]) * 0.5;
                    }
                }
                potentialMap = POMA;

             }
             break;
            case AA_POTENTIAL: {
                double xStep = (frame[1][0] - frame[0][0]) / dimensions[0];
                double yStep = (frame[0][1] - frame[1][1]) / dimensions[1];
                potentialMap = new double[dimensions[1]][dimensions[0]];
                for (int i = 0; i < dimensions[1]; i++) {
                    for (int j = 0; j < dimensions[0]; j++) {
                        double total = 0;
                        for (int k = 0; k < aaConstant; k++) {
                            total += potentialTest(frame[0][0] + j * xStep + Math.random() * xStep, frame[1][1] + i * yStep + Math.random() * yStep);
                        }
                        potentialMap[i][j] = total/aaConstant;
                    }
                }
            }
        }
    }

    public double get(int i, int j){
        return potentialMap[i][j] * colorConstant;
    }

    public int changeAADepth(int i){
        return aaConstant += i + aaConstant > 0 ? i : 0;
    }

    public double potentialTest(double r, double i) {
        double realValue = 0;
        double imaginaryValue = 0;
        double realValueSquared = 0;
        double imaginaryValueSquared = 0;
        int j;
        for (j = 0; j < maxIterations; j++) {
            imaginaryValue = 2 * realValue * imaginaryValue + i;
            realValue = (realValueSquared - imaginaryValueSquared) + r;
            imaginaryValueSquared = imaginaryValue * imaginaryValue;
            realValueSquared = realValue * realValue;
            //(a+bi)(a+bi)
            //(a^2 - b^2) + 2abi
            if (realValueSquared + imaginaryValueSquared > 1000000)
                return j - Math.log(realValueSquared + imaginaryValueSquared) / Math.pow(2, j);
        }
        return 0;
    }

}
//It could be cool to represent each point as jumping to another point.
