package com.example.xavib.vortix;

import org.codetome.hexameter.core.api.contract.SatelliteData;
import org.codetome.hexameter.core.api.defaults.DefaultSatelliteData;

/**
 * Created by xaviB on 6/3/17.
 */

public class HexagonSatelliteData extends DefaultSatelliteData {

    private boolean visible = false;

    public final boolean isVisible() {
        return visible;
    }

    public final void setVisible(final boolean visible) {
        this.visible = visible;
    }


}
