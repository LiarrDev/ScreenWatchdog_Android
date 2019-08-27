package com.liarr.screenwatchdog.Database;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class PauseTime extends LitePalSupport {

    private Date pauseDate;

    public Date getPauseDate() {
        return pauseDate;
    }

    public void setPauseDate(Date pauseDate) {
        this.pauseDate = pauseDate;
    }
}
