package com.example.xavib.vortix;

import android.graphics.Color;
import android.graphics.Paint;

import org.codetome.hexameter.core.api.Hexagon;
import org.codetome.hexameter.core.api.HexagonOrientation;
import org.codetome.hexameter.core.api.HexagonalGrid;
import org.codetome.hexameter.core.api.HexagonalGridBuilder;
import org.codetome.hexameter.core.api.HexagonalGridLayout;
import org.codetome.hexameter.core.api.Point;

import rx.Observable;
import rx.functions.Action1;

import static org.codetome.hexameter.core.api.HexagonOrientation.FLAT_TOP;
import static org.codetome.hexameter.core.api.HexagonalGridLayout.RECTANGULAR;


public class Grid {

    private Paint linea = new Paint();

    private static final int GRID_HEIGHT = 9;
    private static final int GRID_WIDTH = 9;
    private static final HexagonalGridLayout GRID_LAYOUT = RECTANGULAR;
    private static final HexagonOrientation ORIENTATION = FLAT_TOP;
    private static final double RADIUS = 30;

    public void test(){


        // ...
        HexagonalGridBuilder builder = new HexagonalGridBuilder()
                .setGridHeight(GRID_HEIGHT)
                .setGridWidth(GRID_WIDTH)
                .setGridLayout(GRID_LAYOUT)
                .setOrientation(ORIENTATION)
                .setRadius(RADIUS);

        HexagonalGrid grid = builder.build();

        linea.setColor(Color.GREEN);
        Observable<Hexagon> hexagons = grid.getHexagons();

        hexagons.forEach(new Action1<Hexagon>() {
            @Override
            public void call(Hexagon hexagon) {
                for (Object pointObj : hexagon.getPoints()) {
                    // do what you want

                    Point point = (Point) pointObj;

                    System.out.println(((Point) pointObj).getCoordinateX());
                    System.out.println(((Point) pointObj).getCoordinateY());
                }
            }
        });

    }


}
