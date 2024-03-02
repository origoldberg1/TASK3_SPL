package bgu.spl.net.impl.rci;

import java.io.Serializable;

public interface Command<T>{

    byte[] execute(T arg);
}
