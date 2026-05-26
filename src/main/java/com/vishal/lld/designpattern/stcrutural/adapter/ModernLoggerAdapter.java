package com.vishal.lld.designpattern.stcrutural.adapter;

import com.vishal.lld.designpattern.stcrutural.adapter.interfaces.Logger;

public class ModernLoggerAdapter implements Logger {

    private final ModernLogger modernLogger;
    private final String defaultLevel;

    public ModernLoggerAdapter(ModernLogger modernLogger, String defaultLevel) {
        this.modernLogger = modernLogger;
        this.defaultLevel = defaultLevel;
    }

    @Override
    public void log(String message) {
        modernLogger.writeLog(defaultLevel, message);
    }

}
