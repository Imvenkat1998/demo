/*
 * CompressionResponseStream.java
 * $Header: /home/cvs/jakarta-tomcat-4.0/webapps/examples/WEB-INF/classes/compressionFilters/CompressionResponseStream.java,v 1.4 2001/09/06 23:05:08 remm Exp $
 * $Revision: 1.4 $
 * $Date: 2001/09/06 23:05:08 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * [Additional notices, if required by prior licensing conditions]
 *
 */

package compressionFilters;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;


/**
 * Implementation of <b>ServletOutputStream</b> that works with
 * the CompressionServletResponseWrapper implementation.
 *
 * @author Amy Roh
 * @version $Revision: 1.4 $, $Date: 2001/09/06 23:05:08 $
 */

public class CompressionResponseStream
    extends ServletOutputStream {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a servlet output stream associated with the specified Response.
     *
     * @param response The associated response
     */
    public CompressionResponseStream(HttpServletResponse response) throws IOException{

        super();
        closed = false;
        count = 0;
        this.response = response;
        this.output = response.getOutputStream();

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * The threshold number which decides to compress or not.
     * Users can configure in web.xml to set it to fit their needs.
     */
    protected int compressionThreshold = 0;

    /**
     * The buffer through which all of our output bytes are passed.
     */
    protected byte[] buffer = null;
  
    /**
     * Is it big enough to compress?
     */
    protected boolean compressionThresholdReached = false; 

    /**
     * The number of data bytes currently in the buffer.
     */
    protected int bufferCount = 0;

    /**
     * The underlying gzip output stream to which we should write data.
     */
    protected GZIPOutputStream gzipstream = null;
        
    /**
     * Has this stream been closed?
     */
    protected boolean closed = false;

    /**
     * The number of bytes which have already been written to this stream.
     */
    protected int count = 0;


    /**
     * The content length past which we will not write, or -1 if there is
     * no defined content length.
     */
    protected int length = -1;

    /**
     * The response with which this servlet output stream is associated.
     */
    protected HttpServletResponse response = null;

    /**
     * The underlying servket output stream to which we should write data.
     */
    protected ServletOutputStream output = null;


    // --------------------------------------------------------- Public Methods


    /**
     * Set the compressionThreshold number and create buffer for this size
     */
    protected void setBuffer(int threshold) {
        compressionThreshold = threshold;
        buffer = new byte[compressionThreshold];
        //System.out.println("buffer is set to "+compressionThreshold);
    }

    /**
     * Close this output stream, causing any buffered data to be flushed and
     * any further output data to throw an IOException.
     */
    public void close() throws IOException {

        //System.out.println("close() @ CompressionResponseStream");
        if (closed)
            throw new IOException("This output stream has already been closed");

        if (gzipstream != null) {
            flushToGZip();
            gzipstream.close();
        } else {
            if (bufferCount > 0) {
                output.write(buffer, 0, bufferCount);
                bufferCount = 0;
            }
        }

        output.close();
        closed = true;

    }


    /**
     * Flush any buffered data for this output stream, which also causes the
     * response to be committed.
     */
    public void flush() throws IOException {

        //System.out.println("flush() @ CompressionResponseStream");
        if (closed) {
            throw new IOException("Cannot flush a closed output stream");
        }

        if (gzipstream!=null) {
            gzipstream.flush();
        }

    }

    public void flushToGZip() throws IOException {

        //System.out.println("flushToGZip() @ CompressionResponseStream");

        if (bufferCount > 0) {
            //System.out.println("flushing out to GZipStream");
            gzipstream.write(buffer, 0, bufferCount);
            bufferCount = 0;
        }

    }

    /**
     * Write the specified byte to our output stream.
     *
     * @param b The byte to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(int b) throws IOException {

        //System.out.print("write "+b+" in CompressionResponseStream ");
        if (closed)
            throw new IOException("Cannot write to a closed output stream");

        if ((bufferCount >= buffer.length) || (count>=compressionThreshold)) {
            compressionThresholdReached = true;
        }

        if (compressionThresholdReached) {
            writeToGZip(b);
        } else {
            buffer[bufferCount++] = (byte) b;
            count++;
        }

    }


    /**
     * Write the specified byte to our compressed GZip output stream.
     *
     * @param b The byte to be written
     *
     * @exception IOException if an input/output error occurs
     */

    public void writeToGZip(int b) throws IOException {

        //System.out.println("writeToGZip (int b) compressing");
        if (gzipstream == null) {
            gzipstream = new GZIPOutputStream(output);
            response.addHeader("Content-Encoding", "gzip");
        }
        gzipstream.write(b);

    }

    /**
     * Write <code>b.length</code> bytes from the specified byte array
     * to our output stream.
     *
     * @param b The byte array to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(byte b[]) throws IOException {

        write(b, 0, b.length);

    }


    /**
     * Write <code>len</code> bytes from the specified byte array, starting
     * at the specified offset, to our output stream.
     *
     * @param b The byte array containing the bytes to be written
     * @param off Zero-relative starting offset of the bytes to be written
     * @param len The number of bytes to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(byte b[], int off, int len) throws IOException {

        //System.out.println("second write in CompressionResponseStream");
        if (closed)
            throw new IOException("Cannot write to a closed output stream");

        if (len == 0)
            return;
        if (len <= (buffer.length - bufferCount)) {
            System.arraycopy(b, off, buffer, bufferCount, len);
            bufferCount += len;
            count += len;
            return;
        }

        // buffer full, start writing to gzipstream

        writeToGZip(b, off, len);
        count += len;

    }

    public void writeToGZip(byte b[], int off, int len) throws IOException {

        //System.out.println("writeToGZip 2 compressing");
        if (gzipstream == null) {
            gzipstream = new GZIPOutputStream(output);
            response.addHeader("Content-Encoding", "gzip");
        }
        gzipstream.write(b, off, len);

    }


    // -------------------------------------------------------- Package Methods


    /**
     * Has this response stream been closed?
     */
    public boolean closed() {

        return (this.closed);

    }


    /**
     * Reset the count of bytes written to this stream to zero.
     */
    public void reset() {

        count = 0;

    }

}
