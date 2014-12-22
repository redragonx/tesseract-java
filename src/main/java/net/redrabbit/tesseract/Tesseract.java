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

        System.out.println("Hello Tesseract using LWJGL vers: " + Sys.getVersion() + "!");

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
            }
            glPopMatrix();
            //End matrix block
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

    // ************************** Cube ************************************

    /**
     * Draws a standard cube
     */
    public void standardCube() {

        // front face
        glColor3f(1.0f, 1.0f, 0.0f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(-1, -1, -1);
        glTexCoord2f(.5f, 0);
        glVertex3d(1, -1, -1);
        glTexCoord2f(.5f, .5f);
        glVertex3d(1, -1, 1);
        glTexCoord2f(0, .5f);
        glVertex3d(-1, -1, 1);
        glEnd();

        // rear face
        glColor3f(0.0f, 1.0f, 0.0f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(1, 1, -1);
        glTexCoord2f(.5f, 0);
        glVertex3d(-1, 1, -1);
        glTexCoord2f(.5f, .5f);
        glVertex3d(-1, 1, 1);
        glTexCoord2f(0, .5f);
        glVertex3d(1, 1, 1);
        glEnd();

        // right face
        glColor3f(0.5f, 1.0f, 0.5f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(1, -1, -1);
        glTexCoord2f(.5f, 0);
        glVertex3d(1, 1, -1);
        glTexCoord2f(.5f, .5f);
        glVertex3d(1, 1, 1);
        glTexCoord2f(0, .5f);
        glVertex3d(1, -1, 1);
        glEnd();

        // left face
        glColor3f(1.0f, 0.0f, 1.0f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(-1, 1, -1);
        glTexCoord2f(.5f, 0);
        glVertex3d(-1, -1, -1);
        glTexCoord2f(.5f, .5f);
        glVertex3d(-1, -1, 1);
        glTexCoord2f(0, .5f);
        glVertex3d(-1, 1, 1);
        glEnd();

        // top face
        glColor3f(0.0f, 0.0f, 1.0f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(0, 0);
        glVertex3d(-1, -1, 1);
        glTexCoord2f(.75f, 0);
        glVertex3d(1, -1, 1);
        glTexCoord2f(.75f, .5f);
        glVertex3d(1, 1, 1);
        glTexCoord2f(0, .5f);
        glVertex3d(-1, 1, 1);
        glEnd();

        // bottom face
        glColor3f(0.0f, 1.0f, 1.0f);
        glBegin(GL_LINE_LOOP);
        glTexCoord2f(.75f, 0);
        glVertex3d(-1, -1, -1);
        glTexCoord2f(.75f, .5f);
        glVertex3d(-1, 1, -1);
        glTexCoord2f(0, .5f);
        glVertex3d(1, 1, -1);
        glTexCoord2f(0, 0);
        glVertex3d(1, -1, -1);
        glEnd();

    }// End standardCube

}// EOF

