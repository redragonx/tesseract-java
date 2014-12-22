package net.redrabbit.tesseract;

import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;
import java.io.File;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/*
 * Projects a 4D tesseract onto a 2D screen. 
 * @author redragonx && rabbitfighter
 * Some sample code modified from lwjgl.org's example for a small lwjgl3 app.
 * All openGl code is original.
 */
public class Tesseract {

    // Window & Animation stuff
    public static final int window_width = 700;
    public static final int window_height = 500;
    public static final int fps = 30;
    public int angle = 45;

    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;

    // The window handle
    private long window;

    public void run() {

        System.out.println("Hello Tesseract using LWJGL vers: "
                + Sys.getVersion() + "!");

        try {

            init();
            loop();

            // Release window and window callbacks
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            glfwTerminate();
            errorCallback.release();
        }
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (glfwInit() != GL11.GL_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are
                                    // already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden
                                                // after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(window_width, window_height, "Tesseract",
                NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed,
        // repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action,
                    int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                    glfwSetWindowShouldClose(window, GL_TRUE); // We will detect
                                                                // this in our
                                                                // rendering
                                                                // loop
            }
        });

        // Get the resolution of the primary monitor
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(window,
                (GLFWvidmode.width(vidmode) - window_width) / 2,
                (GLFWvidmode.height(vidmode) - window_height) / 2);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GLContext.createFromCurrent();

        // Set the clear color to soft black
        glClearColor(0.04f, 0.04f, 0.04f, 0.04f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (glfwWindowShouldClose(window) == GL_FALSE) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the

            // ********************* Open GL Stuff *************************

            float ratio;
            ratio = window_width / (float) window_height;
            glViewport(0, 0, window_width, window_height);
            glClear(GL_COLOR_BUFFER_BIT);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(-ratio, ratio, -1.f, 1.f, 1.f, -1.f);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            // Matrix block
            glPushMatrix();
            {
                glRotatef((float) glfwGetTime() * 50.f, 25.f, 25.f, 1.f);
                glScaled(.5, .5, .5);
                standardCube();
                insideCube();
                connectVertices();
            }
            glPopMatrix();
            // End matrix block
            glfwSwapBuffers(window);
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            // ******************** End Open GL Stuff *********************

        }
    }

    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath",
                new File("libs/natives").getAbsolutePath());
        new Tesseract().run();
    }

    /*
    * Connects the verices of the tesseract
    */
    public void connectVertices() {

        //Bottom face

        // 1) connect inner FrontLeft-Bottom to outer FrontLeft-Bottom
        glLineWidth(1.0f);
        glColor3f(1.0f, 1.0f, 1.0f);
        glBegin(GL_LINES);
        {
            glVertex3d(-1, -1, -1);
            glVertex3d(-.5, -.5, -.5);
        }
        glEnd();
        // 2) connect inner FrontRight-Botttom to outer FrontRight-Bottom
        glBegin(GL_LINES);
        {
            glVertex3d(1, -1, -1);
            glVertex3d(.5f, -.5f, -.5f);
        }
        glEnd();
        // 3) connect inner BackRight-Bottom to outer BackRight-Bottom
        glBegin(GL_LINES);
        {
            glVertex3d(1, -1, 1);
            glVertex3d(.5f, -.5f, .5f);
        }
        glEnd();
        // 4) connect inner BackL-Bottom to outer BackL-Bottom
        glBegin(GL_LINES);
        {
            glVertex3d(-1, -1, 1);
            glVertex3d(-.5f, -.5f, .5f);
        }
        glEnd();


        // Top face

        // 1) connect inner FrontR-Top to outer FrontR-Top
        glBegin(GL_LINES);
        {
            glVertex3d(1, 1, -1);
            glVertex3d(.5f, .5f, -.5f);
        }
        glEnd();
        // 2) connect inner FrontL-Top to outer FrontL-Top
        glBegin(GL_LINES);
        {
            glVertex3d(-1, 1, -1);
            glVertex3d(-.5f, .5f, -.5f);
        }
        glEnd();
        // 3) connect inner BackL-Top to outer BackL-Top
        glBegin(GL_LINES);
        {
            glVertex3d(-1, 1, 1);
            glVertex3d(-.5f, .5f, .5f);
        }
        glEnd();
        // 4) connect inner BackR-Top to outer BackR-Top
        glBegin(GL_LINES);
        {
            glVertex3d(1, 1, 1);
            glVertex3d(.5f, .5f, .5f);
        }
        glEnd();

        // right face

        // 1) connect inner LowerFront-Right tto outer LowerFront-Right
        glBegin(GL_LINES);
        {
            glVertex3d(1, -1, -1);
            glVertex3d(.5f, -.5f, -.5f);
        }
        glEnd();
        // 2) connect inner UpperFront-Right to outer UpperFront-Right
        glBegin(GL_LINES);
        {
            glVertex3d(1, 1, -1);
            glVertex3d(.5f, .5f, -.5f);
        }
        glEnd();
        // 3) connect inner UpperRear-Right to outer UpperRear-Right
        glBegin(GL_LINES);
        {
            glVertex3d(1, 1, 1);
            glVertex3d(.5f, .5f, .5f);
        }
        glEnd();
        // 4) connect inner LowerRear-Right to outer LowerRear-Right
        glBegin(GL_LINES);
        {
            glVertex3d(1, -1, 1);
            glVertex3d(.5f, -.5f, .5f);
        }
        glEnd();

        // left face

        // 1) connect inner UpperFront-Left tto outter UpperFront-Left
        glBegin(GL_LINES);
        {
            glVertex3d(-1, 1, -1);
            glVertex3d(-.5f, .5f, -.5f);
        }
        glEnd();
        // 2) connect inner LowerFront-Left to outer LowerFront-Left
        glBegin(GL_LINES);
        {
            glVertex3d(-1, -1, -1);
            glVertex3d(-.5f, -.5f, -.5f);
        }
        glEnd();
        // 3) connect inner LowerRear-Left to outer LowerRear-Left
        glBegin(GL_LINES);
        {
            glVertex3d(-1, -1, 1);
            glVertex3d(-.5f, -.5f, .5f);
        }
        glEnd();
        // 4) connect inner UpperRear-Left to outer UpperRear-Left
        glBegin(GL_LINES);
        {
            glVertex3d(-1, 1, 1);
            glVertex3d(-.5f, .5f, .5f);
        }
        glEnd();

        // front face

        // 1) connect inner LowerLeft-Front to outter LowerLeft-Front
        glBegin(GL_LINES);
        {
            glVertex3d(-1, -1, 1);
            glVertex3d(-.5f, -.5f, .5f);
        }
        glEnd();
        // 2) connect inner LowerRight-Front to outer LowerRight-Front
        glBegin(GL_LINES);
        {
            glVertex3d(1, -1, 1);
            glVertex3d(.5f, -.5f, .5f);
        }
        glEnd();
        // 3) connect inner UpperRight-Front to outer UpperRight-Front
        glBegin(GL_LINES);
        {
            glVertex3d(1, 1, 1);
            glVertex3d(.5f, .5f, .5f);
        }
        glEnd();
        // 4) connect inner UpperLeft-Front to outer UpperLeft-Front
        glBegin(GL_LINES);
        {
            glVertex3d(-1, 1, 1);
            glVertex3d(-.5f, .5f, .5f);
        }
        glEnd();

        // back face

        // 1) connect inner LowerLeft-Back to outter LowerLeft-Back
        glBegin(GL_LINES);
        {
            glVertex3d(-1, -1, -1);
            glVertex3d(-.5f, -.5f, -.5f);
        }
        glEnd();
        // 2) connect inner UpperLeft-Back to outer UpperLeft-Back
        glBegin(GL_LINES);
        {
            glVertex3d(-1, 1, -1);
            glVertex3d(-.5f, .5f, -.5f);
        }
        glEnd();
        // 3) connect inner UpperRight-Back ttto outer UpperRight-Back
        glBegin(GL_LINES);
        {
            glVertex3d(1, 1, -1);
            glVertex3d(.5f, .5f, -.5f);
        }
        glEnd();
        // 4) finally, connect inner LowerRight-Back to outer LowerRight-Back
        glBegin(GL_LINES);
        {
            glVertex3d(1, -1, -1);
            glVertex3d(.5f, -.5f, -.5f);
        }
        glEnd();

    }

    // ************************** Cube ************************************

    /**
     * Draws a standard cube
     */
    public void standardCube() {

        // bottom face

        glColor3f(1.0f, 1.0f, 1.0f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(-1, -1, -1);//FrontL-Bottom
        glTexCoord2f(.5f, 0);
        glVertex3d(1, -1, -1);//FrontR-Bottom
        glTexCoord2f(.5f, .5f);
        glVertex3d(1, -1, 1);//BackR-Bottom
        glTexCoord2f(0, .5f);
        glVertex3d(-1, -1, 1);//BackL-Bottom
        glEnd();

        // top face

        //glColor3f(0.0f, 1.0f, 0.0f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(1, 1, -1);//FrontR-Top
        glTexCoord2f(.5f, 0);
        glVertex3d(-1, 1, -1);//FrontL-Top
        glTexCoord2f(.5f, .5f);
        glVertex3d(-1, 1, 1);//BackL-Top
        glTexCoord2f(0, .5f);
        glVertex3d(1, 1, 1);//BackR-Top
        glEnd();

        // right face

        //glColor3f(0.5f, 1.0f, 0.5f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(1, -1, -1);//LowerFront-Right
        glTexCoord2f(.5f, 0);
        glVertex3d(1, 1, -1);//UpperFront-Right
        glTexCoord2f(.5f, .5f);
        glVertex3d(1, 1, 1);//UpperRear-Right
        glTexCoord2f(0, .5f);
        glVertex3d(1, -1, 1);//LowerRear-Right
        glEnd();

        // left face

        //glColor3f(1.0f, 0.0f, 1.0f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(-1, 1, -1);//UpperFront-Left
        glTexCoord2f(.5f, 0);
        glVertex3d(-1, -1, -1);//LowerFront-Left
        glTexCoord2f(.5f, .5f);
        glVertex3d(-1, -1, 1);//LowerRear-Left
        glTexCoord2f(0, .5f);
        glVertex3d(-1, 1, 1);//UpperRear-Left
        glEnd();

        // front face

        //glColor3f(0.0f, 0.0f, 1.0f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(-1, -1, 1);//LowerLeft-Front
        glTexCoord2f(.75f, 0);
        glVertex3d(1, -1, 1);//LowerRight-Front
        glTexCoord2f(.75f, .5f);
        glVertex3d(1, 1, 1);//UpperRight-Front
        glTexCoord2f(0, .5f);
        glVertex3d(-1, 1, 1);//UpperLeft-Front
        glEnd();

        // back face
        //glColor3f(0.0f, 1.0f, 1.0f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(.75f, 0);
        glVertex3d(-1, -1, -1);//LowerLeft-Back
        glTexCoord2f(.75f, .5f);
        glVertex3d(-1, 1, -1);//UpperLeft-Back
        glTexCoord2f(0, .5f);
        glVertex3d(1, 1, -1);//UpperRight-Back
        glTexCoord2f(0, 0);
        glVertex3d(1, -1, -1);//LowerRight-Back
        glEnd();


    }// End standardCube

    public void insideCube() {
        // front face
        //glColor3f(1.0f, 1.0f, 0.0f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(-.5f, -.5f, -.5f);//FrontL-Bottom
        glTexCoord2f(.25f, 0);
        glVertex3d(.5f, -.5f, -.5f);//FrontR-Bottom
        glTexCoord2f(.25f, .25f);
        glVertex3d(.5f, -.5f, .5f);//BackR-Bottom
        glTexCoord2f(0, .25f);
        glVertex3d(-.5f, -.5f, .5f);//BackL-Bottom
        glEnd();

        //

        // rear face
        //glColor3f(0.0f, .5f, 0.0f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(.5f, .5f, -.5f);
        glTexCoord2f(.25f, 0);
        glVertex3d(-.5f, .5f, -.5f);
        glTexCoord2f(.25f, .25f);
        glVertex3d(-.5f, .5f, .5f);
        glTexCoord2f(0, .25f);
        glVertex3d(.5f, .5f, .5f);
        glEnd();

        //

        // right face
        //glColor3f(0.25f, 0.5f, 0.25f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(.5f, -.5f, -.5f);
        glTexCoord2f(.25f, 0);
        glVertex3d(.5f, .5f, -.5f);
        glTexCoord2f(.25f, .25f);
        glVertex3d(.5f, .5f, .5f);
        glTexCoord2f(0, .25f);
        glVertex3d(.5f, -.5f, .5f);
        glEnd();

        //

        // left face
        //glColor3f(.5f, 0.0f, .5f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(-.5f, .5f, -.5f);
        glTexCoord2f(.25f, 0);
        glVertex3d(-.5f, -.5f, -.5f);
        glTexCoord2f(.25f, .25f);
        glVertex3d(-.5f, -.5f, .5f);
        glTexCoord2f(0, .25f);
        glVertex3d(-.5f, .5f, .5f);
        glEnd();

        //

        // top face
        //glColor3f(0.0f, 0.0f, .5f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(-.5f, -.5f, .5f);
        glTexCoord2f(.375f, 0);
        glVertex3d(.5f, -.5f, .5f);
        glTexCoord2f(.375f, .25f);
        glVertex3d(.5f, .5f, .5f);
        glTexCoord2f(0, .25f);
        glVertex3d(-.5f, .5f, .5f);
        glEnd();

        //

        // bottom face
        //glColor3f(0.0f, .5f, .5f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(.375f, 0);
        glVertex3d(-.5f, -.5f, -.5f);
        glTexCoord2f(.375f, .25f);
        glVertex3d(-.5f, .5f, -.5f);
        glTexCoord2f(0, .25f);
        glVertex3d(.5f, .5f, -.5f);
        glTexCoord2f(0, 0);
        glVertex3d(.5f, -.5f, -.5f);
        glEnd();

    }

}// EOF

