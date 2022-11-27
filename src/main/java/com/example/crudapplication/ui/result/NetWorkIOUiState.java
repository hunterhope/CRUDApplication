package com.example.crudapplication.ui.result;

import java.time.LocalDateTime;

public class NetWorkIOUiState {
    private final int msgId;
    private final LocalDateTime key;
    public NetWorkIOUiState(int msgId, LocalDateTime key) {
        this.msgId = msgId;
        this.key = key;
    }

    public int getMsgId() {
        return msgId;
    }

    public LocalDateTime getKey() {
        return key;
    }
}
