package network.message;

import java.io.Serializable;

public class ObjectMessage extends Message implements Serializable {
    public final Object[] objects;

    public ObjectMessage(String content, Object...objects) {
        super(content);
        this.objects = objects;
    }
}
