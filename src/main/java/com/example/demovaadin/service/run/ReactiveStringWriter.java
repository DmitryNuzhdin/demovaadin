package com.example.demovaadin.service.run;



import java.io.IOException;
import java.io.Writer;
import java.util.function.Consumer;

public class ReactiveStringWriter extends Writer {
    private Consumer<String> action;
    private StringBuffer buf;
    
    public ReactiveStringWriter(Consumer<String> onNewString) {
        buf = new StringBuffer();
        lock=buf;
        action=onNewString;
    }
    
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        if ((off < 0) || (off > cbuf.length) || (len < 0) ||
            ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }

        for(int i=off;i<(off+len);i++){
            if (cbuf[i]=='\n' || cbuf[i]=='\r'){
                buf.append(cbuf, off,i-off);
                if (cbuf[i]=='\n'){
                    flush();
                }
                len=len-(i-off)-1;
                off=i+1;
            }
        }
        if ((off < 0) || (off > cbuf.length) || (len < 0) ||
            ((off + len) > cbuf.length) || ((off + len) < 0)) {
            return;
        } else if (len == 0) {
            return;
        }
        buf.append(cbuf, off, len);
    }
    
    @Override
    public void flush() throws IOException {
        spawn(buf.toString());
        buf.delete(0,buf.length());
    }
    
    @Override
    public void close() throws IOException {
    
    }
    
    private void spawn(String s){
        action.accept(s);
    }
}
