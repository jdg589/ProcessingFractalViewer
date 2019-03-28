import Backend.FractalGenerator;
import processing.core.PApplet;


public class Viewer extends PApplet{
    private int t;
    private FractalGenerator mandlebrot;
    private int[] firstCorner;
    private int[] windowSize = new int[] {900, 600};
    private final int gradientSize = 1024;
    private int[][] gradient;
    private double aspectRatio;
    private boolean psychadellicMode;

    public Viewer(){
        mandlebrot = new FractalGenerator();
        t = 0;
    }

    public void settings(){
        size(windowSize[0],windowSize[1]);
        firstCorner = new int[] {-1,-1};
        aspectRatio = ((double)windowSize[1]/windowSize[0]);
    }

    public void setup(){
        mandlebrot.generate();
        generateGradient();
        rectMode(CORNERS);
    }

    public void draw(){
        if(psychadellicMode){
            mandlebrot.changeColorConstant(0.05);
        }
        drawFractal();
        if (firstCorner[0] != -1 && !psychadellicMode)
            drawSelectionBox();
        t++;
    }

    private void drawSelectionBox(){
        fill(0,0,0,0);
        int[] box = makeSelectionBox();
        rect(box[0], box[1], box[2], box[3]);
    }

    private int[] makeSelectionBox(){
        int dx = Math.abs(firstCorner[1] - mouseX);
        int dy = Math.abs(firstCorner[0] - mouseY);
        if(dx * aspectRatio >dy){
            dy = (int) (dx * aspectRatio);
        } else {
            dx = (int)(dy/aspectRatio);
        }
        int xDirection  = (int) Math.signum(firstCorner[1] - mouseX),  yDirection = (int) Math.signum(firstCorner[0] - mouseY);
        return new int[] {firstCorner[1], firstCorner[0], firstCorner[1] - xDirection * dx, firstCorner[0] - yDirection * dy};
    }

    private void drawFractal(){
        loadPixels();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int m = (int) (mandlebrot.get(i,j) * 3) % gradientSize;
                pixels[i*width+j] = color(gradient[m][0], gradient[m][1], gradient[m][2]);
            }
        }
        updatePixels();
    }

    private void generateGradient(){
        gradient = new int[gradientSize][3];
        double[] xes = new double[] {0, 0.16, 0.42, 0.6425, 0.8575, 1};
        int[][] points = new int[][] {
                {0,     32,     237,    255,    0,  0},   //r
                {7,     107,    255,    170,    2,  7},   //g
                {100,   203,    255,    0,      0,  100}  //b
        };
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < gradientSize; i++) {
                double x = (double) i / gradientSize;
                int k = 0;
                while(x>xes[k+1]){k++;}
                gradient[i][j] = points[j][k] + (int) ((points[j][k+1] - points[j][k])/(xes[k+1]- xes[k]) * (x-xes[k]));
            }
        }
    }

    public void mouseClicked(){
        if(firstCorner[0] == -1){
            firstCorner = new int[] {mouseY, mouseX};
        }else {
            int[] box = makeSelectionBox();
            mandlebrot.setFrame(box[1], box[0], box[3], box[2]);
            mandlebrot.generate();
            firstCorner = new int[] {-1,-1};
        }
    }

    public void keyPressed(){
        if(key=='h'){
            mandlebrot.setFrame(new double[][] {{-2, 1}, {1,-1}}, new int[] {900, 600});
            mandlebrot.generate();
        } else if (key == 'q'){
            exit();
        } else if (key == 'p'){
            if(!psychadellicMode) {
                mandlebrot.setMode(FractalGenerator.Mode.POTENTIAL);
            } else {
               mandlebrot.setColorConstant(2);
            }
            psychadellicMode = !psychadellicMode;
        } else if (key == 'a'){
            System.out.println("Mode is now " + mandlebrot.iterateMode());
            mandlebrot.generate();
        } else if (key == 'j'){
            System.out.println("Changed iteration depth to " + mandlebrot.changeIterationDepth(5));
            mandlebrot.generate();
        } else if (key == 'k'){
            System.out.println("Changed iteration depth to " + mandlebrot.changeIterationDepth(-5));
            mandlebrot.generate();
        } else if (key == 'm'){
            System.out.println("Changed antialias supersample to " + mandlebrot.changeAADepth(1) + " points per pixel");
            mandlebrot.generate();
        } else if (key == 'n'){
            System.out.println("Changed antialias supersample to " + mandlebrot.changeAADepth(-1) + " points per pixel");
            mandlebrot.generate();
        } else if (key == 'b'){
            mandlebrot.previousFrame();
            mandlebrot.generate();
        } else if (key == 'd'){
            System.out.println("Color proportionality constant is " + mandlebrot.changeColorConstant(-0.5) + ".");
        } else if(key == 'f'){
            System.out.println("Color proportionality constant is " + mandlebrot.changeColorConstant(0.5) + ".");
        }
    }

    public static void main(String[] args) {
        String[] appletArgs = {"Viewer"};
        PApplet.main(appletArgs);
    }
}
//https://stackoverflow.com/questions/16500656/which-color-gradient-is-used-to-color-mandelbrot-in-wikipedia
//https://en.wikipedia.org/wiki/Mandelbrot_set

