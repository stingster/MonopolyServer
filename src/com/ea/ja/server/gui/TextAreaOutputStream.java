package com.ea.ja.server.gui;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
* An output stream that writes its output to a javax.swing.JTextArea
* control.
*
* @author  Ranganath Kini
* @see      javax.swing.JTextArea
*/
public class TextAreaOutputStream extends OutputStream {
    private JTextArea textControl;

    /**
     * Creates a new instance of TextAreaOutputStream which writes
     * to the specified instance of javax.swing.JTextArea control.
	 *
     */
    public TextAreaOutputStream( JTextArea control ) {
        textControl = control;
    }

    /**
     * Writes the specified byte as a character to the
     * javax.swing.JTextArea.
     *
     */
    public void write( int b ) throws IOException {
        // append the data as characters to the JTextArea control
        textControl.append( String.valueOf( ( char )b ) );
    }  
}