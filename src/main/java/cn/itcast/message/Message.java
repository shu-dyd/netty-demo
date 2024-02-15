package cn.itcast.message;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class Message implements Serializable {

    private int sequenceId;

    public abstract int getMessageType();

    public static final int LoginRequestMessage = 0;
}
