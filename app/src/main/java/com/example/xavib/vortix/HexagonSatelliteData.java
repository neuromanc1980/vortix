package com.example.xavib.vortix;

import org.codetome.hexameter.core.api.contract.SatelliteData;
import org.codetome.hexameter.core.api.defaults.DefaultSatelliteData;

/**
 * Created by xaviB on 6/3/17.
 */

public class HexagonSatelliteData extends DefaultSatelliteData {

    private boolean visible = false;
    private boolean moveable = false;

    public final boolean isVisible() {
        return visible;
    }
    public final void setVisible(final boolean visible) {
        this.visible = visible;
    }

    public boolean isMoveable() {        return moveable;    }
    public void setMoveable(boolean moveable) {        this.moveable = moveable;    }
}
