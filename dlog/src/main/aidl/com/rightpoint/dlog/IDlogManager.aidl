package com.rightpoint.dlog;

import com.rightpoint.dlog.IOnNeedFlushListener;

interface IDlogManager {
    void registerListener(IOnNeedFlushListener listener);
    void unRegisterListener(IOnNeedFlushListener listener);
    List<String> prepareLogFiles();
}
