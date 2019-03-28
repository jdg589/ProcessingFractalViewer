# ProcessingFractalViewer: A Fractal-Viewing Application for Processing


##Installation
There are two methods to use PFV:
1. Download the JAR. This is the easiest way.
2. Compile from source. This is harder, but you can then change the code any way you wish.

Additionally, Processing only works with Java 8, so you might want to install a tool like [jenv](http://www.jenv.be/) to manage any other Java versions you might have.


###From JAR
Just download ProcessingFractalViewer.jar and run with Java 8.


###From source
ProcessingFractalViewer (PFV) needs the [Processing](https://github.com/processing) library in its classpath to compile, which means you're probably going to need to install Processing. However, PFV \*should\* only need `core.jar`, to run which is included here.


##Usage
The best way to figure out how these controls work is to skim this section, and then try them out and see what happens.
If something doesn't work, check back here, and then consult the source.
If you try that and something still isn't working, try fixing it yourself, or raise an issue through Github.


###Mouse Controls
When you open the app, you should be greeted by the standard picture of the Mandlebrot set (you can read more about it [here](https://en.wikipedia.org/wiki/Mandelbrot_set))
Click somewhere, and then move the mouse.
Ideally, you should see a rectangle appear and track your mouse (if it doesn't, don't worry, just try again).
If you click again, the window will jump to fill the rectangle you just defined.
That's all you really need to know to start exploring.


###Key Controls
Below is a list of key controls. When a parameter can be adjusted in two directions, you use a key pair, like `d/f`.

- `h`  The Home key. Takes you back to the default screen.
- `q`  Quit. Exits the app.
- `b`  Back. Goes back a frame.
- `a`  Iterates through three render modes.
    - Potential- the escape value each pixel is generated once, and then smoothed. The fastest (and sometimes prettiest) render mode.
    - Blurred Potential- same as Potential, but each pixel is put though a weighted average with its neighbors. This is a (somewhat unsatasfactory) compromise between speed and antialiasing.
    - Anti Aliased- In this mode, multiple points "inside" each pixel are randomly chosen and averaged to generate the value for the pixel. This is the slowest method, but also the only method with true antialiasing. You can control the number of points with the `m/n` key pair.
- `j/k`  Increase and decrease the maximum number of iterations a point can go through before being declared part of the set. Increases the accuracy of the image, but also  the time to generate it.
- `m/n`  Increase and decrease the number of points averaged in Anti Aliased render mode. 
- `f/d`  Increase and decrease the scale of the color gradient. Can make images more colorful, but also more chaotic.
- `p` Toggles the as-of-yet incomplete 'psychedelic mode'. 


##License
This software is released under the [GPLv3 license](http://www.gnu.org/copyleft/gpl.html). @2019 Jonathan Gould
